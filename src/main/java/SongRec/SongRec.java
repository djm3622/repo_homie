package SongRec;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SongRec {
    public static void entry(Connection conn, BufferedReader reader, int userid) throws IOException, SQLException {
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
                    topSongsForMonth(conn);
                    break;
                case "-5f":
                    topSongsForFriends(conn, userid);
                    break;
                case "-5g":
                    topGenreForMonth(conn);
                    break;
                case "-q":
                    System.out.println("Logging out");
                    break label;
            }
        }
    }

    public static void topSongsForMonth(Connection conn) throws SQLException {
        PreparedStatement stmt;

        stmt = conn.prepareStatement("SELECT s.song_name, COUNT(us.songid) AS count " +
                "FROM p320_09.user_songs AS us, p320_09.song AS s " +
                "WHERE us.songid = s.songid " +
                "AND extract(month from us.user_play) >= extract(month from current_date - 1) "+
                "GROUP BY s.song_name, s.songid " +
                "ORDER BY count DESC " +
                "LIMIT 50");

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String snm = rs.getString("song_name");
            String count = rs.getString("count");
            System.out.println("\t(" + snm + ") played " + count + " times");
        }
    }

    public static void topSongsForFriends(Connection conn, int userid) throws SQLException {
        PreparedStatement stmt;

        stmt = conn.prepareStatement("SELECT s.song_name, COUNT(us.user_play) AS us_count " +
                "FROM p320_09.song AS s, p320_09.user_songs AS us, p320_09.follow AS f " +
                "WHERE s.songID = us.songID " +
                "AND f.user = " + userid + " " +
                "AND f.following = us.userID " +
                "GROUP BY s.song_name " +
                "ORDER BY us_count DESC " +
                "LIMIT 50;");

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String snm = rs.getString("song_name");
            String count = rs.getString("us_count");
            System.out.println("\t(" + snm + ") played " + count + " times");
        }
    }

    public static void topGenreForMonth(Connection conn) throws SQLException {
        PreparedStatement stmt;

        stmt = conn.prepareStatement("SELECT g.genre_name, COUNT(g.genreID) AS g_count "+
                "FROM p320_09.user_songs AS us, p320_09.song AS s, p320_09.genre AS g " +
                "WHERE us.songID = s.songID "+
                "AND s.genreID = g.genreID "+
                "AND extract(month from us.user_play) >= extract(month from current_date - 1) "+
                "GROUP BY g.genre_name, g.genreID "+
                "ORDER BY g_count DESC "+
                "LIMIT 5");

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String snm = rs.getString("genre_name");
            String count = rs.getString("g_count");
            System.out.println("\t(" + snm + ") played " + count + " times");
        }
    }

}
