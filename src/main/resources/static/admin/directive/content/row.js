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

    .directive('ghRow', function ($window, $modal) {

        return {

            restrict: 'E',
            transclude: true,
            scope: {
                row: "="
            },

            template:
                '<div class="content-row">'
                    + '<div class="content-row-handle" ng-click="editSettings()">row</div>'
                    + '<div class="content-row-inner" ng-transclude>'
                    + '</div>'
                    + '</div>',

            link: function (scope, elem, attrs) {

                scope.editSettings = function() {
                    $modal.open({
                        templateUrl: 'directive/content/row-settings.html',
                        resolve: {
                            row: function () { return scope.row }
                        },
                        controller: function ($scope, $modalInstance, row) {
                            $scope.row = row;
                            $scope.ok = function () {
                                $modalInstance.dismiss('ok');
                            };
                            $scope.cancel = function () {
                                $modalInstance.dismiss('cancel');
                            };
                        }
                    });
                };
            }
        };
    });
