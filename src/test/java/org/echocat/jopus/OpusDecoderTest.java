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
import org.junit.Test;

import java.io.*;


/**
 * Created by christian.rijke on 14.08.2014.
 */
public class OpusDecoderTest {

    @Test
    public void decodeOpusFileToRawFile() throws IOException {
        try (final InputStream is = new FileInputStream("foo.opus");
             final OggSyncStateInput ssi = new OggSyncStateInput(is);
             final OpusDecoder od = new OpusDecoder(ssi);
             final OutputStream out = new FileOutputStream("foo2.raw")) {
            while (!od.isEofReached()) {
                final byte[] read = od.read();
                if (read != null) {
                    out.write(read);
                }
            }
        }
    }
}
