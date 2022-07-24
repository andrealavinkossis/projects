import java.util.Random;
import java.util.Scanner;

// this class will handle interactions w user.

public class GameConsole {

    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);
        Messages message = new Messages();

        boolean reStartGame = true;

        while (reStartGame) {

            message.whatGameWouldYouLike();

            try {
                String mainUserEntry = keyboard.nextLine();
                int mainUserEntryInt = Integer.parseInt(mainUserEntry);

                try {
                    if (mainUserEntryInt == 1) {
                        RPS2 rocky = new RPS2(); // instantiate a new game of R,P,S
                        message.RockPaperScissorsHello();
                        String userEntry = keyboard.nextLine();
                        int rockyUserEntryInt = Integer.parseInt(userEntry);
                        rocky.evaluateUserInput(rockyUserEntryInt);

                        Random computerPick = new Random();
                        int answer = computerPick.nextInt(2) + 1;

                        rocky.evaluateComputerRandom(answer);

                        rocky.winLoseorTie(rockyUserEntryInt, answer);
                        reStartGame = false;

                    } else if (mainUserEntryInt == 2) {

                        FortuneTeller2 fortuna = new FortuneTeller2();
                        message.FortuneTellerHello();
                        String fortunaUserEntryOne = keyboard.nextLine();

                        try { // is the try/catch here AND in FortuneTeller too much?
                            int fortunaUserEntryOneInt = Integer.parseInt(fortunaUserEntryOne);
                            fortuna.evaluateFirstChoice(fortunaUserEntryOneInt);
                            reStartGame = false;

                        } catch (Exception e) {
                            System.out.println("Nice try, but I'm on to you. Please make a valid choice.\n");
                            reStartGame = true;
                        }

                        String userEntrySecond = keyboard.nextLine();
                        try {
                            char userEntryChar = userEntrySecond.charAt(0);
                            fortuna.evaluateFirstChar(userEntryChar);

                        } catch (Exception e) {
                            System.out.println("Nice try, but I'm on to you. Please make a valid choice.\n");
                            reStartGame = true;

                        }

                    } else {
                        System.out.println("Please enter a valid selection.\n");
                        reStartGame = true;
                    }

                } catch (Exception e) {
                    System.out.println("Please enter a valid selection.\n");
                    reStartGame = true;
                }
            } catch (Exception e) {
                System.out.println("Please enter a valid selection.\n");
                reStartGame = true;
            }
        }
    }
}



