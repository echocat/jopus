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

final class OggIovecJNI extends OggJNISupport {

    private OggIovecJNI() {
    }

    static native long create();

    static native void destroy(long handle);

    static native void setBuffer(long handle, byte[] value);

    static native byte[] getBuffer(long handle);

}
