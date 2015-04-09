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

    .controller('PagesController', ['$rootScope', '$scope', '$log', '$state', '$stateParams', '$modal',
        'MenuService', 'ThemeService', 'PageService', 'BlogService',
        function ($rootScope, $scope, $log, $state, $stateParams, $modal,
                  MenuService, ThemeService, PageService, BlogService) {

            $scope.menuSelection = {};
            $scope.dragContext = {};

            $scope.theme = ThemeService.active();
            $scope.mainMenu = MenuService.get({id: 'main'});
            $scope.unlinkedMenu = MenuService.get({id: 'unlinked'});


            // watch for menu selections

            $scope.$watch('menuSelection.item', function() {
                var item = $scope.menuSelection.item;
                if (item) {
                    if (item.targetType == 'page') {
                        $state.go('website.page', {pageId: item.targetId});
                    } else if (item.targetType == 'blog') {
                        $state.go('website.blog', {blogId: item.targetId});
                    }
                }
            });


            // Create page functions

            $scope.createPage = function (menu) {
                dismissDropDowns();
                if ($rootScope.$navManager.interceptor) {
                    $rootScope.$navManager.interceptor(function() {
                        doCreatePage(menu);
                    })
                } else {
                    doCreatePage(menu);
                }
            };

            function doCreatePage(menu) {
                $modal.open({
                    templateUrl: 'components/website/page/create-page.html',
                    resolve: {
                        menu: function () { return menu }
                    },
                    controller: function ($scope, $modalInstance) {

                        var parentItem = findSelectedMenuItem(menu);
                        $scope.page = baseMenuItemRequest(menu, parentItem);
                        bindUrlToTitle($scope, 'page', $scope.page);

                        $scope.ok = function () {
                            $scope.saving = true;
                            $scope.page.content = { rows: [] };
                            PageService.save($scope.page, function (page) {
                                $modalInstance.close();
                                addMenuItem(menu, parentItem, page, 'page');
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


            // Create blog functions

            $scope.createBlog = function (menu) {
                dismissDropDowns();
                if ($rootScope.$navManager.interceptor) {
                    $rootScope.$navManager.interceptor(function() {
                        doCreateBlog(menu);
                    })
                } else {
                    doCreateBlog(menu);
                }
            };

            function doCreateBlog(menu) {
                $modal.open({
                    templateUrl: 'components/website/blog/create-blog.html',
                    resolve: {
                        menu: function () { return menu }
                    },
                    controller: function ($scope, $modalInstance) {

                        var parentItem = findSelectedMenuItem(menu);
                        $scope.blog = baseMenuItemRequest(menu, parentItem);
                        bindUrlToTitle($scope, 'blog', $scope.blog);

                        $scope.ok = function () {
                            $scope.saving = true;
                            BlogService.save($scope.blog, function (blog) {
                                $modalInstance.close();
                                addMenuItem(menu, parentItem, blog, 'blog');
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



            // menu management

            function baseMenuItemRequest(menu, parentItem) {
                return {
                    menuId: menu.id,
                    parentItemId: parentItem != null ? parentItem.id : null,
                    position: parentItem != null ? parentItem.items.length : menu.items.length
                };
            }

            function findSelectedMenuItem(menu) {
                if ($scope.menuSelection.item) {
                    if ($scope.menuSelection.item.menuId == menu.id) {
                        return $scope.menuSelection.item;
                    }
                }
                return null;
            }

            function addMenuItem(menu, parentItem, target, type) {
                var item = {
                    id: target.menuItemId,
                    menuId: menu.id,
                    label: target.title,
                    targetType: type,
                    targetId: target.id
                };
                var items = parentItem != null ? parentItem.items : menu.items;
                items.splice(items.length, 0, item);

                if (parentItem != null) {
                    parentItem.expanded = true;
                }
                $scope.menuSelection.item = item;
            }

            function bindUrlToTitle(scope, type, target) {
                scope.$watch(type + '.title', function () {
                    if (target && target.title) {
                        var slug = target.title;
                        slug = slug.toLowerCase().replace(/[^a-zA-Z0-9 ]/g, "").replace(/\s+/g, '-');
                        target.slug = slug;
                    }
                });
            }

            function dismissDropDowns() {
                $scope.mainNavDropdown.isopen = false;
                $scope.unlinkedNavDropdown.isopen = false;
            }

        }]);
