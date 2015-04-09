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

    .controller('MembersController', ['$scope', '$state', '$stateParams', '$modal',
        'Session', 'MemberService', 'SecurityGroupService', 'TagService', 'MemberBulkService',
        function ($scope, $state, $stateParams, $modal, Session, MemberService,
                  SecurityGroupService, TagService, MemberBulkService) {

            $scope.search = Session.data.memberSearch;

            function getCriteria() {
                var criteria = angular.copy($scope.search);

                // convert security groups to IDs
                if (criteria.securityGroups) {
                    criteria.securityGroupIds = [];
                    for (var i = 0; i < criteria.securityGroups.length; i++) {
                        criteria.securityGroupIds.push(criteria.securityGroups[i].id);
                    }
                    delete criteria.securityGroups;
                }

                // convert tags to IDs
                if (criteria.tags && criteria.tags.included &&  criteria.tags.included.tags) {
                    criteria.tags.included.tagIds = [];
                    for (var i = 0; i < criteria.tags.included.tags.length; i++) {
                        criteria.tags.included.tagIds.push(criteria.tags.included.tags[i].id);
                    }
                    delete criteria.tags.included.tags;
                }
                if (criteria.tags && criteria.tags.excluded &&  criteria.tags.excluded.tags) {
                    criteria.tags.excluded.tagIds = [];
                    for (var i = 0; i < criteria.tags.excluded.tags.length; i++) {
                        criteria.tags.excluded.tagIds.push(criteria.tags.excluded.tags[i].id);
                    }
                    delete criteria.tags.excluded.tags;
                }
                return criteria;
            }

            function search(assignImmediatley) {
                var criteria = getCriteria();
                var members = MemberService.search(criteria, function(result) {
                    if (!assignImmediatley) {
                        $scope.members = result;
                    }
                });
                if (assignImmediatley) {
                    $scope.members = members;
                }
            }
            search(true);

            $scope.$watch('search', function () {

                Session.data.memberSearch = $scope.search;

                var s = $scope.search;
                $scope.hasActiveFilter
                    = !(!s.keywords)
                    || s.includeNoAccess
                    || (s.securityGroups && s.securityGroups.length > 0)
                    || s.tags;

                search(false);
            }, true);

            $scope.createMember = function () {
                $state.go('member.create');
            };

            $scope.viewMember = function (member) {
                $state.go('member.view', { memberId: member.id });
            };

            //-----------------------------------------------------------------
            // Filter keywords

            $scope.filterKeywords = function() {

                $modal.open({
                    templateUrl: 'components/member/list/filter-by-keyword.html',
                    resolve: {
                        search: function() { return angular.copy($scope.search); }
                    },
                    controller: function ($scope, $modalInstance, search) {
                        $scope.search = search;
                        $scope.ok = function () {
                            $modalInstance.close(search);
                        };
                        $scope.cancel = function () {
                            $modalInstance.dismiss('cancel');
                        };
                    }
                }).result.then(function(search) { $scope.search = search });
            };

            $scope.clearKeywordsFilter = function() { delete $scope.search.keywords; };

            //-----------------------------------------------------------------
            // Filter access levels

            $scope.filterSecurityGroups = function() {

                $modal.open({
                    templateUrl: 'components/member/list/filter-by-security-group.html',
                    resolve: {
                        search: function() { return angular.copy($scope.search); }
                    },
                    controller: function ($scope, $modalInstance, search) {
                        $scope.search = search;

                        // load access levels
                        $scope.securityGroups = SecurityGroupService.query();

                        $scope.ok = function () {
                            $modalInstance.close(search);
                        };
                        $scope.cancel = function () {
                            $modalInstance.dismiss('cancel');
                        };
                    }
                }).result.then(function(search) { $scope.search = search });
            };

            $scope.clearSecurityGroupFilter = function() {
                delete $scope.search.securityGroups;
            };


            //-----------------------------------------------------------------
            // Filter tags

            $scope.filterTags = function() {

                $modal.open({
                    templateUrl: 'components/member/list/filter-by-tag.html',
                    resolve: {
                        search: function() { return angular.copy($scope.search); }
                    },
                    controller: function ($scope, $modalInstance, search) {
                        $scope.search = search;

                        if (!$scope.search.tags) {
                            $scope.search.tags = {
                                included: { tags: [], matchType: 'any' },
                                excluded: { tags: [], matchType: 'any' }
                            }
                        }

                        $scope.availableTags = TagService.get();

                        $scope.ok = function () {
                            $modalInstance.close(search);
                        };
                        $scope.cancel = function () {
                            $modalInstance.dismiss('cancel');
                        };
                    }
                }).result.then(function(search) {
                        if((!search.tags.included.tags || search.tags.included.tags.length == 0)
                            && (!search.tags.excluded.tags || search.tags.excluded.tags == 0)) {
                            delete search.tags;
                        }
                        $scope.search = search
                    });
            };

            $scope.clearTagFilter = function() {
                delete $scope.search.tags;
            };


            //-----------------------------------------------------------------
            // Bulk export members

            $scope.bulkExport = function() {

                var criteria = getCriteria();
                MemberBulkService.save(criteria, function(bulkMemberSet) {
                    $state.go('member.bulkExport', { bulkSetId: bulkMemberSet.id });
                });
            };

            //-----------------------------------------------------------------
            // Bulk tag members

            $scope.bulkTag = function() {

                var criteria = getCriteria();
                MemberBulkService.save(criteria, function(bulkMemberSet) {
                    $state.go('member.bulkTag', { bulkSetId: bulkMemberSet.id });
                });
            };

        }]);
