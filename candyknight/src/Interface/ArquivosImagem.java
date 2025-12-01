
package Interface;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ArquivosImagem {
    
    public Image ursoDeGoma = this.InicializarImagens("/Imagens/ursoDeGoma.png");
    public Image cavaleiro = this.InicializarImagens("/Imagens/player.png");
    public Image heartHP = this.InicializarImagens("/Imagens/heartHP.png");
    public Image soldadoDeGengibre = this.InicializarImagens("/Imagens/soldadoDeGengibre.png");
    public Image escudoDeGoma = this.InicializarImagens("/Imagens/escudoDeGoma.png");
    public Image espadaDeAlcacuz = this.InicializarImagens("/Imagens/espadaDeAlcacuz.png");
    public Image pocaoDeCalda = this.InicializarImagens("/Imagens/pocaoDeCalda.png");
    public Image menu = this.InicializarGif("/Imagens/menu.gif");
    public Image carta = this.InicializarImagens("/Imagens/carta.png");
    public Image moeda = this.InicializarImagens("/Imagens/moeda.png");
    public Image backgroundGame = this.InicializarImagens("/Imagens/Background.png");
    
    public Image getImage(String nomeDaImagem){
        Image imagem;
        
        try {
            imagem = ImageIO.read(getClass().getResource("/Imagens/"+nomeDaImagem+".png"));
            return imagem;
        } catch (IOException | IllegalArgumentException e) {
             // Adicionei IllegalArgumentException pois se a pasta estiver errada, o getResource retorna null
            System.err.println("Erro ao carregar imagem (verifique se a pasta 'Imagens' está correta em Source Packages): " + e.getMessage());
        }
        
        return null;
    }
    
    public Image InicializarImagens(String nomeDaImagem){
        Image imagem;
        try {
            imagem = ImageIO.read(getClass().getResource(nomeDaImagem));
            return imagem;
        } catch (IOException | IllegalArgumentException e) {
             // Adicionei IllegalArgumentException pois se a pasta estiver errada, o getResource retorna null
            System.err.println("Erro ao carregar imagem (verifique se a pasta 'Imagens' está correta em Source Packages): " + e.getMessage());
        }
        
        return null;
    }
    
    public Image InicializarGif(String nomeDaImagem){
        Image imagem;
        try {
            imagem = ImageIO.read(getClass().getResource(nomeDaImagem));
            return imagem;
        } catch (IOException | IllegalArgumentException e) {
             // Adicionei IllegalArgumentException pois se a pasta estiver errada, o getResource retorna null
            System.err.println("Erro ao carregar imagem (verifique se a pasta 'Imagens' está correta em Source Packages): " + e.getMessage());
        }
        
        return null;
    }
}
