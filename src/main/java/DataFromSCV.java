import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class DataFromSCV {
    public static void main(String[] args) throws SQLException, IOException {
        //String path = "C:\\Users\\David\\Documents\\p320_09\\src\\main\\DataSets\\data.csv";
        File file = new File("src\\main\\DataSets\\data.csv");
        String path = file.getAbsolutePath();
        String[] cols;

        String line = "";
        String splitChar = ",";

        BufferedReader in = new BufferedReader(new FileReader(path));

        boolean skip = true;

        while ((line = in.readLine()) != null) {
            if (skip) {
                skip = false;
                continue;
            }
            cols = line.split(splitChar);
            cols[3] = String.valueOf(Integer.parseInt(cols[3]) / 1000 / 60);
            System.out.println("Duration: (" + cols[3] + ") Title: (" + cols[15] + ") Artist: (" + cols[16] + ")");
        }
    }
}
