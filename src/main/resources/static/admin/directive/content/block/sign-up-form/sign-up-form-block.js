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

    .controller('SignUpFormBlock', ['$scope', '$modal', function ($scope, $modal) {

        var value = $scope.block.value;

        $scope.editSettings = function () {

            $modal.open({
                templateUrl: 'directive/content/block/sign-up-form/sign-up-form-settings.html',
                resolve: {
                    block: function () {
                        return $scope.block
                    }
                },
                controller: function ($scope, $modalInstance, FileUploader, TagService, block) {
                    $scope.details = {
                        buttonLabel : block.value.buttonLabel,
                        successUrl : block.value.successUrl,
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
                        block.value.buttonLabel = $scope.details.buttonLabel;
                        block.value.successUrl = $scope.details.successUrl;
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


    .controller('SignUpFormTool', ['$scope', function ($scope) {

        $scope.blockFactory.getLabel = function (type) {
            return "Sign up"
        };

        $scope.blockFactory.createBlock = function (type) {
            return {
                type: type,
                isNew: true,
                value: {
                    buttonLabel: 'Sign up',
                    successUrl: '/signup-success'
                }
            }
        };

    }]);
