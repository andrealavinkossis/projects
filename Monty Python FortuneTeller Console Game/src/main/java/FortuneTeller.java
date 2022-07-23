import java.util.Random;
import java.util.Scanner;

public class FortuneTeller {
    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);
        System.out.println("Welcome to Fortune Teller!");
        System.out.println("Time to make your first choice! Choose 1 for BLUE, 2 for GREEN, 3 for RED, and 4 for YELLOW.");

        int userEntryInt = 0;
        try {
            userEntryInt = keyboard.nextInt();

            if (userEntryInt == 1) {
                System.out.println("You chose BLUE!");
                System.out.println("Next, pick A, B, C, or D!");
            }
            if (userEntryInt == 2) {
                System.out.println("You chose GREEN!");
                System.out.println("Next, pick E, F, G, or H!");
            }
            if (userEntryInt == 3) {
                System.out.println("You chose RED!");
                System.out.println("Next, pick E, F, G, or H!");
            }
            if (userEntryInt == 4) {
                System.out.println("You chose YELLOW!");
                System.out.println("Next, pick A, B, C, or D!");

            } if (userEntryInt <= 0 || userEntryInt > 4) {
                System.out.println("The Fortune Teller doesn't like it when you are cheeky.");
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("The Fortune Teller doesn't like it when you are cheeky.");
            System.exit(0);
        }

        try {
            char userEntryChar = keyboard.next().charAt(0);

            if (userEntryChar == 'A' || userEntryChar == 'a') {
                System.out.println("'E's not pinin'! 'E's passed on! This parrot is no more! He has ceased to be!");
                System.out.println("'E's expired and gone to meet 'is maker! 'E's a stiff! Bereft of life, 'e rests in peace!");
                System.out.println("If you hadn't nailed 'im to the perch 'e'd be pushing up the daisies!");
                System.out.println("'Is metabolic processes are now 'istory! 'E's off the twig!");
                System.out.println(" 'E's kicked the bucket, 'e's shuffled off 'is mortal coil, run down the curtain and joined the bleedin' choir invisible!!");
                System.out.println("THIS IS AN EX-PARROT!!");
                System.exit(0);

            } else if (userEntryChar == 'B' || userEntryChar == 'b') {
                System.out.println("I'm a lumberjack");
                System.out.println("And I'm okay");
                System.out.println("I sleep all night and I work all day");
                System.out.println("I cut down trees");
                System.out.println("I wear high heels");
                System.out.println("Suspendies' and a bra");
                System.out.println("I wish I'd been a girly");
                System.out.println("Just like my dear pa-pa");
                System.exit(0);

            } else if (userEntryChar == 'C' || userEntryChar == 'c') {
                System.out.println("Morning!");
                System.out.println("Well, what've you got?");
                System.out.println("Well, there's egg and bacon; egg sausage and bacon; egg and spam");
                System.out.println("egg bacon and spam; egg bacon sausage and spam; spam bacon sausage and spam");
                System.out.println("spam egg spam spam bacon and spam; spam sausage spam spam bacon spam tomato and spam;");
                System.exit(0);

            } else if (userEntryChar == 'D' || userEntryChar == 'd') {
                System.out.println("NOBODY expects the Spanish Inquisition!");
                System.out.println("Our chief weapon is surprise...surprise and fear...fear and surprise....");
                System.out.println("Our two weapons are fear and surprise...and ruthless efficiency....");
                System.out.println("Our *three* weapons are fear, and surprise, and ruthless efficiency");
                System.out.println("...and an almost fanatical devotion to the Pope....");
                System.out.println("Our *four*...no... *Amongst* our weapons.... Amongst our weaponry...");
                System.out.println("are such elements as fear, surprise.... I'll come in again.");
                System.exit(0);

            } else if (userEntryChar == 'E' || userEntryChar == 'e') {
                System.out.println("That's no ordinary rabbit!");
                System.out.println("That's the most foul, cruel, and bad-tempered rodent you ever set eyes on!");
                System.out.println("Look, that rabbit's got a vicious streak a mile wide! It's a killer!");
                System.out.println(" ... that rabbit's dynamite!");
                System.exit(0);

            } else if (userEntryChar == 'F' || userEntryChar == 'f') {
                System.out.println("And the Lord spake, saying, 'First shalt thou take out the Holy Pin.");
                System.out.println("Then, shalt thou count to three. No more. No less.");
                System.out.println("Three shalt be the number thou shalt count, and the number of the counting shall be three.");
                System.out.println("Four shalt thou not count, nor either count thou two, excepting that thou then proceed to three.");
                System.out.println("Five is right out.");
                System.exit(0);

            } else if (userEntryChar == 'G' || userEntryChar == 'g') {
                System.out.println("Listen. Strange women lying in ponds distributing swords is no basis for a system of government.");
                System.out.println("Supreme executive power derives from a mandate from the masses, not from some farcical aquatic ceremony.");
                System.out.println("You can't expect to wield supreme power just 'cause some watery tart threw a sword at you!");
                System.exit(0);

            } else if (userEntryChar == 'H' || userEntryChar == 'h') {
                System.out.println("What is the airspeed velocity of an unladen swallow?");
                System.exit(0);

            } else {
                System.out.println("Ni!");
                System.exit(0);

            }
        } catch (Exception e) {
            System.out.println("Ni!");
            System.exit(0);
        }
    }
}







