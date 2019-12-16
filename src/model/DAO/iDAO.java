package model.DAO;

import java.util.List;

public interface iDAO<T> {
    T inserir(T obj);
    T atualizar(T obj);
    void excluir(int id);
    List<T> buscarTodos();
    T buscarPorId(int id);
    
}
