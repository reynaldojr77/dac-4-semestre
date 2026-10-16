# DAC — 4º semestre

Projeto acadêmico em Java para análise de matrículas escolares de Mato Grosso do Sul, desenvolvido por Reynaldo, João e Pedro.

## Versão atual — interface e gráficos

Atualização de 16/10/2026, com interface gráfica desenvolvida em Swing e gráficos utilizando JFreeChart 1.5.6.

Funcionalidades disponíveis:

- Dashboard com cartões de matrículas, aprovados, reprovados e abandono.
- Seletores de município e ano preenchidos a partir dos dados carregados.
- Botão **Aplicar filtros** atualizando indicadores, tabela e gráfico com o mesmo recorte.
- Tabela de consulta por unidade escolar e ano, com rolagem e edição desativada.
- Gráfico de até dez municípios com mais matrículas; ao selecionar uma cidade, apresenta seu total.
- Tratamento de recortes sem registros e aviso de dados parciais de 2026.
- Saída do resumo dos indicadores no console.

O programa ainda carrega somente o CSV de **2026**. A carga histórica, a persistência SQL e as visualizações históricas de aprovação e abandono continuam pendentes.

Os percentuais calculados pelo controller são proporções sobre o total de matrículas; sua metodologia deve ser confirmada pela equipe.

## Organização

| Caminho | Conteúdo |
| --- | --- |
| `src/Main.java` | Carga dos dados e inicialização da interface |
| `src/Controller/` | Filtros, cálculos e coordenação da leitura |
| `src/Model/entity/` | Modelos de dados |
| `src/Model/dao/` | Leitura dos arquivos CSV |
| `src/Viewer/DashboardView.java` | Interface, indicadores, tabela e gráfico |
| `src/Viewer/MainView.java` | Apresentação no console |
| `src/matriculas-por-unidade-escolar-2026.csv` | CSV utilizado pelo programa |
| `lib/jfreechart-1.5.6.jar` | Biblioteca utilizada para os gráficos |
| `sql/` | Espaço reservado aos scripts do banco |
| `docs/PLANO.md` | Divisão de trabalho e pendências |

## Preparação

Utilize um **JDK 17 ou superior**.

A compilação desta versão foi conferida com Java 17 e JFreeChart 1.5.6. A execução da interface também foi confirmada por Reynaldo no IntelliJ IDEA e no VS Code.

A biblioteca JFreeChart está incluída na pasta `lib` e deve ser adicionada às bibliotecas do projeto caso a IDE não a reconheça automaticamente.

Abra a pasta raiz que contém diretamente `src` e `lib`.

O diretório de execução deve ser essa mesma raiz, pois o programa procura o CSV pelo caminho relativo:

`src/matriculas-por-unidade-escolar-2026.csv`

## Executar no IntelliJ IDEA

1. Clone o repositório ou baixe e extraia seu ZIP.
2. Abra a pasta raiz do projeto, que contém `src` e `lib`.
3. Configure um JDK 17 ou superior em **File → Project Structure → Project**.
4. Caso `src` não esteja reconhecida como pasta de código, clique nela com o botão direito e selecione **Mark Directory as → Sources Root**.
5. Em **File → Project Structure → Modules → Dependencies**, clique em **+ → JARs or Directories** e selecione `lib/jfreechart-1.5.6.jar`, caso ainda não esteja associado.
6. Abra `src/Main.java` e execute o método `main`.
7. Se o CSV não for encontrado, confira em **Run → Edit Configurations** se o **Working directory** aponta para a raiz do projeto.

## Executar no VS Code

1. Instale o **Extension Pack for Java**, da Microsoft, e configure um JDK 17 ou superior.
2. Use **File → Open Folder** para abrir a pasta raiz que contém diretamente `src` e `lib`.
3. Aguarde o suporte a Java terminar de carregar.
4. Confira se o JFreeChart aparece em **Java Projects → Referenced Libraries**. Os arquivos JAR da pasta `lib` normalmente são reconhecidos automaticamente.
5. Se a biblioteca não aparecer, utilize o botão **+** de **Referenced Libraries** e selecione `lib/jfreechart-1.5.6.jar`.
6. Abra `src/Main.java` e clique em **Run**, acima do método `main`.

Execute a partir da raiz do projeto para que o programa encontre o CSV.

## Conferência rápida

Com o CSV de 2026 utilizado nesta versão, confira:

| Município | Total de matrículas |
| --- | ---: |
| Todos os municípios | 199.489 |
| Campo Grande | 46.121 |
| Corumba | 6.137 |

Ao alterar o município e clicar em **Aplicar filtros**, os cartões, a tabela e o gráfico devem acompanhar a seleção.

Ao retornar para **Todos os municípios**, o total geral deve reaparecer. Clicar novamente sem alterar os filtros não deve duplicar linhas nem valores.

Aprovados, reprovados e abandono estão zerados no arquivo de 2026. São dados parciais: esses zeros não devem ser interpretados como resultados anuais consolidados.

O gráfico atual compara o volume de matrículas entre municípios.

Fonte oficial: [Dados Abertos de Mato Grosso do Sul — Matrículas por unidade escolar](https://www.dados.ms.gov.br/dataset/matriculas-por-unidade-escolar)

## Trabalho em equipe

Utilize uma branch por tarefa e pull requests para revisar e integrar as alterações.

Consulte [docs/PLANO.md](docs/PLANO.md) para acompanhar a divisão de trabalho e as pendências.

Preferências locais das IDEs e arquivos compilados permanecem fora do versionamento.
