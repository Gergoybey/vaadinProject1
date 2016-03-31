/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.biuwaw.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import pl.adrian.pieper.biuwaw.domain.Gift;
import pl.adrian.pieper.biuwaw.domain.Guest;
import pl.adrian.pieper.biuwaw.domain.Party;
import pl.adrian.pieper.biuwaw.domain.User;

/**
 *
 * @author Adi
 */

public class PartyService {

    private static final PartyService instance = new PartyService();
    
    private final List<Party> parties = new ArrayList<>();
    private final UsersManager usersManager = UsersManager.getInstance();
    
    static 
    {
        Guest guest = new Guest();
        guest.setEmail("login");
        
        Gift gift = new Gift();
        gift.setName("Ser");
        instance.addParty("Gruba Biba", Arrays.asList(gift),Arrays.asList(guest));
    }
    
    private PartyService() {
    }
    
    public static PartyService getInstance() {
        return instance;
    }

    public void addParty(String partyName, List<Gift> gifts, List<Guest> guests) {
        final Party party = new Party(parties.size()+1,partyName,gifts,guests);
        parties.add(party);
        for (Guest guest : guests) {
            User user = usersManager.getFor(guest);
            user.invite(party);
        }
    }

    public List<Party> getAllParties() {
        return new ArrayList<>(parties);
    }
    
    
    
}
