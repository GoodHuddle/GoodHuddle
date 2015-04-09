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

    .directive('ghTagChooser', function ($modal, TagService) {

        return {

            restrict: 'E',

            scope: {
                tags: "=",
                targetId: "=",
                type: "="
            },

            templateUrl: 'directive/tag/tag-chooser.html',

            controller: function ($scope) {

                $scope.isSelected = function(tag) {
                    if ($scope.tags) {
                        for (var i = 0; i < $scope.tags.length; i++) {
                            if ($scope.tags[i].id === tag.id) {
                                return true;
                            }
                        }
                    }
                    return false;
                };

                $scope.availableTags = [];
                TagService.get(function(available) {
                    $scope.availableTags = available.content;
                });

                $scope.toggleTag = function ($event, tag) {
                    $event.stopPropagation();
                    if ($scope.tags) {
                        for (var i = 0; i < $scope.tags.length; i++) {
                            if ($scope.tags[i].id === tag.id) {
                                // add tag
                                TagService.untag({
                                    type: $scope.type,
                                    targetId: $scope.targetId,
                                    tagId: tag.id
                                }, function () {
                                    $scope.tags.splice(i, 1);
                                });
                                return;
                            }
                        }

                        // add tag
                        TagService.tag({
                            type: $scope.type,
                            targetId: $scope.targetId,
                            tagId: tag.id
                        }, function () {
                            $scope.tags.push(tag);
                        });
                    }
                };

                $scope.createAndUseTag = function (tagName) {

                    $modal.open({
                        templateUrl: 'directive/tag/create-tag.html',
                        resolve: {
                            tagName: function () {
                                return tagName;
                            }
                        },
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

                                $scope.availableTags.push(createdTag);

                                TagService.tag({
                                    type: $scope.type,
                                    targetId: $scope.targetId,
                                    tagId: createdTag.id
                                }, function () {
                                    $scope.tags.push(createdTag);
                                });

                            });
                        });
                };

            }
        };
    });
