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

    .controller('ImportMembersController', ['$scope', '$state', '$stateParams', 'FileUploader', 'TagService',
        function ($scope, $state, $stateParams, FileUploader, TagService) {

            $scope.availableTags = TagService.get();

            $scope.request = {tags : []};

            $scope.uploader = new FileUploader({

                url: '/api/member/import',

                onBeforeUploadItem: function(item) {
                    if ($scope.request.tags.length > 0) {
                        var tags = [];
                        $($scope.request.tags).each(function(i, item) {
                            tags.push(item.id);
                        });
                        item.formData = [{tags: tags}];
                    } else {
                        item.formData = [{tags: [-1]}];
                    }

                },

                onAfterAddingFile: function(item) {
                    if (this.queue.length > 1) {
                        this.queue.splice(0, 1);
                    }
                },

                onSuccessItem: function(item, response, status, headers) {
                    $scope.result = response;
                }
            });

            $scope.upload = function() {
                console.log('Uploading');
                var uploader = $scope.uploader;
                if (uploader.queue.length > 0) {
                    var file = uploader.queue[0];
                    file.upload();
                }
            };

        }]);
