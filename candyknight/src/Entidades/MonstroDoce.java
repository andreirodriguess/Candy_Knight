/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author felip
 */
public abstract class MonstroDoce extends EntidadeJogo {

    private int recompensaEmDinheiro;

    public MonstroDoce(String nome, int vidaMax, int forca, int recompensa) {
        super(nome, vidaMax, forca);
        this.recompensaEmDinheiro = recompensa;
    }

    public int getRecompensaEmDinheiro() {
        return recompensaEmDinheiro;
    }
    
    @Override
    public void atacar(EntidadeJogo alvo)
    {
        boolean alvo_armado = alvo.getArmado();
        if(!alvo_armado)//se o alvo não estiver armado, o monstro ataca com seus pontos de vida
            alvo.receberDano(this.getPontosDeVidaAtuais());
        
    }
    
    public void fortalecer(int nivel) {
        if (nivel <= 0) return;

        // Aumenta a vida em 1 a cada 3 niveis
        int novaVida = this.getPontosDeVidaAtuais() + (nivel / 3);
        this.setPontosDeVidaMax(novaVida);
        this.setPontosDeVidaAtuais(novaVida); // Cura o monstro para a nova vida máxima

        System.out.println(">>> O " + this.getNome() + " apareceu mais forte! (Nível " + nivel + ")");
    }
}
