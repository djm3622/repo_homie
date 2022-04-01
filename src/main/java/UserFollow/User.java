package UserFollow;

import Helper.HelperFucntions;
import Home.HomePage;
import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.sql.*;
import java.io.BufferedReader;
import java.util.ArrayList;

/**
 *   Users can follow a friend. Users can search for new friends by email
 * • The application must also allow an user to unfollow a friend
 */
public class User {

    public static void SearchUser(Connection conn, BufferedReader reader)
            throws SQLException, IOException {
        HelperFucntions.barCaps("Search a Friend via Email Below");
        String email;
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
                        System.out.println("You found me \n\t - user: " + us);
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
     * display current list of followers
     *          - user requests follow user that does not exist
     *          - user requests follow valid, added as a follower
     *          - user requests follow and already following
     * @param conn
     * @param reader
*/
    public static void FollowUser(Connection conn, BufferedReader reader, int currID
    ) throws SQLException, IOException {
        PreparedStatement stmt1 = conn.prepareStatement("SELECT f.user, f.following " +
                "FROM p320_09.follow AS f, p320_09.user AS U WHERE f.user = u.userID " +
                "AND u.userID = " + currID);
        ResultSet rs1 = stmt1.executeQuery();
        ArrayList<Integer> currUserfollowing = new ArrayList<>();
        while (rs1.next()) {
            int following = rs1.getInt("following");
            currUserfollowing.add(following);
        }
        int otherID =0;
        HelperFucntions.barCaps("Follow a Friend");
        String otherUs;
        boolean doneOrQuit = false;
        while (!doneOrQuit) {
            System.out.print("Enter username you wish to follow : ");
            otherUs = reader.readLine();
            PreparedStatement stmt2 = conn.prepareStatement("SELECT username, userid FROM p320_09.user");
            ResultSet rs2 = stmt2.executeQuery();
            boolean found = false;
            while (rs2.next()) {
                String un = rs2.getString("username");
                int uid = rs2.getInt("userid");
                if (otherUs.equals(un)) {
                    found = true;
                    otherID = uid;
                    break;
                }
            }
            if(!found){
                System.out.println("this user does not exist");
                break;
            }
            if(currUserfollowing.contains(otherID)){
                System.out.println("you are already following this user");
                break;
            }
            else{
                currUserfollowing.add(otherID);  //added to ur following
                PreparedStatement stmt4 = conn.prepareStatement("INSERT INTO p320_09.follow " +
                        "(\"user\", \"following\") VALUES ( " + currID + ", " + otherID + ")" );
              try {
                    stmt4.executeQuery();
                } catch (PSQLException e) {
                  System.out.println("you are now following this user!");
                  System.out.println("People you are currently following: ");
                  System.out.println(currFollowing(conn, currID));
                }
            }
            System.out.println("if you want to follow another user press y, or any key to return");
            if(!reader.readLine().equals("y")){
                doneOrQuit = true;
            }

        }
    }
    /**
     * Returns a list of usernames you are currently following
     */
    public static ArrayList<String> currFollowing(Connection conn, int currID) throws SQLException {
        ArrayList<String> usrNames = new ArrayList<>();

        PreparedStatement stmt1 = conn.prepareStatement("SELECT f.user, f.following " +
                "FROM p320_09.follow AS f, p320_09.user AS U WHERE f.user = u.userID " +
                "AND u.userID = " + currID);
        ResultSet rs1 = stmt1.executeQuery();
        ArrayList<Integer> usrIds = new ArrayList<>();
        while (rs1.next()) {
            int following = rs1.getInt("following");
            usrIds.add(following);
        }
        for(int i = 0; i<usrIds.size(); i++) {
            PreparedStatement stmt2 = conn.prepareStatement("SELECT username, userid FROM p320_09.user");
            ResultSet rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                String un = rs2.getString("username");
                int uid = rs2.getInt("userid");
                if (uid == usrIds.get(i))
                    usrNames.add(un);
            }
        }
         return usrNames;
    }


    /**
     *      display current list of following too
     * cases -
     *      user requests unfollow but entered username not existing
     *      user requests unfollow but are not a follower of other.user
     *      usr requests unfollow valid, removed as follower.
     * @param conn
     * @param reader
     */

    public static void UnFollowUser(Connection conn, BufferedReader reader, int currID)
            throws SQLException, IOException {
        PreparedStatement stmt1 = conn.prepareStatement("SELECT f.user, f.following " +
                "FROM p320_09.follow AS f, p320_09.user AS U WHERE f.user = u.userID " +
                "AND u.userID = " + currID);
        ResultSet rs1 = stmt1.executeQuery();
        ArrayList<Integer> currUserfollowing = new ArrayList<>();
        while (rs1.next()) {
            int following = rs1.getInt("following");
            currUserfollowing.add(following);
        }
        int otherID = 0;
        HelperFucntions.barCaps("Unfollow a Friend");
        String otherUs;
        boolean doneOrQuit = false;
        while (!doneOrQuit) {
            System.out.print("Enter username you wish to unfollow : ");
            otherUs = reader.readLine();
            PreparedStatement stmt2 = conn.prepareStatement("SELECT username, userid FROM p320_09.user");
            ResultSet rs2 = stmt2.executeQuery();
            boolean found = false;
            while (rs2.next()) {
                String un = rs2.getString("username");
                int uid = rs2.getInt("userid");
                if (otherUs.equals(un)) {
                    found = true;
                    otherID = uid;
                    break;
                }
            }
            if(!found){
                System.out.println("this user does not exist");
                break;
            }
            if(!currUserfollowing.contains(otherID)){
                System.out.println("you are not following this user to begin with");
                break;
            }
            else{
                currUserfollowing.remove(otherID);
                PreparedStatement stmt4 = conn.prepareStatement("DELETE FROM p320_09.follow " +
                        "WHERE \"user\" = " + currID + " AND  \"following\" = " + otherID);
                try {
                    stmt4.executeQuery();
                } catch (PSQLException e) {
                    System.out.println("you have now unfollowed this user!");
                    System.out.println("People you are currently following: ");
                    System.out.println(currFollowing(conn, currID));
                }
            }
            System.out.println("if you want to unfollow another user press y, or any key to return");
            if(!reader.readLine().equals("y")){
                doneOrQuit = true;
            }
        }
    }
}
