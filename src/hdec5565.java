import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;


public class hdec5565 {

	private static final int BUFFER_NUM = 20;
	private static final int ONE_MEGABYTE = 1 * 1024 * 1024 * 8;//in bit
	private static long startTime;
	
	public static void main(String[] args) {
		if (args != null && args.length == 1){
			String filename = args[0];
			decodeFile(filename);
		} else {
			System.out.println("Input error!");
		}
		
	}
	private static void decodeFile(String filename){
		System.out.println("\n\nDecoding starts...\n");
		startTime = System.currentTimeMillis();
		HashMap<Integer, String> hCodesMap = new HashMap<>();
		String decodedFilename = filename + "." + Toolbox.getOriginalFileExtension(filename);
		
		long bitCountOfFile = 0;
		
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		
		try {
			bis = new BufferedInputStream(new FileInputStream(filename));
			bos = new BufferedOutputStream(new FileOutputStream(decodedFilename));
			
			final int byteCountForHCodes = (bis.read() << 8) + bis.read();
			
			System.out.println("byte count for h code is " + byteCountForHCodes);
			
			int shortestCodeLen = Integer.MAX_VALUE;
			
			int byteRead = 0;//byte count that has already read
			int b;
			//----------start to read hcode map-------------
			while(byteRead < byteCountForHCodes){
				b = bis.read();
				byteRead++;
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
			}
			
			System.out.println("just read hcode map: " + hCodesMap);
			
			
			//-------------start to read 8 bytes for bit count-------------
			for(byteRead = 0; byteRead < 8; byteRead++){
				bitCountOfFile += (bis.read() << (7 - byteRead) * 8);
			}
			System.out.println("just read bit count: " + bitCountOfFile);
			
			
			//-------------start to decode file-------------
			StringBuilder bufferString = new StringBuilder();
			boolean bufferTooShort = false;
			int bitRead = 0;
			b = 0;
			int mbRead = -1;
			final float totalMBOfFile = (float)bitCountOfFile / ONE_MEGABYTE;
			
			while(bitRead < bitCountOfFile){
				if((bufferString.length() < BUFFER_NUM && bufferTooShort) && b != -1){
					b = bis.read();

					if(b != -1){
						bufferString.append(Toolbox.get8BitsBinaryStrFromInt(b));
					}
//					System.out.print("reading --- " + bufferString+"|" + bufferString.length() + "\n");
				} else {
					int startIndex = 0;
					int endIndex = shortestCodeLen;
//					System.out.print("else --- " + bufferString+"|" + bufferString.length() + "\n");
					bufferTooShort = false;
					while(bitRead < bitCountOfFile && endIndex <= bufferString.length() && !bufferTooShort){
						String strToCompare = bufferString.substring(startIndex, endIndex);
						boolean matched = false;
//						System.out.println("else's while");
						for(Entry<Integer, String> entry : hCodesMap.entrySet()){
							if(strToCompare.equals(entry.getValue())){
								bos.write(entry.getKey());
								bitRead += entry.getValue().length();
//								System.out.println("bitRead " + bitRead + "  " + strToCompare);
								if(bitRead / ONE_MEGABYTE != mbRead){
									mbRead = bitRead / ONE_MEGABYTE;
									System.out.printf("%d MB/%.2f MB have been decoded... ", mbRead, totalMBOfFile);
									printCurrentTimeSpent();
								}
								
								startIndex = endIndex;
								endIndex = startIndex + shortestCodeLen;
								
								matched = true;
								break;
							}
						}
						if(!matched){
							endIndex++;
						}
						
						if(endIndex > bufferString.length()){
							bufferString = new StringBuilder(bufferString.substring(startIndex));//put the remaining string into new string
							bufferTooShort = true;
						}
					}
					bufferTooShort = true;
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
		
		printCurrentTimeSpent();
		System.out.println("Decoding finished.");
	}
	
	private static void printCurrentTimeSpent(){
		System.out.println("elapsed time: " + ((System.currentTimeMillis() - startTime)));
	}
}
