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

    .controller('HuddleSettingsController',

        ['$rootScope', '$scope', '$state', '$log', '$modal', 'HuddleService',

            function ($rootScope, $scope, $state, $log, $modal, HuddleService) {

                var first = true;

                function loadHuddleSettings() {
                    $scope.huddle = HuddleService.get(function() {
                        setModified(false);
                        first = true;
                    });
                }
                loadHuddleSettings();

                $scope.cancel = function () {
                    loadHuddleSettings();
                };

                $scope.save = function () {
                    $scope.saving = true;
                    HuddleService.update($scope.huddle, function() {
                        $scope.saving = false;
                        setModified(false);
                    });
                };


                //-----------------------------------------------------------------------
                // watch for unsaved changes

                $scope.$watch('huddle', function (newValue, oldValue) {
                    if (first) {
                        first = false;
                        return;
                    }
                    if (newValue === oldValue) {
                        return
                    }
                    if (newValue != oldValue) {
                        setModified(true);
                    }
                }, true);

                window.onbeforeunload = function () {
                    if ($scope.modified) {
                        return "You have unsaved changes, do you want to discard them?";
                    }
                };

                function setModified(modified) {
                    $scope.modified = modified;
                    if (modified) {
                        $rootScope.$navManager.interceptor = function (onSuccess) {
                            $modal.open({
                                templateUrl: 'components/settings/confirm-cancel.html',
                                controller: function ($scope, $modalInstance) {
                                    $scope.ok = function () {
                                        $modalInstance.close();
                                        setModified(false);
                                        onSuccess();
                                    };
                                    $scope.cancel = function () {
                                        $modalInstance.dismiss('cancel');
                                    };
                                },
                                resolve: {
                                    blog: function () {
                                        return $scope.blog
                                    }
                                }
                            });
                        }
                    } else {
                        delete $rootScope.$navManager.interceptor;
                    }
                }
            }

        ]);
