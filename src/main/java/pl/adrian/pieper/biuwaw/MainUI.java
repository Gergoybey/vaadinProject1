package pl.adrian.pieper.biuwaw;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
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
    
    private final BeanItemContainer<Measurement> measurements = new BeanItemContainer<>(Measurement.class);
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        
        final VerticalLayout layout = new VerticalLayout();
                
        layout.addComponents(new PersonForm(),new NewMeasurement(),createTable());
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
        
        measurements.addBean(Measurement.create());
        measurements.addBean(Measurement.create());
        measurements.addBean(Measurement.create());
        measurements.addBean(Measurement.create());
    }
    
    private Table createTable(){
        
        final Table measurementsTable = new Table("Measurements", measurements);
        measurementsTable.setColumnHeader("id", "L. P.");
        measurementsTable.setColumnHeader("value", "Wynik [v]");
        measurementsTable.setSizeFull();
        return measurementsTable;
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
        }
    }
    
    private class NewMeasurement extends VerticalLayout{
        private final BeanItem<Measurement> measurement = new BeanItem<>(new Measurement());

        public NewMeasurement() {
        
            final FormLayout formLayout = new FormLayout();
            final FieldGroup fieldGroup = new FieldGroup(measurement);
            
            formLayout.addComponent(
                    fieldGroup.buildAndBind("value")
            );
            
            fieldGroup.setBuffered(true);
            Button addButton = new Button("Add");
            addButton.addClickListener(new Button.ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    try {
                        fieldGroup.commit();
                        Measurement newMeasurement = measurement.getBean();
                        measurements.addBean(new Measurement(newMeasurement));
                        
                    } catch (FieldGroup.CommitException ex) {
                        Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            addComponent(formLayout);
            addComponent(addButton);
        }
        
        
    }
    
    @WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MainUIServlet extends VaadinServlet {
    }
    
    
}
