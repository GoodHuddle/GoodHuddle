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
      "AM",
      "PM"
    ],
    "DAY": [
      "Sonto",
      "Msombuluko",
      "Lwesibili",
      "Lwesithathu",
      "uLwesine",
      "Lwesihlanu",
      "Mgqibelo"
    ],
    "MONTH": [
      "Januwari",
      "Februwari",
      "Mashi",
      "Apreli",
      "Meyi",
      "Juni",
      "Julayi",
      "Agasti",
      "Septhemba",
      "Okthoba",
      "Novemba",
      "Disemba"
    ],
    "SHORTDAY": [
      "Son",
      "Mso",
      "Bil",
      "Tha",
      "Sin",
      "Hla",
      "Mgq"
    ],
    "SHORTMONTH": [
      "Jan",
      "Feb",
      "Mas",
      "Apr",
      "Mey",
      "Jun",
      "Jul",
      "Aga",
      "Sep",
      "Okt",
      "Nov",
      "Dis"
    ],
    "fullDate": "EEEE dd MMMM y",
    "longDate": "d MMMM y",
    "medium": "d MMM y h:mm:ss a",
    "mediumDate": "d MMM y",
    "mediumTime": "h:mm:ss a",
    "short": "yyyy-MM-dd h:mm a",
    "shortDate": "yyyy-MM-dd",
    "shortTime": "h:mm a"
  },
  "NUMBER_FORMATS": {
    "CURRENCY_SYM": "R",
    "DECIMAL_SEP": ".",
    "GROUP_SEP": ",",
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
        "negPre": "(\u00a4",
        "negSuf": ")",
        "posPre": "\u00a4",
        "posSuf": ""
      }
    ]
  },
  "id": "zu-za",
  "pluralCat": function (n) {  if (n == 1) {   return PLURAL_CATEGORY.ONE;  }  return PLURAL_CATEGORY.OTHER;}
});
}]);
