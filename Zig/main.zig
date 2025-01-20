
const std = @import("std");
const constants = @import("constants.zig");

const WHITE_TO_PLAY = true;
const BLACK_TO_PLAY = false;

//inline  BitscanForward(const  tempBitboard);

const MAGIC:u64 = 0x03f79d71b4cb0a89;

const DEBRUIJN64:[64]usize  = [64]usize
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

fn BitscanForward(tempBitboard:u64) usize {

    return @truncate(DEBRUIJN64[MAGIC *% (tempBitboard ^ (tempBitboard - 1)) >> 58]);
}

//fens
const FEN_STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

const PINNED_SQUARE_INDEX = 0;
const PINNING_PIECE_INDEX = 1;

//LSB
//equation: startingSquare = (DEBRUIJN64[MAGIC * (tempBitboard ^ (tempBitboard - 1)) >> 58]);

const MAX_ULONG = 18446744073709551615;

const MOVE_STARTING = 0;
const MOVE_TARGET = 1;
const MOVE_PIECE = 2;
const MOVE_TAG = 3;

const SQ_CHAR_Y = [_]u8
{
    '8','8','8','8','8','8','8','8',
    '7','7','7','7','7','7','7','7',
    '6','6','6','6','6','6','6','6',
    '5','5','5','5','5','5','5','5',
    '4','4','4','4','4','4','4','4',
    '3','3','3','3','3','3','3','3',
    '2','2','2','2','2','2','2','2',
    '1','1','1','1','1','1','1','1','A'
};

const SQ_CHAR_X = [_]u8 {
    'a','b','c','d','e','f','g','h',
    'a','b','c','d','e','f','g','h',
    'a','b','c','d','e','f','g','h',
    'a','b','c','d','e','f','g','h',
    'a','b','c','d','e','f','g','h',
    'a','b','c','d','e','f','g','h',
    'a','b','c','d','e','f','g','h',
    'a','b','c','d','e','f','g','h','N'
};

const BLACK_PAWN_CHAR = 'p';   
const BLACK_KNIGHT_CHAR = 'n'; 
const BLACK_BISHOP_CHAR = 'b'; 
const BLACK_ROOK_CHAR = 'r';   
const BLACK_QUEEN_CHAR = 'q';  
const BLACK_KING_CHAR = 'k';

const WHITE_PAWN_CHAR = 'P';   
const WHITE_KNIGHT_CHAR = 'N'; 
const WHITE_BISHOP_CHAR = 'B'; 
const WHITE_ROOK_CHAR = 'R';   
const WHITE_QUEEN_CHAR = 'Q';  
const WHITE_KING_CHAR = 'K';   
const DUCK_CHAR = 'D';

const BRACKET_CHAR = '/'; 
const SPACE_CHAR = ' ';  
const DASH_CHAR = '-'; 
const W_SIDE_CHAR = 'w';   
const B_SIDE_CHAR = 'b';   
const CAPITAL_K_CHAR = 'K';    
const CAPITAL_Q_CHAR = 'Q';    
const SMALL_K_CHAR = 'k'; const SMALL_Q_CHAR = 'q';

const BITBOARD_COUNT = 12;

const WKS_CASTLE_RIGHTS = 0;
const WQS_CASTLE_RIGHTS = 1;
const BKS_CASTLE_RIGHTS = 2;
const BQS_CASTLE_RIGHTS = 3;
const WP = 0;
const WN = 1;
const WB = 2;
const WR = 3;
const WQ = 4;
const WK = 5;
const BP = 6;
const BN = 7;
const BB = 8;
const BR = 9;
const BQ = 10;
const BK = 11;

const WHITE_PAWN_ATTACKS = [_]u64
{
        0,  0,  0,  0,  0,  0,  0,  0,
        2,  5,  10, 20, 40, 80, 160,64,
        512,    1280,   2560,   5120,   10240,  20480,  40960,  16384,
        131072, 327680, 655360, 1310720,2621440,5242880,10485760,4194304,
        33554432,83886080,167772160,335544320,671088640,1342177280,2684354560,1073741824,
        8589934592,21474836480,42949672960,85899345920,171798691840,343597383680,687194767360,274877906944,
        2199023255552,5497558138880,10995116277760,21990232555520,43980465111040,87960930222080,175921860444160,70368744177664,
        562949953421312,1407374883553280,2814749767106560,5629499534213120,11258999068426240,22517998136852480,45035996273704960,18014398509481984,
};
const BLACK_PAWN_ATTACKS = [_]u64
{
        512,1280,2560,5120,10240,20480,40960,16384,
        131072,327680,655360,1310720,2621440,5242880,10485760,4194304,
        33554432,83886080,167772160,335544320,671088640,1342177280,2684354560,1073741824,
        8589934592,21474836480,42949672960,85899345920,171798691840,343597383680,687194767360,274877906944,
        2199023255552,5497558138880,10995116277760,21990232555520,43980465111040,87960930222080,175921860444160,70368744177664,
        562949953421312,1407374883553280,2814749767106560,5629499534213120,11258999068426240,22517998136852480,45035996273704960,18014398509481984,
        144115188075855872,360287970189639680,720575940379279360,1441151880758558720,2882303761517117440,5764607523034234880,11529215046068469760,4611686018427387904,
        0,0,0,0,0,0,0,0,
};
const KNIGHT_ATTACKS = [_]u64
{
        132096,
        329728,
        659712,
        1319424,
        2638848,
        5277696,
        10489856,
        4202496,
        33816580,
        84410376,
        168886289,
        337772578,
        675545156,
        1351090312,
        2685403152,
        1075839008,
        8657044482,
        21609056261,
        43234889994,
        86469779988,
        172939559976,
        345879119952,
        687463207072,
        275414786112,
        2216203387392,
        5531918402816,
        11068131838464,
        22136263676928,
        44272527353856,
        88545054707712,
        175990581010432,
        70506185244672,
        567348067172352,
        1416171111120896,
        2833441750646784,
        5666883501293568,
        11333767002587136,
        22667534005174272,
        45053588738670592,
        18049583422636032,
        145241105196122112,
        362539804446949376,
        725361088165576704,
        1450722176331153408,
        2901444352662306816,
        5802888705324613632,
        11533718717099671552,
        4620693356194824192,
        288234782788157440,
        576469569871282176,
        1224997833292120064,
        2449995666584240128,
        4899991333168480256,
        9799982666336960512,
        1152939783987658752,
        2305878468463689728,
        1128098930098176,
        2257297371824128,
        4796069720358912,
        9592139440717824,
        19184278881435648,
        38368557762871296,
        4679521487814656,
        9077567998918656,
};
const BISHOP_MASKS = [_]u64
{
        18049651735527936,
        70506452091904,
        275415828992,
        1075975168,
        38021120,
        8657588224,
        2216338399232,
        567382630219776,
        9024825867763712,
        18049651735527424,
        70506452221952,
        275449643008,
        9733406720,
        2216342585344,
        567382630203392,
        1134765260406784,
        4512412933816832,
        9024825867633664,
        18049651768822272,
        70515108615168,
        2491752130560,
        567383701868544,
        1134765256220672,
        2269530512441344,
        2256206450263040,
        4512412900526080,
        9024834391117824,
        18051867805491712,
        637888545440768,
        1135039602493440,
        2269529440784384,
        4539058881568768,
        1128098963916800,
        2256197927833600,
        4514594912477184,
        9592139778506752,
        19184279556981248,
        2339762086609920,
        4538784537380864,
        9077569074761728,
        562958610993152,
        1125917221986304,
        2814792987328512,
        5629586008178688,
        11259172008099840,
        22518341868716544,
        9007336962655232,
        18014673925310464,
        2216338399232,
        4432676798464,
        11064376819712,
        22137335185408,
        44272556441600,
        87995357200384,
        35253226045952,
        70506452091904,
        567382630219776,
        1134765260406784,
        2832480465846272,
        5667157807464448,
        11333774449049600,
        22526811443298304,
        9024825867763712,
        18049651735527936
};
const ROOK_MASKS = [_]u64
{
        282578800148862,
        565157600297596,
        1130315200595066,
        2260630401190006,
        4521260802379886,
        9042521604759646,
        18085043209519166,
        36170086419038334,
        282578800180736,
        565157600328704,
        1130315200625152,
        2260630401218048,
        4521260802403840,
        9042521604775424,
        18085043209518592,
        36170086419037696,
        282578808340736,
        565157608292864,
        1130315208328192,
        2260630408398848,
        4521260808540160,
        9042521608822784,
        18085043209388032,
        36170086418907136,
        282580897300736,
        565159647117824,
        1130317180306432,
        2260632246683648,
        4521262379438080,
        9042522644946944,
        18085043175964672,
        36170086385483776,
        283115671060736,
        565681586307584,
        1130822006735872,
        2261102847592448,
        4521664529305600,
        9042787892731904,
        18085034619584512,
        36170077829103616,
        420017753620736,
        699298018886144,
        1260057572672512,
        2381576680245248,
        4624614895390720,
        9110691325681664,
        18082844186263552,
        36167887395782656,
        35466950888980736,
        34905104758997504,
        34344362452452352,
        33222877839362048,
        30979908613181440,
        26493970160820224,
        17522093256097792,
        35607136465616896,
        9079539427579068672,
        8935706818303361536,
        8792156787827803136,
        8505056726876686336,
        7930856604974452736,
        6782456361169985536,
        4485655873561051136,
        9115426935197958144
};
const ROOK_MAGIC_NUMBERS = [_]u64
{
        0x8a80104000800020,
        0x140002000100040,
        0x2801880a0017001,
        0x100081001000420,
        0x200020010080420,
        0x3001c0002010008,
        0x8480008002000100,
        0x2080088004402900,
        0x800098204000,
        0x2024401000200040,
        0x100802000801000,
        0x120800800801000,
        0x208808088000400,
        0x2802200800400,
        0x2200800100020080,
        0x801000060821100,
        0x80044006422000,
        0x100808020004000,
        0x12108a0010204200,
        0x140848010000802,
        0x481828014002800,
        0x8094004002004100,
        0x4010040010010802,
        0x20008806104,
        0x100400080208000,
        0x2040002120081000,
        0x21200680100081,
        0x20100080080080,
        0x2000a00200410,
        0x20080800400,
        0x80088400100102,
        0x80004600042881,
        0x4040008040800020,
        0x440003000200801,
        0x4200011004500,
        0x188020010100100,
        0x14800401802800,
        0x2080040080800200,
        0x124080204001001,
        0x200046502000484,
        0x480400080088020,
        0x1000422010034000,
        0x30200100110040,
        0x100021010009,
        0x2002080100110004,
        0x202008004008002,
        0x20020004010100,
        0x2048440040820001,
        0x101002200408200,
        0x40802000401080,
        0x4008142004410100,
        0x2060820c0120200,
        0x1001004080100,
        0x20c020080040080,
        0x2935610830022400,
        0x44440041009200,
        0x280001040802101,
        0x2100190040002085,
        0x80c0084100102001,
        0x4024081001000421,
        0x20030a0244872,
        0x12001008414402,
        0x2006104900a0804,
        0x1004081002402
};
const BISHOP_MAGIC_NUMBERS = [_]u64
{
        0x40040844404084,
        0x2004208a004208,
        0x10190041080202,
        0x108060845042010,
        0x581104180800210,
        0x2112080446200010,
        0x1080820820060210,
        0x3c0808410220200,
        0x4050404440404,
        0x21001420088,
        0x24d0080801082102,
        0x1020a0a020400,
        0x40308200402,
        0x4011002100800,
        0x401484104104005,
        0x801010402020200,
        0x400210c3880100,
        0x404022024108200,
        0x810018200204102,
        0x4002801a02003,
        0x85040820080400,
        0x810102c808880400,
        0xe900410884800,
        0x8002020480840102,
        0x220200865090201,
        0x2010100a02021202,
        0x152048408022401,
        0x20080002081110,
        0x4001001021004000,
        0x800040400a011002,
        0xe4004081011002,
        0x1c004001012080,
        0x8004200962a00220,
        0x8422100208500202,
        0x2000402200300c08,
        0x8646020080080080,
        0x80020a0200100808,
        0x2010004880111000,
        0x623000a080011400,
        0x42008c0340209202,
        0x209188240001000,
        0x400408a884001800,
        0x110400a6080400,
        0x1840060a44020800,
        0x90080104000041,
        0x201011000808101,
        0x1a2208080504f080,
        0x8012020600211212,
        0x500861011240000,
        0x180806108200800,
        0x4000020e01040044,
        0x300000261044000a,
        0x802241102020002,
        0x20906061210001,
        0x5a84841004010310,
        0x4010801011c04,
        0xa010109502200,
        0x4a02012000,
        0x500201010098b028,
        0x8040002811040900,
        0x28000010020204,
        0x6000020202d0240,
        0x8918844842082200,
        0x4010011029020020
};
const BISHOP_REL_BITS =[_]usize
{
        6 ,5 ,5 ,5 ,5 ,5 ,5 ,6 ,
        5 ,5 ,5 ,5 ,5 ,5 ,5 ,5 ,
        5 ,5 ,7 ,7 ,7 ,7 ,5 ,5 ,
        5 ,5 ,7 ,9 ,9 ,7 ,5 ,5 ,
        5 ,5 ,7 ,9 ,9 ,7 ,5 ,5 ,
        5 ,5 ,7 ,7 ,7 ,7 ,5 ,5 ,
        5 ,5 ,5 ,5 ,5 ,5 ,5 ,5 ,
        6 ,5 ,5 ,5 ,5 ,5 ,5 ,6 ,
};
const ROOK_REL_BITS = [_]usize
{
        12 ,11 ,11 ,11 ,11 ,11 ,11 ,12 ,
        11 ,10 ,10 ,10 ,10 ,10 ,10 ,11 ,
        11 ,10 ,10 ,10 ,10 ,10 ,10 ,11 ,
        11 ,10 ,10 ,10 ,10 ,10 ,10 ,11 ,
        11 ,10 ,10 ,10 ,10 ,10 ,10 ,11 ,
        11 ,10 ,10 ,10 ,10 ,10 ,10 ,11 ,
        11 ,10 ,10 ,10 ,10 ,10 ,10 ,11 ,
        12 ,11 ,11 ,11 ,11 ,11 ,11 ,12 ,
};

const KING_ATTACKS = [_]u64
{ 770,
    1797,
    3594,
    7188,
    14376,
    28752,
    57504,
    49216,
    197123,
    460039,
    920078,
    1840156,
    3680312,
    7360624,
    14721248,
    12599488,
    50463488,
    117769984,
    235539968,
    471079936,
    942159872,
    1884319744,
    3768639488,
    3225468928,
    12918652928,
    30149115904,
    60298231808,
    120596463616,
    241192927232,
    482385854464,
    964771708928,
    825720045568,
    3307175149568,
    7718173671424,
    15436347342848,
    30872694685696,
    61745389371392,
    123490778742784,
    246981557485568,
    211384331665408,
    846636838289408,
    1975852459884544,
    3951704919769088,
    7903409839538176,
    15806819679076352,
    31613639358152704,
    63227278716305408,
    54114388906344448,
    216739030602088448,
    505818229730443264,
    1011636459460886528,
    2023272918921773056,
    4046545837843546112,
    8093091675687092224,
    16186183351374184448,
    13853283560024178688,
    144959613005987840,
    362258295026614272,
    724516590053228544,
    1449033180106457088,
    2898066360212914176,
    5796132720425828352,
    11592265440851656704,
    4665729213955833856,
};

const SQUARE_BBS = [_]u64
{
        1,
        2,
        4,
        8,
        16,
        32,
        64,
        128,
        256,
        512,
        1024,
        2048,
        4096,
        8192,
        16384,
        32768,
        65536,
        131072,
        262144,
        524288,
        1048576,
        2097152,
        4194304,
        8388608,
        16777216,
        33554432,
        67108864,
        134217728,
        268435456,
        536870912,
        1073741824,
        2147483648,
        4294967296,
        8589934592,
        17179869184,
        34359738368,
        68719476736,
        137438953472,
        274877906944,
        549755813888,
        1099511627776,
        2199023255552,
        4398046511104,
        8796093022208,
        17592186044416,
        35184372088832,
        70368744177664,
        140737488355328,
        281474976710656,
        562949953421312,
        1125899906842624,
        2251799813685248,
        4503599627370496,
        9007199254740992,
        18014398509481984,
        36028797018963968,
        72057594037927936,
        144115188075855872,
        288230376151711744,
        576460752303423488,
        1152921504606846976,
        2305843009213693952,
        4611686018427387904,
        9223372036854775808,
};

const RANK_1_BITBOARD = 18374686479671623680;
const RANK_2_BITBOARD = 71776119061217280;
const RANK_3_BITBOARD = 280375465082880;
const RANK_4_BITBOARD = 1095216660480;
const RANK_5_BITBOARD = 4278190080;
const RANK_6_BITBOARD = 16711680;
const RANK_7_BITBOARD = 65280;
const RANK_8_BITBOARD = 255;

const FILE_A_BITBOARD = 72340172838076673;
const FILE_B_BITBOARD = 144680345676153346;
const FILE_C_BITBOARD = 289360691352306692;
const FILE_D_BITBOARD = 578721382704613384;
const FILE_E_BITBOARD = 1157442765409226768;
const FILE_F_BITBOARD = 2314885530818453536;
const FILE_G_BITBOARD = 4629771061636907072;
const FILE_H_BITBOARD = 9259542123273814144;

const FILE_TO_SQUARE_BBS = [_]u64
{
    72340172838076673, 144680345676153346, 289360691352306692, 578721382704613384, 1157442765409226768, 2314885530818453536, 4629771061636907072, 9259542123273814144,
    72340172838076673, 144680345676153346, 289360691352306692, 578721382704613384, 1157442765409226768, 2314885530818453536, 4629771061636907072, 9259542123273814144,
    72340172838076673, 144680345676153346, 289360691352306692, 578721382704613384, 1157442765409226768, 2314885530818453536, 4629771061636907072, 9259542123273814144,
    72340172838076673, 144680345676153346, 289360691352306692, 578721382704613384, 1157442765409226768, 2314885530818453536, 4629771061636907072, 9259542123273814144,
    72340172838076673, 144680345676153346, 289360691352306692, 578721382704613384, 1157442765409226768, 2314885530818453536, 4629771061636907072, 9259542123273814144,
    72340172838076673, 144680345676153346, 289360691352306692, 578721382704613384, 1157442765409226768, 2314885530818453536, 4629771061636907072, 9259542123273814144,
    72340172838076673, 144680345676153346, 289360691352306692, 578721382704613384, 1157442765409226768, 2314885530818453536, 4629771061636907072, 9259542123273814144,
    72340172838076673, 144680345676153346, 289360691352306692, 578721382704613384, 1157442765409226768, 2314885530818453536, 4629771061636907072, 9259542123273814144,
};

pub inline fn GetBishopAttacksFast(startingSquare: usize, occupancy: u64) u64 {

    var mutableOccupancy: u64 = occupancy;
    const shiftAmount: u64 = 64 - BISHOP_REL_BITS[startingSquare];

    mutableOccupancy &= BISHOP_MASKS[startingSquare];
    mutableOccupancy *%= BISHOP_MAGIC_NUMBERS[startingSquare];
    mutableOccupancy >>= @truncate(shiftAmount);

    return constants.BISHOP_ATTACKS[startingSquare][@truncate(mutableOccupancy)];
}

pub inline fn GetRookAttacksFast(startingSquare: usize, occupancy: u64) u64 {

    var mutableOccupancy: u64 = occupancy;
    const shiftAmount: u64 = 64 - ROOK_REL_BITS[startingSquare];

    mutableOccupancy &= ROOK_MASKS[startingSquare];
    mutableOccupancy *%= ROOK_MAGIC_NUMBERS[startingSquare];
    mutableOccupancy >>= @truncate(shiftAmount);

    return constants.ROOK_ATTACKS[startingSquare][@truncate(mutableOccupancy)];
}

const WHITE_START_INDEX = WP;
const WHITE_END_INDEX = WK;
const BLACK_START_INDEX = BP;
const BLACK_END_INDEX = BK;

const TAG_NONE = 0;
const TAG_CAPTURE = 1;
const TAG_WHITEEP = 2;
const TAG_BLACKEP = 3;
const TAG_WCASTLEKS = 4;
const TAG_WCASTLEQS = 5;
const TAG_BCASTLEKS = 6;
const TAG_BCASTLEQS = 7;
const TAG_BKnightPromotion = 8; 
const TAG_BBishopPromotion = 9; 
const TAG_BQueenPromotion = 10; 
const TAG_BRookPromotion = 11;
const TAG_WKnightPromotion = 12;
const TAG_WBishopPromotion = 13; 
const TAG_WQueenPromotion = 14; 
const TAG_WRookPromotion = 15;
const TAG_BCaptureKnightPromotion = 16; 
const TAG_BCaptureBishopPromotion = 17;
const TAG_BCaptureQueenPromotion = 18;
const TAG_BCaptureRookPromotion = 19;
const TAG_WCaptureKnightPromotion = 20; 
const TAG_WCaptureBishopPromotion = 21; 
const TAG_WCaptureQueenPromotion = 22; 
const TAG_WCaptureRookPromotion = 23;
const TAG_DoublePawnWhite = 24; 
const TAG_DoublePawnBlack = 25; 
const TAG_CHECK = 26; 
const TAG_CHECK_CAPTURE = 27;

const PROMOTION_START = TAG_BKnightPromotion;
const PROMOTION_END_INCLUSIVE = TAG_WCaptureRookPromotion;

const A8 = 0; const B8 = 1; const C8 = 2; const D8 = 3; const E8 = 4; const F8 = 5; const G8 = 6; const H8 = 7;
const A7 = 8; const B7 = 9; const C7 = 10; const D7 = 11; const E7 = 12; const F7 = 13; const G7 = 14; const H7 = 15;
const A6 = 16; const B6 = 17; const C6 = 18; const D6 = 19; const E6 = 20; const F6 = 21; const G6 = 22; const H6 = 23;
const A5 = 24; const B5 = 25; const C5 = 26; const D5 = 27; const E5 = 28; const F5 = 29; const G5 = 30; const H5 = 31;
const A4 = 32; const B4 = 33; const C4 = 34; const D4 = 35; const E4 = 36; const F4 = 37; const G4 = 38; const H4 = 39;
const A2 = 48; const B2 = 49; const C2 = 50; const D2 = 51; const E2 = 52; const F2 = 53; const G2 = 54; const H2 = 55;
const A1 = 56; const B1 = 57; const C1 = 58; const D1 = 59; const E1 = 60; const F1 = 61; const G1 = 62; const H1 = 63; const NO_SQUARE = 64;

const WKS_EMPTY_BITBOARD = 6917529027641081856;
const WQS_EMPTY_BITBOARD = 1008806316530991104;
const BKS_EMPTY_BITBOARD = 96;
const BQS_EMPTY_BITBOARD = 14;

const COMBINED_OCCUPANCIES = 0;
const WHITE_OCCUPANCIES = 1;
const BLACK_OCCUPANCIES = 2;

var bitboard_array_global: [12]u64 = undefined;
var occupancies_global: [3]u64 = undefined;
var ep_global: usize = undefined;
var castle_rights_global: [4]bool = undefined;
var is_white_global: bool = true;


//struct Board {
    // pieceArray[12];
    // occupancies[3];
    // isWhite;
    // ep;
    // castleRights[4];
//};

const PIECE_PHASE = 0;  
const SIDE_PHASE = 1; 
const CASTLE_PHASE = 2; 
const EP_PHASE = 3; const FIFTY_MOVE_PHASE = 4;
const MOVE_COUNT_PHASE = 5;
const PIECE_COLOURS = [_]u8 { 'W','W','W','W','W','W','b','b','b','b','b','b' };
const PIECE_CHARS = [_]u8 { 'P','N','B','R','Q','K','p','n','b','r','q','k' };

fn ResetBoardGlobal() void {
    castle_rights_global[0] = false;
    castle_rights_global[1] = false;
    castle_rights_global[2] = false;
    castle_rights_global[3] = false;

    for (0..BITBOARD_COUNT) |i| {
        bitboard_array_global[i] = 0;
    }
    is_white_global = true;
    ep_global = NO_SQUARE;
}

fn ParseFenGlobal(input:[]const u8, starting_index:usize) void {
    ResetBoardGlobal();

    const inputLength = input.len;
    if (starting_index >= inputLength) {
        return;
    }
    if (starting_index < 0) {
        return;
    }

    var currentPhase:usize = PIECE_PHASE;
    var bracketCount:usize = 0;
    var squareCount:usize = 0;
    var epX:usize = NO_SQUARE;
    var epY:usize = NO_SQUARE;

    for (0..inputLength) |characterIndex| {

        const characterInFen = input[characterIndex];

        switch (currentPhase) {
        0=> {

            if (bracketCount == 7 and characterInFen == ' ') {
                //prf("     next phase brackcount = 7, is empty\n");
                currentPhase+=1;
            } else {

                if (bracketCount > 7) {
                    bracketCount = 0;
                }

                if (squareCount > 7) {
                    squareCount = 0;
                }

                const square:usize = (bracketCount * 8) + squareCount;

                switch (characterInFen) {

                'B' => {
                    //prf("    W bishop found %d\n", square);
                    bitboard_array_global[WB] |= SQUARE_BBS[square];
                    squareCount+=1;
                    
                    },
                'R' => {
                    //prf("    WR found\n");
                    bitboard_array_global[WR] |= SQUARE_BBS[square];
                    squareCount+=1;
                    },
                'P' => {
                    //prf("     add white pawn %d\n", square);
                    bitboard_array_global[WP] |= SQUARE_BBS[square];
                    squareCount+=1;
                    },
                 'Q'=>{
                    //prf("    WQ found\n");
                    bitboard_array_global[WQ] |= SQUARE_BBS[square];
                    squareCount+=1;
                    },
                 'K'=>{
                    //prf("    WK\n");
                    bitboard_array_global[WK] |= SQUARE_BBS[square];
                    squareCount+=1;
                    },
                 'N'=>{
                    //prf("    WN found\n");
                    bitboard_array_global[WN] |= SQUARE_BBS[square];
                    squareCount+=1;
                    },
                 'b'=>{
                    //prf("    BB found\n");
                    bitboard_array_global[BB] |= SQUARE_BBS[square];
                    squareCount+=1;
                    },
                 'p'=>{
                    //prf("    BP sq: %d\n", square);
                    bitboard_array_global[BP] |= SQUARE_BBS[square];
                    squareCount+=1;
                    },
                 'q'=>{
                    //prf("    BQ sq: %d\n", square);
                    bitboard_array_global[BQ] |= SQUARE_BBS[square];
                    squareCount+=1;
                    },
                 'r'=>{
                    //prf("    BR sq: %d\n", square);
                    bitboard_array_global[BR] |= SQUARE_BBS[square];
                    squareCount+=1;
                    },
                 'n'=>{
                    //prf("    BN sq: %d\n", square);
                    bitboard_array_global[BN] |= SQUARE_BBS[square];
                    squareCount+=1;
                    },
                 'k'=>{
                    //prf("    BK sq: %d\n", square);
                    bitboard_array_global[BK] |= SQUARE_BBS[square];
                    squareCount+=1;
                    },
                 '/'=>{
                    //prf("    forward bracket slash\n", square);
                    squareCount = 0;
                    bracketCount+=1;
                    },
                 '1'=>{
                    //prf("    1 found\n");
                    squareCount += 1;
                    },
                 '2'=>{
                    //prf("    2 found\n");
                    squareCount += 2;
                    },
                 '3'=>{
                    //prf("    3 found\n");
                    squareCount += 3;
                    },
                 '4'=>{
                    //prf("    4 found\n");
                    squareCount += 4;
                    },
                 '5'=>{
                    //prf("    5 found\n");
                    squareCount += 5;
                    },
                 '6'=>{
                    //prf("    6 found\n");
                    squareCount += 6;
                    },
                 '7'=>{
                    //prf("    7 found\n");
                    squareCount += 7;
                    },
                 '8'=>{
                    //prf("    8 found\n");
                    squareCount += 8;
                    },
                    else => {
                        unreachable;
                    }
                }
            }
        },
         1 => {
            if (characterInFen == 'w') {
                is_white_global = true;
            }
            else if (characterInFen == 'b') {
                is_white_global = false;
            }
            else if (characterInFen == ' ') {
                currentPhase+=1;
            }
            break;
         },
         2 => {
            switch (characterInFen)
            {
             'K'=>{
                castle_rights_global[0] = true;
             },
             'Q'=>{
                castle_rights_global[1] = true;
                },
             'k'=>{
                castle_rights_global[2] = true;
                },
             'q'=>{
                castle_rights_global[3] = true;
                },
             '-'=>{

                },
             ' '=>{
                currentPhase+=1;
                },
                else => {
                    
                }
            }
         },
         3 => {
            if (characterInFen == ' ') {
                if (epX != -1) {
                    if (epY != -1) {
                        ep_global = (epY * 8) + epX;
                    }
                }
                currentPhase+=1; 
            } else {
                switch (characterInFen) {
                 'a' => {
                    epX = 0;
                 },
                 'b' =>{
                    epX = 1;
                    },
                 'c'=>{
                    epX = 2;
                    },
                 'd'=>{
                    epX = 3;
                    },
                 'e'=>{
                    epX = 4;
                    },
                 'f'=>{
                    epX = 5;
                    },
                 'g'=>{
                    epX = 6;
                    },
                 'h'=>{
                    epX = 7;
                    },
                 '1'=>{
                    epY = 7;
                    },
                 '2'=>{
                    epY = 6;
                    },
                 '3'=>{
                    epY = 5;
                    },
                 '4'=>{
                    epY = 4;
                    },
                 '5'=>{
                    epY = 3;
                    },
                 '6'=>{
                    epY = 2;
                    },
                 '7'=>{
                    epY = 1;
                    },
                 '8'=>{
                    epY = 0;
                    },
                    else => {
                        unreachable;
                    }
                }
            }
         },
         4 =>{
            
         },
         5 => {
            
         },
         else => {
            unreachable;
         },
        }
    }
}

fn Is_Square_Attacked_By_Black_Global(square:usize, occupancy: u64) bool {
    if ((bitboard_array_global[BP] & WHITE_PAWN_ATTACKS[square]) != 0) {
        return true;
    }
    if ((bitboard_array_global[BN] & KNIGHT_ATTACKS[square]) != 0) {
        return true;
    }
    if ((bitboard_array_global[BK] & KING_ATTACKS[square]) != 0) {
        return true;
    }
    const bishopAttacks = GetBishopAttacksFast(square, occupancy);
    if ((bitboard_array_global[BB] & bishopAttacks) != 0) {
        return true;
    }
    if ((bitboard_array_global[BQ] & bishopAttacks) != 0) {
        return true;
    }
    const rookAttacks = GetRookAttacksFast(square, occupancy);
    if ((bitboard_array_global[BR] & rookAttacks) != 0) {
        return true;
    }
    if ((bitboard_array_global[BQ] & rookAttacks) != 0) {
        return true;
    }
    return false;
}

fn Is_Square_Attacked_By_White_Global(square:usize, occupancy:u64) bool {
    if ((bitboard_array_global[WP] & BLACK_PAWN_ATTACKS[square]) != 0) {
        return true;
    }
    if ((bitboard_array_global[WN] & KNIGHT_ATTACKS[square]) != 0) {
        return true;
    }
    if ((bitboard_array_global[WK] & KING_ATTACKS[square]) != 0) {
        return true;
    }
    const bishopAttacks = GetBishopAttacksFast(square, occupancy);
    if ((bitboard_array_global[WB] & bishopAttacks) != 0) {
        return true;
    }
    if ((bitboard_array_global[WQ] & bishopAttacks) != 0) {
        return true;
    }
    const rookAttacks = GetRookAttacksFast(square, occupancy);
    if ((bitboard_array_global[WR] & rookAttacks) != 0) {
        return true;
    }
    if ((bitboard_array_global[WQ] & rookAttacks) != 0) {
        return true;
    }
    return false;
}

fn OutOfBounds(move:usize) bool {
    if (move > 63)
    {
        return true;
    }
    return false;
}

fn PrMoveNoNL(starting_square:usize, target_square:usize) void {    //starting
    if (OutOfBounds(starting_square) == true) {
        std.debug.print("{}", .{starting_square});
    } else {
        std.debug.print("{c}{c}", .{SQ_CHAR_X[starting_square], SQ_CHAR_Y[starting_square]}); 
    }
    if (OutOfBounds(target_square) == true) {
        std.debug.print("{}", .{target_square});
    } else {
        std.debug.print("{c}{c}", .{SQ_CHAR_X[target_square], SQ_CHAR_Y[target_square]}); 
    }
}

fn perftInline(depth:i8, ply:u8) usize {

    //if (depth == 0)
    //{
    //    return 1;
    //}

    var move_list:[50][4]usize = undefined;
    var move_count:usize = 0;

    //Move generating variables
    const WHITE_OCCUPANCIES_LOCAL:u64 = board.bitboard_array_global[0] | board.bitboard_array_global[1] | board.bitboard_array_global[2] | board.bitboard_array_global[3] | board.bitboard_array_global[4] | board.bitboard_array_global[5];
    const BLACK_OCCUPANCIES_LOCAL:u64 = board.bitboard_array_global[6] | board.bitboard_array_global[7] | board.bitboard_array_global[8] | board.bitboard_array_global[9] | board.bitboard_array_global[10] | board.bitboard_array_global[11];
    const COMBINED_OCCUPANCIES_LOCAL:u64 = WHITE_OCCUPANCIES_LOCAL | BLACK_OCCUPANCIES_LOCAL;
    const EMPTY_OCCUPANCIES:u64 = ~COMBINED_OCCUPANCIES_LOCAL;
    var temp_bitboard:u64 = undefined;
    var check_bitboard:u64 = 0;
    var temp_pin_bitboard:u64 = undefined; 
    var temp_attack:u64 = undefined; 
    var temp_empty:u64 = undefined; 
    var temp_captures:u64 = undefined;
    var starting_square:usize = gen_const.NO_SQUARE;
    var target_square:usize = gen_const.NO_SQUARE;

    var pinArray:[8][2]usize = [8][2]usize {
        .{ gen_const.NO_SQUARE, gen_const.NO_SQUARE },
        .{ gen_const.NO_SQUARE, gen_const.NO_SQUARE },
        .{ gen_const.NO_SQUARE, gen_const.NO_SQUARE },
        .{ gen_const.NO_SQUARE, gen_const.NO_SQUARE },
        .{ gen_const.NO_SQUARE, gen_const.NO_SQUARE },
        .{ gen_const.NO_SQUARE, gen_const.NO_SQUARE },
        .{ gen_const.NO_SQUARE, gen_const.NO_SQUARE },
        .{ gen_const.NO_SQUARE, gen_const.NO_SQUARE},
    };

    var pinNumber:usize = 0;

    if (board.is_white_global == true) {
        var whiteKingCheckCount:usize = 0;
        const whiteKingPosition:usize = gen_const.DEBRUIJN64[gen_const.MAGIC *% (board.bitboard_array_global[gen_const.WK] ^ (board.bitboard_array_global[gen_const.WK] - 1)) >> 58]; 

        //pawns
        temp_bitboard = board.bitboard_array_global[gen_const.BP] & move_constants.WHITE_PAWN_ATTACKS[whiteKingPosition];
        if (temp_bitboard != 0) {
            const pawn_square:usize = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
            check_bitboard = move_constants.SQUARE_BBS[pawn_square];
            
            whiteKingCheckCount+=1;
        }

        //knights
        temp_bitboard = board.bitboard_array_global[gen_const.BN] & move_constants.KNIGHT_ATTACKS[whiteKingPosition];
        if (temp_bitboard != 0) {
            const knight_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
            check_bitboard = move_constants.SQUARE_BBS[knight_square];
            
            whiteKingCheckCount+=1;
        }

        //bishops
        const  bishopAttacksChecks = gen_moves.getBishopAttacksFast(whiteKingPosition, BLACK_OCCUPANCIES_LOCAL);
        temp_bitboard = board.bitboard_array_global[gen_const.BB] & bishopAttacksChecks;
        while (temp_bitboard != 0) {
            const  piece_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square] & WHITE_OCCUPANCIES_LOCAL;

            if (temp_pin_bitboard == 0) {
                check_bitboard = constants.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square];
                whiteKingCheckCount+=1;
            } else {
                const pinned_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_pin_bitboard ^ (temp_pin_bitboard - 1)) >> 58];
                temp_pin_bitboard &= temp_pin_bitboard - 1;

                if (temp_pin_bitboard == 0) {
                    pinArray[pinNumber][gen_const.PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][gen_const.PINNING_PIECE_INDEX] = piece_square;
                    pinNumber+=1;
                }
            }
            temp_bitboard &= temp_bitboard - 1;
        }

        //queen
        temp_bitboard = board.bitboard_array_global[gen_const.BQ] & bishopAttacksChecks;
        while (temp_bitboard != 0) {
            const  piece_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];

            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square] & WHITE_OCCUPANCIES_LOCAL;

            if (temp_pin_bitboard == 0) {
                check_bitboard = constants.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square];
                whiteKingCheckCount+=1;
            } else {
                const  pinned_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_pin_bitboard ^ (temp_pin_bitboard - 1)) >> 58];
                temp_pin_bitboard &= temp_pin_bitboard - 1;

                if (temp_pin_bitboard == 0) {
                    pinArray[pinNumber][gen_const.PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][gen_const.PINNING_PIECE_INDEX] = piece_square;
                    pinNumber+=1;
                }
            }
            temp_bitboard &= temp_bitboard - 1;
        }

        //rook
        const  rook_attacks = gen_moves.getRookAttacksFast(whiteKingPosition, BLACK_OCCUPANCIES_LOCAL);
        temp_bitboard = board.bitboard_array_global[gen_const.BR] & rook_attacks;
        while (temp_bitboard != 0)
        {
            const  piece_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square] & WHITE_OCCUPANCIES_LOCAL;

            if (temp_pin_bitboard == 0) {
                check_bitboard = constants.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square];
                whiteKingCheckCount+=1;
            } else {
                const  pinned_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_pin_bitboard ^ (temp_pin_bitboard - 1)) >> 58];
                temp_pin_bitboard &= temp_pin_bitboard - 1;

                if (temp_pin_bitboard == 0) {
                    pinArray[pinNumber][gen_const.PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][gen_const.PINNING_PIECE_INDEX] = piece_square;
                    pinNumber+=1;
                }
            }
            temp_bitboard &= temp_bitboard - 1;
        }

        //queen
        temp_bitboard = board.bitboard_array_global[gen_const.BQ] & rook_attacks;
        while (temp_bitboard != 0)
        {
            const  piece_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square] & WHITE_OCCUPANCIES_LOCAL;

            if (temp_pin_bitboard == 0) {
                check_bitboard = constants.INBETWEEN_BITBOARDS[whiteKingPosition][piece_square];
                whiteKingCheckCount+=1;
            } else {
                const  pinned_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_pin_bitboard ^ (temp_pin_bitboard - 1)) >> 58];
                temp_pin_bitboard &= temp_pin_bitboard - 1;

                if (temp_pin_bitboard == 0) {
                    pinArray[pinNumber][gen_const.PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][gen_const.PINNING_PIECE_INDEX] = piece_square;
                    pinNumber+=1;
                }
            }
            temp_bitboard &= temp_bitboard - 1;
        }
 
        if (whiteKingCheckCount > 1) {
            const occupanciesWithoutWhiteKing = COMBINED_OCCUPANCIES_LOCAL & (~board.bitboard_array_global[gen_const.WK]);
            temp_attack = move_constants.KING_ATTACKS[whiteKingPosition];
            temp_empty = temp_attack & EMPTY_OCCUPANCIES;
            while (temp_empty != 0) {
                target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_empty ^ (temp_empty - 1)) >> 58];
                temp_empty &= temp_empty - 1;

                if ((board.bitboard_array_global[gen_const.BP] & move_constants.WHITE_PAWN_ATTACKS[target_square]) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.BN] & move_constants.KNIGHT_ATTACKS[target_square]) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.BK] & move_constants.KING_ATTACKS[target_square]) != 0) {
                    continue;
                }
                const bishopAttacks = gen_moves.getBishopAttacksFast(target_square, occupanciesWithoutWhiteKing);
                if ((board.bitboard_array_global[gen_const.BB] & bishopAttacks) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.BQ] & bishopAttacks) != 0) {
                    continue;
                }
                const rookAttacks = gen_moves.getRookAttacksFast(target_square, occupanciesWithoutWhiteKing);
                if ((board.bitboard_array_global[gen_const.BR] & rookAttacks) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.BQ] & rookAttacks) != 0) {
                    continue;
                }

                move_list[move_count][gen_const.MOVE_STARTING] = whiteKingPosition;
                move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_NONE;
                move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WK;
                move_count+=1;
            }

            //captures
            temp_captures = temp_attack & BLACK_OCCUPANCIES_LOCAL;
            while (temp_captures != 0) {
                target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_captures ^ (temp_captures - 1)) >> 58];
                temp_captures &= temp_captures - 1;

                if ((board.bitboard_array_global[gen_const.BP] & move_constants.WHITE_PAWN_ATTACKS[target_square]) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.BN] & move_constants.KNIGHT_ATTACKS[target_square]) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.BK] & move_constants.KING_ATTACKS[target_square]) != 0) {
                    continue;
                }
                const bishopAttacks = gen_moves.getBishopAttacksFast(target_square, occupanciesWithoutWhiteKing);
                if ((board.bitboard_array_global[gen_const.BB] & bishopAttacks) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.BQ] & bishopAttacks) != 0) {
                    continue;
                }
                const  rookAttacks = gen_moves.getRookAttacksFast(target_square, occupanciesWithoutWhiteKing);
                if ((board.bitboard_array_global[gen_const.BR] & rookAttacks) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.BQ] & rookAttacks) != 0) {
                    continue;
                }

                move_list[move_count][gen_const.MOVE_STARTING] = whiteKingPosition;
                move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_CAPTURE;
                move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WK;
                move_count+=1;
            }
        } else {

            if (whiteKingCheckCount == 0) {
                check_bitboard = gen_const.MAX_ULONG;
            }

            const  occupanciesWithoutWhiteKing = COMBINED_OCCUPANCIES_LOCAL & (~board.bitboard_array_global[gen_const.WK]);
            temp_attack = move_constants.KING_ATTACKS[whiteKingPosition];
            temp_empty = temp_attack & EMPTY_OCCUPANCIES;
            while (temp_empty != 0) {
                target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_empty ^ (temp_empty - 1)) >> 58];
                temp_empty &= temp_empty - 1;

                if ((board.bitboard_array_global[gen_const.BP] & move_constants.WHITE_PAWN_ATTACKS[target_square]) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.BN] & move_constants.KNIGHT_ATTACKS[target_square]) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.BK] & move_constants.KING_ATTACKS[target_square]) != 0) {
                    continue;
                }
                const bishopAttacks = gen_moves.getBishopAttacksFast(target_square, occupanciesWithoutWhiteKing);
                if ((board.bitboard_array_global[gen_const.BB] & bishopAttacks) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.BQ] & bishopAttacks) != 0) {
                    continue;
                }
                const rookAttacks = gen_moves.getRookAttacksFast(target_square, occupanciesWithoutWhiteKing);
                if ((board.bitboard_array_global[gen_const.BR] & rookAttacks) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.BQ] & rookAttacks) != 0) {
                    continue;
                }

                move_list[move_count][gen_const.MOVE_STARTING] = whiteKingPosition;
                move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_NONE;
                move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WK;
                move_count+=1;
            }

            //captures
            temp_captures = temp_attack & BLACK_OCCUPANCIES_LOCAL;
            while (temp_captures != 0) {
                target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_captures ^ (temp_captures - 1)) >> 58];
                temp_captures &= temp_captures - 1;

                if ((board.bitboard_array_global[gen_const.BP] & move_constants.WHITE_PAWN_ATTACKS[target_square]) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.BN] & move_constants.KNIGHT_ATTACKS[target_square]) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.BK] & move_constants.KING_ATTACKS[target_square]) != 0) {
                    continue;
                }
                const bishopAttacks = gen_moves.getBishopAttacksFast(target_square, occupanciesWithoutWhiteKing);
                if ((board.bitboard_array_global[gen_const.BB] & bishopAttacks) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.BQ] & bishopAttacks) != 0) {
                    continue;
                }
                const rookAttacks = gen_moves.getRookAttacksFast(target_square, occupanciesWithoutWhiteKing);
                if ((board.bitboard_array_global[gen_const.BR] & rookAttacks) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.BQ] & rookAttacks) != 0) {
                    continue;
                }

                move_list[move_count][gen_const.MOVE_STARTING] = whiteKingPosition;
                move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_CAPTURE;
                move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WK;
                move_count+=1;
            }

            if (whiteKingCheckCount == 0) {
                if (board.castle_rights_global[gen_const.WKS_CASTLE_RIGHTS] == true) {
                    if (whiteKingPosition == gen_const.E1) { //king on e1
                        if ((gen_const.WKS_EMPTY_BITBOARD & COMBINED_OCCUPANCIES_LOCAL) == 0) { //f1 and g1 empty
                            if ((board.bitboard_array_global[gen_const.WR] & move_constants.SQUARE_BBS[gen_const.H1]) != 0) { //rook on h1
                                if (board.isSquareAttackedByBlack(gen_const.F1, COMBINED_OCCUPANCIES_LOCAL) == false) {
                                    if (board.isSquareAttackedByBlack(gen_const.G1, COMBINED_OCCUPANCIES_LOCAL) == false) {
                                        move_list[move_count][gen_const.MOVE_STARTING] = gen_const.E1;
                                        move_list[move_count][gen_const.MOVE_TARGET] = gen_const.G1;
                                        move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_WCASTLEKS;
                                        move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WK;
                                        move_count+=1;
                                    }
                                }
                            }
                        }
                    }
                }
                if (board.castle_rights_global[gen_const.WQS_CASTLE_RIGHTS] == true) {
                    if (whiteKingPosition == gen_const.E1) { //king on e1
                        if ((gen_const.WQS_EMPTY_BITBOARD & COMBINED_OCCUPANCIES_LOCAL) == 0) { //f1 and g1 empty
                            if ((board.bitboard_array_global[gen_const.WR] & move_constants.SQUARE_BBS[gen_const.A1]) != 0) { //rook on h1
                                if (board.isSquareAttackedByBlack(gen_const.C1, COMBINED_OCCUPANCIES_LOCAL) == false) {
                                    if (board.isSquareAttackedByBlack(gen_const.D1, COMBINED_OCCUPANCIES_LOCAL) == false) {
                                        move_list[move_count][gen_const.MOVE_STARTING] = gen_const.E1;
                                        move_list[move_count][gen_const.MOVE_TARGET] = gen_const.C1;
                                        move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_WCASTLEQS;
                                        move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WK;
                                        move_count+=1;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            temp_bitboard = board.bitboard_array_global[gen_const.WN];

            while (temp_bitboard != 0) {
                starting_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
                temp_bitboard &= temp_bitboard - 1; //removes the knight from that square to not infinitely loop

                temp_pin_bitboard = gen_const.MAX_ULONG;
                if (pinNumber != 0) {
                    for (0..pinNumber) |i| {
                        if (pinArray[i][gen_const.PINNED_SQUARE_INDEX] == starting_square) {
                            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][gen_const.PINNING_PIECE_INDEX]];
                        }
                    }
                }

                temp_attack = ((move_constants.KNIGHT_ATTACKS[starting_square] & BLACK_OCCUPANCIES_LOCAL) & check_bitboard) & temp_pin_bitboard; //gets knight captures
                while (temp_attack != 0) {
                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                    temp_attack &= temp_attack - 1;

                    move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                    move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_CAPTURE;
                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WN;
                    move_count+=1;
                }

                temp_attack = ((move_constants.KNIGHT_ATTACKS[starting_square] & EMPTY_OCCUPANCIES) & check_bitboard) & temp_pin_bitboard;

                while (temp_attack != 0) {
                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                    temp_attack &= temp_attack - 1;

                    move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                    move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_NONE;
                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WN;
                    move_count+=1;
                }
            }

            temp_bitboard = board.bitboard_array_global[gen_const.WP];

            while (temp_bitboard != 0) {
                starting_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
                temp_bitboard &= temp_bitboard - 1;

                temp_pin_bitboard = gen_const.MAX_ULONG;
                if (pinNumber != 0) {
                    for (0..pinNumber) |i| {
                        if (pinArray[i][gen_const.PINNED_SQUARE_INDEX] == starting_square) {
                            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][gen_const.PINNING_PIECE_INDEX]];
                        }
                    }
                }

                if ((move_constants.SQUARE_BBS[starting_square - 8] & COMBINED_OCCUPANCIES_LOCAL) == 0) { //if up one square is empty 
                    if (((move_constants.SQUARE_BBS[starting_square - 8] & check_bitboard) & temp_pin_bitboard) != 0) {
                        if ((move_constants.SQUARE_BBS[starting_square] & gen_const.RANK_7_BITBOARD) != 0) { //if promotion
                            move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                            move_list[move_count][gen_const.MOVE_TARGET] = starting_square - 8;
                            move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_W_Q_PROMOTION;
                            move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WP;
                            move_count+=1;

                            move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                            move_list[move_count][gen_const.MOVE_TARGET] = starting_square - 8;
                            move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_W_R_PROMOTION;
                            move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WP;
                            move_count+=1;

                            move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                            move_list[move_count][gen_const.MOVE_TARGET] = starting_square - 8;
                            move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_W_B_PROMOTION;
                            move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WP;
                            move_count+=1;

                            move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                            move_list[move_count][gen_const.MOVE_TARGET] = starting_square - 8;
                            move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_W_N_PROMOTION;
                            move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WP;
                            move_count+=1;

                        } else {
                            move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                            move_list[move_count][gen_const.MOVE_TARGET] = starting_square - 8;
                            move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_NONE;
                            move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WP;
                            move_count+=1;
                        }
                    }

                    if ((move_constants.SQUARE_BBS[starting_square] & gen_const.RANK_2_BITBOARD) != 0) { //if on rank 2
                        if (((move_constants.SQUARE_BBS[starting_square - 16] & check_bitboard) & temp_pin_bitboard) != 0) { //if not pinned or 
                            if (((move_constants.SQUARE_BBS[starting_square - 16]) & COMBINED_OCCUPANCIES_LOCAL) == 0) { //if up two squares and one square are empty
                                move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                                move_list[move_count][gen_const.MOVE_TARGET] = starting_square - 16;
                                move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_W_DOUBLE_PAWN;
                                move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WP;
                                move_count+=1;
                            }
                        }
                    }
                }

                temp_attack = ((move_constants.WHITE_PAWN_ATTACKS[starting_square] & BLACK_OCCUPANCIES_LOCAL) & check_bitboard) & temp_pin_bitboard; //if black piece diagonal to pawn

                while (temp_attack != 0) {

                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                    temp_attack &= temp_attack - 1;

                    if ((move_constants.SQUARE_BBS[starting_square] & gen_const.RANK_7_BITBOARD) != 0) { //if promotion

                        move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                        move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                        move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_W_Q_PROMOTION_CAP;
                        move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WP;
                        move_count+=1;

                        move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                        move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                        move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_W_R_PROMOTION_CAP;
                        move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WP;
                        move_count+=1;

                        move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                        move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                        move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_W_B_PROMOTION_CAP;
                        move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WP;
                        move_count+=1;

                        move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                        move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                        move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_W_N_PROMOTION_CAP;
                        move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WP;
                        move_count+=1;
                    } else {
                        move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                        move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                        move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_CAPTURE;
                        move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WP;
                        move_count+=1;
                    }
                }

                if ((move_constants.SQUARE_BBS[starting_square] & gen_const.RANK_5_BITBOARD) != 0) { //check rank for ep
                    if (board.ep_global != gen_const.NO_SQUARE) {
                        if ((((move_constants.WHITE_PAWN_ATTACKS[starting_square] & move_constants.SQUARE_BBS[board.ep_global]) & check_bitboard) & temp_pin_bitboard) != 0) {
                            if ((board.bitboard_array_global[gen_const.WK] & gen_const.RANK_5_BITBOARD) == 0) { //if no king on rank 5
                                move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                                move_list[move_count][gen_const.MOVE_TARGET] = board.ep_global;
                                move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_WHITE_EP;
                                move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WP;
                                move_count+=1;
                            } else if ((board.bitboard_array_global[gen_const.BR] & gen_const.RANK_5_BITBOARD) == 0 and (board.bitboard_array_global[gen_const.BQ] & gen_const.RANK_5_BITBOARD) == 0) { // if no b rook or queen on rank 5
                                move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                                move_list[move_count][gen_const.MOVE_TARGET] = board.ep_global;
                                move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_WHITE_EP;
                                move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WP;
                                move_count+=1;
                            } else { //wk and br or bq on rank 5
                                var occupancyWithoutEPPawns:u64 = COMBINED_OCCUPANCIES_LOCAL & ~move_constants.SQUARE_BBS[starting_square];
                                occupancyWithoutEPPawns &= ~move_constants.SQUARE_BBS[board.ep_global + 8];

                                const  rookAttacksFromKing = gen_moves.getRookAttacksFast(whiteKingPosition, occupancyWithoutEPPawns);

                                if ((rookAttacksFromKing & board.bitboard_array_global[gen_const.BR]) == 0) {
                                    if ((rookAttacksFromKing & board.bitboard_array_global[gen_const.BQ]) == 0) {
                                        move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                                        move_list[move_count][gen_const.MOVE_TARGET] = board.ep_global;
                                        move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_WHITE_EP;
                                        move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WP;
                                        move_count+=1;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            temp_bitboard = board.bitboard_array_global[gen_const.WR];
            while (temp_bitboard != 0) {
                starting_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
                temp_bitboard &= temp_bitboard - 1;

                temp_pin_bitboard = gen_const.MAX_ULONG;
                if (pinNumber != 0) {
                    for (0..pinNumber) |i| {
                        if (pinArray[i][gen_const.PINNED_SQUARE_INDEX] == starting_square) {
                            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][gen_const.PINNING_PIECE_INDEX]];
                        }
                    }
                }

                const  rookAttacks = gen_moves.getRookAttacksFast(starting_square, COMBINED_OCCUPANCIES_LOCAL);

                temp_attack = ((rookAttacks & BLACK_OCCUPANCIES_LOCAL) & check_bitboard) & temp_pin_bitboard;
                while (temp_attack != 0) {
                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                    temp_attack &= temp_attack - 1;

                    move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                    move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_CAPTURE;
                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WR;
                    move_count+=1;
                }

                temp_attack = ((rookAttacks & EMPTY_OCCUPANCIES) & check_bitboard) & temp_pin_bitboard;
                while (temp_attack != 0) {
                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                    temp_attack &= temp_attack - 1;

                    move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                    move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_NONE;
                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WR;
                    move_count+=1;
                }
            }

            temp_bitboard = board.bitboard_array_global[gen_const.WB];
            while (temp_bitboard != 0) {
                starting_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
                temp_bitboard &= temp_bitboard - 1;

                temp_pin_bitboard = gen_const.MAX_ULONG;
                if (pinNumber != 0) {
                    for (0..pinNumber) |i| {
                        if (pinArray[i][gen_const.PINNED_SQUARE_INDEX] == starting_square) {
                            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][gen_const.PINNING_PIECE_INDEX]];
                        }
                    }
                }

                const bishopAttacks = gen_moves.getBishopAttacksFast(starting_square, COMBINED_OCCUPANCIES_LOCAL);

                temp_attack = ((bishopAttacks & BLACK_OCCUPANCIES_LOCAL) & check_bitboard) & temp_pin_bitboard;
                while (temp_attack != 0) {
                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                    temp_attack &= temp_attack - 1;

                    move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                    move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_CAPTURE;
                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WB;
                    move_count+=1;
                }

                temp_attack = ((bishopAttacks & EMPTY_OCCUPANCIES) & check_bitboard) & temp_pin_bitboard;
                while (temp_attack != 0) {
                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                    temp_attack &= temp_attack - 1;

                    move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                    move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_NONE;
                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WB;
                    move_count+=1;
                }
            }

            temp_bitboard = board.bitboard_array_global[gen_const.WQ];
            while (temp_bitboard != 0) {
                starting_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
                temp_bitboard &= temp_bitboard - 1;

                temp_pin_bitboard = gen_const.MAX_ULONG;
                if (pinNumber != 0) {
                    for (0..pinNumber) |i| {
                        if (pinArray[i][gen_const.PINNED_SQUARE_INDEX] == starting_square) {
                            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][gen_const.PINNING_PIECE_INDEX]];
                        }
                    }
                }

                var queenAttacks:u64 = gen_moves.getRookAttacksFast(starting_square, COMBINED_OCCUPANCIES_LOCAL);
                queenAttacks |= gen_moves.getBishopAttacksFast(starting_square, COMBINED_OCCUPANCIES_LOCAL);

                temp_attack = ((queenAttacks & BLACK_OCCUPANCIES_LOCAL) & check_bitboard) & temp_pin_bitboard;

                while (temp_attack != 0) {
                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                    temp_attack &= temp_attack - 1;

                    move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                    move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_CAPTURE;
                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WQ;
                    move_count+=1;
                }

                temp_attack = ((queenAttacks & EMPTY_OCCUPANCIES) & check_bitboard) & temp_pin_bitboard;
                while (temp_attack != 0) {
                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                    temp_attack &= temp_attack - 1;

                    move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                    move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_NONE;
                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.WQ;
                    move_count+=1;
                }
            }
        }
    } else { //black move
         var blackKingCheckCount:usize = 0;
         const blackKingPosition:usize = gen_const.DEBRUIJN64[gen_const.MAGIC *% (board.bitboard_array_global[gen_const.BK] ^ (board.bitboard_array_global[gen_const.BK] - 1)) >> 58];

        //pawns
        temp_bitboard = board.bitboard_array_global[gen_const.WP] & move_constants.BLACK_PAWN_ATTACKS[blackKingPosition];
        if (temp_bitboard != 0) {
            const  pawn_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
            check_bitboard = move_constants.SQUARE_BBS[pawn_square];
            blackKingCheckCount+=1;
        }

        //knights
        temp_bitboard = board.bitboard_array_global[gen_const.WN] & move_constants.KNIGHT_ATTACKS[blackKingPosition];
        if (temp_bitboard != 0) {
            const knight_square:usize = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
            check_bitboard = move_constants.SQUARE_BBS[knight_square];
            blackKingCheckCount+=1;
        }

        //bishops
        const  bishopAttacksChecks = gen_moves.getBishopAttacksFast(blackKingPosition, WHITE_OCCUPANCIES_LOCAL);
        temp_bitboard = board.bitboard_array_global[gen_const.WB] & bishopAttacksChecks;
        while (temp_bitboard != 0) {
            const piece_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[blackKingPosition][piece_square] & BLACK_OCCUPANCIES_LOCAL;

            if (temp_pin_bitboard == 0) {
                check_bitboard = constants.INBETWEEN_BITBOARDS[blackKingPosition][piece_square];
                blackKingCheckCount+=1;
            } else {
                const pinned_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_pin_bitboard ^ (temp_pin_bitboard - 1)) >> 58];
                temp_pin_bitboard &= temp_pin_bitboard - 1;

                if (temp_pin_bitboard == 0) {
                    pinArray[pinNumber][gen_const.PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][gen_const.PINNING_PIECE_INDEX] = piece_square;
                    pinNumber+=1;
                }
            }
            temp_bitboard &= temp_bitboard - 1;
        }

        //queen
        temp_bitboard = board.bitboard_array_global[gen_const.WQ] & bishopAttacksChecks;
        while (temp_bitboard != 0) {
            const piece_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[blackKingPosition][piece_square] & BLACK_OCCUPANCIES_LOCAL;

            if (temp_pin_bitboard == 0) {
                check_bitboard = constants.INBETWEEN_BITBOARDS[blackKingPosition][piece_square];
                blackKingCheckCount+=1;
            } else {
                const pinned_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_pin_bitboard ^ (temp_pin_bitboard - 1)) >> 58];
                temp_pin_bitboard &= temp_pin_bitboard - 1;

                if (temp_pin_bitboard == 0) {
                    pinArray[pinNumber][gen_const.PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][gen_const.PINNING_PIECE_INDEX] = piece_square;
                    pinNumber+=1;
                }
            }
            temp_bitboard &= temp_bitboard - 1;
        }

        //rook
        const rook_attacks = gen_moves.getRookAttacksFast(blackKingPosition, WHITE_OCCUPANCIES_LOCAL);
        temp_bitboard = board.bitboard_array_global[gen_const.WR] & rook_attacks;
        while (temp_bitboard != 0) {
            const piece_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[blackKingPosition][piece_square] & BLACK_OCCUPANCIES_LOCAL;

            if (temp_pin_bitboard == 0) {
                check_bitboard = constants.INBETWEEN_BITBOARDS[blackKingPosition][piece_square];
                blackKingCheckCount+=1;
            } else {
                const pinned_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_pin_bitboard ^ (temp_pin_bitboard - 1)) >> 58];
                temp_pin_bitboard &= temp_pin_bitboard - 1;

                if (temp_pin_bitboard == 0)
                {
                    pinArray[pinNumber][gen_const.PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][gen_const.PINNING_PIECE_INDEX] = piece_square;
                    pinNumber+=1;
                }
            }
            temp_bitboard &= temp_bitboard - 1;
        }

        //queen
        temp_bitboard = board.bitboard_array_global[gen_const.WQ] & rook_attacks;
        while (temp_bitboard != 0) {
            const piece_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[blackKingPosition][piece_square] & BLACK_OCCUPANCIES_LOCAL;

            if (temp_pin_bitboard == 0) {
                check_bitboard = constants.INBETWEEN_BITBOARDS[blackKingPosition][piece_square];
                blackKingCheckCount+=1;
            } else {
                const pinned_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_pin_bitboard ^ (temp_pin_bitboard - 1)) >> 58];
                temp_pin_bitboard &= temp_pin_bitboard - 1;

                if (temp_pin_bitboard == 0)
                {
                    pinArray[pinNumber][gen_const.PINNED_SQUARE_INDEX] = pinned_square;
                    pinArray[pinNumber][gen_const.PINNING_PIECE_INDEX] = piece_square;
                    pinNumber+=1;
                }
            }
            temp_bitboard &= temp_bitboard - 1;
        }


        if (blackKingCheckCount > 1) {
            const  occupancyWithoutBlackKing = COMBINED_OCCUPANCIES_LOCAL & (~board.bitboard_array_global[gen_const.BK]);
            temp_attack = move_constants.KING_ATTACKS[blackKingPosition] & WHITE_OCCUPANCIES_LOCAL;

            while (temp_attack != 0) {
                target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                temp_attack &= temp_attack - 1;

                if ((board.bitboard_array_global[gen_const.WP] & move_constants.BLACK_PAWN_ATTACKS[target_square]) != 0) {
                    continue;
                } 
                if ((board.bitboard_array_global[gen_const.WN] & move_constants.KNIGHT_ATTACKS[target_square]) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.WK] & move_constants.KING_ATTACKS[target_square]) != 0) {
                    continue;
                }
                const bishopAttacks = gen_moves.getBishopAttacksFast(target_square, occupancyWithoutBlackKing);
                if ((board.bitboard_array_global[gen_const.WB] & bishopAttacks) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.WQ] & bishopAttacks) != 0) {
                    continue;
                }
                const rookAttacks = gen_moves.getRookAttacksFast(target_square, occupancyWithoutBlackKing);
                if ((board.bitboard_array_global[gen_const.WR] & rookAttacks) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.WQ] & rookAttacks) != 0) {
                    continue;
                }

                move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_CAPTURE;
                move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BK;
                move_count+=1;
            }

            temp_attack = move_constants.KING_ATTACKS[blackKingPosition] & ~COMBINED_OCCUPANCIES_LOCAL;

            while (temp_attack != 0) {
                target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                temp_attack &= temp_attack - 1;

                if ((board.bitboard_array_global[gen_const.WP] & move_constants.WHITE_PAWN_ATTACKS[target_square]) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.WN] & move_constants.KNIGHT_ATTACKS[target_square]) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.WK] & move_constants.KING_ATTACKS[target_square]) != 0) {
                    continue;
                }
                const bishopAttacks = gen_moves.getBishopAttacksFast(target_square, occupancyWithoutBlackKing);
                if ((board.bitboard_array_global[gen_const.WB] & bishopAttacks) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.WQ] & bishopAttacks) != 0) {
                    continue;
                }
                const rookAttacks = gen_moves.getRookAttacksFast(target_square, occupancyWithoutBlackKing);
                if ((board.bitboard_array_global[gen_const.WR] & rookAttacks) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.WQ] & rookAttacks) != 0) {
                    continue;
                }

                move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_NONE;
                move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BK;
                move_count+=1;
            }
        } else {
            if (blackKingCheckCount == 0) {
                check_bitboard = gen_const.MAX_ULONG;
            }

            temp_bitboard = board.bitboard_array_global[gen_const.BP];

            while (temp_bitboard != 0) {
                starting_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
                temp_bitboard &= temp_bitboard - 1;

                temp_pin_bitboard = gen_const.MAX_ULONG;
                if (pinNumber != 0) {
                    for (0..pinNumber) |i| {
                        if (pinArray[i][gen_const.PINNED_SQUARE_INDEX] == starting_square) {
                            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][gen_const.PINNING_PIECE_INDEX]];
                        }
                    }
                }

                if ((move_constants.SQUARE_BBS[starting_square + 8] & COMBINED_OCCUPANCIES_LOCAL) == 0) { //if up one square is empty
                    if (((move_constants.SQUARE_BBS[starting_square + 8] & check_bitboard) & temp_pin_bitboard) != 0) {
                        if ((move_constants.SQUARE_BBS[starting_square] & gen_const.RANK_2_BITBOARD) != 0) { //if promotion
                        
                            move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                            move_list[move_count][gen_const.MOVE_TARGET] = starting_square + 8;
                            move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_B_B_PROMOTION;
                            move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BP;
                            move_count+=1;

                            move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                            move_list[move_count][gen_const.MOVE_TARGET] = starting_square + 8;
                            move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_B_N_PROMOTION;
                            move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BP;
                            move_count+=1;

                            move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                            move_list[move_count][gen_const.MOVE_TARGET] = starting_square + 8;
                            move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_B_R_PROMOTION;
                            move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BP;
                            move_count+=1;

                            move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                            move_list[move_count][gen_const.MOVE_TARGET] = starting_square + 8;
                            move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_B_Q_PROMOTION;
                            move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BP;
                            move_count+=1;
                        } else {
                            move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                            move_list[move_count][gen_const.MOVE_TARGET] = starting_square + 8;
                            move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_NONE;
                            move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BP;
                            move_count+=1;
                        }
                    }

                    if ((move_constants.SQUARE_BBS[starting_square] & gen_const.RANK_7_BITBOARD) != 0) { //if on rank 2
                        if (((move_constants.SQUARE_BBS[starting_square + 16] & check_bitboard) & temp_pin_bitboard) != 0) {
                            if (((move_constants.SQUARE_BBS[starting_square + 16]) & COMBINED_OCCUPANCIES_LOCAL) == 0) { //if up two squares and one square are empty
                                move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                                move_list[move_count][gen_const.MOVE_TARGET] = starting_square + 16;
                                move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_B_DOUBLE_PAWN;
                                move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BP;
                                move_count+=1;
                            }
                        }
                    }
                }

                temp_attack = ((move_constants.BLACK_PAWN_ATTACKS[starting_square] & WHITE_OCCUPANCIES_LOCAL) & check_bitboard) & temp_pin_bitboard; //if black piece diagonal to pawn

                while (temp_attack != 0) {
                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58]; //find the bit
                    temp_attack &= temp_attack - 1;

                    if ((move_constants.SQUARE_BBS[starting_square] & gen_const.RANK_2_BITBOARD) != 0) { //if promotion
                        move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                        move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                        move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_B_Q_PROMOTION_CAP;
                        move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BP;
                        move_count+=1;

                        move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                        move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                        move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_B_R_PROMOTION_CAP;
                        move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BP;
                        move_count+=1;

                        move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                        move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                        move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_B_N_PROMOTION_CAP;
                        move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BP;
                        move_count+=1;

                        move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                        move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                        move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_B_B_PROMOTION_CAP;
                        move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BP;
                        move_count+=1;
                    } else {
                        move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                        move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                        move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_CAPTURE;
                        move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BP;
                        move_count+=1;
                    }
                }

                if ((move_constants.SQUARE_BBS[starting_square] & gen_const.RANK_4_BITBOARD) != 0) { //check rank for ep
                    if (board.ep_global != gen_const.NO_SQUARE) {
                        if ((((move_constants.BLACK_PAWN_ATTACKS[starting_square] & move_constants.SQUARE_BBS[board.ep_global]) & check_bitboard) & temp_pin_bitboard) != 0) {
                            if ((board.bitboard_array_global[gen_const.BK] & gen_const.RANK_4_BITBOARD) == 0) { //if no king on rank 5
                                move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                                move_list[move_count][gen_const.MOVE_TARGET] = board.ep_global;
                                move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_BLACK_EP;
                                move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BP;
                                move_count+=1;
                            } else if ((board.bitboard_array_global[gen_const.WR] & gen_const.RANK_4_BITBOARD) == 0 and (board.bitboard_array_global[gen_const.WQ] & gen_const.RANK_4_BITBOARD) == 0) { // if no b rook or queen on rank 5                                 move_list[move_count][MOVE_STARTING] = starting_square;
                                move_list[move_count][gen_const.MOVE_TARGET] = board.ep_global;
                                move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_BLACK_EP;
                                move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BP;
                                move_count+=1;
                            } else { //wk and br or bq on rank 5
                                var occupancyWithoutEPPawns = COMBINED_OCCUPANCIES_LOCAL & ~move_constants.SQUARE_BBS[starting_square];
                                occupancyWithoutEPPawns &= ~move_constants.SQUARE_BBS[board.ep_global - 8];

                                const rookAttacksFromKing = gen_moves.getRookAttacksFast(blackKingPosition, occupancyWithoutEPPawns);

                                if ((rookAttacksFromKing & board.bitboard_array_global[gen_const.WR]) == 0) {
                                    if ((rookAttacksFromKing & board.bitboard_array_global[gen_const.WQ]) == 0) {
                                        move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                                        move_list[move_count][gen_const.MOVE_TARGET] = board.ep_global;
                                        move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_BLACK_EP;
                                        move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BP;
                                        move_count+=1;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            temp_bitboard = board.bitboard_array_global[gen_const.BN];

            while (temp_bitboard != 0) {
                starting_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58]; //looks for the startingSquare
                temp_bitboard &= temp_bitboard - 1; //removes the knight from that square to not infinitely loop

                temp_pin_bitboard = gen_const.MAX_ULONG;
                if (pinNumber != 0) {
                    for (0..pinNumber) |i| {
                        if (pinArray[i][gen_const.PINNED_SQUARE_INDEX] == starting_square) {
                            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][gen_const.PINNING_PIECE_INDEX]];
                        }
                    }
                }

                temp_attack = ((move_constants.KNIGHT_ATTACKS[starting_square] & WHITE_OCCUPANCIES_LOCAL) & check_bitboard) & temp_pin_bitboard; //gets knight captures
                while (temp_attack != 0) {
                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                    temp_attack &= temp_attack - 1;

                    move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                    move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_CAPTURE;
                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BN;
                    move_count+=1;
                }

                temp_attack = ((move_constants.KNIGHT_ATTACKS[starting_square] & (~COMBINED_OCCUPANCIES_LOCAL)) & check_bitboard) & temp_pin_bitboard;

                while (temp_attack != 0) {
                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                    temp_attack &= temp_attack - 1;

                    move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                    move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_NONE;
                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BN;
                    move_count+=1;
                }
            }

            temp_bitboard = board.bitboard_array_global[gen_const.BB];
            while (temp_bitboard != 0) {
                starting_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
                temp_bitboard &= temp_bitboard - 1;

                temp_pin_bitboard = gen_const.MAX_ULONG;
                if (pinNumber != 0) {
                    for (0..pinNumber) |i| {
                        if (pinArray[i][gen_const.PINNED_SQUARE_INDEX] == starting_square) {
                            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][gen_const.PINNING_PIECE_INDEX]];
                        }
                    }
                }

                const  bishopAttacks = gen_moves.getBishopAttacksFast(starting_square, COMBINED_OCCUPANCIES_LOCAL);

                temp_attack = ((bishopAttacks & WHITE_OCCUPANCIES_LOCAL) & check_bitboard) & temp_pin_bitboard;
                while (temp_attack != 0) {
                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                    temp_attack &= temp_attack - 1;

                    move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                    move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_CAPTURE;
                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BB;
                    move_count+=1;
                }

                temp_attack = ((bishopAttacks & (~COMBINED_OCCUPANCIES_LOCAL)) & check_bitboard) & temp_pin_bitboard;
                while (temp_attack != 0) {
                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                    temp_attack &= temp_attack - 1;

                    move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                    move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_NONE;
                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BB;
                    move_count+=1;
                }
            }

            temp_bitboard = board.bitboard_array_global[gen_const.BR];
            while (temp_bitboard != 0) {
                starting_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
                temp_bitboard &= temp_bitboard - 1;

                temp_pin_bitboard = gen_const.MAX_ULONG;
                if (pinNumber != 0) {
                    for (0..pinNumber) |i| {
                        if (pinArray[i][gen_const.PINNED_SQUARE_INDEX] == starting_square) {
                            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][gen_const.PINNING_PIECE_INDEX]];
                        }
                    }
                }

                const rookAttacks = gen_moves.getRookAttacksFast(starting_square, COMBINED_OCCUPANCIES_LOCAL);

                temp_attack = ((rookAttacks & WHITE_OCCUPANCIES_LOCAL) & check_bitboard) & temp_pin_bitboard;
                while (temp_attack != 0) {
                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                    temp_attack &= temp_attack - 1;

                    move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                    move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_CAPTURE;
                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BR;
                    move_count+=1;
                }

                temp_attack = ((rookAttacks & (~COMBINED_OCCUPANCIES_LOCAL)) & check_bitboard) & temp_pin_bitboard;
                while (temp_attack != 0) {
                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                    temp_attack &= temp_attack - 1;

                    move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                    move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_NONE;
                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BR;
                    move_count+=1;
                }
            }

            temp_bitboard = board.bitboard_array_global[gen_const.BQ];
            while (temp_bitboard != 0) {
                starting_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_bitboard ^ (temp_bitboard - 1)) >> 58];
                temp_bitboard &= temp_bitboard - 1;

                temp_pin_bitboard = gen_const.MAX_ULONG;
                if (pinNumber != 0) {
                    for (0..pinNumber) |i| {
                        if (pinArray[i][gen_const.PINNED_SQUARE_INDEX] == starting_square) {
                            temp_pin_bitboard = constants.INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][gen_const.PINNING_PIECE_INDEX]];
                        }
                    }
                }

                var queenAttacks:u64 = gen_moves.getRookAttacksFast(starting_square, COMBINED_OCCUPANCIES_LOCAL);
                queenAttacks |= gen_moves.getBishopAttacksFast(starting_square, COMBINED_OCCUPANCIES_LOCAL);

                temp_attack = ((queenAttacks & WHITE_OCCUPANCIES_LOCAL) & check_bitboard) & temp_pin_bitboard;

                while (temp_attack != 0) {
                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                    temp_attack &= temp_attack - 1;

                    move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                    move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_CAPTURE;
                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BQ;
                    move_count+=1;
                }

                temp_attack = ((queenAttacks & (~COMBINED_OCCUPANCIES_LOCAL)) & check_bitboard) & temp_pin_bitboard;
                while (temp_attack != 0) {
                    target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                    temp_attack &= temp_attack - 1;

                    move_list[move_count][gen_const.MOVE_STARTING] = starting_square;
                    move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_NONE;
                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BQ;
                    move_count+=1;
                }
            }

            temp_attack = move_constants.KING_ATTACKS[blackKingPosition] & WHITE_OCCUPANCIES_LOCAL; //gets knight captures
            while (temp_attack != 0) {
                target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                temp_attack &= temp_attack - 1;

                if ((board.bitboard_array_global[gen_const.WP] & move_constants.BLACK_PAWN_ATTACKS[target_square]) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.WN] & move_constants.KNIGHT_ATTACKS[target_square]) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.WK] & move_constants.KING_ATTACKS[target_square]) != 0) {
                    continue;
                }
                const  occupancyWithoutBlackKing = COMBINED_OCCUPANCIES_LOCAL & (~board.bitboard_array_global[gen_const.BK]);
                const  bishopAttacks = gen_moves.getBishopAttacksFast(target_square, occupancyWithoutBlackKing);
                if ((board.bitboard_array_global[gen_const.WB] & bishopAttacks) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.WQ] & bishopAttacks) != 0) {
                    continue;
                }
                const  rookAttacks = gen_moves.getRookAttacksFast(target_square, occupancyWithoutBlackKing);
                if ((board.bitboard_array_global[gen_const.WR] & rookAttacks) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.WQ] & rookAttacks) != 0) {
                    continue;
                }

                move_list[move_count][gen_const.MOVE_STARTING] = blackKingPosition;
                move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_CAPTURE;
                move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BK;
                move_count+=1;
            }

            temp_attack = move_constants.KING_ATTACKS[blackKingPosition] & (~COMBINED_OCCUPANCIES_LOCAL); //get knight moves to emtpy squares

            while (temp_attack != 0) {
                target_square = gen_const.DEBRUIJN64[gen_const.MAGIC *% (temp_attack ^ (temp_attack - 1)) >> 58];
                temp_attack &= temp_attack - 1;

                if ((board.bitboard_array_global[gen_const.WP] & move_constants.BLACK_PAWN_ATTACKS[target_square]) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.WN] & move_constants.KNIGHT_ATTACKS[target_square]) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.WK] & move_constants.KING_ATTACKS[target_square]) != 0) {
                    continue;
                }
                const  occupancyWithoutBlackKing = COMBINED_OCCUPANCIES_LOCAL & (~board.bitboard_array_global[gen_const.BK]);
                const  bishopAttacks = gen_moves.getBishopAttacksFast(target_square, occupancyWithoutBlackKing);
                if ((board.bitboard_array_global[gen_const.WB] & bishopAttacks) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.WQ] & bishopAttacks) != 0) {
                    continue;
                }
                const  rookAttacks = gen_moves.getRookAttacksFast(target_square, occupancyWithoutBlackKing);
                if ((board.bitboard_array_global[gen_const.WR] & rookAttacks) != 0) {
                    continue;
                }
                if ((board.bitboard_array_global[gen_const.WQ] & rookAttacks) != 0) {
                    continue;
                }

                move_list[move_count][gen_const.MOVE_STARTING] = blackKingPosition;
                move_list[move_count][gen_const.MOVE_TARGET] = target_square;
                move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_NONE;
                move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BK;
                move_count+=1;
            }
        }
        if (blackKingCheckCount == 0) {
            if (board.castle_rights_global[gen_const.BKS_CASTLE_RIGHTS] == true) {
                if (blackKingPosition == gen_const.E8) {
                    if ((gen_const.BKS_EMPTY_BITBOARD & COMBINED_OCCUPANCIES_LOCAL) == 0) {
                        if ((board.bitboard_array_global[gen_const.BR] & move_constants.SQUARE_BBS[gen_const.H8]) != 0) {
                            if (board.isSquareAttackedByWhiteGlobal(gen_const.F8, COMBINED_OCCUPANCIES_LOCAL) == false) {
                                if (board.isSquareAttackedByWhiteGlobal(gen_const.G8, COMBINED_OCCUPANCIES_LOCAL) == false) {
                                    move_list[move_count][gen_const.MOVE_STARTING] = gen_const.E8;
                                    move_list[move_count][gen_const.MOVE_TARGET] = gen_const.G8;
                                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_BCASTLEKS;
                                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BK;
                                    move_count+=1;
                                }
                            }
                        }
                    }
                }
            }
            if (board.castle_rights_global[gen_const.BQS_CASTLE_RIGHTS] == true) {
                if (blackKingPosition == gen_const.E8) {
                    if ((gen_const.BQS_EMPTY_BITBOARD & COMBINED_OCCUPANCIES_LOCAL) == 0) {
                        if ((board.bitboard_array_global[gen_const.BR] & move_constants.SQUARE_BBS[gen_const.A8]) != 0) {
                            if (board.isSquareAttackedByWhiteGlobal(gen_const.C8, COMBINED_OCCUPANCIES_LOCAL) == false) {
                                if (board.isSquareAttackedByWhiteGlobal(gen_const.D8, COMBINED_OCCUPANCIES_LOCAL) == false) {
                                    move_list[move_count][gen_const.MOVE_STARTING] = gen_const.E8;
                                    move_list[move_count][gen_const.MOVE_TARGET] = gen_const.C8;
                                    move_list[move_count][gen_const.MOVE_TAG] = gen_const.TAG_BCASTLEQS;
                                    move_list[move_count][gen_const.MOVE_PIECE] = gen_const.BK;
                                    move_count+=1;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (depth == 1) {
        return move_count;
    }

    var nodes:usize = 0;
    var priorNodes:usize = undefined;
    const copyEp = board.ep_global;
    const copy_castle :[4]bool = [4]bool {
        board.castle_rights_global[0],
        board.castle_rights_global[1],
        board.castle_rights_global[2],
        board.castle_rights_global[3],
    };

    for (0..move_count) |move_index| {

        const  startingSquare = move_list[move_index][gen_const.MOVE_STARTING];
        const  targetSquare = move_list[move_index][gen_const.MOVE_TARGET];
        const  piece = move_list[move_index][gen_const.MOVE_PIECE];
        const  tag = move_list[move_index][gen_const.MOVE_TAG];

        var captureIndex:usize = gen_const.NO_SQUARE;

        if (board.is_white_global == true) {
            board.is_white_global = false;
        } else {
            board.is_white_global = true;
        }

        switch (tag) {
         0, 26 => { //none
            board.bitboard_array_global[piece] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = gen_const.NO_SQUARE;
            },
         1, 27 => { //check cap
            board.bitboard_array_global[piece] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];
            if (piece >= gen_const.WP and piece <= gen_const.WK) {
                for (6..12) |i| {
                    if ((board.bitboard_array_global[i] & move_constants.SQUARE_BBS[targetSquare]) != 0) {
                        captureIndex = i;
                        break;
                    }
                }
                board.bitboard_array_global[captureIndex] &= ~move_constants.SQUARE_BBS[targetSquare];

            } else { //is black
                for (gen_const.WP..gen_const.BP) |i| {
                    if ((board.bitboard_array_global[i] & move_constants.SQUARE_BBS[targetSquare]) != 0) {
                        captureIndex = i;
                        break;
                    }
                }
                board.bitboard_array_global[captureIndex] &= ~move_constants.SQUARE_BBS[targetSquare];
            }

            board.ep_global = gen_const.NO_SQUARE;
            },
         2 => { //white ep
            //move piece
            board.bitboard_array_global[gen_const.WP] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[gen_const.WP] &= ~move_constants.SQUARE_BBS[startingSquare];
            //remove 
            board.bitboard_array_global[gen_const.BP] &= ~move_constants.SQUARE_BBS[targetSquare + 8];
            board.ep_global = gen_const.NO_SQUARE;
            },
         3 => { //black ep
            //move piece
            board.bitboard_array_global[gen_const.BP] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[gen_const.BP] &= ~move_constants.SQUARE_BBS[startingSquare];
            //remove white pawn square up
            board.bitboard_array_global[gen_const.WP] &= ~move_constants.SQUARE_BBS[targetSquare - 8];
            board.ep_global = gen_const.NO_SQUARE;
            },

         4 => { //WKS
            //white king
            board.bitboard_array_global[gen_const.WK] |= move_constants.SQUARE_BBS[gen_const.G1];
            board.bitboard_array_global[gen_const.WK] &= ~move_constants.SQUARE_BBS[gen_const.E1];
            //white rook
            board.bitboard_array_global[gen_const.WR] |= move_constants.SQUARE_BBS[gen_const.F1];
            board.bitboard_array_global[gen_const.WR] &= ~move_constants.SQUARE_BBS[gen_const.H1];
            board.castle_rights_global[gen_const.WKS_CASTLE_RIGHTS] = false;
            board.castle_rights_global[gen_const.WQS_CASTLE_RIGHTS] = false;
            board.ep_global = gen_const.NO_SQUARE;
            },
         5 => { //WQS
            //white king
            board.bitboard_array_global[gen_const.WK] |= move_constants.SQUARE_BBS[gen_const.C1];
            board.bitboard_array_global[gen_const.WK] &= ~move_constants.SQUARE_BBS[gen_const.E1];
            //white rook
            board.bitboard_array_global[gen_const.WR] |= move_constants.SQUARE_BBS[gen_const.D1];
            board.bitboard_array_global[gen_const.WR] &= ~move_constants.SQUARE_BBS[gen_const.A1];
            board.castle_rights_global[gen_const.WKS_CASTLE_RIGHTS] = false;
            board.castle_rights_global[gen_const.WQS_CASTLE_RIGHTS] = false;
            board.ep_global = gen_const.NO_SQUARE;
            },
         6 => { //BKS
            //white king
            board.bitboard_array_global[gen_const.BK] |= move_constants.SQUARE_BBS[gen_const.G8];
            board.bitboard_array_global[gen_const.BK] &= ~move_constants.SQUARE_BBS[gen_const.E8];
            //white rook
            board.bitboard_array_global[gen_const.BR] |= move_constants.SQUARE_BBS[gen_const.F8];
            board.bitboard_array_global[gen_const.BR] &= ~move_constants.SQUARE_BBS[gen_const.H8];

            board.castle_rights_global[gen_const.BKS_CASTLE_RIGHTS] = false;
            board.castle_rights_global[gen_const.BQS_CASTLE_RIGHTS] = false;
            board.ep_global = gen_const.NO_SQUARE;
            },
         7 => { //BQS
            //white king
            board.bitboard_array_global[gen_const.BK] |= move_constants.SQUARE_BBS[gen_const.C8];
            board.bitboard_array_global[gen_const.BK] &= ~move_constants.SQUARE_BBS[gen_const.E8];
            //white rook
            board.bitboard_array_global[gen_const.BR] |= move_constants.SQUARE_BBS[gen_const.D8];
            board.bitboard_array_global[gen_const.BR] &= ~move_constants.SQUARE_BBS[gen_const.A8];
            board.castle_rights_global[gen_const.BKS_CASTLE_RIGHTS] = false;
            board.castle_rights_global[gen_const.BQS_CASTLE_RIGHTS] = false;
            board.ep_global = gen_const.NO_SQUARE;
            },

         8 => { //BNPr
            board.bitboard_array_global[gen_const.BN] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = gen_const.NO_SQUARE;
            },
         9 => { //BBPr
            board.bitboard_array_global[gen_const.BB] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = gen_const.NO_SQUARE;
            },
         10  => { //BQPr
            board.bitboard_array_global[gen_const.BQ] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = gen_const.NO_SQUARE;
            },
         11  => { //BRPr
            board.bitboard_array_global[gen_const.BR] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = gen_const.NO_SQUARE;
            },
         12  => { //WNPr
            board.bitboard_array_global[gen_const.WN] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = gen_const.NO_SQUARE;
            },
         13 => { //WBPr
            board.bitboard_array_global[gen_const.WB] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = gen_const.NO_SQUARE;
            },
         14 => { //WQPr
            board.bitboard_array_global[gen_const.WQ] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = gen_const.NO_SQUARE;
            },
         15 => { //WRPr
            board.bitboard_array_global[gen_const.WR] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = gen_const.NO_SQUARE;
            },
         16 => { //BNPrCAP
            board.bitboard_array_global[gen_const.BN] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = gen_const.NO_SQUARE;
            for (gen_const.WHITE_START_INDEX..gen_const.BP) |i| {
                if ((board.bitboard_array_global[i] & move_constants.SQUARE_BBS[targetSquare]) != 0) {
                    captureIndex = i;
                    break;
                }
            }
            board.bitboard_array_global[captureIndex] &= ~move_constants.SQUARE_BBS[targetSquare];
            },
         17 => { //BBPrCAP
            board.bitboard_array_global[gen_const.BB] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = gen_const.NO_SQUARE;
            for (gen_const.WHITE_START_INDEX..gen_const.BP) |i| {
                if ((board.bitboard_array_global[i] & move_constants.SQUARE_BBS[targetSquare]) != 0) {
                    captureIndex = i;
                    break;
                }
            }
            board.bitboard_array_global[captureIndex] &= ~move_constants.SQUARE_BBS[targetSquare];
            },
         18 => { //BQPrCAP
            board.bitboard_array_global[gen_const.BQ] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = gen_const.NO_SQUARE;
            for (gen_const.WHITE_START_INDEX..gen_const.BP) |i| {
                if ((board.bitboard_array_global[i] & move_constants.SQUARE_BBS[targetSquare]) != 0) {
                    captureIndex = i;
                    break;
                }
            }
            board.bitboard_array_global[captureIndex] &= ~move_constants.SQUARE_BBS[targetSquare];
            },
         19 => { //BRPrCAP
            board.bitboard_array_global[gen_const.BR] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = gen_const.NO_SQUARE;
            for (gen_const.WHITE_START_INDEX..gen_const.BP) |i| {

                if ((board.bitboard_array_global[i] & move_constants.SQUARE_BBS[targetSquare]) != 0) {
                    captureIndex = i;
                    break;
                }
            }
            board.bitboard_array_global[captureIndex] &= ~move_constants.SQUARE_BBS[targetSquare];
            },
         20 => { //WNPrCAP
            board.bitboard_array_global[gen_const.WN] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = gen_const.NO_SQUARE;
            for (gen_const.BP..12) |i| {
                if ((board.bitboard_array_global[i] & move_constants.SQUARE_BBS[targetSquare]) != 0) {
                    captureIndex = i;
                    break;
                }
            }
            board.bitboard_array_global[captureIndex] &= ~move_constants.SQUARE_BBS[targetSquare];
            },
         21 => { //WBPrCAP
            board.bitboard_array_global[gen_const.WB] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = gen_const.NO_SQUARE;
            for (gen_const.BP..12) |i| {
                if ((board.bitboard_array_global[i] & move_constants.SQUARE_BBS[targetSquare]) != 0) {
                    captureIndex = i;
                    break;
                }
            }
            board.bitboard_array_global[captureIndex] &= ~move_constants.SQUARE_BBS[targetSquare];
            },
         22 => { //WQPrCAP
            board.bitboard_array_global[gen_const.WQ] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = gen_const.NO_SQUARE;
            for (gen_const.BLACK_START_INDEX..12) |i| {
                if ((board.bitboard_array_global[i] & move_constants.SQUARE_BBS[targetSquare]) != 0) {
                    captureIndex = i;
                    break;
                }
            }
            board.bitboard_array_global[captureIndex] &= ~move_constants.SQUARE_BBS[targetSquare];
            },
         23 => { //WRPrCAP
            board.bitboard_array_global[gen_const.WR] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[startingSquare];

            board.ep_global = gen_const.NO_SQUARE;
            for (gen_const.BP..12) |i| {
                if ((board.bitboard_array_global[i] & move_constants.SQUARE_BBS[targetSquare]) != 0) {
                    captureIndex = i;
                    break;
                }
            }
            board.bitboard_array_global[captureIndex] &= ~move_constants.SQUARE_BBS[targetSquare];
            },
         24 => { //WDouble
            board.bitboard_array_global[gen_const.WP] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[gen_const.WP] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = targetSquare + 8;
            },
         25 => { //BDouble
            board.bitboard_array_global[gen_const.BP] |= move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[gen_const.BP] &= ~move_constants.SQUARE_BBS[startingSquare];
            board.ep_global = targetSquare - 8;
            },
        else => {
            unreachable;
        }
        }

        if (piece == gen_const.WK) {
            board.castle_rights_global[gen_const.WKS_CASTLE_RIGHTS] = false;
            board.castle_rights_global[gen_const.WQS_CASTLE_RIGHTS] = false;
        } else if (piece == gen_const.BK) {
            board.castle_rights_global[gen_const.BKS_CASTLE_RIGHTS] = false;
            board.castle_rights_global[gen_const.BQS_CASTLE_RIGHTS] = false;
        } else if (piece == gen_const.WR) {
            if (board.castle_rights_global[gen_const.WKS_CASTLE_RIGHTS] == true) {
                if ((board.bitboard_array_global[gen_const.WR] & move_constants.SQUARE_BBS[gen_const.H1]) == 0) {
                    board.castle_rights_global[gen_const.WKS_CASTLE_RIGHTS] = false;
                }
            }
            if (board.castle_rights_global[gen_const.WQS_CASTLE_RIGHTS] == true) {
                if ((board.bitboard_array_global[gen_const.WR] & move_constants.SQUARE_BBS[gen_const.A1]) == 0){
                    board.castle_rights_global[gen_const.WQS_CASTLE_RIGHTS] = false;
                }
            }
        } else if (piece == gen_const.BR) {
            if (board.castle_rights_global[gen_const.BKS_CASTLE_RIGHTS] == true) {
                if ((board.bitboard_array_global[gen_const.BR] & move_constants.SQUARE_BBS[gen_const.H8]) == 0) {
                    board.castle_rights_global[gen_const.BKS_CASTLE_RIGHTS] = false;
                }
            }
            if (board.castle_rights_global[gen_const.BQS_CASTLE_RIGHTS] == true) {
                if ((board.bitboard_array_global[gen_const.BR] & move_constants.SQUARE_BBS[gen_const.A8]) == 0) {
                    board.castle_rights_global[gen_const.BQS_CASTLE_RIGHTS] = false;
                }
            }
        }

        priorNodes = nodes;
        nodes += perftInline(depth - 1, ply + 1);

        board.is_white_global = !board.is_white_global;

        switch (tag) {
         0, 26 => { //check
            board.bitboard_array_global[piece] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[targetSquare];
         },
        
         1, 27 => { //check cap
            board.bitboard_array_global[piece] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[piece] &= ~move_constants.SQUARE_BBS[targetSquare];
            if (piece >= gen_const.WP and piece <= gen_const.WK) {
                board.bitboard_array_global[captureIndex] |= move_constants.SQUARE_BBS[targetSquare];
            } else { //is black
                board.bitboard_array_global[captureIndex] |= move_constants.SQUARE_BBS[targetSquare];
            }

         },
         2 => { //white ep
            board.bitboard_array_global[gen_const.WP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.WP] &= ~move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[gen_const.BP] |= move_constants.SQUARE_BBS[targetSquare + 8];
         },
         3 => { //black ep
            board.bitboard_array_global[gen_const.BP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.BP] &= ~move_constants.SQUARE_BBS[targetSquare];
            board.bitboard_array_global[gen_const.WP] |= move_constants.SQUARE_BBS[targetSquare - 8];
         },
         4 => { //WKS
            //white king
            board.bitboard_array_global[gen_const.WK] |= move_constants.SQUARE_BBS[gen_const.E1];
            board.bitboard_array_global[gen_const.WK] &= ~move_constants.SQUARE_BBS[gen_const.G1];
            //white rook
            board.bitboard_array_global[gen_const.WR] |= move_constants.SQUARE_BBS[gen_const.H1];
            board.bitboard_array_global[gen_const.WR] &= ~move_constants.SQUARE_BBS[gen_const.F1];
         },
         5 => { //WQS
            //white king
            board.bitboard_array_global[gen_const.WK] |= move_constants.SQUARE_BBS[gen_const.E1];
            board.bitboard_array_global[gen_const.WK] &= ~move_constants.SQUARE_BBS[gen_const.C1];
            //white rook
            board.bitboard_array_global[gen_const.WR] |= move_constants.SQUARE_BBS[gen_const.A1];
            board.bitboard_array_global[gen_const.WR] &= ~move_constants.SQUARE_BBS[gen_const.D1];
         },
         6 => { //BKS
            //white king
            board.bitboard_array_global[gen_const.BK] |= move_constants.SQUARE_BBS[gen_const.E8];
            board.bitboard_array_global[gen_const.BK] &= ~move_constants.SQUARE_BBS[gen_const.G8];
            //white rook
            board.bitboard_array_global[gen_const.BR] |= move_constants.SQUARE_BBS[gen_const.H8];
            board.bitboard_array_global[gen_const.BR] &= ~move_constants.SQUARE_BBS[gen_const.F8];
         },
         7 => { //BQS
            //white king
            board.bitboard_array_global[gen_const.BK] |= move_constants.SQUARE_BBS[gen_const.E8];
            board.bitboard_array_global[gen_const.BK] &= ~move_constants.SQUARE_BBS[gen_const.C8];
            //white rook
            board.bitboard_array_global[gen_const.BR] |= move_constants.SQUARE_BBS[gen_const.A8];
            board.bitboard_array_global[gen_const.BR] &= ~move_constants.SQUARE_BBS[gen_const.D8];

         },
         8 => { //BNPr
            board.bitboard_array_global[gen_const.BP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.BN] &= ~move_constants.SQUARE_BBS[targetSquare];
         },
         9 => { //BBPr
            board.bitboard_array_global[gen_const.BP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.BB] &= ~move_constants.SQUARE_BBS[targetSquare];
         },
         10 => { //BQPr
            board.bitboard_array_global[gen_const.BP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.BQ] &= ~move_constants.SQUARE_BBS[targetSquare];
         },
         11 => { //BRPr
            board.bitboard_array_global[gen_const.BP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.BR] &= ~move_constants.SQUARE_BBS[targetSquare];
         },
         12 => { //WNPr
            board.bitboard_array_global[gen_const.WP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.WN] &= ~move_constants.SQUARE_BBS[targetSquare];
         },
         13 => { //WBPr
            board.bitboard_array_global[gen_const.WP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.WB] &= ~move_constants.SQUARE_BBS[targetSquare];
         },
         14 => { //WQPr
            board.bitboard_array_global[gen_const.WP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.WQ] &= ~move_constants.SQUARE_BBS[targetSquare];
         },
         15 => { //WRPr
            board.bitboard_array_global[gen_const.WP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.WR] &= ~move_constants.SQUARE_BBS[targetSquare];
         },
         16 => { //BNPrCAP
            board.bitboard_array_global[gen_const.BP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.BN] &= ~move_constants.SQUARE_BBS[targetSquare];

            board.bitboard_array_global[captureIndex] |= move_constants.SQUARE_BBS[targetSquare];
         },
         17 => { //BBPrCAP
            board.bitboard_array_global[gen_const.BP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.BB] &= ~move_constants.SQUARE_BBS[targetSquare];

            board.bitboard_array_global[captureIndex] |= move_constants.SQUARE_BBS[targetSquare];
         },
         18 => { //BQPrCAP
            board.bitboard_array_global[gen_const.BP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.BQ] &= ~move_constants.SQUARE_BBS[targetSquare];

            board.bitboard_array_global[captureIndex] |= move_constants.SQUARE_BBS[targetSquare];
         },
         19 => { //BRPrCAP
            board.bitboard_array_global[gen_const.BP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.BR] &= ~move_constants.SQUARE_BBS[targetSquare];

            board.bitboard_array_global[captureIndex] |= move_constants.SQUARE_BBS[targetSquare];
         },
         20 => { //WNPrCAP
            board.bitboard_array_global[gen_const.WP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.WN] &= ~move_constants.SQUARE_BBS[targetSquare];

            board.bitboard_array_global[captureIndex] |= move_constants.SQUARE_BBS[targetSquare];
         },
         21 => { //WBPrCAP
            board.bitboard_array_global[gen_const.WP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.WB] &= ~move_constants.SQUARE_BBS[targetSquare];

            board.bitboard_array_global[captureIndex] |= move_constants.SQUARE_BBS[targetSquare];
         },
         22 => { //WQPrCAP
            board.bitboard_array_global[gen_const.WP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.WQ] &= ~move_constants.SQUARE_BBS[targetSquare];

            board.bitboard_array_global[captureIndex] |= move_constants.SQUARE_BBS[targetSquare];
         },
         23 => { //WRPrCAP
            board.bitboard_array_global[gen_const.WP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.WR] &= ~move_constants.SQUARE_BBS[targetSquare];

            board.bitboard_array_global[captureIndex] |= move_constants.SQUARE_BBS[targetSquare];
         },
         24 => { //WDouble
            board.bitboard_array_global[gen_const.WP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.WP] &= ~move_constants.SQUARE_BBS[targetSquare];
         },
         25 => { //BDouble
            board.bitboard_array_global[gen_const.BP] |= move_constants.SQUARE_BBS[startingSquare];
            board.bitboard_array_global[gen_const.BP] &= ~move_constants.SQUARE_BBS[targetSquare];
         },
         else => {
            unreachable;
         }
        }

        board.castle_rights_global[0] = copy_castle[0];
        board.castle_rights_global[1] = copy_castle[1];
        board.castle_rights_global[2] = copy_castle[2];
        board.castle_rights_global[3] = copy_castle[3];
        board.ep_global = copyEp;

        //if (epGlobal != general_constants.NO_SQUARE)
        //{
        //    std::cout << "   ep: " << SQ_CHAR_X[epGlobal] << SQ_CHAR_Y[epGlobal] << '\n';
        //}

        //if (ply == 0)
        //{
           //PrMoveNoNL(startingSquare, targetSquare);
           //std.debug.print(": {}\n", .{nodes - priorNodes});
        //}
    }

    return nodes;
}

pub fn runPerftInline(depth:i8) void {

    const start_time: i64 = std.time.milliTimestamp();

    const nodes = perftInline(depth, 0);

    const stop_time: i64 = std.time.milliTimestamp();

    const elapsed_milliseconds = stop_time - start_time;
    std.debug.print("Nodes: {}\n", .{nodes});
    std.debug.print("Elapsed time: {}\n", .{elapsed_milliseconds});
}

pub fn main() void {

    ParseFenGlobal(FEN_STARTING_POSITION, 0);
    RunPerftInline(6);
}
