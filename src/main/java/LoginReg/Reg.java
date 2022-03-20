package LoginReg;

import Helper.HelperFucntions;
import org.postgresql.util.PSQLException;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.regex.Pattern;

public class Reg {
    public static void processRegister(Connection conn, BufferedReader reader) throws IOException, SQLException {
        PreparedStatement stmt;
        ResultSet rs;
        String input;
        Random rand = new Random();

        HelperFucntions.barCaps("register below");

        boolean flagFound = false;

        String username;
        String pass;
        String email;

        while (true) {
            while (true) {
                System.out.print("Username: ");
                username = reader.readLine();
                System.out.print("Password: ");
                pass = reader.readLine();

                if (rand.nextInt(100)  >= 4) {
                    break;
                }

                System.out.print("You sure? That name kinda sucks. (Y/N): ");
                input = reader.readLine();
                input = input.toLowerCase();

                if (input.equals("y")) {
                    break;
                } else if (input.equals("n")) {
                    System.out.println("\tGood choice.");
                } else {
                    System.out.println("\tI'll take that as a no.");
                }
            }

            System.out.println("\tMore info is needed.");

            while (true) {
                System.out.print("email: ");
                email = reader.readLine();
                if (HelperFucntions.isValidEmail(email))
                    break;
                else
                    System.out.println("\tInvalid email.");
            }

            stmt = conn.prepareStatement("SELECT username, email FROM p320_09.user");
            rs = stmt.executeQuery();

            while (rs.next()) {
                String un = rs.getString("username");
                String em = rs.getString("email");

                if (username.equals(un) || email.equals(em)) {
                    flagFound = true;
                    break;
                }
            }
            if (flagFound) {
                System.out.println("\tThat account already exists.");
                flagFound = false;
                continue;
            }

            buildNewUser(conn, reader, username, pass, email);
            break;
        }
    }

    public static void buildNewUser(Connection conn, BufferedReader reader, String un, String pw, String em) throws IOException, SQLException {
        PreparedStatement stmt;
        ResultSet rs;

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ssX");
        Date date = new Date(System.currentTimeMillis());

        int count = 0;

        stmt = conn.prepareStatement("SELECT * FROM p320_09.user ORDER BY userid DESC LIMIT 1");
        rs = stmt.executeQuery();

        if (rs.next()) {
            count += Integer.parseInt(rs.getString("userid")) + 1;
        }

        System.out.print("First name: ");
        String first = reader.readLine();
        System.out.print("Last name: ");
        String last = reader.readLine();

        stmt = conn.prepareStatement("INSERT INTO p320_09.user (userid, username, email, first_name, last_name," +
                " creation_date, last_access, password) VALUES (" + count + ", '" + un + "', '" + em + "', '" +
                first + "', '" + last + "', '" + formatter.format(date) + "', '" + formatter.format(date) + "', '" + pw + "')");

        try {
            stmt.executeQuery();
        } catch (PSQLException e) {
            System.out.println("\tRegistered.");
        }
    }
}
