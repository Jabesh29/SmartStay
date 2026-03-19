package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() {

        Connection con = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/smartstay_db",
                "root",
                "jabesh2910"
            );

            System.out.println("Database Connected Successfully");

        } catch (ClassNotFoundException | SQLException e) {

            System.out.println(e);

        }

        return con;
    }
}
