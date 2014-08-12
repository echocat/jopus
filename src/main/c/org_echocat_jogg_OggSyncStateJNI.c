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

void Java_org_echocat_jogg_OggSyncStateJNI_checkResponse
    (JNIEnv *env, const char* errorMessage, int result) {
    if (result < 0) {
        jclass type = (*env)->FindClass(env, "org/echocat/jogg/OggSyncStateException");
        (*env)->ThrowNew(env, type, errorMessage);
    }
}

JNIEXPORT long JNICALL Java_org_echocat_jogg_OggSyncStateJNI_create
    (JNIEnv *env, jclass thisClass) {

    ogg_sync_state* state = (ogg_sync_state*) malloc(sizeof(ogg_sync_state));
    ogg_sync_init(state);

    return (long) state;
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggSyncStateJNI_destroy
    (JNIEnv *env, jclass thisClass, jlong handle) {

    ogg_sync_state* state = (ogg_sync_state*) handle;
    ogg_sync_destroy(state);
}

/* ==================================================================================================================*/

/* ==================================================================================================================*/

JNIEXPORT jboolean JNICALL Java_org_echocat_jogg_OggSyncStateJNI_check
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return (jboolean) ogg_sync_check((ogg_sync_state*) handle) == 0;
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggSyncStateJNI_reset
    (JNIEnv *env, jclass thisClass, jlong handle) {

    ogg_sync_reset((ogg_sync_state*) handle);
}

JNIEXPORT jbyteArray JNICALL Java_org_echocat_jogg_OggSyncStateJNI_buffer
    (JNIEnv *env, jclass thisClass, jlong handle, jlong length) {

    ogg_sync_state* state = (ogg_sync_state*) handle;
    jbyteArray result;

    char* buffer = ogg_sync_buffer(state, length);
    if (buffer != NULL) {
        result = Java_org_echocat_jogg_OggJNISupport_bufferToJavaByteArray(env, length, buffer);
    } else {
        result = NULL;
        Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
    }

    return result;
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggSyncStateJNI_wrote
    (JNIEnv *env, jclass thisClass, jlong handle, jbyteArray source, jlong length) {

    ogg_sync_state* state = (ogg_sync_state*) handle;

    if (Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, source)) {
        jsize sourceLength = (*env)->GetArrayLength(env, source);
        if (sourceLength > (state->storage - state->fill)) {
            jclass type = (*env)->FindClass(env, "org/echocat/jogg/OggSyncStateException");
            (*env)->ThrowNew(env, type, "The size of the given array is larger then the one that was produced by buffer().");
        } else if (sourceLength > length) {
            jclass type = (*env)->FindClass(env, "org/echocat/jogg/OggSyncStateException");
            (*env)->ThrowNew(env, type, "The size of bytes that are wrote back is larger then the bytes of the given source.");
        } else {
            jbyte* nsource = (*env)->GetByteArrayElements(env, source, 0);
            if (nsource != NULL) {
                memcpy(state->data + state->fill, nsource, length);
                Java_org_echocat_jogg_OggSyncStateJNI_checkResponse(env, "Number of bytes written overflows the internal storage or an internal error occurred.",
                    ogg_sync_wrote(state, length)
                );
            } else {
                Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
            }
        }
    }
}

JNIEXPORT jboolean JNICALL Java_org_echocat_jogg_OggSyncStateJNI_pageout
    (JNIEnv *env, jclass thisClass, jlong handle, jlong pageHandle) {

    return ogg_sync_pageout((ogg_sync_state*) handle, (ogg_page*) pageHandle) == 1;
}

JNIEXPORT jint JNICALL Java_org_echocat_jogg_OggSyncStateJNI_pageseek
    (JNIEnv *env, jclass thisClass, jlong handle, jlong pageHandle) {

    int result = ogg_sync_pageseek((ogg_sync_state*) handle, (ogg_page*) pageHandle);

    if (result == 0) {
        jclass type = (*env)->FindClass(env, "org/echocat/jogg/OggSyncStateException");
        (*env)->ThrowNew(env, type, "Could not synchronize the sync state to the next page.");
    }

    return result;
}
