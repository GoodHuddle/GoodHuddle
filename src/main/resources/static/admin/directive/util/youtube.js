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

.directive('youtube', function($sce) {

    return {
        restrict: 'EA',
        scope: {
            videoId: '='
        },
        replace: true,
        template: '<iframe style="overflow:hidden;height:100%;width:100%" width="100%" height="100%" src="{{url}}" frameborder="0" allowfullscreen></iframe>',
        link: function(scope) {
            scope.$watch('videoId', function(newVideoId) {
                if (newVideoId) {
                    scope.url = $sce.trustAsResourceUrl("http://www.youtube.com/embed/" + newVideoId);
                }
            });
        }
    };
});
