
package candyknightvisualtest;

import javax.swing.*;
import java.awt.*;




public class TelaGame extends JLayeredPane {

    private JogoBackground jogoPanel;
    private JogoComHUD hudPanel;

    public TelaGame(int largura, int altura) {
        // Define o tamanho deste painel de camadas
        this.setPreferredSize(new Dimension(largura, altura));

        // 1. Crie o Painel do Jogo (Camada de Baixo)
        jogoPanel = new JogoBackground();
        jogoPanel.setBounds(0, 0, largura, altura); // Ocupa todo o espaço

        // 2. Crie o Painel do HUD (Camada de Cima)
        hudPanel = new JogoComHUD();
        hudPanel.setBounds(0, 0, largura, altura); // Também ocupa todo o espaço
        
        // --- A MÁGICA DA TRANSPARÊNCIA ---
        hudPanel.setOpaque(false); 

        // 3. Adicione os painéis ao JLayeredPane em camadas diferentes
        // DEFAULT_LAYER (camada 0) é o fundo
        this.add(jogoPanel, JLayeredPane.DEFAULT_LAYER);
        // PALETTE_LAYER (camada 100) fica na frente
        this.add(hudPanel, JLayeredPane.PALETTE_LAYER);
    }
}
