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
 * Select2 Ukrainian translation.
 * 
 * @author  bigmihail <bigmihail@bigmir.net>
 * @author  Uriy Efremochkin <efremochkin@uriy.me>
 */
(function ($) {
    "use strict";

    $.fn.select2.locales['uk'] = {
        formatMatches: function (matches) { return character(matches, "результат") + " знайдено, використовуйте клавіші зі стрілками вверх та вниз для навігації."; },
        formatNoMatches: function () { return "Нічого не знайдено"; },
        formatInputTooShort: function (input, min) { return "Введіть буль ласка ще " + character(min - input.length, "символ"); },
        formatInputTooLong: function (input, max) { return "Введіть буль ласка на " + character(input.length - max, "символ") + " менше"; },
        formatSelectionTooBig: function (limit) { return "Ви можете вибрати лише " + character(limit, "елемент"); },
        formatLoadMore: function (pageNumber) { return "Завантаження даних…"; },
        formatSearching: function () { return "Пошук…"; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['uk']);

    function character (n, word) {
        return n + " " + word + (n%10 < 5 && n%10 > 0 && (n%100 < 5 || n%100 > 19) ? n%10 > 1 ? "и" : "" : "ів");
    }
})(jQuery);
