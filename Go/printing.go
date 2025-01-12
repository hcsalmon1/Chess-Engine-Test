package main

import "fmt"

func PrintMoveNoNL(starting int, target_square int, tag int) { //starting

	if OutOfBounds(starting) == true {
		fmt.Printf("%d", starting)
	} else {
		fmt.Printf("%c", SQ_CHAR_X[starting])
		fmt.Printf("%c", SQ_CHAR_Y[starting])
	}
	//target
	if OutOfBounds(target_square) == true {
		fmt.Printf("%d", target_square)
	} else {
		fmt.Printf("%c", SQ_CHAR_X[target_square])
		fmt.Printf("%c", SQ_CHAR_Y[target_square])
	}
	if tag == TAG_BCaptureKnightPromotion || tag == TAG_BKnightPromotion || tag == TAG_WKnightPromotion || tag == TAG_WCaptureKnightPromotion {
		fmt.Printf("n")
	} else if tag == TAG_BCaptureRookPromotion || tag == TAG_BRookPromotion || tag == TAG_WRookPromotion || tag == TAG_WCaptureRookPromotion {
		fmt.Printf("r")
	} else if tag == TAG_BCaptureBishopPromotion || tag == TAG_BBishopPromotion || tag == TAG_WBishopPromotion || tag == TAG_WCaptureBishopPromotion {
		fmt.Printf("b")
	} else if tag == TAG_BCaptureQueenPromotion || tag == TAG_BQueenPromotion || tag == TAG_WQueenPromotion || tag == TAG_WCaptureQueenPromotion {
		fmt.Printf("q")
	}
}
