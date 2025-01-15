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

    public static int BitScanForward(long bitboard)
    {
        int index = 0;
        while ((bitboard & 1L << index) == 0)
        {
            index++;
        }
        return index;
    }

    public static long GetRookMovesSeparate(long combined_occ, int square) 
    {
        long combinedAttacks = 0;

        long rookAttackUp = MoveConstants.ROOK_ATTACKS[ROOK_UP][square];
        long rookAndOccs = rookAttackUp & combined_occ;
        if (rookAndOccs != 0) 
        {
            long lastValue = rookAndOccs;
            for (int i = 0; i < 8; i++) 
            {
                rookAndOccs &= rookAndOccs - 1;
                if (rookAndOccs == 0) 
                {
                    int endSquare = BitScanForward(lastValue); // Implement this method
                    combinedAttacks |= Inb.INBETWEEN_BITBOARDS[square][endSquare];
                    break;
                }
                lastValue = rookAndOccs;
            }
        } 
        else 
        {
            combinedAttacks = combinedAttacks | rookAttackUp;
        }
       // Pr.println("Rook up");
        //Pr.printBigIntegerLn(combinedAttacks);

        long rookAttackLeft = MoveConstants.ROOK_ATTACKS[ROOK_LEFT][square];
        rookAndOccs = rookAttackLeft & combined_occ;
        if (rookAndOccs != 0) 
        {
            long lastValue = rookAndOccs;
            for (int i = 0; i < 8; i++) 
            {
                rookAndOccs &= rookAndOccs - 1;
                if (rookAndOccs == 0) 
                {
                    int endSquare = BitScanForward(lastValue); // Implement this method
                    combinedAttacks = combinedAttacks | Inb.INBETWEEN_BITBOARDS[square][endSquare];
                    break;
                }
                lastValue = rookAndOccs;
            }
        } 
        else 
        {
            combinedAttacks = combinedAttacks | rookAttackLeft;
        }
        //Pr.println("Rook left");
        //Pr.printBigIntegerLn(combinedAttacks);

        long rookAttackDown = MoveConstants.ROOK_ATTACKS[ROOK_DOWN][square];
        rookAndOccs = rookAttackDown & combined_occ;
        if (rookAndOccs != 0) 
        {
            int endSquare = BitScanForward(rookAndOccs); // Implement this method
            combinedAttacks = combinedAttacks | Inb.INBETWEEN_BITBOARDS[square][endSquare];
        } 
        else 
        {
            combinedAttacks = combinedAttacks | rookAttackDown;
        }
       // Pr.println("Rook down");
      //  Pr.printBigIntegerLn(combinedAttacks);

        long rookAttackRight = MoveConstants.ROOK_ATTACKS[ROOK_RIGHT][square];
        rookAndOccs = rookAttackRight & combined_occ;
        if (rookAndOccs != 0) 
        {
            int endSquare = BitScanForward(rookAndOccs); // Implement this method
            combinedAttacks = combinedAttacks | Inb.INBETWEEN_BITBOARDS[square][endSquare];
        } 
        else 
        {
            combinedAttacks = combinedAttacks | rookAttackRight;
        }
      //  Pr.println("Rook right");
      //  Pr.printBigIntegerLn(combinedAttacks);

        return combinedAttacks;
    }    

    public static long GetBishopMovesSeparate(long combined_occ, int square) 
    {

        //Pr.printIntLn(square);
        //Pr.printSquareLn(square);

        long combinedAttacks = 0;

        long bishopAttackUpLeft = MoveConstants.BISHOP_ATTACKS[BISHOP_UP_LEFT][square];
        long bishopAndOccs = bishopAttackUpLeft & combined_occ;
        if (bishopAndOccs != 0) {
            long lastValue = bishopAndOccs;
            for (int i = 0; i < 8; i++) {
                bishopAndOccs &= bishopAndOccs - 1;
                if (bishopAndOccs == 0) {
                    int endSquare = BitScanForward(lastValue); // Implement this method
                    combinedAttacks = combinedAttacks | Inb.INBETWEEN_BITBOARDS[square][endSquare];
                    break;
                }
                lastValue = bishopAndOccs;
            }
        } else {
            combinedAttacks = combinedAttacks | bishopAttackUpLeft;
        }
       // Pr.println("Bishop up left");
        //Pr.printBigIntegerLn(combinedAttacks);

        long bishopAttackUpRight = MoveConstants.BISHOP_ATTACKS[BISHOP_UP_RIGHT][square];
        bishopAndOccs = bishopAttackUpRight & combined_occ;
        if (bishopAndOccs != 0) {
            long lastValue = bishopAndOccs;
            for (int i = 0; i < 8; i++) {
                bishopAndOccs &= bishopAndOccs - 1;
                if (bishopAndOccs == 0) {
                    int endSquare = BitScanForward(lastValue); // Implement this method
                    combinedAttacks = combinedAttacks | Inb.INBETWEEN_BITBOARDS[square][endSquare];
                    break;
                }
                lastValue = bishopAndOccs;
            }
        } else {
            combinedAttacks = combinedAttacks | bishopAttackUpRight;
        }
       // Pr.println("Bishop up right");
       // Pr.printBigIntegerLn(combinedAttacks);

        long bishopAttackDownLeft = MoveConstants.BISHOP_ATTACKS[BISHOP_DOWN_LEFT][square];

       // Pr.println("down left bitboard");
       // Pr.printBigIntegerLn(bishopAttackDownLeft);
       // Pr.println("__________");

        bishopAndOccs = bishopAttackDownLeft & combined_occ;
        if (bishopAndOccs != 0) {
            int endSquare = BitScanForward(bishopAndOccs); // Implement this method
            combinedAttacks = combinedAttacks | Inb.INBETWEEN_BITBOARDS[square][endSquare];
        } else {
            combinedAttacks = combinedAttacks | bishopAttackDownLeft;
        }
      //  Pr.println("Bishop down left");
       // Pr.printBigIntegerLn(combinedAttacks);

        long bishopAttackDownRight = MoveConstants.BISHOP_ATTACKS[BISHOP_DOWN_RIGHT][square];
        bishopAndOccs = bishopAttackDownRight & combined_occ;
        if (bishopAndOccs != 0) {
            int endSquare = BitScanForward(bishopAndOccs); // Implement this method
            combinedAttacks = combinedAttacks | Inb.INBETWEEN_BITBOARDS[square][endSquare];
        } else {
            combinedAttacks = combinedAttacks | bishopAttackDownRight;
        }
      //  Pr.println("Bishop down right");
      //  Pr.printBigIntegerLn(combinedAttacks);
        return combinedAttacks;
    }

    public static Boolean Is_Square_Attacked_By_Black_Global(int square, long occupancy)
    {
                    
        if ((Board.bitboard_array_global[GenConst.BP]&MoveConstants.WHITE_PAWN_ATTACKS[square]) != 0) {
            return true;
        }
        if ((Board.bitboard_array_global[GenConst.BN]&MoveConstants.KNIGHT_ATTACKS[square]) != 0) {
            return true;
        }
        if ((Board.bitboard_array_global[GenConst.BK]&MoveConstants.KING_ATTACKS[square]) != 0) {
            return true;
        }
        long bishopAttacks = MoveUtils.GetBishopMovesSeparate(occupancy, square);
        if ((Board.bitboard_array_global[GenConst.BB]&bishopAttacks) != 0) {
            return true;
        }
        if ((Board.bitboard_array_global[GenConst.BQ]&bishopAttacks) != 0) {
            return true;
        }
        long rookAttacks = MoveUtils.GetRookMovesSeparate(occupancy, square); 
        if ((Board.bitboard_array_global[GenConst.BR]&rookAttacks) != 0) {
            return true;
        }
        if ((Board.bitboard_array_global[GenConst.BQ]&rookAttacks) != 0) {
            return true;
        }
    
        return false;
    }


    public static Boolean Is_Square_Attacked_By_White_Global(int square, long occupancy)
    {
                    
        if ((Board.bitboard_array_global[GenConst.WP]&MoveConstants.BLACK_PAWN_ATTACKS[square]) != 0) {
            return true;
        }
        if ((Board.bitboard_array_global[GenConst.WN]&MoveConstants.KNIGHT_ATTACKS[square]) != 0) {
            return true;
        }
        if ((Board.bitboard_array_global[GenConst.WK]&MoveConstants.KING_ATTACKS[square]) != 0) {
            return true;
        }
        long bishopAttacks = MoveUtils.GetBishopMovesSeparate(occupancy, square);
        if ((Board.bitboard_array_global[GenConst.WB]&bishopAttacks) != 0) {
            return true;
        }
        if ((Board.bitboard_array_global[GenConst.WQ]&bishopAttacks) != 0) {
            return true;
        }
        long rookAttacks = MoveUtils.GetRookMovesSeparate(occupancy, square); 
        if ((Board.bitboard_array_global[GenConst.WR]&rookAttacks) != 0) {
            return true;
        }
        if ((Board.bitboard_array_global[GenConst.WQ]&rookAttacks) != 0) {
            return true;
        }
    
        return false;
    }

}
