ANALYSIS:
As expected the amount of time to compress these files seems to increase as the size of the original file increases. This makes sense as to compress the file, my program must first read the entire file from start to finish. Another observation I did want to look for is compression times between HTML vs TXT. The closest file size to test between is 61563 from quotes.htm and 82140 from melville.txt. The time for both these compressions (0.135 and 0.152) are staggeringly close meaning even with the resonable size difference there doesn't seem to be one format that is harder to compress than the other. Another very intresting trend I found is that "rehuffing" files will always take almost the same(slightly less) amount of time as the original huffing of the file. This makes sense as while the huffed file may have less bits the program still has to construct a working huffman tree to fit such a compressed file. This leads me to believe that the process that takes up the most amount of time to compress a file would be the process of building bit inventories and creating the huffman tree itself. I was very supresing to see how fast the calgary file ran compared to the BooksAndHTML one however even with these files having decently large sizes the time to compress them is significantly faster leading me to believe there is a greater amount of repeated characters in these files. Finally, the waterloo file, unsurprisingly took the most amount of time out of any directory. The TIF picture formats had consitantly larger sizes than the files contained in BooksAndHTML and Calgary leading to much larger initial readings of the file and larger average compression times. I imagine that other picture formats will have similar large file sizes and provide similarly slower compression.



BENCHMARK RAW RESULTS:
*************************************

BooksAndHTML: (This includes rehuffing files)
**************************************************
compressing to: D:\JavaProjects\a10\BooksAndHTML\A7_Recursion.html.hf
A7_Recursion.html from	 41163 to	 26189 in	 0.119
compressing to: D:\JavaProjects\a10\BooksAndHTML\A7_Recursion.html.hf.hf
A7_Recursion.html.hf from	 26189 to	 26340 in	 0.116
compressing to: D:\JavaProjects\a10\BooksAndHTML\CiaFactBook2000.txt.hf
CiaFactBook2000.txt from	 3497369 to	 2260664 in	 7.544
compressing to: D:\JavaProjects\a10\BooksAndHTML\CiaFactBook2000.txt.hf.hf
CiaFactBook2000.txt.hf from	 2260664 to	 2240008 in	 6.892
compressing to: D:\JavaProjects\a10\BooksAndHTML\jnglb10.txt.hf
jnglb10.txt from	 292059 to	 168618 in	 0.526
compressing to: D:\JavaProjects\a10\BooksAndHTML\jnglb10.txt.hf.hf
jnglb10.txt.hf from	 168618 to	 167167 in	 0.520
compressing to: D:\JavaProjects\a10\BooksAndHTML\kjv10.txt.hf
kjv10.txt from	 4345020 to	 2489768 in	 8.574
compressing to: D:\JavaProjects\a10\BooksAndHTML\kjv10.txt.hf.hf
kjv10.txt.hf from	 2489768 to	 2452980 in	 7.536
compressing to: D:\JavaProjects\a10\BooksAndHTML\melville.txt.hf
melville.txt from	 82140 to	 47364 in	 0.149
compressing to: D:\JavaProjects\a10\BooksAndHTML\melville.txt.hf.hf
melville.txt.hf from	 47364 to	 47551 in	 0.161
compressing to: D:\JavaProjects\a10\BooksAndHTML\quotes.htm.hf
quotes.htm from	 61563 to	 38423 in	 0.120
compressing to: D:\JavaProjects\a10\BooksAndHTML\quotes.htm.hf.hf
quotes.htm.hf from	 38423 to	 39036 in	 0.133
compressing to: D:\JavaProjects\a10\BooksAndHTML\rawMovieGross.txt.hf
rawMovieGross.txt from	 117272 to	 53833 in	 0.166
compressing to: D:\JavaProjects\a10\BooksAndHTML\rawMovieGross.txt.hf.hf
rawMovieGross.txt.hf from	 53833 to	 51821 in	 0.174
compressing to: D:\JavaProjects\a10\BooksAndHTML\revDictionary.txt.hf
revDictionary.txt from	 1130523 to	 611618 in	 1.887
compressing to: D:\JavaProjects\a10\BooksAndHTML\revDictionary.txt.hf.hf
revDictionary.txt.hf from	 611618 to	 590850 in	 1.811
compressing to: D:\JavaProjects\a10\BooksAndHTML\syllabus.htm.hf
syllabus.htm from	 33273 to	 21342 in	 0.066
compressing to: D:\JavaProjects\a10\BooksAndHTML\syllabus.htm.hf.hf
syllabus.htm.hf from	 21342 to	 21841 in	 0.079
compressing to: D:\JavaProjects\a10\BooksAndHTML\ThroughTheLookingGlass.txt.hf
ThroughTheLookingGlass.txt from	 188199 to	 110293 in	 0.351
compressing to: D:\JavaProjects\a10\BooksAndHTML\ThroughTheLookingGlass.txt.hf.hf
ThroughTheLookingGlass.txt.hf from	 110293 to	 109626 in	 0.353
--------
total bytes read: 15616693
total compressed bytes 11575332
total percent compression 25.878
compression time: 37.277

CALGARY Results
**************************************************
compressing to: D:\JavaProjects\a10\calgary\bib.hf
bib from	 111261 to	 73795 in	 0.286
compressing to: D:\JavaProjects\a10\calgary\book1.hf
book1 from	 768771 to	 439409 in	 1.561
compressing to: D:\JavaProjects\a10\calgary\book2.hf
book2 from	 610856 to	 369335 in	 1.175
compressing to: D:\JavaProjects\a10\calgary\geo.hf
geo from	 102400 to	 73592 in	 0.232
compressing to: D:\JavaProjects\a10\calgary\news.hf
news from	 377109 to	 247428 in	 0.760
compressing to: D:\JavaProjects\a10\calgary\obj1.hf
obj1 from	 21504 to	 17085 in	 0.051
compressing to: D:\JavaProjects\a10\calgary\obj2.hf
obj2 from	 246814 to	 195131 in	 0.588
compressing to: D:\JavaProjects\a10\calgary\paper1.hf
paper1 from	 53161 to	 34371 in	 0.108
compressing to: D:\JavaProjects\a10\calgary\paper2.hf
paper2 from	 82199 to	 48649 in	 0.151
compressing to: D:\JavaProjects\a10\calgary\paper3.hf
paper3 from	 46526 to	 28309 in	 0.088
compressing to: D:\JavaProjects\a10\calgary\paper4.hf
paper4 from	 13286 to	 8894 in	 0.027
compressing to: D:\JavaProjects\a10\calgary\paper5.hf
paper5 from	 11954 to	 8465 in	 0.027
compressing to: D:\JavaProjects\a10\calgary\paper6.hf
paper6 from	 38105 to	 25057 in	 0.079
compressing to: D:\JavaProjects\a10\calgary\pic.hf
pic from	 513216 to	 107586 in	 0.350
compressing to: D:\JavaProjects\a10\calgary\progc.hf
progc from	 39611 to	 26948 in	 0.089
compressing to: D:\JavaProjects\a10\calgary\progl.hf
progl from	 71646 to	 44017 in	 0.144
compressing to: D:\JavaProjects\a10\calgary\progp.hf
progp from	 49379 to	 31248 in	 0.099
compressing to: D:\JavaProjects\a10\calgary\trans.hf
trans from	 93695 to	 66252 in	 0.211


WATERLOO Results
**************************************
compressing to: D:\JavaProjects\a10\waterloo\clegg.tif.hf
clegg.tif from	 2149096 to	 2034595 in	 6.583
compressing to: D:\JavaProjects\a10\waterloo\frymire.tif.hf
frymire.tif from	 3706306 to	 2188593 in	 6.874
compressing to: D:\JavaProjects\a10\waterloo\lena.tif.hf
lena.tif from	 786568 to	 766146 in	 2.358
compressing to: D:\JavaProjects\a10\waterloo\monarch.tif.hf
monarch.tif from	 1179784 to	 1109973 in	 3.414
compressing to: D:\JavaProjects\a10\waterloo\peppers.tif.hf
peppers.tif from	 786568 to	 756968 in	 2.349
compressing to: D:\JavaProjects\a10\waterloo\sail.tif.hf
sail.tif from	 1179784 to	 1085501 in	 3.349
compressing to: D:\JavaProjects\a10\waterloo\serrano.tif.hf
serrano.tif from	 1498414 to	 1127645 in	 3.453
compressing to: D:\JavaProjects\a10\waterloo\tulips.tif.hf
tulips.tif from	 1179784 to	 1135861 in	 3.491
--------
total bytes read: 12466304
total compressed bytes 10205282
total percent compression 18.137
compression time: 31.871

