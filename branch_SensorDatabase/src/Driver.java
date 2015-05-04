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
	
	//Public constructor for the Driver
	public Driver(){
		//Make a new JFrame which Exits upon closing
		myFrame = new JFrame();
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Depending on whether or not we can connect to the database, name it
		if(Database.checkIfDatabaseConnect()){
			myFrame.setTitle("Database:Connected");
		}
		else{
			myFrame.setTitle("Database:Disconnected");
		}
		//Set the initial value of loggedIn to false
		loggedIn = false;
		//Create a new Login
		loginScreen = new Login();
		//cerate a new MenuBar
		myMenuBar = new JMenuBar();
		
		//Initialize the Login Menu
		initLoginMenu();
		//Initialize the Search Menu
		initSearchMenu();
		//Initialize the Admin Menu
		initAdminMenu();
		//Boot up the welcome screen
        welcomeScreen();
		
        //Set the frame's menu bar, set it to visible and focusable, and set it's size
        //and place it in the middle of the screen
		myFrame.setJMenuBar(myMenuBar);
		myFrame.setFocusable(true);
		myFrame.setVisible(true);
		myFrame.setSize(700, 700);
		centerScreen();

	}
	
	//Function that initializes the Admin Menu
	private void initAdminMenu(){
		//Create a new Menu
		JMenu adminMenu = new JMenu("User");
		
		//Make an addFile menu item
		JMenuItem addFile = new JMenuItem("Add data file...");
		//Add an actionListener to the menu item
		addFile.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent actionEvent) {
		    	//Get the current loggedIn status
		    	loggedIn = loginScreen.isLoggedIn();
		    	//If the user is logged in, pull up the file searcher
		    	if(loggedIn){
		    		new AdminAddFile();
		    	}
		    	//If the user is not logged in, tell them
		    	else{
		    		JOptionPane.showMessageDialog(null, "Must log in to load data file");
		    	}
		    }
		});
		//add the menu item to the menu
		adminMenu.add(addFile);
		
		//Menu item for changing passwords
		JMenuItem changePassword = new JMenuItem("Change password");
		//Add an actionlistener to the menu item
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
		    	//If the user is not logged in, tell them
		    	else{
		    		JOptionPane.showMessageDialog(null, "Must log in to change passwords");
		    	}
		    }
		});
		//add the menu item to the menu
		adminMenu.add(changePassword);
		
		//add the menu to the menu bar
		myMenuBar.add(adminMenu);
	}
	
	//Initialize the search menu
	private void initSearchMenu(){
		//Make a search menu 
		JMenu searchMenu = new JMenu("Search...");
		//Make a serial submenu
		JMenu serialSearchMenu = new JMenu("Serial by...");
		//Make a current data menu item 
		JMenuItem serialCurrentDataItem = new JMenuItem("Current Data");
		serialCurrentDataItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent actionEvent) {
			    initPanel.removeAll();
			    //If pressed, call a new instance of the appropriate class
			    myFrame.add(new DatabaseSearchBySerialCurrent());
			    myFrame.revalidate();
			    myFrame.repaint();
		    }
		});
		//add item to submenu
		serialSearchMenu.add(serialCurrentDataItem);
		
		//item for all data
		JMenuItem serialAllDataItem = new JMenuItem("All Data");
		serialAllDataItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent actionEvent) {
			    initPanel.removeAll();
			  //If pressed, call a new instance of the appropriate class
			    myFrame.add(new DatabaseSearchBySerialAll());
			    myFrame.revalidate();
			    myFrame.repaint();
		    }
		});
		//add to sub menu
		serialSearchMenu.add(serialAllDataItem);
		
		//item for time frame
		JMenuItem serialTimeframeItem = new JMenuItem("Timeframe");
		serialTimeframeItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent actionEvent) {
			    initPanel.removeAll();
			    //If pressed, call a new instance of the appropriate class
			    myFrame.add(new DatabaseSearchBySerialTime());
			    myFrame.revalidate();
			    myFrame.repaint();
		    }
		});
		//add to submenu
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
		instrumentTimeframeItem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent actionEvent) {
			    initPanel.removeAll();
			    myFrame.add(new DatabaseSearchByInstrumentTime());
			    myFrame.revalidate();
			    myFrame.repaint();
		    }
		});
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