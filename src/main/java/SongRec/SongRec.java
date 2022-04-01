package SongRec;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SongRec {
    public static void entry(Connection conn, BufferedReader reader) throws IOException, SQLException {
        String input;

        label:
        while (true) {
            System.out.println("");
            System.out.println("Home Commands as Follows:");
            System.out.println("\t-5m  : top 50 for the month (rolling)");
            System.out.println("\t-5f  : top 50 for friends");
            System.out.println("\t-5g  : top 5 genres");
            System.out.println("\t-q  : to log out");
            System.out.print("> ");

            input = reader.readLine();

            switch (input) {
                case "-5m":
                    topSongsForMonth(conn, reader);
                    break;
                case "-5f":
                    topSongsForFriends(conn, reader);
                    break;
                case "-5g":
                    topGenreForMonth(conn, reader);
                    break;
                case "-q":
                    System.out.println("Logging out");
                    break label;
            }
        }
    }

    public static void topSongsForMonth(Connection conn, BufferedReader reader) throws SQLException {
        PreparedStatement stmt;
        stmt = conn.prepareStatement("SELECT s.song_name, ar.artist_name, s.length, a.title, COUNT(us.user_play) AS user_count " +
                "FROM p320_09.song AS s, p320_09.artist AS ar, p320_09.genre AS g , p320_09.album AS a, p320_09.album_track AS at, p320_09.user_songs AS us " +
                "WHERE s.artistID = ar.artistID " +
                "AND g.genreid = s.genreid " +
                "AND ar.artistID = a.artistID " +
                "AND at.songID = s.songID " +
                "AND at.albumID = a.albumID " +
                "AND us. " +
                "GROUP BY a.title, s.song_name, ar.artist_name, s.length, g.genre_name, s.release_date "+
                "ORDER BY user_count ASC");
    }

    public static void topSongsForFriends(Connection conn, BufferedReader reader) {

    }

    public static void topGenreForMonth(Connection conn, BufferedReader reader) {

    }

}
