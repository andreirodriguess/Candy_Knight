
package candy.knight;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.*;

public class ScreenMenu extends JPanel{
    JButton buttonStart;
    JButton buttonExit;
    Font fonteTitulo = new Font("Arial", Font.BOLD, 48);
    Font fonteBotoes = new Font("Arial", Font.PLAIN, 22);
    
    public ScreenMenu(int largura,int altura, ControleJanela JControl){
        this.setLayout(null);
        this.setPreferredSize(new Dimension(largura,altura));
        this.setBackground(Color.DARK_GRAY);
        
        
        //ADICIONAR TÍTULO
        JLabel texto = new JLabel("Candy Knight", SwingConstants.CENTER);
        texto.setFont(fonteTitulo);
        texto.setForeground(Color.WHITE);

        int larguraTexto = 400; int alturaTexto = 50; int x = (largura - larguraTexto) / 2; int y = 100;
        texto.setBounds(x,y, larguraTexto, alturaTexto); // (x, y, largura, altura)

        this.add(texto);
        
        //ADICIONAR BOTÕES
        //Adicionar BotaoStart
            buttonStart = new JButton("Start Game");
            buttonStart.setFont(fonteBotoes);
            buttonStart.setBounds((largura - 200) / 2, y + 200, 200, 50);

            // Adicionando a ação ao botão Start
            buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botão Start Pressionado!");
                JControl.ScreenGame();
                }
            });
            this.add(buttonStart);

        
        // Adicionar BotaoExit
            buttonExit = new JButton("Exit");
            buttonExit.setFont(fonteBotoes);
            buttonExit.setBounds((largura - 200) / 2, y + 270, 200, 50);

            // Adicionando a ação ao botão Exit
            buttonExit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Encerrando o jogo...");
                    JControl.dispose();// Fecha a aplicação completamente
                }
            });
            this.add(buttonExit);
    }
    
}
