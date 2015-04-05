import java.util.Map.Entry;



public class HuffmanNode implements Cloneable{
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
	
//	@Override
//	protected HuffmanNode clone() {
//		HuffmanNode clone = new HuffmanNode();
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
