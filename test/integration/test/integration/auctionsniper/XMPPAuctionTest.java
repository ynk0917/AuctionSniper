package test.integration.auctionsniper;

import java.util.concurrent.CountDownLatch;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import test.endtoend.auctionsniper.ApplicationRunner;
import test.endtoend.auctionsniper.FakeAuctionSerevr;
import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.xmp.XMPPAuction;

public class XMPPAuctionTest {
    private final FakeAuctionSerevr auctionServer = new FakeAuctionSerevr("item-54321");  
    private XMPPConnection connection;

    @Before
    public void connect() throws XMPPException {
        auctionServer.startSellingItem();
        connection = new XMPPConnection(FakeAuctionSerevr.XMPP_HOSTNAME);
        try {
            connection.connect(); 
            connection.login(ApplicationRunner.SNIPER_ID, ApplicationRunner.SNIPER_PASSWORD, FakeAuctionSerevr.AUCTION_RESOURCE);
        } catch (XMPPException xmppe) {
            Assert.fail();
        }
    }
    @Test
    public void receivesEventsFromAuctionServerAfterJoining() throws Exception {
        CountDownLatch auctionWasClosed = new CountDownLatch(1);

        Auction auction = new XMPPAuction(connection, auctionServer.getItemId());
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));

        auction.join();
        auctionServer.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
        auctionServer.announceColsed();

    }

    private AuctionEventListener auctionClosedListener(final CountDownLatch auctionWasClosed) {
        return new AuctionEventListener() {

            @Override
            public void currentPrice(int price, int incerment, PriceSource priceSource) {
            }

            @Override
            public void auctionClosed() {
                auctionWasClosed.countDown();
            }
        };
    }
}
