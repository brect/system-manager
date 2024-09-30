### **O que é PSS (Proportional Set Size)?**
O **PSS (Proportional Set Size)** é uma métrica que representa a quantidade de memória fisicamente usada por um processo, considerando o compartilhamento de memória entre processos. Em outras palavras, ele contabiliza a memória privada do processo e sua parcela proporcional da memória compartilhada com outros processos.

-   **Memória Privada**: Memória que é exclusiva do processo.
-   **Memória Compartilhada**: Memória que pode ser usada por múltiplos processos (por exemplo, bibliotecas compartilhadas).

O PSS é uma medida mais precisa do uso real de memória, pois distribui o custo da memória compartilhada proporcionalmente entre os processos que a utilizam.

#### 1. APP_TOTAL_PSS:
- **Descrição**: Representa o total de memória PSS usada pelo aplicativo, em kilobytes.
- **Significado**: É a soma de todas as áreas de memória (Dalvik, Native e Other) utilizadas pelo aplicativo.
- **Importância**: Fornece uma visão geral do uso de memória do aplicativo. Um valor alto pode indicar problemas de gerenciamento de memória.

#### 2. APP_DALVIK_PSS:
- **Descrição**: Memória PSS utilizada pela máquina virtual Dalvik (heap Java), em kilobytes.
- **Significado**: Inclui objetos alocados no heap Java, como instâncias de classes, arrays, etc.
- **Importância**: Um valor elevado pode indicar vazamentos de memória ou alocações excessivas no código Java/Kotlin.

**Valores Elevados em `APP_DALVIK_PSS`**:

**Possíveis Causas:**
- Alocações excessivas de objetos no código Java/Kotlin.
- Vazamentos de
  memória devido a referências não liberadas.

**Soluções:**
- Revisar o código para otimizar alocações.
- Utilizar ferramentas como o Android Profiler para identificar vazamentos.
- Implementar práticas recomendadas de gerenciamento de memória, como evitar referências estáticas desnecessárias.


#### 3. APP_NATIVE_PSS:
- **Descrição**: Memória PSS utilizada pelo código nativo (C/C++), em kilobytes.
- **Significado**: Inclui memória alocada por bibliotecas nativas ou código JNI.
- **Importância**: Valores altos podem sugerir vazamentos de memória no código nativo ou uso intensivo de bibliotecas nativas.


**Valores Elevados em `APP_NATIVE_PSS`**:

**Possíveis Causas:**
- Vazamentos de memória em código nativo (C/C++).
-  Uso intenso de
   bibliotecas nativas sem liberação adequada de recursos.

**Soluções:**
- Revisar o código nativo para garantir que todos os recursos sejam
  liberados.
- Utilizar ferramentas como o Valgrind para detectar vazamentos em código nativo.


#### 4. APP_OTHER_PSS:

- Descrição: Memória PSS utilizada por outras áreas não categorizadas como Dalvik ou Native, em kilobytes.

- Significado: Pode incluir memória usada por gráficos, armazenamento de código, pilhas de threads, e outras áreas.

- Importância: Ajuda a identificar uso de memória que não está diretamente relacionado ao código Java ou nativo.

**Valores Elevados em `APP_OTHER_PSS`**:

**Possíveis Causas:**
- Uso de gráficos pesados (imagens de alta resolução, texturas).
- Pilhas de threads não utilizadas.
- Código armazenado em cache que não é liberado.

**Soluções:**
- Otimizar recursos gráficos, como reduzir o tamanho e a resolução de imagens.
- Gerenciar threads adequadamente, encerrando-as quando não forem mais necessárias.
- Limpar caches e liberar código não utilizado.

## O que analisar?

- Monitoramento de Desempenho: Entender como a memória é utilizada pelo aplicativo permite otimizar o desempenho e evitar problemas como lentidão ou travamentos.

- Identificação de Vazamentos de Memória: Valores anormalmente altos em qualquer uma dessas categorias podem indicar vazamentos de memória que precisam ser corrigidos.

- Gerenciamento Eficiente de Recursos: Auxilia no planejamento e implementação de estratégias para liberar memória quando não é mais necessária.

### **Ferramentas Úteis**
-   **Android Profiler**: Permite monitorar o uso de CPU, memória, rede e energia do aplicativo em tempo real.
-   **LeakCanary**: Biblioteca para detecção de vazamentos de memória no heap Java.
-   **Valgrind**: Ferramenta para detectar vazamentos de memória em código nativo.