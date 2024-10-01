
## Informações sobre GPU

A classe `GpuInfoManager` é projetada para obter informações sobre a GPU (Unidade de Processamento Gráfico) em um dispositivo Android. Ela fornece métodos para recuperar detalhes como o fornecedor da GPU, o renderizador, a carga da GPU e o governador de escala de CPU. Além disso, a classe pode calcular uma pontuação para a GPU com base no modelo do renderizador.

#### 1. GPU_VENDOR:
**Descrição**: Obtém o nome do fornecedor da GPU. (por exemplo, "Qualcomm").
-   Inicializa o contexto OpenGL ES chamando `initializeOpenGL()`.
-   Usa `GLES20.glGetString(GLES20.GL_VENDOR)` para obter o nome do fornecedor da GPU.
-   Limpa o contexto OpenGL com `cleanupOpenGL()`.
-   Retorna o nome do fornecedor ou uma mensagem de erro se a inicialização falhar.

#### 2. GPU_RENDERER:
**Descrição**: Obtém o modelo ou renderizador específico da GPU (por exemplo, "Adreno 660"). Semelhante ao `getGpuVendor()`, mas usa `GLES20.GL_RENDERER` para obter o modelo específico da GPU.

#### 3. GPU_LOAD:
**Descrição**: Obtém a carga atual da GPU (porcentagem de uso)..
-   A obtenção da carga da GPU não é possível de forma genérica sem acesso root ou APIs específicas do fabricante.
-   O método retorna "Não disponível" para indicar que a informação não está acessível, porque obter a carga da GPU geralmente requer acesso root ou APIs específicas do fabricante..

#### 4. SCALING_GOVERNOR:
**Descrição**: Obtém o governador de escala de frequência da CPU atual.
-   Tenta ler o arquivo `/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor` para obter o governador atual.
-   Retorna o nome do governador, "Não disponível" se o arquivo não existir ou "Erro ao obter" em caso de exceção.

### Informações adicionais
O método `initializeOpenGL` inicializa o contexto OpenGL ES necessário para chamadas a funções OpenGL, onde ele:
-   Estabelece uma conexão com o display EGL padrão.
-   Inicializa o display EGL.
-   Escolhe uma configuração EGL adequada.
-   Cria um contexto OpenGL ES 2.0.
-   Cria um surface de buffer de pixel (Pbuffer).
-   Faz o contexto EGL atual para permitir chamadas OpenGL

O método `cleanupOpenGL` limpa e libera os recursos associados ao contexto OpenGL, onde ele:
-   Desfaz as operações realizadas durante a inicialização.
-   Desassocia o contexto atual.
-   Destroi o surface e o contexto EGL.
-   Encerra a conexão com o display EGL.

Os métodos `initializeOpenGL()` e `cleanupOpenGL()` são cruciais para:
-   Evitar vazamentos de memória.
-   Garantir que os recursos do sistema sejam liberados corretamente.