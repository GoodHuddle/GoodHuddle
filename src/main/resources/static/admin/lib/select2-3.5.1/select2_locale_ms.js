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
 * Select2 Malay translation.
 * 
 * Author: Kepoweran <kepoweran@gmail.com>
 */
(function ($) {
    "use strict";

    $.fn.select2.locales['ms'] = {
        formatNoMatches: function () { return "Tiada padanan yang ditemui"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return "Sila masukkan " + n + " aksara lagi"; },
        formatInputTooLong: function (input, max) { var n = input.length - max; return "Sila hapuskan " + n + " aksara"; },
        formatSelectionTooBig: function (limit) { return "Anda hanya boleh memilih " + limit + " pilihan"; },
        formatLoadMore: function (pageNumber) { return "Sedang memuatkan keputusan…"; },
        formatSearching: function () { return "Mencari…"; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['ms']);
})(jQuery);
