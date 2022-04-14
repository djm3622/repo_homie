package Listening;

import Helper.HelperFucntions;
import org.postgresql.util.PSQLException;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

public class Played {

    public static void entry(Connection conn, BufferedReader reader, int userid) throws IOException, SQLException {
        String input;

        label:
        while (true) {
            System.out.println("");
            System.out.println("Playing Commands as Follows:");
            System.out.println("\t-s : play single song");
            System.out.println("\t-c : play a collection of yours");
            System.out.println("\t-q : to quit");
            System.out.print("> ");

            input = reader.readLine();

            switch (input) {
                case "-s":
                    playSong(conn, reader, userid);
                    break;
                case "-c":
                    playCollection(conn, reader, userid);
                    break;
                case "-q":
                    break label;
            }
        }
    }

    public static void playSong(Connection conn, BufferedReader reader, int userid) throws IOException, SQLException {
        int songID;
        int in;

        HelperFucntions.barCaps("song to play");
        System.out.print("> ");
        String input = reader.readLine();

        PreparedStatement stmt = conn.prepareStatement("SELECT s.songid, s.song_name, a.artist_name FROM " +
                "p320_09.song AS s, p320_09.artist AS a WHERE s.artistid = a.artistid AND s.song_name = '" + input +"'");

        ResultSet rs;
        rs = stmt.executeQuery();

        if (rs.next()) {
            HelperFucntions.barCaps("enter id of song to add");
            songID = rs.getInt("songid");
            String song_name = rs.getString("song_name");
            String artist_name = rs.getString("artist_name");
            System.out.println("\tid: " + songID + ", " + song_name + " by " + artist_name);

            while (rs.next()) {
                songID = rs.getInt("songid");
                song_name = rs.getString("song_name");
                artist_name = rs.getString("artist_name");
                System.out.println("\tid: " + songID + ", " + song_name + " by " + artist_name);
            }

            System.out.print("> ");
            String str = reader.readLine();
            in = Integer.parseInt(str);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssX");
            Date date = new Date(System.currentTimeMillis());

            stmt = conn.prepareStatement("INSERT INTO p320_09.user_songs (userid, songid, user_play) " +
                    "VALUES (" + userid + ", " + in + ", '" + formatter.format(date) + "')");

            try {
                stmt.executeQuery();
            } catch (PSQLException e) {
                System.out.println("\tSong played.");
            }
        } else {
            System.out.println("\tSong does not exist.");
        }
    }

    public static void playCollection(Connection conn, BufferedReader reader, int userid) throws IOException, SQLException {
        int songID;
        int in;

        HelperFucntions.barCaps("collection to play");
        System.out.print("> ");
        String input = reader.readLine();

        PreparedStatement stmt = conn.prepareStatement("SELECT c.collectionid, c.name FROM " +
                "p320_09.collection AS c WHERE c.name = '" + input +"'");
        ResultSet rs;
        rs = stmt.executeQuery();
        if (rs.next()) {
            HelperFucntions.barCaps("enter id of collection to play");

            String id = rs.getString("collectionid");
            System.out.println("\tid: " + id + ", " + input);

            while (rs.next()) {
                id = rs.getString("collectionid");
                System.out.println("\tid: " + id + ", " + input);
            }

            System.out.print("> ");
            String str = reader.readLine();
            in = Integer.parseInt(str);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssX");
            Date date = new Date(System.currentTimeMillis());

            stmt = conn.prepareStatement("SELECT ct.songid FROM " +
                    "p320_09.collection_track AS ct WHERE ct.collectionid = '" + in + "'");
            rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getString("songid");
                stmt = conn.prepareStatement("INSERT INTO p320_09.user_songs (userid, songid, user_play) " +
                        "VALUES (" + userid + ", " + id + ", '" + formatter.format(date) + "')");
                try {
                    stmt.executeQuery();
                } catch (PSQLException ignored) {}
                while (rs.next()) {
                    id = rs.getString("songid");
                    stmt = conn.prepareStatement("INSERT INTO p320_09.user_songs (userid, songid, user_play) " +
                            "VALUES (" + userid + ", " + id + ", '" + formatter.format(date) + "')");
                    try {
                        stmt.executeQuery();
                    } catch (PSQLException ignored) {}
                }
                System.out.println("\tCollections played.");
            } else {
                System.out.println("\tCollections is empty.");
            }
        } else {
            System.out.println("\tCollections does not exist.");
        }
    }
}
