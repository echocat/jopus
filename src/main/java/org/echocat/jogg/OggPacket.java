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

public class OggPacket extends OggHandleBasedSupport {

    public static OggPacket packetFor(ToPacketConvertible bufferFrom) throws IOException {
        return new OggPacket()
                .bufferFrom(bufferFrom);
    }

    public static OggPacket packetFor(byte[] buffer) {
        return new OggPacket()
                .buffer(buffer);
    }

    public static OggPacket packetFor(byte[] buffer, int offset, int length) {
        return new OggPacket()
                .buffer(buffer, offset, length);
    }

    OggPacket(long handle, boolean autoDestroy) {
        super(handle, autoDestroy);
    }

    public OggPacket() {
        this(OggPacketJNI.create(), true);
    }

    public void setBufferFrom(ToPacketConvertible toPacketConvertible) throws IOException {
        setBuffer(toPacketConvertible.toPacket());
    }

    public OggPacket bufferFrom(ToPacketConvertible toPacketConvertible) throws IOException {
        setBufferFrom(toPacketConvertible);
        return this;
    }

    public void setBuffer(byte[] value) {
        assertNotDestroyed();
        OggPacketJNI.setBuffer(handle(), value);
    }

    public void setBuffer(byte[] value, int offset, int length) {
        final byte[] buffer = new byte[length];
        System.arraycopy(value, offset, buffer, 0, length);
        setBuffer(buffer);
    }

    public OggPacket buffer(byte[] value) {
        setBuffer(value);
        return this;
    }

    public OggPacket buffer(byte[] value, int offset, int length) {
        setBuffer(value, offset, length);
        return this;
    }

    public byte[] getBuffer() {
        assertNotDestroyed();
        return OggPacketJNI.getBuffer(handle());
    }

    public void setBos(boolean value) {
        assertNotDestroyed();
        OggPacketJNI.setBos(handle(), value);
    }

    public OggPacket bos(boolean value) {
        setBos(value);
        return this;
    }

    public boolean isBos() {
        assertNotDestroyed();
        return OggPacketJNI.getBos(handle());
    }

    public void setEos(boolean value) {
        assertNotDestroyed();
        OggPacketJNI.setEos(handle(), value);
    }

    public OggPacket eos(boolean value) {
        setEos(value);
        return this;
    }

    public boolean isEos() {
        assertNotDestroyed();
        return OggPacketJNI.getEos(handle());
    }

    public void setGranulepos(long value) {
        assertNotDestroyed();
        OggPacketJNI.setGranulepos(handle(), value);
    }

    public OggPacket granulepos(long value) {
        setGranulepos(value);
        return this;
    }

    public long getGranulepos() {
        assertNotDestroyed();
        return OggPacketJNI.getGranulepos(handle());
    }

    public void setPacketno(long value) {
        assertNotDestroyed();
        OggPacketJNI.setPacketno(handle(), value);
    }

    public OggPacket packetno(long value) {
        setPacketno(value);
        return this;
    }

    public long getPacketno() {
        assertNotDestroyed();
        return OggPacketJNI.getPacketno(handle());
    }

    @Override
    protected String getAdditionalToStringInformation() {
        return "size=" + getBufferLength() + (isBos() ? ", bos"  : "") + (isEos() ? ", eos"  : "") + ", granulepos=" + getGranulepos() + ", packetno=" + getPacketno() + ", ";
    }

    private int getBufferLength() {
        final byte[] buffer = getBuffer();
        return buffer != null ? buffer.length : 0;
    }

    @Override
    protected void destroyHandle(long handle) {
        OggPacketJNI.destroy(handle);
    }

    public static interface ToPacketConvertible {

        public byte[] toPacket() throws IOException;

    }

    public static interface FromPacketConvertible {

        public void fromPacket(byte[] packet) throws IOException;

        public boolean isPossiblePacket(byte[] packet) throws IOException;

    }

}
