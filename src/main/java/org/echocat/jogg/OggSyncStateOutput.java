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

package org.echocat.jogg;

import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

public class OggSyncStateOutput extends OggSyncStateSupport implements Flushable {

    private final OutputStream _delegate;
    private final OggPageOutput _pageOutput = new OggPageOutput();

    public OggSyncStateOutput(OutputStream delegate) {
        if (delegate == null) {
            throw new NullPointerException("No delegate provided.");
        }
        _delegate = delegate;
    }

    public void write(OggPacket packet) throws IOException {
        _pageOutput.add(packet);
        write(_pageOutput);
    }

    public void write(OggPageOutput pageOutput) throws IOException {
        while (OggStreamStateJNI.flush(pageOutput.streamStateHandle(), pageOutput.handle()) > 0) {
            write(pageOutput, _delegate);
        }
    }

    protected long write(OggPageOutput pageOutput, OutputStream to) throws IOException {
        final byte[] header = pageOutput.getHeader();
        final byte[] body = pageOutput.getBody();
        to.write(header);
        to.write(body);
        return header.length + body.length;
    }

    @Override
    protected String getAdditionalToStringInformation() {
        return super.getAdditionalToStringInformation() + ", delegate=" + _delegate + ", ";
    }

    @Override
    public void close() throws IOException {
        try {
            _delegate.close();
        } finally {
            super.close();
        }
    }

    @Override
    public void flush() throws IOException {
        _delegate.flush();
    }

}
