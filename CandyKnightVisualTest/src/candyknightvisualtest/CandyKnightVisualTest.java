
package candyknightvisualtest;

public class CandyKnightVisualTest {

    public static void main(String[] args) {
        /*ControleJanela game = new ControleJanela(); 
        game.menu();//Abre o jogo*/

        
        // 1. Cria o controlador principal da sua aplicação
        ControleJanela2 meuJogo = new ControleJanela2();
        
        // 2. Inicia o jogo
        meuJogo.iniciar();
    }
    
}
