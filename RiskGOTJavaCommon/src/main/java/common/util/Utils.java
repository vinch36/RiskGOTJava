package common.util;

import java.util.Random;

public class Utils {

    public static int getNombreDeTroupesAPlacer(int nbJoueur)
    {
        if (nbJoueur==3){
            return 28;
        }
        if (nbJoueur==4){
            return 24;
        }
        if (nbJoueur==5){
            return 20;
        }
        return 0;
    }

    public static int getNombreDeTroupesAPlacerApresChoixTerrioire(int nbJoueur)
    {
        if (nbJoueur==3){
            return 28-16-2;
        }
        if (nbJoueur==4){
            return 24-12-2;
        }
        if (nbJoueur==5){
            return 20-10-2;
        }
        return 0;
    }

    public static String  givenUsingJava8_whenGeneratingRandomAlphabeticString_thenCorrect() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return(generatedString);
    }

}
