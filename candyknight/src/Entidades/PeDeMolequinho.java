/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author felip
 */
public class PeDeMolequinho extends MonstroDoce {

    private boolean estaDividido;
    private int vidaFase2;

    public PeDeMolequinho(int nivelDificuldade) {
        super(
            "Pé de Molequinho Duro", 
            5 + (nivelDificuldade * 2), // Vida Fase 1 (Base 5)
            3 + (nivelDificuldade * 1), // Força (Base 3)
            15 + (nivelDificuldade * 5)
        );
        
        // A vida dos fragmentos (Fase 2)
        this.vidaFase2 = 3 + (nivelDificuldade * 1); // Base 3
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
        alvo.receberDano(this.getPotencia());
    }

    @Override
    public void receberDano(int quantidade) {
        if (!this.estaVivo()) return;

        int vidaAtual = this.getPontosDeVidaAtuais();
        vidaAtual -= quantidade;
        System.out.println(this.getNome() + " recebeu " + quantidade + " de dano!");

        if (vidaAtual <= 0) {
            if (!estaDividido) {
                System.out.println(this.getNome() + " se quebra e se divide em dois fragmentos!");
                this.estaDividido = true;
                
                // Configura status da Fase 2
                this.setPontosDeVidaMax(this.vidaFase2); 
                this.setPontosDeVidaAtuais(this.vidaFase2);
                this.setNome("Fragmentos de Pé de Moleque");

                // Dano excedente (Overkill) passa para a próxima fase
                int danoExcedente = Math.abs(vidaAtual); 
                if (danoExcedente > 0) {
                    System.out.println("O dano excedente atinge os fragmentos!");
                    this.receberDano(danoExcedente); 
                }

            } else {
                this.setPontosDeVidaAtuais(0);
                this.morrer(); 
            }
        } else {
            this.setPontosDeVidaAtuais(vidaAtual);
        }
    }

    @Override
    public void morrer() {
        System.out.println(this.getNome() + " foi totalmente esfarelado!");
    }
}