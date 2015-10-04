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

    .controller('PetitionBlock', ['$scope', '$modal', function ($scope, $modal) {

        $scope.editSettings = function () {

            $modal.open({
                templateUrl: 'directive/content/block/petition/petition-settings.html',
                resolve: {
                    block: function () {
                        return $scope.block
                    }
                },
                controller: function ($scope, $modalInstance, PetitionService, block) {
                    $scope.details = {
                        petitionId : block.value.petitionId
                    };

                    $scope.availablePetitions = [];
                    PetitionService.get(function(petitions) {
                        $scope.availablePetitions = petitions.content;
                    });

                    $scope.ok = function () {
                        console.log('Petition ID: ' + $scope.details.petitionId);
                        block.value.petitionId = $scope.details.petitionId;
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


    .controller('PetitionTool', ['$scope', function ($scope) {

        $scope.blockFactory.getLabel = function (type) {
            return "Petition"
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
