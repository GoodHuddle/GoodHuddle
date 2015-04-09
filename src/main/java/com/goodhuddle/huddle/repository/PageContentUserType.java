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

package com.goodhuddle.huddle.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodhuddle.huddle.domain.PageContent;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class PageContentUserType implements UserType {

    private static final int[] SQL_TYPES = { Types.LONGVARCHAR };

    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return null;
        }
        PageContent content = (PageContent) value;
        // there is probably a more efficient way to do this
        return convertToPageContentObject(convertToString(content));
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        PageContent content = (PageContent) value;
        return convertToString(content);
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == null) {
            return (y != null);
        }
        return (x.equals(y));
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor implementor, Object owner)
            throws HibernateException, SQLException {

        // NOTE: resultSet.wasNull does not reliably work - not sure why - content should never be null so hard coding
        // in the assumption that it is not null for now.

        //if (!resultSet.wasNull()) {
            String contentAsString = resultSet.getString(names[0]);
            return convertToPageContentObject(contentAsString);
        //}
        //return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor implementor)
            throws HibernateException, SQLException {

        if (value == null) {
            st.setNull(index, SQL_TYPES[0]);
        } else {
            PageContent content = (PageContent) value;
            String contentAsString = convertToString(content);
            st.setString(index, contentAsString);
        }
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class returnedClass() {
        return PageContent.class;
    }

    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    private Object convertToPageContentObject(String contentAsString) {
        try {
            if (StringUtils.isNotBlank(contentAsString)) {
                return mapper.readValue(contentAsString, PageContent.class);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error converting page content from JSON to PageContent object", e);
        }
    }

    private String convertToString(PageContent content) {
        try {
            return mapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting page content from PageContent object to JSON", e);
        }
    }
}
