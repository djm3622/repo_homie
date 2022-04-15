package CSVstuff;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataFromSCV {
    public static void initIn(Connection conn) throws SQLException, IOException {
        String[] cols = new String[2];
        cols[0] = "user_creation_date";
        cols[1] = "date_freq";

        String line = "\n";
        String splitChar = ",";

        // header

        File file = new File("data1-1.csv");
        FileWriter writeCSV = new FileWriter("data1-1.csv");
        doStuffOne(cols, splitChar, line, writeCSV);

        // data

        PreparedStatement stmt;
        ResultSet rs;

        stmt = conn.prepareStatement("SELECT DISTINCT to_char(u.creation_date, 'YYYY-MM') AS user_creation_date, COUNT(to_char(u.creation_date, 'YYYY-MM')) AS date_freq\n" +
                "FROM p320_09.user AS u\n" +
                "GROUP BY user_creation_date\n" +
                "ORDER BY date_freq DESC;");
        rs = stmt.executeQuery();

        doStuffTwo(cols, splitChar, line, writeCSV, rs);
    }

    public static void initInCont(Connection conn) throws SQLException, IOException {
        String[] cols = new String[2];
        cols[0] = "title";
        cols[1] = "artist_name";

        String line = "\n";
        String splitChar = ",";

        // header

        File file = new File("data1-2.csv");
        FileWriter writeCSV = new FileWriter("data1-2.csv");
        doStuffOne(cols, splitChar, line, writeCSV);

        // data

        PreparedStatement stmt;
        ResultSet rs;

        String date = "2022-03";

        stmt = conn.prepareStatement("SELECT a.title, ar.artist_name\n" +
                "FROM p320_09.album AS a, p320_09.artist AS ar\n" +
                "WHERE a.artistID = ar.artistID\n" +
                "AND to_char(a.release_date, 'YYYY-MM') = '" + date + "'\n");
        rs = stmt.executeQuery();

        doStuffTwo(cols, splitChar, line, writeCSV, rs);
    }

    public static void initTwo(Connection conn) throws SQLException, IOException {
        String[] cols = new String[2];
        cols[0] = "decade";
        cols[1] = "popularity";

        String line = "\n";
        String splitChar = ",";

        // header

        File file = new File("data2-1.csv");
        FileWriter writeCSV = new FileWriter("data2-1.csv");
        doStuffOne(cols, splitChar, line, writeCSV);

        // data

        PreparedStatement stmt;
        ResultSet rs;

        stmt = conn.prepareStatement("SELECT DISTINCT EXTRACT(YEAR FROM s.release_date) AS decade, COUNT(us.time_play) AS popularity\n" +
                "FROM p320_09.song AS s, p320_09.user_songs AS us\n" +
                "WHERE s.songID = us.songID\n" +
                "GROUP BY decade\n" +
                "ORDER BY popularity DESC;\n");
        rs = stmt.executeQuery();

        doStuffTwo(cols, splitChar, line, writeCSV, rs);
    }

    public static void initThree(Connection conn) throws SQLException, IOException {
        String[] cols = new String[3];
        cols[0] = "userID";
        cols[1] = "user_collections";
        cols[2] = "followers";

        String line = "\n";
        String splitChar = ",";

        // header

        File file = new File("data3-1.csv");
        FileWriter writeCSV = new FileWriter("data3-1.csv");
        doStuffOne(cols, splitChar, line, writeCSV);

        // data

        PreparedStatement stmt;
        ResultSet rs;

        stmt = conn.prepareStatement("SELECT u.userID, COUNT(c.collectionID) AS user_collections, COUNT(f.following) AS followers\n" +
                "FROM p320_09.user AS u, p320_09.collection AS c, p320_09.follow AS f\n" +
                "WHERE u.userID = c.userID\n" +
                "AND u.userID = f.user\n" +
                "GROUP BY u.userID\n" +
                "ORDER BY u.userID ASC;\n");
        rs = stmt.executeQuery();

        doStuffTwo(cols, splitChar, line, writeCSV, rs);
    }


    public static void doStuffOne(String[] cols, String splitChar, String line, FileWriter writeCSV) throws IOException {
        for (int i = 0; i < cols.length; i++) {
            writeCSV.write(cols[i]);
            if ( i + 1 != cols.length)
                writeCSV.write(splitChar);
        }
        writeCSV.write(line);
    }

    public static void doStuffTwo(String[] cols, String splitChar, String line, FileWriter writeCSV, ResultSet rs) throws IOException, SQLException {
        while (rs.next()) {
            for (int i = 0; i < cols.length; i++) {
                String t = rs.getString(cols[i]);
                writeCSV.write(t);
                if (i+1 != cols.length)
                    writeCSV.write(splitChar);
            }
            writeCSV.write(line);
        }

        writeCSV.close();
    }

}
