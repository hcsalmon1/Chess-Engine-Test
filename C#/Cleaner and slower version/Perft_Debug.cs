using System;
using System.Collections.Generic;
using static CEngineCopy.Debugging;
using static CEngineCopy.Board;


namespace CEngineCopy
{
    internal class Perft_Debug
    {

        struct ErrorInt {
            public Error? Error { get; }
            public int Value { get; set; }

            private ErrorInt(Error? error, int value) {
                Error = error;
                Value = value;
            }
            public static ErrorInt FromValue(int value) {
                return new ErrorInt(null, value);
            }
            public static ErrorInt FromError(Error error) {
                return new ErrorInt(error, default);
            }
            public static void PrintError(Error error) {
                Console.WriteLine(error);
            }
        }

        enum Error
        {
            White_King_Captured,
            Black_King_Captured,
            Too_Many_Moves,
            White_King_In_Check_On_BlackMove,
            Black_King_In_Check_On_White_Move,
            Copy_Boards_Not_Same,
            Invalid_Starting_Square,
            Invalid_Target_Square,
            Invalid_Tag,
            Invalid_Piece,
            Capture_Index_Invalid,
            Capture_Piece_Not_Found,
            Starting_Depth_Too_High,
            Starting_Depth_Zero,
            Depth_Less_Than_Zero,
            Starting_Square_And_Target_Square_The_Same,
            Ep_Not_Target_Square,
            Promotion_When_Not_Expected,
            Side_Not_Changed,
            Side_Not_Changed_Back,
            Piece_On_Same_Square,
        }

        static ulong CombineBitboardsGlobal()
        {
            return Board.bitboard_array_global[0] |
                   Board.bitboard_array_global[1] |
                   Board.bitboard_array_global[2] |
                   Board.bitboard_array_global[3] |
                   Board.bitboard_array_global[4] |
                   Board.bitboard_array_global[5] |
                   Board.bitboard_array_global[6] |
                   Board.bitboard_array_global[7] |
                   Board.bitboard_array_global[8] |
                   Board.bitboard_array_global[9] |
                   Board.bitboard_array_global[10] |
                   Board.bitboard_array_global[11];
        }

        struct DebugInfo
        {
            public int CallCount;
            public int LastStarting;
            public int LastTarget;
            public int LastTag;
            public bool PromotionExpected;
        }

        static ulong GetWhiteOccs()
        {
            return Board.bitboard_array_global[0] | Board.bitboard_array_global[1] | Board.bitboard_array_global[2] | Board.bitboard_array_global[3] | Board.bitboard_array_global[4] | Board.bitboard_array_global[5];
        }
        static ulong GetBlackOccs()
        {
            return Board.bitboard_array_global[6] | Board.bitboard_array_global[7] | Board.bitboard_array_global[8] | Board.bitboard_array_global[9] | Board.bitboard_array_global[10] | Board.bitboard_array_global[11];
        }

        static ErrorInt InitialDebugChecks(int depth)
        {
            if (depth < 0)
            {
                return ErrorInt.FromError(Error.Depth_Less_Than_Zero);
            }
            if (Board.bitboard_array_global[Constants.WK] == 0)
            {
                return ErrorInt.FromError(Error.White_King_Captured);
            }
            if (Board.bitboard_array_global[Constants.BK] == 0)
            {
                return ErrorInt.FromError(Error.Black_King_Captured);
            }
            ulong COMBINED_OCCUPANCIES = CombineBitboardsGlobal();
            if (Board.is_white_global == true)
            {
                int blackKingPosition = MoveUtils.BitScanForward(Board.bitboard_array_global[Constants.BK]);
                if (MoveUtils.Is_Square_Attacked_By_White_Global(blackKingPosition, COMBINED_OCCUPANCIES) == true)
                {
                    return ErrorInt.FromError(Error.Black_King_In_Check_On_White_Move);
                }
            }
            else
            {
                int whiteKingPosition = MoveUtils.BitScanForward(Board.bitboard_array_global[Constants.WK]);
                if (MoveUtils.Is_Square_Attacked_By_Black_Global(whiteKingPosition, COMBINED_OCCUPANCIES) == true)
                {
                    return ErrorInt.FromError(Error.White_King_In_Check_On_BlackMove);
                }
            }
            return ErrorInt.FromValue(0);
        }

        static ErrorInt MoveErrorChecking(ref DebugInfo debugInfo, int startingSquareCopy, int targetSquareCopy, int piece, int tag)
        {
            if (startingSquareCopy < 0 || startingSquareCopy > 63)
            {
                return ErrorInt.FromError(Error.Invalid_Starting_Square);
            }
            if (targetSquareCopy < 0 || targetSquareCopy > 63)
            {
                return ErrorInt.FromError(Error.Invalid_Target_Square);
            }
            if (piece < 0 || piece > 11)
            {
                return ErrorInt.FromError(Error.Invalid_Piece);
            }
            if (tag < 0 || tag > 27)
            {
                return ErrorInt.FromError(Error.Invalid_Tag);
            }
            if (startingSquareCopy == targetSquareCopy)
            {
                return ErrorInt.FromError(Error.Starting_Square_And_Target_Square_The_Same);
            }
            if (tag == Constants.TAG_WHITE_EP || tag == Constants.TAG_BLACK_EP)
            {
                if (Board.ep_global != targetSquareCopy)
                {
                    return ErrorInt.FromError(Error.Ep_Not_Target_Square);
                }
            }
            if (debugInfo.PromotionExpected == false)
            {
                if (tag >= Constants.TAG_B_N_PROMOTION && tag <= Constants.TAG_W_R_PROMOTION_CAP)
                {
                    Debugging.PrintAllDebug(startingSquareCopy, targetSquareCopy, piece, tag, -1);
                    return ErrorInt.FromError(Error.Promotion_When_Not_Expected);
                }
            }
            return ErrorInt.FromValue(0);
        }

        static ErrorInt PerftFunctionsDebug(int depth, int ply, ref DebugInfo debugInfo)
        {

            ErrorInt checkResult = InitialDebugChecks(depth);
            if (checkResult.Error.HasValue) {
                return checkResult;
            }

            debugInfo.CallCount += 1;
            if (depth == 0)
            {
                return ErrorInt.FromValue(1);
            }

            //Same as move list, span to avoid heap allocation
            Span<int> startingSquares = stackalloc int[50];
            Span<int> targetSquares = stackalloc int[50];
            Span<int> tags = stackalloc int[50];
            Span<int> pieces = stackalloc int[50];

            int moveCount = GenMoves.GetMoves(ref startingSquares, ref targetSquares, ref tags, ref pieces);

            ErrorInt nodes = ErrorInt.FromValue(0);

            int copyEp = Board.ep_global;
            Span<bool> copy_castle = stackalloc bool[4];
            copy_castle[0] = Board.castle_rights_global[0];
            copy_castle[1] = Board.castle_rights_global[1];
            copy_castle[2] = Board.castle_rights_global[2];
            copy_castle[3] = Board.castle_rights_global[3];

            ulong[] bitboardCopy = new ulong[12]
            {
                Board.bitboard_array_global[0],
                Board.bitboard_array_global[1],
                Board.bitboard_array_global[2],
                Board.bitboard_array_global[3],
                Board.bitboard_array_global[4],
                Board.bitboard_array_global[5],
                Board.bitboard_array_global[6],
                Board.bitboard_array_global[7],
                Board.bitboard_array_global[8],
                Board.bitboard_array_global[9],
                Board.bitboard_array_global[10],
                Board.bitboard_array_global[11],
            };

            for (int move_index = 0; move_index < moveCount; ++move_index)
            {
                int startingSquareCopy = startingSquares[move_index];
                int targetSquareCopy = targetSquares[move_index];
                int piece = pieces[move_index];
                int tag = tags[move_index];

                ErrorInt moveCheckResult = MoveErrorChecking(ref debugInfo, startingSquareCopy, targetSquareCopy, piece, tag);
                if (moveCheckResult.Error.HasValue) {
                    return moveCheckResult;
                }

                bool sideBefore = Board.is_white_global;

                int captureIndex = Make_Move.MakeMove(startingSquareCopy, targetSquareCopy, tag, piece);

                if (Board.is_white_global == sideBefore) {
                    return ErrorInt.FromError(Error.Side_Not_Changed);
                }

                ulong WHITE_OCCS = GetWhiteOccs();
                ulong BLACK_OCCS = GetBlackOccs();
                if ((WHITE_OCCS & BLACK_OCCS) != 0)
                {
                    PrintAllBitboards();
                    PrintAllDebug(startingSquareCopy, targetSquareCopy, piece, tag, captureIndex);
                    return ErrorInt.FromError(Error.Piece_On_Same_Square);
                }

                ErrorInt priorNodes = ErrorInt.FromValue(nodes.Value);
                ErrorInt nodesToAdd = PerftFunctionsDebug(depth - 1, ply + 1, ref debugInfo);
                if (nodesToAdd.Error.HasValue)
                {
                    return nodesToAdd;
                }
                nodes.Value += nodesToAdd.Value;

                #region Unmakemove

                bool newSide = Board.is_white_global;

                Make_Move.UnmakeMove(startingSquareCopy, targetSquareCopy, piece, tag, captureIndex);

                if (Board.is_white_global == newSide)
                {
                    return ErrorInt.FromError(Error.Side_Not_Changed_Back);
                }

                castle_rights_global[0] = copy_castle[0];
                castle_rights_global[1] = copy_castle[1];
                castle_rights_global[2] = copy_castle[2];
                castle_rights_global[3] = copy_castle[3];
                ep_global = copyEp;

                ulong WHITE_OCCS_2 = GetWhiteOccs();
                ulong BLACK_OCCS_2 = GetBlackOccs();
                if ((WHITE_OCCS_2 & BLACK_OCCS_2) != 0)
                {
                    PrintAllBitboards();
                    PrintAllDebug(startingSquareCopy, targetSquareCopy, piece, tag, captureIndex);
                    return ErrorInt.FromError(Error.Piece_On_Same_Square);
                }

                for (int i = 0; i < 12; i++)
                {
                    if (Board.bitboard_array_global[i] != bitboardCopy[i])
                    {
                        PrintAllBitboards();
                        Console.WriteLine($"global {i}");
                        PrintBitboard(Board.bitboard_array_global[i]);
                        Console.WriteLine($"copy {i}");
                        PrintBitboard(bitboardCopy[i]);
                        PrintAllDebug(startingSquareCopy, targetSquareCopy, piece, tag, captureIndex);
                        return ErrorInt.FromError(Error.Copy_Boards_Not_Same);
                    }
                }

                #endregion

                if (ply == 0)
                {
                    PrintMoveNoNL(startingSquareCopy, targetSquareCopy, tag);
                    Console.Write($": {nodes.Value - priorNodes.Value}\n");
                }
            }

            return nodes;
        }

        public static void RunPerftFunctionsDebug(int depth)
        {
            DateTime start = DateTime.Now;
            DebugInfo debugInfo = new();
            debugInfo.PromotionExpected = false;

            ErrorInt nodes = PerftFunctionsDebug(depth, 0, ref debugInfo);
            if (nodes.Error.HasValue)
            {
                Console.Write("ERROR: ");
                ErrorInt.PrintError(nodes.Error.Value);
            }

            DateTime end = DateTime.Now;
            TimeSpan elapsed = end - start;
            int elapsedTimeInMs = (int)elapsed.TotalMilliseconds; ;
            Console.WriteLine($"Nodes: {nodes.Value}");

            Console.WriteLine($"Elapsed time: {elapsedTimeInMs}ms");
        }


    }
}
