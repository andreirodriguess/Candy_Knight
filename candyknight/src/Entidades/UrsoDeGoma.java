package Entidades;

/**
 *
 * @author felip
 */
public class UrsoDeGoma extends MonstroDoce {

    public UrsoDeGoma(int nivelDificuldade) {
        super(
            "Urso de Goma Grudento", 
            3 + (nivelDificuldade * 1), // Vida Base: 3. Aumenta 1 por nível.
            1 + (nivelDificuldade * 1), // Força Base: 1. Aumenta 1 por nível.
            5 + (nivelDificuldade * 2)  // Recompensa
        );
    }

    @Override
    public void atacar(EntidadeJogo alvo) {
        if (!alvo.estaVivo()) return;

        System.out.println(this.getNome() + " dá uma patada grudenta em " + alvo.getNome() + "!");
        alvo.receberDano(this.getPotencia());
    }

    @Override
    public void morrer() {
        System.out.println(this.getNome() + " derreteu e foi derrotado!");
    }
}