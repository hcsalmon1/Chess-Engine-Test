public class Board
{
    public static long[] bitboard_array_global = new long[12];
    public static boolean is_white_global = true;
    public static boolean[] castle_rights_global = new boolean[4];
    public static int ep;

    static final long BP_STARTING_POSITIONS = 65280;
    static final long WP_STARTING_POSITIONS = 71776119061217280L;
    static final long BK_STARTING_POSITION = 16;
    static final long WK_STARTING_POSITION = 1152921504606846976L;
    static final long BN_STARTING_POSITIONS = 66;
    static final long WN_STARTING_POSITIONS = 4755801206503243776L;
    static final long WR_STARTING_POSITIONS = Long.parseUnsignedLong("9295429630892703744");
    static final long BR_STARTING_POSITIONS = 129;
    static final long BB_STARTING_POSITIONS = 36;
    static final long WB_STARTING_POSITIONS = 2594073385365405696L;
    static final long WQ_STARTING_POSITION = 576460752303423488L;
    static final long BQ_STARTING_POSITION = 8;


    public static void SetTrickyPosition() {

        ep = GenConst.NO_SQUARE;
        is_white_global = true;
        castle_rights_global[0] = true;
        castle_rights_global[1] = true;
        castle_rights_global[2] = true;
        castle_rights_global[3] = true;
    
        bitboard_array_global[GenConst.WP] = 65020788473856000L;
        bitboard_array_global[GenConst.WN] = 4398314946560L;
        bitboard_array_global[GenConst.WB] = 6755399441055744L;
        bitboard_array_global[GenConst.WR] = Long.parseUnsignedLong("9295429630892703744");
        bitboard_array_global[GenConst.WQ] = 35184372088832L;
        bitboard_array_global[GenConst.WK] = 1152921504606846976L;
        bitboard_array_global[GenConst.BP] = 140746083544320L;
        bitboard_array_global[GenConst.BN] = 2228224;
        bitboard_array_global[GenConst.BB] = 81920;
        bitboard_array_global[GenConst.BR] = 129;
        bitboard_array_global[GenConst.BQ] = 4096;
        bitboard_array_global[GenConst.BK] = 16;
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

    static boolean IsOccupied(long bitboard, int square)  {
        return (bitboard&MoveConstants.SQUARE_BBS[square]) != 0;
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
                int square = rank * 8 + file;
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
