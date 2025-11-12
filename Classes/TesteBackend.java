package candyknight;

// Importa as classes necessárias
import candyknight.Entidades.*;
import candyknight.Coletaveis.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Esta classe é um "testador de backend".
 * Ela permite-te jogar o jogo através da consola,
 * sem depender da interface gráfica (Swing).
 */
public class TesteBackend {

    public static void main(String[] args) {
        
        // 1. Inicia a lógica do jogo e o leitor de consola
        GameLogic game = new GameLogic();
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- INICIANDO TESTE DE BACKEND (Candy Knight) ---");
        System.out.println("Controla o Cavaleiro (C) movendo-o com 'c', 'b', 'e', 'd'.");

        // 2. Loop principal do jogo
        while (true) {
            
            // 3. Desenha o tabuleiro na consola
            imprimirTabuleiro(game);

            // 4. Pede a próxima jogada (SEÇÃO MODIFICADA)
            System.out.print("\nMover (c=cima, b=baixo, e=esquerda, d=direita): ");
            String input = scanner.next().toLowerCase(); // Pega a string e põe em minúsculas

            GameLogic.Direcao direcaoEscolhida = null;

            // Converte a string de input para o nosso Enum
            switch (input) {
                case "c":
                    direcaoEscolhida = GameLogic.Direcao.CIMA;
                    break;
                case "b":
                    direcaoEscolhida = GameLogic.Direcao.BAIXO;
                    break;
                case "e":
                    direcaoEscolhida = GameLogic.Direcao.ESQUERDA;
                    break;
                case "d":
                    direcaoEscolhida = GameLogic.Direcao.DIREITA;
                    break;
                default:
                    System.out.println("Comando inválido. Use 'c', 'b', 'e' ou 'd'.");
                    continue; // Pula o resto do loop e pede de novo
            }

            // 5. Executa a lógica do jogo
            System.out.println("\n--- AÇÃO ---");
            // Chama o NOVO método com o Enum
            if (direcaoEscolhida != null) {
                game.tentarMoverJogador(direcaoEscolhida);
            }
            System.out.println("--------------\n");
            
            // (Aqui podes adicionar uma condição de "Game Over")
        }
    }

    /**
     * Método auxiliar para imprimir o tabuleiro 3x3 na consola.
     * @param game A instância da GameLogic atual.
     */
    private static void imprimirTabuleiro(GameLogic game) {
        ArrayList<Celula> tabuleiro = game.getTabuleiro();
        
        System.out.println("--- ESTADO DO TABULEIRO ---");
        
        for (int i = 0; i < tabuleiro.size(); i++) {
            Celula celula = tabuleiro.get(i);
            String representacao = "[ ";

            // Verifica se há uma entidade
            if (celula.temEntidade()) {
                EntidadeJogo entidade = celula.getEntidade();
                if (entidade instanceof Cavaleiro) {
                    representacao += "C"; // Cavaleiro
                } else if (entidade instanceof MonstroDoce) {
                    representacao += "M"; // Monstro
                }
            } else {
                representacao += " "; // Vazio
            }

            representacao += " / ";

            // Verifica se há um item
            if (celula.temItem()) {
                representacao += "I"; // Item
            } else {
                representacao += " "; // Vazio
            }

            representacao += " ]";
            
            // Imprime a célula
            System.out.print(representacao);

            // Quebra a linha a cada 3 células
            if ((i + 1) % 3 == 0) {
                System.out.println(); // Nova linha
            } else {
                System.out.print("  "); // Espaçamento entre células
            }
        }
    }
}