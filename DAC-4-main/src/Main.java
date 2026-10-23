import Viewer.DashboardView;
import javax.swing.SwingUtilities;
import Controller.IndicadoresController;
import Model.entity.Matricula;
import Viewer.MainView;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        IndicadoresController controller = new IndicadoresController();
        MainView view = new MainView();

        List<String> caminhosCSV = listarArquivosCsvPadrao();

        if (caminhosCSV.isEmpty()) {
            view.exibirMensagemErro(
                    "Nenhum CSV encontrado na pasta src. "
                            + "Confira se o diretório de trabalho é a pasta DAC-4-main."
            );
            return;
        }

        System.out.println("Carregando dados dos arquivos: " + caminhosCSV + "...");

        List<Matricula> listaMatriculas = controller.carregarTodosOsDados(caminhosCSV);

        if (listaMatriculas.isEmpty()) {
            view.exibirMensagemErro("Nenhum dado foi encontrado nos arquivos ou o caminho está incorreto.");
            return;
        }

        int totalMatriculas = controller.calcularTotalMatriculas(listaMatriculas);
        int totalAprovados = controller.calcularTotalAprovados(listaMatriculas);
        int totalReprovados = controller.calcularTotalReprovados(listaMatriculas);
        int totalAbandono = controller.calcularTotalAbandono(listaMatriculas);

        double taxaAprovacao = controller.calcularTaxaAprovacaoPercentual(listaMatriculas);
        double taxaReprovacao = controller.calcularTaxaReprovacaoPercentual(listaMatriculas);
        double taxaAbandono = controller.calcularTaxaAbandonoPercentual(listaMatriculas);

        Map<String, Integer> porMunicipio = controller.somarMatriculasPorMunicipio(listaMatriculas);

        view.exibirDashboard(
                totalMatriculas,
                totalAprovados,
                totalReprovados,
                totalAbandono,
                taxaAprovacao,
                taxaReprovacao,
                taxaAbandono
        );

        view.exibirMatriculasPorMunicipio(porMunicipio);

        SwingUtilities.invokeLater(() -> {
            DashboardView dashboard = new DashboardView();
            dashboard.preencherFiltros(listaMatriculas);
            dashboard.configurarFiltros(controller, listaMatriculas);
            dashboard.setVisible(true);
        });
    }

    private static List<String> listarArquivosCsvPadrao() {
        List<String> caminhos = new ArrayList<>();
        File pasta = new File("src");

        if (!pasta.exists() || !pasta.isDirectory()) {
            return caminhos;
        }

        File[] arquivosCsv = pasta.listFiles((diretorio, nome) -> nome.toLowerCase().endsWith(".csv"));
        if (arquivosCsv == null) {
            return caminhos;
        }

        Arrays.sort(arquivosCsv, Comparator.comparing(File::getName));

        for (File arquivo : arquivosCsv) {
            caminhos.add(arquivo.getAbsolutePath());
        }

        return caminhos;
    }
}
