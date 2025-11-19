/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Coletaveis; // Ou package candyknight.Coletaveis; dependendo da sua pasta

import Entidades.Cavaleiro;

/**
 *
 * @author felip
 */
public class EspadaDeAlcacuz extends Arma {
    
    private int ataque;

    // Construtor ajustado para aceitar o nível e escalar o dano
    public EspadaDeAlcacuz(int nivel) {
        // Dano base 3 + 1 por nível. 
        // Nível 0 = 3 dano (Mata monstro fraco em 1 hit, médio em 2)
        // Nível 5 = 8 dano
        this.ataque = 3 + nivel;
    }

    // Construtor padrão (caso seja chamado sem nível, define nível 0)
    public EspadaDeAlcacuz() {
        this(0);
    }
    
    @Override
    public String getNome() {
        return "Espada de Alcaçuz";
    }
    
    @Override
    public void usar(Cavaleiro cavaleiro) {
        System.out.println(cavaleiro.getNome() + " equipou a " + this.getNome() + "!");
        
        cavaleiro.setArmado(true);
        cavaleiro.setArma(this);
        cavaleiro.setPotencia(this.ataque);

        System.out.println("Ataque definido para " + this.ataque + "!");
    }

    @Override
    public void setAtaque(int novoAtaque, Cavaleiro cavaleiro){
        this.ataque = novoAtaque;
        cavaleiro.setPotencia(novoAtaque);
    }
    
    @Override
    public int getAtaque() {
        return this.ataque;
    }

    @Override
    public void quebrar(Cavaleiro cavaleiro) {
        System.out.println("A " + this.getNome() + " quebrou!");
        cavaleiro.setArmado(false);
        cavaleiro.setArma(null);
        // Retorna a força do cavaleiro para o base (ex: 2)
        // Idealmente o cavaleiro deveria guardar sua força base, mas aqui resetamos para algo baixo.
        cavaleiro.setPotencia(2); 
    }
}