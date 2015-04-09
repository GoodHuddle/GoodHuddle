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
 * Select2 Macedonian translation.
 * 
 * Author: Marko Aleksic <psybaron@gmail.com>
 */
(function ($) {
    "use strict";

    $.fn.select2.locales['mk'] = {
        formatNoMatches: function () { return "Нема пронајдено совпаѓања"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return "Ве молиме внесете уште " + n + " карактер" + (n == 1 ? "" : "и"); },
        formatInputTooLong: function (input, max) { var n = input.length - max; return "Ве молиме внесете " + n + " помалку карактер" + (n == 1? "" : "и"); },
        formatSelectionTooBig: function (limit) { return "Можете да изберете само " + limit + " ставк" + (limit == 1 ? "а" : "и"); },
        formatLoadMore: function (pageNumber) { return "Вчитување резултати…"; },
        formatSearching: function () { return "Пребарување…"; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['mk']);
})(jQuery);
