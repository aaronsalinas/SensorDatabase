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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.filechooser.FileNameExtensionFilter;


public class AdminDatabaseBrowser extends JFrame{
	
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 * Description: Custom constructor for the AdminDatabaseBrowser class
	 * Return Type: none
	 * Pre: None
	 * Post: Instantiates a new instance of the AdminDatabaseBrowser class.
	 *************************************************************************/	
	public AdminDatabaseBrowser(String loginUsername){
		currentUser = loginUsername;
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
		myFrame.setTitle("Admin: " + currentUser);
		
		//Instantiate the new panel
		panel = new JPanel(null);
		//Set the size of the panel
		panel.setSize(300, 215);
		
		inputFileName = new String();
		adminEngine = new DatabaseSearchEngine(this);
		
		addOpenSearchEngineButton();
		addUploadFileToDatabaseButton();
		addChangePasswordButton();
		
		
		//Add the frame to the panel
		myFrame.add(panel);
		//Set the frame to focusable, visible, and set the size
		myFrame.setFocusable(true);
		myFrame.setVisible(true);
		myFrame.setSize(300, 215);
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
	 * Description: Adds the "changePasswordButton" button to the JPanel
	 * Return Type: void
	 * Pre: None
	 * Post: Instantiates the private data member changePasswordButton, 
	 * 		 sets the size and location, then adds an ActionListener to it 
	 * 		 indicating to open the change password window.
	 *************************************************************************/
	private void addChangePasswordButton(){
		//Instantiate a the JButton changePasswordButton, and set the 
		//position and size
		changePasswordButton = new JButton("Change Password");
		changePasswordButton.setBounds(70, 115, 140, 25);
				
		//Add an ActionListener to the button
		changePasswordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//If the button is pressed, call the initPasswordUpdate() method
				if(e.getActionCommand().equals("Change Password")){
					initPasswordUpdate();
				}
			}
		});
		//Add the JButton to the panel
		panel.add(changePasswordButton);
		
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
	    	new StoreSensorData(inputFileName);
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
	
	
	/**************************************************************************
	 * Description: Builds the updatePassword window
	 * Return Type: void
	 * Pre: void
	 * Post: Method that initializes the JFrame and JPanel. It populates the 
	 * 		 JPanel with desired labels, text boxes, buttons, and their 
	 * 		 respective functionality, sizes, and locations. It then adds the
	 *       JPanel to the JFrame and sets the desired JFrame attributes.
	 *************************************************************************/
	public void initPasswordUpdate(){
		hideScreen();
		passwordUpdateFrame = new JFrame();
		//
		passwordUpdateFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//Set the title of the window
		passwordUpdateFrame.setTitle("Update Password");
		
		//Instantiate the new panel
		passwordUpdatePanel = new JPanel(null);
		//Set the size of the panel
		passwordUpdatePanel.setSize(400, 230);
		
		//current password label
		currentPasswordLabel = new JLabel("Current Password:");
		currentPasswordLabel.setBounds(10, 20, 120, 25);
		passwordUpdatePanel.add(currentPasswordLabel);
		
		//new password label
		newPasswordEnterLabel = new JLabel("New Password:");
		newPasswordEnterLabel.setBounds(10, 60, 100, 25);
		passwordUpdatePanel.add(newPasswordEnterLabel);
		
		//new password check label
		newPasswordCheckLabel = new JLabel("New Password:");
		newPasswordCheckLabel.setBounds(10, 100, 100, 25);
		passwordUpdatePanel.add(newPasswordCheckLabel);
		
		//old password text field
		oldPasswordText = new JPasswordField();
		oldPasswordText.setBounds(130,20, 220, 25);
		passwordUpdatePanel.add(oldPasswordText);
		
		//new password text field
		newPasswordText = new JPasswordField();
		newPasswordText.setBounds(130,60, 220, 25);
		passwordUpdatePanel.add(newPasswordText);
		
		//new password check text field
		newPasswordCheck = new JPasswordField();
		newPasswordCheck.setBounds(130, 100, 220, 25);
		passwordUpdatePanel.add(newPasswordCheck);
		
		//Add the updatePassword and cancelUpdate buttons.
		addUpdatePasswordButton();
		addCancelUpdateButton();
		
		//Add the panel to the frame
		passwordUpdateFrame.add(passwordUpdatePanel);
		//Set the frame to focusable, visible, and set the size
		passwordUpdateFrame.setFocusable(true);
		passwordUpdateFrame.setVisible(true);
		passwordUpdateFrame.setSize(400, 230);
	}
	
	/**************************************************************************
	 * Description: Adds the "addUpdatePasswordButton" button to the JPanel
	 * Return Type: void
	 * Pre: None
	 * Post: Instantiates the private data member addUpdatePasswordButton, 
	 * 		 sets the size and location, then adds an ActionListener to it 
	 * 		 indicating to check the validity of the information entered and
	 *       act accordingly
	 *************************************************************************/
	private void addUpdatePasswordButton(){
		//Instantiate a the JButton addUpdatePasswordButton, and set the 
		//position and size
		updatePasswordButton = new JButton("Update Password");
		updatePasswordButton.setBounds(30, 145, 140, 25);
				
		//Add an ActionListener to the button
		updatePasswordButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e){
				//If the button is pressed, check that the username and password
				//combination is valid, then see of the new passwords match, if 
				//both are true, update the password
				if(e.getActionCommand().equals("Update Password")){
					if(verifyCurrentPassword() && verifyNewPasswords()){
						AdminDatabaseAccess.changePassword(currentUser, newPasswordText.getText());
						JOptionPane.showMessageDialog(null, "Password successfully updated");
						passwordUpdateFrame.setVisible(false);
						showScreen();
					}
				}
			}
		});
		//Add the JButton to the panel
		passwordUpdatePanel.add(updatePasswordButton);
		
	}
	
	/**************************************************************************
	 * Description: Adds the "cancelUpdateButton" button to the JPanel
	 * Return Type: void
	 * Pre: None
	 * Post: Instantiates the private data member cancelUpdateButton, 
	 * 		 sets the size and location, then adds an ActionListener to it 
	 * 		 indicating to close the current window and switch back to the 
	 *       administrator browser.
	 *************************************************************************/
	private void addCancelUpdateButton(){
		//Instantiate a the JButton cancelUpdateButton, and set the 
		//position and size
		cancelUpdateButton = new JButton("Cancel");
		cancelUpdateButton.setBounds(200, 145, 140, 25);
				
		//Add an ActionListener to the button
		cancelUpdateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//If the button is pressed, close this window and
				//open the administrative browser
				if(e.getActionCommand().equals("Cancel")){
					passwordUpdateFrame.setVisible(false);
					showScreen();
				}
			}
		});
		//Add the JButton to the panel
		passwordUpdatePanel.add(cancelUpdateButton);
		
	}
	
	/**************************************************************************
	 * Description: Verifies the current username & password combination
	 * Return Type: bool
	 * Pre: None
	 * Post: Returns true if the correct password was entered, false otherwise
	 *************************************************************************/
	@SuppressWarnings("deprecation")
	boolean verifyCurrentPassword(){
		boolean passwordIsGood = true;
		//If the username & password is not a valid combination, return false
		if(!AdminDatabaseAccess.validateUser(currentUser, oldPasswordText.getText())){
			passwordIsGood = false;
			JOptionPane.showMessageDialog(null, "Incorrect password");
		}
		return passwordIsGood;
	}
	
	/**************************************************************************
	 * Description: Verifies the new password fields match and are not empty
	 * Return Type: bool
	 * Pre: None
	 * Post: Returns true if the new passwords match and are not empty, false
	 *       otherwise.
	 *************************************************************************/
	@SuppressWarnings("deprecation")
	boolean verifyNewPasswords(){
		boolean passwordsMatch = true;
		//If the new passwords do not match, return false
		if(!newPasswordText.getText().equals(newPasswordCheck.getText())){
			JOptionPane.showMessageDialog(null, "Passwords must match");
			passwordsMatch = false;
		}
		//If the password field is empty, return false
		else if(newPasswordText.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Please enter a new password");
			passwordsMatch = false;
		}
		
		return passwordsMatch;
	}
	
	
	//private data members for the AdminDataBaseBrowser class
	
	//String to hold the absolute path of an input file.
	private String inputFileName;
	private String currentUser;
	
	//JFrame and JPanel for graphical output
	private JFrame passwordUpdateFrame;
	private JPanel passwordUpdatePanel;
	private JFrame myFrame;
	private JPanel panel;
	
	//JFileChooser we use to browse files
	private JFileChooser fileChooser;
	
	//JLabels to label the current, new and re entered password labels
	private JLabel currentPasswordLabel;
	private JLabel newPasswordEnterLabel;
	private JLabel newPasswordCheckLabel;
	
	//JPasswordFields to allow safe entry of passwords
	private JPasswordField oldPasswordText;
	private JPasswordField newPasswordText;
	private JPasswordField newPasswordCheck;
	
	//JButtons that allow the user to interact with the program
	private JButton openSearchEngineButton;
	private JButton uploadFileToDatabaseButton;
	private JButton changePasswordButton;
	private JButton updatePasswordButton;
	private JButton cancelUpdateButton;
	
	//A DataBaseSearchEngine the administrator can call
	private DatabaseSearchEngine adminEngine;
}