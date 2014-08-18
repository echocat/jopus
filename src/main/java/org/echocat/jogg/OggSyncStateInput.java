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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import static org.echocat.jogg.OggSyncStateJNI.*;

public class OggSyncStateInput extends OggSyncStateSupport {

    private final InputStream _delegate;
    private final OggPageInput _pageInputState = new OggPageInput();

    private static final int BUFFER_READ_LENGTH = 4096;

    private boolean _eof;

    public OggSyncStateInput(InputStream delegate) {
        if (delegate == null) {
            throw new NullPointerException("No delegate provided.");
        }
        _delegate = delegate;
    }

    public OggPageInput read() throws IOException {
        assertNotDestroyed();
        assertNotEof();

        OggPageInput result = null;

        while (result == null || isEofReached()) {
            result = tryPageout();

            if (result == null) {
                final byte[] bitStreamBuffer = buffer(handle(), BUFFER_READ_LENGTH);
                final int bitStreamRead = _delegate.read(bitStreamBuffer);
                if (bitStreamRead >= 0) {
                    wrote(handle(), bitStreamBuffer, bitStreamRead);
                } else {
                    _eof = true;
                }
            }
        }

        return result;
    }

    protected void assertNotEof() throws EOFException {
        if (_eof) {
            throw new EOFException();
        }
    }

    public boolean isEofReached() {
        return _eof;
    }

    protected OggPageInput tryPageout() {
        final OggPageInput result;
        if (pageout(handle(), _pageInputState.handle())) {
            result = _pageInputState;
            result.init();
        } else {
            result = null;
        }
        return result;
    }

    @Override
    protected String getAdditionalToStringInformation() {
        return super.getAdditionalToStringInformation() + ", delegate=" + _delegate + (isEofReached() ? ", eofReached" : "") + ", ";
    }

    @Override
    public void close() throws IOException {
        try {
            _delegate.close();
        } finally {
            super.close();
        }
    }

}
