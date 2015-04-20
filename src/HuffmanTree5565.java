import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

//Wei, Shengkun   cs610 PP 5565

/**
 * 
 * @author Shengkun Wei
 *
 */
public class HuffmanTree5565 {
	
	private final MinHeap5565 minHeap;
	private ArrayList<HuffmanNode5565> nodes;
	private HashMap<Integer, String> codes;
	private long bitCount;//the total bit count. sum of key_bit * frequency
	
	public HuffmanTree5565(MinHeap5565 minHeap){
		this.minHeap = minHeap;
		nodes = new ArrayList<>();
		codes = new HashMap<>(minHeap.KEY_COUNT);
	}
	
	private void buildHuffmanTree5565(){
		HuffmanNode5565 left = minHeap.extractMin5565();
		HuffmanNode5565 right = minHeap.extractMin5565();
		
		HuffmanNode5565 parent = null;
		
		//when there is only one node
		if(left != null && right == null){
			parent = new HuffmanNode5565();
			parent.frequency = left.frequency;
			parent.left = left;
			parent.key = -1;
		} else {
			while(left != null && right != null){
				HuffmanNode5565 l = getNodeFromTree5565(left);
				HuffmanNode5565 r = getNodeFromTree5565(right);
				
				parent = new HuffmanNode5565();
				parent.left = l;
				parent.right = r;
				parent.key = -1;
				parent.frequency = left.frequency + right.frequency;
				
				nodes.add(parent);
				parent.index = nodes.size() - 1;
				
				minHeap.insertIntoMinHeap5565(parent);
				
				left = minHeap.extractMin5565();
				right = minHeap.extractMin5565();
			}
		}
		
		
		generateHuffmanCode5565(parent);
		
		printHuffmanNode5565();
//		printHuffmanCodes();
	}
	
	private void generateHuffmanCode5565(HuffmanNode5565 n) {
		if (n.left == null && n.right == null) {
			codes.put(n.key, n.code);
			bitCount += n.frequency * n.code.length();
		} else {
			if (n.left != null) {
				n.left.code = n.code + "0";
				generateHuffmanCode5565(n.left);
			}
			if (n.right != null) {
				n.right.code = n.code + "1";
				generateHuffmanCode5565(n.right);
			}
		}
	}
	
	public HashMap<Integer, String> getHCodeMap5565(){
		if(codes.isEmpty()){
			buildHuffmanTree5565();
		}
		return codes;
	}
	
	public long getTotalBitCount5565(){
		return bitCount;
	}
	
	private void printHuffmanNode5565(){
		for(HuffmanNode5565 n : nodes){
			if(n.left == null && n.right == null){
				System.out.printf("%d : %s [%d]\n", n.key, n.code, n.frequency);
			}
		}
	}
	
	private HuffmanNode5565 getNodeFromTree5565(HuffmanNode5565 n){
		if(n.index == -1){
			nodes.add(n);
			n.index = nodes.size() - 1;
			return n;
		} else {
			return nodes.get(n.index);
		}
	}
	
}
