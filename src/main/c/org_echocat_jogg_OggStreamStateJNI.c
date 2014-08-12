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

void Java_org_echocat_jogg_OggStreamStateJNI_checkResponse
    (JNIEnv *env, const char* errorMessage, int result) {
    if (result < 0) {
        jclass type = (*env)->FindClass(env, "org/echocat/jogg/OggStreamStateException");
        (*env)->ThrowNew(env, type, errorMessage);
    }
}

JNIEXPORT jlong JNICALL Java_org_echocat_jogg_OggStreamStateJNI_create
    (JNIEnv *env, jclass thisClass, jlong serialno) {

    ogg_stream_state* stream = (ogg_stream_state*) malloc(sizeof(ogg_stream_state));
    Java_org_echocat_jogg_OggStreamStateJNI_checkResponse(env, "Could not create stream.",
        ogg_stream_init(stream, serialno)
    );

    return (jlong) stream;
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggStreamStateJNI_destroy
    (JNIEnv *env, jclass thisClass, jlong handle) {

    ogg_stream_state* stream = (ogg_stream_state*) handle;
    ogg_stream_destroy(stream);
}

JNIEXPORT jlong JNICALL Java_org_echocat_jogg_OggStreamStateJNI_getSerialno
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return ((ogg_stream_state*) handle)->serialno;
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggStreamStateJNI_packetin
    (JNIEnv *env, jclass thisClass, jlong handle, jlong packetHandle) {

    Java_org_echocat_jogg_OggStreamStateJNI_checkResponse(env, "Could not submit a packet to the bitstream for page encapsulation.",
        ogg_stream_packetin((ogg_stream_state*) handle, (ogg_packet*) packetHandle)
    );
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggStreamStateJNI_iovecin
    (JNIEnv *env, jclass thisClass, jlong handle, jlong iovecHandle, jint count, jlong e_o_s, jlong granulepos) {

    Java_org_echocat_jogg_OggStreamStateJNI_checkResponse(env, "Could not submit a packet to the bitstream for page encapsulation.",
        ogg_stream_iovecin((ogg_stream_state*) handle, (ogg_iovec_t*) iovecHandle, count, e_o_s, (ogg_int64_t) granulepos)
    );
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggStreamStateJNI_pageout
    (JNIEnv *env, jclass thisClass, jlong handle, jlong pageHandle) {

    Java_org_echocat_jogg_OggStreamStateJNI_checkResponse(env, "Could not form packets into pages.",
        ogg_stream_pageout((ogg_stream_state*) handle, (ogg_page*) pageHandle)
    );
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggStreamStateJNI_pageoutFill
    (JNIEnv *env, jclass thisClass, jlong handle, jlong pageHandle, jint nfill) {

    Java_org_echocat_jogg_OggStreamStateJNI_checkResponse(env, "Could not form packets into pages.",
        ogg_stream_pageout_fill((ogg_stream_state*) handle, (ogg_page*) pageHandle, nfill)
    );
}

JNIEXPORT jint JNICALL Java_org_echocat_jogg_OggStreamStateJNI_flush
    (JNIEnv *env, jclass thisClass, jlong handle, jlong pageHandle) {

    return ogg_stream_flush((ogg_stream_state*) handle, (ogg_page*) pageHandle);
}

JNIEXPORT jint JNICALL Java_org_echocat_jogg_OggStreamStateJNI_flushFill
    (JNIEnv *env, jclass thisClass, jlong handle, jlong pageHandle, jint nfill) {

    return ogg_stream_flush_fill((ogg_stream_state*) handle, (ogg_page*) pageHandle, nfill);
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggStreamStateJNI_pagein
    (JNIEnv *env, jclass thisClass, jlong handle, jlong pageHandle) {

    Java_org_echocat_jogg_OggStreamStateJNI_checkResponse(env, "Could not add a complete page to the bitstream.",
        ogg_stream_pagein((ogg_stream_state*) handle, (ogg_page*) pageHandle)
    );
}

JNIEXPORT jboolean JNICALL Java_org_echocat_jogg_OggStreamStateJNI_packetout
    (JNIEnv *env, jclass thisClass, jlong handle, jlong packetHandle) {

    int result = ogg_stream_packetout((ogg_stream_state*) handle, (ogg_packet*) packetHandle);
    Java_org_echocat_jogg_OggStreamStateJNI_checkResponse(env, "Out of sync and there is a gap in the data.",
        result
    );
    return result;
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggStreamStateJNI_packetpeek
    (JNIEnv *env, jclass thisClass, jlong handle, jlong packetHandle) {

    Java_org_echocat_jogg_OggStreamStateJNI_checkResponse(env, "Could not attempt to assemble a raw data packet and returns it without advancing decoding.",
        ogg_stream_packetpeek((ogg_stream_state*) handle, (ogg_packet*) packetHandle)
    );
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggStreamStateJNI_reset
    (JNIEnv *env, jclass thisClass, jlong handle) {

    Java_org_echocat_jogg_OggStreamStateJNI_checkResponse(env, "Could not sets values back to initial values.",
        ogg_stream_reset((ogg_stream_state*) handle)
    );
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggStreamStateJNI_resetSerialno
    (JNIEnv *env, jclass thisClass, jlong handle, jlong serialno) {

    Java_org_echocat_jogg_OggStreamStateJNI_checkResponse(env, "Could not sets values back to initial values.",
        ogg_stream_reset_serialno((ogg_stream_state*) handle, serialno)
    );
}

JNIEXPORT jboolean JNICALL Java_org_echocat_jogg_OggStreamStateJNI_check
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return (jboolean) ogg_stream_check((ogg_stream_state*) handle) == 0;
}

JNIEXPORT jboolean JNICALL Java_org_echocat_jogg_OggStreamStateJNI_eos
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return (jboolean) ogg_stream_eos((ogg_stream_state*) handle) == 1;
}
