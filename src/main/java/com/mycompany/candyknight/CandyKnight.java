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

        // Loop de combate
        while (jogador.estaVivo() && monstro.estaVivo()) {
            
            // 1. O jogador ataca o monstro
            // POLIMORFISMO: Não importa qual monstro é,
            // o cavaleiro simplesmente chama "atacar".
            jogador.atacar(monstro);

            if (monstro.estaVivo()) {
                // 2. O monstro ataca o jogador
                // POLIMORFISMO: Não importa se é um UrsoDeGoma ou SoldadoGengibre,
                // o jogo simplesmente diz "monstro, ataque!".
                // O Java chama o @Override de atacar() correto.
                monstro.atacar(jogador);
            }
        }

        // Fim do combate
        if (!monstro.estaVivo()) {
            System.out.println(jogador.getNome() + " venceu!");
            jogador.coletarDinheiro(monstro.getRecompensaEmDinheiro());
        }
    }

    // Método principal para testar
    public static void main(String[] args) {
        CandyKnight meuJogo = new CandyKnight();
        Cavaleiro heroi = new Cavaleiro("Sir Crocante");
        
        // Polimorfismo na prática:
        MonstroDoce monstro1 = new UrsoDeGoma();
        MonstroDoce monstro2 = new SoldadoGengibre();

        // O método iniciarCombate() aceita qualquer coisa
        // que "É UM" MonstroDoce.
        meuJogo.iniciarCombate(heroi, monstro1);
        
        // Se o herói ainda estiver vivo
        if(heroi.estaVivo()) {
             meuJogo.iniciarCombate(heroi, monstro2);
        }
    }
}
