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

    .directive('ghCellDivider', function ($document) {

        return {
            restrict: 'E',
            scope: {
                row: "=",
                cell: "="
            },
            template:
                '<div class="cell-divider"'
                    + ' ng-style="{left: ((cell.start + cell.width) * 100 / 12) + \'%\'}"></div>',

            link: function (scope, elem, attrs) {

                var cells = scope.row.cells;

                var x = 0;
                var rowWidth = 0;
                var cellStart = 0;

                var leftCell = null;
                var rightCell = null;


                elem.bind('mousedown', function (event) {

                    elem.addClass('dragging');

                    var index = cells.indexOf(scope.cell);
                    leftCell = cells[index];
                    rightCell = cells[index + 1];

                    var parent = elem.parent()[0];
                    rowWidth = parent.getBoundingClientRect().width;

                    var columnWidth = rowWidth / 12;
                    cellStart = parent.getBoundingClientRect().left + (columnWidth * leftCell.start);

                    $document.bind('mousemove', moveDiv);
                    $document.bind('mouseup', mouseup);
                    event.preventDefault();
                });

                function moveDiv(event) {

                    x = event.clientX - cellStart;
                    var newWidth = Math.round(((x/rowWidth) * 12));
                    newWidth = Math.max(1, Math.min(12, newWidth));

                    var oldLeftWidth = leftCell.width;
                    var oldRightWidth = rightCell.width;

                    var diff = newWidth - oldLeftWidth;
                    diff = Math.min(diff, oldRightWidth - 1);

                    if (diff != 0) {

                        leftCell.width = oldLeftWidth + diff;
                        rightCell.width = oldRightWidth - diff ;

                        // adjust starting points
                        var start = 0;
                        for (var i = 0; i < cells.length; i++) {
                            var cell = cells[i];
                            cell.start = start;
                            start += cell.width;
                        }

                        scope.$apply();
                    }
                }

                function mouseup() {
                    elem.removeClass('dragging');
                    $document.unbind('mousemove', moveDiv);
                    $document.unbind('mouseup', mouseup);
                }
            }
        };
    });
