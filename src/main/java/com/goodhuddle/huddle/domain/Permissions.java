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

package com.goodhuddle.huddle.domain;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Permissions {

    public static List<String> all;

    static {
        try {
            all = new ArrayList<>();
            Class[] permissionClasses = Permissions.class.getDeclaredClasses();
            for (Class permissionClass : permissionClasses) {

                Field[] declaredFields = permissionClass.getDeclaredFields();
                for (Field field : declaredFields) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        String permission = (String) field.get(null);
                        all.add(permission);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error reflectively determining all permissions", e);
        }
    }


    public static final class Admin {

        public static final String access = "admin.access";

        public static final String pageEdit = "admin.page.edit";
        public static final String memberView = "admin.member.view";
    }

    public static final class Member {

        public static final String access = "member.access";
    }
}
