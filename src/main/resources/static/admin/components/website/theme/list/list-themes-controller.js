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

    .controller('ListThemesController', ['$scope', '$log', '$state', '$modal', 'ThemeService',
        function ($scope, $log, $state, $modal, ThemeService) {

            $scope.themes = ThemeService.query(function (themes) {
                for (var i = 0; i < themes.length; i++) {
                    var theme = themes[i];
                    if (theme.active) {
                        $scope.activeTheme = theme;
                    }
                }
            });

            $scope.activate = function (theme) {
                theme.loading = true;
                ThemeService.activate({id: theme.id},
                    function () {
                        theme.loading = false;
                        theme.active = true;
                        var oldTheme = $scope.activeTheme;
                        if (oldTheme) {
                            oldTheme.active = false;
                        }
                        $scope.activeTheme = theme;
                    },
                    function () {
                        theme.loading = false;
                    });
            };

            $scope.editTheme = function (theme) {
                $state.go('website.theme.edit', { themeId: theme.id });

            };

            $scope.deleteTheme = function (theme) {

                var controller = function ($scope, $modalInstance, theme, themes) {
                    $scope.theme = theme;
                    $scope.ok = function () {
                        ThemeService.delete(theme, function() {
                            var index = themes.indexOf(theme);
                            themes.splice(index, 1);
                            $modalInstance.close();
                        });
                    };
                    $scope.cancel = function () {
                        $modalInstance.dismiss('cancel');
                    };
                };
                var modalInstance = $modal.open({
                    templateUrl: 'components/website/theme/list/confirm-delete.html',
                    controller: controller,
                    resolve: {
                        theme: function () {
                            return theme
                        },
                        themes: function () {
                            return $scope.themes
                        }
                    }
                });
            };

        }]);
