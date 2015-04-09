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

    .controller('BlogPostController',
        ['$rootScope', '$scope', '$state', '$stateParams', '$log', '$window', '$modal',
            'BlogService', 'BlogPostService', 'BlogPostCommentService', 'LayoutService', 'TagService', 'FileUploader',

            function ($rootScope, $scope, $state, $stateParams, $log, $window, $modal,
                      BlogService, BlogPostService, BlogPostCommentService, LayoutService, TagService, FileUploader) {

                var postId = $stateParams.postId;
                var originalPost;

                $scope.uploader = new FileUploader({
                    url: '/api/file',
                    autoUpload: true,
                    onAfterAddingFile: function(item) {
                        setModified(true);
                        if (this.queue.length > 1) {
                            this.queue.splice(0, 1);
                        }
                    },
                    onSuccessItem: function(item, response, status, headers) {
                        console.log('Image uploaded with response: ' + response);
                        $scope.post.featureImageId = response;
                        $scope.post.featureImageUrl = '/api/file/' + response;
                    }
                });

                $scope.layouts = LayoutService.query({type: 'blogPost'});

                setModified(false);

                $scope.context = {};

                function loadPost(postId) {
                    BlogPostService.get({id: postId}, function (post, getResponseHeaders) {
                        originalPost = angular.copy(post);
                        $scope.blog = BlogService.get({id: post.blogId});
                        $scope.post = post;
                        $scope.context.attachmentUrl = '/api/blog/post/' + post.id + '/attachment';
                        setModified(false);
                    });
                }

                // ui-select bug needs tags existing initially
                $scope.post = { title: '', tags: [] };

                loadPost(postId);

                /*
                $scope.comments =  BlogPostCommentService.search({
                    blogPostId: postId,
                    size: 1000
                });
                */

                $scope.$watch('post.title', function () {
                    if ($scope.post && !$scope.post.id) {
                        var slug = $scope.post.title;
                        slug = slug.toLowerCase().replace(/[^a-zA-Z0-9 ]/g, "").replace(/\s+/g, '-');
                        $scope.post.slug = slug;
                    }
                });


                $scope.$watch('post', function (newValue, oldValue) {
                    if (newValue === oldValue) {
                        return
                    }
                    if (!angular.equals($scope.post, originalPost)) {
                        setModified(true);
                    }
                }, true);

                window.onbeforeunload = function () {
                    if ($scope.modified) {
                        return "You have unsaved changes, do you want to discard them?";
                    }
                };

                $scope.cancel = function () {
                    var controller = function ($scope, $modalInstance, post) {
                        $scope.ok = function () {
                            $modalInstance.close();
                            angular.copy(originalPost, post);
                            setModified(false);
                        };
                        $scope.cancel = function () {
                            $modalInstance.dismiss('cancel');
                        };
                    };
                    var modalInstance = $modal.open({
                        templateUrl: 'components/website/blog/post/confirm-cancel.html',
                        controller: controller,
                        resolve: {
                            post: function () {
                                return $scope.post
                            }
                        }
                    });
                };

                $scope.deleteBlogPost = function () {
                    $modal.open({
                        templateUrl: 'components/website/blog/post/confirm-delete.html',
                        resolve: {
                            post: function () { return $scope.post }
                        },
                        controller: function ($scope, $modalInstance, post) {
                            $scope.post = post;
                            $scope.ok = function () {
                                BlogPostService.delete($scope.post, function() {
                                    $modalInstance.close();
                                    $state.go('website.blog', {blogId: post.blogId});
                                });
                            };
                            $scope.cancel = function () {
                                $modalInstance.dismiss('cancel');
                            };
                        }

                    });
                };


                $scope.chooseFeatureImage = function () {
                    angular.element('#feature-image').trigger('click');
                };

                $scope.deleteFeatureImage = function () {
                    var post = $scope.post;
                    post.featureImageUrl = null;
                    post.featureImageId = '_remove';
                };

                $scope.view = function () {
                    $window.open($scope.post.url);
                };

                $scope.close = function () {
                    $state.go('website.blog', { blogId: $scope.post.blogId } );
                };

                $scope.deletePost = function () {
                    var controller = function ($scope, $modalInstance, post) {
                        $scope.post = post;
                        $scope.ok = function () {
                            setModified(false);
                            post.$delete(function () {
                                $state.go('website.blog', { blogId: post.blogId });
                            });
                            $modalInstance.close();
                        };
                        $scope.cancel = function () {
                            $modalInstance.dismiss('cancel');
                        };
                    };
                    var modalInstance = $modal.open({
                        templateUrl: 'components/website/blog/post/confirm-delete.html',
                        controller: controller,
                        resolve: {
                            post: function () {
                                return $scope.post
                            }
                        }
                    });
                };

                $scope.save = function (publish) {

                    var tags = $scope.post.tags;
                    if (tags) {
                        var tagIds = [];
                        for (var i = 0; i < tags.length; i++) {
                            tagIds.push(tags[i].id);
                        }
                        $scope.post.tagIds = tagIds;
                    }

                    if (publish) {

                        console.log("Publishing post");

                        $modal.open({
                            templateUrl: 'components/website/blog/post/publish.html',
                            resolve: {
                                parentScope: function () { return $scope },
                                post: function () { return $scope.post },
                                uploader: function () { return $scope.uploader }
                            },
                            controller: function ($scope, $modalInstance, parentScope, post, uploader) {
                                $scope.post = post;
                                $scope.uploader = uploader;

                                $scope.chooseFeatureImage = function () {
                                    angular.element('#publish-feature-image').trigger('click');
                                };

                                $scope.ok = function () {
                                    $scope.saving = true;
                                    post.publish = true;
                                    BlogPostService.update(post, function (post) {
                                        $scope.saving = false;
                                        originalPost = angular.copy(post);
                                        parentScope.post = post;
                                        setModified(false);
                                        $modalInstance.dismiss('ok');
                                        $state.go('website.blog', { blogId: post.blogId });

                                    }, function () {
                                        $scope.saving = false;
                                    });
                                };
                                $scope.cancel = function () {
                                    $modalInstance.dismiss('cancel');
                                };
                            }

                        });

                    } else {
                        $scope.saving = true;
                        BlogPostService.update($scope.post, function (post) {
                            $scope.saving = false;
                            originalPost = angular.copy(post);
                            $scope.post = post;
                            setModified(false);

                        }, function () {
                            $scope.saving = false;
                        });
                    }
                };

                function setModified(modified) {
                    $scope.modified = modified;
                    if (modified) {
                        $rootScope.$navManager.interceptor = function (onSuccess) {
                            var controller = function ($scope, $modalInstance, post) {
                                $scope.ok = function () {
                                    $modalInstance.close();
                                    setModified(false);
                                    onSuccess();
                                };
                                $scope.cancel = function () {
                                    $modalInstance.dismiss('cancel');
                                };
                            };
                            var modalInstance = $modal.open({
                                templateUrl: 'components/website/blog/post/confirm-cancel.html',
                                controller: controller,
                                resolve: {
                                    post: function () {
                                        return $scope.post
                                    }
                                }
                            });
                        }
                    } else {
                        delete $rootScope.$navManager.interceptor;
                    }
                }

            }]);
