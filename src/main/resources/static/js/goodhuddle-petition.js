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

var goodHuddlePetition = (function () {

    return {
        init: function () {

            $('.gh-petition').submit(function (event) {
                event.preventDefault();

                var $form = $(this);

                if ($form.data('submitted') == true) {
                    // submit already in progress
                    console.log('Already submitted');
                    return;
                }

                $form.data('submitted', true);

                // clear previous errors
                $form.find('[data-gh-petition-error]').text('');

                var block = $form.closest('.block');
                var blockId = block.data('block-id');

                var successMessage = block.find('.gh-petition-success');
                successMessage.hide();

                var petitionIdField = $form.find('[data-gh-petition-name="petitionId"]');
                var firstNameField = $form.find('[data-gh-petition-name="firstName"]');
                var lastNameField = $form.find('[data-gh-petition-name="lastName"]');
                var emailField = $form.find('[data-gh-petition-name="email"]');
                var postCodeField = $form.find('[data-gh-petition-name="postCode"]');
                var phoneField = $form.find('[data-gh-petition-name="phone"]');
                var submitButton = $form.find('button[type=submit]');

                var petitionId = petitionIdField.val();
                var firstName = firstNameField.val();
                var lastName = lastNameField.val();
                var email = emailField.val();
                var postCode = postCodeField.val();
                var phone = phoneField.val();

                submitButton.attr('disabled', 'disabled');
                $.ajax({
                    type: "POST",
                    url: '/petition/sign.do',
                    data: JSON.stringify({
                        petitionId: petitionId,
                        firstName: firstName,
                        lastName: lastName,
                        email: email,
                        postCode: postCode,
                        phone: phone
                    }),
                    success: function (result) {

                        submitButton.removeAttr('disabled');
                        $form.data('submitted', false);
                        $form.hide();
                        successMessage.show();

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
                                var errorMarker = $form.find('[data-gh-petition-error="' + field + '"]');
                                errorMarker.text(message);
                            }

                        } else {
                            // handle general error
                            var errorMarker = $form.find('[data-gh-petition-error="general"]');
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
