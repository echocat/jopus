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

final class OggStreamStateJNI extends OggJNISupport {

    private OggStreamStateJNI() {
    }

    static native long create(long serialno);

    static native void destroy(long handle);

    /* ==============================================================================================================*/

    static native long getSerialno(long handle);

    /* ==============================================================================================================*/

    static native void reset(long handle);

    static native void resetSerialno(long handle, long serialno);

    static native boolean check(long handle);

    static native boolean eos(long handle);

    static native void iovecin(long handle, long iovecHandle, int count, long eos, long granulepos);

    static native void pagein(long handle, long pageHandle);

    static native void pageout(long handle, long pageHandle);

    static native void pageoutFill(long handle, long pageHandle, int count);

    static native int flush(long handle, long pageHandle);

    static native int flushFill(long handle, long pageHandle, int count);

    static native void packetin(long handle, long packetHandle);

    static native boolean packetout(long handle, long packetHandle);

    static native void packetpeek(long handle, long packetHandle);


}
