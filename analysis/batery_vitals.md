## Motivadores

### 1. Importância da Temperatura da Bateria
A temperatura da bateria é um indicador importante do estado geral do dispositivo. Temperaturas elevadas podem afetar negativamente o desempenho do dispositivo, levando a:

- Redução do Desempenho do Processador: Para evitar superaquecimento, o sistema pode reduzir a velocidade do processador (throttling).
- Diminuição da Vida Útil da Bateria: Altas temperaturas aceleram a degradação química da bateria.
- Desligamentos Inesperados: O sistema pode forçar o desligamento para evitar danos.
- Segurança: Em casos extremos, pode haver risco de incêndio ou explosão.

Portanto, monitorar a temperatura é essencial para manter o desempenho e a segurança do dispositivo.

### 2.Limites Aceitáveis de Temperatura
As baterias de íon-lítio, comumente usadas em dispositivos móveis, têm uma faixa de temperatura operacional segura.

- Temperatura Operacional Normal: Entre 0°C e 35°C.
- Temperatura Ideal: Entre 20°C e 25°C.
- Temperatura Alta: Acima de 35°C começa a ser preocupante.
- Temperatura Crítica: Acima de 45°C pode causar danos permanentes.

Observação: Esses valores podem variar ligeiramente dependendo do fabricante e do modelo do dispositivo.

Os valores de temperatura acima são diretrizes gerais baseadas em práticas comuns e na faixa típica de operação segura para baterias de dispositivos móveis.

Os valores são adequados como um ponto de partida para muitos dispositivos e cenários. No entanto, não são universalmente aplicáveis a todos os celulares Android. Alguns dispositivos podem operar de forma segura em temperaturas ligeiramente superiores, enquanto outros podem apresentar problemas mesmo em temperaturas mais baixas.

### 3. Avaliando a Temperatura da Bateria
Podemos criar uma função que avalia a temperatura atual da bateria e retorna um score ou uma classificação indicando se está dentro dos limites aceitáveis.

Sugestão de Classificação:
- Temperatura < 30°C: Ótima
- 30°C ≤ Temperatura < 35°C: Boa
- 35°C ≤ Temperatura < 40°C: Elevada
- Temperatura ≥ 40°C: Crítica