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
            System.out.println("\t-v  : to display collection contents");
            System.out.println("\t-q  : to return to home");
            System.out.print("> ");

            input = reader.readLine();

            switch (input) {
                case "-as":
                    addSong(reader, conn, collectionID);
                    break;
                case "-ds":
                    deleteSong(reader, conn, collectionID);
                    break;
                case "-aa":
                    addAlbum(reader, conn, collectionID);
                    break;
                case "-da":
                    deleteAlbum(reader, conn, collectionID);
                    break;
                case "-m":
                    changeCollectionName(reader, conn, collectionID);
                    break;
                case "-d":
                    deleteCollection(reader, conn, collectionID);
                    break;
                case "-c":
                    // delete?
                    break;
                case "-v":
                    displayCollectionContents(conn, reader, collectionID);
                case "-q":
                    break label;
            }
        }
    }

    public static void addSong(BufferedReader reader, Connection conn, int collectionID) throws IOException, SQLException {
        System.out.println(" ");
        HelperFucntions.barCaps("song name you want to add");
        System.out.print("> ");
        String input = reader.readLine();

        // find and verify song to add
        PreparedStatement stmt = conn.prepareStatement("select s.songid, s.song_name, a.artist_name from " +
                "p320_09.song as s, p320_09.artist as a where s.artistid = a.artistid and s.song_name = \'" + input +"\';");
        ResultSet rs;
        try {
            rs = stmt.executeQuery();
        }catch(Exception e){
            System.out.println("Song not found");
            return;
        }
        System.out.println(" ");
        HelperFucntions.barCaps("enter id of song to add");
        while(rs.next()){
            int songID = rs.getInt("songid");
            String song_name = rs.getString("song_name");
            String artist_name = rs.getString("artist_name");
            System.out.println("\tid: " + songID + ", " + song_name + " by " + artist_name);
        }
        System.out.print("> ");
        String str = reader.readLine();
        int in = Integer.parseInt(str);

        // find next track_number
        int track_number = getTrackNumber(conn, collectionID);

        //insert song
        stmt = conn.prepareStatement("insert into p320_09.collection_track(collectionid, songid, track_number) " +
                "values (" + collectionID + ", " + in + ", " + track_number + ")");
        try{
            stmt.executeQuery();
        }catch (PSQLException e){
            System.out.println("Successfully added song");
        }
    }

    public static void deleteSong(BufferedReader reader, Connection conn, int collectionID) throws SQLException, IOException {
        System.out.println(" ");
        HelperFucntions.barCaps("song name you want to delete");
        System.out.print("> ");
        String input = reader.readLine();

        // find and verify song to add
        PreparedStatement stmt = conn.prepareStatement("select s.song_name, ct.songid, ct.track_number, a.artist_name " +
                "from p320_09.collection_track as ct, p320_09.song as s, p320_09.artist as a where s.song_name = \'"
                + input + "\' and ct.songid = s.songid and s.artistid = a.artistid and collectionid = " + collectionID);
        ResultSet rs;
        try {
            rs = stmt.executeQuery();
        }catch(Exception e){
            System.out.println("Song not found");
            return;
        }
        System.out.println(" ");
        HelperFucntions.barCaps("enter track number of song to delete");
        while(rs.next()){
            int track_number = rs.getInt("track_number");
            String song_name = rs.getString("song_name");
            String artist_name = rs.getString("artist_name");
            System.out.println("\ttrack: " + track_number + ", " + song_name + " by " + artist_name);
        }
        System.out.print("> ");
        String str = reader.readLine();
        int in = Integer.parseInt(str);

        // delete song
        stmt = conn.prepareStatement("delete from p320_09.collection_track where collectionid = " + collectionID +
                " and track_number = " + in);
        try{
            stmt.executeQuery();
        }catch (PSQLException e){
            System.out.println("Successfully deleted song");
        }

        // update track_numbers
        stmt = conn.prepareStatement("update p320_09.collection_track set track_number = (track_number - 1) " +
                "where track_number > " + in + " and collectionid = " + collectionID) ;
        try{
            stmt.executeQuery();
        }catch (PSQLException ignored){
        }
    }

    public static void addAlbum(BufferedReader reader, Connection conn, int collectionID) throws IOException, SQLException {
        System.out.println(" ");
        HelperFucntions.barCaps("album name you want to add");
        System.out.print("> ");
        String album = reader.readLine();

        PreparedStatement stmt = conn.prepareStatement("select a.albumid, a.title, a.artistid, ar.artist_name " +
                "from p320_09.album as a, p320_09.artist as ar where ar.artistid = a.artistid and " +
                "a.title = \'" + album + "\'");
        ResultSet rs;

        try{
            rs = stmt.executeQuery();
        }catch(PSQLException e){
            System.out.println("Album not found");
            return;
        }

        System.out.println(" ");
        HelperFucntions.barCaps("enter album id of album to add");
        int albumID;
        while(rs.next()){
            albumID = rs.getInt("albumid");
            String album_name = rs.getString("title");
            String artist_name = rs.getString("artist_name");
            System.out.println("\tID: " + albumID + ", " + album_name + " by " + artist_name);
        }
        System.out.print("> ");
        album = reader.readLine();
        albumID = Integer.parseInt(album);

        // find track_number
        int track_number = getTrackNumber(conn, collectionID);

        // select and add all songs in album
        stmt = conn.prepareStatement("select songid from p320_09.album_track where albumid = " + albumID);
        ResultSet set = stmt.executeQuery();
        while(set.next()){
            int songID = set.getInt("songid");
            PreparedStatement mt = conn.prepareStatement("insert into p320_09.collection_track(collectionid, " +
                    "songid, track_number) values (" + collectionID + ", " + songID + ", " + track_number + ")");
            track_number++;
            try{
                mt.executeQuery();
            }catch (PSQLException ignored){
            }
        }
        System.out.println("Successfully added album");

    }

    public static void deleteAlbum(BufferedReader reader, Connection conn, int collectionID) throws IOException, SQLException {
        System.out.println(" ");
        HelperFucntions.barCaps("name of album you want to delete");
        System.out.print("> ");
        String input = reader.readLine();

        PreparedStatement stmt = conn.prepareStatement("select a.albumid, a.title, ar.artist_name from " +
                "p320_09.album as a, p320_09.album_track as at, p320_09.artist as ar where a.title = \'" + input +
                "\' and a.albumid = at.albumid and a.artistid = ar.artistid");
        ResultSet rs;
        try {
            rs = stmt.executeQuery();
        }catch(Exception e){
            System.out.println("Album not found");
            e.printStackTrace();
            return;
        }
        System.out.println(" ");
        HelperFucntions.barCaps("enter album id of album to delete");
        int albumID;
        while(rs.next()){
            albumID = rs.getInt("albumid");
            String title = rs.getString("title");
            String artist_name = rs.getString("artist_name");
            System.out.println("\tID: " + albumID + ", " + title + " by " + artist_name);
          }
//        else{
//            System.out.println("Could not find album");
//            return;
//        }
        System.out.print("> ");
        String temp = reader.readLine();
        albumID = Integer.parseInt(temp);

        stmt = conn.prepareStatement("select at.songid, ct.track_number from p320_09.album_track as at, " +
                "p320_09.collection_track as ct where at.albumid = " + albumID + " and at.songid = ct.songid and " +
                "collectionid = " + collectionID);
        ResultSet set = stmt.executeQuery();
        if(set.next()){
            int songID = set.getInt("songid");
            int track = set.getInt("track_number");
            System.out.println("songID: " + songID);
            System.out.println("track: " + track);
            // delete song
            PreparedStatement mt = conn.prepareStatement("delete from p320_09.collection_track where " +
                    "collectionid = " + collectionID + " and songid = " + songID);
            try{
                mt.executeQuery();
            }catch(PSQLException ignored){
            }
            updateTrackNumber(conn, collectionID,track);
        }
        System.out.println("Successfully deleted album");
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

    public static void deleteCollection(BufferedReader reader, Connection conn, int collectionID) throws IOException, SQLException {
        HelperFucntions.barCaps("are you sure you want to delete this collection? (y/n)");
        String input = reader.readLine();
        if(input.toLowerCase().equals("y") || input.toLowerCase().equals("yes")) {
            PreparedStatement stmt = conn.prepareStatement("delete from p320_09.collection_track where " +
                    "collectionid = " + collectionID );
            try{
                stmt.executeQuery();
            }catch(PSQLException ignored){

            }
            stmt = conn.prepareStatement("delete from p320_09.collection where collectionid = " + collectionID);
            try{
                stmt.executeQuery();
            }catch(PSQLException ignored){
            }
            System.out.println("Successfully deleted collection");
        }
        else{
            System.out.println("Returning to collection edit options");
        }
    }

    public static void updateTrackNumber(Connection conn, int collectionID, int track) throws SQLException {
        // update track numbers
        PreparedStatement state = conn.prepareStatement("update p320_09.collection_track set track_number = " +
                "(track_number - 1) where track_number > " + track + " and collectionid = " + collectionID);
        try{
            state.executeQuery();
        }catch (PSQLException ignored){
        }
    }

    public static int getTrackNumber(Connection conn, int collectionID) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM p320_09.collection_track where collectionid = " +
                collectionID + " ORDER BY track_number DESC LIMIT 1");
        ResultSet set = stmt.executeQuery();

        int track_number = 1;
        if (set.next()) {
            track_number = Integer.parseInt(set.getString("track_number")) + 1;
        }
        return track_number;
    }

    public static void displayCollectionContents(Connection conn, BufferedReader reader, int collectionID) throws SQLException {
        PreparedStatement stmt;
        ResultSet rs;

        stmt = conn.prepareStatement("SELECT s.song_name, ar.artist_name FROM " +
                "p320_09.collection_track AS ct, p320_09.artist AS ar, p320_09.song AS s " +
                "WHERE ct.songid = s.songid " +
                "AND s.artistid = ar.artistid " +
                "AND ct.collectionid = " + collectionID);
        rs = stmt.executeQuery();

        if (rs.next()) {
            String snm = rs.getString("song_name");
            String anm = rs.getString("artist_name");
            System.out.println("\t(" + snm + ") by (" + anm + ")");

            while (rs.next()) {
                snm = rs.getString("song_name");
                anm = rs.getString("artist_name");
                System.out.println("\t(" + snm + ") by (" + anm + ")");
            }
        } else {
            System.out.println("\tCollection is empty.");
        }
    }
}
