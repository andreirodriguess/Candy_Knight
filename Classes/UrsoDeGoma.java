/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.candyknight;

/**
 *
 * @author felip
 */
public class UrsoDeGoma extends MonstroDoce {

    public UrsoDeGoma() {
        super("Urso de Goma Grudento", 30, 5, 10);
    }

    public void atacar(EntidadeJogo alvo) {
        if (!alvo.estaVivo()) return;

        System.out.println(this.getNome() + " dá uma patada grudenta em " + alvo.getNome() + "!");
        alvo.receberDano(this.getForcaAtaque());
    }

    public void morrer() {
        System.out.println(this.getNome() + " derreteu e foi derrotado!");
    }
}