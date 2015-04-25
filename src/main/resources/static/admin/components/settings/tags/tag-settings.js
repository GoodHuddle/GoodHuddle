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

    .controller('TagSettingsController',

        ['$scope', '$log', '$modal', 'TagService',

            function ($scope, $log, $modal, TagService) {

                $scope.loading = true;
                $scope.tags = TagService.get(function(tags) {
                    $scope.loading = false;
                });

                $scope.addTag = function() {
                    $modal.open({
                        templateUrl: 'components/settings/tags/add-tag.html',
                        controller: function ($scope, $modalInstance) {
                            $scope.tag = {};
                            $scope.ok = function () {
                                TagService.save($scope.tag,
                                    function (tag) {
                                        $scope.saving = false;
                                        $modalInstance.close(tag);
                                    }, function () {
                                        $scope.saving = false;
                                        console.log('Error');
                                    });
                            };
                            $scope.cancel = function () {
                                $modalInstance.close();
                            };
                        }
                    })
                        .result.then(function(tag) {
                            $scope.tags.content.push(tag);
                        });
                }

            }

        ]);
