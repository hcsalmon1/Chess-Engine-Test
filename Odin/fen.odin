package main

FEN_STARTING_POSITION :: "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
FEN_STARTING_POSITION_BLACK :: "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1";
FEN_STARTING_POSITION_ONLY_PAWNS :: "4k3/pppppppp/8/8/8/8/PPPPPPPP/4K3 w KQkq - 0 1";
FEN_STARTING_POSITION_EP_E4 :: "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq e4 0 1";
FEN_STARTING_POSITION_ONLY_KNIGHTS :: "1n2k1n1/8/8/8/8/8/8/1N2K1N1 w - - 0 1";
FEN_STARTING_POSITION_ONLY_KNIGHTS_BLACK :: "1n2k1n1/8/8/8/8/8/8/1N2K1N1 b - - 0 1";
FEN_TEST_KNIGHT_CAPTURES :: "4k1n1/8/8/8/8/2n5/8/1N2K1N1 b - - 0 1";
FEN_TEST_ONLY_KINGS :: "8/8/3k4/8/3K4/8/8/8 w - - 0 1";
FEN_TEST_ONLY_KNIGHTS :: "8/8/3k4/8/3K4/8/8/8 w - - 0 1";
FEN_TRICKY_WHITE :: "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - ";
FEN_TRICKY_BLACK :: "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R b KQkq - ";
FEN_TEST_EP :: "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - ";
FEN_SCHOLARS_MATE :: "r1bqkb1r/pppp1ppp/2n2n2/4p2Q/2B1P3/8/PPPP1PPP/RNB1K1NR w KQkq - 4 4";
FEN_FRIED_LIVER_BLACK :: "r1bqkb1r/pppp1ppp/2n2n2/4p1N1/2B1P3/8/PPPP1PPP/RNBQK2R b KQkq - 5 4";
FEN_FRIED_LIVER_WHITE :: "r1bqkb1r/ppp2ppp/2np1n2/4p1N1/2B1P3/8/PPPP1PPP/RNBQK2R w KQkq - 0 5";
FEN_KNIGHT_FORK :: "rn1qk2r/pp3ppp/2p1pn2/6N1/8/1PP3P1/P4PBP/R2Q1RK1 w - - 0 1";
FEN_MATE_0_TEST :: "r1bqkb1r/pppp1Qpp/2n2n2/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1";
FEN_MATE_4_TEST :: "r7/q6p/2k1p3/2N1Q3/1ppP4/P2b4/1P4PP/K2R4 b - - 0 1";
FEN_BONGCLOUD :: "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPPKPPP/RNBQ1BNR b kq - 1 2";
FEN_PERPETUAL :: "6k1/5pp1/6p1/8/8/3Q3P/5PP1/6K1 w - - 0 1";
FEN_MATE_3 :: "r4rk1/ppp1q1p1/2n2pP1/8/8/2N5/PPP1BQ2/2KR3R w - - 0 1";
FEN_BLACK_DOUBLE_CHECK :: "4k3/8/8/8/4N3/8/8/4R1K1 w - - 0 1";

FEN_PIECES :: 0;
FEN_SIDE :: 1;
FEN_CASTLING :: 2;
FEN_EP :: 3;
FEN_FIFTY :: 4;
FEN_PLY :: 5;

LoadFen :: proc(fen: string) {
	ResetBoard();

	bracketCount: int = 0;
	squareCount: int = 0;

	setting: int = FEN_PIECES;

	ep_file_index:int = -1;
	ep_rank_index:int = -1;

	for character_index := 0; character_index < len(fen); character_index+=1 {
		switch setting {
		case FEN_PIECES:
			Load_Fen_Sort_Pieces(&bracketCount, fen[character_index], &setting, &squareCount);
		case FEN_SIDE:
			Load_Fen_Sort_Side(&setting, fen[character_index]);
		case FEN_CASTLING:
			Load_Fen_Sort_Castling(&setting, fen[character_index]);
		case FEN_EP:
			Load_Fen_Sort_EP(&setting, fen[character_index], &ep_file_index, &ep_rank_index);
		case FEN_FIFTY:
			Load_Fen_Sort_Fifty_Move(&setting, fen[character_index]);
		case FEN_PLY:
			return;
		}
	}
}
Load_Fen_Sort_Pieces :: proc(bracketCount: ^int, character_in_fen: byte, setting: ^int, squareCount: ^int) {
	if bracketCount^ == 7 && character_in_fen == ' ' {
		setting^+=1
		return
	}
	if bracketCount^ > 7 {
		bracketCount^ = 0
	}
	if squareCount^ > 7 {
		squareCount^ = 0
	}
	square:int = (bracketCount^ * 8) + squareCount^;

	switch character_in_fen {

		case 'B':
			PieceArray[WB] = SetBit(PieceArray[WB], square)
			squareCount^+=1;
		case 'R':
			PieceArray[WR] = SetBit(PieceArray[WR], square)
			squareCount^+=1;
		case 'P':
			PieceArray[WP] = SetBit(PieceArray[WP], square)
			squareCount^+=1;
		case 'Q':
			PieceArray[WQ] = SetBit(PieceArray[WQ], square)
			squareCount^+=1;
		case 'K':
			PieceArray[WK] = SetBit(PieceArray[WK], square)
			squareCount^+=1;
		case 'N':
			PieceArray[WN] = SetBit(PieceArray[WN], square)
			squareCount^+=1;
		case 'b':
			PieceArray[BB] = SetBit(PieceArray[BB], square)
			squareCount^+=1;
		case 'p':
			PieceArray[BP] = SetBit(PieceArray[BP], square)
			squareCount^+=1;
		case 'q':
			PieceArray[BQ] = SetBit(PieceArray[BQ], square)
			squareCount^+=1;
		case 'r':
			PieceArray[BR] = SetBit(PieceArray[BR], square)
			squareCount^+=1;
		case 'n':
			PieceArray[BN] = SetBit(PieceArray[BN], square)
			squareCount^+=1;
		case 'k':
			PieceArray[BK] = SetBit(PieceArray[BK], square)
			squareCount^+=1;
		case '/':
			squareCount^ = 0
			bracketCount^+=1;
		case '1':
			squareCount^ += 1
		case '2':
			squareCount^ += 2
		case '3':
			squareCount^ += 3
		case '4':
			squareCount^ += 4
		case '5':
			squareCount^ += 5
		case '6':
			squareCount^ += 6
		case '7':
			squareCount^ += 7
		case '8':
			squareCount^ += 8
	}
}
Load_Fen_Sort_Side :: proc(setting: ^int, character_in_fen: byte) {
	switch character_in_fen {
	case 'w':
		whiteToPlay = true;
	case 'b':
		whiteToPlay = false;
	case ' ':
		setting^+=1;
	}
}
Load_Fen_Sort_Castling :: proc(setting: ^int, character_in_fen: byte) {
	switch character_in_fen {
		case 'K':
			CastleRights[0] = true
		case 'Q':
			CastleRights[1] = true
		case 'k':
			CastleRights[2] = true
		case 'q':
			CastleRights[3] = true
		case '-':
			CastleRights[0] = false
			CastleRights[1] = false
			CastleRights[2] = false
			CastleRights[3] = false
		case ' ':
			setting^+=1
	}
}
Load_Fen_Sort_EP :: proc(setting: ^int, character_in_fen: byte, file_index: ^int, rank_index: ^int) {

	if character_in_fen == '-' {
		ep = NO_SQUARE
	}
	if character_in_fen == ' ' {

		if file_index^ != -1 && rank_index^ != -1 {
			ep = Convert_to_64(file_index^, rank_index^)
		}
		setting^+=1
	}

	switch character_in_fen {

	case 'a':
		file_index^ = 0
		return
	case 'b':
		file_index^ = 1
		return
	case 'c':
		file_index^ = 2
		return
	case 'd':
		file_index^ = 3
		return
	case 'e':
		file_index^ = 4
		return
	case 'f':
		file_index^ = 5
		return
	case 'g':
		file_index^ = 6
		return
	case 'h':
		file_index^ = 7
		return
	}

	switch character_in_fen {

	case '1':
		rank_index^ = 7
		return
	case '2':
		rank_index^ = 6
		return
	case '3':
		rank_index^ = 5
		return
	case '4':
		rank_index^ = 4
		return
	case '5':
		rank_index^ = 3
		return
	case '6':
		rank_index^ = 2
		return
	case '7':
		rank_index^ = 1
		return
	case '8':
		rank_index^ = 0
		return
	}
}
Convert_to_64 :: proc(file: int, rank: int) -> int {
	return (rank * 8) + file;
}
Load_Fen_Sort_Fifty_Move :: proc(setting: ^int, character_in_fen: byte) {
	if character_in_fen == ' ' {
		setting^+=1;
		return;
	}
}
