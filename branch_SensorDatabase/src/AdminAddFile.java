/******************************************************************************
 * Filename: AdminDatabaseBrowser.java
 * Author: Dylan Clohessy (Dylan_Clohessy@baylor.edu)
 * Description: A class that allows administrators to search the database or 
 * 				add content to it by selecting an input file
 * Created: 4/30/2015
 * Modified:4/30/2015
******************************************************************************/
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class AdminAddFile extends JPanel{
	
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 * Description: Custom constructor for the AdminDatabaseBrowser class
	 * Return Type: none
	 * Pre: None
	 * Post: Instantiates a new instance of the AdminDatabaseBrowser class.
	 *************************************************************************/	
	public AdminAddFile(){
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
		inputFileName = new String();
		chooseFile();
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
	
	
	
	//private data members for the AdminDataBaseBrowser class
	
	//String to hold the absolute path of an input file.
	private String inputFileName;
	
	//JFileChooser we use to browse files
	private JFileChooser fileChooser;
}