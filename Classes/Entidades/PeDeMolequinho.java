/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package candyknight.Entidades;

/**
 *
 * @author felip
 */
public class PeDeMolequinho extends MonstroDoce {

    private boolean estaDividido;
    private int vidaFase2;

    // +++ MUDANÇA: Construtor aceita nível de dificuldade +++
    public PeDeMolequinho(int nivel) {
        // +++ MUDANÇA: Valores base reduzidos +++
        int vidaBaseFase1 = 2; // 15 -> 1.5 -> 2
        int vidaBaseFase2 = 4; // 40 -> 4
        int recompensaBase = 1; // 3 -> 1
        
        // +++ MUDANÇA: Lógica de scaling +++
        // Aumenta a vida em 1 a cada 3 níveis
        int vidaAjustadaFase1 = vidaBaseFase1 + (nivel / 3);
        int vidaAjustadaFase2 = vidaBaseFase2 + (nivel / 3);

        super("Pé de Molequinho Duro", vidaAjustadaFase1, recompensaBase);
        this.vidaFase2 = vidaAjustadaFase2; // Define a vida da Fase 2 ajustada
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
        alvo.receberDano(this.getPontosDeVidaAtuais());
    }

    /**
     * Sobrescreve o método base para implementar a divisão.
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
                
                // Define a nova vida máxima (já calculada no construtor)
                this.setPontosDeVidaMax(this.vidaFase2); 
                this.setPontosDeVidaAtuais(this.vidaFase2);
                this.setNome("Fragmentos de Pé de Moleque"); 

                int danoExcedente = Math.abs(vidaAtual); 
                if (danoExcedente > 0) {
                    System.out.println("O dano excedente atinge os fragmentos!");
                    this.receberDano(danoExcedente); 
                }

            } else {
                // Estava na Fase 2 e foi derrotado
                this.setPontosDeVidaAtuais(0);
                this.morrer(); 
            }
        } else {
            // Apenas recebeu dano normal
            this.setPontosDeVidaAtuais(vidaAtual);
        }
    }

    @Override
    public void morrer() {
        System.out.println(this.getNome() + " foi totalmente esfarelado!");
    }
}