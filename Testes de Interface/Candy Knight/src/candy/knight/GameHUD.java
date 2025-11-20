
package candy.knight;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class GameHUD extends JPanel implements ActionListener{

    public GameHUD(int larguraPainel,int alturaPainel, ScreenGame sg) {//Passo Screengame como parametro pra poder acessar suas variáveis
        Timer gameTimer = new Timer(16, (ActionListener) this); 
        gameTimer.start();
    }

    
    @Override//Vai rodar a cada 16ms (60 FPS)
    public void actionPerformed(ActionEvent e) {
        
    }
    
}
