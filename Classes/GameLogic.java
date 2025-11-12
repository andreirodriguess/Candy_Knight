package candyknight; // Pacote raiz

import java.util.ArrayList;
import java.util.Collections;

// Importa TUDO dos seus pacotes de Entidades e Coletaveis
import candyknight.Entidades.*;
import candyknight.Coletaveis.*;

/**
 * Classe central que gerencia o estado do jogo, o tabuleiro
 * e as interações entre as células.
 */
public class GameLogic {
    
    private ArrayList<Celula> tabuleiro;
    private final int TAMANHO_TABULEIRO = 9; // 3x3
    private int posicaoJogador;

    public GameLogic() {
        this.tabuleiro = new ArrayList<>(TAMANHO_TABULEIRO);
        iniciarTabuleiro();
    }

    /**
     * Prepara o tabuleiro para um novo jogo.
     */
    public void iniciarTabuleiro() {
        // 1. Cria 9 células vazias
        tabuleiro.clear();
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            tabuleiro.add(new Celula());
        }

        // 2. Adiciona o jogador (ex: posição 4, o centro)
        // (Note o caminho completo para a classe Cavaleiro)
        tabuleiro.get(4).setEntidade(new candyknight.Entidades.Cavaleiro("Sir Doce"));
        this.posicaoJogador = 4;

        // 3. Adiciona monstros (ex: posições 1 e 7)
        // (Note os caminhos completos)
        tabuleiro.get(1).setEntidade(new candyknight.Entidades.UrsoDeGoma());
        tabuleiro.get(7).setEntidade(new candyknight.Entidades.SoldadoGengibre());

        // 4. Adiciona itens (ex: posições 3 e 5)
        tabuleiro.get(3).setItem(new candyknight.Coletaveis.PocaoDeCalda());
        tabuleiro.get(5).setItem(new candyknight.Coletaveis.EscudoDeGoma());
        
        // (Eventualmente, você vai querer popular isso de forma aleatória)
    }

    /**
     * Método de exemplo para mover o jogador.
     * (Você precisará de lógica para mapear cliques ou teclas para 'proximaPosicao')
     * @param proximaPosicao O índice (0-8) para onde o jogador quer ir.
     */
    public void tentarMoverJogador(int proximaPosicao) {
        
        // Proteção para não sair do array
        if (proximaPosicao < 0 || proximaPosicao >= TAMANHO_TABULEIRO) {
            System.out.println("Movimento inválido.");
            return;
        }

        Celula celulaAtual = tabuleiro.get(posicaoJogador);
        Celula celulaDestino = tabuleiro.get(proximaPosicao);

        // Pega o jogador (fazendo "cast" da EntidadeJogo)
        Cavaleiro jogador = (Cavaleiro) celulaAtual.getEntidade();

        // 1. Interage com item na célula de destino
        if (celulaDestino.temItem()) {
            Coletavel item = celulaDestino.getItem();
            System.out.println(jogador.getNome() + " encontrou e usou " + item.getNome() + "!");
            item.usar(jogador); //
            celulaDestino.limparItem(); // Limpa o item do chão
        }

        // 2. Interage com entidade (monstro) na célula de destino
        if (celulaDestino.temEntidade()) {
            EntidadeJogo monstro = celulaDestino.getEntidade();
            System.out.println(jogador.getNome() + " batalha contra " + monstro.getNome() + "!");
            
            // Batalha simples
            jogador.atacar(monstro); //
            if (monstro.estaVivo()) {
                monstro.atacar(jogador); //
            }

            // Se o monstro foi derrotado na batalha
            if (!monstro.estaVivo()) {
                System.out.println(jogador.getNome() + " derrotou " + monstro.getNome() + "!");
                celulaDestino.limparEntidade();
                
                // Se o jogador usou o escudo de troca, ele pode trocar de lugar
                // (Aqui você adicionaria a lógica do escudo de troca)
                // if (jogador.isEscudoDeTrocaAtivo()) { ... }
            }
            
            // Se o monstro AINDA ESTÁ VIVO, o jogador NÃO se move.
            // A interação do turno acaba aqui.
            
        } else {
            // 3. Célula livre, move o jogador
            celulaDestino.setEntidade(jogador); // Coloca o jogador na nova célula
            celulaAtual.limparEntidade();       // Limpa o jogador da célula antiga
            this.posicaoJogador = proximaPosicao; // ATUALIZA a posição do jogador
            System.out.println(jogador.getNome() + " se moveu para a posição " + proximaPosicao);
        }
        
        // Decrementa buffs do jogador (como o escudo)
        jogador.decrementarDuracaoBuffs(); //
    }
    
    /**
     * Permite que outras classes (como o TabuleiroPanel) leiam o tabuleiro.
     * @return O ArrayList de Células.
     */
    public ArrayList<Celula> getTabuleiro() {
        return this.tabuleiro;
    }
}