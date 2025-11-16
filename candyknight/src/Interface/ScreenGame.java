
package Interface;

import Backend.*;
import Entidades.*;
import Coletaveis.*;


import java.awt.*;
import javax.swing.*;

public class ScreenGame extends JLayeredPane{
    //Variáveis iniciais pra configuração do jogo:
    
    public int gridX; //Posição X do Grid na tela
    public int gridY; //Posição Y do Grid na tela
    public int gridSize = 3; 
    public int gridGap = 30;
    public int cartaAltura = 160;
    public int cartaLargura = 128; 
    public int tamanhoSprite = 100;
    
    
    GameHUD FrontPanel;
    GameBackground BackPanel;
    
    public ScreenGame(int largura, int altura) {
        
        this.setPreferredSize(new Dimension(largura, altura));// TAmanho do painel de camadas
        
        //Cria o painel que fica no fundo:
        BackPanel = new GameBackground(largura,altura,this);
        BackPanel.setBounds(0, 0, largura, altura);
        
        //Criando painel que vai ter o HUD, animações etc.
        FrontPanel = new GameHUD(largura,altura,this);
        FrontPanel.setBounds(0, 0, largura, altura);
        
        FrontPanel.setOpaque(false);//Pa deixar o painel com fundo transparente
        
        this.add(BackPanel,JLayeredPane.DEFAULT_LAYER);//Põe o painel na camada de baixo
        this.add(FrontPanel,JLayeredPane.PALETTE_LAYER);//Põe o painel  na camada de cima
        
    }
}
