package SongRec;

import java.io.BufferedReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * recommendation via similar user: a similar user is one who listens to the same songs,
 * recommend songs that that user listens to, that you don't
 *
 * go to a song you have played/have in collections, in that song you can see who else has played
 * that song (this will be your similr user), now you have their info, filter songs that they have that you
 * dont and reccomend these.
 *
 * For you: Recommend songs to listen to based on your play history (e.g. genre,
 * artist) and the play history of similar users
 *
 * 5 songs from play history + 5 songs from similar users
 * play history:
 * query - get user's play history
 *      for top 5 of these,get the song's genre
 *          query- for each genre get a song that != original
 *          add to list
 *
 * similar users -
 * query - get user play history (same query as above dont need to replicate)
 *     for top 5 of these, get the song,
 *         query - given a song, a user who also plays this song who is not you
 *                 query - form that user a song that is in their library (plays/collections?) that you dont have
 *         add to list
 *
 *  queries:
 *  1. given a userID return that user's play history of songs (songID's)
 *  2. given a song, return a song that is of the same genre but != original
 *
 *  3. given a song and userID, return a userID who also has this song in their play history
 *  4. given a 2 userID's (a and b), return a song that is in b's library(their collections I presume), that a
 *          doesn't have in their play history
 */
public class ForYou {

    public static void ForYou(Connection conn, BufferedReader reader, int userID) throws SQLException {
        System.out.println("Personalized Recommendations - For You\n");
        System.out.println("Based on your play history we recommend:");
        String [] songs;
        songs = PlayHistResults(conn, userID);
        for(int i = 0; i < 5; i++)
            System.out.println("\t" + songs[i]);

        System.out.println("Based on what similar users are listening to we recommend:");
        songs = SimilarUsrResults(conn, userID);
        for(int i = 0; i < 5; i++)
            System.out.println("\t" + songs[i]);
        }


    public static String[] PlayHistResults(Connection conn, int currID) throws SQLException {
        ArrayList<String> temp = new ArrayList<>();
        String [] Usrsongs = new String[5];
        //get play history of current user
        PreparedStatement stmt1 = conn.prepareStatement("SELECT DISTINCT us.songID " +
                "FROM p320_09.user_songs AS us " +
                "WHERE us.userID = " + currID);

        ResultSet rs1 = stmt1.executeQuery();
        while (rs1.next()) {
            temp.add(rs1.getString("songid"));
        }
        for(int i =0; i < Usrsongs.length; i++){ //first 5 songs in play curr history
            Usrsongs[i] = temp.get(i);
            //System.out.println(Usrsongs[i]);
        }
        temp.clear();
        ArrayList<String> gotSongs = new ArrayList<>();
        for(int i = 0; i < Usrsongs.length; i ++) {
            PreparedStatement stmt2 = conn.prepareStatement("SELECT s.song_name FROM p320_09.song AS s " +
                    "WHERE s.genreID IN (SELECT DISTINCT s.genreID FROM p320_09.song AS s " +
                    "WHERE s.songID= " + Usrsongs[i] + ") AND s.songID != " + Usrsongs[i]);
            ResultSet rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                String t = rs2.getString("song_name");
                temp.add(t);//(rs2.getString("song_name"));
            }
        }
        Collections.shuffle(temp);   //randomize all songs in temp
        for(int i =0; i < Usrsongs.length; i++)
        {
            Usrsongs[i] = temp.get(i);
        }

        return Usrsongs;
    }

    public static String[] SimilarUsrResults(Connection conn, int currID) throws SQLException{
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<Integer> tempInt = new ArrayList<>();
        String [] Usrsongs = new String[5];
        Integer [] userSongsInt = new Integer[5];
        //get play history of current user
        PreparedStatement stmt1 = conn.prepareStatement("SELECT DISTINCT us.songID " +
                "FROM p320_09.user_songs AS us " +
                "WHERE us.userID = " + currID);

        ResultSet rs1 = stmt1.executeQuery();
        while (rs1.next()) {
            temp.add(rs1.getString("songid"));
        }
        for(int i =0; i < Usrsongs.length; i++){ //first 5 songs in play curr history
            Usrsongs[i] = temp.get(i);
        }
        temp.clear();
        ArrayList<String> gotSongs = new ArrayList<>();

        //gives list of all followers who listen to same songs
        for(int i = 0; i < Usrsongs.length; i ++) {
            PreparedStatement stmt2 = conn.prepareStatement(" SELECT f.following " +
                    "FROM p320_09.follow AS f, p320_09.user_songs AS us " +
                    "WHERE us.songID = " + Usrsongs[i] + "AND f.user = " + currID +
                    " AND f.user = us.userID LIMIT 1");
            ResultSet rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                temp.add(rs2.getString("following"));
            }
        }
        //gives all the songs similar usrs listen to not belonging in curr's plays
        for(int i = 0; i < Usrsongs.length; i ++) {
            PreparedStatement stmt2 = conn.prepareStatement("SELECT DISTINCT us.songID " +
                    "FROM p320_09.user_songs AS us WHERE us.userID = " + temp.get(i) +
                    " EXCEPT SELECT DISTINCT us.songID FROM p320_09.user_songs AS us " +
                    "WHERE us.userID = " + currID);
            tempInt.clear();
            ResultSet rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                tempInt.add(rs2.getInt("songid"));
            }
        }
        Collections.shuffle(tempInt);   //randomize all songs in temp
        for(int i =0; i < userSongsInt.length; i++)
        {
            userSongsInt[i] = tempInt.get(i);
        }

        // finds the song_name for each of the sondIDs
        for(int i = 0; i < userSongsInt.length; i++) {
            PreparedStatement stmt3 = conn.prepareStatement("select song_name from p320_09.song " +
                    "where songid = " + userSongsInt[i]);
            ResultSet rs3 = stmt3.executeQuery();
            while(rs3.next()){
                Usrsongs[i] = rs3.getString("song_name");
            }
        }

        return Usrsongs;

    }
}
