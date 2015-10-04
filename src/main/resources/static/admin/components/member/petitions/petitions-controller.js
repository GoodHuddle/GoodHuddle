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

    .controller('PetitionsController', ['$scope', '$state', '$stateParams', 'PetitionService',
        function ($scope, $state, $stateParams, PetitionService) {

            var page = parseInt($stateParams.page ? $stateParams.page : 0);
            $scope.search = {};

            $scope.petitions = PetitionService.search({
                phrase: $scope.search.phrase,
                page: page,
                size: 20
            });

            $scope.createPetition = function () {
                $state.go('member.petition.create');
            };

            $scope.viewPetition = function (petition) {
                $state.go('member.petition.view', { petitionId: petition.id });
            };

            $scope.prevPage = function () {
                $state.go('member.petition.list', { page: page - 1 });
            };

            $scope.nextPage = function () {
                $state.go('member.petition.list', { page: page + 1 });
            };


        }]);
