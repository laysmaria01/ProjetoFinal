package helpers;

public class Println {
    public static void colored(String text, AnsiScape ConstColor) {
        System.out.println(ConstColor.getValor() + text + AnsiScape.ANSI_RESET.getValor());
    }
}
