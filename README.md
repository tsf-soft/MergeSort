# **Руководство пользователя**

<br/>

## **1. Назначение комплекта утилит**

Настоящее руководство содержит сведения, необходимые для использования комплекта `Command Line Interface (CLI)` утилит, выполняющих сортировку слиянием
набора текстовых файлов `произвольного` формата. Данные входных файлов сводятся в один текстовый отсортированный выходной файл.

Комплект состоит из двух `CLI` утилит: `TsfDataPrepare` и `TsfMergeSort`. Эти утилиты взаимодополняющие и должны быть использованы последовательно, если в качестве входных данных заданы текстовые данные произвольного формата, такие например, как на рис. 2.1. Утилита `TsfDataPrepare` — приводит такие данные к формату, в котором одно символьное слово занимает одну строку текстового файла, любые пробелы в строках текста отсутствуют. Выходные данные в файлах результата работы этой утилиты — не упорядочены.

Утилита `TsfMergeSort` работает с уже "нормализованными" текстовыми файлами (формат описан выше). Эти "нормализованные" файлы утилита сводит в один упорядоченный (отсортированный) текстовый файл хранящий данные всех входных файлов (см. опции сортировки). В зависимости от типа заявленных данных (см. тип данных) - выходные данные утилиты `TsfMergeSort` отсортированы либо в числовом, либо в лексикографическом порядке (в зависимости от заданных опций сортировки).

<br/>

## **2. Особенности и достоинства утилит**


### **2.1. Произвольный формат входных текстовых файлов**

Под произвольным текстовым форматом данных подразумевается обычный текстовый файл с расширением `.txt` в файловой системе Windows. Файл может иметь ограниченные, либо "бесконечные" строки текста. Текст может содержать любые символы, в т.ч. пробельные, управляющие (служебные) из полной "палитры" текста выраженного через кодировку UTF-8. Текст таких файлов может как иметь какой-либо определённый человеком смысл, так и не иметь какого-либо смысла (рыба-текст).

В качестве примера приведу вариант входного текстового файла содержащего числа в "бесконечных" строках. Этот файл, в том числе использовался в качестве одного из тестовых файлов отладки утилит (файл содержится в комплекте поставки в качестве "сырых" данных).

![Числовые данные с "бесконечными" строками](https://github.com/tsf-soft/MergeSort/assets/6228605/70b8a191-b74e-40eb-91fb-20f96bdf5348)
_<p>Рисунок 2.1. Числовые данные с "бесконечными" строками</p>_

Ещё пример, — входной текстовый файл "с признаками смысла", сгенерированный с помощью ИИ. Файл также имеет "бесконечные" (абзацные) строки текста (содержится в комплекте поставки в качестве "сырых" данных).

![Текстовые данные на руссокм языке с абзацными строками](https://github.com/tsf-soft/MergeSort/assets/6228605/4f460476-ae10-43d7-9f88-14ec8fb91b4f)
_<p>Рисунок 2.2. Текстовые данные на русском языке с абзацными строками</p>_

В комплекте поставки имеется ещё один пример входного файла с англоязычным "рыба-текстом", содержащим в т.ч. множество не литеральных символов.


### **2.2. Распараллеливание процесса обработки**

В утилитах этого комплекта реализована концепция асинхронной обработки данных. Такой подход существенно ускоряет процесс обработки в случае однотипной обработки большого количества данных (файлов). В этом случае за время когда обрабатывается один большой файл, параллельно с ним будут обработаны ещё несколько файлов меньшего размера, т.е. для обработки следующего файла не нужно дожидаться завершения обработки предыдущего, — работа выполняется параллельно. Пример разной скорости завершения потоков показан в частности на рис. ХХ.


### **2.3. Хранение рабочей конфигурации в конфигурационных файлах**

В утилитах реализован принцип хранения рабочей конфигурации во внешних файлах. Обе утилиты используют одну и туже конфигурацию и, соответственно, один и тот же конфигурационный файл. Пользователь может редактировать эти внешние конфигурационные файлы, настраивать их параметры под свои потребности. 

Изменяя конфигурационный файл утилит и их параметры установленные по умолчанию, пользователь таким образом, изменяет режим работы утилит установленный в исходной конфигурации.

![Конфигурационный файл утилит со значениями по умолчанию](https://github.com/tsf-soft/MergeSort/assets/6228605/fc9ac1cf-f69f-4742-af81-b42709dceb2b)
_<p>Рисунок 2.3. Конфигурационный файл утилит со значениями по умолчанию</p>_

Описание конфигурационных параметров утилит и их возможные значения, приведены в разделе ХХ.


### **2.4. Широкие возможности пользовательских настроек**

С учетом сказанного в разделе 2.3, видно, что пользователь может самостоятельно установить все значимые параметры работы утилит в нужные ему значения. Он также может настроить все каталоги для различных категорий своих данных, используемых в процессе обработки (см. раздел ХХ).

### **2.5. "Цветной" вывод в терминал**

Для тех терминалов, которые поддерживают режим совместимости с ANSI-терминалами, — возможен "раскрашенный" вывод статистической информации, а также логов о работе программы. Пример такого использования утилит, показан на рис. 2.4.

![Цветной режим работы терминала](https://github.com/tsf-soft/MergeSort/assets/6228605/9ee82910-35de-40be-8a35-ea22339d6ba8)
_<p>Рисунок 2.4. "Раскрашенный" вывод в терминал информации о работе утилиты</p>_

>Примечание: Терминалы интерпретаторов командной строки используемых в Windows, ANSI-решим не поддерживают, поэтому весь вывод утилит в эти терминалы будет монохромным.

<br/>

## **3. Состав комплекта утилит**

Как было сказано в предыдущих разделах, комплект состоит из двух утилит командной строки:

- Утилита ___подготовки___ файлов входных данных для сортировки (в двух вариантах запуска):
  
  | **Файл** | **Иконка** | **Java (ver)** | **Назначение** |
  |:-----|--------|------------|------------|
  | `TsfDataPrepare.jar` | ![Логотип Java](https://github.com/tsf-soft/MergeSort/assets/6228605/896d02ef-8d0c-4b01-b224-5246397fde94) | 1.11.0[^1]  | CLI-утилита подготовки файлов входных данных, запускается через JVM |
  | `WinTsfDataPrepare.exe` | ![WinTsfDataPrepare - иконка](https://github.com/tsf-soft/MergeSort/assets/6228605/0013e8f1-0c0e-4f0c-937f-9edcbb599c5a) | 1.20.0[^2] | CLI-утилита подготовки файлов входных данных, запускается исполняющей системой Windows |

  _<p>Таблица 3.1.</p>_

- Утилита ___сортировки слиянием___ подготовленных файлов входных данных (в двух вариантах запуска):

  | **Файл** | **Иконка** | **Java (ver)** | **Назначение** |
  |:-----|--------|------------|------------|
  | `TsfMergeSort.jar` | ![Логотип Java](https://github.com/tsf-soft/MergeSort/assets/6228605/896d02ef-8d0c-4b01-b224-5246397fde94) | 1.11.0[^1]  | CLI-утилита сортировки слиянием входных данных, запускается через JVM |
  | `WinTsfMergeSort.exe` | ![WinTsfMergeSort - иконка](https://github.com/tsf-soft/MergeSort/assets/6228605/0f2cf81c-6ccb-4d68-8541-6658baf092d7) | 1.20.0[^2] | CLI-утилита сортировки слиянием входных данных, запускается исполняющей системой Windows |

  _<p>Таблица 3.2.</p>_
  
[^1]: Утилита написана под Java 1.11.0.
[^2]: Утилита собрана с JRE 1.20.0.

<br/>

## **4. Запуск утилит**

Возможно использование обоих вариантов утилит, как запускаемой через Java-машину, так и запускаемой через исполняющую систему Windows. Результат, выдаваемый соответствующими утилитами, не зависит
от способа их запуска.

### **4.1. Запуск утилит из командной строки**

Для запуска утилит подготовки входных данных, в командную строку Windows (`cmd`, `powershell`, либо иных интерпретаторов) нужно ввести такие команды:
```
<Prompt>cd "<Disk>:\<ваш каталог>"
... 
<Prompt>java -jar TsfDataPrepare.jar [<опции>] {<Имя входного файла>...}
... или
<Prompt>TsfDataPrepare.exe [<опции>] <Имя выходного файла> {<Имя входного файла>...}
```
_<p>Листинг 4.1.</p>_

>Примечание: Здесь в первой строке мы переходим в рабочий каталог файловой системы, где расположены запускаемые файлы. Затем, в зависимости от варианта запуска, мы используем либо `jar`-версию программы, либо её `exe`-версию. Об элементах командной строки, указанных в CLI-листингах, сказано далее, в разделе <a href="#cli_args">Аргументы командной строки утилит</a>.
>Здесь и далее, лексема `<Prompt>` - представляет собой стандартное "приглашение" строки терминала Windowd, например такое `C:\Users\AF405>` - указывающее фактическую локацию пользователя в файловой системе. В свою очередь лексема `"<Disk>:\<ваш каталог>"` - это тоже самое, только введённое пользователем например `C:\Users\AF405\Desktop\MergeSort`, т.е. путь файловой системы к локации, в которой находится запускаемый файл утилиты.

<br/>

Для запуска утилиты сортировки поступаем аналогично, но уже применительно к файлам сортировки:
```
<Prompt>cd "<Disk>:\<ваш каталог>"
... 
<Prompt>java -jar WinTsfMergeSort.jar [<опции>] {<Имя входного файла>...}
... или
<Prompt>WinTsfMergeSort.exe [<опции>] <Имя выходного файла> {<Имя входного файла>...}
```
_<p>Листинг 4.2.</p>_

### **4.2. Аргументы командной строки утилит**

<a name="cli_args"></a>

Как показано в _Листингах 4.1.-4.2._, при запуске утилит в командной строке задаются следующие аргументы:
- _[<опции>]_ - не обязательные аргументы (могут отсутствовать) в количестве до 3-х штук определяющие следующие категории:
  - `тип данных` содержащихся в обрабатываемых файлах;
  - `направление сортировки` данных;
  - `степень распараллеливания` процесса обработки.
- _{<Имя входного файла>...}_ — от одного до нескольких входных текстовых файлов, подлежащих обработке. Это обязательный аргумент для обоих утилит.
- _<Имя выходного файла>_ — имя (одного) выходного текстового файла, являющегося результатом работ. Это обязательный аргумент для утилиты `WinTsfMergeSort`. Для утилиты `TsfDataPrepare` этот аргумент __не требуется__. Утилита `TsfDataPrepare` выходным файлам присваивает такие же имена, какие имели позиционно соответствующие им входные файлы.

Специфика командной строки для утилиты `TsfDataPrepare` заключается в том, что все имена файлов, перечисленные в качестве аргументов считаются входными файлами подлежащими обработке, в то время как для утилиты `WinTsfMergeSort` позиционно первое имя файла (после опций, если они представлены), является именем выходного файла. Иные имена файлов (после первого), перечисленные далее через пробелы, — являются входными файлами для этой утилиты.
Это связано с тем, что утилита `TsfDataPrepare` обрабатывает содержимое входного файла и обработанному файлу присваивает то же имя, но файл будет находиться в другом репозитории (каталоге). Для утилиты `WinTsfMergeSort` ситуация иная. Вне зависимости от того, как много файлов будет заявлено для этой утилиты в качестве входных, — результатом работы утилиты будет один файл, формат и содержимое которого соответствуют указанным при запуске утилиты опциям.

#### **4.2.1. Опции командной строки**

В разделе <a href="#cli_args">Аргументы командной строки утилит</a> перечислены три категории опций, указываемых в командной строке. Здесь указанные опции будут описаны подробно.
Опция представляет собой сочетание двух символов: -<дефис> и <один или несколько символов ASCII>, например: `-k`, `-p8`.

Опция __`Тип данных`__ - может принимать одно из двух значений:
- `-i` - числовые данные (целое число от 0 до 2147483647); — эта опция является значением по умолчанию;
- `-s` - символьные данные (любой отображаемый символ UTF-8, за исключением пробельных символов, к которым в т.ч. относятся и управляющие символы).

Опция __`Тип сортировки`__ - может принимать одно из двух значений:
- `-a` - сортировка по возрастанию (значение по умолчанию); для чисел — по возрастанию номера, для символов — по возрастанию лексикографического порядка;
- `-d` - сортировка по убыванию; для чисел — по убыванию номер, для символов — по убыванию лексикографического порядка.

Опция __`Степень распараллеливания`__ - может принимать одно из следующих значений:
- `-p2` - задействуется два параллельных потока исполнения;
- `-p4` - задействуется четыре параллельных потока исполнения (значение по умолчанию);
- `-p8` - задействуется восемь параллельных потоков исполнения;
- `-p16` - задействуется шестнадцать параллельных потоков исполнения;
- `-p32` - задействуется тридцать два параллельных потока исполнения;

Специфика степени распараллеливания рабочего процесса для утилит следующая:
- утилита `TsfDataPrepare` - для обработки каждого нового входного файла запускает новый параллельный поток исполнения;
- утилита `TsfMergeSort` - последовательно обрабатывает каждый из входных файлов, но каждый входной файл она разделяет на несколько фрагментов, — выделение фрагментов из целого большого файла также выполняется в параллельных потоках (по одному фрагменту на поток). После разделения всех входных файлов на фрагменты начинается итеративная сортировка попарным слиянием полученных фрагментов которая, для каждой пары файлов, также производится в параллельных потоках.

Такова специфика распараллеливания рабочего процесса обработки данных для каждой из утилит, позволяющая ускорить рабочий процесс.



