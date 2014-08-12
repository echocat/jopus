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

import java.io.Closeable;
import java.io.IOException;

public abstract class OggHandleBasedSupport implements Closeable {

    private final long _handle;
    private final boolean _autoDestroy;

    private volatile boolean _destroyed;

    protected OggHandleBasedSupport(long handle) {
        this(handle, true);
    }

    protected OggHandleBasedSupport(long handle, boolean autoDestroy) {
        _handle = handle;
        _autoDestroy = autoDestroy;
    }

    protected long handle() {
        return _handle;
    }

    protected abstract void destroyHandle(long handle);

    protected void assertNotDestroyed() {
        if (_destroyed) {
            throw new IllegalStateException("Already destroyed.");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    @Override
    public void close() throws IOException {
        synchronized (this) {
            if (_autoDestroy) {
                if (!_destroyed) {
                    try {
                        _destroyed = true;
                    } finally {
                        destroyHandle(_handle);
                    }
                }
            }
        }
    }

    protected String getAdditionalToStringInformation() {
        return "";
    }

    @Override
    public String toString() {
        final String information = _destroyed ? "destroyed, "  : getAdditionalToStringInformation();
        return getClass().getSimpleName() + "{"+ information + "handle=" + handle() + "}";
    }

    @Override
    public boolean equals(Object o) {
        final boolean result;
        if (this == o) {
            result = true;
        } else if (o == null || getClass() != o.getClass()) {
            result = false;
        } else {
            result = handle() == ((OggHandleBasedSupport) o).handle();
        }
        return result;
    }

    @Override
    public int hashCode() {
        final long handle = handle();
        return (int) (handle ^ (handle >>> 32));
    }

}
