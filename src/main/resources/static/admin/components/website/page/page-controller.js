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

    .controller('PageController',
        ['$rootScope', '$scope', '$state', '$stateParams', '$log', '$window', '$modal',
            'PageService', 'LayoutService', '$cookies', '$cookieStore',
            function ($rootScope, $scope, $state, $stateParams, $log, $window, $modal,
                      PageService, LayoutService, $cookies, $cookieStore) {

                var pageId = $stateParams.pageId;
                var originalPage;

                $scope.layouts = LayoutService.query({type: 'page'});

                setModified(false);
                var first = false;

                $scope.context = {};

                function loadPage(pageId) {
                    PageService.get({id: pageId}, function (page, getResponseHeaders) {
                        first = true;
                        originalPage = angular.copy(page);
                        $scope.page = page;
                        $scope.context.attachmentUrl = '/api/page/' + page.id + '/attachment';
                        setModified(false);
                    });
                }

                loadPage(pageId);

                $scope.$watch('page.title', function () {
                    if ($scope.page) {
                        if ($scope.page && !$scope.page.id) {
                            var slug = $scope.page.title;
                            slug = slug.toLowerCase().replace(/[^a-zA-Z0-9 ]/g, "").replace(/\s+/g, '-');
                            $scope.page.slug = slug;
                        }
                    }
                });

                $scope.$watch('page', function (newValue, oldValue) {
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
                    var controller = function ($scope, $modalInstance, page) {
                        $scope.ok = function () {
                            $modalInstance.close();
                            angular.copy(originalPage, page);
                            first = true;
                            setModified(false);
                        };
                        $scope.cancel = function () {
                            $modalInstance.dismiss('cancel');
                        };
                    };
                    var modalInstance = $modal.open({
                        templateUrl: 'components/website/page/confirm-cancel.html',
                        controller: controller,
                        resolve: {
                            page: function () {
                                return $scope.page
                            }
                        }
                    });
                };

                $scope.view = function () {
                    $window.open($scope.page.url);
                };

                $scope.preview = function () {
                    $scope.page.$preview(function () {
                        $window.open('/page/preview');
                    });
                };

                $scope.deletePage = function () {
                    console.log('Deleting page');
                    var controller = function ($scope, $modalInstance, page) {
                        $scope.page = page;
                        $scope.ok = function () {
                            setModified(false);
                            page.$delete(function () {
                                $state.go('website.menu', {}, {location: 'replace'});
                            });
                            $modalInstance.close();
                        };
                        $scope.cancel = function () {
                            $modalInstance.dismiss('cancel');
                        };
                    };
                    var modalInstance = $modal.open({
                        templateUrl: 'components/website/page/confirm-delete.html',
                        controller: controller,
                        resolve: {
                            page: function () {
                                return $scope.page
                            }
                        }
                    });
                };

                $scope.save = function () {
                    $scope.saving = true;
                    $scope.page.$update(function () {
                        $scope.saving = false;
                        setModified(false);
                        originalPage = angular.copy($scope.page);
                    }, function () {
                        $scope.saving = false;
                    });
                };

                function setModified(modified) {
                    $scope.modified = modified;
                    if (modified) {
                        $rootScope.$navManager.interceptor = function (onSuccess) {
                            var controller = function ($scope, $modalInstance, page) {
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
                                templateUrl: 'components/website/page/confirm-cancel.html',
                                controller: controller,
                                resolve: {
                                    page: function () {
                                        return $scope.page
                                    }
                                }
                            });
                        }
                    } else {
                        delete $rootScope.$navManager.interceptor;
                    }
                }


                // getting started tips

                var currentHighlightContainer = null;
                var currentHighlight = null;
                function highlight(container, target) {
                    clearHighlight();
                    if (target) {
                        currentHighlight = $(target);
                        currentHighlight.addClass('tip-highlight');
                    } else {
                        currentHighlight = null;
                    }
                    if (container) {
                        currentHighlightContainer = $(container);
                        currentHighlightContainer.addClass('tip-highlight-container');
                    } else {
                        currentHighlightContainer = null;
                    }
                }

                function clearHighlight() {
                    if (currentHighlightContainer) {
                        currentHighlightContainer.removeClass('tip-highlight-container');
                    }
                    if (currentHighlight) {
                        currentHighlight.removeClass('tip-highlight');
                    }
                }

                var tip = {
                    showStartTip: function() {
                        clearHighlight();
                        tip.src = 'components/website/page/tips/start.html';
                        tip.style={'top': '20px', left: '50%', 'margin-left': '-200px'};
                    },

                    showTitleTip: function() {
                        highlight('.header-bar', '.title-field');
                        tip.src = 'components/website/page/tips/title.html';
                        tip.style={'top': '170px', left: '100px'};
                    },

                    showContentTip: function() {
                        highlight('.content-wrapper', '.content-wrapper');
                        tip.src = 'components/website/page/tips/content.html';
                        tip.style={'top': '10px', left: '140px', width: '600px'};
                    },

                    showToolsTip: function() {
                        highlight('.content-toolbar', '.content-toolbar');
                        tip.src = 'components/website/page/tips/tools.html';
                        tip.style={'top': '10px', right: '40px'};
                    },

                    showFinalTip: function() {
                        clearHighlight();
                        tip.src = 'components/website/page/tips/final.html';
                        tip.style={'top': '20px', left: '50%', 'margin-left': '-200px'};
                    },

                    dismiss: function() {
                        clearHighlight();
                        $scope.tip.show = false;
                        $cookies.pageHelpDone = true;
                    }
                };
                $scope.tip = tip;


                // if this is the first time the user has been here, start the tips

                var pageHelpDone = $cookies.pageHelpDone;
                if (!pageHelpDone) {
                    tip.showStartTip();
                    $scope.tip.show = true;
                }

                $scope.showTips = function() {
                    tip.showStartTip();
                    $scope.tip.show = true;
                };

            }]);
