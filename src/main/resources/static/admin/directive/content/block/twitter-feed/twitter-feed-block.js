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

    .controller('TwitterFeedBlock', ['$scope', '$http', '$modal', '$document', '$timeout', function ($scope, $http, $modal, $document, $timeout) {
        var originalY = null;


        $scope.startResize = function (event) {
            originalY = event.clientY;
            $document.bind('mousemove', onDrag);
            $document.bind('mouseup', onDragFinished);
            event.preventDefault();
        };

        function onDrag(event) {

            var distance = event.clientY - originalY;
            var newHeight = $scope.block.value.height + distance;
            newHeight = Math.max(40, newHeight);
            $scope.block.value.height = newHeight;
            $scope.$apply();

            originalY = event.clientY;
        }


        function onDragFinished() {
            $document.unbind('mousemove', onDrag);
            $document.unbind('mouseup', onDragFinished);
        }

        $scope.reloadTwitter = function(){
            // Question: why doesn't this have access to twttr?
            $timeout = twttr.widgets.load();
        }

        $scope.chooseTwitterFeed = function () {

            var modalInstance = $modal.open({
                templateUrl: 'directive/content/block/twitter-feed/choose-twitter-feed.html',
                resolve: {
                    block: function () {
                        return $scope.block
                    }
                },
                controller: function ($scope, $modalInstance, block, $window, $document) {

                    $scope.details = {
                        twitterHandle : block.value.twitterHandle || null,
                        height: block.value.height || 600,
                        width: block.value.width || 520
                    };

                    $scope.ok = function () {
                        block.value.twitterHandle = $scope.details.twitterHandle;
                        block.value.height = $scope.details.height;
                        $modalInstance.close();
                    };
                    $scope.cancel = function () {
                        $modalInstance.dismiss('cancel');
                    };
                }
            });
        };

        if ($scope.block.isNew) {
            $scope.chooseTwitterFeed();
            delete $scope.block.isNew;
        }

    }])

    .controller('TwitterFeedTool', ['$scope', function ($scope) {


        $scope.blockFactory.getLabel = function (type) {
            return "Twitter Feed";
        };

        $scope.blockFactory.createBlock = function (type) {
            return {
                type: type,
                isNew: true,
                value: {
                    twitterHandle: 'nonprofitorgs'
                }
            }
        };


    }]);
