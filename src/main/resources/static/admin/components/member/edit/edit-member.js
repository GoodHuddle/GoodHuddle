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

    .controller('EditMemberController', ['$scope', '$state', '$stateParams', 'SecurityGroupService', 'MemberService',
        function ($scope, $state, $stateParams, SecurityGroupService, MemberService) {

            var memberId = $stateParams.memberId;

            $scope.member = MemberService.get({id: memberId}, function() {
                $scope.member.securityGroupId
                    = $scope.member.securityGroup ? $scope.member.securityGroup.id : null;
            });

            $scope.securityGroups = SecurityGroupService.query();

            $scope.$watch('member.securityGroupId', function() {
                $scope.loginEnabled = $scope.member.securityGroupId != null;
            });

            $scope.submit = function () {
                var member = $scope.member;
                MemberService.update(member, function () {
                    $state.go('member.view', {memberId: memberId});
                });
            };

            $scope.cancel = function () {
                $state.go('member.view', { memberId: memberId });
            };


        }]);
