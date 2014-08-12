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

public class OpusHeaderException extends OpusException {

    public OpusHeaderException() {
    }

    public OpusHeaderException(String message) {
        super(message);
    }

    public OpusHeaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpusHeaderException(Throwable cause) {
        super(cause);
    }

}
