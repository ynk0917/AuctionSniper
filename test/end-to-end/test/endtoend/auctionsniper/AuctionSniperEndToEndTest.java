package test.endtoend.auctionsniper;

import org.junit.After;
import org.junit.Test;

public class AuctionSniperEndToEndTest {

    private final FakeAuctionSerevr auction = new FakeAuctionSerevr("item-54321");
    private final FakeAuctionSerevr auction2 = new FakeAuctionSerevr("item-65432");
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
        application.hasShownSniperIsBidding(auction, 1000, 1098);
        
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPPER_XMPP_ID);
        
        auction.reportPrice(1098, 97, ApplicationRunner.SNIPPER_XMPP_ID);
        application.hasShownSniperIsWinning(auction, 1098);
        auction.announceColsed();
        application.showsSniperHasWonAuction(auction, 1098);
    }
    
    @Test
    public void sniperBidsForMultipleItems() throws Exception {
        auction.startSellingItem();
        auction2.startSellingItem();
        
        application.startBiddingIn(auction, auction2);
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPPER_XMPP_ID);
        auction2.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPPER_XMPP_ID);
        
        auction.reportPrice(1000, 98, "other bidder");
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPPER_XMPP_ID);
        
        auction2.reportPrice(500, 21, "other bidder");
        auction2.hasReceivedBid(521, ApplicationRunner.SNIPPER_XMPP_ID);
        
        auction.reportPrice(1098, 97, ApplicationRunner.SNIPPER_XMPP_ID);
        auction2.reportPrice(521, 22, ApplicationRunner.SNIPPER_XMPP_ID);
        
        application.hasShownSniperIsWinning(auction, 1098);
        application.hasShownSniperIsWinning(auction2, 521);
        
        auction.announceColsed();
        auction2.announceColsed();
        
        application.showsSniperHasWonAuction(auction, 1098);
        application.showsSniperHasWonAuction(auction2, 521);
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
