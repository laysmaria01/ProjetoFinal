package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import javax.swing.JOptionPane;
import model.bean.Vendedor;
import DB.Connect;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class VendedorDAO implements iDAO<Vendedor> {

    private final String INSERT = "INSERT INTO vendedor(CPF, NOME, ENDERECO, STATUS) VALUES (?, ?, ?, ?)";
    private final String UPDATE = "UPDATE vendedor SET CPF=?, NOME=?, ENDERECO=?, STATUS=? WHERE ID =?";
    private final String DELETE = "DELETE FROM vendedor WHERE ID =?";
    private final String LISTALL = "SELECT * FROM vendedor";
    private final String LISTBYID = "SELECT * FROM vendedor WHERE ID=?";
    private final String LISTBYCPF = "SELECT * FROM vendedor WHERE CPF= like ? ";

    private Connect conn = null;
    private Connection conexao = null;

    @Override
    public Vendedor inserir(Vendedor novoVendedor) {
        conexao = this.getConnect().connection;
        if (novoVendedor != null && conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

                transacaoSQL.setString(1, novoVendedor.getCPF());
                transacaoSQL.setString(2, novoVendedor.getNome());
                transacaoSQL.setString(3, novoVendedor.getEndereco());
                transacaoSQL.setBoolean(4, novoVendedor.isStatus());

                transacaoSQL.execute();
                JOptionPane.showMessageDialog(null, "Vendedor cadastrado com sucesso", "Registro inserido", JOptionPane.INFORMATION_MESSAGE);

                try (ResultSet generatedKeys = transacaoSQL.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        novoVendedor.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Não foi possível recuperar o ID.");
                    }
                }

                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao inserir o vendedor no banco de" + "dados. \n" + e.getMessage(), "Erro na transação SQL", JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Os dados do vendedor não podem estar vazios.", "Vendedor não informado", JOptionPane.ERROR_MESSAGE);
        }

        return novoVendedor;
    }

    @Override
    public Vendedor atualizar(Vendedor vendedorEditado) {
        conexao = this.getConnect().connection;
        if (vendedorEditado != null && conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(UPDATE);

                transacaoSQL.setString(1, vendedorEditado.getCPF());
                transacaoSQL.setString(2, vendedorEditado.getNome());
                transacaoSQL.setString(3, vendedorEditado.getEndereco());
                transacaoSQL.setBoolean(4, vendedorEditado.isStatus());
                
                transacaoSQL.setInt(5, vendedorEditado.getId());

                int resultado = transacaoSQL.executeUpdate();

                if (resultado == 0) {
                    JOptionPane.showMessageDialog(null, "Não foi possível atualizar as informações", "Erro ao atualizar", JOptionPane.ERROR_MESSAGE);
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                JOptionPane.showMessageDialog(null, "Vendedor atualizado com sucesso", "Registro atualizado", JOptionPane.INFORMATION_MESSAGE);

                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao inserir o vendedor no banco de" + "dados. \n" + e.getMessage(), "Erro na transação SQL", JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Os dados do vendedor não podem estar vazios.", "Vendedor não informado", JOptionPane.ERROR_MESSAGE);
        }

        return vendedorEditado;
    }

    @Override
    public void excluir(int idVendedor) {
        
        int confirmar = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir este vendedor?", "Confirmar exclusão",
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

                JOptionPane.showMessageDialog(null, "Registro excluido", "Vendedor excluido com sucesso", JOptionPane.INFORMATION_MESSAGE);

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
    public List<Vendedor> buscarTodos() {
        conexao = this.getConnect().connection;

        ResultSet resultado = null;
        ArrayList<Vendedor> vendedores = new ArrayList<Vendedor>();

        if (conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(LISTALL);

                resultado = transacaoSQL.executeQuery();

                while (resultado.next()) {
                    Vendedor vendedorEncontrado = new Vendedor();

                    vendedorEncontrado.setId(resultado.getInt("id"));
                    vendedorEncontrado.setCPF(resultado.getString("CPF"));
                    vendedorEncontrado.setNome(resultado.getString("nome"));
                    vendedorEncontrado.setEndereco(resultado.getString("endereco"));
                    vendedorEncontrado.setStatus(resultado.getBoolean("status"));
                   
                    vendedores.add(vendedorEncontrado);
                }
                
                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na transação SQL", "Erro ao procurar vendedores no banco de" + "dados. \n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Problemas de conexão", "Não foi possível se conectar ao banco.", JOptionPane.ERROR_MESSAGE);
        }

        return vendedores;
    }

    @Override
    public Vendedor buscarPorId(int id) {
        conexao = this.getConnect().connection;
        
        ResultSet resultado = null;
        Vendedor vendedorEncontrado = new Vendedor();

        if (conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(LISTBYID);
                transacaoSQL.setInt(1, id);

                resultado = transacaoSQL.executeQuery();

                while (resultado.next()) {

                    vendedorEncontrado.setId(resultado.getInt("id"));
                    vendedorEncontrado.setCPF(resultado.getString("CPF"));
                    vendedorEncontrado.setNome(resultado.getString("nome"));
                    vendedorEncontrado.setEndereco(resultado.getString("endereco"));
                    vendedorEncontrado.setStatus(resultado.getBoolean("status"));
                   
                }
                
                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na transação SQL", "Erro ao procurar vendedor no banco de" + "dados. \n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Problemas de conexão", "Não foi possível se conectar ao banco.", JOptionPane.ERROR_MESSAGE);
        }

        return vendedorEncontrado;
    }

    public Connect getConnect() {
        this.conn = new Connect("root","","NovaLivraria");
        return this.conn;
    }
    
     public Vendedor buscarPorCPF(String CPF) {
        conexao = this.getConnect().connection;
        
        ResultSet resultado = null;
        Vendedor vendedorEncontrado = new Vendedor();

        if (conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(LISTBYCPF);
                transacaoSQL.setString(1, "%"+CPF+"%");

                resultado = transacaoSQL.executeQuery();

                while (resultado.next()) {

                    vendedorEncontrado.setId(resultado.getInt("id"));
                    vendedorEncontrado.setCPF(resultado.getString("CPF"));
                    vendedorEncontrado.setNome(resultado.getString("nome"));
                    vendedorEncontrado.setEndereco(resultado.getString("endereco"));
                    vendedorEncontrado.setStatus(resultado.getBoolean("status"));
                   
                }
                
                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na transação SQL", "Erro ao procurar vendedor no banco de" + "dados. \n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Problemas de conexão", "Não foi possível se conectar ao banco.", JOptionPane.ERROR_MESSAGE);
        }

        return vendedorEncontrado;
    }

}
