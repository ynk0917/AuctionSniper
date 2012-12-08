package auctionsniper;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


public class SniperState {
    public final String itemId;
    public final int lastPrice;
    public final int lastBid;
    
    public SniperState(String itemId, int lastPrice, int lastBid) {
        this.itemId = itemId;
        this.lastPrice = lastPrice;
        this.lastBid = lastBid;
    }
    
    @Override
    public boolean equals(Object obj) {
      return EqualsBuilder.reflectionEquals(this, obj);
    }
    @Override
    public int hashCode() {
      return HashCodeBuilder.reflectionHashCode(this);
    }
    @Override
    public String toString() {
      return ToStringBuilder.reflectionToString(this);
    }

}
