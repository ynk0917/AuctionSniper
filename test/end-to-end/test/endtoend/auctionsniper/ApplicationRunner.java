package test.endtoend.auctionsniper;

import auctionsniper.Main;
import auctionsniper.MainWindow;
import auctionsniper.SniperState;
import auctionsniper.SnipersTableModel;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPPER_XMPP_ID = SNIPER_ID + "@" + FakeAuctionSerevr.XMPP_HOSTNAME + "/Auction";
    private AuctionSniperDriver driver;
    
    public void startBiddingIn(final FakeAuctionSerevr... auctions) {
        startSniper(auctions);
        for (FakeAuctionSerevr auction : auctions) {
            final String itemId = auction.getItemId();
            driver.startBiddingFor(itemId);
            driver.showsSniperStatus(itemId, 0, 0, SnipersTableModel.textFor(SniperState.JOINING));
        }
    }

    private void startSniper(final FakeAuctionSerevr... auctions) {
        Thread thread = new Thread("Test Appliciation") {
            @Override
            public void run() {
                try {
                    Main.main(arguments(auctions));
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
    }
    
    private static String[] arguments(FakeAuctionSerevr... auctions) {
        String[] arguments = new String[auctions.length + 3];
        arguments[0] = FakeAuctionSerevr.XMPP_HOSTNAME;
        arguments[1] = SNIPER_ID;
        arguments[2] = SNIPER_PASSWORD;
        for (int index = 0; index < auctions.length; ++index) {
            arguments[index + 3] = auctions[index].getItemId();
        }
        return arguments;
    }
    
    public void showsSniperHasLostAuction() {
        driver.showsSniperStatus(MainWindow.STATUS_LOST);
    }
    
    public void hasShownSniperIsBidding() {
        driver.showsSniperStatus(MainWindow.STATUS_BIDDING);
    }
    
    public void hasShownSniperIsBidding(FakeAuctionSerevr auction, int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, MainWindow.STATUS_BIDDING);
    }
    
    public void stop() {
        if (null != driver) {
            driver.dispose();
        }
    }

    public void hasShownSniperIsWinning(FakeAuctionSerevr auction, int winningBid) {
        driver.showsSniperStatus(auction.getItemId(), winningBid, winningBid, MainWindow.STATUS_WINNING);
    }

    public void showsSniperHasWonAuction(FakeAuctionSerevr auction, int lastPrice) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice, MainWindow.STATUS_WON);
    }
}
