/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.biuwaw;

import pl.adrian.pieper.biuwaw.domain.Guest;
import pl.adrian.pieper.biuwaw.domain.Gift;
import pl.adrian.pieper.biuwaw.services.PartyService;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.adrian.pieper.biuwaw.services.UsersManager;

/**
 *
 * @author Adi
 */
public class NewPartyView extends CustomComponent implements View{
    
    private final PartyService partyService = PartyService.getInstance();
    private final BeanItemContainer<Gift> gifts = new BeanItemContainer<>(Gift.class);
    private final BeanItemContainer<Guest> guests = new BeanItemContainer<>(Guest.class);
    private final FieldGroup editedGift = new FieldGroup(new BeanItem<>(new Gift()));
    private final TextField partNameTF = new TextField("Nazwa Wydarzenia");

    public NewPartyView() {
        
    }
    
    private void saveParty(Button.ClickEvent event) {
        partyService.addParty(partNameTF.getValue(),gifts.getItemIds(),guests.getItemIds());
        setCompositionRoot(new Label("Zapisano..."));
    }
    
    private Table createGuestsTable(){
        final Table guestsTable = new Table("Goście", guests);
        guestsTable.setColumnHeader("email", "adres e-mail");
        guestsTable.setSizeFull();
        return guestsTable;
    }
    
    private AbstractComponent createGiftView(){
        return new VerticalLayout(new Label("Prezenty"),new HorizontalLayout(new NewGift(),createGiftEditor()),createGiftsTable());
    }
    
    private AbstractComponent createGuestsView(){
        return new VerticalLayout(new Label("Goście"),createNewGuestForm(),createGuestsTable());
    }
    
    private void itemClick(ItemClickEvent event) {
        editedGift.setItemDataSource(event.getItem());
    }
    
    private Table createGiftsTable(){
        
        final Table giftsTable = new Table("Gifts", gifts);
        giftsTable.setColumnHeader("name", "Nazwa");
        giftsTable.setSizeFull();
        giftsTable.addItemClickListener(this::itemClick);
        return giftsTable;
    }
    
    private AbstractComponent createPartyForm(){
        VerticalLayout layout = new VerticalLayout();
        layout.addComponents(
            partNameTF
        );
        layout.setMargin(true);
        return layout;
    }
    
    private AbstractComponent createNewGuestForm(){
        final FormLayout newGuestForm = new FormLayout();
        final Guest guest = new Guest();
        final FieldGroup fieldGroup = new FieldGroup(new BeanItem(guest));
        fieldGroup.setBuffered(false);
        final Button addButton = new Button("Nowy Gość");
        addButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    fieldGroup.commit();
                    guests.addBean(new Guest(guest));
                } catch (FieldGroup.CommitException ex) {
                    Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        newGuestForm.addComponents(
            new Label(),
            fieldGroup.buildAndBind("adres e-mail","email"),
            addButton
        );
        
        return newGuestForm;
    }
            
    private AbstractComponent createGiftEditor(){
        final FormLayout formLayout = new FormLayout();
        editedGift.setBuffered(false);
        final Button editButton = new Button("Zatwierdź");
        editButton.addClickListener((event) -> {
            try {
                editedGift.commit();
            } catch (FieldGroup.CommitException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        formLayout.addComponents(
                new Label("Edytor"),
                editedGift.buildAndBind("name"),
                editButton
        );
            
        formLayout.setMargin(true);
            
        return formLayout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if (UsersManager.getInstance().isUserOnline()){
            final VerticalLayout rootLayout = new VerticalLayout();

            Button saveButton = new Button("Zapisz");
            saveButton.addClickListener(this::saveParty);

            rootLayout.addComponents(
                    new Label(UsersManager.getInstance().getUser().getName()),
                    createPartyForm(),
                    createGiftView(),
                    createGuestsView(),
                    saveButton
            );
            rootLayout.setMargin(true);
            rootLayout.setSpacing(true);
            setCompositionRoot(rootLayout);
        }
    }
    
    private class NewGift extends FormLayout{
        private final BeanItem<Gift> newGiftBeanItem = new BeanItem<>(new Gift());

        public NewGift() {
        
            final FieldGroup fieldGroup = new FieldGroup(newGiftBeanItem);
            
            addComponents(
                    new Label("Nowy prezent"),
                    fieldGroup.buildAndBind("name")
            );
            
            fieldGroup.setBuffered(true);
            Button addButton = new Button("Nowy Prezent");
            addButton.addClickListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    try {
                        fieldGroup.commit();
                        Gift newGift = newGiftBeanItem.getBean();
                        gifts.addBean(new Gift(newGift));
                    } catch (FieldGroup.CommitException ex) {
                        Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            addComponent(addButton);
        }
    }
    
    
}
