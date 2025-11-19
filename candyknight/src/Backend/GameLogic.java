package Backend;

import Backend.Celula;
import Coletaveis.*;
import Entidades.Cavaleiro;
import Entidades.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collections;
import java.util.Random;
import java.util.List;

/**
 * Classe central unificada que gerencia o estado do jogo.
 * Combina a geração aleatória e mecânica de "puxar" (Versão 1)
 * com as regras de combate, durabilidade e Game Over (Versão 2).
 */
public class GameLogic {
    
    // === ENUMERAÇÕES ===
    public enum Direcao {
        CIMA, BAIXO, ESQUERDA, DIREITA
    }

    // === ATRIBUTOS PRINCIPAIS ===
    private ArrayList<Celula> tabuleiro;
    private final int TAMANHO_TABULEIRO = 9; // 3x3
    private int posicaoJogador;

    // Geradores e Dificuldade (V1)
    private Random random;
    private int nivelDificuldade;

    // Estado do Jogo (V2)
    private boolean partidaAtiva;
    private int pontuacaoFinal = 0;

    // === CONSTRUTOR ===
    public GameLogic() {
        this.tabuleiro = new ArrayList<>(TAMANHO_TABULEIRO);
        this.random = new Random(); 
        this.nivelDificuldade = 0;
        this.partidaAtiva = true; // O jogo começa ativo
        iniciarTabuleiro();
    }

    // === INICIALIZAÇÃO (Baseada na V1 para ser aleatória) ===
    /**
     * Prepara o tabuleiro com monstros e itens aleatórios.
     */
    public void iniciarTabuleiro() {
        this.partidaAtiva = true;
        this.nivelDificuldade = 0;
        this.pontuacaoFinal = 0;

        // 1. Cria células vazias
        tabuleiro.clear();
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            tabuleiro.add(new Celula());
        }

        // 2. Adiciona o jogador no centro
        tabuleiro.get(4).setEntidade(new Cavaleiro("Sir Doce"));
        this.posicaoJogador = 4;

        // 3. Define posições para spawn aleatório
        List<Integer> posicoesLivres = new ArrayList<>();
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            if (i != 4) posicoesLivres.add(i); // Tudo exceto o jogador
        }
        Collections.shuffle(posicoesLivres, this.random);

        // 4. Spawna Monstros e Itens (2 de cada)
        int numMonstros = 2;
        int numItens = 2;
        
        for(int i = 0; i < numMonstros && !posicoesLivres.isEmpty(); i++) {
            int pos = posicoesLivres.remove(0);
            tabuleiro.get(pos).setEntidade(getMonstroAleatorio(this.nivelDificuldade));
        }
        
        for(int i = 0; i < numItens && !posicoesLivres.isEmpty(); i++) {
            int pos = posicoesLivres.remove(0);
            tabuleiro.get(pos).setItem(getItemAleatorio(this.nivelDificuldade));
        }
        
        System.out.println("Novo jogo iniciado! Dificuldade: " + nivelDificuldade);
    }

    // === LÓGICA DE MOVIMENTO (Mecânica de "Puxar" da V1 + Checks da V2) ===
    
    public int tentarMoverJogador(Direcao direcao) {
        // Verifica se o jogo acabou (V2)
        if (!this.partidaAtiva) {
            System.out.println("A partida acabou. Reinicie o jogo.");
            return -1;
        }

        int proximaPosicao = -1;
        int posAtual = this.posicaoJogador;
        int posicaoOposta = -1; 
        boolean opostaValida = false; 

        // Cálculo de destino e posição oposta (para preencher o vazio)
        switch (direcao) {
            case CIMA:
                if (posAtual < 3) return -1; // Borda
                proximaPosicao = posAtual - 3;
                if (posAtual <= 5) { posicaoOposta = posAtual + 3; opostaValida = true; }
                break;
            case BAIXO:
                if (posAtual > 5) return -1; // Borda
                proximaPosicao = posAtual + 3;
                if (posAtual >= 3) { posicaoOposta = posAtual - 3; opostaValida = true; }
                break;
            case ESQUERDA:
                if (posAtual % 3 == 0) return -1; // Borda
                proximaPosicao = posAtual - 1;
                if (posAtual % 3 != 2) { posicaoOposta = posAtual + 1; opostaValida = true; }
                break;
            case DIREITA:
                if (posAtual % 3 == 2) return -1; // Borda
                proximaPosicao = posAtual + 1;
                if (posAtual % 3 != 0) { posicaoOposta = posAtual - 1; opostaValida = true; }
                break;
        }

        if (proximaPosicao != -1) {
            // Tenta interagir/mover. Retorna true se o jogador saiu da casa atual.
            boolean jogadorMoveu = processarInteracao(proximaPosicao, posAtual);
            
            Cavaleiro jogador = (Cavaleiro) tabuleiro.get(this.posicaoJogador).getEntidade();

            // Verifica se morreu após interação (Lógica V2)
            if (jogador == null || !jogador.estaVivo()) {
                encerrarPartida();
                return -1;
            } 
            
            

            // Se moveu com sucesso, executa a lógica de "PUXAR" o tabuleiro (V1)
            if (jogadorMoveu) {
                this.nivelDificuldade++; // Aumenta desafio
                
                if (opostaValida) {
                    // Puxa a célula oposta para onde o jogador estava
                    Celula celulaOposta = tabuleiro.get(posicaoOposta);
                    Celula celulaVazia = tabuleiro.get(posAtual); 
                    
                    celulaVazia.setEntidade(celulaOposta.getEntidade());
                    celulaVazia.setItem(celulaOposta.getItem());
                    
                    // Gera novo conteúdo na borda que ficou vazia
                    celulaOposta.limparEntidade();
                    celulaOposta.limparItem();
                    gerarConteudoAleatorio(celulaOposta);
                    
                    return posicaoOposta; 
                }
                return -2; // Moveu mas sem puxar (raro neste grid)
            }
        }
        return -1;
    }

    // === INTERAÇÃO E COMBATE (Fusão V1 e V2) ===
    
    private boolean processarInteracao(int proximaPosicao, int posicaoAntiga) {
        Celula celulaAtual = tabuleiro.get(posicaoAntiga); 
        Celula celulaDestino = tabuleiro.get(proximaPosicao);
        Cavaleiro jogador = (Cavaleiro) celulaAtual.getEntidade();

        // 1. Interage com ITEM
        if (celulaDestino.temItem()) {
            Coletavel item = celulaDestino.getItem();
            System.out.println(jogador.getNome() + " usou " + item.getNome() + "!");
            item.usar(jogador);
            celulaDestino.limparItem(); 
        }

        // 2. Interage com MONSTRO
        if (celulaDestino.temEntidade()) {
            EntidadeJogo monstro = celulaDestino.getEntidade();
            System.out.println("Encontro com " + monstro.getNome() + "!");

            // COMBATE: Lógica Fundida
            if (jogador.getArmado()) {
                // Lógica V2: Arma pode quebrar ou perder ataque
                System.out.println("Ataque armado!");
                jogador.atacar(monstro);
                
                // Acessando a arma para ver se quebra (Lógica V2)
                if(jogador.getArma().getAtaque() < monstro.getPontosDeVidaAtuais()){
                    // Reduz o dano da arma baseado na vida do monstro
                    int novoAtaque = jogador.getArma().getAtaque() - monstro.getPontosDeVidaAtuais();
                    // Nota: O método setAtaque precisa existir na sua classe Arma, ou criar nova arma
                    // Assumindo que existe um método ou lógica para atualizar:
                     jogador.getArma().setAtaque(novoAtaque, jogador); 
                } else {
                    // Arma quebra
                    System.out.println("A arma quebrou no impacto!");
                    jogador.getArma().quebrar(jogador);
                }

            } else {
                // Lógica V2: Checa Escudo de Troca
                if (jogador.isEscudoDeTrocaAtivo()) {
                    System.out.println("Escudo de Troca ativado! Trocando de lugar com o monstro.");
                    celulaAtual.setEntidade(monstro); // Monstro vai para onde o jogador estava
                    celulaDestino.setEntidade(jogador); // Jogador vai para o destino
                    this.posicaoJogador = proximaPosicao;
                    jogador.desativarEscudoDeTroca();
                    return true; // Jogador moveu (troca especial)
                } 
                
                // Lógica V1/V2: Sem arma e sem escudo -> Dano
                System.out.println("Desarmado! Recebendo dano direto.");
                jogador.receberDano(monstro.getPontosDeVidaAtuais());
                
                if (jogador.estaVivo()) {
                    monstro.setPontosDeVidaAtuais(0); // Jogador sobreviveu, monstro morre
                }
            }

            // PÓS-COMBATE
            if (!monstro.estaVivo()) {
                System.out.println("Monstro derrotado!");
                celulaDestino.limparEntidade();
                
                if (jogador.estaVivo()) {
                    // Move jogador
                    celulaDestino.setEntidade(jogador);
                    celulaAtual.limparEntidade();
                    this.posicaoJogador = proximaPosicao;
                    return true; // Sucesso
                }
            }
            
            return false; // Jogador não avançou (ou morreu, ou monstro não morreu)
            
        } else {
            // 3. Célula VAZIA (Movimento simples)
            celulaDestino.setEntidade(jogador); 
            celulaAtual.limparEntidade();       
            this.posicaoJogador = proximaPosicao; 
            return true;
        }
    }

    // === AUXILIARES DE GERAÇÃO (V1) ===
    
    private void gerarConteudoAleatorio(Celula celula) {
        int roll = random.nextInt(10); // 0 a 9
        if (roll < 4) { // 40% Monstro
            celula.setEntidade(getMonstroAleatorio(this.nivelDificuldade));
        } else if (roll < 8) { // 40% Item
            celula.setItem(getItemAleatorio(this.nivelDificuldade));
        } else { // 20% Vazio
            celula.limparEntidade(); celula.limparItem();
        }
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
        int tipo = random.nextInt(3); // 0, 1 ou 2
        switch (tipo) {
            case 0: return new PocaoDeCalda(nivel);
            case 1: return new EspadaDeAlcacuz(nivel); 
            default: return new EscudoDeGoma(nivel); // Adicionei o Escudo aqui
        }
    }

    // === ESTADO DO JOGO (V2) ===

    public void encerrarPartida(){
       this.partidaAtiva = false;
       Cavaleiro jogador = (Cavaleiro)this.tabuleiro.get(posicaoJogador).getEntidade();
       if (jogador != null) {
           this.pontuacaoFinal = jogador.getDinheiro(); // Assumindo que dinheiro é score
       }
       System.out.println("=== GAME OVER ===");
       System.out.println("Pontuação Final: " + this.pontuacaoFinal);
    }

    public boolean isPartidaAtiva() {
        return partidaAtiva;
    }

    public ArrayList<Celula> getTabuleiro() {
        return this.tabuleiro;
    }
}