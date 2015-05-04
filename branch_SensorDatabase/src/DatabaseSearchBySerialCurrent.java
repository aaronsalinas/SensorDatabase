import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;


public class DatabaseSearchBySerialCurrent extends JPanel{
	
	private static final long serialVersionUID = 1L;

	public DatabaseSearchBySerialCurrent(){
		init();
	}
	
	private void init(){
		
		setLayout(new GridBagLayout());
		setBorder(new TitledBorder("Select Instrument/Serial"));
		allInstruments = new ArrayList<String>();
		allSerials = new ArrayList<String>();
		serialLabel = new JLabel("Serial #'s");
		
		myConstraints = new GridBagConstraints();
		addScrollPane();
		myConstraints.gridwidth = 1;
		myConstraints.gridx = 0;
		myConstraints.gridy = 0;
		myConstraints.weightx = .5;
		myConstraints.fill = GridBagConstraints.HORIZONTAL;
		
		initInstrumentDropDown();
	}
	
	private void initInstrumentDropDown(){
		add(new JLabel("Instrument List"), myConstraints);
		myConstraints.gridy++;
		setPreferredSize(new Dimension(600,600));
		for(String item : SensorDatabaseAccess.toListAllInstruments()){
			allInstruments.add(item);
		}

		//Initialize the "instrumentDropDown" JComboBox  
		instrumentDropDown = new JComboBox<String>();
		serialDropDown = new JComboBox<String>();

		//Populate the JComboBox with all members of the "instrumentList"
		for(int i = 0; i < allInstruments.size(); i++){
			instrumentDropDown.addItem(allInstruments.get(i));
		}
		
		add(instrumentDropDown,myConstraints);
		
		//Add an ActionListener to the "instrumentDropDown"
		instrumentDropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//Set "selectedInstrument" to the item selected by the user
				String selectedInstrument = (String) instrumentDropDown.getSelectedItem();
				//Initialize the "serialDropDown" JComboBox
				initSerialDropDown(selectedInstrument);
			}
		});
	}
	
	private void initSerialDropDown(String instrument){
		myConstraints.weightx = 0.5;
		myConstraints.gridy = 0;
		myConstraints.gridx = 1;
		add(serialLabel, myConstraints);
		myConstraints.gridy++;
		
		allSerials.clear();
		serialDropDown.removeAllItems();
		
		for(String item : SensorDatabaseAccess.toListSerialsByInstrument(instrument)){
			allSerials.add(item);
		}
		
		for(String item : allSerials){
			serialDropDown.addItem(item);
		}
		
		add(serialDropDown, myConstraints);
		
		revalidate();
		repaint();
		
		//Add an ActionListener to the "serialDropDown"
		serialDropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				//sets "selectedSerial" to the item selected by the user
				String selectedSerial = (String) serialDropDown.getSelectedItem();
				for(String item : SensorDatabaseAccess.toListADCPCurrentDataInstrSerial(instrument, selectedSerial)){
					outputDisplay.append(item);
				}
			}
		});
	}
	
	
	/**************************************************************************
	 * Description: Method called to instantiate the ScrollPane that will hold
	 *              our output
	 * Return Type: void
	 * Pre: None
	 * Post: Instantiates the JTextArea used to hold search results and binds
	 *       it to a ScrollPane which allows us to scroll though the data.
	 **************************************************************************/
	private void addScrollPane(){
		myConstraints.gridx = 0;
		myConstraints.gridy = 2;
		myConstraints.gridwidth = 2;
		myConstraints.weightx = 0.0;
		myConstraints.fill = GridBagConstraints.SOUTH;
		//Instantiate our JTextArea with a location, height and width and set 
		//it so it cannot be edited by the user.
		outputDisplay = new JTextArea();
		outputDisplay.setEditable(false);
		outputDisplay.setPreferredSize(new Dimension(580,500));
		
		//Instantiate our JScrollPane, setting the vertical and horizontal 
		//scroll bars to appear as needed, as well as setting the bounds
		scrollingDisplay = new JScrollPane(outputDisplay);
		scrollingDisplay.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollingDisplay.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//Add the JScrollPane to the panel
		add(scrollingDisplay,myConstraints);
	}
	
	private JTextArea outputDisplay;
	private JScrollPane scrollingDisplay;
	private JLabel serialLabel;
	private JComboBox<String> instrumentDropDown;
	private JComboBox<String> serialDropDown;
	
	private GridBagConstraints myConstraints;
	private ArrayList<String> allInstruments;
	private ArrayList<String> allSerials;
}