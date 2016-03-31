/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.biuwaw.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adi
 */
public class User {
    private final String email;
    private final List<Party> invitations = new ArrayList<>();
    
    public User(String email) {
        this.email = email;
    }
    
    public void invite(Party party){
        invitations.add(party);
    }
 
    public String getName() {
        return email;
    }

    public List<Party> getInvitations() {
        return new ArrayList<>(invitations);
    }
}
