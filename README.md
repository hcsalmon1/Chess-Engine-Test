Chess Engine Move Generator Comparison

This code is from my two videos comparing different languages in one chess bitboard algorithm.  
The constants are massive and almost everything is written in one function to maximize performance.  

Current results:  
C: 339.4ms  
C#: 683.2ms  
C++: 331.2ms  
D: 438ms  
Go: 627.4ms  
Java: 1988ms  
Nim: 533.6ms  
Odin: 398ms  
Python: 1,383,536ms or 22-23 minutes  
Rust: 536.4ms  
Swift: 585ms  
Zig: 335.8ms  
  
Feel free to make improvements to any of the code. Some notes:  
-We test the opening chess position to depth 6. Target: 119,060,324 nodes  
-The max moves in a chess position are 220. I made the moveList 250 just for safety. The max moves reached from any chess position 
from the start is 46. So you can set the move_list to 47 elements without an index error but this will make the algorithm break 
in any other position.  
-Another approach is to make the move_list global and use an index like this:  
c# example:  
  
        static int[,] StartingSquares = new int[6, 50];
        static int[,] TargetSquares = new int[6, 50];
        static int[,] Tags = new int[6, 50];
        static int[,] Pieces = new int[6, 50];
        static int[,] move_counts = new int[6];

Function example:  

        static int Perft(int depth, int ply)
        {

            int move_count = GetMoves(ply);
            
            if (depth <= 1) {
                return move_count;
            }

            int nodes = 0;
            for (int i = 0; i < move_count; i++)
            {
                int startingSquare = StartingSquares[ply, i];
                //etc
                
                //make move
                nodes += Perft(depth - 1, ply + 1);
                //unmake move
            }

            return nodes;
        }

I might test all code examples with 50 size move_list and global move_lists later.  

The original code was in CPP where I made everything in one big function and was testing loads of versions.  
For that reason the code is very messy and not clean at all. I made a "clean code" version that I put in the C# folder.  
This is where the code extracted into functions and made clearer. However this approach is slower, as function calls have a cost.  

I normally don't code this way. I extract functions and hate over nesting. I only did it here to maximize performance.  





