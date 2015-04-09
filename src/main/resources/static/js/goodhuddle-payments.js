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

var goodHuddlePayments = (function () {

    return {
        init: function (publishableKey) {

            Stripe.setPublishableKey(publishableKey);

            $('.payment-form').each(function() {
                var $form = $(this);

                var oneOff = $form.find('.one-off-wrapper');
                var recurring = $form.find('.recurring-wrapper');
                var recurringFields = $form.find('.recurring-payment-field');

                $form.find('.one-off-button').click(function(e) {
                    recurring.removeClass('active');
                    oneOff.addClass('active');
                    recurringFields.hide();
                    e.preventDefault();
                    return false;
                });

                $form.find('.recurring-button').click(function(e) {
                    oneOff.removeClass('active');
                    recurring.addClass('active');
                    recurringFields.show();
                    e.preventDefault();
                    return false;
                });

                var donateButtons = $form.find('.amount-button');
                var otherFieldButton = $form.find('.other-amount-button');
                var otherField = $form.find('.other-amount');

                donateButtons.click(function() {
                    var $button = $(this);
                    donateButtons.removeClass('selected-amount');
                    $button.addClass('selected-amount');
                    if ($button.hasClass('other-amount-button')) {
                        otherField.focus();
                    } else {
                        otherField.val('');
                    }
                });

                otherField.bind("propertychange change click keyup input paste", function() {
                    donateButtons.removeClass('selected-amount');
                    otherFieldButton.addClass('selected-amount');
                });
            });


            $('.payment-form').submit(function (event) {
                event.preventDefault();

                var $form = $(this);

                if ($form.data('submitted') == true) {
                    // submit already in progress
                    console.log('Already submitted');
                    return;
                }

                $form.data('submitted', true);

                // clear previous errors
                $form.find('[data-gh-payment-error]').text('');

                var thankYouPage = $form.data('thank-you-page');

                var oneOff = $form.find('.one-off-wrapper').hasClass('active');

                console.log('Making donation: oneOff=' + oneOff);

                var selectedAmount = $form.find('.selected-amount');
                if (selectedAmount == null || selectedAmount.length == 0) {
                    var errorMarker = $form.find('[data-gh-payment-error="amount"]');
                    errorMarker.text('Please select an amount to donate');
                    $form.data('submitted', false);
                    return;
                }
                var amount =  selectedAmount.data('amount');
                if (amount == 'other') {
                    amount = $form.find('.other-amount').val();
                    if (!$.isNumeric(amount)) {
                        var errorMarker = $form.find('[data-gh-payment-error="amount"]');
                        errorMarker.text('Please enter a valid amount to donate');
                        $form.data('submitted', false);
                        return;
                    }
                }
                amount = amount + '00';
                console.log("Amount is (in cents): " + amount);


                var frequencyField = $form.find('[data-gh-payment-name="frequency"]');
                var emailField = $form.find('[data-gh-payment-name="email"]');
                var ccCvcField = $form.find('[data-gh-payment-name="cvc"]');
                var ccNumberField = $form.find('[data-gh-payment-name="number"]');
                var ccExpiryMonthField = $form.find('[data-gh-payment-name="exp_month"]');
                var ccExpiryYearField = $form.find('[data-gh-payment-name="exp_year"]');
                var submitButton = $form.find('button[type=submit]');

                var frequency = frequencyField.val();
                var email = emailField.val();
                var ccCvc = ccCvcField.val();
                var ccNumber = ccNumberField.val();
                var ccExpiryMonth = ccExpiryMonthField.val();
                var ccExpiryYear = ccExpiryYearField.val();

                submitButton.attr('disabled','disabled');

                Stripe.card.createToken({
                    number: ccNumber,
                    exp_month: ccExpiryMonth,
                    exp_year: ccExpiryYear,
                    cvc: ccCvc

                }, function(status, response) {

                    if (response.error) {
                        // Show the errors on the form
                        submitButton.removeAttr('disabled');
                        console.log('Error: ' + JSON.stringify(response.error));
                        var field = response.error.param;
                        if ($.inArray(field, ['amount', 'number', 'exp_month', 'exp_year']) < 0) {
                            field = 'general';
                        }
                        var errorMarker = $form.find('[data-gh-payment-error="' + field + '"]');
                        errorMarker.text(response.error.message);
                        $form.data('submitted', false);

                    } else {
                        // token contains id, last4, and card type
                        var token = response.id;

                        $.ajax({
                            type: "POST",
                            url: '/payment/' + (oneOff ? 'pay.do' : 'subscribe.do' ),
                            data: JSON.stringify({
                                type: 'donation',
                                frequency: frequency,
                                email: email,
                                amountInCents: amount,
                                token: token,
                                description: 'Online donation by ' + email
                            }),
                            success: function(result) {

                                // clear private fields in case back button is used to see this page again
                                ccNumberField.val('');
                                ccCvcField.val('');
                                ccExpiryMonthField.val('');
                                ccExpiryYearField.val('');

                                window.location.href = thankYouPage;
                            },
                            error: function (request, status, errorCode) {
                                submitButton.removeAttr('disabled');
                                $form.data('submitted', false);

                                var error = JSON.parse(request.responseText);
                                if (error.code == 'validation_error' || error.code == 'payment_failed' ) {

                                    var length = error.fieldErrors.length;
                                    for (var i = 0; i < length; i++) {
                                        var fieldError = error.fieldErrors[i];
                                        var field = fieldError.field;
                                        var code = fieldError.code;
                                        var message = fieldError.message;
                                        console.log('Error in field "' + field + '": ' + message);
                                        var errorMarker = $form.find('[data-gh-payment-error="' + field + '"]');
                                        errorMarker.text(message);
                                    }

                                } else {
                                    // handle general error
                                    var errorMarker = $form.find('[data-gh-payment-error="general"]');
                                    errorMarker.text('Sorry, something went wrong and we could not process your donation');
                                }
                            },
                            dataType: "json",
                            headers: {
                                'Accept': 'application/json',
                                'Content-Type': 'application/json'
                            }
                        });

                    }
                });
            });
        }
    }
}());
