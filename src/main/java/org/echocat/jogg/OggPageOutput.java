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

public class OggPageOutput extends OggPageSupport {

    public static OggPageOutput pageOutputFor(OggPacket packet) {
        return new OggPageOutput()
                .add(packet);
    }

    public OggPageOutput add(OggPacket packet) {
        if (packet == null) {
            throw new NullPointerException("No packet provided.");
        }
        OggStreamStateJNI.packetin(streamStateHandle(), packet.handle());
        return this;
    }

}
