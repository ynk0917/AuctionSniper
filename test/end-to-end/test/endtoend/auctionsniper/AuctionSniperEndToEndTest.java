package test.endtoend.auctionsniper;

import org.junit.After;
import org.junit.Test;

public class AuctionSniperEndToEndTest {

    private final FakeAuctionSerevr auction = new FakeAuctionSerevr("item-54321");
    private final ApplicationRunner application = new ApplicationRunner();
    
    @Test
    public void sniperJoinAuctionUntilAuctionCloses() throws Exception {
        auction.startSellingItem();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper();
        auction.announceColsed();
        application.showsSniperHasLostAuction();
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
