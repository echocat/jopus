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

JNIEXPORT long JNICALL Java_org_echocat_jogg_OggPacketJNI_create
    (JNIEnv *env, jclass thisClass) {

    ogg_packet* packet = (ogg_packet*) malloc(sizeof(ogg_packet));
    packet->packet = NULL;
    packet->bytes = 0;
    packet->b_o_s = 0;
    packet->e_o_s = 0;
    packet->granulepos = 0;
    packet->packetno = 0;

    return (long) packet;
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPacketJNI_destroy
    (JNIEnv *env, jclass thisClass, jlong handle) {

    ogg_packet* packet = (ogg_packet*) handle;

    if (packet->packet != NULL) {
        free((void*)packet->packet);
    }
    free((void*)packet);
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPacketJNI_setBuffer
    (JNIEnv *env, jclass thisClass, jlong handle, jbyteArray buffer) {

    ogg_packet* packet = (ogg_packet*) handle;

    Java_org_echocat_jogg_OggJNISupport_javaByteArrayToBuffer(env, buffer, &packet->bytes, &packet->packet);
}

JNIEXPORT jbyteArray JNICALL Java_org_echocat_jogg_OggPacketJNI_getBuffer
    (JNIEnv *env, jclass thisClass, jlong handle) {

    ogg_packet* packet = (ogg_packet*) handle;

    return Java_org_echocat_jogg_OggJNISupport_bufferToJavaByteArray(env, packet->bytes, packet->packet);
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPacketJNI_setBos
    (JNIEnv *env, jclass thisClass, jlong handle, jlong value) {

    ((ogg_packet*) handle)->b_o_s = value;
}

JNIEXPORT long JNICALL Java_org_echocat_jogg_OggPacketJNI_getBos
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return ((ogg_packet*) handle)->b_o_s;
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPacketJNI_setEos
    (JNIEnv *env, jclass thisClass, jlong handle, jlong value) {

    ((ogg_packet*) handle)->e_o_s = value;
}

JNIEXPORT long JNICALL Java_org_echocat_jogg_OggPacketJNI_getEos
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return ((ogg_packet*) handle)->e_o_s;
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPacketJNI_setGranulepos
    (JNIEnv *env, jclass thisClass, jlong handle, jlong value) {

    ((ogg_packet*) handle)->granulepos = value;
}

JNIEXPORT long JNICALL Java_org_echocat_jogg_OggPacketJNI_getGranulepos
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return ((ogg_packet*) handle)->granulepos;
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPacketJNI_setPacketno
    (JNIEnv *env, jclass thisClass, jlong handle, jlong value) {

    ((ogg_packet*) handle)->packetno = value;
}

JNIEXPORT long JNICALL Java_org_echocat_jogg_OggPacketJNI_getPacketno
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return ((ogg_packet*) handle)->packetno;
}
