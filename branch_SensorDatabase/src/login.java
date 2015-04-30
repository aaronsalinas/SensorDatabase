import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class login extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**************************************************************************
	 * Description: Custom constructor for the login class
	 * Return Type: none
	 * Pre: Valid login is passed into the constructor
	 * Post: Instantiates a new instance of the newUserPanel class.
	 *************************************************************************/
	public login(){
		createAccount = new AccountCreation(this);
		init();
	}
	
	public void init(){
		myFrame = new JFrame();
		myFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		myFrame.setTitle("Database Login");
		
		panel = new JPanel(null);
		panel.setSize(350, 150);
		
		//User Label
		userLabel = new JLabel("User");
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
		loginButton = new JButton("user login");
		loginButton.setBounds(10, 80, 100, 25);
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if(e.getActionCommand().equals("user login")){
					JOptionPane.showMessageDialog(null, "login as " + userText.getText());
				}
			}
		});
		panel.add(loginButton);
		
		
		//Guest Login Button
		guestButton = new JButton("guest login");
		guestButton.setBounds(120, 80, 100, 25);
		guestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if(e.getActionCommand().equals("guest login")){
					JOptionPane.showMessageDialog(null, "Login as guest");
				}
			}
		});
		panel.add(guestButton);
		
		//Register Button
		registerButton = new JButton("register");
		registerButton.setBounds(230, 80, 100, 25);
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if(e.getActionCommand().equals("register")){
					addAUser();
				}
			}
		});
		panel.add(registerButton);
		
		
		myFrame.add(panel);
		
		myFrame.setFocusable(true);
		myFrame.setVisible(true);
		myFrame.setSize(350, 150);
		
	}
	
	//Method for getting the username in the feild
	private String getUsername(){
		return userText.getText();
	}

	//Method for getting the password in the feild
	@SuppressWarnings("deprecation")
	private String getPassword(){
		return passwordText.getText();
	}
	
	
	//method that allows us to retrieve login information
	public void retrieveLoginInfo(String username, String password){
		username = getUsername();
		password = getPassword();
	}
	
	//function that allows the user to create an account
	public void addAUser(){
		this.hideScreen();
		createAccount.showScreen();
	}
	
	//Function that hides the window
	public void hideScreen(){
		myFrame.setVisible(false);
	}
	
	//Function that shows the window
	public void showScreen(){
		myFrame.setVisible(true);
	}
	
	private JFrame myFrame;
	private JPanel panel;
	
	private JLabel userLabel;
	private JLabel passwordLabel;
	
	private JTextField userText;
	
	private JPasswordField passwordText;
	
	private JButton loginButton;
	private JButton guestButton;
	private JButton registerButton;
	
	private AccountCreation createAccount;
}