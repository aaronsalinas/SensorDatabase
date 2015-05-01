/******************************************************************************
 * Filename: AdminDatabaseBrowser.java
 * Author: Dylan Clohessy (Dylan_Clohessy@baylor.edu)
 * Description: A class that allows administrators to search the database or 
 * 				add content to it by selecting an input file
 * Created: 4/30/2015
 * Modified:4/30/2015
******************************************************************************/
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class AdminDatabaseBrowser extends JFrame{
	
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 * Description: Custom constructor for the AdminDatabaseBrowser class
	 * Return Type: none
	 * Pre: None
	 * Post: Instantiates a new instance of the AdminDatabaseBrowser class.
	 *************************************************************************/	
	public AdminDatabaseBrowser(){
		init();
	}
	
	/**************************************************************************
	 * Description: Builds the AdminDatabaseBrowser window
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
		myFrame.setTitle("Admin Database Browser");
		
		//Instantiate the new panel
		panel = new JPanel(null);
		//Set the size of the panel
		panel.setSize(300, 150);
		
		inputFileName = new String();
		adminEngine = new DatabaseSearchEngine(this);
		
		addOpenSearchEngineButton();
		addUploadFileToDatabaseButton();
		
		
		//Add the frame to the panel
		myFrame.add(panel);
		//Set the frame to focusable, visible, and set the size
		myFrame.setFocusable(true);
		myFrame.setVisible(true);
		myFrame.setSize(300, 150);
	}
	
	/**************************************************************************
	 * Description: Adds the "OpenSearchEngineButton" button to the JPanel
	 * Return Type: void
	 * Pre: None
	 * Post: Instantiates the private data member OpenSearchEngineButton, sets
	 * 		 the size and location, then adds an ActionListener to it 
	 * 		 indicating to open an instance of DatabaseSearchEngine.
	 *************************************************************************/
	private void addOpenSearchEngineButton(){
		//Instantiate a the JButton openSearchEngineButton, and set the 
		//position and size
		openSearchEngineButton = new JButton("Open Search Engine");
		openSearchEngineButton.setBounds(65, 25, 150, 25);
				
		//Add an ActionListener to the button
		openSearchEngineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//If selected, we display the adminEngine screen and hide the
				//current screen
				if(e.getActionCommand().equals("Open Search Engine")){
					adminEngine.showScreen();
					hideScreen();
				}
			}
		});
		//Add the JButton to the panel
		panel.add(openSearchEngineButton);
		
	}
	
	/**************************************************************************
	 * Description: Adds the "uploadFileToDatabaseButton" button to the JPanel
	 * Return Type: void
	 * Pre: None
	 * Post: Instantiates the private data member uploadFileToDatabaseButton, 
	 * 		 sets the size and location, then adds an ActionListener to it 
	 * 		 indicating to open a file chooser.
	 *************************************************************************/
	private void addUploadFileToDatabaseButton(){
		//Instantiate a the JButton uploadFileToDatabaseButton, and set the 
		//position and size
		uploadFileToDatabaseButton = new JButton("Upload File");
		uploadFileToDatabaseButton.setBounds(90, 70, 100, 25);
				
		//Add an ActionListener to the button
		uploadFileToDatabaseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//If the button is pressed, call the chooseFile() method
				if(e.getActionCommand().equals("Upload File")){
					chooseFile();
				}
			}
		});
		//Add the JButton to the panel
		panel.add(uploadFileToDatabaseButton);
		
	}
	
	/**************************************************************************
	 * Description: Instantiates the JFileChooser and use it to get the 
     *				absolute path of an .acs or .txt file for opening and 
     *				reading.
	 * Return Type: void
	 * Pre: None
	 * Post: Instantiates the private data member fileChooser. Then sets the
	 * 		 designated file types we are interested in opening. If the file
	 *       chosen is the correct format, record the absolute path.
	 *************************************************************************/	
	public void chooseFile(){
		//Initialize JFileChooser
		fileChooser = new JFileChooser();
		//Set the filter so we can only open .asc and .txt files
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "ASC & TXT", "acs", "txt");
	    //apply the filter
	    fileChooser.setFileFilter(filter);
	    //Set the returnVal to the type of file being opened
	    int returnVal = fileChooser.showOpenDialog(getParent());
	    //If the returnVal is of the approved format, store the absolute path.
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	inputFileName = fileChooser.getSelectedFile().getAbsolutePath();
	    }
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
	
	
	//private data members for the AdminDataBaseBrowser class
	
	//String to hold the absolute path of an input file.
	private String inputFileName;
	//JFrame and JPanel for graphical output
	private JFrame myFrame;
	private JPanel panel;
	
	//JFileChooser we use to browse files
	private JFileChooser fileChooser;
	
	
	//JButtons that allow the user to interact with the program
	private JButton openSearchEngineButton;
	private JButton uploadFileToDatabaseButton;
	
	//A DataBaseSearchEngine the administrator can call
	private DatabaseSearchEngine adminEngine;
}