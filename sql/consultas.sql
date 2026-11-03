-- Consultas somente de leitura para conferência no SQLite.
SELECT ano_referencia, COUNT(*) AS registros, SUM(matriculas_total) AS matriculas,
       SUM(aprovados) AS aprovados, SUM(reprovados) AS reprovados, SUM(abandono) AS abandono
FROM registro_educacional
GROUP BY ano_referencia ORDER BY ano_referencia;

SELECT r.ano_referencia, m.nome AS municipio, u.nome AS escola, r.matriculas_total
FROM registro_educacional r
JOIN unidade_escolar u ON u.id = r.escola_id
JOIN municipio m ON m.id = u.municipio_id
WHERE r.ano_referencia = 2026
ORDER BY r.matriculas_total DESC, m.nome, u.nome;
