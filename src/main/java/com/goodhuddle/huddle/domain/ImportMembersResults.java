/*
 * (C) Copyright ${year} Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package com.goodhuddle.huddle.domain;

public class ImportMembersResults {

    private int numImported;
    private int numUpdated;
    private boolean success;
    private String errorMessage;

    public int getNumImported() {
        return numImported;
    }

    public void setNumImported(int numImported) {
        this.numImported = numImported;
    }

    public int getNumUpdated() {
        return numUpdated;
    }

    public void setNumUpdated(int numUpdated) {
        this.numUpdated = numUpdated;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
