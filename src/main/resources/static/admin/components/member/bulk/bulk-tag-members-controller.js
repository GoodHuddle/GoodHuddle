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

    .controller('BulkTagMembersController', ['$scope', '$state', '$stateParams','TagService', 'MemberBulkService',
        function ($scope, $state, $stateParams, TagService, MemberBulkService) {

            var bulkSetId = $stateParams.bulkSetId;

            $scope.memberBulkSet = MemberBulkService.get({ id: bulkSetId });

            $scope.selected = {tags: []};
            $scope.availableTags = TagService.get();

            $scope.ok = function () {

                var selectedTags = $scope.selected.tags;
                var tagIds = [];
                for (var i = 0; i < selectedTags.length; i++) {
                    tagIds.push(selectedTags[i].id);
                }

                $scope.saving = true;
                MemberBulkService.tag({
                    bulkSetId: bulkSetId,
                    tagIds: tagIds
                }, function() {
                    $state.go('member.list');
                }, function() {
                    $scope.saving = false;
                });
            };

            $scope.cancel = function () {
                $state.go('member.list');
            };

        }]);
