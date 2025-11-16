/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package candyknight.Coletaveis;
import candyknight.Entidades.*;

/**
 *
 * @author felip
 */
public class EscudoDeGoma implements Coletavel {
    
    private int duracaoEmMovimentos = 2;

    @Override
    public String getNome() {
        return "Escudo de Goma (Troca)";
    }

    @Override
    public void usar(Cavaleiro cavaleiro) {
        System.out.println(cavaleiro.getNome() + " usou " + this.getNome() + "!");
        System.out.println("Pode trocar de lugar com monstros por " + duracaoEmMovimentos + " movimentos!");
        
        // Esta é a parte crucial. O Cavaleiro precisa deste método:
        cavaleiro.ativarEscudoDeTroca(duracaoEmMovimentos);
    }
}