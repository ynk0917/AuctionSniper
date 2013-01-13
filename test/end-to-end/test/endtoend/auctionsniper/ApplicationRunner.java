package test.endtoend.auctionsniper;

import auctionsniper.Main;
import auctionsniper.MainWindow;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPPER_XMPP_ID = SNIPER_ID + "@" + FakeAuctionSerevr.XMPP_HOSTNAME + "/Auction";
    private AuctionSniperDriver driver;
    private String itemId;
    
    public void startBiddingIn(final FakeAuctionSerevr auction) {
        itemId = auction.getItemId();
        Thread thread = new Thread("Test Appliciation") {
            @Override
            public void run() {
                try {
                    Main.main(FakeAuctionSerevr.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(1000);
        driver.hasTitle(MainWindow.APPLICATION_TITLE);
        driver.hasColumnTitles();
        driver.showsSniperStatus(MainWindow.STATUS_JOINING);
    }
    
    public void showsSniperHasLostAuction() {
        driver.showsSniperStatus(MainWindow.STATUS_LOST);
    }
    
    public void hasShownSniperIsBidding() {
        driver.showsSniperStatus(MainWindow.STATUS_BIDDING);
    }
    
    public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
        driver.showsSniperStatus(itemId, lastPrice, lastBid, MainWindow.STATUS_BIDDING);
    }
    
    public void stop() {
        if (null != driver) {
            driver.dispose();
        }
    }

    public void hasShownSniperIsWinning(int winningBid) {
        driver.showsSniperStatus(itemId, winningBid, winningBid, MainWindow.STATUS_WINNING);
    }

    public void showsSniperHasWonAuction(int lastPrice) {
        driver.showsSniperStatus(itemId, lastPrice, lastPrice, MainWindow.STATUS_WON);
    }
}
