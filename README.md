# System Manager

## Painel de Monitoramento

### 1. Memória e Armazenamento

#### RAM

- **Memória RAM total do dispositivo.**
- **Memória RAM disponível.**
- **Limite de uso de memória.**
- **Indicador se a memória RAM está baixa.**
- **Total de memória RAM utilizada.**

#### Armazenamento Interno

- **Tipo de armazenamento (eMMC, UFS).**
- **Capacidade de armazenamento interno.**

#### Armazenamento Externo

- **Tipo de armazenamento externo (microSD, USB OTG).**
- **Capacidade de armazenamento externo disponível.**

### 2. CPU e Processamento

- **Nome do Chipset e Fabricante:**
  - Identificação do processador.
- **Núcleos:**
  - Número de núcleos da CPU.
- **Taxa Máxima de Clock:**
  - Frequência máxima da CPU.
- **Processo:**
  - Tecnologia de fabricação do processador.
- **Nome da GPU e Núcleos:**
  - Especificações da unidade gráfica (GPU).

### 3. Bateria

- **Capacidade:**
  - Capacidade total da bateria (mAh).
- **Integridade:**
  - Estado geral da bateria.
- **Nível:**
  - Percentual de carga atual.
- **Status:**
  - Condição atual da bateria (carregando, descarregada).
- **Fonte de Alimentação:**
  - Tipo de energia conectada ao dispositivo.
- **Tecnologia:**
  - Tipo de bateria (Li-ion, Li-Po).
- **Temperatura:**
  - Temperatura atual da bateria.
- **Tensão:**
  - Voltagem atual da bateria.

### 4. Sistema Operacional

- **SO:**
  - Nome e versão do sistema operacional.
- **Número da Compilação:**
  - Identificador da versão do sistema.
- **Multitarefa:**
  - Capacidades do sistema em termos de gerenciamento de processos.
- **Informações do Kernel:**
  - Versão do kernel e detalhes.

### 5. Rede

- **Interface de Rede:**
  - Tipo de conexão (Wi-Fi, Ethernet).
- **Velocidade do Link:**
  - Taxa de conexão da rede.
- **Frequência da Rede:**
  - Frequência usada pela conexão (2.4 GHz, 5 GHz).

### 6. Exibição

- **Resolução e Densidade:**
  - Resolução da tela e densidade de pixels (PPI).
- **Tamanho Físico:**
  - Dimensões da tela em polegadas.
- **Taxas de Atualização Suportadas:**
  - Taxas de atualização suportadas (e.g., 60 Hz, 120 Hz).
- **Brilho:**
  - Configurações de brilho da tela (automático, manual).
- **Tempo Limite da Tela:**
  - Tempo de inatividade antes da tela desligar.

### 7. Informações do Dispositivo

- **Nome e Modelo do Dispositivo:**
  - Identificação específica do dispositivo.
- **Fabricante e Marca:**
  - Informações sobre o fabricante e a marca.
- **Versão do SO:**
  - Versão do sistema operacional instalada.
- **Versão do SDK:**
  - Versão do SDK utilizada.
- **Tipo de Display:**
  - Tipo de tela do dispositivo.
- **Chip/Hardware:**
  - Especificações do chip e hardware.
- **Placa:**
  - Detalhes sobre a placa-mãe.

## Proposta de Métricas

### CPU (Processador)

- **Número de Núcleos:**
  - Dispositivos com menos núcleos podem processar menos tarefas simultaneamente.
- **Frequência Máxima da CPU:**
  - Frequências mais altas geralmente indicam melhor desempenho.
- **Uso Atual da CPU:**
  - Alta utilização pode indicar que o dispositivo está sobrecarregado.

### Memória (RAM)

- **Total de Memória Disponível:**
  - Dispositivos com menos RAM podem ter dificuldade em manter várias atividades.
- **Memória Disponível Atual:**
  - Baixa memória livre pode levar a encerramentos inesperados.
- **Pressão de Memória:**
  - Se o dispositivo está em estado de "Low Memory".

### GPU (Gráficos)

- **Vendor e Renderer da GPU:**
  - Identificar GPUs menos potentes.
- **Suporte a APIs Gráficas:**
  - Verificar se o dispositivo suporta as funcionalidades gráficas necessárias.
- **Carga da GPU:**
  - Embora difícil sem acesso root, pode-se inferir desempenho gráfico.

### Bateria

- **Nível da Bateria:**
  - Dispositivos com bateria baixa podem ter desempenho reduzido.
- **Saúde e Temperatura da Bateria:**
  - Superaquecimento pode afetar o desempenho.

### Armazenamento

- **Espaço Disponível:**
  - Pouco armazenamento pode afetar cache e desempenho geral.
- **Tipo de Sistema de Arquivos:**
  - Alguns sistemas são mais rápidos que outros.

### Informações do Dispositivo

- **Modelo e Fabricante:**
  - Alguns dispositivos são conhecidos por terem hardware limitado.
- **Versão do Android e SDK:**
  - Dispositivos mais antigos podem não suportar certas otimizações.

### Rede

- **Tipo e Qualidade da Conexão:**
  - Pode afetar funcionalidades que dependem da rede.

### Opções de Desenvolvedor

- **"Não Manter Atividades":**
  - Se ativado, pode afetar o ciclo de vida do app.
- **USB Debugging e Root:**
  - Pode indicar um ambiente não padrão.

## Objetivo da Implementação do Modo "Light"

- **Redução de Animações e Transições:**
  - Desativar ou simplificar animações.
- **Carregamento de Recursos:**
  - Utilizar imagens de menor resolução.
- **Limitação de Funcionalidades:**
  - Desabilitar funcionalidades não essenciais.
- **Gerenciamento de Memória:**
  - Limpar caches e otimizar o uso de memória.
- **Ajuste de Taxa de Atualização:**
  - Reduzir frequência de atualizações em tempo real.
