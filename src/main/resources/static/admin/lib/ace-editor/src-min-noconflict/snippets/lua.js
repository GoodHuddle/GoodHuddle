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

ace.define("ace/snippets/lua",["require","exports","module"],function(e,t,n){"use strict";t.snippetText="snippet #!\n	#!/usr/bin/env lua\n	$1\nsnippet local\n	local ${1:x} = ${2:1}\nsnippet fun\n	function ${1:fname}(${2:...})\n		${3:-- body}\n	end\nsnippet for\n	for ${1:i}=${2:1},${3:10} do\n		${4:print(i)}\n	end\nsnippet forp\n	for ${1:i},${2:v} in pairs(${3:table_name}) do\n	   ${4:-- body}\n	end\nsnippet fori\n	for ${1:i},${2:v} in ipairs(${3:table_name}) do\n	   ${4:-- body}\n	end\n",t.scope="lua"})
