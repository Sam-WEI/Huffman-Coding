import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


/**
 * 
 * @author Shengkun Wei
 *
 */
public class MinHeap5565 {
	
	private HashMap<Integer, Integer> freqMap;
	private ArrayList<HuffmanNode5565> tree;
	final int KEY_COUNT;
	
	public MinHeap5565(HashMap<Integer, Integer> freqMap) {
		this.freqMap = freqMap;
		KEY_COUNT = freqMap.size();
		tree = new ArrayList<>(KEY_COUNT);
		for(Entry<Integer, Integer> e : freqMap.entrySet()){
			tree.add(new HuffmanNode5565(e));
		}
		
		buildMinHeapTree();
	}
	
	private void buildMinHeapTree(){
		/*boolean onceMore = false;
		do{
			onceMore = false;
			for(int childIdx = tree.size() - 1; childIdx >= 1; childIdx--){
				
				int prtIdx = (childIdx - 1) / 2;//same as Math.floor()
				HuffmanNode5565 childEntry = tree.get(childIdx);
				HuffmanNode5565 prtEntry = tree.get(prtIdx);
				if(childEntry.frequency < prtEntry.frequency){
					tree.set(childIdx, prtEntry);
					tree.set(prtIdx, childEntry);
					onceMore = true;
				}
			}	
		}while(onceMore);*/
		
		for(int prtIdx = (tree.size() - 2) / 2; prtIdx >= 0; prtIdx--){
			downHeap(prtIdx);
		}	
	}
	
	private void downHeap(int prtIndex){
		final int leftCIdx = 2 * prtIndex + 1;
		final int rightCIdx = 2 * prtIndex + 2;
		final int leftC = (leftCIdx < tree.size() ? tree.get(leftCIdx).frequency : Integer.MAX_VALUE);
		final int rightC = (rightCIdx < tree.size() ? tree.get(rightCIdx).frequency : Integer.MAX_VALUE);
		final int smallest;
		final int smallestIdx;
		if(leftC <= rightC){
			smallest = leftC;
			smallestIdx = leftCIdx;
		} else {
			smallest = rightC;
			smallestIdx = rightCIdx;
		}
		if(tree.get(prtIndex).frequency > smallest){
			HuffmanNode5565 prt = tree.get(prtIndex);
			tree.set(prtIndex, tree.get(smallestIdx));
			tree.set(smallestIdx, prt);
			downHeap(smallestIdx);
		}
	}
	
	public void insertIntoMinHeap(HuffmanNode5565 node){
		tree.add(node);
		buildMinHeapTree();
	}
	
	public HuffmanNode5565 extractMin(){
		if(!tree.isEmpty()){
			HuffmanNode5565 result = tree.get(0);
			HuffmanNode5565 last = tree.remove(tree.size() - 1);
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
			
			System.out.format("%4d", tree.get(i).frequency).print("");
			
			
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
