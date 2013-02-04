package auctionsniper;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class Main {

    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;
    
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
    public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
    
    private MainWindow ui;
    private final SnipersTableModel snipers = new SnipersTableModel();
    
    private List<Auction> notToBeGCd = new ArrayList<Auction>();
    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";
    public static void main(String... args) throws Exception {
         Main main = new Main();
         XMPPConnection connection = connection( args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
         main.disconnectWhenUICloses(connection);
         main.addUserRequestListenerFor(connection);
    }
    
    private void addUserRequestListenerFor(final XMPPConnection connection) {
        ui.addUserRequestListener(new UserRequestListener() {
            
            @Override
            public void joinAuction(String itemId) {
                snipers.addSniper(SniperSnapshot.joining(itemId));
                Announcer<AuctionEventListener> auctionEventListeners = Announcer.to(AuctionEventListener.class);
                Auction auction = new XMPPAuction(connection, itemId);
                notToBeGCd.add(auction);
                auction.addAuctionEventListener(new AuctionSniper(itemId, auction, new SwingThreadSniperListener(snipers)));
                auction.join();
            }
        });
    }
    
    private void disconnectWhenUICloses(final XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
    }
    
    private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        
        return connection;
    }
    
    
    public Main() throws Exception {
        startUserInterface();
    }
    
    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ui = new MainWindow(snipers);
            }
        });
    }
    
    public class SwingThreadSniperListener implements SniperListener {
        SnipersTableModel snipers;
        public SwingThreadSniperListener(SnipersTableModel snipers) {
            this.snipers = snipers;
        }
        
        @Override
        public void sniperStateChanged(final SniperSnapshot snapshot) {
            SwingUtilities.invokeLater(new Runnable () {
                @Override
                public void run() {
                    snipers.sniperStatusChanged(snapshot);
                }
                
            });
        }
    }
}
