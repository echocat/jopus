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

public enum SamplingRate {
    kHz8(8000, "8kHz"),
    kHz12(12000, "12kHz"),
    kHz16(16000, "16kHz"),
    kHz24(24000, "24kHz"),
    kHz48(48000, "48kHz"),
    ;

    private final int _handle;
    private final String _display;

    SamplingRate(int handle, String display) {
        _handle = handle;
        _display = display;
    }

    public static SamplingRate samplingRateFor(int samplingRateInHz) {
        SamplingRate result = null;
        for (final SamplingRate candidate : values()) {
            if (candidate.handle() == samplingRateInHz) {
                result = candidate;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Could not handle sampling rate in hz of " + samplingRateInHz + ".");
        }
        return result;
    }

    public int handle() {
        return _handle;
    }


    @Override
    public String toString() {
        return _display;
    }

}

