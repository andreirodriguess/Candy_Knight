package candyknightvisualtest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent; 
import java.awt.event.KeyListener; 
import java.io.IOException;
import javax.imageio.ImageIO;

// 1. Adicionamos "implements KeyListener" aqui
public class TelaJogo extends JPanel implements KeyListener {
    
    private Image imagemJogador;
    
    //------CONSTRUTOR - INICIALIZAR PAINEL E CARREGAR AS IMAGENS QUE SERÃO UTILIZADAS----------------------------------------------------------------------

    public TelaJogo(int largura, int altura) {
        this.setLayout(null);
        this.setBackground(Color.DARK_GRAY);
        this.setPreferredSize(new Dimension(largura, altura));

        try {
            imagemJogador = ImageIO.read(getClass().getResource("/Imagens/player.png"));    
        } catch (IOException | IllegalArgumentException e) {
             // Adicionei IllegalArgumentException pois se a pasta estiver errada, o getResource retorna null
            System.err.println("Erro ao carregar imagem (verifique se a pasta 'Imagens' está correta em Source Packages): " + e.getMessage());
        }
        
        // NOVO: Calcula e define a posição do grid e do jogador
        this.SetGridPos(largura,altura);
        
        // Coloca o jogador na primeira carta
        this.xJogador = this.gridX; 
        this.yJogador = this.gridY;
        this.IndiceJogador = 0;
        
        
        this.setFocusable(true);
        this.addKeyListener(this);
        
    }

    //------ATUALIZAR GRID DO JOGO E PINTAR A TELA----------------------------------------------------------------------
    //Variaveis da entidade:
    private int xJogador;
    private int yJogador;
    private int IndiceJogador;
    
    private int larguraJogador = 128;
    private int alturaJogador = 128;
    private int passo = 10;//Passo no qual um card do grid se move
    
    //Definir posições do grid
    int gridSize = 3;
    int gridX;
    int gridY;
    int gridGap = 64;
    int cardLargura = 128;
    int cardAltura = 128;
    
    
    
    
    //calcular posição da primeira carta:
    public void SetGridPos(int painelLargura, int painelAltura){
        //dimensoes do grid
        int larguraTotalGrid = this.cardLargura * this.gridSize + this.gridGap * (this.gridSize-1);
        int alturaTotalGrid = this.cardAltura * this.gridSize + this.gridGap * (this.gridSize-1);
        
        this.gridX = (painelLargura - larguraTotalGrid) / 2;
        this.gridY = (painelAltura - alturaTotalGrid) / 2;
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        // 1. Prepara a tela (limpa com a cor de fundo)
        super.paintComponent(g); 
        Graphics2D g2d = (Graphics2D) g;
        
        // --- 2. PINTAR O GRID NO PAINEL ---
        g2d.setColor(Color.BLACK); // Define a cor dos "slots" do grid
        
        // Calcula a distância total de um slot para o próximo (card + espaço)
        int distanciaX = this.cardLargura + this.gridGap;
        int distanciaY = this.cardAltura + this.gridGap;
        
        // Loop aninhado: para cada coluna...
        for (int col = 0; col < gridSize; col++) {
            // ...passe por cada linha
            for (int linha = 0; linha < gridSize; linha++) {
                
                // Calcula a posição (x, y) deste slot específico
                // (gridX/gridY é o ponto inicial da célula (0,0))
                int x = gridX + col * distanciaX;
                int y = gridY + linha * distanciaY;
                
                // Desenha o retângulo do slot
                g2d.fillRect(x, y, cardLargura, cardAltura);
            }
        }
        //Pintar o Player
            if (imagemJogador != null) {
                g2d.drawImage(imagemJogador, xJogador, yJogador, larguraJogador, alturaJogador, this);
            }else {
                g2d.setColor(Color.RED);
                g2d.fillRect(xJogador, yJogador, larguraJogador, alturaJogador);
            }
        
        
    }
    
    
    public void moverJogador(int deltaX, int deltaY) {
        xJogador += deltaX;
        yJogador += deltaY;
        repaint();
    }
    public void setPosX(int X) {
        xJogador = X;
        repaint();
    }
    public void setPosY(int Y) {
        yJogador = Y;
        repaint();
    }
    public int getPosXFromCard(int Card){
        //posicao No Grid:
        int posXGrid = (int)(Card/this.gridSize);
        //Espaço entre as coordenadas dos cards
        int distanciaX = this.cardLargura + this.gridGap;
        //Converter para coordenadas no painel
        int posXPainel = gridX + posXGrid*(distanciaX);
        
        return posXPainel;
    }
    public int getPosYFromCard(int Card){
        //posicao No Grid:
        int posYGrid = (int)(Card%this.gridSize);
        //Espaço entre as coordenadas dos cards
        int distanciaY = this.cardAltura + this.gridGap; 
        //Converter para coordenadas no painel
        int posYPainel = gridY + posYGrid*(distanciaY);
            
        return posYPainel;
    }
    public boolean validarIndiceCard(int Card){
        int numeroDeCards = this.gridSize*this.gridSize;
        if (Card<0 || (Card>numeroDeCards - 1)){
         return false;   
        }
        return true;
    }
    public int getLinhaFromCard(int Card){
        int linha = (int)(Card%this.gridSize);
        return linha;
    }
    public int getColunaFromCard(int Card){
        int coluna = (int)(Card/this.gridSize);
        return coluna;
    }
    public int getIndiceCard(int coluna,int linha){
        int indice = coluna * this.gridSize + linha;
        return indice;
    }
    
    //Mover um Card do grid para outra posicao
    public void moverCima(int Card){
        int linha = this.getLinhaFromCard(Card);
        int coluna =  this.getColunaFromCard(Card);
        
        if ( linha> 0) {
            linha--;//Subiu de linha
            int CardDestino = getIndiceCard(coluna,linha);
            
            this.moverCard(Card, CardDestino);
            this.IndiceJogador = CardDestino;
        }
    }
    public void moverBaixo(int Card){
        int linha = this.getLinhaFromCard(Card);
        int coluna =  this.getColunaFromCard(Card);
        
        if ( linha < (this.gridSize - 1)) {
            linha++;//Desceu de linha
            int CardDestino = getIndiceCard(coluna,linha);
            
            this.moverCard(Card, CardDestino);
            this.IndiceJogador = CardDestino;
        }
    }
    public void moverEsquerda(int Card){
        int linha = this.getLinhaFromCard(Card);
        int coluna =  this.getColunaFromCard(Card);
        
        if ( coluna > 0) {
            coluna--;//foi para a coluna à esquerda
            int CardDestino = getIndiceCard(coluna,linha);
            
            this.moverCard(Card, CardDestino);
            this.IndiceJogador = CardDestino;
        }
    }
    public void moverDireita(int Card){
        int linha = this.getLinhaFromCard(Card);
        int coluna =  this.getColunaFromCard(Card);
        
        if ( coluna < (this.gridSize - 1)) {
            coluna++;//foi para a coluna à direita
            int CardDestino = getIndiceCard(coluna,linha);
            
            this.moverCard(Card, CardDestino);
            this.IndiceJogador = CardDestino;
        }
    }
    
    
    public void moverCard(int CardA, int CardB) {
        //Validar indice do Card A e Card B
        if(this.validarIndiceCard(CardA) && this.validarIndiceCard(CardB)){
            int xi = this.getPosXFromCard(CardA);
            int yi = this.getPosYFromCard(CardA);
            int xf = this.getPosXFromCard(CardB);
            int yf = this.getPosYFromCard(CardB);
            
            //movimento em x
            if(xi!=xf){
                for(int i=xi;Math.abs(i-xf)>passo;){
                    if(xf>xi){
                        this.moverJogador(passo, 0);
                        i+=passo;
                        repaint();
                    }else{
                        this.moverJogador(-passo, 0);    
                        i-=passo;
                        repaint();
                    }
                }
                this.setPosX(xf);
                    repaint();
            }
            
            //movimento em y
            if(yi!=yf){
                for(int i=yi;Math.abs(i-yf)>passo;){
                    if(yf>yi){
                        this.moverJogador(0, passo);
                        i+=passo;
                        repaint();
                    }else{
                        this.moverJogador(0, -passo);    
                        i-=passo;
                        repaint();
                    }
                    
                }
                this.setPosY(yf);
                    repaint();
            }
            
        }
    }

    
    
     //------ATRIBUTOS E MÉTODOS PARA LER AS TECLAS----------------------------------------------------------------------

    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    
    @Override
    public void keyPressed(KeyEvent e) {
        
        int codigoTecla = e.getKeyCode();

        // Este método é chamado sempre que uma tecla é pressionada para baixo
        switch (codigoTecla) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                if (!upPressed) { 
                    moverCima(this.IndiceJogador);
                    upPressed = true;
                }
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                if (!downPressed) {
                    moverBaixo(this.IndiceJogador);
                    downPressed = true;
                }
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                if (!leftPressed) {
                    moverEsquerda(this.IndiceJogador);
                    leftPressed = true;
                }
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                if (!rightPressed) {
                    moverDireita(this.IndiceJogador);
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