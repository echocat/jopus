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

#define OPUS_ENCODER_PROPERTY_GET_INVOKE(methodName, macroName) \
JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusEncoderJNI_get##methodName \
    (JNIEnv *env, jclass thisClass, jlong handle) { \
    \
    int result; \
    Java_org_echocat_jogg_OggJNISupport_checkResponse(env, \
        opus_encoder_ctl((OpusEncoder*) handle, OPUS_GET_##macroName(&result)) \
    ); \
    return result; \
}

#define OPUS_ENCODER_PROPERTY_SET_INVOKE(methodName, macroName) \
JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusEncoderJNI_set##methodName \
    (JNIEnv *env, jclass thisClass, jlong handle, jint value) { \
    \
    Java_org_echocat_jogg_OggJNISupport_checkResponse(env, \
        opus_encoder_ctl((OpusEncoder*) handle, OPUS_SET_##macroName(value)) \
    ); \
}

#define OPUS_ENCODER_PROPERTY_INVOKES(methodName, macro) \
    OPUS_ENCODER_PROPERTY_GET_INVOKE(methodName, macro) \
    OPUS_ENCODER_PROPERTY_SET_INVOKE(methodName, macro)

JNIEXPORT jlong JNICALL Java_org_echocat_jopus_OpusEncoderJNI_create
    (JNIEnv *env, jclass thisClass, jint samplingRateInHz, jint numberOfChannels, jint application) {

    int error;
    long result = (long) opus_encoder_create((int) samplingRateInHz, (int) numberOfChannels, application, &error);
    Java_org_echocat_jogg_OggJNISupport_checkResponse(env, error);

    return result;
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusEncoderJNI_destroy
    (JNIEnv *env, jclass thisClass, jlong handle) {

    opus_encoder_destroy((OpusEncoder*) handle);
    return;
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusEncoderJNI_init
    (JNIEnv *env, jclass thisClass, jlong handle, jint samplingRateInHz, jint numberOfChannels, jint application) {

    Java_org_echocat_jogg_OggJNISupport_checkResponse(env,
        opus_encoder_init((OpusEncoder*) handle, samplingRateInHz, numberOfChannels, application)
    );
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusEncoderJNI_encode
    (JNIEnv *env, jclass thisClass, jlong handle, jshortArray pcm, jint frameSize, jbyteArray packet, jint packetLength) {

    int result = 0;

    if (Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, pcm) && Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, packet)) {
        jboolean npcmIsCopy = 0;
        short* npcm = (*env)->GetPrimitiveArrayCritical(env, pcm, &npcmIsCopy);
        if (npcm != NULL) {
            jboolean npacketIsCopy = 0;
            char* npacket = (*env)->GetPrimitiveArrayCritical(env, packet, &npacketIsCopy);
            if (npacket != NULL) {
                result = opus_encode((OpusEncoder*) handle, npcm, frameSize, npacket, packetLength);
                (*env)->ReleasePrimitiveArrayCritical(env, packet, npacket, 0);

                Java_org_echocat_jogg_OggJNISupport_checkResponse(env, result);
            } else {
                Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
            }
            (*env)->ReleasePrimitiveArrayCritical(env, pcm, npcm, JNI_ABORT);
        } else {
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        }
    }

    return result;
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusEncoderJNI_encodeFloat
    (JNIEnv *env, jclass thisClass, jlong handle, jfloatArray pcm, jint frameSize, jbyteArray packet, jint packetLength) {

    int result = 0;

    if (Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, pcm) && Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, packet)) {
        jboolean npcmIsCopy = 0;
        float* npcm = (*env)->GetPrimitiveArrayCritical(env, pcm, &npcmIsCopy);
        if (npcm != NULL) {
            jboolean npacketIsCopy = 0;
            char* npacket = (*env)->GetPrimitiveArrayCritical(env, packet, &npacketIsCopy);
            if (npacket != NULL) {
                result = opus_encode_float((OpusEncoder*) handle, npcm, frameSize, npacket, packetLength);
                (*env)->ReleasePrimitiveArrayCritical(env, packet, npacket, 0);

                Java_org_echocat_jogg_OggJNISupport_checkResponse(env, result);
            } else {
                Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
            }
            (*env)->ReleasePrimitiveArrayCritical(env, pcm, npcm, JNI_ABORT);
        } else {
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        }
    }

    return result;
}

OPUS_ENCODER_PROPERTY_INVOKES(Complexity, COMPLEXITY)
OPUS_ENCODER_PROPERTY_INVOKES(BitRate, BITRATE)
OPUS_ENCODER_PROPERTY_INVOKES(Vbr, VBR)
OPUS_ENCODER_PROPERTY_INVOKES(VbrConstraint, VBR_CONSTRAINT)
OPUS_ENCODER_PROPERTY_INVOKES(ForceChannels, FORCE_CHANNELS)
OPUS_ENCODER_PROPERTY_INVOKES(MaxBandwidth, MAX_BANDWIDTH)
OPUS_ENCODER_PROPERTY_INVOKES(Signal, SIGNAL)
OPUS_ENCODER_PROPERTY_INVOKES(Application, APPLICATION)
OPUS_ENCODER_PROPERTY_GET_INVOKE(SampleRate, SAMPLE_RATE)
OPUS_ENCODER_PROPERTY_GET_INVOKE(LookAhead, LOOKAHEAD)
OPUS_ENCODER_PROPERTY_INVOKES(InBandFec, INBAND_FEC)
OPUS_ENCODER_PROPERTY_INVOKES(PacketLossPerc, PACKET_LOSS_PERC)
OPUS_ENCODER_PROPERTY_INVOKES(Dtx, DTX)
OPUS_ENCODER_PROPERTY_INVOKES(LsbDepth, LSB_DEPTH)
OPUS_ENCODER_PROPERTY_INVOKES(ExpertFrameDuration, EXPERT_FRAME_DURATION)
OPUS_ENCODER_PROPERTY_INVOKES(PredictionDisabled, PREDICTION_DISABLED)
OPUS_ENCODER_PROPERTY_GET_INVOKE(FinalRange, FINAL_RANGE)
OPUS_ENCODER_PROPERTY_INVOKES(Bandwidth, BANDWIDTH)
