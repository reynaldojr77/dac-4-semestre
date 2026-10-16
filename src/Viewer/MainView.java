package Viewer;
import Model.entity.Matricula;
import java.util.List;
import java.util.Map;
public class MainView {
    /**
     * Exibe o resumo do dashboard com os principais indicadores calculados.
     */
    public void exibirDashboard(int totalMatriculas, int totalAprovados, int totalReprovados,
                                int totalAbandono, double taxaAprovacao, double taxaAbandono) {
        System.out.println("\n==================================================");
        System.out.println("          DASHBOARD DE MATRÍCULAS ESCOLARES       ");
        System.out.println("==================================================");
        System.out.printf(" Total de Matrículas : %,d%n", totalMatriculas);
        System.out.printf(" Total de Aprovados  : %,d (%.2f%%)%n", totalAprovados, taxaAprovacao);
        System.out.printf(" Total de Reprovados : %,d%n", totalReprovados);
        System.out.printf(" Total de Abandonos  : %,d (%.2f%%)%n", totalAbandono, taxaAbandono);
        System.out.println("--------------------------------------------------");
    }

    /**
     * Exibe a listagem de matrículas agrupadas por município.
     */
    public void exibirMatriculasPorMunicipio(Map<String, Integer> porMunicipio) {
        System.out.println("\n--- MATRÍCULAS POR MUNICÍPIO ---");
        porMunicipio.forEach((municipio, total) ->
                System.out.printf(" %-30s : %,d matrículas%n", municipio, total)
        );
        System.out.println("--------------------------------------------------");
    }

    public void exibirMensagemErro(String mensagem) {
        System.err.println("[ERRO] " + mensagem);
    }
}
