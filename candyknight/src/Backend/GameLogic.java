package Backend;

import Coletaveis.*; 
import Entidades.*;  
import java.util.ArrayList;
import java.util.Random; 

/**
 * Classe central que gerencia o estado do jogo.
 * Mecânica: Infinite Crawler (Tabuleiro móvel) com método fortalecer().
 */
public class GameLogic {
    
    private boolean partidaAtiva;
    private int pontuacaoFinal = 0;
    
    // Controle de Aleatoriedade e Dificuldade
    private Random random;
    private int nivelDificuldade;

    public enum Direcao {
        CIMA, BAIXO, ESQUERDA, DIREITA
    }

    private ArrayList<Celula> tabuleiro;
    private final int TAMANHO_TABULEIRO = 9; // 3x3
    private int posicaoJogador;

    public GameLogic() {
        this.tabuleiro = new ArrayList<>(TAMANHO_TABULEIRO);
        this.random = new Random(); 
        this.nivelDificuldade = 0;  
        iniciarTabuleiro();
    }

    public void iniciarTabuleiro() {
        partidaAtiva = true;
        nivelDificuldade = 0; // Reseta dificuldade (nível 0)
        
        tabuleiro.clear();
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            tabuleiro.add(new Celula());
        }

        // 1. Adiciona o jogador (centro)
        tabuleiro.get(4).setEntidade(new Entidades.Cavaleiro("Sir Doce"));
        this.posicaoJogador = 4;
        
        // 2. Adiciona monstros iniciais (Criar -> Fortalecer -> Adicionar)
        
        Entidades.UrsoDeGoma urso = new Entidades.UrsoDeGoma();
        urso.fortalecer(0); // Nível 0
        tabuleiro.get(1).setEntidade(urso);
        
        Entidades.SoldadoGengibre soldado = new Entidades.SoldadoGengibre();
        soldado.fortalecer(0);
        tabuleiro.get(7).setEntidade(soldado);
        
        Entidades.PeDeMolequinho pede = new Entidades.PeDeMolequinho();
        pede.fortalecer(0);
        tabuleiro.get(8).setEntidade(pede);

        // 3. Adiciona itens iniciais (Assumindo que itens também tenham fortalecer ou similar)
        // Se apenas monstros tiverem, podes remover o fortalecer dos itens aqui.
        Coletaveis.EscudoDeGoma escudo = new Coletaveis.EscudoDeGoma();
        escudo.fortalecer(0);
        tabuleiro.get(3).setItem(escudo); 
        
        Coletaveis.PocaoDeCalda pocao = new Coletaveis.PocaoDeCalda();
        pocao.fortalecer(0);
        tabuleiro.get(5).setItem(pocao);
        
        System.out.println("Novo jogo iniciado! Dificuldade Base: " + nivelDificuldade);
    }
    
    public void tentarMoverJogador(Direcao direcao) {
        if(this.partidaAtiva){
            int posAtual = this.posicaoJogador;
            int proximaPosicao = -1;
            
            // Variáveis para a mecânica de "Spawn" (nas costas do jogador)
            int posicaoOposta = -1; 
            boolean opostaValida = false; 

            // 1. Calcula destino e a posição oposta
            switch (direcao) {
                case CIMA:
                    if (posAtual < 3) {
                        System.out.println("Borda do mapa (Topo).");
                        return;
                    }
                    proximaPosicao = posAtual - 3;
                    if (posAtual <= 5) { posicaoOposta = posAtual + 3; opostaValida = true; }
                    break;

                case BAIXO:
                    if (posAtual > 5) {
                        System.out.println("Borda do mapa (Baixo).");
                        return;
                    }
                    proximaPosicao = posAtual + 3;
                    if (posAtual >= 3) { posicaoOposta = posAtual - 3; opostaValida = true; }
                    break;

                case ESQUERDA:
                    if (posAtual % 3 == 0) {
                        System.out.println("Borda do mapa (Esquerda).");
                        return;
                    }
                    proximaPosicao = posAtual - 1;
                    if (posAtual % 3 != 2) { posicaoOposta = posAtual + 1; opostaValida = true; }
                    break;

                case DIREITA:
                    if (posAtual % 3 == 2) {
                        System.out.println("Borda do mapa (Direita).");
                        return;
                    }
                    proximaPosicao = posAtual + 1;
                    if (posAtual % 3 != 0) { posicaoOposta = posAtual - 1; opostaValida = true; }
                    break;
            }

            // 2. Processa o movimento
            if (proximaPosicao != -1) {
                boolean jogadorSaiuDaCasa = processarInteracao(proximaPosicao);
                
                // +++ MECÂNICA: Se o jogador andou, puxa o tabuleiro +++
                if (jogadorSaiuDaCasa && opostaValida) {
                    this.nivelDificuldade++; // Dificuldade aumenta a cada passo
                    
                    Celula celulaAntiga = tabuleiro.get(posAtual); // Agora vazia
                    Celula celulaOposta = tabuleiro.get(posicaoOposta); // Borda oposta
                    
                    // Puxa o conteúdo da borda para o centro
                    if (celulaOposta.temEntidade()) {
                        celulaAntiga.setEntidade(celulaOposta.getEntidade());
                        celulaOposta.limparEntidade();
                    }
                    if (celulaOposta.temItem()) {
                        celulaAntiga.setItem(celulaOposta.getItem());
                        celulaOposta.limparItem();
                    }
                    
                    // Gera novo conteúdo na borda (já fortalecido)
                    gerarConteudoAleatorio(celulaOposta);
                    
                    System.out.println(">>> O cenário avançou! (Nível atual: " + this.nivelDificuldade + ")");
                }
            }
        }
    }
    
    private boolean processarInteracao(int proximaPosicao) {
        if (proximaPosicao < 0 || proximaPosicao >= TAMANHO_TABULEIRO) return false;

        Celula celulaAtual = tabuleiro.get(posicaoJogador);
        Celula celulaDestino = tabuleiro.get(proximaPosicao);
        Cavaleiro jogador = (Cavaleiro) celulaAtual.getEntidade();

        // 1. Item
        if (celulaDestino.temItem()) {
            Coletavel item = celulaDestino.getItem();
            System.out.println(jogador.getNome() + " usou " + item.getNome());
            item.usar(jogador);
            celulaDestino.limparItem(); 
        }

        // 2. Entidade (Monstro)
        if (celulaDestino.temEntidade()) {
            EntidadeJogo monstro = celulaDestino.getEntidade();
            System.out.println("Encontro: " + monstro.getNome() + " (Vida: " + monstro.getPontosDeVidaAtuais() + ")");
            
            if (jogador.getArmado()) {
                jogador.atacar(monstro);
                if(jogador.getArma().getAtaque() < monstro.getPontosDeVidaAtuais()){
                    // Desgaste da arma
                    jogador.getArma().setAtaque(jogador.getArma().getAtaque() - monstro.getPontosDeVidaAtuais(), jogador);
                } else {
                    jogador.getArma().quebrar(jogador);
                }
            } else {
                // Desarmado
                if(jogador.isEscudoDeTrocaAtivo()){
                    // Troca de lugar (Efeito do Escudo)
                    celulaAtual.setEntidade(monstro);
                    celulaDestino.setEntidade(jogador);
                    this.posicaoJogador = proximaPosicao;
                    jogador.desativarEscudoDeTroca();
                    return true; 
                } else {
                    // Dano direto
                    jogador.receberDano(monstro.getPontosDeVidaAtuais());
                    monstro.receberDano(monstro.getPontosDeVidaAtuais()); // Monstro explode
                    celulaDestino.limparEntidade();
                }
            }

            // Pós-Combate
            if (!monstro.estaVivo()) {
                if (monstro instanceof Entidades.MonstroDoce) {
                    // Drop de Moeda
                    Coletaveis.Moeda moeda = new Coletaveis.Moeda();
                    moeda.setValor(((Entidades.MonstroDoce)monstro).getRecompensaEmDinheiro());
                    celulaDestino.setItem(moeda);
                }

                // REGRA ESPECIAL: Pé de Molequinho se divide?
                if (monstro instanceof Entidades.PeDeMolequinho) {
                    System.out.println(">>> O Pé de Molequinho se dividiu em fragmentos!");
                    Entidades.PeDeMolequinhoFase2 fase2 = new Entidades.PeDeMolequinhoFase2();
                    fase2.fortalecer(this.nivelDificuldade); // Fortalece o novo monstro também
                    celulaDestino.setEntidade(fase2);
                    return false; // Jogador travado, não avança
                } else {
                    // Vitória normal
                    System.out.println(monstro.getNome() + " derrotado!");

                    // Remove o monstro da célula de destino (deixando apenas a moeda, se houver)
                    celulaDestino.limparEntidade(); 

                    // Retorna FALSE para indicar que o jogador NÃO saiu da sua posição atual.
                    // Isso impede que o tabuleiro "ande" e que novos monstros spawnem neste turno.
                    return false; 
                }
            }
            return false; // Monstro vivo (ou jogador morreu), não move

        } else {
            // 3. Célula Vazia
            celulaDestino.setEntidade(jogador); 
            celulaAtual.limparEntidade();       
            this.posicaoJogador = proximaPosicao; 
            return true; 
        }
    }
    
    // +++ MÉTODOS DE GERAÇÃO COM FORTALECER() +++
    
    private void gerarConteudoAleatorio(Celula celula) {
        int roll = random.nextInt(10); 
        if (roll < 4) { // 40% Monstro
            // Cria, fortalece e define na célula
            EntidadeJogo m = getMonstroAleatorio(this.nivelDificuldade);
            celula.setEntidade(m);
        } else if (roll < 7) { // 30% Item
            // Cria, fortalece e define na célula
            Coletavel i = getItemAleatorio(this.nivelDificuldade);
            celula.setItem(i);
        } 
    }

    private EntidadeJogo getMonstroAleatorio(int nivel) {
        int tipo = random.nextInt(3);
        EntidadeJogo monstro;
        
        // 1. Escolhe o tipo (Construtor Vazio)
        switch (tipo) {
            case 0: monstro = new UrsoDeGoma(); break;
            case 1: monstro = new SoldadoGengibre(); break;
            default: monstro = new PeDeMolequinho(); break;
        }
        
        // 2. Fortalece
        monstro.fortalecer(nivel);
        
        return monstro;
    }

    private Coletavel getItemAleatorio(int nivel) {
        int tipo = random.nextInt(3); 
        Coletavel item;
        
        // 1. Escolhe o tipo (Construtor Vazio)
        switch (tipo) {
            case 0: item = new PocaoDeCalda(); break;
            case 1: item = new EspadaDeAlcacuz(); break;
            default: item = new EscudoDeGoma(); break;
        }
        
        // 2. Fortalece
        item.fortalecer(nivel);
        
        return item;
    }
    // +++ FIM +++

    public void encerrarPartida(){
       this.partidaAtiva = false;
       if (tabuleiro.get(posicaoJogador).getEntidade() instanceof Cavaleiro) {
           this.pontuacaoFinal = ((Cavaleiro)this.tabuleiro.get(posicaoJogador).getEntidade()).getDinheiro();
       }
       System.out.println("=== GAME OVER === Pontuação: " + pontuacaoFinal);
    }
    
    public boolean getPartidaAtivo(){ return this.partidaAtiva; }
    public int getPontucaoFinal(){ return this.pontuacaoFinal; }
    public ArrayList<Celula> getTabuleiro() { return this.tabuleiro; }
}