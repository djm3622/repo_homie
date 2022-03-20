package Listening;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;

public class Played {

    public static void entry(Connection conn, BufferedReader reader, int userid) throws IOException {
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
                case "-p":
                    playSong(conn, reader);
                    break;
                case "-c":

                    break;
                case "-q":
                    break label;
            }
        }
    }

    public static void playSong(Connection conn, BufferedReader reader) {

    }

    public static void playCollection(Connection conn, BufferedReader reader, int userid) {

    }
}
