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

    .controller('PaymentSettingsController',

        ['$scope', '$log', '$modal', 'PaymentSettingsService',

            function ($scope, $log, $modal, PaymentSettingsService) {

                $scope.loading = true;
                $scope.settings = PaymentSettingsService.get(function() {
                    $scope.loading = false;
                });

                $scope.changeSettings = function() {
                    $scope.loading = true;
                    PaymentSettingsService.checkChangeAllowed(
                        function() {
                            $scope.loading = false;

                            $modal.open({
                                templateUrl: 'components/settings/payment/change-settings.html',
                                controller: function ($scope, $modalInstance) {
                                    $scope.settings = {};
                                    $scope.ok = function () {
                                        PaymentSettingsService.save($scope.settings,
                                            function (settings) {
                                                $scope.saving = false;
                                                $modalInstance.close(settings);
                                            }, function () {
                                                $scope.saving = false;
                                                console.log('Error');
                                            });
                                    };
                                    $scope.cancel = function () {
                                        $modalInstance.close();
                                    };
                                }
                            }).result.then(function(settings) {
                                $scope.settings.publishableKey = settings.publishableKey;
                            });

                        },
                        function() {
                            $scope.loading = false;
                            console.log('Change settings failed');
                            $modal.open({
                                templateUrl: 'components/settings/payment/change-not-allowed.html',
                                controller: function ($scope, $modalInstance) {
                                    $scope.ok = function () {
                                        $modalInstance.close();
                                    };
                                    $scope.cancel = function () {
                                        $modalInstance.close();
                                    };
                                }
                            });
                        }
                    );
                }
            }

        ]);
