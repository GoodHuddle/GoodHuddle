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

angular.module('huddleAdmin.service.subscription', ['ngResource'])

    .service('SubscriptionService', function ($resource) {

        return $resource('/api/subscription/:id', {id: '@id'},
            {
                search: {
                    method: 'GET',
                    url: '/api/subscription'
                },
                cancel: {
                    method: 'PUT',
                    url: '/api/subscription/:id/cancel'
                }
            });
    });
