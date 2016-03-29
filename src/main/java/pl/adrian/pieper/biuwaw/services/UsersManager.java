/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.biuwaw.services;

import com.vaadin.ui.TextField;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.login.AccountException;
import javax.security.auth.login.LoginException;
import pl.adrian.pieper.biuwaw.domain.Guest;
import pl.adrian.pieper.biuwaw.domain.User;

/**
 *
 * @author Adi
 */
public class UsersManager {
    private Map<String,User> users = new HashMap<>();
    
    private final static UsersManager instance = new UsersManager();

    public static UsersManager getInstance() {
        return instance;
    }
        
    public User getFor(Guest guest){
        final String email = guest.getEmail();
        
        if (users.containsKey(email))
            return users.get(email);
        return createFor(email);
    }
    
    public User login(String email) throws LoginException{
        if (users.containsKey(email))
            return users.get(email);
        throw new LoginException("Użytkownik nie istnieje");
    }

    public User register(String email) throws LoginException{
        if (users.containsKey(email)){
            throw new LoginException("Użytkownik już istnieje");
        }
        return createFor(email);
    }

    private User createFor(String email) {
        
        User user = new User(email);
        users.put(email, user);
        return user;
    }
}
