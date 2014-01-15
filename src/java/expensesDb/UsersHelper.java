/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expensesDb;

import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
import java.io.Serializable;
import java.util.List;
import java.util.Random;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Georgi Ivanov
 */
public class UsersHelper {

    private static final int SESSION_KEY_LENGTH = 50;
    private static final String SESSION_KEY_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final Random random = new Random();

    private Session session = null;

    public UsersHelper() {
        this.session = HibernateUtil.getSessionFactory().openSession();
    }

    public String createUser(String username, String authCode) {
        // TODO: validate username and authCode
        
        Transaction transaction = session.beginTransaction();
        

        try {
            Users user = new Users(username, authCode);
            int userId = (Integer) session.save(user);
            //Users savedUser = (Users)savedObject;

            //Query query = session.createSQLQuery("insert into users (Username, AuthCode) "
            //        + "VALUES ( :username , :authCode);");
            //query.setParameter("username", username);
            // query.setParameter("authCode", authCode);
            //result = query.executeUpdate();
            
            user.setSessionKey(generateSessionKey(userId));            
            
            session.update(user);
            transaction.commit();
            
            return user.getSessionKey();

        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
            
            return null;
        }
    }

    public String login(String username, String authCode) {
        Transaction transaction = this.session.beginTransaction();
        
        try{
            Criteria criteria = session.createCriteria(Users.class);
            criteria.add(Restrictions.eq("username", username));
            criteria.add(Restrictions.eq("authCode", authCode));            
            
            Users user = (Users)criteria.uniqueResult();
            
            user.setSessionKey(generateSessionKey(user.getUserId()));
            transaction.commit();
            
            return user.getSessionKey();
        } catch (HibernateException e) {
            
            transaction.rollback();
            e.printStackTrace();
            return null;
        }
    }
    
    private String generateSessionKey(int userId) {
        StringBuilder sessionKeyBuilder = new StringBuilder();

        sessionKeyBuilder.append(userId);

        while (sessionKeyBuilder.length() < 50) {
            char randomChar = SESSION_KEY_CHARACTERS.charAt(random.nextInt(SESSION_KEY_LENGTH));
            sessionKeyBuilder.append(randomChar);
        }

        return sessionKeyBuilder.toString();
    }

    public boolean logout(String sessionKey) {
        Transaction transaction = this.session.beginTransaction();
        
        boolean isLogoutSuccessful = false;
        
        try {
            Criteria criteria = session.createCriteria(Users.class);
            criteria.add(Restrictions.eq("sessionKey", sessionKey));
            
            Users user = (Users)criteria.uniqueResult();
            user.setSessionKey(null);
            
            transaction.commit();
            isLogoutSuccessful = true;
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        }
        
        return isLogoutSuccessful;
    }

    public int getUserId(String sessionKey) {
        Transaction transaction = this.session.beginTransaction();
        int userId = -1;
        
        try {
            Criteria criteria = session.createCriteria(Users.class);
            criteria.add(Restrictions.eq("sessionKey", sessionKey));
            
            Users user = (Users)criteria.uniqueResult();
            
            userId = user.getUserId();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        
        return userId;
    }

    public Users getUser(String sessionKey) {
        Transaction transaction = this.session.beginTransaction();
        Users user = null;
        
        try {
            Criteria criteria = session.createCriteria(Users.class);
            criteria.add(Restrictions.eq("sessionKey", sessionKey));
            
            user = (Users)criteria.uniqueResult();
            
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        
        return user;
    }
}
