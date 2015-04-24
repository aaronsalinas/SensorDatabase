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
	public Pair(Key key, Value value){
		this.first = key;
		this.second = value;
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
	public void setKey(Key key){
		this.first = key;
	}
	
	public void setValue(Value value){
		this.second = value;
	}
	
}
