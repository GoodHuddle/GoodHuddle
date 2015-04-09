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

    .directive('ghMenuItem', function ($log, $document, RecursionHelper, MenuService) {

        return {
            restrict: 'E',
            transclude: true,
            scope: {
                menu: "=",
                parent: "=",
                item: "=",
                selection: "="
            },
            templateUrl: 'directive/menu/menu-item.html',

            controller: function ($scope, $state, $element, $rootScope) {

                var item = $scope.item;

                $scope.select = function () {
                    $scope.selection.item = $scope.item;
                };

                item.expanded = false;

                $scope.toggleExpanded = function () {
                    $scope.item.expanded = !$scope.item.expanded;
                };
            },

            compile: function (element) {

                var documentBody = angular.element($document[0].body);

                return RecursionHelper.compile(element, function (scope, element, attrs) {

                    if (!scope.item.items) {
                        scope.item.items = [];
                    }

                    var floatie = null;
                    var dropMarker = null;
                    var targetItem = null;
                    var targetScope = null;
                    var targetPos = null;

                    var dragHandle = $(element).find('.menu-item');
                    dragHandle.bind('mousedown', function (event) {

                        var item = scope.item;

                        scope.$apply(function () {

                            floatie = angular.element('<div class="menu-floatie invalid" style="display: none">' + item.label + '</div>');
                            floatie.css('left', (event.clientX + 2) + 'px');
                            floatie.css('top', (event.clientY + 2) + 'px');
                            documentBody.append(floatie);

                            dropMarker = angular.element('<div class="menu-drop-marker" style="display: none"></div>');
                            documentBody.append(dropMarker);

                            $document.bind('mousemove', onDragged);
                            $document.bind('mouseup', onDragFinished);
                        });

                        event.preventDefault();
                    });

                    function onDragged(event) {

                        targetScope = null;
                        targetItem = null;
                        floatie.addClass('invalid');
                        floatie.css('display', 'block');
                        dropMarker.css('display', 'none');

                        var x = event.clientX;
                        var y = event.clientY;
                        floatie.css('left', (x + 2) + 'px');
                        floatie.css('top', (y + 2) + 'px');

                        var dropTarget = angular.element($document[0].elementFromPoint(x, y));
                        var targetBlock = $(dropTarget).closest('.menu-item');
                        if (targetBlock.length > 0) {
                            targetBlock = targetBlock[0];

                            // check for recursive parent
                            var isRecursive = false;
                            var item = scope.item;
                            var elem = dropTarget;
                            var parent = dropTarget.scope().item;
                            while (parent) {
                                if (item === parent) {
                                    isRecursive = true;
                                    break;
                                }
                                elem = elem.parent();
                                parent = elem.scope().item;
                            }

                            if (!isRecursive) {
                                targetScope = dropTarget.scope();
                                targetItem = targetScope.item;
                                floatie.removeClass('invalid');
                                dropMarker.css('display', 'block');

                                var blockBounds = targetBlock.getBoundingClientRect();
                                dropMarker.css('left', blockBounds.left + 'px');
                                dropMarker.css('width', blockBounds.width + 'px');
                                var yInBlock = y - blockBounds.top;
                                if (yInBlock < 10) {
                                    targetPos = 'before';
                                    dropMarker.css('top', blockBounds.top + 'px');
                                } else if (yInBlock > blockBounds.height - 10) {
                                    targetPos = 'after';
                                    dropMarker.css('top', (blockBounds.top + blockBounds.height) + 'px');
                                } else {
                                    targetPos = 'inside';
                                    dropMarker.css('top', (blockBounds.top + (blockBounds.height / 2)) + 'px');
                                }
                            }
                        } else if (dropTarget.hasClass('menu-body')) {
                            targetScope = dropTarget.scope();
                            if (targetScope.menu.items == null || targetScope.menu.items.length == 0) {
                                var blockBounds = dropTarget[0].getBoundingClientRect();
                                console.log('BLOCK: ' + JSON.stringify(blockBounds));
                                floatie.removeClass('invalid');
                                dropMarker.css('display', 'block');
                                targetPos = 'menu';
                                targetItem = null;
                                dropMarker.css('top', (blockBounds.top + 1) + 'px');
                                dropMarker.css('left', (blockBounds.left + 4) + 'px');
                                dropMarker.css('width', (blockBounds.width - 8) + 'px');
                            }
                        }
                    }

                    function onDragFinished() {

                        var item = scope.item;

                        if (targetPos == 'menu' || (targetItem && item && item.id != targetItem.id)) {

                            // add to new parent

                            var newIndex;
                            var newItems;
                            var newParentId;
                            var oldMenuId = scope.menu.id;
                            var newMenu = targetScope.menu;

                            var oldItems;
                            var oldParentId;
                            if (scope.parent) {
                                oldItems = scope.parent.items;
                                oldParentId = scope.parent.id;
                            } else {
                                oldItems = scope.menu.items;
                                oldParentId = null;
                            }
                            var oldIndex = oldItems.indexOf(item);

                            if (targetPos == 'menu') {

                                newItems = targetScope.menu.items;
                                newParentId = null;
                                newIndex = 0;

                            } else if (targetPos == 'before' || targetPos == 'after') {

                                var parent = targetScope.parent;
                                if (parent) {
                                    newItems = parent.items;
                                    newParentId = parent.id;
                                } else {
                                    newItems = targetScope.menu.items;
                                    newParentId = null;
                                }
                                newIndex = newItems.indexOf(targetItem);
                                newIndex += (targetPos == 'after' ? 1 : 0);

                            } else {
                                newParentId = targetItem.id;
                                newItems = targetItem.items;
                                newIndex = 0;
                                targetItem.expanded = true;
                            }

                            if (item.id != '_new') {
                                // save to server
                                MenuService.location({
                                    id: item.id,
                                    menuId: newMenu.id,
                                    parentItemId: newParentId,
                                    position: newIndex
                                }, function () {

                                    doMoveItem();

                                }, function() {

                                    floatie.remove();
                                    floatie = null;
                                    dropMarker.remove();
                                    dropMarker = null;

                                });
                            } else {

                                doMoveItem();
                            }

                        } else {
                            floatie.remove();
                            floatie = null;
                            dropMarker.remove();
                            dropMarker = null;
                        }

                        function doMoveItem() {
                            if (newMenu.id == oldMenuId && newParentId == oldParentId && newIndex > oldIndex) {
                                newIndex--;
                            }

                            // remove from existing parent
                            var index = oldItems.indexOf(item);
                            oldItems.splice(index, 1);

                            newItems.splice(newIndex, 0, item);

                            floatie.remove();
                            floatie = null;
                            dropMarker.remove();
                            dropMarker = null;
                        }


                        $document.unbind('mousemove', onDragged);
                        $document.unbind('mouseup', onDragFinished);

                        scope.$apply();
                    }
                });
            }
        };
    });
