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

    public PocaoDeCalda() {
        this.cura = 25;
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