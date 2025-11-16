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

    // +++ MUDANÇA: Construtor aceita nível de dificuldade +++
    public EspadaDeAlcacuz(int nivel) {
        // +++ MUDANÇA: Valor base reduzido (25 -> 2.5 -> 3) +++
        int ataqueBase = 3; 
        
        // +++ MUDANÇA: Lógica de scaling (passo MENOR) +++
        // Aumenta o pool de dano em 1 a cada 5 níveis
        this.novoAtaque = ataqueBase + (nivel / 5);
    }
    
    public String getNome() {
        return "Espada de Alcaçuz";
    }
    
    public void usar(Cavaleiro cavaleiro) {
        System.out.println(cavaleiro.getNome() + " equipou a " + this.getNome() + "!");
        
        cavaleiro.setArmado(true);

        cavaleiro.setPotencia(this.novoAtaque);
        
        System.out.println("Arma com " + this.novoAtaque + " pontos de dano totais!");
    }
}