package test.integration.auctionsniper.ui;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.objogate.wl.swing.probe.ValueMatcherProbe;

import test.endtoend.auctionsniper.AuctionSniperDriver;
import auctionsniper.MainWindow;
import auctionsniper.SnipersTableModel;
import auctionsniper.UserRequestListener;

public class MainWindowTest {
    private final SnipersTableModel tableModel = new SnipersTableModel();
    private final MainWindow mainwindow = new MainWindow(tableModel);
    private final AuctionSniperDriver driver = new AuctionSniperDriver(100);

    @Test
    public void makeUserRequestWhenJoinButtonClicked() {
        final ValueMatcherProbe<String> buttonProbe = new ValueMatcherProbe<String>(Matchers.equalTo("an item-id"), "join request");

        mainwindow.addUserRequestListener(
                new UserRequestListener() {
                    @Override
                    public void joinAuction(String itemId) {
                        buttonProbe.setReceivedValue(itemId);
                    }
                });
        driver.startBiddingFor("an item id");
        driver.check(buttonProbe);
    }
}
