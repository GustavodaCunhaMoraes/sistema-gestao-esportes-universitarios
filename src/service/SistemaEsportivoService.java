package service;

import exception.AtletaEmMaisDeUmaEquipeException;
import exception.AtletaSuspensoException;
import exception.CampeonatoEncerradoException;
import exception.DadoInvalidoException;
import exception.EspacoEmManutencaoException;
import exception.JogoFinalizadoException;
import exception.NaoCoordenadorException;
import exception.ReservaConflitanteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Atleta;
import model.Campeonato;
import model.Coordenador;
import model.Equipe;
import model.EspacoEsportivo;
import model.Modalidade;
import model.Notificacao;
import model.Pessoa;
import model.Reserva;
import model.Tecnico;

public class SistemaEsportivoService {
    private List<Pessoa> usuarios = new ArrayList<>();
    private List<EspacoEsportivo> espacos = new ArrayList<>();
    private List<Equipe> equipes = new ArrayList<>();
    private List<Campeonato> campeonatos = new ArrayList<>();
    private List<Reserva> reservas = new ArrayList<>();
    private Map<Pessoa, List<Notificacao>> notificacoesPorUsuario = new HashMap<>();

    public void cadastrarUsuario(Pessoa usuario) throws DadoInvalidoException {
        if (usuario == null) {
            throw new DadoInvalidoException("Usuário inválido.");
        }
        for (Pessoa p : usuarios) {
            if (p.getEmail().equalsIgnoreCase(usuario.getEmail())) {
                throw new DadoInvalidoException("Já existe um usuário cadastrado com este e-mail.");
            }
        }
        usuarios.add(usuario);
        notificacoesPorUsuario.put(usuario, new ArrayList<>());
    }

    public Pessoa autenticar(String email, String senha) throws DadoInvalidoException {
        for (Pessoa p : usuarios) {
            if (p.getEmail().equalsIgnoreCase(email) && p.getSenha().equals(senha)) {
                return p;
            }
        }
        throw new DadoInvalidoException("E-mail ou senha incorretos.");
    }

    public List<Atleta> listarAtletas() {
        List<Atleta> lista = new ArrayList<>();
        for (Pessoa p : usuarios) {
            if (p instanceof Atleta) {
                lista.add((Atleta) p);
            }
        }
        return lista;
    }

    public void cadastrarEspaco(Pessoa solicitante, EspacoEsportivo espaco)
            throws NaoCoordenadorException, DadoInvalidoException {
        verificarCoordenador(solicitante);
        if (espaco == null) {
            throw new DadoInvalidoException("Espaço inválido.");
        }
        espacos.add(espaco);
    }

    public void removerEspaco(Pessoa solicitante, EspacoEsportivo espaco) throws NaoCoordenadorException {
        verificarCoordenador(solicitante);
        espacos.remove(espaco);
    }

    public List<EspacoEsportivo> buscarEspacosPorTipo(String tipo) {
        List<EspacoEsportivo> encontrados = new ArrayList<>();
        for (EspacoEsportivo e : espacos) {
            if (e.getTipo().equalsIgnoreCase(tipo)) {
                encontrados.add(e);
            }
        }
        return encontrados;
    }

    public List<EspacoEsportivo> listarEspacosDisponiveis() {
        List<EspacoEsportivo> disponiveis = new ArrayList<>();
        for (EspacoEsportivo e : espacos) {
            if (e.isEmFuncionamento()) {
                disponiveis.add(e);
            }
        }
        return disponiveis;
    }

    public Equipe criarEquipe(Tecnico tecnico, String nome, Modalidade modalidade) throws DadoInvalidoException {
        Equipe equipe = new Equipe(nome, modalidade, tecnico);
        equipes.add(equipe);
        return equipe;
    }

    public void convocarAtleta(Tecnico tecnico, Equipe equipe, Atleta atleta)
            throws AtletaEmMaisDeUmaEquipeException, DadoInvalidoException {
        verificarTecnicoDaEquipe(tecnico, equipe);
        equipe.adicionarAtleta(atleta);
        enviarNotificacao(
                atleta,
                "Convocação de Equipe",
                "Você foi convocado pelo técnico " + tecnico.getNome() + " para a equipe " + equipe.getNome() + "."
        );
    }

    public void escalarAtletaParaPartida(Tecnico tecnico, Equipe equipe, Atleta atleta)
            throws AtletaSuspensoException, DadoInvalidoException {
        verificarTecnicoDaEquipe(tecnico, equipe);
        equipe.escalarAtleta(atleta);
    }

    public Campeonato cadastrarCampeonato(Pessoa solicitante, String nome, Modalidade modalidade, int duracaoPartida)
            throws NaoCoordenadorException, DadoInvalidoException {
        Campeonato campeonato = new Campeonato(solicitante, nome, modalidade, duracaoPartida);
        campeonatos.add(campeonato);
        notificarTodos("Novo Campeonato", "O campeonato '" + nome + "' (" + modalidade + ") foi cadastrado.");
        return campeonato;
    }

    public void inscreverEquipeEmCampeonato(Campeonato campeonato, Equipe equipe)
            throws CampeonatoEncerradoException, DadoInvalidoException {
        campeonato.inscreverEquipe(equipe);
    }

    public void registrarPlacarJogo(Campeonato campeonato, int indiceJogo, int placarA, int placarB)
            throws DadoInvalidoException, JogoFinalizadoException {
        campeonato.registrarResultadoJogo(indiceJogo, placarA, placarB);
    }

    public Reserva solicitarReserva(EspacoEsportivo espaco, Equipe equipe, LocalDateTime inicio, LocalDateTime fim)
            throws DadoInvalidoException, EspacoEmManutencaoException, ReservaConflitanteException {
        for (Reserva r : reservas) {
            if (r.conflitaCom(espaco, inicio, fim)) {
                throw new ReservaConflitanteException(
                        "Já existe uma reserva ativa ou pendente para '" + espaco.getNome() + "' neste horário."
                );
            }
        }
        Reserva reserva = new Reserva(espaco, equipe, inicio, fim);
        reservas.add(reserva);
        return reserva;
    }

    public void aprovarReserva(Pessoa solicitante, Reserva reserva) throws NaoCoordenadorException {
        verificarCoordenador(solicitante);
        reserva.aprovar();
        enviarNotificacao(
                reserva.getEquipe().getTecnico(),
                "Reserva Aprovada",
                "Sua reserva do espaço " + reserva.getEspaco().getNome() + " foi aprovada."
        );
    }

    public void rejeitarReserva(Pessoa solicitante, Reserva reserva) throws NaoCoordenadorException {
        verificarCoordenador(solicitante);
        reserva.rejeitar();
        enviarNotificacao(
                reserva.getEquipe().getTecnico(),
                "Reserva Rejeitada",
                "Sua reserva do espaço " + reserva.getEspaco().getNome() + " foi rejeitada."
        );
    }

    public void enviarNotificacao(Pessoa destinatario, String titulo, String mensagem) {
        if (destinatario == null) {
            return;
        }
        List<Notificacao> lista = notificacoesPorUsuario.get(destinatario);
        if (lista == null) {
            lista = new ArrayList<>();
            notificacoesPorUsuario.put(destinatario, lista);
        }
        lista.add(new Notificacao(titulo, mensagem));
    }

    public void notificarTodos(String titulo, String mensagem) {
        for (Pessoa p : usuarios) {
            enviarNotificacao(p, titulo, mensagem);
        }
    }

    public List<Notificacao> getNotificacoesUsuario(Pessoa usuario) {
        List<Notificacao> lista = notificacoesPorUsuario.get(usuario);
        if (lista == null) {
            return new ArrayList<>();
        }
        return lista;
    }

    public String gerarRelatorioGeral() {
        int totalAtletas = listarAtletas().size();
        int totalEquipes = equipes.size();
        int totalJogos = 0;
        Map<Modalidade, Integer> jogosPorModalidade = new HashMap<>();

        for (Campeonato c : campeonatos) {
            int qtdJogos = c.getTabelaJogos().size();
            totalJogos += qtdJogos;
            int atual = jogosPorModalidade.getOrDefault(c.getModalidade(), 0);
            jogosPorModalidade.put(c.getModalidade(), atual + qtdJogos);
        }

        int reservasAprovadas = 0;
        for (Reserva r : reservas) {
            if ("APROVADA".equals(r.getStatus())) {
                reservasAprovadas++;
            }
        }

        double taxaOcupacao = 0.0;
        if (!espacos.isEmpty()) {
            taxaOcupacao = ((double) reservasAprovadas / espacos.size()) * 100.0;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append("       RELATORIOS E ESTATISTICAS         \n");
        sb.append("=========================================\n");
        sb.append("Atletas cadastrados: ").append(totalAtletas).append("\n");
        sb.append("Equipes ativas: ").append(totalEquipes).append("\n");
        sb.append("Espacos esportivos cadastrados: ").append(espacos.size()).append("\n");
        sb.append("Reservas aprovadas: ").append(reservasAprovadas).append("\n");
        sb.append(String.format("Taxa de ocupacao dos espacos: %.1f%%\n", taxaOcupacao));
        sb.append("Total de jogos tabelados: ").append(totalJogos).append("\n");
        sb.append("Jogos por modalidade: ").append(jogosPorModalidade).append("\n");
        sb.append("\n--- Ranking Geral de Equipes ---\n");

        List<Equipe> ranking = new ArrayList<>(equipes);
        ranking.sort(Comparator.comparingInt(Equipe::getPontos).reversed().thenComparing(Equipe::getNome));
        for (int i = 0; i < ranking.size(); i++) {
            sb.append(i + 1).append("o ").append(ranking.get(i).getNome())
              .append(" - ").append(ranking.get(i).getPontos()).append(" pts\n");
        }
        return sb.toString();
    }

    public List<Pessoa> getUsuarios() {
        return usuarios;
    }

    public List<EspacoEsportivo> getEspacos() {
        return espacos;
    }

    public List<Equipe> getEquipes() {
        return equipes;
    }

    public List<Campeonato> getCampeonatos() {
        return campeonatos;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    private void verificarCoordenador(Pessoa pessoa) throws NaoCoordenadorException {
        if (!(pessoa instanceof Coordenador)) {
            throw new NaoCoordenadorException("Ação restrita ao Coordenador Esportivo.");
        }
    }

    private void verificarTecnicoDaEquipe(Tecnico tecnico, Equipe equipe) throws DadoInvalidoException {
        if (tecnico == null || equipe == null || !equipe.getTecnico().equals(tecnico)) {
            throw new DadoInvalidoException("Apenas o técnico responsável pode realizar esta ação na equipe.");
        }
    }
}
