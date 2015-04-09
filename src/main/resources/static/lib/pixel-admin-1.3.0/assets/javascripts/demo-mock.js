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

$(function(){function a(a,b){var c,d=[];d.push(a.type.toUpperCase()+' url = "'+a.url+'"');for(var e in a.data){if(a.data[e]&&"object"==typeof a.data[e]){c=[];for(var f in a.data[e])c.push(f+': "'+a.data[e][f]+'"');c="{ "+c.join(", ")+" }"}else c='"'+a.data[e]+'"';d.push(e+" = "+c)}d.push("RESPONSE: status = "+b.status),b.responseText&&($.isArray(b.responseText)?(d.push("["),$.each(b.responseText,function(a,b){d.push("{value: "+b.value+', text: "'+b.text+'"}')}),d.push("]")):d.push($.trim(b.responseText))),d.push("--------------------------------------\n")}$.mockjaxSettings.responseTime=500,$.mockjax({url:"/post",response:function(b){try{a(b,this)}catch(c){}}}),$.mockjax({url:"/error",status:400,statusText:"Bad Request",response:function(b){this.responseText="Please input correct value";try{a(b,this)}catch(c){}}}),$.mockjax({url:"/status",status:500,response:function(b){this.responseText="Internal Server Error";try{a(b,this)}catch(c){}}}),$.mockjax({url:"/groups",response:function(b){this.responseText=[{value:0,text:"Guest"},{value:1,text:"Service"},{value:2,text:"Customer"},{value:3,text:"Operator"},{value:4,text:"Support"},{value:5,text:"Admin"}];try{a(b,this)}catch(c){}}})});
