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

public enum Bandwidth {
    /**
     * 4 kHz bandpass
     */
    narrowBand(BANDWIDTH_NARROWBAND),
    /**
     * 6 kHz bandpass
     */
    mediumBand(BANDWIDTH_MEDIUMBAND),
    /**
     * 8 kHz bandpass
     */
    wideBand(BANDWIDTH_WIDEBAND),
    /**
     * <12 kHz bandpass
     */
    superWideBand(BANDWIDTH_SUPERWIDEBAND),
    /**
     * <20 kHz bandpass
     */
    fullBand(BANDWIDTH_FULLBAND),
    ;

    private final int _handle;

    Bandwidth(int handle) {
        _handle = handle;
    }

    public static Bandwidth bandwidthFor(int code) {
        Bandwidth result = null;
        for (final Bandwidth candidate : values()) {
            if (candidate.handle() == code) {
                result = candidate;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Could not handle bandwidth with code: " + code);
        }
        return result;
    }

    public int handle() {
        return _handle;
    }

}

