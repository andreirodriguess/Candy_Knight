/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author kelvi
 */
public class PeDeMolequinhoFase2 extends MonstroDoce {
    
    public PeDeMolequinhoFase2() {
        // Nome, Vida, Ataque, Recompensa
        super("Fragmentos de Amendoim", 30, 15, 50);
    }
    
    public void atacar(EntidadeJogo alvo) {
        System.out.println("Os fragmentos afiados cortam " + alvo.getNome() + "!");
        alvo.receberDano(this.getPotencia());
    }
    
    public void morrer() {
        System.out.println("Os fragmentos viraram farinha. Vitória final!");
    }
}
