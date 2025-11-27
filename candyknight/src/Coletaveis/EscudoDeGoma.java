
package Coletaveis;
import Entidades.Cavaleiro;

public class EscudoDeGoma implements Coletavel {
    
    private int duracaoEmMovimentos = 2;

    @Override
    public String getNome() {
        return "escudoDeGoma";
    }

    @Override
    public void usar(Cavaleiro cavaleiro) {
        System.out.println(cavaleiro.getNome() + " usou " + this.getNome() + "!");
        System.out.println("Pode trocar de lugar com monstros por " + duracaoEmMovimentos + " movimentos!");
        
        // Esta é a parte crucial. O Cavaleiro precisa deste método:
        cavaleiro.ativarEscudoDeTroca(duracaoEmMovimentos);
    }
    @Override
    public void fortalecer(int nivel) {
    }
    
}