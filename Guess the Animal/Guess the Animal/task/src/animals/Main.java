package animals;

public class Main {

    public static void main(String[] args) {
        String filetype = "json";
        if (args.length > 1 && args[0].equals("-type")) {
            filetype = args[1];
        }
        if (System.getProperty("user.language").equals("en")) {
            play_en myGame = new play_en(filetype);
            myGame.menu();
        } else {
            play_eo myGame = new play_eo(filetype);
            myGame.menu();
        }
    }
}
