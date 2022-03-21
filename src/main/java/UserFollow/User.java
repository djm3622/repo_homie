package UserFollow;

import Helper.HelperFucntions;
import Home.HomePage;

import java.io.IOException;
import java.sql.*;
import java.io.BufferedReader;
import java.util.ArrayList;

/**
 *   Users can follow a friend. Users can search for new friends by email
 * • The application must also allow an user to unfollow a friend
 */
public class User {
    private String username;
    private int uID;
    private ArrayList<Integer> followers = new ArrayList<>();
    private ArrayList<Integer> following = new ArrayList<>();

    //prob delete later
    public void User(Connection conn, String username, int uID, ArrayList followers,
                     ArrayList following) throws SQLException {
        this.username = username;
        this.uID = uID;
        this.followers = followers;
        this.following = following;
    }

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
     * display current list of followers
     *          - user requests follow user that does not exist
     *          - user requests follow valid, added as a follower
     *          - user requests follow and already following
     * @param conn
     * @param reader
*/
    public static void FollowUser(Connection conn, BufferedReader reader, int currID
    ) throws SQLException, IOException {

        //get query to return a list of followers and following for the given userID (current user)
        PreparedStatement stmt1 = conn.prepareStatement("SELECT f.follower, f.following " +
                "FROM p320_09.follow AS f, p320_09.user AS U WHERE f.follower = u.userID " +
                "AND u.userID = ?");
        ResultSet rs1 = stmt1.executeQuery("currID");
        ArrayList<Integer> currUserfollowers = new ArrayList<>();  //these are of user_id's
        ArrayList<Integer> currUserfollowing = new ArrayList<>();
        while (rs1.next()) {
            int follower = rs1.getInt("follower");
            currUserfollowers.add(follower);
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
                    System.out.println("this user exists!");   //leave for testing purposes
                    found = true;
                    otherID = uid;
                    break;
                }
            }

            if(!found){
                System.out.println("this user does not exist");
                break;
            }

            //get query to return a list of followers and following for the given otherID (other user)
            PreparedStatement stmt3 = conn.prepareStatement("SELECT f.follower, f.following " +
                    "FROM p320_09.follow AS f, p320_09.user AS U WHERE f.follower = u.userID " +
                    "AND u.userID = ?");
            ResultSet rs3 = stmt3.executeQuery("otherID");

            ArrayList<Integer> otherUserfollowers = new ArrayList<>();  //these are of user_id's
            ArrayList<Integer> otherUserfollowing = new ArrayList<>();
            while (rs1.next()) {
                int follower = rs1.getInt("follower");
                otherUserfollowers.add(follower);
                int following = rs1.getInt("following");
                otherUserfollowing.add(following);
            }


            if(otherUserfollowers.contains(currID)){
                System.out.println("you are already following this user");
                break;
            }
            else{
                otherUserfollowers.add(currID); //added to their followers
                currUserfollowing.add(otherID);  //added to ur following
                //INSERT SQL COMMAND TO ADD these TO DB
                /*
                PreparedStatement stmt4 = conn.prepareStatement("INSERT INTO p320_09.follow " +
                        "(follower, following)\n" +
                        "VALUES (?, ?);");
                ResultSet rs4 = stmt3.executeQuery("currID", "otherID");
                */


                System.out.println("you are now following this user!");
                System.out.println("People you are currently following: ");
                for(int f : currUserfollowing)
                    System.out.print(f + ", ");
            }
        }
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
        //get query to return a list of followers and following for the given userID (current user)
        PreparedStatement stmt1 = conn.prepareStatement("SELECT f.follower, f.following " +
                "FROM p320_09.follow AS f, p320_09.user AS U WHERE f.follower = u.userID " +
                "AND u.userID = ?");
        ResultSet rs1 = stmt1.executeQuery("currID");
        ArrayList<Integer> currUserfollowers = new ArrayList<>();  //these are of user_id's
        ArrayList<Integer> currUserfollowing = new ArrayList<>();
        while (rs1.next()) {
            int follower = rs1.getInt("follower");
            currUserfollowers.add(follower);
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
                    System.out.println("this user exists!");   //leave for testing purposes
                    found = true;
                    otherID = uid;
                    break;
                }
            }
            if(!found){
                System.out.println("this user does not exist");
                break;
            }
            PreparedStatement stmt3 = conn.prepareStatement("SELECT f.follower, f.following " +
                    "FROM p320_09.follow AS f, p320_09.user AS U WHERE f.follower = u.userID " +
                    "AND u.userID = ?");
            ResultSet rs3 = stmt3.executeQuery("otherID");

            ArrayList<Integer> otherUserfollowers = new ArrayList<>();  //these are of user_id's
            ArrayList<Integer> otherUserfollowing = new ArrayList<>();
            while (rs1.next()) {
                int follower = rs1.getInt("follower");
                otherUserfollowers.add(follower);
                int following = rs1.getInt("following");
                otherUserfollowing.add(following);
            }

            if(!otherUserfollowers.contains(currID)){
                System.out.println("you are already following this user");
                break;
            }
            else{
                otherUserfollowers.remove(currID); //added to their followers
                currUserfollowing.remove(otherID);  //added to ur following
                //INSERT SQL COMMAND TO ADD these TO DB
                System.out.println("you are now following this user!");
                System.out.println("People you are currently following: ");
                for(int f : currUserfollowing)
                    System.out.print(f + ", ");
            }
        }
    }


}
