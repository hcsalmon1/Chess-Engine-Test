package main

var SQUARE_BBS [64]uint64 = [64]uint64{
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
}

const ONE_U64 uint64 = 1

const BP_STARTING_POSITIONS uint64 = 65280
const WP_STARTING_POSITIONS uint64 = 71776119061217280
const BK_STARTING_POSITION uint64 = 16
const WK_STARTING_POSITION uint64 = 1152921504606846976
const BN_STARTING_POSITIONS uint64 = 66
const WN_STARTING_POSITIONS uint64 = 4755801206503243776
const WR_STARTING_POSITIONS uint64 = 9295429630892703744
const BR_STARTING_POSITIONS uint64 = 129
const BB_STARTING_POSITIONS uint64 = 36
const WB_STARTING_POSITIONS uint64 = 2594073385365405696
const WQ_STARTING_POSITION uint64 = 576460752303423488
const BQ_STARTING_POSITION uint64 = 8

const EMPTY_BITBOARD uint64 = 0

const MAX_ULONG uint64 = 18446744073709551615

const MAGIC uint64 = 0x03f79d71b4cb0a89

var DEBRUIJN64 [64]int = [64]int{

	0, 47, 1, 56, 48, 27, 2, 60,
	57, 49, 41, 37, 28, 16, 3, 61,
	54, 58, 35, 52, 50, 42, 21, 44,
	38, 32, 29, 23, 17, 11, 4, 62,
	46, 55, 26, 59, 40, 36, 15, 53,
	34, 51, 20, 43, 31, 22, 10, 45,
	25, 39, 14, 33, 19, 30, 9, 24,
	13, 18, 8, 12, 7, 6, 5, 63,
}

func BitscanForward(tempBitboard uint64) int {
	return (DEBRUIJN64[MAGIC*(tempBitboard^(tempBitboard-1))>>58])
}

const RANK_1_BITBOARD = 18374686479671623680
const RANK_2_BITBOARD = 71776119061217280
const RANK_3_BITBOARD = 280375465082880
const RANK_4_BITBOARD = 1095216660480
const RANK_5_BITBOARD = 4278190080
const RANK_6_BITBOARD = 16711680
const RANK_7_BITBOARD = 65280
const RANK_8_BITBOARD = 255

const FILE_A_BITBOARD = 72340172838076673
const FILE_B_BITBOARD = 144680345676153346
const FILE_C_BITBOARD = 289360691352306692
const FILE_D_BITBOARD = 578721382704613384
const FILE_E_BITBOARD = 1157442765409226768
const FILE_F_BITBOARD = 2314885530818453536
const FILE_G_BITBOARD = 4629771061636907072
const FILE_H_BITBOARD = 9259542123273814144

var SQ_CHAR_Y [65]byte = [65]byte{
	'8', '8', '8', '8', '8', '8', '8', '8',
	'7', '7', '7', '7', '7', '7', '7', '7',
	'6', '6', '6', '6', '6', '6', '6', '6',
	'5', '5', '5', '5', '5', '5', '5', '5',
	'4', '4', '4', '4', '4', '4', '4', '4',
	'3', '3', '3', '3', '3', '3', '3', '3',
	'2', '2', '2', '2', '2', '2', '2', '2',
	'1', '1', '1', '1', '1', '1', '1', '1', 'A',
}

var SQ_CHAR_X [65]byte = [65]byte{
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'N',
}