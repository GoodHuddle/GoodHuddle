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
import java.util.List;

@Entity
@Table(name = "menu_item")
@SequenceGenerator(name = "sequence_generator", sequenceName = "menu_item_id_seq")
public class MenuItem extends AbstractHuddleObject<Long> {

    public enum Type { page, blog }

    @Column(name = "label")
    private String label;

    @ManyToOne
    @JoinColumn(name="menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name="parent_item_id")
    private MenuItem parent;

    @Column(name = "position")
    private int position;

    @Column(name = "target_type")
    @Enumerated(EnumType.STRING)
    private Type targetType;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "url")
    private String url;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    @OrderBy("position")
    private List<MenuItem> items;

    public MenuItem() {
    }

    public MenuItem(Huddle huddle, String label, Menu menu, MenuItem parent, int position,
                    Type targetType, Long targetId, String url) {
        super(huddle);
        this.label = label;
        this.menu = menu;
        this.parent = parent;
        this.position = position;
        this.targetType = targetType;
        this.targetId = targetId;
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public MenuItem getParent() {
        return parent;
    }

    public void setParent(MenuItem parent) {
        this.parent = parent;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Type getTargetType() {
        return targetType;
    }

    public void setTargetType(Type targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }
}
