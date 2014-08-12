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

package org.echocat.jopus;

final class OpusHeaderJNI extends OpusJNISupport {

    private OpusHeaderJNI() {
    }

    static native long create();

    static native void destroy(long handle);

    static native int writePacket(long handle, byte[] buffer);

    static native void readPacket(long handle, byte[] buffer);

    static native void setVersion(long handle, int value);

    static native int getVersion(long handle);

    static native void setChannels(long handle, int value);

    static native int getChannels(long handle);

    static native void setPreskip(long handle, int value);

    static native int getPreskip(long handle);

    static native void setInputSampleRate(long handle, int value);

    static native int getInputSampleRate(long handle);

    static native void setGain(long handle, int value);

    static native int getGain(long handle);

    static native void setChannelMapping(long handle, int value);

    static native int getChannelMapping(long handle);

    static native void setNbStreams(long handle, int value);

    static native int getNbStreams(long handle);

    static native void setNbCoupled(long handle, int value);

    static native int getNbCoupled(long handle);

    static native void setStreamMap(long handle, byte[] value);

    static native byte[] getStreamMap(long handle);

}
