package Backend;

import Backend.Celula;
import Coletaveis.Coletavel;
import Entidades.Cavaleiro;
import Entidades.EntidadeJogo;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe central que gerencia o estado do jogo, o tabuleiro
 * e as interações entre as células.
 */
public class GameLogic {
    
    
    private boolean partidaAtiva;
    private int pontuacaoFinal = 0;
    // +++ NOVO: Enum para Direções +++
    /**
     * Define as direções de movimento possíveis.
     * Usar um enum torna o código mais limpo e seguro.
     */
    public enum Direcao {
        CIMA,//0
        BAIXO,//1
        ESQUERDA,//2
        DIREITA//3
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
        partidaAtiva = true;
        
        tabuleiro.clear();
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            tabuleiro.add(new Celula());
        }

        // 2. Adiciona o jogador (ex: posição 4, o centro)
        tabuleiro.get(4).setEntidade(new Entidades.Cavaleiro("player"));
        this.posicaoJogador = 4;
        

        // 3. Adiciona monstros (ex: posições 1 e 7)
        tabuleiro.get(1).setEntidade(new Entidades.UrsoDeGoma());
        tabuleiro.get(7).setEntidade(new Entidades.SoldadoGengibre());
        tabuleiro.get(8).setEntidade(new Entidades.PeDeMolequinho());

        // 4. Adiciona itens (ex: posições 3 e 5)
        // (Adicionei a EspadaDeAlcacuz para testar a tua correção!)
        tabuleiro.get(3).setItem(new Coletaveis.EscudoDeGoma()); 
        tabuleiro.get(5).setItem(new Coletaveis.PocaoDeCalda());
    }
    

    // +++ MÉTODO NOVO (Lógica de Direção) +++
    
    
    public void tentarMoverJogador(Direcao direcao) {
        
        if(this.partidaAtiva){
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
                if(jogador.getArma().getAtaque()<monstro.getPontosDeVidaAtuais()){
                    jogador.getArma().setAtaque(jogador.getArma().getAtaque()-monstro.getPontosDeVidaAtuais(),jogador);// NovoATK = AtaqueArma - VidaMonstro
                }else{
                    jogador.getArma().quebrar(jogador);//Quebra a arma do jogador
                }
            } else {
                //Temos 2 situações:
                //Caso tenha escudo, troca de lugar com o monstro
                if(jogador.isEscudoDeTrocaAtivo()){
                    celulaAtual.setEntidade(monstro);
                    jogador.desativarEscudoDeTroca();
                }else{
                    //caso não tenha escudo, monstro morre, jogador anda, jogador perde vida = 
                    jogador.receberDano(monstro.getPontosDeVidaAtuais());
                    monstro.receberDano(monstro.getPontosDeVidaAtuais());
                    celulaDestino.limparEntidade();//limpa celula do monstro
                    celulaDestino.setEntidade(jogador); // Coloca o jogador na nova célula
                    celulaAtual.limparEntidade();  //Retira o jogador da celula antiga
                }
                celulaDestino.setEntidade(jogador);
                this.posicaoJogador = proximaPosicao;
                System.out.println(jogador.getNome() + " está desarmado e não pode atacar!");
            }

            // Se o monstro foi derrotado
            if (!monstro.estaVivo()) {
                
                    // VERIFICAÇÃO ESPECIAL: É O PÉ DE MOLEQUINHO?
                if (monstro instanceof Entidades.PeDeMolequinho) {
                    System.out.println(">>> AVISO: O monstro se dividiu em fragmentos perigosos!");

                    // Em vez de limpar a célula, substituímos pelo monstro da Fase 2
                    celulaDestino.setEntidade(new Entidades.PeDeMolequinhoFase2());

                    // NÃO MOVE O JOGADOR (jogadorSaiuDaCasa continua false)
                    // O jogador fica onde está e agora tem um novo inimigo na frente dele.
                } else {
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
        }
            
            
        } else {
            // 3. Célula livre, move o jogador (Lógica normal de movimento)
            celulaDestino.setEntidade(jogador); 
            celulaAtual.limparEntidade();       
            this.posicaoJogador = proximaPosicao; 
            System.out.println(jogador.getNome() + " se moveu para a posição " + proximaPosicao);
        }
        
        if(!jogador.estaVivo()){
                this.encerrarPartida();
        }
            
    }
    
    public void encerrarPartida(){
       this.partidaAtiva = false;
       this.pontuacaoFinal = ((Cavaleiro)this.tabuleiro.get(posicaoJogador).getEntidade()).getDinheiro();
       System.out.println("A PARTIDA ACABOU!!!");
    }
    
    public boolean getPartidaAtivo(){
        return this.partidaAtiva;
    }
    public int getPontucaoFinal(){
        return this.pontuacaoFinal;
    }
    public ArrayList<Celula> getTabuleiro() {
        return this.tabuleiro;
    }
}