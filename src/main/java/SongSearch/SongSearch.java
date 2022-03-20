package SongSearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;

public class SongSearch {

    public static void entry(Connection conn) throws IOException {
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

    public static void byName(Connection conn, BufferedReader reader) {

    }

    public static void byArtist(Connection conn, BufferedReader reader) {

    }

    public static void byAlbum(Connection conn, BufferedReader reader) {

    }

    public static void byGenre(Connection conn, BufferedReader reader) {

    }

    public static void generalSort(Connection conn, BufferedReader reader) {
        String t = "SELECT * FROM p320_09.song ORDER BY song.song_name ASC, artist.ar ASC";
    }
}
