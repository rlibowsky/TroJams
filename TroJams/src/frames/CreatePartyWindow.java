package frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import resources.AppearanceSettings;

/*
 * CREATE A PARTY. SHOULD BE A PANEL. CARD LAYOUT WITH SELECTIONWINDOW AS MAIN
 */
public class CreatePartyWindow extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel cpwMainPanel, cpwTopPanel, cpwBottomPanel, cpwRadioButtonPanel, cpwButtonPanel;
	private JLabel dummyLabel1, dummyLabel2, dummyLabel3, dummyLabel4, dummyLabel5, dummyLabel6;
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
		//dummyPanel = new JPanel();
		dummyLabel1 = new JLabel();
		dummyLabel2 = new JLabel();
		dummyLabel3 = new JLabel();
		dummyLabel4 = new JLabel();
		dummyLabel5 = new JLabel();
		dummyLabel6 = new JLabel();
		cpwRadioButtonPanel = new JPanel();
		cpwButtonPanel = new JPanel();
		cpwPartyNameTextField = new JTextField();
		cpwPasswordTextField = new JTextField();
		cpwPublicRadioButton = new JRadioButton("Public");
		cpwPrivateRadioButton = new JRadioButton("Private");
		cpwCreateButton = new JButton("Create Party");
		
	}
	
	public void createGUI() {
		this.setSize(new Dimension(500,800));
		createCPWMenu();
	}
	
	public void addActionListeners() {
		
		cpwPrivateRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cpwPasswordTextField.setVisible(true);
				
			}
			
		});
		
		cpwPublicRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cpwPasswordTextField.setVisible(false);
				
			}
			
		});
	}
	
	public void createCPWMenu() {
		cpwMainPanel.setLayout(new BoxLayout(cpwMainPanel, BoxLayout.Y_AXIS));
		
		AppearanceSettings.setSize(300, 50, cpwPartyNameTextField, cpwPasswordTextField, dummyLabel1, dummyLabel2, dummyLabel3, dummyLabel4, dummyLabel5, dummyLabel6);
		
		//cpwTopPanel.setLayout(new BorderLayout());
		
		//cpwMainPanel.add(dummyPanel);
		cpwTopPanel.add(dummyLabel1);
		cpwTopPanel.add(dummyLabel2);
		cpwTopPanel.add(dummyLabel3);
		cpwTopPanel.add(dummyLabel4);
		cpwTopPanel.add(dummyLabel5);
		//setInvisible(false, dummyLabel1, dummyLabel2, dummyLabel3, dummyLabel4, dummyLabel5);
		//cpwTopPanel.add(dummyLabel6);
		
		//TODO Set FocusListener for textfields
		
		cpwTopPanel.add(cpwPartyNameTextField);
		cpwMainPanel.add(cpwTopPanel);
		
		// Makes it so you can only select one button
		ButtonGroup bg = new ButtonGroup();
		bg.add(cpwPublicRadioButton);
		bg.add(cpwPrivateRadioButton);
		cpwPrivateRadioButton.setSelected(true);
		

		cpwRadioButtonPanel.setLayout(new BoxLayout(cpwRadioButtonPanel, BoxLayout.X_AXIS));
		cpwRadioButtonPanel.add(cpwPublicRadioButton);
		cpwRadioButtonPanel.add(cpwPrivateRadioButton);
		cpwMainPanel.add(cpwRadioButtonPanel);
		
		//cpwBottomPanel.setLayout(new BorderLayout());
		cpwBottomPanel.add(cpwPasswordTextField);
		cpwBottomPanel.add(cpwCreateButton);
		cpwMainPanel.add(cpwBottomPanel);

		//cpwButtonPanel.add(cpwCreateButton);		
		//cpwMainPanel.add(cpwButtonPanel);
		
		cpwMainPanel.setSize(new Dimension(500,800));
		cpwTopPanel.setBackground(Color.black);
		cpwMainPanel.setBackground(Color.black);
		cpwRadioButtonPanel.setBackground(Color.black);
		cpwBottomPanel.setBackground(Color.black);
		cpwPrivateRadioButton.setForeground(Color.white);
		cpwPublicRadioButton.setForeground(Color.white);
		
		add(cpwMainPanel);
		
	}
	
	public static void main(String [] args) {
		new CreatePartyWindow().setVisible(true);
	}
	
}
