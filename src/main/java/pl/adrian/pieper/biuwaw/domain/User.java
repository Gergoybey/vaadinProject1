/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.biuwaw.domain;

/**
 *
 * @author Adi
 */
public class User {
    private String email;

    public User(String email) {
        this.email = email;
    }
 
    public String getName() {
        return email;
    }
}
