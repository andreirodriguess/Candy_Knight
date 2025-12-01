package Backend;

import Coletaveis.*; 
import Entidades.*;  
import java.util.ArrayList;
import java.util.Random; 

public class GameLogic {
    
    private boolean partidaAtiva;
    private int pontuacaoFinal = 0;
    
    // Controle de Aleatoriedade e Dificuldade
    private Random random;
    private int nivelDificuldade;
    private int numMovimentos;

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
        this.numMovimentos = 0;
        iniciarTabuleiro();
    }

    public void iniciarTabuleiro() {
        partidaAtiva = true;
        numMovimentos = 0;
        nivelDificuldade = 0; // Reseta dificuldade (nível 0)
        
        tabuleiro.clear();
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            tabuleiro.add(new Celula());
        }
        
        //por player na celula central
        tabuleiro.get(4).setEntidade(new Entidades.Cavaleiro("player"));
        this.posicaoJogador = 4;
        
        //
        for(int i = 0;i<TAMANHO_TABULEIRO;i++){
            if(i!=4){
                this.gerarConteudoAleatorio(tabuleiro.get(i));
            }
        }
        
        
        System.out.println("Novo jogo iniciado! Dificuldade Base: " + nivelDificuldade);
    }
    
    public void tentarMoverJogador(Direcao direcao) {
        if(this.partidaAtiva){
            int posAtual = this.posicaoJogador;
            int proximaPosicao = -1;
            
            // Variável para calcular onde seria a "costas" do jogador
            int posicaoOposta = -1; 

            // 1. Calcula destino e a posição oposta TEÓRICA (sem restrições de if)
            switch (direcao) {
                case CIMA:
                    if (posAtual < 3) {
                        System.out.println("Borda do mapa (Topo).");
                        return;
                    }
                    proximaPosicao = posAtual - 3;
                    posicaoOposta = posAtual + 3; // Oposta é sempre +3 (Baixo)
                    break;

                case BAIXO:
                    if (posAtual > 5) {
                        System.out.println("Borda do mapa (Baixo).");
                        return;
                    }
                    proximaPosicao = posAtual + 3;
                    posicaoOposta = posAtual - 3; // Oposta é sempre -3 (Cima)
                    break;

                case ESQUERDA:
                    if (posAtual % 3 == 0) {
                        System.out.println("Borda do mapa (Esquerda).");
                        return;
                    }
                    proximaPosicao = posAtual - 1;
                    posicaoOposta = posAtual + 1; // Oposta é sempre +1 (Direita)
                    break;

                case DIREITA:
                    if (posAtual % 3 == 2) {
                        System.out.println("Borda do mapa (Direita).");
                        return;
                    }
                    proximaPosicao = posAtual + 1;
                    posicaoOposta = posAtual - 1; // Oposta é sempre -1 (Esquerda)
                    break;
            }

            // 2. Processa o movimento
            if (proximaPosicao != -1) {
                // Tenta interagir (combate/item). Retorna true se o jogador efetivamente saiu da casa.
                boolean jogadorSaiuDaCasa = processarInteracao(proximaPosicao);
                
                if (jogadorSaiuDaCasa) {
                    //a cada 5 movimentos ele aumenta um nivel
                    this.numMovimentos++; 
                    this.nivelDificuldade = numMovimentos/5;
                    
                    Celula celulaAntiga = tabuleiro.get(posAtual); // A casa que ficou vazia
                    
                    // Verifica se a posição oposta existe DENTRO do tabuleiro (0 a 8)
                    boolean opostaExiste = (posicaoOposta >= 0 && posicaoOposta < TAMANHO_TABULEIRO);
                    
                    // Validação extra para Esquerda/Direita (para evitar que pule de linha, ex: 2 para 3)
                    if (opostaExiste && (direcao == Direcao.ESQUERDA || direcao == Direcao.DIREITA)) {
                        // Se estamos movendo horizontalmente, a oposta tem de estar na mesma linha da atual
                        int linhaAtual = posAtual / 3;
                        int linhaOposta = posicaoOposta / 3;
                        if (linhaAtual != linhaOposta) {
                            opostaExiste = false;
                        }
                    }

                    if (opostaExiste) {
                        // === LÓGICA 1: ESTEIRA (Puxar da borda) ===
                        // Se existe uma célula nas costas, puxamos o conteúdo dela para o centro
                        Celula celulaOpostaObj = tabuleiro.get(posicaoOposta);
                        
                        if (celulaOpostaObj.temEntidade()) {
                            celulaAntiga.setEntidade(celulaOpostaObj.getEntidade());
                            celulaOpostaObj.limparEntidade();
                        }
                        if (celulaOpostaObj.temItem()) {
                            celulaAntiga.setItem(celulaOpostaObj.getItem());
                            celulaOpostaObj.limparItem();
                        }
                        // E geramos novo conteúdo na borda que ficou vazia
                        gerarConteudoAleatorio(celulaOpostaObj);
                        System.out.println(">>> O cenário avançou! (Spawn na borda oposta)");
                        
                    } else {
                        // === LÓGICA 2: REPOSIÇÃO DIRETA (Borda para Meio) ===
                        // Se não existe célula válida nas costas (estamos na borda),
                        // apenas geramos novo conteúdo onde o jogador estava.
                        gerarConteudoAleatorioNovo(celulaAntiga);
                        System.out.println(">>> Nova ameaça surgiu onde você estava!");
                    }
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
            //se for uma espada e o jogador estiver armado, aumenta o dano
            if(item instanceof Coletaveis.EspadaDeAlcacuz && jogador.getArmado())
            {           
                int danoAtual = jogador.getArma().getAtaque();
                int danoItem = ((EspadaDeAlcacuz) item).getAtaque();
                jogador.getArma().setAtaque(danoAtual + danoItem,jogador);
                System.out.println("espada teve o dano aumentado em " + danoItem);
                
            }
            else{
                System.out.println(jogador.getNome() + " usou " + item.getNome());
                item.usar(jogador);
                celulaDestino.limparItem(); 
            }
        }

        // 2. Entidade (Monstro)
        if (celulaDestino.temEntidade()) {
            EntidadeJogo monstro = celulaDestino.getEntidade();
            System.out.println("Encontro: " + monstro.getNome() + " (Vida: " + monstro.getPontosDeVidaAtuais() + ")");
            
            if (jogador.getArmado()) {
                int vidaAtualMonstro = monstro.getPontosDeVidaAtuais();
                this.CombateArmado = true;
                //if(jogador.getArma().getAtaque() >=  monstro.getPontosDeVidaAtuais()){
                    // Desgaste da arma
                    jogador.atacar(monstro);
                    jogador.getArma().setAtaque(jogador.getArma().getAtaque() - vidaAtualMonstro, jogador);
                    
                    //garantir que a arma quebre se chegar a 0:
                    if(jogador.getArma().getAtaque()<=0){
                       jogador.getArma().quebrar(jogador);
                    }
                    
                //} else {
                    
                  //  jogador.getArma().quebrar(jogador);
                //}
                
                
            } else {
                this.CombateArmado = false;
                if(jogador.isEscudoDeTrocaAtivo()){
                    // Troca de lugar (Efeito do Escudo)
                    celulaAtual.setEntidade(monstro);
                    celulaDestino.setEntidade(jogador);
                    this.posicaoJogador = proximaPosicao;
                    jogador.desativarEscudoDeTroca();
                    return false; 
                } else {
                    // Dano direto
                    jogador.receberDano(monstro.getPontosDeVidaAtuais());
                    monstro.receberDano(monstro.getPontosDeVidaAtuais()); // Monstro explode
                    celulaDestino.limparEntidade();
                }
            }

            // Pós-Combate
            
            //verifica se o jogador morreu
            if(jogador.getPontosDeVidaAtuais() <=0){
                this.encerrarPartida();
                return false; 
            }
            
            //Verifica se o monstro foi morto
            if (!monstro.estaVivo()) {
                
                //Verifica se é uma pé de moleque, se for, passa para a fase 2
                if (monstro instanceof Entidades.PeDeMolequinho) {

                    // Em vez de limpar a célula, substituímos pelo monstro da Fase 2
                    celulaDestino.setEntidade(new Entidades.PeDeMolequinhoFase2());
                    System.out.println(">>> O Pé de Molequinho se dividiu em fragmentos!");
                    Entidades.PeDeMolequinhoFase2 fase2 = new Entidades.PeDeMolequinhoFase2();
                    fase2.fortalecer(this.nivelDificuldade); // Fortalece o novo monstro também
                    celulaDestino.setEntidade(fase2);
                    return false; // Jogador travado, não avança
                    // NÃO MOVE O JOGADOR (jogadorSaiuDaCasa continua false)
                    // O jogador fica onde está e agora tem um novo inimigo na frente dele.
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
                
                
            }
            
            
            return false; // Monstro vivo ou jogador morreu, não move

        } else {
            // 3. Célula Vazia
            celulaDestino.setEntidade(jogador); 
            celulaAtual.limparEntidade();       
            this.posicaoJogador = proximaPosicao; 
            return true; 
        }
    }
    
    
     private void gerarConteudoAleatorio(Celula celula) {
        
        int roll = random.nextInt(100);//Sorteia um valor
        
            if (roll < 50) { // 50% Monstro
                // Cria, fortalece e define na célula
                EntidadeJogo m = getMonstroAleatorio(this.nivelDificuldade);
                celula.setEntidade(m);
            } else if (roll <= 100) { // 50% Item
                // Cria, fortalece e define na célula
                Coletavel i = getItemAleatorio(this.nivelDificuldade);
                celula.setItem(i);
            } 
            else{//10% vazio
                System.out.println("A borda veio vazia\n");
            }
        
    }
     
    private void gerarConteudoAleatorioNovo(Celula celula) {
        int qtdEntidade = 0;
        int qtdItem = 0;
        
        //Contar quantas entidades ou itens tem no tabuleiro
        for (Celula C : this.tabuleiro){
            if(C.temEntidade()){
                qtdEntidade++;
            }
            else if(C.temItem()){
                qtdItem++;
            }
        }
        
        
        int roll = random.nextInt(100);//Sorteia um valor
        
        if(qtdEntidade>4){
            if (roll < 10) { // 10% Monstro
                // Cria, fortalece e define na célula
                EntidadeJogo m = getMonstroAleatorio(this.nivelDificuldade);
                celula.setEntidade(m);
            } else if (roll < 90) { // 80% Item
                // Cria, fortalece e define na célula
                Coletavel i = getItemAleatorio(this.nivelDificuldade);
                celula.setItem(i);
            } 
            else{//10% vazio
                System.out.println("A borda veio vazia\n");
            }
        }else if(qtdItem>4){
           if (roll < 80) { // 80% Monstro
                // Cria, fortalece e define na célula
                EntidadeJogo m = getMonstroAleatorio(this.nivelDificuldade);
                celula.setEntidade(m);
            } else if (roll < 90) { // 10% Item
                // Cria, fortalece e define na célula
                Coletavel i = getItemAleatorio(this.nivelDificuldade);
                celula.setItem(i);
            } 
            else{//10% vazio
                System.out.println("A borda veio vazia\n");
            }
        }else{
            if (roll < 45) { // 45% Monstro
                // Cria, fortalece e define na célula
                EntidadeJogo m = getMonstroAleatorio(this.nivelDificuldade);
                celula.setEntidade(m);
            } else if (roll < 90) { // 45% Item
                // Cria, fortalece e define na célula
                Coletavel i = getItemAleatorio(this.nivelDificuldade);
                celula.setItem(i);
            } 
            else{//10% vazio
                System.out.println("A borda veio vazia\n");
            }
        }
    }

    private EntidadeJogo getMonstroAleatorio(int nivel) {
        int tipo = random.nextInt(3);
        EntidadeJogo monstro;
        
        switch (tipo) {
            case 0: monstro = new UrsoDeGoma(); break;
            case 1: monstro = new SoldadoGengibre(); break;
            default: monstro = new PeDeMolequinho(); break;
        }
        
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
    

    public void encerrarPartida(){
       this.partidaAtiva = false;
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
