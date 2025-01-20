using System;
using System.Collections.Generic;
using static CEngineCopy.Constants;
using static CEngineCopy.Board;
using static CEngineCopy.MoveUtils;


namespace CEngineCopy
{
    internal class GenMoves
    {


        static int[] pinArrayPiece = new int[8];
        static int[] pinArraySquare = new int[8];


        public static int GetMoves(ref Span<int> startingSquares, ref Span<int> targetSquares, ref Span<int> tags, ref Span<int> pieces)
        {
            int moveCount = 0;

            //Move generating variables
            ulong WHITE_OCCUPANCIES_LOCAL = Board.bitboard_array_global[0] | Board.bitboard_array_global[1] | Board.bitboard_array_global[2] | Board.bitboard_array_global[3] | Board.bitboard_array_global[4] | Board.bitboard_array_global[5];
            ulong BLACK_OCCUPANCIES_LOCAL = Board.bitboard_array_global[6] | Board.bitboard_array_global[7] | Board.bitboard_array_global[8] | Board.bitboard_array_global[9] | Board.bitboard_array_global[10] | Board.bitboard_array_global[11];
            ulong COMBINED_OCCUPANCIES_LOCAL = WHITE_OCCUPANCIES_LOCAL | BLACK_OCCUPANCIES_LOCAL;
            ulong EMPTY_OCCUPANCIES = ~COMBINED_OCCUPANCIES_LOCAL;
            ulong tempBitboard, checkBitboard = 0UL, tempPinBitboard, tempAttack, tempEmpty, tempCaptures;
            int startingSquare = NO_SQUARE, targetSquare = NO_SQUARE;

            ulong enemyOccupancies;
            int knightIndex, bishopIndex, queenIndex, rookIndex;
            int kingPosition, checkCount = 0;

            int pinNumber = 0;

            #region Generate Moves

            if (Board.is_white_global == true)
            {
                kingPosition = BitScanForward(Board.bitboard_array_global[WK]);
                enemyOccupancies = BLACK_OCCUPANCIES_LOCAL;
                knightIndex = WN;
                rookIndex = WR;
                bishopIndex = WB;
                queenIndex = WQ;

                GetWhitePinsAndCheck(ref checkBitboard, ref checkCount, kingPosition, BLACK_OCCUPANCIES_LOCAL, WHITE_OCCUPANCIES_LOCAL, ref pinNumber);

                #region White king

                ulong occupanciesWithoutWhiteKing = COMBINED_OCCUPANCIES_LOCAL & (~Board.bitboard_array_global[WK]);
                tempAttack = Constants.KING_ATTACKS[kingPosition];
                tempEmpty = tempAttack & EMPTY_OCCUPANCIES;
                while (tempEmpty != 0)
                {
                    targetSquare = BitScanForward(tempEmpty);
                    tempEmpty &= tempEmpty - 1;

                    if (Is_Square_Attacked_By_Black_Global(targetSquare, occupanciesWithoutWhiteKing) == true)
                    {
                        continue;
                    }

                    AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, kingPosition, targetSquare, WK, TAG_NONE);
                }

                //captures
                tempCaptures = tempAttack & BLACK_OCCUPANCIES_LOCAL;
                while (tempCaptures != 0)
                {
                    targetSquare = BitScanForward(tempCaptures);
                    tempCaptures &= tempCaptures - 1;

                    if (Is_Square_Attacked_By_Black_Global(targetSquare, occupanciesWithoutWhiteKing) == true)
                    {
                        continue;
                    }

                    AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, kingPosition, targetSquare, WK, TAG_CAPTURE);
                }

                #endregion end white king

                if (checkCount < 2)
                {
                    if (checkCount == 0)
                    {
                        checkBitboard = ulong.MaxValue;

                        if (kingPosition == E1) //king on e1
                        {
                            if (CanWhiteCastleKingside(COMBINED_OCCUPANCIES_LOCAL) == true)
                            {
                                AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, E1, G1, WK, TAG_W_CASTLE_KS);
                            }
                            if (CanWhiteCastleQueenside(COMBINED_OCCUPANCIES_LOCAL) == true)
                            {
                                AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, E1, C1, WK, TAG_W_CASTLE_QS);
                            }
                        }
                    }
                    GetWhitePawnMoves(pinNumber, kingPosition, COMBINED_OCCUPANCIES_LOCAL, BLACK_OCCUPANCIES_LOCAL, checkBitboard, ref startingSquares, ref targetSquares, ref tags, ref pieces, ref moveCount);
                }
            }
            else //black move
            {
                kingPosition = BitScanForward(Board.bitboard_array_global[BK]);
                enemyOccupancies = WHITE_OCCUPANCIES_LOCAL;
                knightIndex = BN;
                rookIndex = BR;
                bishopIndex = BB;
                queenIndex = BQ;

                GetBlackPinsAndCheck(ref checkBitboard, ref checkCount, kingPosition, BLACK_OCCUPANCIES_LOCAL, WHITE_OCCUPANCIES_LOCAL, ref pinNumber);

                #region Black king
                ulong occupancyWithoutBlackKing = COMBINED_OCCUPANCIES_LOCAL & (~Board.bitboard_array_global[BK]);

                tempAttack = Constants.KING_ATTACKS[kingPosition] & WHITE_OCCUPANCIES_LOCAL;

                while (tempAttack != 0)
                {
                    targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                    tempAttack &= tempAttack - 1;

                    if (MoveUtils.Is_Square_Attacked_By_White_Global(targetSquare, occupancyWithoutBlackKing) == true)
                    {
                        continue;
                    }
                    AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, kingPosition, targetSquare, BK, Constants.TAG_CAPTURE);
                }

                tempAttack = Constants.KING_ATTACKS[kingPosition] & ~COMBINED_OCCUPANCIES_LOCAL;

                while (tempAttack != 0)
                {
                    targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                    tempAttack &= tempAttack - 1;

                    if (MoveUtils.Is_Square_Attacked_By_White_Global(targetSquare, occupancyWithoutBlackKing) == true)
                    {
                        continue;
                    }
                    AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, kingPosition, targetSquare, BK, TAG_NONE);
                }
                #endregion

                if (checkCount < 2)
                {
                    if (checkCount == 0)
                    {
                        checkBitboard = ulong.MaxValue;

                        if (kingPosition == E8) //king on e1
                        {
                            if (CanBlackCastleKingside(COMBINED_OCCUPANCIES_LOCAL) == true)
                            {
                                AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, E8, G8, BK, TAG_B_CASTLE_KS);
                            }
                            if (CanBlackCastleQueenside(COMBINED_OCCUPANCIES_LOCAL) == true)
                            {
                                AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, E8, C8, BK, TAG_B_CASTLE_QS);
                            }
                        }
                    }

                    GetBlackPawnMoves(pinNumber, kingPosition, COMBINED_OCCUPANCIES_LOCAL, WHITE_OCCUPANCIES_LOCAL, checkBitboard, ref startingSquares, ref targetSquares, ref tags, ref pieces, ref moveCount);

                }
            }

            #region knight

            tempBitboard = Board.bitboard_array_global[knightIndex];

            while (tempBitboard != 0)
            {
                startingSquare = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];
                tempBitboard &= tempBitboard - 1; //removes the knight from that square to not infinitely loop

                tempPinBitboard = GetPinBitboard(pinNumber, startingSquare, kingPosition);

                tempAttack = ((Constants.KNIGHT_ATTACKS[startingSquare] & enemyOccupancies) & checkBitboard) & tempPinBitboard; //gets knight captures
                while (tempAttack != 0)
                {
                    targetSquare = (DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58]);
                    tempAttack &= tempAttack - 1;


                    startingSquares[moveCount] = startingSquare;
                    targetSquares[moveCount] = targetSquare;
                    tags[moveCount] = TAG_CAPTURE;
                    pieces[moveCount] = knightIndex;
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
                    pieces[moveCount] = knightIndex;
                    moveCount++;
                }
            }
            #endregion

            #region Rook

            tempBitboard = Board.bitboard_array_global[rookIndex];
            while (tempBitboard != 0)
            {
                startingSquare = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];
                tempBitboard &= tempBitboard - 1;

                tempPinBitboard = GetPinBitboard(pinNumber, startingSquare, kingPosition);

                ulong rookAttacks = Constants.GetRookAttacksFast(startingSquare, COMBINED_OCCUPANCIES_LOCAL);

                tempAttack = ((rookAttacks & enemyOccupancies) & checkBitboard) & tempPinBitboard;
                while (tempAttack != 0)
                {
                    targetSquare = (DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58]);
                    tempAttack &= tempAttack - 1;

                    startingSquares[moveCount] = startingSquare;
                    targetSquares[moveCount] = targetSquare;
                    tags[moveCount] = TAG_CAPTURE;
                    pieces[moveCount] = rookIndex;
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
                    pieces[moveCount] = rookIndex;
                    moveCount++;
                }
            }
            #endregion

            #region Bishop

            tempBitboard = Board.bitboard_array_global[bishopIndex];
            while (tempBitboard != 0)
            {
                startingSquare = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];
                tempBitboard &= tempBitboard - 1;

                tempPinBitboard = GetPinBitboard(pinNumber, startingSquare, kingPosition);

                ulong bishopAttacks = Constants.GetBishopAttacksFast(startingSquare, COMBINED_OCCUPANCIES_LOCAL);

                tempAttack = ((bishopAttacks & enemyOccupancies) & checkBitboard) & tempPinBitboard;
                while (tempAttack != 0)
                {
                    targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                    tempAttack &= tempAttack - 1;

                    AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, targetSquare, bishopIndex, TAG_CAPTURE);
                }

                tempAttack = ((bishopAttacks & EMPTY_OCCUPANCIES) & checkBitboard) & tempPinBitboard;
                while (tempAttack != 0)
                {
                    targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                    tempAttack &= tempAttack - 1;

                    AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, targetSquare, bishopIndex, TAG_NONE);
                }
            }
            #endregion

            #region Queen

            tempBitboard = Board.bitboard_array_global[queenIndex];
            while (tempBitboard != 0)
            {
                startingSquare = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];
                tempBitboard &= tempBitboard - 1;

                tempPinBitboard = GetPinBitboard(pinNumber, startingSquare, kingPosition);

                ulong queenAttacks = Constants.GetRookAttacksFast(startingSquare, COMBINED_OCCUPANCIES_LOCAL);
                queenAttacks |= Constants.GetBishopAttacksFast(startingSquare, COMBINED_OCCUPANCIES_LOCAL);

                tempAttack = ((queenAttacks & enemyOccupancies) & checkBitboard) & tempPinBitboard;

                while (tempAttack != 0)
                {
                    targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                    tempAttack &= tempAttack - 1;

                    AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, targetSquare, queenIndex, TAG_CAPTURE);
                }

                tempAttack = ((queenAttacks & EMPTY_OCCUPANCIES) & checkBitboard) & tempPinBitboard;
                while (tempAttack != 0)
                {
                    targetSquare = DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58];
                    tempAttack &= tempAttack - 1;

                    AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, targetSquare, queenIndex, TAG_NONE);
                }
            }
            #endregion

            #endregion

            return moveCount;

        }

        public static void GetWhitePawnMoves(int pinNumber, int kingPosition, ulong COMBINED_OCCUPANCIES_LOCAL, ulong BLACK_OCCUPANCIES_LOCAL, ulong checkBitboard, ref Span<int> startingSquares, ref Span<int> targetSquares, ref Span<int> tags, ref Span<int> pieces, ref int moveCount)
        {
            ulong tempBitboard = bitboard_array_global[WP];

            while (tempBitboard != 0)
            {
                int startingSquare = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];
                tempBitboard &= tempBitboard - 1;

                ulong tempPinBitboard = GetPinBitboard(pinNumber, startingSquare, kingPosition);

                #region Pawn forward

                if ((SQUARE_BBS[startingSquare - 8] & COMBINED_OCCUPANCIES_LOCAL) == 0) //if up one square is empty
                {
                    if (((SQUARE_BBS[startingSquare - 8] & checkBitboard) & tempPinBitboard) != 0)
                    {
                        if ((SQUARE_BBS[startingSquare] & RANK_7_BITBOARD) != 0) //if promotion
                        {
                            AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, startingSquare - 8, WP, TAG_W_Q_PROMOTION);
                            AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, startingSquare - 8, WP, TAG_W_R_PROMOTION);
                            AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, startingSquare - 8, WP, TAG_W_N_PROMOTION);
                            AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, startingSquare - 8, WP, TAG_W_B_PROMOTION);
                        }
                        else
                        {
                            AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, startingSquare - 8, WP, TAG_NONE);
                        }
                    }

                    if ((SQUARE_BBS[startingSquare] & RANK_2_BITBOARD) != 0) //if on rank 2
                    {
                        if (((SQUARE_BBS[startingSquare - 16] & checkBitboard) & tempPinBitboard) != 0) //if not pinned or 
                        {
                            if (((SQUARE_BBS[startingSquare - 16]) & COMBINED_OCCUPANCIES_LOCAL) == 0) //if up two squares and one square are empty
                            {
                                AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, startingSquare - 16, WP, TAG_W_P_DOUBLE);
                            }
                        }
                    }
                }

                #endregion

                #region Pawn captures

                ulong tempAttack = ((WHITE_PAWN_ATTACKS[startingSquare] & BLACK_OCCUPANCIES_LOCAL) & checkBitboard) & tempPinBitboard; //if black piece diagonal to pawn

                while (tempAttack != 0)
                {
                    int targetSquare = (DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58]);
                    tempAttack &= tempAttack - 1;

                    if ((SQUARE_BBS[startingSquare] & RANK_7_BITBOARD) != 0) //if promotion
                    {
                        AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, targetSquare, WP, TAG_W_Q_PROMOTION_CAP);
                        AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, targetSquare, WP, TAG_W_R_PROMOTION_CAP);
                        AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, targetSquare, WP, TAG_W_N_PROMOTION_CAP);
                        AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, targetSquare, WP, TAG_W_B_PROMOTION_CAP);
                    }
                    else
                    {
                        AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, targetSquare, WP, TAG_CAPTURE);
                    }
                }

                if ((SQUARE_BBS[startingSquare] & RANK_5_BITBOARD) == 0) //check rank for ep
                {
                    continue;
                }
                if (ep_global == NO_SQUARE)
                {
                    continue;
                }
                if ((((WHITE_PAWN_ATTACKS[startingSquare] & SQUARE_BBS[ep_global]) & checkBitboard) & tempPinBitboard) == 0)
                {
                    continue;
                }
                if ((bitboard_array_global[WK] & RANK_5_BITBOARD) == 0) //if no king on rank 5
                {
                    AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, ep_global, WP, TAG_WHITE_EP);
                    continue;
                }
                if ((bitboard_array_global[BR] & RANK_5_BITBOARD) == 0 && (bitboard_array_global[BQ] & RANK_5_BITBOARD) == 0) // if no b rook or queen on rank 5
                {
                    AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, ep_global, WP, TAG_WHITE_EP);
                    continue;
                }

                ulong occupancyWithoutEPPawns = COMBINED_OCCUPANCIES_LOCAL & ~SQUARE_BBS[startingSquare];
                occupancyWithoutEPPawns &= ~SQUARE_BBS[ep_global + 8];

                ulong rookAttacksFromKing = GetRookAttacksFast(kingPosition, occupancyWithoutEPPawns);

                if ((rookAttacksFromKing & bitboard_array_global[BR]) == 0)
                {
                    if ((rookAttacksFromKing & bitboard_array_global[BQ]) == 0)
                    {
                        AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, ep_global, WP, TAG_WHITE_EP);
                    }
                }

                #endregion
            }
        }

        public static void GetWhitePinsAndCheck(ref ulong checkBitboard, ref int checkCount, int kingPosition, ulong BLACK_OCCUPANCIES_LOCAL, ulong WHITE_OCCUPANCIES_LOCAL, ref int pinNumber)
        {
            //pawns
            ulong tempBitboard = bitboard_array_global[BP] & WHITE_PAWN_ATTACKS[kingPosition];
            if (tempBitboard != 0)
            {
                int pawn_square = (DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58]);
                checkBitboard = SQUARE_BBS[pawn_square];
                checkCount++;
            }

            //knights
            tempBitboard = bitboard_array_global[BN] & KNIGHT_ATTACKS[kingPosition];
            if (tempBitboard != 0)
            {
                int knight_square = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];
                checkBitboard = SQUARE_BBS[knight_square];
                checkCount++;
            }

            //bishops
            ulong bishopAttacksChecks = GetBishopAttacksFast(kingPosition, BLACK_OCCUPANCIES_LOCAL);
            tempBitboard = bitboard_array_global[BB] & bishopAttacksChecks;
            while (tempBitboard != 0)
            {
                CheckPinOrCheck(ref checkBitboard, ref checkCount, kingPosition, WHITE_OCCUPANCIES_LOCAL, ref pinNumber, tempBitboard);
                tempBitboard &= tempBitboard - 1;
            }

            //queen bishop moves
            tempBitboard = bitboard_array_global[BQ] & bishopAttacksChecks;
            while (tempBitboard != 0)
            {
                CheckPinOrCheck(ref checkBitboard, ref checkCount, kingPosition, WHITE_OCCUPANCIES_LOCAL, ref pinNumber, tempBitboard);
                tempBitboard &= tempBitboard - 1;
            }

            //rook
            ulong rook_attacks = GetRookAttacksFast(kingPosition, BLACK_OCCUPANCIES_LOCAL);
            tempBitboard = bitboard_array_global[BR] & rook_attacks;
            while (tempBitboard != 0)
            {
                CheckPinOrCheck(ref checkBitboard, ref checkCount, kingPosition, WHITE_OCCUPANCIES_LOCAL, ref pinNumber, tempBitboard);
                tempBitboard &= tempBitboard - 1;
            }

            //queen rook moves
            tempBitboard = bitboard_array_global[BQ] & rook_attacks;
            while (tempBitboard != 0)
            {
                CheckPinOrCheck(ref checkBitboard, ref checkCount, kingPosition, WHITE_OCCUPANCIES_LOCAL, ref pinNumber, tempBitboard);
                tempBitboard &= tempBitboard - 1;
            }
        }


        public static void GetBlackPawnMoves(int pinNumber, int kingPosition, ulong COMBINED_OCCUPANCIES_LOCAL, ulong WHITE_OCCUPANCIES_LOCAL, ulong checkBitboard, ref Span<int> startingSquares, ref Span<int> targetSquares, ref Span<int> tags, ref Span<int> pieces, ref int moveCount)
        {
            ulong tempBitboard = bitboard_array_global[BP];

            while (tempBitboard != 0)
            {
                int startingSquare = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];
                tempBitboard &= tempBitboard - 1;

                ulong tempPinBitboard = GetPinBitboard(pinNumber, startingSquare, kingPosition);

                #region Pawn forward

                if ((SQUARE_BBS[startingSquare + 8] & COMBINED_OCCUPANCIES_LOCAL) == 0) //if up one square is empty
                {
                    if (((SQUARE_BBS[startingSquare + 8] & checkBitboard) & tempPinBitboard) != 0)
                    {
                        if ((SQUARE_BBS[startingSquare] & RANK_2_BITBOARD) != 0) //if promotion
                        {
                            AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, startingSquare + 8, BP, TAG_B_Q_PROMOTION);
                            AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, startingSquare + 8, BP, TAG_B_R_PROMOTION);
                            AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, startingSquare + 8, BP, TAG_B_N_PROMOTION);
                            AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, startingSquare + 8, BP, TAG_B_B_PROMOTION);
                        }
                        else
                        {
                            AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, startingSquare + 8, BP, TAG_NONE);
                        }
                    }

                    if ((SQUARE_BBS[startingSquare] & RANK_7_BITBOARD) != 0) //if on rank 2
                    {
                        if (((SQUARE_BBS[startingSquare + 16] & checkBitboard) & tempPinBitboard) != 0) //if not pinned or 
                        {
                            if (((SQUARE_BBS[startingSquare + 16]) & COMBINED_OCCUPANCIES_LOCAL) == 0) //if up two squares and one square are empty
                            {
                                AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, startingSquare + 16, BP, TAG_B_P_DOUBLE);
                            }
                        }
                    }
                }

                #endregion

                #region Pawn captures

                ulong tempAttack = ((BLACK_PAWN_ATTACKS[startingSquare] & WHITE_OCCUPANCIES_LOCAL) & checkBitboard) & tempPinBitboard; //if black piece diagonal to pawn

                while (tempAttack != 0)
                {
                    int targetSquare = (DEBRUIJN64[MAGIC * (tempAttack ^ (tempAttack - 1)) >> 58]);
                    tempAttack &= tempAttack - 1;

                    if ((SQUARE_BBS[startingSquare] & RANK_2_BITBOARD) != 0) //if promotion
                    {
                        AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, targetSquare, BP, TAG_B_Q_PROMOTION_CAP);
                        AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, targetSquare, BP, TAG_B_R_PROMOTION_CAP);
                        AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, targetSquare, BP, TAG_B_N_PROMOTION_CAP);
                        AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, targetSquare, BP, TAG_B_B_PROMOTION_CAP);
                    }
                    else
                    {
                        AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, targetSquare, BP, TAG_CAPTURE);
                    }
                }

                if ((SQUARE_BBS[startingSquare] & RANK_4_BITBOARD) == 0) //check rank for ep
                {
                    continue;
                }
                if (ep_global == NO_SQUARE)
                {
                    continue;
                }
                if ((((BLACK_PAWN_ATTACKS[startingSquare] & SQUARE_BBS[ep_global]) & checkBitboard) & tempPinBitboard) == 0)
                {
                    continue;
                }
                if ((bitboard_array_global[BK] & RANK_4_BITBOARD) == 0) //if no king on rank 5
                {
                    AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, ep_global, BP, TAG_BLACK_EP);
                    continue;
                }
                if ((bitboard_array_global[WR] & RANK_4_BITBOARD) == 0 && (bitboard_array_global[WQ] & RANK_4_BITBOARD) == 0) // if no b rook or queen on rank 5
                {
                    AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, ep_global, BP, TAG_BLACK_EP);
                    continue;
                }
                //wk and br or bq on rank 5

                ulong occupancyWithoutEPPawns = COMBINED_OCCUPANCIES_LOCAL & ~SQUARE_BBS[startingSquare];
                occupancyWithoutEPPawns &= ~SQUARE_BBS[ep_global - 8];

                ulong rookAttacksFromKing = GetRookAttacksFast(kingPosition, occupancyWithoutEPPawns);

                if ((rookAttacksFromKing & bitboard_array_global[WR]) == 0)
                {
                    if ((rookAttacksFromKing & bitboard_array_global[WQ]) == 0)
                    {
                        AddMove(ref moveCount, ref startingSquares, ref targetSquares, ref tags, ref pieces, startingSquare, ep_global, BP, TAG_BLACK_EP);
                    }
                }
                #endregion
            }
        }

        public static void GetBlackPinsAndCheck(ref ulong checkBitboard, ref int checkCount, int kingPosition, ulong BLACK_OCCUPANCIES_LOCAL, ulong WHITE_OCCUPANCIES_LOCAL, ref int pinNumber)
        {
            //pawns
            ulong tempBitboard = bitboard_array_global[WP] & BLACK_PAWN_ATTACKS[kingPosition];
            if (tempBitboard != 0)
            {
                int pawn_square = (DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58]);
                checkBitboard = SQUARE_BBS[pawn_square];
                checkCount++;
            }

            //knights
            tempBitboard = bitboard_array_global[WN] & KNIGHT_ATTACKS[kingPosition];
            if (tempBitboard != 0)
            {
                int knight_square = DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58];
                checkBitboard = SQUARE_BBS[knight_square];
                checkCount++;
            }

            //bishops
            ulong bishopAttacksChecks = GetBishopAttacksFast(kingPosition, WHITE_OCCUPANCIES_LOCAL);
            tempBitboard = bitboard_array_global[WB] & bishopAttacksChecks;
            while (tempBitboard != 0)
            {
                CheckPinOrCheck(ref checkBitboard, ref checkCount, kingPosition, BLACK_OCCUPANCIES_LOCAL, ref pinNumber, tempBitboard);
                tempBitboard &= tempBitboard - 1;
            }

            //queen bishop moves
            tempBitboard = bitboard_array_global[WQ] & bishopAttacksChecks;
            while (tempBitboard != 0)
            {
                CheckPinOrCheck(ref checkBitboard, ref checkCount, kingPosition, BLACK_OCCUPANCIES_LOCAL, ref pinNumber, tempBitboard);
                tempBitboard &= tempBitboard - 1;
            }

            //rook
            ulong rook_attacks = GetRookAttacksFast(kingPosition, WHITE_OCCUPANCIES_LOCAL);
            tempBitboard = bitboard_array_global[WR] & rook_attacks;
            while (tempBitboard != 0)
            {
                CheckPinOrCheck(ref checkBitboard, ref checkCount, kingPosition, BLACK_OCCUPANCIES_LOCAL, ref pinNumber, tempBitboard);
                tempBitboard &= tempBitboard - 1;
            }

            //queen rook moves
            tempBitboard = bitboard_array_global[WQ] & rook_attacks;
            while (tempBitboard != 0)
            {
                CheckPinOrCheck(ref checkBitboard, ref checkCount, kingPosition, BLACK_OCCUPANCIES_LOCAL, ref pinNumber, tempBitboard);
                tempBitboard &= tempBitboard - 1;
            }
        }


        public static ulong GetPinBitboard(int pinNumber, int startingSquare, int kingPosition)
        {
            ulong pinBitboard = ulong.MaxValue;
            if (pinNumber != 0)
            {
                for (int i = 0; i < pinNumber; i++)
                {
                    if (pinArraySquare[i] == startingSquare)
                    {
                        pinBitboard = INBETWEEN_BITBOARDS[kingPosition, pinArrayPiece[i]];
                        break;
                    }
                }
            }
            return pinBitboard;
        }

        public static void CheckPinOrCheck(ref ulong checkBitboard, ref int checkCount, int kingPosition, ulong OCCUPANCIES, ref int pinNumber, ulong tempBitboard)
        {
            ulong tempPinBitboard;
            int piece_square = (DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58]);

            tempPinBitboard = Constants.INBETWEEN_BITBOARDS[kingPosition, piece_square] & OCCUPANCIES;

            if (tempPinBitboard == 0)
            {
                checkBitboard = Constants.INBETWEEN_BITBOARDS[kingPosition, piece_square];
                checkCount++;
                return;
            }

            int pinned_square = (DEBRUIJN64[MAGIC * (tempPinBitboard ^ (tempPinBitboard - 1)) >> 58]);
            tempPinBitboard &= tempPinBitboard - 1;

            if (tempPinBitboard == 0)
            {
                pinArraySquare[pinNumber] = pinned_square;
                pinArrayPiece[pinNumber] = piece_square;
                pinNumber++;
            }
        }

    }
}
