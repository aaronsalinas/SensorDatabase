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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame{
	
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
		createAccount = new AccountCreation(this);
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
		//Instantiate the new frame
		myFrame = new JFrame();
		//Set default close operation
		myFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//Set the title of the window
		myFrame.setTitle("Database Login");
		
		username = new String();
		password = new String();
		
		//Instantiate the new panel
		panel = new JPanel(null);
		//Set the size of the panel
		panel.setSize(350, 150);
		
		//User Label
		userLabel = new JLabel("Username");
		userLabel.setBounds(10, 10, 80, 25);
		panel.add(userLabel);
		
		//User text field
		userText = new JTextField(20);
		panel.add(userText);
		userText.setBounds(100, 10, 200, 25);
		
		//password label
		passwordLabel = new JLabel("Password");
		panel.add(passwordLabel);
		passwordLabel.setBounds(10, 40, 80, 25);

		//password text field
		passwordText = new JPasswordField(20);
		passwordText.setBounds(100, 40, 200, 25);
		panel.add(passwordText);
	
		
		//Login Button
		addUserLoginButton();
		
		//Guest Login Button
		addGuestLoginButton();
		
		//Register Button
		addRegisterButton();
		
		//Add the frame to the panel
		myFrame.add(panel);
		//Set the frame to focusable, visible, and set the size
		myFrame.setFocusable(true);
		myFrame.setVisible(true);
		myFrame.setSize(350, 150);
		
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
		//Instantiate a the private data member "User Login", and set the 
		//position and size
		loginButton = new JButton("User Login");
		loginButton.setBounds(10, 80, 100, 25);
		
		//Add an ActionListener to the "User Login" button
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//If the button is selected, attempt to login with the info
				//provided. If successful, launch the admin browser
				if(e.getActionCommand().equals("User Login")){
					getLoginInformation();
					if(validateLoginInfo() && AdminDatabaseAccess.validateUser(username, password)){
						JOptionPane.showMessageDialog(null, "Logged in as " + username);
						hideScreen();
						new AdminDatabaseBrowser();
					}
					else{
						JOptionPane.showMessageDialog(null, "Login Failed");
					}
				}
			}
		});
		//Add the JButton to the panel
		panel.add(loginButton);
	}
	
	
	/**************************************************************************
	 * Description: Adds the "Guest Login" button to the JPanel
	 * Return Type: void
	 * Pre: None
	 * Post: Instantiates the private data member loginButton, sets the size
	 *       and location, then adds an ActionListener to it indicating to
	 *       login as a guest
	 *************************************************************************/
	private void addGuestLoginButton(){
		//Instantiate a the private data member "Guest Login", and set the 
		//position and size
		guestButton = new JButton("Guest");
		guestButton.setBounds(120, 80, 100, 25);
		
		//Add an ActionListener to the "Guest Login" button
		guestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//If the button is selected, login as a guest
				if(e.getActionCommand().equals("Guest")){
					JOptionPane.showMessageDialog(null, "Logged in as Guest");
					hideScreen();
					new DatabaseSearchEngine();
				}
			}
		});
		//Add the JButton to the JPanel
		panel.add(guestButton);
	}
	
	/**************************************************************************
	 * Description: Adds the "Register" button to the JPanel
	 * Return Type: void
	 * Pre: None
	 * Post: Instantiates the private data member loginButton, sets the size
	 *       and location, then adds an ActionListener to it indicating to
	 *       call the "addAUser" function when pressed
	 *************************************************************************/
	private void addRegisterButton(){
		//Instantiate a the private data member "Register", and set the 
		//position and size
		registerButton = new JButton("Register");
		registerButton.setBounds(230, 80, 100, 25);
		
		//Add an ActionListener to the "Register" button
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//If the button is selected, call the "addAUser" function
				if(e.getActionCommand().equals("Register")){
					addAUser();
				}
			}
		});
		//Add the JButton to the JPanel
		panel.add(registerButton);
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
	 * Description: Launches the "AccountCreation" window and closes the
	 * 	            "Login" window.
	 * Return Type: void
	 * Pre: None
	 * Post: Sets the "AccountCreation" screen to visible and "Login" screen
	 *       to not visible
	 *************************************************************************/
	public void addAUser(){
		this.hideScreen();
		createAccount.showScreen();
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
	
	//Private data members for "Login" class
	private String username;
	private String password;
	
	//JFrame and JPanel on which we display information
	private JFrame myFrame;
	private JPanel panel;
	
	//JLabels to hold labels that will appear on the screen
	private JLabel userLabel;
	private JLabel passwordLabel;
	
	//JTextField to hold string input data
	private JTextField userText;
	
	//JPasswordField for securely entering and storing password information 
	private JPasswordField passwordText;
	
	//JButtons the user can select
	private JButton loginButton;
	private JButton guestButton;
	private JButton registerButton;
	
	//An "AccountCreation" object to launch if someone wants to register a 
	//new user
	private AccountCreation createAccount;
	
}