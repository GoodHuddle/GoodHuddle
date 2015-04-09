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

    .controller('ChooseThemeController', ['$scope', '$log', '$state', '$modal', 'ThemeService', 'ThemeBundleService',
        function ($scope, $log, $state, $modal, ThemeService, ThemeBundleService) {

            $scope.bundles = ThemeBundleService.query();

            $scope.selectBundle = function(bundle) {
                ThemeBundleService.install(bundle, function() {
                    $state.go('website.theme.current');
                });
            };

            $scope.preview = function(bundle) {
                $modal.open({
                    templateUrl: 'components/website/theme/choose/preview-theme-bundle.html',
                    resolve: {
                        bundle: function () { return bundle }
                    },
                    controller: function ($scope, $modalInstance, bundle) {
                        $scope.bundle = bundle;
                        $scope.ok = function () {
                            ThemeBundleService.install(bundle, function() {
                                $modalInstance.dismiss('ok');
                                $state.go('website.theme.current');
                            });
                        };
                        $scope.cancel = function () {
                            $modalInstance.dismiss('cancel');
                        };
                    }
                });
            };


        }]);
