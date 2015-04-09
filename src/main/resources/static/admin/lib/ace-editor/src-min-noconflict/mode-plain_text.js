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

ace.define("ace/mode/plain_text",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/text_highlight_rules","ace/mode/behaviour"],function(e,t,n){"use strict";var r=e("../lib/oop"),i=e("./text").Mode,s=e("./text_highlight_rules").TextHighlightRules,o=e("./behaviour").Behaviour,u=function(){this.HighlightRules=s,this.$behaviour=new o};r.inherits(u,i),function(){this.type="text",this.getNextLineIndent=function(e,t,n){return""},this.$id="ace/mode/plain_text"}.call(u.prototype),t.Mode=u})
