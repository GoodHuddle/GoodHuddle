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

    .controller('WebsiteDashboardController', [ '$scope', '$state',
        function($scope, $state){

        $scope.onBoardingEnabled = false;
        $scope.onboardingSteps = [
          {
            position: "right",
            description: "<h2>Manage web pages</h2><p>Create and edit web pages for your website, and re-order navigation menus.</p>",
            attachTo: "#manage_pages",
            width: 300,
            nextButtonText: "Next"
          },
          {
            position: "right",
            description: "<h2>Change theme</h2><p>Change the look and feel of your website by customising its theme.</p>",
            attachTo: "#change_theme",
            nextButtonText: "Next",
            previousButtonText: "Previous"
          },
          {
            position: "bottom",
            description: "<h2>View analytics</h2><p>Track who is visiting your sites and which pages are most popular.</p>",
            attachTo: "#account_setup",
            nextButtonText: "Next",
            previousButtonText: "Previous"
          }
        ];


        $scope.startOnboarding = function(){
            $scope.stepIndex = 0;
            $scope.onBoardingEnabled = true;
        }
    }])
