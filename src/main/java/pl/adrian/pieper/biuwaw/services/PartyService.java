/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.biuwaw.services;

import java.util.ArrayList;
import java.util.List;
import pl.adrian.pieper.biuwaw.domain.Gift;
import pl.adrian.pieper.biuwaw.domain.Guest;
import pl.adrian.pieper.biuwaw.domain.Party;

/**
 *
 * @author Adi
 */

public class PartyService {

    private static PartyService instance = new PartyService();
    
    private List<Party> parties = new ArrayList<>();
    
    private PartyService() {
    }
    
    public static PartyService getInstance() {
        return instance;
    }

    public void addParty(String partyName, List<Gift> gifts, List<Guest> guests) {
        parties.add(new Party(partyName,gifts,guests));
    }

    public List<Party> getAllParties() {
        return new ArrayList<>(parties);
    }
    
    
    
}
