
import java.math.BigInteger;


class ErrorInt
{
    private Error error;
    private int value;

    // No-argument constructor
    public ErrorInt(Error _error, int _value) {
        this.error = _error; // or any default Error value
        this.value = _value;    // default value
    }

    public static void printError(Error error) {
        System.out.println(error);
    }

    // Getters for error and value
    public Error getError() {
        return error;
    }

    public int getValue() {
        return value;
    }

    // Setter for value
    public void setValue(int value) {
        this.value = value;
    }
}


class DebugInfo
{
    public int CallCount;
    public int LastStarting;
    public int LastTarget;
    public int LastTag;
    public int lastPiece;
    public boolean PromotionExpected;
}


enum Error
{
    White_King_Captured,
    Black_King_Captured,
    Too_Many_Moves,
    White_King_In_Check_On_BlackMove,
    Black_King_In_Check_On_White_Move,
    Copy_Boards_Not_Same,
    Invalid_Starting_Square,
    Invalid_Target_Square,
    Invalid_Tag,
    Invalid_Piece,
    Capture_Index_Invalid,
    Capture_Piece_Not_Found,
    Starting_Depth_Too_High,
    Starting_Depth_Zero,
    Depth_Less_Than_Zero,
    Starting_Square_And_Target_Square_The_Same,
    Ep_Not_Target_Square,
    Promotion_When_Not_Expected,
    Side_Not_Changed,
    Side_Not_Changed_Back,
}

public class Perft 
{

    static Boolean OutOfBounds(int move) {

        if (move < 0) {
            return true;
        }
        if (move > 63) {
            return true;
        }
        return false;
    }

    static void PrintMoveNoNL(int starting, int target_square, int tag) { //starting

        // Print starting
        if (OutOfBounds(starting)) {
            System.out.printf("%d", starting);
        } else {
            System.out.printf("%c%c", GenConst.SQ_CHAR_X[starting], GenConst.SQ_CHAR_Y[starting]);
        }

        // Print target
        if (OutOfBounds(target_square)) {
            System.out.printf("%d", target_square);
        } else {
            System.out.printf("%c%c", GenConst.SQ_CHAR_X[target_square], GenConst.SQ_CHAR_Y[target_square]);
        }

        // Print promotion tag
        switch (tag) {
            case GenConst.TAG_B_N_PROMOTION:
            case GenConst.TAG_B_N_PROMOTION_CAP:
            case GenConst.TAG_W_N_PROMOTION:
            case GenConst.TAG_W_N_PROMOTION_CAP:
                System.out.printf("n");
                break;
            case GenConst.TAG_B_R_PROMOTION:
            case GenConst.TAG_B_R_PROMOTION_CAP:
            case GenConst.TAG_W_R_PROMOTION:
            case GenConst.TAG_W_R_PROMOTION_CAP:
                System.out.printf("r");
                break;
            case GenConst.TAG_B_B_PROMOTION:
            case GenConst.TAG_B_B_PROMOTION_CAP:
            case GenConst.TAG_W_B_PROMOTION:
            case GenConst.TAG_W_B_PROMOTION_CAP:
                System.out.printf("b");
                break;
            case GenConst.TAG_B_Q_PROMOTION:
            case GenConst.TAG_B_Q_PROMOTION_CAP:
            case GenConst.TAG_W_Q_PROMOTION:
            case GenConst.TAG_W_Q_PROMOTION_CAP:
                System.out.printf("q");
                break;
            default:
                break;
        }
    }

    static final int MOVE_STARTING = 0;
    static final int MOVE_TARGET = 1;
    static final int MOVE_PIECE = 2;
    static final int MOVE_TAG = 3;
    static final int NO_SQUARE = 64;

    static final int WKS_CASTLE_RIGHTS = 0;
    static final int WQS_CASTLE_RIGHTS = 1;
    static final int BKS_CASTLE_RIGHTS = 2;
    static final int BQS_CASTLE_RIGHTS = 3;

    static final int PINNED_SQUARE_INDEX = 0;
    static final int PINNING_PIECE_INDEX = 1;

    static final BigInteger WKS_EMPTY_BITBOARD = new BigInteger("6917529027641081856");
    static final BigInteger WQS_EMPTY_BITBOARD = new BigInteger("1008806316530991104");
    static final BigInteger BKS_EMPTY_BITBOARD = new BigInteger("96");
    static final BigInteger BQS_EMPTY_BITBOARD = new BigInteger("14");

    static final BigInteger RANK_1_BITBOARD = new BigInteger( "18374686479671623680");
    static final BigInteger RANK_2_BITBOARD =new BigInteger( "71776119061217280");
    static final BigInteger RANK_3_BITBOARD =new BigInteger( "280375465082880");
    static final BigInteger RANK_4_BITBOARD =new BigInteger( "1095216660480");
    static final BigInteger RANK_5_BITBOARD =new BigInteger( "4278190080");
    static final BigInteger RANK_6_BITBOARD =new BigInteger( "16711680");
    static final BigInteger RANK_7_BITBOARD =new BigInteger( "65280");
    static final BigInteger RANK_8_BITBOARD =new BigInteger( "255");

    static final BigInteger MAX_long = new BigInteger("18446744073709551615");

    static boolean Is_Square_Attacked_By_Black_Global(int square, long occupancy)
    {
        if ((Board.bitboard_array_global[GenConst.BP] & MoveConstants.WHITE_PAWN_ATTACKS[square]) != 0)
        {
            return true;
        }
        if ((Board.bitboard_array_global[GenConst.BN] & MoveConstants.KNIGHT_ATTACKS[square]) != 0)
        {
            return true;
        }
        if ((Board.bitboard_array_global[GenConst.BK] & MoveConstants.KING_ATTACKS[square]) != 0)
        {
            return true;
        }
        long bishopAttacks = MoveUtils.getBishopMovesSeparate(occupancy, square);
        if ((Board.bitboard_array_global[GenConst.BB] & bishopAttacks) != 0)
        {
            return true;
        }
        if ((Board.bitboard_array_global[GenConst.BQ] & bishopAttacks) != 0)
        {
            return true;
        }
        long rookAttacks = MoveUtils.getRookMovesSeparate(occupancy, square);
        if ((Board.bitboard_array_global[GenConst.BR] & rookAttacks) != 0)
        {
            return true;
        }
        if ((Board.bitboard_array_global[GenConst.BQ] & rookAttacks) != 0)
        {
            return true;
        }
        return false;
    }

    static boolean Is_Square_Attacked_By_White_Global(int square, long occupancy)
    {
        if ((Board.bitboard_array_global[GenConst.WP] & MoveConstants.BLACK_PAWN_ATTACKS[square]) != 0)
        {
            return true;
        }
        if ((Board.bitboard_array_global[GenConst.WN] & MoveConstants.KNIGHT_ATTACKS[square]) != 0)
        {
            return true;
        }
        if ((Board.bitboard_array_global[GenConst.WK] & MoveConstants.KING_ATTACKS[square]) != 0)
        {
            return true;
        }
        long bishopAttacks = MoveUtils.getBishopMovesSeparate(occupancy, square);
        if ((Board.bitboard_array_global[GenConst.WB] & bishopAttacks) != 0)
        {
            return true;
        }
        if ((Board.bitboard_array_global[GenConst.WQ] & bishopAttacks) != 0)
        {
            return true;
        }
        long rookAttacks = MoveUtils.getRookMovesSeparate(occupancy, square);
        if ((Board.bitboard_array_global[GenConst.WR] & rookAttacks) != 0)
        {
            return true;
        }
        if ((Board.bitboard_array_global[GenConst.WQ] & rookAttacks) != 0)
        {
            return true;
        }
        return false;
    }


    static final long MAGIC = 285870213051386505L;

    static final int[] DEBRUIJN64 = {
        0, 47,  1, 56, 48, 27,  2, 60,
        57, 49, 41, 37, 28, 16,  3, 61,
        54, 58, 35, 52, 50, 42, 21, 44,
        38, 32, 29, 23, 17, 11,  4, 62,
        46, 55, 26, 59, 40, 36, 15, 53,
        34, 51, 20, 43, 31, 22, 10, 45,
        25, 39, 14, 33, 19, 30,  9, 24,
        13, 18,  8, 12,  7,  6,  5, 63
    };


    static int bitScanForward(long tempBitboard)
    {
        long debruijnIndex = ((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58);
        //PrintBitboard(debruijnIndex);
        int indexAsInteger = (int)debruijnIndex;
        return (DEBRUIJN64[indexAsInteger]);
    }


    static boolean isNotZero(BigInteger bitboard) {
        return bitboard.signum() != 0;
    }

    static boolean isZero(BigInteger bitboard) {
        return bitboard.signum() == 0;
    }

    static BigInteger removeBit(BigInteger bitboard) {
        return bitboard.and(bitboard.subtract(BigInteger.ONE));
    }

    static void printMoveInfo(int startingSquare, int targetSquare, int tag, int piece) 
    {
        Pr.println("\n    Move Info:");
        Pr.print("     Starting Square: ");
        Pr.printInt(startingSquare);
        Pr.print(" ");
        Pr.printSquareLn(startingSquare);
        Pr.print("     Target Square: ");
        Pr.printInt(targetSquare);
        Pr.print(" ");
        Pr.printSquareLn(targetSquare);
        Pr.print("     Tag: ");
        Pr.printIntLn(tag);
        Pr.print("     Piece: ");
        Pr.printIntLn(piece);
    }

    static ErrorInt errorIntFromValue(int value)
    {
        return new ErrorInt(null, value);
    }

    static ErrorInt errorIntFromError(Error error)
    {
        return new ErrorInt(error, 0);
    }

    static long CombineBitboardsGlobal()
    {
        return Board.bitboard_array_global[0] |
        Board.bitboard_array_global[1] |
        Board.bitboard_array_global[2] |
        Board.bitboard_array_global[3] |
        Board.bitboard_array_global[4] |
        Board.bitboard_array_global[5] |
        Board.bitboard_array_global[6] |
        Board.bitboard_array_global[7] |
        Board.bitboard_array_global[8] |
        Board.bitboard_array_global[9] |
        Board.bitboard_array_global[10] |
        Board.bitboard_array_global[11];
    }

    static void PrintAllDebug(int startingSquare, int targetSquare, int piece, int tag)
    {
        PrintPiece(piece);
        PrintMoveNoNL(startingSquare, targetSquare, tag);
        System.out.println();
        PrintBoardGlobal();
    }

    
    static final char[] SQ_CHAR_Y = {
        '8','8','8','8','8','8','8','8',
        '7','7','7','7','7','7','7','7',
        '6','6','6','6','6','6','6','6',
        '5','5','5','5','5','5','5','5',
        '4','4','4','4','4','4','4','4',
        '3','3','3','3','3','3','3','3',
        '2','2','2','2','2','2','2','2',
        '1','1','1','1','1','1','1','1','A'
    };

    static final char[] SQ_CHAR_X = {
        'a','b','c','d','e','f','g','h',
        'a','b','c','d','e','f','g','h',
        'a','b','c','d','e','f','g','h',
        'a','b','c','d','e','f','g','h',
        'a','b','c','d','e','f','g','h',
        'a','b','c','d','e','f','g','h',
        'a','b','c','d','e','f','g','h',
        'a','b','c','d','e','f','g','h','N'
    };


    static void PrintBoardGlobal()
    {
        char[] yCoordinates = { '8', '7', '6', '5', '4', '3', '2', '1' };
        char[] castleChars = { 'K', 'Q', 'k', 'q' };

        for (int y = 0; y < 8; y++)
        {
            System.out.print(String.format("  %c  ", yCoordinates[y]));
            for (int x = 0; x < 8; x++)
            {
                int square = (y * 8) + x;
                boolean pieceFound = false;

                for (int pieceIndex = 0; pieceIndex < 12; pieceIndex++)
                {
                    //Pr.print("piece index: "); Pr.printIntLn(pieceIndex);
                    if ((Board.bitboard_array_global[pieceIndex] & MoveConstants.SQUARE_BBS[square]) != 0)
                    {
                      //  Pr.print("not zero, anded together:");
                        //PrintBitboard(Board.bitboard_array_global[pieceIndex] & MoveConstants.SQUARE_BBS[square]);
                        System.out.print(PIECE_COLOURS[pieceIndex]);
                        System.out.print(PIECE_CHARS[pieceIndex]);
                        System.out.print(' ');
                        pieceFound = true;
                        break;
                    }
                }

                if (pieceFound == false)
                {
                    System.out.print("-- ");
                }
            }
            System.out.print('\n');
        }
        System.out.println("    A  B  C  D  E  F  G  H\n\n");
        if (Board.is_white_global == true)
        {
            System.out.println("Side: White To Play\n");
        }
        else
        {
            System.out.println("Side: Black To Play\n");
        }
        System.out.print("castle: ");
        for (int i = 0; i < 4; i++)
        {
            if (Board.castle_rights_global[i] == true)
            {
                System.out.print(castleChars[i]);
            }
            else
            {
                System.out.print("-");
            }
        }
        System.out.print("\nEP: ");
        if (Board.ep_global > 63 || Board.ep_global < 0)
        {
            System.out.println(Board.ep_global);
        }
        else
        {
            System.out.println(SQ_CHAR_X[Board.ep_global]);
            System.out.println(SQ_CHAR_Y[Board.ep_global]);
        }

        System.out.print("\n\n");
    }
    
    static final char[] PIECE_COLOURS = { 'W','W','W','W','W','W','b','b','b','b','b','b' };
    static final char[] PIECE_CHARS = { 'P','N','B','R','Q','K','p','n','b','r','q','k' };

    static void PrintPiece(int piece)
    {
        System.out.print(PIECE_COLOURS[piece]);
        System.out.print(PIECE_CHARS[piece]);
    }
    static void PrintPieceLn(int piece)
    {
        System.out.print(PIECE_COLOURS[piece]);
        System.out.println(PIECE_CHARS[piece]);
    }

    
    enum CaptureType
    {
        None, White, Black
    }

    private static int FindCaptureBlack(int targetSquare, int captureIndex)
    {
        for (int i = GenConst.BP; i <= GenConst.BK; ++i)
        {
            if ((Board.bitboard_array_global[i] & MoveConstants.SQUARE_BBS[targetSquare]) != 0)
            {
                captureIndex = i;
                break;
            }
        }
        Board.bitboard_array_global[captureIndex] &= ~MoveConstants.SQUARE_BBS[targetSquare];
        return captureIndex;
    }

    private static void MovePiecePromote(int startingSquare, int targetSquare, int piece, int promotionPiece)
    {
        Board.bitboard_array_global[promotionPiece] |= MoveConstants.SQUARE_BBS[targetSquare];
        Board.bitboard_array_global[piece] &= ~MoveConstants.SQUARE_BBS[startingSquare];
    }

    private static void RemovePiece(int piece, int targetSquare)
    {
        Board.bitboard_array_global[piece] &= ~MoveConstants.SQUARE_BBS[targetSquare];
    }

    private static void MovePiece(int startingSquare, int targetSquare, int piece)
    {
        Board.bitboard_array_global[piece] |= MoveConstants.SQUARE_BBS[targetSquare];
        Board.bitboard_array_global[piece] &= ~MoveConstants.SQUARE_BBS[startingSquare];
    }

    private static int FindCaptureWhite(int targetSquareCopy, int captureIndex)
    {
        for (int i = GenConst.WP; i <= GenConst.WK; ++i)
        {
            if ((Board.bitboard_array_global[i] & MoveConstants.SQUARE_BBS[targetSquareCopy]) != 0)
            {
                captureIndex = i;
                break;
            }
        }
        Board.bitboard_array_global[captureIndex] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];
        return captureIndex;
    }

    private static void UnmakeMove(int startingSquareCopy, int targetSquareCopy, int piece, int tag, int captureIndex)
    {
        Board.is_white_global = !Board.is_white_global;
        switch (tag)
        {
            case 0: //none
            case 26: //check
            Board.bitboard_array_global[piece] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[piece] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];

                break;
            case 1: //capture
            case 27: //check cap
            Board.bitboard_array_global[piece] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[piece] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];
                if (piece >= GenConst.WP && piece <= GenConst.WK)
                {
                    Board.bitboard_array_global[captureIndex] |= MoveConstants.SQUARE_BBS[targetSquareCopy];
                }
                else //is black
                {
                    Board.bitboard_array_global[captureIndex] |= MoveConstants.SQUARE_BBS[targetSquareCopy];
                }

                break;
            case 2: //white ep
            Board.bitboard_array_global[GenConst.WP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.WP] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];
            Board.bitboard_array_global[GenConst.BP] |= MoveConstants.SQUARE_BBS[targetSquareCopy + 8];

                break;
            case 3: //black ep
            Board.bitboard_array_global[GenConst.BP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.BP] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];
            Board.bitboard_array_global[GenConst.WP] |= MoveConstants.SQUARE_BBS[targetSquareCopy - 8];

                break;
            case 4: //WKS
                    //white king
                    Board.bitboard_array_global[GenConst.WK] |= MoveConstants.SQUARE_BBS[GenConst.E1];
                    Board.bitboard_array_global[GenConst.WK] &= ~MoveConstants.SQUARE_BBS[GenConst.G1];
                //white rook
                Board.bitboard_array_global[GenConst.WR] |= MoveConstants.SQUARE_BBS[GenConst.H1];
                Board.bitboard_array_global[GenConst.WR] &= ~MoveConstants.SQUARE_BBS[GenConst.F1];
                break;
            case 5: //WQS
                    //white king
                    Board.bitboard_array_global[GenConst.WK] |= MoveConstants.SQUARE_BBS[GenConst.E1];
                    Board.bitboard_array_global[GenConst.WK] &= ~MoveConstants.SQUARE_BBS[GenConst.C1];
                //white rook
                Board.bitboard_array_global[GenConst.WR] |= MoveConstants.SQUARE_BBS[GenConst.A1];
                Board.bitboard_array_global[GenConst.WR] &= ~MoveConstants.SQUARE_BBS[GenConst.D1];
                break;
            case 6: //BKS
                    //white king
                    Board.bitboard_array_global[GenConst.BK] |= MoveConstants.SQUARE_BBS[GenConst.E8];
                    Board.bitboard_array_global[GenConst.BK] &= ~MoveConstants.SQUARE_BBS[GenConst.G8];
                //white rook
                Board.bitboard_array_global[GenConst.BR] |= MoveConstants.SQUARE_BBS[GenConst.H8];
                Board.bitboard_array_global[GenConst.BR] &= ~MoveConstants.SQUARE_BBS[GenConst.F8];
                break;
            case 7: //BQS
                    //white king
                    Board.bitboard_array_global[GenConst.BK] |= MoveConstants.SQUARE_BBS[GenConst.E8];
                    Board.bitboard_array_global[GenConst.BK] &= ~MoveConstants.SQUARE_BBS[GenConst.C8];
                //white rook
                Board.bitboard_array_global[GenConst.BR] |= MoveConstants.SQUARE_BBS[GenConst.A8];
                Board.bitboard_array_global[GenConst.BR] &= ~MoveConstants.SQUARE_BBS[GenConst.D8];

                break;

            //#region Promotion Unmakemove
            case 8: //BNPr
            Board.bitboard_array_global[GenConst.BP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.BN] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];
                break;
            case 9: //BBPr
            Board.bitboard_array_global[GenConst.BP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.BB] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];
                break;
            case 10: //BQPr
            Board.bitboard_array_global[GenConst.BP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.BQ] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];
                break;
            case 11: //BRPr
            Board.bitboard_array_global[GenConst.BP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.BR] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];
                break;
            case 12: //WNPr
            Board.bitboard_array_global[GenConst.WP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.WN] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];
                break;
            case 13: //WBPr
            Board.bitboard_array_global[GenConst.WP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.WB] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];
                break;
            case 14: //WQPr
            Board.bitboard_array_global[GenConst.WP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.WQ] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];
                break;
            case 15: //WRPr
            Board.bitboard_array_global[GenConst.WP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.WR] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];
                break;
            case 16: //BNPrCAP
            Board.bitboard_array_global[GenConst.BP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.BN] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];

            Board.bitboard_array_global[captureIndex] |= MoveConstants.SQUARE_BBS[targetSquareCopy];
                break;
            case 17: //BBPrCAP
            Board.bitboard_array_global[GenConst.BP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.BB] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];

            Board.bitboard_array_global[captureIndex] |= MoveConstants.SQUARE_BBS[targetSquareCopy];

                break;
            case 18: //BQPrCAP
            Board.bitboard_array_global[GenConst.BP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.BQ] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];

            Board.bitboard_array_global[captureIndex] |= MoveConstants.SQUARE_BBS[targetSquareCopy];
                break;
            case 19: //BRPrCAP
            Board.bitboard_array_global[GenConst.BP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.BR] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];

            Board.bitboard_array_global[captureIndex] |= MoveConstants.SQUARE_BBS[targetSquareCopy];
                break;
            case 20: //WNPrCAP
            Board.bitboard_array_global[GenConst.WP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.WN] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];

            Board.bitboard_array_global[captureIndex] |= MoveConstants.SQUARE_BBS[targetSquareCopy];
                break;
            case 21: //WBPrCAP
            Board.bitboard_array_global[GenConst.WP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.WB] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];

            Board.bitboard_array_global[captureIndex] |= MoveConstants.SQUARE_BBS[targetSquareCopy];
                break;
            case 22: //WQPrCAP
            Board.bitboard_array_global[GenConst.WP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.WQ] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];

            Board.bitboard_array_global[captureIndex] |= MoveConstants.SQUARE_BBS[targetSquareCopy];
                break;
            case 23: //WRPrCAP
            Board.bitboard_array_global[GenConst.WP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.WR] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];

            Board.bitboard_array_global[captureIndex] |= MoveConstants.SQUARE_BBS[targetSquareCopy];
                break;

            //#endregion

            case 24: //WDouble
            Board.bitboard_array_global[GenConst.WP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.WP] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];
                break;
            case 25: //BDouble
            Board.bitboard_array_global[GenConst.BP] |= MoveConstants.SQUARE_BBS[startingSquareCopy];
            Board.bitboard_array_global[GenConst.BP] &= ~MoveConstants.SQUARE_BBS[targetSquareCopy];
                break;
        }
    }


    public static void PrintBitboard(long input)
    {
        if (input == 0)
        {
            System.out.print(String.format("   bitboard: %d", input));
            return;
        }
        for (int y = 0; y < 8; y++)
        {
            System.out.print("    ");
            for (int x = 0; x < 8; x++)
            {
                int square = (y * 8) + x;
                if ((input & MoveConstants.SQUARE_BBS[square]) != 0)
                {
                    System.out.print("X ");
                }
                else
                {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }

        System.out.println(String.format("\nbitboard: %d", input));
    }



    public static void PrintAllBitboards()
    {
        for (int i = 0; i < 12; i++)
        {
            PrintBitboard(Board.bitboard_array_global[i]);
        }
    }

    
    static int MakeMove(int startingSquare, int targetSquare, int tag, int piece)
    {
        int captureIndex = -1;

        Board.is_white_global = !Board.is_white_global;
        Board.ep_global = NO_SQUARE;
        CaptureType captureType = CaptureType.None;
        
        switch (tag)
        {
            case GenConst.TAG_NONE: //none
            case GenConst.TAG_CHECK: //check
                MovePiece(startingSquare, targetSquare, piece);
                break;
            case GenConst.TAG_CHECK_CAP: //capture
            case GenConst.TAG_CAPTURE: //check cap
                MovePiece(startingSquare, targetSquare, piece);
                if (Board.is_white_global == true) {
                    captureType = CaptureType.White;
                } 
                else {
                    captureType = CaptureType.Black;
                }
                break;
            case GenConst.TAG_WHITE_EP: //white ep
                MovePiece(startingSquare, targetSquare, GenConst.WP);
                RemovePiece(GenConst.BP, targetSquare + 8);
                break;
            case GenConst.TAG_BLACK_EP:
                MovePiece(startingSquare, targetSquare, GenConst.BP);
                RemovePiece(GenConst.WP, targetSquare - 8);
                break;

            //#region Castling

            case GenConst.TAG_W_CASTLE_KS:
                MovePiece(GenConst.E1, GenConst.G1, GenConst.WK);
                MovePiece(GenConst.H1, GenConst.F1, GenConst.WR);
                Board.castle_rights_global[WKS_CASTLE_RIGHTS] = false;
                Board.castle_rights_global[WQS_CASTLE_RIGHTS] = false;
                break;
            case GenConst.TAG_W_CASTLE_QS: 
                MovePiece(GenConst.E1, GenConst.C1, GenConst.WK);
                MovePiece(GenConst.A1, GenConst.D1, GenConst.WR);
                Board.castle_rights_global[WKS_CASTLE_RIGHTS] = false;
                Board.castle_rights_global[WQS_CASTLE_RIGHTS] = false;
                break;
            case GenConst.TAG_B_CASTLE_KS:
                MovePiece(GenConst.E8, GenConst.G8, GenConst.BK);
                MovePiece(GenConst.H8, GenConst.F8, GenConst.BR);
                Board.castle_rights_global[BKS_CASTLE_RIGHTS] = false;
                Board.castle_rights_global[BQS_CASTLE_RIGHTS] = false;
                break;
            case GenConst.TAG_B_CASTLE_QS: //BQS
                MovePiece(GenConst.E8, GenConst.C8, GenConst.BK);
                MovePiece(GenConst.A8, GenConst.D8, GenConst.BR);
                Board.castle_rights_global[BKS_CASTLE_RIGHTS] = false;
                Board.castle_rights_global[BQS_CASTLE_RIGHTS] = false;
                break;

            //#endregion

            //#region Promotion makemove

            case GenConst.TAG_B_N_PROMOTION: 
                MovePiecePromote(startingSquare, targetSquare, piece, GenConst.BN);
                break;
            case GenConst.TAG_B_B_PROMOTION: 
                MovePiecePromote(startingSquare, targetSquare, piece, GenConst.BB);
                break;
            case GenConst.TAG_B_Q_PROMOTION:
                MovePiecePromote(startingSquare, targetSquare, piece, GenConst.BQ);
                break;
            case GenConst.TAG_B_R_PROMOTION:
                MovePiecePromote(startingSquare, targetSquare, piece, GenConst.BR);
                break;
            case GenConst.TAG_W_N_PROMOTION:
                MovePiecePromote(startingSquare, targetSquare, piece, GenConst.WN);
                break;
            case GenConst.TAG_W_B_PROMOTION:
                MovePiecePromote(startingSquare, targetSquare, piece, GenConst.WB);
                break;
            case GenConst.TAG_W_Q_PROMOTION:
                MovePiecePromote(startingSquare, targetSquare, piece, GenConst.WQ);
                break;
            case GenConst.TAG_W_R_PROMOTION:
                MovePiecePromote(startingSquare, targetSquare, piece, GenConst.WR);
                break;
            case GenConst.TAG_B_N_PROMOTION_CAP:
                MovePiecePromote(startingSquare, targetSquare, piece, GenConst.BN);
                captureType = CaptureType.White;
                break;
            case GenConst.TAG_B_B_PROMOTION_CAP:
                MovePiecePromote(startingSquare, targetSquare, piece, GenConst.BB);
                captureType = CaptureType.White;
                break;
            case GenConst.TAG_B_Q_PROMOTION_CAP:
                MovePiecePromote(startingSquare, targetSquare, piece, GenConst.BQ);
                captureType = CaptureType.White;
                break;
            case GenConst.TAG_B_R_PROMOTION_CAP:
                MovePiecePromote(startingSquare, targetSquare, piece, GenConst.BR);
                captureType = CaptureType.White;
                break;
            case GenConst.TAG_W_N_PROMOTION_CAP: 
                MovePiecePromote(startingSquare, targetSquare, piece, GenConst.WN);
                captureType = CaptureType.Black;
                break;
            case GenConst.TAG_W_B_PROMOTION_CAP: 
                MovePiecePromote(startingSquare, targetSquare, piece, GenConst.WB);
                captureType = CaptureType.Black;
                break;
            case GenConst.TAG_W_Q_PROMOTION_CAP: 
                MovePiecePromote(startingSquare, targetSquare, piece, GenConst.WQ);
                captureType = CaptureType.Black;
                break;
            case GenConst.TAG_W_R_PROMOTION_CAP: 
                MovePiecePromote(startingSquare, targetSquare, piece, GenConst.WR);
                captureType = CaptureType.Black;
                break;
            //#endregion

            case GenConst.TAG_W_P_DOUBLE:
                MovePiece(startingSquare, targetSquare, GenConst.WP);
                Board.ep_global = targetSquare + 8;
                break;
            case GenConst.TAG_B_P_DOUBLE: 
                MovePiece(startingSquare, targetSquare, GenConst.BP);
                Board.ep_global = targetSquare - 8;
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

        if (piece == GenConst.WK)
        {
            Board.castle_rights_global[WKS_CASTLE_RIGHTS] = false;
            Board.castle_rights_global[WQS_CASTLE_RIGHTS] = false;
        }
        else if (piece == GenConst.BK)
        {
            Board.castle_rights_global[BKS_CASTLE_RIGHTS] = false;
            Board.castle_rights_global[BQS_CASTLE_RIGHTS] = false;
        }
        else if (piece == GenConst.WR)
        {
            if (Board.castle_rights_global[WKS_CASTLE_RIGHTS] == true)
            {
                if ((Board.bitboard_array_global[GenConst.WR] & MoveConstants.SQUARE_BBS[GenConst.H1]) == 0)
                {
                    Board.castle_rights_global[WKS_CASTLE_RIGHTS] = false;
                }
            }
            if (Board.castle_rights_global[WQS_CASTLE_RIGHTS] == true)
            {
                if ((Board.bitboard_array_global[GenConst.WR] & MoveConstants.SQUARE_BBS[GenConst.A1]) == 0)
                {
                    Board.castle_rights_global[WQS_CASTLE_RIGHTS] = false;
                }
            }
        }
        else if (piece == GenConst.BR)
        {
            if (Board.castle_rights_global[BKS_CASTLE_RIGHTS] == true)
            {
                if ((Board.bitboard_array_global[GenConst.BR] & MoveConstants.SQUARE_BBS[GenConst.H8]) == 0)
                {
                    Board.castle_rights_global[BKS_CASTLE_RIGHTS] = false;
                }
            }
            if (Board.castle_rights_global[BQS_CASTLE_RIGHTS] == true)
            {
                if ((Board.bitboard_array_global[GenConst.BR] & MoveConstants.SQUARE_BBS[GenConst.A8]) == 0)
                {
                    Board.castle_rights_global[BQS_CASTLE_RIGHTS] = false;
                }
            }
        }

        return captureIndex;
    }

    
    static int getMoves(int[] startingSquares, int[] targetSquares, int[] tags, int[] pieces)
    {
        int moveCount = 0;

        //Move generating variables
        long WHITE_OCCUPANCIES_LOCAL = Board.bitboard_array_global[0] | Board.bitboard_array_global[1] | Board.bitboard_array_global[2] | Board.bitboard_array_global[3] | Board.bitboard_array_global[4] | Board.bitboard_array_global[5];
        long BLACK_OCCUPANCIES_LOCAL = Board.bitboard_array_global[6] | Board.bitboard_array_global[7] | Board.bitboard_array_global[8] | Board.bitboard_array_global[9] | Board.bitboard_array_global[10] | Board.bitboard_array_global[11];
        long COMBINED_OCCUPANCIES_LOCAL = WHITE_OCCUPANCIES_LOCAL | BLACK_OCCUPANCIES_LOCAL;
        long EMPTY_OCCUPANCIES = ~COMBINED_OCCUPANCIES_LOCAL;
        long tempBitboard;
        long checkBitboard = 0L;
        long tempPinBitboard, tempAttack, tempEmpty, tempCaptures;
        int startingSquare = NO_SQUARE, targetSquare = NO_SQUARE;
        int kingPosition;
        int checkCount;
        long enemy_occupancies;
        int bishop_index;
        int rook_index;
        int queen_index;
        int knight_index;

        int[] pinArrayPiece = {
            -1,-1,-1,-1,-1,-1,-1,-1
        };
        int[] pinArraySquare = {
            -1,-1,-1,-1,-1,-1,-1,-1
        };

        int pinNumber = 0;

        if (Board.is_white_global == true)
        {
            checkCount = 0;
            kingPosition = bitScanForward(Board.bitboard_array_global[GenConst.WK]);

            rook_index = GenConst.WR;
            queen_index = GenConst.WQ;
            knight_index = GenConst.WN;
            bishop_index = GenConst.WB;

            enemy_occupancies = BLACK_OCCUPANCIES_LOCAL;

            //#region white pins and check

            //pawns
            tempBitboard = Board.bitboard_array_global[GenConst.BP] & MoveConstants.WHITE_PAWN_ATTACKS[kingPosition];
            if (tempBitboard != 0)
            {
                int pawn_square = (DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)]);
             
                checkBitboard |= MoveConstants.SQUARE_BBS[pawn_square];
                
                checkCount++;
            }

            //knights
            tempBitboard = Board.bitboard_array_global[GenConst.BN] & MoveConstants.KNIGHT_ATTACKS[kingPosition];
            if (tempBitboard != 0)
            {
                int knight_square = DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)];

                checkBitboard |= MoveConstants.SQUARE_BBS[knight_square];

                checkCount++;
            }

            //bishops
            long bishopAttacksChecks = MoveUtils.getBishopMovesSeparate(BLACK_OCCUPANCIES_LOCAL, kingPosition);
            tempBitboard = Board.bitboard_array_global[GenConst.BB] & bishopAttacksChecks;
            while (tempBitboard != 0)
            {
                int piece_square = DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)];

                tempPinBitboard = Inb.INBETWEEN_BITBOARDS[kingPosition][piece_square] & WHITE_OCCUPANCIES_LOCAL;

                if (tempPinBitboard == 0)
                {

                    checkBitboard |= Inb.INBETWEEN_BITBOARDS[kingPosition][piece_square];
                    
                    checkCount++;
                }
                else
                {
                    int pinned_square = (DEBRUIJN64[(int)((MAGIC * (tempPinBitboard ^ (tempPinBitboard - 1))) >>> 58)]);
                    tempPinBitboard &= tempPinBitboard - 1;

                    if (tempPinBitboard == 0)
                    {
                        pinArraySquare[pinNumber] = pinned_square;
                        pinArrayPiece[pinNumber] = piece_square;
                        pinNumber++;
                    }
                }
                tempBitboard &= tempBitboard - 1;
            }

            //queen
            tempBitboard = Board.bitboard_array_global[GenConst.BQ] & bishopAttacksChecks;
            while (tempBitboard != 0)
            {
                int piece_square = DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)];

                tempPinBitboard = Inb.INBETWEEN_BITBOARDS[kingPosition][piece_square] & WHITE_OCCUPANCIES_LOCAL;

                if (tempPinBitboard == 0)
                {

                    checkBitboard |= Inb.INBETWEEN_BITBOARDS[kingPosition][piece_square];
                    
                    checkCount++;
                }
                else
                {
                    int pinned_square = (DEBRUIJN64[(int)((MAGIC * (tempPinBitboard ^ (tempPinBitboard - 1))) >>> 58)]);
                    tempPinBitboard &= tempPinBitboard - 1;

                    if (tempPinBitboard == 0)
                    {
                        pinArraySquare[pinNumber] = pinned_square;
                        pinArrayPiece[pinNumber] = piece_square;
                        pinNumber++;
                    }
                }
                tempBitboard &= tempBitboard - 1;
            }

            //rook
            long rook_attacks = MoveUtils.getRookMovesSeparate(BLACK_OCCUPANCIES_LOCAL, kingPosition);
            tempBitboard = Board.bitboard_array_global[GenConst.BR] & rook_attacks;
            while (tempBitboard != 0)
            {
                int piece_square = DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)];

                tempPinBitboard = Inb.INBETWEEN_BITBOARDS[kingPosition][piece_square] & WHITE_OCCUPANCIES_LOCAL;

                if (tempPinBitboard == 0)
                {

                    checkBitboard |= Inb.INBETWEEN_BITBOARDS[kingPosition][piece_square];
                    
                    checkCount++;
                }
                else
                {
                    int pinned_square = (DEBRUIJN64[(int)((MAGIC * (tempPinBitboard ^ (tempPinBitboard - 1))) >>> 58)]);
                    tempPinBitboard &= tempPinBitboard - 1;

                    if (tempPinBitboard == 0)
                    {
                        pinArraySquare[pinNumber] = pinned_square;
                        pinArrayPiece[pinNumber] = piece_square;
                        pinNumber++;
                    }
                }
                tempBitboard &= tempBitboard - 1;
            }

            //queen
            tempBitboard = Board.bitboard_array_global[GenConst.BQ] & rook_attacks;
            while (tempBitboard != 0)
            {
                int piece_square = (DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)]);

                tempPinBitboard = Inb.INBETWEEN_BITBOARDS[kingPosition][piece_square] & WHITE_OCCUPANCIES_LOCAL;

                if (tempPinBitboard == 0)
                {
                    checkBitboard |= Inb.INBETWEEN_BITBOARDS[kingPosition][piece_square];
                    
                    checkCount++;
                }
                else
                {
                    int pinned_square = (DEBRUIJN64[(int)((MAGIC * (tempPinBitboard ^ (tempPinBitboard - 1))) >>> 58)]);
                    tempPinBitboard &= tempPinBitboard - 1;

                    if (tempPinBitboard == 0)
                    {
                        pinArraySquare[pinNumber] = pinned_square;
                        pinArrayPiece[pinNumber] = piece_square;
                        pinNumber++;
                    }
                }
                tempBitboard &= tempBitboard - 1;
            }

            //#endregion

            //#region White king

            long occupanciesWithoutWhiteKing = COMBINED_OCCUPANCIES_LOCAL & (~Board.bitboard_array_global[GenConst.WK]);
            tempAttack = MoveConstants.KING_ATTACKS[kingPosition];
            tempEmpty = tempAttack & EMPTY_OCCUPANCIES;
            while (tempEmpty != 0)
            {
                targetSquare = bitScanForward(tempEmpty);
                tempEmpty &= tempEmpty - 1;

                if ((Board.bitboard_array_global[GenConst.BP] & MoveConstants.WHITE_PAWN_ATTACKS[targetSquare]) != 0)
                {
                    continue;
                }
                if ((Board.bitboard_array_global[GenConst.BN] & MoveConstants.KNIGHT_ATTACKS[targetSquare]) != 0)
                {
                    continue;
                }
                if ((Board.bitboard_array_global[GenConst.BK] & MoveConstants.KING_ATTACKS[targetSquare]) != 0)
                {
                    continue;
                }
                long bishopAttacks = MoveUtils.getBishopMovesSeparate(occupanciesWithoutWhiteKing, targetSquare);
                if ((Board.bitboard_array_global[GenConst.BB] & bishopAttacks) != 0)
                {
                    continue;
                }
                if ((Board.bitboard_array_global[GenConst.BQ] & bishopAttacks) != 0)
                {
                    continue;
                }
                long rookAttacks = MoveUtils.getRookMovesSeparate(occupanciesWithoutWhiteKing, targetSquare);
                if ((Board.bitboard_array_global[GenConst.BR] & rookAttacks) != 0)
                {
                    continue;
                }
                if ((Board.bitboard_array_global[GenConst.BQ] & rookAttacks) != 0)
                {
                    continue;
                }

                startingSquares[moveCount] = kingPosition;
                targetSquares[moveCount] = targetSquare;
                tags[moveCount] = GenConst.TAG_NONE;
                pieces[moveCount] = GenConst.WK;
                moveCount++;
            }

            //captures
            tempCaptures = tempAttack & BLACK_OCCUPANCIES_LOCAL;
            while (tempCaptures != 0)
            {
                targetSquare = bitScanForward(tempCaptures);
                tempCaptures &= tempCaptures - 1;

                if ((Board.bitboard_array_global[GenConst.BP] & MoveConstants.WHITE_PAWN_ATTACKS[targetSquare]) != 0)
                {
                    continue;
                }
                if ((Board.bitboard_array_global[GenConst.BN] & MoveConstants.KNIGHT_ATTACKS[targetSquare]) != 0)
                {
                    continue;
                }
                if ((Board.bitboard_array_global[GenConst.BK] & MoveConstants.KING_ATTACKS[targetSquare]) != 0)
                {
                    continue;
                }
                long bishopAttacks = MoveUtils.getBishopMovesSeparate(occupanciesWithoutWhiteKing, targetSquare);
                if ((Board.bitboard_array_global[GenConst.BB] & bishopAttacks) != 0)
                {
                    continue;
                }
                if ((Board.bitboard_array_global[GenConst.BQ] & bishopAttacks) != 0)
                {
                    continue;
                }
                long rookAttacks = MoveUtils.getRookMovesSeparate(occupanciesWithoutWhiteKing, targetSquare);
                if ((Board.bitboard_array_global[GenConst.BR] & rookAttacks) != 0)
                {
                    continue;
                }
                if ((Board.bitboard_array_global[GenConst.BQ] & rookAttacks) != 0)
                {
                    continue;
                }

                startingSquares[moveCount] = kingPosition;
                targetSquares[moveCount] = targetSquare;
                tags[moveCount] = GenConst.TAG_CAPTURE;
                pieces[moveCount] = GenConst.WK;
                moveCount++;
            }

            //#endregion

            if (checkCount < 2) {

                if (checkCount == 0)
                {
                    checkBitboard = GenConst.MAX_ULONG;

                    if (Board.castle_rights_global[WKS_CASTLE_RIGHTS] == true)
                    {
                        if (kingPosition == GenConst.E1) //king on e1
                        {
                            if ((GenConst.WKS_EMPTY_BITBOARD & COMBINED_OCCUPANCIES_LOCAL) == 0) //f1 and g1 empty
                            {
                                if ((Board.bitboard_array_global[GenConst.WR] & MoveConstants.SQUARE_BBS[GenConst.H1]) != 0) //rook on h1
                                {
                                    if (Is_Square_Attacked_By_Black_Global(GenConst.F1, COMBINED_OCCUPANCIES_LOCAL) == false)
                                    {
                                        if (Is_Square_Attacked_By_Black_Global(GenConst.G1, COMBINED_OCCUPANCIES_LOCAL) == false)
                                        {
                                            startingSquares[moveCount] = GenConst.E1;
                                            targetSquares[moveCount] = GenConst.G1;
                                            tags[moveCount] = GenConst.TAG_W_CASTLE_KS;
                                            pieces[moveCount] = GenConst.WK;
                                            moveCount++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (Board.castle_rights_global[WQS_CASTLE_RIGHTS] == true)
                    {
                        if (kingPosition == GenConst.E1) //king on e1
                        {
                            if ((GenConst.WQS_EMPTY_BITBOARD & COMBINED_OCCUPANCIES_LOCAL) == 0) //f1 and g1 empty
                            {
                                if ((Board.bitboard_array_global[GenConst.WR] & MoveConstants.SQUARE_BBS[GenConst.A1]) != 0) //rook on h1
                                {
                                    if (Is_Square_Attacked_By_Black_Global(GenConst.C1, COMBINED_OCCUPANCIES_LOCAL) == false)
                                    {
                                        if (Is_Square_Attacked_By_Black_Global(GenConst.D1, COMBINED_OCCUPANCIES_LOCAL) == false)
                                        {
                                            startingSquares[moveCount] = GenConst.E1;
                                            targetSquares[moveCount] = GenConst.C1;
                                            tags[moveCount] = GenConst.TAG_W_CASTLE_QS;
                                            pieces[moveCount] = GenConst.WK;
                                            moveCount++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                }

                //#region White pawn

                tempBitboard = Board.bitboard_array_global[GenConst.WP];

                while (tempBitboard != 0)
                {
                    startingSquare = DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)];
                    tempBitboard &= tempBitboard - 1;

                    tempPinBitboard = GenConst.MAX_ULONG;
                    if (pinNumber != 0)
                    {
                        for (int i = 0; i < pinNumber; i++)
                        {
                            if (pinArraySquare[i] == startingSquare)
                            {
                                tempPinBitboard = Inb.INBETWEEN_BITBOARDS[kingPosition][pinArrayPiece[i]];
                            }
                        }
                    }

                    //#region Pawn forward

                    if ((MoveConstants.SQUARE_BBS[startingSquare - 8] & COMBINED_OCCUPANCIES_LOCAL) == 0) //if up one square is empty
                    {
                        if (((MoveConstants.SQUARE_BBS[startingSquare - 8] & checkBitboard) & tempPinBitboard) != 0)
                        {
                            if ((MoveConstants.SQUARE_BBS[startingSquare] & GenConst.RANK_7_BITBOARD) != 0) //if promotion
                            {

                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = startingSquare - 8;
                                tags[moveCount] = GenConst.TAG_W_Q_PROMOTION;
                                pieces[moveCount] = GenConst.WP;
                                moveCount++;

                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = startingSquare - 8;
                                tags[moveCount] = GenConst.TAG_W_R_PROMOTION;
                                pieces[moveCount] = GenConst.WP;
                                moveCount++;

                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = startingSquare - 8;
                                tags[moveCount] = GenConst.TAG_W_B_PROMOTION;
                                pieces[moveCount] = GenConst.WP;
                                moveCount++;

                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = startingSquare - 8;
                                tags[moveCount] = GenConst.TAG_W_N_PROMOTION;
                                pieces[moveCount] = GenConst.WP;
                                moveCount++;

                            }
                            else
                            {
                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = startingSquare - 8;
                                tags[moveCount] = GenConst.TAG_NONE;
                                pieces[moveCount] = GenConst.WP;
                                moveCount++;
                            }
                        }

                        if ((MoveConstants.SQUARE_BBS[startingSquare] & GenConst.RANK_2_BITBOARD) != 0) //if on rank 2
                        {
                            if (((MoveConstants.SQUARE_BBS[startingSquare - 16] & checkBitboard) & tempPinBitboard) != 0) //if not pinned or 
                            {
                                if (((MoveConstants.SQUARE_BBS[startingSquare - 16]) & COMBINED_OCCUPANCIES_LOCAL) == 0) //if up two squares and one square are empty
                                {
                                    startingSquares[moveCount] = startingSquare;
                                    targetSquares[moveCount] = startingSquare - 16;
                                    tags[moveCount] = GenConst.TAG_W_P_DOUBLE;
                                    pieces[moveCount] = GenConst.WP;
                                    moveCount++;
                                }
                            }
                        }
                    }

                    //#endregion

                    //#region Pawn captures

                    tempAttack = ((MoveConstants.WHITE_PAWN_ATTACKS[startingSquare] & BLACK_OCCUPANCIES_LOCAL) & checkBitboard) & tempPinBitboard; //if black piece diagonal to pawn

                    while (tempAttack != 0)
                    {
                        targetSquare = (DEBRUIJN64[(int)((MAGIC * (tempAttack ^ (tempAttack - 1))) >>> 58)]);
                        tempAttack &= tempAttack - 1;

                        if ((MoveConstants.SQUARE_BBS[startingSquare] & GenConst.RANK_7_BITBOARD) != 0) //if promotion
                        {
                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = GenConst.TAG_W_Q_PROMOTION_CAP;
                            pieces[moveCount] = GenConst.WP;
                            moveCount++;

                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = GenConst.TAG_W_R_PROMOTION_CAP;
                            pieces[moveCount] = GenConst.WP;
                            moveCount++;

                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = GenConst.TAG_W_B_PROMOTION_CAP;
                            pieces[moveCount] = GenConst.WP;
                            moveCount++;

                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = GenConst.TAG_W_N_PROMOTION_CAP;
                            pieces[moveCount] = GenConst.WP;
                            moveCount++;

                        }
                        else
                        {
                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = GenConst.TAG_CAPTURE;
                            pieces[moveCount] = GenConst.WP;
                            moveCount++;
                        }
                    }

                    if ((MoveConstants.SQUARE_BBS[startingSquare] & GenConst.RANK_5_BITBOARD) != 0) //check rank for ep
                    {
                        if (Board.ep_global != NO_SQUARE)
                        {
                            if ((((MoveConstants.WHITE_PAWN_ATTACKS[startingSquare] & MoveConstants.SQUARE_BBS[Board.ep_global]) & checkBitboard) & tempPinBitboard) != 0)
                            {
                                if ((Board.bitboard_array_global[GenConst.WK] & GenConst.RANK_5_BITBOARD) == 0) //if no king on rank 5
                                {
                                    startingSquares[moveCount] = startingSquare;
                                    targetSquares[moveCount] = Board.ep_global;
                                    tags[moveCount] = GenConst.TAG_WHITE_EP;
                                    pieces[moveCount] = GenConst.WP;
                                    moveCount++;
                                }
                                else if ((Board.bitboard_array_global[GenConst.BR] & GenConst.RANK_5_BITBOARD) == 0 && (Board.bitboard_array_global[GenConst.BQ] & GenConst.RANK_5_BITBOARD) == 0) // if no b rook or queen on rank 5
                                {
                                    startingSquares[moveCount] = startingSquare;
                                    targetSquares[moveCount] = Board.ep_global;
                                    tags[moveCount] = GenConst.TAG_WHITE_EP;
                                    pieces[moveCount] = GenConst.WP;
                                    moveCount++;
                                }
                                else //wk and br or bq on rank 5
                                {
                                    long occupancyWithoutEPPawns = COMBINED_OCCUPANCIES_LOCAL & ~MoveConstants.SQUARE_BBS[startingSquare];
                                    occupancyWithoutEPPawns &= ~MoveConstants.SQUARE_BBS[Board.ep_global + 8];

                                    long rookAttacksFromKing = MoveUtils.getRookMovesSeparate(occupancyWithoutEPPawns, kingPosition);

                                    if ((rookAttacksFromKing & Board.bitboard_array_global[GenConst.BR]) == 0)
                                    {
                                        if ((rookAttacksFromKing & Board.bitboard_array_global[GenConst.BQ]) == 0)
                                        {
                                            startingSquares[moveCount] = startingSquare;
                                            targetSquares[moveCount] = Board.ep_global;
                                            tags[moveCount] = GenConst.TAG_WHITE_EP;
                                            pieces[moveCount] = GenConst.WP;
                                            moveCount++;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //#endregion
                }

                //#endregion
            }
            
        }
        else //black move
        {
            checkCount = 0;
            kingPosition = bitScanForward(Board.bitboard_array_global[GenConst.BK]);

            rook_index = GenConst.BR;
            queen_index = GenConst.BQ;
            knight_index = GenConst.BN;
            bishop_index = GenConst.BB;

            enemy_occupancies = WHITE_OCCUPANCIES_LOCAL;

            //#region pins and check

            //pawns
            tempBitboard = Board.bitboard_array_global[GenConst.WP] & MoveConstants.BLACK_PAWN_ATTACKS[kingPosition];
            if (tempBitboard != 0)
            {
                int pawn_square = DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)];

                checkBitboard |= MoveConstants.SQUARE_BBS[pawn_square];
                
                checkCount++;
            }

            //knights
            tempBitboard = Board.bitboard_array_global[GenConst.WN] & MoveConstants.KNIGHT_ATTACKS[kingPosition];
            if (tempBitboard != 0)
            {
                int knight_square = DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)];

                checkBitboard |= MoveConstants.SQUARE_BBS[knight_square];
                
                checkCount++;
            }

            //bishops
            long bishopAttacksChecks = MoveUtils.getBishopMovesSeparate(WHITE_OCCUPANCIES_LOCAL, kingPosition);
            tempBitboard = Board.bitboard_array_global[GenConst.WB] & bishopAttacksChecks;
            while (tempBitboard != 0)
            {
                int piece_square = DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)];

                tempPinBitboard = Inb.INBETWEEN_BITBOARDS[kingPosition][piece_square] & BLACK_OCCUPANCIES_LOCAL;

                if (tempPinBitboard == 0)
                {

                    checkBitboard |= Inb.INBETWEEN_BITBOARDS[kingPosition][piece_square];
                    
                    checkCount++;
                }
                else
                {
                    int pinned_square = (DEBRUIJN64[(int)((MAGIC * (tempPinBitboard ^ (tempPinBitboard - 1))) >>> 58)]);
                    tempPinBitboard &= tempPinBitboard - 1;

                    if (tempPinBitboard == 0)
                    {
                        pinArraySquare[pinNumber] = pinned_square;
                        pinArrayPiece[pinNumber] = piece_square;
                        pinNumber++;
                    }
                }
                tempBitboard &= tempBitboard - 1;
            }

            //queen
            tempBitboard = Board.bitboard_array_global[GenConst.WQ] & bishopAttacksChecks;
            while (tempBitboard != 0)
            {
                int piece_square = DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)];

                tempPinBitboard = Inb.INBETWEEN_BITBOARDS[kingPosition][piece_square] & BLACK_OCCUPANCIES_LOCAL;

                if (tempPinBitboard == 0)
                {
                    checkBitboard |= Inb.INBETWEEN_BITBOARDS[kingPosition][piece_square];
                    
                    checkCount++;
                }
                else
                {
                    int pinned_square = (DEBRUIJN64[(int)((MAGIC * (tempPinBitboard ^ (tempPinBitboard - 1))) >>> 58)]);
                    tempPinBitboard &= tempPinBitboard - 1;

                    if (tempPinBitboard == 0)
                    {
                        pinArraySquare[pinNumber] = pinned_square;
                        pinArrayPiece[pinNumber] = piece_square;
                        pinNumber++;
                    }
                }
                tempBitboard &= tempBitboard - 1;
            }

            //rook
            long rook_attacks = MoveUtils.getRookMovesSeparate(WHITE_OCCUPANCIES_LOCAL, kingPosition);
            tempBitboard = Board.bitboard_array_global[GenConst.WR] & rook_attacks;
            while (tempBitboard != 0)
            {
                int piece_square = DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)];

                tempPinBitboard = Inb.INBETWEEN_BITBOARDS[kingPosition][piece_square] & BLACK_OCCUPANCIES_LOCAL;

                if (tempPinBitboard == 0)
                {
                    checkBitboard |= Inb.INBETWEEN_BITBOARDS[kingPosition][piece_square];
                    
                    checkCount++;
                }
                else
                {
                    int pinned_square = (DEBRUIJN64[(int)((MAGIC * (tempPinBitboard ^ (tempPinBitboard - 1))) >>> 58)]);
                    tempPinBitboard &= tempPinBitboard - 1;

                    if (tempPinBitboard == 0)
                    {
                        pinArraySquare[pinNumber] = pinned_square;
                        pinArrayPiece[pinNumber] = piece_square;
                        pinNumber++;
                    }
                }
                tempBitboard &= tempBitboard - 1;
            }

            //queen
            tempBitboard = Board.bitboard_array_global[GenConst.WQ] & rook_attacks;
            while (tempBitboard != 0)
            {
                int piece_square = DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)];

                tempPinBitboard = Inb.INBETWEEN_BITBOARDS[kingPosition][piece_square] & BLACK_OCCUPANCIES_LOCAL;

                if (tempPinBitboard == 0)
                {
                    checkBitboard |= Inb.INBETWEEN_BITBOARDS[kingPosition][piece_square];
                    
                    checkCount++;
                }
                else
                {
                    int pinned_square = (DEBRUIJN64[(int)((MAGIC * (tempPinBitboard ^ (tempPinBitboard - 1))) >>> 58)]);
                    tempPinBitboard &= tempPinBitboard - 1;

                    if (tempPinBitboard == 0)
                    {
                        pinArraySquare[pinNumber] = pinned_square;
                        pinArrayPiece[pinNumber] = piece_square;
                        pinNumber++;
                    }
                }
                tempBitboard &= tempBitboard - 1;
            }

            //#endregion

            //#region Black king
            long occupancyWithoutBlackKing = COMBINED_OCCUPANCIES_LOCAL & (~Board.bitboard_array_global[GenConst.BK]);

            tempAttack = MoveConstants.KING_ATTACKS[kingPosition] & WHITE_OCCUPANCIES_LOCAL;

            while (tempAttack != 0)
            {
                targetSquare = DEBRUIJN64[(int)((MAGIC * (tempAttack ^ (tempAttack - 1))) >>> 58)];
                tempAttack &= tempAttack - 1;

                if ((Board.bitboard_array_global[GenConst.WP] & MoveConstants.BLACK_PAWN_ATTACKS[targetSquare]) != 0)
                {
                    continue;
                }
                if ((Board.bitboard_array_global[GenConst.WN] & MoveConstants.KNIGHT_ATTACKS[targetSquare]) != 0)
                {
                    continue;
                }
                if ((Board.bitboard_array_global[GenConst.WK] & MoveConstants.KING_ATTACKS[targetSquare]) != 0)
                {
                    continue;
                }
                long bishopAttacks = MoveUtils.getBishopMovesSeparate(occupancyWithoutBlackKing, targetSquare);
                if ((Board.bitboard_array_global[GenConst.WB] & bishopAttacks) != 0)
                {
                    continue;
                }
                if ((Board.bitboard_array_global[GenConst.WQ] & bishopAttacks) != 0)
                {
                    continue;
                }
                long rookAttacks = MoveUtils.getRookMovesSeparate(occupancyWithoutBlackKing, targetSquare);
                if ((Board.bitboard_array_global[GenConst.WR] & rookAttacks) != 0)
                {
                    continue;
                }
                if ((Board.bitboard_array_global[GenConst.WQ] & rookAttacks) != 0)
                {
                    continue;
                }

                startingSquares[moveCount] = kingPosition;
                targetSquares[moveCount] = targetSquare;
                tags[moveCount] = GenConst.TAG_CAPTURE;
                pieces[moveCount] = GenConst.BK;
                moveCount++;

            }

            tempAttack = MoveConstants.KING_ATTACKS[kingPosition] & EMPTY_OCCUPANCIES;

            while (tempAttack != 0)
            {
                targetSquare = DEBRUIJN64[(int)((MAGIC * (tempAttack ^ (tempAttack - 1))) >>> 58)];
                tempAttack &= tempAttack - 1;

                if ((Board.bitboard_array_global[GenConst.WP] & MoveConstants.BLACK_PAWN_ATTACKS[targetSquare]) != 0)
                {
                    continue;
                }
                if ((Board.bitboard_array_global[GenConst.WN] & MoveConstants.KNIGHT_ATTACKS[targetSquare]) != 0)
                {
                    continue;
                }
                if ((Board.bitboard_array_global[GenConst.WK] & MoveConstants.KING_ATTACKS[targetSquare]) != 0)
                {
                    continue;
                }
                long bishopAttacks = MoveUtils.getBishopMovesSeparate(occupancyWithoutBlackKing, targetSquare);
                if ((Board.bitboard_array_global[GenConst.WB] & bishopAttacks) != 0)
                {
                    continue;
                }
                if ((Board.bitboard_array_global[GenConst.WQ] & bishopAttacks) != 0)
                {
                    continue;
                }
                long rookAttacks = MoveUtils.getRookMovesSeparate(occupancyWithoutBlackKing, targetSquare);
                if ((Board.bitboard_array_global[GenConst.WR] & rookAttacks) != 0)
                {
                    continue;
                }
                if ((Board.bitboard_array_global[GenConst.WQ] & rookAttacks) != 0)
                {
                    continue;
                }

                startingSquares[moveCount] = kingPosition;
                targetSquares[moveCount] = targetSquare;
                tags[moveCount] = GenConst.TAG_NONE;
                pieces[moveCount] = GenConst.BK;
                moveCount++;

            }
            //#endregion

            if (checkCount < 2) {

                if (checkCount == 0)
                {
                    checkBitboard = GenConst.MAX_ULONG;

                    if (Board.castle_rights_global[BKS_CASTLE_RIGHTS] == true)
                    {
                        if (kingPosition == GenConst.E8) //king on e1
                        {
                            if ((GenConst.BKS_EMPTY_BITBOARD & COMBINED_OCCUPANCIES_LOCAL) == 0) //f1 and g1 empty
                            {
                                if ((Board.bitboard_array_global[GenConst.BR] & MoveConstants.SQUARE_BBS[GenConst.H8]) != 0) //rook on h1
                                {
                                    if (Is_Square_Attacked_By_White_Global(GenConst.F8, COMBINED_OCCUPANCIES_LOCAL) == false)
                                    {
                                        if (Is_Square_Attacked_By_White_Global(GenConst.G8, COMBINED_OCCUPANCIES_LOCAL) == false)
                                        {
                                            startingSquares[moveCount] = GenConst.E8;
                                            targetSquares[moveCount] = GenConst.G8;
                                            tags[moveCount] = GenConst.TAG_B_CASTLE_KS;
                                            pieces[moveCount] = GenConst.BK;
                                            moveCount++;

                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (Board.castle_rights_global[BQS_CASTLE_RIGHTS] == true)
                    {
                        if (kingPosition == GenConst.E8) //king on e1
                        {
                            if ((GenConst.BQS_EMPTY_BITBOARD & COMBINED_OCCUPANCIES_LOCAL) == 0) //f1 and g1 empty
                            {
                                if ((Board.bitboard_array_global[GenConst.BR] & MoveConstants.SQUARE_BBS[GenConst.A8]) != 0) //rook on h1
                                {
                                    if (Is_Square_Attacked_By_White_Global(GenConst.C8, COMBINED_OCCUPANCIES_LOCAL) == false)
                                    {
                                        if (Is_Square_Attacked_By_White_Global(GenConst.D8, COMBINED_OCCUPANCIES_LOCAL) == false)
                                        {
                                            startingSquares[moveCount] = GenConst.E8;
                                            targetSquares[moveCount] = GenConst.C8;
                                            tags[moveCount] = GenConst.TAG_B_CASTLE_QS;
                                            pieces[moveCount] = GenConst.BK;
                                            moveCount++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                }

                //#region Black pawns

                tempBitboard = Board.bitboard_array_global[GenConst.BP];

                while (tempBitboard != 0)
                {
                    startingSquare = DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)];
                    tempBitboard &= tempBitboard - 1;

                    tempPinBitboard = GenConst.MAX_ULONG;
                    if (pinNumber != 0)
                    {
                        for (int i = 0; i < pinNumber; i++)
                        {
                            if (pinArraySquare[i] == startingSquare)
                            {
                                tempPinBitboard = Inb.INBETWEEN_BITBOARDS[kingPosition][pinArrayPiece[i]];
                            }
                        }
                    }

                    //#region Pawn forward

                    if ((MoveConstants.SQUARE_BBS[startingSquare + 8] & COMBINED_OCCUPANCIES_LOCAL) == 0) //if up one square is empty
                    {
                        if (((MoveConstants.SQUARE_BBS[startingSquare + 8] & checkBitboard) & tempPinBitboard) != 0)
                        {
                            if ((MoveConstants.SQUARE_BBS[startingSquare] & GenConst.RANK_2_BITBOARD) != 0) //if promotion
                            {
                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = startingSquare + 8;
                                tags[moveCount] = GenConst.TAG_B_B_PROMOTION;
                                pieces[moveCount] = GenConst.BP;
                                moveCount++;

                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = startingSquare + 8;
                                tags[moveCount] = GenConst.TAG_B_N_PROMOTION;
                                pieces[moveCount] = GenConst.BP;
                                moveCount++;

                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = startingSquare + 8;
                                tags[moveCount] = GenConst.TAG_B_R_PROMOTION;
                                pieces[moveCount] = GenConst.BP;
                                moveCount++;

                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = startingSquare + 8;
                                tags[moveCount] = GenConst.TAG_B_Q_PROMOTION;
                                pieces[moveCount] = GenConst.BP;
                                moveCount++;
                            }
                            else
                            {
                                startingSquares[moveCount] = startingSquare;
                                targetSquares[moveCount] = startingSquare + 8;
                                tags[moveCount] = GenConst.TAG_NONE;
                                pieces[moveCount] = GenConst.BP;
                                moveCount++;
                            }
                        }

                        if ((MoveConstants.SQUARE_BBS[startingSquare] & GenConst.RANK_7_BITBOARD) != 0) //if on rank 2
                        {
                            if (((MoveConstants.SQUARE_BBS[startingSquare + 16] & checkBitboard) & tempPinBitboard) != 0)
                            {
                                if (((MoveConstants.SQUARE_BBS[startingSquare + 16]) & COMBINED_OCCUPANCIES_LOCAL) == 0) //if up two squares and one square are empty
                                {
                                    startingSquares[moveCount] = startingSquare;
                                    targetSquares[moveCount] = startingSquare + 16;
                                    tags[moveCount] = GenConst.TAG_B_P_DOUBLE;
                                    pieces[moveCount] = GenConst.BP;
                                    moveCount++;
                                }
                            }
                        }
                    }

                    //#endregion

                    //#region region Pawn captures

                    tempAttack = ((MoveConstants.BLACK_PAWN_ATTACKS[startingSquare] & WHITE_OCCUPANCIES_LOCAL) & checkBitboard) & tempPinBitboard; //if black piece diagonal to pawn

                    while (tempAttack != 0)
                    {
                        targetSquare = DEBRUIJN64[(int)((MAGIC * (tempAttack ^ (tempAttack - 1))) >>> 58)]; //find the bit
                        tempAttack &= tempAttack - 1;

                        if ((MoveConstants.SQUARE_BBS[startingSquare] & GenConst.RANK_2_BITBOARD) != 0) //if promotion
                        {
                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = GenConst.TAG_B_B_PROMOTION_CAP;
                            pieces[moveCount] = GenConst.BP;
                            moveCount++;

                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = GenConst.TAG_B_N_PROMOTION_CAP;
                            pieces[moveCount] = GenConst.BP;
                            moveCount++;

                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = GenConst.TAG_B_R_PROMOTION_CAP;
                            pieces[moveCount] = GenConst.BP;
                            moveCount++;

                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = GenConst.TAG_B_Q_PROMOTION_CAP;
                            pieces[moveCount] = GenConst.BP;
                            moveCount++;

                        }
                        else
                        {
                            startingSquares[moveCount] = startingSquare;
                            targetSquares[moveCount] = targetSquare;
                            tags[moveCount] = GenConst.TAG_CAPTURE;
                            pieces[moveCount] = GenConst.BP;
                            moveCount++;
                        }
                    }

                    if ((MoveConstants.SQUARE_BBS[startingSquare] & GenConst.RANK_4_BITBOARD) != 0) //check rank for ep
                    {
                        if (Board.ep_global != NO_SQUARE)
                        {
                            if ((((MoveConstants.BLACK_PAWN_ATTACKS[startingSquare] & MoveConstants.SQUARE_BBS[Board.ep_global]) & checkBitboard) & tempPinBitboard) != 0)
                            {
                                if ((Board.bitboard_array_global[GenConst.BK] & GenConst.RANK_4_BITBOARD) == 0) //if no king on rank 5
                                {
                                    startingSquares[moveCount] = startingSquare;
                                    targetSquares[moveCount] = Board.ep_global;
                                    tags[moveCount] = GenConst.TAG_BLACK_EP;
                                    pieces[moveCount] = GenConst.BP;
                                    moveCount++;
                                }
                                else if ((Board.bitboard_array_global[GenConst.WR] & GenConst.RANK_4_BITBOARD) == 0 && (Board.bitboard_array_global[GenConst.WQ] & GenConst.RANK_4_BITBOARD) == 0) // if no b rook or queen on rank 5
                                {
                                    startingSquares[moveCount] = startingSquare;
                                    targetSquares[moveCount] = Board.ep_global;
                                    tags[moveCount] = GenConst.TAG_BLACK_EP;
                                    pieces[moveCount] = GenConst.BP;
                                    moveCount++;
                                }
                                else //wk and br or bq on rank 5
                                {
                                    long occupancyWithoutEPPawns = COMBINED_OCCUPANCIES_LOCAL & ~MoveConstants.SQUARE_BBS[startingSquare];
                                    occupancyWithoutEPPawns &= ~MoveConstants.SQUARE_BBS[Board.ep_global - 8];

                                    long rookAttacksFromKing = MoveUtils.getRookMovesSeparate(occupancyWithoutEPPawns, kingPosition);

                                    if ((rookAttacksFromKing & Board.bitboard_array_global[GenConst.WR]) == 0)
                                    {
                                        if ((rookAttacksFromKing & Board.bitboard_array_global[GenConst.WQ]) == 0)
                                        {
                                            startingSquares[moveCount] = startingSquare;
                                            targetSquares[moveCount] = Board.ep_global;
                                            tags[moveCount] = GenConst.TAG_BLACK_EP;
                                            pieces[moveCount] = GenConst.BP;
                                            moveCount++;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //#endregion
                }
                //#endregion

            }
            
        }

        if (checkCount > 1) {
            return moveCount;
        }

        //#region knight

        tempBitboard = Board.bitboard_array_global[knight_index];

        while (tempBitboard != 0)
        {
            startingSquare = DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)];
            tempBitboard &= tempBitboard - 1; //removes the knight from that square to not infinitely loop

            tempPinBitboard = GenConst.MAX_ULONG;
            if (pinNumber != 0)
            {
                for (int i = 0; i < pinNumber; i++)
                {
                    if (pinArraySquare[i] == startingSquare)
                    {
                        tempPinBitboard = Inb.INBETWEEN_BITBOARDS[kingPosition][pinArrayPiece[i]];
                    }
                }
            }

            tempAttack = ((MoveConstants.KNIGHT_ATTACKS[startingSquare] & enemy_occupancies) & checkBitboard) & tempPinBitboard; //gets knight captures
            while (tempAttack != 0)
            {
                targetSquare = (DEBRUIJN64[(int)((MAGIC * (tempAttack ^ (tempAttack - 1))) >>> 58)]);
                tempAttack &= tempAttack - 1;


                startingSquares[moveCount] = startingSquare;
                targetSquares[moveCount] = targetSquare;
                tags[moveCount] = GenConst.TAG_CAPTURE;
                pieces[moveCount] = knight_index;
                moveCount++;

            }

            tempAttack = ((MoveConstants.KNIGHT_ATTACKS[startingSquare] & EMPTY_OCCUPANCIES) & checkBitboard) & tempPinBitboard;

            while (tempAttack != 0)
            {
                targetSquare = DEBRUIJN64[(int)((MAGIC * (tempAttack ^ (tempAttack - 1))) >>> 58)];
                tempAttack &= tempAttack - 1;

                startingSquares[moveCount] = startingSquare;
                targetSquares[moveCount] = targetSquare;
                tags[moveCount] = GenConst.TAG_NONE;
                pieces[moveCount] = knight_index;
                moveCount++;

            }
        }
        //#endregion

        //#region Rook

        tempBitboard = Board.bitboard_array_global[rook_index];
        while (tempBitboard != 0)
        {
            startingSquare = DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)];
            tempBitboard &= tempBitboard - 1;

            tempPinBitboard = GenConst.MAX_ULONG;
            if (pinNumber != 0)
            {
                for (int i = 0; i < pinNumber; i++)
                {
                    if (pinArraySquare[i] == startingSquare)
                    {
                        tempPinBitboard = Inb.INBETWEEN_BITBOARDS[kingPosition][pinArrayPiece[i]];
                    }
                }
            }

            long rookAttacks = MoveUtils.getRookMovesSeparate(COMBINED_OCCUPANCIES_LOCAL, startingSquare);

            tempAttack = ((rookAttacks & enemy_occupancies) & checkBitboard) & tempPinBitboard;
            while (tempAttack != 0)
            {
                targetSquare = (DEBRUIJN64[(int)((MAGIC * (tempAttack ^ (tempAttack - 1))) >>> 58)]);
                tempAttack &= tempAttack - 1;

                startingSquares[moveCount] = startingSquare;
                targetSquares[moveCount] = targetSquare;
                tags[moveCount] = GenConst.TAG_CAPTURE;
                pieces[moveCount] = rook_index;
                moveCount++;
            }

            tempAttack = ((rookAttacks & EMPTY_OCCUPANCIES) & checkBitboard) & tempPinBitboard;
            while (tempAttack != 0)
            {
                targetSquare = (DEBRUIJN64[(int)((MAGIC * (tempAttack ^ (tempAttack - 1))) >>> 58)]);
                tempAttack &= tempAttack - 1;

                startingSquares[moveCount] = startingSquare;
                targetSquares[moveCount] = targetSquare;
                tags[moveCount] = GenConst.TAG_NONE;
                pieces[moveCount] = rook_index;
                moveCount++;

            }
        }
        //#endregion

        //#region bishop

        tempBitboard = Board.bitboard_array_global[bishop_index];
        while (tempBitboard != 0)
        {
            startingSquare = DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)];
            tempBitboard &= tempBitboard - 1;

            tempPinBitboard = GenConst.MAX_ULONG;
            if (pinNumber != 0)
            {
                for (int i = 0; i < pinNumber; i++)
                {
                    if (pinArraySquare[i] == startingSquare)
                    {
                        tempPinBitboard = Inb.INBETWEEN_BITBOARDS[kingPosition][pinArrayPiece[i]];
                    }
                }
            }

            long bishopAttacks = MoveUtils.getBishopMovesSeparate(COMBINED_OCCUPANCIES_LOCAL, startingSquare);

            tempAttack = ((bishopAttacks & enemy_occupancies) & checkBitboard) & tempPinBitboard;
            while (tempAttack != 0)
            {
                targetSquare = DEBRUIJN64[(int)((MAGIC * (tempAttack ^ (tempAttack - 1))) >>> 58)];
                tempAttack &= tempAttack - 1;

                startingSquares[moveCount] = startingSquare;
                targetSquares[moveCount] = targetSquare;
                tags[moveCount] = GenConst.TAG_CAPTURE;
                pieces[moveCount] = bishop_index;
                moveCount++;

            }

            tempAttack = ((bishopAttacks & EMPTY_OCCUPANCIES) & checkBitboard) & tempPinBitboard;
            while (tempAttack != 0)
            {
                targetSquare = DEBRUIJN64[(int)((MAGIC * (tempAttack ^ (tempAttack - 1))) >>> 58)];
                tempAttack &= tempAttack - 1;


                startingSquares[moveCount] = startingSquare;
                targetSquares[moveCount] = targetSquare;
                tags[moveCount] = GenConst.TAG_NONE;
                pieces[moveCount] = bishop_index;
                moveCount++;

            }
        }
        //#endregion

        //#region Queen

        tempBitboard = Board.bitboard_array_global[queen_index];
        while (tempBitboard != 0)
        {
            startingSquare = DEBRUIJN64[(int)((MAGIC * (tempBitboard ^ (tempBitboard - 1))) >>> 58)];
            tempBitboard &= tempBitboard - 1;

            tempPinBitboard = GenConst.MAX_ULONG;
            if (pinNumber != 0)
            {
                for (int i = 0; i < pinNumber; i++)
                {
                    if (pinArraySquare[i] == startingSquare)
                    {
                        tempPinBitboard = Inb.INBETWEEN_BITBOARDS[kingPosition][pinArrayPiece[i]];
                    }
                }
            }

            long queenAttacks = MoveUtils.getRookMovesSeparate(COMBINED_OCCUPANCIES_LOCAL, startingSquare);
            queenAttacks |= MoveUtils.getBishopMovesSeparate(COMBINED_OCCUPANCIES_LOCAL, startingSquare);

            tempAttack = ((queenAttacks & enemy_occupancies) & checkBitboard) & tempPinBitboard;

            while (tempAttack != 0)
            {
                targetSquare = DEBRUIJN64[(int)((MAGIC * (tempAttack ^ (tempAttack - 1))) >>> 58)];
                tempAttack &= tempAttack - 1;

                startingSquares[moveCount] = startingSquare;
                targetSquares[moveCount] = targetSquare;
                tags[moveCount] = GenConst.TAG_CAPTURE;
                pieces[moveCount] = queen_index;
                moveCount++;

            }

            tempAttack = ((queenAttacks & EMPTY_OCCUPANCIES) & checkBitboard) & tempPinBitboard;
            while (tempAttack != 0)
            {
                targetSquare = DEBRUIJN64[(int)((MAGIC * (tempAttack ^ (tempAttack - 1))) >>> 58)];
                tempAttack &= tempAttack - 1;


                startingSquares[moveCount] = startingSquare;
                targetSquares[moveCount] = targetSquare;
                tags[moveCount] = GenConst.TAG_NONE;
                pieces[moveCount] = queen_index;
                moveCount++;

            }
        }
        //#endregion

        return moveCount;

    }

    
    static ErrorInt PerftFunctionsDebug(int depth, int ply, DebugInfo debugInfo)
    {
        if (depth < 0) {
            return errorIntFromError(Error.Depth_Less_Than_Zero);
        }
        if (Board.bitboard_array_global[GenConst.WK] == 0) {
            return errorIntFromError(Error.White_King_Captured);
        }
        if (Board.bitboard_array_global[GenConst.BK] == 0) {
            return errorIntFromError(Error.Black_King_Captured);
        }
        long COMBINED_OCCUPANCIES = CombineBitboardsGlobal();
        if (Board.is_white_global == true) {
            int kingPosition = bitScanForward(Board.bitboard_array_global[GenConst.BK]);
            if (Is_Square_Attacked_By_White_Global(kingPosition, COMBINED_OCCUPANCIES) == true) {
                PrintAllBitboards();
                PrintAllDebug(debugInfo.LastStarting, debugInfo.LastTarget, debugInfo.lastPiece, debugInfo.LastTag);
                return errorIntFromError(Error.Black_King_In_Check_On_White_Move);
            }
        } else {
            int kingPosition = bitScanForward(Board.bitboard_array_global[GenConst.WK]);
            if (Is_Square_Attacked_By_Black_Global(kingPosition, COMBINED_OCCUPANCIES) == true) {

                return errorIntFromError(Error.White_King_In_Check_On_BlackMove);
            }
        }
        debugInfo.CallCount += 1;
        if (depth == 0) {
            return errorIntFromValue(1);
        }

        //Same as move list, span to avoid heap allocation
        int[] startingSquares = new int[50];
        int[] targetSquares = new int[50];
        int[] tags = new int[50];
        int[] pieces = new int[50];

        int moveCount = getMoves(startingSquares, targetSquares, tags, pieces);

        ErrorInt nodes = errorIntFromValue(0);
        
        int copyEp = Board.ep_global;
        boolean[] copy_castle = {
            Board.castle_rights_global[0],
            Board.castle_rights_global[1],
            Board.castle_rights_global[2],
            Board.castle_rights_global[3],
        };

        long[] bitboardCopy = 
        {
            Board.bitboard_array_global[0],
            Board.bitboard_array_global[1],
            Board.bitboard_array_global[2],
            Board.bitboard_array_global[3],
            Board.bitboard_array_global[4],
            Board.bitboard_array_global[5],
            Board.bitboard_array_global[6],
            Board.bitboard_array_global[7],
            Board.bitboard_array_global[8],
            Board.bitboard_array_global[9],
            Board.bitboard_array_global[10],
            Board.bitboard_array_global[11],
        };

        for (int move_index = 0; move_index < moveCount; ++move_index)
        {
            int startingSquareCopy = startingSquares[move_index];
            int targetSquareCopy = targetSquares[move_index];
            int piece = pieces[move_index];
            int tag = tags[move_index];

            if (startingSquareCopy < 0 || startingSquareCopy > 63) {
                return errorIntFromError(Error.Invalid_Starting_Square);
            }
            if (targetSquareCopy < 0 || targetSquareCopy > 63) {
                return errorIntFromError(Error.Invalid_Target_Square);
            }
            if (piece < 0 || piece > 11) {
                return errorIntFromError(Error.Invalid_Piece);
            }
            if (tag < 0 || tag > 27) {
                return errorIntFromError(Error.Invalid_Tag);
            }
            if (startingSquareCopy == targetSquareCopy) {
                return errorIntFromError(Error.Starting_Square_And_Target_Square_The_Same);
            }
            if (tag == GenConst.TAG_WHITE_EP || tag == GenConst.TAG_BLACK_EP) {
                if (Board.ep_global != targetSquareCopy) {
                    return errorIntFromError(Error.Ep_Not_Target_Square);
                }
            }
            if (debugInfo.PromotionExpected == false) {
                if (tag >= GenConst.TAG_B_N_PROMOTION && tag <= GenConst.TAG_W_R_PROMOTION_CAP) {
                    PrintAllDebug(startingSquareCopy, targetSquareCopy, piece, tag);
                    return errorIntFromError(Error.Promotion_When_Not_Expected);
                }
            }

            boolean sideBefore = Board.is_white_global;

            int captureIndex = MakeMove(startingSquareCopy, targetSquareCopy, tag, piece);

            if (Board.is_white_global == sideBefore) {
                return errorIntFromError(Error.Side_Not_Changed);
            }

            debugInfo.lastPiece = piece;
            debugInfo.LastStarting = startingSquareCopy;
            debugInfo.LastTarget = targetSquareCopy;
            debugInfo.LastTag = tag;

            ErrorInt priorNodes = errorIntFromValue(nodes.getValue());
            ErrorInt nodesToAdd = PerftFunctionsDebug(depth - 1, ply + 1, debugInfo);
            if (nodesToAdd.getError() != null) {
                return nodesToAdd;
            }
            nodes.setValue(nodes.getValue() + nodesToAdd.getValue());

            boolean newSide = Board.is_white_global;

            UnmakeMove(startingSquareCopy, targetSquareCopy, piece, tag, captureIndex);

            if (Board.is_white_global == newSide) { 
                return errorIntFromError(Error.Side_Not_Changed_Back);
            }

            Board.castle_rights_global[0] = copy_castle[0];
            Board.castle_rights_global[1] = copy_castle[1];
            Board.castle_rights_global[2] = copy_castle[2];
            Board.castle_rights_global[3] = copy_castle[3];
            Board.ep_global = copyEp;

            for (int i = 0; i < 12; i++) {
                if (Board.bitboard_array_global[i] != bitboardCopy[i]) {
                    PrintAllBitboards();
                    System.out.println(String.format("global %d", i));
                    PrintBitboard(Board.bitboard_array_global[i]);
                    System.out.println(String.format("copy %d", i));
                    PrintBitboard(bitboardCopy[i]);
                    PrintAllDebug(startingSquareCopy, targetSquareCopy, piece, tag);
                    return errorIntFromError(Error.Copy_Boards_Not_Same);
                }
            }

            if (ply == 0)
            {
                PrintMoveNoNL(startingSquareCopy, targetSquareCopy, tag);
                System.out.println(String.format(": %d", nodes.getValue() - priorNodes.getValue()));
            }
        }

        return nodes;
    }

    static void runPerftFunctionsDebug(int depth)
    {
        long startTime = System.currentTimeMillis();
    
        DebugInfo debugInfo = new DebugInfo();
        debugInfo.CallCount = 0;
        debugInfo.LastStarting = 0;

        ErrorInt nodes = PerftFunctionsDebug(depth, 0, debugInfo);
        if (nodes.getError() != null) {
            ErrorInt.printError(nodes.getError());
        }

        long endTime = System.currentTimeMillis();

        long elapsedTime = endTime - startTime;

        String nodeString = String.format("Nodes: %d\n", nodes.getValue());
        String timeString = String.format("Time taken: %d ms\n", elapsedTime);
        
        Pr.print(nodeString);
        Pr.print(timeString);
    }

    //int[] startingSquares = new int[50];
    
    static int PerftFunctions(int depth, int ply)
    {
        int[] startingSquares = new int[50];
        int[] targetSquares = new int[50];
        int[] tags = new int[50];
        int[] pieces = new int[50];

        int moveCount = getMoves(startingSquares, targetSquares, tags, pieces);

        if (depth == 1){
            return moveCount;
        }

        int nodes = 0;
        
        int copyEp = Board.ep_global;
        boolean[] copy_castle = {
            Board.castle_rights_global[0],
            Board.castle_rights_global[1],
            Board.castle_rights_global[2],
            Board.castle_rights_global[3],
        };

        for (int move_index = 0; move_index < moveCount; ++move_index)
        {
            int startingSquareCopy = startingSquares[move_index];
            int targetSquareCopy = targetSquares[move_index];
            int piece = pieces[move_index];
            int tag = tags[move_index];

            int captureIndex = MakeMove(startingSquareCopy, targetSquareCopy, tag, piece);

            int priorNodes = nodes;
            nodes += PerftFunctions(depth - 1, ply + 1);

            UnmakeMove(startingSquareCopy, targetSquareCopy, piece, tag, captureIndex);

            Board.castle_rights_global[0] = copy_castle[0];
            Board.castle_rights_global[1] = copy_castle[1];
            Board.castle_rights_global[2] = copy_castle[2];
            Board.castle_rights_global[3] = copy_castle[3];
            Board.ep_global = copyEp;

            //if (ply == 0)
            //{
                //PrintMoveNoNL(startingSquareCopy, targetSquareCopy, tag);
                //System.out.println(String.format(": %d", nodes - priorNodes));
            //}
        }

        return nodes;
    }

    static void runPerftFunctions(int depth)
    {
        long startTime = System.currentTimeMillis();
    
        int nodes = PerftFunctions(depth, 0);

        long endTime = System.currentTimeMillis();

        long elapsedTime = endTime - startTime;

        String nodeString = String.format("Nodes: %d\n", nodes);
        String timeString = String.format("Time taken: %d ms\n", elapsedTime);
        
        Pr.print(nodeString);
        Pr.print(timeString);
    }




    static void callPerft()
    {
        long startTime = System.currentTimeMillis();
        
        //int nodes = PerftInlineDebug(0, 0);

        long endTime = System.currentTimeMillis();

        long elapsedTime = endTime - startTime;

        //String nodeString = String.format("Nodes: %d\n", nodes);
        String timeString = String.format("Time taken: %d ms\n", elapsedTime);
        
        //Pr.print(nodeString);
        Pr.print(timeString);
    }
}
