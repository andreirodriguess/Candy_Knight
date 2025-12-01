
package Interface;

import Backend.*;
import Entidades.*;
import Coletaveis.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameBackground extends JPanel{
    private ScreenGame sg;
    private final int buttonGap = 20;
    private final int buttonWidth = 200;
    private final int buttonHeight = 50;
    private int largura;
    private int altura;
    private ArquivosImagem arquivo = new ArquivosImagem();
    
    
    //CONSTRUTOR
    public GameBackground(int larguraPainel,int alturaPainel,ScreenGame parentScreenGame) {//Passo Screengame como parametro pra poder acessar suas variáveis
        this.sg = parentScreenGame;
        
        this.altura = alturaPainel;
        this.largura = larguraPainel;
        
        this.setLayout(null);
        this.setPreferredSize(new Dimension(largura,altura));
        this.setBackground(Color.getHSBColor(0.95f, 1.0f, 0.10f));//Função que controla a cor de fundo usando o sistema HSB
        
        this.setGridPosicao(largura, altura);
        
        //Adicionar botões:
        this.addButtonMenu();
        this.addButtonRestart();
            
    }
    
    //MÉTODOS --------------------------------------------------------------------------------------------
    private void addButtonMenu(){
            JButton ButtonMenu = new JButton("Menu");
            ButtonMenu.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
            ButtonMenu.setBounds(this.buttonGap, this.buttonGap, this.buttonWidth, this.buttonHeight);
            // Adicionando a ação ao botão menu
            ButtonMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Retornando ao menu Principal");
                Component component = (Component) e.getSource();
                Window janela = SwingUtilities.getWindowAncestor(component);
                
                // Verifica se a janela é a classe ControleJanela
                if (janela instanceof ControleJanela) {
                    ((ControleJanela) janela).ScreenMenu();//Chama de volta para o menu
                }
            }
            });
            this.add(ButtonMenu);
    }
    
    private void addButtonRestart(){
            JButton ButtonRestart = new JButton("Reiniciar");
            ButtonRestart.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
            ButtonRestart.setBounds(this.largura - (this.buttonGap + this.buttonWidth),this.buttonGap, this.buttonWidth, this.buttonHeight);
            // Adicionando a ação ao botão menu
            ButtonRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Reinicando Partida............");
                sg.JControl.ScreenGame();
                //Reinicia a partida
            }
            });
            this.add(ButtonRestart);
    }
    
    private void setGridPosicao(int larguraPainel, int alturaPainel){
        //posiciona o grid no painel
        int larguraTotalGrid = (this.sg.cartaLargura*this.sg.gridSize) + (this.sg.gridGap*(this.sg.gridSize-1));
        int alturaTotalGrid = (this.sg.cartaAltura*this.sg.gridSize) + (this.sg.gridGap*(this.sg.gridSize-1));
        
        this.sg.gridX = (larguraPainel - larguraTotalGrid) / 2;
        this.sg.gridY = (alturaPainel - alturaTotalGrid) / 2;
    }
    //pinta o fundo:
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        Graphics2D g2d = (Graphics2D) g;

        pintarFundo(g2d);
        // Se screenGame ainda não foi definido, não faz nada
        if (this.sg == null) {
            return;
        }

        pintarGrid(g2d);
    }
    
    private void pintarFundo(Graphics2D g2d) {
        int larguraTela = this.getWidth();
        int alturaTela = this.getHeight();
        
        desenharImagem(arquivo.backgroundGame,0,0,larguraTela,alturaTela,g2d);
    }
    
    public void pintarGrid(Graphics2D g2d){
        // Calcula a distância total de um slot para o próximo
        int distanciaX = this.sg.cartaLargura + this.sg.gridGap;
        int distanciaY = this.sg.cartaAltura + this.sg.gridGap;
        
        for(int coluna = 0; coluna<this.sg.gridSize ;coluna++){
            for(int linha = 0; linha<this.sg.gridSize ;linha++){
                int x = this.sg.gridX + linha*distanciaX;
                int y = this.sg.gridY + coluna*distanciaY;
                g2d.fillRect(x, y, this.sg.cartaLargura, this.sg.cartaAltura);
                desenharImagem(arquivo.carta,x,y, this.sg.cartaLargura,this.sg.cartaAltura,g2d);
            }
        }
        
    }
    private void desenharImagem(Image imagem,int X,int Y,int largura,int altura,Graphics2D g2d){
        //Tenta desenhar na tela uma imagem na tela tamanho (size X size)
        if (imagem!= null) {
                g2d.drawImage(imagem, X, Y, largura, altura, this);
            } else {
                g2d.setColor(Color.getHSBColor(0.40f, 0.30f, 0.7f)); 
                g2d.fillRect(X, Y, largura, altura);
            }
    }

    
}
