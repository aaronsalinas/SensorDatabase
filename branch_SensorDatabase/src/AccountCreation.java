/******************************************************************************
 * Filename: AccountCreation.java
 * Author: Dylan Clohessy (Dylan_Clohessy@baylor.edu)
 * Description: A class that allows users to create a new account. If the user
 *              fills out all JTextFields in a valid manner, attempts to create
 *              the account. If it already exists within the database, doesn't
 *              create account. If not, creates it and returns user to Login
 *              screen.  
 * Created: 4/28/2015
 * Modified:4/30/2015
******************************************************************************/
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class AccountCreation extends JFrame{
	
	private static final long serialVersionUID = 1L;
	/**************************************************************************
	 * Description: Custom constructor for the AccountCreation class
	 * Return Type: none
	 * Pre: Valid login is passed into the constructor
	 * Post: Instantiates a new instance of the AccountCreation class.
	 *************************************************************************/
	public AccountCreation(Login originalLoginMenu){
		//Sets the mainMenu data member to the login passed in
		mainMenu = originalLoginMenu;
		//Init the window
		init();
	}
	
	/**************************************************************************
	 * Description: Builds the AccountCreation window.
	 * Return Type: void
	 * Pre: None
	 * Post: Method that initializes the JFrame and JPanel. It populates the 
	 * 		 JPanel with desired labels, text boxes, buttons, and their 
	 * 		 respective functionality, sizes, and locations. It then adds the
	 *       JPanel to the JFrame and sets the desired JFrame attributes.
	 *************************************************************************/
	private void init() {
		
		//Instantiate the new frame.
		myFrame = new JFrame();
		//Set default close operation
		myFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//Set the title
		myFrame.setTitle("Create New Databse Account");

		//instantiate the new JPanel with size 350x300
		panel = new JPanel(null);
		panel.setSize(350, 300);
		
		//First Name label
		firstNameLabel = new JLabel("First name");
		firstNameLabel .setBounds(10, 10, 120, 25);
		panel.add(firstNameLabel);

		//First name text field
		firstNameText = new JTextField(20);
		firstNameText.setBounds(130, 10, 200, 25);
		panel.add(firstNameText);
		
		
		//Last name label
		lastNameLabel = new JLabel("Last name");
		lastNameLabel .setBounds(10, 40, 120, 25);
		panel.add(lastNameLabel);

		//Last name text field
		lastNameText = new JTextField(20);
		lastNameText.setBounds(130, 40, 200, 25);
		panel.add(lastNameText);
		
		
		//User name label
		usernameLabel = new JLabel("Username");
		usernameLabel.setBounds(10, 70, 120, 25);
		panel.add(usernameLabel);

		//Username text field
		usernameText = new JTextField(20);
		usernameText.setBounds(130, 70, 200, 25);
		panel.add(usernameText);
		
		
		//Password Label
		passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 100, 120, 25);
		panel.add(passwordLabel);

		//Password Text field
		passwordText = new JPasswordField(20);
		passwordText.setBounds(130, 100, 200, 25);
		panel.add(passwordText);
		
		
		//re enter password Label
		passwordCheckLabel = new JLabel("Confirm Password");
		passwordCheckLabel.setBounds(10, 130, 120, 25);
		panel.add(passwordCheckLabel);

		//re enter password field
		passwordCheckText = new JPasswordField(20);
		passwordCheckText.setBounds(130, 130, 200, 25);
		panel.add(passwordCheckText);
		
		
		//email label
		emailLabel = new JLabel("Email");
		emailLabel.setBounds(10, 160, 120, 25);
		panel.add(emailLabel);

		//email text field
		emailText = new JTextField(20);
		emailText.setBounds(130, 160, 200, 25);
		panel.add(emailText);
		
		
		//re enter label
		emailCheckLabel = new JLabel("Confirm Email");
		emailCheckLabel.setBounds(10, 190, 120, 25);
		panel.add(emailCheckLabel);

		//re enter email text field
		emailCheckText = new JTextField(20);
		emailCheckText.setBounds(130, 190, 200, 25);
		panel.add(emailCheckText);
		
		//Create Account Button
		addCreateButton();
		
		//Cancel Button
		addCancelButton();
		
		//Add panel to frame
		myFrame.add(panel);
		//Do not need to see this Window at the beginning of the program
		myFrame.setVisible(false);
		myFrame.setFocusable(true);
		myFrame.setSize(350, 300);
	}
	
	/*****************************Buttons Makers******************************/
	
	/**************************************************************************
	 * Description: Method that creates the Create Account button and adds the
	 * 				correct functionality to it before adding it to the pane
	 * Return Type: void
	 * Pre: None
	 * Post: Instantiates the private data member createButton, sets the size
	 *       and location, then adds the ActionListener to it indicating to
	 *       create the account only if the data entered is valid. Then adds it
	 *       to the panel.
	 *************************************************************************/
	private void addCreateButton(){
		//Create the Create Account Button and set bounds
		createButton = new JButton("Create Account");
		createButton.setBounds(25, 230, 150, 25);
		//Define the action associated with the button
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if(e.getActionCommand().equals("Create Account")){
					if(checkAttributes()){
						if(addNewAccount()){
							JOptionPane.showMessageDialog(null, "Account Created");
							returnToLogin();	
						}
						else{
							JOptionPane.showMessageDialog(null, "Account Creation Failed");
						}
					}
				}
			}
		});
		//Add it to the panel
		panel.add(createButton);
	}
	
	/**************************************************************************
	 * Description: Method that creates the Cancel button and adds the correct
	 *              functionality to it before adding it to the pane
	 * Return Type: void
	 * Pre: None
	 * Post: Instantiates the private data member cancelButton, sets the size
	 *       and location, then adds the ActionListener to it indicating to
	 *       return to the main screen when it is pressed. Then adds it to the
	 *       panel.
	 *************************************************************************/
	private void addCancelButton(){
		//Create the Cancel Button and set bounds
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(200, 230, 100, 25);
		//Define the action associated with it
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if(e.getActionCommand().equals("Cancel")){
					returnToLogin();
				}
			}
		});
		//Add it to the panel
		panel.add(cancelButton);
	}
	/*****************************Buttons Makers******************************/
	
	/**************************************************************************
	 * Description: Method that checks the validity of the passwords entered
	 * Return Type: boolean
	 * Pre: None
	 * Post: Returns true if both password fields are populated with the same
	 *       string, false otherwise. If false, indicates what is missing\wrong
	 *************************************************************************/
	@SuppressWarnings("deprecation")
	private boolean checkPasswords(){
		boolean passwordsMatch = true;
		//If the fields don't match, set return value to false and inform user
		if(!passwordText.getText().equals(passwordCheckText.getText())){	
			JOptionPane.showMessageDialog(null, "Password fields must match");
			passwordsMatch = false;
		}
		
		//If the fields match but are empty, set return value to false and
		//inform the user
		else if(passwordText.getPassword().length == 0){
			JOptionPane.showMessageDialog(null, "Please enter a Password");
			passwordsMatch = false;
		}
		
		return passwordsMatch;
	}
	
	
	/**************************************************************************
	 * Description: Method that checks the validity of the email addresses 
	 * 				entered.
	 * Return Type: boolean
	 * Pre: None
	 * Post: Returns true if both email fields are populated with the same
	 *       string, false otherwise. If false, indicates what is missing\wrong
	 *************************************************************************/
	private boolean checkEmails(){
		boolean emailsMatch = true;
		//If the email addresses do not match, set return value to false and
		//inform the user
		if(!emailText.getText().equals(emailCheckText.getText())){
			JOptionPane.showMessageDialog(null, "Email fields must match");
			emailsMatch = false;
		}
		
		//If the fields match but are empty, set return value to false and
		//inform the user
		else if(emailText.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Please enter an email address");
			emailsMatch = false;
		}
		
		return emailsMatch;
	}
	
	/**************************************************************************
	 * Description: Method that checks the validity of the all fields entered
	 * Return Type: boolean
	 * Pre: None
	 * Post: Returns true if all fields are populated and the fields that
	 *       be matching are, false otherwise. If false, indicates the highest
	 *       field that is missing\wrong.
	 *************************************************************************/
	private boolean checkAttributes(){
		boolean goodInput = true;
		
		//Check to see if there is a first name entered
		if(firstNameText.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Please enter a First name");
			goodInput = false;
		}
		
		//Check to see if there is a last name entered
		else if(lastNameText.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Please enter a Last name");
			goodInput = false;
		}
		
		//Check to see if there is a user name entered
		else if(usernameText.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Please enter a Username");
			goodInput = false;
		}
		
		//Check to see if the password fields are good 
		else if(!checkPasswords()){
			goodInput = false;
		}
		
		//Check to see if the email fields are good
		else if(!checkEmails()){
			goodInput = false;
		}
		
		return goodInput;
	}
	
	/**************************************************************************
	 * Description: Method used to attempt to add an administrator to the 
	 *              database based on the information entered
	 * Return Type: boolean
	 * Pre: We are connected to the database
	 * Post: Returns whether or not the administrator was added to the 
	 * 		 database.
	 *************************************************************************/
	@SuppressWarnings("deprecation")
	private boolean addNewAccount(){
		return AdminDatabaseAccess.addAdministrator(usernameText.getText().trim(),
									  		 		passwordText.getText().trim(),
									  		 		firstNameText.getText().trim(),
									  		 		lastNameText.getText().trim(),
									  		 		emailText.getText().trim());
	}
	
	/**************************************************************************
	 * Description: Returns to the login window when called
	 * Return Type: void
	 * Pre: None
	 * Post: Hides this window and displays the login window
	 *************************************************************************/
	private void returnToLogin(){
		this.hideScreen();
		mainMenu.showScreen();
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
	 * Description: Debug function to check what values the JTextFields are
	 *              reading in
	 * Return Type: void
	 * Pre: None
	 * Post: Prints debug info to the console
	 *************************************************************************/
	@SuppressWarnings("deprecation")
	private void printInformation(){
		System.out.println(firstNameText.getText());
		System.out.println(lastNameText.getText());
		System.out.println(usernameText.getText());
		System.out.println(passwordText.getText());
		System.out.println(emailText.getText());
	}
	
	//all Private data members of the class
	//login to store the original menu
	private Login mainMenu;
	
	//JFrame and JPanel on which we display information
	private JFrame myFrame;
	private JPanel panel;
	
	//JLabels for each field that needs to be filled out
	private JLabel firstNameLabel;
	private JLabel lastNameLabel;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JLabel passwordCheckLabel;
	private JLabel emailLabel;
	private JLabel emailCheckLabel;
	
	//JTextFields for entering data
	private JTextField firstNameText;
	private JTextField lastNameText;
	private JTextField usernameText;
	private JTextField emailText;
	private JTextField emailCheckText;
	
	//JPasswordFields for password input security
	private JPasswordField passwordText;
	private JPasswordField passwordCheckText;
	
	//JButtons for navigation and user logic
	private JButton cancelButton;
	private JButton createButton;
}