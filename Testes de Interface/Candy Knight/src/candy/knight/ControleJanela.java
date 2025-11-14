
/*DESCRIÇÃO DA CLASSE:
    Sua função é gerenciar a navegação entre os diferentes painéis.
*/
package candy.knight;

import javax.swing.JFrame;

public class ControleJanela extends JFrame{
    
    private final int resolucaoALTURA = 684;
    private final int resolucaoLARGURA = 1216;
    
    
    public ControleJanela(){
        this.setTitle("Candy Knight");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void ScreenMenu(){
        ScreenMenu menu = new ScreenMenu(resolucaoLARGURA,resolucaoALTURA,this);
        
        this.setContentPane(menu);
        this.pack(); // Ajusta a janela ao tamanho do painel
        this.setLocationRelativeTo(null); // CEntraliza a janela na tela
        
        this.setVisible(true);
        
        this.revalidate();
        this.repaint();
    }
    
    public void ScreenGame(){
        ScreenGame game = new ScreenGame(resolucaoLARGURA,resolucaoALTURA);
        this.setContentPane(game);
        
        
        //Repinta a tela com o painél novo
        this.revalidate();
        this.repaint();
    }
    
    
}
