public class GenConst {
    public static final int WP = 0;
    public static final int WN = 1;
    public static final int WB = 2;
    public static final int WR = 3;
    public static final int WQ = 4;
    public static final int WK = 5;
    public static final int BP = 6;
    public static final int BN = 7;
    public static final int BB = 8;
    public static final int BR = 9;
    public static final int BQ = 10;
    public static final int BK = 11;
    public static final int EMPTY = 12;

    public static final long RANK_1_BITBOARD = Long.parseUnsignedLong("18374686479671623680");
    public static final long RANK_2_BITBOARD = 71776119061217280L;
    public static final long RANK_3_BITBOARD = 280375465082880L;
    public static final long RANK_4_BITBOARD = 1095216660480L;
    public static final long RANK_5_BITBOARD = 4278190080L;
    public static final long RANK_6_BITBOARD = 16711680L;
    public static final long RANK_7_BITBOARD = 65280L;
    public static final long RANK_8_BITBOARD = 255L;

    public static final long WKS_EMPTY_BITBOARD = 6917529027641081856L;
    public static final long WQS_EMPTY_BITBOARD = 1008806316530991104L;
    public static final long BKS_EMPTY_BITBOARD = 96L;
    public static final long BQS_EMPTY_BITBOARD = 14L;

    public static final long MAX_ULONG = Long.parseUnsignedLong("18446744073709551615");

    public static final int TAG_NONE = 0;
public static final int TAG_CAPTURE = 1;
public static final int TAG_WHITE_EP = 2;
public static final int TAG_BLACK_EP = 3;
public static final int TAG_W_CASTLE_KS = 4;
public static final int TAG_W_CASTLE_QS = 5;
public static final int TAG_B_CASTLE_KS = 6;
public static final int TAG_B_CASTLE_QS = 7;
public static final int TAG_B_N_PROMOTION = 8;
public static final int TAG_B_B_PROMOTION = 9;
public static final int TAG_B_Q_PROMOTION = 10;
public static final int TAG_B_R_PROMOTION = 11;
public static final int TAG_W_N_PROMOTION = 12;
public static final int TAG_W_B_PROMOTION = 13;
public static final int TAG_W_Q_PROMOTION = 14;
public static final int TAG_W_R_PROMOTION = 15;
public static final int TAG_B_N_PROMOTION_CAP = 16;
public static final int TAG_B_B_PROMOTION_CAP = 17;
public static final int TAG_B_Q_PROMOTION_CAP = 18;
public static final int TAG_B_R_PROMOTION_CAP = 19;
public static final int TAG_W_N_PROMOTION_CAP = 20;
public static final int TAG_W_B_PROMOTION_CAP = 21;
public static final int TAG_W_Q_PROMOTION_CAP = 22;
public static final int TAG_W_R_PROMOTION_CAP = 23;
public static final int TAG_W_P_DOUBLE = 24;
public static final int TAG_B_P_DOUBLE = 25;
public static final int TAG_CHECK = 26;
public static final int TAG_CHECK_CAP = 27;

public static final int A8 = 0;
public static final int B8 = 1;
public static final int C8 = 2;
public static final int D8 = 3;
public static final int E8 = 4;
public static final int F8 = 5;
public static final int G8 = 6;
public static final int H8 = 7;
public static final int A7 = 8;
public static final int B7 = 9;
public static final int C7 = 10;
public static final int D7 = 11;
public static final int E7 = 12;
public static final int F7 = 13;
public static final int G7 = 14;
public static final int H7 = 15;
public static final int A6 = 16;
public static final int B6 = 17;
public static final int C6 = 18;
public static final int D6 = 19;
public static final int E6 = 20;
public static final int F6 = 21;
public static final int G6 = 22;
public static final int H6 = 23;
public static final int A5 = 24;
public static final int B5 = 25;
public static final int C5 = 26;
public static final int D5 = 27;
public static final int E5 = 28;
public static final int F5 = 29;
public static final int G5 = 30;
public static final int H5 = 31;
public static final int A4 = 32;
public static final int B4 = 33;
public static final int C4 = 34;
public static final int D4 = 35;
public static final int E4 = 36;
public static final int F4 = 37;
public static final int G4 = 38;
public static final int H4 = 39;
public static final int A3 = 40;
public static final int B3 = 41;
public static final int C3 = 42;
public static final int D3 = 43;
public static final int E3 = 44;
public static final int F3 = 45;
public static final int G3 = 46;
public static final int H3 = 47;
public static final int A2 = 48;
public static final int B2 = 49;
public static final int C2 = 50;
public static final int D2 = 51;
public static final int E2 = 52;
public static final int F2 = 53;
public static final int G2 = 54;
public static final int H2 = 55;
public static final int A1 = 56;
public static final int B1 = 57;
public static final int C1 = 58;
public static final int D1 = 59;
public static final int E1 = 60;
public static final int F1 = 61;
public static final int G1 = 62;
public static final int H1 = 63;

public static final int NO_SQUARE = 64;

static char[] SQ_CHAR_Y = {
    '8', '8', '8', '8', '8', '8', '8', '8',
    '7', '7', '7', '7', '7', '7', '7', '7',
    '6', '6', '6', '6', '6', '6', '6', '6',
    '5', '5', '5', '5', '5', '5', '5', '5',
    '4', '4', '4', '4', '4', '4', '4', '4',
    '3', '3', '3', '3', '3', '3', '3', '3',
    '2', '2', '2', '2', '2', '2', '2', '2',
    '1', '1', '1', '1', '1', '1', '1', '1', 
    'A' // This seems like an extra value; ensure it is intentional
};

static char[] SQ_CHAR_X = {
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 
    'N' // This seems like an extra value; ensure it is intentional
};

}
