
using static CEngineCopy.Constants;
using static CEngineCopy.Board;

namespace CEngineCopy
{
    public class MoveUtils
    {

        public const ulong MAGIC = 0x03f79d71b4cb0a89;

        public static readonly int[] DEBRUIJN64 =
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

        public static int BitScanForward(ulong tempBitboard)
        {
            return (DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58]);
        }

        public static void AddMove(ref int moveCount, ref Span<int> startingSquares, ref Span<int> targetSquares, ref Span<int> tags, ref Span<int> pieces, int startingSquare, int targetSquare, int piece, int tag)
        {
            startingSquares[moveCount] = startingSquare;
            targetSquares[moveCount] = targetSquare;
            tags[moveCount] = tag;
            pieces[moveCount] = piece;
            moveCount++;
        }


        public static bool CanWhiteCastleKingside(ulong COMBINED_OCCS)
        {
            if (castle_rights_global[WKS_CASTLE_RIGHTS] == false)
            {
                return false;
            }
            if ((WKS_EMPTY_BITBOARD & COMBINED_OCCS) != 0) //f1 and g1 empty
            {
                return false;
            }
            if ((bitboard_array_global[WR] & SQUARE_BBS[H1]) == 0) //rook on h1
            {
                return false;
            }
            if (Is_Square_Attacked_By_Black_Global(F1, COMBINED_OCCS) == true)
            {
                return false;
            }
            if (Is_Square_Attacked_By_Black_Global(G1, COMBINED_OCCS) == true)
            {
                return false;
            }
            return true;
        }
        public static bool CanWhiteCastleQueenside(ulong COMBINED_OCCS)
        {
            if (castle_rights_global[WQS_CASTLE_RIGHTS] == false)
            {
                return false;
            }
            if ((WQS_EMPTY_BITBOARD & COMBINED_OCCS) != 0) //f1 and g1 empty
            {
                return false;
            }
            if ((bitboard_array_global[WR] & SQUARE_BBS[A1]) == 0) //rook on h1
            {
                return false;
            }
            if (Is_Square_Attacked_By_Black_Global(C1, COMBINED_OCCS) == true)
            {
                return false;
            }
            if (Is_Square_Attacked_By_Black_Global(D1, COMBINED_OCCS) == true)
            {
                return false;
            }
            return true;
        }
        public static bool CanBlackCastleKingside(ulong COMBINED_OCCS)
        {
            if (castle_rights_global[BKS_CASTLE_RIGHTS] == false)
            {
                return false;
            }
            if ((BKS_EMPTY_BITBOARD & COMBINED_OCCS) != 0)
            { //f8 and g8 not empty
                return false;
            }
            if ((bitboard_array_global[BR] & SQUARE_BBS[H8]) == 0)
            { //rook not on h8
                return false;
            }
            if (Is_Square_Attacked_By_White_Global(F8, COMBINED_OCCS) == true)
            {
                return false;
            }
            if (Is_Square_Attacked_By_White_Global(G8, COMBINED_OCCS) == true)
            {
                return false;
            }
            return true;
        }
        public static bool CanBlackCastleQueenside(ulong COMBINED_OCCS)
        {
            if (castle_rights_global[BQS_CASTLE_RIGHTS] == false)
            {
                return false;
            }
            if ((BQS_EMPTY_BITBOARD & COMBINED_OCCS) != 0)
            { //c8,b8, d8 not empty
                return false;
            }
            if ((bitboard_array_global[BR] & SQUARE_BBS[A8]) == 0)
            { //rook not on a8
                return false;
            }
            if (Is_Square_Attacked_By_White_Global(C8, COMBINED_OCCS) == true)
            {
                return false;
            }
            if (Is_Square_Attacked_By_White_Global(D8, COMBINED_OCCS) == true)
            {
                return false;
            }
            return true;
        }


        public static bool Is_Square_Attacked_By_Black_Global(int square, ulong occupancy)
        {
            if ((bitboard_array_global[BP] & WHITE_PAWN_ATTACKS[square]) != 0)
            {
                return true;
            }
            if ((bitboard_array_global[BN] & KNIGHT_ATTACKS[square]) != 0)
            {
                return true;
            }
            if ((bitboard_array_global[BK] & KING_ATTACKS[square]) != 0)
            {
                return true;
            }
            ulong bishopAttacks = GetBishopAttacksFast(square, occupancy);
            if ((bitboard_array_global[BB] & bishopAttacks) != 0)
            {
                return true;
            }
            if ((bitboard_array_global[BQ] & bishopAttacks) != 0)
            {
                return true;
            }
            ulong rookAttacks = GetRookAttacksFast(square, occupancy);
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

        public static bool Is_Square_Attacked_By_White_Global(int square, ulong occupancy)
        {
            if ((bitboard_array_global[WP] & BLACK_PAWN_ATTACKS[square]) != 0)
            {
                return true;
            }
            if ((bitboard_array_global[WN] & KNIGHT_ATTACKS[square]) != 0)
            {
                return true;
            }
            if ((bitboard_array_global[WK] & KING_ATTACKS[square]) != 0)
            {
                return true;
            }
            ulong bishopAttacks = GetBishopAttacksFast(square, occupancy);
            if ((bitboard_array_global[WB] & bishopAttacks) != 0)
            {
                return true;
            }
            if ((bitboard_array_global[WQ] & bishopAttacks) != 0)
            {
                return true;
            }
            ulong rookAttacks = GetRookAttacksFast(square, occupancy);
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

        public static bool OutOfBounds(int move)
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

    }
}
