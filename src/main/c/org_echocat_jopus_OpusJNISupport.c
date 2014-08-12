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

void Java_org_echocat_jogg_OggJNISupport_checkResponse
    (JNIEnv *env, int result) {
    if (result < 0) {
        jclass type = (*env)->FindClass(env, "org/echocat/jopus/OpusException");
        const char* errorString = opus_strerror(result);
        (*env)->ThrowNew(env, type, errorString);
    }
}
