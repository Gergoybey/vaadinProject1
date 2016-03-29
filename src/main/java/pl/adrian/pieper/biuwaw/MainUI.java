package pl.adrian.pieper.biuwaw;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Theme("maintheme")
@Widgetset("pl.adrian.pieper.biuwaw.MainWidget")
public class MainUI extends UI {
    
    private final Party party = new Party();
    private final BeanItemContainer<Gift> gifts = new BeanItemContainer<>(Gift.class);
    private final BeanItemContainer<Guest> guests = new BeanItemContainer<>(Guest.class);
    private final BeanItem<Party> partyItem = new BeanItem<>(party);
    private final FieldGroup editedGift = new FieldGroup(new BeanItem<>(new Gift()));
    private FieldGroup partyFormBinding;
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        
        final VerticalLayout layout = new VerticalLayout();
                
        Button saveButton = new Button("Zapisz");
        saveButton.addClickListener((e) -> {
            saveParty();
        });
        HorizontalLayout tables = new HorizontalLayout(createGiftsTable(),createGuestsTable());
        tables.setSizeFull();
        layout.addComponents(
                new HorizontalLayout(createPartyForm(),new NewGift(),createGiftEditor(),createNewGuestForm()),
                tables,
                saveButton
        );
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
    }
    
    private void saveParty() {
        List<Gift> allGifts = gifts.getItemIds();
        party.addGifts(allGifts);
        try {
            partyFormBinding.commit();
        } catch (FieldGroup.CommitException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Table createGuestsTable(){
        final Table guestsTable = new Table("Goście", guests);
        guestsTable.setColumnHeader("email", "adres e-mail");
        guestsTable.setSizeFull();
        return guestsTable;
    }
    
    private Table createGiftsTable(){
        
        final Table giftsTable = new Table("Gifts", gifts);
        giftsTable.setColumnHeader("name", "Nazwa");
        giftsTable.setSizeFull();
        giftsTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                editedGift.setItemDataSource(event.getItem());
            }
        });
        return giftsTable;
    }
    
    private AbstractComponent createPartyForm(){
        FormLayout partyForm = new FormLayout();
        partyFormBinding = new FieldGroup(partyItem);
        partyForm.addComponents(
            new Label("Nowe wydarzenie"),
            partyFormBinding.buildAndBind("Nazwa wydarzenia","name")
        );
        partyFormBinding.setBuffered(true);
        partyForm.setMargin(true);
        return partyForm;
    }
    
    private AbstractComponent createNewGuestForm(){
        final FormLayout newGuestForm = new FormLayout();
        final Guest guest = new Guest();
        final FieldGroup fieldGroup = new FieldGroup(new BeanItem(guest));
        fieldGroup.setBuffered(false);
        final Button addButton = new Button("Add");
        addButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
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
        final Button removeButton = new Button("Usuń");
        final Button editButton = new Button("Zatwierdź");
        editButton.addClickListener((ClickEvent event) -> {
            try {
                editedGift.commit();
            } catch (FieldGroup.CommitException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
        formLayout.addComponents(
                new Label("Edytor"),
                editedGift.buildAndBind("name"),
                editButton,
                removeButton
        );
            
        formLayout.setMargin(true);
            
        return formLayout;
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
            Button addButton = new Button("Add");
            addButton.addClickListener(new Button.ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
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
    
    @WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MainUIServlet extends VaadinServlet {
        
    }
    
    
}
