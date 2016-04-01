package pl.adrian.pieper.biuwaw;

import com.vaadin.annotations.Push;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
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
            
            setWidth(256, Unit.PIXELS);
            loginButton.setSizeFull();
            newAccountButton.setSizeFull();
            emailTF.setSizeFull();
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
            emailTF.setCaption("Adres e-mail");
            emailTF.addValidator(new EmailValidator("Nieprawidlowy adres e-mail"));
            emailTF.setRequired(true);
            
            setWidth(256, Unit.PIXELS);
            button.setSizeFull();
            emailTF.setSizeFull();
            
            button.addClickListener(this::register);
        }

        
        
        private void register(Button.ClickEvent event){
            
            try {
                
                emailTF.validate();
                usersManager.register(emailTF.getValue());
                login(emailTF.getValue());
            } catch (Validator.InvalidValueException | LoginException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }     
    }
    
    
    @WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MainUIServlet extends VaadinServlet {
        
    }
    
    
}
