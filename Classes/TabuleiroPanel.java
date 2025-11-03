
package candyknight;


import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;

public class TabuleiroPanel extends JPanel{
    private final int LARGURA_BLOCO = 150; 
    private final int ALTURA_BLOCO = 200; 
    private final int COLUNAS = 3;        
    private final int LINHAS = 3;
    
    public TabuleiroPanel() {
        // Define o tamanho do painel
        // (8 colunas * 50 pixels) x (12 linhas * 50 pixels)
        int largura = COLUNAS * (LARGURA_BLOCO + 10);
        int altura = LINHAS * (ALTURA_BLOCO + 10);
        setPreferredSize(new Dimension(largura, altura));
        
        // fundo para ver onde o painel está
        setBackground(Color.DARK_GRAY); 
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);//Pinta o fundo

        Graphics2D g2d = (Graphics2D) g;

        // Centralizar painel
        
        //Pega o tamanho ATUAL do painel 
        int larguraPainel = getWidth();
        int alturaPainel = getHeight();

        //Calcula o tamanho total da nossa grade
        int larguraGrade = COLUNAS * LARGURA_BLOCO;
        int alturaGrade = LINHAS * ALTURA_BLOCO;

        //Calcula o espaço sobrando e divide por 2 para achar o offset
        int offsetX = (larguraPainel - larguraGrade) / 2;
        int offsetY = (alturaPainel - alturaGrade) / 2;



        // Loop para desenhar os retângulos
        for (int linha = 0; linha < LINHAS; linha++) {
            for (int col = 0; col < COLUNAS; col++) {
                
                // 4. SOMA O OFFSET às posições X e Y
                int x = (col * LARGURA_BLOCO) + offsetX;
                int y = (linha * ALTURA_BLOCO) + offsetY;

                // Define a cor da borda
                g2d.setColor(Color.BLACK);
                // Desenha a borda
                g2d.drawRect(x, y, LARGURA_BLOCO, ALTURA_BLOCO);

                // Colore os blocos
                if ((linha + col) % 2 == 0) {
                     g2d.setColor(new Color(50, 50, 50));
                     // (Corrigi a largura e altura aqui, você tinha -2)
                     g2d.fillRect(x+1, y+1, LARGURA_BLOCO-1, ALTURA_BLOCO-1);
                }
            }
        }
    }
}
