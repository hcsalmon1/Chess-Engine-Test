package main

func EmptyBit(bitboard uint64, square int) uint64 {
	var shifted_bitboard = ONE_U64 << uint64(square)
	return bitboard & ^shifted_bitboard
}

func SetBit(bitboard uint64, square int) uint64 {
	return bitboard | (ONE_U64 << uint64(square))
}

func MoveBit(bitboard uint64, starting_square int, target_square int) uint64 {
	bitboard = EmptyBit(bitboard, starting_square)
	return SetBit(bitboard, target_square)
}
