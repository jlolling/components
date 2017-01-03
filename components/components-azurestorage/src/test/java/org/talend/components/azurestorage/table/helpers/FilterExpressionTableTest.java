// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.components.azurestorage.table.helpers;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.talend.components.azurestorage.table.AzureStorageTableProperties;
import org.talend.components.azurestorage.table.helpers.FilterExpressionTable.Comparison;
import org.talend.components.azurestorage.table.helpers.FilterExpressionTable.Predicate;
import org.talend.daikon.properties.ValidationResult;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyValueEvaluator;

public class FilterExpressionTableTest {

    List<String> columns = new ArrayList<>();

    List<String> values = new ArrayList<>();

    List<Comparison> functions = new ArrayList<>();

    List<Predicate> predicates = new ArrayList<>();

    FilterExpressionTable fet = new FilterExpressionTable("tests");

    public void clearLists() {
        columns.clear();
        values.clear();
        functions.clear();
        predicates.clear();
    }

    public void setTableVals() {
        fet.setupProperties();
        fet.column.setValue(columns);
        fet.function.setValue(functions);
        fet.operand.setValue(values);
        fet.predicate.setValue(predicates);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetPossibleValues() {
        fet.setupProperties();
        assertThat((List<Comparison>) fet.function.getPossibleValues(), contains(Comparison.EQUAL, Comparison.NOT_EQUAL,
                Comparison.GREATER_THAN, Comparison.GREATER_THAN_OR_EQUAL, Comparison.LESS_THAN, Comparison.LESS_THAN_OR_EQUAL));
        assertThat((List<Predicate>) fet.predicate.getPossibleValues(), contains(Predicate.AND, Predicate.OR, Predicate.NOT));
        fet.function.setStoredValue(Arrays.asList("GREATER_THAN", "EQUAL", "LESS_THAN"));
        assertEquals(Arrays.asList(Comparison.GREATER_THAN, Comparison.EQUAL, Comparison.LESS_THAN).toString(),
                fet.function.getValue().toString());

        fet.function.setValueEvaluator(new PropertyValueEvaluator() {

            @SuppressWarnings("rawtypes")
            @Override
            public <T> T evaluate(Property<T> property, Object storedValue) {
                List convertedValues = new ArrayList();
                List values = (List) storedValue;
                for (Object value : values) {
                    convertedValues.add(Comparison.valueOf((String) value));
                }
                return (T) convertedValues;
            }
        });
        assertEquals(Arrays.asList(Comparison.GREATER_THAN, Comparison.EQUAL, Comparison.LESS_THAN), fet.function.getValue());
    }

    @Test
    public void testFilterExpressionTable() {
        String query;
        clearLists();
        //
        columns.add(AzureStorageTableProperties.TABLE_PARTITION_KEY);
        functions.add(Comparison.EQUAL);
        values.add("12345");
        predicates.add(Predicate.NOT);
        setTableVals();
        query = fet.getCombinedFilterConditions();
        assertEquals(query, "PartitionKey eq '12345'");
        //
        columns.add(AzureStorageTableProperties.TABLE_ROW_KEY);
        functions.add(Comparison.GREATER_THAN);
        values.add("AVID12345");
        predicates.add(Predicate.AND);
        setTableVals();
        query = fet.getCombinedFilterConditions();
        assertEquals(query, "(PartitionKey eq '12345') and (RowKey gt 'AVID12345')");
        //
        columns.add(AzureStorageTableProperties.TABLE_TIMESTAMP);
        functions.add(Comparison.GREATER_THAN_OR_EQUAL);
        values.add("2016-01-01 00:00:00");
        predicates.add(Predicate.OR);
        setTableVals();
        query = fet.getCombinedFilterConditions();
        assertEquals(query, "((PartitionKey eq '12345') and (RowKey gt 'AVID12345')) or (Timestamp ge '2016-01-01 00:00:00')");
        //
        columns.add("AnUnknownProperty");
        functions.add(Comparison.LESS_THAN);
        values.add("WEB345");
        predicates.add(Predicate.OR);
        setTableVals();
        query = fet.getCombinedFilterConditions();
        assertEquals(query,
                "(((PartitionKey eq '12345') and (RowKey gt 'AVID12345')) or (Timestamp ge '2016-01-01 00:00:00')) or (AnUnknownProperty lt 'WEB345')");
    }

    @Test
    public void testValidateFilterExpession() {
        clearLists();
        // empty
        assertEquals(ValidationResult.OK, fet.validateFilterExpession());
        // ok
        columns.add(AzureStorageTableProperties.TABLE_PARTITION_KEY);
        functions.add(Comparison.EQUAL);
        values.add("12345");
        predicates.add(Predicate.NOT);
        setTableVals();
        assertEquals(ValidationResult.OK, fet.validateFilterExpession());
        // missing value
        columns.add(AzureStorageTableProperties.TABLE_ROW_KEY);
        functions.add(Comparison.GREATER_THAN);
        values.add("");
        predicates.add(Predicate.AND);
        setTableVals();
        assertEquals(ValidationResult.Result.ERROR, fet.validateFilterExpession().getStatus());
        // missing column
        columns.add("");
        functions.add(Comparison.GREATER_THAN);
        values.add("123456");
        predicates.add(Predicate.AND);
        setTableVals();
        assertEquals(ValidationResult.Result.ERROR, fet.validateFilterExpession().getStatus());
    }

}
