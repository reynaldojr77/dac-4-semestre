package Model.dao;
import Model.entity.Matricula;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MatriculaDAO {
    /** Consulta SQL -> objetos do modelo. Não mistura registros do banco e dos CSVs. */
    public List<Matricula> carregarDoBanco() throws SQLException {
        return carregarDoBanco(ConexaoBanco.localizarBanco());
    }

    public List<Matricula> carregarDoBanco(Path banco) throws SQLException {
        String sql = """
                SELECT r.id_origem, r.ano_referencia,
                       m.nome AS municipio, u.nome AS escola,
                       r.matriculas_total, r.matricula_inicial, r.matricula_apos_censo,
                       r.transferidos, r.cancelados, r.falecido, r.abandono,
                       r.aprovados, r.reprovados, r.cursando, r.outras_situacoes
                FROM registro_educacional AS r
                JOIN unidade_escolar AS u ON u.id = r.escola_id
                JOIN municipio AS m ON m.id = u.municipio_id
                ORDER BY r.ano_referencia, m.nome, u.nome, r.id
                """;
        List<Matricula> registros = new ArrayList<>();
        try (Connection conexao = ConexaoBanco.abrirConexao(banco);
             PreparedStatement consulta = conexao.prepareStatement(sql)) {
            consulta.setQueryTimeout(30);
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    registros.add(new Matricula(
                            numeroObrigatorio(resultado, "id_origem"),
                            numeroObrigatorio(resultado, "ano_referencia"),
                            resultado.getString("municipio"), resultado.getString("escola"),
                            numeroObrigatorio(resultado, "matriculas_total"),
                            numeroObrigatorio(resultado, "matricula_inicial"),
                            numeroObrigatorio(resultado, "matricula_apos_censo"),
                            numeroObrigatorio(resultado, "transferidos"),
                            numeroObrigatorio(resultado, "cancelados"),
                            numeroObrigatorio(resultado, "falecido"),
                            numeroObrigatorio(resultado, "abandono"),
                            numeroObrigatorio(resultado, "aprovados"),
                            numeroObrigatorio(resultado, "reprovados"),
                            numeroObrigatorio(resultado, "cursando"),
                            numeroObrigatorio(resultado, "outras_situacoes")));
                }
            }
            try (PreparedStatement contagem = conexao.prepareStatement("SELECT COUNT(*) FROM registro_educacional");
                 ResultSet resultado = contagem.executeQuery()) {
                if (resultado.next() && resultado.getLong(1) != registros.size()) {
                    throw new SQLException("Há registros sem escola ou município correspondente. "
                            + "A carga foi interrompida para evitar apresentar totais incompletos.");
                }
            }
        }
        return registros;
    }

    private int numeroObrigatorio(ResultSet resultado, String coluna) throws SQLException {
        Object valor = resultado.getObject(coluna);
        if (!(valor instanceof Number numero) || numero.doubleValue() != numero.longValue()
                || numero.longValue() < 0 || numero.longValue() > Integer.MAX_VALUE) {
            throw new SQLException("Valor ausente ou inválido na coluna " + coluna
                    + ". A carga foi interrompida; o valor não foi convertido em zero.");
        }
        return numero.intValue();
    }

    /**
     * Lê um arquivo CSV de matrículas e retorna a lista de objetos Matricula.
     * @param caminhoArquivo Caminho relativo ou absoluto do arquivo CSV.
     * @return Lista com os registros lidos.
     */


    /**
     * Lê múltiplos arquivos CSV (ex: de 2018 até 2026) e agrupa todos os registros em uma única lista.
     * @param caminhosArquivos Lista com os caminhos dos arquivos CSV.
     * @return Lista unificada de matrículas.
     */

    public List<Matricula> carregarPorArquivo(String caminhoArquivo) {
        List<Matricula> listaMatriculas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            boolean ehCabecalho = true;
            int numLinha = 0;

            while ((linha = reader.readLine()) != null) {
                numLinha++;

                if (ehCabecalho) {
                    ehCabecalho = false;
                    continue;
                }

                if (linha.trim().isEmpty()) {
                    continue;
                }

                // Utiliza expressão regular para separar por vírgulas fora de aspas
                String[] campos = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (campos.length >= 15) {
                    try {
                        int id = Integer.parseInt(limparCampo(campos[0]));
                        int ano = Integer.parseInt(limparCampo(campos[1]));
                        String municipio = limparCampo(campos[2]);
                        String escola = limparCampo(campos[3]);
                        int total = Integer.parseInt(limparCampo(campos[4]));
                        int inicial = Integer.parseInt(limparCampo(campos[5]));
                        int aposCenso = Integer.parseInt(limparCampo(campos[6]));
                        int transferidos = Integer.parseInt(limparCampo(campos[7]));
                        int cancelados = Integer.parseInt(limparCampo(campos[8]));
                        int falecido = Integer.parseInt(limparCampo(campos[9]));
                        int abandono = Integer.parseInt(limparCampo(campos[10]));
                        int aprovados = Integer.parseInt(limparCampo(campos[11]));
                        int reprovados = Integer.parseInt(limparCampo(campos[12]));
                        int cursando = Integer.parseInt(limparCampo(campos[13]));
                        int outras = Integer.parseInt(limparCampo(campos[14]));

                        Matricula m = new Matricula(id, ano, municipio, escola, total, inicial,
                                aposCenso, transferidos, cancelados, falecido,
                                abandono, aprovados, reprovados, cursando, outras);
                        listaMatriculas.add(m);

                    } catch (NumberFormatException e) {
                        System.err.println("Erro na conversão de números na linha " + numLinha + ": " + e.getMessage());
                    }
                } else {
                    System.err.println("Linha " + numLinha + " ignorada: tem apenas " + campos.length + " campos de 15 esperados.");
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo CSV (" + caminhoArquivo + "): " + e.getMessage());
        }

        return listaMatriculas;
    }

    // Remove aspas simples/duplas e espaços extras do campo
    private String limparCampo(String campo) {
        if (campo == null) return "";
        return campo.trim().replace("\"", "");
    }

    public List<Matricula> carregarTodosArquivos(List<String> caminhosArquivos) {
        List<Matricula> todasMatriculas = new ArrayList<>();
        for (String caminho : caminhosArquivos) {
            todasMatriculas.addAll(carregarPorArquivo(caminho));
        }
        return todasMatriculas;
    }

}
