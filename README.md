# 🍬 Candy Knight

Candy Knight é um jogo de estratégia em turnos, fortemente inspirado na jogabilidade de Dungeon Cards. Você assume o papel de um Cozinheiro Cavaleiro que deve enfrentar hordas de monstros feitos de doces para coletar o máximo de moedas possível.

## Como Funciona o Jogo

O jogo acontece em um grid 3x3. O seu personagem está sempre no centro da ação ou se movendo para as bordas. A mecânica principal é o movimento do personagem no tabuleiro.

### Combate e Movimento
Toda vez que você tenta se mover para uma célula ocupada, uma interação acontece:

1.  Se for um Item: Você o coleta ou equipa automaticamente.
2.  Se for um Monstro: Você entra em combate.
    * Com Arma: Você ataca o monstro. A durabilidade da sua arma diminui baseada na vida do inimigo.
    * Sem Arma: Você sofre dano igual à vida atual do monstro. Se sobreviver, o monstro é eliminado.
    * Com Escudo de Goma: Se você tiver o efeito do escudo ativo, ao tentar mover-se para um monstro, vocês trocam de lugar sem que você sofra dano.

### Objetivo
Derrote monstros para que eles deixem cair Moedas. Colete-as para aumentar sua pontuação final. O jogo acaba quando sua vida chega a zero.


## Entidades e Itens

### O Herói
* Cavaleiro: Começa com uma Espada de Alcaçuz. Precisa gerenciar sua vida e a durabilidade de suas armas para sobreviver.

### Os Inimigos 
* Urso de Goma: Um inimigo básico.
* Soldado de Gengibre: Mais resistente.
* Pé de Molequinho: Ao morrer pela primeira vez, ele se divide em fragmentos, exigindo mais ataques para ser totalmente derrotado.

### Os Itens (Coletáveis)
* Espada de Alcaçuz: Aumenta seu ataque. Perde potência conforme você bate em inimigos com muita vida.
* Poção: Recupera pontos de vida do herói.
* Escudo de Goma: Um item tático. Permite trocar de posição com um inimigo em vez de atacá-lo.
* Moeda: Deixada pelos monstros derrotados. 


## Como Rodar o Jogo

1.  Baixe o repositório:
    ```bash
    git clone https://github.com/andreirodriguess/Candy_Knighthttps://github.com/andreirodriguess/Candy_Knight
    ```

2. Abra o projeto no Netbeans e clique em "Clean and Build Project"

3. Compile o projeto
