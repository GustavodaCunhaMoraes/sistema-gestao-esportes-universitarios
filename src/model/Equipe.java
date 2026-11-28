package model;

import exception.AtletaEmMaisDeUmaEquipeException;
import exception.AtletaSuspensoException;
import exception.DadoInvalidoException;
import java.util.ArrayList;
import java.util.List;

public class Equipe {
    private String nome;
    private Modalidade modalidade;
    private List<Atleta> elenco = new ArrayList<>();
    private List<Atleta> escalacaoAtual = new ArrayList<>();
    private List<String> historicoParticipacao = new ArrayList<>();
    private Tecnico tecnico;
    private int pontos = 0;

    public Equipe(String nome, Modalidade modalidade, Tecnico tecnico) throws DadoInvalidoException {
        setNome(nome);
        setModalidade(modalidade);
        setTecnico(tecnico);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws DadoInvalidoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new DadoInvalidoException("O nome da equipe não pode ser vazio.");
        }
        this.nome = nome;
    }

    public Modalidade getModalidade() {
        return modalidade;
    }

    public void setModalidade(Modalidade modalidade) throws DadoInvalidoException {
        if (modalidade == null) {
            throw new DadoInvalidoException("A modalidade da equipe é obrigatória.");
        }
        this.modalidade = modalidade;
    }

    public void adicionarAtleta(Atleta atleta) throws AtletaEmMaisDeUmaEquipeException, DadoInvalidoException {
        if (atleta == null) {
            throw new DadoInvalidoException("Atleta inválido.");
        }
        atleta.vincularEquipe(this);
        elenco.add(atleta);
    }

    public void removerAtleta(Atleta atleta) {
        if (elenco.remove(atleta)) {
            atleta.desvincularEquipe(this);
            escalacaoAtual.remove(atleta);
        }
    }

    public void escalarAtleta(Atleta atleta) throws AtletaSuspensoException, DadoInvalidoException {
        if (!elenco.contains(atleta)) {
            throw new DadoInvalidoException("O atleta " + atleta.getNome() + " não pertence ao elenco desta equipe.");
        }
        if (atleta.isSuspenso()) {
            throw new AtletaSuspensoException("O atleta " + atleta.getNome() + " está suspenso e não pode ser escalado.");
        }
        if (!escalacaoAtual.contains(atleta)) {
            escalacaoAtual.add(atleta);
        }
    }

    public void limparEscalacao() {
        escalacaoAtual.clear();
    }

    public List<Atleta> getEscalacaoAtual() {
        return escalacaoAtual;
    }

    public List<Atleta> getElenco() {
        return elenco;
    }

    public void registrarHistorico(String evento) {
        historicoParticipacao.add(evento);
    }

    public String visualizarHistorico() {
        if (historicoParticipacao.isEmpty()) {
            return "Nenhum histórico registrado para a equipe " + nome + ".";
        }
        StringBuilder sb = new StringBuilder("------ Histórico: " + nome + " ------\n");
        for (String item : historicoParticipacao) {
            sb.append("- ").append(item).append("\n");
        }
        return sb.toString();
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public void setTecnico(Tecnico tecnico) throws DadoInvalidoException {
        if (tecnico == null) {
            throw new DadoInvalidoException("A equipe precisa de um técnico responsável.");
        }
        this.tecnico = tecnico;
        tecnico.setEquipeAtual(this.nome);
    }

    public int getPontos() {
        return pontos;
    }

    public void adicionarPontos(int pontos) {
        this.pontos += pontos;
    }

    public void resetarPontos() {
        this.pontos = 0;
    }

    public String visualizarElenco() {
        StringBuilder sb = new StringBuilder("------ Jogadores (" + nome + " - " + modalidade + ") ------\n");
        for (int i = 0; i < elenco.size(); i++) {
            Atleta a = elenco.get(i);
            String status = a.isSuspenso() ? " [SUSPENSO]" : "";
            sb.append(i + 1).append("- ").append(a.getNome()).append(status).append("\n");
        }
        sb.append("------ Técnico ------\n").append(tecnico.getNome());
        return sb.toString();
    }
}