/**
 * Standard templated Pair class used to store a pair of data
 * @author aaronsalinas
 *
 * @param <Key>
 * @param <Value>
 */
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
