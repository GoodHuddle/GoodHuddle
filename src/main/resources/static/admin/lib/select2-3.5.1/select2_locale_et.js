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
 * Select2 Estonian translation.
 *
 * Author: Kuldar Kalvik <kuldar@kalvik.ee>
 */
(function ($) {
    "use strict";

    $.fn.select2.locales['et'] = {
        formatNoMatches: function () { return "Tulemused puuduvad"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return "Sisesta " + n + " täht" + (n == 1 ? "" : "e") + " rohkem"; },
        formatInputTooLong: function (input, max) { var n = input.length - max; return "Sisesta " + n + " täht" + (n == 1? "" : "e") + " vähem"; },
        formatSelectionTooBig: function (limit) { return "Saad vaid " + limit + " tulemus" + (limit == 1 ? "e" : "t") + " valida"; },
        formatLoadMore: function (pageNumber) { return "Laen tulemusi.."; },
        formatSearching: function () { return "Otsin.."; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['et']);
})(jQuery);
