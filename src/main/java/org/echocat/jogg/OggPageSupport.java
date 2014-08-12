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

public abstract class OggPageSupport extends OggHandleBasedSupport {

    private final long _streamStateHandle;

    OggPageSupport(long handle) {
        super(handle, true);
        _streamStateHandle = OggStreamStateJNI.create(getSerialno());
    }

    protected OggPageSupport() {
        this(OggPageJNI.create());
    }

    public byte[] getHeader() {
        assertNotDestroyed();
        return OggPageJNI.getHeader(handle());
    }

    public byte[] getBody() {
        assertNotDestroyed();
        return OggPageJNI.getBody(handle());
    }

    public int getVersion() {
        assertNotDestroyed();
        return OggPageJNI.version(handle());
    }

    public boolean isContinued() {
        assertNotDestroyed();
        return OggPageJNI.continued(handle());
    }

    public boolean isBos() {
        assertNotDestroyed();
        return OggPageJNI.bos(handle()) != 0;
    }

    public boolean isEos() {
        assertNotDestroyed();
        return OggPageJNI.eos(handle()) != 0;
    }

    public long getGranulepos() {
        assertNotDestroyed();
        return OggPageJNI.granulepos(handle());
    }

    public long getSerialno() {
        assertNotDestroyed();
        return OggPageJNI.serialno(handle());
    }

    public long getPageno() {
        assertNotDestroyed();
        return OggPageJNI.pageno(handle());
    }

    public int getNumberOfPackets() {
        assertNotDestroyed();
        return OggPageJNI.packets(handle());
    }

    protected long streamStateHandle() {
        return _streamStateHandle;
    }

    @Override
    protected String getAdditionalToStringInformation() {
        return "headerSize=" + getHeaderLength() + ", bodySize=" + getBodyLength() + ", serialno=" + getSerialno() + ", pageno=" + getPageno() + ", granulepos=" + getGranulepos() + ", numberOfPackets=" + getNumberOfPackets() + (isContinued() ? ", continued" : "") + (isBos() ? ", bos" : "") + (isEos() ? ", eos" : "") +  ", ";
    }

    private int getBodyLength() {
        final byte[] body = getBody();
        return body != null ? body.length : 0;
    }

    private int getHeaderLength() {
        final byte[] header = getHeader();
        return header != null ? header.length : 0;
    }

    @Override
    protected void destroyHandle(long handle) {
        try {
            OggSyncStateJNI.destroy(_streamStateHandle);
        } finally {
            OggPageJNI.destroy(handle);
        }
    }

}
