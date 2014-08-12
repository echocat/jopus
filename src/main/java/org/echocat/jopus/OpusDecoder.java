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
import org.echocat.jomon.runtime.concurrent.StopWatch;

import java.io.*;

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

    private final long _handle;
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

    public static void main(String[] args) throws Exception {
        final OpusDecoder decoder = new OpusDecoder();
        final int numberOfFrames = 2880;

        OpusHeader header = null;
        OpusComments comments = null;

        final StopWatch stopWatch = new StopWatch();

        try (final InputStream is = new FileInputStream("foo.opus")) {
            try (final OutputStream out = new FileOutputStream("foo2.raw")) {
                try (final OggSyncStateInput ssi = new OggSyncStateInput(is)) {
                    while (!ssi.isEofReached()) {
                        final OggPageInput pageInput = ssi.read(4096);
                        while (pageInput != null && pageInput.hasNext()) {
                            final OggPacket packet = pageInput.next();
                            if (header == null) {
                                if (packet.getPacketno() != 0) {
                                    throw new IOException("Illegal packet number. Expected was #0 but got #" + packet.getPacketno() + ".");
                                }
                                header = new OpusHeader();
                                header.fromPacket(packet.getBuffer());
                                decoder.setNumberOfChannels(header.getChannels());
                                decoder.setSamplingRate(SamplingRate.samplingRateFor(header.getInputSampleRate()));
                            } else if (comments == null && packet.getPacketno() == 1) {
                                comments = new OpusComments();
                                comments.fromPacket(packet.getBuffer());
                            } else {
                                decodeAndWrite(packet, decoder, numberOfFrames, header, out);
                            }
                        }
                    }
                }
            }
        }
        //noinspection UseOfSystemOutOrSystemErr
        System.out.println("Tooks " + stopWatch.getCurrentDuration() + ".");
    }

    private static void decodeAndWrite(OggPacket packet, OpusDecoder decoder, int numberOfFrames, OpusHeader header, OutputStream out) throws IOException {
        final byte[] buffer = packet.getBuffer();
        final short[] pcm = new short[numberOfFrames * decoder.getNumberOfChannels()];
        final int pcmLength = OpusDecoderJNI.decode(decoder._handle, buffer, buffer.length, pcm, numberOfFrames, 0);

        final byte[] outBuffer = new byte[pcmLength * 2];
        for (int i=0; i< pcmLength; i++) {
            final short val = pcm[i];
            outBuffer[(i * 2)] = (byte) (val);
            outBuffer[(i * 2) + 1] = (byte) (val >>> 8);
        }
        out.write(outBuffer);
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
