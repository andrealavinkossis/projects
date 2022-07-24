public class FortuneTeller2 {


    String responseAa = "'E's not pinin'! 'E's passed on! This parrot is no more! He has ceased to be! \n" +
            "'E's expired and gone to meet 'is maker! 'E's a stiff! Bereft of life, 'e rests in peace!\n" +
            "If you hadn't nailed 'im to the perch 'e'd be pushing up the daisies!\n" +
            "'E's kicked the bucket, 'e's shuffled off 'is mortal coil, run down the curtain and joined the bleedin' choir invisible!\n" +
            "THIS IS AN EX-PARROT!!";

    String responseBb = "I'm a lumberjack\n And I'm okay. I sleep all night and I work all day.\n" +
            "I cut down trees \n I wear high heels \n Suspendies' and a bra\n" +
            "I wish I'd been a girly \n Just like my dear pa-pa";

    String responseCc = "Morning! \n Well, what've you got? \n Well, there's egg and bacon, egg sausage and bacon, egg and spam\n" +
            "egg bacon and spam, egg bacon sausage and spam, spam bacon sausage and spam\n" +
            "spam egg spam spam bacon and spam, and spam sausage spam spam bacon spam tomato and spam";

    String responseDd = "NOBODY expects the Spanish Inquisition!\n Our chief weapon is surprise...surprise and fear...fear and surprise..." +
            "Our two weapons are fear and surprise...and ruthless efficiency...\n" +
            "Our *three* weapons are fear, and surprise, and ruthless efficiency\n" +
            "...and an almost fanatical devotion to the Pope...\n" +
            "Our *four*...no... *Amongst* our weapons.... Amongst our weaponry...\n" +
            "are such elements as fear, surprise.... I'll come in again.";

    String responseEe = "That's no ordinary rabbit!\n That's the most foul, cruel, and bad-tempered rodent you ever set eyes on!\n" +
            "Look, that rabbit's got a vicious streak a mile wide! It's a killer! \n  ... that rabbit's dynamite!";

    String responseFf = "And the Lord spake, saying, 'First shalt thou take out the Holy Pin.\n" +
            "Then, shalt thou count to three. No more. No less.\nThree shalt be the number thou shalt count, and the number of the counting shall be three." +
            "Four shalt thou not count, nor either count thou two, excepting that thou then proceed to three.\nFive is right out.";

    String responseGg = "Listen. Strange women lying in ponds distributing swords is no basis for a system of government.\n" +
            "Supreme executive power derives from a mandate from the masses, not from some farcical aquatic ceremony.\n" +
            "You can't expect to wield supreme power just 'cause some watery tart threw a sword at you!";

    String responseHh = "What is the airspeed velocity of an unladen swallow?";

    public String evaluateFirstChoice(int fortunaUserEntryOneInt) {

        try {
            if (fortunaUserEntryOneInt == 1) {
                System.out.println("You chose BLUE! \n Next, pick A, B, C, or D!");
            }
            if (fortunaUserEntryOneInt == 2) {
                System.out.println("You chose GREEN! \n Next, pick E, F, G, or H!");
            }
            if (fortunaUserEntryOneInt == 3) {
                System.out.println("You chose RED! \n Next, pick E, F, G, or H!");
            }
            if (fortunaUserEntryOneInt == 4) {
                System.out.println("You chose YELLOW! Next, pick A, B, C, or D!");
            }
        } catch (Exception e) {
            System.out.println("Ni! Make a valid choice!");
        }
        return null;
    }

    char userEntryChar;
    public String evaluateFirstChar(char userEntryChar) {

        try {
            if (userEntryChar == 'A' || userEntryChar == 'a') {
                System.out.println(responseAa);
            } else if (userEntryChar == 'B' || userEntryChar == 'b') {
                System.out.println(responseBb);
            } else if (userEntryChar == 'C' || userEntryChar == 'c') {
                System.out.println(responseCc);
            } else if (userEntryChar == 'D' || userEntryChar == 'd') {
                System.out.println(responseDd);
            } else if (userEntryChar == 'E' || userEntryChar == 'e') {
                System.out.println(responseEe);
            } else if (userEntryChar == 'F' || userEntryChar == 'f') {
                System.out.println(responseFf);
            } else if (userEntryChar == 'G' || userEntryChar == 'g') {
                System.out.println(responseGg);
            } else if (userEntryChar == 'H' || userEntryChar == 'h') {
                System.out.println(responseHh);
            } else {
                System.out.println("Ni!");
            }

        } catch (Exception Ee) {
            System.out.println("Ni! Make a valid choice!");
        }
        return null;
    }
}

