Chess Engine Move Generator Comparison

This code is from my two videos comparing different languages in one chess bitboard algorithm.  
The constants are massive and almost everything is written in one function to maximize performance.  

Current results:  
C: 353.4ms  
C#: 768.4ms  
C++: 337.2ms  
D: 438ms  
Go: 685.8ms  
Java: NA, I will try using Java with longs when I have time.  
Nim: 533.6ms  
Odin: 505.8ms  
Python: 1,383,536ms or 22-23 minutes  
Rust: 536.4ms  
Swift: 585ms  
Zig: 348ms  
  
Feel free to make improvements to any of the code. Some notes:  
-We test the opening chess position to depth 6. Target: 119,060,324 nodes  
-The max moves in a chess position are 220. I made the moveList 250 just for safety. The max moves reached from any chess position 
from the start is 46. So you can set the move_list to 46 elements without an index error but this will make the algorithm break 
in any other position.  
-Another approach is to make the move_list global and use an index like this:  
c# example:  
  
Together:  

        static int[,] move_list_global = new int[500, 4];
        static int[] move_counts = new int[10];

or separate:  
      
        static int[] StartingSquares = new int[500];
        static int[] TargetSquares = new int[500];
        static int[] Tags = new int[500];
        static int[] Pieces = new int[500];
        static int[] move_counts = new int[10];

Function example:  

        static int Perft(int depth, int ply)
        {

            move_counts[ply + 1] = GetMoves(ply);
            int move_count = move_counts[ply + 1] - move_counts[ply];

            if (depth <= 1) {
                return move_count;
            }

            int nodes = 0;
            for (int i = move_counts[ply]; i < move_counts[ply + 1]; i++)
            {
                //make move
                nodes += Perft(depth - 1, ply + 1);
                //unmake move
            }

            return nodes;
        }

  The max moves I found using this approach was around 150, so the global array could be that size.  
  Again this will break in any other position with lots of moves or with more depth.  

  I might test all code examples with 46 size move_list and global move_lists later.  
