/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.candyknight;

/**
 *
 * @author felip
 */
public class PeDeMolequinho extends MonstroDoce {

    private boolean estaDividido;
    private int vidaFase2;

    public PeDeMolequinho() {
        // (Nome, VidaFase1, Forca, Recompensa)
        super("Pé de Molequinho Duro", 60, 10, 30);
        this.vidaFase2 = 40; // Vida total dos dois fragmentos juntos
        this.estaDividido = false;
    }

    @Override
    public void atacar(EntidadeJogo alvo) {
        if (!alvo.estaVivo()) return;

        if (!estaDividido) {
            System.out.println(this.getNome() + " dá uma cabeçada crocante em " + alvo.getNome() + "!");
        } else {
            System.out.println(this.getNome() + " ataca com um chute duplo de amendoim!");
        }
        alvo.receberDano(this.getForcaAtaque());
    }

    /**
     * Sobrescreve o método base para implementar a divisão.
     * Isso REQUER que a classe EntidadeJogo tenha os métodos:
     * - setPontosDeVidaAtuais(int valor)
     * - setPontosDeVidaMax(int valor)
     */
    @Override
    public void receberDano(int quantidade) {
        if (!this.estaVivo()) return;

        int vidaAtual = this.getPontosDeVidaAtuais();
        vidaAtual -= quantidade;
        System.out.println(this.getNome() + " recebeu " + quantidade + " de dano!");

        if (vidaAtual <= 0) {
            // Monstro foi derrotado
            if (!estaDividido) {
                // Estava na Fase 1, agora transiciona para a Fase 2
                System.out.println(this.getNome() + " se quebra e se divide em dois fragmentos!");
                this.estaDividido = true;
                
                // Define a nova vida máxima e cura totalmente para a Fase 2
                // (Assumindo que os setters existem em EntidadeJogo)
                this.setPontosDeVidaMax(this.vidaFase2); 
                this.setPontosDeVidaAtuais(this.vidaFase2);
                this.setNome("Fragmentos de Pé de Moleque"); // Muda o nome

                // Se o dano foi maior (ex: 70 de dano em 60 de vida), 
                // o dano excedente (10) é aplicado à Fase 2.
                int danoExcedente = Math.abs(vidaAtual); // vidaAtual é negativa (ex: -10)
                if (danoExcedente > 0) {
                    System.out.println("O dano excedente atinge os fragmentos!");
                    this.receberDano(danoExcedente); // Chama recursivamente para a Fase 2
                }

            } else {
                // Estava na Fase 2 e foi derrotado
                this.setPontosDeVidaAtuais(0);
                this.morrer(); // Chama a morte definitiva
            }
        } else {
            // Apenas recebeu dano normal
            this.setPontosDeVidaAtuais(vidaAtual);
        }
    }

    @Override
    public void morrer() {
        // Este método só é chamado pela lógica acima quando o monstro
        // é derrotado na Fase 2.
        System.out.println(this.getNome() + " foi totalmente esfarelado!");
    }
}