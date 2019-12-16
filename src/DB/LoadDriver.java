package DB;

public class LoadDriver {
    
    public static String message = null;

    public static boolean load() {
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver")
                    .newInstance();
            message = "JDBC carregado";
            return true;
            
        } catch (Exception erro) {
            message = "JDBC não encontrado. " + erro.getMessage();
            return false;
            
        }
    }
}
