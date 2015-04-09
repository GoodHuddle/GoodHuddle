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

    .controller('YoutubeBlock', ['$scope', '$modal', function ($scope, $modal) {

        $scope.chooseVideo = function () {

            $modal.open({
                templateUrl: 'directive/content/block/youtube/choose-video.html',
                resolve: {
                    block: function () {
                        return $scope.block
                    }
                },
                controller: function ($scope, $modalInstance, block) {

                    $scope.details = {
                        url : block.value.videoId ? 'https://www.youtube.com/watch?v='+block.value.videoId : null
                    };

                    $scope.validYouTube = function() {
                        var p = /^(?:https?:\/\/)?(?:www\.)?youtube\.com\/watch\?(?=.*v=((\w|-){11}))(?:\S+)?$/;
                        return ($scope.details.url.match(p)) ? true : false;
                    };

                    $scope.ok = function () {

                        var videoId = $scope.details.url.split('v=')[1];
                        var ampersandPosition = videoId.indexOf('&');
                        if(ampersandPosition != -1) {
                          videoId = videoId.substring(0, ampersandPosition);
                        }
                        block.value.videoId = videoId;
                        $modalInstance.close();
                    };
                    $scope.cancel = function () {
                        $modalInstance.dismiss('cancel');
                    };
                }
            });
        };

        if ($scope.block.isNew) {
            $scope.chooseVideo();
            delete $scope.block.isNew;
        }

    }])

    .controller('YoutubeTool', ['$scope', function ($scope) {


        $scope.blockFactory.getLabel = function (type) {
            return "Video";
        };

        $scope.blockFactory.createBlock = function (type) {
            return {
                type: type,
                isNew: true,
                value: {
                    videoId: null
                }
            }
        };


    }]);
