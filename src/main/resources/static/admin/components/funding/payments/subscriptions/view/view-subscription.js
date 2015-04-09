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

    .controller('ViewSubscriptionController', ['$scope', '$state', '$stateParams', '$modal', 'SubscriptionService', 'PaymentService',
        function ($scope, $state, $stateParams, $modal, SubscriptionService, PaymentService) {

            var subscriptionId = $stateParams.subscriptionId;

            $scope.subscription = SubscriptionService.get({id: subscriptionId});

            var page = parseInt($stateParams.page ? $stateParams.page : 0);

            $scope.payments = PaymentService.search({
                subscriptionId: subscriptionId,
                page: page,
                size: 20
            });

            $scope.viewPayment = function (payment) {
                $state.go('funding.payment', { paymentId: payment.id});
            };

            $scope.cancelSubscription = function () {

                $modal.open({
                    templateUrl: 'components/funding/payments/subscriptions/view/confirm-cancel.html',
                    resolve: {
                        subscription: function () {
                            return $scope.subscription
                        }
                    },
                    controller: function ($scope, $modalInstance, subscription) {
                        $scope.ok = function () {

                            $scope.cancelling = true;
                            SubscriptionService.cancel(subscription,
                                function (subscription) {
                                    $modalInstance.close(subscription);
                                },
                                function () {
                                    $scope.cancelling = false;
                                });
                        };
                        $scope.cancel = function () {
                            $modalInstance.dismiss('cancel');
                        };
                    }
                })
                .result.then(function (subscription) {
                    console.log('Cancelled: ' + JSON.stringify(subscription));
                    $scope.subscription = subscription;
                });
            };

        }]);
