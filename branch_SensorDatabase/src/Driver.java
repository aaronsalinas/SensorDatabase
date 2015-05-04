import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/******************************************************************************
 * Filename: Driver.java
 * Author: Dylan Clohessy (Dylan_Clohessy@baylor.edu)
 * Description: The driver file for the project. 
 * Created: 4/28/2015
 * Modified:4/30/2015
******************************************************************************/
public class Driver{

	public static void main(String[] args){
		new Driver();
	}
	
	public Driver(){
		myFrame = new JFrame();
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if(Database.checkIfDatabaseConnect()){
			myFrame.setTitle("Database:Connected");
		}
		else{
			myFrame.setTitle("Database:Disconnected");
		}
		loggedIn = false;
		loginScreen = new Login();
		
		myMenuBar = new JMenuBar();
		
		initLoginMenu();
		initSearchMenu();
		initAdminMenu();
        welcomeScreen();
		
		myFrame.setJMenuBar(myMenuBar);
		myFrame.setFocusable(true);
		myFrame.setVisible(true);
		myFrame.setSize(700, 700);
		centerScreen();

	}
	
	private void initAdminMenu(){
		JMenu adminMenu = new JMenu("User");
		
		JMenuItem addFile = new JMenuItem("Add data file...");
		addFile.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent actionEvent) {
		    	//Get the current loggedIn status
		    	loggedIn = loginScreen.isLoggedIn();
		    	//If the user is not logged in, allow them to log in
		    	if(loggedIn){
		    		new AdminAddFile();
			    	//Re-validate and repaint the JFrame
			    	myFrame.revalidate();
			    	myFrame.repaint();
		    	}
		    	//If the user is already logged in, tell them
		    	else{
		    		JOptionPane.showMessageDialog(null, "Must log in to load data file");
		    	}
		    }
		});
		adminMenu.add(addFile);
		
		JMenuItem changePassword = new JMenuItem("Change password");
		changePassword.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent actionEvent) {
		    	//Get the current loggedIn status
		    	loggedIn = loginScreen.isLoggedIn();
		    	//If the user is logged in, allow them to change passwords
		    	if(loggedIn){
		    		initPanel.removeAll();
		    		userName = loginScreen.getUser();
		    		myFrame.add(new AdminChangePassword(userName));
			    	//Re-validate and repaint the JFrame
			    	myFrame.revalidate();
			    	myFrame.repaint();
		    	}
		    	//If the user is not already logged in, tell them
		    	else{
		    		JOptionPane.showMessageDialog(null, "Must log in to change passwords");
		    	}
		    }
		});
		adminMenu.add(changePassword);
		
		myMenuBar.add(adminMenu);
	}
	
	private void initSearchMenu(){
		
		JMenu searchMenu = new JMenu("Search...");
		
		JMenu serialSearchMenu = new JMenu("Serial by...");
		
		JMenuItem serialCurrentDataItem = new JMenuItem("Current Data");
		serialCurrentDataItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent actionEvent) {
			    initPanel.removeAll();
			    myFrame.add(new DatabaseSearchBySerialCurrent());
			    myFrame.revalidate();
			    myFrame.repaint();
		    }
		});
		serialSearchMenu.add(serialCurrentDataItem);
		
		JMenuItem serialAllDataItem = new JMenuItem("All Data");
		serialAllDataItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent actionEvent) {
			    initPanel.removeAll();
			    myFrame.add(new DatabaseSearchBySerialAll());
			    myFrame.revalidate();
			    myFrame.repaint();
		    }
		});
		serialSearchMenu.add(serialAllDataItem);
		
		JMenuItem serialTimeframeItem = new JMenuItem("Timeframe");
		serialSearchMenu.add(serialTimeframeItem);
		
		JMenu instrumentSearchMenu = new JMenu("Instrument by...");
		
		JMenuItem instrumentCurrentDataItem = new JMenuItem("Current Data");
		instrumentCurrentDataItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent actionEvent) {
			    initPanel.removeAll();
			    myFrame.add(new DatabaseSearchByInstrumentCurrent());
			    myFrame.revalidate();
			    myFrame.repaint();
		    }
		});
		instrumentSearchMenu.add(instrumentCurrentDataItem);
		
		JMenuItem instrumentAllDataItem = new JMenuItem("All Data");
		instrumentAllDataItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent actionEvent) {
			    initPanel.removeAll();
			    myFrame.add(new DatabaseSearchByInstrumentAll());
			    myFrame.revalidate();
			    myFrame.repaint();
		    }
		});
		instrumentSearchMenu.add(instrumentAllDataItem);
		
		JMenuItem instrumentTimeframeItem = new JMenuItem("Timeframe");
		instrumentSearchMenu.add(instrumentTimeframeItem);
		
		
		searchMenu.add(serialSearchMenu);
		searchMenu.add(instrumentSearchMenu);
		myMenuBar.add(searchMenu);
	}
	
	private void initLoginMenu(){
		//Create a new JMenu called "Login" and add it to the JMenuBar
		JMenu loginMenu = new JMenu("Login");
		myMenuBar.add(loginMenu);
		
		//Make a choice called "User Login" and set its properties
		JMenuItem userLoginItem = new JMenuItem("User Login");
		userLoginItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent actionEvent) {
		    	//Get the current loggedIn status
		    	loggedIn = loginScreen.isLoggedIn();
		    	//If the user is not logged in, allow them to log in
		    	if(!loggedIn){
		    		//Remove everything from the initial panel
			    	initPanel.removeAll();
			    	//Add the loginScreen to the JFrame
			    	myFrame.add(loginScreen);
			    	//Re-validate and repaint the JFrame
			    	myFrame.revalidate();
			    	myFrame.repaint();
		    	}
		    	//If the user is already logged in, tell them
		    	else{
		    		JOptionPane.showMessageDialog(null, "Already logged in");
		    	}
		    }
		});
		//Add the login item to the menu
		loginMenu.add(userLoginItem);
		
		//Make a register menu item
		JMenuItem registerLoginItem = new JMenuItem("Register");
		registerLoginItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent actionEvent) {
		    	//Get the current logged in status
		    	loggedIn = loginScreen.isLoggedIn();
		    	//If no one is logged in, proceed
		    	if(!loggedIn){
		    		//Remove everything from the initial panel
			    	initPanel.removeAll();
			    	//Add a new AccountCreation item to the Frame
			    	myFrame.add(new AccountCreation());
			    	//Re-validate and repaint the screen
			    	myFrame.revalidate();
			    	myFrame.repaint();
		    	}
		    	//If the user is signed in
		    	else{
		    		//Tell them they are
		    		JOptionPane.showMessageDialog(null, "Sign out to create new Account");
		    	}

		    }
		});
		//Add the register item to the menu
		loginMenu.add(registerLoginItem);
		
		//Loggout opotion
		JMenuItem loggoutLoginItem = new JMenuItem("Logout");
		loggoutLoginItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent actionEvent) {
		    	//Get the login status
		    	loggedIn = loginScreen.isLoggedIn();
		    	//If we are logged in, logout
		    	if(loggedIn){
		    		//Remove everything from the initial panel
		    		initPanel.removeAll();
		    		//Invoke the welcome screen
		    		welcomeScreen();
		    		//Set loggedIn to false
		    		loggedIn = false;
		    		//Create a new Login()
		    		loginScreen = new Login();
		    		//Notify the user that they have sigened out
		    		JOptionPane.showMessageDialog(null, "Signed out");
		    		//Re-validate and repaint the frame
			    	myFrame.revalidate();
			    	myFrame.repaint();
		    	}
		    	//If we are logged out
		    	else{
		    		//Tell the user
		    		JOptionPane.showMessageDialog(null, "Not signed in");
		    	}
		    }
		});
		//add the logout button to the menu
		loginMenu.add(loggoutLoginItem);
		
	}
	
	private void centerScreen(){
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		myFrame.setLocation(dim.width/2-myFrame.getSize().width/2, dim.height/2-myFrame.getSize().height/2);
		
	}
	
	private void welcomeScreen(){
		/*
		JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Color.BLACK);
        content.setBorder(new EmptyBorder(0,0,0,0));
        myFrame.setContentPane(content);
        */
        
		initPanel = new JPanel(new GridBagLayout());

		JLabel welcomeLabel = new JLabel("Welcome to the Dr. Kheul's database");
		welcomeLabel.setFont(new Font("Verdana",1,20) );
		initPanel.add(welcomeLabel);
		
		myFrame.setContentPane(initPanel);
	}
	
	private Login loginScreen;
	private JPanel initPanel;
	private JFrame myFrame;
	private JMenuBar myMenuBar;
	private boolean loggedIn;
	private String userName;
}