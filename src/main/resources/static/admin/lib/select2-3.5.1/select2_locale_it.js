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
 * Select2 Italian translation
 */
(function ($) {
    "use strict";

    $.fn.select2.locales['it'] = {
        formatNoMatches: function () { return "Nessuna corrispondenza trovata"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return "Inserisci ancora " + n + " caratter" + (n == 1? "e" : "i"); },
        formatInputTooLong: function (input, max) { var n = input.length - max; return "Inserisci " + n + " caratter" + (n == 1? "e" : "i") + " in meno"; },
        formatSelectionTooBig: function (limit) { return "Puoi selezionare solo " + limit + " element" + (limit == 1 ? "o" : "i"); },
        formatLoadMore: function (pageNumber) { return "Caricamento in corso…"; },
        formatSearching: function () { return "Ricerca…"; }
    };

    $.extend($.fn.select2.defaults, $.fn.select2.locales['it']);
})(jQuery);
