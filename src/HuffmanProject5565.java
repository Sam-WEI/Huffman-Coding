import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 
 * @author Shengkun Wei
 *
 */
public class HuffmanProject5565 {
	
	private static final String HUF_EXTENSION = ".huf";
	static final int KEY_BYTE_NUM = 2;
	private static final int BUFFER_NUM = 30;
	
	
	public static void main(String[] args) {
		
//		int i = 0;
//		for(Entry<Integer, Integer> entry : freqMap.entrySet()){
//			System.out.format("%3d - %c : %d", i, entry.getKey(), entry.getValue()).println();
//			i++;
//		}
		String filename = "test.txt";
//		String filename = "liberty.jpg";
//		String filename = "easy.txt";
//		String filename = "project1 610s15.pdf";
		
		encodeFile(filename);
		decodeFile(filename + HUF_EXTENSION);
		
		
	}

	private static void encodeFile(String filename){
		HashMap<Integer, Integer> freqMap = readFrequenciesFromFile(filename);
		MinHeap minHeap = new MinHeap(freqMap);
		
		HuffmanTree tree = new HuffmanTree(minHeap);
		tree.buildHuffmanTree();
		
		HashMap<Integer, String> hCodesMap = tree.getHCodeMap();
		
		final int BYTE_NEEDED_FOR_HCODE = getByteNeededForHCodes(hCodesMap);
		
		System.out.println("~~~~~byte needed for hcode maps " + BYTE_NEEDED_FOR_HCODE);

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(filename + ".huf"));

			//data structure is: {byte number of all hcode|1st code's key|1st code's bit number N|1st code's code[need ceil(N/8) bytes]|2nd code's key|...}
			//use the first 2 bytes to save the byte number of hcode
			bos.write(BYTE_NEEDED_FOR_HCODE>>8);
			bos.write(BYTE_NEEDED_FOR_HCODE);
			/* Suppose there are 256 (at most) huffman codes, each needs 1 byte for key, 
			 * 1 byte for bit count, and 2 bytes for code. Only 4*256=1024 bytes are needed.
			 * So 2 bytes(=65536) for storing this count is enough.*/
			
			String codeInStr = null;
			
			//write huffman code maps
			int byteUsed = 0;
			for(Entry<Integer, String> entry : hCodesMap.entrySet()){
				bos.write(entry.getKey());
				byteUsed++;
				codeInStr = entry.getValue();
//				codeInStr="000000000"+codeInStr;
				
				int codeInInt = getIntFromBinaryString(codeInStr, false);//code in int
				int codeBitsCount = codeInStr.length();
				final int BYTE_COUNT = (int) Math.ceil(codeBitsCount / 8f);
				
				bos.write(codeBitsCount);
				if(BYTE_COUNT == 1){
					//if only has 1 byte then write it immediately
					bos.write(codeInInt);
					byteUsed++;
				} else {
					for(int i = 0; i < BYTE_COUNT; i++){
						bos.write(codeInInt);
						byteUsed++;
						codeInInt >>= (8 * (i+1));//NOTE that I'm writing the bytes in reverse order, which means for code 1111111100000000 I first write 00000000 then 11111111
						
						/*if(codeInInt == 0){
							//if the prior bytes are 0, then we don't need to write it. Just go to write bit count.
							break;
						}
						System.out.println("2+>>>> " + codeInStr + "  " + (codeInInt));*/
					}
				}
				
				byteUsed++;
				
			}
			System.out.println("~~~~byte used for hcode maps " + byteUsed);
			
			//encode file
			bis = new BufferedInputStream(new FileInputStream(filename));
			int b = 0;
			StringBuilder sb = new StringBuilder();
			while((b = bis.read()) != -1){
				sb.append(hCodesMap.get(b));//TODO need to be memory optimized
				
			}
			final int totalBits = sb.length();
			int pos = 0;
			String oneByteStr = null;
			while(pos < totalBits){
				oneByteStr = sb.substring(pos, pos + Math.min(totalBits - pos, 8));
				System.out.print(oneByteStr+"|");
				int oneByte = getIntFromBinaryString(oneByteStr, true);
				bos.write(oneByte);
				pos += 8;
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
	}
	
	private static int getByteNeededForHCodes(HashMap<Integer, String> hCodes){
		int byteCount = 0;
		String code = null;
		for(Entry<Integer, String> e : hCodes.entrySet()){
			byteCount++;//1 byte for key
			byteCount++;//1 byte for bit count
			code = e.getValue();
			int count = (int) Math.ceil(code.length() / 8f);
			byteCount += count;
		}
		
		return byteCount;
	}
	
	private static void decodeFile(String filename){
		HashMap<Integer, String> hCodesMap = new HashMap<>();
		String decodedFilename = filename + "." + getOriginalFileExtension(filename);
		
		
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		
		try {
			bis = new BufferedInputStream(new FileInputStream(filename));
			bos = new BufferedOutputStream(new FileOutputStream(decodedFilename));
			
			int byteCountForHCodes = (bis.read() << 8) + bis.read();
			System.out.println("=== byte count for h code is " + byteCountForHCodes);
			
			int shortestCodeLen = Integer.MAX_VALUE;
			StringBuilder bufferString = new StringBuilder();
			
			int byteRead = 0;//byte count that has already read
			int b;
			while((b = bis.read()) != -1){
				byteRead++;
				if(byteRead <= byteCountForHCodes){
					final int KEY = b;
					String codeInStrTemp = null;
					final int CODE_BIT_COUNT = bis.read();
					byteRead++;
					
					int codeInInt = 0;
					final int CODE_BYTE_COUNT = (int)Math.ceil(CODE_BIT_COUNT / 8f);
					for(int i = 0; i < CODE_BYTE_COUNT; i++){
						codeInInt += (bis.read()<<(i*8)); //remember that I wrote the bytes of this code in reverse order. So read likewise.
						byteRead++;
					}
					
					codeInStrTemp = Integer.toBinaryString(codeInInt);
					StringBuilder codeInStr = new StringBuilder();
					for(int j = 0; j < CODE_BIT_COUNT - codeInStrTemp.length(); j++){
						codeInStr.append("0");
					}
					codeInStr.append(codeInStrTemp);
					
					shortestCodeLen = Math.min(shortestCodeLen, codeInStr.length());//in order to faster the later comparison when decoding
					
					hCodesMap.put(KEY, codeInStr.toString());
				} else {
					//begin to decode
					if(true||bufferString.length() < BUFFER_NUM){
						bufferString.append(get8BitsBinaryStrFromInt(b));
					} else {
						while(bufferString.length() > 0){
							int cutPoint = 0;
							for(int i = shortestCodeLen; i < bufferString.length(); i++){
								String strToCompare = bufferString.substring(0, i);
								for(Entry<Integer, String> entry : hCodesMap.entrySet()){
									if(strToCompare.equals(entry.getValue())){
										bos.write(entry.getKey());
										break;
									}
								}
								cutPoint = i;
								break;
							}
							bufferString = new StringBuilder(bufferString.substring(cutPoint));
						}
					}
				}
				
				
				
			}
			int startIndex = 0;
			int endIndex = shortestCodeLen;
			System.out.print(bufferString+"|");
			while(endIndex < bufferString.length()){
				String strToCompare = bufferString.substring(startIndex, endIndex);
//				System.out.println("~~"+strToCompare+ "  " + hCodesMap);
				for(Entry<Integer, String> entry : hCodesMap.entrySet()){
					if(strToCompare.equals(entry.getValue())){
						bos.write(entry.getKey());
						startIndex = endIndex;
						endIndex = startIndex + shortestCodeLen - 1;//-1 is because of the later endIndex++
						System.out.println("got "+strToCompare+"  "+entry.getKey());
						break;
					}
				}
				endIndex++;
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
	}
	
	private static String getOriginalFileExtension(String encodedFilename){
		String[] ss = encodedFilename.split("\\.");
		if(ss.length >= 2){
			return ss[ss.length - 2];
		}
		return null;
	}
	
	private static String get8BitsBinaryStrFromInt(int integer){
		String tmp = Integer.toBinaryString(integer);
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 8 - tmp.length(); i++){
			sb.append("0");
		}
		sb.append(tmp);
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @param str
	 * @param moveBitsToLeft If true, binary string 111 will be converted to 11100000, which is useful when decoding the last byte of file.
	 * <br/>If false, binary string 111 will be converted to 00000111, which should be used when writing hcode maps.
	 * @return
	 */
	private static int getIntFromBinaryString(String str, boolean moveBitsToLeft){
		final int len = str.length();
		
		int i = 0;
		int result = 0;
		char c;
		while(i < len){
			c = str.charAt(i);
			result <<= 1;
			result += c - '0';
			i++;
		}
		
		if(moveBitsToLeft){
			result <<= 8 - len;//when encoding file, if len < 8, this byte must be the last one. We must put all the bits to the very left or it won't be decoded correctly.
		}
		
		return result;
	}
	
	private static HashMap<Integer, Integer> readFrequenciesFromFile(String filename){
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
