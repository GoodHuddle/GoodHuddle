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
 * Select2 Dutch translation
 */
(function ($) {
    "use strict";

    $.fn.select2.locales['nl'] = {
        formatNoMatches: function () { return "Geen resultaten gevonden"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return "Vul nog " + n + " karakter" + (n == 1? "" : "s") + " in"; },
        formatInputTooLong: function (input, max) { var n = input.length - max; return "Haal " + n + " karakter" + (n == 1? "" : "s") + " weg"; },
        formatSelectionTooBig: function (limit) { return "Maximaal " + limit + " item" + (limit == 1 ? "" : "s") + " toegestaan"; },
        formatLoadMore: function (pageNumber) { return "Meer resultaten laden…"; },
        formatSearching: function () { return "Zoeken…"; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['nl']);
})(jQuery);
