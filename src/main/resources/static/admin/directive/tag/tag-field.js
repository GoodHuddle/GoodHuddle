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

    .directive('ghTagField', function ($modal, TagService) {

        return {

            restrict: 'E',
            require: "ngModel",
            templateUrl: 'directive/tag/tag-field.html',

            link: function (scope, element, attr, ngModel) {

                ngModel.$render = function() {
                    scope.selectedTags = ngModel.$viewValue;
                };

                scope.tagListVisible = false;

                function isSelected(tag) {
                    for (var i = 0; i < scope.selectedTags.length; i++) {
                        if (scope.selectedTags[i].id == tag.id) {
                            return true;
                        }
                    }
                    return false;
                }

                var allTags = null;
                function filterTags() {
                    scope.availableTags = [];
                    if (allTags) {
                        for (var i = 0; i < allTags.length; i++) {
                            var tag = allTags[i];
                            if (!isSelected(tag)) {
                                if (scope.searchTerm && scope.searchTerm.length > 0) {
                                    if (tag.name && tag.name.toLowerCase().indexOf(scope.searchTerm.toLowerCase()) >= 0) {
                                        scope.availableTags.push(tag);
                                    }
                                } else {
                                    scope.availableTags.push(tag);
                                }
                            }
                        }
                    }
                    scope.highlightedTag = scope.availableTags.length > 0 ? scope.availableTags[0] : null;
                }

                scope.addTag = function(tag) {
                    if (tag) {
                        scope.selectedTags.push(tag);
                    }
                    scope.tagListVisible = false;
                    scope.searchTerm = null;
                };

                scope.removeTag = function(tag) {
                    scope.selectedTags.splice(scope.selectedTags.indexOf(tag), 1);
                };

                scope.showTagList = function() {
                    if (!allTags) {
                        TagService.get(function(tags) {
                            allTags = tags.content;
                            filterTags();
                        });
                    } else {
                        filterTags();
                    }
                    scope.tagListVisible = true;
                    element.find("input").focus();
                };

                scope.hideTagList = function() {
                    scope.tagListVisible = false;
                };

                scope.highlight = function(tag) {
                    scope.highlightedTag = tag;
                };

                scope.$watch("searchTerm", function() {
                    filterTags();
                    console.log("Search term changed to: " + scope.searchTerm);
                    if (scope.searchTerm != null) {
                        if (!scope.tagListVisible) {
                            scope.showTagList();
                        }
                    } else {
                        scope.hideTagList();
                    }
                });

                scope.createAndUseTag = function (tagName) {

                    $modal.open({
                        templateUrl: 'directive/tag/create-tag.html',
                        resolve: { tagName: function () { return tagName; } },
                        controller: function ($scope, $modalInstance, tagName) {
                            $scope.tag = { name: tagName };

                            $scope.ok = function () {
                                $modalInstance.close($scope.tag);
                            };
                            $scope.cancel = function () {
                                $modalInstance.dismiss('cancel');
                            };
                        }
                    })
                        .result.then(function (tag) {
                            TagService.save(tag, function (createdTag) {
                                scope.availableTags.push(createdTag);
                                scope.selectedTags.push(createdTag);
                            });
                        });
                };
            }
        };
    });
