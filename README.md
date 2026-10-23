# DAC — Indicadores Educacionais de MS

Projeto acadêmico do 4º semestre de Análise e Desenvolvimento de Sistemas, desenvolvido por **Reynaldo, João e Pedro**.

Aplicação em Java para consultar e visualizar dados de matrículas, aprovação, reprovação e abandono escolar de Mato Grosso do Sul.

## Funcionalidades disponíveis

- Carregamento dos arquivos CSV de **2024 e 2026**.
- Interface gráfica desenvolvida com **Java Swing**.
- Filtros por **município e ano**.
- Cartões com totais de matrículas, aprovados, reprovados e abandono.
- Indicadores percentuais de aprovação, reprovação e abandono.
- Tabela de consulta por unidade escolar.
- Gráfico de até dez municípios com mais matrículas no ano selecionado.
- Gráfico de taxas gerais de aprovação, reprovação e abandono.
- Atualização dos indicadores, da tabela e dos gráficos pelo botão **Aplicar filtros**.
- Tratamento de consultas sem registros.
- Resumo dos dados carregados no console.

Os gráficos utilizam a biblioteca **JFreeChart 1.5.6**, incluída no repositório.

## Organização do repositório

| Caminho | Conteúdo |
| --- | --- |
| `DAC-4-main/` | Projeto Java |
| `DAC-4-main/src/Main.java` | Carregamento dos dados e inicialização da aplicação |
| `DAC-4-main/src/Controller/` | Filtros, cálculos e coordenação da leitura |
| `DAC-4-main/src/Model/entity/` | Modelos de dados |
| `DAC-4-main/src/Model/dao/` | Leitura dos arquivos CSV |
| `DAC-4-main/src/Viewer/DashboardView.java` | Interface gráfica, indicadores, tabela e gráficos |
| `DAC-4-main/src/Viewer/MainView.java` | Apresentação dos resultados no console |
| `DAC-4-main/src/matriculas-por-unidade-escolar-2024.csv` | Dados de 2024 |
| `DAC-4-main/src/matriculas-por-unidade-escolar-2026.csv` | Dados de 2026 |
| `DAC-4-main/lib/jfreechart-1.5.6.jar` | Biblioteca dos gráficos |
| `docs/PLANO.md` | Planejamento inicial da equipe |
| `sql/` | Pasta reservada à documentação e aos scripts do banco de dados |

O arquivo `docs/PLANO.md` registra o planejamento inicial e precisa ser revisado conforme o avanço das entregas. A descrição das funcionalidades disponíveis nesta versão está neste README.

## Requisitos e preparação

- **JDK 17 ou superior**.
- IntelliJ IDEA ou VS Code com suporte a Java.
- Biblioteca JFreeChart incluída em `DAC-4-main/lib/`.

Depois de clonar ou baixar o repositório, abra na IDE a pasta **`DAC-4-main`**, que contém diretamente as pastas `src` e `lib`.

**O diretório de trabalho da execução também deve ser `DAC-4-main`.**

O programa procura automaticamente os arquivos com extensão `.csv` diretamente na pasta `src`, usando um caminho relativo ao diretório de trabalho.

## Executar no IntelliJ IDEA

1. Clone o repositório ou baixe e extraia seu ZIP.
2. No IntelliJ, abra a pasta **`DAC-4-main`**.
3. Configure um JDK 17 ou superior em **File → Project Structure → Project**.
4. Se necessário, clique com o botão direito em `src` e selecione **Mark Directory as → Sources Root**.
5. Em **File → Project Structure → Modules → Dependencies**, adicione `lib/jfreechart-1.5.6.jar` por **+ → JARs or Directories**, caso a biblioteca ainda não esteja associada.
6. Abra `src/Main.java` e execute o método `main`.

Se aparecer a mensagem de que nenhum CSV foi encontrado, confira em **Run → Edit Configurations** se o campo **Working directory** aponta para a pasta `DAC-4-main`.

## Executar no VS Code

1. Instale o **Extension Pack for Java**, da Microsoft, e configure um JDK 17 ou superior.
2. Use **File → Open Folder** para abrir a pasta **`DAC-4-main`**.
3. Aguarde o suporte a Java terminar de carregar.
4. Confira se o JFreeChart aparece em **Java Projects → Referenced Libraries**.
5. Se necessário, use o botão **+** de **Referenced Libraries** para adicionar `lib/jfreechart-1.5.6.jar`.
6. Abra `src/Main.java` e clique em **Run**, acima do método `main`.

Mantenha `DAC-4-main` como diretório de trabalho para que os arquivos CSV sejam encontrados.

## Dados e interpretação dos indicadores

A versão atual inclui arquivos de **2024 e 2026**, obtidos no Portal de Dados Abertos de Mato Grosso do Sul.

Fonte: [Matrículas por unidade escolar — Dados Abertos de MS](https://www.dados.ms.gov.br/dataset/matriculas-por-unidade-escolar)

O dashboard apresenta os resultados do **ano e município selecionados**. O resumo inicial do console considera o conjunto de arquivos carregados e pode apresentar totais diferentes dos exibidos na interface.

### Dados de 2026

No CSV de 2026 incluído nesta versão, os campos de aprovados, reprovados e abandono contêm valores iguais a zero.

Por isso, os respectivos totais e percentuais também aparecem zerados na aplicação, e as barras do gráfico de taxas podem não ficar visíveis.

Esses zeros fazem parte do arquivo de origem. Como os dados de 2026 são parciais, não devem ser interpretados como resultados anuais consolidados.

### Cálculo dos percentuais

O código calcula cada percentual dividindo o total do indicador pelo total de matrículas do recorte selecionado e multiplicando por 100.

Exemplo:

`Percentual de aprovação = (total de aprovados / total de matrículas) × 100`

A mesma lógica é utilizada para reprovação e abandono. Quando o total de matrículas é zero, o cálculo retorna zero.

A metodologia deve ser conferida pela equipe antes de apresentar esses percentuais como taxas oficiais de rendimento escolar.

## Conferência rápida

Selecione o ano **2026**, escolha o município indicado e clique em **Aplicar filtros**:

| Ano | Município | Total esperado de matrículas |
| --- | --- | ---: |
| 2026 | Todos os municípios | 199.489 |
| 2026 | Campo Grande | 46.121 |

Confira também:

- Ao mudar os filtros, os cartões, a tabela e os gráficos devem acompanhar a seleção.
- Ao aplicar novamente os mesmos filtros, as linhas e os valores não devem ser duplicados.
- As abas **Tabela**, **Gráfico por município** e **Taxas gerais** devem estar disponíveis.
- Em 2024, os indicadores de aprovação, reprovação e abandono devem refletir os valores presentes no CSV.
- Em 2026, esses indicadores permanecem zerados conforme o arquivo de origem.

## Banco de dados e próximas entregas

Nesta versão do repositório, a aplicação lê os dados diretamente dos arquivos CSV. A integração Java com o banco de dados ainda está pendente.

Próximas entregas:

- Incluir os scripts e as instruções do banco na pasta `sql/`.
- Integrar as consultas do banco ao código Java.
- Ampliar o histórico com os demais anos disponíveis.
- Conferir a consistência dos dados e a metodologia dos indicadores.
- Atualizar o planejamento e a documentação conforme as entregas da equipe.

## Equipe e colaboração

| Integrante | Frente principal |
| --- | --- |
| Reynaldo | Interface, filtros, indicadores e gráficos |
| João | Código Java, consultas e integração |
| Pedro | Estrutura do banco, importação e consultas SQL |

Utilize uma branch por tarefa e abra pull requests para revisar e integrar as alterações à `main`.

Preferências locais das IDEs e arquivos gerados pela compilação devem permanecer fora do versionamento, conforme o `.gitignore`.
