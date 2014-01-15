/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expensesBeans;

import expensesDb.UsersHelper;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Georgi Ivanov
 */
@ManagedBean
@SessionScoped
public class UsersController {

    private String username;
    private String password;
    private String sessionKey;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
    protected static MessageDigest cript;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String authCode) {
        this.password = authCode;
    }

    UsersHelper usersHelper;

    /**
     * Creates a new instance of NewJSFManagedBean
     */
    public UsersController() throws NoSuchAlgorithmException {
        this.cript = MessageDigest.getInstance("SHA-1");
        this.usersHelper = new UsersHelper();
        this.username = null;
        this.password = null;
    }

    public String login() throws NoSuchAlgorithmException {
        String authCode = sha1(this.password);
        this.sessionKey = this.usersHelper.login(this.username, authCode);
        this.clearPassword();
        
        return "/expenses/all-expenses";
    }

    public String createUsername() throws NoSuchAlgorithmException {

        String authCode = this.sha1(this.password);
        this.sessionKey = this.usersHelper.createUser(this.username, authCode);
        this.clearPassword();
        
        return "/expenses/all-expenses";
    }

    public String logout() {
        boolean isLogoutSucessful = this.usersHelper.logout(this.sessionKey);
        if (isLogoutSucessful) {
            this.sessionKey = null;
            return "/login";
        }
        return null;
    }

    public boolean hasSessionKey() {
        if (this.sessionKey != null) {
            return this.sessionKey.length() == 50;
        }
        return false;
    }

    private void clearPassword() {
        this.password = null;
    }

    private static String sha1(String input) throws NoSuchAlgorithmException {
        byte[] result = cript.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
