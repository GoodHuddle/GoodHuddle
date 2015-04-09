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

    .controller('CreateMemberController', ['$scope', '$state', '$stateParams', 'MemberService', 'SecurityGroupService',
        function($scope, $state, $stateParams, MemberService, SecurityGroupService) {

            $scope.member = {};
            $scope.loginEnabled = false;

            $scope.securityGroups = SecurityGroupService.query();

            $scope.$watch('member.securityGroupId', function() {
                $scope.loginEnabled = $scope.member.securityGroupId != null;
            });

            $scope.submit = function() {
                MemberService.save($scope.member, function() {
                    $state.go('member.list');
                });
            };

            $scope.cancel = function() {
                $state.go('member.list');
            };

    }]);
