package main

import "core:fmt"
import "core:time"

PerftInline :: proc(depth: int, ply: int) -> u64 {
	#no_bounds_check{
    //if (depth == 0)
    //{
    //    return 1;
    //}

	//if depth == 0 {
	//	return 1
	//}

	moveList: [50][4]int;
	moveCount: int = 0;

	WHITE_OCCUPANCIES: u64 = PieceArray[0] |
		PieceArray[1] |
		PieceArray[2] |
		PieceArray[3] |
		PieceArray[4] |
		PieceArray[5];

	BLACK_OCCUPANCIES: u64 = PieceArray[6] |
		PieceArray[7] |
		PieceArray[8] |
		PieceArray[9] |
		PieceArray[10] |
		PieceArray[11];

	COMBINED_OCCUPANCIES: u64 = WHITE_OCCUPANCIES | BLACK_OCCUPANCIES;
	EMPTY_OCCUPANCIES: u64 = ~COMBINED_OCCUPANCIES;
	tempBitboard: u64;
	checkBitboard: u64 = EMPTY_BITBOARD;
	tempPinBitboard: u64;
	tempAttack: u64;
	tempEmpty: u64;
	tempCaptures: u64;
	startingSquare:int = NO_SQUARE;
	targetSquare:int = NO_SQUARE;

	pinArray := [8][2]int{
		{-1, -1},
		{-1, -1},
		{-1, -1},
		{-1, -1},
		{-1, -1},
		{-1, -1},
		{-1, -1},
		{-1, -1},
	}
	pinNumber := 0;

	//Generate Moves
	if whiteToPlay {

		whiteKingCheckCount: int = 0
		whiteKingPosition: int = BitscanForward(PieceArray[WK])

		//pawns
		tempBitboard = PieceArray[BP] & WHITE_PAWN_ATTACKS[whiteKingPosition]
		if tempBitboard != 0 {

			pawn_square: int = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58]);
			checkBitboard = SQUARE_BBS[pawn_square];
			
			whiteKingCheckCount+=1;
		}

		//knights
		tempBitboard = PieceArray[BN] & KNIGHT_ATTACKS[whiteKingPosition]
		if tempBitboard != 0 {

			knight_square: int = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58]);
			checkBitboard = SQUARE_BBS[knight_square];
			whiteKingCheckCount+=1;
		}

		//bishops
		bishopAttacksChecks: u64 = GetBishopAttacksFast(whiteKingPosition, BLACK_OCCUPANCIES);
		tempBitboard = PieceArray[BB] & bishopAttacksChecks;
		for tempBitboard != 0 {

			piece_square: int = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58]);
			tempPinBitboard = INBETWEEN_BITBOARDS[whiteKingPosition][piece_square] & WHITE_OCCUPANCIES;

			if tempPinBitboard == 0 {

				checkBitboard = INBETWEEN_BITBOARDS[whiteKingPosition][piece_square];
				whiteKingCheckCount+=1;
				
			} else {

				pinned_square: int = (DEBRUIJN64[MAGIC*(tempPinBitboard~(tempPinBitboard-1))>>58]);
				tempPinBitboard &= tempPinBitboard - 1;

				if tempPinBitboard == 0 {

					pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
					pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
					pinNumber+=1;
				}
			}
			tempBitboard &= tempBitboard - 1;
		}

		//queen
		tempBitboard = PieceArray[BQ] & bishopAttacksChecks;
		for tempBitboard != 0 {

			piece_square: int = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58]);
			tempPinBitboard = INBETWEEN_BITBOARDS[whiteKingPosition][piece_square] & WHITE_OCCUPANCIES;

			if tempPinBitboard == 0 {
				
				checkBitboard = INBETWEEN_BITBOARDS[whiteKingPosition][piece_square];
				whiteKingCheckCount+=1;
				
			} else {
				pinned_square: int = (DEBRUIJN64[MAGIC*(tempPinBitboard~(tempPinBitboard-1))>>58]);
				tempPinBitboard &= tempPinBitboard - 1;

				if tempPinBitboard == 0 {

					pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
					pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
					pinNumber+=1;
				}
			}
			tempBitboard &= tempBitboard - 1;
		}

		//rook
		rook_attacks: u64 = GetRookAttacksFast(whiteKingPosition, BLACK_OCCUPANCIES);
		tempBitboard = PieceArray[BR] & rook_attacks;
		for tempBitboard != 0 {

			piece_square: int = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58])
			tempPinBitboard = INBETWEEN_BITBOARDS[whiteKingPosition][piece_square] & WHITE_OCCUPANCIES

			if tempPinBitboard == 0 {

				checkBitboard = INBETWEEN_BITBOARDS[whiteKingPosition][piece_square];
				whiteKingCheckCount+=1;
				
			} else {
				pinned_square: int = (DEBRUIJN64[MAGIC*(tempPinBitboard~(tempPinBitboard-1))>>58]);
				tempPinBitboard &= tempPinBitboard - 1;

				if tempPinBitboard == 0 {
					pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
					pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
					pinNumber+=1;
				}
			}
			tempBitboard &= tempBitboard - 1;
		}

		//queen
		tempBitboard = PieceArray[BQ] & rook_attacks;
		for tempBitboard != 0 {

			piece_square: int = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58]);
			tempPinBitboard = INBETWEEN_BITBOARDS[whiteKingPosition][piece_square] & WHITE_OCCUPANCIES;

			if tempPinBitboard == 0 {
				
				checkBitboard = INBETWEEN_BITBOARDS[whiteKingPosition][piece_square];
				whiteKingCheckCount+=1;
			} else {
				pinned_square: int = (DEBRUIJN64[MAGIC*(tempPinBitboard~(tempPinBitboard-1))>>58]);
				tempPinBitboard &= tempPinBitboard - 1;

				if tempPinBitboard == 0 {
					pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square;
					pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square;
					pinNumber+=1;
				}
			}
			tempBitboard &= tempBitboard - 1;
		}

        occupanciesWithoutWhiteKing: u64 = COMBINED_OCCUPANCIES & (~PieceArray[WK]);
        tempAttack = KING_ATTACKS[whiteKingPosition];
        tempEmpty = tempAttack & EMPTY_OCCUPANCIES;
        for tempEmpty != 0 {
            targetSquare = BitscanForward(tempEmpty);
            tempEmpty &= tempEmpty - 1;

            if (PieceArray[BP] & WHITE_PAWN_ATTACKS[targetSquare]) != 0 {
                continue
            }
            if (PieceArray[BN] & KNIGHT_ATTACKS[targetSquare]) != 0 {
                continue
            }
            if (PieceArray[BK] & KING_ATTACKS[targetSquare]) != 0 {
                continue
            }
            bishopAttacks: u64 = GetBishopAttacksFast(targetSquare, occupanciesWithoutWhiteKing);
            if (PieceArray[BB] & bishopAttacks) != 0 {
                continue
            }
            if (PieceArray[BQ] & bishopAttacks) != 0 {
                continue
            }
            rookAttacks: u64 = GetRookAttacksFast(targetSquare, occupanciesWithoutWhiteKing)
            if (PieceArray[BR] & rookAttacks) != 0 {
                continue
            }
            if (PieceArray[BQ] & rookAttacks) != 0 {
                continue
            }

            moveList[moveCount][MOVE_STARTING] = whiteKingPosition;
            moveList[moveCount][MOVE_TARGET] = targetSquare;
            moveList[moveCount][MOVE_TAG] = TAG_NONE;
            moveList[moveCount][MOVE_PIECE] = WK;
            moveCount+=1;
        }

        //captures
        tempCaptures = tempAttack & BLACK_OCCUPANCIES
        for tempCaptures != 0 {
            targetSquare = BitscanForward(tempCaptures)
            tempCaptures &= tempCaptures - 1

            if (PieceArray[BP] & WHITE_PAWN_ATTACKS[targetSquare]) != 0 {
                continue
            }
            if (PieceArray[BN] & KNIGHT_ATTACKS[targetSquare]) != 0 {
                continue
            }
            if (PieceArray[BK] & KING_ATTACKS[targetSquare]) != 0 {
                continue
            }
            bishopAttacks: u64 = GetBishopAttacksFast(targetSquare, occupanciesWithoutWhiteKing);
            if (PieceArray[BB] & bishopAttacks) != 0 {
                continue
            }
            if (PieceArray[BQ] & bishopAttacks) != 0 {
                continue
            }
            rookAttacks: u64 = GetRookAttacksFast(targetSquare, occupanciesWithoutWhiteKing);
            if (PieceArray[BR] & rookAttacks) != 0 {
                continue
            }
            if (PieceArray[BQ] & rookAttacks) != 0 {
                continue
            }

            moveList[moveCount][MOVE_STARTING] = whiteKingPosition;
            moveList[moveCount][MOVE_TARGET] = targetSquare;
            moveList[moveCount][MOVE_TAG] = TAG_CAPTURE;
            moveList[moveCount][MOVE_PIECE] = WK;
            moveCount+=1;
        }

		//If not check
		if whiteKingCheckCount < 2 {

			if whiteKingCheckCount == 0 {
				checkBitboard = MAX_ULONG;
			}

			if whiteKingCheckCount == 0 {

				if CastleRights[WKS_CASTLE_RIGHTS] == true {

					if whiteKingPosition == E1 { //king on e1

						if (WKS_EMPTY_BITBOARD & COMBINED_OCCUPANCIES) == 0 { //f1 and g1 empty

							if (PieceArray[WR] & SQUARE_BBS[H1]) != 0 { //rook on h1

								if Is_Square_Attacked_By_Black(F1, COMBINED_OCCUPANCIES) == false {

									if Is_Square_Attacked_By_Black(G1, COMBINED_OCCUPANCIES) == false {

										moveList[moveCount][MOVE_STARTING] = E1;
										moveList[moveCount][MOVE_TARGET] = G1;
										moveList[moveCount][MOVE_TAG] = TAG_WCASTLEKS;
										moveList[moveCount][MOVE_PIECE] = WK;
										moveCount+=1;
									}
								}
							}
						}
					}
				}
				if CastleRights[WQS_CASTLE_RIGHTS] == true {

					if whiteKingPosition == E1 { //king on e1

						if (WQS_EMPTY_BITBOARD & COMBINED_OCCUPANCIES) == 0 { //f1 and g1 empty

							if (PieceArray[WR] & SQUARE_BBS[A1]) != 0 { //rook on h1

								if Is_Square_Attacked_By_Black(C1, COMBINED_OCCUPANCIES) == false {

									if Is_Square_Attacked_By_Black(D1, COMBINED_OCCUPANCIES) == false {

										moveList[moveCount][MOVE_STARTING] = E1;
										moveList[moveCount][MOVE_TARGET] = C1;
										moveList[moveCount][MOVE_TAG] = TAG_WCASTLEQS;
										moveList[moveCount][MOVE_PIECE] = WK;
										moveCount+=1;
									}
								}
							}
						}
					}
				}
			}

			tempBitboard = PieceArray[WN]

			for tempBitboard != 0 {

				startingSquare = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58])
				tempBitboard &= tempBitboard - 1 //removes the knight from that square to not infinitely loop

				tempPinBitboard = MAX_ULONG
				if pinNumber != 0 {

					for i := 0; i < pinNumber; i+=1 {

						if pinArray[i][PINNED_SQUARE_INDEX] == startingSquare {

							tempPinBitboard = INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][PINNING_PIECE_INDEX]];
						}
					}
				}

				tempAttack = ((KNIGHT_ATTACKS[startingSquare] & BLACK_OCCUPANCIES) & checkBitboard) & tempPinBitboard //gets knight captures
				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58]);
					tempAttack &= tempAttack - 1;

					moveList[moveCount][MOVE_STARTING] = startingSquare;
					moveList[moveCount][MOVE_TARGET] = targetSquare;
					moveList[moveCount][MOVE_TAG] = TAG_CAPTURE;
					moveList[moveCount][MOVE_PIECE] = WN;
					moveCount+=1;
				}

				tempAttack = ((KNIGHT_ATTACKS[startingSquare] & EMPTY_OCCUPANCIES) & checkBitboard) & tempPinBitboard;

				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58]);
					tempAttack &= tempAttack - 1;

					moveList[moveCount][MOVE_STARTING] = startingSquare;
					moveList[moveCount][MOVE_TARGET] = targetSquare;
					moveList[moveCount][MOVE_TAG] = TAG_NONE;
					moveList[moveCount][MOVE_PIECE] = WN;
					moveCount+=1;
				}
			}

			tempBitboard = PieceArray[WP]

			for tempBitboard != 0 {

				startingSquare = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58])
				tempBitboard &= tempBitboard - 1

				tempPinBitboard = MAX_ULONG
				if pinNumber != 0 {
					for i := 0; i < pinNumber; i+=1 {
						if pinArray[i][PINNED_SQUARE_INDEX] == startingSquare {

							tempPinBitboard = INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][PINNING_PIECE_INDEX]]
						}
					}
				}

				if (SQUARE_BBS[startingSquare-8] & COMBINED_OCCUPANCIES) == 0 { //if up one square is empty

					if ((SQUARE_BBS[startingSquare-8] & checkBitboard) & tempPinBitboard) != 0 {

						if (SQUARE_BBS[startingSquare] & RANK_7_BITBOARD) != 0 { //if promotion

							moveList[moveCount][MOVE_STARTING] = startingSquare
							moveList[moveCount][MOVE_TARGET] = startingSquare - 8
							moveList[moveCount][MOVE_TAG] = TAG_WQueenPromotion
							moveList[moveCount][MOVE_PIECE] = WP
							moveCount+=1

							moveList[moveCount][MOVE_STARTING] = startingSquare
							moveList[moveCount][MOVE_TARGET] = startingSquare - 8
							moveList[moveCount][MOVE_TAG] = TAG_WRookPromotion
							moveList[moveCount][MOVE_PIECE] = WP
							moveCount+=1

							moveList[moveCount][MOVE_STARTING] = startingSquare
							moveList[moveCount][MOVE_TARGET] = startingSquare - 8
							moveList[moveCount][MOVE_TAG] = TAG_WBishopPromotion
							moveList[moveCount][MOVE_PIECE] = WP
							moveCount+=1

							moveList[moveCount][MOVE_STARTING] = startingSquare
							moveList[moveCount][MOVE_TARGET] = startingSquare - 8
							moveList[moveCount][MOVE_TAG] = TAG_WKnightPromotion
							moveList[moveCount][MOVE_PIECE] = WP
							moveCount+=1

						} else {

							moveList[moveCount][MOVE_STARTING] = startingSquare
							moveList[moveCount][MOVE_TARGET] = startingSquare - 8
							moveList[moveCount][MOVE_TAG] = TAG_NONE
							moveList[moveCount][MOVE_PIECE] = WP
							moveCount+=1
						}
					}

					if (SQUARE_BBS[startingSquare] & RANK_2_BITBOARD) != 0 { //if on rank 2

						if ((SQUARE_BBS[startingSquare-16] & checkBitboard) & tempPinBitboard) != 0 { //if not pinned or

							if ((SQUARE_BBS[startingSquare-16]) & COMBINED_OCCUPANCIES) == 0 { //if up two squares and one square are empty

								moveList[moveCount][MOVE_STARTING] = startingSquare
								moveList[moveCount][MOVE_TARGET] = startingSquare - 16
								moveList[moveCount][MOVE_TAG] = TAG_DoublePawnWhite
								moveList[moveCount][MOVE_PIECE] = WP
								moveCount+=1
							}
						}
					}
				}

				tempAttack = ((WHITE_PAWN_ATTACKS[startingSquare] & BLACK_OCCUPANCIES) & checkBitboard) & tempPinBitboard //if black piece diagonal to pawn

				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58])
					tempAttack &= tempAttack - 1

					if (SQUARE_BBS[startingSquare] & RANK_7_BITBOARD) != 0 { //if promotion

						moveList[moveCount][MOVE_STARTING] = startingSquare
						moveList[moveCount][MOVE_TARGET] = targetSquare
						moveList[moveCount][MOVE_TAG] = TAG_WCaptureQueenPromotion
						moveList[moveCount][MOVE_PIECE] = WP
						moveCount+=1

						moveList[moveCount][MOVE_STARTING] = startingSquare
						moveList[moveCount][MOVE_TARGET] = targetSquare
						moveList[moveCount][MOVE_TAG] = TAG_WCaptureRookPromotion
						moveList[moveCount][MOVE_PIECE] = WP
						moveCount+=1

						moveList[moveCount][MOVE_STARTING] = startingSquare
						moveList[moveCount][MOVE_TARGET] = targetSquare
						moveList[moveCount][MOVE_TAG] = TAG_WCaptureBishopPromotion
						moveList[moveCount][MOVE_PIECE] = WP
						moveCount+=1

						moveList[moveCount][MOVE_STARTING] = startingSquare
						moveList[moveCount][MOVE_TARGET] = targetSquare
						moveList[moveCount][MOVE_TAG] = TAG_WCaptureKnightPromotion
						moveList[moveCount][MOVE_PIECE] = WP
						moveCount+=1
					} else {

						moveList[moveCount][MOVE_STARTING] = startingSquare
						moveList[moveCount][MOVE_TARGET] = targetSquare
						moveList[moveCount][MOVE_TAG] = TAG_CAPTURE
						moveList[moveCount][MOVE_PIECE] = WP
						moveCount+=1
					}
				}

				if (SQUARE_BBS[startingSquare] & RANK_5_BITBOARD) != 0 { //check rank for ep

					if ep != NO_SQUARE {

						if (((WHITE_PAWN_ATTACKS[startingSquare] & SQUARE_BBS[ep]) & checkBitboard) & tempPinBitboard) != 0 {

							if (PieceArray[WK] & RANK_5_BITBOARD) == 0 { //if no king on rank 5

								moveList[moveCount][MOVE_STARTING] = startingSquare
								moveList[moveCount][MOVE_TARGET] = int(ep)
								moveList[moveCount][MOVE_TAG] = TAG_WHITEEP
								moveList[moveCount][MOVE_PIECE] = WP
								moveCount+=1
							} else if (PieceArray[BR]&RANK_5_BITBOARD) == 0 && (PieceArray[BQ]&RANK_5_BITBOARD) == 0 { // if no b rook or queen on rank 5

								moveList[moveCount][MOVE_STARTING] = startingSquare
								moveList[moveCount][MOVE_TARGET] = int(ep)
								moveList[moveCount][MOVE_TAG] = TAG_WHITEEP
								moveList[moveCount][MOVE_PIECE] = WP
								moveCount+=1
							} else { //wk and br or bq on rank 5

								occupancyWithoutEPPawns: u64 = COMBINED_OCCUPANCIES & ~SQUARE_BBS[startingSquare]
								occupancyWithoutEPPawns &= ~SQUARE_BBS[ep+8]

								rookAttacksFromKing: u64 = GetRookAttacksFast(whiteKingPosition, occupancyWithoutEPPawns)

								if (rookAttacksFromKing & PieceArray[BR]) == 0 {

									if (rookAttacksFromKing & PieceArray[BQ]) == 0 {

										moveList[moveCount][MOVE_STARTING] = startingSquare
										moveList[moveCount][MOVE_TARGET] = int(ep)
										moveList[moveCount][MOVE_TAG] = TAG_WHITEEP
										moveList[moveCount][MOVE_PIECE] = WP
										moveCount+=1
									}
								}
							}
						}
					}
				}
			}

			tempBitboard = PieceArray[WR]
			for tempBitboard != 0 {

				startingSquare = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58])
				tempBitboard &= tempBitboard - 1

				tempPinBitboard = MAX_ULONG
				if pinNumber != 0 {

					for i := 0; i < pinNumber; i+=1 {

						if pinArray[i][PINNED_SQUARE_INDEX] == startingSquare {

							tempPinBitboard = INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][PINNING_PIECE_INDEX]]
						}
					}
				}

				rookAttacks :u64= GetRookAttacksFast(startingSquare, COMBINED_OCCUPANCIES)

				tempAttack = ((rookAttacks & BLACK_OCCUPANCIES) & checkBitboard) & tempPinBitboard
				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58])
					tempAttack &= tempAttack - 1

					moveList[moveCount][MOVE_STARTING] = startingSquare
					moveList[moveCount][MOVE_TARGET] = targetSquare
					moveList[moveCount][MOVE_TAG] = TAG_CAPTURE
					moveList[moveCount][MOVE_PIECE] = WR
					moveCount+=1
				}

				tempAttack = ((rookAttacks & EMPTY_OCCUPANCIES) & checkBitboard) & tempPinBitboard
				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58])
					tempAttack &= tempAttack - 1

					moveList[moveCount][MOVE_STARTING] = startingSquare
					moveList[moveCount][MOVE_TARGET] = targetSquare
					moveList[moveCount][MOVE_TAG] = TAG_NONE
					moveList[moveCount][MOVE_PIECE] = WR
					moveCount+=1
				}
			}

			tempBitboard = PieceArray[WB]
			for tempBitboard != 0 {

				startingSquare = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58])
				tempBitboard &= tempBitboard - 1

				tempPinBitboard = MAX_ULONG
				if pinNumber != 0 {

					for i := 0; i < pinNumber; i+=1 {

						if pinArray[i][PINNED_SQUARE_INDEX] == startingSquare {

							tempPinBitboard = INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][PINNING_PIECE_INDEX]]
						}
					}
				}

				bishopAttacks :u64= GetBishopAttacksFast(startingSquare, COMBINED_OCCUPANCIES)

				tempAttack = ((bishopAttacks & BLACK_OCCUPANCIES) & checkBitboard) & tempPinBitboard
				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58])
					tempAttack &= tempAttack - 1

					moveList[moveCount][MOVE_STARTING] = startingSquare
					moveList[moveCount][MOVE_TARGET] = targetSquare
					moveList[moveCount][MOVE_TAG] = TAG_CAPTURE
					moveList[moveCount][MOVE_PIECE] = WB
					moveCount+=1
				}

				tempAttack = ((bishopAttacks & EMPTY_OCCUPANCIES) & checkBitboard) & tempPinBitboard
				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58])
					tempAttack &= tempAttack - 1

					moveList[moveCount][MOVE_STARTING] = startingSquare
					moveList[moveCount][MOVE_TARGET] = targetSquare
					moveList[moveCount][MOVE_TAG] = TAG_NONE
					moveList[moveCount][MOVE_PIECE] = WB
					moveCount+=1
				}
			}

			tempBitboard = PieceArray[WQ]
			for tempBitboard != 0 {

				startingSquare = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58])
				tempBitboard &= tempBitboard - 1

				tempPinBitboard = MAX_ULONG
				if pinNumber != 0 {

					for i := 0; i < pinNumber; i+=1 {

						if pinArray[i][PINNED_SQUARE_INDEX] == startingSquare {

							tempPinBitboard = INBETWEEN_BITBOARDS[whiteKingPosition][pinArray[i][PINNING_PIECE_INDEX]]
						}
					}
				}

				queenAttacks :u64= GetRookAttacksFast(startingSquare, COMBINED_OCCUPANCIES)
				queenAttacks |= GetBishopAttacksFast(startingSquare, COMBINED_OCCUPANCIES)

				tempAttack = ((queenAttacks & BLACK_OCCUPANCIES) & checkBitboard) & tempPinBitboard

				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58])
					tempAttack &= tempAttack - 1

					moveList[moveCount][MOVE_STARTING] = startingSquare
					moveList[moveCount][MOVE_TARGET] = targetSquare
					moveList[moveCount][MOVE_TAG] = TAG_CAPTURE
					moveList[moveCount][MOVE_PIECE] = WQ
					moveCount+=1
				}

				tempAttack = ((queenAttacks & EMPTY_OCCUPANCIES) & checkBitboard) & tempPinBitboard
				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58])
					tempAttack &= tempAttack - 1

					moveList[moveCount][MOVE_STARTING] = startingSquare
					moveList[moveCount][MOVE_TARGET] = targetSquare
					moveList[moveCount][MOVE_TAG] = TAG_NONE
					moveList[moveCount][MOVE_PIECE] = WQ
					moveCount+=1
				}
			}

		}
	} else { //black move

		blackKingCheckCount: int = 0
		blackKingPosition: int = (DEBRUIJN64[MAGIC*(PieceArray[BK]~(PieceArray[BK]-1))>>58])

       // if (blackKingPosition <0 || blackKingPosition > 63) {

            //fmt.println("no black king found, error");
            //PrintBoard();
            //assert(blackKingCheckCount >= 0 && blackKingCheckCount <= 63);
        //}

		//pawns
		tempBitboard = PieceArray[WP] & BLACK_PAWN_ATTACKS[blackKingPosition]
		if tempBitboard != 0 {

			pawn_square:int = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58])

			checkBitboard = SQUARE_BBS[pawn_square]
			
			blackKingCheckCount+=1;
		}

		//knights
		tempBitboard = PieceArray[WN] & KNIGHT_ATTACKS[blackKingPosition]
		if tempBitboard != 0 {

			knight_square: int = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58])

			checkBitboard = SQUARE_BBS[knight_square]
			
			blackKingCheckCount+=1;
		}

		//bishops
		bishopAttacksChecks :u64= GetBishopAttacksFast(blackKingPosition, WHITE_OCCUPANCIES)
		tempBitboard = PieceArray[WB] & bishopAttacksChecks
		for tempBitboard != 0 {

			piece_square: int = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58])
			tempPinBitboard = INBETWEEN_BITBOARDS[blackKingPosition][piece_square] & BLACK_OCCUPANCIES

			if tempPinBitboard == 0 {

				checkBitboard = INBETWEEN_BITBOARDS[blackKingPosition][piece_square]
				blackKingCheckCount+=1
				
			} else {

				pinned_square: int = (DEBRUIJN64[MAGIC*(tempPinBitboard~(tempPinBitboard-1))>>58])
				tempPinBitboard &= tempPinBitboard - 1

				if tempPinBitboard == 0 {

					pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square
					pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square
					pinNumber+=1
				}
			}
			tempBitboard &= tempBitboard - 1
		}

		//queen
		tempBitboard = PieceArray[WQ] & bishopAttacksChecks
		for tempBitboard != 0 {

			piece_square := (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58])
			tempPinBitboard = INBETWEEN_BITBOARDS[blackKingPosition][piece_square] & BLACK_OCCUPANCIES

			if tempPinBitboard == 0 {

				checkBitboard = INBETWEEN_BITBOARDS[blackKingPosition][piece_square]
				blackKingCheckCount+=1
			} else {

				pinned_square:int = (DEBRUIJN64[MAGIC*(tempPinBitboard~(tempPinBitboard-1))>>58])
				tempPinBitboard &= tempPinBitboard - 1

				if tempPinBitboard == 0 {

					pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square
					pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square
					pinNumber+=1
				}
			}
			tempBitboard &= tempBitboard - 1
		}

		//rook
		rook_attacks :u64= GetRookAttacksFast(blackKingPosition, WHITE_OCCUPANCIES)
		tempBitboard = PieceArray[WR] & rook_attacks
		for tempBitboard != 0 {

			piece_square := (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58])
			tempPinBitboard = INBETWEEN_BITBOARDS[blackKingPosition][piece_square] & BLACK_OCCUPANCIES

			if tempPinBitboard == 0 {
				
				checkBitboard = INBETWEEN_BITBOARDS[blackKingPosition][piece_square]
				blackKingCheckCount+=1
			} else {

				pinned_square := (DEBRUIJN64[MAGIC*(tempPinBitboard~(tempPinBitboard-1))>>58])
				tempPinBitboard &= tempPinBitboard - 1

				if tempPinBitboard == 0 {

					pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square
					pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square
					pinNumber+=1
				}
			}
			tempBitboard &= tempBitboard - 1
		}

		//queen
		tempBitboard = PieceArray[WQ] & rook_attacks
		for tempBitboard != 0 {

			piece_square :int= (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58])
			tempPinBitboard = INBETWEEN_BITBOARDS[blackKingPosition][piece_square] & BLACK_OCCUPANCIES

			if tempPinBitboard == 0 {

				checkBitboard = INBETWEEN_BITBOARDS[blackKingPosition][piece_square]
				blackKingCheckCount+=1
			} else {

				pinned_square := (DEBRUIJN64[MAGIC*(tempPinBitboard~(tempPinBitboard-1))>>58])
				tempPinBitboard &= tempPinBitboard - 1

				if tempPinBitboard == 0 {

					pinArray[pinNumber][PINNED_SQUARE_INDEX] = pinned_square
					pinArray[pinNumber][PINNING_PIECE_INDEX] = piece_square
					pinNumber+=1
				}
			}
			tempBitboard &= tempBitboard - 1
		}

        occupancyWithoutBlackKing := COMBINED_OCCUPANCIES & (~PieceArray[BK])
        tempAttack = KING_ATTACKS[blackKingPosition] & WHITE_OCCUPANCIES

        for tempAttack != 0 {

            targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58])
            tempAttack &= tempAttack - 1

            if (PieceArray[WP] & BLACK_PAWN_ATTACKS[targetSquare]) != 0 {
                continue
            }
            if (PieceArray[WN] & KNIGHT_ATTACKS[targetSquare]) != 0 {
                continue
            }
            if (PieceArray[WK] & KING_ATTACKS[targetSquare]) != 0 {
                continue
            }
            bishopAttacks := GetBishopAttacksFast(targetSquare, occupancyWithoutBlackKing)
            if (PieceArray[WB] & bishopAttacks) != 0 {
                continue
            }
            if (PieceArray[WQ] & bishopAttacks) != 0 {
                continue
            }
            rookAttacks := GetRookAttacksFast(targetSquare, occupancyWithoutBlackKing)
            if (PieceArray[WR] & rookAttacks) != 0 {
                continue
            }
            if (PieceArray[WQ] & rookAttacks) != 0 {
                continue
            }

            moveList[moveCount][MOVE_STARTING] = blackKingPosition
            moveList[moveCount][MOVE_TARGET] = targetSquare
            moveList[moveCount][MOVE_TAG] = TAG_CAPTURE
            moveList[moveCount][MOVE_PIECE] = BK
            moveCount+=1
        }

        tempAttack = KING_ATTACKS[blackKingPosition] & EMPTY_OCCUPANCIES;

        for tempAttack != 0 {
            targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58])
            tempAttack &= tempAttack - 1

            if (PieceArray[WP] & WHITE_PAWN_ATTACKS[targetSquare]) != 0 {
                continue
            }
            if (PieceArray[WN] & KNIGHT_ATTACKS[targetSquare]) != 0 {
                continue
            }
            if (PieceArray[WK] & KING_ATTACKS[targetSquare]) != 0 {
                continue
            }
            bishopAttacks := GetBishopAttacksFast(targetSquare, occupancyWithoutBlackKing)
            if (PieceArray[WB] & bishopAttacks) != 0 {
                continue
            }
            if (PieceArray[WQ] & bishopAttacks) != 0 {
                continue
            }
            rookAttacks := GetRookAttacksFast(targetSquare, occupancyWithoutBlackKing)
            if (PieceArray[WR] & rookAttacks) != 0 {
                continue
            }
            if (PieceArray[WQ] & rookAttacks) != 0 {
                continue
            }

            moveList[moveCount][MOVE_STARTING] = blackKingPosition;
            moveList[moveCount][MOVE_TARGET] = targetSquare
            moveList[moveCount][MOVE_TAG] = TAG_NONE
            moveList[moveCount][MOVE_PIECE] = BK
            moveCount+=1
        }

		if blackKingCheckCount < 2 {

			if blackKingCheckCount == 0 {
				checkBitboard = MAX_ULONG
			}

			tempBitboard = PieceArray[BP]

			for tempBitboard != 0 {
				startingSquare = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58])
				tempBitboard &= tempBitboard - 1

				tempPinBitboard = MAX_ULONG
				if pinNumber != 0 {
					for i := 0; i < pinNumber; i+=1 {

						if pinArray[i][PINNED_SQUARE_INDEX] == startingSquare {
							tempPinBitboard = INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][PINNING_PIECE_INDEX]]
						}
					}
				}

				if (SQUARE_BBS[startingSquare+8] & COMBINED_OCCUPANCIES) == 0 { //if up one square is empty

					if ((SQUARE_BBS[startingSquare+8] & checkBitboard) & tempPinBitboard) != 0 {

						if (SQUARE_BBS[startingSquare] & RANK_2_BITBOARD) != 0 { //if promotion

							moveList[moveCount][MOVE_STARTING] = startingSquare
							moveList[moveCount][MOVE_TARGET] = startingSquare + 8
							moveList[moveCount][MOVE_TAG] = TAG_BBishopPromotion
							moveList[moveCount][MOVE_PIECE] = BP
							moveCount+=1

							moveList[moveCount][MOVE_STARTING] = startingSquare
							moveList[moveCount][MOVE_TARGET] = startingSquare + 8
							moveList[moveCount][MOVE_TAG] = TAG_BKnightPromotion
							moveList[moveCount][MOVE_PIECE] = BP
							moveCount+=1

							moveList[moveCount][MOVE_STARTING] = startingSquare
							moveList[moveCount][MOVE_TARGET] = startingSquare + 8
							moveList[moveCount][MOVE_TAG] = TAG_BRookPromotion
							moveList[moveCount][MOVE_PIECE] = BP
							moveCount+=1

							moveList[moveCount][MOVE_STARTING] = startingSquare
							moveList[moveCount][MOVE_TARGET] = startingSquare + 8
							moveList[moveCount][MOVE_TAG] = TAG_BQueenPromotion
							moveList[moveCount][MOVE_PIECE] = BP
							moveCount+=1
						} else {

							moveList[moveCount][MOVE_STARTING] = startingSquare
							moveList[moveCount][MOVE_TARGET] = startingSquare + 8
							moveList[moveCount][MOVE_TAG] = TAG_NONE
							moveList[moveCount][MOVE_PIECE] = BP
							moveCount+=1
						}
					}

					if (SQUARE_BBS[startingSquare] & RANK_7_BITBOARD) != 0 { //if on rank 2

						if ((SQUARE_BBS[startingSquare+16] & checkBitboard) & tempPinBitboard) != 0 {

							if ((SQUARE_BBS[startingSquare+16]) & COMBINED_OCCUPANCIES) == 0 { //if up two squares and one square are empty

								moveList[moveCount][MOVE_STARTING] = startingSquare
								moveList[moveCount][MOVE_TARGET] = startingSquare + 16
								moveList[moveCount][MOVE_TAG] = TAG_DoublePawnBlack
								moveList[moveCount][MOVE_PIECE] = BP
								moveCount+=1
							}
						}
					}
				}

				tempAttack = ((BLACK_PAWN_ATTACKS[startingSquare] & WHITE_OCCUPANCIES) & checkBitboard) & tempPinBitboard //if black piece diagonal to pawn

				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58]) //find the bit
					tempAttack &= tempAttack - 1

					if (SQUARE_BBS[startingSquare] & RANK_2_BITBOARD) != 0 { //if promotion

						moveList[moveCount][MOVE_STARTING] = startingSquare
						moveList[moveCount][MOVE_TARGET] = targetSquare
						moveList[moveCount][MOVE_TAG] = TAG_BCaptureQueenPromotion
						moveList[moveCount][MOVE_PIECE] = BP
						moveCount+=1

						moveList[moveCount][MOVE_STARTING] = startingSquare
						moveList[moveCount][MOVE_TARGET] = targetSquare
						moveList[moveCount][MOVE_TAG] = TAG_BCaptureRookPromotion
						moveList[moveCount][MOVE_PIECE] = BP
						moveCount+=1

						moveList[moveCount][MOVE_STARTING] = startingSquare
						moveList[moveCount][MOVE_TARGET] = targetSquare
						moveList[moveCount][MOVE_TAG] = TAG_BCaptureKnightPromotion
						moveList[moveCount][MOVE_PIECE] = BP
						moveCount+=1

						moveList[moveCount][MOVE_STARTING] = startingSquare
						moveList[moveCount][MOVE_TARGET] = targetSquare
						moveList[moveCount][MOVE_TAG] = TAG_BCaptureBishopPromotion
						moveList[moveCount][MOVE_PIECE] = BP
						moveCount+=1
					} else {

						moveList[moveCount][MOVE_STARTING] = startingSquare
						moveList[moveCount][MOVE_TARGET] = targetSquare
						moveList[moveCount][MOVE_TAG] = TAG_CAPTURE
						moveList[moveCount][MOVE_PIECE] = BP
						moveCount+=1
					}
				}

				if (SQUARE_BBS[startingSquare] & RANK_4_BITBOARD) != 0 { //check rank for ep

					if ep != NO_SQUARE {

						if (((BLACK_PAWN_ATTACKS[startingSquare] & SQUARE_BBS[ep]) & checkBitboard) & tempPinBitboard) != 0 {

							if (PieceArray[BK] & RANK_4_BITBOARD) == 0 { //if no king on rank 5

								moveList[moveCount][MOVE_STARTING] = startingSquare
								moveList[moveCount][MOVE_TARGET] = int(ep)
								moveList[moveCount][MOVE_TAG] = TAG_BLACKEP
								moveList[moveCount][MOVE_PIECE] = BP
								moveCount+=1
							} else if (PieceArray[WR]&RANK_4_BITBOARD) == 0 && (PieceArray[WQ]&RANK_4_BITBOARD) == 0 { // if no b rook or queen on rank 5

								moveList[moveCount][MOVE_STARTING] = startingSquare
								moveList[moveCount][MOVE_TARGET] = int(ep)
								moveList[moveCount][MOVE_TAG] = TAG_BLACKEP
								moveList[moveCount][MOVE_PIECE] = BP
								moveCount+=1
							} else { //wk and br or bq on rank 5

								occupancyWithoutEPPawns :u64= COMBINED_OCCUPANCIES & ~SQUARE_BBS[startingSquare]
								occupancyWithoutEPPawns &= ~SQUARE_BBS[ep-8]

								rookAttacksFromKing :u64= GetRookAttacksFast(blackKingPosition, occupancyWithoutEPPawns);

								if (rookAttacksFromKing & PieceArray[WR]) == 0 {

									if (rookAttacksFromKing & PieceArray[WQ]) == 0 {
										moveList[moveCount][MOVE_STARTING] = startingSquare
										moveList[moveCount][MOVE_TARGET] = int(ep)
										moveList[moveCount][MOVE_TAG] = TAG_BLACKEP
										moveList[moveCount][MOVE_PIECE] = BP
										moveCount+=1
									}
								}
							}
						}
					}
				}
			}

			tempBitboard = PieceArray[BN]

			for tempBitboard != 0 {

				startingSquare = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58]) //looks for the startingSquare
				tempBitboard &= tempBitboard - 1                                         //removes the knight from that square to not infinitely loop

				tempPinBitboard = MAX_ULONG
				if pinNumber != 0 {

					for i := 0; i < pinNumber; i+=1 {

						if pinArray[i][PINNED_SQUARE_INDEX] == startingSquare {

							tempPinBitboard = INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][PINNING_PIECE_INDEX]]
						}
					}
				}

				tempAttack = ((KNIGHT_ATTACKS[startingSquare] & WHITE_OCCUPANCIES) & checkBitboard) & tempPinBitboard //gets knight captures
				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58])
					tempAttack &= tempAttack - 1

					moveList[moveCount][MOVE_STARTING] = startingSquare
					moveList[moveCount][MOVE_TARGET] = targetSquare
					moveList[moveCount][MOVE_TAG] = TAG_CAPTURE
					moveList[moveCount][MOVE_PIECE] = BN
					moveCount+=1
				}

				tempAttack = ((KNIGHT_ATTACKS[startingSquare] & (~COMBINED_OCCUPANCIES)) & checkBitboard) & tempPinBitboard

				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58])
					tempAttack &= tempAttack - 1

					moveList[moveCount][MOVE_STARTING] = startingSquare
					moveList[moveCount][MOVE_TARGET] = targetSquare
					moveList[moveCount][MOVE_TAG] = TAG_NONE
					moveList[moveCount][MOVE_PIECE] = BN
					moveCount+=1
				}
			}

			tempBitboard = PieceArray[BB]
			for tempBitboard != 0 {

				startingSquare = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58])
				tempBitboard &= tempBitboard - 1

				tempPinBitboard = MAX_ULONG
				if pinNumber != 0 {
					for i := 0; i < pinNumber; i+=1 {

						if pinArray[i][PINNED_SQUARE_INDEX] == startingSquare {

							tempPinBitboard = INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][PINNING_PIECE_INDEX]]
						}
					}
				}

				bishopAttacks :u64= GetBishopAttacksFast(startingSquare, COMBINED_OCCUPANCIES)

				tempAttack = ((bishopAttacks & WHITE_OCCUPANCIES) & checkBitboard) & tempPinBitboard
				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58])
					tempAttack &= tempAttack - 1

					moveList[moveCount][MOVE_STARTING] = startingSquare
					moveList[moveCount][MOVE_TARGET] = targetSquare
					moveList[moveCount][MOVE_TAG] = TAG_CAPTURE
					moveList[moveCount][MOVE_PIECE] = BB
					moveCount+=1
				}

				tempAttack = ((bishopAttacks & (~COMBINED_OCCUPANCIES)) & checkBitboard) & tempPinBitboard
				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58])
					tempAttack &= tempAttack - 1

					moveList[moveCount][MOVE_STARTING] = startingSquare
					moveList[moveCount][MOVE_TARGET] = targetSquare
					moveList[moveCount][MOVE_TAG] = TAG_NONE
					moveList[moveCount][MOVE_PIECE] = BB
					moveCount+=1
				}
			}

			tempBitboard = PieceArray[BR]
			for tempBitboard != 0 {

				startingSquare = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58])
				tempBitboard &= tempBitboard - 1

				tempPinBitboard = MAX_ULONG
				if pinNumber != 0 {

					for i := 0; i < pinNumber; i+=1 {

						if pinArray[i][PINNED_SQUARE_INDEX] == startingSquare {

							tempPinBitboard = INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][PINNING_PIECE_INDEX]]
						}
					}
				}

				rookAttacks :u64= GetRookAttacksFast(startingSquare, COMBINED_OCCUPANCIES)

				tempAttack = ((rookAttacks & WHITE_OCCUPANCIES) & checkBitboard) & tempPinBitboard
				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58])
					tempAttack &= tempAttack - 1

					moveList[moveCount][MOVE_STARTING] = startingSquare
					moveList[moveCount][MOVE_TARGET] = targetSquare
					moveList[moveCount][MOVE_TAG] = TAG_CAPTURE
					moveList[moveCount][MOVE_PIECE] = BR
					moveCount+=1
				}

				tempAttack = ((rookAttacks & (~COMBINED_OCCUPANCIES)) & checkBitboard) & tempPinBitboard
				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58])
					tempAttack &= tempAttack - 1

					moveList[moveCount][MOVE_STARTING] = startingSquare
					moveList[moveCount][MOVE_TARGET] = targetSquare
					moveList[moveCount][MOVE_TAG] = TAG_NONE
					moveList[moveCount][MOVE_PIECE] = BR
					moveCount+=1
				}
			}

			tempBitboard = PieceArray[BQ]
			for tempBitboard != 0 {

				startingSquare = (DEBRUIJN64[MAGIC*(tempBitboard~(tempBitboard-1))>>58])
				tempBitboard &= tempBitboard - 1

				tempPinBitboard = MAX_ULONG
				if pinNumber != 0 {

					for i := 0; i < pinNumber; i+=1 {

						if pinArray[i][PINNED_SQUARE_INDEX] == startingSquare {

							tempPinBitboard = INBETWEEN_BITBOARDS[blackKingPosition][pinArray[i][PINNING_PIECE_INDEX]]
						}
					}
				}

				queenAttacks :u64= GetRookAttacksFast(startingSquare, COMBINED_OCCUPANCIES)
				queenAttacks |= GetBishopAttacksFast(startingSquare, COMBINED_OCCUPANCIES)

				tempAttack = ((queenAttacks & WHITE_OCCUPANCIES) & checkBitboard) & tempPinBitboard

				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58])
					tempAttack &= tempAttack - 1

					moveList[moveCount][MOVE_STARTING] = startingSquare
					moveList[moveCount][MOVE_TARGET] = targetSquare
					moveList[moveCount][MOVE_TAG] = TAG_CAPTURE
					moveList[moveCount][MOVE_PIECE] = BQ
					moveCount+=1
				}

				tempAttack = ((queenAttacks & (~COMBINED_OCCUPANCIES)) & checkBitboard) & tempPinBitboard

				for tempAttack != 0 {

					targetSquare = (DEBRUIJN64[MAGIC*(tempAttack~(tempAttack-1))>>58])
					tempAttack &= tempAttack - 1

					moveList[moveCount][MOVE_STARTING] = startingSquare
					moveList[moveCount][MOVE_TARGET] = targetSquare
					moveList[moveCount][MOVE_TAG] = TAG_NONE
					moveList[moveCount][MOVE_PIECE] = BQ
					moveCount+=1
				}
			}
		}
		if blackKingCheckCount == 0 {

			if CastleRights[BKS_CASTLE_RIGHTS] == true {

				if blackKingPosition == E8 { //king on e1

					if (BKS_EMPTY_BITBOARD & COMBINED_OCCUPANCIES) == 0 { //f1 and g1 empty

						if (PieceArray[BR] & SQUARE_BBS[H8]) != 0 { //rook on h1

							if Is_Square_Attacked_By_White(F8, COMBINED_OCCUPANCIES) == false {

								if Is_Square_Attacked_By_White(G8, COMBINED_OCCUPANCIES) == false {

									moveList[moveCount][MOVE_STARTING] = E8
									moveList[moveCount][MOVE_TARGET] = G8
									moveList[moveCount][MOVE_TAG] = TAG_BCASTLEKS
									moveList[moveCount][MOVE_PIECE] = BK
									moveCount+=1
								}
							}
						}
					}
				}
			}
			if CastleRights[BQS_CASTLE_RIGHTS] == true {

				if blackKingPosition == E8 { //king on e1

					if (BQS_EMPTY_BITBOARD & COMBINED_OCCUPANCIES) == 0 { //f1 and g1 empty

						if (PieceArray[BR] & SQUARE_BBS[A8]) != 0 { //rook on h1

							if Is_Square_Attacked_By_White(C8, COMBINED_OCCUPANCIES) == false {

								if Is_Square_Attacked_By_White(D8, COMBINED_OCCUPANCIES) == false {

									moveList[moveCount][MOVE_STARTING] = E8
									moveList[moveCount][MOVE_TARGET] = C8
									moveList[moveCount][MOVE_TAG] = TAG_BCASTLEQS
									moveList[moveCount][MOVE_PIECE] = BK
									moveCount+=1
								}
							}
						}
					}
				}
			}
		}
	}

	if depth == 1 {
		return moveCount;
	}

	nodes: int = 0
	priorNodes: int
	copyEp: int = ep
	copyCastle: [4]bool;
	copyCastle[0] = CastleRights[0]
	copyCastle[1] = CastleRights[1]
	copyCastle[2] = CastleRights[2]
	copyCastle[3] = CastleRights[3]

	for moveIndex := 0; moveIndex < int(moveCount); moveIndex+=1 {

		startingSquare := moveList[moveIndex][MOVE_STARTING]
		targetSquare := moveList[moveIndex][MOVE_TARGET]
		piece := moveList[moveIndex][MOVE_PIECE]
		tag := moveList[moveIndex][MOVE_TAG]

       // if (startingSquare < 0 || startingSquare > 63) {
            //fmt.printf("INVALID ST SQ: depth: %d, ply: %d, st: %d, tar: %d, piece: %d, tag: %d\n", depth, ply, startingSquare, targetSquare, piece, tag);
        //}

        //assert(startingSquare >= 0 && startingSquare <= 63, "invalid st sq");
        //assert(targetSquare >= 0 && targetSquare <= 63, "invalid tar sq");
        //assert(piece >= 0 && piece <= 11, "invalid piece");

		captureIndex: int = -1

		whiteToPlay = !whiteToPlay

		switch tag {
		case TAG_NONE: //none
			PieceArray[piece] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			ep = NO_SQUARE
		case TAG_CHECK: //check
			PieceArray[piece] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			ep = NO_SQUARE
		case TAG_CAPTURE: //capture
			PieceArray[piece] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			if PieceIsWhite(piece) {

				for i := BLACK_START_INDEX; i <= BLACK_END_INDEX; i+=1 {

					if (PieceArray[i] & SQUARE_BBS[targetSquare]) != 0 {

						captureIndex = i
						break
					}
				}
				PieceArray[captureIndex] &= ~SQUARE_BBS[targetSquare]

			} else { //is black

				for i := WHITE_START_INDEX; i <= WHITE_END_INDEX; i+=1 {

					if (PieceArray[i] & SQUARE_BBS[targetSquare]) != 0 {

						captureIndex = i
						break
					}
				}
				PieceArray[captureIndex] &= ~SQUARE_BBS[targetSquare]

			}
			ep = NO_SQUARE
		case TAG_CHECK_CAPTURE: //check cap
			PieceArray[piece] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			if PieceIsWhite(piece) {

				for i := BLACK_START_INDEX; i <= BLACK_END_INDEX; i+=1 {

					if (PieceArray[i] & SQUARE_BBS[targetSquare]) != 0 {

						captureIndex = i
						break
					}
				}
				PieceArray[captureIndex] &= ~SQUARE_BBS[targetSquare]

			} else { //is black

				for i := WHITE_START_INDEX; i <= WHITE_END_INDEX; i+=1 {

					if (PieceArray[i] & SQUARE_BBS[targetSquare]) != 0 {

						captureIndex = i
						break
					}
				}
				PieceArray[captureIndex] &= ~SQUARE_BBS[targetSquare]

			}
			ep = NO_SQUARE
		case TAG_WHITEEP: //white ep
			//move piece
			PieceArray[WP] |= SQUARE_BBS[targetSquare]
			PieceArray[WP] &= ~SQUARE_BBS[startingSquare]
			//remove
			PieceArray[BP] &= ~SQUARE_BBS[targetSquare+8]
			ep = NO_SQUARE
		case TAG_BLACKEP: //black ep
			//move piece
			PieceArray[BP] |= SQUARE_BBS[targetSquare]
			PieceArray[BP] &= ~SQUARE_BBS[startingSquare]
			//remove white pawn square up
			PieceArray[WP] &= ~SQUARE_BBS[targetSquare-8]
			ep = NO_SQUARE

		case TAG_WCASTLEKS: //WKS
			//white king
			PieceArray[WK] |= SQUARE_BBS[G1]
			PieceArray[WK] &= ~SQUARE_BBS[E1]
			//white rook
			PieceArray[WR] |= SQUARE_BBS[F1]
			PieceArray[WR] &= ~SQUARE_BBS[H1]
			//occupancies
			CastleRights[WKS_CASTLE_RIGHTS] = false
			CastleRights[WQS_CASTLE_RIGHTS] = false
			ep = NO_SQUARE

		case TAG_WCASTLEQS: //WQS
			//white king
			PieceArray[WK] |= SQUARE_BBS[C1]
			PieceArray[WK] &= ~SQUARE_BBS[E1]
			//white rook
			PieceArray[WR] |= SQUARE_BBS[D1]
			PieceArray[WR] &= ~SQUARE_BBS[A1]

			CastleRights[WKS_CASTLE_RIGHTS] = false
			CastleRights[WQS_CASTLE_RIGHTS] = false
			ep = NO_SQUARE

		case TAG_BCASTLEKS: //BKS
			//white king
			PieceArray[BK] |= SQUARE_BBS[G8]
			PieceArray[BK] &= ~SQUARE_BBS[E8]
			//white rook
			PieceArray[BR] |= SQUARE_BBS[F8]
			PieceArray[BR] &= ~SQUARE_BBS[H8]
			CastleRights[BKS_CASTLE_RIGHTS] = false
			CastleRights[BQS_CASTLE_RIGHTS] = false
			ep = NO_SQUARE

		case TAG_BCASTLEQS: //BQS
			//white king
			PieceArray[BK] |= SQUARE_BBS[C8]
			PieceArray[BK] &= ~SQUARE_BBS[E8]
			//white rook
			PieceArray[BR] |= SQUARE_BBS[D8]
			PieceArray[BR] &= ~SQUARE_BBS[A8]
			CastleRights[BKS_CASTLE_RIGHTS] = false
			CastleRights[BQS_CASTLE_RIGHTS] = false
			ep = NO_SQUARE

		case TAG_BKnightPromotion: //BNPr
			PieceArray[BN] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			ep = NO_SQUARE

		case TAG_BBishopPromotion: //BBPr
			PieceArray[BB] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			ep = NO_SQUARE

		case TAG_BQueenPromotion: //BQPr
			PieceArray[BQ] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			ep = NO_SQUARE

		case TAG_BRookPromotion: //BRPr
			PieceArray[BR] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			ep = NO_SQUARE

		case 12: //WNPr
			PieceArray[WN] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			ep = NO_SQUARE

		case 13: //WBPr
			PieceArray[WB] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			ep = NO_SQUARE

		case 14: //WQPr
			PieceArray[WQ] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			ep = NO_SQUARE

		case 15: //WRPr
			PieceArray[WR] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			ep = NO_SQUARE

		case 16: //BNPrCAP
			PieceArray[BN] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			ep = NO_SQUARE
			for i := WHITE_START_INDEX; i <= WHITE_END_INDEX; i+=1 {

				if (PieceArray[i] & SQUARE_BBS[targetSquare]) != 0 {

					captureIndex = i
					break
				}
			}
			PieceArray[captureIndex] &= ~SQUARE_BBS[targetSquare]

		case 17: //BBPrCAP
			PieceArray[BB] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]

			ep = NO_SQUARE
			for i := WHITE_START_INDEX; i <= WHITE_END_INDEX; i+=1 {

				if (PieceArray[i] & SQUARE_BBS[targetSquare]) != 0 {

					captureIndex = i
					break
				}
			}
			PieceArray[captureIndex] &= ~SQUARE_BBS[targetSquare]

		case 18: //BQPrCAP
			PieceArray[BQ] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			ep = NO_SQUARE
			for i := WHITE_START_INDEX; i <= WHITE_END_INDEX; i+=1 {

				if (PieceArray[i] & SQUARE_BBS[targetSquare]) != 0 {

					captureIndex = i
					break
				}
			}
			PieceArray[captureIndex] &= ~SQUARE_BBS[targetSquare]

		case 19: //BRPrCAP
			PieceArray[BR] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			ep = NO_SQUARE
			for i := WHITE_START_INDEX; i <= WHITE_END_INDEX; i+=1 {

				if (PieceArray[i] & SQUARE_BBS[targetSquare]) != 0 {

					captureIndex = i
					break
				}
			}
			PieceArray[captureIndex] &= ~SQUARE_BBS[targetSquare]

		case 20: //WNPrCAP
			PieceArray[WN] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			ep = NO_SQUARE
			for i := BLACK_START_INDEX; i <= BLACK_END_INDEX; i+=1 {

				if (PieceArray[i] & SQUARE_BBS[targetSquare]) != 0 {

					captureIndex = i
					break
				}
			}
			PieceArray[captureIndex] &= ~SQUARE_BBS[targetSquare]

		case 21: //WBPrCAP
			PieceArray[WB] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			ep = NO_SQUARE
			for i := BLACK_START_INDEX; i <= BLACK_END_INDEX; i+=1 {

				if (PieceArray[i] & SQUARE_BBS[targetSquare]) != 0 {

					captureIndex = i
					break
				}
			}
			PieceArray[captureIndex] &= ~SQUARE_BBS[targetSquare]

		case 22: //WQPrCAP
			PieceArray[WQ] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			ep = NO_SQUARE
			for i := BLACK_START_INDEX; i <= BLACK_END_INDEX; i+=1 {

				if (PieceArray[i] & SQUARE_BBS[targetSquare]) != 0 {

					captureIndex = i
					break
				}
			}
			PieceArray[captureIndex] &= ~SQUARE_BBS[targetSquare]

		case 23: //WRPrCAP
			PieceArray[WR] |= SQUARE_BBS[targetSquare]
			PieceArray[piece] &= ~SQUARE_BBS[startingSquare]
			ep = NO_SQUARE
			for i := BLACK_START_INDEX; i <= BLACK_END_INDEX; i+=1 {

				if (PieceArray[i] & SQUARE_BBS[targetSquare]) != 0 {

					captureIndex = i
					break
				}
			}
			PieceArray[captureIndex] &= ~SQUARE_BBS[targetSquare]

		case 24: //WDouble
			PieceArray[WP] |= SQUARE_BBS[targetSquare]
			PieceArray[WP] &= ~SQUARE_BBS[startingSquare]
			ep = targetSquare + 8

		case 25: //BDouble
			PieceArray[BP] |= SQUARE_BBS[targetSquare]
			PieceArray[BP] &= ~SQUARE_BBS[startingSquare]
			ep = targetSquare - 8
		}

		if piece == WK {

			CastleRights[WKS_CASTLE_RIGHTS] = false
			CastleRights[WQS_CASTLE_RIGHTS] = false
		} else if piece == BK {

			CastleRights[BKS_CASTLE_RIGHTS] = false
			CastleRights[BQS_CASTLE_RIGHTS] = false
		} else if piece == WR {

			if CastleRights[WKS_CASTLE_RIGHTS] == true {

				if (PieceArray[WR] & SQUARE_BBS[H1]) == 0 {

					CastleRights[WKS_CASTLE_RIGHTS] = false
				}
			}
			if CastleRights[WQS_CASTLE_RIGHTS] == true {

				if (PieceArray[WR] & SQUARE_BBS[A1]) == 0 {

					CastleRights[WQS_CASTLE_RIGHTS] = false
				}
			}
		} else if piece == BR {

			if CastleRights[BKS_CASTLE_RIGHTS] == true {

				if (PieceArray[BR] & SQUARE_BBS[H8]) == 0 {

					CastleRights[BKS_CASTLE_RIGHTS] = false
				}
			}
			if CastleRights[BQS_CASTLE_RIGHTS] == true {

				if (PieceArray[BR] & SQUARE_BBS[A8]) == 0 {

					CastleRights[BQS_CASTLE_RIGHTS] = false
				}
			}
		}

		priorNodes = nodes
		nodes += PerftInline(depth-1, ply+1)

		whiteToPlay = !whiteToPlay
		switch tag {
		case TAG_NONE: //none
			PieceArray[piece] |= SQUARE_BBS[startingSquare]
			PieceArray[piece] &= ~SQUARE_BBS[targetSquare]
		case TAG_CHECK: //check
			PieceArray[piece] |= SQUARE_BBS[startingSquare]
			PieceArray[piece] &= ~SQUARE_BBS[targetSquare]

		case TAG_CAPTURE: //capture
			PieceArray[piece] |= SQUARE_BBS[startingSquare]
			PieceArray[piece] &= ~SQUARE_BBS[targetSquare]
			if PieceIsWhite(piece) == true {

				PieceArray[captureIndex] |= SQUARE_BBS[targetSquare]
			} else { //is black

				PieceArray[captureIndex] |= SQUARE_BBS[targetSquare]
			}
		case TAG_CHECK_CAPTURE: //check cap
			PieceArray[piece] |= SQUARE_BBS[startingSquare]
			PieceArray[piece] &= ~SQUARE_BBS[targetSquare]
			if PieceIsWhite(piece) == true {

				PieceArray[captureIndex] |= SQUARE_BBS[targetSquare]
			} else { //is black

				PieceArray[captureIndex] |= SQUARE_BBS[targetSquare]
			}

		case TAG_WHITEEP: //white ep
			PieceArray[WP] |= SQUARE_BBS[startingSquare]
			PieceArray[WP] &= ~SQUARE_BBS[targetSquare]
			PieceArray[BP] |= SQUARE_BBS[targetSquare+8]

		case TAG_BLACKEP: //black ep
			PieceArray[BP] |= SQUARE_BBS[startingSquare]
			PieceArray[BP] &= ~SQUARE_BBS[targetSquare]
			PieceArray[WP] |= SQUARE_BBS[targetSquare-8]

		case TAG_WCASTLEKS: //WKS
			//white king
			PieceArray[WK] |= SQUARE_BBS[E1]
			PieceArray[WK] &= ~SQUARE_BBS[G1]
			//white rook
			PieceArray[WR] |= SQUARE_BBS[H1]
			PieceArray[WR] &= ~SQUARE_BBS[F1]

		case TAG_WCASTLEQS: //WQS
			//white king
			PieceArray[WK] |= SQUARE_BBS[E1]
			PieceArray[WK] &= ~SQUARE_BBS[C1]
			//white rook
			PieceArray[WR] |= SQUARE_BBS[A1]
			PieceArray[WR] &= ~SQUARE_BBS[D1]

		case TAG_BCASTLEKS: //BKS
			//white king
			PieceArray[BK] |= SQUARE_BBS[E8]
			PieceArray[BK] &= ~SQUARE_BBS[G8]
			//white rook
			PieceArray[BR] |= SQUARE_BBS[H8]
			PieceArray[BR] &= ~SQUARE_BBS[F8]

		case TAG_BCASTLEQS: //BQS
			//white king
			PieceArray[BK] |= SQUARE_BBS[E8]
			PieceArray[BK] &= ~SQUARE_BBS[C8]
			//white rook
			PieceArray[BR] |= SQUARE_BBS[A8]
			PieceArray[BR] &= ~SQUARE_BBS[D8]

		case TAG_BKnightPromotion: //BNPr
			PieceArray[BP] |= SQUARE_BBS[startingSquare]
			PieceArray[BN] &= ~SQUARE_BBS[targetSquare]

		case TAG_BBishopPromotion: //BBPr
			PieceArray[BP] |= SQUARE_BBS[startingSquare]
			PieceArray[BB] &= ~SQUARE_BBS[targetSquare]

		case TAG_BQueenPromotion: //BQPr
			PieceArray[BP] |= SQUARE_BBS[startingSquare]
			PieceArray[BQ] &= ~SQUARE_BBS[targetSquare]

		case TAG_BRookPromotion: //BRPr
			PieceArray[BP] |= SQUARE_BBS[startingSquare]
			PieceArray[BR] &= ~SQUARE_BBS[targetSquare]

		case TAG_WKnightPromotion: //WNPr
			PieceArray[WP] |= SQUARE_BBS[startingSquare]
			PieceArray[WN] &= ~SQUARE_BBS[targetSquare]

		case TAG_WBishopPromotion: //WBPr
			PieceArray[WP] |= SQUARE_BBS[startingSquare]
			PieceArray[WB] &= ~SQUARE_BBS[targetSquare]

		case TAG_WQueenPromotion: //WQPr
			PieceArray[WP] |= SQUARE_BBS[startingSquare]
			PieceArray[WQ] &= ~SQUARE_BBS[targetSquare]

		case TAG_WRookPromotion: //WRPr
			PieceArray[WP] |= SQUARE_BBS[startingSquare]
			PieceArray[WR] &= ~SQUARE_BBS[targetSquare]

		case TAG_BCaptureKnightPromotion: //BNPrCAP
			PieceArray[BP] |= SQUARE_BBS[startingSquare]
			PieceArray[BN] &= ~SQUARE_BBS[targetSquare]
			PieceArray[captureIndex] |= SQUARE_BBS[targetSquare]

		case TAG_BCaptureBishopPromotion: //BBPrCAP
			PieceArray[BP] |= SQUARE_BBS[startingSquare]
			PieceArray[BB] &= ~SQUARE_BBS[targetSquare]
			PieceArray[captureIndex] |= SQUARE_BBS[targetSquare]

		case TAG_BCaptureQueenPromotion: //BQPrCAP
			PieceArray[BP] |= SQUARE_BBS[startingSquare]
			PieceArray[BQ] &= ~SQUARE_BBS[targetSquare]
			PieceArray[captureIndex] |= SQUARE_BBS[targetSquare]

		case TAG_BCaptureRookPromotion: //BRPrCAP
			PieceArray[BP] |= SQUARE_BBS[startingSquare]
			PieceArray[BR] &= ~SQUARE_BBS[targetSquare]
			PieceArray[captureIndex] |= SQUARE_BBS[targetSquare]

		case TAG_WCaptureKnightPromotion: //WNPrCAP
			PieceArray[WP] |= SQUARE_BBS[startingSquare]
			PieceArray[WN] &= ~SQUARE_BBS[targetSquare]
			PieceArray[captureIndex] |= SQUARE_BBS[targetSquare]

		case TAG_WCaptureBishopPromotion: //WBPrCAP
			PieceArray[WP] |= SQUARE_BBS[startingSquare]
			PieceArray[WB] &= ~SQUARE_BBS[targetSquare]
			PieceArray[captureIndex] |= SQUARE_BBS[targetSquare]

		case TAG_WCaptureQueenPromotion: //WQPrCAP
			PieceArray[WP] |= SQUARE_BBS[startingSquare]
			PieceArray[WQ] &= ~SQUARE_BBS[targetSquare]
			PieceArray[captureIndex] |= SQUARE_BBS[targetSquare]

		case TAG_WCaptureRookPromotion: //WRPrCAP
			PieceArray[WP] |= SQUARE_BBS[startingSquare]
			PieceArray[WR] &= ~SQUARE_BBS[targetSquare]
			PieceArray[captureIndex] |= SQUARE_BBS[targetSquare]

		case TAG_DoublePawnWhite: //WDouble
			PieceArray[WP] |= SQUARE_BBS[startingSquare]
			PieceArray[WP] &= ~SQUARE_BBS[targetSquare]

		case TAG_DoublePawnBlack: //BDouble
			PieceArray[BP] |= SQUARE_BBS[startingSquare]
			PieceArray[BP] &= ~SQUARE_BBS[targetSquare]

		}

		CastleRights[0] = copyCastle[0]
		CastleRights[1] = copyCastle[1]
		CastleRights[2] = copyCastle[2]
		CastleRights[3] = copyCastle[3]
		ep = copyEp

		//if ply == 0 {
			//PrintMoveNoNL(moveList[moveIndex][MOVE_STARTING], moveList[moveIndex][MOVE_TARGET], moveList[moveIndex][MOVE_TAG])
			//fmt.printf(": %d\n", nodes-priorNodes)
		//}
	}

	return nodes
	}
}

RunPerftInline :: proc(depth: int) {


	start_time: = time.now();

	nodes := PerftInline(depth, 0)

	end_time: = time.now();
	elapsed_time:= end_time._nsec - start_time._nsec; 


	fmt.printf("Nodes: %d\n", nodes)
	fmt.printf("Raw elapsed duration: %d ms\n", elapsed_time);
}
