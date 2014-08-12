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

import static org.echocat.jopus.OpusConstants.APPLICATION_AUDIO;
import static org.echocat.jopus.OpusConstants.APPLICATION_RESTRICTED_LOW_DELAY;
import static org.echocat.jopus.OpusConstants.APPLICATION_VOIP;

public enum Application {
    /**
     * Gives best quality at a given bitrate for voice signals. It enhances the  input signal by high-pass filtering and
     * emphasizing formants and harmonics. Optionally  it includes in-band forward error correction to protect against
     * packet loss. Use this mode for typical VoIP applications. Because of the enhancement, even at high bitrates the
     * output may sound different from the input.
     */
    voip(APPLICATION_VOIP),
    /**
     * Gives best quality at a given bitrate for most non-voice signals like music. Use this mode for music and mixed
     * (music/voice) content, broadcast, and applications requiring less than 15 ms of coding delay.
     */
    audio(APPLICATION_AUDIO),
    /**
     * Configures low-delay mode that disables the speech-optimized mode in exchange for slightly reduced delay.
     * This mode can only be set on an newly initialized or freshly reset encoder because it changes the codec delay.
     */
    restrictedLowDelay(APPLICATION_RESTRICTED_LOW_DELAY);

    private final int _handle;

    Application(int handle) {
        _handle = handle;
    }

    public static Application applicationFor(int code) {
        Application result = null;
        for (final Application candidate : values()) {
            if (candidate.handle() == code) {
                result = candidate;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Could not handle application with code: " + code);
        }
        return result;
    }

    public int handle() {
        return _handle;
    }

}

