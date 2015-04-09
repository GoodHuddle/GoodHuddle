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

angular.module('huddleAdmin.service.page', ['ngResource'])

    .service('PageService', function ($resource, $http) {

        return $resource('/api/page/:id', {id: '@id'},
            {
                get: {
                    method: 'GET',
                    transformResponse: $http.defaults.transformResponse.concat([
                        function (page, headersGetter) {
                            angular.forEach(page.content.rows, function(row, index) {
                                var start = 0;
                                for (var i = 0; i < row.cells.length; i++) {
                                    row.cells[i].start = start;
                                    start += row.cells[i].width;
                                }
                            });
                            return page;
                        }
                    ])
                },

                update: {
                    method: 'PUT'
                },

                preview: {
                    method: 'POST',
                    url: '/page/preview'
                },

                list: {
                    method: 'POST',
                    url: '/api/search'
                }
            });
    });
