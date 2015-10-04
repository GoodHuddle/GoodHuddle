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

    .controller('ViewPetitionController', ['$scope', '$state', '$stateParams', '$timeout', '$sce', '$modal', 'PetitionService', 'PetitionSignatureService',
        function ($scope, $state, $stateParams, $timeout, $sce, $modal, PetitionService, PetitionSignatureService) {

            var petitionId = $stateParams.petitionId;
            var page = parseInt($stateParams.page ? $stateParams.page : 0);

            $scope.petition = PetitionService.get({id: petitionId}, function(petition) {
                // dodgy hack to update iframe content since angular doesn't seem to support this
                var iframeDocument = document.querySelector('#content-iframe').contentWindow.document;
                var content = petition.content;
                iframeDocument.open('text/html', 'replace');
                iframeDocument.write(content);
                iframeDocument.close();
            });

            $scope.signatures = PetitionSignatureService.search({
                petitionId: petitionId,
                page: page,
                size: 20
            });


            $scope.edit = function() {
                $state.go('member.petition.edit', { petitionId: petitionId});
            };

            $scope.viewMember = function(member) {
                $state.go('member.view', { memberId: member.id});
            };

        }]);
