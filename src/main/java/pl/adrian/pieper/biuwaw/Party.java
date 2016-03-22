/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.biuwaw;

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
