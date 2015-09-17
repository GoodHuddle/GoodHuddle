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

    .controller('MailChimpSyncMembersController', ['$scope', '$state', '$stateParams', 'MailChimpService',
        function ($scope, $state, $stateParams, MailChimpService) {

            $scope.request = {};

            $scope.availableLists = MailChimpService.lists();

            $scope.ok = function () {
                console.log('request: ' + JSON.stringify($scope.request));

                $scope.saving = true;
                MailChimpService.syncMembers($scope.request, function() {
                    $scope.saving = false;
                }, function() {
                    $scope.saving = false;
                });
            };

            $scope.cancel = function () {
                $state.go('member.list');
            };

        }]);
