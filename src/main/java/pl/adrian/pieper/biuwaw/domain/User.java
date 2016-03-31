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
    private final List<Gift> gifts = new ArrayList<>();
    
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

    public void remove(Gift gift) {
        gifts.remove(gift);
        gift.setStatus(Gift.Status.FREE);
        gift.setBuyer(null);
    }

    public void add(Gift gift) {
        gifts.add(gift);
        gift.setBuyer(this);
        gift.setStatus(Gift.Status.HAS_BUYER);
    }

    @Override
    public String toString() {
        return email;
    }
    
    
}
