package inputscrubber;

import java.util.Scanner;
import java.util.StringTokenizer;

public class Scrubber {
    
    public static char charScrubber(String str, Scanner sc) {
    if (str.length() != 1 || str == null) {
            System.out.print("Sorry, input was null or input wasn't a single character! Please repeat your answer: ");
            String input = sc.next();
            return charScrubber(input, sc);
    }
    str = str.toLowerCase();
    str = str.trim();
    String shortString = str.substring(0,1);
    System.out.println("The char to be compared to abcd is " + shortString);
    
    if (shortString.matches("[a-d]") == false) {
            System.out.print("Sorry, input wasn't a, b, c, or d (can be capitalized too)! Please repeat your answer: ");
            String input = sc.next();
            return charScrubber(input, sc);
    }
    return shortString.charAt(0);
}

    public static boolean boolScrubber(String str, Scanner sc) {
        while (!str.equalsIgnoreCase("true") && !str.equalsIgnoreCase("false") && !str.equalsIgnoreCase("t") && !str.equalsIgnoreCase("f")) {
                System.out.print("Sorry, input isn't true, false, t, or f. (Can be upper- or lower case.)");
                str = sc.nextLine();
        }

            if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("t"))
                return true;
            else
                return false;
    }

    public static String[] shortScrubber(String str, Scanner sc) {
        /*for this to work, the scanner that initially brought in the answer from user must be set to nextLine(). 
        This method returns an array of either length 1 or 2. 
        Even if this method is only passed a string with no spaces, it will still return an array. 
        The array that is returned will only contain lower case characters.*/ 
        
        while (str == null || str == "") {
            System.out.print("Uh-oh! Your input is blank! Please enter again: ");
            str = sc.nextLine();
        }
        
        if (!str.contains(" ")) {   
            str = str.toLowerCase();
            String[] strArray = new String[] {str};
            return strArray;
        }
        
        else {
            StringTokenizer tokens = new StringTokenizer(str);
            int wordCount = tokens.countTokens();
            while (wordCount != 2) {
                    System.out.print("Oops! Your input has too many or too few words! Please enter again: ");
                    str = sc.nextLine();
                    StringTokenizer tokens2 = new StringTokenizer(str);
                    wordCount = tokens2.countTokens();
            }
            str = str.toLowerCase();
            String[] strArray = str.split("\\s+");// the \\s+ should detect multiple spaces and eliminate those as well, meaning that we don't need to use a for loop to trim().
            return strArray;
        }
    }

    public static int intScrubber(String unscrubbedInput, Scanner sc) {
        while (unscrubbedInput == null || unscrubbedInput == "") {
            System.out.print("Sorry, your input is blank! Please enter again: ");
            unscrubbedInput = sc.nextLine();
        }
        while (!unscrubbedInput.matches("[0-9]+")) {
            System.out.println("Sorry, your input wasn't an int! Please enter again: ");
            unscrubbedInput = sc.nextLine();
        }
        int myInt = Integer.parseInt(unscrubbedInput);
        return myInt;
    }
}