package contoller;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.bean.Vendedor;
import model.DAO.VendedorDAO;

public class VendedorController {

    private Vendedor vendedorSelecionado;
    private List<Vendedor> tabelaDeVendedores;
    private VendedorDAO vDAO;

    public VendedorController() {
        vDAO = new VendedorDAO();
    }

    public void listarTodos(DefaultTableModel modeloTabela) {
        modeloTabela.setNumRows(0);
        List<Vendedor> listaVendedores = vDAO.buscarTodos();

        for (Vendedor v : listaVendedores) {
            modeloTabela.addRow(new Object[]{v.getId(), v.getCPF(),
                v.getNome(), v.getEndereco(), v.isStatus() ? "1 - Ativo" : "2 - Inativo"});
        }
    }

    public void listarPorId(DefaultTableModel modeloTabela, int id) {
        modeloTabela.setNumRows(0);
        Vendedor vendedorBuscado = vDAO.buscarPorId(id);

        modeloTabela.addRow(new Object[]{vendedorBuscado.getId(), vendedorBuscado.getCPF(),
            vendedorBuscado.getNome(), vendedorBuscado.getEndereco(), vendedorBuscado.isStatus() ? "1 - Ativo" : "2 - Inativo"});
    }
    
    public void salvar(DefaultTableModel modeloTabela, Vendedor vendedor, boolean novo ) {
        if( novo ) {
            vDAO.inserir(vendedor);
        } else {
            vDAO.atualizar(vendedor);
        }
        this.listarTodos(modeloTabela);
    }
    
    public void excluir(DefaultTableModel modeloTabela, Vendedor vendedor ) {
        System.out.println("Excluindo vendedor No.: " + vendedor.getId());
        if( vendedor.getId() != 0 ) {
            vDAO.excluir(vendedor.getId());
        } else {
            JOptionPane.showMessageDialog(null, "Não foi possível excluir as informações.\nVendedor não localizado.", "Erro ao excluir", JOptionPane.ERROR_MESSAGE);
        }
        this.listarTodos(modeloTabela);
    }

    public void listarPorCPF(DefaultTableModel tabelaModelo, String CPF) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
