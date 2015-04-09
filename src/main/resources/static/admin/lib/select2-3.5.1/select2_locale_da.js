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
 * Select2 Danish translation.
 *
 * Author: Anders Jenbo <anders@jenbo.dk>
 */
(function ($) {
    "use strict";

    $.fn.select2.locales['da'] = {
        formatNoMatches: function () { return "Ingen resultater fundet"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return "Angiv venligst " + n + " tegn mere"; },
        formatInputTooLong: function (input, max) { var n = input.length - max; return "Angiv venligst " + n + " tegn mindre"; },
        formatSelectionTooBig: function (limit) { return "Du kan kun vælge " + limit + " emne" + (limit === 1 ? "" : "r"); },
        formatLoadMore: function (pageNumber) { return "Indlæser flere resultater…"; },
        formatSearching: function () { return "Søger…"; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['da']);
})(jQuery);
