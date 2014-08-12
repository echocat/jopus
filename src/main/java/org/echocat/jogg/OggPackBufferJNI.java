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

final class OggPackBufferJNI extends OggJNISupport {

    private OggPackBufferJNI() {
    }

    static native long create();

    static native void destroy(long handle);

    /* ==================================================================================================================*/

    /* ==================================================================================================================*/

    static native void writeinit(long handle);

    static native boolean writecheck(long handle);

    static native void writetrunc(long handle, long bits);

    static native void writecopy(long handle, byte[] source);

    static native void writeclear(long handle);

    static native void readinit(long handle, byte[] source);

    static native void write(long handle, long value, int bits);

    static native long look(long handle, int bits);

    static native long look1(long handle);

    static native void adv(long handle, int bits);

    static native void adv1(long handle);

    static native long read(long handle, int bits);

    static native long read1(long handle);

    static native long bytes(long handle);

    static native long bits(long handle);


}
