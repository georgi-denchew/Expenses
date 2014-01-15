/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expenses.lazyModels;

import expensesDb.Expenses;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Georgi Ivanov
 */
public class LazyExpensesDataModel extends LazyDataModel<Expenses> {

    private List<Expenses> datasource;

    public LazyExpensesDataModel(List<Expenses> datasource) {
        this.datasource = datasource;
    }

    @Override
    public Expenses getRowData(String rowKey) {
        for (Expenses expenses : datasource) {
            if (expenses.getExpenseId().toString().equals(rowKey)) {
                return expenses;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(Expenses expenses) {
        return expenses.getExpenseId();
    }

    @Override
    public List<Expenses> load(int first, int pageSize, String sortField,
            SortOrder sortOrder, Map<String, String> filters) {
        List<Expenses> data = new ArrayList<>();

        for (Expenses expense : datasource) {
            boolean match = true;

            for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                try {
                    String filterProperty = it.next();
                    String filterValue = filters.get(filterProperty);
                    String fieldValue = String.valueOf(expense.getClass().getField(filterProperty).get(expense));

                    if (filterValue == null || fieldValue.startsWith(filterValue)) {
                        match = true;
                    } else {
                        match = false;
                        break;
                    }
                } catch (Exception e) {
                    match = false;
                }
            }

            if (match) {
                data.add(expense);
            }
        }

        if (sortField != null) {
            //Collections.sort(data, new LazySorter(sortField, sortOrder));  
        }
        
        return null;
    }

}
