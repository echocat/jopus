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

import org.echocat.jogg.OggPacket;
import org.echocat.jogg.OggPageInput;
import org.echocat.jogg.OggSyncStateInput;
import org.echocat.jomon.runtime.concurrent.StopWatch;
import org.echocat.jopus.*;
import org.junit.Test;

import java.io.*;

/**
 * Created by christian.rijke on 14.08.2014.
 */
public class OpusDecoderTest {

    @Test
    public void decodeOpusFileToRawFile() throws IOException {
        final OpusDecoder decoder = new OpusDecoder();
        final int numberOfFrames = 2880;

        OpusHeader header = null;
        OpusComments comments = null;

        final StopWatch stopWatch = new StopWatch();

        try (final InputStream is = new FileInputStream("foo.opus");
             final OggSyncStateInput ssi = new OggSyncStateInput(is);
             final OutputStream out = new FileOutputStream("foo2.raw")) {
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
}
