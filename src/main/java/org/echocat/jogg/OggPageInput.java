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

import java.util.Iterator;
import java.util.NoSuchElementException;

public class OggPageInput extends OggPageSupport implements Iterator<OggPacket> {

    private OggPacket _next;
    private Boolean _hasNext;

    protected OggPageInput() {
    }

    protected void init() {
        syncSerialno();
        OggStreamStateJNI.pagein(streamStateHandle(), handle());
        _hasNext = null;
        _next = null;
    }

    @Override
    public boolean hasNext() {
        if (_hasNext == null) {
            _next = read();
            _hasNext = _next != null;
        }
        return _hasNext;
    }


    @Override
    public OggPacket next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        _hasNext = null;
        return _next;
    }

    protected OggPacket read() {
        final OggPacket packet = new OggPacket();
        return OggStreamStateJNI.packetout(streamStateHandle(), packet.handle()) ? packet : null;
    }

    protected void syncSerialno() {
        final long mySerialno = getSerialno();
        final long streamStateSerialno = OggStreamStateJNI.getSerialno(streamStateHandle());
        if (streamStateSerialno != mySerialno) {
            OggStreamStateJNI.resetSerialno(streamStateHandle(), mySerialno);
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
