using System;

namespace CEngineCopy
{
    public class Program
    {


        static void Main()
        {
            Board.SetStartingPosition();
            Debugging.PrintBoardGlobal();
            Console.ReadLine();
            Perft_Func.RunPerftFunctions(6);
            //Perft_Debug.RunPerftFunctionsDebug(6);
        }


    }
}
