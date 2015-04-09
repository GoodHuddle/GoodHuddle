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
 * Select2 Arabic translation.
 *
 * Author: Adel KEDJOUR <adel@kedjour.com>
 */
(function ($) {
    "use strict";

    $.fn.select2.locales['ar'] = {
        formatNoMatches: function () { return "لم يتم العثور على مطابقات"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; if (n == 1){ return "الرجاء إدخال حرف واحد على الأكثر"; } return n == 2 ? "الرجاء إدخال حرفين على الأكثر" : "الرجاء إدخال " + n + " على الأكثر"; },
        formatInputTooLong: function (input, max) { var n = input.length - max; if (n == 1){ return "الرجاء إدخال حرف واحد على الأقل"; } return n == 2 ? "الرجاء إدخال حرفين على الأقل" : "الرجاء إدخال " + n + " على الأقل "; },
        formatSelectionTooBig: function (limit) { if (n == 1){ return "يمكنك أن تختار إختيار واحد فقط"; } return n == 2 ? "يمكنك أن تختار إختيارين فقط" : "يمكنك أن تختار " + n + " إختيارات فقط"; },
        formatLoadMore: function (pageNumber) { return "تحميل المزيد من النتائج…"; },
        formatSearching: function () { return "البحث…"; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['ar']);
})(jQuery);
