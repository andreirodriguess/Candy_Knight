/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package candyknight;


import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControleJanela implements ActionListener{
    
    JFrame janela;
    JButton botao1;
    JButton botao2;
    JButton botaoHome;
    JButton botaoReset;
    
    ControleJanela(){
        //cria uma janela para a aplicação (controla a apresentação de paineis na tela)
        janela = new JFrame("Candy Knight");
        janela.setSize(1440, 810);
        janela.setResizable(true);
        //Encerra o programa ao fechar
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    void start(){
        //Cria um painel que vai ser apresentado na Janela
        JPanel painel = new JPanel(); 
       
        //O layout do painel vai ser organizado em um grid:
        painel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        
        gbc.insets = new Insets(30, 30, 30, 30);//Para po respaço entre os elementos da tela
        
        //Fontes que serão usadas
        Font fonteTitulo = new Font("Arial", Font.BOLD, 48);
        Font fonteBotoes = new Font("Arial", Font.PLAIN, 22);
        
        //Cria e põe um texto no painel
        JLabel texto = new JLabel("Candy Knight");
        texto.setFont(fonteTitulo);
        gbc.gridy = 0;
        painel.add(texto, gbc);

        //Botões
        gbc.insets = new Insets(10, 10, 10, 10);
        botao1 = new JButton("Iniciar Partida");
        botao1.setFont(fonteBotoes);
        gbc.gridy = 2;
        painel.add(botao1, gbc);

        botao2 = new JButton("Sair");
        botao2.setFont(fonteBotoes);
        gbc.gridy = 3;
        painel.add(botao2, gbc);

        //Ativa o action Listener dos botões
        botao1.addActionListener(this);
        botao2.addActionListener(this);
        
        //Apaga o que tiver na tela(content pane, que é controlado pela Janela[JPanel]) e adiciona o painel à tela:
        Container contentPane = janela.getContentPane();
        contentPane.removeAll();
        contentPane.add(painel);
        
        janela.revalidate();
        janela.repaint();
        
        
        //Abre a Janela
        janela.setVisible(true);
    }

    //Função do AcTIon Listener: é chamada para qualquer botão(JButton) que for pressionado
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == botao1) {
            iniciarJogo();
        } 
        else if (e.getSource() == botao2) {
            janela.dispose();
        }
        else if (e.getSource() == botaoHome) {
            start();
        } 
        else if (e.getSource() == botaoReset) {
            iniciarJogo();
        }
        
    }
    
    void iniciarJogo() {
        JPanel painelGame = new JPanel();
        
        painelGame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        //Fontes
        Font fonteTitulo = new Font("Arial", Font.BOLD, 28);
        Font fonteBotoes = new Font("Arial", Font.PLAIN, 22);
        
        /*BOTÕES E TEXTO:----------------------------------------------------------*/
        //Mostrar Pontuação
        JLabel score = new JLabel("Pontos: ");
        score.setFont(fonteTitulo);
        
        gbc = new GridBagConstraints(); // Reseta as configurações da grade
        gbc.gridx = 1; gbc.gridy = 2; 
        gbc.anchor = GridBagConstraints.SOUTH; // Gruda na parte de baixo da célula
        gbc.insets = new Insets(0, 0, 20, 0);
        painelGame.add(score, gbc);
        
        //Botão para retornar ao menu
        botaoHome = new JButton("MENU");
        botaoHome.setFont(fonteBotoes);
        
        gbc.gridx = 0;gbc.gridy = 0;//(0,0)
        gbc.anchor = GridBagConstraints.NORTHWEST; // Gruda no canto superior esquerdo
        gbc.insets = new Insets(20, 20, 0, 0); // Margem (cima, esq, baixo, dir)
        painelGame.add(botaoHome, gbc);
        
        //Botão para dar restart no jogo
        botaoReset = new JButton("RESTART");
        botaoReset.setFont(fonteBotoes);
        
        gbc = new GridBagConstraints(); 
        gbc.gridx = 2;gbc.gridy = 0;//(2,0)
        gbc.anchor = GridBagConstraints.NORTHEAST; 
        gbc.insets = new Insets(20, 0, 0, 20);
        painelGame.add(botaoReset, gbc);;
        
        //Ativa o action Listener dos botões
        botaoHome.addActionListener(this);
        botaoReset.addActionListener(this);
        
        /*TABULEIRO DO JOGO (GRID):----------------------------------------------------------*/
        TabuleiroPanel painelDoTabuleiro = new TabuleiroPanel();
        
        gbc = new GridBagConstraints(); // Reseta
        gbc.gridx = 0;gbc.gridy = 1;//(1,1)
        gbc.gridwidth = 3;  // Ocupa todas as 3 colunas
        gbc.weightx = 1.0;  // Estica horizontalmente
        gbc.weighty = 1.0;  // Estica verticalmente
        gbc.fill = GridBagConstraints.BOTH; // Preenche todo o espaço
        
        painelGame.add(painelDoTabuleiro, gbc); // Adiciona o tabuleiro!
        
        /*PREENCHER ESPAÇO VAZIO:-------------------------------------------------------------*/
        
        
        
        //Pega uma referência a tela dentro da Janela, limpa todo o conteudo, Adiciona o painel criado:
        Container contentPane = janela.getContentPane();
        contentPane.removeAll();
        contentPane.add(painelGame);
        
        //Atualiza a tela
        janela.revalidate();
        janela.repaint();
    }
}
