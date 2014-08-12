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

JNIEXPORT jlong JNICALL Java_org_echocat_jopus_OpusRepacketizerJNI_create
    (JNIEnv *env, jclass thisClass) {

    long result = (long) opus_repacketizer_create();

    return result;
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusRepacketizerJNI_destroy
    (JNIEnv *env, jclass thisClass, jlong handle) {

    opus_repacketizer_destroy((OpusRepacketizer*) handle);
    return;
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusRepacketizerJNI_init
    (JNIEnv *env, jclass thisClass, jlong handle) {

    opus_repacketizer_init((OpusRepacketizer*) handle);
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusRepacketizerJNI_cat
    (JNIEnv *env, jclass thisClass, jlong handle, jbyteArray packet) {

    char* buffer;
    size_t length;

    Java_org_echocat_jogg_OggJNISupport_javaByteArrayToBuffer(env, packet, &length, &buffer);

    int result = opus_repacketizer_cat((OpusRepacketizer*) handle, buffer, length);
    free(buffer);

    Java_org_echocat_jogg_OggJNISupport_checkResponse(env, result);

    return result;
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusRepacketizerJNI_outRange
    (JNIEnv *env, jclass thisClass, jlong handle, jint begin, jint end, jbyteArray packet) {

    char* buffer;
    size_t length;

    Java_org_echocat_jogg_OggJNISupport_javaByteArrayToBuffer(env, packet, &length, &buffer);

    int result = opus_repacketizer_out_range((OpusRepacketizer*) handle, begin, end, buffer, length);
    free(buffer);

    Java_org_echocat_jogg_OggJNISupport_checkResponse(env, result);

    return result;
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusRepacketizerJNI_getNumberOfFrames
    (JNIEnv *env, jclass thisClass, jlong handle) {

    int result = opus_repacketizer_get_nb_frames((OpusRepacketizer*) handle);
    Java_org_echocat_jogg_OggJNISupport_checkResponse(env, result);

    return result;
}

JNIEXPORT jint JNICALL Java_org_echocat_jopus_OpusRepacketizerJNI_out
    (JNIEnv *env, jclass thisClass, jlong handle, jbyteArray packet) {

    char* buffer;
    size_t length;

    Java_org_echocat_jogg_OggJNISupport_javaByteArrayToBuffer(env, packet, &length, &buffer);

    int result = opus_repacketizer_out((OpusRepacketizer*) handle, buffer, length);
    free(buffer);

    Java_org_echocat_jogg_OggJNISupport_checkResponse(env, result);

    return result;
}

JNIEXPORT void JNICALL Java_org_echocat_jopus_OpusRepacketizerJNI_pad
    (JNIEnv *env, jclass thisClass, jbyteArray packet, jint newPacketLength) {

    char* buffer;
    size_t length;

    Java_org_echocat_jogg_OggJNISupport_javaByteArrayToBuffer(env, packet, &length, &buffer);

    int result = opus_packet_pad(buffer, length, newPacketLength);
    free(buffer);

    Java_org_echocat_jogg_OggJNISupport_checkResponse(env, result);
}

JNIEXPORT int JNICALL Java_org_echocat_jopus_OpusRepacketizerJNI_unpad
    (JNIEnv *env, jclass thisClass, jbyteArray packet) {
    char* buffer;
    size_t length;

    Java_org_echocat_jogg_OggJNISupport_javaByteArrayToBuffer(env, packet, &length, &buffer);

    int result = opus_packet_unpad(buffer, length);
    free(buffer);

    Java_org_echocat_jogg_OggJNISupport_checkResponse(env, result);

    return result;
}
