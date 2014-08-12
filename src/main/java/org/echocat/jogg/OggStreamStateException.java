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

public class OggStreamStateException extends OggException {

    public OggStreamStateException() {
    }

    public OggStreamStateException(String message) {
        super(message);
    }

    public OggStreamStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public OggStreamStateException(Throwable cause) {
        super(cause);
    }

}
