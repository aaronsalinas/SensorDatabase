/******************************************************************************
 * Filename: Login.java
 * Author: Dylan Clohessy (Dylan_Clohessy@baylor.edu)
 * Description: A class that allows users to login as users or guests, or to 
 *              create a new account if they want. GUI implementation of the
 *              aforementioned built from JFrame, JPanel, JTextFields, etc.. 
 *              Lets users log in if credentials are valid according to the
 *              database, informs them if it is not.  
 * Created: 4/28/2015
 * Modified:4/30/2015
******************************************************************************/
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class Login extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**************************************************************************
	 * Description: Custom constructor for the Login class
	 * Return Type: none
	 * Pre: None
	 * Post: Instantiates a new instance of the Login class.
	 *************************************************************************/
	public Login(){
		init();
	}
	
	/**************************************************************************
	 * Description: Builds the Login window
	 * Return Type: void
	 * Pre: None
	 * Post: Method that initializes the JFrame and JPanel. It populates the 
	 * 		 JPanel with desired labels, text boxes, buttons, and their 
	 * 		 respective functionality, sizes, and locations. It then adds the
	 *       JPanel to the JFrame and sets the desired JFrame attributes.
	 *************************************************************************/
	public void init(){
		loggedIn = false;
		username = new String();
		password = new String();
		
		//Instantiate the new panel
		setLayout(new GridBagLayout());
		setBorder(new TitledBorder("Login"));
		
		myConstraints = new GridBagConstraints();
		
		myConstraints.gridx = 0;
		myConstraints.gridy = 0;
		add(new JLabel("Username"), myConstraints);
		myConstraints.gridy++;
		add(new JLabel("Password"), myConstraints);
		
		myConstraints.gridx++;
		myConstraints.gridy = 0;
		myConstraints.gridwidth = 2;
		myConstraints.fill = GridBagConstraints.HORIZONTAL;
		myConstraints.weightx = 1;
		
		
		userText = new JTextField(25);
		add(userText, myConstraints);
		myConstraints.gridy++;
		
		
		passwordText = new JPasswordField(25);
		add(passwordText, myConstraints);
		
		addUserLoginButton();
	}
	
	
	/**************************************************************************
	 * Description: Adds the "User Login" button to the JPanel
	 * Return Type: void
	 * Pre: None
	 * Post: Instantiates the private data member loginButton, sets the size
	 *       and location, then adds an ActionListener to it indicating to
	 *       attempt to login with the current credentials.
	 *************************************************************************/
	private void addUserLoginButton(){
		
		myConstraints.gridx = 1;
		myConstraints.gridy++;
		myConstraints.gridwidth = 1;
		myConstraints.weightx = 0;
		myConstraints.fill = GridBagConstraints.NONE;
		
		//Instantiate a the private data member "User Login", and set the 
		//position and size
		loginButton = new JButton("User Login");
		
		//Add an ActionListener to the "User Login" button
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//If the button is selected, attempt to login with the info
				//provided. If successful, launch the admin browser
				if(e.getActionCommand().equals("User Login")){
					if(!loggedIn){
						getLoginInformation();
						if(validateLoginInfo()){
							if(AdminDatabaseAccess.validateUser(username, password)){
								JOptionPane.showMessageDialog(null, "Logged in as " + username);
								loggedIn = true;
							}
							else{
								JOptionPane.showMessageDialog(null, "Login Failed");
							}
						}
					}
					else{
						JOptionPane.showMessageDialog(null, "Already logged in");
					}
				}
			}
		});
		//Add the JButton to the panel
		add(loginButton, myConstraints);
	}
	
	/**************************************************************************
	 * Description: Check the validity of the Username and Password
	 * Return Type: Boolean
	 * Pre: None
	 * Post: Returns true if both fields are populated, false otherwise
	 *************************************************************************/
	private boolean validateLoginInfo(){
		boolean goodData = true;
		//Check if userText has content
		if(username.isEmpty()){
			//If not, tell the user and return false
			JOptionPane.showMessageDialog(null, "Enter a Username");
			goodData = false;
		}
		//Check if passwordText has content
		else if(password.isEmpty()){
			//If not, tell the user and return false
			JOptionPane.showMessageDialog(null, "Enter a Password");
			goodData = false;
		}
		return goodData;
	}

	/**************************************************************************
	 * Description: Loads the username and password into their respective
	 *              strings.
	 * Return Type: void
	 * Pre: None
	 * Post: Sets "username" to the String contained in userText and "password"
	 *       to the string set in passwordText.
	 *************************************************************************/
	@SuppressWarnings("deprecation")
	private void getLoginInformation(){
		username = userText.getText();
		password = passwordText.getText();
	}
	
	public boolean isLoggedIn(){
		return loggedIn;
	}
	
	public String getUser(){
		return username;
	}
	
	private boolean loggedIn;
	
	private GridBagConstraints myConstraints;
	
	//Private data members for "Login" class
	private String username;
	private String password;
	
	//JTextField to hold string input data
	private JTextField userText;
	
	//JPasswordField for securely entering and storing password information 
	private JPasswordField passwordText;
	
	//JButtons the user can select
	private JButton loginButton;
}