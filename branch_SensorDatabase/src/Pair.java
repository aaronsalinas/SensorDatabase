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
	private Key first;
	private Value second;
	
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
	
	/* *************************************
	 * 			Access Functions
	 ***************************************/
	public Key getFirst(){
		return this.first;
	}
	
	public Value getSecond(){
		return this.second;
	}
	
	/* *************************************
	 * 			Modifiers
	 ***************************************/
	public void setFirst(Key first){
		this.first = first;
	}
	
	public void setSecond(Value second){
		this.second = second;
	}
	
}
