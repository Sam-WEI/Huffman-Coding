//Wei, Shengkun   cs610 PP 5565

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 
 * @author Shengkun Wei
 *
 */
public class henc5565 {
	
	private static final String HUF_EXTENSION = ".huf";
	static final int KEY_BYTE_NUM = 2;
	
	private static long startTime;
	
	
	public static void main(String[] args) {
		if (args != null && args.length == 1){
			String filename = args[0];
			encodeFile5565(filename);
		} else {
			System.out.println("Input error!");
		}
		
	}

	private static void encodeFile5565(String filename){
		System.out.println("Encoding starts...\n");
		startTime = System.currentTimeMillis();
		HashMap<Integer, Integer> freqMap = Toolbox5565.readFrequenciesFromFile5565(filename);
		
		if(freqMap.size() == 0){
			System.out.println("Empty file or file does not exist!");
			return;
		}
		
		MinHeap5565 minHeap = new MinHeap5565(freqMap);
		
		System.out.println("Generating huffman tree...");
		HuffmanTree5565 tree = new HuffmanTree5565(minHeap);
		HashMap<Integer, String> hCodesMap = tree.getHCodeMap5565();
		printCurrentTimeSpent5565();
		
		final int BYTE_NEEDED_FOR_HCODE = Toolbox5565.getByteNeededForHCodes5565(hCodesMap);
		
		System.out.println("Byte needed for saving hcode maps: " + BYTE_NEEDED_FOR_HCODE);

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(filename + HUF_EXTENSION));

			//data structure of the part of hcode map is: {byte number of all hcode|1st code's key|1st code's bit number N|1st code's code[need ceil(N/8) bytes]|2nd code's key|...}
			//use the first 2 bytes to save the byte number of hcode
			bos.write(BYTE_NEEDED_FOR_HCODE>>8);
			bos.write(BYTE_NEEDED_FOR_HCODE);
			/* Suppose there are 256 (at most) huffman codes, each of which needs 1 byte for key, 
			 * 1 byte for bit count, and 2 bytes for code. Only 4*256=1024 bytes are needed.
			 * So 2 bytes(=65536) for storing this count is enough.*/
			
			String codeInStr = null;
			
			/*-----------write huffman code maps---------------*/
			//data structure of the part of encoded file is: 
			int byteUsed = 0;
			for(Entry<Integer, String> entry : hCodesMap.entrySet()){
				bos.write(entry.getKey());
				byteUsed++;
				codeInStr = entry.getValue();
				
				int codeInInt = Toolbox5565.getIntFromBinaryString5565(codeInStr, false);//code in int
				int codeBitsCount = codeInStr.length();
				final int BYTE_COUNT = (int) Math.ceil(codeBitsCount / 8f);
				
				bos.write(codeBitsCount);
				byteUsed++;
				if(BYTE_COUNT == 1){
					//if only has 1 byte then write it immediately
					bos.write(codeInInt);
					byteUsed++;
				} else {
					for(int i = 0; i < BYTE_COUNT; i++){
						bos.write(codeInInt >> (8 * i));//NOTE that I'm writing the bytes in reverse order, which means for code 1111111100000000 I first write 00000000 then 11111111
						byteUsed++;
						
						/*if(codeInInt == 0){
							//if the prior bytes are 0, then we don't need to write it. Just go to write bit count.
							break;
						}
						System.out.println("2+>>>> " + codeInStr + "  " + (codeInInt));*/
					}
				}
				
			}
			System.out.println("Byte used for saving hcode maps: " + byteUsed);
			
			/*---------------encode file---------------*/
			bis = new BufferedInputStream(new FileInputStream(filename));
			
			//first write total bit count, which needs 8 bytes. 4 bytes are probably not enough since 32 bits have an upper limit 512M.
			final long totalBitCount = tree.getTotalBitCount5565();
			System.out.println("bit written: " + totalBitCount);
			for(int i = 0; i < 8; i++){
				bos.write((int) (totalBitCount >> (7 - i) * 8));
			}
			
			int b = 0;
			StringBuilder sb = new StringBuilder();
			
			final int bufferSize = 8 * 8;
			boolean bufferTooShort = true;
			int bitWritten = 0;
			int bitRead = 0;
			String oneByteStr = null;
			while(bitWritten < totalBitCount){
				if(bufferTooShort && sb.length() < bufferSize && b != -1){
					if((b = bis.read()) != -1){
						String code = hCodesMap.get(b);
						sb.append(code);
						bitRead += code.length();
					}
				} else {
					bufferTooShort = false;
					int pos = 0;
					final int len = sb.length();
//					System.out.println("else");
					while(bitWritten <= bitRead){
						int nextLen = Math.min(len - pos, 8);
						if(nextLen == 8 || (nextLen < 8 && bitWritten + nextLen == totalBitCount && nextLen != 0)){
							//if remaining at least one byte or reaching the end
							oneByteStr = sb.substring(pos, pos + nextLen);
							bitWritten += nextLen;
							
							int oneByte = Toolbox5565.getIntFromBinaryString5565(oneByteStr, true);
							bos.write(oneByte);
							pos += nextLen;
						} else {
							sb = new StringBuilder(sb.substring(pos));
							bufferTooShort = true;
							break;
						}
					}
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
			if(bos != null){
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("Encoding Finished.\n");
	}
	

	

	

	
	private static void printCurrentTimeSpent5565(){
		System.out.println("elapsed time: " + ((System.currentTimeMillis() - startTime)));
	}
}
