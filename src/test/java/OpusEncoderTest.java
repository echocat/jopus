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

import org.echocat.jogg.OggSyncStateOutput;
import org.echocat.jopus.OpusComments;
import org.echocat.jopus.OpusEncoder;
import org.junit.Test;

import java.io.*;

/**
 * Created by christian.rijke on 14.08.2014.
 */
public class OpusEncoderTest {

    @Test
    public void encodeRawFileToOpusFile() throws IOException {
        final int numberOfFrames = 2880;

        try (final InputStream is = new FileInputStream("foo.raw");
             final DataInputStream in = new DataInputStream(is);
             final OutputStream out = new FileOutputStream("foo.opus");
             final OggSyncStateOutput sso = new OggSyncStateOutput(out);
             final OpusEncoder oe = new OpusEncoder(sso)) {

            OpusComments opusComments = new OpusComments(
                "ALBUM", "Foo",
                "ARTIST", "deadmau5",
                "DATE", "2012",
                "ENCODER", "opusenc from opus-tools 0.1.8",
                "GENRE", "Electronic",
                "TITLE", "Strobe",
                "TRACKNUMBER", "1"
            );
            oe.setComments(opusComments);

            final short[] pcm = new short[numberOfFrames * oe.getNumberOfChannels()];
            final byte[] buffer = new byte[pcm.length * 2];
            try {
                //noinspection InfiniteLoopStatement
                for (; ; ) {
                    in.readFully(buffer);
                    for (int i = 0; i < pcm.length; i++) {
                        pcm[i] = (short) ((buffer[i * 2] & 0xff) | (buffer[i * 2 + 1] << 8));
                    }
                    oe.write(numberOfFrames, pcm);
                }
            } catch (final EOFException ignored) {
            }
        }
    }
}
