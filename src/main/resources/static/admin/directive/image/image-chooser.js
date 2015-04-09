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

    .directive('ghImageChooser', function ($modal) {

        return {
            restrict: 'E',
            transclude: true,
            scope: {
                model: "="
            },
            templateUrl: 'directive/image/image-chooser.html',
            link: function (scope, elem, attrs) {
                scope.chooseImage = function () {

                    console.log('Showing choose image modal');

                    var controller = function ($scope, $modalInstance, FileUploader, model) {

                        $scope.details = { url : model.url };

                        $scope.uploader = new FileUploader({
                            url: '/api/image',
                            onAfterAddingFile: function(item) {
                                if (this.queue.length > 1) {
                                    this.queue.splice(0, 1);
                                }
                            },
                            onCompleteAll: function() {
                            }
                        });

                        $scope.tab = 'upload';
                        $scope.selectTab = function (tabName) {
                            $scope.tab = tabName;
                        };

                        $scope.ok = function () {

                            var tab = $scope.tab;

                            if (tab == 'upload') {

                            } else if (tab == 'gallery') {

                            } else if (tab == 'link') {
                                console.log('Setting image URL to: ' + $scope.details.url);
                                model.url = $scope.details.url;
                            }

                            $modalInstance.close();

                        };
                        $scope.cancel = function () {
                            $modalInstance.dismiss('cancel');
                        };
                    };
                    var modalInstance = $modal.open({
                        size: 'lg',
                        templateUrl: 'directive/image/choose-image.html',
                        controller: controller,
                        resolve: {
                            model: function () {
                                return scope.model
                            }
                        }
                    });
                };
            }
        };
    });

