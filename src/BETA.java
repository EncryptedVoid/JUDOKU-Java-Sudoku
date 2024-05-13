public class BETA {

    public static void LoadingScreen() {
        System.out.println("LOADING...");
        System.out.println("BOOTING UP...");
        System.out.println("LOADING ASSETS...");
        System.out.println("INITIALISING VARIABLES...");
        System.out.println("STARTING...");
    }

    public static String[][] Menu() {
        System.out.println("yes");
        return null;
    }

    public static String[][] Startup() {
        LoadingScreen();
        return Menu();
    }

    public static void main (String [] args) {
        String[][] map = StartUp();

        do {
            gameState = simulate(gameState);
        } while (!endgameState(gameState));

        ShutDown();
    }
}
