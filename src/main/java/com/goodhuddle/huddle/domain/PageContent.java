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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageContent {

    @Valid
    private List<Row> rows;

    public PageContent() {
        this.rows = new ArrayList<>();
    }

    public List<Row> getRows() {
        return rows;
    }

    //-------------------------------------------------------------------------

    public static final class Row {

        private String cssClass;
        private boolean fullWidth;
        private List<Cell> cells;

        public Row() {
            this.cells = new ArrayList<>();
        }

        public boolean isFullWidth() {
            return fullWidth;
        }

        public String getCssClass() {
            return cssClass;
        }

        public List<Cell> getCells() {
            return cells;
        }
    }

    //-------------------------------------------------------------------------

    public static final class Cell {

        private int width;
        private List<Block> blocks;

        public Cell() {
        }

        public Cell(int width) {
            this.width = width;
            this.blocks = new ArrayList<>();
        }

        public int getWidth() {
            return width;
        }

        public List<Block> getBlocks() {
            return blocks;
        }
    }

    //-------------------------------------------------------------------------

    public static Block createHtmlBlock(String html) {
        Map map = new HashMap();
        map.put("html", html);
        return new Block("html", map);
    }

    public static final class Block {

        private String type;
        private Map value;

        public Block() {
        }

        public Block(String type, Map value) {
            this.type = type;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public Map getValue() {
            return value;
        }
    }
}
