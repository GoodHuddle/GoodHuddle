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

var goodHuddleContactUs = (function () {

    return {
        init: function () {

            $('.gh-contact-us-form').submit(function (event) {
                event.preventDefault();

                var $form = $(this);

                if ($form.data('submitted') == true) {
                    // submit already in progress
                    console.log('Already submitted');
                    return;
                }

                $form.data('submitted', true);

                // clear previous errors
                $form.find('[data-gh-contact-us-error]').text('');

                var successMessage = $form.find('.gh-contact-us-success');
                successMessage.hide();

                var emailField = $form.find('[data-gh-contact-us-name="email"]');
                var nameField = $form.find('[data-gh-contact-us-name="name"]');
                var messageField = $form.find('[data-gh-contact-us-name="message"]');
                var submitButton = $form.find('button[type=submit]');

                var email = emailField.val();
                var name = nameField.val();
                var message = messageField.val();

                submitButton.attr('disabled', 'disabled');
                $.ajax({
                    type: "POST",
                    url: '/member/contact.do',
                    data: JSON.stringify({
                        email: email,
                        name: name,
                        message: message
                    }),
                    success: function (payment) {

                        submitButton.removeAttr('disabled');
                        $form.data('submitted', false);
                        $form.find('[data-gh-contact-us-name]').val('');
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
                                var errorMarker = $form.find('[data-gh-contact-us-error="' + field + '"]');
                                errorMarker.text(message);
                            }

                        } else {
                            // handle general error
                            var errorMarker = $form.find('[data-gh-contact-us-error="general"]');
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
