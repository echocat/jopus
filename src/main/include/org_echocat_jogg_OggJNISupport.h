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

#ifndef org_echocat_jogg_OggJNISupport_H
#define org_echocat_jogg_OggJNISupport_H

#include <jni.h>
#include <stdio.h>
#include <ogg/ogg.h>

#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>

#ifdef __cplusplus
extern "C" {
#endif

void Java_org_echocat_jogg_OggJNISupport_throwOutOfMemoryError
    (JNIEnv *env);

void Java_org_echocat_jogg_OggJNISupport_throwNullPointerException
    (JNIEnv *env);

int Java_org_echocat_jogg_OggJNISupport_checkNotNull(JNIEnv *env, void *value);

jbyteArray Java_org_echocat_jogg_OggJNISupport_bufferToJavaByteArray
    (JNIEnv *env, long length, void *buffer);

void Java_org_echocat_jogg_OggJNISupport_javaByteArrayToBuffer
    (JNIEnv *env, jbyteArray source, long *lengthDrain, void **bufferDrain);

#ifdef __cplusplus
}
#endif

#endif /* org_echocat_jogg_OggJNISupport_H */