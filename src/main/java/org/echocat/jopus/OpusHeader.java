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

import org.echocat.jogg.OggPacket;

import java.io.IOException;

import static org.echocat.jogg.OggPacket.ToPacketConvertible;

public class OpusHeader extends OpusHandleBasedSupport implements ToPacketConvertible, OggPacket.FromPacketConvertible {

    OpusHeader(long handle, boolean autoDestroy) {
        super(handle, autoDestroy);
    }

    public OpusHeader() {
        this(OpusHeaderJNI.create(), true);
    }

    public OpusHeader(byte[] packet) {
        this(OpusHeaderJNI.create(), true);
        OpusHeaderJNI.readPacket(handle(), packet);
    }

    @Override
    public byte[] toPacket() {
        assertNotDestroyed();
        final byte[] buffer = new byte[100];
        final int length = OpusHeaderJNI.writePacket(handle(), buffer);
        final byte[] result = new byte[length];
        System.arraycopy(buffer, 0, result, 0, length);
        return result;
    }

    public void setVersion(int value) {
        assertNotDestroyed();
        OpusHeaderJNI.setVersion(handle(), value);
    }

    public int getVersion() {
        assertNotDestroyed();
        return OpusHeaderJNI.getVersion(handle());
    }

    public void setChannels(int value) {
        assertNotDestroyed();
        OpusHeaderJNI.setChannels(handle(), value);
    }

    public int getChannels() {
        assertNotDestroyed();
        return OpusHeaderJNI.getChannels(handle());
    }

    public void setPreskip(int value) {
        assertNotDestroyed();
        OpusHeaderJNI.setPreskip(handle(), value);
    }

    public int getPreskip() {
        assertNotDestroyed();
        return OpusHeaderJNI.getPreskip(handle());
    }

    public void setInputSampleRate(int value) {
        assertNotDestroyed();
        OpusHeaderJNI.setInputSampleRate(handle(), value);
    }

    public int getInputSampleRate() {
        assertNotDestroyed();
        return OpusHeaderJNI.getInputSampleRate(handle());
    }

    public void setGain(int value) {
        assertNotDestroyed();
        OpusHeaderJNI.setGain(handle(), value);
    }

    public int getGain() {
        assertNotDestroyed();
        return OpusHeaderJNI.getGain(handle());
    }

    public void setChannelMapping(int value) {
        assertNotDestroyed();
        OpusHeaderJNI.setChannelMapping(handle(), value);
    }

    public int getChannelMapping() {
        assertNotDestroyed();
        return OpusHeaderJNI.getChannelMapping(handle());
    }

    public void setNbStreams(int value) {
        assertNotDestroyed();
        OpusHeaderJNI.setNbStreams(handle(), value);
    }

    public int getNbStreams() {
        assertNotDestroyed();
        return OpusHeaderJNI.getNbStreams(handle());
    }

    public void setNbCoupled(int value) {
        assertNotDestroyed();
        OpusHeaderJNI.setNbCoupled(handle(), value);
    }

    public int getNbCoupled() {
        assertNotDestroyed();
        return OpusHeaderJNI.getNbCoupled(handle());
    }

    public void setStreamMap(byte[] value) {
        assertNotDestroyed();
        OpusHeaderJNI.setStreamMap(handle(), value);
    }

    public byte[] getStreamMap() {
        assertNotDestroyed();
        return OpusHeaderJNI.getStreamMap(handle());
    }

    @Override
    protected void destroyHandle(long handle) {
        OpusHeaderJNI.destroy(handle);
    }

    @Override
    public void fromPacket(byte[] packet) throws IOException {
        assertNotDestroyed();
        OpusHeaderJNI.readPacket(handle(), packet);
    }

    @Override
    public boolean isPossiblePacket(byte[] packet) throws IOException {
        return true;
    }
}
