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

    .controller('CarouselBlock', ['$scope', '$modal', function ($scope, $modal) {

        $scope.chooseCarouselImages = function () {

            var controller = function ($scope, $modalInstance, block) {
                var slideArray;
                if (block.value.slides.length) {
                    slideArray = angular.copy(block.value.slides);

                } else {
                    slideArray = [{imageUrl:null, href:null, caption:null, active: true }]
                }
                $scope.details = {
                    slideInterval : block.value.slideInterval ? block.value.slideInterval : 5000,
                    slides : slideArray
                };

                $scope.addSlide = function(){
                    $scope.details.slides.push({
                        imageUrl : null,
                        href : null,
                        caption: null,
                        active: true
                    });
                };

                $scope.deleteSlide = function(slide){
                    if ($scope.details.slides.length > 1){
                        $scope.details.slides.splice($scope.details.slides.indexOf(slide), 1 );
                    }
                };

                $scope.ok = function () {
                        block.value.slides = $scope.details.slides;
                        block.value.slideInterval = $scope.details.slideInterval;
                        $modalInstance.close();
               };
                $scope.cancel = function () {
                    $modalInstance.dismiss('cancel');
                };
            };
            var modalInstance = $modal.open({
                size: 'lg',
                templateUrl: 'directive/content/block/carousel/choose-carousel-images.html',
                controller: controller,
                resolve: {
                    block: function () {
                        return $scope.block;
                    },
                    context: function () {
                        return $scope.blockFunctions.context;
                    }
                }
            });
        };

        if ($scope.block.isNew) {
            $scope.chooseCarouselImages();
            delete $scope.block.isNew;
        }

    }])

    .controller('CarouselTool', ['$scope', function ($scope) {


        $scope.blockFactory.getLabel = function (type) {
            return "Carousel"
        };

        $scope.blockFactory.createBlock = function (type) {
            return {
                type: type,
                isNew: true,
                value: {
                    slides:[],
                    slideInterval: null
                }
            }
        };

    }
]);
