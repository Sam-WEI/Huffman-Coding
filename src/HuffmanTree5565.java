import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


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
	
	private void buildHuffmanTree(){
		HuffmanNode5565 left = minHeap.extractMin();
		HuffmanNode5565 right = minHeap.extractMin();
		
		HuffmanNode5565 parent = null;
		
		while(left != null && right != null){
			HuffmanNode5565 l = getNodeFromTree(left);
			HuffmanNode5565 r = getNodeFromTree(right);
			
			parent = new HuffmanNode5565();
			parent.left = l;
			parent.right = r;
			parent.key = -1;
			parent.frequency = left.frequency + right.frequency;
			
			nodes.add(parent);
			parent.index = nodes.size() - 1;
			
			minHeap.insertIntoMinHeap(parent);
			
			left = minHeap.extractMin();
			right = minHeap.extractMin();
			
		}
		
		generateHuffmanCode(parent);
		
		printHuffmanNode();
//		printHuffmanCodes();
	}
	
	private void generateHuffmanCode(HuffmanNode5565 n) {
		if (n.left == null && n.right == null) {
			codes.put(n.key, n.code);
			bitCount += n.frequency * n.code.length();
		} else {
			if (n.left != null) {
				n.left.code = n.code + "0";
				generateHuffmanCode(n.left);
			}
			if (n.right != null) {
				n.right.code = n.code + "1";
				generateHuffmanCode(n.right);
			}
		}
	}
	
	public HashMap<Integer, String> getHCodeMap(){
		if(codes.isEmpty()){
			buildHuffmanTree();
		}
		return codes;
	}
	
	public long getTotalBitCount(){
		return bitCount;
	}
	
	private void printHuffmanNode(){
		for(HuffmanNode5565 n : nodes){
			if(n.left == null && n.right == null){
				System.out.printf("%d : %s [%d]\n", n.key, n.code, n.frequency);
			}
		}
	}
	
	private void printHuffmanCodes(){
		for(Entry<Integer, String> entry : codes.entrySet()){
			System.out.printf("%d : %s\n [%d]", entry.getKey(), entry.getValue());
		}
	}
	
	private HuffmanNode5565 getNodeFromTree(HuffmanNode5565 n){
		if(n.index == -1){
			nodes.add(n);
			n.index = nodes.size() - 1;
			return n;
		} else {
			return nodes.get(n.index);
		}
	}
	
}
