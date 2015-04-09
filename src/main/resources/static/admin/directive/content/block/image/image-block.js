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

    .controller('ImageBlock', ['$scope', '$modal', 'SearchService', function ($scope, $modal, SearchService) {

        $scope.chooseImage = function () {

            var controller = function ($scope, $modalInstance, FileUploader, block, context) {
                $scope.details = {
                    url : block.value.url,
                    altText : block.value.altText,
                    href: block.value.href,
                    newPage: block.value.newPage
                };

                $scope.uploader = new FileUploader({
                    url: context.attachmentUrl,
                    onAfterAddingFile: function(item) {
                        if (this.queue.length > 1) {
                            this.queue.splice(0, 1);
                        }
                    }
                });

                $scope.clickUpload = function(){
                    angular.element('input#upload-field').trigger('click');
                };

                $scope.results = SearchService.search({searchPhrase:''});

                $scope.tab = 'upload';
                $scope.selectTab = function (tabName) {
                    $scope.tab = tabName;
                };

                $scope.ok = function () {

                    var tab = $scope.tab;

                    if (tab == 'upload') {

                        $scope.uploader.onSuccessItem = function (item, response, status, headers) {
                            block.value.url = response;
                            $modalInstance.close();
                        };

                        block.value.href = $scope.details.href;
                        block.value.altText = $scope.details.altText;
                        block.value.newPage = $scope.details.newPage;
                        // Check if editing & close modal if so, otherwise upload image
                        if ($scope.uploader.queue.length) {
                            $scope.uploader.uploadAll();
                        } else {
                            $modalInstance.close();
                        }
                    } else if (tab == 'gallery') {

                        console.log('Gallery not yet supported');

                    } else if (tab == 'link') {
                        block.value.url = $scope.details.url;
                        $modalInstance.close();
                    }


                };
                $scope.cancel = function () {
                    $modalInstance.dismiss('cancel');
                };
            };
            var modalInstance = $modal.open({
                size: 'lg',
                templateUrl: 'directive/content/block/image/choose-image.html',
                controller: controller,
                resolve: {
                    block: function () {
                        return $scope.block
                    },
                    context: function () {
                        return $scope.blockFunctions.context
                    }
                }
            });
        };

        if ($scope.block.isNew) {
            $scope.chooseImage();
            delete $scope.block.isNew;
        }

    }])

    .controller('ImageTool', ['$scope', function ($scope) {


        $scope.blockFactory.getLabel = function (type) {
            return "Image"
        };

        $scope.blockFactory.createBlock = function (type) {
            return {
                type: type,
                isNew: true,
                value: {
                    url: null
                }
            }
        };


    }]);
