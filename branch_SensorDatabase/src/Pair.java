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
	private Key key;
	private Value value;
	
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
		this.key = key;
		this.value = value;
	}
	
	/* *************************************
	 * 			Access Functions
	 ***************************************/
	public Key getKey(){
		return this.key;
	}
	
	public Value getValue(){
		return this.value;
	}
	
	/* *************************************
	 * 			Modifiers
	 ***************************************/
	public void setKey(Key key){
		this.key = key;
	}
	
	public void setValue(Value value){
		this.value = value;
	}
	
}
