package candyknight;

import java.util.ArrayList;
import java.util.Collections;
import candyknight.Entidades.*;
import candyknight.Coletaveis.*;

/**
 * Classe central que gerencia o estado do jogo, o tabuleiro
 * e as interações entre as células.
 */
public class GameLogic {
    
    // +++ NOVO: Enum para Direções +++
    /**
     * Define as direções de movimento possíveis.
     * Usar um enum torna o código mais limpo e seguro.
     */
    public enum Direcao {
        CIMA,
        BAIXO,
        ESQUERDA,
        DIREITA
    }
    // +++ Fim da Adição +++

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
        tabuleiro.get(4).setEntidade(new candyknight.Entidades.Cavaleiro("Sir Doce"));
        this.posicaoJogador = 4;

        // 3. Adiciona monstros (ex: posições 1 e 7)
        tabuleiro.get(1).setEntidade(new candyknight.Entidades.UrsoDeGoma());
        tabuleiro.get(7).setEntidade(new candyknight.Entidades.SoldadoGengibre());

        // 4. Adiciona itens (ex: posições 3 e 5)
        // (Adicionei a EspadaDeAlcacuz para testar a tua correção!)
        tabuleiro.get(3).setItem(new candyknight.Coletaveis.EspadaDeAlcacuz()); 
        tabuleiro.get(5).setItem(new candyknight.Coletaveis.PocaoDeCalda());
    }

    // +++ MÉTODO NOVO (Lógica de Direção) +++
    /**
     * Tenta mover o jogador numa direção específica.
     * @param direcao A direção (CIMA, BAIXO, ESQUERDA, DIREITA) para onde mover.
     */
    public void tentarMoverJogador(Direcao direcao) {
        
        int proximaPosicao = -1;
        int posAtual = this.posicaoJogador;

        // 1. Calcula a posição de destino e verifica os limites do tabuleiro
        switch (direcao) {
            case CIMA:
                // Não pode mover para cima se estiver na linha 0 (pos 0, 1, 2)
                if (posAtual < 3) {
                    System.out.println("Não pode mover-se para cima. (Borda do tabuleiro)");
                    return; // Para a execução do método
                }
                proximaPosicao = posAtual - 3; // Move uma linha para cima
                break;

            case BAIXO:
                // Não pode mover para baixo se estiver na linha 2 (pos 6, 7, 8)
                if (posAtual > 5) {
                    System.out.println("Não pode mover-se para baixo. (Borda do tabuleiro)");
                    return;
                }
                proximaPosicao = posAtual + 3; // Move uma linha para baixo
                break;

            case ESQUERDA:
                // Não pode mover para esquerda se estiver na coluna 0 (pos 0, 3, 6)
                if (posAtual % 3 == 0) {
                    System.out.println("Não pode mover-se para a esquerda. (Borda do tabuleiro)");
                    return;
                }
                proximaPosicao = posAtual - 1; // Move uma coluna para esquerda
                break;

            case DIREITA:
                // Não pode mover para direita se estiver na coluna 2 (pos 2, 5, 8)
                if (posAtual % 3 == 2) {
                    System.out.println("Não pode mover-se para a direita. (Borda do tabuleiro)");
                    return;
                }
                proximaPosicao = posAtual + 1; // Move uma coluna para direita
                break;
        }

        // 2. Se o cálculo foi bem-sucedido, processa a interação
        if (proximaPosicao != -1) {
            processarInteracao(proximaPosicao);
        }
    }
    
    private void processarInteracao(int proximaPosicao) {
        
        if (proximaPosicao < 0 || proximaPosicao >= TAMANHO_TABULEIRO) {
            System.out.println("Movimento inválido.");
            return;
        }

        Celula celulaAtual = tabuleiro.get(posicaoJogador);
        Celula celulaDestino = tabuleiro.get(proximaPosicao);

        Cavaleiro jogador = (Cavaleiro) celulaAtual.getEntidade();

        // 1. Interage com item na célula de destino
        if (celulaDestino.temItem()) {
            Coletavel item = celulaDestino.getItem();
            System.out.println(jogador.getNome() + " encontrou e usou " + item.getNome() + "!");
            item.usar(jogador);
            celulaDestino.limparItem(); 
        }

        // 2. Interage com entidade (monstro) na célula de destino
        if (celulaDestino.temEntidade()) {
            EntidadeJogo monstro = celulaDestino.getEntidade();
            System.out.println(jogador.getNome() + " encontra " + monstro.getNome() + "!");
            
            // +++ INÍCIO MODIFICAÇÃO (REGRA 1: Combate) +++
            
            // O jogador só ataca se estiver armado
            if (jogador.getArmado()) {
                System.out.println(jogador.getNome() + " está armado e ataca!");
                jogador.atacar(monstro);
            } else {
                System.out.println(jogador.getNome() + " está desarmado e não pode atacar!");
            }

            // Se o monstro sobreviveu...
            if (monstro.estaVivo()) {
                // Ele só ataca de volta se o jogador estiver SEM arma
                if (!jogador.getArmado()) {
                    System.out.println(monstro.getNome() + " revida o encontro!");
                    monstro.atacar(jogador);
                } else {
                    System.out.println(monstro.getNome() + " fica intimidado pela arma do jogador!");
                }
            }
            // +++ FIM MODIFICAÇÃO (REGRA 1) +++


            // Se o monstro foi derrotado
            if (!monstro.estaVivo()) {
                System.out.println(jogador.getNome() + " derrotou " + monstro.getNome() + "!");
                celulaDestino.limparEntidade(); // Limpa o monstro
                
                // +++ INÍCIO MODIFICAÇÃO (REGRA 2: Movimento pós-vitória) +++
                System.out.println(jogador.getNome() + " toma a posição do monstro!");
                
                // (Esta é a mesma lógica de movimento da secção "else" abaixo)
                celulaDestino.setEntidade(jogador); // Coloca o jogador na nova célula
                celulaAtual.limparEntidade();       // Limpa o jogador da célula antiga
                this.posicaoJogador = proximaPosicao; // ATUALIZA a posição do jogador
                // +++ FIM MODIFICAÇÃO (REGRA 2) +++
            }
            
            // Se o monstro AINDA ESTÁ VIVO, o jogador NÃO se move.
            // (Não alterámos isto)
            
        } else {
            // 3. Célula livre, move o jogador (Lógica normal de movimento)
            celulaDestino.setEntidade(jogador); 
            celulaAtual.limparEntidade();       
            this.posicaoJogador = proximaPosicao; 
            System.out.println(jogador.getNome() + " se moveu para a posição " + proximaPosicao);
        }
        
        jogador.decrementarDuracaoBuffs();
    }
    
    /**
     * Permite que outras classes (como o TabuleiroPanel) leiam o tabuleiro.
     * @return O ArrayList de Células.
     */
    public ArrayList<Celula> getTabuleiro() {
        return this.tabuleiro;
    }
}