package model.bean;

public class Venda {
    private int id;
    private Livro livro;
    private Vendedor vendedor;
    private int quantidade;
    private double precoVendido;

    public void setId(int id) {
        this.id = id;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setPrecoVendido(double precoVendido) {
        this.precoVendido = precoVendido;
    }

    public int getId() {
        return id;
    }

    public Livro getLivro() {
        return livro;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoVendido() {
        return precoVendido;
    }
    
}
