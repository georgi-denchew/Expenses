/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package expensesDb;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Georgi Ivanov
 */
public class CategoriesHelper {
    
    private Session session = null;
    private UsersHelper usersHelper;
    
    
    public CategoriesHelper() {
        this.session = HibernateUtil.getSessionFactory().openSession();
        this.usersHelper = new UsersHelper();
    }
    
    public List<Categories> getAllCategories(String sessionKey) {
        
        List<Categories> resultList = null;
        
        try {
            Transaction transaction = this.session.beginTransaction();
            
            Criteria criteria = this.session.createCriteria(Categories.class);
            criteria.createAlias("userscategorieses", "uc");
            criteria.createAlias("uc.users", "u");
            criteria.add(Restrictions.eq("u.sessionKey", sessionKey));
            
            
            resultList = (List<Categories>)criteria.list();
        
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return resultList;
    }

    Categories getCategory(int categoryId) {
        Categories category = null;
        
        try {
            Transaction transaction = this.session.beginTransaction();
            
            Criteria criteria = this.session.createCriteria(Categories.class);
            criteria.add(Restrictions.eq("categoryId", categoryId));
            
            category = (Categories)criteria.uniqueResult();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return category;
    }
}
