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

    public PocaoDeCalda() {
        this.cura = 5;
    }
    
    @Override
    public String getNome() {
        return "pocaoDeCalda";
    }
    @Override
    public void usar(Cavaleiro cavaleiro) {
        System.out.println(cavaleiro.getNome() + " usou " + this.getNome() + "!");
        cavaleiro.curar(this.cura);
        System.out.println("Vida recuperada em " + this.cura + " pontos!");
    }
    @Override
    public void fortalecer(int nivel) {
        this.cura = cura + (nivel /4);
    }
}