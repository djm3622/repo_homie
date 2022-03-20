package Helper;

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
}
