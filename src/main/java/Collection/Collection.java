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
    /**
     * currently doesnt show collections without any songs
     * @param conn
     * @param userID
     * @param flag
     * @throws SQLException
     */
    public static void viewCollections(Connection conn, int userID, boolean flag) throws SQLException {
        // true to print only names of a user's collections
        if(flag){
            PreparedStatement stmt = conn.prepareStatement("select name from p320_09.collection where userid=" +
                    userID);
            ResultSet set = stmt.executeQuery();
            while(set.next()){
                String collectionName = set.getString("name");
                System.out.println("\t- " + collectionName);
            }
        }
        else{
            PreparedStatement stmt = conn.prepareStatement("SELECT c.name, COUNT(ct.track_number) AS total_songs, " +
                    "SUM(s.length) AS total_length FROM p320_09.collection_track AS ct, p320_09.collection AS c, " +
                    "p320_09.song AS s WHERE ct.songID = s.songID AND c.collectionID = ct.collectionID AND " +
                    "c.userID = " + userID + " GROUP BY c.name " +
                    "ORDER BY c.name ASC;");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                String name = rs.getString("name");
                int songs = rs.getInt("total_songs");
                int length = rs.getInt("total_length");
                System.out.println(name + ", " + songs + " songs, " + length + " minutes");
            }
        }
    }

    public static void chooseCollection(BufferedReader reader, Connection conn, int userID) throws SQLException,
            IOException {
        viewCollections(conn, userID, true);
        // call viewCollection with special flag for only names
        System.out.println("choose a collection to edit");
        System.out.print("> ");
        String input = reader.readLine();

        PreparedStatement stmt = conn.prepareStatement("select collectionid, name from p320_09.collection where " +
                "userid=" + userID);
        ResultSet set = stmt.executeQuery();

        label:
        if(set.next()){
            int collectionID = set.getInt("collectionid");
            String name = set.getString("name");
            if(name.equals(input)){
                System.out.println("callling editCollection");
                //editCollection(reader, conn, collectionID);
                break label;
            }
        }
    }

    public static void editCollection(BufferedReader reader, Connection conn, int collectionID) throws IOException {
        String input;
        label:
        while (true) {
            System.out.println("");
            System.out.println("Commands as Follows:");
            System.out.println("\t-c : to add song");
            System.out.println("\t-v : to delete song");
            System.out.println("\t-s : to add album");
            System.out.println("\t-e : to delete album");
            System.out.println("\t-u : to modify name");
            System.out.println("\t-q : to delete collection");
            System.out.print("> ");

            input = reader.readLine();

            switch (input) {
                case "-c":
                    break;
                case "-v":
                    break;
                case "-s":
                    break;
                case "-e":
                    break;
                case "-u":
                    break;
                case "-q":
                    break label;
            }
        }
    }
}
