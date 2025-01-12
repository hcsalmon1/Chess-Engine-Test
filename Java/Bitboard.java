import java.math.BigInteger;

public class Bitboard 
{

    public static BigInteger addBit(BigInteger bitboard, int square) 
    {
        return bitboard.setBit(square);
    }

    public static void printBigInteger(BigInteger bitboard) 
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

    static void processSquare(BigInteger bitboard, int square) 
    {
        if (bitboard.testBit(square)) 
        {
            System.out.print("X ");
        } 
        else 
        {
            System.out.print("_ ");
        }
    }

    static int bitScanForwardSlow(BigInteger bitboard) 
    {
        for (int i = 0; i < 64; i++) 
        {
            if (bitboard.testBit(i)) 
            {
                return i;
            }
        }
        return -1;
    }
}
