package main

GetRookAttacksFast :: proc(startingSquare: int, occupancy: u64) -> u64 {

	mutable_occupancy := occupancy;
	mutable_occupancy &= ROOK_MASKS[startingSquare]
	mutable_occupancy *= ROOK_MAGIC_NUMBERS[startingSquare]
	mutable_occupancy >>= 64 - ROOK_REL_BITS[startingSquare]
	return ROOK_ATTACKS[startingSquare][mutable_occupancy]
}

GetBishopAttacksFast :: proc(startingSquare: int, occupancy: u64) -> u64 {

	mutable_occupancy := occupancy;
	mutable_occupancy &= BISHOP_MASKS[startingSquare]
	mutable_occupancy *= BISHOP_MAGIC_NUMBERS[startingSquare]
	mutable_occupancy >>= 64 - BISHOP_REL_BITS[startingSquare]
	return BISHOP_ATTACKS[startingSquare][mutable_occupancy]
}

Is_Square_Attacked_By_Black :: proc(square: int, occupancy: u64) -> bool {
	if (PieceArray[BP] & WHITE_PAWN_ATTACKS[square]) != 0 {
		return true
	}
	if (PieceArray[BN] & KNIGHT_ATTACKS[square]) != 0 {
		return true
	}
	if (PieceArray[BK] & KING_ATTACKS[square]) != 0 {
		return true
	}
	bishopAttacks := GetBishopAttacksFast(square, occupancy)
	if (PieceArray[BB] & bishopAttacks) != 0 {
		return true
	}
	if (PieceArray[BQ] & bishopAttacks) != 0 {
		return true
	}
	rookAttacks := GetRookAttacksFast(square, occupancy)
	if (PieceArray[BR] & rookAttacks) != 0 {
		return true
	}
	if (PieceArray[BQ] & rookAttacks) != 0 {
		return true
	}
	return false
}

Is_Square_Attacked_By_White :: proc(square: int, occupancy: u64) -> bool {

	if (PieceArray[WP] & BLACK_PAWN_ATTACKS[square]) != 0 {
		return true
	}
	if (PieceArray[WN] & KNIGHT_ATTACKS[square]) != 0 {
		return true
	}
	if (PieceArray[WK] & KING_ATTACKS[square]) != 0 {
		return true
	}
	bishopAttacks := GetBishopAttacksFast(square, occupancy)
	if (PieceArray[WB] & bishopAttacks) != 0 {
		return true
	}
	if (PieceArray[WQ] & bishopAttacks) != 0 {
		return true
	}
	rookAttacks := GetRookAttacksFast(square, occupancy)
	if (PieceArray[WR] & rookAttacks) != 0 {
		return true
	}
	if (PieceArray[WQ] & rookAttacks) != 0 {
		return true
	}
	return false
}
