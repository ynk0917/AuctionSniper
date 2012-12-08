package test.endtoend.auctionsniper;

import org.junit.After;
import org.junit.Test;

public class AuctionSniperEndToEndTest {

    public static final String ITEM_ID = "item-54321";
    private final FakeAuctionSerevr auction = new FakeAuctionSerevr(ITEM_ID);
    private final ApplicationRunner application = new ApplicationRunner();
    
    @Test
    public void sniperJoinAuctionUntilAuctionCloses() throws Exception {
        auction.startSellingItem();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPPER_XMPP_ID);
        auction.announceColsed();
        application.showsSniperHasLostAuction();
    }
    
    @Test
    public void sniperMakesAHigherBidButLoses() throws Exception {
        auction.startSellingItem();
        
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPPER_XMPP_ID);
        
        auction.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding();
        
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPPER_XMPP_ID);
        
        auction.announceColsed();
        application.showsSniperHasLostAuction();
    }
    
    @Test
    public void sniperWinsAnAuctionByBiddingHigher() throws Exception {
        auction.startSellingItem();
        
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPPER_XMPP_ID);
        
        auction.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding(1000, 1098);
        
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPPER_XMPP_ID);
        
        auction.reportPrice(1098, 97, ApplicationRunner.SNIPPER_XMPP_ID);
        application.hasShownSniperIsWinning(1098);
        auction.announceColsed();
        application.showsSniperHasWonAuction(1098);
    }
    
    @After
    public void stopAuction() {
        auction.stop();
    }
    
    @After
    public void stopApplication() {
        application.stop();
    }
}
