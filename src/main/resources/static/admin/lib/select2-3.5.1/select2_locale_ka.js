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
 * Select2 Georgian (Kartuli) translation.
 * 
 * Author: Dimitri Kurashvili dimakura@gmail.com
 */
(function ($) {
    "use strict";

    $.fn.select2.locales['ka'] = {
        formatNoMatches: function () { return "ვერ მოიძებნა"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return "გთხოვთ შეიყვანოთ კიდევ " + n + " სიმბოლო"; },
        formatInputTooLong: function (input, max) { var n = input.length - max; return "გთხოვთ წაშალოთ " + n + " სიმბოლო"; },
        formatSelectionTooBig: function (limit) { return "თქვენ შეგიძლიათ მხოლოდ " + limit + " ჩანაწერის მონიშვნა"; },
        formatLoadMore: function (pageNumber) { return "შედეგის ჩატვირთვა…"; },
        formatSearching: function () { return "ძებნა…"; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['ka']);
})(jQuery);
