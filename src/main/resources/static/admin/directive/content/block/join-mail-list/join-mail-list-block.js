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

    .controller('JoinMailListBlock', ['$scope', '$modal', 'TagService', function ($scope, $modal, TagService) {

        $scope.showSettings = function () {
            $modal.open({
                templateUrl: 'directive/content/block/join-mail-list/join-mail-list-settings.html',
                resolve: {
                    block: function () {
                        return angular.copy($scope.block)
                    }
                },
                controller: function ($scope, $modalInstance, block) {

                    if (!block.value.tags) {
                        block.value.tags = [];
                    }
                    $scope.block = block;

                    TagService.get(function(tags) {
                        $scope.availableTags = tags.content;
                    });

                    $scope.ok = function () {
                        $modalInstance.close($scope.block);
                    };
                    $scope.cancel = function () {
                        $modalInstance.dismiss('cancel');
                    };
                }
            })
                .result.then(function (block) {
                    $scope.block.value = block.value;
                })
            ;
        }


    }])


    .controller('JoinMailListTool', ['$scope', function ($scope) {

        $scope.blockFactory.getLabel = function (type) {
            return "Join mailing list button"
        };

        $scope.blockFactory.createBlock = function (type) {
            return {
                type: type,
                value: {
                    tags: []
                }
            }
        };

    }]);
