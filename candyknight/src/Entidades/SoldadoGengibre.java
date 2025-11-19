package Entidades;

public class SoldadoGengibre extends MonstroDoce {

    public SoldadoGengibre(int nivelDificuldade) {
        super(
            "Soldado de Gengibre", 
            4 + (nivelDificuldade * 2), // Vida Base: 4. Escala mais rápido (+2/nível)
            2 + (nivelDificuldade * 1), // Força Base: 2
            10 + (nivelDificuldade * 3) // Recompensa maior
        );
    }

    @Override
    public void atacar(EntidadeJogo alvo) {
        if (!alvo.estaVivo()) return;
        
        System.out.println(this.getNome() + " golpeia com seu bastão de açúcar!");
        alvo.receberDano(this.getPotencia());
    }

    @Override
    public void morrer() {
        System.out.println(this.getNome() + " se espatifou!");
    }
}