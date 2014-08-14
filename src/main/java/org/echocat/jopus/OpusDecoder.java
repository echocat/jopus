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

import static org.echocat.jopus.Bandwidth.bandwidthFor;
import static org.echocat.jopus.OpusDecoderJNI.*;
import static org.echocat.jopus.SamplingRate.kHz48;

public class OpusDecoder {

    private static final int MAXIMUM_NUMBER_OF_SUPPORTED_CHANNELS = 255;
    private static final int MINIMUM_GAIN = -32768;
    private static final int MAXIMUM_GAIN = 32767;

    public static int getMaximumNumberOfSupportedChannels() {
        return MAXIMUM_NUMBER_OF_SUPPORTED_CHANNELS;
    }

    public static int getMinimumGain() {
        return MINIMUM_GAIN;
    }

    public static int getMaximumGain() {
        return MAXIMUM_GAIN;
    }

    // TODO this is just temporarily public
    public final long _handle;
    private SamplingRate _samplingRate;
    private int _numberOfChannels;

    public OpusDecoder() {
        this(kHz48, 2);
    }

    public OpusDecoder(SamplingRate samplingRate, int numberOfChannels) {
        validateSamplingRate(samplingRate);
        validateNumberOfChannels(numberOfChannels);

        _samplingRate = samplingRate;
        _numberOfChannels = numberOfChannels;
        _handle = create(samplingRate.handle(), numberOfChannels);
    }

    protected void reinitialize() {
        synchronized (this) {
            init(_handle, _samplingRate.handle(), _numberOfChannels);
        }
    }

    public SamplingRate getSamplingRate() {
        return _samplingRate;
    }

    protected void validateSamplingRate(SamplingRate value) {
        if (value == null) {
            throw new IllegalArgumentException("The sampling rate cannot be null.");
        }
    }

    public void setSamplingRate(SamplingRate samplingRate) {
        validateSamplingRate(samplingRate);
        _samplingRate = samplingRate;
        reinitialize();
    }

    public int getNumberOfChannels() {
        return _numberOfChannels;
    }

    protected void validateNumberOfChannels(int numberOfChannels) {
        final int max = getMaximumNumberOfSupportedChannels();
        if (numberOfChannels < 1 || numberOfChannels > max) {
            throw new IllegalArgumentException(numberOfChannels + " is an illegal value. Use one between 1 and " + max + ".");
        }
    }

    public void setNumberOfChannels(int numberOfChannels) {
        _numberOfChannels = numberOfChannels;
        reinitialize();
    }

    public Bandwidth getBandwidth() {
        return bandwidthFor(OpusDecoderJNI.getBandwidth(_handle));
    }

    public int getLookAhead() {
        return OpusDecoderJNI.getLookAhead(_handle);
    }

    public int getFinalRange() {
        return OpusDecoderJNI.getFinalRange(_handle);
    }

    public int getLastPacketDuration() {
        return OpusDecoderJNI.getLastPacketDuration(_handle);
    }

    public int getPitch() {
        return OpusDecoderJNI.getPitch(_handle);
    }

    public int getGain() {
        return OpusDecoderJNI.getGain(_handle);
    }

    protected void validateGain(int value) {
        final int min = getMinimumGain();
        final int max = getMaximumGain();
        if (value < min || value > max) {
            throw new IllegalArgumentException(value + " is an illegal value. Use one between " + min + " and " + max + ".");
        }
    }

    public void setGain(int value) {
        validateGain(value);
        OpusDecoderJNI.setGain(_handle, value);
    }

    @Override
    public String toString() {
        return "OpusDecoder{samplingRate: " + getSamplingRate() + ", channels=" + getNumberOfChannels() + "}";
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            destroy(_handle);
        } finally {
            super.finalize();
        }
    }

}
