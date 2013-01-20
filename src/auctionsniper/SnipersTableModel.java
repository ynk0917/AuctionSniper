package auctionsniper;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.objogate.exception.Defect;


public class SnipersTableModel extends AbstractTableModel {
    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);
    private static String[] STATUS_TEXT = { "Joining", "Bidding", "Winning", "Lost", "Won" };
    private List<SniperSnapshot>snapshots = new ArrayList<SniperSnapshot>();

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public int getRowCount() {
        return snapshots.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshots.get(rowIndex));
    }
    
    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }
    
    public void sniperStatusChanged(SniperSnapshot newSnapshot) {
        int row = rowMataching(newSnapshot);
        snapshots.set(row, newSnapshot);
        fireTableRowsUpdated(row, row);
    }
    
    private int rowMataching(SniperSnapshot snapshot) {
        for (int i = 0; i < snapshots.size(); ++i) {
            if (snapshot.isForSameItemAs(snapshots.get(i))) {
                return i;
            }
        }
        throw new Defect("Cannot find match for " + snapshot);
    }

    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }

    public void addSniper(SniperSnapshot joining) {
        snapshots.add(joining);
        fireTableRowsInserted(0, 0);
    }
}
