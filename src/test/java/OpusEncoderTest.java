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
import org.echocat.jogg.OggSyncStateOutput;
import org.echocat.jomon.runtime.concurrent.StopWatch;
import org.echocat.jopus.OpusComments;
import org.echocat.jopus.OpusEncoder;
import org.echocat.jopus.OpusHeader;
import org.junit.Test;

import java.io.*;

import static org.echocat.jogg.OggPacket.packetFor;
import static org.echocat.jopus.OpusEncoderJNI.encode;

/**
 * Created by christian.rijke on 14.08.2014.
 */
public class OpusEncoderTest {

    @Test
    public void encodeRawFileToOpusFile() throws IOException {
        final OpusEncoder encoder = new OpusEncoder();
        final int numberOfFrames = 2880;
        final int numberOfChannels = encoder.getNumberOfChannels();

        final OpusHeader header = createHeaderFor(encoder);

        try (final InputStream is = new FileInputStream("foo.raw");
             final DataInputStream in = new DataInputStream(is);
             final OutputStream out = new FileOutputStream("foo.opus");
             final OggSyncStateOutput sso = new OggSyncStateOutput(out)) {
            sso.write(packetFor(header)
                    .packetno(0)
                    .bos(true)
            );

            sso.write(packetFor(new OpusComments(
                    "ALBUM", "Foo",
                    "ARTIST", "deadmau5",
                    "DATE", "2012",
                    "ENCODER", "opusenc from opus-tools 0.1.8",
                    "GENRE", "Electronic",
                    "TITLE", "Strobe",
                    "TRACKNUMBER", "1"
                ))
                    .bos(false)
                    .eos(false)
                    .packetno(1)
            );

            final StopWatch stopWatch = new StopWatch();
            final short[] pcm = new short[numberOfFrames * numberOfChannels];
            final byte[] buffer = new byte[pcm.length * 2];
            OggPacket packet = null;
            try {
                //noinspection InfiniteLoopStatement
                for (; ; ) {
                    in.readFully(buffer);
                    for (int i = 0; i < pcm.length; i++) {
                        pcm[i] = (short) ((buffer[i * 2] & 0xff) | (buffer[i * 2 + 1] << 8));
                    }
                    packet = encodeAndWrite(encoder, numberOfFrames, pcm, packet);
                    sso.write(packet);
                }
            } catch (final EOFException ignored) {
            }

            //noinspection UseOfSystemOutOrSystemErr
            System.out.println("Tooks " + stopWatch.getCurrentDuration() + ".");
        }
    }

    private OggPacket encodeAndWrite(OpusEncoder encoder, int frameSize, short[] pcm, OggPacket lastPacket) throws IOException {
        final byte[] packet = new byte[pcm.length * 2];
        final int encoded = encode(encoder._handle, pcm, frameSize, packet, packet.length);

        return packetFor(packet, 0, encoded)
            .packetno(lastPacket != null ? lastPacket.getPacketno() + 1 : 2)
            .granulepos(lastPacket != null ? lastPacket.getGranulepos() + frameSize : frameSize);
    }


    protected OpusHeader createHeaderFor(OpusEncoder encoder) {
        final OpusHeader header = new OpusHeader();
        header.setGain(0);
        header.setVersion(1);
        header.setChannels(encoder.getNumberOfChannels());
        header.setInputSampleRate(encoder.getSamplingRate().handle());
        header.setNbStreams(1);
        header.setNbCoupled(1);
        header.setChannelMapping(0);
        header.setPreskip(0);
        return header;
    }
}
