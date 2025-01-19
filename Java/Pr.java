import java.math.BigInteger;

public class Pr {
    
    public static void print(String input)
    {
        System.out.print(input);
    }
    public static void println(String input)
    {
        System.out.println(input);
    }
    public static void printInt(int input)
    {
        System.out.print(input);
    }
    public static void printIntLn(int input)
    {
        System.out.println(input);
    }
    public static void printBigInteger(BigInteger input)
    {
        System.out.print(input);
    }
    public static void printBigIntegerLn(BigInteger input)
    {
        System.out.println(input);
    }



    public static void printSquareLn(int input)
    {
        assert input >= 0 && input < 64 : "Invalid square: " + input;

        System.out.print(GenConst.SQ_CHAR_X[input]);
        System.out.println(GenConst.SQ_CHAR_Y[input]);
    }   
}
