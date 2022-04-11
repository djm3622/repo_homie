package SongRec;

import java.io.BufferedReader;
import java.sql.Array;
import java.sql.Connection;

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
 * query - get user play history
 *      for top 5 of these,get the song's genre
 *          query- for each genre a song != original
 *          add to list
 *
 * similar users -
 * query - get user play history (same query as above dont need to replicate)
 *     for top 5 of these, get the song,
 *         query - a user who also plays this song who is not you
 *                 query - form that user a song that is in their library (plays/collections?) that you dont have
 *         add to list
 */
public class ForYou {

    public static void ForYou(Connection conn, BufferedReader reader, int userID){
        System.out.println("Personalized Recommendations- For You");
        System.out.println("Based on your play history we recommend:");
        String [] songs;
        songs = PlayHistResults();
        for(int i = 0; i < 4; i++)
            System.out.println(songs[i]);

        System.out.println("Based on what similar users are listening to we recommend:");
        songs = SimilarUsrResults();
        for(int i = 0; i < 4; i++)
            System.out.println(songs[i]);
        }

    public static String[] PlayHistResults(){
        String [] songs = new String[5];
        return songs;
    }

    public static String[] SimilarUsrResults(){
        String [] songs = new String[5];
        return songs;
    }
}
