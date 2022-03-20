package SongSearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SongSearch {

    public static void entry(Connection conn) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );
        String input;

        label:
        while (true) {
            System.out.println("");
            System.out.println("Searching Commands as Follows:");
            System.out.println("\t-nm : by name");
            System.out.println("\t-an : by artist");
            System.out.println("\t-ab : by album");
            System.out.println("\t-gr : by album");
            System.out.println("\t-q : to quit");
            System.out.print("> ");

            input = reader.readLine();

            switch (input) {
                case "-nm":
                    byName(conn, reader);
                    break;
                case "-an":
                    byArtist(conn, reader);
                    break;
                case "-ab":
                    byAlbum(conn, reader);
                    break;
                case "-gr":
                    byGenre(conn, reader);
                    break;
                case "-q":
                    break label;
            }
        }

    }

    public static void byName(Connection conn, BufferedReader reader) throws IOException, SQLException {
        PreparedStatement stmt;
        ResultSet rs;

        System.out.print("Song Name: ");
        String input = reader.readLine();

        stmt = conn.prepareStatement("SELECT s.song_name, a.artist_name " +
                "FROM p320_09.song AS s, p320_09.artist AS a " +
                "WHERE s.artistID = a.artistID AND s.song_name LIKE '%" + input + "%'" +
                "ORDER BY s.song_name ASC, a.artist_name ASC");
        rs = stmt.executeQuery();
        while (rs.next()) {
            String sn = rs.getString("song_name");
            String an = rs.getString("artist_name");

            System.out.println("\t-(" + sn + ") by (" + an + ")");
        }
    }

    public static void byArtist(Connection conn, BufferedReader reader) throws IOException, SQLException {
        PreparedStatement stmt;
        ResultSet rs;

        System.out.print("Artist Name: ");
        String input = reader.readLine();

        stmt = conn.prepareStatement("SELECT s.song_name, a.artist_name " +
                "FROM p320_09.song AS s, p320_09.artist AS a " +
                "WHERE s.artistID = a.artistID AND a.artist LIKE '%" + input + "%'" +
                "ORDER BY s.song_name ASC, a.artist_name ASC");
        rs = stmt.executeQuery();
        while (rs.next()) {
            String sn = rs.getString("song_name");
            String an = rs.getString("artist_name");

            System.out.println("\t-(" + sn + ") by (" + an + ")");
        }

    }

    public static void byAlbum(Connection conn, BufferedReader reader) throws IOException, SQLException {
        PreparedStatement stmt;
        ResultSet rs;

        System.out.print("Album Name: ");
        String input = reader.readLine();

        stmt = conn.prepareStatement("SELECT s.song_name, a.artist_name, b.album_name, b.albumid" +
                "FROM p320_09.song AS s, p320_09.artist AS a, p320_09.album AS b" +
                "WHERE s.artistID = a.artistID AND b.album LIKE '%" + input + "%'" +
                "ORDER BY s.song_name ASC, a.artist_name ASC");
        rs = stmt.executeQuery();
        while (rs.next()) {
            String sn = rs.getString("song_name");
            String an = rs.getString("artist_name");

            System.out.println("\t-(" + sn + ") by (" + an + ")");
        }

    }

    public static void byGenre(Connection conn, BufferedReader reader) throws IOException, SQLException {
        PreparedStatement stmt;
        ResultSet rs;

        System.out.print("Album Name: ");
        String input = reader.readLine();

        stmt = conn.prepareStatement("SELECT s.song_name, a.artist_name, b.album_name, b.albumid" +
                "FROM p320_09.song AS s, p320_09.artist AS a, p320_09.album AS b" +
                "WHERE s.artistID = a.artistID AND b.album LIKE '%" + input + "%'" +
                "ORDER BY s.song_name ASC, a.artist_name ASC");
        rs = stmt.executeQuery();
        while (rs.next()) {
            String sn = rs.getString("song_name");
            String an = rs.getString("artist_name");

            System.out.println("\t-(" + sn + ") by (" + an + ")");
        }
    }

    public static void generalSort(Connection conn, BufferedReader reader) {
        String t = "SELECT s.song_name, a.artist_name " +
                "FROM p320_09.song AS s, p320_09.artist AS a " +
                "WHERE s.artistID = a.artistID LIKE '%search%'" +
                "ORDER BY s.song_name ASC, a.artist_name ASC";
    }
}
