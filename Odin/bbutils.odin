package main

EmptyBit :: proc(bitboard: u64, square: int) -> u64 {
	shifted_bitboard := SQUARE_BBS[square]; 
	return bitboard & ~shifted_bitboard;
}

SetBit :: proc(bitboard: u64, square: int) -> u64 {
	return bitboard | SQUARE_BBS[square];
}

MoveBit :: proc(bitboard: u64, starting_square: int, target_square: int) -> u64 {
	copy_bitboard :u64= EmptyBit(bitboard, starting_square);
	return SetBit(copy_bitboard, target_square);
}
