package DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateDB {

    private boolean DbCreated = false;
    private boolean TablesCreated = false;

    public CreateDB(Connection c, String nomeDoBanco) throws SQLException {
        String sql = "CREATE DATABASE IF NOT EXISTS " + nomeDoBanco;
        Statement stmt = c.createStatement();
        stmt.executeUpdate(sql);
        this.setDbCreated(true);
        this.createTables(c, nomeDoBanco);
    }

    public boolean DbIsCreated() {
        return DbCreated;
    }

    public final void setDbCreated(boolean DbCreated) {
        this.DbCreated = DbCreated;
    }

    public boolean isTablesCreated() {
        return TablesCreated;
    }

    public void setTablesCreated(boolean TablesCreated) {
        this.TablesCreated = TablesCreated;
    }

    private void createTables(Connection c, String nomeDoBanco) {
        String abrirBanco = "USE " + nomeDoBanco + ";";
        String tabelaVendedor = "CREATE TABLE IF NOT EXISTS vendedor ("
                + "  id int(11) NOT NULL AUTO_INCREMENT,"
                + "  CPF varchar(17) NOT NULL,"
                + "  nome varchar(60) NOT NULL,"
                + "  endereco varchar(150) NOT NULL,"
                + "  status int(1) NOT NULL DEFAULT 1,"
                + "  PRIMARY KEY (id)"
                + ") ;";

        String tabelaLivro = "CREATE TABLE IF NOT EXISTS livro ("
                + "  id int(11) NOT NULL AUTO_INCREMENT,"
                + "  ISBN int(11) NOT NULL,"
                + "  Titulo varchar(100) NOT NULL,"
                + "  Autor varchar(100) NOT NULL,"
                + "  Paginas int(11) NOT NULL,"
                + "  Preco double NOT NULL,"
                + "  Categoria varchar(50) NOT NULL,"
                + "  PRIMARY KEY (id),"
                + "  KEY ISBN (ISBN)"
                + ") ;";

        String tabelaVenda = "CREATE TABLE IF NOT EXISTS venda ("
                + "  id int(11) NOT NULL AUTO_INCREMENT,"
                + "  livro_id int(11) NOT NULL,"
                + "  quantidade_vendida int(11) NOT NULL,"
                + "  preco_vendido double NOT NULL,"
                + "  vendedor_id int(11) NOT NULL,"
                + "  PRIMARY KEY (id),"
                + "  CONSTRAINT livro_fk FOREIGN KEY (livro_id) REFERENCES livro (id),"
                + "  CONSTRAINT vendedor_fk FOREIGN KEY (vendedor_id) REFERENCES vendedor (id)"
                + ") ;";
        
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            stmt.executeQuery(abrirBanco);
            stmt.execute(tabelaLivro);
            stmt.execute(tabelaVendedor);
            stmt.execute(tabelaVenda);
        } catch (SQLException ex) {
            Logger.getLogger(CreateDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
