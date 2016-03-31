/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.biuwaw;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import static pl.adrian.pieper.biuwaw.MainUI.NEW_ACCOUNT;
import static pl.adrian.pieper.biuwaw.MainUI.NEW_PARTY;
import static pl.adrian.pieper.biuwaw.MainUI.PARTY;
import pl.adrian.pieper.biuwaw.domain.Gift;
import pl.adrian.pieper.biuwaw.domain.Party;
import pl.adrian.pieper.biuwaw.services.UsersManager;

/**
 *
 * @author Adi
 */
class UserPanel extends CustomComponent  {
    private final Label userLabel = new Label();
    private final Button newPartyButton = new Button("Nowe wydarzenie");
    private final ComboBox comboBox = new ComboBox();
    private final UsersManager manager = UsersManager.getInstance();
    private Navigator userNavigator;
    private Party party;

    public UserPanel(MainUI mainUI) {
        if (manager.isUserOnline()){
            
            userLabel.setValue(manager.getUser().getName());
            comboBox.addItems(manager.getUser().getInvitations());
            VerticalLayout verticalLayout = new VerticalLayout();
            Navigator.EmptyView emptyView = new Navigator.EmptyView();
            emptyView.setSizeFull();
            verticalLayout.addComponents(userLabel, newPartyButton, comboBox, emptyView);
            newPartyButton.addClickListener((com.vaadin.ui.Button.ClickEvent event) -> {
                userNavigator.navigateTo(MainUI.NEW_PARTY);
            });
            comboBox.setCaption("Imprezy");
            comboBox.addValueChangeListener((com.vaadin.data.Property.ValueChangeEvent event) -> {
                party = (Party) comboBox.getValue();
                userNavigator.navigateTo(MainUI.PARTY);
            });
            this.userNavigator = new Navigator(mainUI, emptyView);
            userNavigator.addView("", NewPartyView.class);
            userNavigator.addView(NEW_PARTY, new NewPartyView());
            userNavigator.addView(PARTY, new PartyView());
            setCompositionRoot(verticalLayout);
        }
    }
    
    public class PartyView extends CustomComponent implements View{

        private final BeanItemContainer<Gift> gifts = new BeanItemContainer<>(Gift.class);

        public PartyView() {
            Table table = new Table("Prezenty", gifts);
            table.setColumnHeader("name", "Nazwa");
            setCompositionRoot(table);
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            gifts.addAll(party.getGifts());
        }
    }    
}
