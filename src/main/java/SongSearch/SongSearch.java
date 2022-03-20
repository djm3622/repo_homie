package SongSearch;

import Helper.HelperFucntions;

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
            System.out.println("\t-gr : by genre");
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

        stmt = conn.prepareStatement("SELECT s.song_name, ar.artist_name, s.length, a.title " +
                "FROM p320_09.song AS s, p320_09.artist AS ar, p320_09.genre AS g , p320_09.album AS a, p320_09.album_track AS at " +
                "WHERE s.artistID = ar.artistID " +
                "AND g.genreid = s.genreid " +
                "AND ar.artistID = a.artistID " +
                "AND at.songID = s.songID " +
                "AND at.albumID = a.albumID " +
                "AND s.song_name LIKE '%" + input + "%'" +
                "ORDER BY s.song_name ASC, ar.artist_name ASC");
        rs = stmt.executeQuery();
        HelperFucntions.printStuff(rs);

        String s;

        while (true) {
            int by = HelperFucntions.sortByStats(reader);
            s = HelperFucntions.unpackStats(by);

            if (s.equals(" ")) {
                break;
            }

            stmt = conn.prepareStatement("SELECT s.song_name, ar.artist_name, s.length, a.title " +
                    "FROM p320_09.song AS s, p320_09.artist AS ar, p320_09.genre AS g , p320_09.album AS a, p320_09.album_track AS at " +
                    "WHERE s.artistID = ar.artistID " +
                    "AND g.genreid = s.genreid " +
                    "AND ar.artistID = a.artistID " +
                    "AND at.songID = s.songID " +
                    "AND at.albumID = a.albumID " +
                    "AND s.song_name LIKE '%" + input + "%'" +s);
            rs = stmt.executeQuery();

            HelperFucntions.printStuff(rs);
        }
    }

    public static void byArtist(Connection conn, BufferedReader reader) throws IOException, SQLException {
        PreparedStatement stmt;
        ResultSet rs;

        System.out.print("Artist Name: ");
        String input = reader.readLine();

        stmt = conn.prepareStatement("SELECT s.song_name, ar.artist_name, s.length, a.title " +
                "FROM p320_09.song AS s, p320_09.artist AS ar, p320_09.genre AS g , p320_09.album AS a, p320_09.album_track AS at " +
                "WHERE s.artistID = ar.artistID " +
                "AND g.genreid = s.genreid " +
                "AND ar.artistID = a.artistID " +
                "AND at.songID = s.songID " +
                "AND at.albumID = a.albumID " +
                "AND ar.artist_name LIKE '%" + input + "%'" +
                "ORDER BY s.song_name ASC, ar.artist_name ASC");
        rs = stmt.executeQuery();
        HelperFucntions.printStuff(rs);

        String s;

        while (true) {
            int by = HelperFucntions.sortByStats(reader);
            s = HelperFucntions.unpackStats(by);

            if (s.equals(" ")) {
                break;
            }

            stmt = conn.prepareStatement("SELECT s.song_name, ar.artist_name, s.length, a.title " +
                    "FROM p320_09.song AS s, p320_09.artist AS ar, p320_09.genre AS g , p320_09.album AS a, p320_09.album_track AS at " +
                    "WHERE s.artistID = ar.artistID " +
                    "AND g.genreid = s.genreid " +
                    "AND ar.artistID = a.artistID " +
                    "AND at.songID = s.songID " +
                    "AND at.albumID = a.albumID " +
                    "AND ar.artist_name LIKE '%" + input + "%'" + s);
            rs = stmt.executeQuery();

            HelperFucntions.printStuff(rs);
        }
    }



    public static void byAlbum(Connection conn, BufferedReader reader) throws IOException, SQLException {
        PreparedStatement stmt;
        ResultSet rs;

        System.out.print("Album Name: ");
        String input = reader.readLine();

        stmt = conn.prepareStatement("SELECT s.song_name, ar.artist_name, s.length, a.title " +
                "FROM p320_09.song AS s, p320_09.artist AS ar, p320_09.genre AS g , p320_09.album AS a, p320_09.album_track AS at " +
                "WHERE s.artistID = ar.artistID " +
                "AND g.genreid = s.genreid " +
                "AND ar.artistID = a.artistID " +
                "AND at.songID = s.songID " +
                "AND at.albumID = a.albumID " +
                "AND a.title LIKE '%" + input + "%'"+
                "ORDER BY s.song_name ASC, ar.artist_name ASC");
        rs = stmt.executeQuery();
        HelperFucntions.printStuff(rs);

        String s;

        while (true) {
            int by = HelperFucntions.sortByStats(reader);
            s = HelperFucntions.unpackStats(by);

            if (s.equals(" ")) {
                break;
            }

            stmt = conn.prepareStatement("SELECT s.song_name, ar.artist_name, s.length, a.title " +
                    "FROM p320_09.song AS s, p320_09.artist AS ar, p320_09.genre AS g , p320_09.album AS a, p320_09.album_track AS at " +
                    "WHERE s.artistID = ar.artistID " +
                    "AND g.genreid = s.genreid " +
                    "AND ar.artistID = a.artistID " +
                    "AND at.songID = s.songID " +
                    "AND at.albumID = a.albumID " +
                    "AND a.title LIKE '%" + input + "%'" + s);
            rs = stmt.executeQuery();

            HelperFucntions.printStuff(rs);
        }
    }

    public static void byGenre(Connection conn, BufferedReader reader) throws IOException, SQLException {
        PreparedStatement stmt;
        ResultSet rs;

        System.out.print("Genre Name: ");
        String input = reader.readLine();

        stmt = conn.prepareStatement("SELECT s.song_name, ar.artist_name, s.length, a.title " +
                "FROM p320_09.song AS s, p320_09.artist AS ar, p320_09.genre AS g , p320_09.album AS a, p320_09.album_track AS at " +
                "WHERE s.artistID = ar.artistID " +
                "AND g.genreid = s.genreid " +
                "AND ar.artistID = a.artistID " +
                "AND at.songID = s.songID " +
                "AND at.albumID = a.albumID " +
                "AND g.genre_name LIKE '%" + input + "%'"+
                "ORDER BY s.song_name ASC, ar.artist_name ASC");
        rs = stmt.executeQuery();
        HelperFucntions.printStuff(rs);

        String s;

        while (true) {
            int by = HelperFucntions.sortByStats(reader);
            s = HelperFucntions.unpackStats(by);

            if (s.equals(" ")) {
                break;
            }

            stmt = conn.prepareStatement("SELECT s.song_name, ar.artist_name, s.length, a.title " +
                    "FROM p320_09.song AS s, p320_09.artist AS ar, p320_09.genre AS g, p320_09.album AS a, p320_09.album_track AS at " +
                    "WHERE s.artistID = ar.artistID " +
                    "AND g.genreid = s.genreid " +
                    "AND ar.artistID = a.artistID " +
                    "AND at.songID = s.songID " +
                    "AND at.albumID = a.albumID " +
                    "AND g.genre_name LIKE '%" + input + "%'"+s);
            rs = stmt.executeQuery();

            HelperFucntions.printStuff(rs);
        }
    }
}
