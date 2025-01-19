
public class ChessEngine 
{
    public static void main(String[] args) 
    {
        Board.setStartingPosition();
        //Board.setTestPosition();
        Perft.PrintBoardGlobal();
        //Perft.runPerftFunctionsDebug(6);
        Perft.runPerftFunctions(6);
    }
}