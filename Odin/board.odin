package main

import "core:fmt"

PieceArray: [12]u64 = [12]u64{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
whiteToPlay: bool = true;
CastleRights: [4]bool = [4]bool{true, true, true, true};
ep:int = NO_SQUARE;
BoardPly: u8 = 0;

SetStartingPosition :: proc() {

	ep = NO_SQUARE;
	whiteToPlay = true
	CastleRights[0] = true
	CastleRights[1] = true
	CastleRights[2] = true
	CastleRights[3] = true

	PieceArray[WP] = WP_STARTING_POSITIONS
	PieceArray[WN] = WN_STARTING_POSITIONS
	PieceArray[WB] = WB_STARTING_POSITIONS
	PieceArray[WR] = WR_STARTING_POSITIONS
	PieceArray[WQ] = WQ_STARTING_POSITION
	PieceArray[WK] = WK_STARTING_POSITION
	PieceArray[BP] = BP_STARTING_POSITIONS
	PieceArray[BN] = BN_STARTING_POSITIONS
	PieceArray[BB] = BB_STARTING_POSITIONS
	PieceArray[BR] = BR_STARTING_POSITIONS
	PieceArray[BQ] = BQ_STARTING_POSITION
	PieceArray[BK] = BK_STARTING_POSITION
}

PrintBoard :: proc() {
	fmt.println("Board:")
	boardArray: [64]int = FillBoardArray();

	for rank := 0; rank < 8; rank+=1 {

		fmt.print("   ")

		for file := 0; file < 8; file+=1 {

			square: int = (rank * 8) + file
			fmt.printf("%c%c ", PieceColours[boardArray[square]], PieceNames[boardArray[square]])
		}

		fmt.println()
	}
	fmt.println()

	fmt.printf("White to play: %t\n", whiteToPlay)

	fmt.printf("Castle: %t %t %t %t\n", CastleRights[0], CastleRights[1], CastleRights[2], CastleRights[3])
	fmt.printf("ep: %d\n", ep)
	fmt.printf("ply: %d\n", BoardPly)
	fmt.println()
	fmt.println()
}

PrintAllBitboards :: proc() {
	for i := 0; i < 12; i+=1 {
		fmt.println(PieceArray[i])
	}

}

printBoardBasic :: proc() {
	fmt.println("PieceArray:")
	for i := 0; i < 12; i+=1 {
		fmt.println(PieceArray[i])
	}
	fmt.printf("white to play: %t\n", whiteToPlay);
	fmt.printf("Castle: %t %t %t %t\n", CastleRights[0], CastleRights[1], CastleRights[2], CastleRights[3]);
	fmt.printf("ep: %d\n", ep);
	fmt.printf("ply %d\n", BoardPly);
}

FillBoardArray :: proc() -> [64]int {
	boardArray: [64]int;
	for i := 0; i < 64; i+=1 {
		boardArray[i] = GetOccupiedIndex(i)
	}
	return boardArray
}

IsBoardArraySame :: proc(copy: [12]u64) -> bool {
	for i := 0; i < 12; i+=1 {
		if PieceArray[i] != copy[i] {
			fmt.println("ERROR piece not same: %d", i);
			return false;
		}
	}
	return true
}

PrintBitboard :: proc(bitboard: u64) {
	for rank := 0; rank < 8; rank+=1 {
		for file := 0; file < 8; file+=1 {
			square :int= (rank * 8) + file;
			if (bitboard & (SQUARE_BBS[square])) != 0 {
				fmt.print("X ");
				continue;
			}
			fmt.print("_ ");
		}
		fmt.println();
	}
	fmt.println(bitboard);
}

ResetBoard :: proc() {
	for i := 0; i < 12; i+=1 {
		PieceArray[i] = EMPTY_BITBOARD;
	}
	whiteToPlay = true;
	for i := 0; i < 4; i+=1 {
		CastleRights[i] = true;
	}
	ep = NO_SQUARE;
	BoardPly = 0;
}
