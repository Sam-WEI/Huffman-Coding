import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map.Entry;


public class HuffmanProject5565 {
	
	static final int KEY_BYTE_NUM = 2;
	
	public static void main(String[] args) {
//		HashMap<Integer, Integer> freqMap = readFile("liberty.jpg");
		HashMap<Integer, Integer> freqMap = readFile("test.txt");
		int i = 0;
		for(Entry<Integer, Integer> entry : freqMap.entrySet()){
//			System.out.println(i + " - " + entry.getKey() + "  :  " + entry.getValue());
			System.out.format("%d - %c : %d", i, entry.getKey(), entry.getValue()).println();
			i++;
		}
		
		MinHeap minHeap = new MinHeap(freqMap);
		Entry<Integer, Integer> min = minHeap.extractMin();
		while(min != null){
			System.out.println("MIN = " + min.getValue());
			min = minHeap.extractMin();
		}
		
	}

	private static HashMap<Integer, Integer> readFile(String filename){
		HashMap<Integer, Integer> freqMap = new HashMap<>();
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(filename));
			int b = 0;
			while((b = bis.read()) != -1){
				Integer value = freqMap.get(b);
				if(value == null){
					freqMap.put(b, 1);
				} else {
					freqMap.put(b, value + 1);
				}
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(bis != null){
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return freqMap;
	}
	
}
