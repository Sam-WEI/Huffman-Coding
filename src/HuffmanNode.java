//Wei, Shengkun   cs610 PP 5565

import java.util.Map.Entry;


/**
 * 
 * @author Shengkun Wei
 *
 */
public class HuffmanNode{
	String code = "";
	int key;
	int frequency;
	int index = -1;
	HuffmanNode left;
	HuffmanNode right;
	HuffmanNode parent;
	
	public HuffmanNode() { }

	public HuffmanNode(Entry<Integer, Integer> entry){
		key = entry.getKey();
		frequency = entry.getValue();
	}
	
}
