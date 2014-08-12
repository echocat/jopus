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

JNIEXPORT jstring JNICALL Java_org_echocat_jopus_OpusUtilsJNI_getErrorStringForErrorCode
    (JNIEnv *env, jclass thisClass, jint errorCode) {

    const char* errorString = opus_strerror(errorCode);
    return (*env)->NewStringUTF(env, errorString);
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusUtilsJNI_getBandwidthOfPacket
    (JNIEnv *env, jclass thisClass, jbyteArray packet) {

    jint result = 0;

    if (Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, packet)) {
        char* npacket = (*env)->GetByteArrayElements(env, packet, 0);
        if (npacket != NULL) {
            result = opus_packet_get_bandwidth(npacket);
            (*env)->ReleaseByteArrayElements(env, packet, npacket, JNI_ABORT);

            Java_org_echocat_jogg_OggJNISupport_checkResponse(env, result);
        } else {
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        }
    }

    return result;
}


JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusUtilsJNI_getSamplesPerFrameOfPacket
    (JNIEnv *env, jclass thisClass, jbyteArray packet, jint samplingRateInHz) {

    jint result = 0;

    if (Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, packet)) {
        char* npacket = (*env)->GetByteArrayElements(env, packet, 0);
        if (npacket != NULL) {
            result = opus_packet_get_samples_per_frame(npacket, samplingRateInHz);
            (*env)->ReleaseByteArrayElements(env, packet, npacket, JNI_ABORT);

            Java_org_echocat_jogg_OggJNISupport_checkResponse(env, result);
        } else {
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        }
    }

    return result;
}


JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusUtilsJNI_getNumberOfChannelsOfPacket
    (JNIEnv *env, jclass thisClass, jbyteArray packet) {

    jint result = 0;

    if (Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, packet)) {
        char* npacket = (*env)->GetByteArrayElements(env, packet, 0);
        if (npacket != NULL) {
            result = opus_packet_get_nb_channels(npacket);
            (*env)->ReleaseByteArrayElements(env, packet, npacket, JNI_ABORT);

            Java_org_echocat_jogg_OggJNISupport_checkResponse(env, result);
        } else {
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        }
    }

    return result;
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusUtilsJNI_getNumberOfFramesOfPacket
    (JNIEnv *env, jclass thisClass, jbyteArray packet) {

    jint result = 0;

    if (Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, packet)) {
        char* npacket = (*env)->GetByteArrayElements(env, packet, 0);
        if (npacket != NULL) {
            jsize length = (*env)->GetArrayLength(env, packet);
            result = opus_packet_get_nb_frames(npacket, length);
            (*env)->ReleaseByteArrayElements(env, packet, npacket, JNI_ABORT);

            Java_org_echocat_jogg_OggJNISupport_checkResponse(env, result);
        } else {
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        }
    }

    return result;
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusUtilsJNI_getNumberOfSamplesOfPacket
    (JNIEnv *env, jclass thisClass, jbyteArray packet, jint samplingRateInHz) {

    jint result = 0;

    if (Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, packet)) {
        char* npacket = (*env)->GetByteArrayElements(env, packet, 0);
        if (npacket != NULL) {
            jsize length = (*env)->GetArrayLength(env, packet);
            result = opus_packet_get_nb_samples(npacket, length, samplingRateInHz);
            (*env)->ReleaseByteArrayElements(env, packet, npacket, JNI_ABORT);

            Java_org_echocat_jogg_OggJNISupport_checkResponse(env, result);
        } else {
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        }
    }

    return result;
}


JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusUtilsJNI_applyPcmSoftClipping
    (JNIEnv *env, jclass thisClass, jfloatArray pcm, jint frameSize, jint numberOfChannels, jfloatArray softClipping) {

    if (Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, pcm) && Java_org_echocat_jogg_OggJNISupport_checkNotNull(env, softClipping)) {
        float* npcm = (*env)->GetFloatArrayElements(env, pcm, 0);
        if (npcm != NULL) {
            float* nsoftClipping = (*env)->GetFloatArrayElements(env, softClipping, 0);
            if (nsoftClipping != NULL) {
                opus_pcm_soft_clip(npcm, frameSize, numberOfChannels, nsoftClipping);

                (*env)->ReleaseFloatArrayElements(env, softClipping, nsoftClipping, 0);
            } else {
                Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
            }

            (*env)->ReleaseFloatArrayElements(env, pcm, npcm, 0);
        } else {
            Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError(env);
        }
    }
}


