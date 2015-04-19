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

    .controller('LinkButtonBlock', ['$scope', '$http', '$modal', '$document', '$sce',  function ($scope, $http, $modal, $document, $sce) {

        $scope.editSettings = function () {

            $modal.open({
                templateUrl: 'directive/content/block/link-button/edit-settings.html',
                resolve: {
                    block: function () {
                        return $scope.block
                    }
                },
                controller: function ($scope, $modalInstance, block, $window, $document) {

                    $scope.details = {
                        label : block.value.label,
                        url : block.value.url
                    };

                    $scope.ok = function () {
                        block.value.label = $scope.details.label;
                        block.value.url = $scope.details.url;
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

    .controller('LinkButtonTool', ['$scope', function ($scope) {


        $scope.blockFactory.getLabel = function (type) {
            return "Button";
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
