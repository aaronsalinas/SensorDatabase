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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;


public class DatabaseSearchEngine extends JFrame{
	
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 * Description: Default constructor for the DatabaseSearchEngine class
	 * Return Type: none
	 * Pre: None
	 * Post: Instantiates a new instance of the DatabaseSearchEngine class
	 *       where the user is not an administrator.
	 *************************************************************************/
	public DatabaseSearchEngine(){
		isAdmin = false;
		init();
	}
	
	
	/**************************************************************************
	 * Description: Custom constructor for the DatabaseSearchEngine class
	 * Return Type: none
	 * Pre: None
	 * Post: Instantiates a new instance of the DatabaseSearchEngine class
	 * 		 indicating that the user is an administrator and attaching the
	 * 		 administrator browser to the search engine
	 *************************************************************************/
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
		panel = new JPanel();
		panel.setLayout(null);
		//Set the size of the panel
		panel.setSize(1200, 800);
		
		//Add the scroll pane used to output the information
		addScrollPane();
		//Add the Begin and End date drop-down boxes
		initBeginDateDropDown();
		initEndDateDropDown();
		//Add the searchByDateButton
		addSearchByDateButton();
		//Add the searchByBothButton
		addSearchByBothButton();
		
		//Initialize the strings
		selectedInstrument = new String();
		selectedSerial = new String();
		beginDateString = new String();
		endDateString = new String();
		
		//Initialize the serialList
		serialList = new ArrayList<String>();
		
		//Add the instrument drop-down menu
		initInstrumentDropDown();
		
		//Initialize the searchLabel, define position, size and add it to
		//the panel
		searchLabel = new JLabel("Select Instrument:");
		searchLabel.setBounds(120, 15, 125, 25);
		panel.add(searchLabel);
		
		//Initialize the instrumentLabel, define position, size, and add it to
		//the panel
		instrumentLabel = new JLabel();
		instrumentLabel.setBounds(400, 15, 125, 25);
		
		//add the search and reset buttons
		addSearchButton();
		addResetButton();
		
		//If the user is an administrator, add the administrator menu button
		if(isAdmin){
			addAdminButton();
		}
		
		//Add the frame to the panel
		myFrame.add(panel);
		//Set the frame to focusable, visible, and set the size
		myFrame.setFocusable(true);
		myFrame.setVisible(false);
		myFrame.setSize(1200, 800);
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
		instrumentDropDown.setBounds(110, 40, 150, 30);
		
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
		serialList.add("AE0159");
		serialList.add("00C647");
		serialList.add("314EAA");
		serialList.add("41A89E");
		
		//Initialize the "serialDropDown" JComboBox and set the position and
		//dimensions
		serialDropDown = new JComboBox<String>();
		serialDropDown.setBounds(380, 40, 120, 30);
		
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
	 * Description: Builds the drop-down selection boxes for the beginning date
	 * Return Type: void
	 * Pre: None
	 * Post: Creates a drop-down box for the Year, Month, Day, Hour, Minute,
	 *       and Second of the beginning date.
	 *************************************************************************/
	private void initBeginDateDropDown(){
		//Initialize the beginDate----DropDown data members to store the string
		//representations of the time members they will be holding
		beginDateYearDropDown = new JComboBox<String>();
		beginDateMonthDropDown = new JComboBox<String>();
		beginDateDayDropDown = new JComboBox<String>();
		beginDateHourDropDown = new JComboBox<String>();
		beginDateMinuteDropDown = new JComboBox<String>();
		beginDateSecondDropDown = new JComboBox<String>();
		
		//Set the location and size of each
		beginDateYearDropDown.setBounds(30, 400, 120, 30);
		beginDateMonthDropDown.setBounds(150, 400, 90, 30);
		beginDateDayDropDown.setBounds(240, 400, 90, 30);
		beginDateHourDropDown.setBounds(330, 400, 90, 30);
		beginDateMinuteDropDown.setBounds(420, 400, 90, 30);
		beginDateSecondDropDown.setBounds(510, 400, 90, 30);
		
		//Populate the JComboBoxes with all members of the their respective 
		//String arrays
		for(String item : yearStringArray){
			beginDateYearDropDown.addItem(item);
		}
		for(String item : monthStringArray){
			beginDateMonthDropDown.addItem(item);
		}
		for(String item : dayStringArray){
			beginDateDayDropDown.addItem(item);
		}
		for(String item : timeStringArray){
			beginDateHourDropDown.addItem(item);
			beginDateMinuteDropDown.addItem(item);
			beginDateSecondDropDown.addItem(item);
		}
		
		//Set up the JLabels for beginDate and add them to the panel
		beginDateLabel = new JLabel("Select Beginning Date:");
		beginDateLabel.setBounds(250, 350, 200, 30);
		panel.add(beginDateLabel);
		
		beginYMDLabel = new JLabel("YYYY    :    MM    :    DD");
		beginYMDLabel.setBounds(110, 370, 150, 30);
		panel.add(beginYMDLabel);
		
		beginHMSLabel = new JLabel("HH     :     MM     :     SS");
		beginHMSLabel.setBounds(360, 370, 150, 30);
		panel.add(beginHMSLabel);
		
		
		
		//Add the "beginDate---DropDown" boxes to the panel
		panel.add(beginDateYearDropDown);
		panel.add(beginDateMonthDropDown);
		panel.add(beginDateDayDropDown);
		panel.add(beginDateHourDropDown);
		panel.add(beginDateMinuteDropDown);
		panel.add(beginDateSecondDropDown);
		
	}
	
	/**************************************************************************
	 * Description: Builds the drop-down selection boxes for the ending date
	 * Return Type: void
	 * Pre: None
	 * Post: Creates a drop-down box for the Year, Month, Day, Hour, Minute,
	 *       and Second of the ending date.
	 *************************************************************************/
	public void initEndDateDropDown(){
		//Initialize the endDate----DropDown data members to store the string
		//representations of the time members they will be holding
		endDateYearDropDown = new JComboBox<String>();
		endDateMonthDropDown = new JComboBox<String>();
		endDateDayDropDown = new JComboBox<String>();
		endDateHourDropDown = new JComboBox<String>();
		endDateMinuteDropDown = new JComboBox<String>();
		endDateSecondDropDown = new JComboBox<String>();
				
		//Set the location and size
		endDateYearDropDown.setBounds(30, 500, 120, 30);
		endDateMonthDropDown.setBounds(150, 500, 90, 30);
		endDateDayDropDown.setBounds(240, 500, 90, 30);
		endDateHourDropDown.setBounds(330, 500, 90, 30);
		endDateMinuteDropDown.setBounds(420, 500, 90, 30);
		endDateSecondDropDown.setBounds(510, 500, 90, 30);
				
		//Populate the JComboBoxes with all members of the their respective 
		//String arrays
		for(String item : yearStringArray){
			endDateYearDropDown.addItem(item);
		}
		for(String item : monthStringArray){
			endDateMonthDropDown.addItem(item);
		}
		for(String item : dayStringArray){
			endDateDayDropDown.addItem(item);
		}
		for(String item : timeStringArray){
			endDateHourDropDown.addItem(item);
			endDateMinuteDropDown.addItem(item);
			endDateSecondDropDown.addItem(item);
		}
				
		//Set up the JLabels for endDate and add them to the panel
		endDateLabel = new JLabel("Select End Date:");
		endDateLabel.setBounds(270, 450, 200, 30);
		panel.add(endDateLabel);
				
		endYMDLabel = new JLabel("YYYY    :    MM    :    DD");
		endYMDLabel.setBounds(110, 470, 150, 30);
		panel.add(endYMDLabel);
				
		endHMSLabel = new JLabel("HH     :     MM     :     SS");
		endHMSLabel.setBounds(360, 470, 150, 30);
		panel.add(endHMSLabel);
				
				
				
		//Add the "endDate---DropDown" menus to the panel
		panel.add(endDateYearDropDown);
		panel.add(endDateMonthDropDown);
		panel.add(endDateDayDropDown);
		panel.add(endDateHourDropDown);
		panel.add(endDateMinuteDropDown);
		panel.add(endDateSecondDropDown);
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
		searchByInstrumentButton = new JButton("Search Instrument");
		searchByInstrumentButton.setBounds(110, 80, 150, 25);
				
		//Add an ActionListener to the "Search" button
		searchByInstrumentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//If the button is selected, search for the serial number of
				//the selected instrument
				if(e.getActionCommand().equals("Search Instrument")){
					if(isValidQuery()){
						outputDisplay.setText("Displaying all data gathered by the " + 
											   selectedInstrument + " sensor with serial number:"
											   + selectedSerial + "\n");
					}
				}
			}
		});
		//Add the JButton to the panel
		panel.add(searchByInstrumentButton);
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
		resetButton.setBounds(380, 80, 120, 25);
				
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
		adminButton.setBounds(250, 700, 150, 25);
				
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
	 * Description: Adds the "searchByDateButton" button to the JPanel
	 * Return Type: void
	 * Pre: None
	 * Post: Instantiates the private data member searchByDateButton, sets the
	 *       size and location, then adds an ActionListener to it indicating to
	 *       retrieve the data entered by the user.
	 *************************************************************************/
	private void addSearchByDateButton(){
		//Instantiate a the private data member "searchByDateButton", and sets 
		//the position and size
		searchByDateButton = new JButton("Search Dates");
		searchByDateButton.setBounds(250, 550, 150, 25);
				
		//Add an ActionListener to the searchByDateButton button
		searchByDateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//If the button is selected, record the date values indicated by the
				//user
				if(e.getActionCommand().equals("Search Dates")){
					beginDateString = (String) beginDateYearDropDown.getSelectedItem() + "-";
					beginDateString += (String) beginDateMonthDropDown.getSelectedItem() + "-";
					beginDateString += (String) beginDateDayDropDown.getSelectedItem() + " ";
					beginDateString += (String) beginDateHourDropDown.getSelectedItem() + ":";
					beginDateString += (String) beginDateMinuteDropDown.getSelectedItem() + ":";
					beginDateString += (String) beginDateSecondDropDown.getSelectedItem();
					
					endDateString =  (String) endDateYearDropDown.getSelectedItem() + "-";
					endDateString += (String) endDateMonthDropDown.getSelectedItem() + "-";
					endDateString += (String) endDateDayDropDown.getSelectedItem() + " ";
					endDateString += (String) endDateHourDropDown.getSelectedItem() + ":";
					endDateString += (String) endDateMinuteDropDown.getSelectedItem() + ":";
					endDateString += (String) endDateSecondDropDown.getSelectedItem();
					
					outputDisplay.setText("Displaying data collected by all sensors between "
										  + beginDateString + " to " + endDateString + "\n");
					
				}
			}
		});
		//Add the JButton to the panel
		panel.add(searchByDateButton);
	}
	
	
	private void addSearchByBothButton(){
		//Instantiate a the private data member "searchByDateButton", and sets 
		//the position and size
		searchByBothButton = new JButton("Search by Both");
		searchByBothButton.setBounds(250, 625, 150, 25);
				
		//Add an ActionListener to the searchByDateButton button
		searchByBothButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//If the button is selected, record the date values indicated by the
				//user
				if(e.getActionCommand().equals("Search by Both")){
					if(isValidQuery()){
						beginDateString = (String) beginDateYearDropDown.getSelectedItem() + "-";
						beginDateString += (String) beginDateMonthDropDown.getSelectedItem() + "-";
						beginDateString += (String) beginDateDayDropDown.getSelectedItem() + " ";
						beginDateString += (String) beginDateHourDropDown.getSelectedItem() + ":";
						beginDateString += (String) beginDateMinuteDropDown.getSelectedItem() + ":";
						beginDateString += (String) beginDateSecondDropDown.getSelectedItem();
						
						endDateString =  (String) endDateYearDropDown.getSelectedItem() + "-";
						endDateString += (String) endDateMonthDropDown.getSelectedItem() + "-";
						endDateString += (String) endDateDayDropDown.getSelectedItem() + " ";
						endDateString += (String) endDateHourDropDown.getSelectedItem() + ":";
						endDateString += (String) endDateMinuteDropDown.getSelectedItem() + ":";
						endDateString += (String) endDateSecondDropDown.getSelectedItem();
						
						outputDisplay.setText("Displaying data collected by the "
											  + selectedInstrument + " sensor with serial number:"
											  + selectedSerial + " between "
											  + beginDateString + " and " + endDateString + "\n");
						
					}
				}
			}
		});
		//Add the JButton to the panel
		panel.add(searchByBothButton);
	}
	
	/**************************************************************************
	 * Description: Method called to instantiate the ScrollPane that will hold
	 *              our output
	 * Return Type: void
	 * Pre: None
	 * Post: Instantiates the JTextArea used to hold search results and binds
	 *       it to a ScrollPane which allows us to scroll though the data.
	 **************************************************************************/
	private void addScrollPane(){
		//Instantiate our JTextArea with a location, height and width and set 
		//it so it cannot be edited by the user.
		outputDisplay = new JTextArea();
		outputDisplay.setEditable(false);
		outputDisplay.setBounds(600, 25, 580, 700);
		
		//Instantiate our JScrollPane, setting the vertical and horizontal 
		//scroll bars to appear as needed, as well as setting the bounds
		scrollingDisplay = new JScrollPane(outputDisplay);
		scrollingDisplay.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollingDisplay.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollingDisplay.setBounds(600, 25, 580, 700);
		//Add the JScrollPane to the panel
		panel.add(scrollingDisplay);
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
	private String beginDateString;
	private String endDateString;
		
	//JComboBoxes to limit the user to valid searches
	private JComboBox<String> instrumentDropDown;
	private JComboBox<String> serialDropDown;
	private JComboBox<String> beginDateYearDropDown;
	private JComboBox<String> beginDateMonthDropDown;
	private JComboBox<String> beginDateDayDropDown;
	private JComboBox<String> beginDateHourDropDown;
	private JComboBox<String> beginDateMinuteDropDown;
	private JComboBox<String> beginDateSecondDropDown;
	private JComboBox<String> endDateYearDropDown;
	private JComboBox<String> endDateMonthDropDown;
	private JComboBox<String> endDateDayDropDown;
	private JComboBox<String> endDateHourDropDown;
	private JComboBox<String> endDateMinuteDropDown;
	private JComboBox<String> endDateSecondDropDown;
	
	//JFrame and JPanel on which we can display the information
	private JFrame myFrame;
	private JPanel panel;
	
	//ArrayLists of Strings to hold the list of instruments and serial numbers
	//for the JComboBoxes
	private ArrayList<String> instrumentList;
	private ArrayList<String> serialList;
	
	//String arrays to hold the constant values of Years, Months, Days, Hours,
	//Minutes, and Seconds.
	private String[] yearStringArray =        {"1975", "1976","1977","1978","1979",
			"1980","1981","1982","1983","1984","1985","1986","1987","1988","1989",
			"1990","1991","1992","1993","1994","1995","1996","1997","1998","1999",
			"2000","2001","2002","2003","2004","2005","2006","2007","2008","2009",
			"2010","2011","2012","2013","2014","2015","2016","2017","2018","2019"};
	
	private String[] monthStringArray = {"01","02","03","04","05","06",
										 "07","08","09","10","11","12"};
	
	private String[] dayStringArray = {"01","02","03","04","05","06","07","08","09","10",
									   "11","12","13","14","15","16","17","18","19","20",
									   "21","22","23","24","25","26","27","28","29","30",
									   "31"};
	
	private String[] timeStringArray = {"01","02","03","04","05","06","07","08","09","10",
			   							"11","12","13","14","15","16","17","18","19","20",
			   							"21","22","23","24","25","26","27","28","29","30",
			   							"31","32","33","34","35","36","37","38","39","40",
			   							"41","42","43","44","45","46","47","48","49","50",
			   							"51","52","53","54","55","56","57","58","59","60"};
	
	//JLabels to label objects in the GUI
	private JLabel searchLabel;
	private JLabel instrumentLabel;
	private JLabel beginDateLabel;
	private JLabel endDateLabel;
	private JLabel beginYMDLabel;
	private JLabel beginHMSLabel;
	private JLabel endYMDLabel;
	private JLabel endHMSLabel;
	
	//JButtons to allow user interaction
	private JButton resetButton;
	private JButton searchByInstrumentButton;
	private JButton searchByDateButton;
	private JButton adminButton;
	private JButton searchByBothButton;
	
	//AdminDatabaseBrowser in case the person using the system is an administrator
	private AdminDatabaseBrowser adminBrowser;
	//Boolean to indicate if the user is an administrator
	private boolean isAdmin;
	
	//JTextArea to display the results of our queries in
	private JTextArea outputDisplay;
	//JScrollPane to augment our JTextArea
	private JScrollPane scrollingDisplay;
}

