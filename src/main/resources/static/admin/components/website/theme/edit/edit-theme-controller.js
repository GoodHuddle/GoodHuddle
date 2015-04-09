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

    .controller('EditThemeController', ['$scope', '$log', '$state', '$stateParams', '$modal', 'ThemeSettingService',
        function ($scope, $log, $state, $stateParams, $modal, ThemeSettingService) {

            $scope.settingsForm = ThemeSettingService.getForm();
            ThemeSettingService.get(function(settings) {
                $scope.settings = settings;
            });

            $scope.uploaders = [];

            $scope.save = function () {
                $scope.saving = true;

                function doSave() {
                    ThemeSettingService.save(
                        {
                            settings: $scope.settings
                        },
                        function () {
                            console.log('Done saving');
                            $scope.saving = false;
                            $state.go('website.theme.current');
                        },
                        function () {
                            console.log('Error saving');
                            $scope.saving = false;
                        });
                }

                function uploadNext(index) {
                    if (index < $scope.uploaders.length) {
                        $scope.uploaders[index].upload(function() {
                            uploadNext(index+1);
                        });
                    } else {
                        doSave();
                    }
                }

                uploadNext(0);
            };

            $scope.cancel = function () {
                $state.go('website.theme.current');
            };


        }]);
