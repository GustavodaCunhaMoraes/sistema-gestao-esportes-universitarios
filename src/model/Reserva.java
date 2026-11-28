package model;

import exception.DadoInvalidoException;
import exception.EspacoEmManutencaoException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reserva {
    private EspacoEsportivo espaco;
    private Equipe equipe;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private String status; // PENDENTE, APROVADA, REJEITADA

    public Reserva(EspacoEsportivo espaco, Equipe equipe, LocalDateTime inicio, LocalDateTime fim)
            throws DadoInvalidoException, EspacoEmManutencaoException {
        if (espaco == null) {
            throw new DadoInvalidoException("O espaço esportivo é obrigatório para a reserva.");
        }
        if (equipe == null) {
            throw new DadoInvalidoException("A equipe solicitante é obrigatória.");
        }
        if (inicio == null || fim == null || !fim.isAfter(inicio)) {
            throw new DadoInvalidoException("O horário de término deve ser posterior ao horário de início.");
        }
        espaco.verificarDisponibilidade();

        this.espaco = espaco;
        this.equipe = equipe;
        this.inicio = inicio;
        this.fim = fim;
        this.status = "PENDENTE";
    }

    public boolean conflitaCom(EspacoEsportivo outroEspaco, LocalDateTime outroInicio, LocalDateTime outroFim) {
        if (!this.espaco.equals(outroEspaco)) {
            return false;
        }
        if ("REJEITADA".equals(this.status)) {
            return false;
        }
        return this.inicio.isBefore(outroFim) && this.fim.isAfter(outroInicio);
    }

    public EspacoEsportivo getEspaco() {
        return espaco;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public String getStatus() {
        return status;
    }

    public void aprovar() {
        this.status = "APROVADA";
    }

    public void rejeitar() {
        this.status = "REJEITADA";
    }

    public String exibirResumo() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "[" + status + "] Espaço: " + espaco.getNome() +
               " | Equipe: " + equipe.getNome() +
               " | De: " + inicio.format(fmt) + " até " + fim.format(fmt);
    }
}