package UserFollow;

import Helper.HelperFucntions;

import java.io.IOException;
import java.sql.Connection;
import java.io.BufferedReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {

    public static void UserMain(Connection conn, BufferedReader reader) throws SQLException, IOException {
        HelperFucntions.barCaps("Follow a Friend via Email Below");

        ArrayList<String> follows = new ArrayList<>();

        User u = new User();
        u.SearchUser(conn, reader);

    }

    public boolean SearchUser(Connection conn, BufferedReader reader) throws IOException, SQLException {
        String email = "";
        while (true) {
            System.out.print("Enter email : ");
            email = reader.readLine();
            if (HelperFucntions.isValidEmail(email)){
                PreparedStatement stmt = conn.prepareStatement("SELECT email FROM p320_09.user");
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String em = rs.getString("email");
                    if (email.equals(em))
                        break;
                }

            }
            else
                System.out.println("\tWhoops, not valid :(");
        }
    }


}
