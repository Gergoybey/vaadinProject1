/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adrian.pieper.biuwaw;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import static pl.adrian.pieper.biuwaw.MainUI.NEW_PARTY;
import static pl.adrian.pieper.biuwaw.MainUI.PARTY;
import pl.adrian.pieper.biuwaw.domain.Gift;
import pl.adrian.pieper.biuwaw.domain.Party;
import pl.adrian.pieper.biuwaw.services.Dispatcher;
import pl.adrian.pieper.biuwaw.services.PartyService;
import pl.adrian.pieper.biuwaw.services.UsersManager;

/**
 *
 * @author Adi
 */
class UserPanel extends CustomComponent implements Dispatcher.BroadcastListener{
    private final Label userLabel = new Label();
    private final Button newPartyButton = new Button("Nowe wydarzenie");
    private final ComboBox comboBox = new ComboBox();
    private final UsersManager manager = UsersManager.getInstance();
    private Navigator userNavigator;
    private PartyView partyView;
    private MainUI mainUI;
    
    @Override
    public void attach() {
        Dispatcher.getInstance().register(this);
        super.attach();
    }
    
    @Override
    public void detach() {
        Dispatcher.getInstance().unregister(this);
        super.detach();
    }
    
    public UserPanel(MainUI mainUI) {
        this.mainUI = mainUI;
        if (manager.isUserOnline()){
            PartyService.getInstance(); // To remove
            userLabel.setValue(manager.getUser().getName());
            comboBox.addItems(manager.getUser().getInvitations());
            VerticalLayout root = new VerticalLayout();
            Navigator.EmptyView emptyView = new Navigator.EmptyView();
            HorizontalLayout userMenu = new HorizontalLayout(userLabel, newPartyButton, comboBox);
            emptyView.setWidth(80, Unit.PERCENTAGE);
            emptyView.setHeightUndefined();
            root.addComponents(userMenu, emptyView);
            root.setComponentAlignment(userMenu, Alignment.MIDDLE_CENTER);
            root.setComponentAlignment(emptyView, Alignment.MIDDLE_CENTER);
            newPartyButton.addClickListener((com.vaadin.ui.Button.ClickEvent event) -> {
                userNavigator.navigateTo(MainUI.NEW_PARTY);
            });
            comboBox.setCaption("Imprezy");
            comboBox.addValueChangeListener((com.vaadin.data.Property.ValueChangeEvent event) -> {
                if (comboBox.getValue() instanceof Party){
                    Party party = (Party) comboBox.getValue();
                    userNavigator.navigateTo(MainUI.PARTY+"/"+party.getId());
                }
            });
            this.userNavigator = new Navigator(mainUI, emptyView);
            final NewPartyView newPartyView = new NewPartyView(userNavigator);
            userNavigator.addView("", new Navigator.EmptyView());
            userNavigator.addView(NEW_PARTY, newPartyView);
            partyView = new PartyView();
            userNavigator.addView(PARTY, partyView);
            setCompositionRoot(root);
        }
    }

    @Override
    public void receiveBroadcast(String message) {
        mainUI.access(() -> {
            if (message.equals("NEW_INVITE")){
                comboBox.removeAllItems();
                comboBox.addItems(manager.getUser().getInvitations());
            }else if (message.equals("GIFT_STATUS")){
                partyView.reloadGifts();;
            }
        });
        
    }
    
    public class PartyView extends CustomComponent implements View{

        private final BeanItemContainer<Gift> partyGifts = new BeanItemContainer<>(Gift.class);
        private final BeanItemContainer<Gift> userGifts = new BeanItemContainer<>(Gift.class);
        private Party party;
    
        public PartyView() {
            final Table partyGiftsTable = new Table("Prezenty", partyGifts);
            partyGiftsTable.setColumnHeader("name", "Nazwa");
            partyGiftsTable.setColumnHeader("status", "Status");
            partyGiftsTable.setColumnHeader("buyer", "Kupujący");
            partyGifts.removeContainerProperty("party");
            partyGiftsTable.addItemClickListener(this::partyGiftClick);
            partyGiftsTable.setWidth(70, Unit.PERCENTAGE);
            final Table userGiftsTable = new Table("Zakupy", userGifts);
            userGifts.removeContainerProperty("party");
            userGifts.removeContainerProperty("buyer");
            userGifts.removeContainerProperty("status");
            userGiftsTable.addItemClickListener(this::userGiftClick);
            userGiftsTable.setWidth(30, Unit.PERCENTAGE);
            HorizontalLayout horizontalLayout = new HorizontalLayout(partyGiftsTable,userGiftsTable);
            horizontalLayout.setSizeFull();
            horizontalLayout.setComponentAlignment(partyGiftsTable, Alignment.MIDDLE_CENTER);
            horizontalLayout.setComponentAlignment(userGiftsTable, Alignment.MIDDLE_CENTER);
            setCompositionRoot(horizontalLayout);
        }

        private void remove(Gift gift){
            manager.getUser().remove(gift);
            Dispatcher.getInstance().broadcast("GIFT_STATUS");
        }
        
        public void userGiftClick(ItemClickEvent event) {
            Gift gift = (Gift) event.getItemId();
            manager.getUser().buy(gift);
            Dispatcher.getInstance().broadcast("GIFT_STATUS");
        }
        
        public void partyGiftClick(ItemClickEvent event) {
            Gift gift = (Gift) event.getItemId();
            if (gift.getStatus() == Gift.Status.FREE){
                manager.getUser().add(gift);
                userGifts.addBean(gift);
                Dispatcher.getInstance().broadcast("GIFT_STATUS");
            }else if (gift.getStatus() == Gift.Status.HAS_BUYER && gift.getBuyer().equals(manager.getUser())){
                remove(gift);
            }
        }
        
        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            party = PartyService.getInstance().getById(Long.parseLong(event.getParameters()));
            reloadGifts();
        }
        
        void reloadGifts(){
            if (isAttached()){
                partyGifts.removeAllItems();
                partyGifts.addAll(party.getGifts());
                userGifts.removeAllItems();
                userGifts.addAll(manager.getUser().getGifts());
            }
        }
    }    
}
