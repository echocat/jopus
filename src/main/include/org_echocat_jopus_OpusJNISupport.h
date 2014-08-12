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

#ifndef org_echocat_jopus_OpusJNISupport_H
#define org_echocat_jopus_OpusJNISupport_H

#include "org_echocat_jogg_OggJNISupport.h"
#include <opus.h>

#ifdef __cplusplus
extern "C" {
#endif

void Java_org_echocat_jogg_OggJNISupport_checkResponse
    (JNIEnv *env, int result);

#ifdef __cplusplus
}
#endif

#endif /* org_echocat_jopus_OpusJNISupport_H */