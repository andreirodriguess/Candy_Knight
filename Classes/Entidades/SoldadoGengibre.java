/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package candyknight.Entidades;
/**
 *
 * @author felip
 */
public class SoldadoGengibre extends MonstroDoce {

    public SoldadoGengibre() {
        super("Soldado de Gengibre", 50, 8, 20);
    }

    public void atacar(EntidadeJogo alvo) {
        if (!alvo.estaVivo()) return;
        
        System.out.println(this.getNome() + " golpeia com seu bastão de açúcar!");
        alvo.receberDano(this.getPotencia());
    }

    public void morrer() {
        System.out.println(this.getNome() + " se espatifou!");
    }
}