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

/**
 * Select2 Finnish translation
 */
(function ($) {
    "use strict";
    $.fn.select2.locales['fi'] = {
        formatNoMatches: function () {
            return "Ei tuloksia";
        },
        formatInputTooShort: function (input, min) {
            var n = min - input.length;
            return "Ole hyvä ja anna " + n + " merkkiä lisää";
        },
        formatInputTooLong: function (input, max) {
            var n = input.length - max;
            return "Ole hyvä ja anna " + n + " merkkiä vähemmän";
        },
        formatSelectionTooBig: function (limit) {
            return "Voit valita ainoastaan " + limit + " kpl";
        },
        formatLoadMore: function (pageNumber) {
            return "Ladataan lisää tuloksia…";
        },
        formatSearching: function () {
            return "Etsitään…";
        }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['fi']);
})(jQuery);
