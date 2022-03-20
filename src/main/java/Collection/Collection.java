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
                System.out.println("\t: " + collectionName);
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
        // call viewCollection with special flag for only names
        HelperFucntions.barCaps("choose a collection to edit");
        viewCollections(conn, userID, true);

        label:
        while (true) {
            System.out.print("> ");
            String input = reader.readLine();

            PreparedStatement stmt = conn.prepareStatement("select collectionid, name from p320_09.collection where " +
                    "userid=" + userID);
            ResultSet set = stmt.executeQuery();


            while (set.next()) {
                int collectionID = set.getInt("collectionid");
                String name = set.getString("name");
                if (name.equals(input)) {
                    editCollection(reader, conn, collectionID);
                    break label;
                }
            }

            System.out.println("\tCollection does not exist.");
        }
    }

    public static void editCollection(BufferedReader reader, Connection conn, int collectionID) throws IOException, SQLException {
        String input;
        label:
        while (true) {
            System.out.println("");
            System.out.println("Collection Commands as Follows:");
            System.out.println("\t-as : to add song");
            System.out.println("\t-ds : to delete song");
            System.out.println("\t-aa : to add album");
            System.out.println("\t-da : to delete album");
            System.out.println("\t-m  : to modify name");
            System.out.println("\t-d  : to delete collection");
            System.out.println("\t-c  : to change collection");
            System.out.println("\t-q  : to go back to home");
            System.out.print("> ");

            input = reader.readLine();

            switch (input) {
                case "-as":
                    break;
                case "-ds":
                    break;
                case "-aa":
                    break;
                case "-da":
                    break;
                case "-m":
                    changeCollectionName(reader, conn, collectionID);
                    break;
                case "-d":
                    break;
                case "-q":
                    break label;
            }
        }
    }

    public static void changeCollectionName(BufferedReader reader, Connection conn, int collectionID) throws IOException, SQLException {
        HelperFucntions.barCaps("change name below");
        System.out.print("> ");
        String input = reader.readLine();

        PreparedStatement stmt = conn.prepareStatement("UPDATE p320_09.collection SET name = '" + input +
                "' WHERE collectionid = '" + collectionID + "'");
        try{
            stmt.executeQuery();
        }catch(PSQLException e){
            System.out.println("\tName updated.");
        }
    }
}
