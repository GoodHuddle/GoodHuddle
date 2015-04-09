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

    .controller('HtmlBlock', ['$scope', function ($scope) {

        // setup editor options
        $scope.editorOptions = {
            toolbar: 'simple',
            toolbar_simple: [
                { name: 'basicstyles', items: [ 'Format', 'Bold', 'Italic', 'Underline' ] },
                { name: 'paragraph', items: [ 'BulletedList', 'NumberedList', 'Blockquote' ] },
                { name: 'editing', items: ['JustifyLeft', 'JustifyCenter', 'JustifyRight' ] },
                { name: 'links', items: [ 'Ghlink', 'Unlink' ] },
                { name: 'tools', items: [ 'SpellChecker', 'Maximize' ] },
                { name: 'styles', items: [ 'FontSize', 'TextColor', 'PasteText', 'PasteFromWord' ] },
                { name: 'insert', items: [ 'Image', 'Table' ] },
                { name: 'clipboard', items: [ 'Undo', 'Redo' ] },
                { name: 'document', items: [ 'Source' ] }
            ]
        };

    }])

    .controller('HtmlTool', ['$scope', function ($scope) {

        $scope.blockFactory.getLabel = function (type) {
            return "Text"
        };

        $scope.blockFactory.createBlock = function (type) {
            return {
                type: type,
                value: {
                    html: 'A new text block'
                }
            }
        };

    }]);
