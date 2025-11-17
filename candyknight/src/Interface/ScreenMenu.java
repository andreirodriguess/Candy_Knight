package Interface;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.*;
import java.net.URL; // Importação adicionada

// 1. Alterado de JPanel para JLayeredPane
public class ScreenMenu extends JLayeredPane { 
    
    JButton buttonStart;
    JButton buttonExit;
    Font fonteTitulo = new Font("Arial", Font.BOLD, 48);
    Font fonteBotoes = new Font("Arial", Font.PLAIN, 22);
    
    public ScreenMenu(int largura, int altura, ControleJanela JControl) {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(largura, altura));
        
        try {
            URL gifUrl = getClass().getResource("/Imagens/menu.gif");
            if (gifUrl != null) {
                ImageIcon fundoGif = new ImageIcon(gifUrl);
                JLabel labelFundo = new JLabel(fundoGif);
                labelFundo.setBounds(0, 0, largura, altura);
                
                // Adiciona o fundo à camada INFERIOR
                this.add(labelFundo, JLayeredPane.DEFAULT_LAYER);
            } else {
                System.err.println("Erro ao carregar o menu.gif! Ficheiro não encontrado em /Imagens/");
                // Fallback para a cor cinza se o GIF falhar
                JPanel fundoCor = new JPanel();
                fundoCor.setBackground(Color.DARK_GRAY);
                fundoCor.setBounds(0, 0, largura, altura);
                this.add(fundoCor, JLayeredPane.DEFAULT_LAYER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        int larguraTexto = 400; int x = (largura - larguraTexto) / 2; int y = 100;

        // --- ADICIONAR BOTÕES (CAMADA DE CIMA) ---
        //Adicionar BotaoStart
        buttonStart = new JButton("Start Game");
        buttonStart.setFont(fonteBotoes);
        buttonStart.setBounds((largura - 300) / 2, y + 250, 300, 75);

        // Adicionando a ação ao botão Start
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botão Start Pressionado!");
                JControl.ScreenGame();
            }
        });
        // 5. Adiciona o botão à camada SUPERIOR
        this.add(buttonStart, JLayeredPane.PALETTE_LAYER);

        
        // Adicionar BotaoExit
        buttonExit = new JButton("Exit");
        buttonExit.setFont(fonteBotoes);
        buttonExit.setBounds((largura - 300) / 2, y + 350, 300, 75);

        // Adicionando a ação ao botão Exit
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Encerrando o jogo...");
                JControl.dispose(); // Fecha a aplicação completamente
            }
        });
        // 6. Adiciona o botão à camada SUPERIOR
        this.add(buttonExit, JLayeredPane.PALETTE_LAYER);
    }
}