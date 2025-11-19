/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Coletaveis;

import Entidades.Cavaleiro;

/**
 *
 * @author felip
 */
public class PocaoDeCalda implements Coletavel {

    private int cura;

    // Construtor com nível
    public PocaoDeCalda(int nivel) {
        // Cura base 5 (50% do HP inicial). Aumenta 1 a cada 2 níveis.
        this.cura = 5 + (nivel / 2);
    }
    
    // Construtor padrão
    public PocaoDeCalda() {
        this(0);
    }
    
    @Override
    public String getNome() {
        return "Poção de Calda de Morango";
    }

    @Override
    public void usar(Cavaleiro cavaleiro) {
        System.out.println(cavaleiro.getNome() + " bebeu a " + this.getNome() + "!");
        cavaleiro.curar(this.cura);
        // A mensagem de cura já é exibida dentro do método cavaleiro.curar()
    }
}