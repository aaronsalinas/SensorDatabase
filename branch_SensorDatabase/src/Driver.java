/******************************************************************************
 * Filename: Driver.java
 * Author: Dylan Clohessy (Dylan_Clohessy@baylor.edu)
 * Description: The driver file for the project. 
 * Created: 4/28/2015
 * Modified:4/30/2015
******************************************************************************/
public class Driver{
	public static void main(String[] args){
		new Driver();
	}
	
	public Driver(){
		init();
	}
	
	private void init(){
		loginScreen = new Login();
	}
	
	private Login loginScreen;
}