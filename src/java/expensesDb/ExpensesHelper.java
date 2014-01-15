/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expensesDb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.hql.ast.tree.RestrictableStatement;

/**
 *
 * @author Georgi Ivanov
 */
public class ExpensesHelper {

    Session session = null;
    UsersHelper usersHelper;
    CategoriesHelper categoriesHelper;

    public ExpensesHelper() {
        this.session = HibernateUtil.getSessionFactory().openSession();
        this.usersHelper = new UsersHelper();
        this.categoriesHelper = new CategoriesHelper();
    }

    public List<Expenses> getAllExpenses(String sessionKey) {

        List<Expenses> resultList = new ArrayList<>();

        Transaction transaction = this.session.beginTransaction();

        try {
            int userId = this.usersHelper.getUserId(sessionKey);

            Criteria criteria = this.session.createCriteria(Expenses.class);
            criteria.add(Restrictions.eq("users.userId", userId));

            resultList = (List<Expenses>) criteria.list();

        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public void addExpense(String sessionKey, Expenses expenseToAdd, int categoryId) {
        Users user = this.usersHelper.getUser(sessionKey);
        Categories category = this.categoriesHelper.getCategory(categoryId);

        expenseToAdd.setCategories(category);
        expenseToAdd.setUsers(user);
        
        if (expenseToAdd.getDate() == null) {
            expenseToAdd.setDate(new Date());
        }
        
        Transaction transaction = this.session.beginTransaction();

        try {
            this.session.saveOrUpdate(expenseToAdd);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    public boolean deleteExpense(String sessionKey, Expenses expense) {
        boolean deleteSuccessful = false;

        Users user = this.usersHelper.getUser(sessionKey);

        Transaction transaction = this.session.beginTransaction();

        try {
            if (user.getUserId() == expense.getUsers().getUserId()) {
                transaction.begin();
                this.session.delete(expense);
                transaction.commit();
                deleteSuccessful = true;
            }
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return deleteSuccessful;
    }
}
