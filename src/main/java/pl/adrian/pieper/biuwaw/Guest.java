/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.biuwaw;

/**
 *
 * @author Adi
 */
public class Guest {
    private String email;

    public Guest(Guest guest) {
        email = guest.email;
    }

    public Guest() {
        email = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
