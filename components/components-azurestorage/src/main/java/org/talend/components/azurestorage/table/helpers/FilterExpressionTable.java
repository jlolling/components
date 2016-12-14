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

import static org.talend.daikon.properties.property.PropertyFactory.newEnumList;
import static org.talend.daikon.properties.property.PropertyFactory.newProperty;

import java.util.List;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.talend.components.api.properties.ComponentPropertiesImpl;
import org.talend.daikon.properties.ValidationResult;
import org.talend.daikon.properties.ValidationResult.Result;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.EnumListProperty;
import org.talend.daikon.properties.property.Property;

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

    public static final TypeLiteral<List<String>> LIST_STRING_TYPE = new TypeLiteral<List<String>>() {
    };

    public static final TypeLiteral<List<Comparison>> LIST_COMPARISON_TYPE = new TypeLiteral<List<Comparison>>() {
    };

    public static final TypeLiteral<List<Predicate>> LIST_PREDICATE_TYPE = new TypeLiteral<List<Predicate>>() {
    };

    public Property<List<String>> column = newProperty(LIST_STRING_TYPE, "column"); //$NON-NLS-1$

    public EnumListProperty<Comparison> function = newEnumList("function", LIST_COMPARISON_TYPE);

    public Property<List<String>> operand = newProperty(LIST_STRING_TYPE, "operand"); //$NON-NLS-1$

    public EnumListProperty<Predicate> predicate = newEnumList("predicate", LIST_PREDICATE_TYPE);

    public FilterExpressionTable(String name) {
        super(name);
    }

    @Override
    public void setupProperties() {
        function.setPossibleValues(Comparison.values());
        predicate.setPossibleValues(Predicate.values());
    }

    @Override
    public void setupLayout() {
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addColumn(column);
        mainForm.addColumn(function);
        mainForm.addColumn(operand);
        mainForm.addColumn(predicate);
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
            break;
        }

        return null;
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
            break;
        }

        return null;
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
        for (int idx = 0; idx < column.getValue().size(); idx++) {
            String c = column.getValue().get(idx);
            String f = getComparison(function.getValue().get(idx));
            String v = operand.getValue().get(idx);
            String p = getOperator(predicate.getValue().get(idx));

            String flt = TableQuery.generateFilterCondition(c, f, v);

            if (!filter.isEmpty()) {
                filter = TableQuery.combineFilters(filter, p, flt);
            } else {
                filter = flt;
            }
        }
        return filter;
    }

}
