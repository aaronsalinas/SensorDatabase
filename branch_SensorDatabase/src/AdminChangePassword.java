/******************************************************************************
 * Filename: AdminDatabaseBrowser.java
 * Author: Dylan Clohessy (Dylan_Clohessy@baylor.edu)
 * Description: A class that allows administrators to search the database or 
 * 				add content to it by selecting an input file
 * Created: 4/30/2015
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
import javax.swing.border.TitledBorder;


public class AdminChangePassword extends JPanel{
	
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 * Description: Custom constructor for the AdminDatabaseBrowser class
	 * Return Type: none
	 * Pre: None
	 * Post: Instantiates a new instance of the AdminDatabaseBrowser class.
	 *************************************************************************/	
	public AdminChangePassword(String user){
		currentUser = user;
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
		//Instantiate the new panel
		setLayout(new GridBagLayout());
		setBorder(new TitledBorder("Update Password"));
				
		myConstraints = new GridBagConstraints();
		
		myConstraints.gridx = 0;
		myConstraints.gridy = 0;
		
		//current password label
		add( new JLabel("Current Password:"), myConstraints);
		myConstraints.gridy++;
		add( new JLabel("New Password:"), myConstraints);
		myConstraints.gridy++;
		add( new JLabel("Confirm Password:"), myConstraints);
		
		myConstraints.gridx++;
		myConstraints.gridy = 0;
		myConstraints.gridwidth = 2;
		myConstraints.fill = GridBagConstraints.HORIZONTAL;
		myConstraints.weightx = 1;
		
		//old password text field
		oldPasswordText = new JPasswordField(25);
		add(oldPasswordText, myConstraints);
		myConstraints.gridy++;
		
		newPasswordText = new JPasswordField(25);
		add(newPasswordText, myConstraints);
		myConstraints.gridy++;
		
		//new password check text field
		newPasswordCheck = new JPasswordField(25);
		add(newPasswordCheck, myConstraints);
		
		//Add the updatePassword and cancelUpdate buttons.
		addUpdatePasswordButton();
		
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
		
		myConstraints.gridx = 1;
		myConstraints.gridy++;
		myConstraints.gridwidth = 1;
		myConstraints.weightx = 0;
		myConstraints.fill = GridBagConstraints.NONE;
		//Instantiate a the JButton addUpdatePasswordButton, and set the 
		//position and size
		updatePasswordButton = new JButton("Update Password");
				
		//Add an ActionListener to the button
		updatePasswordButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e){
				//If the button is pressed, check that the username and password
				//combination is valid, then see of the new passwords match, if 
				//both are true, update the password
				if(e.getActionCommand().equals("Update Password")){
					if(verifyCurrentPassword()){
						if(verifyNewPasswords()){
							AdminDatabaseAccess.changePassword(currentUser, newPasswordText.getText());
							JOptionPane.showMessageDialog(null, "Password successfully updated");
						}
					}
				}
			}
		});
		//Add the JButton to the panel
		add(updatePasswordButton, myConstraints);
		
	}
	
	
	/**************************************************************************
	 * Description: Verifies the current username & password combination
	 * Return Type: bool
	 * Pre: None
	 * Post: Returns true if the correct password was entered, false otherwise
	 *************************************************************************/
	@SuppressWarnings("deprecation")
	boolean verifyCurrentPassword(){
		boolean passwordIsGood = false;
		//If the username & password is not a valid combination, return false
		if(oldPasswordText.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Enter current password");
		}
		else if(AdminDatabaseAccess.validateUser(currentUser, oldPasswordText.getText())){
			passwordIsGood = true;
		}
		else{
			JOptionPane.showMessageDialog(null, "Current password is incorrect");
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
			JOptionPane.showMessageDialog(null, "New passwords must match");
			passwordsMatch = false;
		}
		//If the password field is empty, return false
		else if(newPasswordText.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Please enter a new password");
			passwordsMatch = false;
		}
		
		return passwordsMatch;
	}
	
	private GridBagConstraints myConstraints;
	
	private String currentUser;
	
	//JPasswordFields to allow safe entry of passwords
	private JPasswordField oldPasswordText;
	private JPasswordField newPasswordText;
	private JPasswordField newPasswordCheck;
	
	private JButton updatePasswordButton;

}