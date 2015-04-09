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

    .directive('ghErrorAlert', function ($document) {

        return {

            restrict: 'E',

            template: '<div id="main-error" ng-show="error" class="alert alert-danger">'
                + '<span class="message">{{error}}</span>'
                + '<span class="dismiss" ng-click="dismissError()"><i class="fa fa-times"></i></span></div>',

            controller: function ($scope, $element, $rootScope) {
                $scope.dismissError = function() {
                    delete $rootScope.error;
                }
            }
        };
    });
