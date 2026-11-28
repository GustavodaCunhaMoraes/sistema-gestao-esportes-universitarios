package model;

import exception.AtletaEmMaisDeUmaEquipeException;
import exception.DadoInvalidoException;
import java.util.ArrayList;
import java.util.List;

public class Atleta extends Pessoa {
    private List<Equipe> equipes = new ArrayList<>();
    private boolean suspenso;
    private static int numeroAtletas = 0;

    public Atleta(String nome, String email, String senha, int idade, char sexo, boolean suspenso)
            throws DadoInvalidoException {
        super(nome, email, senha, idade, sexo);
        setSuspenso(suspenso);
        numeroAtletas++;
    }

    @Override
    public String getTipoPerfil() {
        return "Atleta";
    }

    public static int getNumeroAtletas() {
        return numeroAtletas;
    }

    public List<Equipe> getEquipes() {
        return equipes;
    }

    public void vincularEquipe(Equipe novaEquipe) throws AtletaEmMaisDeUmaEquipeException {
        for (Equipe e : equipes) {
            if (e.getModalidade() == novaEquipe.getModalidade()) {
                throw new AtletaEmMaisDeUmaEquipeException(
                    "O atleta " + getNome() + " já está vinculado a uma equipe de " + novaEquipe.getModalidade()
                );
            }
        }
        equipes.add(novaEquipe);
    }

    public void desvincularEquipe(Equipe equipe) {
        equipes.remove(equipe);
    }

    public boolean isSuspenso() {
        return suspenso;
    }

    public void setSuspenso(boolean suspenso) {
        this.suspenso = suspenso;
    }
}