/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expensesBeans;

import expensesDb.Categories;
import expensesDb.CategoriesHelper;
import expensesDb.Expenses;
import expensesDb.ExpensesHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Georgi Ivanov
 */
@ManagedBean
@SessionScoped
public class ExpensesController {

    private Expenses currentExpense;
    private List<Expenses> expensesToList;
    private List<Categories> categories;
    private List<SelectItem> categoriesSelectList;
    private int selectedCategoryId;
    private boolean useCurrentDate;
    private PieChartModel categoriesChart;
   
    
    //private LazyDataModel<Expenses> expensesLazyModel;
    
    private ExpensesHelper expensesHelper;
    private CategoriesHelper categoriesHelper;

    @ManagedProperty(value = "#{usersController}")
    UsersController usersController;

    /**
     * Creates a new instance of ExpensesController
     */
    public ExpensesController() {
        this.categoriesSelectList = new ArrayList<>();
        this.expensesToList = null;
        this.currentExpense = new Expenses();
        this.useCurrentDate = true;

        this.categoriesChart = new PieChartModel();

        this.expensesHelper = new ExpensesHelper();
        this.categoriesHelper = new CategoriesHelper();
    }

    public UsersController getUsersController() {
        return usersController;
    }

    public void setUsersController(UsersController usersController) {
        this.usersController = usersController;
    }

    public boolean isUseCurrentDate() {
        return useCurrentDate;
    }

    public void setUseCurrentDate(boolean useCurrentDate) {
        this.useCurrentDate = useCurrentDate;
    }

    public Expenses getExpenseToAdd() {
        return currentExpense;
    }

    public void setExpenseToAdd(Expenses expenseToAdd) {
        this.currentExpense = expenseToAdd;
    }

    public List<SelectItem> getCategoriesSelectList() {
        return categoriesSelectList;
    }

    public void setCategoriesSelectList(List<SelectItem> categoriesSelectList) {
        this.categoriesSelectList = categoriesSelectList;
    }

    public List<Expenses> getExpensesToList() {
        return expensesToList;
    }

    public void setExpensesToList(List<Expenses> expensesToList) {
        this.expensesToList = expensesToList;
    }

    public List<Categories> getCategories() {
        this.setCategories(this.categoriesHelper.getAllCategories(this.usersController.getSessionKey()));
        return categories;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    public int getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public void setSelectedCategoryId(int selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
    }

    public PieChartModel getCategoriesChart() {
        return categoriesChart;
    }

    public void setCategoriesChart(PieChartModel categoriesChart) {
        this.categoriesChart = categoriesChart;
    }

    @PostConstruct
    public void setAllExpensesForUser() {
        this.expensesToList = expensesHelper.getAllExpenses(this.usersController.getSessionKey());
        this.categories = this.categoriesHelper.getAllCategories(this.usersController.getSessionKey());
        this.fillCategoriesSelectList();
        this.fillCategoriesChart();
        //this.expensesLazyModel = new LazyDataModel(expensesToList);
    }

    public String addExpense() {

        if (this.useCurrentDate) {
            this.currentExpense.setDate(new Date());
        }

        this.expensesHelper.addExpense(this.usersController.getSessionKey(), this.currentExpense, this.selectedCategoryId);

        this.setAllExpensesForUser();
        this.resetCurrentExpense();
        this.resetSelectedCategory();
        return "all-expenses";
    }

    public String editExpense(Expenses expense) {
        this.currentExpense = expense;
        this.selectedCategoryId = this.currentExpense.getCategories().getCategoryId();
        return "add-expense";
    }

    public void deleteExpense(Expenses expense) {
        boolean deleteSuccessful = this.expensesHelper.deleteExpense(this.usersController.getSessionKey(), expense);

        if (deleteSuccessful) {
            this.expensesToList.remove(expense);
        }
    }

    private void fillCategoriesSelectList() {
        this.categoriesSelectList.clear();
        for (Categories category : this.categories) {
            SelectItem categoryItem = new SelectItem(category.getCategoryId(), category.getName());

            categoriesSelectList.add(categoryItem);
        }
    }

    private void resetCurrentExpense() {
        this.currentExpense = new Expenses();
    }

    private void resetSelectedCategory() {
        this.selectedCategoryId = (int) this.categoriesSelectList.get(0).getValue();
    }

    private void fillCategoriesChart() {
        Map<String, BigDecimal> categoriesExpenses = getCategoryExpensesMap();
        
        for (Map.Entry<String, BigDecimal> categoryExpensePair : categoriesExpenses.entrySet()) {
            String category = categoryExpensePair.getKey();
            BigDecimal totalExpensesPrice = categoryExpensePair.getValue();
            
            this.categoriesChart.set(category, totalExpensesPrice);
        }
    }

    private Map<String, BigDecimal> getCategoryExpensesMap() {

        Map<String, BigDecimal> categoriesExpenses = new HashMap<>();

        for (Expenses expenses : expensesToList) {
            String categoryName = expenses.getCategories().getName();
            BigDecimal expensePrice = expenses.getPrice();

            if (!categoriesExpenses.containsKey(categoryName)) {
                categoriesExpenses.put(categoryName, expensePrice);
            } else {
                BigDecimal newValue = categoriesExpenses.get(categoryName).add(expensePrice);
                categoriesExpenses.put(categoryName, newValue);
            }
        }

        return categoriesExpenses;
    }
}