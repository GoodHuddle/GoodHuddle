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

    .directive('ghSearchNav', function () {

        return {

            restrict: 'E',

            scope: {
                search: "=",
                results: "="
            },

            templateUrl: 'directive/search/search-nav.html',

            controller: function ($scope) {

                $scope.prevPage = function () {
                    $scope.search.page--;
                };

                $scope.nextPage = function () {
                    $scope.search.page++;
                };
            }
        };
    });
