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

public enum VbrConstraint {
    unconstrained(0),
    constrained(1),

    ;

    private final int _handle;

    VbrConstraint(int handle) {
        _handle = handle;
    }

    public static VbrConstraint vbrConstraintFor(int code) {
        VbrConstraint result = null;
        for (final VbrConstraint candidate : values()) {
            if (candidate.handle() == code) {
                result = candidate;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Could not handle vbr constrained with code: " + code);
        }
        return result;
    }

    public int handle() {
        return _handle;
    }

}

