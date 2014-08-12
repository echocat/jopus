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

JNIEXPORT long JNICALL Java_org_echocat_jogg_OggPackBufferJNI_create
    (JNIEnv *env, jclass thisClass) {

    return (long) malloc(sizeof(oggpack_buffer));
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPackBufferJNI_destroy
    (JNIEnv *env, jclass thisClass, jlong handle) {

    free((void*) handle);
}

/* ==================================================================================================================*/


/* ==================================================================================================================*/

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPackBufferJNI_writetnit
    (JNIEnv *env, jclass thisClass, jlong handle) {

    oggpack_writeinit((oggpack_buffer*) handle);
}

JNIEXPORT jboolean JNICALL Java_org_echocat_jogg_OggPackBufferJNI_writecheck
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return (jboolean) oggpack_writecheck((oggpack_buffer*) handle) == 0;
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPackBufferJNI_writetrunc
    (JNIEnv *env, jclass thisClass, jlong handle, jlong bits) {

    oggpack_writetrunc((oggpack_buffer*) handle, bits);
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPackBufferJNI_writealign
    (JNIEnv *env, jclass thisClass, jlong handle) {

    oggpack_writealign((oggpack_buffer*) handle);
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPackBufferJNI_writecopy
    (JNIEnv *env, jclass thisClass, jlong handle, jbyteArray buffer) {

    if (Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, buffer)) {
        jsize length = (*env)->GetArrayLength(env, buffer);
        char* nbuffer = (*env)->GetByteArrayElements(env, buffer, 0);
        if (nbuffer != NULL) {
            oggpack_writecopy((oggpack_buffer*) handle, nbuffer, length);
            (*env)->ReleaseByteArrayElements(env, buffer, nbuffer, JNI_ABORT);
        } else {
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        }
    }
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPackBufferJNI_reset
    (JNIEnv *env, jclass thisClass, jlong handle) {

    oggpack_reset((oggpack_buffer*) handle);
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPackBufferJNI_writeclear
    (JNIEnv *env, jclass thisClass, jlong handle) {

    oggpack_writeclear((oggpack_buffer*) handle);
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPackBufferJNI_readinit
    (JNIEnv *env, jclass thisClass, jlong handle, jbyteArray buffer) {

    if (Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, buffer)) {
        jsize length = (*env)->GetArrayLength(env, buffer);
        char* nbuffer = (*env)->GetByteArrayElements(env, buffer, 0);
        if (nbuffer != NULL) {
            oggpack_readinit((oggpack_buffer*) handle, nbuffer, length);
            (*env)->ReleaseByteArrayElements(env, buffer, nbuffer, JNI_ABORT);
        } else {
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        }
    }
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPackBufferJNI_write
    (JNIEnv *env, jclass thisClass, jlong handle, jlong value, jint bits) {

    oggpack_write((oggpack_buffer*) handle, value, bits);
}

JNIEXPORT jlong JNICALL Java_org_echocat_jogg_OggPackBufferJNI_look
    (JNIEnv *env, jclass thisClass, jlong handle, jint bits) {

    return (jlong) oggpack_look((oggpack_buffer*) handle, bits);
}

JNIEXPORT jlong JNICALL Java_org_echocat_jogg_OggPackBufferJNI_look1
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return (jlong) oggpack_look1((oggpack_buffer*) handle);
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPackBufferJNI_adv
    (JNIEnv *env, jclass thisClass, jlong handle, jint bits) {

    oggpack_adv((oggpack_buffer*) handle, bits);
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPackBufferJNI_adv1
    (JNIEnv *env, jclass thisClass, jlong handle) {

    oggpack_adv1((oggpack_buffer*) handle);
}

JNIEXPORT jlong JNICALL Java_org_echocat_jogg_OggPackBufferJNI_read
    (JNIEnv *env, jclass thisClass, jlong handle, jint bits) {

    return (jlong) oggpack_read((oggpack_buffer*) handle, bits);
}

JNIEXPORT jlong JNICALL Java_org_echocat_jogg_OggPackBufferJNI_read1
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return (jlong) oggpack_read1((oggpack_buffer*) handle);
}

JNIEXPORT jlong JNICALL Java_org_echocat_jogg_OggPackBufferJNI_bytes
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return (jlong) oggpack_bytes((oggpack_buffer*) handle);
}

JNIEXPORT jlong JNICALL Java_org_echocat_jogg_OggPackBufferJNI_bits
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return (jlong) oggpack_bits((oggpack_buffer*) handle);
}

