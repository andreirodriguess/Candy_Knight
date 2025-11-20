
package candy.knight;

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
    
    //CONSTRUTOR
    public GameBackground(int larguraPainel,int alturaPainel,ScreenGame parentScreenGame) {//Passo Screengame como parametro pra poder acessar suas variáveis
        sg = parentScreenGame;
        this.altura = alturaPainel;
        this.largura = larguraPainel;
        
        this.setLayout(null);
        this.setPreferredSize(new Dimension(largura,altura));
        this.setBackground(Color.getHSBColor(0.95f, 0.50f, 0.22f));//Função que controla a cor de fundo usando o sistema HSB
        
        this.setGridPosicao(largura, altura);
        
        //Adicionar botões:
        this.addButtonMenu();
        this.addButtonRestart();
            
    }
    
    //MÉTODOS --------------------------------------------------------------------------------------------
    private void addButtonMenu(){
            JButton ButtonMenu = new JButton("Menu");
            ButtonMenu.setFont(new Font("Arial", Font.PLAIN, 22));
            ButtonMenu.setBounds(this.buttonGap, this.buttonGap, this.buttonWidth, this.buttonHeight);
            // Adicionando a ação ao botão menu
            ButtonMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Retornando ao menu Principal");
                Component component = (Component) e.getSource();//Ta gravando o botão que chama o evento em "component"
                Window janela = SwingUtilities.getWindowAncestor(component);//Encontra a janela em que está o botão
                
                // Verifica se a janela é a classe ControleJanela
                if (janela instanceof ControleJanela) {
                    ((ControleJanela) janela).ScreenMenu();//Chama de volta para o menu
                }
            }
            });
            this.add(ButtonMenu);
    }
    private void addButtonRestart(){
            JButton ButtonRestart = new JButton("Restart");
            ButtonRestart.setFont(new Font("Arial", Font.PLAIN, 22));
            ButtonRestart.setBounds(this.largura - (this.buttonGap + this.buttonWidth),this.buttonGap, this.buttonWidth, this.buttonHeight);
            // Adicionando a ação ao botão menu
            ButtonRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Reinicando Partida............");
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
        super.paintComponent(g); // Desenha o fundo HSB
        Graphics2D g2d = (Graphics2D) g;

        // Se screenGame ainda não foi definido, não faz nada
        if (this.sg == null) {
            return;
        }

        pintarGrid(g2d);
    }
    public void pintarGrid(Graphics2D g2d){
        
        g2d.setColor(Color.getHSBColor(0.45f, 0.20f, 0.66f)); 
        
        // Calcula a distância total de um slot para o próximo
        int distanciaX = this.sg.cartaLargura + this.sg.gridGap;
        int distanciaY = this.sg.cartaAltura + this.sg.gridGap;
        
        for(int coluna = 0; coluna<this.sg.gridSize ;coluna++){
            for(int linha = 0; linha<this.sg.gridSize ;linha++){
                int x = this.sg.gridX + linha*distanciaX;
                int y = this.sg.gridY + coluna*distanciaY;
                g2d.fillRect(x, y, this.sg.cartaLargura, this.sg.cartaAltura);
            }
        }
        
    }
    
}
