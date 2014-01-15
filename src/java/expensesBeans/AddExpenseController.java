/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package expensesBeans;

import expensesDb.Categories;
import expensesDb.CategoriesHelper;
import expensesDb.ExpensesHelper;
import javax.enterprise.context.SessionScoped;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author Georgi Ivanov
 */
@ManagedBean
@javax.faces.bean.SessionScoped
public class AddExpenseController {

    private String description;
    private BigDecimal price;
    private List<Categories> categories;
    
    private ExpensesHelper expensesHelper;
    private CategoriesHelper categoriesHelper;
    
    @ManagedProperty( value = "#{usersController}")
    UsersController usersController;
    /**
     * Creates a new instance of AddExpenseController
     */
    public AddExpenseController() {
        this.categories = null;
        this.description = null;
        this.price = null;
        
        this.expensesHelper = new ExpensesHelper();
        this.categoriesHelper = new CategoriesHelper();
    }
    
    public UsersController getUsersController() {
        return usersController;
    }

    public void setUsersController(UsersController usersController) {
        this.usersController = usersController;
    }

    public List<Categories> getCategories() {
        return categories;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }
    
    @PostConstruct
    public void setCategories() {
        this.categories = this.categoriesHelper.getAllCategories(this.usersController.getSessionKey());
    }
}