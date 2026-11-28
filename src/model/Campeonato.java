package model;

import exception.CampeonatoEncerradoException;
import exception.DadoInvalidoException;
import exception.JogoFinalizadoException;
import exception.JogoSemEquipeException;
import exception.NaoCoordenadorException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Campeonato {
    private String nome;
    private List<String> regulamento = new ArrayList<>();
    private Modalidade modalidade;
    private List<Jogo> tabelaJogos = new ArrayList<>();
    private List<Equipe> equipesParticipantes = new ArrayList<>();
    private int duracaoPartida;
    private boolean encerrado = false;

    public Campeonato(Pessoa criador, String nome, Modalidade modalidade, int duracaoPartida)
            throws NaoCoordenadorException, DadoInvalidoException {
        if (!(criador instanceof Coordenador)) {
            throw new NaoCoordenadorException("Apenas coordenadores esportivos podem cadastrar campeonatos.");
        }
        setNome(nome);
        setModalidade(modalidade);
        setDuracaoPartida(duracaoPartida);
    }

    // --- Inscrição de Equipes com Validações ---
    public void inscreverEquipe(Equipe equipe) throws CampeonatoEncerradoException, DadoInvalidoException {
        if (encerrado) {
            throw new CampeonatoEncerradoException("O campeonato '" + nome + "' já está encerrado e não aceita novas inscrições.");
        }
        if (equipe == null) {
            throw new DadoInvalidoException("Equipe inválida.");
        }
        if (equipe.getModalidade() != this.modalidade) {
            throw new DadoInvalidoException("A equipe '" + equipe.getNome() + "' é de " + equipe.getModalidade() +
                    ", incompatível com o campeonato de " + this.modalidade + ".");
        }
        if (equipesParticipantes.contains(equipe)) {
            throw new DadoInvalidoException("A equipe '" + equipe.getNome() + "' já está inscrita neste campeonato.");
        }
        equipesParticipantes.add(equipe);
        equipe.registrarHistorico("Inscrita no campeonato: " + this.nome);
    }

    // --- Geração Automática da Tabela de Jogos (Todos contra Todos) ---
    public void gerarTabelaJogos(LocalDateTime dataInicio, int intervaloDiasEntreRodadas)
            throws CampeonatoEncerradoException, DadoInvalidoException, JogoSemEquipeException {
        if (encerrado) {
            throw new CampeonatoEncerradoException("Não é possível gerar jogos para um campeonato encerrado.");
        }
        if (equipesParticipantes.size() < 2) {
            throw new DadoInvalidoException("São necessárias pelo menos 2 equipes inscritas para gerar a tabela de jogos.");
        }
        if (dataInicio == null) {
            throw new DadoInvalidoException("A data de início dos jogos deve ser informada.");
        }

        tabelaJogos.clear();
        int contadorJogos = 0;

        for (int i = 0; i < equipesParticipantes.size(); i++) {
            for (int j = i + 1; j < equipesParticipantes.size(); j++) {
                Equipe timeA = equipesParticipantes.get(i);
                Equipe timeB = equipesParticipantes.get(j);

                LocalDateTime horarioJogo = dataInicio.plusDays((long) contadorJogos * intervaloDiasEntreRodadas);
                Jogo novoJogo = new Jogo(this.duracaoPartida, horarioJogo, timeA, timeB);
                tabelaJogos.add(novoJogo);
                contadorJogos++;
            }
        }
    }

    // --- Registro de Placar e Atualização Automática da Classificação ---
    public void registrarResultadoJogo(int indiceJogo, int placarA, int placarB)
            throws DadoInvalidoException, JogoFinalizadoException {
        if (indiceJogo < 0 || indiceJogo >= tabelaJogos.size()) {
            throw new DadoInvalidoException("Jogo não encontrado na tabela.");
        }
        Jogo jogo = tabelaJogos.get(indiceJogo);
        jogo.registrarPlacar(placarA, placarB);
        atualizarClassificacao();
    }

    public void atualizarClassificacao() {
        equipesParticipantes.sort(
            Comparator.comparingInt(Equipe::getPontos).reversed()
                      .thenComparing(Equipe::getNome)
        );
    }

    public String visualizarTabelaJogos() {
        if (tabelaJogos.isEmpty()) {
            return "Nenhum jogo gerado para o campeonato " + nome + ".";
        }
        StringBuilder sb = new StringBuilder("------ Tabela de Jogos: " + nome + " ------\n");
        for (int i = 0; i < tabelaJogos.size(); i++) {
            sb.append("[").append(i + 1).append("] ").append(tabelaJogos.get(i).visualizarPlacarJogo()).append("\n");
        }
        return sb.toString();
    }

    public String visualizarClassificacao() {
        atualizarClassificacao();
        StringBuilder sb = new StringBuilder("------ Classificação: " + nome + " ------\n");
        for (int i = 0; i < equipesParticipantes.size(); i++) {
            Equipe e = equipesParticipantes.get(i);
            sb.append(i + 1).append("º - ").append(e.getNome()).append(" | ").append(e.getPontos()).append(" pts\n");
        }
        return sb.toString();
    }

    // --- Getters, Setters e Regulamento ---
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws DadoInvalidoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new DadoInvalidoException("O nome do campeonato é obrigatório.");
        }
        this.nome = nome;
    }

    public void adicionarRegra(String regra) {
        regulamento.add(regra);
    }

    public void removerRegra(String regra) {
        regulamento.remove(regra);
    }

    public List<String> getRegulamento() {
        return regulamento;
    }

    public Modalidade getModalidade() {
        return modalidade;
    }

    public void setModalidade(Modalidade modalidade) throws DadoInvalidoException {
        if (modalidade == null) {
            throw new DadoInvalidoException("A modalidade do campeonato é obrigatória.");
        }
        this.modalidade = modalidade;
    }

    public int getNumEquipes() {
        return equipesParticipantes.size();
    }

    public List<Equipe> getEquipesParticipantes() {
        return equipesParticipantes;
    }

    public List<Jogo> getTabelaJogos() {
        return tabelaJogos;
    }

    public int getDuracaoPartida() {
        return duracaoPartida;
    }

    public void setDuracaoPartida(int duracaoPartida) throws DadoInvalidoException {
        if (duracaoPartida <= 0) {
            throw new DadoInvalidoException("A duração da partida deve ser maior que zero.");
        }
        this.duracaoPartida = duracaoPartida;
    }

    public boolean isEncerrado() {
        return encerrado;
    }

    public void encerrarCampeonato() {
        this.encerrado = true;
    }
}