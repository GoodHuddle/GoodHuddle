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
 * Select2 Thai translation.
 *
 * Author: Atsawin Chaowanakritsanakul <joke@nakhon.net>
 */
(function ($) {
    "use strict";

    $.fn.select2.locales['th'] = {
        formatNoMatches: function () { return "ไม่พบข้อมูล"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return "โปรดพิมพ์เพิ่มอีก " + n + " ตัวอักษร"; },
        formatInputTooLong: function (input, max) { var n = input.length - max; return "โปรดลบออก " + n + " ตัวอักษร"; },
        formatSelectionTooBig: function (limit) { return "คุณสามารถเลือกได้ไม่เกิน " + limit + " รายการ"; },
        formatLoadMore: function (pageNumber) { return "กำลังค้นข้อมูลเพิ่ม…"; },
        formatSearching: function () { return "กำลังค้นข้อมูล…"; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['th']);
})(jQuery);
