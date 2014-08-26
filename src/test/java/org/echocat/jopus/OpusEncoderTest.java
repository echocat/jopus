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

import org.echocat.jogg.OggSyncStateOutput;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by christian.rijke on 14.08.2014.
 */
public class OpusEncoderTest {

    Logger LOG = LoggerFactory.getLogger(OpusEncoderTest.class);

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void encodeMono() throws IOException {
        final OpusComments commments = new OpusComments(
            "ARTIST", "J.S. Bach",
            "GENRE", "Classical",
            "TRACKNUMBER", "1"
        );
        File result = encode("bach_48k_mono.wav", commments, 1, SamplingRate.kHz48);
        assertThat(result.length(), is(73898L));
    }

    @Test
    public void encodeStereo() throws IOException {
        final OpusComments commments = new OpusComments(
            "ARTIST", "J.S. Bach",
            "GENRE", "Classical",
            "TRACKNUMBER", "1"
        );
        File result = encode("bach_48k_stereo.wav", commments, 2, SamplingRate.kHz48);
        assertThat(result.length(), is(140784L));
    }

    public File encode(String source, OpusComments opusComments, int channels, SamplingRate samplingRate) throws IOException {
        LOG.info("encoding {}...", source);

        final int numberOfFrames = 2880;

        File tempFile = tempFolder.newFile(source.substring(0, source.length() - 4) + ".opus");

        try (final InputStream is = getClass().getResourceAsStream(source);
             final DataInputStream in = new DataInputStream(is);
             final OutputStream out = new FileOutputStream(tempFile);
             final OggSyncStateOutput sso = new OggSyncStateOutput(out);
             final OpusEncoder oe = new OpusEncoder(sso)) {

            oe.setComments(opusComments);
            oe.setNumberOfChannels(channels);
            oe.setSamplingRate(samplingRate);

            final short[] pcm = new short[numberOfFrames * oe.getNumberOfChannels()];
            final byte[] buffer = new byte[pcm.length * 2];
            int numberOfBytes;
            while ((numberOfBytes = in.read(buffer)) != -1) {
                int numberOfShorts = numberOfBytes / 2;
                for (int i = 0; i < numberOfShorts; i++) {
                    pcm[i] = (short) ((buffer[i * 2] & 0xff) | (buffer[i * 2 + 1] << 8));
                }
                oe.write(numberOfFrames, Arrays.copyOfRange(pcm, 0, numberOfShorts));
            }

        }
        LOG.info("encoding finished");
        return tempFile;
    }
}
