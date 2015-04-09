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
 * Select2 Traditional Chinese translation
 */
(function ($) {
    "use strict";
    $.fn.select2.locales['zh-TW'] = {
        formatNoMatches: function () { return "沒有找到相符的項目"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return "請再輸入" + n + "個字元";},
        formatInputTooLong: function (input, max) { var n = input.length - max; return "請刪掉" + n + "個字元";},
        formatSelectionTooBig: function (limit) { return "你只能選擇最多" + limit + "項"; },
        formatLoadMore: function (pageNumber) { return "載入中…"; },
        formatSearching: function () { return "搜尋中…"; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['zh-TW']);
})(jQuery);
