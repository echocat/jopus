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

import org.echocat.jogg.OggSyncStateInput;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


/**
 * Created by christian.rijke on 14.08.2014.
 */
public class OpusDecoderTest {

    Logger LOG = LoggerFactory.getLogger(OpusDecoderTest.class);

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();


    @Test
    public void decodeMono() throws IOException {
        File result = decode("bach_48k_mono.opus", 1, SamplingRate.kHz48);
        assertThat(result.length(), is(1023360L));
    }

    @Test
    public void decodeStereo() throws IOException {
        File result = decode("bach_48k_stereo.opus", 2, SamplingRate.kHz48);
        // TODO fix decoding
       fail();
    }

    public File decode(String source, int channels, SamplingRate samplingRate) throws IOException {
        LOG.info("decoding {}...", source);

        File tempFile = tempFolder.newFile(source.substring(0, source.length() - 5) + ".raw");

        try (final InputStream is = getClass().getResourceAsStream(source);
             final OggSyncStateInput ssi = new OggSyncStateInput(is);
             final OpusDecoder od = new OpusDecoder(ssi);
             final FileOutputStream fos = new FileOutputStream(tempFile);
             final DataOutputStream dos = new DataOutputStream(fos)) {

            od.setSamplingRate(samplingRate);
            od.setNumberOfChannels(channels);

            while (!od.isEofReached()) {
                for (short i : od.read()) {
                    dos.writeShort(i);
                }
            }
        }
        LOG.info("decoding finished");
        return tempFile;
    }
}