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
 * Select2 Portuguese (Portugal) translation
 */
(function ($) {
    "use strict";

    $.fn.select2.locales['pt-PT'] = {
        formatNoMatches: function () { return "Nenhum resultado encontrado"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return "Introduza " + n + " car" + (n == 1 ? "ácter" : "acteres"); },
        formatInputTooLong: function (input, max) { var n = input.length - max; return "Apague " + n + " car" + (n == 1 ? "ácter" : "acteres"); },
        formatSelectionTooBig: function (limit) { return "Só é possível selecionar " + limit + " elemento" + (limit == 1 ? "" : "s"); },
        formatLoadMore: function (pageNumber) { return "A carregar mais resultados…"; },
        formatSearching: function () { return "A pesquisar…"; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['pt-PT']);
})(jQuery);
