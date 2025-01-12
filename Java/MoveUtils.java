import java.math.BigInteger;

public class MoveUtils 
{

    static final int BISHOP_UP_LEFT = 0;
    static final int BISHOP_UP_RIGHT = 1;
    static final int BISHOP_DOWN_LEFT = 2;
    static final int BISHOP_DOWN_RIGHT = 3;

    static final int ROOK_UP = 0;
    static final int ROOK_DOWN = 2;
    static final int ROOK_LEFT = 3;
    static final int ROOK_RIGHT = 1;

    public static int BitScanForward(BigInteger bitboard)
    {
        int index = 0;
        while (bitboard.and(BigInteger.ONE.shiftLeft(index)).equals(BigInteger.ZERO))
        {
            index++;
        }
        return index;
    }

    public static BigInteger GetRookMovesSeparate(BigInteger combined_occ, int square) 
    {
        BigInteger combinedAttacks = BigInteger.ZERO;

        BigInteger rookAttackUp = MoveConstants.ROOK_ATTACKS[ROOK_UP][square];
        BigInteger rookAndOccs = rookAttackUp.and(combined_occ);
        if (!rookAndOccs.equals(BigInteger.ZERO)) 
        {
            BigInteger lastValue = rookAndOccs;
            for (int i = 0; i < 8; i++) 
            {
                rookAndOccs = rookAndOccs.and(rookAndOccs.subtract(BigInteger.ONE));
                if (rookAndOccs.equals(BigInteger.ZERO)) 
                {
                    int endSquare = BitScanForward(lastValue); // Implement this method
                    combinedAttacks = combinedAttacks.or(Inb.INBETWEEN_BITBOARDS[square][endSquare]);
                    break;
                }
                lastValue = rookAndOccs;
            }
        } 
        else 
        {
            combinedAttacks = combinedAttacks.or(rookAttackUp);
        }
       // Pr.println("Rook up");
        //Pr.printBigIntegerLn(combinedAttacks);

        BigInteger rookAttackLeft = MoveConstants.ROOK_ATTACKS[ROOK_LEFT][square];
        rookAndOccs = rookAttackLeft.and(combined_occ);
        if (!rookAndOccs.equals(BigInteger.ZERO)) 
        {
            BigInteger lastValue = rookAndOccs;
            for (int i = 0; i < 8; i++) 
            {
                rookAndOccs = rookAndOccs.and(rookAndOccs.subtract(BigInteger.ONE));
                if (rookAndOccs.equals(BigInteger.ZERO)) 
                {
                    int endSquare = BitScanForward(lastValue); // Implement this method
                    combinedAttacks = combinedAttacks.or(Inb.INBETWEEN_BITBOARDS[square][endSquare]);
                    break;
                }
                lastValue = rookAndOccs;
            }
        } 
        else 
        {
            combinedAttacks = combinedAttacks.or(rookAttackLeft);
        }
        //Pr.println("Rook left");
        //Pr.printBigIntegerLn(combinedAttacks);

        BigInteger rookAttackDown = MoveConstants.ROOK_ATTACKS[ROOK_DOWN][square];
        rookAndOccs = rookAttackDown.and(combined_occ);
        if (!rookAndOccs.equals(BigInteger.ZERO)) 
        {
            int endSquare = BitScanForward(rookAndOccs); // Implement this method
            combinedAttacks = combinedAttacks.or(Inb.INBETWEEN_BITBOARDS[square][endSquare]);
        } 
        else 
        {
            combinedAttacks = combinedAttacks.or(rookAttackDown);
        }
       // Pr.println("Rook down");
      //  Pr.printBigIntegerLn(combinedAttacks);

        BigInteger rookAttackRight = MoveConstants.ROOK_ATTACKS[ROOK_RIGHT][square];
        rookAndOccs = rookAttackRight.and(combined_occ);
        if (!rookAndOccs.equals(BigInteger.ZERO)) 
        {
            int endSquare = BitScanForward(rookAndOccs); // Implement this method
            combinedAttacks = combinedAttacks.or(Inb.INBETWEEN_BITBOARDS[square][endSquare]);
        } 
        else 
        {
            combinedAttacks = combinedAttacks.or(rookAttackRight);
        }
      //  Pr.println("Rook right");
      //  Pr.printBigIntegerLn(combinedAttacks);

        return combinedAttacks;
    }    

    public static BigInteger GetBishopMovesSeparate(BigInteger combined_occ, int square) 
    {

        //Pr.printIntLn(square);
        //Pr.printSquareLn(square);

        BigInteger combinedAttacks = BigInteger.ZERO;

        BigInteger bishopAttackUpLeft = MoveConstants.BISHOP_ATTACKS[BISHOP_UP_LEFT][square];
        BigInteger bishopAndOccs = bishopAttackUpLeft.and(combined_occ);
        if (!bishopAndOccs.equals(BigInteger.ZERO)) {
            BigInteger lastValue = bishopAndOccs;
            for (int i = 0; i < 8; i++) {
                bishopAndOccs = bishopAndOccs.and(bishopAndOccs.subtract(BigInteger.ONE));
                if (bishopAndOccs.equals(BigInteger.ZERO)) {
                    int endSquare = BitScanForward(lastValue); // Implement this method
                    combinedAttacks = combinedAttacks.or(Inb.INBETWEEN_BITBOARDS[square][endSquare]);
                    break;
                }
                lastValue = bishopAndOccs;
            }
        } else {
            combinedAttacks = combinedAttacks.or(bishopAttackUpLeft);
        }
       // Pr.println("Bishop up left");
        //Pr.printBigIntegerLn(combinedAttacks);

        BigInteger bishopAttackUpRight = MoveConstants.BISHOP_ATTACKS[BISHOP_UP_RIGHT][square];
        bishopAndOccs = bishopAttackUpRight.and(combined_occ);
        if (!bishopAndOccs.equals(BigInteger.ZERO)) {
            BigInteger lastValue = bishopAndOccs;
            for (int i = 0; i < 8; i++) {
                bishopAndOccs = bishopAndOccs.and(bishopAndOccs.subtract(BigInteger.ONE));
                if (bishopAndOccs.equals(BigInteger.ZERO)) {
                    int endSquare = BitScanForward(lastValue); // Implement this method
                    combinedAttacks = combinedAttacks.or(Inb.INBETWEEN_BITBOARDS[square][endSquare]);
                    break;
                }
                lastValue = bishopAndOccs;
            }
        } else {
            combinedAttacks = combinedAttacks.or(bishopAttackUpRight);
        }
       // Pr.println("Bishop up right");
       // Pr.printBigIntegerLn(combinedAttacks);

        BigInteger bishopAttackDownLeft = MoveConstants.BISHOP_ATTACKS[BISHOP_DOWN_LEFT][square];

       // Pr.println("down left bitboard");
       // Pr.printBigIntegerLn(bishopAttackDownLeft);
       // Pr.println("__________");

        bishopAndOccs = bishopAttackDownLeft.and(combined_occ);
        if (!bishopAndOccs.equals(BigInteger.ZERO)) {
            int endSquare = BitScanForward(bishopAndOccs); // Implement this method
            combinedAttacks = combinedAttacks.or(Inb.INBETWEEN_BITBOARDS[square][endSquare]);
        } else {
            combinedAttacks = combinedAttacks.or(bishopAttackDownLeft);
        }
      //  Pr.println("Bishop down left");
       // Pr.printBigIntegerLn(combinedAttacks);

        BigInteger bishopAttackDownRight = MoveConstants.BISHOP_ATTACKS[BISHOP_DOWN_RIGHT][square];
        bishopAndOccs = bishopAttackDownRight.and(combined_occ);
        if (!bishopAndOccs.equals(BigInteger.ZERO)) {
            int endSquare = BitScanForward(bishopAndOccs); // Implement this method
            combinedAttacks = combinedAttacks.or(Inb.INBETWEEN_BITBOARDS[square][endSquare]);
        } else {
            combinedAttacks = combinedAttacks.or(bishopAttackDownRight);
        }
      //  Pr.println("Bishop down right");
      //  Pr.printBigIntegerLn(combinedAttacks);
        return combinedAttacks;
    }

    public static Boolean Is_Square_Attacked_By_Black_Global(int square, BigInteger occupancy)
    {
                    
        if (Board.bitboard_array_global[GenConst.BP].and(MoveConstants.WHITE_PAWN_ATTACKS[square]).signum() != 0) {
            return true;
        }
        if (Board.bitboard_array_global[GenConst.BN].and(MoveConstants.KNIGHT_ATTACKS[square]).signum() != 0) {
            return true;
        }
        if (Board.bitboard_array_global[GenConst.BK].and(MoveConstants.KING_ATTACKS[square]).signum() != 0) {
            return true;
        }
        BigInteger bishopAttacks = MoveUtils.GetBishopMovesSeparate(occupancy, square);
        if (Board.bitboard_array_global[GenConst.BB].and(bishopAttacks).signum() != 0) {
            return true;
        }
        if (Board.bitboard_array_global[GenConst.BQ].and(bishopAttacks).signum() != 0) {
            return true;
        }
        BigInteger rookAttacks = MoveUtils.GetRookMovesSeparate(occupancy, square); 
        if (Board.bitboard_array_global[GenConst.BR].and(rookAttacks).signum() != 0) {
            return true;
        }
        if (Board.bitboard_array_global[GenConst.BQ].and(rookAttacks).signum() != 0) {
            return true;
        }
    
        return false;
    }


    public static Boolean Is_Square_Attacked_By_White_Global(int square, BigInteger occupancy)
    {
                    
        if (Board.bitboard_array_global[GenConst.WP].and(MoveConstants.BLACK_PAWN_ATTACKS[square]).signum() != 0) {
            return true;
        }
        if (Board.bitboard_array_global[GenConst.WN].and(MoveConstants.KNIGHT_ATTACKS[square]).signum() != 0) {
            return true;
        }
        if (Board.bitboard_array_global[GenConst.WK].and(MoveConstants.KING_ATTACKS[square]).signum() != 0) {
            return true;
        }
        BigInteger bishopAttacks = MoveUtils.GetBishopMovesSeparate(occupancy, square);
        if (Board.bitboard_array_global[GenConst.WB].and(bishopAttacks).signum() != 0) {
            return true;
        }
        if (Board.bitboard_array_global[GenConst.WQ].and(bishopAttacks).signum() != 0) {
            return true;
        }
        BigInteger rookAttacks = MoveUtils.GetRookMovesSeparate(occupancy, square); 
        if (Board.bitboard_array_global[GenConst.WR].and(rookAttacks).signum() != 0) {
            return true;
        }
        if (Board.bitboard_array_global[GenConst.WQ].and(rookAttacks).signum() != 0) {
            return true;
        }
    
        return false;
    }

}
