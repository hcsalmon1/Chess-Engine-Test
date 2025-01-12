package main

IsOccupied :: proc(bitboard: u64, square: int) -> bool {
	return (bitboard & SQUARE_BBS[square]) != 0
}

GetOccupiedIndex :: proc(square: int) -> int {
	for i := 0; i < 12; i+=1 {
		if IsOccupied(PieceArray[i], square) {
			return i
		}
	}
	return EMPTY
}

OutOfBounds :: proc(move: int) -> bool {

	if move < 0 {
		return true
	}
	if move > 63 {
		return true
	}
	return false
}
