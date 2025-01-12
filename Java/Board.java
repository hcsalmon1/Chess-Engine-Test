
import java.math.BigInteger;

public class Board 
{
    public static BigInteger[] bitboard_array_global = new BigInteger[12];
    public static boolean is_white_global = true;
    public static boolean[] castle_rights_global = new boolean[4];
    public static int ep;

    static final BigInteger BP_STARTING_POSITIONS = new BigInteger("65280");
    static final BigInteger WP_STARTING_POSITIONS = new BigInteger("71776119061217280");
    static final BigInteger BK_STARTING_POSITION = new BigInteger("16");
    static final BigInteger WK_STARTING_POSITION = new BigInteger("1152921504606846976");
    static final BigInteger BN_STARTING_POSITIONS = new BigInteger("66");
    static final BigInteger WN_STARTING_POSITIONS = new BigInteger("4755801206503243776");
    static final BigInteger WR_STARTING_POSITIONS = new BigInteger("9295429630892703744");
    static final BigInteger BR_STARTING_POSITIONS = new BigInteger("129");
    static final BigInteger BB_STARTING_POSITIONS = new BigInteger("36");
    static final BigInteger WB_STARTING_POSITIONS = new BigInteger("2594073385365405696");
    static final BigInteger WQ_STARTING_POSITION = new BigInteger("576460752303423488");
    static final BigInteger BQ_STARTING_POSITION = new BigInteger("8");


    public static void SetTrickyPosition() {

        ep = GenConst.NO_SQUARE;
        is_white_global = true;
        castle_rights_global[0] = true;
        castle_rights_global[1] = true;
        castle_rights_global[2] = true;
        castle_rights_global[3] = true;
    
        bitboard_array_global[GenConst.WP] = new BigInteger("65020788473856000");
        bitboard_array_global[GenConst.WN] = new BigInteger("4398314946560");
        bitboard_array_global[GenConst.WB] = new BigInteger("6755399441055744");
        bitboard_array_global[GenConst.WR] = new BigInteger("9295429630892703744");
        bitboard_array_global[GenConst.WQ] = new BigInteger("35184372088832");
        bitboard_array_global[GenConst.WK] = new BigInteger("1152921504606846976");
        bitboard_array_global[GenConst.BP] = new BigInteger("140746083544320");
        bitboard_array_global[GenConst.BN] = new BigInteger("2228224");
        bitboard_array_global[GenConst.BB] = new BigInteger("81920");
        bitboard_array_global[GenConst.BR] = new BigInteger("129");
        bitboard_array_global[GenConst.BQ] = new BigInteger("4096");
        bitboard_array_global[GenConst.BK] = new BigInteger("16");
    }

    public static void SetStartingPosition() {

        ep = GenConst.NO_SQUARE;
        is_white_global = true;
        castle_rights_global[0] = true;
        castle_rights_global[1] = true;
        castle_rights_global[2] = true;
        castle_rights_global[3] = true;
    
        bitboard_array_global[GenConst.WP] = WP_STARTING_POSITIONS;
        bitboard_array_global[GenConst.WN] = WN_STARTING_POSITIONS;
        bitboard_array_global[GenConst.WB] = WB_STARTING_POSITIONS;
        bitboard_array_global[GenConst.WR] = WR_STARTING_POSITIONS;
        bitboard_array_global[GenConst.WQ] = WQ_STARTING_POSITION;
        bitboard_array_global[GenConst.WK] = WK_STARTING_POSITION;
        bitboard_array_global[GenConst.BP] = BP_STARTING_POSITIONS;
        bitboard_array_global[GenConst.BN] = BN_STARTING_POSITIONS;
        bitboard_array_global[GenConst.BB] = BB_STARTING_POSITIONS;
        bitboard_array_global[GenConst.BR] = BR_STARTING_POSITIONS;
        bitboard_array_global[GenConst.BQ] = BQ_STARTING_POSITION;
        bitboard_array_global[GenConst.BK] = BK_STARTING_POSITION;
    }

    static final char[] PieceNames = {'P', 'N', 'B', 'R', 'Q', 'K', 'P', 'N', 'B', 'R', 'Q', 'K', '_'};
    static final char[] PieceColours = {'W', 'W', 'W', 'W', 'W', 'W', 'B', 'B', 'B', 'B', 'B', 'B', '_'};

static final int EMPTY = 12;

    static boolean IsOccupied(BigInteger bitboard, int square)  {
        return !bitboard.and(MoveConstants.SQUARE_BBS[square]).equals(BigInteger.ZERO);
    }
    
    static int GetOccupiedIndex(int square) {
        for (int i = 0; i < 12; i++) {
            if (IsOccupied(bitboard_array_global[i], square)) {
                return i;
            }
        }
        return EMPTY;
    }

    static int[] fillBoardArray() {
        int[] boardArray = new int[64];
        for (int i = 0; i < 64; i++) {
            boardArray[i] = GetOccupiedIndex(i);
        }
        return boardArray;
    }

    public static void PrintBoard() {
        System.out.println("Board:");
    
        int[] boardArray = fillBoardArray();
    
        for (int rank = 0; rank < 8; rank++) {
            System.out.print("   ");
            
            for (int file = 0; file < 8; file++) {
                int square = (rank * 8) + file;
                System.out.printf("%c%c ", PieceColours[boardArray[square]], PieceNames[boardArray[square]]);
            }
            
            System.out.println();
        }
        System.out.println();
        
        System.out.printf("White to play: %b\n", is_white_global);
        System.out.printf("Castle: %b %b %b %b\n", castle_rights_global[0], castle_rights_global[1], castle_rights_global[2], castle_rights_global[3]);
        System.out.printf("ep: %d\n", ep);
        System.out.println();
        System.out.println();
    }

}
