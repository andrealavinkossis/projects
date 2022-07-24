import java.util.Random;
import java.util.Scanner;

public class RPS2 {


        public void evaluateUserInput(int rockyUserEntryInt) {
            try {
                if (rockyUserEntryInt == 1) {
                    System.out.println("You chose Rock!");
                }
                if (rockyUserEntryInt == 2) {
                    System.out.println("You chose Paper!");
                }
                if (rockyUserEntryInt == 3) {
                    System.out.println("You chose Scissors!");
                }
            } catch (Exception e) {
                System.out.println("Nice try, but I'm on to you. Please enter a valid number.");
            }
        }

            public void evaluateComputerRandom(int answer) {

            if (answer == 1) {
                System.out.println("Your opponent chose Rock!");
            }
            if (answer == 2) {
                System.out.println("Your opponent chose Paper!");
            } else {
                System.out.println("Your opponent chose Scissors!");
            }
        }

    public void winLoseorTie(int rockyUserEntryInt, int answer) {
        boolean rockPaperLose = (rockyUserEntryInt == 1 && answer == 2);
        boolean rockScissorsWin = (rockyUserEntryInt == 1 && answer == 3);
        boolean paperRockWin = (rockyUserEntryInt == 2 && answer == 1);
        boolean paperScissorsLose = (rockyUserEntryInt == 2 && answer == 3);
        boolean scissorsRockLose = (rockyUserEntryInt == 3 && answer == 1);
        boolean scissorsPaperWin = (rockyUserEntryInt == 3 && answer == 2);

        if (rockPaperLose || paperScissorsLose || scissorsRockLose) {
            System.out.println("You lost! Womp-womp!");

        } else if (rockScissorsWin || paperRockWin || scissorsPaperWin) {
            System.out.println("You won! Woot-woot!");

        } else {
            System.out.println("You tied! Try again!");

        }
    }
    }



