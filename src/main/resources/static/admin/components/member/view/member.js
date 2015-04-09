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

    .controller('MemberController', ['$scope', '$state', '$stateParams', 'MemberService',
        function ($scope, $state, $stateParams, MemberService) {

            var memberId = $stateParams.memberId;

            $scope.member = MemberService.get({id: memberId});

            $scope.edit = function() {
                $state.go('member.edit', { memberId: memberId});
            };

            $scope.showEmails = function() {
                $state.go('member.view.emails', { memberId: memberId}, {location: 'replace'});
            };

            $scope.showDonations = function() {
                $state.go('member.view.donations', { memberId: memberId}, {location: 'replace'});
            };

            $scope.showSubscriptions = function() {
                $state.go('member.view.subscriptions', { memberId: memberId}, {location: 'replace'});
            };

        }]);
