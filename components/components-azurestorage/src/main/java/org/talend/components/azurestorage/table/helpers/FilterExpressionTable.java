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

import static org.talend.daikon.properties.property.PropertyFactory.newProperty;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.talend.components.api.properties.ComponentPropertiesImpl;
import org.talend.daikon.properties.ValidationResult;
import org.talend.daikon.properties.ValidationResult.Result;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyValueEvaluator;

import com.microsoft.azure.storage.table.EdmType;
import com.microsoft.azure.storage.table.TableQuery;
import com.microsoft.azure.storage.table.TableQuery.Operators;
import com.microsoft.azure.storage.table.TableQuery.QueryComparisons;

public class FilterExpressionTable extends ComponentPropertiesImpl {

    private static final long serialVersionUID = -5175064100089239187L;

    public enum Comparison {
        EQUAL,
        NOT_EQUAL,
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN,
        LESS_THAN_OR_EQUAL
    }

    public enum Predicate {
        AND,
        OR,
        NOT
    }

    public enum FieldType {
        STRING,
        NUMERIC,
        DATE,
        INT64,
        BINARY,
        GUID
    }

    public static final TypeLiteral<List<String>> LIST_STRING_TYPE = new TypeLiteral<List<String>>() {
    };

    public static final TypeLiteral<List<Comparison>> LIST_COMPARISON_TYPE = new TypeLiteral<List<Comparison>>() {
    };

    public static final TypeLiteral<List<Predicate>> LIST_PREDICATE_TYPE = new TypeLiteral<List<Predicate>>() {
    };

    public static final TypeLiteral<List<FieldType>> LIST_FIELDTYPE_TYPE = new TypeLiteral<List<FieldType>>() {
    };

    public Property<List<String>> column = newProperty(LIST_STRING_TYPE, "column"); //$NON-NLS-1$

    public Property<List<String>> operand = newProperty(LIST_STRING_TYPE, "operand"); //$NON-NLS-1$

    public Property<List<Comparison>> function = newProperty(LIST_COMPARISON_TYPE, "function");

    public Property<List<Predicate>> predicate = newProperty(LIST_PREDICATE_TYPE, "predicate");

    public Property<List<FieldType>> fieldType = newProperty(LIST_FIELDTYPE_TYPE, "fieldType");
    // public EnumListProperty<Comparison> function = newEnumList("function", LIST_COMPARISON_TYPE);
    // public EnumListProperty<Predicate> predicate = newEnumList("predicate", LIST_PREDICATE_TYPE);

    public FilterExpressionTable(String name) {
        super(name);

        function.setValueEvaluator(new PropertyValueEvaluator() {

            @SuppressWarnings({ "rawtypes", "unchecked" })
            @Override
            public <T> T evaluate(Property<T> property, Object storedValue) {
                List convertedValues = new ArrayList();
                List values = (List) storedValue;
                for (Object value : values) {
                    if (value instanceof Comparison)
                        convertedValues.add(value);
                    else
                        convertedValues.add(Comparison.valueOf((String) value));
                }
                return (T) convertedValues;
            }
        });

        predicate.setValueEvaluator(new PropertyValueEvaluator() {

            @SuppressWarnings({ "rawtypes", "unchecked" })
            @Override
            public <T> T evaluate(Property<T> property, Object storedValue) {
                List convertedValues = new ArrayList();
                List values = (List) storedValue;
                for (Object value : values) {
                    if (value instanceof Predicate)
                        convertedValues.add(value);
                    else
                        convertedValues.add(Predicate.valueOf((String) value));
                }
                return (T) convertedValues;
            }
        });
    }

    @Override
    public void setupProperties() {
        function.setPossibleValues(Comparison.values());
        predicate.setPossibleValues(Predicate.values());
        fieldType.setPossibleValues(FieldType.values());
    }

    @Override
    public void setupLayout() {
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addColumn(column);
        mainForm.addColumn(Widget.widget(function).setWidgetType(Widget.ENUMERATION_WIDGET_TYPE));
        mainForm.addColumn(operand);
        mainForm.addColumn(Widget.widget(predicate).setWidgetType(Widget.ENUMERATION_WIDGET_TYPE));
        mainForm.addColumn(Widget.widget(fieldType).setWidgetType(Widget.ENUMERATION_WIDGET_TYPE));
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);

        if (form.getName() == Form.MAIN && size() > 0) {
            form.setSubtitle(getCombinedFilterConditions());
        }
    }

    public String getComparison(Comparison f) {
        switch (f) {
        case EQUAL:
            return QueryComparisons.EQUAL;
        case NOT_EQUAL:
            return QueryComparisons.NOT_EQUAL;
        case GREATER_THAN:
            return QueryComparisons.GREATER_THAN;
        case GREATER_THAN_OR_EQUAL:
            return QueryComparisons.GREATER_THAN_OR_EQUAL;
        case LESS_THAN:
            return QueryComparisons.LESS_THAN;
        case LESS_THAN_OR_EQUAL:
            return QueryComparisons.LESS_THAN_OR_EQUAL;
        default:
            return null;
        }
    }

    public String getOperator(Predicate p) {
        switch (p) {
        case AND:
            return Operators.AND;
        case OR:
            return Operators.OR;
        case NOT:
            return Operators.NOT;
        default:
            return null;
        }
    }

    public EdmType getType(FieldType ft) {
        switch (ft) {
        case STRING:
            return EdmType.STRING;
        case NUMERIC:
            return EdmType.INT32;
        case INT64:
            return EdmType.INT64;
        case DATE:
            return EdmType.DATE_TIME;
        case BINARY:
            return EdmType.BINARY;
        case GUID:
            return EdmType.GUID;
        default:
            return null;
        }
    }

    public int size() {
        if (column.getValue() != null && !column.getValue().isEmpty())
            return column.getValue().size();
        else
            return 0;
    }

    public ValidationResult validateFilterExpession() {
        Boolean filterOk = true;
        if (size() == 0)
            return ValidationResult.OK;
        for (int idx = 0; idx < column.getValue().size(); idx++) {
            filterOk = filterOk && (!column.getValue().get(idx).isEmpty()) && (!operand.getValue().get(idx).isEmpty());
            if (!filterOk) {
                ValidationResult vr = new ValidationResult();
                vr.setStatus(Result.ERROR);
                vr.setMessage("Missing column name or value !");
                return vr;
            }
        }

        return ValidationResult.OK;
    }

    public String getCombinedFilterConditions() {
        String filter = "";
        if (validateFilterExpession().getStatus().equals(Result.ERROR))
            return filter;
        for (int idx = 0; idx < size(); idx++) {
            String c = column.getValue().get(idx);
            Object _t = function.getValue().get(idx);
            Comparison cfn = _t instanceof String ? Comparison.valueOf(_t.toString()) : (Comparison) _t;
            _t = predicate.getValue().get(idx);
            Predicate cop = _t instanceof String ? Predicate.valueOf(_t.toString()) : (Predicate) _t;
            _t = fieldType.getValue().get(idx);
            FieldType typ = _t instanceof String ? FieldType.valueOf(_t.toString()) : (FieldType) _t;

            String f = getComparison(cfn);
            String v = operand.getValue().get(idx);
            String p = getOperator(cop);

            EdmType t = getType(typ);

            String flt = TableQuery.generateFilterCondition(c, f, v, t);

            if (!filter.isEmpty()) {
                filter = TableQuery.combineFilters(filter, p, flt);
            } else {
                filter = flt;
            }
        }
        return filter;
    }

}
