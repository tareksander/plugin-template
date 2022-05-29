#include <sys/socket.h>
#include <sys/types.h>
#include <sys/un.h>

#include <cerrno>
#include <cstring>
#include <cstdint>

#include <jni.h>
#include <string>


using std::string;
using std::to_string;

void throwIOException(JNIEnv *env, const string& msg) {
    jclass ioexception = env->FindClass("java/io/IOException");
    if (ioexception == nullptr) {
        return;
    }
    env->ThrowNew(ioexception, msg.c_str());
}




extern "C"
JNIEXPORT void JNICALL
Java_com_termux_plugin_1shared_UnixSocketUtils_recv(JNIEnv *env, jclass clazz, jint fd, jintArray readFDArray) {
    if (fd < 0) {
        throwIOException(env, "recv(): Invalid fd \"" + to_string(fd) + "\" passed");
        return;
    }

    jint* readFD = env->GetIntArrayElements(readFDArray, nullptr);
    if (env->ExceptionCheck()) return;
    if (readFD == nullptr) {
        throwIOException(env, "recv(): readFD passed is null");
        return;
    }

    if (env->GetArrayLength(readFDArray) < 1) {
        env->ReleaseIntArrayElements(readFDArray, readFD, 0);
        throwIOException(env, "recv(): readFD length is < 1");
        return;
    }
    if (env->ExceptionCheck()) {
        env->ReleaseIntArrayElements(readFDArray, readFD, 0);
        return;
    }

    readFD[0] = -1; // set to an invalid value to signify if an fd was received or not

    // enough size for exactly one control message with one fd, so the excess fds are automatically closed
    constexpr int CONTROLLEN = CMSG_SPACE(sizeof(int));
    union {
        cmsghdr _; // for alignment
        char controlBuffer[CONTROLLEN];
    } controlBufferUnion;
    memset(&controlBufferUnion, 0, CONTROLLEN); // clear the buffer to be sure

    uint8_t data;
    iovec buffer{&data, 1};
    msghdr receiveHeader{nullptr, 0, &buffer, 1, controlBufferUnion.controlBuffer, sizeof(controlBufferUnion.controlBuffer), 0};

    int ret = recvmsg(fd, &receiveHeader, 0);
    if (ret == -1) {
        int errnoBackup = errno;
        env->ReleaseIntArrayElements(readFDArray, readFD, 0);
        throwIOException(env, "recv(): Failed to read on fd " + to_string(fd) + " with error: "+string(strerror(errnoBackup)));
        return;
    }

    for (struct cmsghdr* cmsg = CMSG_FIRSTHDR(&receiveHeader); cmsg != nullptr; cmsg = CMSG_NXTHDR(&receiveHeader, cmsg)) {
        if (cmsg->cmsg_level == SOL_SOCKET && cmsg->cmsg_type == SCM_RIGHTS) {
            int recfd;
            memcpy(&recfd, CMSG_DATA(cmsg), sizeof(recfd));
            readFD[0] = recfd;
            break;
        }
    }
    
    env->ReleaseIntArrayElements(readFDArray, readFD, 0);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_termux_plugin_1shared_UnixSocketUtils_send(JNIEnv *env, jclass clazz, jint fd, jint send_fd) {
    if (fd < 0) {
        throwIOException(env, "send(): Invalid fd \"" + to_string(fd) + "\" passed");
        return;
    }

    if (send_fd < 0) {
        throwIOException(env, "send(): Invalid sendFD \"" + to_string(fd) + "\" passed");
        return;
    }

    const int sendFDInt = send_fd; // in case a platform has an int that isn't the same size as jint

    // enough size for exactly one control message with one fd
    constexpr int CONTROLLEN = CMSG_SPACE(sizeof(int));
    union {
        cmsghdr _; // for alignment
        char controlBuffer[CONTROLLEN];
    } controlBufferUnion;
    memset(&controlBufferUnion, 0, CONTROLLEN); // clear the buffer to be sure

    uint8_t data = 0;
    iovec buffer{&data, 1};
    msghdr sendHeader{nullptr, 0, &buffer, 1, controlBufferUnion.controlBuffer, sizeof(controlBufferUnion.controlBuffer), 0};

    struct cmsghdr* cmsg = CMSG_FIRSTHDR(&sendHeader);
    if (cmsg == nullptr) {
        throwIOException(env, "send(): CMSG_FIRSTHDR returned NULL");
        return;
    }
    cmsg->cmsg_level = SOL_SOCKET;
    cmsg->cmsg_type = SCM_RIGHTS;
    cmsg->cmsg_len = CMSG_LEN(sizeof(sendFDInt));
    memcpy(CMSG_DATA(cmsg), &sendFDInt, sizeof(sendFDInt));

    int ret = sendmsg(fd, &sendHeader, MSG_NOSIGNAL);
    if (ret == -1) {
        int errnoBackup = errno;
        throwIOException(env, "send(): Failed to send on fd " + to_string(fd)+ " with error: "+string(strerror(errnoBackup)));
        return;
    }
}