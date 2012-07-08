package test.endtoend.auctionsniper;

import auctionsniper.Main;
import auctionsniper.MainWindow;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SINPER_PASSWORD = "sniper";
    public static final String SNIPPER_XMPP_ID = SNIPER_ID + "@" + FakeAuctionSerevr.XMPP_HOSTNAME + "/Auction";
    private AuctionSniperDriver driver;
    
    public void startBiddingIn(final FakeAuctionSerevr auction) {
        Thread thread = new Thread("Test Appliciation") {
            @Override
            public void run() {
                try {
                    Main.main(FakeAuctionSerevr.XMPP_HOSTNAME, SNIPER_ID, SINPER_PASSWORD, auction.getItemId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(1000);
        driver.showsSniperStatus(MainWindow.STATUS_JOINING);
    }
    
    public void showsSniperHasLostAuction() {
        driver.showsSniperStatus(MainWindow.STATUS_LOST);
    }
    
    public void hasShownSniperIsBidding() {
        driver.showsSniperStatus(MainWindow.STATUS_BIDDING);
    }
    
    public void stop() {
        if (null != driver) {
            driver.dispose();
        }
    }
}
