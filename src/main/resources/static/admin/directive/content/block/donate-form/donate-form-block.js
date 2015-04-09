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

    .controller('DonateFormBlock', ['$scope', '$modal', function ($scope, $modal) {

        if (!$scope.block.value.amounts) {
            $scope.block.value.amounts = [
                { amount: 20, "default": false},
                { amount: 50, "default": false},
                { amount: 70, "default": true},
                { amount: 100, "default": false}
            ];
            $scope.block.value.otherAmount = true;
        }

        $scope.editSettings = function() {
            $modal.open({
                templateUrl: 'directive/content/block/donate-form/edit-settings.html',
                resolve: {
                    block: function () { return $scope.block }
                },
                controller: function ($scope, $modalInstance, block) {
                    $scope.details = {
                        amounts : angular.copy(block.value.amounts),
                        otherAmount : block.value.otherAmount,
                        thankYouPage : block.value.thankYouPage
                    };

                    $scope.updateSelection = function(selected) {
                        angular.forEach($scope.details.amounts, function(amount, index) {
                            if (selected != index)
                                amount.default = false;
                        });
                    };

                    $scope.moveAmountUp = function(index) {
                        var amounts = $scope.details.amounts;
                        if (index > 0) {
                            var value = amounts[index];
                            amounts.splice(index, 1);
                            amounts.splice(index - 1, 0, value);
                        }
                    };

                    $scope.moveAmountDown = function(index) {
                        var amounts = $scope.details.amounts;
                        if (index < amounts.length - 1) {
                            var value = amounts[index];
                            amounts.splice(index, 1);
                            amounts.splice(index + 1, 0, value);
                        }
                    };

                    $scope.deleteAmount = function(index) {
                        var amounts = $scope.details.amounts;
                        if (amounts.length > 1 && index < amounts.length) {
                            amounts.splice(index, 1);
                        }
                    };

                    $scope.ok = function () {
                        block.value.amounts = $scope.details.amounts;
                        block.value.otherAmount = $scope.details.otherAmount;
                        block.value.thankYouPage = $scope.details.thankYouPage;
                        $modalInstance.dismiss('ok');
                    };

                    $scope.cancel = function () {
                        $modalInstance.dismiss('cancel');
                    };
                }
            });
        };

        if ($scope.block.isNew) {
            delete $scope.block.isNew;
            $scope.editSettings();
        }

    }])


    .controller('DonateFormTool', ['$scope', function ($scope) {

        $scope.blockFactory.getLabel = function (type) {
            return "Donation form"
        };

        $scope.blockFactory.createBlock = function (type) {
            return {
                type: type,
                isNew: true,
                value: {
                    amounts: [
                        { amount: 20, "default": false},
                        { amount: 50, "default": false},
                        { amount: 70, "default": true},
                        { amount: 100, "default": false}
                    ],
                    otherAmount: true
                }
            }
        };

    }]);
