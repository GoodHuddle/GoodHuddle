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

'use strict';

angular.module('huddleAdmin')

    .controller('CreateMailoutController', ['$scope', '$state', '$stateParams', 'MailoutService',
        function($scope, $state, $stateParams, MailoutService) {

            $scope.mailout = {
                generateEmails: true
            };

            // setup editor options
            $scope.editorOptions = {
                toolbar: 'simple',
                toolbar_simple: [
                    { name: 'basicstyles', items: [ 'Format', 'Bold', 'Italic', 'Underline' ] },
                    { name: 'paragraph', items: [ 'BulletedList', 'NumberedList', 'Blockquote' ] },
                    { name: 'editing', items: ['JustifyLeft', 'JustifyCenter', 'JustifyRight' ] },
                    { name: 'links', items: [ 'Ghlink', 'Unlink' ] },
                    { name: 'tools', items: [ 'SpellChecker' ] },
                    { name: 'styles', items: [ 'FontSize', 'TextColor', 'PasteText', 'PasteFromWord' ] },
                    { name: 'insert', items: [ 'Image' ] },
                    { name: 'clipboard', items: [ 'Undo', 'Redo' ] },
                    { name: 'document', items: [ 'Source' ] }
                ]
            };

            $scope.submit = function() {
                MailoutService.save($scope.mailout, function(mailout) {
                    $state.go('member.mailout.view', { mailoutId: mailout.id });
                });
            };

            $scope.cancel = function() {
                $state.go('member.mailout.list');
            };

    }]);
