
package Interface;

import Backend.*;
import Entidades.*;
import Coletaveis.*;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
    
    //Variáveis auxiliares para deslocamento:
    private boolean animaDeslocamento = false;//Variável que sinaliza se está tendo uma animação de deslocamento
    private int Xatual;//Pos atual
    private int Yatual;
    private int Xi;//Pos inicial
    private int Yi;
    private int Xf;//Pos final
    private int Yf;
    
    private int direcaoMov;
    private final int passo = 10;//Velocidade com que as imagens se movem
    private boolean chegouX;//Sinaliza que chegou no destino X
    private boolean chegouY;//Sinaliza que chegou no destino Y
    
    
    
    public GameHUD(int larguraPainel,int alturaPainel, ScreenGame sg) {//Passo Screengame como parametro pra poder acessar suas variáveis
        this.sg = sg;
        Timer gameTimer = new Timer(16, (ActionListener) this); 
        gameTimer.start();
        
        this.setFocusable(true); 
        this.addKeyListener(this);
    }

    @Override//Vai rodar a cada 16ms (60 FPS)
    public void actionPerformed(ActionEvent e) {
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        Graphics2D g2d = (Graphics2D) g;
        
        mostrarCelulas(g2d);
       
        
    }
    
    public void mostrarCelulas(Graphics2D g2d){
        this.tabuleiro = this.gameControl.getTabuleiro();
        
        for(int i=0;i<9;i++){
            if(this.tabuleiro.get(i).temEntidade()){
                //desenhar entidade:
                Image sprite = this.arquivo.getImage(this.tabuleiro.get(i).getEntidade().getNome());
                X = this.getPosXFromCard(i);
                Y = this.getPosYFromCard(i);
                this.desenharImagem(sprite,X,Y,this.sg.tamanhoSprite, g2d);
                
                if(this.tabuleiro.get(i).getEntidade().getArmado()){
                    
                }
            }
        }
    }
    
    public void desenharImagem(Image imagem,int X,int Y,int size,Graphics2D g2d){
        //Tenta desenhar na tela uma imagem na tela tamanho (size X size)
        if (imagem!= null) {
                g2d.drawImage(imagem, X, Y, size, size, this);
            } else {
                g2d.setColor(Color.ORANGE);
                g2d.fillRect(X, Y, size, size);
            }
    }
    
    public void Mover(int direcao){
        
    }
    
    //métodos de auxilio Implementado
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
                    this.Mover(0);
                    upPressed = true;
                }
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                if (!downPressed) {
                    this.Mover(1);
                    downPressed = true;
                }
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                if (!leftPressed) {
                    this.Mover(2);
                    leftPressed = true;
                }
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                if (!rightPressed) {
                    this.Mover(3);
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
