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
public class EspadaDeAlcacuz implements Coletavel {
    
    private int novoAtaque;

    public EspadaDeAlcacuz() {
        // Este valor (25) agora representa o "Pool de Dano Total" da arma.
        this.novoAtaque = 25; 
    }
    
    public String getNome() {
        return "Espada de Alcaçuz";
    }
    
    // Em EspadaDeAlcacuz.java
    public void usar(Cavaleiro cavaleiro) {
        System.out.println(cavaleiro.getNome() + " equipou a " + this.getNome() + "!");
        
        cavaleiro.setArmado(true);

        // Define a 'potencia' do cavaleiro como o 'pool de dano' da arma
        cavaleiro.setPotencia(this.novoAtaque);
        
        // Mensagem atualizada para refletir a nova mecânica
        System.out.println("Arma com " + this.novoAtaque + " pontos de dano totais!");
    }
}