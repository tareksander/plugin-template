package com.termux.plugin_shared;


import android.os.ParcelFileDescriptor;

import androidx.annotation.NonNull;

import java.io.IOException;

/**
 * This class provides methods to use the Unix sockets received in a plugin.
 * The sockets can be used like ordinary files for reading and writing, or you can use the methods here to send and receive
 * file descriptors.
 */
public final class UnixSocketUtils
{
    private UnixSocketUtils() {}
    
    private static volatile Boolean libLoaded = null;
    
    public static boolean checkLib() {
        if (libLoaded == null) {
            synchronized (UnixSocketUtils.class) {
                // check again after the lock is obtained, so we don't try to load the library twice
                if (libLoaded == null) {
                    try {
                        System.loadLibrary("plugin-shared");
                        libLoaded = true;
                    }
                    catch (UnsatisfiedLinkError e) {
                        libLoaded = false;
                    }
                }
            }
        }
        return libLoaded;
    }
    
    private static final ThreadLocal<int[]> recvFDBuffer = new ThreadLocal<int[]>() {
        @NonNull
        @Override
        protected int[] initialValue() {
            return new int[1];
        }
    };
    
    private static native void recv(int fd, int[] readFD) throws IOException;
    private static native void send(int fd, int sendFD) throws IOException;
    
    
    /**
     * Reads and discards one byte from the socket and returns the attached file descriptor, if any.
     * 
     * @param socket The Unix socket file descriptor.
     * @return The received file descriptor, or null when there was no file descriptor.
     */
    public static ParcelFileDescriptor recvFD(ParcelFileDescriptor socket) throws IOException {
        if (! checkLib()) {
            throw new IOException("Could not load plugin-shared library");
        }
        int[] fdbuf = recvFDBuffer.get();
        if (fdbuf == null) fdbuf = new int[1]; // so android studio doesn't complain about fdbuf being possibly null
        recv(socket.getFd(), fdbuf);
        if (fdbuf[0] == -1) {
            return null;
        } else {
            return ParcelFileDescriptor.adoptFd(fdbuf[0]);
        }
    }
    
    /**
     * Sends a null byte and attaches a file descriptor to it.
     * 
     * @param socket The Unix socket file descriptor.
     * @param fd The file descriptor to send. Only a duplicate is send, the file descriptor is not closed.
     */
    public static void sendFD(ParcelFileDescriptor socket, ParcelFileDescriptor fd) throws IOException {
        if (! checkLib()) {
            throw new IOException("Could not load plugin-shared library");
        }
        send(socket.getFd(), fd.getFd());
    }
    
    
    
}
