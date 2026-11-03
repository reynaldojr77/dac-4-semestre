package Model.dao;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Set;

/** Localiza o arquivo distribuído com o projeto e abre somente para consulta. */
public final class ConexaoBanco {
    private ConexaoBanco() { }

    public static Path localizarBanco() throws SQLException {
        String configurado = System.getProperty("dac.banco");
        if (configurado == null || configurado.isBlank()) configurado = System.getenv("DAC_BANCO");
        if (configurado != null && !configurado.isBlank()) {
            return validarArquivo(Path.of(configurado));
        }

        Set<Path> origens = new LinkedHashSet<>();
        try {
            Path codigo = Path.of(ConexaoBanco.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            origens.add(Files.isDirectory(codigo) ? codigo : codigo.getParent());
        } catch (Exception ignorada) {
            // O diretório de trabalho continua disponível quando a IDE não fornece CodeSource.
        }
        origens.add(Path.of(System.getProperty("user.dir")));
        for (Path origem : origens) {
            Path pasta = origem.toAbsolutePath().normalize();
            for (int nivel = 0; pasta != null && nivel < 7; nivel++, pasta = pasta.getParent()) {
                for (Path candidato : new Path[]{pasta.resolve("database/dac_ods4.db"),
                        pasta.resolve("DAC-4-main/database/dac_ods4.db")}) {
                    if (Files.isRegularFile(candidato)) return validarArquivo(candidato);
                }
            }
        }
        throw new SQLException("Banco não encontrado. Mantenha database/dac_ods4.db dentro de DAC-4-main. "
                + "Você também pode indicar o caminho com -Ddac.banco=CAMINHO ou DAC_BANCO.");
    }

    private static Path validarArquivo(Path caminho) throws SQLException {
        Path absoluto = caminho.toAbsolutePath().normalize();
        if (!Files.isRegularFile(absoluto) || !Files.isReadable(absoluto)) {
            throw new SQLException("Banco inexistente ou sem permissão de leitura: " + absoluto);
        }
        return absoluto;
    }

    public static Connection abrirConexao(Path caminho) throws SQLException {
        Path banco = validarArquivo(caminho);
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException | LinkageError erro) {
            throw new SQLException("Driver SQLite não carregado. Adicione todos os arquivos JAR de lib "
                    + "às dependências do projeto ou execute executar.bat.", erro);
        }
        // mode=ro impede criar um banco vazio por engano e impede alterar o arquivo existente.
        Connection conexao = DriverManager.getConnection("jdbc:sqlite:" + banco.toUri().toASCIIString() + "?mode=ro");
        try (Statement comando = conexao.createStatement()) {
            comando.execute("PRAGMA foreign_keys = ON");
            comando.execute("PRAGMA query_only = ON");
            return conexao;
        } catch (SQLException erro) {
            conexao.close();
            throw erro;
        }
    }
}
