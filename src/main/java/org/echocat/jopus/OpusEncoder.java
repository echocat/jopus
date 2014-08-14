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

import static org.echocat.jopus.Application.audio;
import static org.echocat.jopus.Bandwidth.bandwidthFor;
import static org.echocat.jopus.FrameSize.frameSizeFor;
import static org.echocat.jopus.OpusConstants.AUTO;
import static org.echocat.jopus.OpusConstants.BIT_RATE_MAX;
import static org.echocat.jopus.OpusEncoderJNI.*;
import static org.echocat.jopus.SamplingRate.kHz48;
import static org.echocat.jopus.Signal.signalFor;
import static org.echocat.jopus.Vbr.vbrFor;
import static org.echocat.jopus.VbrConstraint.vbrConstraintFor;

public class OpusEncoder {

    private static final int MAXIMUM_NUMBER_OF_SUPPORTED_CHANNELS = 255;
    private static final int MAXIMUM_COMPLEXITY = 10;
    private static final int MINIMUM_BIT_RATE = 500;
    private static final int MAXIMUM_BIT_RATE = 512000;
    private static final int MAXIMUM_PACKET_LOSS_PERC = 100;
    private static final int MINIMUM_LSB_DEPTH = 8;
    private static final int MAXIMUM_LSB_DEPTH = 24;

    public static int getMaximumNumberOfSupportedChannels() {
        return MAXIMUM_NUMBER_OF_SUPPORTED_CHANNELS;
    }

    public static int getMaximumComplexity() {
        return MAXIMUM_COMPLEXITY;
    }

    public static int getMinimumBitRate() {
        return MINIMUM_BIT_RATE;
    }

    public static int getMaximumBitRate() {
        return MAXIMUM_BIT_RATE;
    }

    public static int getMaximumPacketLossPerc() {
        return MAXIMUM_PACKET_LOSS_PERC;
    }

    public static int getMinimumLsbDepth() {
        return MINIMUM_LSB_DEPTH;
    }

    public static int getMaximumLsbDepth() {
        return MAXIMUM_LSB_DEPTH;
    }

    // TODO this is just temporarily public
    public final long _handle;
    private SamplingRate _samplingRate;
    private int _numberOfChannels;
    private Application _application;

    public OpusEncoder() {
        this(kHz48, 2, audio);
    }

    public OpusEncoder(SamplingRate samplingRate, int numberOfChannels, Application application) {
        validateSamplingRate(samplingRate);
        validateNumberOfChannels(numberOfChannels);
        validateApplication(application);

        _samplingRate = samplingRate;
        _numberOfChannels = numberOfChannels;
        _application = application;
        _handle = create(samplingRate.handle(), numberOfChannels, application.handle());
    }

    protected void reinitialize() {
        synchronized (this) {
            init(_handle, _samplingRate.handle(), _numberOfChannels, _application.handle());
        }
    }

    public Application getApplication() {
        return _application;
    }

    protected void validateApplication(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("The application cannot be null.");
        }
    }

    public void setApplication(Application application) {
        validateApplication(application);
        _application = application;
        reinitialize();
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

    public int getComplexity() {
        return OpusEncoderJNI.getComplexity(_handle);
    }

    protected void validateComplexity(int value) {
        final int max = getMaximumComplexity();
        if (value < 0 || value > max) {
            throw new IllegalArgumentException(value + " is an illegal value. Use one between 0 and " + max + ".");
        }
    }

    public void setComplexity(int value) {
        validateComplexity(value);
        OpusEncoderJNI.setComplexity(_handle, value);
    }

    public int getBitRate() {
        return OpusEncoderJNI.getBitRate(_handle);
    }

    protected void validateBitRate(int value) {
        final int max = getMaximumBitRate();
        final int min = getMinimumBitRate();
        if (value != AUTO && value != BIT_RATE_MAX && (value < min || value > max)) {
            throw new IllegalArgumentException(value + " is an illegal value. Use one between " + min + " and " + max + "; or one of " + OpusConstants.class.getSimpleName() + ".AUTO or " + OpusConstants.class.getSimpleName() + ".BIT_RATE_MAX.");
        }
    }

    public void setBitRate(int value) {
        validateBitRate(value);
        OpusEncoderJNI.setBitRate(_handle, value);
    }

    public Vbr getVbr() {
        return vbrFor(OpusEncoderJNI.getVbr(_handle));
    }

    protected void validateVbr(Vbr value) {
        if (value == null) {
            throw new IllegalArgumentException("There is no vbr provided.");
        }
    }

    public void setVbr(Vbr value) {
        validateVbr(value);
        OpusEncoderJNI.setVbr(_handle, value.handle());
    }

    public VbrConstraint getVbrConstraint() {
        return vbrConstraintFor(OpusEncoderJNI.getVbrConstraint(_handle));
    }

    protected void validateVbrConstraint(VbrConstraint value) {
        if (value == null) {
            throw new IllegalArgumentException("There is no vbr constraint provided.");
        }
    }

    public void setVbrConstraint(VbrConstraint value) {
        validateVbrConstraint(value);
        OpusEncoderJNI.setVbrConstraint(_handle, value.handle());
    }

    public Integer getForceChannels() {
        final int result = OpusEncoderJNI.getBitRate(_handle);
        return result == AUTO ? null : result;
    }

    protected void validateForceChannels(Integer value) {
        final int max = getMaximumNumberOfSupportedChannels();
        if (value != null && (value < 1 || value > max)) {
            throw new IllegalArgumentException(value + " is an illegal value. Use one between " + 1 + " and " + max + "; or null for auto.");
        }
    }

    public void setForceChannels(Integer value) {
        validateBitRate(value);
        OpusEncoderJNI.setForceChannels(_handle, value == null ? AUTO : value);
    }

    public Bandwidth getMaximumBandwidth() {
        return bandwidthFor(OpusEncoderJNI.getMaxBandwidth(_handle));
    }

    protected void validateMaximumBandwidth(Bandwidth value) {
        validateBandwidth(value);
    }

    public void setMaximumBandwidth(Bandwidth value) {
        validateMaximumBandwidth(value);
        OpusEncoderJNI.setMaxBandwidth(_handle, value.handle());
    }

    public Bandwidth getBandwidth() {
        return bandwidthFor(OpusEncoderJNI.getBandwidth(_handle));
    }

    protected void validateBandwidth(Bandwidth value) {
        if (value == null) {
            throw new IllegalArgumentException("There is no bandwidth provided.");
        }
    }

    public void setBandwidth(Bandwidth value) {
        validateBandwidth(value);
        OpusEncoderJNI.setBandwidth(_handle, value.handle());
    }

    public Signal getSignal() {
        return signalFor(OpusEncoderJNI.getSignal(_handle));
    }

    protected void validateSignal(Signal value) {
        if (value == null) {
            throw new IllegalArgumentException("There is no signal provided.");
        }
    }

    public void setSignal(Signal value) {
        validateSignal(value);
        OpusEncoderJNI.setSignal(_handle, value.handle());
    }

    public int getLookAhead() {
        return OpusEncoderJNI.getLookAhead(_handle);
    }

    public void setUseInBandFec(boolean value) {
        OpusEncoderJNI.setInBandFec(_handle, value ? 1 : 0);
    }

    public boolean isUseInBandFec() {
        final int value = OpusEncoderJNI.getInBandFec(_handle);
        return value != 0;
    }

    public int getPacketLossPerc() {
        return OpusEncoderJNI.getPacketLossPerc(_handle);
    }

    protected void validatePacketLossPerc(int value) {
        final int max = getMaximumPacketLossPerc();
        if (value < 0 || value > max) {
            throw new IllegalArgumentException(value + " is an illegal value. Use one between 0 and " + max + ".");
        }
    }

    public void setPacketLossPerc(int value) {
        validatePacketLossPerc(value);
        OpusEncoderJNI.setPacketLossPerc(_handle, value);
    }

    public void setUseDtx(boolean value) {
        OpusEncoderJNI.setDtx(_handle, value ? 1 : 0);
    }

    public boolean isUseDtx() {
        final int value = OpusEncoderJNI.getDtx(_handle);
        return value != 0;
    }

    public int getLsbDepth() {
        return OpusEncoderJNI.getLsbDepth(_handle);
    }

    protected void validateLsbDepth(int value) {
        final int max = getMaximumLsbDepth();
        final int min = getMinimumLsbDepth();
        if (value < min || value > max) {
            throw new IllegalArgumentException(value + " is an illegal value. Use one between " + min + " and " + max + ".");
        }
    }

    public void setLsbDepth(int value) {
        validateLsbDepth(value);
        OpusEncoderJNI.setLsbDepth(_handle, value);
    }

    public FrameSize getExpertFrameDuration() {
        return frameSizeFor(OpusEncoderJNI.getExpertFrameDuration(_handle));
    }

    protected void validateExpertFrameDuration(FrameSize value) {
        if (value == null) {
            throw new IllegalArgumentException("There is no frame size provided.");
        }
    }

    public void setExpertFrameDuration(FrameSize value) {
        validateExpertFrameDuration(value);
        OpusEncoderJNI.setExpertFrameDuration(_handle, value.handle());
    }

    public void setUsePrediction(boolean value) {
        OpusEncoderJNI.setPredictionDisabled(_handle, value ? 0 : 1);
    }

    public boolean isUsePrediction() {
        final int value = OpusEncoderJNI.getPredictionDisabled(_handle);
        return value == 0;
    }

    public int getFinalRange() {
        return OpusEncoderJNI.getFinalRange(_handle);
    }

    @Override
    public String toString() {
        return "OpusEncoder{samplingRate: " + getSamplingRate() + ", channels=" + getNumberOfChannels() + ", application=" + getApplication() + "}";
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
