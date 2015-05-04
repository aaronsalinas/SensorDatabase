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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class AccountCreation extends JPanel{
	
	private static final long serialVersionUID = 1L;
	/**************************************************************************
	 * Description: Custom constructor for the AccountCreation class
	 * Return Type: none
	 * Pre: Valid login is passed into the constructor
	 * Post: Instantiates a new instance of the AccountCreation class.
	 *************************************************************************/
	public AccountCreation(){
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
		
		//Instantiate the new panel
		setLayout(new GridBagLayout());
		setBorder(new TitledBorder("Register new user"));
		
		myConstraints = new GridBagConstraints();
		
		myConstraints.gridx = 0;
		myConstraints.gridy = 0;
		
		
		add(new JLabel("First Name"), myConstraints);
		myConstraints.gridy++;
		add(new JLabel("Last name"), myConstraints);
		myConstraints.gridy++;
		add(new JLabel("Username"), myConstraints);
		myConstraints.gridy++;
		add(new JLabel("Password"), myConstraints);
		myConstraints.gridy++;
		add(new JLabel("Confirm Password"), myConstraints);
		myConstraints.gridy++;
		add(new JLabel("Email Address"), myConstraints);
		myConstraints.gridy++;
		add(new JLabel("Confirm Address"), myConstraints);
		
		myConstraints.gridx++;
		myConstraints.gridy = 0;
		myConstraints.gridwidth = 2;
		myConstraints.fill = GridBagConstraints.HORIZONTAL;
		myConstraints.weightx = 1;
		

		//First name text field
		firstNameText = new JTextField(25);
		add(firstNameText, myConstraints);
		myConstraints.gridy++;

		//Last name text field
		lastNameText = new JTextField(25);
		add(lastNameText, myConstraints);
		myConstraints.gridy++;

		//Username text field
		usernameText = new JTextField(25);
		add(usernameText, myConstraints);
		myConstraints.gridy++;

		//Password Text field
		passwordText = new JPasswordField(25);
		add(passwordText, myConstraints);
		myConstraints.gridy++;

		//re enter password field
		passwordCheckText = new JPasswordField(25);
		add(passwordCheckText, myConstraints);
		myConstraints.gridy++;

		//email text field
		emailText = new JTextField(25);
		add(emailText, myConstraints);
		myConstraints.gridy++;

		//re enter email text field
		emailCheckText = new JTextField(25);
		add(emailCheckText, myConstraints);
		
		//Create Account Button
		addCreateButton();
		
		//Cancel Button
		addResetButton();
	}
	
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
		
		myConstraints.gridx = 1;
		myConstraints.gridy++;
		myConstraints.gridwidth = 1;
		myConstraints.weightx = 0;
		myConstraints.fill = GridBagConstraints.NONE;
		
		//Create the Create Account Button and set bounds
		createButton = new JButton("Create Account");
		
		//Define the action associated with the button
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if(e.getActionCommand().equals("Create Account")){
					if(checkAttributes()){
						if(addNewAccount()){
							JOptionPane.showMessageDialog(null, "Account Created");	
							clearInformation();
						}
						else{
							if(AdminDatabaseAccess.checkIfAdminExists(usernameText.getText())){
								JOptionPane.showMessageDialog(null, "Username already exists");	
							}
							else{
								JOptionPane.showMessageDialog(null, "Account creation failed");
							}
						}
					}
				}
			}
		});
		//Add it to the panel
		add(createButton, myConstraints);
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
	private void addResetButton(){
		myConstraints.gridx++;
		//Create the Cancel Button and set bounds
		resetButton = new JButton("Reset");
		//Define the action associated with it
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if(e.getActionCommand().equals("Reset")){
					clearInformation();
				}
			}
		});
		//Add it to the panel
		add(resetButton, myConstraints);
	}
	
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
	 * Description: Function that clears all fields 
	 * Return Type: void
	 * Pre: None
	 * Post: Prints debug info to the console
	 *************************************************************************/
	private void clearInformation(){
		firstNameText.setText("");
		lastNameText.setText("");
		usernameText.setText("");
		emailText.setText("");
		emailCheckText.setText("");
		passwordText.setText("");
		passwordCheckText.setText("");
		
	}
	
	private GridBagConstraints myConstraints;
	
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
	private JButton resetButton;
	private JButton createButton;
}