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

    // +++ MUDANÇA: Construtor aceita nível de dificuldade +++
    public SoldadoGengibre(int nivel) {
        // +++ MUDANÇA: Valores base reduzidos (35->4, 2->1) +++
        int vidaBase = 4; // 3.5 arredondado para 4
        int recompensaBase = 1;
        
        // +++ MUDANÇA: Lógica de scaling +++
        // Aumenta a vida em 1 a cada 3 níveis
        int vidaAjustada = vidaBase + (nivel / 3);
        
        super("Soldado de Gengibre", vidaAjustada, recompensaBase);
    }

    public void atacar(EntidadeJogo alvo) {
        if (!alvo.estaVivo()) return;
        
        System.out.println(this.getNome() + " golpeia com seu bastão de açúcar!");
        alvo.receberDano(this.getPontosDeVidaAtuais());
    }

    public void morrer() {
        System.out.println(this.getNome() + " se espatifou!");
    }
}