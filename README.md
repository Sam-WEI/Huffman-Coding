# Huffman Encoder and Decoder

This program can compress files using `Huffman Coding Algorithm` and unzip compressed files.

Multiple files can be encoding and decoding simultaneously in seperate threads.

There is no upper limit of file size.

## Result

- `New_Jersey_Driver_Manual.pdf` 4.36 MB (4,577,231 bytes)   
  __After compression:__   
  `New_Jersey_Driver_Manual.pdf.huf` 3.93 MB (4,121,965 bytes)   
  Compression ratio: 90%  

- `longtext.txt` 502 KB (514,353 bytes)   
  __After compression:__   
  `longtext.txt.huf` 343 KB (351,641 bytes)   
  Compression ratio: 68%  

- `city.jpg` 8.58 MB (8,998,088 bytes)   
  __After compression:__   
  `city.jpg.huf` 8.57 MB (8,995,371 bytes)  
  Compression ratio: 99.8% 

