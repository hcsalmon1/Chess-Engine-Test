
import java.math.BigInteger;


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
            case GenConst.TAG_BCaptureKnightPromotion:
            case GenConst.TAG_BKnightPromotion:
            case GenConst.TAG_WKnightPromotion:
            case GenConst.TAG_WCaptureKnightPromotion:
                System.out.printf("n");
                break;
            case GenConst.TAG_BCaptureRookPromotion:
            case GenConst.TAG_BRookPromotion:
            case GenConst.TAG_WRookPromotion:
            case GenConst.TAG_WCaptureRookPromotion:
                System.out.printf("r");
                break;
            case GenConst.TAG_BCaptureBishopPromotion:
            case GenConst.TAG_BBishopPromotion:
            case GenConst.TAG_WBishopPromotion:
            case GenConst.TAG_WCaptureBishopPromotion:
                System.out.printf("b");
                break;
            case GenConst.TAG_BCaptureQueenPromotion:
            case GenConst.TAG_BQueenPromotion:
            case GenConst.TAG_WQueenPromotion:
            case GenConst.TAG_WCaptureQueenPromotion:
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

    static final BigInteger MAX_ULONG = new BigInteger("18446744073709551615");

    static final BigInteger MAGIC = new BigInteger("285870213051386505");

    static final int[] DEBRUIJN64 =
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

    static int BitScanForward(BigInteger bitboard) {

        for (int i = 0; i < 64; i++) {
            if (bitboard.testBit(i)) {
                return i;
            }
        }
        throw new AssertionError("Bitscanforward: bit not found");
        //BigInteger xorResult = bitboard.xor(bitboard.subtract(BigInteger.ONE));
        //BigInteger multiplied = xorResult.multiply(MAGIC);
        //int index = multiplied.shiftRight(58).intValue();
        //return DEBRUIJN64[index];
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


    static int makeMove(int piece, int tag, int startingSquare, int targetSquare)
    {
        Pr.println("___make move____  piece: " + piece + " tag: " + tag + " startingSquare: " + startingSquare + " targetSquare: " + targetSquare);
        assert piece >= GenConst.WP && piece <= GenConst.BK : "invalid piece: " + piece;
        assert startingSquare >= 0 && startingSquare < 64 : "invalid starting square: " + startingSquare;
        assert targetSquare >= 0 && targetSquare < 64 : "invalid target square: " + targetSquare;

       // printMoveInfo(startingSquare, targetSquare, tag, piece);

        int captureIndex = -1;
        Board.is_white_global = !Board.is_white_global;
        switch (tag)
        {
        case GenConst.TAG_NONE: //none
        case GenConst.TAG_CHECK: //check
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_CAPTURE: //capture
        case GenConst.TAG_CHECK_CAPTURE: //check cap
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            if (piece >= GenConst.WP && piece <= GenConst.WK)
            {
                for (int i = GenConst.BP; i <= GenConst.BK; ++i)
                {
                    if (targetSquare == 47) {
                        Pr.println("   board array: " + Board.bitboard_array_global[i]);
                    }

                    if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                    {
                        captureIndex = i;
                        break;
                    }
                }
                assert captureIndex >= 0 && captureIndex <= 11 : "invalid capture index, capture";
                if (captureIndex < 0 || captureIndex > 11)
                {
                    Pr.println("Invalid capture index: " + captureIndex);
                }
                Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            }
            else //is black
            {
                for (int i = GenConst.WP; i <= GenConst.WK; ++i)
                {
                    if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                    {
                        captureIndex = i;
                        break;
                    }
                }
                assert captureIndex >= 0 && captureIndex <= 11 : "invalid capture index, capture";
                Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            }

            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_WHITEEP: //white ep
            //move piece
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].and(MoveConstants.SQUARE_BBS[targetSquare + 8].not());
            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_BLACKEP: //black ep
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].and(MoveConstants.SQUARE_BBS[targetSquare - 8].not());
            Board.ep = NO_SQUARE;
            break;

        case GenConst.TAG_WCASTLEKS: //WKS
            //white king
            Board.bitboard_array_global[GenConst.WK] =Board.bitboard_array_global[GenConst.WK].or(MoveConstants.SQUARE_BBS[GenConst.G1]);
            Board.bitboard_array_global[GenConst.WK] =Board.bitboard_array_global[GenConst.WK].and(MoveConstants.SQUARE_BBS[GenConst.E1].not());
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].or(MoveConstants.SQUARE_BBS[GenConst.F1]);
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[GenConst.H1].not());

            Board.castle_rights_global[WKS_CASTLE_RIGHTS] = false;
            Board.castle_rights_global[WQS_CASTLE_RIGHTS] = false;
            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_WCASTLEQS: //WQS
            Board.bitboard_array_global[GenConst.WK] =Board.bitboard_array_global[GenConst.WK].or(MoveConstants.SQUARE_BBS[GenConst.C1]);
            Board.bitboard_array_global[GenConst.WK] =Board.bitboard_array_global[GenConst.WK].and(MoveConstants.SQUARE_BBS[GenConst.E1].not());
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].or(MoveConstants.SQUARE_BBS[GenConst.D1]);
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[GenConst.A1].not());

            Board.castle_rights_global[WKS_CASTLE_RIGHTS] = false;
            Board.castle_rights_global[WQS_CASTLE_RIGHTS] = false;
            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_BCASTLEKS: //BKS
            Board.bitboard_array_global[GenConst.BK] =Board.bitboard_array_global[GenConst.BK].or(MoveConstants.SQUARE_BBS[GenConst.G8]);
            Board.bitboard_array_global[GenConst.BK] =Board.bitboard_array_global[GenConst.BK].and(MoveConstants.SQUARE_BBS[GenConst.E8].not());
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].or(MoveConstants.SQUARE_BBS[GenConst.F8]);
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[GenConst.H8].not());

            Board.castle_rights_global[BKS_CASTLE_RIGHTS] = false;
            Board.castle_rights_global[BQS_CASTLE_RIGHTS] = false;
            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_BCASTLEQS: //BQS
            Board.bitboard_array_global[GenConst.BK] =Board.bitboard_array_global[GenConst.BK].or(MoveConstants.SQUARE_BBS[GenConst.C8]);
            Board.bitboard_array_global[GenConst.BK] =Board.bitboard_array_global[GenConst.BK].and(MoveConstants.SQUARE_BBS[GenConst.E8].not());
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].or(MoveConstants.SQUARE_BBS[GenConst.D8]);
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[GenConst.A8].not());

            Board.castle_rights_global[BKS_CASTLE_RIGHTS] = false;
            Board.castle_rights_global[BQS_CASTLE_RIGHTS] = false;
            Board.ep = NO_SQUARE;
            break;

        case GenConst.TAG_BKnightPromotion: //BNPr
            Board.bitboard_array_global[GenConst.BN] =Board.bitboard_array_global[GenConst.BN].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_BBishopPromotion: //BBPr
            Board.bitboard_array_global[GenConst.BB] =Board.bitboard_array_global[GenConst.BB].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_BQueenPromotion: //BQPr
            Board.bitboard_array_global[GenConst.BQ] =Board.bitboard_array_global[GenConst.BQ].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_BRookPromotion: //BRPr
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case 12: //WNPr
            Board.bitboard_array_global[GenConst.WN] =Board.bitboard_array_global[GenConst.WN].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case 13: //WBPr
            Board.bitboard_array_global[GenConst.WB] =Board.bitboard_array_global[GenConst.WB].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case 14: //WQPr
            Board.bitboard_array_global[GenConst.WQ] =Board.bitboard_array_global[GenConst.WQ].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case 15: //WRPr
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case 16: //BNPrCAP
            Board.bitboard_array_global[GenConst.BN] =Board.bitboard_array_global[GenConst.BN].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            for (int i = GenConst.WP; i <= GenConst.WK; ++i)
            {
                if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                {
                    captureIndex = i;
                    break;
                }
            }
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].and(MoveConstants.SQUARE_BBS[targetSquare].not());

            break;
        case 17: //BBPrCAP
            Board.bitboard_array_global[GenConst.BB] =Board.bitboard_array_global[GenConst.BB].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            for (int i = GenConst.WP; i <= GenConst.WK; ++i)
            {
                if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                {
                    captureIndex = i;
                    break;
                }
            }
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 18: //BQPrCAP
            Board.bitboard_array_global[GenConst.BQ] =Board.bitboard_array_global[GenConst.BQ].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            for (int i = GenConst.WP; i <= GenConst.WK; ++i)
            {
                if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                {
                    captureIndex = i;
                    break;
                }
            }
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 19: //BRPrCAP
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            for (int i = GenConst.WP; i <= GenConst.WK; ++i)
            {
                if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                {
                    captureIndex = i;
                    break;
                }
            }
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 20: //WNPrCAP
            Board.bitboard_array_global[GenConst.WN] =Board.bitboard_array_global[GenConst.WN].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            for (int i = GenConst.BP; i <= GenConst.BK; ++i)
            {
                if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                {
                    captureIndex = i;
                    break;
                }
            }
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].and(MoveConstants.SQUARE_BBS[targetSquare].not());

            break;
        case 21: //WBPrCAP
            Board.bitboard_array_global[GenConst.WB] =Board.bitboard_array_global[GenConst.WB].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            for (int i = GenConst.BP; i <= GenConst.BK; ++i)
            {
                if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                {
                    captureIndex = i;
                    break;
                }
            }
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].and(MoveConstants.SQUARE_BBS[targetSquare].not());

            break;
        case 22: //WQPrCAP
            Board.bitboard_array_global[GenConst.WQ] =Board.bitboard_array_global[GenConst.WQ].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            for (int i = GenConst.BP; i <= GenConst.BK; ++i)
            {
                if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                {
                    captureIndex = i;
                    break;
                }
            }
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].and(MoveConstants.SQUARE_BBS[targetSquare].not());

            break;
        case 23: //WRPrCAP
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());

            Board.ep = NO_SQUARE;
            for (int i = GenConst.BP; i <= GenConst.BK; i++)
            {
                if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                {
                    captureIndex = i;
                    break;
                }
            }
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 24: //WDouble
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = targetSquare + 8;
            break;
        case 25: //BDouble
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = targetSquare - 8;
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
                if (isZero((Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[GenConst.H1]))))
                {
                    Board.castle_rights_global[WKS_CASTLE_RIGHTS] = false;
                }
            }
            if (Board.castle_rights_global[WQS_CASTLE_RIGHTS] == true)
            {
                if (isZero((Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[GenConst.A1]))))
                {
                    Board.castle_rights_global[WQS_CASTLE_RIGHTS] = false;
                }
            }
        }
        else if (piece == GenConst.BR)
        {
            if (Board.castle_rights_global[BKS_CASTLE_RIGHTS] == true)
            {
                if (isZero((Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[GenConst.H8]))))
                {
                    Board.castle_rights_global[BKS_CASTLE_RIGHTS] = false;
                }
            }
            if (Board.castle_rights_global[BQS_CASTLE_RIGHTS] == true)
            {
                if (isZero((Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[GenConst.A8]))))
                {
                    Board.castle_rights_global[BQS_CASTLE_RIGHTS] = false;
                }
            }
        }
        return captureIndex;
    }

    static void unmakeMove(int piece, int tag, int startingSquare, int targetSquare, int captureIndex)
    {
        Pr.println("___unmake move____  piece: " + piece + " tag: " + tag + " startingSquare: " + startingSquare + " targetSquare: " + targetSquare + " captureIndex: " + captureIndex);
        Board.is_white_global = !Board.is_white_global;
        switch (tag)
        {
        case GenConst.TAG_NONE: //none
        case GenConst.TAG_CHECK: //check
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case GenConst.TAG_CAPTURE: //capture
        case GenConst.TAG_CHECK_CAPTURE: //check cap
            assert captureIndex != -1 : "invalid capture index";
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Pr.println("bp capture before: " + Board.bitboard_array_global[GenConst.BP]);
            Board.bitboard_array_global[captureIndex] = Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Pr.println("bp capture after: " + Board.bitboard_array_global[GenConst.BP]);
            break;
        case GenConst.TAG_WHITEEP: //white ep
            //move piece
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].and(MoveConstants.SQUARE_BBS[targetSquare + 8]);
            break;
        case GenConst.TAG_BLACKEP: //black ep
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].or(MoveConstants.SQUARE_BBS[targetSquare - 8]);
            break;

        case GenConst.TAG_WCASTLEKS: //WKS
            //white king
            Board.bitboard_array_global[GenConst.WK] =Board.bitboard_array_global[GenConst.WK].or(MoveConstants.SQUARE_BBS[GenConst.E1]);
            Board.bitboard_array_global[GenConst.WK] =Board.bitboard_array_global[GenConst.WK].and(MoveConstants.SQUARE_BBS[GenConst.G1].not());
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].or(MoveConstants.SQUARE_BBS[GenConst.H1]);
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[GenConst.F1].not());
            break;
        case GenConst.TAG_WCASTLEQS: //WQS
            Board.bitboard_array_global[GenConst.WK] =Board.bitboard_array_global[GenConst.WK].or(MoveConstants.SQUARE_BBS[GenConst.E1]);
            Board.bitboard_array_global[GenConst.WK] =Board.bitboard_array_global[GenConst.WK].and(MoveConstants.SQUARE_BBS[GenConst.C1].not());
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].or(MoveConstants.SQUARE_BBS[GenConst.A1]);
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[GenConst.D1].not());
            break;
        case GenConst.TAG_BCASTLEKS: //BKS
            Board.bitboard_array_global[GenConst.BK] =Board.bitboard_array_global[GenConst.BK].or(MoveConstants.SQUARE_BBS[GenConst.E8]);
            Board.bitboard_array_global[GenConst.BK] =Board.bitboard_array_global[GenConst.BK].and(MoveConstants.SQUARE_BBS[GenConst.G8].not());
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].or(MoveConstants.SQUARE_BBS[GenConst.H8]);
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[GenConst.F8].not());
            break;
        case GenConst.TAG_BCASTLEQS: //BQS
            Board.bitboard_array_global[GenConst.BK] =Board.bitboard_array_global[GenConst.BK].or(MoveConstants.SQUARE_BBS[GenConst.E8]);
            Board.bitboard_array_global[GenConst.BK] =Board.bitboard_array_global[GenConst.BK].and(MoveConstants.SQUARE_BBS[GenConst.C8].not());
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].or(MoveConstants.SQUARE_BBS[GenConst.A8]);
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[GenConst.D8].not());
            break;

        case GenConst.TAG_BKnightPromotion: //BNPr
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BN] =Board.bitboard_array_global[GenConst.BN].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case GenConst.TAG_BBishopPromotion: //BBPr
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BB] =Board.bitboard_array_global[GenConst.BB].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case GenConst.TAG_BQueenPromotion: //BQPr
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BQ] =Board.bitboard_array_global[GenConst.BQ].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case GenConst.TAG_BRookPromotion: //BRPr
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 12: //WNPr
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WN] =Board.bitboard_array_global[GenConst.WN].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 13: //WBPr
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WB] =Board.bitboard_array_global[GenConst.WB].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 14: //WQPr
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WQ] =Board.bitboard_array_global[GenConst.WQ].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 15: //WRPr
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 16: //BNPrCAP
            assert captureIndex != -1 : "invalid capture index";
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BN] =Board.bitboard_array_global[GenConst.BN].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[targetSquare]);
            break;
        case 17: //BBPrCAP
            assert captureIndex != -1 : "invalid capture index";
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BB] =Board.bitboard_array_global[GenConst.BB].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[targetSquare]);
            break;
        case 18: //BQPrCAP
            assert captureIndex != -1 : "invalid capture index";
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BQ] =Board.bitboard_array_global[GenConst.BQ].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[targetSquare]);
            break;
        case 19: //BRPrCAP
            assert captureIndex != -1 : "invalid capture index";
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[targetSquare]);
            break;
        case 20: //WNPrCAP
            assert captureIndex != -1 : "invalid capture index";
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WN] =Board.bitboard_array_global[GenConst.WN].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[targetSquare]);
            break;
        case 21: //WBPrCAP
            assert captureIndex != -1 : "invalid capture index";
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WB] =Board.bitboard_array_global[GenConst.WB].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[targetSquare]);
            break;
        case 22: //WQPrCAP
            assert captureIndex != -1 : "invalid capture index";
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WQ] =Board.bitboard_array_global[GenConst.WQ].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[targetSquare]);
            break;
        case 23: //WRPrCAP
            assert captureIndex != -1 : "invalid capture index";
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[targetSquare]);
            break;
        case 24: //WDouble

            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 25: //BDouble
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            break;
        }


    }

    static int getMoves(int[][] move_list)
    {
        Pr.println(" getMoves:");

        int move_count = 0;

        //#region Moves

        //Move generating variables
        BigInteger WHITE_OCCUPANCIES = Board.bitboard_array_global[0]
        .or(Board.bitboard_array_global[1])
        .or(Board.bitboard_array_global[2])
        .or(Board.bitboard_array_global[3])
        .or(Board.bitboard_array_global[4])
        .or(Board.bitboard_array_global[5]); 
        BigInteger BLACK_OCCUPANCIES = Board.bitboard_array_global[6]
        .or(Board.bitboard_array_global[7])
        .or(Board.bitboard_array_global[8])
        .or(Board.bitboard_array_global[9])
        .or(Board.bitboard_array_global[10])
        .or(Board.bitboard_array_global[11]);
        BigInteger COMBINED_OCCUPANCIES = WHITE_OCCUPANCIES.or(BLACK_OCCUPANCIES);
        BigInteger EMPTY_OCCUPANCIES = COMBINED_OCCUPANCIES.not(); 
        BigInteger temp_bitboard, temp_pin_bitboard, temp_attack, temp_empty, temp_captures;
        BigInteger check_bitboard = new BigInteger("0");
        int starting_square = NO_SQUARE, target_square = NO_SQUARE;

        int[][] pinArray =
        {
            { -1, -1 },
            { -1, -1 },
            { -1, -1 },
            { -1, -1 },
            { -1, -1 },
            { -1, -1 },
            { -1, -1 },
            { -1, -1 },
        };

        int pinNumber = 0;

        Pr.println(" -variables declared");

    if (Board.is_white_global == true)
    {
        Pr.println(" white to play");

        int whiteKingCheckCount = 0;
        int whiteKingPosition = BitScanForward(Board.bitboard_array_global[GenConst.WK]);

        Pr.print("  white king position: ");
        Pr.printSquareLn(whiteKingPosition);

        //#region White checks and pins

        //pawns
        temp_bitboard = Board.bitboard_array_global[GenConst.BP].and(MoveConstants.WHITE_PAWN_ATTACKS[whiteKingPosition]);
        if (temp_bitboard.compareTo(BigInteger.ZERO) != 0) 
        {
            int pawn_square = BitScanForward(temp_bitboard); 

                if (check_bitboard.compareTo(BigInteger.ZERO) != 0) 
                {
                    check_bitboard = MoveConstants.SQUARE_BBS[pawn_square];
                }
            
            whiteKingCheckCount++;
        }

        //knights
        temp_bitboard = Board.bitboard_array_global[GenConst.BN].and(MoveConstants.KNIGHT_ATTACKS[whiteKingPosition]);
        if (temp_bitboard.compareTo(BigInteger.ZERO) != 0) 
        {
            int knight_square = BitScanForward(temp_bitboard);

            if (check_bitboard.compareTo(BigInteger.ZERO) != 0) 
            {
                check_bitboard = MoveConstants.SQUARE_BBS[knight_square];
            }
            
            whiteKingCheckCount++;
        }

        //bishops
        BigInteger bishopAttacksChecks = MoveUtils.GetBishopMovesSeparate(BLACK_OCCUPANCIES, whiteKingPosition);
        temp_bitboard = Board.bitboard_array_global[GenConst.BB].and(bishopAttacksChecks);
        while (temp_bitboard.compareTo(BigInteger.ZERO) != 0) 
        {
            int piece_square = BitScanForward(temp_bitboard); 
            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square].and(WHITE_OCCUPANCIES);

            if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0) 
            {
                if (check_bitboard.compareTo(BigInteger.ZERO) != 0) 
                {
                    check_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square];
                }
                whiteKingCheckCount++;
            }
            else
            {
                int pinned_square = BitScanForward(temp_pin_bitboard); 
                temp_pin_bitboard = temp_pin_bitboard.and(temp_pin_bitboard.subtract(BigInteger.ONE));

                if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0) 
                {
                    pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
                    pinNumber++;
                }
            }
            temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));
        }

        //queen
        temp_bitboard = Board.bitboard_array_global[GenConst.BQ].and(bishopAttacksChecks);
        while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
        {
            int piece_square = BitScanForward(temp_bitboard); 

            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square].and(WHITE_OCCUPANCIES);

            if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                if (check_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    check_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square];
                }
                whiteKingCheckCount++;
            }
            else
            {
                int pinned_square = BitScanForward(temp_pin_bitboard); 
                temp_pin_bitboard = temp_pin_bitboard.and(temp_pin_bitboard.subtract(BigInteger.ONE));

                if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
                    pinNumber++;
                }
            }
            temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));
        }

        //rook
        BigInteger rook_attacks = MoveUtils.GetRookMovesSeparate(BLACK_OCCUPANCIES, whiteKingPosition); 
        temp_bitboard = Board.bitboard_array_global[GenConst.BR].and(rook_attacks);
        while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
        {
            int piece_square = BitScanForward(temp_bitboard); 
            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square].and(WHITE_OCCUPANCIES);

            if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                if (check_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    check_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square];
                }
                whiteKingCheckCount++;
            }
            else
            {
                int pinned_square = BitScanForward(temp_pin_bitboard); 
                temp_pin_bitboard = temp_pin_bitboard.and(temp_pin_bitboard.subtract(BigInteger.ONE));

                if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
                    pinNumber++;
                }
            }
            temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));
        }

        //queen
        temp_bitboard = Board.bitboard_array_global[GenConst.BQ].and(rook_attacks);
        while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
        {
            int piece_square = BitScanForward(temp_bitboard); 
            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square].and(WHITE_OCCUPANCIES);

            if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                if (check_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    check_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square];
                }
                whiteKingCheckCount++;
            }
            else
            {
                int pinned_square = BitScanForward(temp_pin_bitboard); 
                temp_pin_bitboard = temp_pin_bitboard.and(temp_pin_bitboard.subtract(BigInteger.ONE));

                if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
                    pinNumber++;
                }
            }
            temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));
        }

        Pr.println("  -pins and checks complete: ");
        Pr.print("  white king check count: ");
        Pr.printIntLn(whiteKingCheckCount);
        Pr.print("  pin count: ");
        Pr.printIntLn(pinNumber);
        Pr.println("  _____________");

        //#endregion

        BigInteger occupanciesWithoutWhiteKing = COMBINED_OCCUPANCIES.and(Board.bitboard_array_global[GenConst.WK].not());
        int wKingMoveCount = 0;


        temp_attack = MoveConstants.KING_ATTACKS[whiteKingPosition];
        temp_empty = temp_attack.and(EMPTY_OCCUPANCIES);
        while (temp_empty.signum() != 0) // Using BigInteger's signum() to check if it's not 0
        {
            target_square = BitScanForward(temp_empty);
            temp_empty = temp_empty.and(temp_empty.subtract(BigInteger.ONE)); // Using BigInteger's subtract and and
        
            if (Board.bitboard_array_global[GenConst.BP].and(MoveConstants.WHITE_PAWN_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.BN].and(MoveConstants.KNIGHT_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.BK].and(MoveConstants.KING_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            BigInteger bishopAttacks = MoveUtils.GetBishopMovesSeparate(occupanciesWithoutWhiteKing, target_square);
            if (Board.bitboard_array_global[GenConst.BB].and(bishopAttacks).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.BQ].and(bishopAttacks).signum() != 0) {
                continue;
            }
            BigInteger rookAttacks = MoveUtils.GetRookMovesSeparate(occupanciesWithoutWhiteKing, target_square); 
            if (Board.bitboard_array_global[GenConst.BR].and(rookAttacks).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.BQ].and(rookAttacks).signum() != 0) {
                continue;
            }
        
            move_list[move_count][MOVE_STARTING] = whiteKingPosition;
            move_list[move_count][MOVE_TARGET] = target_square;
            move_list[move_count][MOVE_TAG] = GenConst.TAG_NONE;
            move_list[move_count][MOVE_PIECE] = GenConst.WK;
            move_count++;
            wKingMoveCount++;
        }

        //captures
        temp_captures = temp_attack.and(BLACK_OCCUPANCIES);
        while (temp_captures.signum() != 0) // Using BigInteger's signum() to check if it's not 0
        {
            target_square = BitScanForward(temp_captures);
            temp_captures = temp_captures.and(temp_captures.subtract(BigInteger.ONE)); // Using BigInteger's subtract and and
        
            if (Board.bitboard_array_global[GenConst.BP].and(MoveConstants.WHITE_PAWN_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.BN].and(MoveConstants.KNIGHT_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.BK].and(MoveConstants.KING_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            BigInteger bishopAttacks = MoveUtils.GetBishopMovesSeparate(occupanciesWithoutWhiteKing, target_square);
            if (Board.bitboard_array_global[GenConst.BB].and(bishopAttacks).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.BQ].and(bishopAttacks).signum() != 0) {
                continue;
            }
            BigInteger rookAttacks = MoveUtils.GetRookMovesSeparate(occupanciesWithoutWhiteKing, target_square); 
            if (Board.bitboard_array_global[GenConst.BR].and(rookAttacks).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.BQ].and(rookAttacks).signum() != 0) {
                continue;
            }
        
            move_list[move_count][MOVE_STARTING] = whiteKingPosition;
            move_list[move_count][MOVE_TARGET] = target_square;
            move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
            move_list[move_count][MOVE_PIECE] = GenConst.WK;
            move_count++;
            wKingMoveCount++;
        }

        Pr.print("  white king move count: ");
        Pr.printIntLn(wKingMoveCount);
        Pr.println("  _____________");

        if (whiteKingCheckCount < 2)
        {

            if (whiteKingCheckCount == 0)
            {
                check_bitboard = MAX_ULONG;
            }

            //#region White castling

            if (whiteKingCheckCount == 0)
            {
                if (Board.castle_rights_global[WKS_CASTLE_RIGHTS] == true)
                {
                    if (whiteKingPosition == GenConst.E1) //king on e1
                    {
                        if (isNotZero(WKS_EMPTY_BITBOARD.and(COMBINED_OCCUPANCIES)) == false) //f1 and g1 empty
                        {
                            if (isNotZero(Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[GenConst.H1])) == false) //rook on h1
                            {
                                if (MoveUtils.Is_Square_Attacked_By_Black_Global(GenConst.F1, COMBINED_OCCUPANCIES) == false)
                                {
                                    if (MoveUtils.Is_Square_Attacked_By_Black_Global(GenConst.G1, COMBINED_OCCUPANCIES) == false)
                                    {
                                        move_list[move_count][MOVE_STARTING] = GenConst.E1;
                                        move_list[move_count][MOVE_TARGET] = GenConst.G1;
                                        move_list[move_count][MOVE_TAG] = GenConst.TAG_WCASTLEKS;
                                        move_list[move_count][MOVE_PIECE] = GenConst.WK;
                                        move_count++;
                                    }
                                }
                            }
                        }
                    }
                }
                if (Board.castle_rights_global[WQS_CASTLE_RIGHTS] == true)
                {
                    if (whiteKingPosition == GenConst.E1) //king on e1
                    {
                        if (isNotZero(WQS_EMPTY_BITBOARD.and(COMBINED_OCCUPANCIES)) == false) //f1 and g1 empty
                        {
                            if ((isNotZero(Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[GenConst.A1]))) == true) //rook on h1
                            {
                                if (MoveUtils.Is_Square_Attacked_By_Black_Global(GenConst.C1, COMBINED_OCCUPANCIES) == false)
                                {
                                    if (MoveUtils.Is_Square_Attacked_By_Black_Global(GenConst.D1, COMBINED_OCCUPANCIES) == false)
                                    {
                                        move_list[move_count][MOVE_STARTING] = GenConst.E1;
                                        move_list[move_count][MOVE_TARGET] = GenConst.C1;
                                        move_list[move_count][MOVE_TAG] = GenConst.TAG_WCASTLEQS;
                                        move_list[move_count][MOVE_PIECE] = GenConst.WK;
                                        move_count++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //#endregion

            //#region White knight moves

            Pr.println("  -white knight moves: ");
            temp_bitboard = Board.bitboard_array_global[GenConst.WN];
            Pr.print("    white knight bitboard: ");
            Pr.printBigIntegerLn(temp_bitboard);
            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard);
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE)); //removes the knight from that square to not infinitely loop

                Pr.println("\n   knight on square: " + starting_square);

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                temp_attack = ((MoveConstants.KNIGHT_ATTACKS[starting_square].and(BLACK_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard))); //gets knight captures
                Pr.println("   knight captures: " + temp_attack);

                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);
                    
                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                    move_list[move_count][MOVE_PIECE] = GenConst.WN;
                    move_count++;
                }

                temp_attack = ((MoveConstants.KNIGHT_ATTACKS[starting_square].and(EMPTY_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                Pr.println("   knight regular moves: " + temp_attack);

                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] =  GenConst.TAG_NONE;
                    move_list[move_count][MOVE_PIECE] = GenConst.WN;
                    move_count++;
                }
            }

            //#endregion

            //#region White pawn moves
            Pr.println("  _____________");
            Pr.println("  movecount: " + move_count);
            temp_bitboard = Board.bitboard_array_global[GenConst.WP];
            Pr.println("  -white pawn moves: ");

            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard); 
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));

                Pr.println("\n   pawn on square: " + starting_square);

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                //#region pawn forward

                if (isZero(MoveConstants.SQUARE_BBS[starting_square - 8].and(COMBINED_OCCUPANCIES)) == true) //if up one square is empty
                {
                    if (isNotZero(MoveConstants.SQUARE_BBS[starting_square - 8].and(check_bitboard).and(temp_pin_bitboard)) == true) //if not pinned or check
                    {
                        if (isNotZero(MoveConstants.SQUARE_BBS[starting_square].and(RANK_7_BITBOARD))) //if promotion
                        {
                            Pr.println("   pawn promotion forward!");
                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square - 8;
                            move_list[move_count][MOVE_TAG] = GenConst.TAG_WQueenPromotion;
                            move_list[move_count][MOVE_PIECE] = GenConst.WP;
                            move_count++;

                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square - 8;
                            move_list[move_count][MOVE_TAG] = GenConst.TAG_WRookPromotion;
                            move_list[move_count][MOVE_PIECE] = GenConst.WP;
                            move_count++;

                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square - 8;
                            move_list[move_count][MOVE_TAG] = GenConst.TAG_WBishopPromotion;
                            move_list[move_count][MOVE_PIECE] = GenConst.WP;
                            move_count++;

                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square - 8;
                            move_list[move_count][MOVE_TAG] = GenConst.TAG_WBishopPromotion;
                            move_list[move_count][MOVE_PIECE] = GenConst.WP;
                            move_count++;

                        }
                        else
                        {
                            Pr.println("   pawn forward one");
                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square - 8;
                            move_list[move_count][MOVE_TAG] =  GenConst.TAG_NONE;
                            move_list[move_count][MOVE_PIECE] = GenConst.WP;
                            move_count++;
                        }
                    }

                    if ((isNotZero(MoveConstants.SQUARE_BBS[starting_square].and(RANK_2_BITBOARD))) == true) //if on rank 2
                    {
                        if (isNotZero(MoveConstants.SQUARE_BBS[starting_square - 16].and(check_bitboard).and(temp_pin_bitboard)) == true) //if not pinned or 
                        {
                            if (isZero(MoveConstants.SQUARE_BBS[starting_square - 16].and(COMBINED_OCCUPANCIES)) == true) //if up two squares and one square are empty
                            {
                                Pr.println("   pawn forward two");
                                move_list[move_count][MOVE_STARTING] = starting_square;
                                move_list[move_count][MOVE_TARGET] = starting_square - 16;
                                move_list[move_count][MOVE_TAG] = GenConst.TAG_DoublePawnWhite;
                                move_list[move_count][MOVE_PIECE] = GenConst.WP;
                                move_count++;
                            }
                        }
                    }
                }

                //#endregion
                //#region pawn attacks

                temp_attack = ((MoveConstants.WHITE_PAWN_ATTACKS[starting_square].and(BLACK_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard))); //if black piece diagonal to pawn
                Pr.println("   pawn attacks: " + temp_attack);

                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    if (isNotZero(MoveConstants.SQUARE_BBS[starting_square].and(RANK_7_BITBOARD)) == true) //if promotion
                    {
                        Pr.println("   pawn promotion attack!");
                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_WCaptureQueenPromotion;
                        move_list[move_count][MOVE_PIECE] = GenConst.WP;
                        move_count++;

                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_WCaptureRookPromotion;
                        move_list[move_count][MOVE_PIECE] = GenConst.WP;
                        move_count++;

                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_WCaptureBishopPromotion;
                        move_list[move_count][MOVE_PIECE] = GenConst.WP;
                        move_count++;

                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_WCaptureKnightPromotion;
                        move_list[move_count][MOVE_PIECE] = GenConst.WP;
                        move_count++;
                    }
                    else
                    {
                        Pr.println("   add pawn attack move " + starting_square + " to " + target_square);
                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                        move_list[move_count][MOVE_PIECE] = GenConst.WP;
                        move_count++;
                    }
                }

                if (isNotZero(MoveConstants.SQUARE_BBS[starting_square].and(RANK_5_BITBOARD))) 
                { // check rank for ep
                    if (Board.ep != NO_SQUARE) 
                    {
                        if (isNotZero(MoveConstants.WHITE_PAWN_ATTACKS[starting_square]
                                .and(MoveConstants.SQUARE_BBS[Board.ep])
                                .and(check_bitboard)
                                .and(temp_pin_bitboard))) {
                            if (isZero(Board.bitboard_array_global[GenConst.WK].and(RANK_5_BITBOARD))) { // if no king on rank 5
                                move_list[move_count][MOVE_STARTING] = starting_square;
                                move_list[move_count][MOVE_TARGET] = Board.ep;
                                move_list[move_count][MOVE_TAG] = GenConst.TAG_WHITEEP;
                                move_list[move_count][MOVE_PIECE] = GenConst.WP;
                                move_count++;
                            } else if (isZero(Board.bitboard_array_global[GenConst.BR].and(RANK_5_BITBOARD)) &&
                                       isZero(Board.bitboard_array_global[GenConst.BQ].and(RANK_5_BITBOARD))) { // if no b rook or queen on rank 5
                                move_list[move_count][MOVE_STARTING] = starting_square;
                                move_list[move_count][MOVE_TARGET] = Board.ep;
                                move_list[move_count][MOVE_TAG] = GenConst.TAG_WHITEEP;
                                move_list[move_count][MOVE_PIECE] = GenConst.WP;
                                move_count++;
                            } else { // wk and br or bq on rank 5
                                BigInteger occupancyWithoutEPPawns = COMBINED_OCCUPANCIES.and(MoveConstants.SQUARE_BBS[starting_square].not());
                                occupancyWithoutEPPawns = occupancyWithoutEPPawns.and(MoveConstants.SQUARE_BBS[Board.ep + 8].not());
                
                                BigInteger rookAttacksFromKing = MoveUtils.GetRookMovesSeparate(occupancyWithoutEPPawns, whiteKingPosition);
                
                                if (isZero(rookAttacksFromKing.and(Board.bitboard_array_global[GenConst.BR]))) 
                                {
                                    if (isZero(rookAttacksFromKing.and(Board.bitboard_array_global[GenConst.BQ]))) 
                                    {
                                        move_list[move_count][MOVE_STARTING] = starting_square;
                                        move_list[move_count][MOVE_TARGET] = Board.ep;
                                        move_list[move_count][MOVE_TAG] = GenConst.TAG_WHITEEP;
                                        move_list[move_count][MOVE_PIECE] = GenConst.WP;
                                        move_count++;
                                    }
                                }
                            }
                        }
                    }
                }

                //#endregion
            }

            //#endregion

            Pr.println("  -after pawns");

            //#region White rook moves

            temp_bitboard = Board.bitboard_array_global[GenConst.WR];
            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard);
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                BigInteger rookAttacks = MoveUtils.GetRookMovesSeparate(COMBINED_OCCUPANCIES, starting_square);

                temp_attack = ((rookAttacks.and(BLACK_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                    move_list[move_count][MOVE_PIECE] = GenConst.WR;
                    move_count++;
                }

                temp_attack = ((rookAttacks.and(EMPTY_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_NONE;
                    move_list[move_count][MOVE_PIECE] = GenConst.WR;
                    move_count++;
                }
            }

            //#endregion

            Pr.println("  -after rooks");

            //#region White bishop moves

            //Pr.println("\nwhite bishop");
            temp_bitboard = Board.bitboard_array_global[GenConst.WB];
            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard);
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                //Pr.println("get bishop attacks");
                BigInteger bishopAttacks = MoveUtils.GetBishopMovesSeparate(COMBINED_OCCUPANCIES, starting_square);
                //Pr.println("bishop attacks");
                //Pr.printBigIntegerLn(bishopAttacks);

                temp_attack = ((bishopAttacks.and(BLACK_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                    move_list[move_count][MOVE_PIECE] = GenConst.WB;
                    move_count++;
                }

                temp_attack = ((bishopAttacks.and(EMPTY_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] =  GenConst.TAG_NONE;
                    move_list[move_count][MOVE_PIECE] = GenConst.WB;
                    move_count++;
                }
            }

            //#endregion

            Pr.println("  -after bishop");

            //#region White queen moves

            Pr.println("______________");
            Pr.println("   white queen:");
            temp_bitboard = Board.bitboard_array_global[GenConst.WQ];
            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard);
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));

                Pr.println("\n   queen on square: " + starting_square);

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                BigInteger queenAttacks = MoveUtils.GetRookMovesSeparate(COMBINED_OCCUPANCIES, starting_square);
                Pr.println("\n   queen attacks rook: " + queenAttacks);
                queenAttacks = queenAttacks.or(MoveUtils.GetBishopMovesSeparate(COMBINED_OCCUPANCIES, starting_square));
                Pr.println("\n   queen attacks combined: " + queenAttacks);

                temp_attack = ((queenAttacks.and(BLACK_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                Pr.println("\n   queen attacks captures: " + temp_attack);

                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                    move_list[move_count][MOVE_PIECE] = GenConst.WQ;
                    move_count++;
                    Pr.println("   add queen attack capture " + starting_square + " to " + target_square);
                }

                temp_attack = ((queenAttacks.and(EMPTY_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                Pr.println("\n   queen regular moves: " + temp_attack);
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] =  GenConst.TAG_NONE;
                    move_list[move_count][MOVE_PIECE] = GenConst.WQ;
                    move_count++;
                    Pr.println("   add queen move " + starting_square + " to " + target_square);
                }
            }

            //#endregion
        
            Pr.println("  -after queen");
        }
    }
    else //black move
    {
        int blackKingCheckCount = 0;
        int blackKingPosition = BitScanForward(Board.bitboard_array_global[GenConst.BK]);

       // Pr.print("  black king position: ");
       // Pr.printIntLn(blackKingPosition);

        //#region black checks and pins

        //pawns
        temp_bitboard = Board.bitboard_array_global[GenConst.WP].and(MoveConstants.BLACK_PAWN_ATTACKS[blackKingPosition]);
        if (temp_bitboard.compareTo(BigInteger.ZERO) != 0) //if there is pawn attacking king
        {
            int pawn_square = BitScanForward(temp_bitboard); 

            if (check_bitboard.compareTo(BigInteger.ZERO) != 0) 
            {
                check_bitboard = MoveConstants.SQUARE_BBS[pawn_square];
            }
            
            blackKingCheckCount++;
        }

        //knights
        temp_bitboard = Board.bitboard_array_global[GenConst.WN].and(MoveConstants.KNIGHT_ATTACKS[blackKingPosition]);
        if (temp_bitboard.compareTo(BigInteger.ZERO) != 0) 
        {
            int knight_square = BitScanForward(temp_bitboard);

            if (check_bitboard.compareTo(BigInteger.ZERO) != 0) 
            {
                check_bitboard = MoveConstants.SQUARE_BBS[knight_square];
            }
            
            blackKingCheckCount++;
        }

        //bishops
        BigInteger bishopAttacksChecks = MoveUtils.GetBishopMovesSeparate(WHITE_OCCUPANCIES, blackKingPosition);
        temp_bitboard = Board.bitboard_array_global[GenConst.WB].and(bishopAttacksChecks);
        while (temp_bitboard.compareTo(BigInteger.ZERO) != 0) 
        {
            int piece_square = BitScanForward(temp_bitboard); 
            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][piece_square].and(BLACK_OCCUPANCIES);

            if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0) 
            {
                if (check_bitboard.compareTo(BigInteger.ZERO) != 0) 
                {
                    check_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][piece_square];
                }
                blackKingCheckCount++;
            }
            else
            {
                int pinned_square = BitScanForward(temp_pin_bitboard); 
                temp_pin_bitboard = temp_pin_bitboard.and(temp_pin_bitboard.subtract(BigInteger.ONE));

                if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0) 
                {
                    pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
                    pinNumber++;
                }
            }
            temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));
        }

        //queen
        temp_bitboard = Board.bitboard_array_global[GenConst.WQ].and(bishopAttacksChecks);
        while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
        {
            int piece_square = BitScanForward(temp_bitboard); 

            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][piece_square].and(BLACK_OCCUPANCIES);

            if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                if (check_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    check_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][piece_square];
                }
                blackKingCheckCount++;
            }
            else
            {
                int pinned_square = BitScanForward(temp_pin_bitboard); 
                temp_pin_bitboard = temp_pin_bitboard.and(temp_pin_bitboard.subtract(BigInteger.ONE));

                if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
                    pinNumber++;
                }
            }
            temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));
        }

        //rook
        BigInteger rook_attacks = MoveUtils.GetRookMovesSeparate(WHITE_OCCUPANCIES, blackKingPosition); 
        temp_bitboard = Board.bitboard_array_global[GenConst.WR].and(rook_attacks);
        while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
        {
            int piece_square = BitScanForward(temp_bitboard); 
            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][piece_square].and(BLACK_OCCUPANCIES);

            if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                if (check_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    check_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][piece_square];
                }
                blackKingCheckCount++;
            }
            else
            {
                int pinned_square = BitScanForward(temp_pin_bitboard); 
                temp_pin_bitboard = temp_pin_bitboard.and(temp_pin_bitboard.subtract(BigInteger.ONE));

                if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
                    pinNumber++;
                }
            }
            temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));
        }

        //queen
        temp_bitboard = Board.bitboard_array_global[GenConst.WQ].and(rook_attacks);
        while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
        {
            int piece_square = BitScanForward(temp_bitboard); 
            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][piece_square].and(WHITE_OCCUPANCIES);

            if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                if (check_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    check_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][piece_square];
                }
                blackKingCheckCount++;
            }
            else
            {
                int pinned_square = BitScanForward(temp_pin_bitboard); 
                temp_pin_bitboard = temp_pin_bitboard.and(temp_pin_bitboard.subtract(BigInteger.ONE));

                if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
                    pinNumber++;
                }
            }
            temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));
        }

        //#endregion

        BigInteger occupanciesWithoutWhiteKing = COMBINED_OCCUPANCIES.and(Board.bitboard_array_global[GenConst.WK].not());

        temp_attack = MoveConstants.KING_ATTACKS[blackKingPosition];
        temp_empty = temp_attack.and(EMPTY_OCCUPANCIES);

        while (temp_empty.signum() != 0) // Using BigInteger's signum() to check if it's not 0
        {
            target_square = BitScanForward(temp_empty);
            temp_empty = temp_empty.and(temp_empty.subtract(BigInteger.ONE)); // Using BigInteger's subtract and and
        
            if (Board.bitboard_array_global[GenConst.WP].and(MoveConstants.BLACK_PAWN_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.WN].and(MoveConstants.KNIGHT_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.WK].and(MoveConstants.KING_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            BigInteger bishopAttacks = MoveUtils.GetBishopMovesSeparate(occupanciesWithoutWhiteKing, target_square);
            if (Board.bitboard_array_global[GenConst.WB].and(bishopAttacks).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.WQ].and(bishopAttacks).signum() != 0) {
                continue;
            }
            BigInteger rookAttacks = MoveUtils.GetRookMovesSeparate(occupanciesWithoutWhiteKing, target_square); 
            if (Board.bitboard_array_global[GenConst.WR].and(rookAttacks).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.WQ].and(rookAttacks).signum() != 0) {
                continue;
            }
        
            move_list[move_count][MOVE_STARTING] = blackKingPosition;
            move_list[move_count][MOVE_TARGET] = target_square;
            move_list[move_count][MOVE_TAG] = GenConst.TAG_NONE;
            move_list[move_count][MOVE_PIECE] = GenConst.BK;
            move_count++;
        }

        //captures
        temp_captures = temp_attack.and(WHITE_OCCUPANCIES);

        while (temp_captures.signum() != 0) // Using BigInteger's signum() to check if it's not 0
        {
            target_square = BitScanForward(temp_captures);
            temp_captures = temp_captures.and(temp_captures.subtract(BigInteger.ONE)); // Using BigInteger's subtract and and
        
            if (Board.bitboard_array_global[GenConst.WP].and(MoveConstants.BLACK_PAWN_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.WN].and(MoveConstants.KNIGHT_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.WK].and(MoveConstants.KING_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            BigInteger bishopAttacks = MoveUtils.GetBishopMovesSeparate(occupanciesWithoutWhiteKing, target_square);
            if (Board.bitboard_array_global[GenConst.WB].and(bishopAttacks).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.WQ].and(bishopAttacks).signum() != 0) {
                continue;
            }
            BigInteger rookAttacks = MoveUtils.GetRookMovesSeparate(occupanciesWithoutWhiteKing, target_square); 
            if (Board.bitboard_array_global[GenConst.WR].and(rookAttacks).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.WQ].and(rookAttacks).signum() != 0) {
                continue;
            }
        
            move_list[move_count][MOVE_STARTING] = blackKingPosition;
            move_list[move_count][MOVE_TARGET] = target_square;
            move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
            move_list[move_count][MOVE_PIECE] = GenConst.BK;
            move_count++;
        }

        if (blackKingCheckCount < 2)
        {

            if (blackKingCheckCount == 0)
            {
                check_bitboard = MAX_ULONG;
            }

            //#region Black castling

            if (blackKingCheckCount == 0)
            {
                if (Board.castle_rights_global[BKS_CASTLE_RIGHTS] == true)
                {
                    if (blackKingPosition == GenConst.E8) //king on e1
                    {
                        if (isNotZero(BKS_EMPTY_BITBOARD.and(COMBINED_OCCUPANCIES)) == false) //f1 and g1 empty
                        {
                            if (isNotZero(Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[GenConst.H8])) == false) //rook on h8
                            {
                                if (MoveUtils.Is_Square_Attacked_By_White_Global(GenConst.F8, COMBINED_OCCUPANCIES) == false)
                                {
                                    if (MoveUtils.Is_Square_Attacked_By_White_Global(GenConst.G8, COMBINED_OCCUPANCIES) == false)
                                    {
                                        move_list[move_count][MOVE_STARTING] = GenConst.E8;
                                        move_list[move_count][MOVE_TARGET] = GenConst.G8;
                                        move_list[move_count][MOVE_TAG] = GenConst.TAG_BCASTLEKS;
                                        move_list[move_count][MOVE_PIECE] = GenConst.BK;
                                        move_count++;
                                    }
                                }
                            }
                        }
                    }
                }
                if (Board.castle_rights_global[BQS_CASTLE_RIGHTS] == true)
                {
                    if (blackKingPosition == GenConst.E8) //king on e1
                    {
                        if (isNotZero(BQS_EMPTY_BITBOARD.and(COMBINED_OCCUPANCIES)) == false) //f1 and g1 empty
                        {
                            if ((isNotZero(Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[GenConst.A8]))) == true) //rook on h1
                            {
                                if (MoveUtils.Is_Square_Attacked_By_White_Global(GenConst.C8, COMBINED_OCCUPANCIES) == false)
                                {
                                    if (MoveUtils.Is_Square_Attacked_By_White_Global(GenConst.D8, COMBINED_OCCUPANCIES) == false)
                                    {
                                        move_list[move_count][MOVE_STARTING] = GenConst.E8;
                                        move_list[move_count][MOVE_TARGET] = GenConst.C8;
                                        move_list[move_count][MOVE_TAG] = GenConst.TAG_BCASTLEQS;
                                        move_list[move_count][MOVE_PIECE] = GenConst.BK;
                                        move_count++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //#endregion

            //#region Black knight moves

            temp_bitboard = Board.bitboard_array_global[GenConst.BN];

            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard);
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE)); //removes the knight from that square to not infinitely loop

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                temp_attack = ((MoveConstants.KNIGHT_ATTACKS[starting_square].and(WHITE_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard))); //gets knight captures
                
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);
                    
                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                    move_list[move_count][MOVE_PIECE] = GenConst.BN;
                    move_count++;
                }

                temp_attack = ((MoveConstants.KNIGHT_ATTACKS[starting_square].and(EMPTY_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));

                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] =  GenConst.TAG_NONE;
                    move_list[move_count][MOVE_PIECE] = GenConst.BN;
                    move_count++;
                }
            }

            //#endregion

            //#region Black pawn moves

            temp_bitboard = Board.bitboard_array_global[GenConst.BP];

            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard); 
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                //#region pawn forward

                if (isZero(MoveConstants.SQUARE_BBS[starting_square + 8].and(COMBINED_OCCUPANCIES)) == true) //if up one square is empty
                {
                    if (isNotZero(MoveConstants.SQUARE_BBS[starting_square + 8].and(check_bitboard).and(temp_pin_bitboard)) == true) //if not pinned or check
                    {
                        if (isNotZero(MoveConstants.SQUARE_BBS[starting_square].and(RANK_2_BITBOARD))) //if promotion
                        {
                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square + 8;
                            move_list[move_count][MOVE_TAG] = GenConst.TAG_BQueenPromotion;
                            move_list[move_count][MOVE_PIECE] = GenConst.BP;
                            move_count++;

                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square + 8;
                            move_list[move_count][MOVE_TAG] = GenConst.TAG_BRookPromotion;
                            move_list[move_count][MOVE_PIECE] = GenConst.BP;
                            move_count++;

                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square + 8;
                            move_list[move_count][MOVE_TAG] = GenConst.TAG_BBishopPromotion;
                            move_list[move_count][MOVE_PIECE] = GenConst.BP;
                            move_count++;

                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square + 8;
                            move_list[move_count][MOVE_TAG] = GenConst.TAG_BBishopPromotion;
                            move_list[move_count][MOVE_PIECE] = GenConst.BP;
                            move_count++;

                        }
                        else
                        {
                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square + 8;
                            move_list[move_count][MOVE_TAG] =  GenConst.TAG_NONE;
                            move_list[move_count][MOVE_PIECE] = GenConst.BP;
                            move_count++;
                        }
                    }

                    if ((isNotZero(MoveConstants.SQUARE_BBS[starting_square].and(RANK_7_BITBOARD))) == true) //if on rank 7
                    {
                        if (isNotZero(MoveConstants.SQUARE_BBS[starting_square + 16].and(check_bitboard).and(temp_pin_bitboard)) == true) //if not pinned or 
                        {
                            if (isZero(MoveConstants.SQUARE_BBS[starting_square + 16].and(COMBINED_OCCUPANCIES)) == true) //if up two squares and one square are empty
                            {
                                move_list[move_count][MOVE_STARTING] = starting_square;
                                move_list[move_count][MOVE_TARGET] = starting_square + 16;
                                move_list[move_count][MOVE_TAG] = GenConst.TAG_DoublePawnBlack;
                                move_list[move_count][MOVE_PIECE] = GenConst.BP;
                                move_count++;
                            }
                        }
                    }
                }

                //#endregion
                //#region pawn attacks

                temp_attack = ((MoveConstants.BLACK_PAWN_ATTACKS[starting_square].and(WHITE_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard))); //if black piece diagonal to pawn

                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    if (isNotZero(MoveConstants.SQUARE_BBS[starting_square].and(RANK_2_BITBOARD)) == true) //if promotion
                    {
                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_BCaptureQueenPromotion;
                        move_list[move_count][MOVE_PIECE] = GenConst.BP;
                        move_count++;

                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_BCaptureRookPromotion;
                        move_list[move_count][MOVE_PIECE] = GenConst.BP;
                        move_count++;

                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_BCaptureBishopPromotion;
                        move_list[move_count][MOVE_PIECE] = GenConst.BP;
                        move_count++;

                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_BCaptureKnightPromotion;
                        move_list[move_count][MOVE_PIECE] = GenConst.BP;
                        move_count++;
                    }
                    else
                    {
                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                        move_list[move_count][MOVE_PIECE] = GenConst.BP;
                        move_count++;
                    }
                }

                if (isNotZero(MoveConstants.SQUARE_BBS[starting_square].and(RANK_4_BITBOARD))) 
                { // check rank for ep
                    if (Board.ep != NO_SQUARE) 
                    {
                        if (isNotZero(MoveConstants.BLACK_PAWN_ATTACKS[starting_square]
                                .and(MoveConstants.SQUARE_BBS[Board.ep])
                                .and(check_bitboard)
                                .and(temp_pin_bitboard))) {
                            if (isZero(Board.bitboard_array_global[GenConst.WK].and(RANK_4_BITBOARD))) { // if no king on rank 5
                                move_list[move_count][MOVE_STARTING] = starting_square;
                                move_list[move_count][MOVE_TARGET] = Board.ep;
                                move_list[move_count][MOVE_TAG] = GenConst.TAG_BLACKEP;
                                move_list[move_count][MOVE_PIECE] = GenConst.BP;
                                move_count++;
                            } else if (isZero(Board.bitboard_array_global[GenConst.WR].and(RANK_4_BITBOARD)) &&
                                       isZero(Board.bitboard_array_global[GenConst.WQ].and(RANK_4_BITBOARD))) { // if no b rook or queen on rank 5
                                move_list[move_count][MOVE_STARTING] = starting_square;
                                move_list[move_count][MOVE_TARGET] = Board.ep;
                                move_list[move_count][MOVE_TAG] = GenConst.TAG_BLACKEP;
                                move_list[move_count][MOVE_PIECE] = GenConst.BP;
                                move_count++;
                            } else { // wk and br or bq on rank 5
                                BigInteger occupancyWithoutEPPawns = COMBINED_OCCUPANCIES.and(MoveConstants.SQUARE_BBS[starting_square].not());
                                occupancyWithoutEPPawns = occupancyWithoutEPPawns.and(MoveConstants.SQUARE_BBS[Board.ep - 8].not());
                
                                BigInteger rookAttacksFromKing = MoveUtils.GetRookMovesSeparate(occupancyWithoutEPPawns, blackKingPosition);
                
                                if (isZero(rookAttacksFromKing.and(Board.bitboard_array_global[GenConst.WR]))) 
                                {
                                    if (isZero(rookAttacksFromKing.and(Board.bitboard_array_global[GenConst.WQ]))) 
                                    {
                                        move_list[move_count][MOVE_STARTING] = starting_square;
                                        move_list[move_count][MOVE_TARGET] = Board.ep;
                                        move_list[move_count][MOVE_TAG] = GenConst.TAG_BLACKEP;
                                        move_list[move_count][MOVE_PIECE] = GenConst.BP;
                                        move_count++;
                                    }
                                }
                            }
                        }
                    }
                }

                //#endregion
            }

            //#endregion

            //#region Black rook moves

            temp_bitboard = Board.bitboard_array_global[GenConst.BR];
            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard);
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));

             //   Pr.print("   rook square: ");
               // Pr.printIntLn(starting_square);

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                BigInteger rookAttacks = MoveUtils.GetRookMovesSeparate(COMBINED_OCCUPANCIES, starting_square);

               // Pr.print("rook attacks:");
               // Pr.printBigIntegerLn(rookAttacks);

                temp_attack = ((rookAttacks.and(WHITE_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));

              //  Pr.print("against white:");
              //  Pr.printBigIntegerLn(temp_attack);

                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                    move_list[move_count][MOVE_PIECE] = GenConst.BR;
                    move_count++;
                }

                temp_attack = ((rookAttacks.and(EMPTY_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
              //  Pr.print("against empty:");
              //  Pr.printBigIntegerLn(temp_attack);

                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_NONE;
                    move_list[move_count][MOVE_PIECE] = GenConst.BR;
                    move_count++;
                }
            }

            //#endregion

            //#region Black bishop moves

            temp_bitboard = Board.bitboard_array_global[GenConst.BB];
            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard);
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                BigInteger bishopAttacks = MoveUtils.GetBishopMovesSeparate(COMBINED_OCCUPANCIES, starting_square);

                temp_attack = ((bishopAttacks.and(WHITE_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                    move_list[move_count][MOVE_PIECE] = GenConst.BB;
                    move_count++;
                }

                temp_attack = ((bishopAttacks.and(EMPTY_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] =  GenConst.TAG_NONE;
                    move_list[move_count][MOVE_PIECE] = GenConst.BB;
                    move_count++;
                }
            }

            //#endregion

            //#region Black queen moves

            temp_bitboard = Board.bitboard_array_global[GenConst.BQ];
            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard);
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                BigInteger queenAttacks = MoveUtils.GetRookMovesSeparate(COMBINED_OCCUPANCIES, starting_square);
                queenAttacks = queenAttacks.or(MoveUtils.GetBishopMovesSeparate(COMBINED_OCCUPANCIES, starting_square));

                temp_attack = ((queenAttacks.and(BLACK_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));


                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                    move_list[move_count][MOVE_PIECE] = GenConst.BQ;
                    move_count++;
                }

                temp_attack = ((queenAttacks.and(EMPTY_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] =  GenConst.TAG_NONE;
                    move_list[move_count][MOVE_PIECE] = GenConst.BQ;
                    move_count++;
                }
            }

            //#endregion
        }
    }

    //#endregion
        Pr.println("-generated moves, count: " + move_count);
        return move_count;
    }


    static int PerftInlineDebug(int depth, int ply)
    {
        if (depth == 0)
        {
            return 1;
        }

        BigInteger[] bitboard_array_copy = new BigInteger[12];

        for (int i = 0; i < 12; i++)
        {
            bitboard_array_copy[i] = Board.bitboard_array_global[i];
        }

        int[][] move_list = new int[250][4];
        int move_count = getMoves(move_list);

      //  Pr.print("Move count: ");
      //  Pr.printIntLn(move_count);

    //if (depth == 1)
    //{
        //return move_count;
    //}

    int nodes = 0;
    int priorNodes;
    int copyEp = Board.ep;
    
    boolean[] copy_castle = {
        Board.castle_rights_global[0],
        Board.castle_rights_global[1],
        Board.castle_rights_global[2],
        Board.castle_rights_global[3],
    };

    Pr.println("  -Looping through moves");
    for (int move_index = 0; move_index < move_count; move_index++)
    {
        int startingSquare = move_list[move_index][MOVE_STARTING];
        int targetSquare = move_list[move_index][MOVE_TARGET];
        int piece = move_list[move_index][MOVE_PIECE];
        int tag = move_list[move_index][MOVE_TAG];

        int captureIndex = makeMove(piece, tag, startingSquare, targetSquare);

        priorNodes = nodes;
        nodes += PerftInlineDebug(depth - 1, ply + 1);

        unmakeMove(piece, tag, startingSquare, targetSquare, captureIndex);

        Board.castle_rights_global[0] = copy_castle[0];
        Board.castle_rights_global[1] = copy_castle[1];
        Board.castle_rights_global[2] = copy_castle[2];
        Board.castle_rights_global[3] = copy_castle[3];
        Board.ep = copyEp;

        for (int i = 0; i < 12; i++)
        {
            Pr.println("i: " + i + " bitboard: " + Board.bitboard_array_global[i]);
            assert Board.bitboard_array_global[i] == bitboard_array_copy[i] : "Bitboards are not the same!";
        }

        if (ply == 0)
        {
            //Pr.printInt(startingSquare);
            //Pr.printInt(targetSquare);
            //Pr.printInt(tag);
            PrintMoveNoNL(startingSquare, targetSquare, tag);
            System.out.printf(": %d\n", nodes - priorNodes);
        }
    }

        return nodes;
    }



    static int PerftInlineGlobal(int depth, int ply)
    {
        if (depth == 0)
        {
            return 1;
        }

        int[][] move_list = new int[250][4];
        int move_count = 0;

        //Move generating variables
        BigInteger WHITE_OCCUPANCIES = Board.bitboard_array_global[0]
        .or(Board.bitboard_array_global[1])
        .or(Board.bitboard_array_global[2])
        .or(Board.bitboard_array_global[3])
        .or(Board.bitboard_array_global[4])
        .or(Board.bitboard_array_global[5]); 
        BigInteger BLACK_OCCUPANCIES = Board.bitboard_array_global[6]
        .or(Board.bitboard_array_global[7])
        .or(Board.bitboard_array_global[8])
        .or(Board.bitboard_array_global[9])
        .or(Board.bitboard_array_global[10])
        .or(Board.bitboard_array_global[11]);
        BigInteger COMBINED_OCCUPANCIES = WHITE_OCCUPANCIES.or(BLACK_OCCUPANCIES);
        BigInteger EMPTY_OCCUPANCIES = COMBINED_OCCUPANCIES.not(); 
        BigInteger temp_bitboard, temp_pin_bitboard, temp_attack, temp_empty, temp_captures;
        BigInteger check_bitboard = new BigInteger("0");
        int starting_square = NO_SQUARE, target_square = NO_SQUARE;

        int[][] pinArray =
        {
            { -1, -1 },
            { -1, -1 },
            { -1, -1 },
            { -1, -1 },
            { -1, -1 },
            { -1, -1 },
            { -1, -1 },
            { -1, -1 },
        };

        int pinNumber = 0;

    if (Board.is_white_global == true)
    {
        int whiteKingCheckCount = 0;
        int whiteKingPosition = BitScanForward(Board.bitboard_array_global[GenConst.WK]);

        //#region White checks and pins

        //pawns
        temp_bitboard = Board.bitboard_array_global[GenConst.BP].and(MoveConstants.WHITE_PAWN_ATTACKS[whiteKingPosition]);
        if (temp_bitboard.compareTo(BigInteger.ZERO) != 0) 
        {
            int pawn_square = BitScanForward(temp_bitboard); 

                if (check_bitboard.compareTo(BigInteger.ZERO) != 0) 
                {
                    check_bitboard = MoveConstants.SQUARE_BBS[pawn_square];
                }
            
            whiteKingCheckCount++;
        }

        //knights
        temp_bitboard = Board.bitboard_array_global[GenConst.BN].and(MoveConstants.KNIGHT_ATTACKS[whiteKingPosition]);
        if (temp_bitboard.compareTo(BigInteger.ZERO) != 0) 
        {
            int knight_square = BitScanForward(temp_bitboard);

            if (check_bitboard.compareTo(BigInteger.ZERO) != 0) 
            {
                check_bitboard = MoveConstants.SQUARE_BBS[knight_square];
            }
            
            whiteKingCheckCount++;
        }

        //bishops
        BigInteger bishopAttacksChecks = MoveUtils.GetBishopMovesSeparate(BLACK_OCCUPANCIES, whiteKingPosition);
        temp_bitboard = Board.bitboard_array_global[GenConst.BB].and(bishopAttacksChecks);
        while (temp_bitboard.compareTo(BigInteger.ZERO) != 0) 
        {
            int piece_square = BitScanForward(temp_bitboard); 
            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square].and(WHITE_OCCUPANCIES);

            if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0) 
            {
                if (check_bitboard.compareTo(BigInteger.ZERO) != 0) 
                {
                    check_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square];
                }
                whiteKingCheckCount++;
            }
            else
            {
                int pinned_square = BitScanForward(temp_pin_bitboard); 
                temp_pin_bitboard = temp_pin_bitboard.and(temp_pin_bitboard.subtract(BigInteger.ONE));

                if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0) 
                {
                    pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
                    pinNumber++;
                }
            }
            temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));
        }

        //queen
        temp_bitboard = Board.bitboard_array_global[GenConst.BQ].and(bishopAttacksChecks);
        while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
        {
            int piece_square = BitScanForward(temp_bitboard); 

            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square].and(WHITE_OCCUPANCIES);

            if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                if (check_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    check_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square];
                }
                whiteKingCheckCount++;
            }
            else
            {
                int pinned_square = BitScanForward(temp_pin_bitboard); 
                temp_pin_bitboard = temp_pin_bitboard.and(temp_pin_bitboard.subtract(BigInteger.ONE));

                if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
                    pinNumber++;
                }
            }
            temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));
        }

        //rook
        BigInteger rook_attacks = MoveUtils.GetRookMovesSeparate(BLACK_OCCUPANCIES, whiteKingPosition); 
        temp_bitboard = Board.bitboard_array_global[GenConst.BR].and(rook_attacks);
        while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
        {
            int piece_square = BitScanForward(temp_bitboard); 
            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square].and(WHITE_OCCUPANCIES);

            if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                if (check_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    check_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square];
                }
                whiteKingCheckCount++;
            }
            else
            {
                int pinned_square = BitScanForward(temp_pin_bitboard); 
                temp_pin_bitboard = temp_pin_bitboard.and(temp_pin_bitboard.subtract(BigInteger.ONE));

                if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
                    pinNumber++;
                }
            }
            temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));
        }

        //queen
        temp_bitboard = Board.bitboard_array_global[GenConst.BQ].and(rook_attacks);
        while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
        {
            int piece_square = BitScanForward(temp_bitboard); 
            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square].and(WHITE_OCCUPANCIES);

            if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                if (check_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    check_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square];
                }
                whiteKingCheckCount++;
            }
            else
            {
                int pinned_square = BitScanForward(temp_pin_bitboard); 
                temp_pin_bitboard = temp_pin_bitboard.and(temp_pin_bitboard.subtract(BigInteger.ONE));

                if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
                    pinNumber++;
                }
            }
            temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));
        }

        //#endregion

        BigInteger occupanciesWithoutWhiteKing = COMBINED_OCCUPANCIES.and(Board.bitboard_array_global[GenConst.WK].not());

        temp_attack = MoveConstants.KING_ATTACKS[whiteKingPosition];
        temp_empty = temp_attack.and(EMPTY_OCCUPANCIES);
        while (temp_empty.signum() != 0) // Using BigInteger's signum() to check if it's not 0
        {
            target_square = BitScanForward(temp_empty);
            temp_empty = temp_empty.and(temp_empty.subtract(BigInteger.ONE)); // Using BigInteger's subtract and and
        
            if (Board.bitboard_array_global[GenConst.BP].and(MoveConstants.WHITE_PAWN_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.BN].and(MoveConstants.KNIGHT_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.BK].and(MoveConstants.KING_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            BigInteger bishopAttacks = MoveUtils.GetBishopMovesSeparate(occupanciesWithoutWhiteKing, target_square);
            if (Board.bitboard_array_global[GenConst.BB].and(bishopAttacks).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.BQ].and(bishopAttacks).signum() != 0) {
                continue;
            }
            BigInteger rookAttacks = MoveUtils.GetRookMovesSeparate(occupanciesWithoutWhiteKing, target_square); 
            if (Board.bitboard_array_global[GenConst.BR].and(rookAttacks).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.BQ].and(rookAttacks).signum() != 0) {
                continue;
            }
        
            move_list[move_count][MOVE_STARTING] = whiteKingPosition;
            move_list[move_count][MOVE_TARGET] = target_square;
            move_list[move_count][MOVE_TAG] = GenConst.TAG_NONE;
            move_list[move_count][MOVE_PIECE] = GenConst.WK;
            move_count++;
        }

        //captures
        temp_captures = temp_attack.and(BLACK_OCCUPANCIES);
        while (temp_captures.signum() != 0) // Using BigInteger's signum() to check if it's not 0
        {
            target_square = BitScanForward(temp_captures);
            temp_captures = temp_captures.and(temp_captures.subtract(BigInteger.ONE)); // Using BigInteger's subtract and and
        
            if (Board.bitboard_array_global[GenConst.BP].and(MoveConstants.WHITE_PAWN_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.BN].and(MoveConstants.KNIGHT_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.BK].and(MoveConstants.KING_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            BigInteger bishopAttacks = MoveUtils.GetBishopMovesSeparate(occupanciesWithoutWhiteKing, target_square);
            if (Board.bitboard_array_global[GenConst.BB].and(bishopAttacks).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.BQ].and(bishopAttacks).signum() != 0) {
                continue;
            }
            BigInteger rookAttacks = MoveUtils.GetRookMovesSeparate(occupanciesWithoutWhiteKing, target_square); 
            if (Board.bitboard_array_global[GenConst.BR].and(rookAttacks).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.BQ].and(rookAttacks).signum() != 0) {
                continue;
            }
        
            move_list[move_count][MOVE_STARTING] = whiteKingPosition;
            move_list[move_count][MOVE_TARGET] = target_square;
            move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
            move_list[move_count][MOVE_PIECE] = GenConst.WK;
            move_count++;
        }

        if (whiteKingCheckCount < 2)
        {

            if (whiteKingCheckCount == 0)
            {
                check_bitboard = MAX_ULONG;
            }

            //#region White castling

            if (whiteKingCheckCount == 0)
            {
                if (Board.castle_rights_global[WKS_CASTLE_RIGHTS] == true)
                {
                    if (whiteKingPosition == GenConst.E1) //king on e1
                    {
                        if (isNotZero(WKS_EMPTY_BITBOARD.and(COMBINED_OCCUPANCIES)) == false) //f1 and g1 empty
                        {
                            if (isNotZero(Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[GenConst.H1])) == false) //rook on h1
                            {
                                if (MoveUtils.Is_Square_Attacked_By_Black_Global(GenConst.F1, COMBINED_OCCUPANCIES) == false)
                                {
                                    if (MoveUtils.Is_Square_Attacked_By_Black_Global(GenConst.G1, COMBINED_OCCUPANCIES) == false)
                                    {
                                        move_list[move_count][MOVE_STARTING] = GenConst.E1;
                                        move_list[move_count][MOVE_TARGET] = GenConst.G1;
                                        move_list[move_count][MOVE_TAG] = GenConst.TAG_WCASTLEKS;
                                        move_list[move_count][MOVE_PIECE] = GenConst.WK;
                                        move_count++;
                                    }
                                }
                            }
                        }
                    }
                }
                if (Board.castle_rights_global[WQS_CASTLE_RIGHTS] == true)
                {
                    if (whiteKingPosition == GenConst.E1) //king on e1
                    {
                        if (isNotZero(WQS_EMPTY_BITBOARD.and(COMBINED_OCCUPANCIES)) == false) //f1 and g1 empty
                        {
                            if ((isNotZero(Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[GenConst.A1]))) == true) //rook on h1
                            {
                                if (MoveUtils.Is_Square_Attacked_By_Black_Global(GenConst.C1, COMBINED_OCCUPANCIES) == false)
                                {
                                    if (MoveUtils.Is_Square_Attacked_By_Black_Global(GenConst.D1, COMBINED_OCCUPANCIES) == false)
                                    {
                                        move_list[move_count][MOVE_STARTING] = GenConst.E1;
                                        move_list[move_count][MOVE_TARGET] = GenConst.C1;
                                        move_list[move_count][MOVE_TAG] = GenConst.TAG_WCASTLEQS;
                                        move_list[move_count][MOVE_PIECE] = GenConst.WK;
                                        move_count++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //#endregion

            //#region White knight moves

            temp_bitboard = Board.bitboard_array_global[GenConst.WN];

            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard);
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE)); //removes the knight from that square to not infinitely loop

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                temp_attack = ((MoveConstants.KNIGHT_ATTACKS[starting_square].and(BLACK_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard))); //gets knight captures
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);
                    
                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                    move_list[move_count][MOVE_PIECE] = GenConst.WN;
                    move_count++;
                }

                temp_attack = ((MoveConstants.KNIGHT_ATTACKS[starting_square].and(EMPTY_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));

                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] =  GenConst.TAG_NONE;
                    move_list[move_count][MOVE_PIECE] = GenConst.WN;
                    move_count++;
                }
            }

            //#endregion

            //#region White pawn moves

            temp_bitboard = Board.bitboard_array_global[GenConst.WP];

            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard); 
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                //#region pawn forward

                if (isZero(MoveConstants.SQUARE_BBS[starting_square - 8].and(COMBINED_OCCUPANCIES)) == true) //if up one square is empty
                {
                    if (isNotZero(MoveConstants.SQUARE_BBS[starting_square - 8].and(check_bitboard).and(temp_pin_bitboard)) == true) //if not pinned or check
                    {
                        if (isNotZero(MoveConstants.SQUARE_BBS[starting_square].and(RANK_7_BITBOARD))) //if promotion
                        {
                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square - 8;
                            move_list[move_count][MOVE_TAG] = GenConst.TAG_WQueenPromotion;
                            move_list[move_count][MOVE_PIECE] = GenConst.WP;
                            move_count++;

                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square - 8;
                            move_list[move_count][MOVE_TAG] = GenConst.TAG_WRookPromotion;
                            move_list[move_count][MOVE_PIECE] = GenConst.WP;
                            move_count++;

                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square - 8;
                            move_list[move_count][MOVE_TAG] = GenConst.TAG_WBishopPromotion;
                            move_list[move_count][MOVE_PIECE] = GenConst.WP;
                            move_count++;

                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square - 8;
                            move_list[move_count][MOVE_TAG] = GenConst.TAG_WBishopPromotion;
                            move_list[move_count][MOVE_PIECE] = GenConst.WP;
                            move_count++;

                        }
                        else
                        {
                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square - 8;
                            move_list[move_count][MOVE_TAG] =  GenConst.TAG_NONE;
                            move_list[move_count][MOVE_PIECE] = GenConst.WP;
                            move_count++;
                        }
                    }

                    if ((isNotZero(MoveConstants.SQUARE_BBS[starting_square].and(RANK_2_BITBOARD))) == true) //if on rank 2
                    {
                        if (isNotZero(MoveConstants.SQUARE_BBS[starting_square - 16].and(check_bitboard).and(temp_pin_bitboard)) == true) //if not pinned or 
                        {
                            if (isZero(MoveConstants.SQUARE_BBS[starting_square - 16].and(COMBINED_OCCUPANCIES)) == true) //if up two squares and one square are empty
                            {
                                move_list[move_count][MOVE_STARTING] = starting_square;
                                move_list[move_count][MOVE_TARGET] = starting_square - 16;
                                move_list[move_count][MOVE_TAG] = GenConst.TAG_DoublePawnWhite;
                                move_list[move_count][MOVE_PIECE] = GenConst.WP;
                                move_count++;
                            }
                        }
                    }
                }

                //#endregion
                //#region pawn attacks

                temp_attack = ((MoveConstants.WHITE_PAWN_ATTACKS[starting_square].and(BLACK_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard))); //if black piece diagonal to pawn

                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    if (isNotZero(MoveConstants.SQUARE_BBS[starting_square].and(RANK_7_BITBOARD)) == true) //if promotion
                    {
                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_WCaptureQueenPromotion;
                        move_list[move_count][MOVE_PIECE] = GenConst.WP;
                        move_count++;

                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_WCaptureRookPromotion;
                        move_list[move_count][MOVE_PIECE] = GenConst.WP;
                        move_count++;

                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_WCaptureBishopPromotion;
                        move_list[move_count][MOVE_PIECE] = GenConst.WP;
                        move_count++;

                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_WCaptureKnightPromotion;
                        move_list[move_count][MOVE_PIECE] = GenConst.WP;
                        move_count++;
                    }
                    else
                    {
                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                        move_list[move_count][MOVE_PIECE] = GenConst.WP;
                        move_count++;
                    }
                }

                if (isNotZero(MoveConstants.SQUARE_BBS[starting_square].and(RANK_5_BITBOARD))) 
                { // check rank for ep
                    if (Board.ep != NO_SQUARE) 
                    {
                        if (isNotZero(MoveConstants.WHITE_PAWN_ATTACKS[starting_square]
                                .and(MoveConstants.SQUARE_BBS[Board.ep])
                                .and(check_bitboard)
                                .and(temp_pin_bitboard))) {
                            if (isZero(Board.bitboard_array_global[GenConst.WK].and(RANK_5_BITBOARD))) { // if no king on rank 5
                                move_list[move_count][MOVE_STARTING] = starting_square;
                                move_list[move_count][MOVE_TARGET] = Board.ep;
                                move_list[move_count][MOVE_TAG] = GenConst.TAG_WHITEEP;
                                move_list[move_count][MOVE_PIECE] = GenConst.WP;
                                move_count++;
                            } else if (isZero(Board.bitboard_array_global[GenConst.BR].and(RANK_5_BITBOARD)) &&
                                       isZero(Board.bitboard_array_global[GenConst.BQ].and(RANK_5_BITBOARD))) { // if no b rook or queen on rank 5
                                move_list[move_count][MOVE_STARTING] = starting_square;
                                move_list[move_count][MOVE_TARGET] = Board.ep;
                                move_list[move_count][MOVE_TAG] = GenConst.TAG_WHITEEP;
                                move_list[move_count][MOVE_PIECE] = GenConst.WP;
                                move_count++;
                            } else { // wk and br or bq on rank 5
                                BigInteger occupancyWithoutEPPawns = COMBINED_OCCUPANCIES.and(MoveConstants.SQUARE_BBS[starting_square].not());
                                occupancyWithoutEPPawns = occupancyWithoutEPPawns.and(MoveConstants.SQUARE_BBS[Board.ep + 8].not());
                
                                BigInteger rookAttacksFromKing = MoveUtils.GetRookMovesSeparate(occupancyWithoutEPPawns, whiteKingPosition);
                
                                if (isZero(rookAttacksFromKing.and(Board.bitboard_array_global[GenConst.BR]))) 
                                {
                                    if (isZero(rookAttacksFromKing.and(Board.bitboard_array_global[GenConst.BQ]))) 
                                    {
                                        move_list[move_count][MOVE_STARTING] = starting_square;
                                        move_list[move_count][MOVE_TARGET] = Board.ep;
                                        move_list[move_count][MOVE_TAG] = GenConst.TAG_WHITEEP;
                                        move_list[move_count][MOVE_PIECE] = GenConst.WP;
                                        move_count++;
                                    }
                                }
                            }
                        }
                    }
                }

                //#endregion
            }

            //#endregion

            //#region White rook moves

            temp_bitboard = Board.bitboard_array_global[GenConst.WR];
            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard);
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                BigInteger rookAttacks = MoveUtils.GetRookMovesSeparate(COMBINED_OCCUPANCIES, starting_square);

                temp_attack = ((rookAttacks.and(BLACK_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                    move_list[move_count][MOVE_PIECE] = GenConst.WR;
                    move_count++;
                }

                temp_attack = ((rookAttacks.and(EMPTY_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_NONE;
                    move_list[move_count][MOVE_PIECE] = GenConst.WR;
                    move_count++;
                }
            }

            //#endregion

            //#region White bishop moves

            //Pr.println("\nwhite bishop");
            temp_bitboard = Board.bitboard_array_global[GenConst.WB];
            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard);
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                //Pr.println("get bishop attacks");
                BigInteger bishopAttacks = MoveUtils.GetBishopMovesSeparate(COMBINED_OCCUPANCIES, starting_square);
                //Pr.println("bishop attacks");
                //Pr.printBigIntegerLn(bishopAttacks);

                temp_attack = ((bishopAttacks.and(BLACK_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                    move_list[move_count][MOVE_PIECE] = GenConst.WB;
                    move_count++;
                }

                temp_attack = ((bishopAttacks.and(EMPTY_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] =  GenConst.TAG_NONE;
                    move_list[move_count][MOVE_PIECE] = GenConst.WB;
                    move_count++;
                }
            }

            //#endregion

            //#region White queen moves

            temp_bitboard = Board.bitboard_array_global[GenConst.WQ];
            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard);
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                BigInteger queenAttacks = MoveUtils.GetRookMovesSeparate(COMBINED_OCCUPANCIES, starting_square);
                queenAttacks = queenAttacks.and(MoveUtils.GetBishopMovesSeparate(COMBINED_OCCUPANCIES, starting_square));

                temp_attack = ((queenAttacks.and(BLACK_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));

                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                    move_list[move_count][MOVE_PIECE] = GenConst.WQ;
                    move_count++;
                }

                temp_attack = ((queenAttacks.and(EMPTY_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] =  GenConst.TAG_NONE;
                    move_list[move_count][MOVE_PIECE] = GenConst.WQ;
                    move_count++;
                }
            }

            //#endregion
        }
    }
    else //black move
    {
        int blackKingCheckCount = 0;
        int blackKingPosition = BitScanForward(Board.bitboard_array_global[GenConst.BK]);

        //#region black checks and pins

        //pawns
        temp_bitboard = Board.bitboard_array_global[GenConst.WP].and(MoveConstants.BLACK_PAWN_ATTACKS[blackKingPosition]);
        if (temp_bitboard.compareTo(BigInteger.ZERO) != 0) //if there is pawn attacking king
        {
            int pawn_square = BitScanForward(temp_bitboard); 

            if (check_bitboard.compareTo(BigInteger.ZERO) != 0) 
            {
                check_bitboard = MoveConstants.SQUARE_BBS[pawn_square];
            }
            
            blackKingCheckCount++;
        }

        //knights
        temp_bitboard = Board.bitboard_array_global[GenConst.WN].and(MoveConstants.KNIGHT_ATTACKS[blackKingPosition]);
        if (temp_bitboard.compareTo(BigInteger.ZERO) != 0) 
        {
            int knight_square = BitScanForward(temp_bitboard);

            if (check_bitboard.compareTo(BigInteger.ZERO) != 0) 
            {
                check_bitboard = MoveConstants.SQUARE_BBS[knight_square];
            }
            
            blackKingCheckCount++;
        }

        //bishops
        BigInteger bishopAttacksChecks = MoveUtils.GetBishopMovesSeparate(WHITE_OCCUPANCIES, blackKingPosition);
        temp_bitboard = Board.bitboard_array_global[GenConst.WB].and(bishopAttacksChecks);
        while (temp_bitboard.compareTo(BigInteger.ZERO) != 0) 
        {
            int piece_square = BitScanForward(temp_bitboard); 
            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][piece_square].and(BLACK_OCCUPANCIES);

            if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0) 
            {
                if (check_bitboard.compareTo(BigInteger.ZERO) != 0) 
                {
                    check_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][piece_square];
                }
                blackKingCheckCount++;
            }
            else
            {
                int pinned_square = BitScanForward(temp_pin_bitboard); 
                temp_pin_bitboard = temp_pin_bitboard.and(temp_pin_bitboard.subtract(BigInteger.ONE));

                if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0) 
                {
                    pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
                    pinNumber++;
                }
            }
            temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));
        }

        //queen
        temp_bitboard = Board.bitboard_array_global[GenConst.WQ].and(bishopAttacksChecks);
        while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
        {
            int piece_square = BitScanForward(temp_bitboard); 

            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][piece_square].and(BLACK_OCCUPANCIES);

            if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                if (check_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    check_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][piece_square];
                }
                blackKingCheckCount++;
            }
            else
            {
                int pinned_square = BitScanForward(temp_pin_bitboard); 
                temp_pin_bitboard = temp_pin_bitboard.and(temp_pin_bitboard.subtract(BigInteger.ONE));

                if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
                    pinNumber++;
                }
            }
            temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));
        }

        //rook
        BigInteger rook_attacks = MoveUtils.GetRookMovesSeparate(WHITE_OCCUPANCIES, blackKingPosition); 
        temp_bitboard = Board.bitboard_array_global[GenConst.WR].and(rook_attacks);
        while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
        {
            int piece_square = BitScanForward(temp_bitboard); 
            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][piece_square].and(BLACK_OCCUPANCIES);

            if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                if (check_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    check_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][piece_square];
                }
                blackKingCheckCount++;
            }
            else
            {
                int pinned_square = BitScanForward(temp_pin_bitboard); 
                temp_pin_bitboard = temp_pin_bitboard.and(temp_pin_bitboard.subtract(BigInteger.ONE));

                if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
                    pinNumber++;
                }
            }
            temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));
        }

        //queen
        temp_bitboard = Board.bitboard_array_global[GenConst.WQ].and(rook_attacks);
        while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
        {
            int piece_square = BitScanForward(temp_bitboard); 
            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][piece_square].and(WHITE_OCCUPANCIES);

            if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                if (check_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    check_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][piece_square];
                }
                blackKingCheckCount++;
            }
            else
            {
                int pinned_square = BitScanForward(temp_pin_bitboard); 
                temp_pin_bitboard = temp_pin_bitboard.and(temp_pin_bitboard.subtract(BigInteger.ONE));

                if (temp_pin_bitboard.compareTo(BigInteger.ZERO) != 0)
                {
                    pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
                    pinNumber++;
                }
            }
            temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));
        }

        //#endregion

        BigInteger occupanciesWithoutWhiteKing = COMBINED_OCCUPANCIES.and(Board.bitboard_array_global[GenConst.WK].not());

        temp_attack = MoveConstants.KING_ATTACKS[blackKingPosition];
        temp_empty = temp_attack.and(EMPTY_OCCUPANCIES);
        while (temp_empty.signum() != 0) // Using BigInteger's signum() to check if it's not 0
        {
            target_square = BitScanForward(temp_empty);
            temp_empty = temp_empty.and(temp_empty.subtract(BigInteger.ONE)); // Using BigInteger's subtract and and
        
            if (Board.bitboard_array_global[GenConst.WP].and(MoveConstants.BLACK_PAWN_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.WN].and(MoveConstants.KNIGHT_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.WK].and(MoveConstants.KING_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            BigInteger bishopAttacks = MoveUtils.GetBishopMovesSeparate(occupanciesWithoutWhiteKing, target_square);
            if (Board.bitboard_array_global[GenConst.WB].and(bishopAttacks).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.WQ].and(bishopAttacks).signum() != 0) {
                continue;
            }
            BigInteger rookAttacks = MoveUtils.GetRookMovesSeparate(occupanciesWithoutWhiteKing, target_square); 
            if (Board.bitboard_array_global[GenConst.WR].and(rookAttacks).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.WQ].and(rookAttacks).signum() != 0) {
                continue;
            }
        
            move_list[move_count][MOVE_STARTING] = blackKingPosition;
            move_list[move_count][MOVE_TARGET] = target_square;
            move_list[move_count][MOVE_TAG] = GenConst.TAG_NONE;
            move_list[move_count][MOVE_PIECE] = GenConst.BK;
            move_count++;
        }

        //captures
        temp_captures = temp_attack.and(BLACK_OCCUPANCIES);
        while (temp_captures.signum() != 0) // Using BigInteger's signum() to check if it's not 0
        {
            target_square = BitScanForward(temp_captures);
            temp_captures = temp_captures.and(temp_captures.subtract(BigInteger.ONE)); // Using BigInteger's subtract and and
        
            if (Board.bitboard_array_global[GenConst.WP].and(MoveConstants.BLACK_PAWN_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.WN].and(MoveConstants.KNIGHT_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.WK].and(MoveConstants.KING_ATTACKS[target_square]).signum() != 0) {
                continue;
            }
            BigInteger bishopAttacks = MoveUtils.GetBishopMovesSeparate(occupanciesWithoutWhiteKing, target_square);
            if (Board.bitboard_array_global[GenConst.WB].and(bishopAttacks).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.WQ].and(bishopAttacks).signum() != 0) {
                continue;
            }
            BigInteger rookAttacks = MoveUtils.GetRookMovesSeparate(occupanciesWithoutWhiteKing, target_square); 
            if (Board.bitboard_array_global[GenConst.WR].and(rookAttacks).signum() != 0) {
                continue;
            }
            if (Board.bitboard_array_global[GenConst.WQ].and(rookAttacks).signum() != 0) {
                continue;
            }
        
            move_list[move_count][MOVE_STARTING] = blackKingPosition;
            move_list[move_count][MOVE_TARGET] = target_square;
            move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
            move_list[move_count][MOVE_PIECE] = GenConst.BK;
            move_count++;
        }

        if (blackKingCheckCount < 2)
        {

            if (blackKingCheckCount == 0)
            {
                check_bitboard = MAX_ULONG;
            }

            //#region Black castling

            if (blackKingCheckCount == 0)
            {
                if (Board.castle_rights_global[BKS_CASTLE_RIGHTS] == true)
                {
                    if (blackKingPosition == GenConst.E8) //king on e1
                    {
                        if (isNotZero(BKS_EMPTY_BITBOARD.and(COMBINED_OCCUPANCIES)) == false) //f1 and g1 empty
                        {
                            if (isNotZero(Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[GenConst.H8])) == false) //rook on h8
                            {
                                if (MoveUtils.Is_Square_Attacked_By_White_Global(GenConst.F8, COMBINED_OCCUPANCIES) == false)
                                {
                                    if (MoveUtils.Is_Square_Attacked_By_White_Global(GenConst.G8, COMBINED_OCCUPANCIES) == false)
                                    {
                                        move_list[move_count][MOVE_STARTING] = GenConst.E8;
                                        move_list[move_count][MOVE_TARGET] = GenConst.G8;
                                        move_list[move_count][MOVE_TAG] = GenConst.TAG_BCASTLEKS;
                                        move_list[move_count][MOVE_PIECE] = GenConst.BK;
                                        move_count++;
                                    }
                                }
                            }
                        }
                    }
                }
                if (Board.castle_rights_global[BQS_CASTLE_RIGHTS] == true)
                {
                    if (blackKingPosition == GenConst.E8) //king on e1
                    {
                        if (isNotZero(BQS_EMPTY_BITBOARD.and(COMBINED_OCCUPANCIES)) == false) //f1 and g1 empty
                        {
                            if ((isNotZero(Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[GenConst.A8]))) == true) //rook on h1
                            {
                                if (MoveUtils.Is_Square_Attacked_By_White_Global(GenConst.C8, COMBINED_OCCUPANCIES) == false)
                                {
                                    if (MoveUtils.Is_Square_Attacked_By_White_Global(GenConst.D8, COMBINED_OCCUPANCIES) == false)
                                    {
                                        move_list[move_count][MOVE_STARTING] = GenConst.E8;
                                        move_list[move_count][MOVE_TARGET] = GenConst.C8;
                                        move_list[move_count][MOVE_TAG] = GenConst.TAG_BCASTLEQS;
                                        move_list[move_count][MOVE_PIECE] = GenConst.BK;
                                        move_count++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //#endregion

            //#region Black knight moves

            temp_bitboard = Board.bitboard_array_global[GenConst.BN];

            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard);
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE)); //removes the knight from that square to not infinitely loop

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                temp_attack = ((MoveConstants.KNIGHT_ATTACKS[starting_square].and(WHITE_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard))); //gets knight captures
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);
                    
                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                    move_list[move_count][MOVE_PIECE] = GenConst.BN;
                    move_count++;
                }

                temp_attack = ((MoveConstants.KNIGHT_ATTACKS[starting_square].and(EMPTY_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));

                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] =  GenConst.TAG_NONE;
                    move_list[move_count][MOVE_PIECE] = GenConst.BN;
                    move_count++;
                }
            }

            //#endregion

            //#region Black pawn moves

            temp_bitboard = Board.bitboard_array_global[GenConst.BP];

            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard); 
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                //#region pawn forward

                if (isZero(MoveConstants.SQUARE_BBS[starting_square + 8].and(COMBINED_OCCUPANCIES)) == true) //if up one square is empty
                {
                    if (isNotZero(MoveConstants.SQUARE_BBS[starting_square + 8].and(check_bitboard).and(temp_pin_bitboard)) == true) //if not pinned or check
                    {
                        if (isNotZero(MoveConstants.SQUARE_BBS[starting_square].and(RANK_2_BITBOARD))) //if promotion
                        {
                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square + 8;
                            move_list[move_count][MOVE_TAG] = GenConst.TAG_BQueenPromotion;
                            move_list[move_count][MOVE_PIECE] = GenConst.BP;
                            move_count++;

                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square + 8;
                            move_list[move_count][MOVE_TAG] = GenConst.TAG_BRookPromotion;
                            move_list[move_count][MOVE_PIECE] = GenConst.BP;
                            move_count++;

                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square + 8;
                            move_list[move_count][MOVE_TAG] = GenConst.TAG_BBishopPromotion;
                            move_list[move_count][MOVE_PIECE] = GenConst.BP;
                            move_count++;

                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square + 8;
                            move_list[move_count][MOVE_TAG] = GenConst.TAG_BBishopPromotion;
                            move_list[move_count][MOVE_PIECE] = GenConst.BP;
                            move_count++;

                        }
                        else
                        {
                            move_list[move_count][MOVE_STARTING] = starting_square;
                            move_list[move_count][MOVE_TARGET] = starting_square + 8;
                            move_list[move_count][MOVE_TAG] =  GenConst.TAG_NONE;
                            move_list[move_count][MOVE_PIECE] = GenConst.BP;
                            move_count++;
                        }
                    }

                    if ((isNotZero(MoveConstants.SQUARE_BBS[starting_square].and(RANK_7_BITBOARD))) == true) //if on rank 7
                    {
                        if (isNotZero(MoveConstants.SQUARE_BBS[starting_square + 16].and(check_bitboard).and(temp_pin_bitboard)) == true) //if not pinned or 
                        {
                            if (isZero(MoveConstants.SQUARE_BBS[starting_square + 16].and(COMBINED_OCCUPANCIES)) == true) //if up two squares and one square are empty
                            {
                                move_list[move_count][MOVE_STARTING] = starting_square;
                                move_list[move_count][MOVE_TARGET] = starting_square - 16;
                                move_list[move_count][MOVE_TAG] = GenConst.TAG_DoublePawnBlack;
                                move_list[move_count][MOVE_PIECE] = GenConst.BP;
                                move_count++;
                            }
                        }
                    }
                }

                //#endregion
                //#region pawn attacks

                temp_attack = ((MoveConstants.BLACK_PAWN_ATTACKS[starting_square].and(BLACK_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard))); //if black piece diagonal to pawn

                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    if (isNotZero(MoveConstants.SQUARE_BBS[starting_square].and(RANK_2_BITBOARD)) == true) //if promotion
                    {
                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_BCaptureQueenPromotion;
                        move_list[move_count][MOVE_PIECE] = GenConst.BP;
                        move_count++;

                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_BCaptureRookPromotion;
                        move_list[move_count][MOVE_PIECE] = GenConst.BP;
                        move_count++;

                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_BCaptureBishopPromotion;
                        move_list[move_count][MOVE_PIECE] = GenConst.BP;
                        move_count++;

                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_BCaptureKnightPromotion;
                        move_list[move_count][MOVE_PIECE] = GenConst.BP;
                        move_count++;
                    }
                    else
                    {
                        move_list[move_count][MOVE_STARTING] = starting_square;
                        move_list[move_count][MOVE_TARGET] = target_square;
                        move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                        move_list[move_count][MOVE_PIECE] = GenConst.BP;
                        move_count++;
                    }
                }

                if (isNotZero(MoveConstants.SQUARE_BBS[starting_square].and(RANK_4_BITBOARD))) 
                { // check rank for ep
                    if (Board.ep != NO_SQUARE) 
                    {
                        if (isNotZero(MoveConstants.BLACK_PAWN_ATTACKS[starting_square]
                                .and(MoveConstants.SQUARE_BBS[Board.ep])
                                .and(check_bitboard)
                                .and(temp_pin_bitboard))) {
                            if (isZero(Board.bitboard_array_global[GenConst.WK].and(RANK_5_BITBOARD))) { // if no king on rank 5
                                move_list[move_count][MOVE_STARTING] = starting_square;
                                move_list[move_count][MOVE_TARGET] = Board.ep;
                                move_list[move_count][MOVE_TAG] = GenConst.TAG_BLACKEP;
                                move_list[move_count][MOVE_PIECE] = GenConst.BP;
                                move_count++;
                            } else if (isZero(Board.bitboard_array_global[GenConst.WR].and(RANK_5_BITBOARD)) &&
                                       isZero(Board.bitboard_array_global[GenConst.WQ].and(RANK_5_BITBOARD))) { // if no b rook or queen on rank 5
                                move_list[move_count][MOVE_STARTING] = starting_square;
                                move_list[move_count][MOVE_TARGET] = Board.ep;
                                move_list[move_count][MOVE_TAG] = GenConst.TAG_BLACKEP;
                                move_list[move_count][MOVE_PIECE] = GenConst.BP;
                                move_count++;
                            } else { // wk and br or bq on rank 5
                                BigInteger occupancyWithoutEPPawns = COMBINED_OCCUPANCIES.and(MoveConstants.SQUARE_BBS[starting_square].not());
                                occupancyWithoutEPPawns = occupancyWithoutEPPawns.and(MoveConstants.SQUARE_BBS[Board.ep - 8].not());
                
                                BigInteger rookAttacksFromKing = MoveUtils.GetRookMovesSeparate(occupancyWithoutEPPawns, blackKingPosition);
                
                                if (isZero(rookAttacksFromKing.and(Board.bitboard_array_global[GenConst.WR]))) 
                                {
                                    if (isZero(rookAttacksFromKing.and(Board.bitboard_array_global[GenConst.WQ]))) 
                                    {
                                        move_list[move_count][MOVE_STARTING] = starting_square;
                                        move_list[move_count][MOVE_TARGET] = Board.ep;
                                        move_list[move_count][MOVE_TAG] = GenConst.TAG_BLACKEP;
                                        move_list[move_count][MOVE_PIECE] = GenConst.BP;
                                        move_count++;
                                    }
                                }
                            }
                        }
                    }
                }

                //#endregion
            }

            //#endregion

            //#region Black rook moves

            temp_bitboard = Board.bitboard_array_global[GenConst.BR];
            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard);
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                BigInteger rookAttacks = MoveUtils.GetRookMovesSeparate(COMBINED_OCCUPANCIES, starting_square);

                temp_attack = ((rookAttacks.and(WHITE_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                    move_list[move_count][MOVE_PIECE] = GenConst.BR;
                    move_count++;
                }

                temp_attack = ((rookAttacks.and(EMPTY_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_NONE;
                    move_list[move_count][MOVE_PIECE] = GenConst.BR;
                    move_count++;
                }
            }

            //#endregion

            //#region Black bishop moves

            temp_bitboard = Board.bitboard_array_global[GenConst.BB];
            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard);
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                BigInteger bishopAttacks = MoveUtils.GetBishopMovesSeparate(COMBINED_OCCUPANCIES, starting_square);

                temp_attack = ((bishopAttacks.and(WHITE_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                    move_list[move_count][MOVE_PIECE] = GenConst.BB;
                    move_count++;
                }

                temp_attack = ((bishopAttacks.and(EMPTY_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] =  GenConst.TAG_NONE;
                    move_list[move_count][MOVE_PIECE] = GenConst.BB;
                    move_count++;
                }
            }

            //#endregion

            //#region Black queen moves

            temp_bitboard = Board.bitboard_array_global[GenConst.BQ];
            while (temp_bitboard.compareTo(BigInteger.ZERO) != 0)
            {
                starting_square = BitScanForward(temp_bitboard);
                temp_bitboard = temp_bitboard.and(temp_bitboard.subtract(BigInteger.ONE));

                temp_pin_bitboard = MAX_ULONG;
                if (pinNumber != 0)
                {
                    for (int i = 0; i < pinNumber; i++)
                    {
                        if (pinArray[i][PINNED_SQUARE_INDEX] == starting_square)
                        {
                            temp_pin_bitboard = Inb.INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
                        }
                    }
                }

                BigInteger queenAttacks = MoveUtils.GetRookMovesSeparate(COMBINED_OCCUPANCIES, starting_square);
                queenAttacks = queenAttacks.and(MoveUtils.GetBishopMovesSeparate(COMBINED_OCCUPANCIES, starting_square));

                temp_attack = ((queenAttacks.and(BLACK_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));

                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] = GenConst.TAG_CAPTURE;
                    move_list[move_count][MOVE_PIECE] = GenConst.BQ;
                    move_count++;
                }

                temp_attack = ((queenAttacks.and(EMPTY_OCCUPANCIES).and(check_bitboard).and(temp_pin_bitboard)));
                while (isNotZero(temp_attack))
                {
                    target_square = BitScanForward(temp_attack);
                    temp_attack = removeBit(temp_attack);

                    move_list[move_count][MOVE_STARTING] = starting_square;
                    move_list[move_count][MOVE_TARGET] = target_square;
                    move_list[move_count][MOVE_TAG] =  GenConst.TAG_NONE;
                    move_list[move_count][MOVE_PIECE] = GenConst.BQ;
                    move_count++;
                }
            }

            //#endregion
        }
    }

    //if (depth == 1)
    //{
        //return move_count;
    //}

    int nodes = 0;
    int priorNodes;
    int copyEp = Board.ep;
    
    boolean[] copy_castle = {
        Board.castle_rights_global[0],
        Board.castle_rights_global[1],
        Board.castle_rights_global[2],
        Board.castle_rights_global[3],
    };

    for (int move_index = 0; move_index < move_count; move_index++)
    {
        int startingSquare = move_list[move_index][MOVE_STARTING];
        int targetSquare = move_list[move_index][MOVE_TARGET];
        int piece = move_list[move_index][MOVE_PIECE];
        int tag = move_list[move_index][MOVE_TAG];

        int captureIndex = -1;

        Board.is_white_global = !Board.is_white_global;
        switch (tag)
        {
        case GenConst.TAG_NONE: //none
        case GenConst.TAG_CHECK: //check
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_CAPTURE: //capture
        case GenConst.TAG_CHECK_CAPTURE: //check cap
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            if (piece >= GenConst.WP && piece <= GenConst.WK)
            {
                for (int i = GenConst.BP; i <= GenConst.WP; ++i)
                {
                    if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                    {
                        captureIndex = i;
                        break;
                    }
                }
                Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            }
            else //is black
            {
                for (int i = GenConst.WP; i <= GenConst.WK; ++i)
                {
                    if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                    {
                        captureIndex = i;
                        break;
                    }
                }
                Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            }

            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_WHITEEP: //white ep
            //move piece
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].and(MoveConstants.SQUARE_BBS[targetSquare + 8].not());
            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_BLACKEP: //black ep
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].and(MoveConstants.SQUARE_BBS[targetSquare - 8].not());
            Board.ep = NO_SQUARE;
            break;

        case GenConst.TAG_WCASTLEKS: //WKS
            //white king
            Board.bitboard_array_global[GenConst.WK] =Board.bitboard_array_global[GenConst.WK].or(MoveConstants.SQUARE_BBS[GenConst.G1]);
            Board.bitboard_array_global[GenConst.WK] =Board.bitboard_array_global[GenConst.WK].and(MoveConstants.SQUARE_BBS[GenConst.E1].not());
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].or(MoveConstants.SQUARE_BBS[GenConst.F1]);
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[GenConst.H1].not());

            Board.castle_rights_global[WKS_CASTLE_RIGHTS] = false;
            Board.castle_rights_global[WQS_CASTLE_RIGHTS] = false;
            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_WCASTLEQS: //WQS
            Board.bitboard_array_global[GenConst.WK] =Board.bitboard_array_global[GenConst.WK].or(MoveConstants.SQUARE_BBS[GenConst.C1]);
            Board.bitboard_array_global[GenConst.WK] =Board.bitboard_array_global[GenConst.WK].and(MoveConstants.SQUARE_BBS[GenConst.E1].not());
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].or(MoveConstants.SQUARE_BBS[GenConst.D1]);
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[GenConst.A1].not());

            Board.castle_rights_global[WKS_CASTLE_RIGHTS] = false;
            Board.castle_rights_global[WQS_CASTLE_RIGHTS] = false;
            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_BCASTLEKS: //BKS
            Board.bitboard_array_global[GenConst.BK] =Board.bitboard_array_global[GenConst.BK].or(MoveConstants.SQUARE_BBS[GenConst.G8]);
            Board.bitboard_array_global[GenConst.BK] =Board.bitboard_array_global[GenConst.BK].and(MoveConstants.SQUARE_BBS[GenConst.E8].not());
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].or(MoveConstants.SQUARE_BBS[GenConst.F8]);
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[GenConst.H8].not());

            Board.castle_rights_global[BKS_CASTLE_RIGHTS] = false;
            Board.castle_rights_global[BQS_CASTLE_RIGHTS] = false;
            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_BCASTLEQS: //BQS
            Board.bitboard_array_global[GenConst.BK] =Board.bitboard_array_global[GenConst.BK].or(MoveConstants.SQUARE_BBS[GenConst.C8]);
            Board.bitboard_array_global[GenConst.BK] =Board.bitboard_array_global[GenConst.BK].and(MoveConstants.SQUARE_BBS[GenConst.E8].not());
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].or(MoveConstants.SQUARE_BBS[GenConst.D8]);
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[GenConst.A8].not());

            Board.castle_rights_global[BKS_CASTLE_RIGHTS] = false;
            Board.castle_rights_global[BQS_CASTLE_RIGHTS] = false;
            Board.ep = NO_SQUARE;
            break;

        case GenConst.TAG_BKnightPromotion: //BNPr
            Board.bitboard_array_global[GenConst.BN] =Board.bitboard_array_global[GenConst.BN].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_BBishopPromotion: //BBPr
            Board.bitboard_array_global[GenConst.BB] =Board.bitboard_array_global[GenConst.BB].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_BQueenPromotion: //BQPr
            Board.bitboard_array_global[GenConst.BQ] =Board.bitboard_array_global[GenConst.BQ].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_BRookPromotion: //BRPr
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case 12: //WNPr
            Board.bitboard_array_global[GenConst.WN] =Board.bitboard_array_global[GenConst.WN].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case 13: //WBPr
            Board.bitboard_array_global[GenConst.WB] =Board.bitboard_array_global[GenConst.WB].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case 14: //WQPr
            Board.bitboard_array_global[GenConst.WQ] =Board.bitboard_array_global[GenConst.WQ].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case 15: //WRPr
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case 16: //BNPrCAP
            Board.bitboard_array_global[GenConst.BN] =Board.bitboard_array_global[GenConst.BN].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            for (int i = GenConst.WP; i <= GenConst.WK; ++i)
            {
                if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                {
                    captureIndex = i;
                    break;
                }
            }
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].and(MoveConstants.SQUARE_BBS[targetSquare].not());

            break;
        case 17: //BBPrCAP
            Board.bitboard_array_global[GenConst.BB] =Board.bitboard_array_global[GenConst.BB].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            for (int i = GenConst.WP; i <= GenConst.WK; ++i)
            {
                if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                {
                    captureIndex = i;
                    break;
                }
            }
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 18: //BQPrCAP
            Board.bitboard_array_global[GenConst.BQ] =Board.bitboard_array_global[GenConst.BQ].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            for (int i = GenConst.WP; i <= GenConst.WK; ++i)
            {
                if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                {
                    captureIndex = i;
                    break;
                }
            }
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 19: //BRPrCAP
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            for (int i = GenConst.WP; i <= GenConst.WK; ++i)
            {
                if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                {
                    captureIndex = i;
                    break;
                }
            }
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 20: //WNPrCAP
            Board.bitboard_array_global[GenConst.WN] =Board.bitboard_array_global[GenConst.WN].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            for (int i = GenConst.BP; i <= GenConst.BK; ++i)
            {
                if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                {
                    captureIndex = i;
                    break;
                }
            }
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].and(MoveConstants.SQUARE_BBS[targetSquare].not());

            break;
        case 21: //WBPrCAP
            Board.bitboard_array_global[GenConst.WB] =Board.bitboard_array_global[GenConst.WB].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            for (int i = GenConst.BP; i <= GenConst.BK; ++i)
            {
                if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                {
                    captureIndex = i;
                    break;
                }
            }
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].and(MoveConstants.SQUARE_BBS[targetSquare].not());

            break;
        case 22: //WQPrCAP
            Board.bitboard_array_global[GenConst.WQ] =Board.bitboard_array_global[GenConst.WQ].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = NO_SQUARE;
            for (int i = GenConst.BP; i <= GenConst.BK; ++i)
            {
                if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                {
                    captureIndex = i;
                    break;
                }
            }
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].and(MoveConstants.SQUARE_BBS[targetSquare].not());

            break;
        case 23: //WRPrCAP
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[startingSquare].not());

            Board.ep = NO_SQUARE;
            for (int i = GenConst.BP; i <= GenConst.BK; i++)
            {
                if (isNotZero((Board.bitboard_array_global[i].and(MoveConstants.SQUARE_BBS[targetSquare]))))
                {
                    captureIndex = i;
                    break;
                }
            }
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 24: //WDouble
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = targetSquare + 8;
            break;
        case 25: //BDouble
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            Board.ep = targetSquare - 8;
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
                if (isZero((Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[GenConst.H1]))))
                {
                    Board.castle_rights_global[WKS_CASTLE_RIGHTS] = false;
                }
            }
            if (Board.castle_rights_global[WQS_CASTLE_RIGHTS] == true)
            {
                if (isZero((Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[GenConst.A1]))))
                {
                    Board.castle_rights_global[WQS_CASTLE_RIGHTS] = false;
                }
            }
        }
        else if (piece == GenConst.BR)
        {
            if (Board.castle_rights_global[BKS_CASTLE_RIGHTS] == true)
            {
                if (isZero((Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[GenConst.H8]))))
                {
                    Board.castle_rights_global[BKS_CASTLE_RIGHTS] = false;
                }
            }
            if (Board.castle_rights_global[BQS_CASTLE_RIGHTS] == true)
            {
                if (isZero((Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[GenConst.A8]))))
                {
                    Board.castle_rights_global[BQS_CASTLE_RIGHTS] = false;
                }
            }
        }

        priorNodes = nodes;
        nodes += PerftInlineGlobal(depth - 1, ply + 1);

        Board.is_white_global = !Board.is_white_global;
        switch (tag)
        {
        case GenConst.TAG_NONE: //none
        case GenConst.TAG_CHECK: //check
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case GenConst.TAG_CAPTURE: //capture
        case GenConst.TAG_CHECK_CAPTURE: //check cap
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[captureIndex] = Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[startingSquare]);
            break;
        case GenConst.TAG_WHITEEP: //white ep
            //move piece
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].and(MoveConstants.SQUARE_BBS[targetSquare + 8]);
            break;
        case GenConst.TAG_BLACKEP: //black ep
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].or(MoveConstants.SQUARE_BBS[targetSquare - 8]);
            break;

        case GenConst.TAG_WCASTLEKS: //WKS
            //white king
            Board.bitboard_array_global[GenConst.WK] =Board.bitboard_array_global[GenConst.WK].or(MoveConstants.SQUARE_BBS[GenConst.E1]);
            Board.bitboard_array_global[GenConst.WK] =Board.bitboard_array_global[GenConst.WK].and(MoveConstants.SQUARE_BBS[GenConst.G1].not());
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].or(MoveConstants.SQUARE_BBS[GenConst.H1]);
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[GenConst.F1].not());
            break;
        case GenConst.TAG_WCASTLEQS: //WQS
            Board.bitboard_array_global[GenConst.WK] =Board.bitboard_array_global[GenConst.WK].or(MoveConstants.SQUARE_BBS[GenConst.E1]);
            Board.bitboard_array_global[GenConst.WK] =Board.bitboard_array_global[GenConst.WK].and(MoveConstants.SQUARE_BBS[GenConst.C1].not());
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].or(MoveConstants.SQUARE_BBS[GenConst.A1]);
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[GenConst.D1].not());
            break;
        case GenConst.TAG_BCASTLEKS: //BKS
            Board.bitboard_array_global[GenConst.BK] =Board.bitboard_array_global[GenConst.BK].or(MoveConstants.SQUARE_BBS[GenConst.E8]);
            Board.bitboard_array_global[GenConst.BK] =Board.bitboard_array_global[GenConst.BK].and(MoveConstants.SQUARE_BBS[GenConst.G8].not());
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].or(MoveConstants.SQUARE_BBS[GenConst.H8]);
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[GenConst.F8].not());
            break;
        case GenConst.TAG_BCASTLEQS: //BQS
            Board.bitboard_array_global[GenConst.BK] =Board.bitboard_array_global[GenConst.BK].or(MoveConstants.SQUARE_BBS[GenConst.E8]);
            Board.bitboard_array_global[GenConst.BK] =Board.bitboard_array_global[GenConst.BK].and(MoveConstants.SQUARE_BBS[GenConst.C8].not());
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].or(MoveConstants.SQUARE_BBS[GenConst.A8]);
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[GenConst.D8].not());
            break;

        case GenConst.TAG_BKnightPromotion: //BNPr
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BN] =Board.bitboard_array_global[GenConst.BN].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case GenConst.TAG_BBishopPromotion: //BBPr
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BB] =Board.bitboard_array_global[GenConst.BB].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case GenConst.TAG_BQueenPromotion: //BQPr
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BQ] =Board.bitboard_array_global[GenConst.BQ].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case GenConst.TAG_BRookPromotion: //BRPr
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.ep = NO_SQUARE;
            break;
        case 12: //WNPr
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WN] =Board.bitboard_array_global[GenConst.WN].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 13: //WBPr
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WB] =Board.bitboard_array_global[GenConst.WB].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 14: //WQPr
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WQ] =Board.bitboard_array_global[GenConst.WQ].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 15: //WRPr
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 16: //BNPrCAP
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BN] =Board.bitboard_array_global[GenConst.BN].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[targetSquare]);
            break;
        case 17: //BBPrCAP
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BB] =Board.bitboard_array_global[GenConst.BB].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[targetSquare]);
            break;
        case 18: //BQPrCAP
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BQ] =Board.bitboard_array_global[GenConst.BQ].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[targetSquare]);
            break;
        case 19: //BRPrCAP
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.BR] =Board.bitboard_array_global[GenConst.BR].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[targetSquare]);
            break;
        case 20: //WNPrCAP
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WN] =Board.bitboard_array_global[GenConst.WN].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[targetSquare]);
            break;
        case 21: //WBPrCAP
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WB] =Board.bitboard_array_global[GenConst.WB].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[targetSquare]);
            break;
        case 22: //WQPrCAP
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WQ] =Board.bitboard_array_global[GenConst.WQ].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[targetSquare]);
            break;
        case 23: //WRPrCAP
            Board.bitboard_array_global[piece] =Board.bitboard_array_global[piece].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WR] =Board.bitboard_array_global[GenConst.WR].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            Board.bitboard_array_global[captureIndex] =Board.bitboard_array_global[captureIndex].or(MoveConstants.SQUARE_BBS[targetSquare]);
            break;
        case 24: //WDouble
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].or(MoveConstants.SQUARE_BBS[startingSquare]);
            Board.bitboard_array_global[GenConst.WP] =Board.bitboard_array_global[GenConst.WP].and(MoveConstants.SQUARE_BBS[targetSquare].not());
            break;
        case 25: //BDouble
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].or(MoveConstants.SQUARE_BBS[targetSquare]);
            Board.bitboard_array_global[GenConst.BP] =Board.bitboard_array_global[GenConst.BP].and(MoveConstants.SQUARE_BBS[startingSquare].not());
            break;
        }

        Board.castle_rights_global[0] = copy_castle[0];
        Board.castle_rights_global[1] = copy_castle[1];
        Board.castle_rights_global[2] = copy_castle[2];
        Board.castle_rights_global[3] = copy_castle[3];
        Board.ep = copyEp;

        //if (epGlobal != NO_SQUARE)
        //{
        //    std::cout << "   ep: " << SQ_CHAR_X[epGlobal] << SQ_CHAR_Y[epGlobal] << '\n';
        //}



        if (ply == 0)
        {
            //Pr.printInt(startingSquare);
            //Pr.printInt(targetSquare);
            //Pr.printInt(tag);
            PrintMoveNoNL(startingSquare, targetSquare, tag);
            System.out.printf(": %d\n", nodes - priorNodes);
        }
    }

        return nodes;
    }

    public static void RunPerftInlineGlobalOcc(int depth)
    {
        long startTime = System.currentTimeMillis();
        
        int nodes = PerftInlineDebug(depth, 0);

        long endTime = System.currentTimeMillis();

        long elapsedTime = endTime - startTime;

        String nodeString = String.format("Nodes: %d\n", nodes);
        String timeString = String.format("Time taken: %d ms\n", elapsedTime);
        
        Pr.print(nodeString);
        Pr.print(timeString);
    }
}
