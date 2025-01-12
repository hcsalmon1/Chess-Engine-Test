
public class ChessEngine 
{
    public static void main(String[] args) 
    {
        //Testing.testRookMoves();
        Board.SetTrickyPosition();
        //Board.SetStartingPosition();
        Board.PrintBoard();
        Perft.RunPerftInlineGlobalOcc(2);
        //Board.PrintBoard();
    }
}