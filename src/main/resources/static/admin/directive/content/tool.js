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

    .directive('ghTool', function ($document) {

        return {
            restrict: 'E',
            require: '^ghContentEditor',
            scope: {
                blockFunctions: '=',
                type: '='
            },
            template: '<li class="content-tool" ng-include src="getToolTemplate()"></li>',

            link: function (scope, elem, attrs) {

                scope.getToolTemplate = function () {
                    var type = scope.type;
                    if (!type) {
                        type = 'html';
                    }
                    return 'directive/content/block/' + type + '/' + type + '-tool.html';
                };

                scope.blockFactory = {
                    getLabel: function (type) {
                        return type;
                    },
                    createBlock: function (type) {
                        return { type: type };
                    }
                };

                elem.bind('mousedown', function (event) {
                    console.log('Starting tool drag: ' + scope.type);
                    scope.blockFunctions.startBlockDrag({
                        label: scope.blockFactory.getLabel(scope.type),
                        x: event.clientX,
                        y: event.clientY,
                        createBlock: function () {
                            return scope.blockFactory.createBlock(scope.type);
                        },
                        afterBlockAdded: function() {
                            if (scope.blockFactory.afterBlockAdded) {
                                scope.blockFactory.afterBlockAdded();
                            }
                        }
                    });
                    event.preventDefault();
                });
            }
        };
    });
