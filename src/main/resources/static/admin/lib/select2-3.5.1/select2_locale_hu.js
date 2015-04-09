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
 * Select2 Hungarian translation
 */
(function ($) {
    "use strict";

    $.fn.select2.locales['hu'] = {
        formatNoMatches: function () { return "Nincs találat."; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return "Túl rövid. Még " + n + " karakter hiányzik."; },
        formatInputTooLong: function (input, max) { var n = input.length - max; return "Túl hosszú. " + n + " karakterrel több, mint kellene."; },
        formatSelectionTooBig: function (limit) { return "Csak " + limit + " elemet lehet kiválasztani."; },
        formatLoadMore: function (pageNumber) { return "Töltés…"; },
        formatSearching: function () { return "Keresés…"; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['hu']);
})(jQuery);
