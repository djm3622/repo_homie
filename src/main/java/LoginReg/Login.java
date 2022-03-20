package LoginReg;

import Helper.HelperFucntions;
import Home.HomePage;
import org.postgresql.util.PSQLException;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

public class Login {
    public static void processLogin(Connection conn, BufferedReader reader) throws IOException, SQLException {
        PreparedStatement stmt;
        ResultSet rs;

        HelperFucntions.barCaps("login below");

        boolean flagFound = false;

        while (true) {
            System.out.print("Username: ");
            String username = reader.readLine();
            if(username.equals("-q")){
                break;
            }
            System.out.print("Password: ");
            String pass = reader.readLine();

            stmt = conn.prepareStatement("SELECT username, password FROM p320_09.user");
            rs = stmt.executeQuery();

            while (rs.next()) {
                String un = rs.getString("username");
                String pw = rs.getString("password");

                if (username.equals(un) && pass.equals(pw)) {
                    flagFound = true;
                    break;
                }
            }
            if (flagFound) {
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ssX");
                Date date = new Date(System.currentTimeMillis());
                stmt = conn.prepareStatement("UPDATE p320_09.user SET last_access = '" + formatter.format(date)
                        + "' WHERE username = '" + username + "'");
                try {
                    stmt.executeQuery();
                } catch (PSQLException e) {
                    System.out.println("\tSuccessfully logged in.");
                }
                HomePage home = new HomePage(conn,username);
                home.homeHandle(reader, conn);
                break;
            } else {
                System.out.println("\tInvalid Username and/or Password.");
            }
        }
    }
}
