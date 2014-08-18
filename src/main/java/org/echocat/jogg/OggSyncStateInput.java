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

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import static org.echocat.jogg.OggSyncStateJNI.*;

public class OggSyncStateInput extends OggSyncStateSupport implements Iterator<OggPageInput> {

    private final InputStream _delegate;
    private final OggPageInput _pageInputState = new OggPageInput();

    private static final int BUFFER_READ_LENGTH = 4096;

    private boolean _hasNext;

    private IOException _lastException;

    public OggSyncStateInput(InputStream delegate) {
        if (delegate == null) {
            throw new NullPointerException("No delegate provided.");
        }
        _delegate = delegate;
        _hasNext = true;
    }

    @Override
    public OggPageInput next() {
        assertNotDestroyed();

        OggPageInput result = null;

        while (result == null && hasNext()) {
            result = tryPageout();

            if (result == null) {
                final byte[] bitStreamBuffer = buffer(handle(), BUFFER_READ_LENGTH);
                int bitStreamRead;
                try {
                    bitStreamRead = _delegate.read(bitStreamBuffer);
                } catch (IOException e) {
                    bitStreamRead = 0;
                    _lastException = e;
                    _hasNext = false;
                }
                if (bitStreamRead >= 0) {
                    wrote(handle(), bitStreamBuffer, bitStreamRead);
                } else {
                    _hasNext = false;
                }
            }
        }

        if (result.isEos()) {
            _hasNext = false;
        }

        return result;
    }

    @Override
    public boolean hasNext() {
        return _hasNext;
    }

    public IOException lastException() {
        return _lastException;
    }

    private OggPageInput tryPageout() {
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
        return super.getAdditionalToStringInformation() + ", delegate=" + _delegate + (hasNext() ? "" : ", eofReached") + ", ";
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
