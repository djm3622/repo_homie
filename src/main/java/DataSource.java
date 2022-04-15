import CSVstuff.DataFromSCV;
import Helper.HelperFucntions;
import Home.HomePage;
import LoginReg.Login;
import LoginReg.Reg;
import com.jcraft.jsch.*;
import org.postgresql.util.PSQLException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class DataSource {
    public static void main(String[] args) throws SQLException {
        int lport = 5432;
        String rhost = "starbug.cs.rit.edu";
        int rport = 5432;
        String user = "amk1881"; //change to your username
        String password = "$Icecream11230"; //change to your password
        String databaseName = "p320_09"; //change to your database name

        String driverName = "org.postgresql.Driver";
        Connection conn = null;
        Session session = null;
        try {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            session = jsch.getSession(user, rhost, 22);
            session.setPassword(password);
            session.setConfig(config);
            session.setConfig("PreferredAuthentications","publickey,keyboard-interactive,password");
            session.connect();
            System.out.println("Connected");
            int assigned_port = session.setPortForwardingL(lport, "localhost", rport);
            System.out.println("Port Forwarded");

            // Assigned port could be different from 5432 but rarely happens
            String url = "jdbc:postgresql://localhost:"+ assigned_port + "/" + databaseName;

            System.out.println("database Url: " + url);
            Properties props = new Properties();
            props.put("user", user);
            props.put("password", password);

            Class.forName(driverName);
            conn = DriverManager.getConnection(url, props);
            System.out.println("Database connection established");

            processInit(conn);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("caught");
        } finally {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Closing Database Connection");
                conn.close();
            }
            if (session != null && session.isConnected()) {
                System.out.println("Closing SSH Connection");
                session.disconnect();
            }
        }
    }

    public static void processInit(Connection conn) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );
        String input;

        // DataFromSCV.initIn(conn);

        label:
        while (true) {
            System.out.println("");
            System.out.println("Commands as Follows:");
            System.out.println("\t-l : to login");
            System.out.println("\t-r : to register");
            System.out.println("\t-q : to quit");
            System.out.print("> ");

            input = reader.readLine();

            switch (input) {
                case "-l":
                    Login.processLogin(conn, reader);
                    break;
                case "-r":
                    Reg.processRegister(conn, reader);
                    break;
                case "-q":
                    break label;
            }
        }

    }
}
