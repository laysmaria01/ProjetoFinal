package livraria;

import DB.Connect;
import view.Menu;

public class Livraria {

    public static void main(String[] args) {
        // Criar conexão e banco
        new Connect("root","","NovaLivraria");
        
        // Abrir tela de Menu
        Menu viewMenu = new Menu();
        viewMenu.setVisible(true);
    }
    
}
