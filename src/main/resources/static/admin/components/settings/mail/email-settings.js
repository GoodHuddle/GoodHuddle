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

    .controller('EmailSettingsController',

        ['$scope', '$log', '$modal', 'EmailSettingsService',

            function ($scope, $log, $modal, EmailSettingsService) {

                $scope.loading = true;
                $scope.settings = EmailSettingsService.get(function() {
                    $scope.loading = false;
                });

                $scope.changeSettings = function() {
                    $modal.open({
                        templateUrl: 'components/settings/mail/change-settings.html',
                        controller: function ($scope, $modalInstance) {
                            $scope.settings = {};
                            $scope.ok = function () {
                                EmailSettingsService.save($scope.settings,
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
                    })
                        .result.then(function(settings) {
                            $scope.settings.sendFromAddress = settings.sendFromAddress;
                            $scope.settings.sendFromName = settings.sendFromName;
                        });
                }

            }

        ]);
