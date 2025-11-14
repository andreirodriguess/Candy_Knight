/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package candyknightvisualtest;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent; 
import java.awt.event.KeyListener; 
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;

public class TelaDoJogo extends JPanel implements KeyListener,ActionListener{
    //SIMULAndo ENTIDADES:
    
    ArrayList <Entidade> listaEntidade = new ArrayList<>();
    //cavaleiro:
    public Entidade player = new Entidade("Cavaleiro",10,5,4,0);//tipo jogador
    //inimigo:
    public Entidade inimigo = new Entidade("Urso de Goma",10,0,5,1);//tipo inimigo
    
    
    // NOVO: Variáveis do Game Loop e Animação
    private Timer gameLoopTimer;      // Nosso "coração" de 60 FPS
    private boolean estaAnimando = false; // Trava para não aceitar input durante a animação
    private int passoAnimacao = 8;    // Velocidade do movimento (pixels por frame)
    
    // --- VARIÁVEIS GENÉRICAS DE ANIMAÇÃO (COMO VOCÊ QUERIA) ---
    
    // Guarda QUAL entidade está sendo animada agora
    private Entidade entidadeEmAnimacao = null; 
    
    // Posição VISUAL (pixel) da entidade que está se movendo
    private double animPixelX;
    private double animPixelY;
    
    // Posição de DESTINO (pixel) da animação
    private int animDestinoX;
    private int animDestinoY;
    
    // O que fazer quando a animação terminar
    private int casoAnimacao = 0;
    private int indiceDestino;
    
    //-------------------------------------------------------------------------------------
    //Manipulação de imagens:
    private ArquivosImagem arquivoImagem = new ArquivosImagem();
    private Image sprite;//Guarda a imagem qeu está sendo utilizada no momento
    private Image spritePlayer;
    private Image spriteInimigo;
    
    //Construtor
    public TelaDoJogo(int larguraPainel,int alturaPainel){
        this.setLayout(null);
        this.setBackground(Color.darkGray);
        this.setPreferredSize(new Dimension(larguraPainel, alturaPainel));
        
        this.spritePlayer = this.getImage(arquivoImagem.cavaleiro);
        this.spriteInimigo = this.getImage(arquivoImagem.ursoDeGoma);
        
        //posiciona o grid no painel
        this.setGridPosicao(larguraPainel,alturaPainel);
        
        //Para o teclado
        this.setFocusable(true);
        this.addKeyListener(this);
        
        // Inicia o Game Loop
        gameLoopTimer = new Timer(16, this); // ~60 FPS
        gameLoopTimer.start();
    }
    
    
    //-------------------------------------------------------------------------------------
    //Atributos do Grid e cartas:
    //Grid:
    private final int gridSize = 3;
    private final int gridGap = 30;
    private int gridX;
    private int gridY;
    private final int larguraImagem = 90;
    private final int alturaImagem = 120;
    //Cartas:
    private final int cartaLargura = 90;
    private final int cartaAltura = 120;
    
    
    //---------Métodos que controlam a interface:---------
    private int caso; //para o controle de caso da interface(se vai mover, bater, etc...)
    private int direcao;
    
    
    private Image getImage(String nomeDaImagem){
        //Método que coleta as imagens
        Image imagem;
        
        try {
            imagem = ImageIO.read(getClass().getResource(nomeDaImagem));
            return imagem;
        } catch (IOException | IllegalArgumentException e) {
             // Adicionei IllegalArgumentException pois se a pasta estiver errada, o getResource retorna null
            System.err.println("Erro ao carregar imagem (verifique se a pasta 'Imagens' está correta em Source Packages): " + e.getMessage());
        }
        
        return null;
    }
    
    private void setGridPosicao(int larguraPainel, int alturaPainel){
        //posiciona o grid no painel
        int larguraTotalGrid = this.cartaLargura * this.gridSize + this.gridGap * (this.gridSize-1);
        int alturaTotalGrid = this.cartaAltura * this.gridSize + this.gridGap * (this.gridSize-1);
        
        this.gridX = (larguraPainel - larguraTotalGrid) / 2;
        this.gridY = (alturaPainel - alturaTotalGrid) / 2;
    }
    //posiciona uma nova entidade:
    public void setEntidadePosicao(Entidade E,int novaPosicao){
        E.posicao = novaPosicao;
    }
    
    public int getPosXFromCard(int indiceCarta){
        //A partir do índice da carta gera uma coordenada X no grid
        int coluna = this.getColunaDaCarta(indiceCarta); 
        
        int distanciaX = this.cartaLargura + this.gridGap;
        int posXPainel = gridX + coluna * (distanciaX);
        
        return posXPainel;
    }
    
    
    public int getPosYFromCard(int indiceCarta){
        //A partir do índice da carta gera uma coordenada Y no grid
        int linha = this.getLinhaDaCarta(indiceCarta); 
        
        int distanciaY = this.cartaAltura + this.gridGap; 
        int posYPainel = gridY + linha * (distanciaY);
                
        return posYPainel;
    }
    
    public boolean validarIndiceCarta(int indiceCarta){
        //Validar indice da carta
        int numeroDeCards = this.gridSize*this.gridSize;
        if (indiceCarta<0 || (indiceCarta>numeroDeCards - 1)){
         return false;   
        }
        return true;
    }
    
    public int getLinhaDaCarta(int indiceCarta){
        //Retorna indice da linha da matriz(y)
        int linha = (int)(indiceCarta / this.gridSize);
        return linha;
    }
    
    
    public int getColunaDaCarta(int indiceCarta){
        //Retorna indice da coluna da matriz (x)
        int coluna = (int)(indiceCarta % this.gridSize);
        return coluna;
    }
    
    
    public int getIndiceCarta(int coluna, int linha){
        //Retorna indice na matriz a partir da coluna e linha
        int indice = (linha * this.gridSize) + coluna;
        return indice;
    }
    //tenta mover o jogador em determinada direção
    public void moverJogador(int direcao){
        if (entidadeEmAnimacao != null) return;//Se houver uma entidade em movimento, não executar esse código
        
        int casoRetornado = solicitacaoMovimento(direcao);
        
        if (casoRetornado >= 1 && casoRetornado <= 3) {
            // Pega os pixels INICIAIS (Estação 0)
            int xInicial = getPosXFromCard(player.posicao);
            int yInicial = getPosYFromCard(player.posicao);
            
            // Pega os pixels FINAIS (Estação 1)
            // 'solicitacaoMovimento' já setou 'this.indiceDestino'
            int xFinal = getPosXFromCard(this.indiceDestino);
            int yFinal = getPosYFromCard(this.indiceDestino);
            
            // --- CONFIGURA A ANIMAÇÃO GENÉRICA ---
            this.entidadeEmAnimacao = player; // Trava a animação para esta entidade
            this.animPixelX = xInicial;       // Define o ponto de partida visual
            this.animPixelY = yInicial;       // Define o ponto de partida visual
            this.animDestinoX = xFinal;       // Define o alvo
            this.animDestinoY = yFinal;       // Define o alvo
            this.casoAnimacao = casoRetornado;// Guarda o que fazer no final
        
        } else if (casoRetornado == 4) {
            System.out.println("Ataca inimigo!");
        } else {
            System.out.println("Movimento bloqueado!");
        }
        
        repaint();
    }
    
    
    //------- Funções do paintComponent --------
    // "Coração" do Jogo - Chamado a cada 16ms
    @Override
    public void actionPerformed(ActionEvent e) {
        // 1. ATUALIZA O ESTADO DA ANIMAÇÃO (se houver uma)
        if (entidadeEmAnimacao != null) {
            
            boolean chegouX = false;
            boolean chegouY = false;

            // Move um "passo" em direção ao X de destino
            if (animPixelX < animDestinoX) {
                animPixelX += passoAnimacao;
                if (animPixelX >= animDestinoX) { animPixelX = animDestinoX; chegouX = true; }
            } else if (animPixelX > animDestinoX) {
                animPixelX -= passoAnimacao;
                if (animPixelX <= animDestinoX) { animPixelX = animDestinoX; chegouX = true; }
            } else {
                chegouX = true;
            }

            // Move um "passo" em direção ao Y de destino
            if (animPixelY < animDestinoY) {
                animPixelY += passoAnimacao;
                if (animPixelY >= animDestinoY) { animPixelY = animDestinoY; chegouY = true; }
            } else if (animPixelY > animDestinoY) {
                animPixelY -= passoAnimacao;
                if (animPixelY <= animDestinoY) { animPixelY = animDestinoY; chegouY = true; }
            } else {
                chegouY = true;
            }

            // Chegou ao destino?
            if (chegouX && chegouY) {
                // 1. Atualiza a posição LÓGICA da entidade
                entidadeEmAnimacao.posicao = this.indiceDestino;
                
                // 2. Executa a consequência (só se for o player)
                if (entidadeEmAnimacao == player) {
                    executarCasoAnimacao();
                }

                // 3. Libera a animação
                entidadeEmAnimacao = null;
                casoAnimacao = 0;
            }
        }
        
        // 2. REDESENHA A TELA
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        // 1. Limpa a tela
        super.paintComponent(g); 
        Graphics2D g2d = (Graphics2D) g;
        
        // 2. Desenha o Grid (NO FUNDO)
        pintarGrid(g2d);
        
        // --- 3. Desenha o Inimigo ---
        int inimigoX, inimigoY;
        if (entidadeEmAnimacao == inimigo) {
            // Pega a posição da animação (pixel a pixel)
            inimigoX = (int)animPixelX;
            inimigoY = (int)animPixelY;
        } else {
            // Pega a posição lógica (parado no slot)
            inimigoX = getPosXFromCard(inimigo.posicao);
            inimigoY = getPosYFromCard(inimigo.posicao);
        }
        
        // Bloco de desenho do Inimigo
        if (this.spriteInimigo != null) {
            g2d.drawImage(this.spriteInimigo, inimigoX, inimigoY, larguraImagem, alturaImagem, this);
        } else {
            // Fallback (se a imagem falhou ao carregar)
            g2d.setColor(Color.ORANGE);
            g2d.fillRect(inimigoX, inimigoY, cartaLargura, cartaAltura);
        }

        // --- 4. Desenha o Player (POR CIMA) ---
        int playerX, playerY;
        if (entidadeEmAnimacao == player) {
            // Pega a posição da animação
            playerX = (int)animPixelX;
            playerY = (int)animPixelY;
        } else {
            // Pega a posição lógica (parado no slot)
            playerX = getPosXFromCard(player.posicao);
            playerY = getPosYFromCard(player.posicao);
        }
        
        // Bloco de desenho do Player
        if (this.spritePlayer != null) {
            g2d.drawImage(this.spritePlayer, playerX, playerY, larguraImagem, alturaImagem, this);
        } else {
            // Fallback
            g2d.setColor(Color.RED);
            g2d.fillRect(playerX, playerY, cartaLargura, cartaAltura);
        }
    }
    
    // --- PINTAR O GRID NO PAINEL ---
    public void pintarGrid(Graphics2D g2d){
        
        g2d.setColor(Color.BLACK); 
        
        // Calcula a distância total de um slot para o próximo
        int distanciaX = this.cartaLargura + this.gridGap;
        int distanciaY = this.cartaAltura + this.gridGap;
        
        // Loop aninhado: para cada coluna...
        for (int col = 0; col < gridSize; col++) {
            // e para para cada linha
            for (int linha = 0; linha < gridSize; linha++) {
                // Calcula a posição (x, y) deste slot específico
                // (gridX/gridY é o ponto inicial da célula (0,0))
                int x = gridX + col * distanciaX;
                int y = gridY + linha * distanciaY;
                
                // Desenha o retângulo do slot
                g2d.fillRect(x, y, cartaLargura, cartaAltura);
            }
        }
    }
   
    private void executarCasoAnimacao() {
        switch (this.casoAnimacao) {
            case 1: 
                System.out.println("Chegou no índice: " + player.posicao); break;
                
            case 2: 
                System.out.println("Pegou moeda no índice: " + player.posicao); break;
            case 3: 
                System.out.println("Tomou dano do inimigo no índice: " + player.posicao);
                player.HP -= inimigo.HP;
                break;
        }
    }
    //--------Métodos da classe GameControl--------------
   //--------Métodos da classe GameControl--------------
    
    /**
     * Verifica a solicitação de movimento do jogador e retorna o que acontece.
     * 0 = Bloqueado (Limite do grid)
     * 1 = Movimento livre
     * 2 = Pegou moeda (Simulado)
     * 3 = Encontrou inimigo (Toma dano)
     * 4 = Encontrou inimigo (Ataca)
     */
    public int solicitacaoMovimento(int direcaoSelecionada) {
        
        // 1. Pega a posição LÓGICA atual do jogador
        int linhaAtual = getLinhaDaCarta(player.posicao);
        int colunaAtual = getColunaDaCarta(player.posicao);
        
        // 2. Calcula a posição LÓGICA de destino
        int linhaDestino = linhaAtual;
        int colunaDestino = colunaAtual;

        if (direcaoSelecionada == 0) linhaDestino--; // Cima
        if (direcaoSelecionada == 1) linhaDestino++; // Baixo
        if (direcaoSelecionada == 2) colunaDestino--; // Esquerda
        if (direcaoSelecionada == 3) colunaDestino++; // Direita

        // --- 3. INICIA AS VERIFICAÇÕES ---

        // CASO 0: Não Pode Mover (Limites do grid)
        // Checa se a linha OU a coluna estão fora do grid
        if (linhaDestino < 0 || linhaDestino >= gridSize || 
            colunaDestino < 0 || colunaDestino >= gridSize) {
            
            return 0; // Bloqueado
        }
        
        // Se chegou aqui, o movimento é DENTRO do grid.
        // Podemos calcular o índice final.
        int novoIndiceDestino = getIndiceCarta(colunaDestino, linhaDestino);
        
        // --- Checa o que há no destino ---

        // CASO 3 ou 4: Tem um inimigo
        if (novoIndiceDestino == inimigo.posicao) {
            
            // Salva o destino (para o caso de tomar dano)
            this.indiceDestino = novoIndiceDestino; 

            // Simulação da sua lógica:
            // if (player.temArma) {
            //    return 4; // Ataca (não move)
            // } else {
            //    return 3; // Toma dano (move)
            // }
            
            // Vamos simular que ele toma dano (caso 3)
            return 3; 
        }
        
        // CASO 2: Tem uma moeda (Simulação)
        // (No futuro, você teria um array ou lista de moedas para checar)
        // if (mapa[novoIndiceDestino].temMoeda) {
        //     this.indiceDestino = novoIndiceDestino;
        //     return 2;
        // }

        // CASO 1: Não há nada (Movimento livre)
        // Se não bateu nos limites e não achou um inimigo, o caminho está livre.
        this.indiceDestino = novoIndiceDestino;
        return 1;
    }
    
    
    
    //---------------------------------------------------------------------------------------
    //Implementação do Key Listener:
    
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    
    @Override
    public void keyPressed(KeyEvent e) {
        //Ignora input se alguma animação de movimento estiver rodando
        if (entidadeEmAnimacao != null) {
            return;
        }
        
        int codigoTecla = e.getKeyCode();

        // Este método é chamado sempre que uma tecla é pressionada para baixo
        switch (codigoTecla) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                if (!upPressed) { 
                    this.moverJogador(0);
                    upPressed = true;
                }
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                if (!downPressed) {
                    this.moverJogador(1);
                    downPressed = true;
                }
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                if (!leftPressed) {
                    this.moverJogador(2);
                    leftPressed = true;
                }
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                if (!rightPressed) {
                    this.moverJogador(3);
                    rightPressed = true;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int codigoTecla = e.getKeyCode();
        
        // Permite que as teclas seja pressionada de novo
        switch (codigoTecla) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                upPressed = false; 
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                downPressed = false;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    
}
