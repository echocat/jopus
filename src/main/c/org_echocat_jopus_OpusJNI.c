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

JNIEXPORT jstring JNICALL Java_org_echocat_jopus_OpusJNI_getVersionString
    (JNIEnv *env, jclass thisClass) {

    const char* versionString = opus_get_version_string();
    return (*env)->NewStringUTF(env, versionString);
}
