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

    .controller('BulkExportMembersController', ['$scope', '$state', '$stateParams', 'MemberBulkService', 'ExportService',
        function ($scope, $state, $stateParams, MemberBulkService, ExportService) {

            var bulkSetId = $stateParams.bulkSetId;

            $scope.memberBulkSet = MemberBulkService.get({ id: bulkSetId });

            $scope.ok = function () {

                $scope.saving = true;
                ExportService.exportMembers({
                    bulkSetId: bulkSetId
                }, function(downloadFileName) {
                    $scope.saving = false;
                    $scope.downloadFileName = downloadFileName;
                }, function() {
                    $scope.saving = false;
                });
            };

            $scope.cancel = function () {
                $state.go('member.list');
            };

        }]);
