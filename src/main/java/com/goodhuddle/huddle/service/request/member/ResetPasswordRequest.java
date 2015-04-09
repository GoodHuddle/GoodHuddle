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

package com.goodhuddle.huddle.service.request.member;

import com.goodhuddle.huddle.service.validator.ChangePasswordRequest;
import com.goodhuddle.huddle.service.validator.PasswordsEqualConstraint;
import org.hibernate.validator.constraints.NotBlank;

@PasswordsEqualConstraint(message = "Passwords do not match")
public class ResetPasswordRequest implements ChangePasswordRequest {

    @NotBlank
    private String resetCode;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

    public ResetPasswordRequest() {
    }

    public String getResetCode() {
        return resetCode;
    }

    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
