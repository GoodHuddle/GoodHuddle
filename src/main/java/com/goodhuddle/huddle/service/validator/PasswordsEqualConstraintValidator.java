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

package com.goodhuddle.huddle.service.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsEqualConstraintValidator  implements ConstraintValidator<PasswordsEqualConstraint, Object> {

    private String errorMessage;

    @Override
    public void initialize(PasswordsEqualConstraint constraint) {
        errorMessage = constraint.message();
    }

    @Override
    public boolean isValid(Object candidate, ConstraintValidatorContext context) {
        ChangePasswordRequest request = (ChangePasswordRequest) candidate;
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage).addPropertyNode("confirmPassword").addConstraintViolation();
            return false;
        } else {
            return true;
        }
    }
}
