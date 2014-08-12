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

import static org.echocat.jopus.OpusConstants.*;

public enum Signal {
    /**
     * Signal being encoded is voice
     */
    voice(SIGNAL_VOICE),
    /**
     * Signal being encoded is music
     */
    music(SIGNAL_MUSIC),

    ;

    private final int _handle;

    Signal(int handle) {
        _handle = handle;
    }

    public static Signal signalFor(int code) {
        Signal result = null;
        for (final Signal candidate : values()) {
            if (candidate.handle() == code) {
                result = candidate;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Could not handle signal with code: " + code);
        }
        return result;
    }

    public int handle() {
        return _handle;
    }

}

