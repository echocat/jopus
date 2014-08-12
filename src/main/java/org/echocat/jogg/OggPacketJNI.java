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

final class OggPacketJNI extends OggJNISupport {

    private OggPacketJNI() {
    }

    static native long create();

    static native void destroy(long packetHandle);

    static native void setBuffer(long packetHandle, byte[] value);

    static native byte[] getBuffer(long packetHandle);

    static native void setBos(long packetHandle, boolean value);

    static native boolean getBos(long packetHandle);

    static native void setEos(long packetHandle, boolean value);

    static native boolean getEos(long packetHandle);

    static native void setGranulepos(long packetHandle, long value);

    static native long getGranulepos(long packetHandle);

    static native void setPacketno(long packetHandle, long value);

    static native long getPacketno(long packetHandle);

}
