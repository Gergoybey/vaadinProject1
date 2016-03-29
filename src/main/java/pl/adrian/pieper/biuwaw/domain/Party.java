/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.biuwaw.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Adi
 */
public class Party {
    private String name;
    private List<Gift> gifts;
    private List<Guest> guests;

    public Party(String name, List<Gift> gifts, List<Guest> guests) {
        this.name = name;
        this.gifts = new ArrayList<>(gifts);
        this.guests = new ArrayList<>(guests);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addGifts(Collection<? extends Gift> allGifts) {
        gifts = new ArrayList<>(allGifts);
    }
    
    
}
