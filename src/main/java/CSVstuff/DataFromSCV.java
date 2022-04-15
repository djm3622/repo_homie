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

        File file = new File("data1.csv");
        FileWriter writeCSV = new FileWriter("data1.csv");
        for (int i = 0; i < cols.length; i++) {
            writeCSV.write(cols[i]);
            if (i++ != cols.length-1)
                writeCSV.write(splitChar);
        }

        writeCSV.write(line);

        // data

        PreparedStatement stmt;
        ResultSet rs;

        stmt = conn.prepareStatement("SELECT DISTINCT to_char(u.creation_date, 'YYYY-MM') AS user_creation_date, COUNT(to_char(u.creation_date, 'YYYY-MM')) AS date_freq\n" +
                "FROM p320_09.user AS u\n" +
                "GROUP BY user_creation_date\n" +
                "ORDER BY date_freq DESC\n" +
                "[LIMIT X];\n" +
                "\n" +
                "SELECT a.title, ar.artist_name\n" +
                "FROM p320_09.album AS a, p320_09.artist AS ar\n" +
                "WHERE a.artistID = ar.artistID\n" +
                "AND to_char(a.release_date, 'YYYY-MM') = [DATE];\n");
        rs = stmt.executeQuery();

        while (rs.next()) {
            for (int i = 0; i < cols.length; i++) {
                String t = rs.getString(cols[i]);
                writeCSV.write(t);
                if (i++ != cols.length-1)
                    writeCSV.write(splitChar);
            }
            writeCSV.write(line);
        }

        writeCSV.close();
    }
}
