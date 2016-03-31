/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.biuwaw.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Party {
    private final long id;
    private final String name;
    private final List<Gift> gifts;
    private final List<Guest> guests;

    public Party(long id, String name, List<Gift> gifts, List<Guest> guests) {
        this.id = id;
        this.name = name;
        this.gifts = new ArrayList<>(gifts);
        this.guests = new ArrayList<>(guests);
    }
    
    public String getName() {
        return name;
    }

    public Collection<? extends Gift> getGifts() {
        return new ArrayList<>(gifts);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Party other = (Party) obj;
        return (this.id == other.id);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return name;
    }

    public long getId() {
        return id;
    }
    
    
    
    
    
}
