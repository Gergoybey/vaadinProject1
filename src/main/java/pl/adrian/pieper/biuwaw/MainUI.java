package pl.adrian.pieper.biuwaw;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
@Theme("maintheme")
@Widgetset("pl.adrian.pieper.biuwaw.MainWidget")
public class MainUI extends UI {
    
    private final BeanItemContainer<Gift> gifts = new BeanItemContainer<>(Gift.class);
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        
        final VerticalLayout layout = new VerticalLayout();
                
        layout.addComponents(new HorizontalLayout(new PartyForm(),new NewGift(),createGiftEditor()),createTable());
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
    }
    
    private Table createTable(){
        
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
    
    private class PartyForm extends FormLayout{
        private final BeanItem<Party> eventItem = new BeanItem<>(new Party());
        
        public PartyForm() {
        
            final FieldGroup fieldGroup = new FieldGroup(eventItem);
            
            
            addComponents(
                    new Label("Nowe wydarzenie"),
                    fieldGroup.buildAndBind("Nazwa wydarzenia","name")
            );
            setImmediate(true);
            fieldGroup.setBuffered(true);
            setMargin(true);
        }
    }
    
    private class PersonForm extends VerticalLayout{
        private final BeanItem<Person> personItem = new BeanItem<>(new Person());

        public PersonForm() {
        
            final FormLayout formLayout = new FormLayout();
            final FieldGroup fieldGroup = new FieldGroup(personItem);
            
            formLayout.addComponents(
                    fieldGroup.buildAndBind("name"),
                    fieldGroup.buildAndBind("Nazwisko","surName")
            );
            
            fieldGroup.setBuffered(true);
            
            addComponent(formLayout);
            setMargin(true);
        }
    }
    
    final FieldGroup editedGift = new FieldGroup(new BeanItem<>(new Gift()));

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
