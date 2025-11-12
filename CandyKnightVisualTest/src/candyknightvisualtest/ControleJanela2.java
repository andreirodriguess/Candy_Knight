package candyknightvisualtest;


import javax.swing.JFrame;
import java.awt.Dimension;

public class ControleJanela2 {

    private JFrame janela;
    private final int LARGURA = 800;
    private final int ALTURA = 600;

    public void iniciar() {
        // 1. Cria a janela principal
        janela = new JFrame("Exemplo de JLayeredPane (HUD)");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Define um tamanho fixo para a ÁREA INTERNA da janela
        janela.getContentPane().setPreferredSize(new Dimension(LARGURA, ALTURA));
        janela.pack(); // pack() ajusta o tamanho da janela para caber o conteúdo

        // 2. Cria o painel de camadas
        TelaGame painelPrincipal = new TelaGame(LARGURA, ALTURA);
        
        // 3. Adiciona o painel de camadas à janela
        janela.add(painelPrincipal);
        
        // 4. Finaliza e mostra a janela
        janela.setLocationRelativeTo(null); // Centraliza na tela
        janela.setResizable(false);
        janela.setVisible(true);
    }
}