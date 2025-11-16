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
public class EspadaDeAlcacuz implements Coletavel {
    
    private int novoAtaque;

    public EspadaDeAlcacuz() {
        this.novoAtaque = 25;
    }
    
    public String getNome() {
        return "Espada de Alcaçuz";
    }
    
    // Em EspadaDeAlcacuz.java
    public void usar(Cavaleiro cavaleiro) {
        System.out.println(cavaleiro.getNome() + " equipou a " + this.getNome() + "!");
        
        cavaleiro.setArmado(true);

        cavaleiro.setPotencia(this.novoAtaque);

        System.out.println("Ataque aumentado para " + this.novoAtaque + "!");
    }
}
