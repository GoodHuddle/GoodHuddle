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

ace.define("ace/snippets/haml",["require","exports","module"],function(e,t,n){"use strict";t.snippetText="snippet t\n	%table\n		%tr\n			%th\n				${1:headers}\n		%tr\n			%td\n				${2:headers}\nsnippet ul\n	%ul\n		%li\n			${1:item}\n		%li\nsnippet =rp\n	= render :partial => '${1:partial}'\nsnippet =rpl\n	= render :partial => '${1:partial}', :locals => {}\nsnippet =rpc\n	= render :partial => '${1:partial}', :collection => @$1\n\n",t.scope="haml"})
