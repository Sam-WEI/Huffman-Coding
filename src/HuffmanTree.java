import java.util.ArrayList;
import java.util.Map.Entry;


public class HuffmanTree {
	
	private final MinHeap minHeap;
	private ArrayList<HuffmanNode> nodes;
	
	public HuffmanTree(MinHeap minHeap){
		this.minHeap = minHeap;
	}
	
	public void buildHuffmanTree(){
		Entry<Integer, Integer> e1 = minHeap.extractMin();
		Entry<Integer, Integer> e2 = minHeap.extractMin();
		if(e1 == null || e2 == null){
			return;
		}
		
		HuffmanNode left;
		HuffmanNode right;
		HuffmanNode parent;
		while(e1 != null && e2 != null){
			left = new HuffmanNode(e1);
			right = new HuffmanNode(e2);
			parent = new HuffmanNode();
			parent.leftChild = left;
			parent.rightChild = right;
			parent.key = -1;
			parent.value = left.value + right.value;
			
		}
	}
	
	public static class HuffmanNode{
		int code;
		int key;
		int value;
		HuffmanNode leftChild;
		HuffmanNode rightChild;
		HuffmanNode parent;
		
		public HuffmanNode() { }

		HuffmanNode(Entry<Integer, Integer> entry){
			key = entry.getKey();
			value = entry.getValue();
		}
	}
}
