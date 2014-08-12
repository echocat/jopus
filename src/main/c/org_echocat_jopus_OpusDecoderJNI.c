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

#define OPUS_DECODER_PROPERTY_GET_INVOKE(methodName, macroName) \
JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusDecoderJNI_get##methodName \
    (JNIEnv *env, jclass thisClass, jlong handle) { \
    \
    int result; \
    Java_org_echocat_jogg_OggJNISupport_checkResponse(env, \
        opus_decoder_ctl((OpusDecoder*) handle, OPUS_GET_##macroName(&result)) \
    ); \
    return result; \
}

#define OPUS_DECODER_PROPERTY_SET_INVOKE(methodName, macroName) \
JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusDecoderJNI_set##methodName \
    (JNIEnv *env, jclass thisClass, jlong handle, jint value) { \
    \
    Java_org_echocat_jogg_OggJNISupport_checkResponse(env, \
        opus_decoder_ctl((OpusDecoder*) handle, OPUS_SET_##macroName(value)) \
    ); \
}

#define OPUS_DECODER_PROPERTY_INVOKES(methodName, macro) \
    OPUS_DECODER_PROPERTY_GET_INVOKE(methodName, macro) \
    OPUS_DECODER_PROPERTY_SET_INVOKE(methodName, macro)


JNIEXPORT jlong JNICALL Java_org_echocat_jopus_OpusDecoderJNI_create
    (JNIEnv *env, jclass thisClass, jint samplingRateInHz, jint numberOfChannels) {

    int error;
    long result = (long) opus_decoder_create((int) samplingRateInHz, (int) numberOfChannels, &error);
    Java_org_echocat_jogg_OggJNISupport_checkResponse(env, error);

    return result;
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusDecoderJNI_destroy
    (JNIEnv *env, jclass thisClass, jlong handle) {

    opus_decoder_destroy((OpusDecoder*) handle);
    return;
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusDecoderJNI_init
    (JNIEnv *env, jclass thisClass, jlong handle, jint samplingRateInHz, jint numberOfChannels) {

    Java_org_echocat_jogg_OggJNISupport_checkResponse(env,
        opus_decoder_init((OpusDecoder*) handle, samplingRateInHz, numberOfChannels)
    );
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusDecoderJNI_decode
    (JNIEnv *env, jclass thisClass, jlong handle, jbyteArray packet, jint packetLength, jshortArray pcm, jint frameSize, jint decodeFec) {

    int result = 0;
    if (Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, packet) && Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, pcm)) {
        jbyte* npacket = (*env)->GetByteArrayElements(env, packet, 0);
        if (npacket != NULL) {
            jshort* npcm = (*env)->GetShortArrayElements(env, pcm, 0);
            if (npcm != NULL) {
                result = opus_decode((OpusDecoder*) handle, npacket, packetLength, npcm, frameSize, decodeFec);
                (*env)->ReleaseShortArrayElements(env, pcm, npcm, 0);

                Java_org_echocat_jogg_OggJNISupport_checkResponse(env, result);
            } else {
                Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
            }
            (*env)->ReleaseByteArrayElements(env, packet, npacket, JNI_ABORT);
        } else {
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        }
    }

    return result;
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusDecoderJNI_decodeFloat
    (JNIEnv *env, jclass thisClass, jlong handle, jbyteArray packet, jint packetLength, jfloatArray pcm, jint frameSize, jint decodeFec) {

    int result = 0;

    if (Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, packet) && Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, pcm)) {
        jbyte* npacket = (*env)->GetByteArrayElements(env, packet, 0);
        if (npacket != NULL) {
            float* npcm = (*env)->GetFloatArrayElements(env, pcm, 0);
            if (npcm != NULL) {
                result = opus_decode_float((OpusDecoder*) handle, npacket, packetLength, npcm, frameSize, decodeFec);
                (*env)->ReleaseFloatArrayElements(env, pcm, npcm, 0);

                Java_org_echocat_jogg_OggJNISupport_checkResponse(env, result);
            } else {
                Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
            }
            (*env)->ReleaseByteArrayElements(env, packet, npacket, JNI_ABORT);
        } else {
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        }
    }
    return result;
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusDecoderJNI_getNumberOfSamplesOfPacket
    (JNIEnv *env, jclass thisClass, jlong handle, jbyteArray packet) {

    int result = 0;
    if (Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, packet)) {
        size_t length = (*env)->GetArrayLength(env, packet);
        char* npacket = (*env)->GetByteArrayElements(env, packet, 0);
        if (npacket != NULL) {
            result = opus_decoder_get_nb_samples((OpusDecoder*) handle, npacket, length);
            (*env)->ReleaseByteArrayElements(env, packet, npacket, JNI_ABORT);

            Java_org_echocat_jogg_OggJNISupport_checkResponse(env, result);
        } else {
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        }
    }
    return result;
}

OPUS_DECODER_PROPERTY_GET_INVOKE(SampleRate, SAMPLE_RATE)
OPUS_DECODER_PROPERTY_GET_INVOKE(LookAhead, LOOKAHEAD)
OPUS_DECODER_PROPERTY_GET_INVOKE(LastPacketDuration, LAST_PACKET_DURATION)
OPUS_DECODER_PROPERTY_GET_INVOKE(FinalRange, FINAL_RANGE)
OPUS_DECODER_PROPERTY_GET_INVOKE(Pitch, PITCH)
OPUS_DECODER_PROPERTY_GET_INVOKE(Bandwidth, BANDWIDTH)
OPUS_DECODER_PROPERTY_INVOKES(Gain, GAIN)
