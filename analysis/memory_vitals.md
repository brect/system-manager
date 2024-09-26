## Motivadores

### 1. Uso de android:largeHeap="true" em um aplicativo

O que você obtém quando isso é definido como true:
- Você obtém um heap maior para seu aplicativo, o que significa diminuição do risco de OutOfMemoryError.

Quais são algumas das desvantagens:

- Um heap maior faz com que as coletas de lixo demorem mais. Porque o coletor de lixo basicamente tem que percorrer todo o seu conjunto ativo de objetos. Normalmente, o tempo de pausa da coleta de lixo é de cerca de 5 ms, e você pode pensar que alguns milissegundos não são um grande problema. Mas cada milissegundo conta. O dispositivo Android tem que atualizar sua tela a cada 16 ms e um tempo de GC mais longo pode empurrar seu tempo de processamento de quadros acima da barreira de 16 milissegundos, o que pode causar um travamento visível.
- O sistema Android pode matar processos no cache LRU começando pelo processo menos usado recentemente, mas também dando alguma consideração para quais processos são mais intensivos em memória. Então, se você usar um heap maior, seu processo provavelmente será morto quando estiver em segundo plano, o que significa que pode levar mais tempo quando os usuários quiserem alternar de outros aplicativos para o seu.

Conclusão:
- Evite usar a opção largeHeap tanto quanto possível

#### Referenências:
- Visão geral do gerenciamento de memória: https://developer.android.com/topic/performance/memory-overview?hl=pt-br
- Gerenciar a memória do app: https://developer.android.com/topic/performance/memory?hl=pt-br
- application heap size available: https://stackoverflow.com/questions/2630158/detect-application-heap-size-in-android/9428660#9428660