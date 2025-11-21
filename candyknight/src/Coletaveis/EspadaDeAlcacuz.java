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
public class EspadaDeAlcacuz extends Arma {
    
    private int ataque;

    public EspadaDeAlcacuz() {
        this.ataque = 5;
    }
    
    @Override
    public String getNome() {
        return "espadaDeAlcacuz";
    }
    
    
    // Em EspadaDeAlcacuz.java
    @Override
    public void usar(Cavaleiro cavaleiro) {
        System.out.println(cavaleiro.getNome() + " equipou a " + this.getNome() + "!");
        
        cavaleiro.setArmado(true);

        cavaleiro.setArma(this);
        
        cavaleiro.setPotencia(this.ataque);

        System.out.println("Ataque aumentado para " + this.ataque + "!");
    }

    @Override
    public void setAtaque(int novoAtaque,Cavaleiro cavaleiro){
        this.ataque = novoAtaque;
        cavaleiro.setPotencia(novoAtaque);
    }
    
    @Override
    public int getAtaque() {
        return this.ataque;
    }

    @Override
    public void quebrar(Cavaleiro cavaleiro) {
        cavaleiro.setArmado(false);
        cavaleiro.setArma(null);
        cavaleiro.setPotencia(0);
    }
    @Override
    public void fortalecer(int nivel) {
        // Aumenta o ataque em 1 a cada 4 niveis
        this.ataque = ataque + (nivel / 4);
    }
}
