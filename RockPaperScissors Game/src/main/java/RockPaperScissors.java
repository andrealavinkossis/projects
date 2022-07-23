import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class RockPaperScissors { // how do i turn this into a callable class?
    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in); // once this connects to Games I can remove the keyboard element here as well as the lines thru "try"

        boolean reStartGame = true;

        while (reStartGame) {
            System.out.println("Enter 1 for ROCK, 2 for PAPER, or 3 for SCISSORS");

            try {
                int userEntryInt = Integer.parseInt(keyboard.nextLine()); // if the player enters a letter instead of an int, this skips to the catch statement

                if (userEntryInt == 1) {
                    System.out.println("You chose Rock!");

                } else if (userEntryInt == 2) {
                    System.out.println("You chose Paper!");

                } else if (userEntryInt == 3) {
                    System.out.println("You chose Scissors!");

                } else {
                    System.out.println("Nice try, but I'm on to you. Please pick a valid number.");
                    reStartGame = true;
                }

                Random computerPick = new Random();
                int answer = computerPick.nextInt(2) + 1;

                if (answer == 1) {
                    System.out.println("Your opponent chose Rock!");

                } else if (answer == 2) {
                    System.out.println("Your opponent chose Paper!");

                } else {
                    System.out.println("Your opponent chose Scissors!");
                }

                boolean rockTie = (userEntryInt == 1 && answer == 1);
                boolean paperTie = (userEntryInt == 2 && answer == 2);
                boolean scissorsTie = (userEntryInt == 3 && answer == 3);
                boolean rockPaperLose = (userEntryInt == 1 && answer == 2);
                boolean rockScissorsWin = (userEntryInt == 1 && answer == 3);
                boolean paperRockWin = (userEntryInt == 2 && answer == 1);
                boolean paperScissorsLose = (userEntryInt == 2 && answer == 3);
                boolean scissorsRockLose = (userEntryInt == 3 && answer == 1);
                boolean scissorsPaperWin = (userEntryInt == 3 && answer == 2);

                if (rockTie || paperTie || scissorsTie) {
                    System.out.println("You tied! Try again!");
                    reStartGame = true;

                } else if (rockPaperLose || paperScissorsLose || scissorsRockLose) {
                    System.out.println("You lost! Womp-womp!");
                    reStartGame = false;

                } else if (rockScissorsWin || paperRockWin || scissorsPaperWin) {
                    System.out.println("You won! Woot-woot!");
                    reStartGame = false;
                }

            } catch (NumberFormatException e) {
                System.out.println("Nice try, but I'm on to you! You lose :(");
            }
        }
    }
}
