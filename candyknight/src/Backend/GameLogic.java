package Backend;

import Backend.Celula;
import Coletaveis.Coletavel;
import Entidades.Cavaleiro;
import Entidades.EntidadeJogo;
import Coletaveis.*; 
import Entidades.*;  
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random; 

/**
 * Classe central que gerencia o estado do jogo, o tabuleiro
 * e as interações entre as células.
 * Classe central que gerencia o estado do jogo.
 * Mecânica: Infinite Crawler (Tabuleiro móvel) com método fortalecer().
 */
public class GameLogic {
    
    private boolean partidaAtiva;
    private int pontuacaoFinal = 0;
    // +++ NOVO: Enum para Direções +++
    /**
     * Define as direções de movimento possíveis.
     * Usar um enum torna o código mais limpo e seguro.
     */
    
    // Controle de Aleatoriedade e Dificuldade
    private Random random;
    private int nivelDificuldade;

    public enum Direcao {
        CIMA, BAIXO, ESQUERDA, DIREITA
    }

    private ArrayList<Celula> tabuleiro;
    private final int TAMANHO_TABULEIRO = 9; // 3x3
    private int posicaoJogador;
    private boolean CombateArmado = false;

    public GameLogic() {
        this.tabuleiro = new ArrayList<>(TAMANHO_TABULEIRO);
        this.random = new Random(); 
        this.nivelDificuldade = 0;  
        iniciarTabuleiro();
    }

    /**
     * Prepara o tabuleiro para um novo jogo.
     */
    public void iniciarTabuleiro() {
        // 1. Cria 9 células vazias
        partidaAtiva = true;
        nivelDificuldade = 0; // Reseta dificuldade (nível 0)
        
        tabuleiro.clear();
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            tabuleiro.add(new Celula());
        }

        // 2. Adiciona o jogador (ex: posição 4, o centro)
        
        //por player na celula central
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
        //
        for(int i = 0;i<TAMANHO_TABULEIRO;i++){
            if(i!=4){
                this.gerarConteudoAleatorio(tabuleiro.get(i));
            }
        }
        
        
        System.out.println("Novo jogo iniciado! Dificuldade Base: " + nivelDificuldade);
    }
    

    // +++ MÉTODO NOVO (Lógica de Direção) +++
    
    
    public void tentarMoverJogador(Direcao direcao) {
        
        if(this.partidaAtiva){
            int proximaPosicao = -1;
            int posAtual = this.posicaoJogador;
            int proximaPosicao = -1;
            
            // Variáveis para a mecânica de "Spawn" (nas costas do jogador)
            int posicaoOposta = -1; 
            boolean opostaValida = false; 

            // 1. Calcula a posição de destino e verifica os limites do tabuleiro
            // 1. Calcula destino e a posição oposta
            switch (direcao) {
                case CIMA:
                    if (posAtual < 3) {
                        System.out.println("Não pode mover-se para cima. (Borda do tabuleiro)");
                        return; // Para a execução do método
                        System.out.println("Borda do mapa (Topo).");
                        return;
                    }
                    proximaPosicao = posAtual - 3;
                    if (posAtual <= 5) { posicaoOposta = posAtual + 3; opostaValida = true; }
                    break;

                case BAIXO:
                    if (posAtual > 5) {
                        System.out.println("Não pode mover-se para baixo. (Borda do tabuleiro)");
                        System.out.println("Borda do mapa (Baixo).");
                        return;
                    }
                    proximaPosicao = posAtual + 3; // Move uma linha para baixo
                    proximaPosicao = posAtual + 3;
                    if (posAtual >= 3) { posicaoOposta = posAtual - 3; opostaValida = true; }
                    break;

                case ESQUERDA:
                    if (posAtual % 3 == 0) {
                        System.out.println("Não pode mover-se para a esquerda. (Borda do tabuleiro)");
                        System.out.println("Borda do mapa (Esquerda).");
                        return;
                    }
                    proximaPosicao = posAtual - 1; // Move uma coluna para esquerda
                    proximaPosicao = posAtual - 1;
                    if (posAtual % 3 != 2) { posicaoOposta = posAtual + 1; opostaValida = true; }
                    break;

                case DIREITA:
                    if (posAtual % 3 == 2) {
                        System.out.println("Não pode mover-se para a direita. (Borda do tabuleiro)");
                        System.out.println("Borda do mapa (Direita).");
                        return;
                    }
                    proximaPosicao = posAtual + 1; // Move uma coluna para direita
                    proximaPosicao = posAtual + 1;
                    if (posAtual % 3 != 0) { posicaoOposta = posAtual - 1; opostaValida = true; }
                    break;
            }

            // 2. Se o cálculo foi bem-sucedido, processa a interação
            // 2. Processa o movimento
            if (proximaPosicao != -1) {
                processarInteracao(proximaPosicao);
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
    
    private void processarInteracao(int proximaPosicao) {
        
        if (proximaPosicao < 0 || proximaPosicao >= TAMANHO_TABULEIRO) {
            System.out.println("Movimento inválido.");
            return;
        }
    private boolean processarInteracao(int proximaPosicao) {
        if (proximaPosicao < 0 || proximaPosicao >= TAMANHO_TABULEIRO) return false;

        Celula celulaAtual = tabuleiro.get(posicaoJogador);
        Celula celulaDestino = tabuleiro.get(proximaPosicao);
        Cavaleiro jogador = (Cavaleiro) celulaAtual.getEntidade();
        

        // 1. Interage com item na célula de destino
        // 1. Item
        if (celulaDestino.temItem()) {
            Coletavel item = celulaDestino.getItem();
            System.out.println(jogador.getNome() + " encontrou e usou " + item.getNome() + "!");
            System.out.println(jogador.getNome() + " usou " + item.getNome());
            item.usar(jogador);
            celulaDestino.limparItem(); 
        }

        // 2. Interage com entidade (monstro) na célula de destino
        // 2. Entidade (Monstro)
        if (celulaDestino.temEntidade()) {
            EntidadeJogo monstro = celulaDestino.getEntidade();
            System.out.println(jogador.getNome() + " encontra " + monstro.getNome() + "!");
            System.out.println("Encontro: " + monstro.getNome() + " (Vida: " + monstro.getPontosDeVidaAtuais() + ")");
            
            // +++ INÍCIO MODIFICAÇÃO (REGRA 1: Combate) +++
            
            // O jogador só ataca se estiver armado
            if (jogador.getArmado()) {
                this.CombateArmado = true;
                jogador.atacar(monstro);
                if(jogador.getArma().getAtaque()<monstro.getPontosDeVidaAtuais()){
                    jogador.getArma().setAtaque(jogador.getArma().getAtaque()-monstro.getPontosDeVidaAtuais(),jogador);// NovoATK = AtaqueArma - VidaMonstro
                }else{
                    jogador.getArma().quebrar(jogador);//Quebra a arma do jogador
                if(jogador.getArma().getAtaque() < monstro.getPontosDeVidaAtuais()){
                    // Desgaste da arma
                    jogador.getArma().setAtaque(jogador.getArma().getAtaque() - monstro.getPontosDeVidaAtuais(), jogador);
                    
                    //garantir que a arma quebre se chegar a 0:
                    if(jogador.getArma().getAtaque()<=0){
                        jogador.getArma().quebrar(jogador);
                    }
                    
                } else {
                    
                    jogador.getArma().quebrar(jogador);
                }
                
                
            } else {
                //Temos 2 situações:
                //Caso tenha escudo, troca de lugar com o monstro
                this.CombateArmado = false;
                if(jogador.isEscudoDeTrocaAtivo()){
                    // Troca de lugar (Efeito do Escudo)
                    celulaAtual.setEntidade(monstro);
                    celulaDestino.setEntidade(jogador);
                    this.posicaoJogador = proximaPosicao;
                    jogador.desativarEscudoDeTroca();
                }else{
                    //caso não tenha escudo, monstro morre, jogador anda, jogador perde vida = 
                    return true; 
                } else {
                    // Dano direto
                    jogador.receberDano(monstro.getPontosDeVidaAtuais());
                    monstro.receberDano(monstro.getPontosDeVidaAtuais());
                    celulaDestino.limparEntidade();//limpa celula do monstro
//                    celulaDestino.setEntidade(jogador); // Coloca o jogador na nova célula
//                    celulaAtual.limparEntidade();  //Retira o jogador da celula antiga
                    monstro.receberDano(monstro.getPontosDeVidaAtuais()); // Monstro explode
                    celulaDestino.limparEntidade();
                }
//                celulaDestino.setEntidade(jogador);
//                this.posicaoJogador = proximaPosicao;
            }

            // Se o monstro foi derrotado
            // Pós-Combate
            
            //verifica se o jogador morreu
            if(jogador.getPontosDeVidaAtuais() <=0){
                this.encerrarPartida();
                return false; 
            }
            
            //Verifica se o monstro foi morto
            if (!monstro.estaVivo()) {
                
                // +++ INÍCIO MODIFICAÇÃO (REGRA 2: Movimento pós-vitória) +++
                
                // (Esta é a mesma lógica de movimento da secção "else" abaixo)
//                celulaDestino.setEntidade(jogador); // Coloca o jogador na nova célula
//                celulaAtual.limparEntidade();       // Limpa o jogador da célula antiga
//                this.posicaoJogador = proximaPosicao; // ATUALIZA a posição do jogador
                
                Entidades.MonstroDoce monstroDoce = (Entidades.MonstroDoce) monstro;

                // Cria a moeda
                Coletaveis.Moeda moedaDrop = new Coletaveis.Moeda();
                moedaDrop.setValor(monstroDoce.getRecompensaEmDinheiro()); // Define o valor baseado no monstro

                // Coloca a moeda na célula onde o monstro morreu
                celulaDestino.setItem(moedaDrop);
                System.out.println("O monstro deixou cair uma moeda de valor " + moedaDrop.getValor() + "!");
                
                    // VERIFICAÇÃO ESPECIAL: É O PÉ DE MOLEQUINHO?
                //Verifica se é uma pé de moleque, se for, passa para a fase 2
                if (monstro instanceof Entidades.PeDeMolequinho) {
<<<<<<< Updated upstream
                    System.out.println(">>> AVISO: O monstro se dividiu em fragmentos perigosos!");

                    // Em vez de limpar a célula, substituímos pelo monstro da Fase 2
                    celulaDestino.setEntidade(new Entidades.PeDeMolequinhoFase2());

                    // NÃO MOVE O JOGADOR (jogadorSaiuDaCasa continua false)
                    // O jogador fica onde está e agora tem um novo inimigo na frente dele.
                } else {
                        System.out.println(jogador.getNome() + " derrotou " + monstro.getNome() + "!");
                        //celulaDestino.limparEntidade(); // Limpa o monstro

                        // +++ INÍCIO MODIFICAÇÃO (REGRA 2: Movimento pós-vitória) +++
                        System.out.println(jogador.getNome() + " toma a posição do monstro!");

                        // (Esta é a mesma lógica de movimento da secção "else" abaixo)
                        //celulaDestino.setEntidade(jogador); // Coloca o jogador na nova célula
                        //celulaAtual.limparEntidade();       // Limpa o jogador da célula antiga
                        //this.posicaoJogador = proximaPosicao; // ATUALIZA a posição do jogador
                        // +++ FIM MODIFICAÇÃO (REGRA 2) +++
=======
                    System.out.println(">>> O Pé de Molequinho se dividiu em fragmentos!");
                    Entidades.PeDeMolequinhoFase2 fase2 = new Entidades.PeDeMolequinhoFase2();
                    fase2.fortalecer(this.nivelDificuldade); // Fortalece o novo monstro também
                    celulaDestino.setEntidade(fase2);
                    return false; // Jogador travado, não avança
                }
                
                //Se não for um pe de moleque:
                else if (monstro instanceof Entidades.MonstroDoce) {
                    
                    //e for um combate armado: O inimigo dropa uma moeda e o jogador não anda
                    if(this.CombateArmado){
                        // Drop de Moeda
                        Coletaveis.Moeda moeda = new Coletaveis.Moeda();
                        moeda.setValor(((Entidades.MonstroDoce)monstro).getRecompensaEmDinheiro());
                        celulaDestino.setItem(moeda);
                        // Remove o monstro da célula de destino (deixando apenas a moeda)
                        celulaDestino.limparEntidade(); 
                        return false; 
                    }
                    //e não for um combate armado: o jogador vai para a posição do inimigo
                    else{
                        // Remove o monstro da célula de destino
                        celulaDestino.limparEntidade();
                        //move o jogador pra célula de destnino
                        celulaDestino.setEntidade(jogador);
                        celulaAtual.limparEntidade();   
                        this.posicaoJogador = proximaPosicao;
                        return true; 
                    }
                }
                
                
>>>>>>> Stashed changes
            }
        }
            
            
            return false; // Monstro vivo ou jogador morreu, não move

        } else {
            // 3. Célula livre, move o jogador (Lógica normal de movimento)
            // 3. Célula Vazia
            celulaDestino.setEntidade(jogador); 
            celulaAtual.limparEntidade();       
            this.posicaoJogador = proximaPosicao; 
            System.out.println(jogador.getNome() + " se moveu para a posição " + proximaPosicao);
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
        } else if (roll < 8) { // 40% Item
            // Cria, fortalece e define na célula
            Coletavel i = getItemAleatorio(this.nivelDificuldade);
            celula.setItem(i);
        } 
    }

    private EntidadeJogo getMonstroAleatorio(int nivel) {
        int tipo = random.nextInt(3);
        EntidadeJogo monstro;
        
        if(!jogador.estaVivo()){
                this.encerrarPartida();
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
       this.pontuacaoFinal = ((Cavaleiro)this.tabuleiro.get(posicaoJogador).getEntidade()).getDinheiro();
       System.out.println("A PARTIDA ACABOU!!!");
    }
    
    public boolean getPartidaAtivo(){
        return this.partidaAtiva;
    }
    public int getPontucaoFinal(){
        return this.pontuacaoFinal;
       if (tabuleiro.get(posicaoJogador).getEntidade() instanceof Cavaleiro) {
           this.pontuacaoFinal = ((Cavaleiro)this.tabuleiro.get(posicaoJogador).getEntidade()).getDinheiro();
       }
       System.out.println("=== GAME OVER === Pontuação: " + pontuacaoFinal);
    }
    public int getPontuacaoAtual(){
        return ((Cavaleiro)this.tabuleiro.get(posicaoJogador).getEntidade()).getDinheiro();
    }
    public boolean getPartidaAtivo(){ return this.partidaAtiva; }
    public int getPontucaoFinal(){ return this.pontuacaoFinal; }
    public ArrayList<Celula> getTabuleiro() { return this.tabuleiro; }
}