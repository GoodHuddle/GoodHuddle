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

    .controller('FacebookFeedBlock', ['$scope', '$http', '$modal', '$document', '$sce',  function ($scope, $http, $modal, $document, $sce) {
        var originalY = null;

        $scope.$watch('block.value.facebookFeedId', function() {
            $scope.facebookUrl = $sce.trustAsResourceUrl(
                'http://www.facebook.com/plugins/likebox.php?href=http%3A%2F%2Fwww.facebook.com%2F'
                    + $scope.block.value.facebookFeedId
                    + '&width=600&colorscheme=light&show_faces=true&border_color&stream=true&header=true'
            );
        });

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

        $scope.chooseFacebookFeed = function () {

            var modalInstance = $modal.open({
                templateUrl: 'directive/content/block/facebook-feed/choose-facebook-feed.html',
                resolve: {
                    block: function () {
                        return $scope.block
                    }
                },
                controller: function ($scope, $modalInstance, block, $window, $document) {

                    $scope.details = {
                        facebookFeedId : block.value.facebookFeedId || null,
                        height: block.value.height || 600,
                        width: block.value.width || 520
                    };

                    $scope.ok = function () {
                        block.value.facebookFeedId = $scope.details.facebookFeedId;
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
            $scope.chooseFacebookFeed();
            delete $scope.block.isNew;
        }

    }])

    .controller('FacebookFeedTool', ['$scope', function ($scope) {


        $scope.blockFactory.getLabel = function (type) {
            return "Facebook Feed";
        };

        $scope.blockFactory.createBlock = function (type) {
            return {
                type: type,
                isNew: true,
                value: {
                    facebookFeedId: 'goodhuddle'
                }
            }
        };


    }]);
