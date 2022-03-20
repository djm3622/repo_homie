package Helper;

public class HelperFucntions {
    public static void barCaps(String str) {
        int len = str.length();
        String bar = new String(new char[len]).replace('\0', '=');
        System.out.println("" + bar + "");
        System.out.println("" + str.toUpperCase() + "");
        System.out.println("" + bar + "");
    }
}
