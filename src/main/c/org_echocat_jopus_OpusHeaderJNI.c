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

#include "org_echocat_jopus_OpusJNISupport.h"
#include <opus_header.h>

void Java_org_echocat_jopus_OpusHeaderJNI_checkResponse
    (JNIEnv *env, const char* errorMessage, int result) {
    if (result == 0) {
        jclass type = (*env)->FindClass(env, "org/echocat/jopus/OpusHeaderException");
        (*env)->ThrowNew(env, type, errorMessage);
    }
}

JNIEXPORT jlong JNICALL Java_org_echocat_jopus_OpusHeaderJNI_create
    (JNIEnv *env, jclass thisClass) {

    return (long) malloc(sizeof(OpusHeader));
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusHeaderJNI_destroy
    (JNIEnv *env, jclass thisClass, jlong handle) {

    free((void*) handle);
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusHeaderJNI_writePacket
    (JNIEnv *env, jclass thisClass, jlong handle, jbyteArray buffer) {

    OpusHeader* header = (OpusHeader*) handle;

    jint result = 0;
    if (Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, buffer)) {
        jsize length = (*env)->GetArrayLength(env, buffer);
        jbyte* nbuffer = (*env)->GetByteArrayElements(env, buffer, 0);
        if (nbuffer != NULL) {
            result = opus_header_to_packet(header, nbuffer, length);
            Java_org_echocat_jopus_OpusHeaderJNI_checkResponse(env, "Could not create header packet.", result);
            (*env)->ReleaseByteArrayElements(env, buffer, nbuffer, 0);
        } else {
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        }
    }

    return result;
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusHeaderJNI_readPacket
    (JNIEnv *env, jclass thisClass, jlong handle, jbyteArray buffer) {

    OpusHeader* header = (OpusHeader*) handle;

    if (Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, buffer)) {
        jsize length = (*env)->GetArrayLength(env, buffer);
        jbyte* nbuffer = (*env)->GetByteArrayElements(env, buffer, 0);
        if (nbuffer != NULL) {
            Java_org_echocat_jopus_OpusHeaderJNI_checkResponse(env, "Could not parse header packet.",
                opus_header_parse(nbuffer, length, header)
            );
        (*env)->ReleaseByteArrayElements(env, buffer, nbuffer, JNI_ABORT);
        } else {
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        }
    }
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusHeaderJNI_setVersion
    (JNIEnv *env, jclass thisClass, jlong handle, jint value) {

    ((OpusHeader*)handle)->version = value;
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusHeaderJNI_getVersion
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return ((OpusHeader*)handle)->version;
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusHeaderJNI_setChannels
    (JNIEnv *env, jclass thisClass, jlong handle, jint value) {

    ((OpusHeader*)handle)->channels = value;
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusHeaderJNI_getChannels
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return ((OpusHeader*)handle)->channels;
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusHeaderJNI_setPreskip
    (JNIEnv *env, jclass thisClass, jlong handle, jint value) {

    ((OpusHeader*)handle)->preskip = value;
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusHeaderJNI_getPreskip
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return ((OpusHeader*)handle)->preskip;
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusHeaderJNI_setInputSampleRate
    (JNIEnv *env, jclass thisClass, jlong handle, jint value) {

    ((OpusHeader*)handle)->input_sample_rate = value;
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusHeaderJNI_getInputSampleRate
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return ((OpusHeader*)handle)->input_sample_rate;
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusHeaderJNI_setGain
    (JNIEnv *env, jclass thisClass, jlong handle, jint value) {

    ((OpusHeader*)handle)->gain = value;
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusHeaderJNI_getGain
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return ((OpusHeader*)handle)->gain;
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusHeaderJNI_setChannelMapping
    (JNIEnv *env, jclass thisClass, jlong handle, jint value) {

    ((OpusHeader*)handle)->channel_mapping = value;
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusHeaderJNI_getChannelMapping
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return ((OpusHeader*)handle)->channel_mapping;
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusHeaderJNI_setNbStreams
    (JNIEnv *env, jclass thisClass, jlong handle, jint value) {

    ((OpusHeader*)handle)->nb_streams = value;
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusHeaderJNI_getNbStreams
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return ((OpusHeader*)handle)->nb_streams;
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusHeaderJNI_setNbCoupled
    (JNIEnv *env, jclass thisClass, jlong handle, jint value) {

    ((OpusHeader*)handle)->nb_coupled = value;
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusHeaderJNI_getNbCoupled
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return ((OpusHeader*)handle)->nb_coupled;
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusHeaderJNI_setStreamMap
    (JNIEnv *env, jclass thisClass, jlong handle, jbyteArray value) {

    OpusHeader* header = (OpusHeader*) handle;

    if (Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, value)) {
        jbyte* nvalue = (*env)->GetByteArrayElements(env, value, 0);
        if (nvalue != NULL) {
            size_t length = (*env)->GetArrayLength(env, value);
            for (int i=0; i < 255 && i < length; i++) {
                ((OpusHeader*)handle)->stream_map[i] = (char) nvalue[i];
            }
            (*env)->ReleaseByteArrayElements(env, value, nvalue, JNI_ABORT);
        } else {
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        }
    }
}

JNIEXPORT jbyteArray JNICALL Java_org_echocat_jopus_OpusHeaderJNI_getStreamMap
    (JNIEnv *env, jclass thisClass, jlong handle) {

    OpusHeader* header = (OpusHeader*) handle;

    jbyteArray buffer = (*env)->NewByteArray(env, 255);
    if (buffer != NULL) {
        jbyte *nbuffer = (*env)->GetByteArrayElements(env, buffer, 0);
        if (nbuffer != NULL) {
            for (int i=0; i < 255; i++) {
                nbuffer[i] = (jbyte) header->stream_map[i];
            }
            (*env)->ReleaseByteArrayElements(env, buffer, nbuffer, 0);
        } else {
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        }
    } else {
        Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
    }

    return buffer;
}


