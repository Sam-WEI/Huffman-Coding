//Wei, Shengkun   cs610 PP 5565

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 
 * @author Wei
 *
 */
public class Toolbox5565 {
	public static int getByteNeededForHCodes5565(HashMap<Integer, String> hCodes){
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
	
	public static String getOriginalFileExtension5565(String encodedFilename){
		String[] ss = encodedFilename.split("\\.");
		if(ss.length >= 2){
			return ss[ss.length - 2];
		}
		return null;
	}
	
	public static String get8BitsBinaryStrFromInt5565(int integer){
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
	public static int getIntFromBinaryString5565(String str, boolean moveBitsToLeft){
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
	
	public static HashMap<Integer, Integer> readFrequenciesFromFile5565(String filename){
		System.out.println("[" + filename + "] Reading frequencies from file...");
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
