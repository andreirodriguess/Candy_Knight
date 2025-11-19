package Backend;

import Backend.Celula;
import Coletaveis.*;
import Entidades.Cavaleiro;
import Entidades.*; // Importa UrsoDeGoma, SoldadoGengibre, etc.
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.List;

/**
 * Classe central que gerencia o estado do jogo.
 */
public class GameLogic {
    
    // === ENUMERAÇÕES ===
    public enum Direcao {
        CIMA, BAIXO, ESQUERDA, DIREITA
    }

    // === ATRIBUTOS ===
    private ArrayList<Celula> tabuleiro;
    private final int TAMANHO_TABULEIRO = 9; // 3x3
    private int posicaoJogador;

    // Geradores e Dificuldade
    private Random random;
    private int nivelDificuldade;

    // Estado do Jogo
    private boolean partidaAtiva;
    private int pontuacaoFinal = 0;

    // === CONSTRUTOR ===
    public GameLogic() {
        this.tabuleiro = new ArrayList<>(TAMANHO_TABULEIRO);
        this.random = new Random(); 
        this.nivelDificuldade = 0;
        this.partidaAtiva = true;
        iniciarTabuleiro();
    }

    // === INICIALIZAÇÃO ===
    public void iniciarTabuleiro() {
        this.partidaAtiva = true;
        this.nivelDificuldade = 0;
        this.pontuacaoFinal = 0;

        // 1. Cria células vazias
        tabuleiro.clear();
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            tabuleiro.add(new Celula());
        }

        // 2. Adiciona o jogador no centro (Posição 4)
        tabuleiro.get(4).setEntidade(new Cavaleiro("Sir Doce"));
        this.posicaoJogador = 4;

        // 3. Adiciona monstros iniciais (CORRIGIDO: Passando o nível 0)
        tabuleiro.get(1).setEntidade(new UrsoDeGoma(this.nivelDificuldade));
        tabuleiro.get(7).setEntidade(new SoldadoGengibre(this.nivelDificuldade));

        // 4. Adiciona itens iniciais
        tabuleiro.get(3).setItem(new EspadaDeAlcacuz(this.nivelDificuldade)); 
        tabuleiro.get(5).setItem(new PocaoDeCalda(this.nivelDificuldade));
        
        System.out.println("Novo jogo iniciado! Dificuldade: " + nivelDificuldade);
    }

    // === LÓGICA DE MOVIMENTO ===
    public void tentarMoverJogador(Direcao direcao) {
        if (!this.partidaAtiva) {
            System.out.println("A partida acabou. Reinicie o jogo.");
            return;
        }

        int posAtual = this.posicaoJogador;
        int proximaPosicao = -1;
        int posicaoOposta = -1; 
        boolean opostaValida = false; 

        // Lógica para calcular destino e posição oposta (para o "spawn" de novos inimigos)
        switch (direcao) {
            case CIMA:
                if (posAtual >= 3) { 
                    proximaPosicao = posAtual - 3;
                    if (posAtual <= 5) { posicaoOposta = posAtual + 3; opostaValida = true; }
                }
                break;
            case BAIXO:
                if (posAtual <= 5) { 
                    proximaPosicao = posAtual + 3;
                    if (posAtual >= 3) { posicaoOposta = posAtual - 3; opostaValida = true; }
                }
                break;
            case ESQUERDA:
                if (posAtual % 3 != 0) { 
                    proximaPosicao = posAtual - 1;
                    if (posAtual % 3 != 2) { posicaoOposta = posAtual + 1; opostaValida = true; }
                }
                break;
            case DIREITA:
                if (posAtual % 3 != 2) { 
                    proximaPosicao = posAtual + 1;
                    if (posAtual % 3 != 0) { posicaoOposta = posAtual - 1; opostaValida = true; }
                }
                break;
        }

        if (proximaPosicao != -1) {
            // Tenta interagir/mover. Retorna true se o jogador saiu da casa atual.
            boolean jogadorMoveu = processarInteracao(proximaPosicao, posAtual);
            
            Cavaleiro jogador = (Cavaleiro) tabuleiro.get(this.posicaoJogador).getEntidade();

            // Verifica Game Over
            if (jogador == null || !jogador.estaVivo()) {
                encerrarPartida();
                return;
            } 

            // Lógica de "Puxar" o tabuleiro (Spawnar novos inimigos nas costas do jogador)
            if (jogadorMoveu && opostaValida) {
                this.nivelDificuldade++; // Aumenta dificuldade a cada passo
                
                Celula celulaOposta = tabuleiro.get(posicaoOposta);
                Celula celulaVazia = tabuleiro.get(posAtual); // Onde o jogador estava
                
                // Puxa o conteúdo da ponta para o centro
                celulaVazia.setEntidade(celulaOposta.getEntidade());
                celulaVazia.setItem(celulaOposta.getItem());
                
                // Gera novo monstro/item na borda que ficou vazia
                celulaOposta.limparEntidade();
                celulaOposta.limparItem();
                gerarConteudoAleatorio(celulaOposta);
            }
        } else {
            System.out.println("Movimento inválido (Borda).");
        }
    }
 
    
    //PROCESSA A INTERAÇÃO DO JOGADOR COM O QUE HOUVER NA CELULA DE DESTINO
    private boolean processarInteracao(int proximaPosicao, int posicaoAntiga) {
        Celula celulaAtual = tabuleiro.get(posicaoAntiga); 
        Celula celulaDestino = tabuleiro.get(proximaPosicao);
        Cavaleiro jogador = (Cavaleiro) celulaAtual.getEntidade();

        // 1. Interage com ITEM
        if (celulaDestino.temItem()) {
            Coletavel item = celulaDestino.getItem();
            item.usar(jogador);
            celulaDestino.limparItem(); 
        }

        // 2. Interage com MONSTRO
        if (celulaDestino.temEntidade()) {
            EntidadeJogo monstro = celulaDestino.getEntidade();
            System.out.println("Encontro com " + monstro.getNome() + "!");

            if (jogador.getArmado()) {
                // Combate com arma
                jogador.atacar(monstro);
                
                // Reduz dano da arma ou quebra
                if(jogador.getArma().getAtaque() < monstro.getPontosDeVidaAtuais()){
                    int novoAtaque = Math.max(1, jogador.getArma().getAtaque() - 2); // Perde fio
                    jogador.getArma().setAtaque(novoAtaque, jogador); 
                } else {
                    jogador.getArma().quebrar(jogador); // Quebra se matar hitkill (exemplo)
                }

            } else {
                // Combate desarmado ou Esquiva
                if (jogador.isEscudoDeTrocaAtivo()) {
                    System.out.println("Escudo de Troca! Trocando de lugar com o monstro.");
                    celulaAtual.setEntidade(monstro);
                    celulaDestino.setEntidade(jogador);
                    this.posicaoJogador = proximaPosicao;
                    return true; 
                }
                //lutando totalmente desarmado
                else{
                    jogador.receberDano(monstro.getPontosDeVidaAtuais()); 
                    
                    // Remove o monstro (ele é destruído na colisão)
                    celulaDestino.limparEntidade(); 
                    
                    // Move o jogador para a posição do monstro
                    celulaDestino.setEntidade(jogador); 
                    celulaAtual.limparEntidade();       
                    this.posicaoJogador = proximaPosicao;
                    
                    return true;
                }
                
            }

            // Se venceu
            if (!monstro.estaVivo()) {
                System.out.println("Monstro derrotado!");
                if (monstro instanceof MonstroDoce) {
                    jogador.coletarDinheiro(((MonstroDoce)monstro).getRecompensaEmDinheiro());
                }
                celulaDestino.limparEntidade();
                
                // Move jogador
                celulaDestino.setEntidade(jogador);
                celulaAtual.limparEntidade();
                this.posicaoJogador = proximaPosicao;
                
                return true;
            }
            
            return false; // MORREU
            
        } else {
            // 3. Célula VAZIA
            celulaDestino.setEntidade(jogador); 
            celulaAtual.limparEntidade();       
            this.posicaoJogador = proximaPosicao; 
            return true;
        }
    }

    // === GERADORES ===
    
    private void gerarConteudoAleatorio(Celula celula) {
        int roll = random.nextInt(10); // 0 a 9
        if (roll < 4) { // 40% Monstro
            celula.setEntidade(getMonstroAleatorio(this.nivelDificuldade));
        } else if (roll < 7) { // 30% Item
            celula.setItem(getItemAleatorio(this.nivelDificuldade));
        } 
        // 30% Vazio
    }

    private MonstroDoce getMonstroAleatorio(int nivel) {
        int tipo = random.nextInt(3);
        switch (tipo) {
            case 0: return new UrsoDeGoma(nivel);
            case 1: return new SoldadoGengibre(nivel);
            default: return new PeDeMolequinho(nivel);
        }
    }

    private Coletavel getItemAleatorio(int nivel) {
        int tipo = random.nextInt(3); 
        switch (tipo) {
            case 0: return new PocaoDeCalda(nivel);
            case 1: return new EspadaDeAlcacuz(nivel); 
            default: return new EscudoDeGoma(nivel);
        }
    }

    public void encerrarPartida(){
       this.partidaAtiva = false;
       // Tenta recuperar jogador para pegar pontuação, se ainda existir na referência
       if (posicaoJogador >= 0 && posicaoJogador < TAMANHO_TABULEIRO) {
           EntidadeJogo ent = tabuleiro.get(posicaoJogador).getEntidade();
           if (ent instanceof Cavaleiro) {
               this.pontuacaoFinal = ((Cavaleiro)ent).getDinheiro();
           }
       }
       System.out.println("=== GAME OVER ===");
       System.out.println("Pontuação Final: " + this.pontuacaoFinal);
    }

    public ArrayList<Celula> getTabuleiro() {
        return this.tabuleiro;
    }
}