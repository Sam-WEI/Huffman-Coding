import java.util.ArrayList;
import java.util.Map.Entry;


public class HuffmanTree {
	
	private final MinHeap minHeap;
	private ArrayList<HuffmanNode> nodes;
	
	public HuffmanTree(MinHeap minHeap){
		this.minHeap = minHeap;
		nodes = new ArrayList<>();
	}
	
	public void buildHuffmanTree(){
		HuffmanNode left = minHeap.extractMin();
		HuffmanNode right = minHeap.extractMin();
		
		HuffmanNode parent = null;
		
		while(left != null && right != null){
			HuffmanNode l = getNodeFromTree(left);
			HuffmanNode r = getNodeFromTree(right);
			
			parent = new HuffmanNode();
			parent.left = l;
			parent.right = r;
			parent.key = -1;
			parent.value = left.value + right.value;
			
			nodes.add(parent);
			parent.index = nodes.size() - 1;
			
			minHeap.insertIntoMinHeap(parent);
			
			left = minHeap.extractMin();
			right = minHeap.extractMin();
			
			System.out.println("buiding....");
		}
		
		generateHuffmanCode(parent);
		printHuffmanNode();
		
	}
	
	private void generateHuffmanCode(HuffmanNode n){
		if(n.left != null){
			n.left.code = n.code + "0";
			
			generateHuffmanCode(n.left);
		}
		if(n.right != null){
			n.right.code = n.code + "1";
			System.out.println("!!!!!!!! " + n.left.code);
			generateHuffmanCode(n.right);
		}
		
	}
	
	private void printHuffmanNode(){
		HuffmanNode tmp;
		for(HuffmanNode n : nodes){
			System.out.format("[%d] %c : %10s : %d", n.index, (n.key == -1? '-':n.key), n.code, n.value);
			System.out.print("  (left: ");
			tmp = n.left;
			if(tmp != null){
//				System.out.format("%c", (tmp.key == -1? '-':tmp.key));
				System.out.print(tmp.value);
			} else {
				System.out.print("null");
			}
			System.out.print(", right: ");
			tmp = n.right;
			if(tmp != null){
//				System.out.format("%c", (tmp.key == -1? '-':tmp.key));
				System.out.print(tmp.value);
			} else {
				System.out.print("null");
			}
			
			System.out.print(", parent: ");
			tmp = n.parent;
			if(tmp != null){
				System.out.format("%c", (tmp.key == -1? '-':tmp.key));
			} else {
				System.out.print("null");
			}
			System.out.print(")\n");
		}
	}
	
	private HuffmanNode getNodeFromTree(HuffmanNode n){
		if(n.index == -1){
			nodes.add(n);
			n.index = nodes.size() - 1;
			return n;
		} else {
			return nodes.get(n.index);
		}
	}
	
}
