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

package com.goodhuddle.huddle.web.dto.huddle;

public class HuddleDetail extends HuddleRef {

    private String description;
    private boolean setupWizardComplete;
    private boolean comingSoon;

    public HuddleDetail(long id, String slug, String name, String baseUrl, String description,
                        boolean setupWizardComplete, boolean comingSoon) {
        super(id, slug, name, baseUrl);
        this.description = description;
        this.setupWizardComplete = setupWizardComplete;
        this.comingSoon = comingSoon;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSetupWizardComplete() {
        return setupWizardComplete;
    }

    public boolean isComingSoon() {
        return comingSoon;
    }
}

