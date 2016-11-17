package frames;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import logic.User;
import resources.AppearanceConstants;
import resources.AppearanceSettings;

/*
 * CREATE A PARTY. SHOULD BE A PANEL. CARD LAYOUT WITH SELECTIONWINDOW AS MAIN
 */
public class CreatePartyWindow extends JFrame {
	
	private JPanel cpwMainPanel, cpwTopPanel, cpwBottomPanel;
	private JTextField cpwPartyNameTextField;
	private JTextField cpwPasswordTextField;
	private JRadioButton cpwPublicRadioButton;
	private JRadioButton cpwPrivateRadioButton;
	private JButton cpwCreateButton;
	
	public CreatePartyWindow() {
		super("TroJams");
		initializeComponents();
		createGUI();
		addActionListeners();
		
	}
	
	public void initializeComponents() {
		cpwMainPanel = new JPanel();
		cpwTopPanel = new JPanel();
		cpwBottomPanel = new JPanel();
		cpwPartyNameTextField = new JTextField();
		cpwPasswordTextField = new JTextField();
		cpwPublicRadioButton = new JRadioButton("Public");
		cpwPrivateRadioButton = new JRadioButton("Private");
		cpwCreateButton = new JButton("Create Party");
		
	}
	
	public void createGUI() {
		this.setSize(AppearanceConstants.GUI_WIDTH, AppearanceConstants.GUI_HEIGHT);
		createCPWMenu();
	}
	
	public void addActionListeners() {
		
	}
	
	public void createCPWMenu() {
		cpwMainPanel.setLayout(new BoxLayout(cpwMainPanel, BoxLayout.Y_AXIS));
		
		AppearanceSettings.setSize(300, 75, cpwPartyNameTextField, cpwPasswordTextField);
		cpwTopPanel.add(cpwPartyNameTextField);
		cpwMainPanel.add(cpwTopPanel);
		
		// Makes it so you can only select one button
		ButtonGroup bg = new ButtonGroup();
		bg.add(cpwPublicRadioButton);
		bg.add(cpwPrivateRadioButton);
		
		
		cpwMainPanel.add(cpwPublicRadioButton);
		cpwMainPanel.add(cpwPrivateRadioButton);
		
		cpwBottomPanel.add(cpwPasswordTextField);
		cpwMainPanel.add(cpwBottomPanel);
		
		add(cpwMainPanel);
		
	}
	
	public static void main(String [] args) {
		new CreatePartyWindow().setVisible(true);
	}
	
}
