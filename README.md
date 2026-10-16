# DAC — 4º semestre

Projeto acadêmico em Java para análise de matrículas escolares de Mato Grosso do Sul, desenvolvido por Reynaldo, João e Pedro.

## Versão atual — interface e gráficos

Atualização de 16/10/2026 a partir de `DAC 4 + DashboardView.zip`. A base inicial do João, recebida em `DAC 4.rar`, permanece no histórico do Git.

- DashboardView em Swing com cartões de matrículas, aprovados, reprovados e abandono.
- Seletores de município e ano preenchidos a partir dos dados.
- Botão Aplicar filtros atualizando indicadores, tabela e gráfico com o mesmo recorte.
- Tabela de consulta por unidade escolar e ano, com rolagem e edição desativada.
- Gráfico de até dez municípios com mais matrículas; ao selecionar uma cidade, mostra seu total.
- Tratamento de recortes sem registros e aviso de dados parciais de 2026.
- Saída original do resumo no console.

O Main ainda carrega somente o CSV de **2026**. Carga histórica, persistência SQL e visualizações históricas de aprovação e abandono continuam pendentes. Os percentuais do controller são proporções sobre matrículas totais; sua metodologia deve ser confirmada pela equipe.

## Organização

| Caminho | Conteúdo |
| --- | --- |
| `src/Main.java` | Carga e inicialização da interface |
| `src/Controller/` | Filtros, cálculos e coordenação da leitura |
| `src/Model/` | Modelos e leitura CSV |
| `src/Viewer/DashboardView.java` | Interface, indicadores, tabela e gráfico |
| `src/Viewer/MainView.java` | Apresentação no console |
| `src/matriculas-por-unidade-escolar-2026.csv` | CSV utilizado pelo programa |
| `pom.xml` | JFreeChart 1.5.6 e configuração de compilação |
| `sql/` | Espaço para scripts do banco |
| `docs/PLANO.md` | Divisão de trabalho e pendências |

## Preparação

Use um **JDK 17 ou superior**. A compilação dos fontes desta atualização foi conferida com Java 17 e o JAR 1.5.6 do ZIP. Reynaldo confirmou a execução da interface no IntelliJ IDEA e no VS Code.

O `pom.xml` declara `org.jfree:jfreechart:1.5.6` e mantém os fontes em `src`, sem reorganizar os pacotes. Na primeira importação, o Maven precisa de internet para obter a biblioteca. O JAR local do ZIP não precisa ser versionado.

Abra a raiz que contém diretamente `src` e `pom.xml`. O diretório de execução deve ser essa mesma raiz: o programa usa o caminho relativo `src/matriculas-por-unidade-escolar-2026.csv`.

## IntelliJ IDEA

1. Clone o repositório ou extraia seu ZIP.
2. Abra `pom.xml` como projeto, ou aceite carregar o projeto Maven ao abrir a raiz.
3. Configure o JDK e aguarde a resolução das dependências.
4. Execute o método main de `src/Main.java`.
5. Mantenha o Working directory na raiz do repositório.

## VS Code

1. Instale o **Extension Pack for Java**, da Microsoft, e configure um JDK.
2. Use File → Open Folder para abrir a raiz que contém `pom.xml` e `src`.
3. Aguarde a importação do projeto Maven e o download das dependências.
4. Abra `src/Main.java` e clique em Run acima do método main.
5. Execute a partir da raiz para localizar o CSV.

## Compilação pelo terminal

Com JDK e Maven instalados, execute na raiz:

```sh
mvn compile
```

## Conferência rápida

Com o CSV recebido, confira **199.489** matrículas no total, **46.121** para Campo Grande e **6.137** para Corumba. Ao voltar para todos os municípios, o total geral deve reaparecer sem duplicar registros.

Aprovados, reprovados e abandono estão zerados no arquivo de 2026. São dados parciais: os zeros não representam resultados anuais consolidados. O gráfico compara volume de matrículas, não qualidade do ensino.

Fonte oficial: https://www.dados.ms.gov.br/dataset/matriculas-por-unidade-escolar

## Trabalho em equipe

Use uma branch por tarefa e pull requests para revisar a integração. Consulte [docs/PLANO.md](docs/PLANO.md). Preferências locais das IDEs e arquivos compilados permanecem fora do versionamento.
