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

var goodHuddleSignUp = (function () {

    return {
        init: function () {

            $('.gh-sign-up-form').submit(function (event) {
                event.preventDefault();

                var $form = $(this);

                if ($form.data('submitted') == true) {
                    // submit already in progress
                    console.log('Already submitted');
                    return;
                }

                $form.data('submitted', true);

                // clear previous errors
                $form.find('[data-gh-sign-up-error]').text('');

                var block = $form.closest('.block');
                var blockId = block.data('block-id');

                var successMessage = $form.find('.gh-sign-up-success');
                successMessage.hide();

                var firstNameField = $form.find('[data-gh-sign-up-name="firstName"]');
                var lastNameField = $form.find('[data-gh-sign-up-name="lastName"]');
                var emailField = $form.find('[data-gh-sign-up-name="email"]');
                var postCodeField = $form.find('[data-gh-sign-up-name="postCode"]');
                var submitButton = $form.find('button[type=submit]');

                var firstName = firstNameField.val();
                var lastName = lastNameField.val();
                var email = emailField.val();
                var postCode = postCodeField.val();

                submitButton.attr('disabled', 'disabled');
                $.ajax({
                    type: "POST",
                    url: '/member/signup.do',
                    data: JSON.stringify({
                        blockId: blockId,
                        firstName: firstName,
                        lastName: lastName,
                        email: email,
                        postCode: postCode
                    }),
                    success: function (result) {

                        submitButton.removeAttr('disabled');
                        $form.data('submitted', false);
                        window.location.href = result.successUrl;
                    },
                    error: function (request, status, errorCode) {
                        submitButton.removeAttr('disabled');
                        $form.data('submitted', false);

                        var error = JSON.parse(request.responseText);
                        if (error.code == 'validation_error') {

                            var length = error.fieldErrors.length;
                            for (var i = 0; i < length; i++) {
                                var fieldError = error.fieldErrors[i];
                                var field = fieldError.field;
                                var code = fieldError.code;
                                var message = fieldError.message;
                                var errorMarker = $form.find('[data-gh-sign-up-error="' + field + '"]');
                                errorMarker.text(message);
                            }

                        } else {
                            // handle general error
                            var errorMarker = $form.find('[data-gh-sign-up-error="general"]');
                            errorMarker.text('Sorry, something went wrong and we could not process your request');
                        }
                    },
                    dataType: "json",
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    }
                });
            });
        }
    }
}());
