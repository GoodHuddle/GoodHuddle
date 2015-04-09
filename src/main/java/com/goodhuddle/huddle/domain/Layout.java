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


import javax.persistence.*;

@Entity
@Table(name = "layout")
@SequenceGenerator(name = "sequence_generator", sequenceName = "layout_id_seq")
public class Layout extends AbstractHuddleObject<Long> {

    public enum Type { page, blog, blogPost }

    @ManyToOne
    @JoinColumn(name="theme_id")
    private Theme theme;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    public Layout() {
    }

    public Layout(Theme theme, String name, Type type) {
        super(theme.getHuddle());
        this.theme = theme;
        this.name = name;
        this.type = type;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
