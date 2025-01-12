package main

func GetRookAttacksFast(startingSquare int, occupancy uint64) uint64 {

	occupancy &= ROOK_MASKS[startingSquare]
	occupancy *= ROOK_MAGIC_NUMBERS[startingSquare]
	occupancy >>= 64 - ROOK_REL_BITS[startingSquare]
	return ROOK_ATTACKS[startingSquare][occupancy]
}

func GetBishopAttacksFast(startingSquare int, occupancy uint64) uint64 {

	occupancy &= BISHOP_MASKS[startingSquare]
	occupancy *= BISHOP_MAGIC_NUMBERS[startingSquare]
	occupancy >>= 64 - BISHOP_REL_BITS[startingSquare]
	return BISHOP_ATTACKS[startingSquare][occupancy]
}

func Is_Square_Attacked_By_Black(square int, occupancy uint64) bool {
	if (PieceArray[BP] & WHITE_PAWN_ATTACKS[square]) != 0 {
		return true
	}
	if (PieceArray[BN] & KNIGHT_ATTACKS[square]) != 0 {
		return true
	}
	if (PieceArray[BK] & KING_ATTACKS[square]) != 0 {
		return true
	}
	var bishopAttacks = GetBishopAttacksFast(square, occupancy)
	if (PieceArray[BB] & bishopAttacks) != 0 {
		return true
	}
	if (PieceArray[BQ] & bishopAttacks) != 0 {
		return true
	}
	var rookAttacks = GetRookAttacksFast(square, occupancy)
	if (PieceArray[BR] & rookAttacks) != 0 {
		return true
	}
	if (PieceArray[BQ] & rookAttacks) != 0 {
		return true
	}
	return false
}

func Is_Square_Attacked_By_White(square int, occupancy uint64) bool {

	if (PieceArray[WP] & BLACK_PAWN_ATTACKS[square]) != 0 {
		return true
	}
	if (PieceArray[WN] & KNIGHT_ATTACKS[square]) != 0 {
		return true
	}
	if (PieceArray[WK] & KING_ATTACKS[square]) != 0 {
		return true
	}
	var bishopAttacks = GetBishopAttacksFast(square, occupancy)
	if (PieceArray[WB] & bishopAttacks) != 0 {
		return true
	}
	if (PieceArray[WQ] & bishopAttacks) != 0 {
		return true
	}
	var rookAttacks = GetRookAttacksFast(square, occupancy)
	if (PieceArray[WR] & rookAttacks) != 0 {
		return true
	}
	if (PieceArray[WQ] & rookAttacks) != 0 {
		return true
	}
	return false
}
