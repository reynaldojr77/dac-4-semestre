import Viewer.DashboardView;
import javax.swing.SwingUtilities;
import Controller.IndicadoresController;
import Model.entity.Matricula;
import Viewer.MainView;
import java.util.List;
import java.util.Map;
public class Main {
    public static void main(String[] args){


        IndicadoresController controller = new IndicadoresController();
        MainView view = new MainView();

        // Informe o caminho do seu arquivo CSV (ex: 2026)
        // Certifique-se de que o arquivo esteja na raiz do projeto ou especifique o caminho correto
        String caminhoCSV = "src/matriculas-por-unidade-escolar-2026.csv";

        System.out.println("Carregando dados do arquivo: " + caminhoCSV + "...");

        // 1. Carrega os dados via Controller -> DAO
        List<Matricula> listaMatriculas = controller.carregarDadosDoArquivo(caminhoCSV);

        if (listaMatriculas.isEmpty()) {
            view.exibirMensagemErro("Nenhum dado foi encontrado no arquivo ou o caminho está incorreto.");
            return;
        }

        // 2. Executa os cálculos dos indicadores
        int totalMatriculas = controller.calcularTotalMatriculas(listaMatriculas);
        int totalAprovados = controller.calcularTotalAprovados(listaMatriculas);
        int totalReprovados = controller.calcularTotalReprovados(listaMatriculas);
        int totalAbandono = controller.calcularTotalAbandono(listaMatriculas);
        double taxaAprovacao = controller.calcularTaxaAprovacaoPercentual(listaMatriculas);
        double taxaAbandono = controller.calcularTaxaAbandonoPercentual(listaMatriculas);
        Map<String, Integer> porMunicipio = controller.somarMatriculasPorMunicipio(listaMatriculas);

        // 3. Exibe os resultados na View
        view.exibirDashboard(totalMatriculas, totalAprovados, totalReprovados, totalAbandono, taxaAprovacao, taxaAbandono);
        view.exibirMatriculasPorMunicipio(porMunicipio);

        // Cria e configura a interface na linha de execução do Swing.
        SwingUtilities.invokeLater(() -> {

            DashboardView dashboard = new DashboardView();

            // Preenche as opções dos seletores.
            dashboard.preencherFiltros(listaMatriculas);

            // Conecta o botão e atualiza os resultados iniciais.
            dashboard.configurarFiltros(controller, listaMatriculas);

            // Mostra a janela.
            dashboard.setVisible(true);
        });
    }

}

