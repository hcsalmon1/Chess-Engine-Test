package main

func IsOccupied(bitboard uint64, square int) bool {
	return (bitboard & SQUARE_BBS[square]) != 0
}

func GetOccupiedIndex(square int) int {
	for i := 0; i < 12; i++ {
		if IsOccupied(PieceArray[i], square) {
			return i
		}
	}
	return EMPTY
}

func OutOfBounds(move int) bool {

	if move < 0 {
		return true
	}
	if move > 63 {
		return true
	}
	return false
}
