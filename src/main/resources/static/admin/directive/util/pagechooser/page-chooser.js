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

    .directive('ghPageChooser', function (SearchService) {

        return {
            restrict: 'E',
            require: "ngModel",
            templateUrl: 'directive/util/pagechooser/page-chooser.html',

            link: function (scope, elem, attrs, ngModel) {

                // bind 'page' model to outer model
                var render = function(){
                    scope.page = (ngModel.$modelValue);
                };
                scope.$watch(attrs['ngModel'], render);
                render();

                scope.$watch('page', function(newValue) {
                    ngModel.$setViewValue(newValue);
                    scope.$apply();
                });

                scope.searchPages = function(searchPhrase) {
                    return SearchService.search( { searchPhrase: searchPhrase })
                        .$promise.then(function(response){
                            return response.content;
                        } );
                }

            }
        };
    });
