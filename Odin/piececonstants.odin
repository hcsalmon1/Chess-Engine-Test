package main

WP :: 0
WN :: 1
WB :: 2
WR :: 3
WQ :: 4
WK :: 5
BP :: 6
BN :: 7
BB :: 8
BR :: 9
BQ :: 10
BK :: 11
EMPTY :: 12
@(rodata)
PieceNames := [13]byte{'P', 'N', 'B', 'R', 'Q', 'K', 'P', 'N', 'B', 'R', 'Q', 'K', '_'};
@(rodata)
PieceColours := [13]byte{'W', 'W', 'W', 'W', 'W', 'W', 'B', 'B', 'B', 'B', 'B', 'B', '_'};

WHITE_START_INDEX :: WP;
WHITE_END_INDEX :: WK;
BLACK_START_INDEX :: BP;
BLACK_END_INDEX :: BK;
