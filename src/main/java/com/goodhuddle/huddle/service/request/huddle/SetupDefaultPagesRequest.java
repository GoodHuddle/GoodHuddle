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

package com.goodhuddle.huddle.service.request.huddle;

public class SetupDefaultPagesRequest {

    private boolean home;
    private boolean blog;
    private boolean about;
    private boolean contact;
    private boolean donate;

    public SetupDefaultPagesRequest() {
    }

    public boolean isHome() {
        return home;
    }

    public void setHome(boolean home) {
        this.home = home;
    }

    public boolean isBlog() {
        return blog;
    }

    public void setBlog(boolean blog) {
        this.blog = blog;
    }

    public boolean isAbout() {
        return about;
    }

    public void setAbout(boolean about) {
        this.about = about;
    }

    public boolean isContact() {
        return contact;
    }

    public void setContact(boolean contact) {
        this.contact = contact;
    }

    public boolean isDonate() {
        return donate;
    }

    public void setDonate(boolean donate) {
        this.donate = donate;
    }
}
