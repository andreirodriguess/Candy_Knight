/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author felip
 */
public class UrsoDeGoma extends MonstroDoce {

    public UrsoDeGoma() {
        super("ursoDeGoma", 6, 5, 10);
    }

    public void atacar(EntidadeJogo alvo) {
        if (!alvo.estaVivo()) return;

        System.out.println(this.getNome() + " dá uma patada grudenta em " + alvo.getNome() + "!");
        alvo.receberDano(this.getPotencia());
    }

    public void morrer() {
        System.out.println(this.getNome() + " derreteu e foi derrotado!");
    }
}