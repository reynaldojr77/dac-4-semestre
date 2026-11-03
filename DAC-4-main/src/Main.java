import Controller.IndicadoresController;
import Model.dao.ConexaoBanco;
import Model.entity.Matricula;
import Viewer.DashboardView;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        IndicadoresController controller = new IndicadoresController();
        try {
            System.out.println("Origem: SQLite — " + ConexaoBanco.localizarBanco());
            List<Matricula> registros = controller.carregarDadosDoBanco();
            if (registros.isEmpty()) throw new SQLException("O banco não contém registros educacionais.");
            List<Integer> anos = registros.stream().map(Matricula::getAnoReferencia).distinct().sorted().toList();
            System.out.println("Registros carregados: " + registros.size());
            System.out.println("Anos disponíveis: " + anos);
            System.out.println("Ano | Registros | Matrículas | Aprovados | Reprovados | Abandono");
            for (int ano : anos) {
                var resumo = controller.resumirAno(registros, "Todos os municípios", ano);
                System.out.printf("%d | %d | %d | %d | %d | %d%n", ano, resumo.quantidadeRegistros(),
                        resumo.matriculas(), resumo.aprovados(), resumo.reprovados(), resumo.abandono());
            }
            if (Arrays.asList(args).contains("--verificar-banco")) {
                System.out.println("Consulta ao SQLite concluída. Nenhum CSV foi carregado.");
                return;
            }
            if (GraphicsEnvironment.isHeadless()) {
                throw new IllegalStateException("Interface gráfica indisponível. Use --verificar-banco para consultar pelo terminal.");
            }
            SwingUtilities.invokeLater(() -> {
                DashboardView dashboard = new DashboardView();
                dashboard.preencherFiltros(registros);
                dashboard.configurarFiltros(controller, registros);
                dashboard.setVisible(true);
            });
        } catch (SQLException | RuntimeException erro) {
            String mensagem = "Não foi possível iniciar o sistema.\n" + erro.getMessage();
            System.err.println(mensagem);
            if (!GraphicsEnvironment.isHeadless()) {
                JOptionPane.showMessageDialog(null, mensagem, "DAC — Falha na carga do banco", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(1);
        }
    }
}
