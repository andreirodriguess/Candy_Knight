package candyknight;

import java.util.ArrayList;
import java.util.Collections; // +++ NOVO: Para embaralhar a lista +++
import java.util.Random;     // +++ NOVO: Para gerar aleatórios +++
import java.util.List;       // +++ NOVO: Para usar a interface List +++
import candyknight.Entidades.*;
import candyknight.Coletaveis.*;

/**
 * Classe central que gerencia o estado do jogo, o tabuleiro
 * e as interações entre as células.
 */
public class GameLogic {
    
    public enum Direcao {
        CIMA,
        BAIXO,
        ESQUERDA,
        DIREITA
    }

    private ArrayList<Celula> tabuleiro;
    private final int TAMANHO_TABULEIRO = 9; // 3x3
    private int posicaoJogador;

    // +++ NOVO: Gerador de números aleatórios +++
    private Random random;
    
    // +++ NOVO: Controle de dificuldade +++
    private int nivelDificuldade;

    public GameLogic() {
        this.tabuleiro = new ArrayList<>(TAMANHO_TABULEIRO);
        this.random = new Random(); 
        this.nivelDificuldade = 0; // Começa no nível 0
        iniciarTabuleiro();
    }

    /**
     * Prepara o tabuleiro para um novo jogo, com
     * monstros e itens em posições aleatórias.
     */
    public void iniciarTabuleiro() {
        // 1. Cria 9 células vazias
        tabuleiro.clear();
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            tabuleiro.add(new Celula());
        }

        // 2. Adiciona o jogador (posição 4, o centro)
        tabuleiro.get(4).setEntidade(new candyknight.Entidades.Cavaleiro("Sir Doce"));
        this.posicaoJogador = 4;
        
        // +++ NOVO: Reseta a dificuldade no início +++
        this.nivelDificuldade = 0;


        // +++ INÍCIO DA NOVA LÓGICA ALEATÓRIA +++

        // 3. Define a quantidade de monstros e itens
        int numMonstros = 2;
        int numItens = 2; 

        // 4. Cria uma lista de posições livres para spawn
        List<Integer> posicoesLivres = new ArrayList<>();
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            posicoesLivres.add(i);
        }
        
        posicoesLivres.remove(Integer.valueOf(this.posicaoJogador)); 

        // 5. Embaralha a lista de posições livres
        Collections.shuffle(posicoesLivres, this.random);

        // 6. Adiciona os monstros em posições aleatórias
        int monstrosAdicionados = 0;
        while (monstrosAdicionados < numMonstros && !posicoesLivres.isEmpty()) {
            
            int posParaSpawn = posicoesLivres.remove(0); 
            
            // +++ MUDANÇA: Passa o nível de dificuldade (0 no início) +++
            MonstroDoce monstro = getMonstroAleatorio(this.nivelDificuldade); 
            tabuleiro.get(posParaSpawn).setEntidade(monstro);
            
            monstrosAdicionados++;
        }
        
        // 7. Adiciona os itens em posições aleatórias
        int itensAdicionados = 0;
        while (itensAdicionados < numItens && !posicoesLivres.isEmpty()) {
            
            int posParaSpawn = posicoesLivres.remove(0); 
            
            // +++ MUDANÇA: Passa o nível de dificuldade (0 no início) +++
            Coletavel item = getItemAleatorio(this.nivelDificuldade); 
            
            tabuleiro.get(posParaSpawn).setItem(item);
            
            itensAdicionados++;
        }
        
        System.out.println("Tabuleiro iniciado com " + monstrosAdicionados + " monstros e " + itensAdicionados + " itens.");
        // +++ FIM DA NOVA LÓGICA ALEATÓRIA +++
    }

    // +++ MÉTODO MODIFICADO: Aceita o nível de dificuldade +++
    /**
     * Cria e retorna uma instância de um MonstroDoce aleatório,
     * com status ajustados para o nível atual.
     * @param nivel O nível de dificuldade atual do jogo.
     * @return Um MonstroDoce (Urso, Soldado ou PeDeMolequinho).
     */
    private MonstroDoce getMonstroAleatorio(int nivel) {
        int tipoMonstro = random.nextInt(3); 

        switch (tipoMonstro) {
            case 0:
                return new UrsoDeGoma(nivel);
            case 1:
                return new SoldadoGengibre(nivel);
            case 2:
            default: 
                return new PeDeMolequinho(nivel);
        }
    }
    
    // +++ MÉTODO MODIFICADO: Aceita o nível de dificuldade +++
    /**
     * Cria e retorna uma instância de um Coletavel aleatório,
     * com status ajustados para o nível atual.
     * @param nivel O nível de dificuldade atual do jogo.
     * @return Um Coletavel (Pocao ou Espada).
     */
    private Coletavel getItemAleatorio(int nivel) {
        int tipoItem = random.nextInt(2); 
        
        switch (tipoItem) {
            case 0:
                return new PocaoDeCalda(nivel);
            case 1:
            default:
                return new EspadaDeAlcacuz(nivel); // A "arma"
        }
    }
    
    // +++ MÉTODO MODIFICADO: Passa o nível de dificuldade +++
    private void gerarConteudoAleatorio(Celula celula) {

        int tipoSpawn = random.nextInt(5); 

        switch (tipoSpawn) {
            case 0: 
            case 1:
                // SPAWN MONSTRO (com dificuldade atual)
                celula.setEntidade(getMonstroAleatorio(this.nivelDificuldade));
                celula.limparItem(); 
                System.out.println("...um Monstro apareceu na borda!");
                break;
                
            case 2: 
            case 3:
                // SPAWN ITEM (com dificuldade atual)
                celula.setItem(getItemAleatorio(this.nivelDificuldade));
                celula.limparEntidade(); 
                System.out.println("...um Item apareceu na borda!");
                break;
                
            case 4: 
            default:
                // SPAWN VAZIO
                celula.limparEntidade();
                celula.limparItem();
                System.out.println("...a borda veio vazia.");
                break;
        }
    }


    public int tentarMoverJogador(Direcao direcao) {
        int proximaPosicao = -1;
        int posAtual = this.posicaoJogador;
        
        int posicaoOposta = -1; 
        boolean opostaValida = false; 

        // 1. Calcula a posição de destino e a posição oposta
        switch (direcao) {
            case CIMA:
                if (posAtual < 3) {
                    System.out.println("Não pode mover-se para cima. (Borda do tabuleiro)");
                    return -1; // Falha (borda)
                }
                proximaPosicao = posAtual - 3;
                if (posAtual <= 5) { 
                    posicaoOposta = posAtual + 3;
                    opostaValida = true;
                }
                break;

            case BAIXO:
                if (posAtual > 5) {
                    System.out.println("Não pode mover-se para baixo. (Borda do tabuleiro)");
                    return -1; // Falha (borda)
                }
                proximaPosicao = posAtual + 3;
                if (posAtual >= 3) { 
                    posicaoOposta = posAtual - 3;
                    opostaValida = true;
                }
                break;

            case ESQUERDA:
                if (posAtual % 3 == 0) {
                    System.out.println("Não pode mover-se para a esquerda. (Borda do tabuleiro)");
                    return -1; // Falha (borda)
                }
                proximaPosicao = posAtual - 1;
                if (posAtual % 3 != 2) { 
                    posicaoOposta = posAtual + 1;
                    opostaValida = true;
                }
                break;

            case DIREITA:
                if (posAtual % 3 == 2) {
                    System.out.println("Não pode mover-se para a direita. (Borda do tabuleiro)");
                    return -1; // Falha (borda)
                }
                proximaPosicao = posAtual + 1;
                if (posAtual % 3 != 0) { 
                    posicaoOposta = posAtual - 1;
                    opostaValida = true;
                }
                break;
        }

        // 2. Se o cálculo foi bem-sucedido, processa a interação
        if (proximaPosicao != -1) {
            
            boolean jogadorMoveu = processarInteracao(proximaPosicao, posAtual);

            // Pega o jogador 
            Cavaleiro jogador = (Cavaleiro) tabuleiro.get(this.posicaoJogador).getEntidade();
            if (jogador != null) {
                jogador.decrementarDuracaoBuffs();
            }


            // 3. Se o jogador se moveu, executamos a lógica de "puxar"
            if (jogadorMoveu) {
                
                // +++ NOVO: Aumenta a dificuldade a cada movimento +++
                this.nivelDificuldade++;
                System.out.println("Nível de dificuldade aumentado para: " + this.nivelDificuldade);
                // +++ FIM DA MUDANÇA +++
                
                if (opostaValida) {
                    Celula celulaOposta = tabuleiro.get(posicaoOposta);
                    Celula celulaDestinoPuxada = tabuleiro.get(posAtual); 

                    EntidadeJogo entidadeOposta = celulaOposta.getEntidade();
                    Coletavel itemOposto = celulaOposta.getItem();

                    celulaDestinoPuxada.setEntidade(entidadeOposta);
                    celulaDestinoPuxada.setItem(itemOposto);

                    celulaOposta.limparEntidade();
                    celulaOposta.limparItem();
                    
                    // Gera novo conteúdo (já usando o nívelDificuldade atualizado)
                    gerarConteudoAleatorio(celulaOposta); 
                    
                    System.out.println("Célula " + posicaoOposta + " foi puxada para a posição " + posAtual);
                    
                    return posicaoOposta; 
                } else {
                    return -2; // Sucesso, sem "puxar"
                }
                
            } else {
                return -1; // Falha (bloqueado)
            }
        }
        
        return -1;
    }
    
    // O restante do método processarInteracao() permanece igual ao que você forneceu,
    // pois a lógica de combate (pool de dano, desarmado vs armado) já está correta.
    // ... (resto da classe) ...
    
    private boolean processarInteracao(int proximaPosicao, int posicaoAntiga) {
        
        
        if (proximaPosicao < 0 || proximaPosicao >= TAMANHO_TABULEIRO) {
            System.out.println("Movimento inválido.");
            return false; 
        }

        Celula celulaAtual = tabuleiro.get(posicaoAntiga); 
        Celula celulaDestino = tabuleiro.get(proximaPosicao);

        Cavaleiro jogador = (Cavaleiro) celulaAtual.getEntidade();
        
        // STATS ANTES DA AÇÃO
            System.out.printf("Vida do jogador: %d/%d",jogador.getPontosDeVidaAtuais(), jogador.getPontosDeVidaMax());
            System.out.printf("\nDano/Pool do jogador: %d", jogador.getPotencia());
            System.out.printf("\nPossui arma?: %s", jogador.getArmado());
            System.out.println("\n");
        //

        // 1. Interage com item na célula de destino
        if (celulaDestino.temItem()) {
            Coletavel item = celulaDestino.getItem();
            System.out.println(jogador.getNome() + " encontrou e usou " + item.getNome() + "!");
            item.usar(jogador);
            celulaDestino.limparItem(); 
            
            // STATS PÓS-ITEM
            System.out.printf("\nVida do jogador: %d/%d", jogador.getPontosDeVidaAtuais(), jogador.getPontosDeVidaMax());
            System.out.printf("\nDano/Pool do jogador: %d", jogador.getPotencia());
            System.out.printf("\nPossui arma?: %s", jogador.getArmado());
            System.out.println("\n");
        //
        }

        // 2. Interage com entidade (monstro) na célula de destino
        if (celulaDestino.temEntidade()) {
            EntidadeJogo monstro = celulaDestino.getEntidade();
            
            System.out.println("Logs de monstro encontrado:\n");
            
            System.out.println(jogador.getNome() + " encontra " + monstro.getNome() + "!");
            
            if (jogador.getArmado()) {
                System.out.println(jogador.getNome() + " está armado e ataca!");
                jogador.atacar(monstro); 
                System.out.println(monstro.getNome() + " fica intimidado e não revida!");

            } else {
                System.out.println(jogador.getNome() + " está desarmado e avança destemidamente!");
                int vidaMonstro = monstro.getPontosDeVidaAtuais();
                System.out.println(monstro.getNome() + " tem " + vidaMonstro + " pontos de vida.");
                System.out.println(jogador.getNome() + " recebe " + vidaMonstro + " de dano para derrotá-lo!");
                
                jogador.receberDano(vidaMonstro);

                if (jogador.estaVivo()) {
                    monstro.setPontosDeVidaAtuais(0); 
                }
                else {
                    System.out.println("O dano foi fatal!");
                }
            }
            
            // STATS APÓS LUTA
            System.out.printf("\nVida do jogador: %d/%d", jogador.getPontosDeVidaAtuais(), jogador.getPontosDeVidaMax());
            System.out.printf("\nDano/Pool do jogador: %d", jogador.getPotencia());
            System.out.printf("\nPossui arma?: %s", jogador.getArmado());
            System.out.println("\n");
            
            System.out.printf("Vida do monstro: %d/%d", monstro.getPontosDeVidaAtuais(), monstro.getPontosDeVidaMax());
            System.out.println("\n");
            //
            
            if (!monstro.estaVivo()) {
                System.out.println(jogador.getNome() + " derrotou " + monstro.getNome() + "!");
                celulaDestino.limparEntidade(); 

                if (jogador.estaVivo()) {
                    System.out.println(jogador.getNome() + " toma a posição do monstro!");
                    celulaDestino.setEntidade(jogador); 
                    celulaAtual.limparEntidade();       
                    this.posicaoJogador = proximaPosicao; 
                    return true; // Jogador moveu-se
                } else {
                    System.out.println("...mas não sobreviveu ao encontro.");
                    return false;
                }
            } else {
                System.out.println(jogador.getNome() + " não pode avançar!");
                return false; // Jogador não se moveu
            }
            
        } else {
            // 3. Célula livre, move o jogador
            celulaDestino.setEntidade(jogador); 
            celulaAtual.limparEntidade();       
            this.posicaoJogador = proximaPosicao; 
            System.out.println(jogador.getNome() + " se moveu para a posição " + proximaPosicao);
            
            return true; // Jogador moveu-se
        }
    }
    
    public ArrayList<Celula> getTabuleiro() {
        return this.tabuleiro;
    }
}