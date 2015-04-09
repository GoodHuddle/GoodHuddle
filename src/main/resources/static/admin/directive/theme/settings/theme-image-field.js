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

    .directive('ghThemeImageField', function (FileUploader) {

        return {
            restrict: 'E',
            scope: {
                settings: "=",
                fieldCode: "@",
                uploaders: "="
            },
            templateUrl: 'directive/theme/settings/theme-image-field.html',
            link: function ($scope, element, attrs) {

                $scope.uploader = new FileUploader({
                    url: '/api/theme/setting/attachment/' + $scope.fieldCode,
                    onAfterAddingFile: function(item) {
                        if (this.queue.length > 1) {
                            this.queue.splice(0, 1);
                        }
                        $scope.settings[$scope.fieldCode] = null;
                        $scope.hasImage = true;
                    }
                });

                $scope.clickUpload = function(){
                    element.find('input#upload-field').trigger('click');
                };

                $scope.removeImage = function(){
                    $scope.settings[$scope.fieldCode] = null;
                    $scope.uploader.clearQueue();
                    $scope.hasImage = false;
                };

                $scope.hasImage = $scope.settings && $scope.settings[$scope.fieldCode];
                $scope.$watch('settings', function() {
                    console.log('Settings changed');
                    $scope.hasImage = ($scope.settings && $scope.settings[$scope.fieldCode])
                        || $scope.uploader.queue.length > 0;
                }, true);

                $scope.uploaders.push({
                    upload: function(onFinished) {
                        var uploader = $scope.uploader;
                        if (uploader.queue.length) {
                            uploader.onCompleteAll = function() {
                                $scope.settings[$scope.fieldCode] = '/api/theme/setting/attachment/' + $scope.fieldCode;
                                $scope.uploader.clearQueue();
                                if (onFinished) {
                                    onFinished();
                                }
                            };
                            uploader.uploadAll();
                        } else {
                            if (onFinished) {
                                onFinished();
                            }
                        }
                    }
                });

            }
        };
    });

