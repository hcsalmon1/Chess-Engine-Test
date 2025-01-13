using System;


namespace CEngineCopy
{
    public class Program
    {

        const ulong MAGIC = 0x03f79d71b4cb0a89;

        const int PINNED_SQUARE_INDEX = 0;
        const int PINNING_PIECE_INDEX = 1;

        const int MOVE_STARTING = 0;
        const int MOVE_TARGET = 1;
        const int MOVE_PIECE = 2;
        const int MOVE_TAG = 3;

        const int TAG_NONE = 0, TAG_CAPTURE = 1, TAG_WHITE_EP = 2, TAG_BLACK_EP = 3, TAG_W_CASTLE_KS = 4, TAG_W_CASTLE_QS = 5, TAG_B_CASTLE_KS = 6, TAG_B_CASTLE_QS = 7,
    TAG_B_N_PROMOTION = 8, TAG_B_B_PROMOTION = 9, TAG_B_Q_PROMOTION = 10, TAG_B_R_PROMOTION = 11,
    TAG_W_N_PROMOTION = 12, TAG_W_B_PROMOTION = 13, TAG_W_Q_PROMOTION = 14, TAG_W_R_PROMOTION = 15,
    TAG_B_N_PROMOTION_CAP = 16, TAG_B_B_PROMOTION_CAP = 17, TAG_B_Q_PROMOTION_CAP = 18, TAG_B_R_PROMOTION_CAP = 19,
    TAG_W_N_PROMOTION_CAP = 20, TAG_W_B_PROMOTION_CAP = 21, TAG_W_Q_PROMOTION_CAP = 22, TAG_W_R_PROMOTION_CAP = 23,
    TAG_W_P_DOUBLE = 24, TAG_B_P_DOUBLE = 25, TAG_CHECK = 26, TAG_CHECK_CAP = 27;

        const int A8 = 0, B8 = 1, C8 = 2, D8 = 3, E8 = 4, F8 = 5, G8 = 6, H8 = 7,
    A7 = 8, B7 = 9, C7 = 10, D7 = 11, E7 = 12, F7 = 13, G7 = 14, H7 = 15,
    A6 = 16, B6 = 17, C6 = 18, D6 = 19, E6 = 20, F6 = 21, G6 = 22, H6 = 23,
    A5 = 24, B5 = 25, C5 = 26, D5 = 27, E5 = 28, F5 = 29, G5 = 30, H5 = 31,
    A4 = 32, B4 = 33, C4 = 34, D4 = 35, E4 = 36, F4 = 37, G4 = 38, H4 = 39,
    A3 = 40, B3 = 41, C3 = 42, D3 = 43, E3 = 44, F3 = 45, G3 = 46, H3 = 47,
    A2 = 48, B2 = 49, C2 = 50, D2 = 51, E2 = 52, F2 = 53, G2 = 54, H2 = 55,
    A1 = 56, B1 = 57, C1 = 58, D1 = 59, E1 = 60, F1 = 61, G1 = 62, H1 = 63, NO_SQUARE = 64;

        static readonly int[] DEBRUIJN64 =
        {
        0, 47,  1, 56, 48, 27,  2, 60,
       57, 49, 41, 37, 28, 16,  3, 61,
       54, 58, 35, 52, 50, 42, 21, 44,
       38, 32, 29, 23, 17, 11,  4, 62,
       46, 55, 26, 59, 40, 36, 15, 53,
       34, 51, 20, 43, 31, 22, 10, 45,
       25, 39, 14, 33, 19, 30,  9, 24,
       13, 18,  8, 12,  7,  6,  5, 63
    };

        static int BitScanForward(ulong tempBitboard)
        {
            return (DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58]);
        }

        const ulong WKS_EMPTY_BITBOARD = 6917529027641081856UL;
        const ulong WQS_EMPTY_BITBOARD = 1008806316530991104UL;
        const ulong BKS_EMPTY_BITBOARD = 96UL;
        const ulong BQS_EMPTY_BITBOARD = 14UL;

        const int WKS_CASTLE_RIGHTS = 0, WQS_CASTLE_RIGHTS = 1, BKS_CASTLE_RIGHTS = 2, BQS_CASTLE_RIGHTS = 3;

        const int WHITE_START_INDEX = WP;
        const int WHITE_END_INDEX = WK;
        const int BLACK_START_INDEX = BP;
        const int BLACK_END_INDEX = BK;

        const ulong BP_STARTING_POSITIONS = 65280;
        const ulong WP_STARTING_POSITIONS = 71776119061217280;
        const ulong BK_STARTING_POSITION = 16;
        const ulong WK_STARTING_POSITION = 1152921504606846976;
        const ulong BN_STARTING_POSITIONS = 66;
        const ulong WN_STARTING_POSITIONS = 4755801206503243776;
        const ulong WR_STARTING_POSITIONS = 9295429630892703744;
        const ulong BR_STARTING_POSITIONS = 129;
        const ulong BB_STARTING_POSITIONS = 36;
        const ulong WB_STARTING_POSITIONS = 2594073385365405696;
        const ulong WQ_STARTING_POSITION = 576460752303423488;
        const ulong BQ_STARTING_POSITION = 8;

        static readonly char[] SQ_CHAR_Y =
    {
    '8','8','8','8','8','8','8','8',
    '7','7','7','7','7','7','7','7',
    '6','6','6','6','6','6','6','6',
    '5','5','5','5','5','5','5','5',
    '4','4','4','4','4','4','4','4',
    '3','3','3','3','3','3','3','3',
    '2','2','2','2','2','2','2','2',
    '1','1','1','1','1','1','1','1','A'
};

        static readonly char[] SQ_CHAR_X =
        {
    'a','b','c','d','e','f','g','h',
    'a','b','c','d','e','f','g','h',
    'a','b','c','d','e','f','g','h',
    'a','b','c','d','e','f','g','h',
    'a','b','c','d','e','f','g','h',
    'a','b','c','d','e','f','g','h',
    'a','b','c','d','e','f','g','h',
    'a','b','c','d','e','f','g','h','N'
};

        const int WP = 0, WN = 1, WB = 2, WR = 3, WQ = 4, WK = 5,
        BP = 6, BN = 7, BB = 8, BR = 9, BQ = 10, BK = 11;


        static ulong[] bitboard_array_global = new ulong[12];
        static int ep_global;
        static bool[] castle_rights_global = new bool[4];
        static bool is_white_global;

        static void SetStartingPosition()
        {

            ep_global = Constants.NO_SQUARE;
            is_white_global = true;
            castle_rights_global[0] = true;
            castle_rights_global[1] = true;
            castle_rights_global[2] = true;
            castle_rights_global[3] = true;

            bitboard_array_global[WP] = WP_STARTING_POSITIONS;
            bitboard_array_global[WN] = WN_STARTING_POSITIONS;
            bitboard_array_global[WB] = WB_STARTING_POSITIONS;
            bitboard_array_global[WR] = WR_STARTING_POSITIONS;
            bitboard_array_global[WQ] = WQ_STARTING_POSITION;
            bitboard_array_global[WK] = WK_STARTING_POSITION;
            bitboard_array_global[BP] = BP_STARTING_POSITIONS;
            bitboard_array_global[BN] = BN_STARTING_POSITIONS;
            bitboard_array_global[BB] = BB_STARTING_POSITIONS;
            bitboard_array_global[BR] = BR_STARTING_POSITIONS;
            bitboard_array_global[BQ] = BQ_STARTING_POSITION;
            bitboard_array_global[BK] = BK_STARTING_POSITION;

        }


        static bool Is_Square_Attacked_By_Black_Global(int square, ulong occupancy)
        {
            if ((bitboard_array_global[BP] & Constants.WHITE_PAWN_ATTACKS[square]) != 0)
            {
                return true;
            }
            if ((bitboard_array_global[BN] & Constants.KNIGHT_ATTACKS[square]) != 0)
            {
                return true;
            }
            if ((bitboard_array_global[BK] & Constants.KING_ATTACKS[square]) != 0)
            {
                return true;
            }
            ulong bishopAttacks = Constants.GetBishopAttacksFast(square, occupancy);
            if ((bitboard_array_global[BB] & bishopAttacks) != 0)
            {
                return true;
            }
            if ((bitboard_array_global[BQ] & bishopAttacks) != 0)
            {
                return true;
            }
            ulong rookAttacks = Constants.GetRookAttacksFast(square, occupancy);
            if ((bitboard_array_global[BR] & rookAttacks) != 0)
            {
                return true;
            }
            if ((bitboard_array_global[BQ] & rookAttacks) != 0)
            {
                return true;
            }
            return false;
        }

        static bool Is_Square_Attacked_By_White_Global(int square, ulong occupancy)
        {
            if ((bitboard_array_global[WP] & Constants.BLACK_PAWN_ATTACKS[square]) != 0)
            {
                return true;
            }
            if ((bitboard_array_global[WN] & Constants.KNIGHT_ATTACKS[square]) != 0)
            {
                return true;
            }
            if ((bitboard_array_global[WK] & Constants.KING_ATTACKS[square]) != 0)
            {
                return true;
            }
            ulong bishopAttacks = Constants.GetBishopAttacksFast(square, occupancy);
            if ((bitboard_array_global[WB] & bishopAttacks) != 0)
            {
                return true;
            }
            if ((bitboard_array_global[WQ] & bishopAttacks) != 0)
            {
                return true;
            }
            ulong rookAttacks = Constants.GetRookAttacksFast(square, occupancy);
            if ((bitboard_array_global[WR] & rookAttacks) != 0)
            {
                return true;
            }
            if ((bitboard_array_global[WQ] & rookAttacks) != 0)
            {
                return true;
            }
            return false;
        }

        static bool OutOfBounds(int move)
        {
            if (move < 0)
            {
                return true;
            }
            if (move > 63)
            {
                return true;
            }
            return false;
        }

        static void PrintMoveNoNL(int[] move)
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
         static int PerftInline(int depth, int ply)
        {
            //if (depth == 0)
            //{
            //    return 1;
            //}

            Span<int> startingSquares = stackalloc int[50];
            Span<int> targetSquares = stackalloc int[50];
            Span<int> tags = stackalloc int[50];
            Span<int> pieces = stackalloc int[50];

            int moveCount = 0;
            //Move generating variables
            ulong WHITE_OCCUPANCIES_LOCAL = bitboard_array_global[0] | bitboard_array_global[1] | bitboard_array_global[2] | bitboard_array_global[3] | bitboard_array_global[4] | bitboard_array_global[5];
            ulong BLACK_OCCUPANCIES_LOCAL = bitboard_array_global[6] | bitboard_array_global[7] | bitboard_array_global[8] | bitboard_array_global[9] | bitboard_array_global[10] | bitboard_array_global[11];
            ulong COMBINED_OCCUPANCIES_LOCAL = WHITE_OCCUPANCIES_LOCAL | BLACK_OCCUPANCIES_LOCAL;
            ulong EMPTY_OCCUPANCIES = ~COMBINED_OCCUPANCIES_LOCAL;
            ulong tempBitboard, checkBitboard = 0UL, tempPinBitboard, tempAttack, tempEmpty, tempCaptures;
            int startingSquare = NO_SQUARE, targetSquare = NO_SQUARE;

            Span<int> pinArrayPiece = stackalloc int[8]
            {
                -1,-1,-1,-1,-1,-1,-1,-1
            };
            Span<int> pinArraySquare = stackalloc int[8]
            {
                -1,-1,-1,-1,-1,-1,-1,-1
            };

            int pinNumber = 0;

            #region Generate Moves

            if (is_white_global == true)
            {
                int whiteKingCheckCount = 0;
                int whiteKingPosition = BitScanForward(bitboard_array_global[WK]);

                #region pins and check

                //pawns
                tempBitboard = bitboard_array_global[BP] & Constants.WHITE_PAWN_ATTACKS[whiteKingPosition];
                if (tempBitboard != 0)
                {
                    int pawn_square = (DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58]);

                    if (checkBitboard == 0)
                    {
                        checkBitboard = 1UL << pawn_square;
                    }

                    whiteKingCheckCount++;
                }

                //knights
                tempBitboard = bitboard_array_global[BN] & Constants.KNIGHT_ATTACKS[whiteKingPosition];
                if (tempBitboard != 0)
                {
                    int knight_square = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];

                    if (checkBitboard == 0)
                    {
                        checkBitboard = Constants.SQUARE_BBS[knight_square];
                    }

                    whiteKingCheckCount++;
                }

                //bishops
                ulong bishopAttacksChecks = Constants.GetBishopAttacksFast(whiteKingPosition, BLACK_OCCUPANCIES_LOCAL);
                tempBitboard = bitboard_array_global[BB] & bishopAttacksChecks;
                while (tempBitboard != 0)
                {
                    int piece_square = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];

                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[whiteKingPosition, piece_square] & WHITE_OCCUPANCIES_LOCAL;

                    if (tempPinBitboard == 0)
                    {
                        if (checkBitboard == 0)
                        {
                            checkBitboard = Constants.INBETWEEN_BITBOARDS[whiteKingPosition, piece_square];
                        }
                        whiteKingCheckCount++;
                    }
                    else
                    {
                        int pinned_square = (DEBRUIJN64[MAGIC * (tempPinBitboard ^ (tempPinBitboard - 1)) >> 58]);
                        tempPinBitboard &= tempPinBitboard - 1;

                        if (tempPinBitboard == 0)
                        {
                            pinArraySquare[pinNumber] = pinned_square;
                            pinArrayPiece[pinNumber] = piece_square;
                            pinNumber++;
                        }
                    }
                    tempBitboard &= tempBitboard - 1;
                }

                //queen
                tempBitboard = bitboard_array_global[BQ] & bishopAttacksChecks;
                while (tempBitboard != 0)
                {
                    int piece_square = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];

                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[whiteKingPosition, piece_square] & WHITE_OCCUPANCIES_LOCAL;

                    if (tempPinBitboard == 0)
                    {
                        if (checkBitboard == 0)
                        {
                            checkBitboard = Constants.INBETWEEN_BITBOARDS[whiteKingPosition, piece_square];
                        }
                        whiteKingCheckCount++;
                    }
                    else
                    {
                        int pinned_square = (DEBRUIJN64[MAGIC * (tempPinBitboard ^ (tempPinBitboard - 1)) >> 58]);
                        tempPinBitboard &= tempPinBitboard - 1;

                        if (tempPinBitboard == 0)
                        {
                            pinArraySquare[pinNumber] = pinned_square;
                            pinArrayPiece[pinNumber] = piece_square;
                            pinNumber++;
                        }
                    }
                    tempBitboard &= tempBitboard - 1;
                }

                //rook
                ulong rook_attacks = Constants.GetRookAttacksFast(whiteKingPosition, BLACK_OCCUPANCIES_LOCAL);
                tempBitboard = bitboard_array_global[BR] & rook_attacks;
                while (tempBitboard != 0)
                {
                    int piece_square = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];

                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[whiteKingPosition, piece_square] & WHITE_OCCUPANCIES_LOCAL;

                    if (tempPinBitboard == 0)
                    {
                        if (checkBitboard == 0)
                        {
                            checkBitboard = Constants.INBETWEEN_BITBOARDS[whiteKingPosition, piece_square];
                        }
                        whiteKingCheckCount++;
                    }
                    else
                    {
                        int pinned_square = (DEBRUIJN64[MAGIC * (tempPinBitboard ^ (tempPinBitboard - 1)) >> 58]);
                        tempPinBitboard &= tempPinBitboard - 1;

                        if (tempPinBitboard == 0)
                        {
                            pinArraySquare[pinNumber] = pinned_square;
                            pinArrayPiece[pinNumber] = piece_square;
                            pinNumber++;
                        }
                    }
                    tempBitboard &= tempBitboard - 1;
                }

                //queen
                tempBitboard = bitboard_array_global[BQ] & rook_attacks;
                while (tempBitboard != 0)
                {
                    int piece_square = (DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58]);

                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[whiteKingPosition, piece_square] & WHITE_OCCUPANCIES_LOCAL;

                    if (tempPinBitboard == 0)
                    {
                        if (checkBitboard == 0)
                        {
                            checkBitboard = Constants.INBETWEEN_BITBOARDS[whiteKingPosition, piece_square];
                        }
                        whiteKingCheckCount++;
                    }
                    else
                    {
                        int pinned_square = (DEBRUIJN64[MAGIC * (tempPinBitboard ^ (tempPinBitboard - 1)) >> 58]);
                        tempPinBitboard &= tempPinBitboard - 1;

                        if (tempPinBitboard == 0)
                        {
                            pinArraySquare[pinNumber] = pinned_square;
                            pinArrayPiece[pinNumber] = piece_square;
                            pinNumber++;
                        }
                    }
                    tempBitboard &= tempBitboard - 1;
                }

                #endregion

                if (whiteKingCheckCount > 1)
                {
                    #region White king double check

                    ulong occupanciesWithoutWhiteKing = COMBINED_OCCUPANCIES_LOCAL & (~bitboard_array_global[WK]);
                    tempAttack = Constants.KING_ATTACKS[whiteKingPosition];
                    tempEmpty = tempAttack & EMPTY_OCCUPANCIES;
                    while (tempEmpty != 0)
                    {
                        targetSquare = BitScanForward(tempEmpty);
                        tempEmpty &= tempEmpty - 1;

                        if ((bitboard_array_global[BP] & Constants.WHITE_PAWN_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[BN] & Constants.KNIGHT_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[BK] & Constants.KING_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        ulong bishopAttacks = Constants.GetBishopAttacksFast(targetSquare, occupanciesWithoutWhiteKing);
                        if ((bitboard_array_global[BB] & bishopAttacks) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[BQ] & bishopAttacks) != 0)
                        {
                            continue;
                        }
                        ulong rookAttacks = Constants.GetRookAttacksFast(targetSquare, occupanciesWithoutWhiteKing);
                        if ((bitboard_array_global[BR] & rookAttacks) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[BQ] & rookAttacks) != 0)
                        {
                            continue;
                        }

                        startingSquares[moveCount] = whiteKingPosition;
                        targetSquares[moveCount] = targetSquare;
                        tags[moveCount] = TAG_NONE;
                        pieces[moveCount] = WK;
                        moveCount++;
                    }

                    //captures
                    tempCaptures = tempAttack & BLACK_OCCUPANCIES_LOCAL;
                    while (tempCaptures != 0)
                    {
                        targetSquare = BitScanForward(tempCaptures);
                        tempCaptures &= tempCaptures - 1;

                        if ((bitboard_array_global[BP] & Constants.WHITE_PAWN_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[BN] & Constants.KNIGHT_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[BK] & Constants.KING_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        ulong bishopAttacks = Constants.GetBishopAttacksFast(targetSquare, occupanciesWithoutWhiteKing);
                        if ((bitboard_array_global[BB] & bishopAttacks) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[BQ] & bishopAttacks) != 0)
                        {
                            continue;
                        }
                        ulong rookAttacks = Constants.GetRookAttacksFast(targetSquare, occupanciesWithoutWhiteKing);
                        if ((bitboard_array_global[BR] & rookAttacks) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[BQ] & rookAttacks) != 0)
                        {
                            continue;
                        }

                        startingSquares[moveCount] = whiteKingPosition;
                        targetSquares[moveCount] = targetSquare;
                        tags[moveCount] = TAG_CAPTURE;
                        pieces[moveCount] = WK;
                        moveCount++;
                    }

                    #endregion
                }
                else
                {

                    if (whiteKingCheckCount == 0)
                    {
                        checkBitboard = ulong.MaxValue;
                    }

                    #region White king
                    ulong occupanciesWithoutWhiteKing = COMBINED_OCCUPANCIES_LOCAL & (~bitboard_array_global[WK]);
                    tempAttack = Constants.KING_ATTACKS[whiteKingPosition];
                    tempEmpty = tempAttack & EMPTY_OCCUPANCIES;
                    while (tempEmpty != 0)
                    {
                        targetSquare = BitScanForward(tempEmpty);
                        tempEmpty &= tempEmpty - 1;

                        if ((bitboard_array_global[BP] & Constants.WHITE_PAWN_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[BN] & Constants.KNIGHT_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[BK] & Constants.KING_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        ulong bishopAttacks = Constants.GetBishopAttacksFast(targetSquare, occupanciesWithoutWhiteKing);
                        if ((bitboard_array_global[BB] & bishopAttacks) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[BQ] & bishopAttacks) != 0)
                        {
                            continue;
                        }
                        ulong rookAttacks = Constants.GetRookAttacksFast(targetSquare, occupanciesWithoutWhiteKing);
                        if ((bitboard_array_global[BR] & rookAttacks) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[BQ] & rookAttacks) != 0)
                        {
                            continue;
                        }

                        startingSquares[moveCount] = whiteKingPosition;
                        targetSquares[moveCount] = targetSquare;
                        tags[moveCount] = TAG_NONE;
                        pieces[moveCount] = WK;
                        moveCount++;
                    }

                    //captures
                    tempCaptures = tempAttack & BLACK_OCCUPANCIES_LOCAL;
                    while (tempCaptures != 0)
                    {
                        targetSquare = BitScanForward(tempCaptures);
                        tempCaptures &= tempCaptures - 1;

                        if ((bitboard_array_global[BP] & Constants.WHITE_PAWN_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[BN] & Constants.KNIGHT_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[BK] & Constants.KING_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        ulong bishopAttacks = Constants.GetBishopAttacksFast(targetSquare, occupanciesWithoutWhiteKing);
                        if ((bitboard_array_global[BB] & bishopAttacks) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[BQ] & bishopAttacks) != 0)
                        {
                            continue;
                        }
                        ulong rookAttacks = Constants.GetRookAttacksFast(targetSquare, occupanciesWithoutWhiteKing);
                        if ((bitboard_array_global[BR] & rookAttacks) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[BQ] & rookAttacks) != 0)
                        {
                            continue;
                        }

                        startingSquares[moveCount] = whiteKingPosition;
                        targetSquares[moveCount] = targetSquare;
                        tags[moveCount] = TAG_CAPTURE;
                        pieces[moveCount] = WK;
                        moveCount++;
                    }

                    if (whiteKingCheckCount == 0)
                    {
                        if (castle_rights_global[WKS_CASTLE_RIGHTS] == true)
                        {
                            if (whiteKingPosition == E1) //king on e1
                            {
                                if ((WKS_EMPTY_BITBOARD & COMBINED_OCCUPANCIES_LOCAL) == 0) //f1 and g1 empty
                                {
                                    if ((bitboard_array_global[WR] & Constants.SQUARE_BBS[H1]) != 0) //rook on h1
                                    {
                                        if (Is_Square_Attacked_By_Black_Global(F1, COMBINED_OCCUPANCIES_LOCAL) == false)
                                        {
                                            if (Is_Square_Attacked_By_Black_Global(G1, COMBINED_OCCUPANCIES_LOCAL) == false)
                                            {
                                                startingSquares[moveCount] = E1;
                                                targetSquares[moveCount] = G1;
                                                tags[moveCount] = TAG_W_CASTLE_KS;
                                                pieces[moveCount] = WK;
                                                moveCount++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (castle_rights_global[WQS_CASTLE_RIGHTS] == true)
                        {
                            if (whiteKingPosition == E1) //king on e1
                            {
                                if ((WQS_EMPTY_BITBOARD & COMBINED_OCCUPANCIES_LOCAL) == 0) //f1 and g1 empty
                                {
                                    if ((bitboard_array_global[WR] & Constants.SQUARE_BBS[A1]) != 0) //rook on h1
                                    {
                                        if (Is_Square_Attacked_By_Black_Global(C1, COMBINED_OCCUPANCIES_LOCAL) == false)
                                        {
                                            if (Is_Square_Attacked_By_Black_Global(D1, COMBINED_OCCUPANCIES_LOCAL) == false)
                                            {
                                                startingSquares[moveCount] = E1;
                                                targetSquares[moveCount] = C1;
                                                tags[moveCount] = TAG_W_CASTLE_QS;
                                                pieces[moveCount] = WK;
                                                moveCount++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    #endregion

                    #region White knight

                    int blackKingSquare = DEBRUIJN64[MAGIC * (bitboard_array_global[BK] ^ (bitboard_array_global[BK] - 1)) >> 58];

                    tempBitboard = bitboard_array_global[WN];

                    while (tempBitboard != 0)
                    {
                        startingSquare = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];
                        tempBitboard &= tempBitboard - 1; //removes the knight from that square to not infinitely loop

                        tempPinBitboard = ulong.MaxValue;
                        if (pinNumber != 0)
                        {
                            for (int i = 0; i < pinNumber; i++)
                            {
                                if (pinArraySquare[i] == startingSquare)
                                {
                                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[whiteKingPosition, pinArrayPiece[i]];
                                }
                            }
                        }

                        tempAttack = ((Constants.KNIGHT_ATTACKS[startingSquare] & BLACK_OCCUPANCIES_LOCAL) & checkBitboard) & tempPinBitboard; //gets knight captures
                        while (tempAttack != 0)
                        {
                            targetSquare = (DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58]);
                            tempAttack &= tempAttack - 1;


                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = TAG_CAPTURE;
                            pieces[moveCount] = WN;
                            moveCount++;

                        }

                        tempAttack = ((Constants.KNIGHT_ATTACKS[startingSquare] & EMPTY_OCCUPANCIES) & checkBitboard) & tempPinBitboard;

                        while (tempAttack != 0)
                        {
                            targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                            tempAttack &= tempAttack - 1;

                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = TAG_NONE;
                            pieces[moveCount] = WN;
                            moveCount++;

                        }
                    }
                    #endregion

                    #region White pawn

                    ulong whitePawnCheckBitboard = Constants.BLACK_PAWN_ATTACKS[blackKingSquare];

                    tempBitboard = bitboard_array_global[WP];

                    while (tempBitboard != 0)
                    {
                        startingSquare = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];
                        tempBitboard &= tempBitboard - 1;

                        tempPinBitboard = ulong.MaxValue;
                        if (pinNumber != 0)
                        {
                            for (int i = 0; i < pinNumber; i++)
                            {
                                if (pinArraySquare[i] == startingSquare)
                                {
                                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[whiteKingPosition, pinArrayPiece[i]];
                                }
                            }
                        }

                        #region Pawn forward

                        if ((Constants.SQUARE_BBS[startingSquare - 8] & COMBINED_OCCUPANCIES_LOCAL) == 0) //if up one square is empty
                        {
                            if (((Constants.SQUARE_BBS[startingSquare - 8] & checkBitboard) & tempPinBitboard) != 0)
                            {
                                if ((Constants.SQUARE_BBS[startingSquare] & Constants.RANK_7_BITBOARD) != 0) //if promotion
                                {

                                    startingSquares[moveCount] = startingSquare;
                                    targetSquares[moveCount] = startingSquare - 8;
                                    tags[moveCount] = TAG_W_Q_PROMOTION;
                                    pieces[moveCount] = WP;
                                    moveCount++;

                                    startingSquares[moveCount] = startingSquare;
                                    targetSquares[moveCount] = startingSquare - 8;
                                    tags[moveCount] = TAG_W_R_PROMOTION;
                                    pieces[moveCount] = WP;
                                    moveCount++;

                                    startingSquares[moveCount] = startingSquare;
                                    targetSquares[moveCount] = startingSquare - 8;
                                    tags[moveCount] = TAG_W_B_PROMOTION;
                                    pieces[moveCount] = WP;
                                    moveCount++;

                                    startingSquares[moveCount] = startingSquare;
                                    targetSquares[moveCount] = startingSquare - 8;
                                    tags[moveCount] = TAG_W_N_PROMOTION;
                                    pieces[moveCount] = WP;
                                    moveCount++;

                                }
                                else
                                {

                                    if ((whitePawnCheckBitboard & Constants.SQUARE_BBS[startingSquare - 8]) != 0)
                                    {
                                        startingSquares[moveCount] = startingSquare;
                                        targetSquares[moveCount] = startingSquare - 8;
                                        tags[moveCount] = TAG_CHECK;
                                        pieces[moveCount] = WP;
                                        moveCount++;
                                    }
                                    else
                                    {
                                        startingSquares[moveCount] = startingSquare;
                                        targetSquares[moveCount] = startingSquare - 8;
                                        tags[moveCount] = TAG_NONE;
                                        pieces[moveCount] = WP;
                                        moveCount++;
                                    }
                                }
                            }

                            if ((Constants.SQUARE_BBS[startingSquare] & Constants.RANK_2_BITBOARD) != 0) //if on rank 2
                            {
                                if (((Constants.SQUARE_BBS[startingSquare - 16] & checkBitboard) & tempPinBitboard) != 0) //if not pinned or 
                                {
                                    if (((Constants.SQUARE_BBS[startingSquare - 16]) & COMBINED_OCCUPANCIES_LOCAL) == 0) //if up two squares and one square are empty
                                    {
                                        startingSquares[moveCount] = startingSquare;
                                        targetSquares[moveCount] = startingSquare - 16;
                                        tags[moveCount] = TAG_W_P_DOUBLE;
                                        pieces[moveCount] = WP;
                                        moveCount++;
                                    }
                                }
                            }
                        }

                        #endregion

                        #region Pawn captures

                        tempAttack = ((Constants.WHITE_PAWN_ATTACKS[startingSquare] & BLACK_OCCUPANCIES_LOCAL) & checkBitboard) & tempPinBitboard; //if black piece diagonal to pawn

                        while (tempAttack != 0)
                        {
                            targetSquare = (DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58]);
                            tempAttack &= tempAttack - 1;

                            if ((Constants.SQUARE_BBS[startingSquare] & Constants.RANK_7_BITBOARD) != 0) //if promotion
                            {

                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = targetSquare;
                                tags[moveCount] = TAG_W_Q_PROMOTION_CAP;
                                pieces[moveCount] = WP;
                                moveCount++;

                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = targetSquare;
                                tags[moveCount] = TAG_W_R_PROMOTION_CAP;
                                pieces[moveCount] = WP;
                                moveCount++;

                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = targetSquare;
                                tags[moveCount] = TAG_W_B_PROMOTION_CAP;
                                pieces[moveCount] = WP;
                                moveCount++;

                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = targetSquare;
                                tags[moveCount] = TAG_W_N_PROMOTION_CAP;
                                pieces[moveCount] = WP;
                                moveCount++;

                            }
                            else
                            {

                                if ((whitePawnCheckBitboard & Constants.SQUARE_BBS[targetSquare]) != 0)
                                {
                                    startingSquares[moveCount] = startingSquare;
                                    targetSquares[moveCount] = targetSquare;
                                    tags[moveCount] = TAG_CHECK_CAP;
                                    pieces[moveCount] = WP;
                                    moveCount++;
                                }
                                else
                                {
                                    startingSquares[moveCount] = startingSquare;
                                    targetSquares[moveCount] = targetSquare;
                                    tags[moveCount] = TAG_CAPTURE;
                                    pieces[moveCount] = WP;
                                    moveCount++;
                                }
                            }
                        }

                        if ((Constants.SQUARE_BBS[startingSquare] & Constants.RANK_5_BITBOARD) != 0) //check rank for ep
                        {
                            if (ep_global != NO_SQUARE)
                            {
                                if ((((Constants.WHITE_PAWN_ATTACKS[startingSquare] & Constants.SQUARE_BBS[ep_global]) & checkBitboard) & tempPinBitboard) != 0)
                                {
                                    if ((bitboard_array_global[WK] & Constants.RANK_5_BITBOARD) == 0) //if no king on rank 5
                                    {
                                        startingSquares[moveCount] = startingSquare;
                                        targetSquares[moveCount] = ep_global;
                                        tags[moveCount] = TAG_WHITE_EP;
                                        pieces[moveCount] = WP;
                                        moveCount++;
                                    }
                                    else if ((bitboard_array_global[BR] & Constants.RANK_5_BITBOARD) == 0 && (bitboard_array_global[BQ] & Constants.RANK_5_BITBOARD) == 0) // if no b rook or queen on rank 5
                                    {
                                        startingSquares[moveCount] = startingSquare;
                                        targetSquares[moveCount] = ep_global;
                                        tags[moveCount] = TAG_WHITE_EP;
                                        pieces[moveCount] = WP;
                                        moveCount++;
                                    }
                                    else //wk and br or bq on rank 5
                                    {
                                        ulong occupancyWithoutEPPawns = COMBINED_OCCUPANCIES_LOCAL & ~Constants.SQUARE_BBS[startingSquare];
                                        occupancyWithoutEPPawns &= ~Constants.SQUARE_BBS[ep_global + 8];

                                        ulong rookAttacksFromKing = Constants.GetRookAttacksFast(whiteKingPosition, occupancyWithoutEPPawns);

                                        if ((rookAttacksFromKing & bitboard_array_global[BR]) == 0)
                                        {
                                            if ((rookAttacksFromKing & bitboard_array_global[BQ]) == 0)
                                            {
                                                startingSquares[moveCount] = startingSquare;
                                                targetSquares[moveCount] = ep_global;
                                                tags[moveCount] = TAG_WHITE_EP;
                                                pieces[moveCount] = WP;
                                                moveCount++;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        #endregion
                    }

                    #endregion

                    #region White Rook

                    tempBitboard = bitboard_array_global[WR];
                    while (tempBitboard != 0)
                    {
                        startingSquare = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];
                        tempBitboard &= tempBitboard - 1;

                        tempPinBitboard = ulong.MaxValue;
                        if (pinNumber != 0)
                        {
                            for (int i = 0; i < pinNumber; i++)
                            {
                                if (pinArraySquare[i] == startingSquare)
                                {
                                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[whiteKingPosition, pinArrayPiece[i]];
                                }
                            }
                        }

                        ulong rookAttacks = Constants.GetRookAttacksFast(startingSquare, COMBINED_OCCUPANCIES_LOCAL);

                        tempAttack = ((rookAttacks & BLACK_OCCUPANCIES_LOCAL) & checkBitboard) & tempPinBitboard;
                        while (tempAttack != 0)
                        {
                            targetSquare = (DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58]);
                            tempAttack &= tempAttack - 1;


                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = TAG_CAPTURE;
                            pieces[moveCount] = WR;
                            moveCount++;


                        }

                        tempAttack = ((rookAttacks & EMPTY_OCCUPANCIES) & checkBitboard) & tempPinBitboard;
                        while (tempAttack != 0)
                        {
                            targetSquare = (DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58]);
                            tempAttack &= tempAttack - 1;

                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = TAG_NONE;
                            pieces[moveCount] = WR;
                            moveCount++;

                        }
                    }
                    #endregion

                    #region White bishop

                    tempBitboard = bitboard_array_global[WB];
                    while (tempBitboard != 0)
                    {
                        startingSquare = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];
                        tempBitboard &= tempBitboard - 1;

                        tempPinBitboard = ulong.MaxValue;
                        if (pinNumber != 0)
                        {
                            for (int i = 0; i < pinNumber; i++)
                            {
                                if (pinArraySquare[i] == startingSquare)
                                {
                                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[whiteKingPosition, pinArrayPiece[i]];
                                }
                            }
                        }

                        ulong bishopAttacks = Constants.GetBishopAttacksFast(startingSquare, COMBINED_OCCUPANCIES_LOCAL);

                        tempAttack = ((bishopAttacks & BLACK_OCCUPANCIES_LOCAL) & checkBitboard) & tempPinBitboard;
                        while (tempAttack != 0)
                        {
                            targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                            tempAttack &= tempAttack - 1;

                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = TAG_CAPTURE;
                            pieces[moveCount] = WB;
                            moveCount++;

                        }

                        tempAttack = ((bishopAttacks & EMPTY_OCCUPANCIES) & checkBitboard) & tempPinBitboard;
                        while (tempAttack != 0)
                        {
                            targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                            tempAttack &= tempAttack - 1;


                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = TAG_NONE;
                            pieces[moveCount] = WB;
                            moveCount++;

                        }
                    }
                    #endregion

                    #region White Queen

                    tempBitboard = bitboard_array_global[WQ];
                    while (tempBitboard != 0)
                    {
                        startingSquare = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];
                        tempBitboard &= tempBitboard - 1;

                        tempPinBitboard = ulong.MaxValue;
                        if (pinNumber != 0)
                        {
                            for (int i = 0; i < pinNumber; i++)
                            {
                                if (pinArraySquare[i] == startingSquare)
                                {
                                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[whiteKingPosition, pinArrayPiece[i]];
                                }
                            }
                        }

                        ulong queenAttacks = Constants.GetRookAttacksFast(startingSquare, COMBINED_OCCUPANCIES_LOCAL);
                        queenAttacks |= Constants.GetBishopAttacksFast(startingSquare, COMBINED_OCCUPANCIES_LOCAL);

                        tempAttack = ((queenAttacks & BLACK_OCCUPANCIES_LOCAL) & checkBitboard) & tempPinBitboard;

                        while (tempAttack != 0)
                        {
                            targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                            tempAttack &= tempAttack - 1;

                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = TAG_CAPTURE;
                            pieces[moveCount] = WQ;
                            moveCount++;

                        }

                        tempAttack = ((queenAttacks & EMPTY_OCCUPANCIES) & checkBitboard) & tempPinBitboard;
                        while (tempAttack != 0)
                        {
                            targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                            tempAttack &= tempAttack - 1;


                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = TAG_NONE;
                            pieces[moveCount] = WQ;
                            moveCount++;

                        }
                    }
                    #endregion
                }
            }
            else //black move
            {
                int blackKingCheckCount = 0;
                int blackKingPosition = BitScanForward(bitboard_array_global[BK]);

                #region pins and check

                //pawns
                tempBitboard = bitboard_array_global[WP] & Constants.BLACK_PAWN_ATTACKS[blackKingPosition];
                if (tempBitboard != 0)
                {
                    int pawn_square = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];

                    if (checkBitboard == 0)
                    {
                        checkBitboard = Constants.SQUARE_BBS[pawn_square];
                    }

                    blackKingCheckCount++;
                }

                //knights
                tempBitboard = bitboard_array_global[WN] & Constants.KNIGHT_ATTACKS[blackKingPosition];
                if (tempBitboard != 0)
                {
                    int knight_square = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];

                    if (checkBitboard == 0)
                    {
                        checkBitboard = Constants.SQUARE_BBS[knight_square];
                    }

                    blackKingCheckCount++;
                }

                //bishops
                ulong bishopAttacksChecks = Constants.GetBishopAttacksFast(blackKingPosition, WHITE_OCCUPANCIES_LOCAL);
                tempBitboard = bitboard_array_global[WB] & bishopAttacksChecks;
                while (tempBitboard != 0)
                {
                    int piece_square = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];

                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[blackKingPosition, piece_square] & BLACK_OCCUPANCIES_LOCAL;

                    if (tempPinBitboard == 0)
                    {
                        if (checkBitboard == 0)
                        {
                            checkBitboard = Constants.INBETWEEN_BITBOARDS[blackKingPosition, piece_square];
                        }
                        blackKingCheckCount++;
                    }
                    else
                    {
                        int pinned_square = (DEBRUIJN64[MAGIC * (tempPinBitboard ^ (tempPinBitboard - 1)) >> 58]);
                        tempPinBitboard &= tempPinBitboard - 1;

                        if (tempPinBitboard == 0)
                        {
                            pinArraySquare[pinNumber] = pinned_square;
                            pinArrayPiece[pinNumber] = piece_square;
                            pinNumber++;
                        }
                    }
                    tempBitboard &= tempBitboard - 1;
                }

                //queen
                tempBitboard = bitboard_array_global[WQ] & bishopAttacksChecks;
                while (tempBitboard != 0)
                {
                    int piece_square = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];

                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[blackKingPosition, piece_square] & BLACK_OCCUPANCIES_LOCAL;

                    if (tempPinBitboard == 0)
                    {
                        if (checkBitboard == 0)
                        {
                            checkBitboard = Constants.INBETWEEN_BITBOARDS[blackKingPosition, piece_square];
                        }
                        blackKingCheckCount++;
                    }
                    else
                    {
                        int pinned_square = (DEBRUIJN64[MAGIC * (tempPinBitboard ^ (tempPinBitboard - 1)) >> 58]);
                        tempPinBitboard &= tempPinBitboard - 1;

                        if (tempPinBitboard == 0)
                        {
                            pinArraySquare[pinNumber] = pinned_square;
                            pinArrayPiece[pinNumber] = piece_square;
                            pinNumber++;
                        }
                    }
                    tempBitboard &= tempBitboard - 1;
                }

                //rook
                ulong rook_attacks = Constants.GetRookAttacksFast(blackKingPosition, WHITE_OCCUPANCIES_LOCAL);
                tempBitboard = bitboard_array_global[WR] & rook_attacks;
                while (tempBitboard != 0)
                {
                    int piece_square = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];

                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[blackKingPosition, piece_square] & BLACK_OCCUPANCIES_LOCAL;

                    if (tempPinBitboard == 0)
                    {
                        if (checkBitboard == 0)
                        {
                            checkBitboard = Constants.INBETWEEN_BITBOARDS[blackKingPosition, piece_square];
                        }
                        blackKingCheckCount++;
                    }
                    else
                    {
                        int pinned_square = (DEBRUIJN64[MAGIC * (tempPinBitboard ^ (tempPinBitboard - 1)) >> 58]);
                        tempPinBitboard &= tempPinBitboard - 1;

                        if (tempPinBitboard == 0)
                        {
                            pinArraySquare[pinNumber] = pinned_square;
                            pinArrayPiece[pinNumber] = piece_square;
                            pinNumber++;
                        }
                    }
                    tempBitboard &= tempBitboard - 1;
                }

                //queen
                tempBitboard = bitboard_array_global[WQ] & rook_attacks;
                while (tempBitboard != 0)
                {
                    int piece_square = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];

                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[blackKingPosition, piece_square] & BLACK_OCCUPANCIES_LOCAL;

                    if (tempPinBitboard == 0)
                    {
                        if (checkBitboard == 0)
                        {
                            checkBitboard = Constants.INBETWEEN_BITBOARDS[blackKingPosition, piece_square];
                        }
                        blackKingCheckCount++;
                    }
                    else
                    {
                        int pinned_square = (DEBRUIJN64[MAGIC * (tempPinBitboard ^ (tempPinBitboard - 1)) >> 58]);
                        tempPinBitboard &= tempPinBitboard - 1;

                        if (tempPinBitboard == 0)
                        {
                            pinArraySquare[pinNumber] = pinned_square;
                            pinArrayPiece[pinNumber] = piece_square;
                            pinNumber++;
                        }
                    }
                    tempBitboard &= tempBitboard - 1;
                }

                #endregion

                if (blackKingCheckCount > 1)
                {
                    #region Black king
                    ulong occupancyWithoutBlackKing = COMBINED_OCCUPANCIES_LOCAL & (~bitboard_array_global[BK]);

                    tempAttack = Constants.KING_ATTACKS[blackKingPosition] & WHITE_OCCUPANCIES_LOCAL;

                    while (tempAttack != 0)
                    {
                        targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                        tempAttack &= tempAttack - 1;

                        if ((bitboard_array_global[WP] & Constants.BLACK_PAWN_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[WN] & Constants.KNIGHT_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[WK] & Constants.KING_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        ulong bishopAttacks = Constants.GetBishopAttacksFast(targetSquare, occupancyWithoutBlackKing);
                        if ((bitboard_array_global[WB] & bishopAttacks) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[WQ] & bishopAttacks) != 0)
                        {
                            continue;
                        }
                        ulong rookAttacks = Constants.GetRookAttacksFast(targetSquare, occupancyWithoutBlackKing);
                        if ((bitboard_array_global[WR] & rookAttacks) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[WQ] & rookAttacks) != 0)
                        {
                            continue;
                        }

                        startingSquares[moveCount] = blackKingPosition;
                        targetSquares[moveCount] = targetSquare;
                        tags[moveCount] = TAG_CAPTURE;
                        pieces[moveCount] = BK;
                        moveCount++;

                    }

                    tempAttack = Constants.KING_ATTACKS[blackKingPosition] & ~COMBINED_OCCUPANCIES_LOCAL;

                    while (tempAttack != 0)
                    {
                        targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                        tempAttack &= tempAttack - 1;

                        if ((bitboard_array_global[WP] & Constants.WHITE_PAWN_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[WN] & Constants.KNIGHT_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[WK] & Constants.KING_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        ulong bishopAttacks = Constants.GetBishopAttacksFast(targetSquare, occupancyWithoutBlackKing);
                        if ((bitboard_array_global[WB] & bishopAttacks) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[WQ] & bishopAttacks) != 0)
                        {
                            continue;
                        }
                        ulong rookAttacks = Constants.GetRookAttacksFast(targetSquare, occupancyWithoutBlackKing);
                        if ((bitboard_array_global[WR] & rookAttacks) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[WQ] & rookAttacks) != 0)
                        {
                            continue;
                        }

                        startingSquares[moveCount] = blackKingPosition;
                        targetSquares[moveCount] = targetSquare;
                        tags[moveCount] = TAG_NONE;
                        pieces[moveCount] = BK;
                        moveCount++;

                    }
                    #endregion
                }
                else
                {
                    if (blackKingCheckCount == 0)
                    {
                        checkBitboard = ulong.MaxValue;
                    }

                    #region Black pawns

                    tempBitboard = bitboard_array_global[BP];

                    while (tempBitboard != 0)
                    {
                        startingSquare = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];
                        tempBitboard &= tempBitboard - 1;

                        tempPinBitboard = ulong.MaxValue;
                        if (pinNumber != 0)
                        {
                            for (int i = 0; i < pinNumber; i++)
                            {
                                if (pinArraySquare[i] == startingSquare)
                                {
                                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[blackKingPosition, pinArrayPiece[i]];
                                }
                            }
                        }

                        #region Pawn forward

                        if ((Constants.SQUARE_BBS[startingSquare + 8] & COMBINED_OCCUPANCIES_LOCAL) == 0) //if up one square is empty
                        {
                            if (((Constants.SQUARE_BBS[startingSquare + 8] & checkBitboard) & tempPinBitboard) != 0)
                            {
                                if ((Constants.SQUARE_BBS[startingSquare] & Constants.RANK_2_BITBOARD) != 0) //if promotion
                                {
                                    startingSquares[moveCount] = startingSquare;
                                    targetSquares[moveCount] = startingSquare + 8;
                                    tags[moveCount] = TAG_B_B_PROMOTION;
                                    pieces[moveCount] = BP;
                                    moveCount++;

                                    startingSquares[moveCount] = startingSquare;
                                    targetSquares[moveCount] = startingSquare + 8;
                                    tags[moveCount] = TAG_B_N_PROMOTION;
                                    pieces[moveCount] = BP;
                                    moveCount++;

                                    startingSquares[moveCount] = startingSquare;
                                    targetSquares[moveCount] = startingSquare + 8;
                                    tags[moveCount] = TAG_B_R_PROMOTION;
                                    pieces[moveCount] = BP;
                                    moveCount++;

                                    startingSquares[moveCount] = startingSquare;
                                    targetSquares[moveCount] = startingSquare + 8;
                                    tags[moveCount] = TAG_B_Q_PROMOTION;
                                    pieces[moveCount] = BP;
                                    moveCount++;
                                }
                                else
                                {
                                    startingSquares[moveCount] = startingSquare;
                                    targetSquares[moveCount] = startingSquare + 8;
                                    tags[moveCount] = TAG_NONE;
                                    pieces[moveCount] = BP;
                                    moveCount++;
                                }
                            }

                            if ((Constants.SQUARE_BBS[startingSquare] & Constants.RANK_7_BITBOARD) != 0) //if on rank 2
                            {
                                if (((Constants.SQUARE_BBS[startingSquare + 16] & checkBitboard) & tempPinBitboard) != 0)
                                {
                                    if (((Constants.SQUARE_BBS[startingSquare + 16]) & COMBINED_OCCUPANCIES_LOCAL) == 0) //if up two squares and one square are empty
                                    {
                                        startingSquares[moveCount] = startingSquare;
                                        targetSquares[moveCount] = startingSquare + 16;
                                        tags[moveCount] = TAG_B_P_DOUBLE;
                                        pieces[moveCount] = BP;
                                        moveCount++;
                                    }
                                }
                            }
                        }

                        #endregion

                        #region region Pawn captures

                        tempAttack = ((Constants.BLACK_PAWN_ATTACKS[startingSquare] & WHITE_OCCUPANCIES_LOCAL) & checkBitboard) & tempPinBitboard; //if black piece diagonal to pawn

                        while (tempAttack != 0)
                        {
                            targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58]; //find the bit
                            tempAttack &= tempAttack - 1;

                            if ((Constants.SQUARE_BBS[startingSquare] & Constants.RANK_2_BITBOARD) != 0) //if promotion
                            {
                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = targetSquare;
                                tags[moveCount] = TAG_B_B_PROMOTION_CAP;
                                pieces[moveCount] = BP;
                                moveCount++;

                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = targetSquare;
                                tags[moveCount] = TAG_B_N_PROMOTION_CAP;
                                pieces[moveCount] = BP;
                                moveCount++;

                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = targetSquare;
                                tags[moveCount] = TAG_B_R_PROMOTION_CAP;
                                pieces[moveCount] = BP;
                                moveCount++;

                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = targetSquare;
                                tags[moveCount] = TAG_B_Q_PROMOTION_CAP;
                                pieces[moveCount] = BP;
                                moveCount++;

                            }
                            else
                            {
                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = targetSquare;
                                tags[moveCount] = TAG_CAPTURE;
                                pieces[moveCount] = BP;
                                moveCount++;
                            }
                        }

                        if ((Constants.SQUARE_BBS[startingSquare] & Constants.RANK_4_BITBOARD) != 0) //check rank for ep
                        {
                            if (ep_global != NO_SQUARE)
                            {
                                if ((((Constants.BLACK_PAWN_ATTACKS[startingSquare] & Constants.SQUARE_BBS[ep_global]) & checkBitboard) & tempPinBitboard) != 0)
                                {
                                    if ((bitboard_array_global[BK] & Constants.RANK_4_BITBOARD) == 0) //if no king on rank 5
                                    {
                                        startingSquares[moveCount] = startingSquare;
                                        targetSquares[moveCount] = ep_global;
                                        tags[moveCount] = TAG_BLACK_EP;
                                        pieces[moveCount] = BP;
                                        moveCount++;
                                    }
                                    else if ((bitboard_array_global[WR] & Constants.RANK_4_BITBOARD) == 0 && (bitboard_array_global[WQ] & Constants.RANK_4_BITBOARD) == 0) // if no b rook or queen on rank 5
                                    {
                                        startingSquares[moveCount] = startingSquare;
                                        targetSquares[moveCount] = ep_global;
                                        tags[moveCount] = TAG_BLACK_EP;
                                        pieces[moveCount] = BP;
                                        moveCount++;
                                    }
                                    else //wk and br or bq on rank 5
                                    {
                                        ulong occupancyWithoutEPPawns = COMBINED_OCCUPANCIES_LOCAL & ~Constants.SQUARE_BBS[startingSquare];
                                        occupancyWithoutEPPawns &= ~Constants.SQUARE_BBS[ep_global - 8];

                                        ulong rookAttacksFromKing = Constants.GetRookAttacksFast(blackKingPosition, occupancyWithoutEPPawns);

                                        if ((rookAttacksFromKing & bitboard_array_global[WR]) == 0)
                                        {
                                            if ((rookAttacksFromKing & bitboard_array_global[WQ]) == 0)
                                            {
                                                startingSquares[moveCount] = startingSquare;
                                                targetSquares[moveCount] = ep_global;
                                                tags[moveCount] = TAG_BLACK_EP;
                                                pieces[moveCount] = BP;
                                                moveCount++;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        #endregion
                    }
                    #endregion

                    #region black Knight

                    int whiteKingSquare = DEBRUIJN64[MAGIC * (bitboard_array_global[WK] ^ (bitboard_array_global[WK] - 1)) >> 58];

                    tempBitboard = bitboard_array_global[BN];

                    while (tempBitboard != 0)
                    {
                        startingSquare = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58]; //looks for the startingSquare
                        tempBitboard &= tempBitboard - 1; //removes the knight from that square to not infinitely loop

                        tempPinBitboard = ulong.MaxValue;
                        if (pinNumber != 0)
                        {
                            for (int i = 0; i < pinNumber; i++)
                            {
                                if (pinArraySquare[i] == startingSquare)
                                {
                                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[blackKingPosition, pinArrayPiece[i]];
                                }
                            }
                        }

                        tempAttack = ((Constants.KNIGHT_ATTACKS[startingSquare] & WHITE_OCCUPANCIES_LOCAL) & checkBitboard) & tempPinBitboard; //gets knight captures
                        while (tempAttack != 0)
                        {
                            targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                            tempAttack &= tempAttack - 1;


                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = TAG_CAPTURE;
                            pieces[moveCount] = BN;
                            moveCount++;

                        }

                        tempAttack = ((Constants.KNIGHT_ATTACKS[startingSquare] & (~COMBINED_OCCUPANCIES_LOCAL)) & checkBitboard) & tempPinBitboard;

                        while (tempAttack != 0)
                        {
                            targetSquare = (DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58]);
                            tempAttack &= tempAttack - 1;


                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = TAG_NONE;
                            pieces[moveCount] = BN;
                            moveCount++;

                        }
                    }
                    #endregion

                    #region Black bishop
                    ulong blackBishopCheckBitboard = Constants.GetBishopAttacksFast(whiteKingSquare, COMBINED_OCCUPANCIES_LOCAL);

                    tempBitboard = bitboard_array_global[BB];
                    while (tempBitboard != 0)
                    {
                        startingSquare = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];
                        tempBitboard &= tempBitboard - 1;

                        tempPinBitboard = ulong.MaxValue;
                        if (pinNumber != 0)
                        {
                            for (int i = 0; i < pinNumber; i++)
                            {
                                if (pinArraySquare[i] == startingSquare)
                                {
                                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[blackKingPosition, pinArrayPiece[i]];
                                }
                            }
                        }

                        ulong bishopAttacks = Constants.GetBishopAttacksFast(startingSquare, COMBINED_OCCUPANCIES_LOCAL);

                        tempAttack = ((bishopAttacks & WHITE_OCCUPANCIES_LOCAL) & checkBitboard) & tempPinBitboard;
                        while (tempAttack != 0)
                        {
                            targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                            tempAttack &= tempAttack - 1;


                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = TAG_CAPTURE;
                            pieces[moveCount] = BB;
                            moveCount++;

                        }

                        tempAttack = ((bishopAttacks & (~COMBINED_OCCUPANCIES_LOCAL)) & checkBitboard) & tempPinBitboard;
                        while (tempAttack != 0)
                        {
                            targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                            tempAttack &= tempAttack - 1;


                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = TAG_NONE;
                            pieces[moveCount] = BB;
                            moveCount++;

                        }
                    }
                    #endregion

                    #region Black Rook
                    tempBitboard = bitboard_array_global[BR];
                    while (tempBitboard != 0)
                    {
                        startingSquare = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];
                        tempBitboard &= tempBitboard - 1;

                        tempPinBitboard = ulong.MaxValue;
                        if (pinNumber != 0)
                        {
                            for (int i = 0; i < pinNumber; i++)
                            {
                                if (pinArraySquare[i] == startingSquare)
                                {
                                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[blackKingPosition, pinArrayPiece[i]];
                                }
                            }
                        }

                        ulong rookAttacks = Constants.GetRookAttacksFast(startingSquare, COMBINED_OCCUPANCIES_LOCAL);

                        tempAttack = ((rookAttacks & WHITE_OCCUPANCIES_LOCAL) & checkBitboard) & tempPinBitboard;
                        while (tempAttack != 0)
                        {
                            targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                            tempAttack &= tempAttack - 1;


                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = TAG_CAPTURE;
                            pieces[moveCount] = BR;
                            moveCount++;

                        }

                        tempAttack = ((rookAttacks & (~COMBINED_OCCUPANCIES_LOCAL)) & checkBitboard) & tempPinBitboard;
                        while (tempAttack != 0)
                        {
                            targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                            tempAttack &= tempAttack - 1;


                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = TAG_NONE;
                            pieces[moveCount] = BR;
                            moveCount++;

                        }
                    }
                    #endregion

                    #region Black queen

                    tempBitboard = bitboard_array_global[BQ];
                    while (tempBitboard != 0)
                    {
                        startingSquare = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];
                        tempBitboard &= tempBitboard - 1;

                        tempPinBitboard = ulong.MaxValue;
                        if (pinNumber != 0)
                        {
                            for (int i = 0; i < pinNumber; i++)
                            {
                                if (pinArraySquare[i] == startingSquare)
                                {
                                    tempPinBitboard = Constants.INBETWEEN_BITBOARDS[blackKingPosition, pinArrayPiece[i]];
                                }
                            }
                        }

                        ulong queenAttacks = Constants.GetRookAttacksFast(startingSquare, COMBINED_OCCUPANCIES_LOCAL);
                        queenAttacks |= Constants.GetBishopAttacksFast(startingSquare, COMBINED_OCCUPANCIES_LOCAL);

                        tempAttack = ((queenAttacks & WHITE_OCCUPANCIES_LOCAL) & checkBitboard) & tempPinBitboard;

                        while (tempAttack != 0)
                        {
                            targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                            tempAttack &= tempAttack - 1;


                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = TAG_CAPTURE;
                            pieces[moveCount] = BQ;
                            moveCount++;

                        }

                        tempAttack = ((queenAttacks & (~COMBINED_OCCUPANCIES_LOCAL)) & checkBitboard) & tempPinBitboard;
                        while (tempAttack != 0)
                        {
                            targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                            tempAttack &= tempAttack - 1;


                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = TAG_NONE;
                            pieces[moveCount] = BQ;
                            moveCount++;

                        }
                    }
                    #endregion

                    #region Black King

                    tempAttack = Constants.KING_ATTACKS[blackKingPosition] & WHITE_OCCUPANCIES_LOCAL; //gets knight captures
                    while (tempAttack != 0)
                    {
                        targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                        tempAttack &= tempAttack - 1;

                        if ((bitboard_array_global[WP] & Constants.BLACK_PAWN_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[WN] & Constants.KNIGHT_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[WK] & Constants.KING_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        ulong occupancyWithoutBlackKing = COMBINED_OCCUPANCIES_LOCAL & (~bitboard_array_global[BK]);
                        ulong bishopAttacks = Constants.GetBishopAttacksFast(targetSquare, occupancyWithoutBlackKing);
                        if ((bitboard_array_global[WB] & bishopAttacks) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[WQ] & bishopAttacks) != 0)
                        {
                            continue;
                        }
                        ulong rookAttacks = Constants.GetRookAttacksFast(targetSquare, occupancyWithoutBlackKing);
                        if ((bitboard_array_global[WR] & rookAttacks) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[WQ] & rookAttacks) != 0)
                        {
                            continue;
                        }

                        startingSquares[moveCount] = blackKingPosition;
                        targetSquares[moveCount] = targetSquare;
                        tags[moveCount] = TAG_CAPTURE;
                        pieces[moveCount] = BK;
                        moveCount++;

                    }

                    tempAttack = Constants.KING_ATTACKS[blackKingPosition] & (~COMBINED_OCCUPANCIES_LOCAL); //get knight moves to emtpy squares

                    while (tempAttack != 0)
                    {
                        targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                        tempAttack &= tempAttack - 1;

                        if ((bitboard_array_global[WP] & Constants.BLACK_PAWN_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[WN] & Constants.KNIGHT_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[WK] & Constants.KING_ATTACKS[targetSquare]) != 0)
                        {
                            continue;
                        }
                        ulong occupancyWithoutBlackKing = COMBINED_OCCUPANCIES_LOCAL & (~bitboard_array_global[BK]);
                        ulong bishopAttacks = Constants.GetBishopAttacksFast(targetSquare, occupancyWithoutBlackKing);
                        if ((bitboard_array_global[WB] & bishopAttacks) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[WQ] & bishopAttacks) != 0)
                        {
                            continue;
                        }
                        ulong rookAttacks = Constants.GetRookAttacksFast(targetSquare, occupancyWithoutBlackKing);
                        if ((bitboard_array_global[WR] & rookAttacks) != 0)
                        {
                            continue;
                        }
                        if ((bitboard_array_global[WQ] & rookAttacks) != 0)
                        {
                            continue;
                        }

                        startingSquares[moveCount] = blackKingPosition;
                        targetSquares[moveCount] = targetSquare;
                        tags[moveCount] = TAG_NONE;
                        pieces[moveCount] = BK;
                        moveCount++;

                    }
                }
                if (blackKingCheckCount == 0)
                {
                    if (castle_rights_global[BKS_CASTLE_RIGHTS] == true)
                    {
                        if (blackKingPosition == E8) //king on e1
                        {
                            if ((BKS_EMPTY_BITBOARD & COMBINED_OCCUPANCIES_LOCAL) == 0) //f1 and g1 empty
                            {
                                if ((bitboard_array_global[BR] & Constants.SQUARE_BBS[H8]) != 0) //rook on h1
                                {
                                    if (Is_Square_Attacked_By_White_Global(F8, COMBINED_OCCUPANCIES_LOCAL) == false)
                                    {
                                        if (Is_Square_Attacked_By_White_Global(G8, COMBINED_OCCUPANCIES_LOCAL) == false)
                                        {
                                            startingSquares[moveCount] = E8;
                                            targetSquares[moveCount] = G8;
                                            tags[moveCount] = TAG_B_CASTLE_KS;
                                            pieces[moveCount] = BK;
                                            moveCount++;

                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (castle_rights_global[BQS_CASTLE_RIGHTS] == true)
                    {
                        if (blackKingPosition == E8) //king on e1
                        {
                            if ((BQS_EMPTY_BITBOARD & COMBINED_OCCUPANCIES_LOCAL) == 0) //f1 and g1 empty
                            {
                                if ((bitboard_array_global[BR] & Constants.SQUARE_BBS[A8]) != 0) //rook on h1
                                {
                                    if (Is_Square_Attacked_By_White_Global(C8, COMBINED_OCCUPANCIES_LOCAL) == false)
                                    {
                                        if (Is_Square_Attacked_By_White_Global(D8, COMBINED_OCCUPANCIES_LOCAL) == false)
                                        {
                                            startingSquares[moveCount] = E8;
                                            targetSquares[moveCount] = C8;
                                            tags[moveCount] = TAG_B_CASTLE_QS;
                                            pieces[moveCount] = BK;
                                            moveCount++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                #endregion
            }

            #endregion


            if (depth == 1)
            {
                return moveCount;
            }

            int nodes = 0, priorNodes;
            int copyEp = ep_global;
            bool[] copy_castle = { castle_rights_global[0], castle_rights_global[1], castle_rights_global[2], castle_rights_global[3] };

            for (int move_index = 0; move_index < moveCount; ++move_index)
            {
                int startingSquareCopy = startingSquares[move_index];
                int targetSquareCopy = targetSquares[move_index];
                int piece = pieces[move_index];
                int tag = tags[move_index];

                int captureIndex = -1;

                #region Makemove

                if (is_white_global == true)
                {
                    is_white_global = false;
                }
                else
                {
                    is_white_global = true;
                }

                switch (tag)
                {
                    case 0: //none
                    case 26: //check
                        bitboard_array_global[piece] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = NO_SQUARE;
                        break;
                    case 1: //capture
                    case 27: //check cap
                        bitboard_array_global[piece] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        if (piece >= WP && piece <= WK)
                        {
                            for (int i = BLACK_START_INDEX; i <= BLACK_END_INDEX; ++i)
                            {
                                if ((bitboard_array_global[i] & Constants.SQUARE_BBS[targetSquareCopy]) != 0)
                                {
                                    captureIndex = i;
                                    break;
                                }
                            }
                            bitboard_array_global[captureIndex] &= ~Constants.SQUARE_BBS[targetSquareCopy];

                        }
                        else //is black
                        {
                            for (int i = WHITE_START_INDEX; i <= WHITE_END_INDEX; ++i)
                            {
                                if ((bitboard_array_global[i] & Constants.SQUARE_BBS[targetSquareCopy]) != 0)
                                {
                                    captureIndex = i;
                                    break;
                                }
                            }
                            bitboard_array_global[captureIndex] &= ~Constants.SQUARE_BBS[targetSquareCopy];
                        }

                        ep_global = NO_SQUARE;
                        break;
                    case 2: //white ep
                            //move piece
                        bitboard_array_global[WP] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[WP] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        //remove 
                        bitboard_array_global[BP] &= ~Constants.SQUARE_BBS[targetSquareCopy + 8];
                        ep_global = NO_SQUARE;
                        break;
                    case 3: //black ep
                            //move piece
                        bitboard_array_global[BP] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[BP] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        //remove white pawn square up
                        bitboard_array_global[WP] &= ~Constants.SQUARE_BBS[targetSquareCopy - 8];
                        ep_global = NO_SQUARE;
                        break;

                    #region Castling

                    case 4: //WKS
                            //white king
                        bitboard_array_global[WK] |= Constants.SQUARE_BBS[G1];
                        bitboard_array_global[WK] &= ~Constants.SQUARE_BBS[E1];
                        //white rook
                        bitboard_array_global[WR] |= Constants.SQUARE_BBS[F1];
                        bitboard_array_global[WR] &= ~Constants.SQUARE_BBS[H1];
                        castle_rights_global[WKS_CASTLE_RIGHTS] = false;
                        castle_rights_global[WQS_CASTLE_RIGHTS] = false;
                        ep_global = NO_SQUARE;
                        break;
                    case 5: //WQS
                            //white king
                        bitboard_array_global[WK] |= Constants.SQUARE_BBS[C1];
                        bitboard_array_global[WK] &= ~Constants.SQUARE_BBS[E1];
                        //white rook
                        bitboard_array_global[WR] |= Constants.SQUARE_BBS[D1];
                        bitboard_array_global[WR] &= ~Constants.SQUARE_BBS[A1];
                        castle_rights_global[WKS_CASTLE_RIGHTS] = false;
                        castle_rights_global[WQS_CASTLE_RIGHTS] = false;
                        ep_global = NO_SQUARE;
                        break;
                    case 6: //BKS
                            //white king
                        bitboard_array_global[BK] |= Constants.SQUARE_BBS[G8];
                        bitboard_array_global[BK] &= ~Constants.SQUARE_BBS[E8];
                        //white rook
                        bitboard_array_global[BR] |= Constants.SQUARE_BBS[F8];
                        bitboard_array_global[BR] &= ~Constants.SQUARE_BBS[H8];

                        castle_rights_global[BKS_CASTLE_RIGHTS] = false;
                        castle_rights_global[BQS_CASTLE_RIGHTS] = false;
                        ep_global = NO_SQUARE;
                        break;
                    case 7: //BQS
                            //white king
                        bitboard_array_global[BK] |= Constants.SQUARE_BBS[C8];
                        bitboard_array_global[BK] &= ~Constants.SQUARE_BBS[E8];
                        //white rook
                        bitboard_array_global[BR] |= Constants.SQUARE_BBS[D8];
                        bitboard_array_global[BR] &= ~Constants.SQUARE_BBS[A8];
                        castle_rights_global[BKS_CASTLE_RIGHTS] = false;
                        castle_rights_global[BQS_CASTLE_RIGHTS] = false;
                        ep_global = NO_SQUARE;
                        break;

                    #endregion

                    #region Promotion makemove

                    case 8: //BNPr
                        bitboard_array_global[BN] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = NO_SQUARE;
                        break;
                    case 9: //BBPr
                        bitboard_array_global[BB] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = NO_SQUARE;
                        break;
                    case 10: //BQPr
                        bitboard_array_global[BQ] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = NO_SQUARE;
                        break;
                    case 11: //BRPr
                        bitboard_array_global[BR] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = NO_SQUARE;
                        break;
                    case 12: //WNPr
                        bitboard_array_global[WN] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = NO_SQUARE;
                        break;
                    case 13: //WBPr
                        bitboard_array_global[WB] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = NO_SQUARE;
                        break;
                    case 14: //WQPr
                        bitboard_array_global[WQ] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = NO_SQUARE;
                        break;
                    case 15: //WRPr
                        bitboard_array_global[WR] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = NO_SQUARE;
                        break;
                    case 16: //BNPrCAP
                        bitboard_array_global[BN] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = NO_SQUARE;
                        for (int i = WHITE_START_INDEX; i <= WHITE_END_INDEX; ++i)
                        {
                            if ((bitboard_array_global[i] & Constants.SQUARE_BBS[targetSquareCopy]) != 0)
                            {
                                captureIndex = i;
                                break;
                            }
                        }
                        bitboard_array_global[captureIndex] &= ~Constants.SQUARE_BBS[targetSquareCopy];

                        break;
                    case 17: //BBPrCAP
                        bitboard_array_global[BB] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = NO_SQUARE;
                        for (int i = WHITE_START_INDEX; i <= WHITE_END_INDEX; ++i)
                        {
                            if ((bitboard_array_global[i] & Constants.SQUARE_BBS[targetSquareCopy]) != 0)
                            {
                                captureIndex = i;
                                break;
                            }
                        }
                        bitboard_array_global[captureIndex] &= ~Constants.SQUARE_BBS[targetSquareCopy];

                        break;
                    case 18: //BQPrCAP
                        bitboard_array_global[BQ] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = NO_SQUARE;
                        for (int i = WHITE_START_INDEX; i <= WHITE_END_INDEX; ++i)
                        {
                            if ((bitboard_array_global[i] & Constants.SQUARE_BBS[targetSquareCopy]) != 0)
                            {
                                captureIndex = i;
                                break;
                            }
                        }
                        bitboard_array_global[captureIndex] &= ~Constants.SQUARE_BBS[targetSquareCopy];

                        break;
                    case 19: //BRPrCAP
                        bitboard_array_global[BR] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = NO_SQUARE;
                        for (int i = WHITE_START_INDEX; i <= WHITE_END_INDEX; ++i)
                        {
                            if ((bitboard_array_global[i] & Constants.SQUARE_BBS[targetSquareCopy]) != 0)
                            {
                                captureIndex = i;
                                break;
                            }
                        }
                        bitboard_array_global[captureIndex] &= ~Constants.SQUARE_BBS[targetSquareCopy];

                        break;
                    case 20: //WNPrCAP
                        bitboard_array_global[WN] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = NO_SQUARE;
                        for (int i = BLACK_START_INDEX; i <= BLACK_END_INDEX; ++i)
                        {
                            if ((bitboard_array_global[i] & Constants.SQUARE_BBS[targetSquareCopy]) != 0)
                            {
                                captureIndex = i;
                                break;
                            }
                        }
                        bitboard_array_global[captureIndex] &= ~Constants.SQUARE_BBS[targetSquareCopy];

                        break;
                    case 21: //WBPrCAP
                        bitboard_array_global[WB] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = NO_SQUARE;
                        for (int i = BLACK_START_INDEX; i <= BLACK_END_INDEX; ++i)
                        {
                            if ((bitboard_array_global[i] & Constants.SQUARE_BBS[targetSquareCopy]) != 0)
                            {
                                captureIndex = i;
                                break;
                            }
                        }
                        bitboard_array_global[captureIndex] &= ~Constants.SQUARE_BBS[targetSquareCopy];

                        break;
                    case 22: //WQPrCAP
                        bitboard_array_global[WQ] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = NO_SQUARE;
                        for (int i = BLACK_START_INDEX; i <= BLACK_END_INDEX; ++i)
                        {
                            if ((bitboard_array_global[i] & Constants.SQUARE_BBS[targetSquareCopy]) != 0)
                            {
                                captureIndex = i;
                                break;
                            }
                        }
                        bitboard_array_global[captureIndex] &= ~Constants.SQUARE_BBS[targetSquareCopy];

                        break;
                    case 23: //WRPrCAP
                        bitboard_array_global[WR] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[startingSquareCopy];

                        ep_global = NO_SQUARE;
                        for (int i = BLACK_START_INDEX; i <= BLACK_END_INDEX; ++i)
                        {
                            if ((bitboard_array_global[i] & Constants.SQUARE_BBS[targetSquareCopy]) != 0)
                            {
                                captureIndex = i;
                                break;
                            }
                        }
                        bitboard_array_global[captureIndex] &= ~Constants.SQUARE_BBS[targetSquareCopy];
                        break;

                    #endregion

                    case 24: //WDouble
                        bitboard_array_global[WP] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[WP] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = targetSquareCopy + 8;
                        break;
                    case 25: //BDouble
                        bitboard_array_global[BP] |= Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[BP] &= ~Constants.SQUARE_BBS[startingSquareCopy];
                        ep_global = targetSquareCopy - 8;
                        break;
                }

                if (piece == WK)
                {
                    castle_rights_global[WKS_CASTLE_RIGHTS] = false;
                    castle_rights_global[WQS_CASTLE_RIGHTS] = false;
                }
                else if (piece == BK)
                {
                    castle_rights_global[BKS_CASTLE_RIGHTS] = false;
                    castle_rights_global[BQS_CASTLE_RIGHTS] = false;
                }
                else if (piece == WR)
                {
                    if (castle_rights_global[WKS_CASTLE_RIGHTS] == true)
                    {
                        if ((bitboard_array_global[WR] & Constants.SQUARE_BBS[H1]) == 0)
                        {
                            castle_rights_global[WKS_CASTLE_RIGHTS] = false;
                        }
                    }
                    if (castle_rights_global[WQS_CASTLE_RIGHTS] == true)
                    {
                        if ((bitboard_array_global[WR] & Constants.SQUARE_BBS[A1]) == 0)
                        {
                            castle_rights_global[WQS_CASTLE_RIGHTS] = false;
                        }
                    }
                }
                else if (piece == BR)
                {
                    if (castle_rights_global[BKS_CASTLE_RIGHTS] == true)
                    {
                        if ((bitboard_array_global[BR] & Constants.SQUARE_BBS[H8]) == 0)
                        {
                            castle_rights_global[BKS_CASTLE_RIGHTS] = false;
                        }
                    }
                    if (castle_rights_global[BQS_CASTLE_RIGHTS] == true)
                    {
                        if ((bitboard_array_global[BR] & Constants.SQUARE_BBS[A8]) == 0)
                        {
                            castle_rights_global[BQS_CASTLE_RIGHTS] = false;
                        }
                    }
                }

                #endregion

                priorNodes = nodes;
                nodes += PerftInline(depth - 1, ply + 1);

                #region Unmakemove

                if (is_white_global == true)
                {
                    is_white_global = false;
                }
                else
                {
                    is_white_global = true;
                }

                switch (tag)
                {
                    case 0: //none
                    case 26: //check
                        bitboard_array_global[piece] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[targetSquareCopy];

                        break;
                    case 1: //capture
                    case 27: //check cap
                        bitboard_array_global[piece] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[piece] &= ~Constants.SQUARE_BBS[targetSquareCopy];
                        if (piece >= WP && piece <= WK)
                        {
                            bitboard_array_global[captureIndex] |= Constants.SQUARE_BBS[targetSquareCopy];
                        }
                        else //is black
                        {
                            bitboard_array_global[captureIndex] |= Constants.SQUARE_BBS[targetSquareCopy];
                        }

                        break;
                    case 2: //white ep
                        bitboard_array_global[WP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[WP] &= ~Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[BP] |= Constants.SQUARE_BBS[targetSquareCopy + 8];

                        break;
                    case 3: //black ep
                        bitboard_array_global[BP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[BP] &= ~Constants.SQUARE_BBS[targetSquareCopy];
                        bitboard_array_global[WP] |= Constants.SQUARE_BBS[targetSquareCopy - 8];

                        break;
                    case 4: //WKS
                            //white king
                        bitboard_array_global[WK] |= Constants.SQUARE_BBS[E1];
                        bitboard_array_global[WK] &= ~Constants.SQUARE_BBS[G1];
                        //white rook
                        bitboard_array_global[WR] |= Constants.SQUARE_BBS[H1];
                        bitboard_array_global[WR] &= ~Constants.SQUARE_BBS[F1];
                        break;
                    case 5: //WQS
                            //white king
                        bitboard_array_global[WK] |= Constants.SQUARE_BBS[E1];
                        bitboard_array_global[WK] &= ~Constants.SQUARE_BBS[C1];
                        //white rook
                        bitboard_array_global[WR] |= Constants.SQUARE_BBS[A1];
                        bitboard_array_global[WR] &= ~Constants.SQUARE_BBS[D1];
                        break;
                    case 6: //BKS
                            //white king
                        bitboard_array_global[BK] |= Constants.SQUARE_BBS[E8];
                        bitboard_array_global[BK] &= ~Constants.SQUARE_BBS[G8];
                        //white rook
                        bitboard_array_global[BR] |= Constants.SQUARE_BBS[H8];
                        bitboard_array_global[BR] &= ~Constants.SQUARE_BBS[F8];
                        break;
                    case 7: //BQS
                            //white king
                        bitboard_array_global[BK] |= Constants.SQUARE_BBS[E8];
                        bitboard_array_global[BK] &= ~Constants.SQUARE_BBS[C8];
                        //white rook
                        bitboard_array_global[BR] |= Constants.SQUARE_BBS[A8];
                        bitboard_array_global[BR] &= ~Constants.SQUARE_BBS[D8];

                        break;

                    #region Promotion Unmakemove
                    case 8: //BNPr
                        bitboard_array_global[BP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[BN] &= ~Constants.SQUARE_BBS[targetSquareCopy];
                        break;
                    case 9: //BBPr
                        bitboard_array_global[BP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[BB] &= ~Constants.SQUARE_BBS[targetSquareCopy];
                        break;
                    case 10: //BQPr
                        bitboard_array_global[BP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[BQ] &= ~Constants.SQUARE_BBS[targetSquareCopy];
                        break;
                    case 11: //BRPr
                        bitboard_array_global[BP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[BR] &= ~Constants.SQUARE_BBS[targetSquareCopy];
                        break;
                    case 12: //WNPr
                        bitboard_array_global[WP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[WN] &= ~Constants.SQUARE_BBS[targetSquareCopy];
                        break;
                    case 13: //WBPr
                        bitboard_array_global[WP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[WB] &= ~Constants.SQUARE_BBS[targetSquareCopy];
                        break;
                    case 14: //WQPr
                        bitboard_array_global[WP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[WQ] &= ~Constants.SQUARE_BBS[targetSquareCopy];
                        break;
                    case 15: //WRPr
                        bitboard_array_global[WP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[WR] &= ~Constants.SQUARE_BBS[targetSquareCopy];
                        break;
                    case 16: //BNPrCAP
                        bitboard_array_global[BP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[BN] &= ~Constants.SQUARE_BBS[targetSquareCopy];

                        bitboard_array_global[captureIndex] |= Constants.SQUARE_BBS[targetSquareCopy];
                        break;
                    case 17: //BBPrCAP
                        bitboard_array_global[BP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[BB] &= ~Constants.SQUARE_BBS[targetSquareCopy];

                        bitboard_array_global[captureIndex] |= Constants.SQUARE_BBS[targetSquareCopy];

                        break;
                    case 18: //BQPrCAP
                        bitboard_array_global[BP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[BQ] &= ~Constants.SQUARE_BBS[targetSquareCopy];

                        bitboard_array_global[captureIndex] |= Constants.SQUARE_BBS[targetSquareCopy];
                        break;
                    case 19: //BRPrCAP
                        bitboard_array_global[BP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[BR] &= ~Constants.SQUARE_BBS[targetSquareCopy];

                        bitboard_array_global[captureIndex] |= Constants.SQUARE_BBS[targetSquareCopy];
                        break;
                    case 20: //WNPrCAP
                        bitboard_array_global[WP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[WN] &= ~Constants.SQUARE_BBS[targetSquareCopy];

                        bitboard_array_global[captureIndex] |= Constants.SQUARE_BBS[targetSquareCopy];
                        break;
                    case 21: //WBPrCAP
                        bitboard_array_global[WP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[WB] &= ~Constants.SQUARE_BBS[targetSquareCopy];

                        bitboard_array_global[captureIndex] |= Constants.SQUARE_BBS[targetSquareCopy];
                        break;
                    case 22: //WQPrCAP
                        bitboard_array_global[WP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[WQ] &= ~Constants.SQUARE_BBS[targetSquareCopy];

                        bitboard_array_global[captureIndex] |= Constants.SQUARE_BBS[targetSquareCopy];
                        break;
                    case 23: //WRPrCAP
                        bitboard_array_global[WP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[WR] &= ~Constants.SQUARE_BBS[targetSquareCopy];

                        bitboard_array_global[captureIndex] |= Constants.SQUARE_BBS[targetSquareCopy];
                        break;

                    #endregion

                    case 24: //WDouble
                        bitboard_array_global[WP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[WP] &= ~Constants.SQUARE_BBS[targetSquareCopy];
                        break;
                    case 25: //BDouble
                        bitboard_array_global[BP] |= Constants.SQUARE_BBS[startingSquareCopy];
                        bitboard_array_global[BP] &= ~Constants.SQUARE_BBS[targetSquareCopy];
                        break;
                }

                castle_rights_global[0] = copy_castle[0];
                castle_rights_global[1] = copy_castle[1];
                castle_rights_global[2] = copy_castle[2];
                castle_rights_global[3] = copy_castle[3];
                ep_global = copyEp;

                //if (epGlobal != NO_SQUARE)
                //{
                //    std::cout << "   ep: " << SQ_CHAR_X[epGlobal] << SQ_CHAR_Y[epGlobal] << '\n';
                //}

                #endregion

                //if (ply == 0)
                //{
                //   PrintMoveNoNL(move_list[move_index]);
                //  Console.Write(": %llu\n", nodes - priorNodes);
                //}
            }

            return nodes;
        }

        static void RunPerftInline(int depth)
        {
            DateTime start = DateTime.Now;

            int nodes = PerftInline(depth, 0);

            DateTime end = DateTime.Now;
            TimeSpan elapsed = end - start;
            int elapsedTimeInMs = (int)elapsed.TotalMilliseconds; ;
            Console.WriteLine($"Nodes: {nodes}");

            Console.WriteLine($"Elapsed time: {elapsedTimeInMs}ms");
        }

        static void Main()
        {
            SetStartingPosition();
            RunPerftInline(6);
        }


    }
}
