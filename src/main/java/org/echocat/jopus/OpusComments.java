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

import org.echocat.jomon.runtime.CollectionUtils;
import org.echocat.jomon.runtime.iterators.ConvertingIterator;
import org.echocat.jomon.runtime.util.Entry;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.echocat.jogg.OggPacket.FromPacketConvertible;
import static org.echocat.jogg.OggPacket.ToPacketConvertible;
import static org.echocat.jomon.runtime.CollectionUtils.asImmutableSet;

public class OpusComments implements Iterable<Entry<String, String>>, ToPacketConvertible, FromPacketConvertible {

    protected static final byte[] HEAD = new byte[]{'O', 'p', 'u', 's', 'T', 'a', 'g', 's'};

    private final Map<String, String> _raw = new LinkedHashMap<>();

    public OpusComments(Map<String, String> source) {
        if (source != null) {
            for (final Map.Entry<String, String> entry : source.entrySet()) {
                add(entry.getKey(), entry.getValue());
            }
        }
    }

    public OpusComments(String... commentKeyAndValue) {
        this(CollectionUtils.<String, String>asMap(commentKeyAndValue));
    }

    public String find(String key) {
        return key != null ? _raw.get(key.toUpperCase()) : null;
    }

    public boolean contains(String key) {
        return key != null && _raw.containsKey(key.toUpperCase());
    }

    public void add(String key, String value) {
        if (key == null) {
            throw new NullPointerException("There is no key provided.");
        }
        _raw.put(key.toUpperCase(), value != null ? value : "");
    }

    public Set<String> keys() {
        return asImmutableSet(_raw.keySet());
    }

    @Override
    public Iterator<Entry<String, String>> iterator() {
        return new ConvertingIterator<Map.Entry<String, String>, Entry<String, String>>(_raw.entrySet().iterator()) {

            @Override
            protected Entry<String, String> convert(Map.Entry<String, String> input) {
                return new Entry.Impl<>(input.getKey(), input.getValue());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    @Override
    public byte[] toPacket() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(255);
        baos.write(HEAD);

        final String vendorString = "libopus 1.1";
        final byte[] vendor = vendorString.getBytes("UTF-8");
        baos.write(toBytes(vendor.length));
        baos.write(vendor);

        baos.write(toBytes(_raw.size()));

        for (final Map.Entry<String, String> property : _raw.entrySet()) {
            final String comment = property.getKey() + "=" + property.getValue();
            final byte[] commentInBytes = comment.getBytes("UTF-8");
            baos.write(toBytes(commentInBytes.length));
            baos.write(commentInBytes);
        }

        return baos.toByteArray();
    }

    @Override
    public void fromPacket(byte[] packet) throws IOException {
        final Position position = new Position();
        validateHead(packet, position);
        readString(packet, position, "vendor string.");
        final int numberOfComments = readInteger(packet, position, "Could not find the number of comments.");
        _raw.clear();
        for (int i = 0; i < numberOfComments; i++) {
            final String comment = readString(packet, position, "comment #" + i);
            final int fc = comment.indexOf('=');
            final String key = fc > 0 ? comment.substring(0, fc) : comment;
            final String value = fc > 0 && fc + 1 > comment.length() ? comment.substring(fc + 1) : "";
            _raw.put(key.toUpperCase(), value);
        }
    }

    @Override
    public boolean isPossiblePacket(byte[] packet) throws IOException {
        boolean result;
        if (packet.length < HEAD.length) {
            result = false;
        } else {
            result = true;
            for (int i = 0; i < HEAD.length; i++) {
                if (HEAD[i] != packet[i]) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    protected static class Position {

        private int _value;

        public void add(int value) {
            _value += value;
        }

        public int getValue() {
            return _value;
        }

    }

    protected void validateHead(byte[] packet, Position position) throws EOFException {
        if (packet.length < HEAD.length) {
            throw new EOFException("Could not find the OpusTags marker.");
        }
        for (int i = 0; i < HEAD.length; i++) {
            if (HEAD[i] != packet[i]) {
                throw new EOFException("Could not find the OpusTags marker.");
            }
        }
        position.add(HEAD.length);
    }

    protected String readString(byte[] packet, Position position, String stringName) throws EOFException, UnsupportedEncodingException {
        final int length = readInteger(packet, position, "Unexpected end of stream: Could not find size marker for " + stringName + ".");
        final int offset = position.getValue();
        if (packet.length < (offset + length)) {
            throw new EOFException(stringName + " have to be a length of " + length + " bytes but there are not enough bytes available in input.");
        }
        position.add(length);
        return new String(packet, offset, length, "UTF-8");
    }

    protected byte[] toBytes(int val) {
        final byte[] b = new byte[4];
        b[0] = (byte) (val);
        b[1] = (byte) (val >>> 8);
        b[2] = (byte) (val >>> 16);
        b[3] = (byte) (val >>> 24);
        return b;
    }

    protected int readInteger(byte[] b, Position positiion, String exceptionMessage) throws EOFException {
        final int offset = positiion.getValue();
        if (b.length < offset + 4) {
            throw new EOFException(exceptionMessage);
        }
        positiion.add(4);
        return ((b[offset + 0] & 0xFF)) +
                ((b[offset + 1] & 0xFF) << 8) +
                ((b[offset + 2] & 0xFF) << 16) +
                ((b[offset + 3]) << 24);
    }

}

