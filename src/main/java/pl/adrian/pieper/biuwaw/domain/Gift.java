/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.biuwaw.domain;

import java.util.Objects;

/**
 *
 * @author Adi
 */
public class Gift {
    private String name;
    private User buyer;
    private Status status = Status.FREE;
    private Party party;
    
    public Gift(){
        name = "";
    }
    
    public Gift(Gift newGift) {
        name = newGift.name;
        buyer = newGift.buyer;
        status = newGift.status;
        party = newGift.party;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        if (status == Status.BOUGHT){
            buyer.remove(this);
        }
        this.status = status;
    }    
        
    public enum Status{
        HAS_BUYER, FREE, BOUGHT;
    }
}
