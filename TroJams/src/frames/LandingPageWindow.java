package frames;

import javax.swing.*;
import java.awt.*;

public class LandingPageWindow extends JFrame {

    private ImagePanel mainPanel;

    public LandingPageWindow() {
        super("TroJams");

        initializeComponents();
        createGUI();
        addListeners();
    }

    public void initializeComponents() {
        ImageIcon icon = new ImageIcon("background.png");
        mainPanel = new ImagePanel(icon);
    }

    public void createGUI() {
        add(mainPanel, BorderLayout.CENTER);
        setSize(1650,1080);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
    }

    public void addListeners() {

    }

    public class ImagePanel extends JPanel {
        ImageIcon icon;
        public ImagePanel(ImageIcon icon) {
            this.icon = icon;
        }
        public void setIcon(ImageIcon icon) {
            this.icon = icon;
        }
        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String [] args) {
        LandingPageWindow lpw = new LandingPageWindow();
        lpw.setVisible(true);
    }
}
