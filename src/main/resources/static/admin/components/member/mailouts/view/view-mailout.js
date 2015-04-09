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

    .controller('ViewMailoutController', ['$scope', '$state', '$stateParams', '$timeout', '$sce', '$modal', 'MailoutService', 'EmailService',
        function ($scope, $state, $stateParams, $timeout, $sce, $modal, MailoutService, EmailService) {

            var mailoutId = $stateParams.mailoutId;
            var page = parseInt($stateParams.page ? $stateParams.page : 0);

            var refresher = undefined;

            function loadMailout(assignOnSuccess) {
                var result = MailoutService.get({id: mailoutId}, function(mailout) {
                    if (mailout.status == 'sending') {
                        startRefresher();
                    }
                    if (assignOnSuccess) {
                        $scope.mailout = mailout;
                    }

                    // dodgy hack to update iframe content since angular doesn't seem to support this
                    var iframeDocument = document.querySelector('#content-iframe').contentWindow.document;
                    var content = mailout.content;
                    iframeDocument.open('text/html', 'replace');
                    iframeDocument.write(content);
                    iframeDocument.close();

                });
                if (!assignOnSuccess) {
                    $scope.mailout = result;
                }
            }
            loadMailout();

            $scope.edit = function() {
                $state.go('member.mailout.edit', { mailoutId: mailoutId});
            };

            $scope.viewEmail = function(email) {
                $state.go('member.mailout.email.view', { emailId: email.id});
            };

            $scope.viewMember = function(member) {
                $state.go('member.view', { memberId: member.id});
            };

            $scope.generateEmails = function() {
                MailoutService.generateEmails({id: mailoutId}, function(mailout) {
                    $scope.mailout = mailout;
                    loadEmails();
                });
            };

            $scope.sendEmails = function() {

                $modal.open({
                    templateUrl: 'components/member/mailouts/view/confirm-send.html',
                    resolve: {
                        emails: function () { return $scope.emails },
                        mailout: function () { return $scope.mailout }
                    },
                    controller: function ($scope, $modalInstance, emails, mailout) {
                        $scope.emails = emails;
                        $scope.mailout = mailout;
                        $scope.ok = function () {
                            MailoutService.sendEmails({id: mailoutId}, function(mailout) {
                                $scope.mailout.status = mailout.status;
                                loadEmails();
                            });
                            startRefresher();
                            $modalInstance.close();
                        };
                        $scope.cancel = function () {
                            $modalInstance.dismiss('cancel');
                        };
                    }
                });
            };


            function loadEmails(assignOnSuccess) {
                var emails = EmailService.search({
                    mailoutId: mailoutId,
                    page: page,
                    size: 20
                }, function(result) {
                    if (assignOnSuccess) {
                        $scope.emails = result;
                    }
                });

                if (!assignOnSuccess) {
                    $scope.emails = emails;
                }
            }
            loadEmails();

            function startRefresher() {
                stopRefresher();
                refresher = $timeout(function() {
                    console.log("Refreshing status");
                    loadEmails(true);
                    loadMailout(true);
                }, 3000);
            }
            function stopRefresher() {
                if (angular.isDefined(refresher)) {
                    $timeout.cancel(refresher);
                    refresher = undefined;
                }
            }

            // Cancel interval on page changes
            $scope.$on('$destroy', function() {
                stopRefresher();
            });

        }]);
