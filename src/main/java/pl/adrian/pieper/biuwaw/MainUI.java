package pl.adrian.pieper.biuwaw;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Property;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
@Theme("maintheme")
@Widgetset("pl.adrian.pieper.biuwaw.MainWidget")
public class MainUI extends UI {

    final TextField firstNumberTF = new TextField();
    final TextField secondNumberTF = new TextField();
    final Label resultLabel = new Label();
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        
        
        
        firstNumberTF.addValueChangeListener( event -> {
            calc();
        });
        secondNumberTF.addValueChangeListener( event -> {
            calc();
        });
                
        layout.addComponents(firstNumberTF,secondNumberTF,resultLabel);
        layout.setMargin(true);
        layout.setSpacing(true);
        
        setContent(layout);
    }
    
    private void calc(){
        try {
            int first = Integer.parseInt(firstNumberTF.getValue());
            int second = Integer.parseInt(secondNumberTF.getValue());
            resultLabel.setValue(Integer.toString(first + second));
        } catch (Exception e) {
            resultLabel.setValue("?");
        }
    }

    @WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MainUIServlet extends VaadinServlet {
    }
}
