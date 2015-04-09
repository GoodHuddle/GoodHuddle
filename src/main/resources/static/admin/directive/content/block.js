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

    .directive('ghBlock', function ($window) {

        return {
            restrict: 'E',
            scope: {
                cell: "=",
                block: "=",
                blockFunctions: '='
            },
            template: '<div class="content-block {{block.type}}-block {{dragging ? \'block-being-dragged\' : \'\'}}">'
                + '<div class="default-drag-handle drag-handle"></div>'
                + '<div class="content-block-inner" ng-include="getBlockTemplateUrl()" onload="setupBlock()"></div>'
                + '<div class="delete-block" ng-click="deleteBlock()"><i class="fa fa-times"></i></div>'
                + '</div>',


            link: function (scope, elem, attrs) {

                var guid = (function() {
                    function s4() {
                        return Math.floor((1 + Math.random()) * 0x10000)
                            .toString(16)
                            .substring(1);
                    }
                    return function() {
                        return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
                            s4() + '-' + s4() + s4() + s4();
                    };
                })();
                scope.id = guid();

                scope.getBlockTemplateUrl = function () {
                    var type = scope.block.type;
                    if (!type) {
                        type = 'html';
                    }

                    // 'line' renamed to 'divider'
                    if (type == 'line') {
                        type = 'divider';
                    }

                    return 'directive/content/block/' + type + '/' + type + '-block.html';
                };

                scope.deleteBlock = function () {
                    var blocks = scope.cell.blocks;
                    var index = blocks.indexOf(scope.block);
                    blocks.splice(index, 1);
                    if (blocks.length == 0) {
                        scope.blockFunctions.deleteCell(scope.cell);
                    }
                };

                scope.setupBlock = function () {

                    var dragHandle = $(elem).find('.content-block-inner .drag-handle');
                    if (dragHandle.length == 0) {
                        dragHandle = $(elem).find('.default-drag-handle');
                    } else {
                        dragHandle = angular.element(dragHandle[0]);
                    }

                    dragHandle.bind('mousedown', function (event) {

                        scope.dragging = true;
                        scope.blockFunctions.startBlockDrag({
                            blockId: scope.id,
                            label: 'Drag to place',
                            x: event.clientX,
                            y: event.clientY,
                            createBlock: function () {
                                var newBlock = JSON.parse(JSON.stringify(scope.block));
                                delete newBlock.$$hashKey;
                                return  newBlock;
                            },
                            afterBlockAdded: function () {
                                var index = scope.cell.blocks.indexOf(scope.block);
                                scope.cell.blocks.splice(index, 1);
                            },
                            blockDragDone: function() {
                                scope.dragging = false;
                            }
                        });
                        event.preventDefault();
                    });
                };
            }
        };
    });
