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

var goodHuddleBlog = (function () {

    return {
        init: function () {
            console.log('Setting up blog');

            $('.blog-comment-form').submit(function (event) {
                event.preventDefault();

                var $form = $(this);

                if ($form.data('submitted') == true) {
                    // submit already in progress
                    console.log('Already submitted');
                    return;
                }

                $form.data('submitted', true);

                var blogPostIdField = $form.find('[data-gh-field="blog-post-id"]');
                var nameField = $form.find('[data-gh-field="name"]');
                var commentField = $form.find('[data-gh-field="comment"]');
                var submitButton = $form.find('button[type=submit]');

                var blogPostId = blogPostIdField.val();
                var name = nameField.val();
                var comment = commentField.val();

                submitButton.attr('disabled','disabled');

                $.ajax({
                    type: "POST",
                    url: '/blog/comment',
                    data: JSON.stringify({
                        blogPostId: blogPostId,
                        displayName: name,
                        comment: comment
                    }),
                    success: function(result) {
                        submitButton.removeAttr('disabled');

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
                                console.log('Error in field "' + field + '": ' + message);
                                var errorMarker = $form.find('[data-gh-field-error="' + field + '"]');
                                errorMarker.text(message);
                            }

                        } else {
                            // handle general error
                            var errorMarker = $form.find('[data-gh-error="general"]');
                            errorMarker.text('Sorry, something went wrong and we could not submit your comment');
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
