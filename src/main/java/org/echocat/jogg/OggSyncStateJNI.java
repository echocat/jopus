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

package org.echocat.jogg;

final class OggSyncStateJNI extends OggJNISupport {

    private OggSyncStateJNI() {
    }

    static native long create();

    static native void destroy(long handle);

    /* ==============================================================================================================*/

    /* ==============================================================================================================*/

    static native byte[] buffer(long handle, long length);

    static native void wrote(long handle, byte[] buffer, long bytes);

    static native void reset(long handle);

    static native boolean check(long handle);

    static native boolean pageout(long handle, long pageHandle);

    static native int pageseek(long handle, long pageHandle);

}
