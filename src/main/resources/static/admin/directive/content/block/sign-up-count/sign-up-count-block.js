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

    .controller('SignUpCountBlock', ['$scope', '$modal', function ($scope, $modal) {

        var value = $scope.block.value;

        $scope.editSettings = function () {

            $modal.open({
                templateUrl: 'directive/content/block/sign-up-count/sign-up-count-settings.html',
                resolve: {
                    block: function () {
                        return $scope.block
                    }
                },
                controller: function ($scope, $modalInstance, TagService, block) {
                    $scope.details = {
                        tagId : null
                    };

                    // currently only supported one tag but model supports multiple
                    if (block.value.tagIds && block.value.tagIds.length > 0) {
                        $scope.details.tagId = block.value.tagIds[0];
                    }

                    $scope.availableTags = [];
                    TagService.get(function(tags) {
                        $scope.availableTags = tags.content;
                        $scope.availableTags.unshift(null);
                    });

                    $scope.ok = function () {
                        block.value.tagIds = [ $scope.details.tagId ];
                        $modalInstance.close();
                    };
                    $scope.cancel = function () {
                        $modalInstance.dismiss('cancel');
                    };
                }
            });
        };

        if ($scope.block.isNew) {
            $scope.editSettings();
            delete $scope.block.isNew;
        }

    }])


    .controller('SignUpCountTool', ['$scope', function ($scope) {

        $scope.blockFactory.getLabel = function (type) {
            return "Sign up counter"
        };

        $scope.blockFactory.createBlock = function (type) {
            return {
                type: type,
                isNew: true,
                value: {
                }
            }
        };

    }]);
