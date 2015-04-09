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

    .controller('BlogFeedBlock', ['$scope', '$modal', function ($scope, $modal) {

        var value = $scope.block.value;
        $scope.posts = new Array(value.numPosts);
        $scope.$watch('block.value.numPosts', function (newValue, oldValue) {
            $scope.posts.length = newValue;
        });

        $scope.editSettings = function () {

            var controller = function ($scope, $modalInstance, FileUploader, BlogService, block) {

                $scope.details = {
                    blogId: block.value.blogId,
                    numPosts : parseInt(block.value.numPosts),
                    title : block.value.title,
                    showTitle : block.value.showTitle,
                    showFeatureImage : block.value.showFeatureImage,
                    showAuthor : block.value.showAuthor,
                    showDatePublished : block.value.showDatePublished,
                    showMoreNewsLink : block.value.showMoreNewsLink
                };

                $scope.blogSelectOptions = {
                    multiple: true,
                    allowClear: true
                };

                $scope.availableBlogs = [];
                BlogService.query(function(blogs) {
                    $scope.availableBlogs = blogs;
                });

                $scope.ok = function () {
                    block.value.blogId = $scope.details.blogId;
                    block.value.numPosts = $scope.details.numPosts;
                    block.value.title = $scope.details.title;
                    block.value.showTitle = $scope.details.showTitle;
                    block.value.showFeatureImage = $scope.details.showFeatureImage;
                    block.value.showAuthor = $scope.details.showAuthor;
                    block.value.showDatePublished = $scope.details.showDatePublished;
                    block.value.showMoreNewsLink = $scope.details.showMoreNewsLink;
                    $modalInstance.close();
                };
                $scope.cancel = function () {
                    $modalInstance.dismiss('cancel');
                };
            };
            var modalInstance = $modal.open({
                templateUrl: 'directive/content/block/blog-feed/edit-settings.html',
                controller: controller,
                resolve: {
                    block: function () {
                        return $scope.block
                    }
                }
            });
        };

        if ($scope.block.isNew) {
            $scope.editSettings();
            delete $scope.block.isNew;
        }

    }])


    .controller('BlogFeedTool', ['$scope', function ($scope) {

        $scope.blockFactory.getLabel = function (type) {
            return "Blog feed"
        };

        $scope.blockFactory.createBlock = function (type) {
            return {
                type: type,
                isNew: true,
                value: {
                    numPosts: 5,
                    showTitle: true,
                    showAuthor: true,
                    showDatePublished: true,
                    showMoreNewsLink: true
                }
            }
        };

    }]);
