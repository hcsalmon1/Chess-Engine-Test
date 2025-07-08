#![allow(non_snake_case)]
#![allow(unused)]

use std::time::Instant;
mod constants;

const NO_SQUARE: usize = 64;
struct Engine {
    PIECE_ARRAY: [u64; 12],   //= [0; 12];
    WHITE_TO_PLAY: bool,      // = true;
    CASTLE_RIGHTS: [bool; 4], // = [true, true, true, true];
    EP: usize,                //= NO_SQUARE;

    STARTING_SQUARES: [[usize; 50]; 6], // = [[0; 50]; 6];
    TARGET_SQUARES: [[usize; 50]; 6],   //= [[0; 50]; 6];
    TAGS: [[usize; 50]; 6],             //= [[0; 50]; 6];
    PIECES: [[usize; 50]; 6],           //= [[0; 50]; 6];
    PIN_ARRAY_SQUARES: [usize; 8],      //= [NO_SQUARE; 8];
    PIN_ARRAY_PIECES: [usize; 8],       //= [NO_SQUARE; 8];
}

impl Engine {
    fn new() -> Self {
        Engine {
            PIECE_ARRAY: [0; 12],
            WHITE_TO_PLAY: true,
            CASTLE_RIGHTS: [true, true, true, true],
            EP: NO_SQUARE,
            STARTING_SQUARES: [[0; 50]; 6],
            TARGET_SQUARES: [[0; 50]; 6],
            TAGS: [[0; 50]; 6],
            PIECES: [[0; 50]; 6],
            PIN_ARRAY_SQUARES: [NO_SQUARE; 8],
            PIN_ARRAY_PIECES: [NO_SQUARE; 8],
        }
    }
}
mod consts {
    pub const MAGIC: u64 = 0x03f79d71b4cb0a89;

    pub const PINNED_SQUARE_INDEX: usize = 0;
    pub const PINNING_PIECE_INDEX: usize = 1;

    pub const TAG_NONE: usize = 0;
    pub const TAG_CAPTURE: usize = 1;
    pub const TAG_WHITEEP: usize = 2;
    pub const TAG_BLACKEP: usize = 3;
    pub const TAG_WCASTLEKS: usize = 4;
    pub const TAG_WCASTLEQS: usize = 5;
    pub const TAG_BCASTLEKS: usize = 6;
    pub const TAG_BCASTLEQS: usize = 7;
    pub const TAG_B_KNIGHT_PROMOTION: usize = 8;
    pub const TAG_B_BISHOP_PROMOTION: usize = 9;
    pub const TAG_B_QUEEN_PROMOTION: usize = 10;
    pub const TAG_B_ROOK_PROMOTION: usize = 11;
    pub const TAG_W_KNIGHT_PROMOTION: usize = 12;
    pub const TAG_W_BISHOP_PROMOTION: usize = 13;
    pub const TAG_W_QUEEN_PROMOTION: usize = 14;
    pub const TAG_W_ROOK_PROMOTION: usize = 15;
    pub const TAG_B_CAPTURE_KNIGHT_PROMOTION: usize = 16;
    pub const TAG_B_CAPTURE_BISHOP_PROMOTION: usize = 17;
    pub const TAG_B_CAPTURE_QUEEN_PROMOTION: usize = 18;
    pub const TAG_B_CAPTURE_ROOK_PROMOTION: usize = 19;
    pub const TAG_W_CAPTURE_KNIGHT_PROMOTION: usize = 20;
    pub const TAG_W_CAPTURE_BISHOP_PROMOTION: usize = 21;
    pub const TAG_W_CAPTURE_QUEEN_PROMOTION: usize = 22;
    pub const TAG_W_CAPTURE_ROOK_PROMOTION: usize = 23;
    pub const TAG_DOUBLE_PAWN_WHITE: usize = 24;
    pub const TAG_DOUBLE_PAWN_BLACK: usize = 25;

    pub const MOVE_STARTING: usize = 0;
    pub const MOVE_TARGET: usize = 1;
    pub const MOVE_PIECE: usize = 2;
    pub const MOVE_TAG: usize = 3;
    pub const MAX_ULONG: u64 = 18446744073709551615;

    pub const RANK_1_BITBOARD: u64 = 18374686479671623680;
    pub const RANK_2_BITBOARD: u64 = 71776119061217280;
    pub const RANK_3_BITBOARD: u64 = 280375465082880;
    pub const RANK_4_BITBOARD: u64 = 1095216660480;
    pub const RANK_5_BITBOARD: u64 = 4278190080;
    pub const RANK_6_BITBOARD: u64 = 16711680;
    pub const RANK_7_BITBOARD: u64 = 65280;
    pub const RANK_8_BITBOARD: u64 = 255;

    pub const WKS_CASTLE_RIGHTS: usize = 0;
    pub const WQS_CASTLE_RIGHTS: usize = 1;
    pub const BKS_CASTLE_RIGHTS: usize = 2;
    pub const BQS_CASTLE_RIGHTS: usize = 3;

    pub const WKS_EMPTY_BITBOARD: u64 = 6917529027641081856;
    pub const WQS_EMPTY_BITBOARD: u64 = 1008806316530991104;
    pub const BKS_EMPTY_BITBOARD: u64 = 96;
    pub const BQS_EMPTY_BITBOARD: u64 = 14;

    pub const EMPTY_BITBOARD: u64 = 0;

    pub const A8: usize = 0;
    pub const B8: usize = 1;
    pub const C8: usize = 2;
    pub const D8: usize = 3;
    pub const E8: usize = 4;
    pub const F8: usize = 5;
    pub const G8: usize = 6;
    pub const H8: usize = 7;
    pub const A7: usize = 8;
    pub const B7: usize = 9;
    pub const C7: usize = 10;
    pub const D7: usize = 11;
    pub const E7: usize = 12;
    pub const F7: usize = 13;
    pub const G7: usize = 14;
    pub const H7: usize = 15;
    pub const A6: usize = 16;
    pub const B6: usize = 17;
    pub const C6: usize = 18;
    pub const D6: usize = 19;
    pub const E6: usize = 20;
    pub const F6: usize = 21;
    pub const G6: usize = 22;
    pub const H6: usize = 23;
    pub const A5: usize = 24;
    pub const B5: usize = 25;
    pub const C5: usize = 26;
    pub const D5: usize = 27;
    pub const E5: usize = 28;
    pub const F5: usize = 29;
    pub const G5: usize = 30;
    pub const H5: usize = 31;
    pub const A4: usize = 32;
    pub const B4: usize = 33;
    pub const C4: usize = 34;
    pub const D4: usize = 35;
    pub const E4: usize = 36;
    pub const F4: usize = 37;
    pub const G4: usize = 38;
    pub const H4: usize = 39;
    pub const A3: usize = 40;
    pub const B3: usize = 41;
    pub const C3: usize = 42;
    pub const D3: usize = 43;
    pub const E3: usize = 44;
    pub const F3: usize = 45;
    pub const G3: usize = 46;
    pub const H3: usize = 47;
    pub const A2: usize = 48;
    pub const B2: usize = 49;
    pub const C2: usize = 50;
    pub const D2: usize = 51;
    pub const E2: usize = 52;
    pub const F2: usize = 53;
    pub const G2: usize = 54;
    pub const H2: usize = 55;
    pub const A1: usize = 56;
    pub const B1: usize = 57;
    pub const C1: usize = 58;
    pub const D1: usize = 59;
    pub const E1: usize = 60;
    pub const F1: usize = 61;
    pub const G1: usize = 62;
    pub const H1: usize = 63;

    pub const SQ_CHAR_Y: [char; 65] = [
        '8', '8', '8', '8', '8', '8', '8', '8', '7', '7', '7', '7', '7', '7', '7', '7', '6', '6',
        '6', '6', '6', '6', '6', '6', '5', '5', '5', '5', '5', '5', '5', '5', '4', '4', '4', '4',
        '4', '4', '4', '4', '3', '3', '3', '3', '3', '3', '3', '3', '2', '2', '2', '2', '2', '2',
        '2', '2', '1', '1', '1', '1', '1', '1', '1', '1', 'A',
    ];

    pub const SQ_CHAR_X: [char; 65] = [
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'a', 'b',
        'c', 'd', 'e', 'f', 'g', 'h', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'a', 'b', 'c', 'd',
        'e', 'f', 'g', 'h', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'N',
    ];

    pub const WP: usize = 0;
    pub const WN: usize = 1;
    pub const WB: usize = 2;
    pub const WR: usize = 3;
    pub const WQ: usize = 4;
    pub const WK: usize = 5;
    pub const BP: usize = 6;
    pub const BN: usize = 7;
    pub const BB: usize = 8;
    pub const BR: usize = 9;
    pub const BQ: usize = 10;
    pub const BK: usize = 11;
}
use consts::*;
impl Engine {
    fn get_rook_attacks_fast( starting_square: usize, mut occupancy: u64) -> u64 {
        occupancy &= constants::ROOK_MASKS[starting_square];
        // Note: use of saturating_mul here since I think it is intended to ignore overflows
        occupancy = occupancy.saturating_mul(constants::ROOK_MAGIC_NUMBERS[starting_square]);
        occupancy >>= 64 - constants::ROOK_REL_BITS[starting_square];
        let converted_occupancy: usize = occupancy as usize;
        constants::ROOK_ATTACKS[starting_square][converted_occupancy]
    }

    fn get_bishop_attacks_fast(starting_square: usize, mut occupancy: u64) -> u64 {
        occupancy &= constants::BISHOP_MASKS[starting_square];
        // Note: use of saturating_mul here since I think it is intended to ignore overflows
        occupancy = occupancy.saturating_mul(constants::BISHOP_MAGIC_NUMBERS[starting_square]);
        occupancy >>= 64 - constants::BISHOP_REL_BITS[starting_square];
        constants::BISHOP_ATTACKS[starting_square][occupancy as usize]
    }

    fn get_rook_attacks_fast_old(starting_square: usize, mut occupancy: u64) -> u64 {
        occupancy &= constants::ROOK_MASKS[starting_square];
        occupancy *= constants::ROOK_MAGIC_NUMBERS[starting_square];
        occupancy >>= 64 - constants::ROOK_REL_BITS[starting_square];
        let converted_occupancy: usize = occupancy as usize;
        return constants::ROOK_ATTACKS[starting_square][converted_occupancy];
    }

    fn get_bishop_attacks_fast_old(starting_square: usize, occupancy: u64) -> u64 {
        let mut mutable_occupancy = occupancy;

        mutable_occupancy &= constants::BISHOP_MASKS[starting_square];
        mutable_occupancy *= constants::BISHOP_MAGIC_NUMBERS[starting_square];
        mutable_occupancy >>= 64 - constants::BISHOP_REL_BITS[starting_square];
        return constants::BISHOP_ATTACKS[starting_square][mutable_occupancy as usize];
    }

    fn bitscan_forward_separate( bitboard: u64) -> usize {
        let bitboard_combined: u64 = bitboard ^ (bitboard - 1);
        let calculation: u128 = 0x03f79d71b4cb0a89 * bitboard_combined as u128;
        let calc_truncated: u64 = calculation as u64;
        let index: usize = (calc_truncated >> 58) as usize;
        constants::DEBRUIJN64[index]
    }

    fn Is_Square_Attacked_By_Black_Global(&self, square: usize, occupancy: u64) -> bool {
        if (self.PIECE_ARRAY[BP] & constants::WHITE_PAWN_ATTACKS[square]) != 0 {
            return true;
        }
        if (self.PIECE_ARRAY[BN] & constants::KNIGHT_ATTACKS[square]) != 0 {
            return true;
        }
        if (self.PIECE_ARRAY[BK] & constants::KING_ATTACKS[square]) != 0 {
            return true;
        }
        let bishopAttacks = Self::get_bishop_attacks_fast_old(square, occupancy);
        if (self.PIECE_ARRAY[BB] & bishopAttacks) != 0 {
            return true;
        }
        if (self.PIECE_ARRAY[BQ] & bishopAttacks) != 0 {
            return true;
        }
        let rookAttacks = Self::get_rook_attacks_fast_old(square, occupancy);
        if (self.PIECE_ARRAY[BR] & rookAttacks) != 0 {
            return true;
        }
        if (self.PIECE_ARRAY[BQ] & rookAttacks) != 0 {
            return true;
        }
        false
    }

    fn Is_Square_Attacked_By_White_Global(& self, square: usize, occupancy: u64) -> bool {
        if (self.PIECE_ARRAY[WP] & constants::BLACK_PAWN_ATTACKS[square]) != 0 {
            return true;
        }
        if (self.PIECE_ARRAY[WN] & constants::KNIGHT_ATTACKS[square]) != 0 {
            return true;
        }
        if (self.PIECE_ARRAY[WK] & constants::KING_ATTACKS[square]) != 0 {
            return true;
        }
        let bishopAttacks = Self::get_bishop_attacks_fast_old(square, occupancy);
        if (self.PIECE_ARRAY[WB] & bishopAttacks) != 0 {
            return true;
        }
        if (self.PIECE_ARRAY[WQ] & bishopAttacks) != 0 {
            return true;
        }
        let rookAttacks = Self::get_rook_attacks_fast_old(square, occupancy);
        if (self.PIECE_ARRAY[WR] & rookAttacks) != 0 {
            return true;
        }
        if (self.PIECE_ARRAY[WQ] & rookAttacks) != 0 {
            return true;
        }
        false
    }

    fn OutOfBounds( input: usize) -> bool {
        if input > 63 {
            return true;
        }
        false
    }

    fn print_move_no_nl(starting_square: usize, target_square: usize) {
        //starting
        if Self::OutOfBounds(starting_square) {
            print!("{}", starting_square);
        } else {
            print!(
                "{}{}",
                SQ_CHAR_X[starting_square], SQ_CHAR_Y[starting_square]
            );
        }
        if Self::OutOfBounds(target_square) {
            print!("{}", target_square);
        } else {
            print!("{}{}", SQ_CHAR_X[target_square], SQ_CHAR_Y[target_square]);
        }
    }

    fn print_u64(bitboard: u64) {
        for rank in 0..8 {
            for file in 0..8 {
                let square = rank * 8 + file;
                Engine::process_square(bitboard, square);
            }
            println!();
        }
        println!("u64: {}", bitboard);
    }

    fn process_square(bitboard: u64, square: u32) {
        if (bitboard & (1u64 << square)) != 0 {
            print!("X ");
        } else {
            print!("_ ");
        }
    }

    fn set_starting_position(&mut self) {
        self.EP = 65;
        self.WHITE_TO_PLAY = true;
        self.CASTLE_RIGHTS[0] = true;
        self.CASTLE_RIGHTS[1] = true;
        self.CASTLE_RIGHTS[2] = true;
        self.CASTLE_RIGHTS[3] = true;

        self.PIECE_ARRAY[0] = 71776119061217280;
        self.PIECE_ARRAY[1] = 4755801206503243776;
        self.PIECE_ARRAY[2] = 2594073385365405696;
        self.PIECE_ARRAY[3] = 9295429630892703744;
        self.PIECE_ARRAY[4] = 576460752303423488;
        self.PIECE_ARRAY[5] = 1152921504606846976;
        self.PIECE_ARRAY[6] = 65280;
        self.PIECE_ARRAY[7] = 66;
        self.PIECE_ARRAY[8] = 36;
        self.PIECE_ARRAY[9] = 129;
        self.PIECE_ARRAY[10] = 8;
        self.PIECE_ARRAY[11] = 16;
    }

    fn is_occupied( bitboard: u64, square: usize) -> bool {
        (bitboard & constants::SQUARE_BBS[square]) != 0
    }

    fn get_occupied_index(&self, square: usize) -> usize {
        for i in 0..12 {
            if Self::is_occupied(self.PIECE_ARRAY[i], square) {
                return i;
            }
        }
        12
    }

    fn fill_board_array(&self) -> [usize; 64] {
        let mut board_array: [usize; 64] = [0; 64];

        for i in 0..64 {
            board_array[i] = self.get_occupied_index(i);
        }
        board_array
    }

    fn print_board(& self) {
        
        const PIECE_NAMES: [u8; 13] = [
            b'P', b'N', b'B', b'R', b'Q', b'K', b'P', b'N', b'B', b'R', b'Q', b'K', b'_',
        ];
        const PIECE_COLOURS: [u8; 13] = [
            b'W', b'W', b'W', b'W', b'W', b'W', b'B', b'B', b'B', b'B', b'B', b'B', b'_',
        ];
	
        println!("Board:");
        let board_array = self.fill_board_array();

        for rank in 0..8 {
            print!("   ");

            for file in 0..8 {
                let square: usize = (rank * 8) + file;
                print!(
                    "{}{} ",
                    PIECE_COLOURS[board_array[square]] as char,
                    PIECE_NAMES[board_array[square]] as char
                );
            }

            println!();
        }
        println!();

        println!("White to play: {}", self.WHITE_TO_PLAY);

        println!(
            "Castle: {} {} {} {}",
            self.CASTLE_RIGHTS[0],
            self.CASTLE_RIGHTS[1],
            self.CASTLE_RIGHTS[2],
            self.CASTLE_RIGHTS[3]
        );
        println!("ep: {}\n", self.EP);
        println!();
        println!();
    }

    fn perft_inline(&mut self, depth: i8, ply: usize) -> usize {
        //if (depth == 0) {
            //return 1;
        //}

        let mut move_count: usize = 0;

        //Move generating variables
        let white_occupancies: u64 = self.PIECE_ARRAY[0]
            | self.PIECE_ARRAY[1]
            | self.PIECE_ARRAY[2]
            | self.PIECE_ARRAY[3]
            | self.PIECE_ARRAY[4]
            | self.PIECE_ARRAY[5];
        let black_occupancies: u64 = self.PIECE_ARRAY[6]
            | self.PIECE_ARRAY[7]
            | self.PIECE_ARRAY[8]
            | self.PIECE_ARRAY[9]
            | self.PIECE_ARRAY[10]
            | self.PIECE_ARRAY[11];
        let combined_occupancies: u64 = white_occupancies | black_occupancies;
        let EMPTY_OCCUPANCIES: u64 = !combined_occupancies;
        let mut temp_bitboard: u64;
        let mut check_bitboard: u64 = 0;
        let mut temp_pin_bitboard: u64;
        let mut temp_attack: u64;
        let mut temp_empty: u64;
        let mut temp_captures: u64;
        let mut starting_square: usize = NO_SQUARE;
        let mut target_square: usize = NO_SQUARE;

        let mut pin_number: usize = 0;

        if self.WHITE_TO_PLAY {
            let mut white_king_check_count: usize = 0;
            let white_king_position: usize = Self::bitscan_forward_separate(self.PIECE_ARRAY[WK]);

            //pawns
            temp_bitboard =
                self.PIECE_ARRAY[BP] & constants::WHITE_PAWN_ATTACKS[white_king_position];
            if temp_bitboard != 0 {
                let pawn_square: usize = Self::bitscan_forward_separate(temp_bitboard);
                check_bitboard = constants::SQUARE_BBS[pawn_square];

                white_king_check_count += 1;
            }

            //knights
            temp_bitboard = self.PIECE_ARRAY[BN] & constants::KNIGHT_ATTACKS[white_king_position];
            if temp_bitboard != 0 {
                let knight_square: usize = Self::bitscan_forward_separate(temp_bitboard);

                check_bitboard = constants::SQUARE_BBS[knight_square];

                white_king_check_count += 1;
            }

            //bishops
            let bishop_attacks_checks: u64 =
                Self::get_bishop_attacks_fast_old(white_king_position, black_occupancies);
            temp_bitboard = self.PIECE_ARRAY[BB] & bishop_attacks_checks;
            while temp_bitboard != 0 {
                let piece_square: usize = Self::bitscan_forward_separate(temp_bitboard);
                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS[white_king_position]
                    [piece_square]
                    & white_occupancies;

                if temp_pin_bitboard == 0 {
                    check_bitboard =
                        constants::INBETWEEN_BITBOARDS[white_king_position][piece_square];
                    white_king_check_count += 1;
                } else {
                    let pinned_square: usize = Self::bitscan_forward_separate(temp_pin_bitboard);
                    temp_pin_bitboard &= temp_pin_bitboard - 1;

                    if temp_pin_bitboard == 0 {
                        self.PIN_ARRAY_SQUARES[pin_number] = pinned_square;
                        self.PIN_ARRAY_PIECES[pin_number] = piece_square;
                        pin_number += 1;
                    }
                }
                temp_bitboard &= temp_bitboard - 1;
            }

            //queen
            temp_bitboard = self.PIECE_ARRAY[BQ] & bishop_attacks_checks;
            while temp_bitboard != 0 {
                let piece_square: usize = Self::bitscan_forward_separate(temp_bitboard);

                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS[white_king_position]
                    [piece_square]
                    & white_occupancies;

                if temp_pin_bitboard == 0 {
                    check_bitboard =
                        constants::INBETWEEN_BITBOARDS[white_king_position][piece_square];
                    white_king_check_count += 1;
                } else {
                    let pinned_square: usize = Self::bitscan_forward_separate(temp_pin_bitboard);
                    temp_pin_bitboard &= temp_pin_bitboard - 1;

                    if temp_pin_bitboard == 0 {
                        self.PIN_ARRAY_SQUARES[pin_number] = pinned_square;
                        self.PIN_ARRAY_PIECES[pin_number] = piece_square;
                        pin_number += 1;
                    }
                }
                temp_bitboard &= temp_bitboard - 1;
            }

            //rook
            let rook_attacks: u64 =
                Self::get_rook_attacks_fast_old(white_king_position, black_occupancies);
            temp_bitboard = self.PIECE_ARRAY[BR] & rook_attacks;
            while temp_bitboard != 0 {
                let piece_square: usize = Self::bitscan_forward_separate(temp_bitboard);
                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS[white_king_position]
                    [piece_square]
                    & white_occupancies;

                if temp_pin_bitboard == 0 {
                    check_bitboard =
                        constants::INBETWEEN_BITBOARDS[white_king_position][piece_square];
                    white_king_check_count += 1;
                } else {
                    let pinned_square: usize = Self::bitscan_forward_separate(temp_pin_bitboard);
                    temp_pin_bitboard &= temp_pin_bitboard - 1;

                    if temp_pin_bitboard == 0 {
                        self.PIN_ARRAY_SQUARES[pin_number] = pinned_square;
                        self.PIN_ARRAY_PIECES[pin_number] = piece_square;
                        pin_number += 1;
                    }
                }
                temp_bitboard &= temp_bitboard - 1;
            }

            //queen
            temp_bitboard = self.PIECE_ARRAY[BQ] & rook_attacks;
            while temp_bitboard != 0 {
                let piece_square: usize = Self::bitscan_forward_separate(temp_bitboard);
                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS[white_king_position]
                    [piece_square]
                    & white_occupancies;

                if temp_pin_bitboard == 0 {
                    check_bitboard =
                        constants::INBETWEEN_BITBOARDS[white_king_position][piece_square];
                    white_king_check_count += 1;
                } else {
                    let pinned_square: usize = Self::bitscan_forward_separate(temp_pin_bitboard);
                    temp_pin_bitboard &= temp_pin_bitboard - 1;

                    if temp_pin_bitboard == 0 {
                        self.PIN_ARRAY_SQUARES[pin_number] = pinned_square;
                        self.PIN_ARRAY_PIECES[pin_number] = piece_square;
                        pin_number += 1;
                    }
                }
                temp_bitboard &= temp_bitboard - 1;
            }

            if white_king_check_count > 1 {
                let occupancies_without_white_king: u64 =
                    combined_occupancies & (!self.PIECE_ARRAY[WK]);
                temp_attack = constants::KING_ATTACKS[white_king_position];
                temp_empty = temp_attack & EMPTY_OCCUPANCIES;
                while temp_empty != 0 {
                    target_square = Self::bitscan_forward_separate(temp_empty);
                    temp_empty &= temp_empty - 1;

                    if (self.PIECE_ARRAY[BP] & constants::WHITE_PAWN_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[BN] & constants::KNIGHT_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[BK] & constants::KING_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    let bishop_attacks: u64 =
                        Self::get_bishop_attacks_fast_old(target_square, occupancies_without_white_king);
                    if (self.PIECE_ARRAY[BB] & bishop_attacks) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[BQ] & bishop_attacks) != 0 {
                        continue;
                    }
                    let rook_attacks: u64 =
                        Self::get_rook_attacks_fast_old(target_square, occupancies_without_white_king);
                    if (self.PIECE_ARRAY[BR] & rook_attacks) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[BQ] & rook_attacks) != 0 {
                        continue;
                    }

                    self.STARTING_SQUARES[ply][move_count] = white_king_position;
                    self.TARGET_SQUARES[ply][move_count] = target_square;
                    self.TAGS[ply][move_count] = TAG_NONE;
                    self.PIECES[ply][move_count] = WK;
                    move_count += 1;
                }

                //captures
                temp_captures = temp_attack & black_occupancies;
                while temp_captures != 0 {
                    target_square = Self::bitscan_forward_separate(temp_captures);
                    temp_captures &= temp_captures - 1;

                    if (self.PIECE_ARRAY[BP] & constants::WHITE_PAWN_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[BN] & constants::KNIGHT_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[BK] & constants::KING_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    let bishop_attacks: u64 =
                        Self::get_bishop_attacks_fast_old(target_square, occupancies_without_white_king);
                    if (self.PIECE_ARRAY[BB] & bishop_attacks) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[BQ] & bishop_attacks) != 0 {
                        continue;
                    }
                    let rook_attacks: u64 =
                        Self::get_rook_attacks_fast_old(target_square, occupancies_without_white_king);
                    if (self.PIECE_ARRAY[BR] & rook_attacks) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[BQ] & rook_attacks) != 0 {
                        continue;
                    }

                    self.STARTING_SQUARES[ply][move_count] = white_king_position;
                    self.TARGET_SQUARES[ply][move_count] = target_square;
                    self.TAGS[ply][move_count] = TAG_CAPTURE;
                    self.PIECES[ply][move_count] = WK;
                    move_count += 1;
                }
            } else {
                if white_king_check_count == 0 {
                    check_bitboard = MAX_ULONG;
                }

                let occupancies_without_white_king: u64 =
                    combined_occupancies & (!self.PIECE_ARRAY[WK]);
                temp_attack = constants::KING_ATTACKS[white_king_position];
                temp_empty = temp_attack & EMPTY_OCCUPANCIES;
                while temp_empty != 0 {
                    target_square = Self::bitscan_forward_separate(temp_empty);
                    temp_empty &= temp_empty - 1;

                    if (self.PIECE_ARRAY[BP] & constants::WHITE_PAWN_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[BN] & constants::KNIGHT_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[BK] & constants::KING_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    let bishop_attacks: u64 =
                        Self::get_bishop_attacks_fast_old(target_square, occupancies_without_white_king);
                    if (self.PIECE_ARRAY[BB] & bishop_attacks) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[BQ] & bishop_attacks) != 0 {
                        continue;
                    }
                    let rook_attacks: u64 =
                        Self::get_rook_attacks_fast_old(target_square, occupancies_without_white_king);
                    if (self.PIECE_ARRAY[BR] & rook_attacks) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[BQ] & rook_attacks) != 0 {
                        continue;
                    }

                    self.STARTING_SQUARES[ply][move_count] = white_king_position;
                    self.TARGET_SQUARES[ply][move_count] = target_square;
                    self.TAGS[ply][move_count] = TAG_NONE;
                    self.PIECES[ply][move_count] = WK;
                    move_count += 1;
                }

                //captures
                temp_captures = temp_attack & black_occupancies;
                while temp_captures != 0 {
                    target_square = Self::bitscan_forward_separate(temp_captures);
                    temp_captures &= temp_captures - 1;

                    if (self.PIECE_ARRAY[BP] & constants::WHITE_PAWN_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[BN] & constants::KNIGHT_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[BK] & constants::KING_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    let bishop_attacks: u64 =
                        Self::get_bishop_attacks_fast_old(target_square, occupancies_without_white_king);
                    if (self.PIECE_ARRAY[BB] & bishop_attacks) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[BQ] & bishop_attacks) != 0 {
                        continue;
                    }
                    let rook_attacks: u64 =
                        Self::get_rook_attacks_fast_old(target_square, occupancies_without_white_king);
                    if (self.PIECE_ARRAY[BR] & rook_attacks) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[BQ] & rook_attacks) != 0 {
                        continue;
                    }

                    self.STARTING_SQUARES[ply][move_count] = white_king_position;
                    self.TARGET_SQUARES[ply][move_count] = target_square;
                    self.TAGS[ply][move_count] = TAG_CAPTURE;
                    self.PIECES[ply][move_count] = WK;
                    move_count += 1;
                }

                if white_king_check_count == 0 {
                    if self.CASTLE_RIGHTS[WKS_CASTLE_RIGHTS]
                        && white_king_position == E1
                        && (WKS_EMPTY_BITBOARD & combined_occupancies) == 0
                        && (self.PIECE_ARRAY[WR] & constants::SQUARE_BBS[H1]) != 0
                        && !self.Is_Square_Attacked_By_Black_Global(F1, combined_occupancies)
                        && !self.Is_Square_Attacked_By_Black_Global(G1, combined_occupancies)
                    {
                        self.STARTING_SQUARES[ply][move_count] = E1;
                        self.TARGET_SQUARES[ply][move_count] = G1;
                        self.TAGS[ply][move_count] = TAG_WCASTLEKS;
                        self.PIECES[ply][move_count] = WK;
                        move_count += 1;
                    }
                    if self.CASTLE_RIGHTS[WQS_CASTLE_RIGHTS]
                        && white_king_position == E1
                        && (WQS_EMPTY_BITBOARD & combined_occupancies) == 0
                        && (self.PIECE_ARRAY[WR] & constants::SQUARE_BBS[A1]) != 0
                        && !self.Is_Square_Attacked_By_Black_Global(C1, combined_occupancies)
                        && !self.Is_Square_Attacked_By_Black_Global(D1, combined_occupancies)
                    {
                        self.STARTING_SQUARES[ply][move_count] = E1;
                        self.TARGET_SQUARES[ply][move_count] = C1;
                        self.TAGS[ply][move_count] = TAG_WCASTLEQS;
                        self.PIECES[ply][move_count] = WK;
                        move_count += 1;
                    }
                }

                temp_bitboard = self.PIECE_ARRAY[WN];

                while temp_bitboard != 0 {
                    starting_square = Self::bitscan_forward_separate(temp_bitboard);
                    temp_bitboard &= temp_bitboard - 1; //removes the knight from that square to not infinitely loop

                    temp_pin_bitboard = MAX_ULONG;
                    if pin_number != 0 {
                        for i in 0..pin_number {
                            if self.PIN_ARRAY_SQUARES[i] == starting_square {
                                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS
                                    [white_king_position][self.PIN_ARRAY_PIECES[i]];
                            }
                        }
                    }

                    temp_attack = ((constants::KNIGHT_ATTACKS[starting_square]
                        & black_occupancies)
                        & check_bitboard)
                        & temp_pin_bitboard; //gets knight captures
                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack);
                        temp_attack &= temp_attack - 1;

                        self.STARTING_SQUARES[ply][move_count] = starting_square;
                        self.TARGET_SQUARES[ply][move_count] = target_square;
                        self.TAGS[ply][move_count] = TAG_CAPTURE;
                        self.PIECES[ply][move_count] = WN;
                        move_count += 1;
                    }

                    temp_attack = ((constants::KNIGHT_ATTACKS[starting_square]
                        & EMPTY_OCCUPANCIES)
                        & check_bitboard)
                        & temp_pin_bitboard;

                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack);
                        temp_attack &= temp_attack - 1;

                        self.STARTING_SQUARES[ply][move_count] = starting_square;
                        self.TARGET_SQUARES[ply][move_count] = target_square;
                        self.TAGS[ply][move_count] = TAG_NONE;
                        self.PIECES[ply][move_count] = WN;
                        move_count += 1;
                    }
                }

                temp_bitboard = self.PIECE_ARRAY[WP];

                while temp_bitboard != 0 {
                    starting_square = Self::bitscan_forward_separate(temp_bitboard);
                    temp_bitboard &= temp_bitboard - 1;

                    temp_pin_bitboard = MAX_ULONG;
                    if pin_number != 0 {
                        for i in 0..pin_number {
                            if self.PIN_ARRAY_SQUARES[i] == starting_square {
                                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS
                                    [white_king_position][self.PIN_ARRAY_PIECES[i]];
                            }
                        }
                    }

                    if (constants::SQUARE_BBS[starting_square - 8] & combined_occupancies) == 0 {
                        //if up one square is empty
                        if ((constants::SQUARE_BBS[starting_square - 8] & check_bitboard)
                            & temp_pin_bitboard)
                            != 0
                        {
                            if (constants::SQUARE_BBS[starting_square] & RANK_7_BITBOARD) != 0 {
                                //if promotion
                                self.STARTING_SQUARES[ply][move_count] = starting_square;
                                self.TARGET_SQUARES[ply][move_count] = starting_square - 8;
                                self.TAGS[ply][move_count] = TAG_W_QUEEN_PROMOTION;
                                self.PIECES[ply][move_count] = WP;
                                move_count += 1;

                                self.STARTING_SQUARES[ply][move_count] = starting_square;
                                self.TARGET_SQUARES[ply][move_count] = starting_square - 8;
                                self.TAGS[ply][move_count] = TAG_W_ROOK_PROMOTION;
                                self.PIECES[ply][move_count] = WP;
                                move_count += 1;

                                self.STARTING_SQUARES[ply][move_count] = starting_square;
                                self.TARGET_SQUARES[ply][move_count] = starting_square - 8;
                                self.TAGS[ply][move_count] = TAG_W_BISHOP_PROMOTION;
                                self.PIECES[ply][move_count] = WP;
                                move_count += 1;

                                self.STARTING_SQUARES[ply][move_count] = starting_square;
                                self.TARGET_SQUARES[ply][move_count] = starting_square - 8;
                                self.TAGS[ply][move_count] = TAG_W_KNIGHT_PROMOTION;
                                self.PIECES[ply][move_count] = WP;
                                move_count += 1;
                            } else {
                                self.STARTING_SQUARES[ply][move_count] = starting_square;
                                self.TARGET_SQUARES[ply][move_count] = starting_square - 8;
                                self.TAGS[ply][move_count] = TAG_NONE;
                                self.PIECES[ply][move_count] = WP;
                                move_count += 1;
                            }
                        }

                        if (constants::SQUARE_BBS[starting_square] & RANK_2_BITBOARD) != 0 {
                            //if on rank 2
                            if ((constants::SQUARE_BBS[starting_square - 16] & check_bitboard)
                                & temp_pin_bitboard)
                                != 0
                            {
                                //if not pinned or
                                if ((constants::SQUARE_BBS[starting_square - 16])
                                    & combined_occupancies)
                                    == 0
                                {
                                    //if up two squares and one square are empty
                                    self.STARTING_SQUARES[ply][move_count] = starting_square;
                                    self.TARGET_SQUARES[ply][move_count] = starting_square - 16;
                                    self.TAGS[ply][move_count] = TAG_DOUBLE_PAWN_WHITE;
                                    self.PIECES[ply][move_count] = WP;
                                    move_count += 1;
                                }
                            }
                        }
                    }

                    temp_attack = ((constants::WHITE_PAWN_ATTACKS[starting_square]
                        & black_occupancies)
                        & check_bitboard)
                        & temp_pin_bitboard; //if black piece diagonal to pawn

                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack);
                        temp_attack &= temp_attack - 1;

                        if (constants::SQUARE_BBS[starting_square] & RANK_7_BITBOARD) != 0
                        //if promotion
                        {
                            self.STARTING_SQUARES[ply][move_count] = starting_square;
                            self.TARGET_SQUARES[ply][move_count] = target_square;
                            self.TAGS[ply][move_count] = TAG_W_CAPTURE_BISHOP_PROMOTION;
                            self.PIECES[ply][move_count] = WP;
                            move_count += 1;

                            self.STARTING_SQUARES[ply][move_count] = starting_square;
                            self.TARGET_SQUARES[ply][move_count] = target_square;
                            self.TAGS[ply][move_count] = TAG_W_CAPTURE_KNIGHT_PROMOTION;
                            self.PIECES[ply][move_count] = WP;
                            move_count += 1;

                            self.STARTING_SQUARES[ply][move_count] = starting_square;
                            self.TARGET_SQUARES[ply][move_count] = target_square;
                            self.TAGS[ply][move_count] = TAG_W_CAPTURE_QUEEN_PROMOTION;
                            self.PIECES[ply][move_count] = WP;
                            move_count += 1;

                            self.STARTING_SQUARES[ply][move_count] = starting_square;
                            self.TARGET_SQUARES[ply][move_count] = target_square;
                            self.TAGS[ply][move_count] = TAG_W_CAPTURE_ROOK_PROMOTION;
                            self.PIECES[ply][move_count] = WP;
                            move_count += 1;
                        } else {
                            self.STARTING_SQUARES[ply][move_count] = starting_square;
                            self.TARGET_SQUARES[ply][move_count] = target_square;
                            self.TAGS[ply][move_count] = TAG_CAPTURE;
                            self.PIECES[ply][move_count] = WP;
                            move_count += 1;
                        }
                    }

                    if (constants::SQUARE_BBS[starting_square] & RANK_5_BITBOARD) != 0
                        && self.EP != NO_SQUARE
                        && (((constants::WHITE_PAWN_ATTACKS[starting_square]
                            & constants::SQUARE_BBS[self.EP])
                            & check_bitboard)
                            & temp_pin_bitboard)
                            != 0
                    {
                        if (self.PIECE_ARRAY[WK] & RANK_5_BITBOARD) == 0
                        //if no king on rank 5
                        {
                            self.STARTING_SQUARES[ply][move_count] = starting_square;
                            self.TARGET_SQUARES[ply][move_count] = self.EP;
                            self.TAGS[ply][move_count] = TAG_WHITEEP;
                            self.PIECES[ply][move_count] = WP;
                            move_count += 1;
                        } else if (self.PIECE_ARRAY[BR] & RANK_5_BITBOARD) == 0
                            && (self.PIECE_ARRAY[BQ] & RANK_5_BITBOARD) == 0
                        // if no b rook or queen on rank 5
                        {
                            self.STARTING_SQUARES[ply][move_count] = starting_square;
                            self.TARGET_SQUARES[ply][move_count] = self.EP;
                            self.TAGS[ply][move_count] = TAG_WHITEEP;
                            self.PIECES[ply][move_count] = WP;
                            move_count += 1;
                        } else
                        //wk and br or bq on rank 5
                        {
                            let mut occupancy_without_ep_pawns: u64 =
                                combined_occupancies & (!constants::SQUARE_BBS[starting_square]);
                            occupancy_without_ep_pawns &= !constants::SQUARE_BBS[self.EP + 8];

                            let rook_attacks_from_king: u64 = Self::get_rook_attacks_fast_old(
                                white_king_position,
                                occupancy_without_ep_pawns,
                            );
                            if (rook_attacks_from_king & self.PIECE_ARRAY[BR]) == 0
                                && (rook_attacks_from_king & self.PIECE_ARRAY[BQ]) == 0
                            {
                                self.STARTING_SQUARES[ply][move_count] = starting_square;
                                self.TARGET_SQUARES[ply][move_count] = self.EP;
                                self.TAGS[ply][move_count] = TAG_WHITEEP;
                                self.PIECES[ply][move_count] = WP;
                                move_count += 1;
                            }
                        }
                    }
                }

                temp_bitboard = self.PIECE_ARRAY[WR];
                while temp_bitboard != 0 {
                    starting_square = Self::bitscan_forward_separate(temp_bitboard);
                    temp_bitboard &= temp_bitboard - 1;

                    temp_pin_bitboard = MAX_ULONG;
                    if pin_number != 0 {
                        for i in 0..pin_number {
                            if self.PIN_ARRAY_SQUARES[i] == starting_square {
                                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS
                                    [white_king_position][self.PIN_ARRAY_PIECES[i]];
                            }
                        }
                    }

                    let rook_attacks =
                        Self::get_rook_attacks_fast_old(starting_square, combined_occupancies);

                    temp_attack =
                        ((rook_attacks & black_occupancies) & check_bitboard) & temp_pin_bitboard;
                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack);
                        temp_attack &= temp_attack - 1;

                        self.STARTING_SQUARES[ply][move_count] = starting_square;
                        self.TARGET_SQUARES[ply][move_count] = target_square;
                        self.TAGS[ply][move_count] = TAG_CAPTURE;
                        self.PIECES[ply][move_count] = WR;
                        move_count += 1;
                    }

                    temp_attack =
                        ((rook_attacks & EMPTY_OCCUPANCIES) & check_bitboard) & temp_pin_bitboard;
                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack);
                        temp_attack &= temp_attack - 1;

                        self.STARTING_SQUARES[ply][move_count] = starting_square;
                        self.TARGET_SQUARES[ply][move_count] = target_square;
                        self.TAGS[ply][move_count] = TAG_NONE;
                        self.PIECES[ply][move_count] = WR;
                        move_count += 1;
                    }
                }

                temp_bitboard = self.PIECE_ARRAY[WB];
                while temp_bitboard != 0 {
                    starting_square = Self::bitscan_forward_separate(temp_bitboard);
                    temp_bitboard &= temp_bitboard - 1;

                    temp_pin_bitboard = MAX_ULONG;
                    if pin_number != 0 {
                        for i in 0..pin_number {
                            if self.PIN_ARRAY_SQUARES[i] == starting_square {
                                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS
                                    [white_king_position][self.PIN_ARRAY_PIECES[i]];
                            }
                        }
                    }

                    let bishop_attacks: u64 =
                        Self::get_bishop_attacks_fast_old(starting_square, combined_occupancies);

                    temp_attack =
                        ((bishop_attacks & black_occupancies) & check_bitboard) & temp_pin_bitboard;
                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack);
                        temp_attack &= temp_attack - 1;

                        self.STARTING_SQUARES[ply][move_count] = starting_square;
                        self.TARGET_SQUARES[ply][move_count] = target_square;
                        self.TAGS[ply][move_count] = TAG_CAPTURE;
                        self.PIECES[ply][move_count] = WB;
                        move_count += 1;
                    }

                    temp_attack =
                        ((bishop_attacks & EMPTY_OCCUPANCIES) & check_bitboard) & temp_pin_bitboard;
                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack);
                        temp_attack &= temp_attack - 1;

                        self.STARTING_SQUARES[ply][move_count] = starting_square;
                        self.TARGET_SQUARES[ply][move_count] = target_square;
                        self.TAGS[ply][move_count] = TAG_NONE;
                        self.PIECES[ply][move_count] = WB;
                        move_count += 1;
                    }
                }

                temp_bitboard = self.PIECE_ARRAY[WQ];
                while temp_bitboard != 0 {
                    starting_square = Self::bitscan_forward_separate(temp_bitboard);
                    temp_bitboard &= temp_bitboard - 1;

                    temp_pin_bitboard = MAX_ULONG;
                    if pin_number != 0 {
                        for i in 0..pin_number {
                            if self.PIN_ARRAY_SQUARES[i] == starting_square {
                                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS
                                    [white_king_position][self.PIN_ARRAY_PIECES[i]];
                            }
                        }
                    }

                    let mut queen_attacks =
                        Self::get_rook_attacks_fast_old(starting_square, combined_occupancies);
                    queen_attacks |=
                        Self::get_bishop_attacks_fast_old(starting_square, combined_occupancies);

                    temp_attack =
                        ((queen_attacks & black_occupancies) & check_bitboard) & temp_pin_bitboard;

                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack);
                        temp_attack &= temp_attack - 1;

                        self.STARTING_SQUARES[ply][move_count] = starting_square;
                        self.TARGET_SQUARES[ply][move_count] = target_square;
                        self.TAGS[ply][move_count] = TAG_CAPTURE;
                        self.PIECES[ply][move_count] = WQ;
                        move_count += 1;
                    }

                    temp_attack =
                        ((queen_attacks & EMPTY_OCCUPANCIES) & check_bitboard) & temp_pin_bitboard;
                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack);
                        temp_attack &= temp_attack - 1;

                        self.STARTING_SQUARES[ply][move_count] = starting_square;
                        self.TARGET_SQUARES[ply][move_count] = target_square;
                        self.TAGS[ply][move_count] = TAG_NONE;
                        self.PIECES[ply][move_count] = WQ;
                        move_count += 1;
                    }
                }
            }
        } else {
            //black move
            let mut black_king_check_count: u8 = 0;
            let black_king_position: usize = Self::bitscan_forward_separate(self.PIECE_ARRAY[BK]);
            //pawns
            temp_bitboard =
                self.PIECE_ARRAY[WP] & constants::BLACK_PAWN_ATTACKS[black_king_position];
            if temp_bitboard != 0 {
                let pawn_square = Self::bitscan_forward_separate(temp_bitboard);
                if check_bitboard == 0 {
                    check_bitboard = constants::SQUARE_BBS[pawn_square];
                }

                black_king_check_count += 1;
            }

            //knights
            temp_bitboard = self.PIECE_ARRAY[WN] & constants::KNIGHT_ATTACKS[black_king_position];
            if temp_bitboard != 0 {
                let knight_square: usize = Self::bitscan_forward_separate(temp_bitboard);

                check_bitboard = constants::SQUARE_BBS[knight_square];

                black_king_check_count += 1;
            }

            //bishops
            let bishop_attacks_checks: u64 =
                Self::get_bishop_attacks_fast_old(black_king_position, white_occupancies);
            temp_bitboard = self.PIECE_ARRAY[WB] & bishop_attacks_checks;
            while temp_bitboard != 0 {
                let piece_square: usize = Self::bitscan_forward_separate(temp_bitboard);
                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS[black_king_position]
                    [piece_square]
                    & black_occupancies;

                if temp_pin_bitboard == 0 {
                    if check_bitboard == 0 {
                        check_bitboard =
                            constants::INBETWEEN_BITBOARDS[black_king_position][piece_square];
                    }
                    black_king_check_count += 1;
                } else {
                    let pinned_square: usize = Self::bitscan_forward_separate(temp_pin_bitboard);
                    temp_pin_bitboard &= temp_pin_bitboard - 1;

                    if temp_pin_bitboard == 0 {
                        self.PIN_ARRAY_SQUARES[pin_number] = pinned_square;
                        self.PIN_ARRAY_PIECES[pin_number] = piece_square;
                        pin_number += 1;
                    }
                }
                temp_bitboard &= temp_bitboard - 1;
            }

            //queen
            temp_bitboard = self.PIECE_ARRAY[WQ] & bishop_attacks_checks;
            while temp_bitboard != 0 {
                let piece_square: usize = Self::bitscan_forward_separate(temp_bitboard);
                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS[black_king_position]
                    [piece_square]
                    & black_occupancies;

                if temp_pin_bitboard == 0 {
                    if check_bitboard == 0 {
                        check_bitboard =
                            constants::INBETWEEN_BITBOARDS[black_king_position][piece_square];
                    }
                    black_king_check_count += 1;
                } else {
                    let pinned_square: usize = Self::bitscan_forward_separate(temp_pin_bitboard);
                    temp_pin_bitboard &= temp_pin_bitboard - 1;

                    if temp_pin_bitboard == 0 {
                        self.PIN_ARRAY_SQUARES[pin_number] = pinned_square;
                        self.PIN_ARRAY_PIECES[pin_number] = piece_square;
                        pin_number += 1;
                    }
                }
                temp_bitboard &= temp_bitboard - 1;
            }

            //rook
            let rook_attacks = Self::get_rook_attacks_fast_old(black_king_position, white_occupancies);
            temp_bitboard = self.PIECE_ARRAY[WR] & rook_attacks;
            while temp_bitboard != 0 {
                let piece_square: usize = Self::bitscan_forward_separate(temp_bitboard);
                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS[black_king_position]
                    [piece_square]
                    & black_occupancies;

                if temp_pin_bitboard == 0 {
                    if check_bitboard == 0 {
                        check_bitboard =
                            constants::INBETWEEN_BITBOARDS[black_king_position][piece_square];
                    }
                    black_king_check_count += 1;
                } else {
                    let pinned_square: usize = Self::bitscan_forward_separate(temp_pin_bitboard);
                    temp_pin_bitboard &= temp_pin_bitboard - 1;

                    if temp_pin_bitboard == 0 {
                        self.PIN_ARRAY_SQUARES[pin_number] = pinned_square;
                        self.PIN_ARRAY_PIECES[pin_number] = piece_square;
                        pin_number += 1;
                    }
                }
                temp_bitboard &= temp_bitboard - 1;
            }

            //queen
            temp_bitboard = self.PIECE_ARRAY[WQ] & rook_attacks;
            while temp_bitboard != 0 {
                let piece_square: usize = Self::bitscan_forward_separate(temp_bitboard);

                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS[black_king_position]
                    [piece_square]
                    & black_occupancies;

                if temp_pin_bitboard == 0 {
                    if check_bitboard == 0 {
                        check_bitboard =
                            constants::INBETWEEN_BITBOARDS[black_king_position][piece_square];
                    }
                    black_king_check_count += 1;
                } else {
                    let pinned_square: usize = Self::bitscan_forward_separate(temp_pin_bitboard);
                    temp_pin_bitboard &= temp_pin_bitboard - 1;

                    if temp_pin_bitboard == 0 {
                        self.PIN_ARRAY_SQUARES[pin_number] = pinned_square;
                        self.PIN_ARRAY_PIECES[pin_number] = piece_square;
                        pin_number += 1;
                    }
                }
                temp_bitboard &= temp_bitboard - 1;
            }

            if black_king_check_count > 1 {
                let occupancy_without_black_king = combined_occupancies & (!self.PIECE_ARRAY[BK]);
                temp_attack = constants::KING_ATTACKS[black_king_position] & white_occupancies;

                temp_attack = constants::KING_ATTACKS[black_king_position] & white_occupancies;

                while temp_attack != 0 {
                    target_square = Self::bitscan_forward_separate(temp_attack);
                    temp_attack &= temp_attack - 1;

                    if (self.PIECE_ARRAY[WP] & constants::BLACK_PAWN_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[WN] & constants::KNIGHT_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[WK] & constants::KING_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    let bishop_attacks =
                        Self::get_bishop_attacks_fast_old(target_square, occupancy_without_black_king);
                    if (self.PIECE_ARRAY[WB] & bishop_attacks) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[WQ] & bishop_attacks) != 0 {
                        continue;
                    }
                    let rook_attacks =
                        Self::get_rook_attacks_fast_old(target_square, occupancy_without_black_king);
                    if (self.PIECE_ARRAY[WR] & rook_attacks) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[WQ] & rook_attacks) != 0 {
                        continue;
                    }

                    self.STARTING_SQUARES[ply][move_count] = starting_square;
                    self.TARGET_SQUARES[ply][move_count] = target_square;
                    self.TAGS[ply][move_count] = TAG_CAPTURE;
                    self.PIECES[ply][move_count] = BK;
                    move_count += 1;
                }

                temp_attack = constants::KING_ATTACKS[black_king_position] & !combined_occupancies;

                while temp_attack != 0 {
                    target_square = Self::bitscan_forward_separate(temp_attack);
                    temp_attack &= temp_attack - 1;

                    if (self.PIECE_ARRAY[WP] & constants::WHITE_PAWN_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[WN] & constants::KNIGHT_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[WK] & constants::KING_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    let bishop_attacks =
                        Self::get_bishop_attacks_fast_old(target_square, occupancy_without_black_king);
                    if (self.PIECE_ARRAY[WB] & bishop_attacks) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[WQ] & bishop_attacks) != 0 {
                        continue;
                    }
                    let rook_attacks =
                        Self::get_rook_attacks_fast_old(target_square, occupancy_without_black_king);
                    if (self.PIECE_ARRAY[WR] & rook_attacks) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[WQ] & rook_attacks) != 0 {
                        continue;
                    }

                    self.STARTING_SQUARES[ply][move_count] = starting_square;
                    self.TARGET_SQUARES[ply][move_count] = target_square;
                    self.TAGS[ply][move_count] = TAG_NONE;
                    self.PIECES[ply][move_count] = BK;
                    move_count += 1;
                }
            } else {
                if black_king_check_count == 0 {
                    check_bitboard = MAX_ULONG;
                }

                temp_bitboard = self.PIECE_ARRAY[BP];

                while temp_bitboard != 0 {
                    starting_square = Self::bitscan_forward_separate(temp_bitboard);
                    temp_bitboard &= temp_bitboard - 1;

                    temp_pin_bitboard = MAX_ULONG;
                    if pin_number != 0 {
                        for i in 0..pin_number {
                            if self.PIN_ARRAY_SQUARES[i] == starting_square {
                                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS
                                    [black_king_position][self.PIN_ARRAY_PIECES[i]];
                            }
                        }
                    }

                    if (constants::SQUARE_BBS[starting_square + 8] & combined_occupancies) == 0
                    //if up one square is empty
                    {
                        if ((constants::SQUARE_BBS[starting_square + 8] & check_bitboard)
                            & temp_pin_bitboard)
                            != 0
                        {
                            if (constants::SQUARE_BBS[starting_square] & RANK_2_BITBOARD) != 0
                            //if promotion
                            {
                                self.STARTING_SQUARES[ply][move_count] = starting_square;
                                self.TARGET_SQUARES[ply][move_count] = starting_square + 8;
                                self.TAGS[ply][move_count] = TAG_B_BISHOP_PROMOTION;
                                self.PIECES[ply][move_count] = BP;
                                move_count += 1;

                                self.STARTING_SQUARES[ply][move_count] = starting_square;
                                self.TARGET_SQUARES[ply][move_count] = starting_square + 8;
                                self.TAGS[ply][move_count] = TAG_B_KNIGHT_PROMOTION;
                                self.PIECES[ply][move_count] = BP;
                                move_count += 1;

                                self.STARTING_SQUARES[ply][move_count] = starting_square;
                                self.TARGET_SQUARES[ply][move_count] = starting_square + 8;
                                self.TAGS[ply][move_count] = TAG_B_ROOK_PROMOTION;
                                self.PIECES[ply][move_count] = BP;
                                move_count += 1;

                                self.STARTING_SQUARES[ply][move_count] = starting_square;
                                self.TARGET_SQUARES[ply][move_count] = starting_square + 8;
                                self.TAGS[ply][move_count] = TAG_B_QUEEN_PROMOTION;
                                self.PIECES[ply][move_count] = BP;
                                move_count += 1;
                            } else {
                                self.STARTING_SQUARES[ply][move_count] = starting_square;
                                self.TARGET_SQUARES[ply][move_count] = starting_square + 8;
                                self.TAGS[ply][move_count] = TAG_NONE;
                                self.PIECES[ply][move_count] = BP;
                                move_count += 1;
                            }
                        }

                        if (constants::SQUARE_BBS[starting_square] & RANK_7_BITBOARD) != 0
                            && ((constants::SQUARE_BBS[starting_square + 16] & check_bitboard)
                                & temp_pin_bitboard)
                                != 0
                            && ((constants::SQUARE_BBS[starting_square + 16])
                                & combined_occupancies)
                                == 0
                        {
                            self.STARTING_SQUARES[ply][move_count] = starting_square;
                            self.TARGET_SQUARES[ply][move_count] = starting_square + 16;
                            self.TAGS[ply][move_count] = TAG_DOUBLE_PAWN_BLACK;
                            self.PIECES[ply][move_count] = BP;
                            move_count += 1;
                        }
                    }

                    temp_attack = ((constants::BLACK_PAWN_ATTACKS[starting_square]
                        & white_occupancies)
                        & check_bitboard)
                        & temp_pin_bitboard; //if black piece diagonal to pawn

                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack); //find the bit
                        temp_attack &= temp_attack - 1;

                        if (constants::SQUARE_BBS[starting_square] & RANK_2_BITBOARD) != 0
                        //if promotion
                        {
                            self.STARTING_SQUARES[ply][move_count] = starting_square;
                            self.TARGET_SQUARES[ply][move_count] = target_square;
                            self.TAGS[ply][move_count] = TAG_B_CAPTURE_BISHOP_PROMOTION;
                            self.PIECES[ply][move_count] = BP;
                            move_count += 1;

                            self.STARTING_SQUARES[ply][move_count] = starting_square;
                            self.TARGET_SQUARES[ply][move_count] = target_square;
                            self.TAGS[ply][move_count] = TAG_B_CAPTURE_ROOK_PROMOTION;
                            self.PIECES[ply][move_count] = BP;
                            move_count += 1;

                            self.STARTING_SQUARES[ply][move_count] = starting_square;
                            self.TARGET_SQUARES[ply][move_count] = target_square;
                            self.TAGS[ply][move_count] = TAG_B_CAPTURE_QUEEN_PROMOTION;
                            self.PIECES[ply][move_count] = BP;
                            move_count += 1;

                            self.STARTING_SQUARES[ply][move_count] = starting_square;
                            self.TARGET_SQUARES[ply][move_count] = target_square;
                            self.TAGS[ply][move_count] = TAG_B_CAPTURE_KNIGHT_PROMOTION;
                            self.PIECES[ply][move_count] = BP;
                            move_count += 1;
                        } else {
                            self.STARTING_SQUARES[ply][move_count] = starting_square;
                            self.TARGET_SQUARES[ply][move_count] = target_square;
                            self.TAGS[ply][move_count] = TAG_CAPTURE;
                            self.PIECES[ply][move_count] = BP;
                            move_count += 1;
                        }
                    }

                    if (constants::SQUARE_BBS[starting_square] & RANK_4_BITBOARD) != 0
                        && self.EP != NO_SQUARE
                        && (((constants::BLACK_PAWN_ATTACKS[starting_square]
                            & constants::SQUARE_BBS[self.EP])
                            & check_bitboard)
                            & temp_pin_bitboard)
                            != 0
                    {
                        if (self.PIECE_ARRAY[BK] & RANK_4_BITBOARD) == 0
                        //if no king on rank 5
                        {
                            self.STARTING_SQUARES[ply][move_count] = starting_square;
                            self.TARGET_SQUARES[ply][move_count] = self.EP;
                            self.TAGS[ply][move_count] = TAG_BLACKEP;
                            self.PIECES[ply][move_count] = BP;
                            move_count += 1;
                        } else if (self.PIECE_ARRAY[WR] & RANK_4_BITBOARD) == 0
                            && (self.PIECE_ARRAY[WQ] & RANK_4_BITBOARD) == 0
                        // if no b rook or queen on rank 5
                        {
                            self.STARTING_SQUARES[ply][move_count] = starting_square;
                            self.TARGET_SQUARES[ply][move_count] = self.EP;
                            self.TAGS[ply][move_count] = TAG_BLACKEP;
                            self.PIECES[ply][move_count] = BP;
                            move_count += 1;
                        } else
                        //wk and br or bq on rank 5
                        {
                            let mut occupancy_without_ep_pawns: u64 =
                                combined_occupancies & !constants::SQUARE_BBS[starting_square];
                            occupancy_without_ep_pawns &= !constants::SQUARE_BBS[self.EP - 8];

                            let rook_attacks_from_king = Self::get_rook_attacks_fast_old(
                                black_king_position,
                                occupancy_without_ep_pawns,
                            );
                            if (rook_attacks_from_king & self.PIECE_ARRAY[WR]) == 0
                                && (rook_attacks_from_king & self.PIECE_ARRAY[WQ]) == 0
                            {
                                self.STARTING_SQUARES[ply][move_count] = starting_square;
                                self.TARGET_SQUARES[ply][move_count] = self.EP;
                                self.TAGS[ply][move_count] = TAG_BLACKEP;
                                self.PIECES[ply][move_count] = BP;
                                move_count += 1;
                            }
                        }
                    }
                }

                temp_bitboard = self.PIECE_ARRAY[BN];

                while temp_bitboard != 0 {
                    starting_square = Self::bitscan_forward_separate(temp_bitboard); //looks for the startingSquare
                    temp_bitboard &= temp_bitboard - 1; //removes the knight from that square to not infinitely loop

                    temp_pin_bitboard = MAX_ULONG;
                    if pin_number != 0 {
                        for i in 0..pin_number {
                            if self.PIN_ARRAY_SQUARES[i] == starting_square {
                                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS
                                    [black_king_position][self.PIN_ARRAY_PIECES[i]];
                            }
                        }
                    }

                    temp_attack = ((constants::KNIGHT_ATTACKS[starting_square]
                        & white_occupancies)
                        & check_bitboard)
                        & temp_pin_bitboard; //gets knight captures
                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack);
                        temp_attack &= temp_attack - 1;

                        self.STARTING_SQUARES[ply][move_count] = starting_square;
                        self.TARGET_SQUARES[ply][move_count] = target_square;
                        self.TAGS[ply][move_count] = TAG_CAPTURE;
                        self.PIECES[ply][move_count] = BN;
                        move_count += 1;
                    }

                    temp_attack = ((constants::KNIGHT_ATTACKS[starting_square]
                        & (!combined_occupancies))
                        & check_bitboard)
                        & temp_pin_bitboard;

                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack);
                        temp_attack &= temp_attack - 1;

                        self.STARTING_SQUARES[ply][move_count] = starting_square;
                        self.TARGET_SQUARES[ply][move_count] = target_square;
                        self.TAGS[ply][move_count] = TAG_NONE;
                        self.PIECES[ply][move_count] = BN;
                        move_count += 1;
                    }
                }

                temp_bitboard = self.PIECE_ARRAY[BB];
                while temp_bitboard != 0 {
                    starting_square = Self::bitscan_forward_separate(temp_bitboard);
                    temp_bitboard &= temp_bitboard - 1;

                    temp_pin_bitboard = MAX_ULONG;
                    if pin_number != 0 {
                        for i in 0..pin_number {
                            if self.PIN_ARRAY_SQUARES[i] == starting_square {
                                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS
                                    [black_king_position][self.PIN_ARRAY_PIECES[i]];
                            }
                        }
                    }

                    let bishop_attacks =
                        Self::get_bishop_attacks_fast_old(starting_square, combined_occupancies);

                    temp_attack =
                        ((bishop_attacks & white_occupancies) & check_bitboard) & temp_pin_bitboard;
                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack);
                        temp_attack &= temp_attack - 1;

                        self.STARTING_SQUARES[ply][move_count] = starting_square;
                        self.TARGET_SQUARES[ply][move_count] = target_square;
                        self.TAGS[ply][move_count] = TAG_CAPTURE;
                        self.PIECES[ply][move_count] = BB;
                        move_count += 1;
                    }

                    temp_attack = ((bishop_attacks & (!combined_occupancies)) & check_bitboard)
                        & temp_pin_bitboard;
                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack);
                        temp_attack &= temp_attack - 1;

                        self.STARTING_SQUARES[ply][move_count] = starting_square;
                        self.TARGET_SQUARES[ply][move_count] = target_square;
                        self.TAGS[ply][move_count] = TAG_NONE;
                        self.PIECES[ply][move_count] = BB;
                        move_count += 1;
                    }
                }

                temp_bitboard = self.PIECE_ARRAY[BR];
                while temp_bitboard != 0 {
                    starting_square = Self::bitscan_forward_separate(temp_bitboard);
                    temp_bitboard &= temp_bitboard - 1;

                    temp_pin_bitboard = MAX_ULONG;
                    if pin_number != 0 {
                        for i in 0..pin_number {
                            if self.PIN_ARRAY_SQUARES[i] == starting_square {
                                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS
                                    [black_king_position][self.PIN_ARRAY_PIECES[i]];
                            }
                        }
                    }

                    let rook_attacks =
                        Self::get_rook_attacks_fast_old(starting_square, combined_occupancies);

                    temp_attack =
                        ((rook_attacks & white_occupancies) & check_bitboard) & temp_pin_bitboard;
                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack);
                        temp_attack &= temp_attack - 1;

                        self.STARTING_SQUARES[ply][move_count] = starting_square;
                        self.TARGET_SQUARES[ply][move_count] = target_square;
                        self.TAGS[ply][move_count] = TAG_CAPTURE;
                        self.PIECES[ply][move_count] = BR;
                        move_count += 1;
                    }

                    temp_attack = ((rook_attacks & (!combined_occupancies)) & check_bitboard)
                        & temp_pin_bitboard;
                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack);
                        temp_attack &= temp_attack - 1;

                        self.STARTING_SQUARES[ply][move_count] = starting_square;
                        self.TARGET_SQUARES[ply][move_count] = target_square;
                        self.TAGS[ply][move_count] = TAG_NONE;
                        self.PIECES[ply][move_count] = BR;
                        move_count += 1;
                    }
                }

                temp_bitboard = self.PIECE_ARRAY[BQ];
                while temp_bitboard != 0 {
                    starting_square = Self::bitscan_forward_separate(temp_bitboard);
                    temp_bitboard &= temp_bitboard - 1;

                    temp_pin_bitboard = MAX_ULONG;
                    if pin_number != 0 {
                        for i in 0..pin_number {
                            if self.PIN_ARRAY_SQUARES[i] == starting_square {
                                temp_pin_bitboard = constants::INBETWEEN_BITBOARDS
                                    [black_king_position][self.PIN_ARRAY_PIECES[i]];
                            }
                        }
                    }

                    let mut queen_attacks =
                        Self::get_rook_attacks_fast_old(starting_square, combined_occupancies);
                    queen_attacks |=
                        Self::get_bishop_attacks_fast_old(starting_square, combined_occupancies);

                    temp_attack =
                        ((queen_attacks & white_occupancies) & check_bitboard) & temp_pin_bitboard;

                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack);
                        temp_attack &= temp_attack - 1;

                        self.STARTING_SQUARES[ply][move_count] = starting_square;
                        self.TARGET_SQUARES[ply][move_count] = target_square;
                        self.TAGS[ply][move_count] = TAG_CAPTURE;
                        self.PIECES[ply][move_count] = BQ;
                        move_count += 1;
                    }

                    temp_attack = ((queen_attacks & (!combined_occupancies)) & check_bitboard)
                        & temp_pin_bitboard;
                    while temp_attack != 0 {
                        target_square = Self::bitscan_forward_separate(temp_attack);
                        temp_attack &= temp_attack - 1;

                        self.STARTING_SQUARES[ply][move_count] = starting_square;
                        self.TARGET_SQUARES[ply][move_count] = target_square;
                        self.TAGS[ply][move_count] = TAG_NONE;
                        self.PIECES[ply][move_count] = BQ;
                        move_count += 1;
                    }
                }

                temp_attack = constants::KING_ATTACKS[black_king_position] & white_occupancies; //gets knight captures
                while temp_attack != 0 {
                    target_square = Self::bitscan_forward_separate(temp_attack);
                    temp_attack &= temp_attack - 1;

                    if (self.PIECE_ARRAY[WP] & constants::BLACK_PAWN_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[WN] & constants::KNIGHT_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[WK] & constants::KING_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    let occupancy_without_black_king =
                        combined_occupancies & (!self.PIECE_ARRAY[BK]);
                    let bishop_attacks =
                        Self::get_bishop_attacks_fast_old(target_square, occupancy_without_black_king);
                    if (self.PIECE_ARRAY[WB] & bishop_attacks) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[WQ] & bishop_attacks) != 0 {
                        continue;
                    }
                    let rook_attacks =
                        Self::get_rook_attacks_fast_old(target_square, occupancy_without_black_king);
                    if (self.PIECE_ARRAY[WR] & rook_attacks) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[WQ] & rook_attacks) != 0 {
                        continue;
                    }

                    self.STARTING_SQUARES[ply][move_count] = black_king_position;
                    self.TARGET_SQUARES[ply][move_count] = target_square;
                    self.TAGS[ply][move_count] = TAG_CAPTURE;
                    self.PIECES[ply][move_count] = BK;
                    move_count += 1;
                }

                temp_attack = constants::KING_ATTACKS[black_king_position] & EMPTY_OCCUPANCIES; //get knight moves to emtpy squares

                while temp_attack != 0 {
                    target_square = Self::bitscan_forward_separate(temp_attack);
                    temp_attack &= temp_attack - 1;

                    if (self.PIECE_ARRAY[WP] & constants::BLACK_PAWN_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[WN] & constants::KNIGHT_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[WK] & constants::KING_ATTACKS[target_square]) != 0 {
                        continue;
                    }
                    let occupancy_without_black_king =
                        combined_occupancies & (!self.PIECE_ARRAY[BK]);
                    let bishop_attacks =
                        Self::get_bishop_attacks_fast_old(target_square, occupancy_without_black_king);
                    if (self.PIECE_ARRAY[WB] & bishop_attacks) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[WQ] & bishop_attacks) != 0 {
                        continue;
                    }
                    let rook_attacks =
                        Self::get_rook_attacks_fast_old(target_square, occupancy_without_black_king);
                    if (self.PIECE_ARRAY[WR] & rook_attacks) != 0 {
                        continue;
                    }
                    if (self.PIECE_ARRAY[WQ] & rook_attacks) != 0 {
                        continue;
                    }

                    self.STARTING_SQUARES[ply][move_count] = black_king_position;
                    self.TARGET_SQUARES[ply][move_count] = target_square;
                    self.TAGS[ply][move_count] = TAG_NONE;
                    self.PIECES[ply][move_count] = BK;
                    move_count += 1;
                }
            }
            if black_king_check_count == 0 {
                if self.CASTLE_RIGHTS[BKS_CASTLE_RIGHTS]
                    && black_king_position == E8
                    && (BKS_EMPTY_BITBOARD & combined_occupancies) == 0
                    && (self.PIECE_ARRAY[BR] & constants::SQUARE_BBS[H8]) != 0
                    && !self.Is_Square_Attacked_By_White_Global(F8, combined_occupancies)
                    && !self.Is_Square_Attacked_By_White_Global(G8, combined_occupancies)
                {
                    self.STARTING_SQUARES[ply][move_count] = E8;
                    self.TARGET_SQUARES[ply][move_count] = G8;
                    self.TAGS[ply][move_count] = TAG_BCASTLEKS;
                    self.PIECES[ply][move_count] = BK;
                    move_count += 1;
                }
                if self.CASTLE_RIGHTS[BQS_CASTLE_RIGHTS]
                    && black_king_position == E8
                    && (BQS_EMPTY_BITBOARD & combined_occupancies) == 0
                    && (self.PIECE_ARRAY[BR] & constants::SQUARE_BBS[A8]) != 0
                    && !self.Is_Square_Attacked_By_White_Global(C8, combined_occupancies)
                    && !self.Is_Square_Attacked_By_White_Global(D8, combined_occupancies)
                {
                    self.STARTING_SQUARES[ply][move_count] = E8;
                    self.TARGET_SQUARES[ply][move_count] = C8;
                    self.TAGS[ply][move_count] = TAG_BCASTLEQS;
                    self.PIECES[ply][move_count] = BK;
                    move_count += 1;
                }
            }
        }

        if depth == 1 {
            return move_count;
        }

        let mut nodes: usize = 0;
        //let mut prior_nodes: u64;
        let copy_ep: usize = self.EP;
        let mut copy_castle: [bool; 4] = [true, true, true, true];
        copy_castle[0] = self.CASTLE_RIGHTS[0];
        copy_castle[1] = self.CASTLE_RIGHTS[1];
        copy_castle[2] = self.CASTLE_RIGHTS[2];
        copy_castle[3] = self.CASTLE_RIGHTS[3];

        for move_index in 0..move_count {
            // println!("move_index: {}", move_index);

            let starting_square: usize = self.STARTING_SQUARES[ply][move_index];
            let target_square: usize = self.TARGET_SQUARES[ply][move_index];
            let piece: usize = self.PIECES[ply][move_index];
            let tag: usize = self.TAGS[ply][move_index];

            let mut capture_index: usize = 0;

            self.WHITE_TO_PLAY = !self.WHITE_TO_PLAY;

            match tag {
                TAG_NONE => {
                    //none
                    self.PIECE_ARRAY[piece] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = NO_SQUARE;
                }
                TAG_CAPTURE => {
                    //capture
                    self.PIECE_ARRAY[piece] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];
                    if piece <= WK {
                        for i in BP..BK + 1 {
                            if (self.PIECE_ARRAY[i] & constants::SQUARE_BBS[target_square]) != 0 {
                                capture_index = i;
                                break;
                            }
                        }
                        self.PIECE_ARRAY[capture_index] &= !constants::SQUARE_BBS[target_square]
                    } else {
                        //is black

                        for i in WP..BP {
                            if (self.PIECE_ARRAY[i] & constants::SQUARE_BBS[target_square]) != 0 {
                                capture_index = i;
                                break;
                            }
                        }
                        self.PIECE_ARRAY[capture_index] &= !constants::SQUARE_BBS[target_square];
                    }
                    self.EP = NO_SQUARE;
                }
                TAG_WHITEEP => {
                    //white ep
                    //move piece
                    self.PIECE_ARRAY[WP] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[WP] &= !constants::SQUARE_BBS[starting_square];
                    //remove
                    self.PIECE_ARRAY[BP] &= !constants::SQUARE_BBS[target_square + 8];
                    self.EP = NO_SQUARE;
                }
                TAG_BLACKEP => {
                    //black ep
                    //move piece
                    self.PIECE_ARRAY[BP] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[BP] &= !constants::SQUARE_BBS[starting_square];
                    //remove white pawn square up
                    self.PIECE_ARRAY[WP] &= !constants::SQUARE_BBS[target_square - 8];
                    self.EP = NO_SQUARE;
                }
                TAG_WCASTLEKS => {
                    //WKS
                    //white king
                    self.PIECE_ARRAY[WK] |= constants::SQUARE_BBS[G1];
                    self.PIECE_ARRAY[WK] &= !constants::SQUARE_BBS[E1];
                    //white rook
                    self.PIECE_ARRAY[WR] |= constants::SQUARE_BBS[F1];
                    self.PIECE_ARRAY[WR] &= !constants::SQUARE_BBS[H1];
                    //occupancies
                    self.CASTLE_RIGHTS[WKS_CASTLE_RIGHTS] = false;
                    self.CASTLE_RIGHTS[WQS_CASTLE_RIGHTS] = false;
                    self.EP = NO_SQUARE;
                }
                TAG_WCASTLEQS => {
                    //WQS
                    //white king
                    self.PIECE_ARRAY[WK] |= constants::SQUARE_BBS[C1];
                    self.PIECE_ARRAY[WK] &= !constants::SQUARE_BBS[E1];
                    //white rook
                    self.PIECE_ARRAY[WR] |= constants::SQUARE_BBS[D1];
                    self.PIECE_ARRAY[WR] &= !constants::SQUARE_BBS[A1];

                    self.CASTLE_RIGHTS[WKS_CASTLE_RIGHTS] = false;
                    self.CASTLE_RIGHTS[WQS_CASTLE_RIGHTS] = false;
                    self.EP = NO_SQUARE;
                }
                TAG_BCASTLEKS => {
                    //BKS
                    //white king
                    self.PIECE_ARRAY[BK] |= constants::SQUARE_BBS[G8];
                    self.PIECE_ARRAY[BK] &= !constants::SQUARE_BBS[E8];
                    //white rook
                    self.PIECE_ARRAY[BR] |= constants::SQUARE_BBS[F8];
                    self.PIECE_ARRAY[BR] &= !constants::SQUARE_BBS[H8];
                    self.CASTLE_RIGHTS[BKS_CASTLE_RIGHTS] = false;
                    self.CASTLE_RIGHTS[BQS_CASTLE_RIGHTS] = false;
                    self.EP = NO_SQUARE;
                }
                TAG_BCASTLEQS => {
                    //BQS
                    //white king
                    self.PIECE_ARRAY[BK] |= constants::SQUARE_BBS[C8];
                    self.PIECE_ARRAY[BK] &= !constants::SQUARE_BBS[E8];
                    //white rook
                    self.PIECE_ARRAY[BR] |= constants::SQUARE_BBS[D8];
                    self.PIECE_ARRAY[BR] &= !constants::SQUARE_BBS[A8];
                    self.CASTLE_RIGHTS[BKS_CASTLE_RIGHTS] = false;
                    self.CASTLE_RIGHTS[BQS_CASTLE_RIGHTS] = false;
                    self.EP = NO_SQUARE;
                }
                TAG_B_KNIGHT_PROMOTION => {
                    //BNPr
                    self.PIECE_ARRAY[BN] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = NO_SQUARE;
                }
                TAG_B_BISHOP_PROMOTION => {
                    //BBPr
                    self.PIECE_ARRAY[BB] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = NO_SQUARE;
                }
                TAG_B_QUEEN_PROMOTION => {
                    //BQPr
                    self.PIECE_ARRAY[BQ] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = NO_SQUARE;
                }
                TAG_B_ROOK_PROMOTION => {
                    //BRPr
                    self.PIECE_ARRAY[BR] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = NO_SQUARE;
                }
                TAG_W_KNIGHT_PROMOTION => {
                    //WNPr
                    self.PIECE_ARRAY[WN] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = NO_SQUARE;
                }
                TAG_W_BISHOP_PROMOTION => {
                    //WBPr
                    self.PIECE_ARRAY[WB] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = NO_SQUARE;
                }
                TAG_W_QUEEN_PROMOTION => {
                    //WQPr
                    self.PIECE_ARRAY[WQ] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = NO_SQUARE;
                }
                TAG_W_ROOK_PROMOTION => {
                    //WRPr
                    self.PIECE_ARRAY[WR] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = NO_SQUARE;
                }
                TAG_B_CAPTURE_KNIGHT_PROMOTION => {
                    //BNPrCAP
                    self.PIECE_ARRAY[BN] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = NO_SQUARE;
                    for i in WP..BP {
                        if (self.PIECE_ARRAY[i] & constants::SQUARE_BBS[target_square]) != 0 {
                            capture_index = i;
                            break;
                        }
                    }
                    self.PIECE_ARRAY[capture_index] &= !constants::SQUARE_BBS[target_square]
                }
                TAG_B_CAPTURE_BISHOP_PROMOTION => {
                    //BBPrCAP
                    self.PIECE_ARRAY[BB] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];

                    self.EP = NO_SQUARE;
                    for i in WP..BP {
                        if (self.PIECE_ARRAY[i] & constants::SQUARE_BBS[target_square]) != 0 {
                            capture_index = i;
                            break;
                        }
                    }
                    self.PIECE_ARRAY[capture_index] &= !constants::SQUARE_BBS[target_square];
                }
                TAG_B_CAPTURE_QUEEN_PROMOTION => {
                    //BQPrCAP
                    self.PIECE_ARRAY[BQ] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = NO_SQUARE;
                    for i in WP..BP {
                        if (self.PIECE_ARRAY[i] & constants::SQUARE_BBS[target_square]) != 0 {
                            capture_index = i;
                            break;
                        }
                    }
                    self.PIECE_ARRAY[capture_index] &= !constants::SQUARE_BBS[target_square];
                }
                TAG_B_CAPTURE_ROOK_PROMOTION => {
                    //BRPrCAP
                    self.PIECE_ARRAY[BR] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = NO_SQUARE;
                    for i in WP..BP {
                        if (self.PIECE_ARRAY[i] & constants::SQUARE_BBS[target_square]) != 0 {
                            capture_index = i;
                            break;
                        }
                    }
                    self.PIECE_ARRAY[capture_index] &= !constants::SQUARE_BBS[target_square];
                }
                TAG_W_CAPTURE_KNIGHT_PROMOTION => {
                    //WNPrCAP
                    self.PIECE_ARRAY[WN] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = NO_SQUARE;
                    for i in BP..BK + 1 {
                        if (self.PIECE_ARRAY[i] & constants::SQUARE_BBS[target_square]) != 0 {
                            capture_index = i;
                            break;
                        }
                    }
                    self.PIECE_ARRAY[capture_index] &= !constants::SQUARE_BBS[target_square]
                }
                TAG_W_CAPTURE_BISHOP_PROMOTION => {
                    //WBPrCAP
                    self.PIECE_ARRAY[WB] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = NO_SQUARE;
                    for i in BP..BK + 1 {
                        if (self.PIECE_ARRAY[i] & constants::SQUARE_BBS[target_square]) != 0 {
                            capture_index = i;
                            break;
                        }
                    }
                    self.PIECE_ARRAY[capture_index] &= !constants::SQUARE_BBS[target_square]
                }
                TAG_W_CAPTURE_QUEEN_PROMOTION => {
                    //WQPrCAP
                    self.PIECE_ARRAY[WQ] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = NO_SQUARE;
                    for i in BP..BK + 1 {
                        if (self.PIECE_ARRAY[i] & constants::SQUARE_BBS[target_square]) != 0 {
                            capture_index = i;
                            break;
                        }
                    }
                    self.PIECE_ARRAY[capture_index] &= !constants::SQUARE_BBS[target_square];
                }
                TAG_W_CAPTURE_ROOK_PROMOTION => {
                    //WRPrCAP
                    self.PIECE_ARRAY[WR] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = NO_SQUARE;
                    for i in BP..BK + 1 {
                        if (self.PIECE_ARRAY[i] & constants::SQUARE_BBS[target_square]) != 0 {
                            capture_index = i;
                            break;
                        }
                    }
                    self.PIECE_ARRAY[capture_index] &= !constants::SQUARE_BBS[target_square];
                }
                TAG_DOUBLE_PAWN_WHITE => {
                    //WDouble
                    self.PIECE_ARRAY[WP] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[WP] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = target_square + 8;
                }
                TAG_DOUBLE_PAWN_BLACK => {
                    //BDouble
                    self.PIECE_ARRAY[BP] |= constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[BP] &= !constants::SQUARE_BBS[starting_square];
                    self.EP = target_square - 8;
                }
                _ => {}
            }
            if piece == WK {
                self.CASTLE_RIGHTS[WKS_CASTLE_RIGHTS] = false;
                self.CASTLE_RIGHTS[WQS_CASTLE_RIGHTS] = false;
            } else if piece == BK {
                self.CASTLE_RIGHTS[BKS_CASTLE_RIGHTS] = false;
                self.CASTLE_RIGHTS[BQS_CASTLE_RIGHTS] = false;
            } else if piece == WR {
                if self.CASTLE_RIGHTS[WKS_CASTLE_RIGHTS]
                    && (self.PIECE_ARRAY[WR] & constants::SQUARE_BBS[H1]) == 0
                {
                    self.CASTLE_RIGHTS[WKS_CASTLE_RIGHTS] = false;
                }
                if self.CASTLE_RIGHTS[WQS_CASTLE_RIGHTS]
                    && (self.PIECE_ARRAY[WR] & constants::SQUARE_BBS[A1]) == 0
                {
                    self.CASTLE_RIGHTS[WQS_CASTLE_RIGHTS] = false;
                }
            } else if piece == BR {
                if self.CASTLE_RIGHTS[BKS_CASTLE_RIGHTS]
                    && (self.PIECE_ARRAY[BR] & constants::SQUARE_BBS[H8]) == 0
                {
                    self.CASTLE_RIGHTS[BKS_CASTLE_RIGHTS] = false;
                }
                if self.CASTLE_RIGHTS[BQS_CASTLE_RIGHTS]
                    && (self.PIECE_ARRAY[BR] & constants::SQUARE_BBS[A8]) == 0
                {
                    self.CASTLE_RIGHTS[BQS_CASTLE_RIGHTS] = false;
                }
            }

            // println!("calling perft_inline with depth: {:>3} and move_index: {:>3}", depth - 1, move_index);
            let prior_nodes:usize = nodes;
            nodes += self.perft_inline(depth - 1, ply + 1);

            self.WHITE_TO_PLAY = !self.WHITE_TO_PLAY;

            match tag {
                TAG_NONE => {
                    //none
                    self.PIECE_ARRAY[piece] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[target_square];
                }
                TAG_CAPTURE => {
                    //capture
                    self.PIECE_ARRAY[piece] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[piece] &= !constants::SQUARE_BBS[target_square];
                    if piece <= WK {
                        self.PIECE_ARRAY[capture_index] |= constants::SQUARE_BBS[target_square];
                    } else {
                        //is black

                        self.PIECE_ARRAY[capture_index] |= constants::SQUARE_BBS[target_square];
                    }
                }
                TAG_WHITEEP => {
                    //white ep
                    self.PIECE_ARRAY[WP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[WP] &= !constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[BP] |= constants::SQUARE_BBS[target_square + 8];
                }
                TAG_BLACKEP => {
                    //black ep
                    self.PIECE_ARRAY[BP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[BP] &= !constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[WP] |= constants::SQUARE_BBS[target_square - 8];
                }
                TAG_WCASTLEKS => {
                    //WKS
                    //white king
                    self.PIECE_ARRAY[WK] |= constants::SQUARE_BBS[E1];
                    self.PIECE_ARRAY[WK] &= !constants::SQUARE_BBS[G1];
                    //white rook
                    self.PIECE_ARRAY[WR] |= constants::SQUARE_BBS[H1];
                    self.PIECE_ARRAY[WR] &= !constants::SQUARE_BBS[F1];
                }
                TAG_WCASTLEQS => {
                    //WQS
                    //white king
                    self.PIECE_ARRAY[WK] |= constants::SQUARE_BBS[E1];
                    self.PIECE_ARRAY[WK] &= !constants::SQUARE_BBS[C1];
                    //white rook
                    self.PIECE_ARRAY[WR] |= constants::SQUARE_BBS[A1];
                    self.PIECE_ARRAY[WR] &= !constants::SQUARE_BBS[D1];
                }
                TAG_BCASTLEKS => {
                    //BKS
                    //white king
                    self.PIECE_ARRAY[BK] |= constants::SQUARE_BBS[E8];
                    self.PIECE_ARRAY[BK] &= !constants::SQUARE_BBS[G8];
                    //white rook
                    self.PIECE_ARRAY[BR] |= constants::SQUARE_BBS[H8];
                    self.PIECE_ARRAY[BR] &= !constants::SQUARE_BBS[F8];
                }
                TAG_BCASTLEQS => {
                    //BQS
                    //white king
                    self.PIECE_ARRAY[BK] |= constants::SQUARE_BBS[E8];
                    self.PIECE_ARRAY[BK] &= !constants::SQUARE_BBS[C8];
                    //white rook
                    self.PIECE_ARRAY[BR] |= constants::SQUARE_BBS[A8];
                    self.PIECE_ARRAY[BR] &= !constants::SQUARE_BBS[D8];
                }
                TAG_B_KNIGHT_PROMOTION => {
                    //BNPr
                    self.PIECE_ARRAY[BP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[BN] &= !constants::SQUARE_BBS[target_square];
                }
                TAG_B_BISHOP_PROMOTION => {
                    //BBPr
                    self.PIECE_ARRAY[BP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[BB] &= !constants::SQUARE_BBS[target_square];
                }
                TAG_B_QUEEN_PROMOTION => {
                    //BQPr
                    self.PIECE_ARRAY[BP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[BQ] &= !constants::SQUARE_BBS[target_square];
                }
                TAG_B_ROOK_PROMOTION => {
                    //BRPr
                    self.PIECE_ARRAY[BP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[BR] &= !constants::SQUARE_BBS[target_square];
                }
                TAG_W_KNIGHT_PROMOTION => {
                    //WNPr
                    self.PIECE_ARRAY[WP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[WN] &= !constants::SQUARE_BBS[target_square];
                }
                TAG_W_BISHOP_PROMOTION => {
                    //WBPr
                    self.PIECE_ARRAY[WP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[WB] &= !constants::SQUARE_BBS[target_square];
                }
                TAG_W_QUEEN_PROMOTION => {
                    //WQPr
                    self.PIECE_ARRAY[WP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[WQ] &= !constants::SQUARE_BBS[target_square];
                }
                TAG_W_ROOK_PROMOTION => {
                    //WRPr
                    self.PIECE_ARRAY[WP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[WR] &= !constants::SQUARE_BBS[target_square];
                }
                TAG_B_CAPTURE_KNIGHT_PROMOTION => {
                    //BNPrCAP
                    self.PIECE_ARRAY[BP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[BN] &= !constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[capture_index] |= constants::SQUARE_BBS[target_square];
                }
                TAG_B_CAPTURE_BISHOP_PROMOTION => {
                    //BBPrCAP
                    self.PIECE_ARRAY[BP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[BB] &= !constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[capture_index] |= constants::SQUARE_BBS[target_square];
                }
                TAG_B_CAPTURE_QUEEN_PROMOTION => {
                    //BQPrCAP
                    self.PIECE_ARRAY[BP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[BQ] &= !constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[capture_index] |= constants::SQUARE_BBS[target_square];
                }
                TAG_B_CAPTURE_ROOK_PROMOTION => {
                    //BRPrCAP
                    self.PIECE_ARRAY[BP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[BR] &= !constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[capture_index] |= constants::SQUARE_BBS[target_square];
                }
                TAG_W_CAPTURE_KNIGHT_PROMOTION => {
                    //WNPrCAP
                    self.PIECE_ARRAY[WP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[WN] &= !constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[capture_index] |= constants::SQUARE_BBS[target_square];
                }
                TAG_W_CAPTURE_BISHOP_PROMOTION => {
                    //WBPrCAP
                    self.PIECE_ARRAY[WP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[WB] &= !constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[capture_index] |= constants::SQUARE_BBS[target_square];
                }
                TAG_W_CAPTURE_QUEEN_PROMOTION => {
                    //WQPrCAP
                    self.PIECE_ARRAY[WP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[WQ] &= !constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[capture_index] |= constants::SQUARE_BBS[target_square];
                }
                TAG_W_CAPTURE_ROOK_PROMOTION => {
                    //WRPrCAP
                    self.PIECE_ARRAY[WP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[WR] &= !constants::SQUARE_BBS[target_square];
                    self.PIECE_ARRAY[capture_index] |= constants::SQUARE_BBS[target_square];
                }
                TAG_DOUBLE_PAWN_WHITE => {
                    //WDouble
                    self.PIECE_ARRAY[WP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[WP] &= !constants::SQUARE_BBS[target_square];
                }
                TAG_DOUBLE_PAWN_BLACK => {
                    //BDouble
                    self.PIECE_ARRAY[BP] |= constants::SQUARE_BBS[starting_square];
                    self.PIECE_ARRAY[BP] &= !constants::SQUARE_BBS[target_square];
                }
                _ => {}
            }

            self.CASTLE_RIGHTS[0] = copy_castle[0];
            self.CASTLE_RIGHTS[1] = copy_castle[1];
            self.CASTLE_RIGHTS[2] = copy_castle[2];
            self.CASTLE_RIGHTS[3] = copy_castle[3];
            self.EP = copy_ep;

            //if (epGlobal != NO_SQUARE)
            //{
            //    std::cout << "   ep: " << SQ_CHAR_X[epGlobal] << SQ_CHAR_Y[epGlobal] << '\n';
            //}

            //if (ply == 0) {
                //Engine::print_move_no_nl(starting_square, target_square);
                //print!(": {}\n", nodes - prior_nodes);
            //}
        }

        nodes
    }
    fn run_perft(&mut self, depth: i8) {
        let timestamp_start = Instant::now();

        let nodes = self.perft_inline(depth, 0);

        let elapsed = timestamp_start.elapsed();

        println!("Nodes: {}", nodes);
        println!("Elapsed time: {:?}", elapsed);
    }
}
fn main() {
    let mut wrapper = Engine::new();
    wrapper.set_starting_position();
    wrapper.print_board();
    wrapper.run_perft(6);
}