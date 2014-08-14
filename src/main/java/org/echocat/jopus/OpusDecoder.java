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

import org.echocat.jogg.OggPacket;
import org.echocat.jogg.OggPageInput;
import org.echocat.jogg.OggSyncStateInput;

import java.io.Closeable;
import java.io.IOException;

import static org.echocat.jopus.Bandwidth.bandwidthFor;
import static org.echocat.jopus.OpusDecoderJNI.*;
import static org.echocat.jopus.SamplingRate.kHz48;

public class OpusDecoder implements Closeable {

    private static final int MAXIMUM_NUMBER_OF_SUPPORTED_CHANNELS = 255;
    private static final int MINIMUM_GAIN = -32768;
    private static final int MAXIMUM_GAIN = 32767;

    private static final int NUMBER_OF_FRAMES = 2880;
    private static final int BUFFER_LENGTH = 4096;

    public static int getMaximumNumberOfSupportedChannels() {
        return MAXIMUM_NUMBER_OF_SUPPORTED_CHANNELS;
    }

    public static int getMinimumGain() {
        return MINIMUM_GAIN;
    }

    public static int getMaximumGain() {
        return MAXIMUM_GAIN;
    }

    private final long _handle;
    private SamplingRate _samplingRate;
    private int _numberOfChannels;
    private OggSyncStateInput _ssi;
    private boolean _metadataProcessed;
    private byte[] buf;

    public OpusDecoder(OggSyncStateInput ssi) {
        this(kHz48, 2, ssi);
    }

    public OpusDecoder(SamplingRate samplingRate, int numberOfChannels, OggSyncStateInput ssi) {
        validateSamplingRate(samplingRate);
        validateNumberOfChannels(numberOfChannels);

        _samplingRate = samplingRate;
        _numberOfChannels = numberOfChannels;
        _handle = create(samplingRate.handle(), numberOfChannels);
        _ssi = ssi;
        _metadataProcessed = false;
    }

    public byte[] read() throws IOException {
        if (!_metadataProcessed) {
            processMetadata();
        }

        // return buffered data;
        byte[] result = buf;

        // store next packet in internal buffer
        if (!isEofReached()) {
            OggPacket nextPacket = getNextPacket();
            if (nextPacket != null) {
                buf = decode(nextPacket);
            } else {
                buf = null;
            }
        }
        return result;
    }

    private OggPacket getNextPacket() throws IOException {
        OggPacket packet = null;
        if (!_ssi.isEofReached()) {
            final OggPageInput pageInput = _ssi.read(BUFFER_LENGTH);

            if (pageInput != null && pageInput.hasNext()) {
                packet = pageInput.next();
            }
        }
        return packet;
    }

    private void processMetadata() throws IOException {
        OggPacket packet = getNextPacket();
        assertPacketIndex(packet, 0);

        OpusHeader header = new OpusHeader();
        header.fromPacket(packet.getBuffer());
        setNumberOfChannels(header.getChannels());
        setSamplingRate(SamplingRate.samplingRateFor(header.getInputSampleRate()));

        packet = getNextPacket();
        assertPacketIndex(packet, 1);

        OpusComments comments = new OpusComments();
        comments.fromPacket(packet.getBuffer());

        buf = getNextPacket().getBuffer();

        _metadataProcessed = true;
    }

    private void assertPacketIndex(OggPacket packet, int requiredPacketIndex) throws IOException {
        if (packet.getPacketno() != requiredPacketIndex) {
            throw new IOException("Illegal packet number. Expected was #" + requiredPacketIndex + " but got #" + packet.getPacketno() + ".");
        }
    }

    private byte[] decode(OggPacket packet) throws IOException {
        final byte[] buffer = packet.getBuffer();
        final short[] pcm = new short[NUMBER_OF_FRAMES * getNumberOfChannels()];
        final int pcmLength = OpusDecoderJNI.decode(_handle, buffer, buffer.length, pcm, NUMBER_OF_FRAMES, 0);

        final byte[] outBuffer = new byte[pcmLength * 2];
        for (int i = 0; i < pcmLength; i++) {
            final short val = pcm[i];
            outBuffer[(i * 2)] = (byte) (val);
            outBuffer[(i * 2) + 1] = (byte) (val >>> 8);
        }
        return outBuffer;
    }

    private void reinitialize() {
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

    @Override
    public void close() throws IOException {
        // TODO OggHandleBlaSupport?
    }

    public boolean isEofReached() {
        return _ssi.isEofReached() && buf == null;
    }
}
