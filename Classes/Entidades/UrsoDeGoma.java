/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package candyknight.Entidades;

/**
 *
 * @author felip
 */
public class UrsoDeGoma extends MonstroDoce {

    // +++ MUDANÇA: Construtor aceita nível de dificuldade +++
    public UrsoDeGoma(int nivel) {
        // +++ MUDANÇA: Valores base reduzidos (30->3, 10->1) +++
        int vidaBase = 3;
        int recompensaBase = 1;
        
        // +++ MUDANÇA: Lógica de scaling +++
        // Aumenta a vida em 1 a cada 3 níveis
        int vidaAjustada = vidaBase + (nivel / 3);
        
        super("Urso de Goma Grudento", vidaAjustada, recompensaBase);
    }

    public void atacar(EntidadeJogo alvo) {
        if (!alvo.estaVivo()) return;

        System.out.println(this.getNome() + " dá uma patada grudenta em " + alvo.getNome() + "!");
        alvo.receberDano(this.getPontosDeVidaAtuais());
    }

    public void morrer() {
        System.out.println(this.getNome() + " derreteu e foi derrotado!");
    }
}