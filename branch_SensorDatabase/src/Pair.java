/******************************************************************************
 * Filename: Pair.java
 * Author: Aaron D. Salinas (Aaron_Salinas@baylor.edu)
 * Description: This class holds a structure of a standard Pair class, similar
 * functionality to that of the std::Pair in C++
 * Created: 4/28/2015
 * Modified:5/4/2015
******************************************************************************/
public class Pair<Key, Value>{
	
	/* *************************************
	 * 		Class Member Variables
	 ***************************************/
	public Key first;
	public Value second;
	
	/* *************************************
	 * 			Constructors
	 ***************************************/
	/**
	 * Default Constructor
	 * <p>
	 * Initializes Pair Class based on key and value data passed into function
	 * @param key
	 * @param value
	 */
	public Pair(Key first, Value second){
		this.first = first;
		this.second = second;
	}
	
	public String toString(){
		return first + " " + second;
	}

	
}
