## Informações sobre Modo Desenvolvedor

#### 1. DEVELOPMENT_SETTINGS:
Indica se as opções de desenvolvedor estão habilitadas no dispositivo.
- Utiliza o `Settings.Global` para acessar a configuração `DEVELOPMENT_SETTINGS_ENABLED`.

#### 2. DONT_KEEP_ACTIVITIES
Indica se a opção "Não manter atividades" está habilitada, o que força o sistema a destruir todas as atividades assim que o usuário as sai.
-   Acessa a configuração `ALWAYS_FINISH_ACTIVITIES` através de `Settings.Global`.
-   Retorna `true` se o valor for diferente de 0, indicando que a opção está habilitada.

#### 3. USB_DEBUGGING
Indica se a depuração USB está habilitada, permitindo que o dispositivo se comunique com o computador via USB para fins de depuração.
-   Acessa a configuração `ADB_ENABLED` através de `Settings.Global`.
-   Retorna `true` se o valor for diferente de 0, indicando que a depuração USB está habilitada.

#### 4. DEVICE_ROOTED
Indica se o dispositivo está "rootado", ou seja, se o usuário tem acesso root (superusuário) ao sistema.
-   Verifica se o `Build.TAGS` contém "test-keys", o que pode indicar uma build customizada com acesso root.
-   Verifica a existência de arquivos comuns em dispositivos "rootados", como o binário `su`.
-   Se qualquer uma dessas verificações for positiva, retorna `true`, indicando que o dispositivo está "rootado".