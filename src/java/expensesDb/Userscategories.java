package expensesDb;
// Generated Jan 7, 2014 5:40:32 PM by Hibernate Tools 3.6.0



/**
 * Userscategories generated by hbm2java
 */
public class Userscategories  implements java.io.Serializable {


     private UserscategoriesId id;
     private Categories categories;
     private Users users;

    public Userscategories() {
    }

    public Userscategories(UserscategoriesId id, Categories categories, Users users) {
       this.id = id;
       this.categories = categories;
       this.users = users;
    }
   
    public UserscategoriesId getId() {
        return this.id;
    }
    
    public void setId(UserscategoriesId id) {
        this.id = id;
    }
    public Categories getCategories() {
        return this.categories;
    }
    
    public void setCategories(Categories categories) {
        this.categories = categories;
    }
    public Users getUsers() {
        return this.users;
    }
    
    public void setUsers(Users users) {
        this.users = users;
    }




}

