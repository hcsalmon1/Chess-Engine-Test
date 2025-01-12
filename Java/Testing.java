import java.math.BigInteger;

public class Testing 
{

    public static void testRookMoves()
    {
        int rookSquare = 36;

        BigInteger WP_STARTING_POSITIONS = new BigInteger("71776119061217280");
        BigInteger BP_STARTING_POSITIONS = new BigInteger("65280");
        BigInteger COMBINED_OCC = WP_STARTING_POSITIONS.or(BP_STARTING_POSITIONS);

        BigInteger rookMoves =  MoveUtils.GetRookMovesSeparate(COMBINED_OCC, rookSquare);
        Bitboard.printBigInteger(rookMoves);
    }

    public static void TestKnightMoves() 
    {
        BigInteger MAX_BIGINT = new BigInteger("18446744073709551615"); // Max u64 value
        BigInteger knightBitboard = BigInteger.ZERO;
        knightBitboard = knightBitboard.setBit(45);
        knightBitboard = knightBitboard.setBit(42);

        Bitboard.printBigInteger(knightBitboard);
        Bitboard.printBigInteger(MAX_BIGINT);

        BigInteger tempBitboard = knightBitboard;

        while (!tempBitboard.equals(BigInteger.ZERO)) 
        {
            int knightSquare = Bitboard.bitScanForwardSlow(tempBitboard);
            tempBitboard = tempBitboard.clearBit(knightSquare);

            Pr.println("Knight square: " + knightSquare);

            if (knightSquare == -1) 
            {
                Pr.println("Error knight sq -1");
                break;
            }

            BigInteger knightAttacks = MoveConstants.KNIGHT_ATTACKS[knightSquare];
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
