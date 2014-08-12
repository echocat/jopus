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

import org.echocat.jogg.OggHandleBasedSupport;

public abstract class OpusHandleBasedSupport extends OggHandleBasedSupport {

    protected OpusHandleBasedSupport(long handle) {
        super(handle);
    }

    protected OpusHandleBasedSupport(long handle, boolean autoDestroy) {
        super(handle, autoDestroy);
    }

}
