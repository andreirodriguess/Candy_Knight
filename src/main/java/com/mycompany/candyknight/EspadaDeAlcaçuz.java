/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.candyknight;

/**
 *
 * @author felip
 */
public class EspadaDeAlcaçuz implements Coletavel {
    
    private int novoAtaque;

    public EspadaDeAlcaçuz() {
        this.novoAtaque = 25;
    }
    
    public String getNome() {
        return "Espada de Alcaçuz";
    }
    
    public void usar(Cavaleiro cavaleiro) {
        System.out.println(cavaleiro.getNome() + " equipou a " + this.getNome() + "!");
        System.out.println("Ataque aumentado para " + this.novoAtaque + "!");
    }
}