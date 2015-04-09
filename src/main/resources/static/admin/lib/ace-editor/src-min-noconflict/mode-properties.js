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

ace.define("ace/mode/properties_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"],function(e,t,n){"use strict";var r=e("../lib/oop"),i=e("./text_highlight_rules").TextHighlightRules,s=function(){var e=/\\u[0-9a-fA-F]{4}|\\/;this.$rules={start:[{token:"comment",regex:/[!#].*$/},{token:"keyword",regex:/[=:]$/},{token:"keyword",regex:/[=:]/,next:"value"},{token:"constant.language.escape",regex:e},{defaultToken:"variable"}],value:[{regex:/\\$/,token:"string",next:"value"},{regex:/$/,token:"string",next:"start"},{token:"constant.language.escape",regex:e},{defaultToken:"string"}]}};r.inherits(s,i),t.PropertiesHighlightRules=s}),ace.define("ace/mode/properties",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/properties_highlight_rules"],function(e,t,n){"use strict";var r=e("../lib/oop"),i=e("./text").Mode,s=e("./properties_highlight_rules").PropertiesHighlightRules,o=function(){this.HighlightRules=s};r.inherits(o,i),function(){this.$id="ace/mode/properties"}.call(o.prototype),t.Mode=o})
