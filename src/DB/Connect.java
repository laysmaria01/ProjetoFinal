package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import helpers.AnsiScape;
import helpers.Println;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Connect {

    public static Connection connection = null;

    private String userName = null;
    private String userPassword = null;
    private String databaseName = null;

    private String timeZoneString = "&useTimezone=true&serverTimezone=UTC";
    private String baseURL = "jdbc:mysql://localhost:3306/";

    public Connect(String usuario, String senha, String nomeDoBanco) {
        this.databaseName = nomeDoBanco;
        this.userName = usuario;
        this.userPassword = senha;

        try {
            this.createConnection("");
            if (connection != null) {
                Println.colored(LoadDriver.message, AnsiScape.ANSI_BLUE);
                if (createDatabase(this.databaseName)) {
                    Println.colored("Conectado com sucesso ao banco " + this.databaseName, AnsiScape.ANSI_BLUE);
                } else {
                    Println.colored("Falha ao conectar ao banco " + this.databaseName, AnsiScape.ANSI_RED);
                }
            }
        } catch (SQLException ex) {
            connection = null;
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
        }
    }

    private void createConnection(String DatabaseName) throws SQLException {
        if (LoadDriver.load()) {

            connection = DriverManager.getConnection(
                    this.baseURL
                    + DatabaseName
                    + "?user=" + this.userName
                    + "&password=" + this.userPassword
                    + this.timeZoneString);
        } else {
            Println.colored(LoadDriver.message, AnsiScape.ANSI_RED);
        }

    }

    private boolean createDatabase(String nomeDoBanco) throws SQLException {
        CreateDB banco = new CreateDB(connection, nomeDoBanco);
        this.createConnection(nomeDoBanco);

        return banco.DbIsCreated();
    }
    
    public static void closeConnection(Connection conn) {
 
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Fechada a conexão com o banco de dados");
            }
 
        } catch (Exception e) {
            System.out.println("Não foi possível fechar a conexão com o banco de dados " + e.getMessage());
        }
    }
 
    public static void fechaConexao(Connection conn, PreparedStatement stmt) {
 
        try {
            if (conn != null) {
                closeConnection(conn);
            }
            if (stmt != null) {
                stmt.close();
                System.out.println("Statement fechado com sucesso");
            }
 
 
        } catch (Exception e) {
            System.out.println("Não foi possível fechar o statement " + e.getMessage());
        }
    }
 
    public static void fechaConexao(Connection conn, PreparedStatement stmt, ResultSet rs) {
 
        try {
            if (conn != null || stmt != null) {
                fechaConexao(conn, stmt);
            }
            if (rs != null) {
                rs.close();
                System.out.println("ResultSet fechado com sucesso");
            }
 
 
        } catch (Exception e) {
            System.out.println("Não foi possível fechar o ResultSet " + e.getMessage());
        }
    }
    
    public static Connection getConnection() {
        return connection;
    }
}
