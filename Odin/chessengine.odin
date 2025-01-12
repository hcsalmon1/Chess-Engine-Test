package main

main :: proc() {

	LoadFen(FEN_STARTING_POSITION)
	PrintBoard()
	RunPerftInline(6)
}
