
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


    public static long getRookMovesSeparate(long combined_occ, int square) 
    {
        long combinedAttacks = 0;

        long rookAttackUp = MoveConstants.ROOK_ATTACKS[ROOK_UP][square];
        long rookAndOccs = rookAttackUp & combined_occ;
        if (rookAndOccs != 0) 
        {
            long lastValue = rookAndOccs;
            for (int i = 0; i < 8; i++) 
            {
                rookAndOccs = rookAndOccs & rookAndOccs - 1;
                if (rookAndOccs == 0) 
                {
                    int endSquare = Perft.bitScanForward(lastValue); // Implement this method
                    combinedAttacks |= Inb.INBETWEEN_BITBOARDS[square][endSquare];
                    break;
                }
                lastValue = rookAndOccs;
            }
        } 
        else 
        {
            combinedAttacks |= rookAttackUp;
        }
       // Pr.println("Rook up");
        //Pr.printlongLn(combinedAttacks);

        long rookAttackLeft = MoveConstants.ROOK_ATTACKS[ROOK_LEFT][square];
        rookAndOccs = rookAttackLeft & combined_occ;
        if (rookAndOccs != 0) 
        {
            long lastValue = rookAndOccs;
            for (int i = 0; i < 8; i++) 
            {
                rookAndOccs = rookAndOccs & rookAndOccs - 1;
                if (rookAndOccs == 0) 
                {
                    int endSquare = Perft.bitScanForward(lastValue); // Implement this method
                    combinedAttacks |= Inb.INBETWEEN_BITBOARDS[square][endSquare];
                    break;
                }
                lastValue = rookAndOccs;
            }
        } 
        else 
        {
            combinedAttacks |= rookAttackLeft;
        }
        //Pr.println("Rook left");
        //Pr.printlongLn(combinedAttacks);

        long rookAttackDown = MoveConstants.ROOK_ATTACKS[ROOK_DOWN][square];
        rookAndOccs = rookAttackDown & combined_occ;
        if (rookAndOccs != 0) 
        {
            int endSquare = Perft.bitScanForward(rookAndOccs); // Implement this method
            combinedAttacks |= Inb.INBETWEEN_BITBOARDS[square][endSquare];
        } 
        else 
        {
            combinedAttacks |= rookAttackDown;
        }
       // Pr.println("Rook down");
      //  Pr.printlongLn(combinedAttacks);

        long rookAttackRight = MoveConstants.ROOK_ATTACKS[ROOK_RIGHT][square];
        rookAndOccs = rookAttackRight & combined_occ;
        if (rookAndOccs != 0) 
        {
            int endSquare = Perft.bitScanForward(rookAndOccs); // Implement this method
            combinedAttacks |= Inb.INBETWEEN_BITBOARDS[square][endSquare];
        } 
        else 
        {
            combinedAttacks |= rookAttackRight;
        }
      //  Pr.println("Rook right");
      //  Pr.printlongLn(combinedAttacks);

        return combinedAttacks;
    }    

    public static long getBishopMovesSeparate(long combined_occ, int square) 
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
                    int endSquare = Perft.bitScanForward(lastValue); // Implement this method
                    combinedAttacks |= Inb.INBETWEEN_BITBOARDS[square][endSquare];
                    break;
                }
                lastValue = bishopAndOccs;
            }
        } else {
            combinedAttacks |= bishopAttackUpLeft;
        }
       // Pr.println("Bishop up left");
        //Pr.printlongLn(combinedAttacks);

        long bishopAttackUpRight = MoveConstants.BISHOP_ATTACKS[BISHOP_UP_RIGHT][square];
        bishopAndOccs = bishopAttackUpRight & combined_occ;
        if (bishopAndOccs != 0) {
            long lastValue = bishopAndOccs;
            for (int i = 0; i < 8; i++) {
                bishopAndOccs &= bishopAndOccs - 1;
                if (bishopAndOccs == 0) {
                    int endSquare = Perft.bitScanForward(lastValue); // Implement this method
                    combinedAttacks |= Inb.INBETWEEN_BITBOARDS[square][endSquare];
                    break;
                }
                lastValue = bishopAndOccs;
            }
        } else {
            combinedAttacks |= bishopAttackUpRight;
        }
       // Pr.println("Bishop up right");
       // Pr.printlongLn(combinedAttacks);

        long bishopAttackDownLeft = MoveConstants.BISHOP_ATTACKS[BISHOP_DOWN_LEFT][square];

       // Pr.println("down left bitboard");
       // Pr.printlongLn(bishopAttackDownLeft);
       // Pr.println("__________");

        bishopAndOccs = bishopAttackDownLeft & combined_occ;
        if (bishopAndOccs != 0) {
            int endSquare = Perft.bitScanForward(bishopAndOccs); // Implement this method
            combinedAttacks |= Inb.INBETWEEN_BITBOARDS[square][endSquare];
        } else {
            combinedAttacks |= bishopAttackDownLeft;
        }
      //  Pr.println("Bishop down left");
       // Pr.printlongLn(combinedAttacks);

        long bishopAttackDownRight = MoveConstants.BISHOP_ATTACKS[BISHOP_DOWN_RIGHT][square];
        bishopAndOccs = bishopAttackDownRight & combined_occ;
        if (bishopAndOccs != 0) {
            int endSquare = Perft.bitScanForward(bishopAndOccs); // Implement this method
            combinedAttacks |= Inb.INBETWEEN_BITBOARDS[square][endSquare];
        } else {
            combinedAttacks |= bishopAttackDownRight;
        }
      //  Pr.println("Bishop down right");
      //  Pr.printlongLn(combinedAttacks);
        return combinedAttacks;
    }

    public static Boolean Is_Square_Attacked_By_Black_Global(int square, long occupancy)
    {
                    
       // if (Board.bitboard_array_global[GenConst.BP] & MoveConstants.WHITE_PAWN_ATTACKS[square]).signum() != 0) {
            //return true;
        //}
        //if (Board.bitboard_array_global[GenConst.BN] & MoveConstants.KNIGHT_ATTACKS[square]).signum() != 0) {
            //return true;
        //}
        //if (Board.bitboard_array_global[GenConst.BK] & MoveConstants.KING_ATTACKS[square]).signum() != 0) {
         ///   return true;
        //}
        //long bishopAttacks = MoveUtils.GetBishopMovesSeparate(occupancy, square);
        //if (Board.bitboard_array_global[GenConst.BB] & bishopAttacks).signum() != 0) {
         ///   return true;
        //}
        //if (Board.bitboard_array_global[GenConst.BQ] & bishopAttacks).signum() != 0) {
         ///   return true;
        //}
        //B/igInteger rookAttacks = MoveUtils.GetRookMovesSeparate(occupancy, square); 
        //if (Board.bitboard_array_global[GenConst.BR] & rookAttacks).signum() != 0) {
            //return true;
        //}
        //if (Board.bitboard_array_global[GenConst.BQ] & rookAttacks).signum() != 0) {
            //return true;
        //}
    
        return false;
    }


    public static Boolean Is_Square_Attacked_By_White_Global(int square, long occupancy)
    {
                    
       // if (Board.bitboard_array_global[GenConst.WP] & MoveConstants.BLACK_PAWN_ATTACKS[square]).signum() != 0) {
            //return true;
        //}
        //if (Board.bitboard_array_global[GenConst.WN] & MoveConstants.KNIGHT_ATTACKS[square]).signum() != 0) {
            //return true;
        //}
        //if (Board.bitboard_array_global[GenConst.WK] & MoveConstants.KING_ATTACKS[square]).signum() != 0) {
            //return true;
        //}
        //long bishopAttacks = MoveUtils.GetBishopMovesSeparate(occupancy, square);
        //if (Board.bitboard_array_global[GenConst.WB] & bishopAttacks).signum() != 0) {
            //return true;
        //}
        //if (Board.bitboard_array_global[GenConst.WQ] & bishopAttacks).signum() != 0) {
            //return true;
        //}
        //long rookAttacks = MoveUtils.GetRookMovesSeparate(occupancy, square); 
        //if (Board.bitboard_array_global[GenConst.WR] & rookAttacks).signum() != 0) {
            //return true;
        //}
        //if (Board.bitboard_array_global[GenConst.WQ] & rookAttacks).signum() != 0) {
            //return true;
        //}
    
        return false;
    }


}
