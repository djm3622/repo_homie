package Collection;

import Helper.HelperFucntions;
import org.postgresql.util.PSQLException;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;

public class Collection {
    public static void createCollection(BufferedReader reader, Connection conn, int userID) throws SQLException, IOException {
        HelperFucntions.barCaps("Name Your New Collection Below");
        System.out.print("> ");
        String input = reader.readLine();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM p320_09.collection ORDER BY collectionid DESC LIMIT 1");
        ResultSet set = stmt.executeQuery();

        int ID = 0;
        if (set.next()) {
            ID += Integer.parseInt(set.getString("collectionid")) + 1;
        }

        stmt = conn.prepareStatement("INSERT INTO p320_09.collection (collectionid, userid, name) values (" +
                ID + ", " + userID + ", '" + input + "')");
        try{
            stmt.executeQuery();
        }catch(PSQLException e){
            System.out.println("Collection Created.");
        }
    }
}
