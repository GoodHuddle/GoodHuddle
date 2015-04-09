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

    .controller('RawHtmlBlock', ['$scope', '$modal', function ($scope, $modal) {

        $scope.enterHtml = function () {

            var controller = function ($scope, $modalInstance, block) {
                $scope.details = {
                    html : block.value.html || null
                };

                ace.require("ace/ext/language_tools");

                $scope.aceLoaded = function(_editor){
                    _editor.setTheme("ace/theme/chrome");
                    _editor.setOptions({
                        showGutter: true,
                        enableBasicAutocompletion: true,
                        enableSnippets: true,
                        enableLiveAutocompletion: true,
                    });
                    _editor.getSession().setMode("ace/mode/html");
                };

                $scope.ok = function () {
                    block.value.html = $scope.details.html
                    $modalInstance.close();
                };

                $scope.cancel = function () {
                    $modalInstance.dismiss('cancel');
                };
            };

            var modalInstance = $modal.open({
                size: 'lg',
                templateUrl: 'directive/content/block/raw-html/enter-html-ace.html',
                controller: controller,
                resolve: {
                    block: function () {
                        return $scope.block;
                    },
                }
            });
        };

        if ($scope.block.isNew) {
            $scope.enterHtml();
            delete $scope.block.isNew;
        }
    }])

    .controller('RawHtmlTool', ['$scope', function ($scope) {


        $scope.blockFactory.getLabel = function (type) {
            return "Raw Html";
        };

        $scope.blockFactory.createBlock = function (type) {
            return {
                type: type,
                isNew: true,
                value: {
                    html: null
                }
            }
        };
    }]);


