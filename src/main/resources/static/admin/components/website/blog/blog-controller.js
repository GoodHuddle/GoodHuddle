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

    .controller('BlogController',
        [
            '$rootScope', '$scope', '$state', '$stateParams', '$log', '$window', '$modal',
            'BlogService', 'BlogPostService', 'LayoutService',

            function ($rootScope, $scope, $state, $stateParams, $log, $window, $modal,
                      BlogService, BlogPostService, LayoutService) {

                var blogId = $stateParams.blogId;
                var page = $stateParams.page;
                page = page ? page : 0;
                var originalBlog;

                $scope.layouts = LayoutService.query({type: 'blog'});
                $scope.search = {};

                $scope.allowComments = [
                    { value: 'all', label: 'Anyone can comment' },
                    { value: 'members', label: 'Only members can comment' },
                    { value: 'none', label: 'Comments not allowed' }
                ];

                $scope.requireApprovals = [
                    { value: 'none', label: 'No approval required' },
                    { value: 'anonymous', label: 'For comments posted anonymously' },
                    { value: 'all', label: 'For all comments ' }
                ];

                $scope.$watch('search.phrase', function () {
                    $scope.posts = BlogPostService.search({
                        blogIds: [blogId],
                        phrase: $scope.search.phrase,
                        includeUnpublished: true,
                        page: page,
                        size: 10
                    });
                });

                setModified(false);
                var first = false;

                function loadBlog(blogId) {
                    BlogService.get({id: blogId}, function (blog, getResponseHeaders) {
                        first = true;
                        originalBlog = angular.copy(blog);
                        $scope.blog = blog;
                        setModified(false);
                    });
                    $scope.posts = BlogPostService.search({
                        blogIds: [blogId],
                        phrase: $scope.search.phrase,
                        includeUnpublished: true,
                        page: page,
                        size: 10
                    });
                }

                loadBlog(blogId);

                $scope.$watch('blog.title', function () {
                    if ($scope.blog) {
                        if ($scope.blog && !$scope.blog.id) {
                            var slug = $scope.blog.title;
                            slug = slug.toLowerCase().replace(/[^a-zA-Z0-9 ]/g, "").replace(/\s+/g, '-');
                            $scope.blog.slug = slug;
                        }
                    }
                });

                $scope.$watch('blog', function (newValue, oldValue) {
                    if (first) {
                        first = false;
                        return;
                    }

                    if (newValue === oldValue) {
                        return
                    }
                    if (newValue != oldValue) {
                        setModified(true);
                    }
                }, true);

                window.onbeforeunload = function () {
                    if ($scope.modified) {
                        return "You have unsaved changes, do you want to discard them?";
                    }
                };

                $scope.cancel = function () {
                    var controller = function ($scope, $modalInstance, blog) {
                        $scope.ok = function () {
                            $modalInstance.close();
                            angular.copy(originalBlog, blog);
                            first = true;
                            setModified(false);
                        };
                        $scope.cancel = function () {
                            $modalInstance.dismiss('cancel');
                        };
                    };
                    var modalInstance = $modal.open({
                        templateUrl: 'components/website/blog/confirm-cancel.html',
                        controller: controller,
                        resolve: {
                            blog: function () {
                                return $scope.blog
                            }
                        }
                    });
                };

                $scope.view = function () {
                    $window.open($scope.blog.url);
                };

                $scope.addPost = function () {
                    if ($rootScope.$navManager.interceptor) {
                        $rootScope.$navManager.interceptor(function() {
                            doAddPost();
                        })
                    } else {
                        doAddPost();
                    }
                };

                function doAddPost() {
                    $modal.open({
                        templateUrl: 'components/website/blog/post/create-blog-post.html',
                        resolve: {
                            blog: function () { return $scope.blog }
                        },
                        controller: function ($scope, $modalInstance, blog) {

                            $scope.blog = blog;
                            $scope.post = { blogId: blog.id };

                            $scope.$watch('post.title', function () {
                                var post = $scope.post;
                                if (post && post.title) {
                                    var slug = post.title;
                                    slug = slug.toLowerCase().replace(/[^a-zA-Z0-9 ]/g, "").replace(/\s+/g, '-');
                                    post.slug = slug;
                                }
                            });

                            $scope.ok = function () {
                                $scope.saving = true;
                                $scope.post.content = { rows: [] };
                                BlogPostService.save($scope.post, function (post) {
                                    $modalInstance.close();
                                    $state.go('website.blogPost', {blogId: blog.id, postId: post.id});
                                }, function () {
                                    $scope.saving = false;
                                });
                            };

                            $scope.cancel = function () {
                                $modalInstance.dismiss('cancel');
                            };
                        }
                    });
                }


                $scope.editPost = function (post) {
                    $state.go('website.blogPost',
                        { blogId: $scope.blog.id, postId: post.id });
                };

                $scope.viewPost = function (post) {
                    $window.open(post.url);
                };

                $scope.prevPage = function (post) {
                    $state.go('website.blog',
                        { id: $scope.blog.id, page: page - 1 });
                };

                $scope.nextPage = function (post) {
                    $state.go('website.blog',
                        { id: $scope.blog.id, page: page + 1 });
                };

                $scope.deleteBlog = function () {
                    console.log('Deleting blog');
                    var controller = function ($scope, $modalInstance, blog) {
                        $scope.blog = blog;
                        $scope.ok = function () {
                            setModified(false);
                            blog.$delete(function () {
                                $state.go('website.menu', {}, {location: 'replace'});
                            });
                            $modalInstance.close();
                        };
                        $scope.cancel = function () {
                            $modalInstance.dismiss('cancel');
                        };
                    };
                    var modalInstance = $modal.open({
                        templateUrl: 'components/website/blog/confirm-delete.html',
                        controller: controller,
                        resolve: {
                            blog: function () { return $scope.blog }
                        }
                    });
                };

                $scope.save = function () {
                    $scope.saving = true;
                    $scope.blog.$update(function () {
                        $scope.saving = false;
                        setModified(false);
                        originalBlog = angular.copy($scope.blog);
                    }, function () {
                        $scope.saving = false;
                    });
                };

                function setModified(modified) {
                    $scope.modified = modified;
                    if (modified) {
                        $rootScope.$navManager.interceptor = function (onSuccess) {
                            var controller = function ($scope, $modalInstance, blog) {
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
                                templateUrl: 'components/website/blog/confirm-cancel.html',
                                controller: controller,
                                resolve: {
                                    blog: function () {
                                        return $scope.blog
                                    }
                                }
                            });
                        }
                    } else {
                        delete $rootScope.$navManager.interceptor;
                    }
                }

            }]);
