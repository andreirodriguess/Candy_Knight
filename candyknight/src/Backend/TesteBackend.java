package Backend;

// Importa as classes necessárias
import Backend.Celula;
import Coletaveis.Coletavel; // Importante para ver os itens
import Entidades.*;        // Importa todas as entidades para podermos distinguir
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Versão MELHORADA do testador.
 * Mostra vida dos monstros e detalhes dos itens.
 */
public class TesteBackend {

    public static void main(String[] args) {
        
        GameLogic game = new GameLogic();
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- INICIANDO TESTE DE BACKEND (Candy Knight) ---");
        System.out.println("Legenda: [J]=Jogador, [U]=Urso, [S]=Soldado, [P]=PéDeMoleque, [I]=Item");
        System.out.println("Os números ao lado das letras são a VIDA atual.");

        // Loop principal
        while (game.getPartidaAtivo()) { // Verifica se o jogo ainda está ativo
            
            imprimirTabuleiroDetalhado(game);

            System.out.print("\nMover (c=cima, b=baixo, e=esquerda, d=direita, s=sair): ");
            String input = scanner.next().toLowerCase();

            GameLogic.Direcao direcaoEscolhida = null;

            if (input.equals("s")) {
                break; // Opção para sair manualmente
            }

            switch (input) {
                case "c": direcaoEscolhida = GameLogic.Direcao.CIMA; break;
                case "b": direcaoEscolhida = GameLogic.Direcao.BAIXO; break;
                case "e": direcaoEscolhida = GameLogic.Direcao.ESQUERDA; break;
                case "d": direcaoEscolhida = GameLogic.Direcao.DIREITA; break;
                default:
                    System.out.println("Comando inválido.");
                    continue;
            }

            System.out.println("\n--- AÇÃO ---");
            if (direcaoEscolhida != null) {
                game.tentarMoverJogador(direcaoEscolhida);
            }
            System.out.println("--------------\n");
        }
        
        System.out.println("FIM DE JOGO! O loop encerrou.");
        scanner.close();
    }

    /**
     * Imprime o tabuleiro mostrando QUEM é o monstro e a VIDA dele.
     * Isso ajuda a testar se o 'fortalecer()' está funcionando.
     */
    private static void imprimirTabuleiroDetalhado(GameLogic game) {
        ArrayList<Celula> tabuleiro = game.getTabuleiro();
        
        System.out.println("--- ESTADO DO TABULEIRO (Pontos: " + game.getPontucaoFinal() + ") ---");
        
        for (int i = 0; i < tabuleiro.size(); i++) {
            Celula celula = tabuleiro.get(i);
            String celulaStr = "[ ";

            // --- ENTIDADES ---
            if (celula.temEntidade()) {
                EntidadeJogo ent = celula.getEntidade();
                // Mostra a inicial do nome e a vida entre parênteses
                // Ex: J(100) ou U(15)
                String inicial = ent.getNome().substring(0, 1);
                
                // Diferenciação visual rápida
                if (ent instanceof Cavaleiro) inicial = "J"; // Jogador
                else if (ent instanceof UrsoDeGoma) inicial = "U";
                else if (ent instanceof SoldadoGengibre) inicial = "S";
                else if (ent instanceof PeDeMolequinho) inicial = "P";
                else if (ent instanceof PeDeMolequinhoFase2) inicial = "p"; // Fase 2 minúscula
                
                celulaStr += inicial + "(" + ent.getPontosDeVidaAtuais() + ")";
            } else {
                celulaStr += "     "; // Espaço vazio para alinhar
            }

            celulaStr += " | ";

            // --- ITENS ---
            if (celula.temItem()) {
                Coletavel item = celula.getItem();
                // Mostra inicial do item
                celulaStr += item.getNome().substring(0, 1);
            } else {
                celulaStr += " ";
            }

            celulaStr += " ]";
            
            System.out.print(celulaStr);

            // Quebra de linha a cada 3 células (Grid 3x3)
            if ((i + 1) % 3 == 0) {
                System.out.println();
                System.out.println("------------------------------------------");
            } else {
                System.out.print("  ");
            }
        }
    }
}