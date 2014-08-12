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

JNIEXPORT long JNICALL Java_org_echocat_jogg_OggIovecJNI_create
    (JNIEnv *env, jclass thisClass) {

    return (long) malloc(sizeof(ogg_iovec_t));
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggIovecJNI_destroy
    (JNIEnv *env, jclass thisClass, jlong handle) {

    free((void*) handle);
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggIovecJNI_setBuffer
    (JNIEnv *env, jclass thisClass, jlong handle, jbyteArray buffer) {

    ogg_iovec_t* iovec = (ogg_iovec_t*) handle;

    Java_org_echocat_jogg_OggJNISupport_javaByteArrayToBuffer(env, buffer, &((long)iovec->iov_len), &iovec->iov_base);
}

JNIEXPORT jbyteArray JNICALL Java_org_echocat_jogg_OggIovecJNI_getBuffer
    (JNIEnv *env, jclass thisClass, jlong handle) {

    ogg_iovec_t* iovec = (ogg_iovec_t*) handle;

    return Java_org_echocat_jogg_OggJNISupport_bufferToJavaByteArray(env, iovec->iov_len, iovec->iov_base);
}
