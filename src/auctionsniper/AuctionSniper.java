package auctionsniper;

public class AuctionSniper implements AuctionEventListener {
    private final SniperListener sniperListener;

    public AuctionSniper(SniperListener sniperListener) {
        this.sniperListener = sniperListener;
    }

    public void auctionCloesd() {
        sniperListener.sniperLost();
    }

    @Override
    public void auctionClosed() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void currentPrice(int price, int incerment) {
        // TODO Auto-generated method stub
        
    }

}
