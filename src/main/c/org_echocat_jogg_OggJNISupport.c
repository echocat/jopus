/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat JOpus, Copyright (c) 2014 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

#include "org_echocat_jogg_OggJNISupport.h"

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggJNISupport_init
    (JNIEnv *env, jclass thisClass) {

    _CrtSetDbgFlag ( _CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF );
}

void Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError
    (JNIEnv *env) {
    jclass type = (*env)->FindClass(env, "java/lang/OutOfMemoryError");
    (*env)->ThrowNew(env, type, NULL);
}

void Java_org_echocat_jogg_OggJNISupport_throwNullPointerException
    (JNIEnv *env) {
    jclass type = (*env)->FindClass(env, "java/lang/NullPointerException");
    (*env)->ThrowNew(env, type, NULL);
}

int Java_org_echocat_jogg_OggJNISupport_checkNotNull
    (JNIEnv *env, void *value) {

    int result = value != NULL;
    if (!result) {
        Java_org_echocat_jogg_OggJNISupport_throwNullPointerException(env);
    }
    return result;
}

jbyteArray Java_org_echocat_jogg_OggJNISupport_bufferToJavaByteArray
    (JNIEnv *env, long length, void *buffer) {
    jbyteArray result;
    if (length >= 0 && buffer != NULL) {
        result = (*env)->NewByteArray(env, length);
        if (result != NULL) {
            jbyte *nresult = (*env)->GetByteArrayElements(env, result, 0);
            if (nresult != NULL) {
                memcpy(nresult, buffer, length);
                (*env)->ReleaseByteArrayElements(env, result, nresult, 0);
            } else { 
                Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
            } 
        } else { 
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        } 
    } else { 
        result = NULL;
    }
    return result;
}

void Java_org_echocat_jogg_OggJNISupport_javaByteArrayToBuffer
    (JNIEnv *env, jbyteArray source, long *lengthDrain, void **bufferDrain) {
    if (source != NULL) { 
        jsize length = (*env)->GetArrayLength(env, source);
        jbyte* nsource = (*env)->GetByteArrayElements(env, source, 0);
        if (nsource != NULL) {
            void* buffer = (void*) malloc((int) length);
            if (buffer != NULL) {
                memcpy(buffer, nsource, length);
                (*env)->ReleaseByteArrayElements(env, source, nsource, JNI_ABORT);
                if (*bufferDrain != NULL) {
                    free(*bufferDrain);
                }
                *bufferDrain = buffer;
                *lengthDrain = length;
            } else {
                Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
            } 
        } else { 
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        } 
    } else { 
        *bufferDrain = NULL;
        *lengthDrain = 0;
    }
}
