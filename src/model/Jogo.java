package model;

import exception.DadoInvalidoException;
import exception.JogoFinalizadoException;
import exception.JogoSemEquipeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Jogo {
    private int duracao;
    private int placarTimeA = 0;
    private int placarTimeB = 0;
    private LocalDateTime horario;
    private Equipe equipeA;
    private Equipe equipeB;
    private EspacoEsportivo local;
    private static final int VITORIA = 3;
    private static final int EMPATE = 1;
    private static final int DERROTA = 0;
    private boolean finalizado = false;

    public Jogo(int duracao, LocalDateTime horario, Equipe equipeA, Equipe equipeB)
            throws JogoSemEquipeException, DadoInvalidoException {
        if (equipeA == null || equipeB == null) {
            throw new JogoSemEquipeException("Um jogo não pode ser criado sem duas equipes participantes.");
        }
        if (equipeA.equals(equipeB)) {
            throw new DadoInvalidoException("Uma equipe não pode jogar contra si mesma.");
        }
        setDuracao(duracao);
        setHorario(horario);
        this.equipeA = equipeA;
        this.equipeB = equipeB;
    }

    public void registrarPlacar(int placarTimeA, int placarTimeB)
            throws JogoFinalizadoException, DadoInvalidoException {
        if (this.finalizado) {
            throw new JogoFinalizadoException("Este jogo já foi finalizado e não pode ter o placar alterado.");
        }
        if (placarTimeA < 0 || placarTimeB < 0) {
            throw new DadoInvalidoException("O placar não pode ser negativo.");
        }
        this.placarTimeA = placarTimeA;
        this.placarTimeB = placarTimeB;
        distribuirPontos();
    }

    private void distribuirPontos() {
        if (placarTimeA == placarTimeB) {
            equipeA.adicionarPontos(EMPATE);
            equipeB.adicionarPontos(EMPATE);
        } else if (placarTimeA > placarTimeB) {
            equipeA.adicionarPontos(VITORIA);
            equipeB.adicionarPontos(DERROTA);
        } else {
            equipeA.adicionarPontos(DERROTA);
            equipeB.adicionarPontos(VITORIA);
        }
        this.finalizado = true;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) throws DadoInvalidoException {
        if (duracao <= 0) {
            throw new DadoInvalidoException("A duração do jogo deve ser maior que zero.");
        }
        this.duracao = duracao;
    }

    public int getPlacarTimeA() {
        return placarTimeA;
    }

    public int getPlacarTimeB() {
        return placarTimeB;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }

    public Equipe getEquipeA() {
        return equipeA;
    }

    public Equipe getEquipeB() {
        return equipeB;
    }

    public EspacoEsportivo getLocal() {
        return local;
    }

    public void setLocal(EspacoEsportivo local) {
        this.local = local;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public String visualizarPlacarJogo() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataStr = (horario != null) ? horario.format(fmt) : "Data a definir";
        String statusStr = finalizado ? "[FINALIZADO]" : "[AGENDADO]";
        return statusStr + " " + dataStr + " | " + equipeA.getNome() + " " + placarTimeA + " x " + placarTimeB + " " + equipeB.getNome();
    }
}