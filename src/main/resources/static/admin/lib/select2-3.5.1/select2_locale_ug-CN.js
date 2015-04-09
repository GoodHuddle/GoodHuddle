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
 * Select2 Uyghur translation
 */
(function ($) {
    "use strict";
    $.fn.select2.locales['ug-CN'] = {
        formatNoMatches: function () { return "ماس كېلىدىغان ئۇچۇر تېپىلمىدى"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return "يەنە " + n + " ھەرپ كىرگۈزۈڭ";},
        formatInputTooLong: function (input, max) { var n = input.length - max; return "" + n + "ھەرپ ئۆچۈرۈڭ";},
        formatSelectionTooBig: function (limit) { return "ئەڭ كۆپ بولغاندا" + limit + " تال ئۇچۇر تاللىيالايسىز"; },
        formatLoadMore: function (pageNumber) { return "ئۇچۇرلار ئوقۇلىۋاتىدۇ…"; },
        formatSearching: function () { return "ئىزدەۋاتىدۇ…"; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['ug-CN']);
})(jQuery);
