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

.directive('twitter', function($sce, $window) {

    return {
        restrict: 'EA',
        scope: {
            twitterHandle: '='
        },
        replace: true,
        transclude: true,
        template: '',
        link: function(scope, element, attrs) {
            function loadTwitter() {
                var link = '<a class="twitter-timeline" ng-href="https://twitter.com/' + scope.twitterHandle + '"' + ' data-screen-name="' + scope.twitterHandle + '"' + " data-widget-id='522550625389670400'>Tweets by @" + scope.twitterHandle + '</a>';
                element.append(link);

                // Insert twitter script if not already on page
                (! function(d, s, id) {
                    var js, fjs = d.getElementsByTagName(s)[0],
                        p = /^http:/.test(d.location) ? 'http' : 'https';
                    if (!d.getElementById(id)) {
                        js = d.createElement(s);
                        js.id = id;
                        js.src = p + "://platform.twitter.com/widgets.js";
                        fjs.parentNode.insertBefore(js, fjs);
                    } else {
                        // reload widget
                        twttr.widgets.load();
                    }
                }(document, "script", "twitter-wjs"));
            }

            loadTwitter();

            scope.$watch(
              'twitterHandle',
              function(newValue, oldValue) {
                if ( newValue !== oldValue ) {
                    $('.twitter-timeline', element).remove();
                    loadTwitter();
                }
              }
            );
        }
    };
});
