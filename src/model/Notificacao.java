package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notificacao {
    private String titulo;
    private String mensagem;
    private LocalDateTime dataHora;
    private boolean lida;

    public Notificacao(String titulo, String mensagem) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.dataHora = LocalDateTime.now();
        this.lida = false;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public boolean isLida() {
        return lida;
    }

    public void marcarComoLida() {
        this.lida = true;
    }

    public String formatar() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String status = lida ? "[LIDA]" : "[NOVA]";
        return status + " (" + dataHora.format(fmt) + ") " + titulo + ": " + mensagem;
    }
}