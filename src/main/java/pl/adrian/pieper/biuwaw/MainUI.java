package pl.adrian.pieper.biuwaw;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@Theme("maintheme")
@Widgetset("pl.adrian.pieper.biuwaw.MainWidget")
public class MainUI extends UI {
    
    private TextField field = new TextField();
    private Button button = new Button("Zaloguj");
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        
        setContent(new NewPartyView());
    }
    
    
    
    @WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MainUIServlet extends VaadinServlet {
        
    }
    
    
}
