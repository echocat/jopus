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

final class OggPageJNI extends OggJNISupport {

    private OggPageJNI() {
    }

    static native long create();

    static native void destroy(long handle);

    /* ==============================================================================================================*/

    static native void setHeader(long handle, byte[] value);

    static native byte[] getHeader(long handle);

    static native void setBody(long handle, byte[] value);

    static native byte[] getBody(long handle);

    /* ==============================================================================================================*/

    static native void checksumSet(long handle);

    static native int version(long handle);

    static native boolean continued(long handle);

    static native int bos(long handle);

    static native int eos(long handle);

    static native long granulepos(long handle);

    static native long serialno(long handle);

    static native long pageno(long handle);

    static native int packets(long handle);

}
