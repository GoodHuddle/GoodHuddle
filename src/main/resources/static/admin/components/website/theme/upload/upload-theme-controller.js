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

    .controller('UploadThemeController', ['$scope', '$log', '$state', 'ThemeService', 'FileUploader',
        function ($scope, $log, $state, ThemeService, FileUploader) {

            $scope.uploader = new FileUploader({
                url: '/api/theme/bundle',
                onAfterAddingFile: function(item) {
                    if (this.queue.length > 1) {
                        this.queue.splice(0, 1);
                    }
                },
                onCompleteAll: function() {
                    $state.go('website.theme.current');
                }
            });

            $scope.upload = function() {
                var uploader = $scope.uploader;
                if (uploader.queue.length > 0) {
                    var file = uploader.queue[0];
                    file.upload();
                }
            };

        }]);
