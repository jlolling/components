// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.components.jdbc.avro;

import org.apache.avro.Schema;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.converter.AvroConverter;

/**
 * Converts from datum Long to avro String
 */
public class LongToStringConverter implements AvroConverter<Long, String> {

    @Override
    public Schema getSchema() {
        return AvroUtils._string();
    }

    @Override
    public Class<Long> getDatumClass() {
        return Long.class;
    }

    @Override
    public Long convertToDatum(String value) {
        Long datumLong = Long.parseLong(value);
        return datumLong;
    }

    @Override
    public String convertToAvro(Long value) {
        String avroString = value.toString();
        return avroString;
    }

}
