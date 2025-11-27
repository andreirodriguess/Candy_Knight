package Entidades;

public class PeDeMolequinho extends MonstroDoce {

    public PeDeMolequinho() {
        // Nome, Vida, Ataque, Recompensa
        super("PeDeMoleque", 5, 5, 10);
    }

    @Override
    public void atacar(EntidadeJogo alvo) {
        System.out.println("O Pé de Molequinho dá uma cabeçada dura!");
        alvo.receberDano(this.getPotencia());
    }
    
    @Override
    public void morrer() {
        System.out.println("A casca do Pé de Moleque se partiu!");
    }
}