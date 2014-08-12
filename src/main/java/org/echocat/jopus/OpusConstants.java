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

import static org.echocat.jopus.OpusJNI.getVersionString;

public interface OpusConstants {

    public static final String VERSION = getVersionString();

    public static final int AUTO = -1000;
    public static final int BIT_RATE_MAX = -1;

    /**
     * Best for most VoIP/videoconference applications where listening quality and intelligibility matter most
     */
    public static final int APPLICATION_VOIP = 2048;

    /**
     * Best for broadcast/high-fidelity application where the decoded audio should be as close as possible to the input
     */
    public static final int APPLICATION_AUDIO = 2049;
    /**
     * Only use when lowest-achievable latency is what matters most. Voice-optimized modes cannot be used.
     */
    public static final int APPLICATION_RESTRICTED_LOW_DELAY = 2051;

    /**
     * Signal being encoded is voice
     */
    public static final int SIGNAL_VOICE = 3001;
    /**
     * Signal being encoded is music
     */
    public static final int SIGNAL_MUSIC = 3002;
    /**
     * 4 kHz bandpass
     */
    public static final int BANDWIDTH_NARROWBAND = 1101;
    /**
     * 6 kHz bandpass
     */
    public static final int BANDWIDTH_MEDIUMBAND = 1102;
    /**
     * 8 kHz bandpass
     */
    public static final int BANDWIDTH_WIDEBAND = 1103;
    /**
     * <12 kHz bandpass
     */
    public static final int BANDWIDTH_SUPERWIDEBAND = 1104;
    /**
     * <20 kHz bandpass
     */
    public static final int BANDWIDTH_FULLBAND = 1105;

    /**
     * Select frame size from the argument (default)
     */
    public static final int FRAMESIZE_ARG = 5000;
    /**
     * Use 2.5 ms frames
     */
    public static final int FRAMESIZE_2_5_MS = 5001;
    /**
     * Use 5 ms frames
     */
    public static final int FRAMESIZE_5_MS = 5002;
    /**
     * Use 10 ms frames
     */
    public static final int FRAMESIZE_10_MS = 5003;
    /**
     * Use 20 ms frames
     */
    public static final int FRAMESIZE_20_MS = 5004;
    /**
     * Use 40 ms frames
     */
    public static final int FRAMESIZE_40_MS = 5005;
    /**
     * Use 60 ms frames
     */
    public static final int FRAMESIZE_60_MS = 5006;
    /**
     * Optimize the frame size dynamically
     */
    public static final int FRAMESIZE_VARIABLE = 5010;

}
