package main

const WP = 0
const WN = 1
const WB = 2
const WR = 3
const WQ = 4
const WK = 5
const BP = 6
const BN = 7
const BB = 8
const BR = 9
const BQ = 10
const BK = 11
const EMPTY = 12

var PieceNames = [13]byte{'P', 'N', 'B', 'R', 'Q', 'K', 'P', 'N', 'B', 'R', 'Q', 'K', '_'}
var PieceColours = [13]byte{'W', 'W', 'W', 'W', 'W', 'W', 'B', 'B', 'B', 'B', 'B', 'B', '_'}

const WHITE_START_INDEX = WP
const WHITE_END_INDEX = WK
const BLACK_START_INDEX = BP
const BLACK_END_INDEX = BK
