
package candyknightvisualtest;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaInicial extends JPanel{
    JButton buttonStart;//inicia o game
    JButton buttonExit;//Fecha o Jogo
    
    // Fontes que serão usadas
    Font fonteTitulo = new Font("Arial", Font.BOLD, 48);
    Font fonteBotoes = new Font("Arial", Font.PLAIN, 22);
    
    public TelaInicial(int largura,int altura,ControleJanela janela){
        this.setLayout(null);
        this.setPreferredSize(new Dimension(largura,altura));
        this.setBackground(Color.DARK_GRAY);
        
        //Adicionar Título
            JLabel texto = new JLabel("Candy Knight", SwingConstants.CENTER);
            texto.setFont(fonteTitulo);
            texto.setForeground(Color.WHITE);

            int larguraTexto = 400; int alturaTexto = 50; int x = (largura - larguraTexto) / 2; int y = 100;
            texto.setBounds(x,y, larguraTexto, alturaTexto); // (x, y, largura, altura)

            this.add(texto);
        
        
        //Adicionar BotaoStart
            buttonStart = new JButton("Start Game");
            buttonStart.setFont(fonteBotoes);
            buttonStart.setBounds((largura - 200) / 2, y + 200, 200, 50);

            // Adicionando a ação ao botão Start
            buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botão Start Pressionado!");
                janela.telaDoJogo();
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
                    janela.dispose();// Fecha a aplicação completamente
                }
            });
            this.add(buttonExit);
    }
    
}
