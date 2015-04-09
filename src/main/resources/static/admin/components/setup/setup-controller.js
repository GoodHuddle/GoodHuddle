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


    .controller('SetupStartController', ['$scope', '$log', '$state',
        function ($scope, $log, $state) {

            $scope.next = function () {
                $state.go('setupGeneral');
            };

        }])


    .controller('SetupGeneralController', ['$scope', '$log', '$state', 'HuddleService',
        function ($scope, $log, $state, HuddleService) {

            $scope.huddle = HuddleService.get();

            $scope.back = function () {
                $state.go('setupStart');
            };

            $scope.next = function () {
                $scope.saving = true;
                HuddleService.update($scope.huddle, function () {
                    $scope.saving = false;
                    $state.go('setupTheme');
                });
            }

        }])


    .controller('SetupThemeController', ['$scope', '$log', '$state', '$modal', 'ThemeBundleService',
        function ($scope, $log, $state, $modal, ThemeBundleService) {

            $scope.selected = { bundle: null };
            $scope.bundles = ThemeBundleService.query();

            $scope.selectBundle = function (bundle) {
                $scope.selected.bundle = bundle;
            };

            $scope.preview = function (bundle) {
                $modal.open({
                    templateUrl: 'components/website/theme/choose/preview-theme-bundle.html',
                    resolve: {
                        bundle: function () {
                            return bundle
                        },
                        selected: function () {
                            return $scope.selected
                        }
                    },
                    controller: function ($scope, $modalInstance, bundle, selected) {
                        $scope.bundle = bundle;
                        $scope.ok = function () {
                            selected.bundle = bundle;
                            $modalInstance.dismiss('ok');
                        };
                        $scope.cancel = function () {
                            $modalInstance.dismiss('cancel');
                        };
                    }
                });
            };

            $scope.$watch('selected.bundle', function () {
                if ($scope.selected.bundle) {
                    $scope.showError = false;
                }
            });


            $scope.back = function () {
                $state.go('setupGeneral');
            };

            $scope.next = function () {
                if ($scope.selected.bundle) {
                    $scope.saving = true;
                    ThemeBundleService.install($scope.selected.bundle, function () {
                        $scope.saving = false;
                        $state.go('setupMailouts');
                    });
                } else {
                    $scope.showError = true;
                }
            }
        }])


    .controller('SetupMailoutsController', ['$scope', '$log', '$state', 'HuddleService', 'EmailSettingsService',
        function ($scope, $log, $state, HuddleService, EmailSettingsService) {

            $scope.loading = true;
            $scope.settings = EmailSettingsService.get(function() {
                $scope.loading = false;
            });

            $scope.back = function () {
                $state.go('setupTheme');
            };

            $scope.next = function () {

                $scope.saving = true;
                EmailSettingsService.save($scope.settings,
                    function (settings) {
                        $scope.saving = false;
                        $state.go('setupPayments');
                    }, function () {
                        $scope.saving = false;
                        console.log('Error');
                    });
            }

        }])


    .controller('SetupPaymentsController', ['$scope', '$log', '$state', 'HuddleService', 'PaymentSettingsService',
        function ($scope, $log, $state, HuddleService, PaymentSettingsService) {

            $scope.payment = {choice : 'production'};

            $scope.loading = true;
            $scope.settings = PaymentSettingsService.get(function() {
                $scope.loading = false;
            });

            $scope.$watch('payment.choice', function () {
                $scope.showError = false;
            });

            $scope.back = function () {
                $state.go('setupMailouts');
            };

            $scope.next = function () {

                $scope.saving = true;
                PaymentSettingsService.save($scope.settings,
                    function (result) {
                        $scope.saving = false;
                        $state.go('setupPages');

                    }, function () {
                        $scope.saving = false;
                        console.log('Error');
                    });
            }

        }])


    .controller('SetupPagesController', ['$scope', '$log', '$state', 'HuddleService', 'SearchService',
        function ($scope, $log, $state, HuddleService, SearchService) {

            $scope.loading = true;

            $scope.pages = {
                home: { show: true, exists: false, selected: true },
                blog: { show: true, exists: false, selected: true },
                about: { show: true, exists: false, selected: true },
                contact: { show: true, exists: false, selected: true },
                donate: { show: true, exists: false, selected: true }
            };

            SearchService.search({  }, function(pages) {
                for (var i = 0; i < pages.content.length; i++) {
                    var page = pages.content[i];
                    if (page.slug == 'home') { $scope.pages.home.exists = true }
                    if (page.slug == 'blog') { $scope.pages.blog.exists = true }
                    if (page.slug == 'about') { $scope.pages.about.exists = true }
                    if (page.slug == 'contact') { $scope.pages.contact.exists = true }
                    if (page.slug == 'donate') { $scope.pages.donate.exists = true }
                }
            });



            $scope.back = function () {
                $state.go('setupPayments');
            };

            $scope.next = function () {
                var pages = $scope.pages;
                var pageRequest = {
                    home: pages.home.selected && !pages.home.exists,
                    blog: pages.blog.selected && !pages.blog.exists,
                    about: pages.about.selected && !pages.about.exists,
                    contact: pages.contact.selected && !pages.contact.exists,
                    donate: pages.donate.selected && !pages.donate.exists
                };
                $scope.saving = true;
                HuddleService.setupPages(pageRequest, function () {
                    $scope.saving = false;
                    HuddleService.setupWizardComplete(function () {
                        $scope.huddle.setupWizardComplete = true;
                        $state.go('setupDone');
                    });
                });
            }

        }])


    .controller('SetupDoneController', ['$scope', '$log', '$state', '$window', 'HuddleService',
        function ($scope, $log, $state, $window, HuddleService) {

            $scope.showSite = function () {
                $window.window.open(
                    $scope.huddle.baseUrl,
                    '_blank' // <- This is what makes it open in a new window.
                );
            };

            $scope.editSite = function () {
                $state.go('website');
            }
        }])

;
