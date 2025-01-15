public class Testing 
{

    public static void testRookMoves()
    {
        int rookSquare = 36;

        long  WP_STARTING_POSITIONS = 71776119061217280L;
        long  BP_STARTING_POSITIONS = 65280;
        Bitboard.printBigInteger(WP_STARTING_POSITIONS);
        Bitboard.printBigInteger(BP_STARTING_POSITIONS);
        long COMBINED_OCC = WP_STARTING_POSITIONS | BP_STARTING_POSITIONS;

        long rookMoves =  MoveUtils.GetRookMovesSeparate(COMBINED_OCC, rookSquare);
        Bitboard.printBigInteger(rookMoves);
    }

    public static void TestKnightMoves() 
    {
        long  MAX_BIGINT = -1; // Max u64 value
        long knightBitboard = 0;
        knightBitboard |= 1L << 45;
        knightBitboard |= 1L << 42;

        Bitboard.printBigInteger(knightBitboard);
        Bitboard.printBigInteger(MAX_BIGINT);

        long tempBitboard = knightBitboard;

        while (tempBitboard != 0)
        {
            int knightSquare = Bitboard.bitScanForwardSlow(tempBitboard);
            tempBitboard = tempBitboard & ~(1L << knightSquare);

            Pr.println("Knight square: " + knightSquare);

            if (knightSquare == -1) 
            {
                Pr.println("Error knight sq -1");
                break;
            }

            long knightAttacks = MoveConstants.KNIGHT_ATTACKS[knightSquare];
            Pr.println("Knight on square " + knightSquare);
            Bitboard.printBigInteger(knightAttacks);
        }
    }

    public static void printRookMoves()
    {
        for (int direction = 0; direction < 4; direction++)
        {
            Pr.println("direction: " + direction);
            for (int sq = 0; sq < 64; sq++)
            {
                Bitboard.printBigInteger(MoveConstants.ROOK_ATTACKS[direction][sq]);
            }
        }
    }

    public static void printBishopMoves()
    {
        for (int direction = 0; direction < 4; direction++)
        {
            Pr.println("direction: " + direction);
            for (int sq = 0; sq < 64; sq++)
            {
                Bitboard.printBigInteger(MoveConstants.BISHOP_ATTACKS[direction][sq]);
            }
        }
    }
}
