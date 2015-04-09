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

    .controller('ViewPaymentController', ['$scope', '$state', '$stateParams', 'PaymentService',
        function ($scope, $state, $stateParams, PaymentService) {

            var paymentId = $stateParams.paymentId;

            $scope.payment = PaymentService.get({id: paymentId});


            $scope.prevPage = function (post) {
                $state.go('funding.payments.subscriptions', { page: page - 1 });
            };

            $scope.nextPage = function (post) {
                $state.go('funding.payments.subscriptions', { page: page + 1 });
            };
        }]);
