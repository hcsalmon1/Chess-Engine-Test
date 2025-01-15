public class Bitboard
{

    public static long addBit(long bitboard, int square)
    {
        return bitboard | 1L << square;
    }

    public static void printBigInteger(long bitboard)
    {
        for (int rank = 0; rank < 8; rank++)
        {
            for (int file = 0; file < 8; file++)
            {
                int square = rank * 8 + file;
                processSquare(bitboard, square);
            }
            System.out.println();
        }
        System.out.println("BigInteger: " + bitboard);
    }

    static void processSquare(long bitboard, int square)
    {
        if ((bitboard & 1L << square)!=0)
        {
            System.out.print("X ");
        }
        else
        {
            System.out.print("_ ");
        }
    }

    static int bitScanForwardSlow(long bitboard)
    {
        for (int i = 0; i < 64; i++)
        {
            if ((bitboard & 1L << i)!=0)
            {
                return i;
            }
        }
        return -1;
    }
}
