import java.util.Map.Entry;


/**
 * 
 * @author Shengkun Wei
 *
 */
public class HuffmanNode5565{
	String code = "";
	int key;
	int frequency;
	int index = -1;
	HuffmanNode5565 left;
	HuffmanNode5565 right;
	HuffmanNode5565 parent;
	
	public HuffmanNode5565() { }

	public HuffmanNode5565(Entry<Integer, Integer> entry){
		key = entry.getKey();
		frequency = entry.getValue();
	}
	
//	@Override
//	protected HuffmanNode5565 clone() {
//		HuffmanNode5565 clone = new HuffmanNode5565();
//		clone.code = code;
//		clone.key = key;
//		clone.value = frequency;
//		clone.index = index;
//		clone.leftChild = left.clone();
//		clone.rightChild = right.clone();
//		clone.parent = parent.clone();
//		
//		return clone;
//	}
}
