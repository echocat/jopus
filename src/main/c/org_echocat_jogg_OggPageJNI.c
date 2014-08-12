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

int Java_org_echocat_jogg_OggPageJNI_checkResponse
    (JNIEnv *env, const char* errorMessage, int result) {
    if (result < 0) {
        jclass type = (*env)->FindClass(env, "org/echocat/jogg/OggPageException");
        (*env)->ThrowNew(env, type, errorMessage);
    }
    return result;
}

JNIEXPORT long JNICALL Java_org_echocat_jogg_OggPageJNI_create
    (JNIEnv *env, jclass thisClass) {

    ogg_page* page = (ogg_page*) malloc(sizeof(ogg_page));
    page->header = NULL;
    page->header_len = 0;
    page->body = NULL;
    page->body_len = 0;

    return (long) page;
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPageJNI_destroy
    (JNIEnv *env, jclass thisClass, jlong handle) {

    free((ogg_page*) handle);
}

/* ==================================================================================================================*/

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPageJNI_setHeader
    (JNIEnv *env, jclass thisClass, jlong handle, jbyteArray header) {

    ogg_page* page = (ogg_page*) handle;

    Java_org_echocat_jogg_OggJNISupport_javaByteArrayToBuffer(env, header, &page->header_len, &page->header);
}

JNIEXPORT jbyteArray JNICALL Java_org_echocat_jogg_OggPageJNI_getHeader
    (JNIEnv *env, jclass thisClass, jlong handle) {

    ogg_page* page = (ogg_page*) handle;

    return Java_org_echocat_jogg_OggJNISupport_bufferToJavaByteArray(env, page->header_len, page->header);
}

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPageJNI_setBody
    (JNIEnv *env, jclass thisClass, jlong handle, jbyteArray body) {

    ogg_page* page = (ogg_page*) handle;

    Java_org_echocat_jogg_OggJNISupport_javaByteArrayToBuffer(env, body, &page->body_len, &page->body);
}

JNIEXPORT jbyteArray JNICALL Java_org_echocat_jogg_OggPageJNI_getBody
    (JNIEnv *env, jclass thisClass, jlong handle) {

    ogg_page* page = (ogg_page*) handle;

    return Java_org_echocat_jogg_OggJNISupport_bufferToJavaByteArray(env, page->body_len, page->body);
}

/* ==================================================================================================================*/

JNIEXPORT void JNICALL Java_org_echocat_jogg_OggPageJNI_checksumSet
    (JNIEnv *env, jclass thisClass, jlong handle) {

    ogg_page_checksum_set((ogg_page *) handle);

    return;
}

JNIEXPORT jint JNICALL Java_org_echocat_jogg_OggPageJNI_version
    (JNIEnv *env, jclass thisClass, jlong handle) {

    return Java_org_echocat_jogg_OggPageJNI_checkResponse(env, "Could not receive the version of page.",
        ogg_page_version((ogg_page *) handle)
    );
}

JNIEXPORT jboolean JNICALL Java_org_echocat_jogg_OggPageJNI_continued
    (JNIEnv *env, jclass thisClass, jlong handle) {

    ogg_page * page = (ogg_page *) handle;

    return page->header != NULL ? ogg_page_continued(page) : 0;
}

JNIEXPORT jint JNICALL Java_org_echocat_jogg_OggPageJNI_bos
    (JNIEnv *env, jclass thisClass, jlong handle) {

    ogg_page * page = (ogg_page *) handle;

    return page->header != NULL ? ogg_page_bos(page) : 0;
}

JNIEXPORT jint JNICALL Java_org_echocat_jogg_OggPageJNI_eos
    (JNIEnv *env, jclass thisClass, jlong handle) {

    ogg_page * page = (ogg_page *) handle;

    return page->header != NULL ? ogg_page_eos(page) : 0;
}

JNIEXPORT jlong JNICALL Java_org_echocat_jogg_OggPageJNI_granulepos
    (JNIEnv *env, jclass thisClass, jlong handle) {

    ogg_page * page = (ogg_page *) handle;

    return page->header != NULL ? ogg_page_granulepos(page) : 0;
}

JNIEXPORT jlong JNICALL Java_org_echocat_jogg_OggPageJNI_serialno
    (JNIEnv *env, jclass thisClass, jlong handle) {

    ogg_page * page = (ogg_page *) handle;

    return page->header != NULL ? ogg_page_serialno(page) : 0;
}

JNIEXPORT jlong JNICALL Java_org_echocat_jogg_OggPageJNI_pageno
    (JNIEnv *env, jclass thisClass, jlong handle) {

    ogg_page * page = (ogg_page *) handle;

    return page->header != NULL ? ogg_page_pageno(page) : 0;
}

JNIEXPORT jint JNICALL Java_org_echocat_jogg_OggPageJNI_packets
    (JNIEnv *env, jclass thisClass, jlong handle) {

    ogg_page * page = (ogg_page *) handle;

    return page->header != NULL ? ogg_page_packets(page) : 0;
}

