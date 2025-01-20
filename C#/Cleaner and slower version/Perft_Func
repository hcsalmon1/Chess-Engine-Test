using System;
using System.Collections.Generic;

namespace CEngineCopy
{
    internal class Perft_Func
    {

        static int PerftFunctions(int depth, int ply)
        {
            //Same as move list, span to avoid heap allocation
            Span<int> startingSquares = stackalloc int[50];
            Span<int> targetSquares = stackalloc int[50];
            Span<int> tags = stackalloc int[50];
            Span<int> pieces = stackalloc int[50];

            int moveCount = GenMoves.GetMoves(ref startingSquares, ref targetSquares, ref tags, ref pieces);

            if (depth == 1)
            {
                return moveCount;
            }

            int nodes = 0, priorNodes;
            int copyEp = Board.ep_global;
            Span<bool> copy_castle = stackalloc bool[4];
            copy_castle[0] = Board.castle_rights_global[0];
            copy_castle[1] = Board.castle_rights_global[1];
            copy_castle[2] = Board.castle_rights_global[2];
            copy_castle[3] = Board.castle_rights_global[3];

            for (int move_index = 0; move_index < moveCount; ++move_index)
            {
                int startingSquareCopy = startingSquares[move_index];
                int targetSquareCopy = targetSquares[move_index];
                int piece = pieces[move_index];
                int tag = tags[move_index];

                int captureIndex = Make_Move.MakeMove(startingSquareCopy, targetSquareCopy, tag, piece);

                priorNodes = nodes;
                nodes += PerftFunctions(depth - 1, ply + 1);

                #region Unmakemove

                Make_Move.UnmakeMove(startingSquareCopy, targetSquareCopy, piece, tag, captureIndex);

                Board.castle_rights_global[0] = copy_castle[0];
                Board.castle_rights_global[1] = copy_castle[1];
                Board.castle_rights_global[2] = copy_castle[2];
                Board.castle_rights_global[3] = copy_castle[3];
                Board.ep_global = copyEp;

                #endregion

                //if (ply == 0)
                //{
                //   PrintMoveNoNL(move_list[move_index]);
                //   Console.Write(": %llu\n", nodes - priorNodes);
                //}
            }

            return nodes;
        }

        public static void RunPerftFunctions(int depth)
        {
            DateTime start = DateTime.Now;

            int nodes = PerftFunctions(depth, 0);

            DateTime end = DateTime.Now;
            TimeSpan elapsed = end - start;
            int elapsedTimeInMs = (int)elapsed.TotalMilliseconds; ;
            Console.WriteLine($"Nodes: {nodes}");

            Console.WriteLine($"Elapsed time: {elapsedTimeInMs}ms");
        }

    }
}
