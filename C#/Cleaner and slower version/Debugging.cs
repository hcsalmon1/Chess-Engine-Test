using System;
using static CEngineCopy.Constants;
using static CEngineCopy.MoveUtils;

namespace CEngineCopy
{
    internal class Debugging
    {

        public static void PrintAllBitboards()
        {
            for (int i = 0; i < 12; i++)
            {
                PrintBitboard(Board.bitboard_array_global[i]);
            }
        }


        public static void PrintBitboard(ulong input)
        {
            if (input == 0)
            {
                Console.WriteLine($"   bitboard: {input}");
                return;
            }
            for (int y = 0; y < 8; y++)
            {
                Console.Write("    ");
                for (int x = 0; x < 8; x++)
                {
                    int square = (y * 8) + x;
                    if ((input & Constants.SQUARE_BBS[square]) != 0)
                    {
                        Console.Write("X ");
                    }
                    else
                    {
                        Console.Write("- ");
                    }
                }
                Console.Write("\n");
            }

            Console.WriteLine($"\nbitboard: {input}\n");
        }


        public static void PrintAllDebug(int startingSquare, int targetSquare, int piece, int tag, int captureIndex)
        {
            PrintPiece(piece);
            PrintMoveNoNL(startingSquare, targetSquare, tag);
            Console.WriteLine();
            Console.WriteLine($"capture index: {captureIndex}");
            PrintBoardGlobal();
        }

        public static void PrintBoardGlobal()
        {
            char[] yCoordinates = { '8', '7', '6', '5', '4', '3', '2', '1' };
            char[] castleChars = { 'K', 'Q', 'k', 'q' };

            for (int y = 0; y < 8; y++)
            {
                Console.Write($"  {yCoordinates[y]} ");
                for (int x = 0; x < 8; x++)
                {
                    PrintPiece(y, x);
                }
                Console.Write('\n');
            }
            Console.WriteLine("    A  B  C  D  E  F  G  H\n\n");
            if (Board.is_white_global == true)
            {
                Console.WriteLine("Side: White To Play\n");
            }
            else
            {
                Console.WriteLine("Side: Black To Play\n");
            }
            Console.Write("castle: ");
            for (int i = 0; i < 4; i++)
            {
                if (Board.castle_rights_global[i] == true)
                {
                    Console.Write(castleChars[i]);
                }
                else
                {
                    Console.Write("-");
                }
            }
            Console.Write($"\nEP: ");
            if (Board.ep_global > 63 || Board.ep_global < 0)
            {
                Console.WriteLine(Board.ep_global);
            }
            else
            {
                Console.WriteLine(Constants.SQ_CHAR_X[Board.ep_global]);
                Console.WriteLine(Constants.SQ_CHAR_Y[Board.ep_global]);
            }

            Console.Write("\n\n");
        }

        private static void PrintPiece(int y, int x)
        {
            int square = (y * 8) + x;
            bool pieceFound = false;

            for (int pieceIndex = 0; pieceIndex < 12; pieceIndex++)
            {
                if ((Board.bitboard_array_global[pieceIndex] & Constants.SQUARE_BBS[square]) != 0)
                {
                    Console.Write(Constants.PIECE_COLOURS[pieceIndex]);
                    Console.Write(Constants.PIECE_CHARS[pieceIndex]);
                    Console.Write(' ');
                    pieceFound = true;
                    break;
                }
            }

            if (pieceFound == false)
            {
                Console.Write("-- ");
            }
        }

        public static void PrintMoveNoNL(int starting, int target, int tag)
        {    //starting
            if (OutOfBounds(starting) == true)
            {
                Console.Write(starting);
            }
            else
            {
                Console.Write(SQ_CHAR_X[starting]);
                Console.Write(SQ_CHAR_Y[starting]);
            }
            //target
            if (OutOfBounds(target) == true)
            {
                Console.Write(target);
            }
            else
            {
                Console.Write(SQ_CHAR_X[target]);
                Console.Write(SQ_CHAR_Y[target]);
            }
            if (tag == TAG_B_N_PROMOTION_CAP || tag == TAG_B_N_PROMOTION || tag == TAG_W_N_PROMOTION || tag == TAG_W_N_PROMOTION_CAP)
            {
                Console.Write("n");
            }
            else if (tag == TAG_B_R_PROMOTION_CAP || tag == TAG_B_R_PROMOTION || tag == TAG_W_R_PROMOTION || tag == TAG_W_R_PROMOTION_CAP)
            {
                Console.Write("r");
            }
            else if (tag == TAG_B_B_PROMOTION_CAP || tag == TAG_B_B_PROMOTION || tag == TAG_W_B_PROMOTION || tag == TAG_W_B_PROMOTION_CAP)
            {
                Console.Write("b");
            }
            else if (tag == TAG_B_Q_PROMOTION_CAP || tag == TAG_B_Q_PROMOTION || tag == TAG_W_Q_PROMOTION || tag == TAG_W_Q_PROMOTION_CAP)
            {
                Console.Write("q");
            }
        }

        public static void PrintMoveNoNL(int[] move)
        {    //starting
            if (OutOfBounds(move[MOVE_STARTING]) == true)
            {
                Console.Write("%d", move[MOVE_STARTING]);
            }
            else
            {
                Console.Write("%c", SQ_CHAR_X[move[MOVE_STARTING]]);
                Console.Write("%c", SQ_CHAR_Y[move[MOVE_STARTING]]);
            }
            //target
            if (OutOfBounds(move[MOVE_TARGET]) == true)
            {
                Console.Write("%d", move[MOVE_TARGET]);
            }
            else
            {
                Console.Write("%c", SQ_CHAR_X[move[MOVE_TARGET]]);
                Console.Write("%c", SQ_CHAR_Y[move[MOVE_TARGET]]);
            }
            int tag = move[MOVE_TAG];
            if (tag == TAG_B_N_PROMOTION_CAP || tag == TAG_B_N_PROMOTION || tag == TAG_W_N_PROMOTION || tag == TAG_W_N_PROMOTION_CAP)
            {
                Console.Write("n");
            }
            else if (tag == TAG_B_R_PROMOTION_CAP || tag == TAG_B_R_PROMOTION || tag == TAG_W_R_PROMOTION || tag == TAG_W_R_PROMOTION_CAP)
            {
                Console.Write("r");
            }
            else if (tag == TAG_B_B_PROMOTION_CAP || tag == TAG_B_B_PROMOTION || tag == TAG_W_B_PROMOTION || tag == TAG_W_B_PROMOTION_CAP)
            {
                Console.Write("b");
            }
            else if (tag == TAG_B_Q_PROMOTION_CAP || tag == TAG_B_Q_PROMOTION || tag == TAG_W_Q_PROMOTION || tag == TAG_W_Q_PROMOTION_CAP)
            {
                Console.Write("q");
            }
        }

        public static void PrintPiece(int piece)
        {
            Console.Write(Constants.PIECE_COLOURS[piece]);
            Console.Write(Constants.PIECE_CHARS[piece]);
            Console.Write(' ');
        }
        public static void PrintPieceLn(int piece)
        {
            Console.WriteLine(Constants.PIECE_COLOURS[piece]);
            Console.WriteLine(Constants.PIECE_CHARS[piece]);
        }
    }
}
