package Home;

import Collection.Collection;
import Helper.HelperFucntions;
import Listening.Played;
import Reccs.Recc;
import SongRec.SongRec;
import SongSearch.SongSearch;
import UserFollow.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;

public class HomePage {
    private int userID;
    private String username;
    private String first_name;

    public HomePage(Connection conn, String username) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select userid, username, first_name from p320_09.user");
        ResultSet set = stmt.executeQuery();
        while(set.next()){
            int id = set.getInt("userid");
            String user = set.getString("username");
            String name = set.getString("first_name");
            if(user.equals(username)){
                this.userID = id;
                this.username = user;
                this.first_name = name;
                break;
            }
        }
    }
    public void homeHandle(BufferedReader reader, Connection conn) throws IOException, SQLException {
        System.out.println();
        HelperFucntions.barCaps("Welcome " + first_name + "!");

        System.out.print("Followers: ");
        printNumFollowers(conn, this.userID);

        System.out.print("\tFollowing: ");
        printNumFollowing(conn, this.userID);

        System.out.print("\nNumber of Collections: ");
        printNumCollections(conn, this.userID);

        System.out.println("\nTop 10 Artists: ");
        printTopArtists(conn, this.userID);

        String input;

        label:
        while (true) {
            System.out.println("");
            System.out.println("Home Commands as Follows:");
            System.out.println("\t-c  : to create collection");
            System.out.println("\t-v  : to view collections");
            System.out.println("\t-e  : to edit collection");
            System.out.println("\t-s  : to search for a song");
            System.out.println("\t-p  : play song(s)");
            System.out.println("\t-su : to search for a user");
            System.out.println("\t-fu : to follow a user");
            System.out.println("\t-uf : to unfollow a user");
            System.out.println("\t-re : get the rec");
            System.out.println("\t-q  : to log out");
            System.out.println("\t-r  : see your recommended songs");
            System.out.print("> ");

            input = reader.readLine();

            switch (input) {
                case "-c":
                    Collection.createCollection(reader, conn, this.userID);
                    break;
                case "-v":
                    Collection.viewCollections(conn, this.userID, false);
                    break;
                case "-e":
                    Collection.chooseCollection(reader, conn, this.userID);
                    break;
                case "-s":
                    SongSearch.entry(conn);
                    break;
                case "-p":
                    Played.entry(conn, reader, this.userID);
                    break;
                case "-su":
                    User.SearchUser(conn, reader);
                    break;
                case "-fu":
                    User.FollowUser(conn, reader, this.userID);
                    break;
                case "-uf":
                    User.UnFollowUser(conn, reader, this.userID);
                    break;
                case "-r":
                    Recc.Recc();
                case "-re":
                    SongRec.entry(conn, reader);
                    break;
                case "-q":
                    System.out.println("Logging out");
                    break label;
            }
        }
    }

    private void printNumFollowers(Connection conn, int userID) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select count(f.following) from " +
                "p320_09.follow as f where f.user = " + userID);
        ResultSet rs;
        try{
            rs = stmt.executeQuery();
        }catch(Exception e){
            System.out.println("Could not find followers");
            return;
        }
        while(rs.next()){
            int num = rs.getInt("count");
            System.out.print(num);
        }
    }

    private void printNumFollowing(Connection conn, int userID) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select count(f.user) from " +
                "p320_09.follow as f where f.following = " + userID);
        ResultSet rs;
        try{
            rs = stmt.executeQuery();
        }catch(Exception e){
            System.out.println("Could not find following");
            return;
        }
        while(rs.next()){
            int num = rs.getInt("count");
            System.out.print(num);
        }
    }

    private void printNumCollections(Connection conn, int userID) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select count(c.collectionid) from " +
                "p320_09.collection as c where c.userid = " + userID);
        ResultSet rs;
        try{
            rs = stmt.executeQuery();
        }catch(Exception e){
            System.out.println("Could not find collections");
            e.printStackTrace();
            return;
        }
        while(rs.next()){
            int num = rs.getInt("count");
            System.out.print(num);
        }
    }

    private void printTopArtists(Connection conn, int userID) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select a.artist_name, count(a.artistid)" +
                "from p320_09.user_songs as us, p320_09.song as s, p320_09.artist as a " +
                "where us.songid = s.songid and s.artistid = a.artistid and us.userid = " + userID +
                " group by a.artist_name, a.artistid order by a.artistid desc limit 10");
        ResultSet rs;
        try{
            rs = stmt.executeQuery();
        }catch(Exception e){
            System.out.println("Could not print top artists");
            e.printStackTrace();
            return;
        }
        while(rs.next()){
            String artist_name = rs.getString("artist_name");
            int num = rs.getInt("count");
            System.out.println("\t" + artist_name + ": " + num);
        }
    }
}
