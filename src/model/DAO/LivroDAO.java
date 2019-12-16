package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import javax.swing.JOptionPane;
import model.bean.Livro;
import DB.Connect;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LivroDAO implements iDAO<Livro> {
  
 private final String INSERT = "INSERT INTO livro(ISBN, NOME, AUTOR,PAGINA,CATEGORIA) VALUES (?, ?, ?, ?, ?,)";
    private final String UPDATE = "UPDATE livro SET ISBN=?, NOME=?, AUTOR=?, PAGINA=?, CATEGORIA=?, WHERE ISBN =?";
    private final String DELETE = "DELETE FROM livro WHERE ISBN =?";
    private final String LISTALL = "SELECT * FROM livro";
    private final String LISTBYID = "SELECT * FROM livro WHERE ISBN=?";
    private final String LISTBYCPF = "SELECT * FROM livro WHERE ISBN= like ? ";

    private Connect conn = null;
    private Connection conexao = null;

    @Override
    public Livro inserir(Livro novoLivro) {
        conexao = this.getConnect().connection;
        if (novoLivro != null && conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

                transacaoSQL.setInt(1, novoLivro.getISBN());
                transacaoSQL.setInt(2, novoLivro.getId());
                transacaoSQL.setString(3, novoLivro.getTitulo());
                transacaoSQL.setString(4, novoLivro.getAutor());
                transacaoSQL.setInt(5, novoLivro.getPaginas());
                transacaoSQL.setString(6, novoLivro.getCategoria());
               transacaoSQL.setDouble(7, novoLivro.getPreco());
               transacaoSQL.setBoolean(8, novoLivro.isStatus());
                

                transacaoSQL.execute();
                JOptionPane.showMessageDialog(null, "Livro cadastrado com sucesso", "Registro inserido", JOptionPane.INFORMATION_MESSAGE);

                try (ResultSet generatedKeys = transacaoSQL.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        novoLivro.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Não foi possível recuperar o ISBN.");
                    }
                }

                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao inserir o Livro no banco de" + "dados. \n" + e.getMessage(), "Erro na transação SQL", JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Os dados do Livro não podem estar vazios.", "Vendedor não informado", JOptionPane.ERROR_MESSAGE);
        }

        return novoLivro;
    }

    @Override
    public Livro atualizar(Livro LivroEditado) {
        conexao = this.getConnect().connection;
        if (LivroEditado != null && conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(UPDATE);

                transacaoSQL.setInt(1, LivroEditado.getISBN());
                transacaoSQL.setString(2, LivroEditado.getTitulo());
                transacaoSQL.setString(3, LivroEditado.getAutor());
                transacaoSQL.setString(4, LivroEditado.getCategoria());
                transacaoSQL.setDouble(5,LivroEditado.getPreco());
                transacaoSQL.setBoolean(6, LivroEditado.isStatus());
                 transacaoSQL.setInt(7, LivroEditado.getPaginas());
                transacaoSQL.setInt(8, LivroEditado.getId());

                int resultado = transacaoSQL.executeUpdate();

                if (resultado == 0) {
                    JOptionPane.showMessageDialog(null, "Não foi possível atualizar as informações", "Erro ao atualizar", JOptionPane.ERROR_MESSAGE);
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                JOptionPane.showMessageDialog(null, "Livro atualizado com sucesso", "Registro atualizado", JOptionPane.INFORMATION_MESSAGE);

                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao inserir o livro no banco de" + "dados. \n" + e.getMessage(), "Erro na transação SQL", JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Os dados do livro não podem estar vazios.", "Vendedor não informado", JOptionPane.ERROR_MESSAGE);
        }

        return LivroEditado;
    }

    @Override
    public void excluir(int idVendedor) {
        
        int confirmar = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir este Livro?", "Confirmar exclusão",
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        // 0 - Sim  1 - Não
        if(confirmar == 1) {
            return;
        }
        conexao = this.getConnect().connection;
        if (conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(DELETE);

                transacaoSQL.setInt(1, idVendedor);

                boolean erroAoExcluir = transacaoSQL.execute();

                if (erroAoExcluir) {
                    JOptionPane.showMessageDialog(null, "Erro ao excluir", "Não foi possível excluir as informações", JOptionPane.ERROR_MESSAGE);
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                JOptionPane.showMessageDialog(null, "Registro excluido", "Livro excluido com sucesso", JOptionPane.INFORMATION_MESSAGE);

                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na transação SQL", "Erro ao excluir do vendedor no banco de" + "dados. \n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Problemas de conexão", "Não foi possível se conectar ao banco.", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<Livro> buscarTodos() {
        conexao = this.getConnect().connection;

        ResultSet resultado = null;
        ArrayList<Livro> Livros = new ArrayList<Livro>();

        if (conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(LISTALL);

                resultado = transacaoSQL.executeQuery();

                while (resultado.next()) {
                    Livro LivroEncontrado = new Livro();

                    LivroEncontrado.setId(resultado.getInt("isbn"));
                    LivroEncontrado.setCategoria(resultado.getString("categoria"));
                    LivroEncontrado.setTitulo(resultado.getString("Titulo"));
                    LivroEncontrado.setAutor(resultado.getString("autor"));
                    LivroEncontrado.setStatus(resultado.getBoolean("paginas"));
                    LivroEncontrado.setPreco(resultado.getDouble("Preco"));
                
                   
                    Livros.add(LivroEncontrado);
                }
                
                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na transação SQL", "Erro ao procurar vendedores no banco de" + "dados. \n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Problemas de conexão", "Não foi possível se conectar ao banco.", JOptionPane.ERROR_MESSAGE);
        }

        return Livros;
    }

    @Override
    public Livro buscarPorId(int id) {
        conexao = this.getConnect().connection;
        
        ResultSet resultado = null;
        Livro LivroEncontrado = new Livro();

        if (conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(LISTBYID);
                transacaoSQL.setInt(1, id);

                resultado = transacaoSQL.executeQuery();

                while (resultado.next()) {

                    LivroEncontrado.setISBN(resultado.getInt("ISBN"));
                    LivroEncontrado.setTitulo(resultado.getString("Titulo"));
                    LivroEncontrado.setAutor(resultado.getString("Autor"));
                    LivroEncontrado.setCategoria(resultado.getString("Categoria"));
                    LivroEncontrado.setStatus(resultado.getBoolean("status"));
                   
                }
                
                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na transação SQL", "Erro ao procurar vendedor no banco de" + "dados. \n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Problemas de conexão", "Não foi possível se conectar ao banco.", JOptionPane.ERROR_MESSAGE);
        }

        return LivroEncontrado;
    }

    public Connect getConnect() {
        this.conn = new Connect("root","","NovaLivraria");
        return this.conn;
    }
    
     public Livro buscarPorCPF(String CPF) {
        conexao = this.getConnect().connection;
        
        ResultSet resultado = null;
        Livro LivroEncontrado = new Livro();

        if (conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(LISTBYID);
                transacaoSQL.setString(1, "%"+CPF+"%");

                resultado = transacaoSQL.executeQuery();

                while (resultado.next()) {

                    LivroEncontrado.setId(resultado.getInt("id"));
                    LivroEncontrado.setISBN(resultado.getInt("ISBN"));
                    LivroEncontrado.setAutor(resultado.getString("Autor"));
                    LivroEncontrado.setCategoria(resultado.getString("Categoria"));
                    LivroEncontrado.setStatus(resultado.getBoolean("status"));
                   
                }
                
                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na transação SQL", "Erro ao procurar vendedor no banco de" + "dados. \n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Problemas de conexão", "Não foi possível se conectar ao banco.", JOptionPane.ERROR_MESSAGE);
        }

        return LivroEncontrado;
    }

} 

