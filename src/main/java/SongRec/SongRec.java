package SongRec;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                    topSongsForMonth(conn);
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

    public static void topSongsForMonth(Connection conn) throws SQLException {
        PreparedStatement stmt;

        stmt = conn.prepareStatement("SELECT s.song_name, COUNT(s.songid) AS count " +
                "FROM p320_09.user_songs AS us, p320_09.song AS s " +
                "WHERE us.songid = s.songid " +
                "GROUP BY s.song_name, s.songid " +
                "ORDER BY s.songid DESC " +
                "LIMIT 10");

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String snm = rs.getString("song_name");
            String count = rs.getString("count");
            System.out.println("\t(" + snm + ") played " + count + " times");
        }
    }

    public static void topSongsForFriends(Connection conn, BufferedReader reader) {

    }

    public static void topGenreForMonth(Connection conn, BufferedReader reader) {

    }

}
