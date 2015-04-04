import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;



public class MinHeap {
	
	private HashMap<Integer, Integer> freqMap;
	private ArrayList<HuffmanNode> tree;
	public MinHeap(HashMap<Integer, Integer> freqMap) {
		this.freqMap = freqMap;
		tree = new ArrayList<>(freqMap.size());
		for(Entry<Integer, Integer> e : freqMap.entrySet()){
			tree.add(new HuffmanNode(e));
		}
		
		buildMinHeapTree();
		
	}
	
	private void buildMinHeapTree(){
		boolean onceMore = false;
		do{
			onceMore = false;
			for(int childIdx = tree.size() - 1; childIdx >= 1; childIdx--){
				
				int prtIdx = (childIdx - 1) / 2;//same as Math.floor()
				HuffmanNode childEntry = tree.get(childIdx);
				HuffmanNode prtEntry = tree.get(prtIdx);
				if(childEntry.value < prtEntry.value){
					tree.set(childIdx, prtEntry);
					tree.set(prtIdx, childEntry);
					onceMore = true;
				}
			}	
		}while(onceMore);
		printTree();
	}
	
	public void insertIntoMinHeap(HuffmanNode node){
		tree.add(node);
		buildMinHeapTree();
	}
	
	public HuffmanNode extractMin(){
		if(!tree.isEmpty()){
			HuffmanNode result = tree.get(0);
			HuffmanNode last = tree.remove(tree.size() - 1);
			if(tree.size() > 0){
				tree.set(0, last);
			}
			buildMinHeapTree();
			return result;
		}
		return null;
	}
	
	private void printTree(){
		System.out.println("\n------START------");
		final int size = tree.size();
		final int levelNum = (int) Math.ceil(Math.log(size));
		
		int lvl = 0;
		
		for(int i = 0; i < size; i++){
			final int nodeNumInThisLevel = (int) Math.pow(2, lvl);
			int lineOffset = (int) (Math.pow(2, levelNum - lvl - 1)); 
			
			System.out.format("%4d", tree.get(i).value).print("");
			
			
			int spaceAfterEachNode = 2 * lineOffset + 1;
			for(int space = 0; space < spaceAfterEachNode; space++) 
				System.out.print(" ");
			
			if(i == 2 * nodeNumInThisLevel - 2){
				//reached the end of this level
				System.out.println();
				
				for(int space = 0; space < lineOffset; space++) 
					System.out.print(" ");
				lvl++;
			}
		}
		
		System.out.println("\n=======END=======\n");
	}
	
	private class MinHeapNode {
		int key;
		int value;
		int huffmanTreeIndex;
	}
}
