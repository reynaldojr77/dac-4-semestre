# DAC — 4º semestre

Projeto acadêmico em Java para leitura e análise de matrículas escolares, desenvolvido por Reynaldo, João e Pedro no IntelliJ IDEA.

## Versão inicial

O código e o CSV desta versão foram extraídos de `DAC 4.rar`, enviado em 14/10/2026. Os arquivos em `src/` foram preservados sem alteração. Arquivos compilados e preferências locais do IntelliJ foram excluídos da versão compartilhada.

A aplicação atual lê o CSV de 2026, calcula totais de matrículas, aprovados, reprovados e abandonos, calcula percentuais de aprovação e abandono sobre o total de matrículas e exibe resultados no console, incluindo matrículas por município.

Existe suporte no código para carregar vários arquivos, mas somente o CSV de 2026 foi recebido. Interface gráfica e integração SQL ainda não constam desta versão.

## Organização

| Caminho | Conteúdo |
| --- | --- |
| `src/Main.java` | Inicialização da aplicação |
| `src/Controller/` | Cálculos e coordenação da leitura |
| `src/Model/entity/` | Modelos de dados |
| `src/Model/dao/` | Leitura de arquivos CSV |
| `src/Viewer/` | Exibição atual no console |
| `src/matriculas-por-unidade-escolar-2026.csv` | Dados recebidos |
| `sql/` | Espaço para os scripts do banco |
| `docs/PLANO.md` | Divisão de trabalho e pendências |

## Abrir no IntelliJ IDEA

1. Clone este repositório pelo IntelliJ em **Get from Version Control**, ou baixe e descompacte o ZIP.
2. Abra a pasta do projeto e configure um **JDK 23**, versão indicada na configuração do projeto original.
3. Se o IntelliJ não reconhecer `src`, clique com o botão direito nessa pasta e escolha **Mark Directory as → Sources Root**.
4. Execute o método `main` de `src/Main.java`.
5. Na configuração de execução, mantenha o **Working directory** na raiz do repositório. O programa busca o CSV em `src/matriculas-por-unidade-escolar-2026.csv`.

Esta organização inicial foi conferida por comparação dos arquivos com o arquivo original. A compilação e a execução devem ser validadas pela equipe no IntelliJ; não foram executadas nesta preparação.

## Trabalho em equipe

Crie uma branch para cada tarefa, por exemplo `feature/interface`, `feature/historico` ou `feature/banco-sql`. Faça commits com descrições curtas e abra um pull request para revisar e integrar as alterações. Consulte as pendências em [docs/PLANO.md](docs/PLANO.md).
