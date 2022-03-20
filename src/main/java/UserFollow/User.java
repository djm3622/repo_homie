package UserFollow;

import Helper.HelperFucntions;
import Home.HomePage;

import java.io.IOException;
import java.sql.Connection;
import java.io.BufferedReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {
    private String username;

    public void User(String username){
        this.username = username;
    }

    public static void SearchUser(Connection conn, BufferedReader reader, String username)
            throws SQLException, IOException {
        HelperFucntions.barCaps("Search a Friend via Email Below");

        ArrayList<String> follows = new ArrayList<>();

        User u = new User();
        String email = "";
        boolean foundOrQuit = false;
        while (!foundOrQuit) {
            System.out.print("Enter email : ");
            email = reader.readLine();
            if (HelperFucntions.isValidEmail(email)){
                PreparedStatement stmt = conn.prepareStatement("SELECT username, email FROM p320_09.user");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String em = rs.getString("email");
                    String us = rs.getString("username");
                    if (email.equals(em)) {
                        System.out.println("User with provided email exists!");
                        System.out.println("You found me \n\t - user: " + em);
                        foundOrQuit = true;
                        break;
                    }
                }
                if(!foundOrQuit){
                    System.out.println("\tWhoops, not valid :(");

                    System.out.println("If you wish to return to main menu enter '-m',or enter to continue:");
                    String tempEnter = reader.readLine();
                    if(tempEnter.equals("-m"))
                        foundOrQuit = true;
                }
            }
        }
    }

    /**
     * cases
     * USERS MUST HAVE LIST OF FOLLOWERS AND FOLLOWING FOR EACH INSTANCE
     * should have access to this.user and other.user
     * display current list of followers
     *          - user requests follow user that does not exist
     *          - user requests follow valid, added as a follower
     *          - user requests follow and already following
     * @param conn
     * @param reader
     */
    public static void FollowUser(Connection conn, BufferedReader reader){
    }

    /**
     *      should have access to this.user and other.user
     *      display current list of following
     * cases -
     *      user requests unfollow but entered username not existing
     *      user requests unfollow but are not a follower of other.user
     *      usr requests unfollow valid, removed as follower.
     * @param conn
     * @param reader
     */
    public static void UnFollowUser(Connection conn, BufferedReader reader){

    }


}
