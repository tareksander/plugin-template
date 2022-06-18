package com.termux.plugintemplate;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import com.termux.plugin_aidl.IPluginCallback;
import com.termux.plugin_aidl.Task;
import com.termux.plugin_shared.PluginServiceWrapper;
import com.termux.plugin_shared.PluginUtils;
import com.termux.plugin_shared.TermuxPluginConstants;
import com.termux.plugin_shared.UnixSocketUtils;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;


@RunWith(AndroidJUnit4.class)
public class PluginTestsWithPluginAndRunCommandPermission
{
    @Rule public GrantPermissionRule pluginPermission = GrantPermissionRule.grant(TermuxPluginConstants.PERMISSION_TERMUX_PLUGIN);
    @Rule public GrantPermissionRule runCommandPermission = GrantPermissionRule.grant(TermuxPluginConstants.PERMISSION_RUN_COMMAND);
    
    @Test
    public void bindServiceTest() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assert PluginUtils.bindPluginService(appContext) != null; // binding the service should be possible with the Plugin permission
    }
    
    @Test public void runTaskTest() throws RemoteException, IOException, InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        
        
        final int[] exitpid = new int[1];
        final int[] exitcode = new int[1];
        
        PluginServiceWrapper w = PluginUtils.bindPluginService(appContext);
        assert w != null; // binding the service should be possible with the Plugin permission
        w.setCallbackBinder(new IPluginCallback.Stub()
        {
            @Override
            public int getCallbackVersion() {
                Log.d("IPluginCallback","getCallbackVersion");
                return IPluginCallback.CURRENT_CALLBACK_VERSION;
            }
    
            @Override
            public void socketConnection(String sockname, ParcelFileDescriptor connection) {}
            @Override
            public void taskFinished(int pid, int code) {
                exitpid[0] = pid;
                exitcode[0] = code;
            }
        });
        
        ParcelFileDescriptor[] pipes = ParcelFileDescriptor.createPipe();
        Task t = w.runTask(TermuxPluginConstants.TERMUX_FILES_DIR_PATH+"/usr/bin/cat", new String[]{TermuxPluginConstants.TERMUX_FILES_DIR_PATH+"/usr/bin/cat"}, pipes[0], "/", null);
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            BufferedWriter wr = new BufferedWriter(new FileWriter(pipes[1].getFileDescriptor()));
            try {
                wr.write("test\n");
                wr.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        
        BufferedReader r = new BufferedReader(new FileReader(t.stdout.getFileDescriptor()));
        assert "test".equals(r.readLine());
        
        // kill the Task
        assert w.signalTask(t.pid, 9);
        
        while (exitpid[0] == 0) {
            Thread.sleep(1);
        }
        assert exitpid[0] == t.pid;
        assert exitcode[0] == -9;
    }
    
    
    @Test
    public void openFileOutsidePluginDirTest() throws RemoteException{
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    
        PluginServiceWrapper w = PluginUtils.bindPluginService(appContext);
        assert w != null; // binding the service should be possible with the Plugin permission
        w.setCallbackBinder(new IPluginCallback.Stub()
        {
            @Override
            public int getCallbackVersion() {
                Log.d("IPluginCallback", "getCallbackVersion");
                return IPluginCallback.CURRENT_CALLBACK_VERSION;
            }
    
            @Override
            public void socketConnection(String sockname, ParcelFileDescriptor connection) {}
    
            @Override
            public void taskFinished(int pid, int code) {}
        });
        try {
            w.openFile("../test.txt", "w");
            assert false; // the method should throw an Exception, because the relative path leads outside of the plugin dir
        } catch (IllegalArgumentException ignored) {}
    }
    
    @Test
    public void openFileTest() throws RemoteException, IOException, InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    
        PluginServiceWrapper w = PluginUtils.bindPluginService(appContext);
        assert w != null; // binding the service should be possible with the Plugin permission
        w.setCallbackBinder(new IPluginCallback.Stub()
        {
            @Override
            public int getCallbackVersion() {
                Log.d("IPluginCallback","getCallbackVersion");
                return IPluginCallback.CURRENT_CALLBACK_VERSION;
            }
    
            @Override
            public void socketConnection(String sockname, ParcelFileDescriptor connection) {}
            @Override
            public void taskFinished(int pid, int code) {}
        });
        final String writeString = "Hello Plugin!\n";
        
        ParcelFileDescriptor p = w.openFile("test.txt", "w");
        assert p != null; // the method should return a ParcelFileDescriptor open for writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(p.getFileDescriptor()))) {
            writer.write(writeString);
        }
        p.close();
    
        {
            Intent catIntent = new Intent(TermuxPluginConstants.RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND);
            catIntent.setComponent(ComponentName.createRelative(TermuxPluginConstants.TERMUX_PACKAGE_NAME, TermuxPluginConstants.RUN_COMMAND_SERVICE_NAME));
            catIntent.putExtra(TermuxPluginConstants.RUN_COMMAND_SERVICE.EXTRA_COMMAND_PATH, TermuxPluginConstants.TERMUX_FILES_DIR_PATH+"/usr/bin/cat");
            catIntent.putExtra(TermuxPluginConstants.RUN_COMMAND_SERVICE.EXTRA_ARGUMENTS, new String[] {TermuxPluginConstants.TERMUX_PLUGINS_DIR_PATH+"/"+appContext.getPackageName()+"/test.txt"});
            //noinspection deprecation
            catIntent.putExtra(TermuxPluginConstants.RUN_COMMAND_SERVICE.EXTRA_BACKGROUND, true);
            catIntent.putExtra(TermuxPluginConstants.RUN_COMMAND_SERVICE.EXTRA_PENDING_INTENT, PendingIntent.getBroadcast(appContext, 0, new Intent(appContext.getPackageName()+".fileintent"), PendingIntent.FLAG_CANCEL_CURRENT));
            
            Object sync = new Object();
            final boolean[] finished = {false};
            final String[] content = {null};
            
            BroadcastReceiver rec = new BroadcastReceiver()
            {
                @Override
                public void onReceive(Context context, Intent intent) {
                    synchronized (sync) {
                        content[0] = intent.getBundleExtra(TermuxPluginConstants.EXTRA_PLUGIN_RESULT_BUNDLE).getString(TermuxPluginConstants.EXTRA_PLUGIN_RESULT_BUNDLE_STDOUT);
                        finished[0] = true;
                        sync.notifyAll();
                    }
                }
            };
            appContext.registerReceiver(rec, new IntentFilter(appContext.getPackageName()+".fileintent"));
            appContext.startService(catIntent);
            while (! finished[0]) {
                synchronized (sync) {
                    sync.wait();
                }
            }
            appContext.unregisterReceiver(rec);
    
            assert writeString.equals(content[0]); // file isn't there / content wrong if it doesn't match
        }
    }
    
    @Test
    public void openSocketOutsidePluginDirTest() throws RemoteException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        
        PluginServiceWrapper w = PluginUtils.bindPluginService(appContext);
        assert w != null; // binding the service should be possible with the Plugin permission
        w.setCallbackBinder(new IPluginCallback.Stub()
        {
            @Override
            public int getCallbackVersion() {
                Log.d("IPluginCallback","getCallbackVersion");
                return IPluginCallback.CURRENT_CALLBACK_VERSION;
            }
            
            @Override
            public void socketConnection(String sockname, ParcelFileDescriptor connection) {}
            @Override
            public void taskFinished(int pid, int code) {}
        });
        
        try {
            w.listenOnSocketFile("../test.sock");
            assert false; // the method should throw an Exception, because the relative path leads outside of the plugin dir
        } catch (IllegalArgumentException ignored) {}
        
    }
    
    @Test
    public void openSocketTest() throws RemoteException, InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        
        Object sync = new Object();
        final boolean[] finished = {false};
        final String[] res = {null};
        final String testString = "socket test";
        
        PluginServiceWrapper w = PluginUtils.bindPluginService(appContext);
        assert w != null; // binding the service should be possible with the Plugin permission
        w.setCallbackBinder(new IPluginCallback.Stub()
        {
            @Override
            public int getCallbackVersion() {
                Log.d("IPluginCallback","getCallbackVersion");
                return IPluginCallback.CURRENT_CALLBACK_VERSION;
            }
    
            @Override
            public void socketConnection(String sockname, ParcelFileDescriptor connection) {
                BufferedReader r = new BufferedReader(new FileReader(connection.getFileDescriptor()));
                try {
                    res[0] = r.readLine();
                    r.close();
                    connection.close();
                }
                catch (IOException ignored) {}
                synchronized (sync) {
                    finished[0] = true;
                    sync.notifyAll();
                }
            }
            @Override
            public void taskFinished(int pid, int code) {}
        });
        
        w.listenOnSocketFile("test.sock");
        
        {
            Intent ncIntent = new Intent(TermuxPluginConstants.RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND);
            ncIntent.setComponent(ComponentName.createRelative(TermuxPluginConstants.TERMUX_PACKAGE_NAME, TermuxPluginConstants.RUN_COMMAND_SERVICE_NAME));
            ncIntent.putExtra(TermuxPluginConstants.RUN_COMMAND_SERVICE.EXTRA_COMMAND_PATH, TermuxPluginConstants.TERMUX_FILES_DIR_PATH+"/usr/bin/bash");
            ncIntent.putExtra(TermuxPluginConstants.RUN_COMMAND_SERVICE.EXTRA_ARGUMENTS, new String[] {"-c", "echo \""+testString+"\" | timeout 0.1 nc -U "+TermuxPluginConstants.TERMUX_PLUGINS_DIR_PATH+"/"+appContext.getPackageName()+"/test.sock"});
            //noinspection deprecation
            ncIntent.putExtra(TermuxPluginConstants.RUN_COMMAND_SERVICE.EXTRA_BACKGROUND, true);
            
            appContext.startService(ncIntent);
        }
        
        while (! finished[0]) {
            synchronized (sync) {
                sync.wait();
            }
        }
        assert testString.equals(res[0]); // check if the text send with nc came through
    }
    
    
    @Test
    public void socketFDRecSendTest() throws RemoteException, InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        
        Object sync = new Object();
        final boolean[] finished = {false};
        final String[] res = {null};
        
        final String testString = "socket test";
        
        PluginServiceWrapper w = PluginUtils.bindPluginService(appContext);
        assert w != null; // binding the service should be possible with the Plugin permission
        w.setCallbackBinder(new IPluginCallback.Stub()
        {
            @Override
            public int getCallbackVersion() {
                Log.d("IPluginCallback","getCallbackVersion");
                return IPluginCallback.CURRENT_CALLBACK_VERSION;
            }
            
            @Override
            public void socketConnection(String sockname, ParcelFileDescriptor connection) {
                try {
                    ParcelFileDescriptor[] pair = ParcelFileDescriptor.createSocketPair();
                    UnixSocketUtils.sendFD(connection, pair[1]);
                    pair[1].close();
                    BufferedWriter w = new BufferedWriter(new FileWriter(pair[0].getFileDescriptor()));
                    w.write(testString);
                    w.flush();
                    w.close();
                    pair[0].close();
                    ParcelFileDescriptor rec = UnixSocketUtils.recvFD(connection);
                    BufferedReader r = new BufferedReader(new FileReader(Objects.requireNonNull(rec).getFileDescriptor()));
                    res[0] = r.readLine();
                    r.close();
                    rec.close();
                    connection.close();
                }
                catch (Exception ignored) {}
                synchronized (sync) {
                    finished[0] = true;
                    sync.notifyAll();
                }
            }
            @Override
            public void taskFinished(int pid, int code) {}
        });
        
        w.listenOnSocketFile("test.sock");
        
        
        {
            Intent ncIntent = new Intent(TermuxPluginConstants.RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND);
            ncIntent.setComponent(ComponentName.createRelative(TermuxPluginConstants.TERMUX_PACKAGE_NAME, TermuxPluginConstants.RUN_COMMAND_SERVICE_NAME));
            ncIntent.putExtra(TermuxPluginConstants.RUN_COMMAND_SERVICE.EXTRA_COMMAND_PATH, TermuxPluginConstants.TERMUX_FILES_DIR_PATH+"/usr/bin/python");
            ncIntent.putExtra(TermuxPluginConstants.RUN_COMMAND_SERVICE.EXTRA_ARGUMENTS, new String[] {"-c",
                    "from socket import *\n" +
                    "from array import *\n" +
                    "main = socket(AF_UNIX)\n" +
                    "main.connect(\""+TermuxPluginConstants.TERMUX_PLUGINS_DIR_PATH+"/"+appContext.getPackageName()+"/test.sock"+"\")\n" +
                    "msg, fds, _, _ = recv_fds(main, 1, 1)\n" +
                    "sub1 = socket(fileno=fds[0])\n" +
                    "data = sub1.recv(1000)\n" +
                    "sub1.close()\n" +
                    "socks = socketpair()\n" +
                    "send_fds(main, [bytes(\"\\0\", \"utf-8\")], [socks[1].fileno()])\n" +
                    "socks[1].close()\n" +
                    "main.close()\n" +
                    "socks[0].sendall(data)\n" +
                    "socks[0].close()\n"});
            //noinspection deprecation
            ncIntent.putExtra(TermuxPluginConstants.RUN_COMMAND_SERVICE.EXTRA_BACKGROUND, true);
            
            appContext.startService(ncIntent);
        }
        
        while (! finished[0]) {
            synchronized (sync) {
                sync.wait();
            }
        }
        assert testString.equals(res[0]); // check if the text send with nc came through
    }
    
    
    @After
    public void revokePermission() {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("pm revoke "+InstrumentationRegistry.getInstrumentation().getTargetContext().getPackageName()+" "+TermuxPluginConstants.PERMISSION_TERMUX_PLUGIN);
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("pm revoke "+InstrumentationRegistry.getInstrumentation().getTargetContext().getPackageName()+" "+TermuxPluginConstants.PERMISSION_RUN_COMMAND);
    }
}