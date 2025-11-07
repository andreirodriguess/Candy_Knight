/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package candyknightvisualtest;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControleJanela extends JFrame {
    //Atributos
    int resolucaoAltura = 576;
    int resolucaoLargura = 1024;
    
    
    ControleJanela(){
        this.setTitle("Candy Knight");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    
    public void menu(){
        TelaInicial menu = new TelaInicial(resolucaoLargura,resolucaoAltura,this);
        
        this.add(menu);
        this.pack(); // Ajusta a janela ao tamanho do painel
        this.setLocationRelativeTo(null); // Centraliza a janela na tela
        this.setVisible(true);
        
    }
    
    public void telaJogo(){
        TelaJogo jogoPainel = new TelaJogo(resolucaoLargura,resolucaoAltura);
        this.setContentPane(jogoPainel);
        
        // Atualiza a visualização
        this.revalidate();
        this.repaint();
        
        // Passa o foco para o painel do jogo (importante para teclado)
        jogoPainel.requestFocusInWindow();
        
    }
    
}
