package pl.adrian.pieper.biuwaw;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import pl.adrian.pieper.biuwaw.domain.User;
import pl.adrian.pieper.biuwaw.services.UsersManager;

@Theme("maintheme")
@Widgetset("pl.adrian.pieper.biuwaw.MainWidget")
public class MainUI extends UI {
    
    private Navigator navigator; 
    public static final String NEW_PARTY = "NewParty";
    public static final String NEW_ACCOUNT = "NewAccount";
    private final UsersManager usersManager = UsersManager.getInstance();
    private static final String USER_PANEL = "UserPanel";
    private User onlineUser;
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        
        navigator = new Navigator(this, this);
        navigator.addView("", new LoginView());
        navigator.addView(NEW_PARTY, new NewPartyView());
        navigator.addView(NEW_ACCOUNT, new RegisterView());
        navigator.addView(USER_PANEL, new UserPanel());
    }
    
    private void login(User onlineUser) {
        this.onlineUser = onlineUser;
        navigator.navigateTo(USER_PANEL);
    }
    
    private class UserPanel extends VerticalLayout implements View{

        private final Label userLabel = new Label();
                
        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            addComponent(userLabel);
            userLabel.setValue(onlineUser.getName());
        }
        
    }
    private class RegisterView extends VerticalLayout implements View{
        private final TextField emailTF = new TextField();
        private final Button button = new Button("Zaloguj");

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            addComponents(emailTF,button);
            button.addClickListener(this::register);
        }
        
        private void register(Button.ClickEvent event){
            
            try {
                User user = usersManager.register(emailTF.getValue());
                login(user);
            } catch (LoginException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }     
    }
    
    private class LoginView extends VerticalLayout implements View{
        private final TextField emailTF = new TextField();
        private final Button loginButton = new Button("Zaloguj");
        private final Button newAccountButton = new Button("Nowe Konto");

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            addComponents(emailTF,loginButton,newAccountButton);
            loginButton.addClickListener(this::loggin);
            newAccountButton.addClickListener(this::newAccount);
        }
        
        private void loggin(Button.ClickEvent event){
            try {
                login(usersManager.login(emailTF.getValue()));
            } catch (LoginException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        private void newAccount(Button.ClickEvent event){
            navigator.navigateTo(NEW_ACCOUNT);
        }
    }
    
    @WebServlet(urlPatterns = "/*", name = "MainUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MainUIServlet extends VaadinServlet {
        
    }
    
    
}
