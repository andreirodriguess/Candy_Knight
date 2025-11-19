package Backend;

import Backend.GameLogic;
import Backend.Celula;
import Entidades.EntidadeJogo;
import Entidades.Cavaleiro;
import Entidades.MonstroDoce;
import Coletaveis.Coletavel;
import java.util.ArrayList;
import java.util.Scanner;

public class TesteBackend {

    public static void main(String[] args) {
        
        GameLogic game = new GameLogic();
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- INICIANDO TESTE DE BACKEND (Candy Knight) ---");
        System.out.println("Controla o Cavaleiro (C) movendo-o com 'c', 'b', 'e', 'd'.");

        while (true) {
            imprimirTabuleiro(game);

            System.out.print("\nMover (c=cima, b=baixo, e=esquerda, d=direita): ");
            String input = scanner.next().toLowerCase();

            GameLogic.Direcao direcaoEscolhida = null;

            switch (input) {
                case "c": direcaoEscolhida = GameLogic.Direcao.CIMA; break;
                case "b": direcaoEscolhida = GameLogic.Direcao.BAIXO; break;
                case "e": direcaoEscolhida = GameLogic.Direcao.ESQUERDA; break;
                case "d": direcaoEscolhida = GameLogic.Direcao.DIREITA; break;
                default: System.out.println("Comando inválido."); continue;
            }

            if (direcaoEscolhida != null) {
                game.tentarMoverJogador(direcaoEscolhida);
            }
        }
    }

    private static void imprimirTabuleiro(GameLogic game) {
        ArrayList<Celula> tabuleiro = game.getTabuleiro();
        System.out.println("--- ESTADO DO TABULEIRO ---");
        
        for (int i = 0; i < tabuleiro.size(); i++) {
            Celula celula = tabuleiro.get(i);
            String representacao = "[ ";

            if (celula.temEntidade()) {
                EntidadeJogo entidade = celula.getEntidade();
                if (entidade instanceof Cavaleiro) {
                    representacao += "C(" + entidade.getPontosDeVidaAtuais() + ")"; 
                } else {
                    // Pega a primeira letra do nome do monstro
                    representacao += entidade.getNome().charAt(0) + "(" + entidade.getPontosDeVidaAtuais() + ")";
                }
            } else {
                representacao += "   "; 
            }

            representacao += " | ";

            if (celula.temItem()) {
                representacao += "I"; 
            } else {
                representacao += " "; 
            }
            representacao += " ]";
            System.out.print(representacao);

            if ((i + 1) % 3 == 0) System.out.println(); 
            else System.out.print("  "); 
        }
    }
}