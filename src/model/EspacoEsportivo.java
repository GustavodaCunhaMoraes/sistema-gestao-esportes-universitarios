package model;

import exception.DadoInvalidoException;
import exception.EspacoEmManutencaoException;

public class EspacoEsportivo {
    private String nome;
    private String tipo;
    private String localizacao;
    private int capacidade;
    private String horaDisp;
    private boolean emFuncionamento;

    public EspacoEsportivo(String nome, String tipo, String localizacao,
                           int capacidade, String horaDisp, boolean emFuncionamento) throws DadoInvalidoException {
        setNome(nome);
        setTipo(tipo);
        setLocalizacao(localizacao);
        setCapacidade(capacidade);
        setHoraDisp(horaDisp);
        setEmFuncionamento(emFuncionamento);
    }

    public void verificarDisponibilidade() throws EspacoEmManutencaoException {
        if (!emFuncionamento) {
            throw new EspacoEmManutencaoException("O espaço '" + nome + "' está em manutenção e não pode ser reservado.");
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws DadoInvalidoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do espaço é obrigatório.");
        }
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) throws DadoInvalidoException {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new DadoInvalidoException("Tipo do espaço é obrigatório.");
        }
        this.tipo = tipo;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) throws DadoInvalidoException {
        if (capacidade <= 0) {
            throw new DadoInvalidoException("A capacidade deve ser maior que zero.");
        }
        this.capacidade = capacidade;
    }

    public String getHoraDisp() {
        return horaDisp;
    }

    public void setHoraDisp(String horaDisp) {
        this.horaDisp = horaDisp;
    }

    public boolean isEmFuncionamento() {
        return emFuncionamento;
    }

    public void setEmFuncionamento(boolean emFuncionamento) {
        this.emFuncionamento = emFuncionamento;
    }

    public void exibirInfo() {
        String status = emFuncionamento ? "Ativo" : "Em Manutenção";
        System.out.println("Nome: " + nome + " | Tipo: " + tipo +
                " | Localização: " + localizacao + " | Capacidade: " + capacidade +
                " | Horários: " + horaDisp + " | Status: " + status);
    }
}