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
* Select2 Hebrew translation.
*
* Author: Yakir Sitbon <http://www.yakirs.net/>
*/
(function ($) {
    "use strict";

    $.fn.select2.locales['he'] = {
        formatNoMatches: function () { return "לא נמצאו התאמות"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return "נא להזין עוד " + n + " תווים נוספים"; },
        formatInputTooLong: function (input, max) { var n = input.length - max; return "נא להזין פחות " + n + " תווים"; },
        formatSelectionTooBig: function (limit) { return "ניתן לבחור " + limit + " פריטים"; },
        formatLoadMore: function (pageNumber) { return "טוען תוצאות נוספות…"; },
        formatSearching: function () { return "מחפש…"; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['he']);
})(jQuery);
