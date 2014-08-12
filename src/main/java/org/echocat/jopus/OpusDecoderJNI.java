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

final class OpusDecoderJNI extends OpusJNISupport {

    private OpusDecoderJNI() {
    }

    static native long create(int samplingRateInHz, int numberOfChannels);

    static native void init(long decoderHandle, int samplingRateInHz, int numberOfChannels);

    static native int decode(long decoderHandle, byte[] packet, int packetLength, short[] pcm, int frameSize, int decodeFec);

    static native int decodeFloat(long decoderHandle, byte[] packet, int packetLength, float[] pcm, int frameSize, int decodeFec);

    static native void destroy(long decoderHandle);

    static native int getNumberOfSamplesOfPacket(long decoderHandle, byte[] packet);

    static native int getSampleRate(long decoderHandle);

    static native int getLookAhead(long decoderHandle);

    static native int getLastPacketDuration(long decoderHandle);

    static native int getFinalRange(long decoderHandle);

    static native int getPitch(long decoderHandle);

    static native int getBandwidth(long decoderHandle);

    static native void setGain(long decoderHandle, int value);

    static native int getGain(long decoderHandle);

}
