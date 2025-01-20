using System;
using System.Collections.Generic;
using static CEngineCopy.Constants;
using static CEngineCopy.Board;

namespace CEngineCopy
{
    internal class Make_Move
    {

        enum CaptureType
        {
            None, White, Black
        }

        public static int MakeMove(int startingSquare, int targetSquare, int tag, int piece)
        {
            int captureIndex = -1;

            is_white_global = !is_white_global;
            ep_global = NO_SQUARE;
            CaptureType captureType = CaptureType.None;

            switch (tag)
            {
                case TAG_NONE: //none
                case TAG_CHECK: //check
                    MovePiece(startingSquare, targetSquare, piece);
                    break;
                case TAG_CHECK_CAP: //capture
                case TAG_CAPTURE: //check cap
                    MovePiece(startingSquare, targetSquare, piece);
                    if (is_white_global == true)
                    {
                        captureType = CaptureType.White;
                    }
                    else
                    {
                        captureType = CaptureType.Black;
                    }
                    break;
                case TAG_WHITE_EP: //white ep
                    MovePiece(startingSquare, targetSquare, WP);
                    RemovePiece(BP, targetSquare + 8);
                    break;
                case TAG_BLACK_EP:
                    MovePiece(startingSquare, targetSquare, BP);
                    RemovePiece(WP, targetSquare - 8);
                    break;

                #region Castling

                case TAG_W_CASTLE_KS:
                    MovePiece(E1, G1, WK);
                    MovePiece(H1, F1, WR);
                    castle_rights_global[WKS_CASTLE_RIGHTS] = false;
                    castle_rights_global[WQS_CASTLE_RIGHTS] = false;
                    break;
                case TAG_W_CASTLE_QS:
                    MovePiece(E1, C1, WK);
                    MovePiece(A1, D1, WR);
                    castle_rights_global[WKS_CASTLE_RIGHTS] = false;
                    castle_rights_global[WQS_CASTLE_RIGHTS] = false;
                    break;
                case TAG_B_CASTLE_KS:
                    MovePiece(E8, G8, BK);
                    MovePiece(H8, F8, BR);
                    castle_rights_global[BKS_CASTLE_RIGHTS] = false;
                    castle_rights_global[BQS_CASTLE_RIGHTS] = false;
                    break;
                case TAG_B_CASTLE_QS: //BQS
                    MovePiece(E8, C8, BK);
                    MovePiece(A8, D8, BR);
                    castle_rights_global[BKS_CASTLE_RIGHTS] = false;
                    castle_rights_global[BQS_CASTLE_RIGHTS] = false;
                    break;

                #endregion

                #region Promotion makemove

                case TAG_B_N_PROMOTION:
                    MovePiecePromote(startingSquare, targetSquare, piece, BN);
                    break;
                case TAG_B_B_PROMOTION:
                    MovePiecePromote(startingSquare, targetSquare, piece, BB);
                    break;
                case TAG_B_Q_PROMOTION:
                    MovePiecePromote(startingSquare, targetSquare, piece, BQ);
                    break;
                case TAG_B_R_PROMOTION:
                    MovePiecePromote(startingSquare, targetSquare, piece, BR);
                    break;
                case TAG_W_N_PROMOTION:
                    MovePiecePromote(startingSquare, targetSquare, piece, WN);
                    break;
                case TAG_W_B_PROMOTION:
                    MovePiecePromote(startingSquare, targetSquare, piece, WB);
                    break;
                case TAG_W_Q_PROMOTION:
                    MovePiecePromote(startingSquare, targetSquare, piece, WQ);
                    break;
                case TAG_W_R_PROMOTION:
                    MovePiecePromote(startingSquare, targetSquare, piece, WR);
                    break;
                case TAG_B_N_PROMOTION_CAP:
                    MovePiecePromote(startingSquare, targetSquare, piece, BN);
                    captureType = CaptureType.White;
                    break;
                case TAG_B_B_PROMOTION_CAP:
                    MovePiecePromote(startingSquare, targetSquare, piece, BB);
                    captureType = CaptureType.White;
                    break;
                case TAG_B_Q_PROMOTION_CAP:
                    MovePiecePromote(startingSquare, targetSquare, piece, BQ);
                    captureType = CaptureType.White;
                    break;
                case TAG_B_R_PROMOTION_CAP:
                    MovePiecePromote(startingSquare, targetSquare, piece, BR);
                    captureType = CaptureType.White;
                    break;
                case TAG_W_N_PROMOTION_CAP:
                    MovePiecePromote(startingSquare, targetSquare, piece, WN);
                    captureType = CaptureType.Black;
                    break;
                case TAG_W_B_PROMOTION_CAP:
                    MovePiecePromote(startingSquare, targetSquare, piece, WB);
                    captureType = CaptureType.Black;
                    break;
                case TAG_W_Q_PROMOTION_CAP:
                    MovePiecePromote(startingSquare, targetSquare, piece, WQ);
                    captureType = CaptureType.Black;
                    break;
                case TAG_W_R_PROMOTION_CAP:
                    MovePiecePromote(startingSquare, targetSquare, piece, WR);
                    captureType = CaptureType.Black;
                    break;
                #endregion

                case TAG_W_P_DOUBLE:
                    MovePiece(startingSquare, targetSquare, WP);
                    ep_global = targetSquare + 8;
                    break;
                case TAG_B_P_DOUBLE:
                    MovePiece(startingSquare, targetSquare, BP);
                    ep_global = targetSquare - 8;
                    break;
            }

            switch (captureType)
            {
                case CaptureType.None:
                    break;
                case CaptureType.White:
                    captureIndex = FindCaptureWhite(targetSquare, captureIndex);
                    break;
                case CaptureType.Black:
                    captureIndex = FindCaptureBlack(targetSquare, captureIndex);
                    break;
            }

            UpdateCastleRights(piece);

            return captureIndex;
        }

        private static void UpdateCastleRights(int piece)
        {
            if (piece == WK)
            {
                castle_rights_global[WKS_CASTLE_RIGHTS] = false;
                castle_rights_global[WQS_CASTLE_RIGHTS] = false;
                return;
            }
            if (piece == BK)
            {
                castle_rights_global[BKS_CASTLE_RIGHTS] = false;
                castle_rights_global[BQS_CASTLE_RIGHTS] = false;
                return;
            }
            if (piece == WR)
            {
                if (castle_rights_global[WKS_CASTLE_RIGHTS] == true)
                {
                    if ((bitboard_array_global[WR] & SQUARE_BBS[H1]) == 0)
                    {
                        castle_rights_global[WKS_CASTLE_RIGHTS] = false;
                    }
                }
                if (castle_rights_global[WQS_CASTLE_RIGHTS] == true)
                {
                    if ((bitboard_array_global[WR] & SQUARE_BBS[A1]) == 0)
                    {
                        castle_rights_global[WQS_CASTLE_RIGHTS] = false;
                    }
                }
                return;
            }
            if (piece == BR)
            {
                if (castle_rights_global[BKS_CASTLE_RIGHTS] == true)
                {
                    if ((bitboard_array_global[BR] & SQUARE_BBS[H8]) == 0)
                    {
                        castle_rights_global[BKS_CASTLE_RIGHTS] = false;
                    }
                }
                if (castle_rights_global[BQS_CASTLE_RIGHTS] == true)
                {
                    if ((bitboard_array_global[BR] & SQUARE_BBS[A8]) == 0)
                    {
                        castle_rights_global[BQS_CASTLE_RIGHTS] = false;
                    }
                }
            }
        }

        public static void UnmakeMove(int startingSquareCopy, int targetSquareCopy, int piece, int tag, int captureIndex)
        {
            is_white_global = !is_white_global;
            //should use constants and MovePiece, a bit too lazy to refactor
            switch (tag)
            {
                case 0: //none
                case 26: //check
                    bitboard_array_global[piece] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[piece] &= ~SQUARE_BBS[targetSquareCopy];

                    break;
                case 1: //capture
                case 27: //check cap
                    bitboard_array_global[piece] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[piece] &= ~SQUARE_BBS[targetSquareCopy];
                    bitboard_array_global[captureIndex] |= SQUARE_BBS[targetSquareCopy];


                    break;
                case 2: //white ep
                    bitboard_array_global[WP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[WP] &= ~SQUARE_BBS[targetSquareCopy];
                    bitboard_array_global[BP] |= SQUARE_BBS[targetSquareCopy + 8];

                    break;
                case 3: //black ep
                    bitboard_array_global[BP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[BP] &= ~SQUARE_BBS[targetSquareCopy];
                    bitboard_array_global[WP] |= SQUARE_BBS[targetSquareCopy - 8];

                    break;
                case 4: //WKS
                        //white king
                    bitboard_array_global[WK] |= SQUARE_BBS[E1];
                    bitboard_array_global[WK] &= ~SQUARE_BBS[G1];
                    //white rook
                    bitboard_array_global[WR] |= SQUARE_BBS[H1];
                    bitboard_array_global[WR] &= ~SQUARE_BBS[F1];
                    break;
                case 5: //WQS
                        //white king
                    bitboard_array_global[WK] |= SQUARE_BBS[E1];
                    bitboard_array_global[WK] &= ~SQUARE_BBS[C1];
                    //white rook
                    bitboard_array_global[WR] |= SQUARE_BBS[A1];
                    bitboard_array_global[WR] &= ~SQUARE_BBS[D1];
                    break;
                case 6: //BKS
                        //white king
                    bitboard_array_global[BK] |= SQUARE_BBS[E8];
                    bitboard_array_global[BK] &= ~SQUARE_BBS[G8];
                    //white rook
                    bitboard_array_global[BR] |= SQUARE_BBS[H8];
                    bitboard_array_global[BR] &= ~SQUARE_BBS[F8];
                    break;
                case 7: //BQS
                        //white king
                    bitboard_array_global[BK] |= SQUARE_BBS[E8];
                    bitboard_array_global[BK] &= ~SQUARE_BBS[C8];
                    //white rook
                    bitboard_array_global[BR] |= SQUARE_BBS[A8];
                    bitboard_array_global[BR] &= ~SQUARE_BBS[D8];

                    break;

                #region Promotion Unmakemove
                case 8: //BNPr
                    bitboard_array_global[BP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[BN] &= ~SQUARE_BBS[targetSquareCopy];
                    break;
                case 9: //BBPr
                    bitboard_array_global[BP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[BB] &= ~SQUARE_BBS[targetSquareCopy];
                    break;
                case 10: //BQPr
                    bitboard_array_global[BP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[BQ] &= ~SQUARE_BBS[targetSquareCopy];
                    break;
                case 11: //BRPr
                    bitboard_array_global[BP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[BR] &= ~SQUARE_BBS[targetSquareCopy];
                    break;
                case 12: //WNPr
                    bitboard_array_global[WP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[WN] &= ~SQUARE_BBS[targetSquareCopy];
                    break;
                case 13: //WBPr
                    bitboard_array_global[WP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[WB] &= ~SQUARE_BBS[targetSquareCopy];
                    break;
                case 14: //WQPr
                    bitboard_array_global[WP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[WQ] &= ~SQUARE_BBS[targetSquareCopy];
                    break;
                case 15: //WRPr
                    bitboard_array_global[WP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[WR] &= ~SQUARE_BBS[targetSquareCopy];
                    break;
                case 16: //BNPrCAP
                    bitboard_array_global[BP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[BN] &= ~SQUARE_BBS[targetSquareCopy];

                    bitboard_array_global[captureIndex] |= SQUARE_BBS[targetSquareCopy];
                    break;
                case 17: //BBPrCAP
                    bitboard_array_global[BP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[BB] &= ~SQUARE_BBS[targetSquareCopy];

                    bitboard_array_global[captureIndex] |= SQUARE_BBS[targetSquareCopy];

                    break;
                case 18: //BQPrCAP
                    bitboard_array_global[BP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[BQ] &= ~SQUARE_BBS[targetSquareCopy];

                    bitboard_array_global[captureIndex] |= SQUARE_BBS[targetSquareCopy];
                    break;
                case 19: //BRPrCAP
                    bitboard_array_global[BP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[BR] &= ~SQUARE_BBS[targetSquareCopy];

                    bitboard_array_global[captureIndex] |= SQUARE_BBS[targetSquareCopy];
                    break;
                case 20: //WNPrCAP
                    bitboard_array_global[WP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[WN] &= ~SQUARE_BBS[targetSquareCopy];

                    bitboard_array_global[captureIndex] |= SQUARE_BBS[targetSquareCopy];
                    break;
                case 21: //WBPrCAP
                    bitboard_array_global[WP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[WB] &= ~SQUARE_BBS[targetSquareCopy];

                    bitboard_array_global[captureIndex] |= SQUARE_BBS[targetSquareCopy];
                    break;
                case 22: //WQPrCAP
                    bitboard_array_global[WP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[WQ] &= ~SQUARE_BBS[targetSquareCopy];

                    bitboard_array_global[captureIndex] |= SQUARE_BBS[targetSquareCopy];
                    break;
                case 23: //WRPrCAP
                    bitboard_array_global[WP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[WR] &= ~SQUARE_BBS[targetSquareCopy];

                    bitboard_array_global[captureIndex] |= SQUARE_BBS[targetSquareCopy];
                    break;

                #endregion

                case 24: //WDouble
                    bitboard_array_global[WP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[WP] &= ~SQUARE_BBS[targetSquareCopy];
                    break;
                case 25: //BDouble
                    bitboard_array_global[BP] |= SQUARE_BBS[startingSquareCopy];
                    bitboard_array_global[BP] &= ~SQUARE_BBS[targetSquareCopy];
                    break;
            }
        }
       
        private static int FindCaptureBlack(int targetSquare, int captureIndex)
        {
            for (int i = BLACK_START_INDEX; i <= BLACK_END_INDEX; ++i)
            {
                if ((bitboard_array_global[i] & SQUARE_BBS[targetSquare]) != 0)
                {
                    captureIndex = i;
                    break;
                }
            }
            bitboard_array_global[captureIndex] &= ~SQUARE_BBS[targetSquare];
            return captureIndex;
        }

        private static void MovePiecePromote(int startingSquare, int targetSquare, int piece, int promotionPiece)
        {
            bitboard_array_global[promotionPiece] |= SQUARE_BBS[targetSquare];
            bitboard_array_global[piece] &= ~SQUARE_BBS[startingSquare];
        }

        private static void RemovePiece(int piece, int targetSquare)
        {
            bitboard_array_global[piece] &= ~SQUARE_BBS[targetSquare];
        }

        private static void MovePiece(int startingSquare, int targetSquare, int piece)
        {
            bitboard_array_global[piece] |= SQUARE_BBS[targetSquare];
            bitboard_array_global[piece] &= ~SQUARE_BBS[startingSquare];
        }

        private static int FindCaptureWhite(int targetSquareCopy, int captureIndex)
        {
            for (int i = WHITE_START_INDEX; i <= WHITE_END_INDEX; ++i)
            {
                if ((bitboard_array_global[i] & SQUARE_BBS[targetSquareCopy]) != 0)
                {
                    captureIndex = i;
                    break;
                }
            }
            bitboard_array_global[captureIndex] &= ~SQUARE_BBS[targetSquareCopy];
            return captureIndex;
        }



    }
}
