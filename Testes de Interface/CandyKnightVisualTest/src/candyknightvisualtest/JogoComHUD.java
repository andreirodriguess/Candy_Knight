
package candyknightvisualtest;
// PainelHUD.java (Agora controla a bola E a pontuação)
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class JogoComHUD extends JPanel implements ActionListener {
    
    // --- Lógica da Pontuação ---
    private int pontuacao = 0;
    private int contadorTimer = 0; // Contador para o placar
    
    // --- Lógica da Bola (Movida para cá) ---
    private int xCirculo = 0;
    private int yCirculo = 100;
    private int velX = 2;

    public JogoComHUD() {
        // ATUALIZADO: O Timer agora roda a 60 FPS (16ms)
        // Ele vai controlar TUDO que se move
        Timer gameTimer = new Timer(16, this); 
        gameTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Prepara o Graphics, mas não limpa (é transparente)
        Graphics2D g2d = (Graphics2D) g;
        
        // --- Desenha os elementos do HUD ---
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.setColor(Color.WHITE);
        g2d.drawString("Pontuação: " + pontuacao, 20, 40);
        
        // ... (barra de vida, etc) ...

        // --- Desenha a Bola (Movida para cá) ---
        g2d.setColor(Color.RED);
        g2d.fillOval(xCirculo, yCirculo, 50, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        // --- Lógica da Bola (roda a 60 FPS) ---
        xCirculo += velX;
        if (xCirculo > getWidth() || xCirculo < 0) {
            velX = -velX;
        }

        // --- Lógica da Pontuação (roda 1x por segundo) ---
        contadorTimer++;
        // 60 frames = 1 segundo (aprox.)
        if (contadorTimer >= 60) { 
            pontuacao += 10;
            contadorTimer = 0; // Reseta o contador
        }
        
        // Repinta SÓ este painel transparente
        // O Swing vai redesenhar a bola E a pontuação por cima do fundo azul.
        repaint();
    }
}