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

public final class OpusUtilsJNI extends OpusJNISupport {

    private OpusUtilsJNI() {
    }

    static native String getErrorStringForErrorCode(int errorCode);

    static native int getBandwidthOfPacket(byte[] packet);

    static native int getSamplesPerFrameOfPacket(byte[] packet, int samplingRateInHz);

    static native int getNumberOfChannelsOfPacket(byte[] packet);

    static native int getNumberOfFramesOfPacket(byte[] packet);

    static native int getNumberOfSamplesOfPacket(byte[] packet, int samplingRateInHz);

    static native void applyPcmSoftClipping(float[] pcm, int frameSize, int numberOfChannels, float[] softClipping);

}
