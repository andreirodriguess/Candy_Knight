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

    public GameLogic() {
        this.tabuleiro = new ArrayList<>(TAMANHO_TABULEIRO);
        // +++ NOVO: Inicializa o gerador aleatório +++
        this.random = new Random(); 
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

        // +++ INÍCIO DA NOVA LÓGICA ALEATÓRIA +++

        // 3. Define a quantidade de monstros e itens
        // (Podes alterar estes valores para balancear o jogo)
        int numMonstros = 2;
        int numItens = 2; // "Quase igual" (neste caso, igual)

        // 4. Cria uma lista de posições livres para spawn
        List<Integer> posicoesLivres = new ArrayList<>();
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            posicoesLivres.add(i);
        }
        
        // Remove a posição do jogador da lista de spawn
        // Usamos Integer.valueOf() para remover o OBJETO 4, e não o item no ÍNDICE 4.
        posicoesLivres.remove(Integer.valueOf(this.posicaoJogador)); 

        // 5. Embaralha a lista de posições livres
        Collections.shuffle(posicoesLivres, this.random);

        // 6. Adiciona os monstros em posições aleatórias
        int monstrosAdicionados = 0;
        while (monstrosAdicionados < numMonstros && !posicoesLivres.isEmpty()) {
            
            // Pega e remove a primeira posição da lista embaralhada
            int posParaSpawn = posicoesLivres.remove(0); 
            
            MonstroDoce monstro = getMonstroAleatorio(); // Pega um monstro aleatório
            tabuleiro.get(posParaSpawn).setEntidade(monstro);
            
            monstrosAdicionados++;
        }
        
        // 7. Adiciona os itens em posições aleatórias
        int itensAdicionados = 0;
        while (itensAdicionados < numItens && !posicoesLivres.isEmpty()) {
            
            // Pega e remove a próxima posição da lista
            int posParaSpawn = posicoesLivres.remove(0); 
            
            Coletavel item = getItemAleatorio(); // Pega um item aleatório
            
            // (A nossa lógica já garante que um item não cai sobre um monstro,
            // pois estamos a usar e remover posições da mesma lista 'posicoesLivres')
            
            tabuleiro.get(posParaSpawn).setItem(item);
            
            itensAdicionados++;
        }
        
        System.out.println("Tabuleiro iniciado com " + monstrosAdicionados + " monstros e " + itensAdicionados + " itens.");
        // +++ FIM DA NOVA LÓGICA ALEATÓRIA +++
    }

    // +++ NOVO MÉTODO AUXILIAR +++
    /**
     * Cria e retorna uma instância de um MonstroDoce aleatório.
     * @return Um MonstroDoce (Urso, Soldado ou PeDeMolequinho).
     */
    private MonstroDoce getMonstroAleatorio() {
        // Gera um número aleatório entre 0, 1 ou 2
        int tipoMonstro = random.nextInt(3); 

        switch (tipoMonstro) {
            case 0:
                return new UrsoDeGoma();
            case 1:
                return new SoldadoGengibre();
            case 2:
            default: // 'default' garante que algo é sempre retornado
                return new PeDeMolequinho();
        }
    }
    
    // +++ NOVO MÉTODO AUXILIAR +++
    /**
     * Cria e retorna uma instância de um Coletavel aleatório.
     * Isto inclui armas (Espada) e outros itens (Poção, Escudo).
     * @return Um Coletavel (Pocao, Espada ou Escudo).
     */
    private Coletavel getItemAleatorio() {
        // Gera um número aleatório entre 0, 1 ou 2
        int tipoItem = random.nextInt(2); 
        
        switch (tipoItem) {
            case 0:
                return new PocaoDeCalda();
            case 1:
            default:
                return new EspadaDeAlcacuz(); // A "arma"
//            case 2:
//            default:
//                return new EscudoDeGoma();
        }
    }
    
    private void gerarConteudoAleatorio(Celula celula) {
        System.out.println("funcao foi chamada(debugging");

        // Gera um número aleatório entre 0 e 4 (total de 5 resultados)
        int tipoSpawn = random.nextInt(5); 

        switch (tipoSpawn) {
            case 0: // 40% de chance (Casos 0 e 1)
            case 1:
                // SPAWN MONSTRO
                celula.setEntidade(getMonstroAleatorio());
                celula.limparItem(); // Garante que não tem item
                System.out.println("...um Monstro apareceu na borda!");
                break;
                
            case 2: // 40% de chance (Casos 2 e 3)
            case 3:
                // SPAWN ITEM
                celula.setItem(getItemAleatorio());
                celula.limparEntidade(); // Garante que não tem monstro
                System.out.println("...um Item apareceu na borda!");
                break;
                
            case 4: // 20% de chance (Caso 4)
            default:
                // SPAWN VAZIO
                celula.limparEntidade();
                celula.limparItem();
                System.out.println("...a borda veio vazia.");
                break;
        }
    }


    // ... (O resto da classe 'GameLogic.java' continua aqui) ...
    // (O teu método 'tentarMoverJogador' modificado anteriormente está perfeito
    // e não precisa ser alterado por esta nova funcionalidade)
    
    public int tentarMoverJogador(Direcao direcao) {
        int proximaPosicao = -1;
        int posAtual = this.posicaoJogador;
        
        // +++ MUDANÇA: lógica para encontrar a célula oposta +++
        int posicaoOposta = -1; 
        boolean opostaValida = false; // Flag para saber se a célula oposta existe

        // 1. Calcula a posição de destino e a posição oposta
        switch (direcao) {
            case CIMA:
                if (posAtual < 3) {
                    System.out.println("Não pode mover-se para cima. (Borda do tabuleiro)");
                    return -1; // Falha (borda)
                }
                proximaPosicao = posAtual - 3;
                // Oposto é a célula de BAIXO
                if (posAtual <= 5) { // Se não está na última linha
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
                // Oposto é a célula de CIMA
                if (posAtual >= 3) { // Se não está na primeira linha
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
                // Oposto é a célula da DIREITA
                if (posAtual % 3 != 2) { // Se não está na borda direita
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
                // Oposto é a célula da ESQUERDA
                if (posAtual % 3 != 0) { // Se não está na borda esquerda
                    posicaoOposta = posAtual - 1;
                    opostaValida = true;
                }
                break;
        }

        // 2. Se o cálculo foi bem-sucedido, processa a interação
        if (proximaPosicao != -1) {
            
            // +++ MUDANÇA: Chamamos a nova processarInteracao() +++
            // Ela agora nos diz se o jogador realmente mudou de lugar.
            boolean jogadorMoveu = processarInteracao(proximaPosicao, posAtual);

            // Pega o jogador (necessário para decrementar buffs)
            // Nota: a posição do jogador (this.posicaoJogador) pode ter mudado
            // dentro de processarInteracao.
            Cavaleiro jogador = (Cavaleiro) tabuleiro.get(this.posicaoJogador).getEntidade();
            if (jogador != null) {
                jogador.decrementarDuracaoBuffs();
            }


            // 3. Se o jogador se moveu, executamos a lógica de "puxar"
            if (jogadorMoveu) {
                
                if (opostaValida) {
                    // Pega a célula oposta (de onde vamos puxar)
                    Celula celulaOposta = tabuleiro.get(posicaoOposta);
                    // Pega a célula que o jogador acabou de deixar (para onde vamos puxar)
                    Celula celulaDestinoPuxada = tabuleiro.get(posAtual); 

                    // Pega o que tem na célula oposta
                    EntidadeJogo entidadeOposta = celulaOposta.getEntidade();
                    Coletavel itemOposto = celulaOposta.getItem();

                    // Move para a célula antiga do jogador
                    celulaDestinoPuxada.setEntidade(entidadeOposta);
                    celulaDestinoPuxada.setItem(itemOposto);

                    // --- CORREÇÃO ---
                    // Limpa a célula oposta ANTES de gerar novo conteúdo
                    celulaOposta.limparEntidade();
                    celulaOposta.limparItem();
                    // --- FIM DA CORREÇÃO ---
                    
                    gerarConteudoAleatorio(celulaOposta); // Agora geras na célula limpa
                    
                    System.out.println("Célula " + posicaoOposta + " foi puxada para a posição " + posAtual);
                    
                    // Retorna o ID da célula que foi movida
                    return posicaoOposta; 
                } else {
                    // O jogador moveu, mas não havia célula oposta válida
                    return -2; // Sucesso, sem "puxar"
                }
                
            } else {
                // O jogador não se moveu (ex: bloqueado por monstro)
                return -1; // Falha (bloqueado)
            }
        }
        
        // Se proximaPosicao == -1 (cálculo falhou lá em cima)
        return -1;
    }
    
    private boolean processarInteracao(int proximaPosicao, int posicaoAntiga) {
        
        
        if (proximaPosicao < 0 || proximaPosicao >= TAMANHO_TABULEIRO) {
            System.out.println("Movimento inválido.");
            return false; // +++ MUDANÇA +++
        }

        Celula celulaAtual = tabuleiro.get(posicaoAntiga); // +++ MUDANÇA +++
        Celula celulaDestino = tabuleiro.get(proximaPosicao);

        Cavaleiro jogador = (Cavaleiro) celulaAtual.getEntidade();
        
        // STATS
            System.out.printf("Vida do jogador: %d/%d", jogador.getPontosDeVidaAtuais(), jogador.pontosDeVidaMax());
            System.out.printf("\nDano do jogador: %d", jogador.getPotencia());
            System.out.printf("\nPossui arma?: %s", jogador.getArmado());
            if (jogador.getArmado()) {
                System.out.printf("\nUsos: %d", jogador.getDurabilidadeArma());
            }
            System.out.println("\n");
        //

        // 1. Interage com item na célula de destino
        if (celulaDestino.temItem()) {
            Coletavel item = celulaDestino.getItem();
            System.out.println(jogador.getNome() + " encontrou e usou " + item.getNome() + "!");
            item.usar(jogador);
            celulaDestino.limparItem(); 
            
            // STATS
            System.out.printf("\nVida do jogador: %d/%d", jogador.getPontosDeVidaAtuais(), jogador.pontosDeVidaMax());
            System.out.printf("\nDano do jogador: %d", jogador.getPotencia());
            System.out.printf("\nPossui arma?: %s", jogador.getArmado());
            if (jogador.getArmado()) {
                System.out.printf("\nUsos: %d", jogador.getDurabilidadeArma());
            }
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
                
            } else {
                System.out.println(jogador.getNome() + " está desarmado e não pode atacar sem receber dano!");
            }

            
            
            if (monstro.estaVivo()) {
                if (!jogador.getArmado()) {
                    System.out.println(monstro.getNome() + " revida o encontro!");
                    monstro.atacar(jogador);
                } else {
                    System.out.println(monstro.getNome() + " fica intimidado pela arma do jogador!");
                }
            }

            // Bug corrigido do jogador só poder atacar com arma, sem arma ele ataca e o monstro ataca também
            jogador.atacar(monstro);
            jogador.decrementarDurabilidadeArma();
            
            // STATS APÓS LUTA
            System.out.printf("\nVida do jogador: %d/%d", jogador.getPontosDeVidaAtuais(), jogador.pontosDeVidaMax());
            System.out.printf("\nDano do jogador: %d", jogador.getPotencia());
            System.out.printf("\nPossui arma?: %s", jogador.getArmado());
            if (jogador.getArmado()) {
                System.out.printf("\nUsos: %d", jogador.getDurabilidadeArma());
            }
            System.out.println("\n");
            
            System.out.printf("Vida do monstro: %d/%d", monstro.getPontosDeVidaAtuais(), monstro.pontosDeVidaMax());
            System.out.println("\n");
            //
            
            if (!monstro.estaVivo()) {
                System.out.println(jogador.getNome() + " derrotou " + monstro.getNome() + "!");
                celulaDestino.limparEntidade(); 
                
                System.out.println(jogador.getNome() + " toma a posição do monstro!");
                
                celulaDestino.setEntidade(jogador); 
                celulaAtual.limparEntidade();       
                this.posicaoJogador = proximaPosicao; // ATUALIZA a posição do jogador
                
                return true; // +++ MUDANÇA: Jogador moveu-se +++
            } else {
                // +++ MUDANÇA: Monstro está vivo, jogador não se moveu +++
                System.out.println(jogador.getNome() + " não pode avançar!");
                return false; 
            }
            
        } else {
            // 3. Célula livre, move o jogador (Lógica normal de movimento)
            celulaDestino.setEntidade(jogador); 
            celulaAtual.limparEntidade();       
            this.posicaoJogador = proximaPosicao; 
            System.out.println(jogador.getNome() + " se moveu para a posição " + proximaPosicao);
            
            return true; // +++ MUDANÇA: Jogador moveu-se +++
        }
    }
    
    public ArrayList<Celula> getTabuleiro() {
        return this.tabuleiro;
    }
}