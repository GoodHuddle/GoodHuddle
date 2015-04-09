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
 * Select2 Azerbaijani translation.
 *
 * Author: Farhad Safarov <farhad.safarov@gmail.com>
 */
(function ($) {
    "use strict";

     $.fn.select2.locales['az'] = {
        formatMatches: function (matches) { return matches + " nəticə mövcuddur, hərəkət etdirmək üçün yuxarı və aşağı düymələrindən istifadə edin."; },
        formatNoMatches: function () { return "Nəticə tapılmadı"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return n + " simvol daxil edin"; },
        formatInputTooLong: function (input, max) { var n = input.length - max; return n + " simvol silin"; },
        formatSelectionTooBig: function (limit) { return "Sadəcə " + limit + " element seçə bilərsiniz"; },
        formatLoadMore: function (pageNumber) { return "Daha çox nəticə yüklənir…"; },
        formatSearching: function () { return "Axtarılır…"; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['az']);
})(jQuery);
