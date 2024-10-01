
## Informações sobre Bateria

A classe `DeviceBatteryManager` é responsável por fornecer informações detalhadas sobre a bateria do dispositivo Android. Ela coleta diversos dados, como nível da bateria, estado de saúde, temperatura, voltagem, tecnologia, entre outros. Além disso, calcula um "score" da bateria com base em determinados parâmetros.

#### 1. BATTERY_HEALTH:
-   **Descrição**: Obtém o estado de saúde da bateria.
-   **Retorno**: `String` indicando a saúde da bateria (boa, superaquecimento, descarregada, sobretensão, falha, fria e desconhecida).

#### 2. BATTERY_LEVEL:
-   **Descrição**: Obtém o nível atual da bateria em porcentagem.
-   **Retorno**: `Int` representando o nível da bateria (0 a 100).

#### 3. BATTERY_STATUS:
-   **Descrição**: Obtém o status atual da bateria (carregando, descarregando, cheia, não está carregando e desconhecido).
-   **Retorno**: `String` indicando o status da bateria.

#### 4. POWER_SOURCE:
-   **Descrição**: Identifica a fonte de energia atual do dispositivo (e.g., bateria, USB, AC).
-   **Retorno**: `String` indicando a fonte de energia.

#### 5. BATTERY_TECHNOLOGY:
-   **Descrição**: Obtém a tecnologia da bateria (e.g., Li-ion).
-   **Retorno**: `String` com o nome da tecnologia ou "Desconhecido" se não disponível.

#### 6. BATTERY_TEMPERATURE:
-   **Descrição**: Obtém a temperatura atual da bateria em graus Celsius.
-   **Retorno**: `Float` representando a temperatura.

#### 7. BATTERY_TEMPERATURE_STATUS:
-   **Descrição**: Avalia o status da temperatura da bateria em categorias (e.g., "Ótima", "Elevada") com base em suas faixas fornecidas.
    - temperature < 30 -> "Ótima"
    - temperature < 35 -> "Boa"
    - temperature < 40 -> "Elevada"
    - else -> "Crítica"
-   **Retorno**: `String` indicando o status da temperatura.


#### 8. BATTERY_TEMPERATURE_STATUS_PERCENT:
-   **Descrição**: Calcula uma pontuação baseada na temperatura da bateria.
    - temperature < 30 -> 100.0
    - temperature < 35 -> 80.0
    - temperature < 40 -> 50.0
    - else -> 20.0
-   **Retorno**: `Double` representando a pontuação (0 a 100).

#### 9. BATTERY_VOLTAGE:
-   **Descrição**: Obtém a voltagem atual da bateria em milivolts.
-   **Retorno**: `Int` representando a voltagem.

#### 10. BATTERY_CAPACITY:
-   **Descrição**: Tenta obter a capacidade total da bateria em miliampere-hora (mAh).
-   **Retorno**: `Double` com a capacidade da bateria ou `0.0` em caso de falha.


#### 11. BATTERY_SCORE:
-   **Descrição**: Calcula uma pontuação geral da bateria com base no nível e na saúde.
    -   Combina o nível da bateria (70% de peso) e a pontuação da saúde (30% de peso).
    -  	 Garante que o score esteja entre 0 e 100.
-   **Retorno**: `Double` representando a pontuação (0 a 100).
