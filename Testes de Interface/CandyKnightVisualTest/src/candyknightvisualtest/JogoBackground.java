
package candyknightvisualtest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


// Não precisa mais de ActionListener
class JogoBackground extends JPanel {
    
    // Nenhuma variável de animação aqui
    
    public JogoBackground() {
        // Nenhum Timer aqui
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        
        // Apenas desenha o fundo estático UMA VEZ
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight()); 
        g.setColor(Color.ORANGE);
        g.fillRect(getWidth()/2, 0, 80, getHeight()); 
        
        
    }
}
