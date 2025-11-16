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
public class PocaoDeCalda implements Coletavel {

    private int cura;

    // +++ MUDANÇA: Construtor aceita nível de dificuldade +++
    public PocaoDeCalda(int nivel) {
        // +++ MUDANÇA: Valor base reduzido (25 -> 2.5 -> 3) +++
        int curaBase = 3;
        
        // +++ MUDANÇA: Lógica de scaling +++
        // Aumenta a cura em 1 a cada 4 níveis
        this.cura = curaBase + (nivel / 4);
    }
    
    @Override
    public String getNome() {
        return "Poção de Calda de Morango";
    }
    
    @Override
    public void usar(Cavaleiro cavaleiro) {
        System.out.println(cavaleiro.getNome() + " usou " + this.getNome() + "!");
        cavaleiro.curar(this.cura);
        System.out.println("Vida recuperada em " + this.cura + " pontos!");
    }
}