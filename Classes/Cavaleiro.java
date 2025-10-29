/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.candyknight;

/**
 *
 * @author felip
 */
// Cavaleiro "É UMA" EntidadeJogo
public class Cavaleiro extends EntidadeJogo {

    private int dinheiro;

    public Cavaleiro(String nome) {
        super(nome, 100, 15); 
        this.dinheiro = 0;
    }

    public void atacar(EntidadeJogo alvo) {
        if (!alvo.estaVivo()) return;

        System.out.println(this.getNome() + " ataca " + alvo.getNome() + " com sua Espada Doce!");
        alvo.receberDano(this.getForcaAtaque());
    }

    public void morrer() {
        System.out.println(this.getNome() + " foi derrotado! Fim de jogo...");
    }

    public void coletarDinheiro(int quantidade) {
        this.dinheiro += quantidade;
        System.out.println(this.getNome() + " coletou " + quantidade + " moedas!");
    }
}
