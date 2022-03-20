package Helper;

import LoginReg.Login;
import LoginReg.Reg;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class HelperFucntions {
    public static void barCaps(String str) {
        int len = str.length();
        String bar = new String(new char[len]).replace('\0', '=');
        System.out.println("" + bar + "");
        System.out.println("" + str.toUpperCase() + "");
        System.out.println("" + bar + "");
    }

    public static boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    // even return means ASC, odd means DESC
    // if even divide it by 2, odd leave it
    // 1 means by name
    // 3 means by artist
    // 5 means by genre
    // 7 means by release year
    public static int sortByStats(BufferedReader reader) throws IOException {

        int by = 0;
        String input;

        label:
        while(true) {
            System.out.println("Sort By:");
            System.out.println("\t-nm : by name");
            System.out.println("\t-an : by artist");
            System.out.println("\t-gr : by genre");
            System.out.println("\t-ry : by release year");
            System.out.println("\t-q : to quit");
            System.out.print("> ");

            input = reader.readLine();

            switch (input) {
                case "-nm":
                    by = 1;
                    break label;
                case "-an":
                    by = 3;
                    break label;
                case "-gr":
                    by = 5;
                    break label;
                case "-ry":
                    by = 7;
                    break label;
                case "-q":
                    return by;
            }
        }

        label:
        while (true) {
            System.out.print("ASC or DESC: ");

            input = reader.readLine();

            switch (input.toUpperCase()) {
                case "ASC":
                    by = by * 2;
                    break label;
                case "DESC":
                    break label;
            }
        }
    return by;
    }
}

