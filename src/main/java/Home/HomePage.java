package Home;

import Collection.Collection;
import Listening.Played;
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
        System.out.println("\nWelcome " + first_name + "!");

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
            System.out.println("\t-q : to log out");
            System.out.println("\t-u  : to unfollow a user");
            System.out.println("\t-q  : to log out");
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
                case "-q":
                    System.out.println("Logging out");
                    break label;
            }
        }
    }
}
