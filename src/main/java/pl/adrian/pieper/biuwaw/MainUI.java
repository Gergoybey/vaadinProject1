package pl.adrian.pieper.biuwaw;

import com.vaadin.annotations.Push;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import pl.adrian.pieper.biuwaw.domain.Gift;
import pl.adrian.pieper.biuwaw.domain.Party;
import pl.adrian.pieper.biuwaw.services.UsersManager;

@Push
@Theme("maintheme")
@Widgetset("pl.adrian.pieper.biuwaw.MainWidget")
public class MainUI extends UI {
    
    public static final String NEW_PARTY = "NewParty";
    public static final String NEW_ACCOUNT = "NewAccount";
    public static final String USER_PANEL = "UserPanel";
    public static final String PARTY = "Party";
    
    private final UsersManager usersManager = UsersManager.getInstance();
        
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        if (usersManager.isUserOnline()){
            setContent(new UserPanel(this));
        }else{
            setContent(new LoginView());
        }
    }
    
    private void login(String username)  { 
        try {
            usersManager.login(username);
            setContent(new UserPanel(this));
        } catch (LoginException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private class LoginView extends VerticalLayout {
        private final TextField emailTF = new TextField();
        private final Button loginButton = new Button("Zaloguj");
        private final Button newAccountButton = new Button("Nowe Konto");

        public LoginView() {
        
            addComponents(emailTF,loginButton,newAccountButton);
            loginButton.addClickListener(this::loggin);
            newAccountButton.addClickListener((e)->{
                setContent(new RegisterView());
            });
        }

        
        
        private void loggin(Button.ClickEvent event){
            login(emailTF.getValue());
        }
    }
    
    private class RegisterView extends VerticalLayout {
        private final TextField emailTF = new TextField();
        private final Button button = new Button("Nowe Konto");

        public RegisterView() {
            addComponents(emailTF,button);
            button.addClickListener(this::register);
        }

        
        
        private void register(Button.ClickEvent event){
            
            try {
                usersManager.register(emailTF.getValue());
                login(emailTF.getValue());
            } catch (LoginException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }     
    }
    
    
    @WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MainUIServlet extends VaadinServlet {
        
    }
    
    
}
