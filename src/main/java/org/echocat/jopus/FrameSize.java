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

public enum FrameSize {
    argument(FRAMESIZE_ARG, "argument"),
    ms2_5(FRAMESIZE_2_5_MS, "2.5ms"),
    ms5(FRAMESIZE_5_MS, "5ms"),
    ms10(FRAMESIZE_10_MS, "10ms"),
    ms20(FRAMESIZE_20_MS, "20ms"),
    ms40(FRAMESIZE_40_MS, "40ms"),
    ms60(FRAMESIZE_60_MS, "50ms"),
    variable(FRAMESIZE_VARIABLE, "variable"),
    ;

    private final int _handle;
    private final String _title;

    FrameSize(int handle, String title) {
        _handle = handle;
        _title = title;
    }

    public static FrameSize frameSizeFor(int code) {
        FrameSize result = null;
        for (final FrameSize candidate : values()) {
            if (candidate.handle() == code) {
                result = candidate;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Could not handle frameSize with code: " + code);
        }
        return result;
    }

    public int handle() {
        return _handle;
    }


    @Override
    public String toString() {
        return _title;
    }
}

