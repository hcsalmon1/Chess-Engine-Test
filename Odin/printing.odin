package main

import "core:fmt"

PrintMoveNoNL :: proc(starting: int, target_square: int, tag: int) { //starting

	if OutOfBounds(starting) == true {
		fmt.printf("%d", starting);
	} else {
		fmt.printf("%c", SQ_CHAR_X[starting]);
		fmt.printf("%c", SQ_CHAR_Y[starting]);
	}
	//target
	if OutOfBounds(target_square) == true {
		fmt.printf("%d", target_square);
	} else {
		fmt.printf("%c", SQ_CHAR_X[target_square]);
		fmt.printf("%c", SQ_CHAR_Y[target_square]);
	}
	if tag == TAG_BCaptureKnightPromotion || tag == TAG_BKnightPromotion || tag == TAG_WKnightPromotion || tag == TAG_WCaptureKnightPromotion {
		fmt.printf("n");
	} else if tag == TAG_BCaptureRookPromotion || tag == TAG_BRookPromotion || tag == TAG_WRookPromotion || tag == TAG_WCaptureRookPromotion {
		fmt.printf("r");
	} else if tag == TAG_BCaptureBishopPromotion || tag == TAG_BBishopPromotion || tag == TAG_WBishopPromotion || tag == TAG_WCaptureBishopPromotion {
		fmt.printf("b");
	} else if tag == TAG_BCaptureQueenPromotion || tag == TAG_BQueenPromotion || tag == TAG_WQueenPromotion || tag == TAG_WCaptureQueenPromotion {
		fmt.printf("q");
	}
}
