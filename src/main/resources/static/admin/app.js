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

angular

    .module('huddleAdmin',
        [
            "ui.router",
            "ui.bootstrap",
            "ui.select2",
            "ui.select",
            "ngSanitize",
            "ngCkeditor",
            "angularFileUpload",
            "angularMoment",
            "ui.ace",
            "ngCookies",
            "ngOnboarding",
            "checklist-model",

            "huddleAdmin.service.recursion",

            "huddleAdmin.service.huddle",
            "huddleAdmin.service.menu",
            "huddleAdmin.service.layout",
            "huddleAdmin.service.page",
            "huddleAdmin.service.search",
            "huddleAdmin.service.blog",
            "huddleAdmin.service.blogPost",
            "huddleAdmin.service.blogPostComment",
            "huddleAdmin.service.theme",
            "huddleAdmin.service.themeSetting",
            "huddleAdmin.service.themeBundle",
            "huddleAdmin.service.member",
            "huddleAdmin.service.memberBulk",
            "huddleAdmin.service.export",
            "huddleAdmin.service.tag",
            "huddleAdmin.service.mailout",
            "huddleAdmin.service.email",
            "huddleAdmin.service.payment",
            "huddleAdmin.service.subscription",
            "huddleAdmin.service.payment.settings",
            "huddleAdmin.service.email.settings",
            "huddleAdmin.service.securityGroup",
            "huddleAdmin.service.auth"

        ])

    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, ngOnboardingDefaultsProvider) {

        // send credentials with requests
        $httpProvider.defaults.withCredentials = true;


        ngOnboardingDefaultsProvider.set({
            showStepInfo: false,
            closeButtonText: '<i class="fa fa-times"></i>',
            nextButtonText: "Next",
            previousButtonText: "Previous",
            overlayOpacity: '0.2'
        });

        $urlRouterProvider.otherwise("/website");
        $urlRouterProvider.when('/website', '/website/dashboard');
        $urlRouterProvider.when('/member', '/member/dashboard');
        $urlRouterProvider.when('/member/view/{id}', '/member/view/{id}/emails');
        $urlRouterProvider.when('/funding', '/funding/payments');
        $urlRouterProvider.when('/funding/payments', '/funding/payments/paid');
        $urlRouterProvider.when('/settings', '/settings/huddle');

        $stateProvider

            .state('login', {
                url: "/login",
                templateUrl: "components/login/login.html"
            })

            // Huddle base states

            .state('dashboard', {
                url: "/dashboard",
                templateUrl: "components/huddle-dashboard.html"
            })

            // Setup wizard states

            .state('setupStart', {
                url: "/setup",
                templateUrl: "components/setup/setup-start.html"
            })

            .state('setupGeneral', {
                url: "/setup/general",
                templateUrl: "components/setup/setup-general.html"
            })

            .state('setupTheme', {
                url: "/setup/theme",
                templateUrl: "components/setup/setup-theme.html"
            })

            .state('setupPages', {
                url: "/setup/pages",
                templateUrl: "components/setup/setup-pages.html"
            })

            .state('setupMailouts', {
                url: "/setup/mailouts",
                templateUrl: "components/setup/setup-mailouts.html"
            })

            .state('setupPayments', {
                url: "/setup/payments",
                templateUrl: "components/setup/setup-payments.html"
            })

            .state('setupDone', {
                url: "/setup/done",
                templateUrl: "components/setup/setup-done.html"
            })

            // Website states

            .state('website', {
                url: "/website",
                template: "<ui-view/>"
            })

            .state('website.dashboard', {
                url: "/dashboard",
                templateUrl: "components/website/dashboard/website-dashboard.html"
            })

            .state('website.menu', {
                url: "/menu",
                templateUrl: "components/website/menu/menu.html"
            })

            .state('website.page', {
                url: "/page/{pageId}",
                templateUrl: "components/website/page/page.html"
            })

            .state('website.blog', {
                url: "/blog/{blogId}",
                templateUrl: "components/website/blog/blog.html"
            })

            .state('website.blogPost', {
                url: "/blog/post/{postId}",
                templateUrl: "components/website/blog/post/blog-post.html"
            })

            .state('website.theme', {
                url: "/theme",
                template: "<ui-view/>"
            })

            .state('website.theme.current', {
                url: "/current",
                templateUrl: "components/website/theme/current/current-theme.html"
            })

            .state('website.theme.choose', {
                url: "/choose",
                templateUrl: "components/website/theme/choose/choose-theme.html"
            })

            .state('website.theme.order', {
                url: "/order",
                templateUrl: "components/website/theme/order/order-theme.html"
            })

            .state('website.theme.edit', {
                url: "/edit/{themeId}",
                templateUrl: "components/website/theme/edit/edit-theme.html"
            })

            .state('website.theme.upload', {
                url: "/upload",
                templateUrl: "components/website/theme/upload/upload-theme.html"
            })

            // Member states

            .state('member', {
                url: "/member",
                template: "<ui-view/>"
            })

            .state('member.dashboard', {
                url: "/dashboard",
                templateUrl: "components/member/dashboard/member-dashboard.html"
            })

            .state('member.list', {
                url: "/list",
                templateUrl: "components/member/list/members.html"
            })

            .state('member.view', {
                url: "/view/{memberId}",
                templateUrl: "components/member/view/member.html"
            })

            .state('member.view.emails', {
                url: "/emails",
                templateUrl: "components/member/view/emails/member-emails.html"
            })

            .state('member.view.donations', {
                url: "/payments",
                templateUrl: "components/member/view/donations/member-donations.html"
            })

            .state('member.view.subscriptions', {
                url: "/subscriptions",
                templateUrl: "components/member/view/subscriptions/member-subscriptions.html"
            })

            .state('member.create', {
                url: "/create",
                templateUrl: "components/member/create/create-member.html"
            })

            .state('member.edit', {
                url: "/edit/{memberId}",
                templateUrl: "components/member/edit/edit-member.html"
            })

            .state('member.mailout', {
                url: "/mailout",
                template: "<ui-view/>"
            })

            .state('member.mailout.list', {
                url: "/list",
                templateUrl: "components/member/mailouts/mailouts.html"
            })

            .state('member.mailout.create', {
                url: "/create",
                templateUrl: "components/member/mailouts/create/create-mailout.html"
            })

            .state('member.mailout.view', {
                url: "/view/{mailoutId}",
                templateUrl: "components/member/mailouts/view/view-mailout.html"
            })

            .state('member.bulkTag', {
                url: "/bulk/tag/{bulkSetId}",
                templateUrl: "components/member/bulk/bulk-tag-members.html"
            })

            .state('member.bulkExport', {
                url: "/bulk/export/{bulkSetId}",
                templateUrl: "components/member/bulk/bulk-export-members.html"
            })

            // Email states

            .state('member.mailout.email', {
                url: "/email",
                template: "<ui-view/>"
            })

            .state('member.mailout.email.view', {
                url: "/view/{emailId}",
                templateUrl: "components/member/mailouts/email/view/view-email.html"
            })

            // Funding states

            .state('funding', {
                url: "/funding",
                template: "<ui-view/>"
            })

            .state('funding.payments', {
                url: "/payments",
                templateUrl: "components/funding/payments/payments.html"
            })

            .state('funding.payments.paid', {
                url: "/paid",
                templateUrl: "components/funding/payments/paid/paid.html"
            })

            .state('funding.payment', {
                url: "/payment/{paymentId}",
                templateUrl: "components/funding/payments/paid/view/view-payment.html"
            })

            .state('funding.payments.subscriptions', {
                url: "/subscriptions",
                templateUrl: "components/funding/payments/subscriptions/subscriptions.html"
            })

            .state('funding.subscription', {
                url: "/subscription/{subscriptionId}",
                templateUrl: "components/funding/payments/subscriptions/view/view-subscription.html"
            })

            // Settings states
            .state('settings', {
                url: "/settings",
                templateUrl: "components/settings/settings.html"
            })

            .state('settings.huddle', {
                url: "/huddle",
                templateUrl: "components/settings/huddle/huddle-settings.html"
            })

            .state('settings.mail', {
                url: "/mail",
                templateUrl: "components/settings/mail/email-settings.html"
            })

            .state('settings.payment', {
                url: "/payment",
                templateUrl: "components/settings/payment/payment-settings.html"
            })

            .state('settings.tags', {
                url: "/tags",
                templateUrl: "components/settings/tags/tag-settings.html"
            })
        ;


        $httpProvider.interceptors.push(function ($q, $rootScope, $window) {
            return {
                'responseError': function (rejection) {
                    var status = rejection.status;
                    var config = rejection.config;
                    var method = config.method;
                    var url = config.url;

                    console.log('HTTP ERROR: ' + status);
                    if (status == 401 || status == 403) {
                        $window.location.href = "/admin/login";
                    } else {
                        var error = method + " on " + url + " failed with status " + status;
                        console.log(error);
                        $rootScope.error = error;
                    }

                    return $q.reject(rejection);
                }
            };
        });

    })

    .run(
        [
            '$rootScope', '$urlRouter', '$state', '$stateParams', '$modal', 'HuddleService', 'AuthService',
            function ($rootScope, $urlRouter, $state, $stateParams, $modal, HuddleService, AuthService) {
                $rootScope.$state = $state;
                $rootScope.$stateParams = $stateParams;
                $rootScope.$navManager = {};

                // reset error when a new view is loaded
                $rootScope.$on('$viewContentLoaded', function() {
                    delete $rootScope.error;
                });

                $rootScope.currentUser = AuthService.get();
                $rootScope.logout = function(){
                    window.location.href = '/admin/logout';
                };

                $rootScope.globalLoading = true;
                $rootScope.huddle = HuddleService.get(function(huddle) {
                    $rootScope.globalLoading = false;
                });

                $rootScope.changePassword = function(){

                    $modal.open({
                        templateUrl: 'components/auth/change-password.html',
                        controller: function ($scope, $modalInstance, AuthService) {

                            $scope.details = {
                                error: false
                            };

                            $scope.submit = function () {
                                if ($scope.details.password === $scope.details.confirmNewPassword){
                                    alert("send me to the Auth service and I'm changed unless your old password is wrong ;)");
                                    // AuthService.changePassword($scope.details, function () {
                                    //     alert('password changed')
                                    //     $modalInstance.close();
                                    //     // just show a dialog eg like the error popup saying password changed.
                                    // }, function(){
                                    //     alert('password change failed');
                                    // });

                                } else {
                                    alert("Something has gone wrong please contact your Administrator")
                                }

                            };
                            $scope.cancel = function () {
                                $modalInstance.dismiss('cancel');
                            };
                        }
                    });
                };


                $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {

                    if ($rootScope.globalLoading) {
                        event.preventDefault();
                        var stopWatching = $rootScope.$watch('globalLoading', function(stillLoading) {
                            if (!stillLoading) {
                                stopWatching();
                                if (!$rootScope.huddle.setupWizardComplete && toState.url.indexOf('/setup') != 0) {
                                    console.log('Redirecting to setupStart');
                                    $state.go('setupStart');
                                } else {
                                    $state.go(toState, toParams);
                                }
                            }
                        });
                    } else {

                        if (!$rootScope.huddle.setupWizardComplete && toState.url.indexOf('/setup') != 0) {
                            console.log('Redirecting to setupStart');
                            $state.go('setupStart');
                            event.preventDefault();
                        }

                    }


                    if ($rootScope.$navManager.interceptor) {
                        event.preventDefault();
                        $rootScope.$navManager.interceptor(function() {
                            $state.go(toState, toParams);
                        });
                    }
                });
            }
        ]
    );
