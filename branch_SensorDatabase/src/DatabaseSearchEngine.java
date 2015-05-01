/******************************************************************************
 * Filename: DataBaseSearchEngine.java
 * Author: Dylan Clohessy (Dylan_Clohessy@baylor.edu)
 * Description: A class that allows users to search the database if logged into
 *              the system. Provides a graphical framework for this task.
 * Created: 4/30/2015
 * Modified:4/30/2015
******************************************************************************/
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class DatabaseSearchEngine extends JFrame{
	
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 * Description: Custom constructor for the DatabaseSearchEngine class
	 * Return Type: none
	 * Pre: None
	 * Post: Instantiates a new instance of the DatabaseSearchEngine class.
	 *************************************************************************/
	public DatabaseSearchEngine(){
		isAdmin = false;
		init();
	}
	
	public DatabaseSearchEngine(AdminDatabaseBrowser attachmentBrowser){
		adminBrowser = attachmentBrowser;
		isAdmin = true;
		init();
	}
	

	/**************************************************************************
	 * Description: Builds the DatabaseSearchEngine window
	 * Return Type: void
	 * Pre: void
	 * Post: Method that initializes the JFrame and JPanel. It populates the 
	 * 		 JPanel with desired labels, text boxes, buttons, and their 
	 * 		 respective functionality, sizes, and locations. It then adds the
	 *       JPanel to the JFrame and sets the desired JFrame attributes.
	 *************************************************************************/
	private void init(){

		//Instantiate the new frame
		myFrame = new JFrame();
		//Set default close operation
		myFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//Set the title of the window
		myFrame.setTitle("Database Search Engine");
		
		//Instantiate the new panel
		panel = new JPanel(null);
		//Set the size of the panel
		panel.setSize(365, 250);
		
		selectedInstrument = new String();
		selectedSerial = new String();
		
		serialList = new ArrayList<String>();
		initInstrumentDropDown();
		
		searchLabel = new JLabel("Select Instrument:");
		searchLabel.setBounds(30, 15, 125, 25);
		panel.add(searchLabel);
		
		instrumentLabel = new JLabel();
		instrumentLabel.setBounds(220, 15, 125, 25);
		
		addSearchButton();
		addResetButton();
		if(isAdmin){
			addAdminButton();
		}
		
		//Add the frame to the panel
		myFrame.add(panel);
		//Set the frame to focusable, visible, and set the size
		myFrame.setFocusable(true);
		myFrame.setVisible(true);
		myFrame.setSize(365, 250);
	}
	
	/**************************************************************************
	 * Description: Builds the drop-down selection box of instruments
	 * Return Type: void
	 * Pre: None
	 * Post: Creates a drop-down box containing the names of all of the
	 *       instruments for the user to select from.
	 *************************************************************************/
	private void initInstrumentDropDown(){
		//Initialize the instrumentList data member to store the names of all
		//of the instruments from the database
		instrumentList = new ArrayList<String>();
		
		//BULLSHIT
		instrumentList.add("Instrument 1");
		instrumentList.add("Instrument 2");
		instrumentList.add("Instrument 3");
		instrumentList.add("Instrument 4");
		
		//Initialize the "instrumentDropDown" JComboBox  
		instrumentDropDown = new JComboBox<String>();
		//Set the location and size
		instrumentDropDown.setBounds(30, 40, 120, 30);
		
		//Populate the JComboBox with all members of the "instrumentList"
		for(String item : instrumentList){
			instrumentDropDown.addItem(item);
		}
		
		//Add the "instrumentDropDown" to the panel
		panel.add(instrumentDropDown);
		
		//Add an ActionListener to the "instrumentDropDown"
		instrumentDropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//Set "selectedInstrument" to the item selected by the user
				selectedInstrument = (String) instrumentDropDown.getSelectedItem();
				//Add the reset button to the panel
				panel.add(resetButton);
				//Initialize the "serialDropDown" JComboBox
				initSerialDropDown();
			}
		});
		
	}
	
	/**************************************************************************
	 * Description: Builds the drop-down selection box of serial numbers
	 * Return Type: void
	 * Pre: None
	 * Post: Creates a drop-down box containing the all of the serial numbers
	 *       of the specified instrument.
	 *************************************************************************/
	private void initSerialDropDown(){
		//Set the "instrumentLabel" to the instrument that was selected from
		//the previous drop-down menu and add the JLabel to the JPanel
		instrumentLabel.setText(selectedInstrument);
		panel.add(instrumentLabel);
		
		//Clear the "serialList"
		serialList.clear();
		
		//BULLSHIT
		serialList.add("Hello");
		serialList.add("There");
		serialList.add("Mason");
		serialList.add("McGough");
		
		//Initialize the "serialDropDown" JComboBox and set the position and
		//dimensions
		serialDropDown = new JComboBox<String>();
		serialDropDown.setBounds(200, 40, 120, 30);
		
		//Iterate through the "serialList", adding all items inside
		for(String item : serialList){
			serialDropDown.addItem(item);
		}
		
		//Add the "serialDropDown" to the JPanel
		panel.add(serialDropDown);
		
		//Revalidate and repaint the JPanel 
		panel.revalidate();
		panel.repaint();
		
		//Add an ActionListener to the "serialDropDown"
		serialDropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//sets "selectedSerial" to the item selected by the user
				selectedSerial = (String) serialDropDown.getSelectedItem();
			}
		});
		
	}
	
	/**************************************************************************
	 * Description: Adds the "searchButton" button to the JPanel
	 * Return Type: void
	 * Pre: None
	 * Post: Instantiates the private data member searchButton, sets the size
	 *       and location, then adds an ActionListener to it indicating to
	 *       search for the selected instrument serial number.
	 *************************************************************************/
	private void addSearchButton(){
		//Instantiate a the private data member "Search", and set the 
		//position and size
		searchButton = new JButton("Search");
		searchButton.setBounds(35, 100, 100, 25);
				
		//Add an ActionListener to the "Search" button
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//If the button is selected, search for the serial number of
				//the selected instrument
				if(e.getActionCommand().equals("Search")){
					if(isValidQuery()){
						System.out.print(selectedInstrument + " " + selectedSerial);
					}
				}
			}
		});
		//Add the JButton to the panel
		panel.add(searchButton);
	}
	
	/**************************************************************************
	 * Description: Adds the "resetButton" button to the JPanel
	 * Return Type: void
	 * Pre: None
	 * Post: Instantiates the private data member resetButton, sets the size
	 *       and location, then adds an ActionListener to it indicating to
	 *       remove the "serialDropDown" and clear the search elements.
	 *************************************************************************/
	private void addResetButton(){
		//Instantiate a the private data member "Reset", and set the 
		//position and size
		resetButton = new JButton("Reset");
		resetButton.setBounds(210, 100, 100, 25);
				
		//Add an ActionListener to the "Reset" button
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//If the button is selected, remove indicated elements from the
				//JPanel then revalidate and repaint it.
				if(e.getActionCommand().equals("Reset")){
					selectedInstrument = selectedSerial = "";
					panel.remove(resetButton);
					panel.remove(instrumentLabel);
					panel.remove(serialDropDown);
					panel.revalidate();
					panel.repaint();
				}
			}
		});
	}
	
	
	/**************************************************************************
	 * Description: Adds the "adminButton" button to the JPanel
	 * Return Type: void
	 * Pre: None
	 * Post: Instantiates the private data member adminButton, sets the size
	 *       and location, then adds an ActionListener to it indicating to
	 *       hide the search engine and show the adminBrowser
	 *************************************************************************/
	private void addAdminButton(){
		//Instantiate a the private data member "addAdmin", and set the 
		//position and size
		adminButton = new JButton("Admin Menu");
		adminButton.setBounds(100, 175, 150, 25);
				
		//Add an ActionListener to the "addAdmin" button
		adminButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//If the button is selected, hide this screen and display
				//the adminBrowser
				if(e.getActionCommand().equals("Admin Menu")){
					hideScreen();
					adminBrowser.showScreen();
				}
			}
		});
		//Add this Button to the panel
		panel.add(adminButton);
	}
	
	/**************************************************************************
	 * Description: Determines if the desired query is valid
	 * Return Type: boolean
	 * Pre: None
	 * Post: Returns true if both "selectedInstrument" and "selectedSerial"
	 *       are populated, false otherwise.
	 *************************************************************************/
	private boolean isValidQuery(){
		boolean validQuery = true;
		//If the "selectedInstrument" is empty, return false and tell the user
		if(selectedInstrument.isEmpty()){
			JOptionPane.showMessageDialog(null, "Select an Instrument");
			validQuery = false;
		}
		//If the "selectedSerial" is empty, return false and inform the user
		else if(selectedSerial.isEmpty()){
			JOptionPane.showMessageDialog(null, "Select a Serial Number");
			validQuery = false;
		}
		
		return validQuery;
	}
	
	/**************************************************************************
	 * Description: Hides this window
	 * Return Type: void
	 * Pre: None
	 * Post: Hides this window
	 *************************************************************************/
	public void hideScreen(){
		myFrame.setVisible(false);
	}
	
	/**************************************************************************
	 * Description: Displays this window
	 * Return Type: void
	 * Pre: None
	 * Post: Displays this window
	 *************************************************************************/
	public void showScreen(){
		myFrame.setVisible(true);
	}
	
	//Private data members of the DatabaseSearchEngine class
	
	//Strings to hold the value of selected search objects
	private String selectedSerial;
	private String selectedInstrument;
		
	//JComboBoxes to limit the user to valid searches
	private JComboBox<String> instrumentDropDown;
	private JComboBox<String> serialDropDown;
	
	//JFrame and JPanel on which we can display the information
	private JFrame myFrame;
	private JPanel panel;
	
	//ArrayLists of Strings to hold the list of instruments and serial numbers
	//for the JComboBoxes
	ArrayList<String> instrumentList;
	ArrayList<String> serialList;
	
	//JLabels to label objects in the GUI
	private JLabel searchLabel;
	private JLabel instrumentLabel;
	
	//JButtons to allow user interaction
	private JButton resetButton;
	private JButton searchButton;
	private JButton adminButton;
	
	private AdminDatabaseBrowser adminBrowser;
	private boolean isAdmin;
}