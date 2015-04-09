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

package com.goodhuddle.huddle.web.api.util;

import com.goodhuddle.huddle.service.exception.HuddleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorDTO processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        ValidationErrorDTO dto = new ValidationErrorDTO("validation_error", "The information provided was missing or incorrect");
        for (FieldError fieldError: fieldErrors) {
            dto.addFieldError(fieldError.getField(), fieldError.getCode(), fieldError.getDefaultMessage());
        }
        return dto;
    }

    @ExceptionHandler(HuddleException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO processHuddleException(HuddleException ex) {
        log.error("Processing HuddleException: " + ex);
        return new ErrorDTO(ex.getCode(), ex.getMessage());
    }


    //-------------------------------------------------------------------------

    private class ErrorDTO {

        private long timestamp;
        private String code;
        private String message;

        public ErrorDTO(String code, String message) {
            this.code = code;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public long getTimestamp() {
            return timestamp;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    //-------------------------------------------------------------------------

    private class ValidationErrorDTO extends ErrorDTO {

        private List<FieldErrorDTO> fieldErrors = new ArrayList<>();

        public ValidationErrorDTO(String code, String message) {
            super(code, message);
        }

        public void addFieldError(String path, String code, String message) {
            FieldErrorDTO error = new FieldErrorDTO(path, code, message);
            fieldErrors.add(error);
        }

        public List<FieldErrorDTO> getFieldErrors() {
            return fieldErrors;
        }
    }

    //-------------------------------------------------------------------------

    private class FieldErrorDTO {

        private String field;
        private String code;
        private String message;

        public FieldErrorDTO(String field, String code, String message) {
            this.field = field;
            this.code = code;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
