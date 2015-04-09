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

    .directive('ghContentEditor', function ($window, $document) {

        return {

            restrict: 'E',
            scope: {
                content: '=',
                context: '='
            },
            templateUrl: 'directive/content/content-editor.html',

            controller: function ($scope) {

                $scope.showGrid = false;
                $scope.hideMedia = true;
                $scope.hideFeeds = true;
                $scope.hideMemberships = true;
                $scope.hideDonations = true;
                $scope.hideAdvanced = true;


                var dragContext = {
                    dragging: false
                };
                $scope.dragContext = dragContext;

                var dropTarget = {
                    type: false
                };
                $scope.dropTarget = dropTarget;

                $scope.blockFunctions = {

                    startBlockDrag: function (params) {
                        console.log('Starting drag for block: ' + params.label);
                        dragContext.blockId = params.blockId;
                        dragContext.label = params.label;
                        dragContext.getBlock = params.createBlock;
                        dragContext.afterBlockAdded = params.afterBlockAdded;
                        dragContext.blockDragDone = params.blockDragDone;
                        dragContext.x = params.x;
                        dragContext.y = params.y;
                        dragContext.dragging = true;

                        disableSelect();
                        $document.bind('mousemove', blockDragged);
                        $document.bind('mouseup', blockDropped);
                        $scope.$apply();
                    },

                    context: $scope.context,

                    deleteCell: function(cell) {
                        var rows = $scope.content.rows;
                        for (var i = 0; i < rows.length; i++) {
                            var row = rows[i];
                            var index = row.cells.indexOf(cell);
                            if (index >= 0) {

                                if (index < row.cells.length - 1) {
                                    row.cells[index + 1].width += cell.width;
                                    row.cells[index + 1].start -= cell.width;
                                } else if (index > 0) {
                                    row.cells[index - 1].width += cell.width;
                                }

                                row.cells.splice(index, 1);
                                if (row.cells.length == 0) {
                                    $scope.content.rows.splice(i, 1);
                                }
                                break;
                            }
                        }
                    }

                };

                function blockDragged(event) {
                    dragContext.x = event.clientX;
                    dragContext.y = event.clientY;
                    $scope.$apply();
                    event.preventDefault();
                }

                function blockDropped(event) {
                    console.log('Block dragging finished');
                    if (dragContext.blockDragDone) {
                        dragContext.blockDragDone();
                    }

                    dragContext.dragging = false;

                    if (dropTarget.type == 'page') {

                        // drop a new row when no content
                        $scope.content.rows = [{
                            cells: [
                                {
                                    start: 0,
                                    width: 12,
                                    blocks: [  dragContext.getBlock() ]
                                }
                            ]
                        }];

                        if (dragContext.afterBlockAdded) {
                            dragContext.afterBlockAdded();
                        }

                    } else if (dropTarget.type == 'row') {

                        // drop a new row

                        $scope.content.rows.splice(dropTarget.index, 0, {
                            cells: [
                                {
                                    start: 0,
                                    width: 12,
                                    blocks: [ dragContext.getBlock() ]
                                }
                            ]
                        });

                        if (dragContext.afterBlockAdded) {
                            dragContext.afterBlockAdded();
                        }

                    } else if (dropTarget.type == 'cell') {

                        // drop a new cell

                        console.log('Adding new cell at: ' + dropTarget.index);
                        var space = 0;

                        // find space looking forward
                        var currIndex = dropTarget.index;
                        while (currIndex < dropTarget.list.length && space < 2) {
                            var nextCell = dropTarget.list[currIndex];
                            if (nextCell.width > 2) {
                                nextCell.width -= 2;
                                space += 2;
                            } else if (nextCell.width > 1) {
                                nextCell.width -= 1;
                                space += 1;
                            }
                            currIndex++;
                        }

                        // find space looking backward
                        currIndex = dropTarget.list.length - 1;
                        while (currIndex >= 0 && space < 1) {
                            var prevCell = dropTarget.list[currIndex];
                            if (prevCell.width > 2) {
                                prevCell.width -= 2;
                                space += 2;
                            } else if (prevCell.width > 1) {
                                prevCell.width -= 1;
                                space += 1;
                            }
                            currIndex--;
                        }

                        // add new cell if there's space
                        if (space > 0) {
                            dropTarget.list.splice(dropTarget.index, 0, {
                                start: 0,
                                width: space,
                                blocks: [ dragContext.getBlock() ]
                            });

                            // re-adjust starts
                            for (var i = 1; i < dropTarget.list.length; i++) {
                                var prevCell = dropTarget.list[i - 1];
                                var start = prevCell.start + prevCell.width;
                                console.log('New start: i=' + i + ' s=' + prevCell.start
                                    + ', w=' + prevCell.width + ', ns=' + start);
                                dropTarget.list[i].start = start;
                            }
                        } else {
                            $window.alert('This row is full');
                        }

                        if (dragContext.afterBlockAdded) {
                            dragContext.afterBlockAdded();
                        }

                    } else if (dropTarget.type == 'block') {

                        dropTarget.list.splice(dropTarget.index, 0, dragContext.getBlock());

                        if (dragContext.afterBlockAdded) {
                            dragContext.afterBlockAdded();
                        }
                    }

                    // prune any empty rows and cells

                    var rows = $scope.content.rows;
                    for (var rowIndex = 0; rowIndex < rows.length; rowIndex++) {
                        var row = rows[rowIndex];
                        for (var cellIndex = 0; cellIndex < row.cells.length; cellIndex++) {
                            var cell = row.cells[cellIndex];
                            if (cell.blocks.length == 0) {

                                // redistribute space
                                if (cellIndex < row.cells.length - 1) {
                                    var nextCell = row.cells[cellIndex + 1];
                                    nextCell.start = cell.start;
                                    nextCell.width = nextCell.width + cell.width;
                                } else if (cellIndex > 0) {
                                    var prevCell = row.cells[cellIndex - 1];
                                    prevCell.width = prevCell.width + cell.width;
                                }

                                // prune block
                                row.cells.splice(cellIndex, 1);
                                cellIndex--;
                            }
                        }

                        if (row.cells.length == 0) {
                            // prune row
                            rows.splice(rowIndex, 1);
                            rowIndex--;
                        }
                    }


                    $scope.$apply();

                    enableSelect();
                    $document.unbind('mousemove', blockDragged);
                    $document.unbind('mouseup', blockDropped);
                    event.preventDefault();
                }

                var documentBody = angular.element($document[0].body);

                var disableSelect = function () {
                    documentBody.css({
                        '-moz-user-select': '-moz-none',
                        '-khtml-user-select': 'none',
                        '-webkit-user-select': 'none',
                        '-ms-user-select': 'none',
                        'user-select': 'none'
                    });
                };

                var enableSelect = function () {
                    documentBody.css({
                        '-moz-user-select': '',
                        '-khtml-user-select': '',
                        '-webkit-user-select': '',
                        '-ms-user-select': '',
                        'user-select': ''
                    });
                };
            },

            link: function (scope, elem, attrs) {

                scope.$watch('dragContext', function (newValue, oldValue) {

                    var dragContext = scope.dragContext;
                    var dropTarget = scope.dropTarget;

                    // find drop position

                    dropTarget.type = null;

                    if (scope.dragContext.dragging) {
                        var elemBounds = elem.find('.content-wrapper')[0].getBoundingClientRect();
                        if (dragContext.x > elemBounds.left
                            && dragContext.x < elemBounds.right
                            && dragContext.y > elemBounds.top
                            && dragContext.y < elemBounds.bottom) {

                            var rowElems = elem.find('gh-row');
                            for (var rowIndex = 0; rowIndex < rowElems.length; rowIndex++) {
                                var rowElem = rowElems[rowIndex];
                                var row = $(rowElem).scope().row;
                                var rowBounds = rowElem.getBoundingClientRect();

                                if (dragContext.y >= rowBounds.top && dragContext.y <= rowBounds.top + rowBounds.height) {

                                    // we are inside a row - find the best cell

                                    var cellElems = angular.element(rowElem).find('.content-cell');
                                    for (var cellIndex = 0; cellIndex < cellElems.length; cellIndex++) {
                                        var cellElem = cellElems[cellIndex];
                                        var cell = $(cellElem).scope().cell;
                                        var cellBounds = cellElem.getBoundingClientRect();
                                        if (cellBounds.left < dragContext.x && dragContext.x < cellBounds.right) {

                                            // we are inside a cell - find the best block
                                            var blockElems = angular.element(cellElem).find('.content-block');
                                            // only use block alignments if more than one cell in row
                                            if (cellElems.length > 1) {
                                                for (var blockIndex = 0; blockIndex < blockElems.length; blockIndex++) {
                                                    var blockElem = blockElems[blockIndex];
                                                    var blockBounds = blockElem.getBoundingClientRect();

                                                    var blockId = $(blockElem).scope().id;
                                                    if (blockBounds.top < dragContext.y && dragContext.y < blockBounds.bottom + 3) {

                                                        if (blockId == dragContext.blockId) {
                                                            return;
                                                        }

                                                        // inside a block, find the nearest edge
                                                        var pos = findNearestEdge({ x: dragContext.x, y: dragContext.y }, blockBounds);
                                                        switch (pos) {
                                                            case 'top':
                                                                dropTarget.type = 'block';
                                                                dropTarget.list = cell.blocks;
                                                                dropTarget.index = blockIndex;
                                                                dropTarget.markerBounds = {
                                                                    top: blockBounds.top - 2,
                                                                    left: blockBounds.left - 2,
                                                                    width: blockBounds.width + 4,
                                                                    height: 4
                                                                };
                                                                break;
                                                            case 'left':
                                                                dropTarget.type = 'cell';
                                                                dropTarget.list = row.cells;
                                                                dropTarget.index = cellIndex;
                                                                dropTarget.markerBounds = {
                                                                    top: cellBounds.top - 2,
                                                                    left: cellBounds.left - 2,
                                                                    width: 4,
                                                                    height: rowBounds.height + 4
                                                                };
                                                                break;
                                                            case 'bottom':
                                                                dropTarget.type = 'block';
                                                                dropTarget.list = cell.blocks;
                                                                dropTarget.index = blockIndex + 1;
                                                                dropTarget.markerBounds = {
                                                                    top: blockBounds.bottom - 2,
                                                                    left: blockBounds.left - 2,
                                                                    width: blockBounds.width + 4,
                                                                    height: 4
                                                                };
                                                                break;
                                                            case 'right':
                                                                dropTarget.type = 'cell';
                                                                dropTarget.list = row.cells;
                                                                dropTarget.index = cellIndex + 1;
                                                                dropTarget.markerBounds = {
                                                                    top: cellBounds.top - 2,
                                                                    left: cellBounds.right - 2,
                                                                    width: 4,
                                                                    height: rowBounds.height + 4
                                                                };
                                                                break;
                                                        }
                                                    }
                                                }
                                            }

                                            if (dropTarget.type == null) {

                                                // we are inside a cell but not over a block, find nearest edge
                                                var checkBounds = {
                                                    top: cellBounds.top,
                                                    left: cellBounds.left,
                                                    bottom: rowBounds.bottom,
                                                    right: cellBounds.right
                                                };

                                                // adjust top to be last block
                                                var usingLastBlock = false;
                                                var lastBlockElem = null;
                                                var lastBlockBounds = null;
                                                if (blockElems.length > 0 && cellElems.length > 1) {
                                                    lastBlockElem = blockElems[blockElems.length - 1];
                                                    lastBlockBounds = lastBlockElem.getBoundingClientRect();
                                                    if (dragContext.y > lastBlockBounds.bottom) {
                                                        console.log('Adjusting top from ' + checkBounds.top + " to " + lastBlockBounds.bottom);
                                                        checkBounds.top = lastBlockBounds.bottom;
                                                        usingLastBlock = true;
                                                    }
                                                }

                                                var pos = findNearestEdge({ x: dragContext.x, y: dragContext.y }, checkBounds);
                                                console.log('Nearest edge is: ' + pos);
                                                switch (pos) {
                                                    case 'top':
                                                        if (usingLastBlock) {
                                                            dropTarget.type = 'block';
                                                            dropTarget.list = cell.blocks;
                                                            dropTarget.index = cell.blocks.length;
                                                            dropTarget.markerBounds = {
                                                                top: lastBlockBounds.bottom - 2,
                                                                left: lastBlockBounds.left - 2,
                                                                width: lastBlockBounds.width + 4,
                                                                height: 4
                                                            };
                                                        } else {
                                                            dropTarget.type = 'row';
                                                            dropTarget.index = rowIndex;
                                                            dropTarget.markerBounds = {
                                                                top: rowBounds.top - 2,
                                                                left: rowBounds.left - 2,
                                                                width: rowBounds.width + 4,
                                                                height: 4
                                                            };
                                                        }
                                                        break;
                                                    case 'left':
                                                        dropTarget.type = 'cell';
                                                        dropTarget.list = row.cells;
                                                        dropTarget.index = cellIndex;
                                                        dropTarget.markerBounds = {
                                                            top: cellBounds.top - 2,
                                                            left: cellBounds.left - 2,
                                                            width: 4,
                                                            height: rowBounds.height + 4
                                                        };
                                                        break;
                                                    case 'bottom':
                                                        dropTarget.type = 'row';
                                                        dropTarget.index = rowIndex + 1;
                                                        dropTarget.markerBounds = {
                                                            top: rowBounds.bottom - 2,
                                                            left: rowBounds.left - 2,
                                                            width: rowBounds.width + 4,
                                                            height: 4
                                                        };
                                                        break;
                                                    case 'right':
                                                        dropTarget.type = 'cell';
                                                        dropTarget.list = row.cells;
                                                        dropTarget.index = cellIndex + 1;
                                                        dropTarget.markerBounds = {
                                                            top: cellBounds.top - 2,
                                                            left: cellBounds.right - 2,
                                                            width: 4,
                                                            height: rowBounds.height + 4
                                                        };
                                                        break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (dropTarget.type == null) {

                                // we are not inside any rows, default to the bottom of the page
                                if (rowElems.length > 0) {
                                    var lastRowElem = rowElems[rowElems.length - 1];
                                    var lastRowBounds = lastRowElem.getBoundingClientRect();
                                    dropTarget.type = 'row';
                                    dropTarget.index = rowElems.length;
                                    dropTarget.markerBounds = {
                                        top: lastRowBounds.bottom - 2,
                                        left: lastRowBounds.left - 2,
                                        width: lastRowBounds.width + 4,
                                        height: 4
                                    };

                                } else {

                                    dropTarget.type = 'page';
                                    dropTarget.markerBounds = {
                                        top: elemBounds.top,
                                        left: elemBounds.left,
                                        width: elemBounds.width,
                                        height: 4
                                    };

                                }

                            }
                        }
                    }

                }, true);

                function findNearestEdge(pos, bounds) {
                    var distFromTop = pos.y - bounds.top;
                    var distFromLeft = pos.x - bounds.left;
                    var distFromBottom = bounds.bottom - pos.y;
                    var distFromRight = bounds.right - pos.x;
                    var dists = [distFromTop, distFromLeft, distFromBottom, distFromRight ];
                    if (isSmallest(distFromTop, dists)) {
                        return "top";
                    } else if (isSmallest(distFromLeft, dists)) {
                        return "left";
                    } else if (isSmallest(distFromBottom, dists)) {
                        return "bottom";
                    } else {
                        return "right";
                    }
                }

                function isSmallest(num, nums) {
                    for (var i = 0; i < nums.length; i++) {
                        var next = nums[i];
                        if (next < num) {
                            return false;
                        }
                    }
                    return true;
                }


            }
        };
    });
