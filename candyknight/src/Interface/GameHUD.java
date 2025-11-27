
package Interface;

import Backend.*;
import Entidades.*;
import Coletaveis.*;


import java.awt.Color;
import java.awt.*;
import java.util.List;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;


public class GameHUD extends JPanel implements KeyListener,ActionListener{
    
    private ScreenGame sg;
    
    private ArquivosImagem arquivo = new ArquivosImagem();
    private GameLogic gameControl = new GameLogic();
    private List<Celula> tabuleiro = new ArrayList<>();
    
    
    //Para desenhar na tela 
    private int X;
    private int Y;
    Image sprite;
    
    //Variáveis auxiliares para deslocamento das cartas:
    private boolean animaDeslocamento = false;//Variável que sinaliza se está tendo uma animação de deslocamento
    private int Xatual;//Pos atual
    private int Yatual;
    private int Xi;//Pos inicial
    private int Yi;
    private int Xf;//Pos final
    private int Yf;
    
    private GameLogic.Direcao direcaoMov = null;
    private final int passo = 10;//Velocidade com que os sprites se movem
    private boolean chegouX;//Sinaliza que chegou no destino X na anim
    private boolean chegouY;//Sinaliza que chegou no destino Y naanimação
    
    
    
    public GameHUD(int larguraPainel,int alturaPainel, ScreenGame sg) {//Passo Screengame como parametro pra poder acessar as variáveis
        this.sg = sg;
        Timer gameTimer = new Timer(16, (ActionListener) this); 
        gameTimer.start();
        
        
        //Conrtolar teclado
        this.setFocusable(true); 
        this.addKeyListener(this);
    }

    @Override//Vai rodar a cada 16ms (60 FPS)
    public void actionPerformed(ActionEvent e) {
        
        if (this.direcaoMov != null) {
            this.gameControl.tentarMoverJogador(this.direcaoMov);
            this.direcaoMov = null;
        }
        
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        Graphics2D g2d = (Graphics2D) g;
        
        this.mostrarPontuacao(g2d);
        
        //Se a partida estiver ativa
        if(this.gameControl.getPartidaAtivo() == true){
            
            this.mostrarCelulas(g2d);
        }else{
            this.gameOverScreen(g2d);
        }
    }
    
    //métodos para desenho na janela
    private void mostrarCelulas(Graphics2D g2d){
        this.tabuleiro = this.gameControl.getTabuleiro();
        
        for(int i=0;i<9;i++){
            if(this.tabuleiro.get(i).temEntidade()){
                this.desenharEntidade(this.tabuleiro.get(i).getEntidade(),i, g2d);
            }else if(this.tabuleiro.get(i).temItem()){
                this.desenharItem(this.tabuleiro.get(i).getItem(),i,g2d);
            }
        }
    }
    private void mostrarPontuacao(Graphics2D g2d) {
        //PONTUAÇÃO DO JOGADOR:
        int pontosAtuais = this.gameControl.getPontuacaoAtual();
        
         //Dimensões da tela
        int larguraTela = this.getWidth();
        int alturaTela = this.getHeight();
        int larguraQuadro = 200;
        int alturaQuadro = 50;
        int X = ((larguraTela-larguraQuadro)/2)-80;
        int Y = 5;
        
        
        g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
        g2d.setColor(Color.getHSBColor(0.2f, 0.5f, 1.0f));
        g2d.drawString(pontosAtuais + " moedas",X,Y+(this.sg.tamanhoSprite/2)-15);
        
        desenharImagem(this.arquivo.moeda,X-(this.sg.tamanhoSprite/2),Y,this.sg.tamanhoSprite/2,g2d);
        
    }
    private void desenharImagem(Image imagem,int X,int Y,int size,Graphics2D g2d){
        //Tenta desenhar na tela uma imagem na tela tamanho (size X size)
        if (imagem!= null) {
                g2d.drawImage(imagem, X, Y, size, size, this);
            } else {
                g2d.setColor(Color.ORANGE);
                g2d.fillRect(X, Y, size, size);
            }
    }
    private void desenharItem(Coletavel C,int indice,Graphics2D g2d){
        sprite = this.arquivo.getImage(C.getNome());
        X = this.getPosXFromCard(indice);
        Y = this.getPosYFromCard(indice);
        int Xoffset = (this.sg.cartaLargura-this.sg.tamanhoSprite)/2;
        int Yoffset = (this.sg.cartaAltura-this.sg.tamanhoSprite)/2;
        this.desenharImagem(sprite, X+Xoffset, Y+Yoffset, this.sg.tamanhoSprite, g2d);
        
    }
    private void desenharEntidade(EntidadeJogo E,int indice, Graphics2D g2d){
        sprite = this.arquivo.getImage(E.getNome());
        X = this.getPosXFromCard(indice);
        Y = this.getPosYFromCard(indice);
        
        int Xoffset = (this.sg.cartaLargura-this.sg.tamanhoSprite)/2;
        int Yoffset = (this.sg.cartaAltura-this.sg.tamanhoSprite)/2;
        
        this.desenharImagem(sprite, X+Xoffset, Y+Yoffset, this.sg.tamanhoSprite, g2d);
        
        //apresentar HP
        int vida = E.getPontosDeVidaAtuais();
        
        int xHP = X+this.sg.cartaLargura-30;
        int yHP = Y+15;
        this.desenharImagem(this.arquivo.heartHP,xHP,yHP , 30, g2d);
        
        g2d.setFont(new Font("Arial", Font.BOLD, 15));
        g2d.setColor(Color.BLACK);
        g2d.drawString(""+vida, xHP+5,yHP);
        
        //Se for o player apresentar arma na mão do personagem:
        if(E.isPlayer()){
            if(E.getArmado()){
                sprite = this.arquivo.getImage(((Cavaleiro) E).getArma().getNome());
                int danoDaArma = E.getPotencia();
                this.desenharArmaNaMao(sprite, X, Y, g2d,danoDaArma);
            }
            if(((Cavaleiro)E).isEscudoDeTrocaAtivo()){
                this.desenharEscudo(sprite, X, Y, g2d);
            }
        }
    }
    private void desenharArmaNaMao(Image imagemArma,int X,int Y,Graphics2D g2d,int dano){
        this.desenharImagem(imagemArma, X-15, Y+45,(this.sg.tamanhoSprite-this.sg.tamanhoSprite/4), g2d);
        //Apresentar força da arma que está sendo apresentada
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.setColor(Color.BLACK);
        g2d.drawString(""+dano, X+10, Y+this.sg.cartaAltura-10);
        
    }
    private void desenharEscudo(Image imagemArma,int X,int Y,Graphics2D g2d){
        
        int Xoffset = this.sg.cartaLargura-80;
        int Yoffset = (this.sg.cartaAltura/4)+10;
        
        this.desenharImagem(this.arquivo.escudoDeGoma, X+Xoffset, Y+Yoffset,(this.sg.tamanhoSprite-this.sg.tamanhoSprite/6), g2d);
        
        
    }
    private void gameOverScreen(Graphics2D g2d) {
        //Dimensões da tela
        int larguraTela = this.getWidth();
        int alturaTela = this.getHeight();
        int larguraQuadro = 800;
        int alturaQuadro = 500;
        int Xq = (larguraTela-larguraQuadro)/2;
        int Yq = (alturaTela-alturaQuadro)/2;
        
        int larguraTexto = 700;
        int alturaTexto = 400;
        int Xmsg = (larguraQuadro - larguraTexto) / 2; // Cálculo para centralizar no quadrinho
        int Ymsg = (alturaQuadro - alturaTexto) / 6; 

        g2d.fillRect(Xq,Yq, larguraQuadro, alturaQuadro);

        JLabel texto = new JLabel("FIM DE JOGO",SwingConstants.CENTER);
        texto.setFont(new Font("Comic Sans MS", Font.BOLD,60));
        texto.setForeground(Color.getHSBColor(0.95f, 0.8f, 0.9f));
        texto.setBounds(Xq+Xmsg, Yq+Ymsg, larguraTexto, alturaTexto);
        this.add(texto);
        
        larguraTexto = 400;
        alturaTexto = 250;
        Xmsg = (larguraQuadro - larguraTexto) / 2; // Cálculo para centralizar no quadro
        Ymsg = ((alturaQuadro - alturaTexto) / 2)+200; 
        
        int pontuacaoFinal = this.gameControl.getPontucaoFinal();
        JLabel texto2 = new JLabel("Pontuação: " + pontuacaoFinal,SwingConstants.CENTER);
        texto2.setFont(new Font("Comic Sans MS", Font.BOLD,30));
        texto2.setForeground(Color.getHSBColor(0.95f, 0.1f, 0.8f));
        texto2.setBounds(Xq+Xmsg, Yq+Ymsg, larguraTexto, alturaTexto/5);
        this.add(texto2);
    }
    
    //métodos de auxílio - Implementado()
    private void setPosFinal(int X,int Y,int direcao){
        //define a posição final com base na direção e pos inicial
        int distanciaX = sg.cartaLargura + sg.gridGap;
        int distanciaY = sg.cartaAltura + sg.gridGap;
        
        // Ponto de partida: destino é igual ao início
        this.Xf = X;
        this.Yf = Y;
        
        // Ajusta o destino baseado na direção
        if (direcao == 0) { // Cima
            this.Yf = Y - distanciaY;
        } else if (direcao == 1) { // Baixo
            this.Yf = Y + distanciaY;
        } else if (direcao == 2) { // Esquerda
            this.Xf = X - distanciaX;
        } else if (direcao == 3) { // Direita
            this.Xf = X + distanciaX;
        }
    }
    private void setPosInicial(int X,int Y,int direcao){
        //define a posição inicial com base na direção e pos final
        int distanciaX = sg.cartaLargura + sg.gridGap;
        int distanciaY = sg.cartaAltura + sg.gridGap;
        
        // Ponto de partida: destino é igual ao início
        this.Xf = X;
        this.Yf = Y;
        
        // Ajusta o destino baseado na direção
        if (direcao == 0) { // Cima
            this.Yi = Y + distanciaY;
        } else if (direcao == 1) { // Baixo
            this.Yi = Y - distanciaY;
        } else if (direcao == 2) { // Esquerda
            this.Xi = X + distanciaX;
        } else if (direcao == 3) { // Direita
            this.Xi = X - distanciaX;
        }
    }
    private void movePosX(){
        
        if (Math.abs(this.Xatual - this.Xf) <= this.passo) {
            this.Xatual = this.Xf;
            this.chegouX = true;
        
        } else if (this.Xatual < this.Xf) {
            this.Xatual += this.passo;
        
        } else if (this.Xatual > this.Xf) {
            this.Xatual -= this.passo;
        }
    }
    private void movePosY(){
        if (Math.abs(this.Yatual - this.Yf) <= this.passo) {
            this.Yatual = this.Yf;
            this.chegouY = true;

        } else if (this.Yatual < this.Yf) {
            this.Yatual += this.passo;
        
        } else if (this.Yatual > this.Yf) {
            this.Yatual -= this.passo;
        }
    }
    public int getLinhaDaCarta(int indiceCarta){
        //Retorna indice da linha da matriz(y)
        int linha = (int)(indiceCarta / this.sg.gridSize);
        return linha;
    }
    public int getColunaDaCarta(int indiceCarta){
        //Retorna indice da coluna da matriz (x)
        int coluna = (int)(indiceCarta % this.sg.gridSize);
        return coluna;
    }
    public int getIndiceCarta(int coluna, int linha){
        //Retorna indice na matriz a partir da coluna e linha
        int indice = (linha * this.sg.gridSize) + coluna;
        return indice;
    }
    public int getPosXFromCard(int indiceCarta){
        //A partir do índice da carta gera uma coordenada X no grid
        int coluna = this.getColunaDaCarta(indiceCarta); 
        
        int distanciaX = this.sg.cartaLargura + this.sg.gridGap;
        int posXPainel = this.sg.gridX + coluna * (distanciaX);
        
        return posXPainel;
    }
    public int getPosYFromCard(int indiceCarta){
        //A partir do índice da carta gera uma coordenada Y no grid
        int linha = this.getLinhaDaCarta(indiceCarta); 
        
        int distanciaY = this.sg.cartaAltura + this.sg.gridGap; 
        int posYPainel = this.sg.gridY + linha * (distanciaY);
                
        return posYPainel;
    }
    
    
    
    //RECEBER COMANDOS DO TECLADO---------------------------------------------------------------------------------
    
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    
    

    @Override
    public void keyPressed(KeyEvent e) {
        //Ignora input se alguma animação de movimento estiver rodando
        if (this.animaDeslocamento) {
            return;
        }
        
        
         int codigoTecla = e.getKeyCode();

        // Este método é chamado sempre que uma tecla é pressionada para baixo
        switch (codigoTecla) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                if (!upPressed) { 
                    this.direcaoMov = GameLogic.Direcao.CIMA;
                    upPressed = true;
                }
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                if (!downPressed) {
                    this.direcaoMov = GameLogic.Direcao.BAIXO;
                    downPressed = true;
                }
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                if (!leftPressed) {
                    this.direcaoMov = GameLogic.Direcao.ESQUERDA;
                    leftPressed = true;
                }
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                if (!rightPressed) {
                    this.direcaoMov = GameLogic.Direcao.DIREITA;
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
