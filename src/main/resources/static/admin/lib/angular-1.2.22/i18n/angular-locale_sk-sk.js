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

'use strict';
angular.module("ngLocale", [], ["$provide", function($provide) {
var PLURAL_CATEGORY = {ZERO: "zero", ONE: "one", TWO: "two", FEW: "few", MANY: "many", OTHER: "other"};
$provide.value("$locale", {
  "DATETIME_FORMATS": {
    "AMPMS": [
      "dopoludnia",
      "popoludn\u00ed"
    ],
    "DAY": [
      "nede\u013ea",
      "pondelok",
      "utorok",
      "streda",
      "\u0161tvrtok",
      "piatok",
      "sobota"
    ],
    "MONTH": [
      "janu\u00e1ra",
      "febru\u00e1ra",
      "marca",
      "apr\u00edla",
      "m\u00e1ja",
      "j\u00fana",
      "j\u00fala",
      "augusta",
      "septembra",
      "okt\u00f3bra",
      "novembra",
      "decembra"
    ],
    "SHORTDAY": [
      "ne",
      "po",
      "ut",
      "st",
      "\u0161t",
      "pi",
      "so"
    ],
    "SHORTMONTH": [
      "jan",
      "feb",
      "mar",
      "apr",
      "m\u00e1j",
      "j\u00fan",
      "j\u00fal",
      "aug",
      "sep",
      "okt",
      "nov",
      "dec"
    ],
    "fullDate": "EEEE, d. MMMM y",
    "longDate": "d. MMMM y",
    "medium": "d.M.yyyy H:mm:ss",
    "mediumDate": "d.M.yyyy",
    "mediumTime": "H:mm:ss",
    "short": "d.M.yyyy H:mm",
    "shortDate": "d.M.yyyy",
    "shortTime": "H:mm"
  },
  "NUMBER_FORMATS": {
    "CURRENCY_SYM": "\u20ac",
    "DECIMAL_SEP": ",",
    "GROUP_SEP": "\u00a0",
    "PATTERNS": [
      {
        "gSize": 3,
        "lgSize": 3,
        "macFrac": 0,
        "maxFrac": 3,
        "minFrac": 0,
        "minInt": 1,
        "negPre": "-",
        "negSuf": "",
        "posPre": "",
        "posSuf": ""
      },
      {
        "gSize": 3,
        "lgSize": 3,
        "macFrac": 0,
        "maxFrac": 2,
        "minFrac": 2,
        "minInt": 1,
        "negPre": "-",
        "negSuf": "\u00a0\u00a4",
        "posPre": "",
        "posSuf": "\u00a0\u00a4"
      }
    ]
  },
  "id": "sk-sk",
  "pluralCat": function (n) {  if (n == 1) {   return PLURAL_CATEGORY.ONE;  }  if (n == (n | 0) && n >= 2 && n <= 4) {   return PLURAL_CATEGORY.FEW;  }  return PLURAL_CATEGORY.OTHER;}
});
}]);
