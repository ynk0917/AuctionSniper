package auctionsniper;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class MainWindow extends JFrame {

    public static final String SNIPER_STATUS_NAME = "sniper status";
    public static final String STATUS_JOINING = "Joining";
    public static final String STATUS_LOST = "Lost";
    
    private final JLabel sniperStatus = createLabel(MainWindow.STATUS_JOINING);

    private static JLabel createLabel(String initialText) {
        JLabel result = new JLabel(initialText);
        result.setName(SNIPER_STATUS_NAME);
        result.setBorder(new LineBorder(Color.BLACK));
        return result;
    }
    
    public MainWindow() {
        super("Auction Sniper");
        setName(Main.MAIN_WINDOW_NAME);
        add(sniperStatus);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    public void showStatus(String status) {
        sniperStatus.setText(status);
    }
}
