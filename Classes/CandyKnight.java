/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.candyknight;

/**
 *
 * @author felip
 */
public class CandyKnight {

    public void iniciarCombate(Cavaleiro jogador, MonstroDoce monstro) {
        System.out.println("--- Combate iniciado! ---");
        System.out.println(jogador.getNome() + " vs " + monstro.getNome());

        while (jogador.estaVivo() && monstro.estaVivo()) {
            jogador.atacar(monstro);

            if (monstro.estaVivo()) {
                monstro.atacar(jogador);
            }
        }

        // Fim do combate
        if (!monstro.estaVivo()) {
            System.out.println(jogador.getNome() + " venceu!");
            jogador.coletarDinheiro(monstro.getRecompensaEmDinheiro());
        }
    }

    public static void main(String[] args) {
        CandyKnight meuJogo = new CandyKnight();
        Cavaleiro heroi = new Cavaleiro("Sir Crocante");
        
        MonstroDoce monstro1 = new UrsoDeGoma();
        MonstroDoce monstro2 = new SoldadoGengibre();

        meuJogo.iniciarCombate(heroi, monstro1);
        
        if(heroi.estaVivo()) {
             meuJogo.iniciarCombate(heroi, monstro2);
        }
    }
}
