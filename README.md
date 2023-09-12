# **Руководство пользователя**

<br/>

## **1. Назначение комплекта утилит**

<a name="chapt_1"></a>

Настоящее руководство содержит сведения, необходимые для использования комплекта `Command Line Interface (CLI)` утилит, выполняющих сортировку слиянием
набора текстовых файлов `произвольного` формата. Данные входных файлов сводятся в один текстовый отсортированный выходной файл.

Комплект состоит из двух `CLI` утилит: `TsfDataPrepare` и `TsfMergeSort`. Это взаимодополняющие утилиты используемые последовательно в случае, когда в качестве входных данных заданы текстовые данные произвольного формата, такие например, как показанные на <a href="#fig2_1">рис. 2.1</a>, либо  <a href="#fig2_2">рис. 2.2</a>. Утилита `TsfDataPrepare` — приводит такие данные к формату, в котором одно символьное слово занимает одну строку текстового файла, любые пробелы в строках текста отсутствуют. Выходные данные в файлах результата работы этой утилиты — не упорядочены.

Утилита `TsfMergeSort` работает с уже "нормализованными" текстовыми файлами (формат описан выше). Эти "нормализованные" файлы утилита сводит в один упорядоченный (отсортированный) текстовый файл хранящий данные всех входных файлов (см. <a href="#sort_opt">опции сортировки</a> в разделе 4.2.1). В зависимости от типа заявленных данных (см. тип данных) - выходные данные утилиты `TsfMergeSort` отсортированы либо в числовом, либо в лексикографическом порядке (в зависимости от заданных опций сортировки).

<br/>

## **2. Особенности и достоинства утилит**


### **2.1. Произвольный формат входных текстовых файлов**

Под произвольным текстовым форматом данных подразумевается обычный текстовый файл с расширением `.txt` в файловой системе Windows. Файл может иметь ограниченные, либо "бесконечные" строки текста. Текст может содержать любые символы, в т.ч. пробельные, управляющие (служебные) символы из полной "палитры" текста выраженного через кодировку UTF-8. Текст таких файлов может как иметь какой-либо определённый человеком смысл, так и не иметь какого-либо смысла (рыба-текст).

В качестве примера приведу вариант входного текстового файла содержащего числа в "бесконечных" строках. Этот файл, в том числе использовался в качестве одного из тестовых файлов отладки утилит (файл содержится в <a href="#fish_text">комплекте поставки</a> в качестве "сырых" данных).

<a name="fig2_1"></a>

![Числовые данные с "бесконечными" строками](https://github.com/tsf-soft/MergeSort/assets/6228605/d741e12a-4ef6-43cf-a8d4-a0c69e782a13)
_<p>Рисунок 2.1. Числовые данные с "бесконечными" строками</p>_

<br/>

Ещё пример, — входной текстовый файл "с признаками смысла", сгенерированный с помощью ИИ. Файл также имеет "бесконечные" (абзацные) строки текста (содержится в <a href="#fish_text">комплекте поставки</a> в качестве "сырых" данных).

<a name="fig2_2"></a>

![Текстовые данные на руссокм языке с строками-абзацами](https://github.com/tsf-soft/MergeSort/assets/6228605/8e2f7ff0-7825-4b77-a40a-986ceaba42c2)
_<p>Рисунок 2.2. Текстовые данные на русском языке с абзацными строками</p>_

В комплекте поставки имеется ещё один пример входного файла с англоязычным "рыба-текстом", содержащим в т.ч. множество не литеральных символов.


### **2.2. Распараллеливание процесса обработки**

В утилитах этого комплекта реализована концепция асинхронной обработки данных. Такой подход существенно ускоряет процесс обработки в случае однотипной обработки большого количества данных (файлов). В этом случае за время когда обрабатывается один большой файл, параллельно с ним будут обработаны ещё несколько файлов меньшего размера, т.е. для обработки следующего файла не нужно дожидаться завершения обработки предыдущего, — работа выполняется параллельно. Пример разной скорости завершения потоков показан в частности на <a href="#dif_vel">рис. 6.1</a>.


### **2.3. Хранение рабочей конфигурации в конфигурационных файлах**

<a name="amz_stor"></a>

В утилитах реализован принцип хранения рабочей конфигурации во внешних файлах. Обе утилиты используют одну и ту же конфигурацию и, соответственно, один и тот же конфигурационный файл. Пользователь может редактировать эти внешние конфигурационные файлы, настраивать их параметры под свои потребности. 

Изменяя параметры конфигурационного файла утилит установленные по умолчанию, пользователь таким образом, изменяет режим работы утилит установленный в исходной конфигурации.

![Конфигурационный файл утилит со значениями по умолчанию](https://github.com/tsf-soft/MergeSort/assets/6228605/af10bcc8-fd80-4f48-9020-08763499dad2)
_<p>Рисунок 2.3. Конфигурационный файл утилит со значениями по умолчанию</p>_

Описание конфигурационных параметров утилит и их возможные значения, приведены в <a href="#config_f">разделе 5.2</a>.


### **2.4. Широкие возможности пользовательских настроек**

С учетом сказанного в  <a href="#amz_stor">разделе 2.3</a> видно, что пользователь может самостоятельно установить все значимые параметры работы утилит в нужные ему значения. Он также может настроить все каталоги для различных категорий своих данных, используемых в процессе обработки (см. <a href="#cat_preset">раздел 5.1</a>.).

### **2.5. "Цветной" вывод в терминал**

Для тех терминалов, которые поддерживают режим совместимости с ANSI-терминалами, — возможен "раскрашенный" вывод статистической информации, а также логов о работе программы. Пример такого использования утилит, показан на рис. 2.4.

![Цветной режим работы терминала](https://github.com/tsf-soft/MergeSort/assets/6228605/0076ee35-6956-432d-bbaf-c619bfdc8c1b)
_<p>Рисунок 2.4. "Раскрашенный" вывод в терминал информации о работе утилиты</p>_

> [!NOTE]
> _**Примечание:**
> Терминалы интерпретаторов командной строки используемых в Windows, ANSI-режим не поддерживают, поэтому весь вывод утилит в эти терминалы будет монохромным._

<br/>

## **3. Состав комплекта утилит**

Как было сказано в предыдущих разделах, комплект состоит из двух утилит командной строки:

- Утилита ___подготовки___ файлов входных данных для сортировки (в двух вариантах запуска):
  
  | **Файл** | **Иконка** | **Java (ver)** | **Назначение** |
  |:-----|--------|------------|------------|
  | `TsfDataPrepare.jar` | ![Логотип Java](https://github.com/tsf-soft/MergeSort/assets/6228605/11b2fe64-6f89-43b7-ada1-e374f4f7af2c) | 1.11.0[^1]  | CLI-утилита подготовки файлов входных данных, запускается через JVM |
  | `WinTsfDataPrepare.exe` | ![WinTsfDataPrepare - иконка](https://github.com/tsf-soft/MergeSort/assets/6228605/9b514d8b-4578-4814-8ab6-8e99c89d9d41) | 1.20.0[^2] | CLI-утилита подготовки файлов входных данных, запускается исполняющей системой Windows |

  _<p>Таблица 3.1.</p>_

- Утилита ___сортировки слиянием___ подготовленных файлов входных данных (в двух вариантах запуска):

  | **Файл** | **Иконка** | **Java (ver)** | **Назначение** |
  |:-----|--------|------------|------------|
  | `TsfMergeSort.jar` | ![Логотип Java](https://github.com/tsf-soft/MergeSort/assets/6228605/11b2fe64-6f89-43b7-ada1-e374f4f7af2c) | 1.11.0[^1]  | CLI-утилита сортировки слиянием входных данных, запускается через JVM |
  | `WinTsfMergeSort.exe` | ![WinTsfMergeSort - иконка](https://github.com/tsf-soft/MergeSort/assets/6228605/f9fa10d6-4a6a-4034-8f27-aa45d37ccdd9) | 1.20.0[^2] | CLI-утилита сортировки слиянием входных данных, запускается исполняющей системой Windows |

  _<p>Таблица 3.2.</p>_
  
[^1]: Утилита написана под Java 1.11.0.
[^2]: Утилита собрана с JRE 1.20.0.

<br/>

## **4. Запуск утилит**

Возможно использование обоих вариантов утилит, как утилиты запускаемой через Java-машину, так и утилиты запускаемой исполняющей системой Windows. Результат, выдаваемый соответствующими утилитами, не зависит
от способа их запуска.

### **4.1. Запуск утилит из командной строки**

Для запуска утилиты подготовки входных данных, в командную строку Windows (`cmd`, `powershell`, либо иного интерпретатора) нужно ввести следующие команды:
```bash
<Prompt>cd "<Disk>:\<ваш каталог>"
... 
<Prompt>java -jar TsfDataPrepare.jar [<опции>] {<Имя входного файла>...}
... или
<Prompt>TsfDataPrepare.exe [<опции>] {<Имя входного файла>...}
```
_<p>Листинг 4.1.</p>_

> [!NOTE]
> _**Примечание:** Здесь в первой строке мы переходим в рабочий каталог файловой системы, где расположены запускаемые файлы. Затем, в зависимости от варианта запуска, мы используем либо `jar`-версию утилиты, либо её `exe`-версию. Об элементах командной строки, указанных в CLI-листингах, сказано далее, в разделе <a href="#cli_args">Аргументы командной строки утилит</a>.
> Здесь и далее, лексема `<Prompt>` - представляет собой стандартное "приглашение" строки терминала Windows, например такое `C:\Users\AF405>` - указывающее фактическую локацию пользователя в файловой системе. В свою очередь лексема `"<Disk>:\<ваш каталог>"` - это тоже самое, только введённое пользователем например `C:\Users\AF405\Desktop\MergeSort`, т.е. путь файловой системы к локации, в которой находится запускаемый файл утилиты._

<br/>

Для запуска утилиты сортировки поступаем аналогично, но уже применительно к файлам утилиты сортировки:
```bash
<Prompt>cd "<Disk>:\<ваш каталог>"
... 
<Prompt>java -jar WinTsfMergeSort.jar [<опции>] <Имя выходного файла> {<Имя входного файла>...}
... или
<Prompt>WinTsfMergeSort.exe [<опции>] <Имя выходного файла> {<Имя входного файла>...}
```
_<p>Листинг 4.2.</p>_

### **4.2. Аргументы командной строки утилит**

<a name="cli_args"></a>

Как показано в листингах 4.1-4.2, при запуске утилит в командной строке задаются следующие аргументы:
- _[<опции>]_ - не обязательные аргументы (могут отсутствовать) в количестве до 3-х штук определяющие следующие категории:
  - `тип данных` содержащихся в обрабатываемых файлах;
  - `направление сортировки` данных;
  - `степень распараллеливания` процесса обработки.
- _{<Имя входного файла>...}_ - от одного до нескольких входных текстовых файлов, подлежащих обработке. Это обязательный аргумент для обоих утилит.
- _<Имя выходного файла>_ - имя (одного) выходного текстового файла, являющегося результатом работы утилиты. Это обязательный аргумент для утилиты `WinTsfMergeSort`. Для утилиты `TsfDataPrepare` этот аргумент __не требуется__. Утилита `TsfDataPrepare` выходным файлам присваивает такие же имена, какие имели позиционно соответствующие им входные файлы.

Специфика командной строки для утилиты `TsfDataPrepare` заключается в том, что все имена файлов, перечисленные в качестве аргументов считаются входными файлами подлежащими обработке, в то время как для утилиты `WinTsfMergeSort` позиционно первое имя файла (после опций, если они представлены), является именем выходного файла. Иные имена файлов (после первого), перечисленные далее через пробелы, - являются входными файлами для этой утилиты. Данное обстоятельство связано с тем, что утилита `TsfDataPrepare` обрабатывает содержимое входного файла и обработанному файлу присваивает то же имя, однако при этом файл будет находиться в другом репозитории (каталоге). Для утилиты `WinTsfMergeSort` ситуация иная. Вне зависимости от того, как много файлов будет заявлено для этой утилиты в качестве входных, — результатом работы утилиты будет один файл, формат и содержимое которого соответствуют указанным при запуске утилиты опциям.

#### **4.2.1. Опции командной строки**

В разделе <a href="#cli_args">Аргументы командной строки утилит</a> перечислены три категории опций, указываемых в командной строке при запуске утилит. Далее, применяемые опции будут описаны подробно.

Опция представляет собой сочетание двух символов: -<дефис> и <одного или нескольких символов ASCII>, например: `-k`, `-p8`.

<a name="sort_opt"></a>

Опция __`Тип данных`__ - может принимать одно из следующих двух значений:
- `-i` - числовые данные (целое число от 0 до 2147483647); — эта опция является значением по умолчанию;
- `-s` - символьные данные (любой отображаемый символ UTF-8, за исключением пробельных символов, к которым в т.ч. относятся и управляющие символы).

Опция __`Тип сортировки`__ - может принимать одно из двух значений:
- `-a` - сортировка по возрастанию (значение по умолчанию); для чисел - по возрастанию номера, для символов - по возрастанию лексикографического порядка;
- `-d` - сортировка по убыванию; для чисел - по убыванию номер, для символов - по убыванию лексикографического порядка.

Опция __`Степень распараллеливания`__ - может принимать одно из следующих значений:
- `-p2` - задействуется два параллельных потока исполнения;
- `-p4` - задействуется четыре параллельных потока исполнения (значение по умолчанию);
- `-p8` - задействуется восемь параллельных потоков исполнения;
- `-p16` - задействуется шестнадцать параллельных потоков исполнения;
- `-p32` - задействуется тридцать два параллельных потока исполнения;

Специфика степени распараллеливания рабочего процесса для утилит следующая:
- утилита `TsfDataPrepare` - для обработки каждого нового входного файла запускает новый параллельный поток исполнения;
- утилита `TsfMergeSort` - последовательно обрабатывает каждый из входных файлов, но каждый входной файл она разделяет на несколько фрагментов, - выделение фрагментов из целого большого файла также выполняется в параллельных потоках (по одному фрагменту на поток). После разделения всех входных файлов на фрагменты начинается итеративная сортировка попарным слиянием полученных фрагментов которая, для каждой пары файлов, также производится в параллельных потоках.

Такова специфика распараллеливания рабочего процесса обработки данных для каждой из утилит, позволяющая ускорить рабочий процесс.

Для случая, когда в аргументах командной строки будут содержаться несколько взаимоисключающих опций одной категории, например `-p4 -i -p16`, - к фактическому исполнению будет принята последняя из опций указанной категории, т.е. в нашем случае для категории `Степень распараллеливания` будет установлено значение 16.

Следует отметить, что приоритет опций параметров командной строки выше, чем приоритет аналогичных параметров, получаемых утилитами из конфигурационного файла. Т.е. В конфигурационном файле для утилит определены значения по умолчанию, которые при необходимости обновляются из командной строки при запуске утилит.

#### **4.2.2 Имена рабочих файлов**

В качестве второй и третьей группы параметров, как указано в разделе 4.2 - используются имена файлов файловой системы Windows. К именам этих аргументов применимы стандартные требования Windows и имена могут задаваться:
- в составе относительного пути к файлу, например `data\temp_data\MyFile.txt`;
- в составе абсолютного пути к файлу, например `C:/Users/TSFUser/eclipse-workspace/MergeSort/data/temp_data/MyFile.txt`;
- простым именем файла (без указания пути) `MyFile.txt`;
- групповой операцией (с использованием мета символов), например `data\temp_data\*`, при этом будут найдены все файлы с расширением `.txt` (по умолчанию);
- с неопределённым символом в имени, например `temp_data\My??le.txt`;
- в иных форматах, допустимых синтаксисом командной строки.

> [!NOTE]
> _**Примечание:** направление символов слэш (прямой или обратный) - для утилит не имеет значения. В одном пути к файлу могут быть указаны и те и другие символы одновременно, например: `C:/Users\TSFUser\eclipse-workspace/MergeSort/data/temp_data\MyFile.txt` - утилита перекодирует такие символы, поэтому возможно использование любых вариантов._

При использовании "групповых" операций (мета символов), каталоги поиска могут содержать разнообразные файлы и вложенные в них каталоги. Утилиты "возьмут" только те, которые удовлетворяют их фильтрам поиска.

<br/>

## **5. Конфигурация и архитектура утилит**

### **5.1. Структура рабочих каталогов**

<a name="cat_preset"></a>

Для своей работы утилиты создают следующую структуру каталогов хранения данных:
- путь хранения "сырых" входных файлов, где хранятся исходные данные произвольного формата; этот путь используется утилитой `TsfDataPrepare`, она по умолчанию извлекает входные данные для своей работы из этого каталога;
- путь хранения подготовленных данных, где по умолчанию хранятся выходные данные результата работы утилиты `TsfDataPrepare`;
- путь хранения входных данных для утилиты `TsfMergeSort`, - отсюда утилита сортировки берёт свои входные данные по умолчанию;
- путь хранения промежуточных (шардированных) данных для работы утилиты `TsfMergeSort`; в этот каталог по умолчанию утилита "складывает" свои промежуточные результаты;
- путь хранения выходных отсортированных данных (файлов); сюда (по умолчанию) утилита `TsfMergeSort` помещает свои выходные файлы с отсортированными данными.

Для перечисленных категорий рабочих файлов, в конфигурации утилит по умолчанию установлены следующие значения:
- `data/raw_data/` - относительный путь хранения "сырых" данных (файлов);
- `data/prep_data/`- относительный путь хранения подготовленных утилитой `TsfDataPrepare` "нормализованных" данных;
- `data/inp_data/` - относительный путь хранения входных данных для утилиты `TsfMergeSort`; по умолчанию отсюда она берёт свои входные файлы для выполнения сортировки слиянием содержащихся в них файлов;
- `data/out_data/` - относительный путь хранения выходных данных утилиты `TsfMergeSort`; по умолчанию сюда она помещает результат своей работы - выходной файл.

При первом запуске утилит, они автоматически определяют локацию (каталог) своего запуска и далее, в случае отсутствия требуемых каталогов в файловой системе, перечисленных выше, - утилиты автоматически создают эти каталоги и инициализируют их (в частности полностью очищают каталог промежуточных файлов).

### **5.2. Параметры файла конфигурации**

<a name="config_f"></a>

На <a href="#amz_stor">рис. 2.3</a> приведен скриншот конфигурационного файла по умолчанию используемого утилитами для работы. Продублирую здесь содержимое конфигурационного файла:

```json
{
  "dataType" : false,
  "sortOrder" : false,
  "threadPoolCnt" : 4,
  "tmpFilePref" : "A#",
  "fileExt" : ".txt",
  "minShardFactor" : 20,
  "minShardSize" : 3000,
  "absBasePath" : "C:/Users/TSFUser/eclipse-workspace/MergeSort/",
  "relRawDataPath" : "data/raw_data/",
  "relPrepDataPath" : "data/prep_data/",
  "relInFilePath" : "data/inp_data/",
  "relTmpFilePath" : "data/temp_data/",
  "relOutFilePath" : "data/out_data/",
  "dtcreation" : 1694276431113
}
```
_<p>Листинг 5.1.</p>_

По сути, это JSON-файл хранящий данные по схеме "Ключ": "Значение" и имеющий расширение `.cfg`. Имя конфигурационного файла определено жестко и не может быть изменено пользователем. Конфигурационный файл называется `TsfMergeSortConfig.cfg`. Конфигурационный файл появляется в системе после первого запуска одной из утилит. Значения параметров конфигурационного файла пользователь может изменять. В дальнейшем утилиты при своей работе "руководствуются" конфигурацией, значения которой определены в конфигурационном файле.

Рассмотрим параметры конфигурации подробно.

- `dataType` параметр, определяет тип содержимого файлов, над которыми будут проводиться операции обработки; для этого параметра возможны такие значения:
  - `false` указывает, что входные файлы рассматриваются как содержащие данные в виде целых чисел (это значение по умолчанию);
  - `true` указывает, что входные файлы рассматриваются как содержащие символьные данные;
- `sortOrder` параметр, определяет направление сортировки к которому должен быть приведён выходной результат; для этого параметра возможны такие значения:
  - `false` указывает, что файл должен быть отсортирован по возрастанию (это значение по умолчанию);
  - `true` указывает, что файл должен быть отсортирован по убыванию;
- `threadPoolCnt` параметр, определяет на какое количество фрагментов входной файл будет разделён с использованием параллельных потоков выполнения; значение по умолчанию для параметра = 4; допустимые значения описаны в <a href="#cli_args">разделе 4.2.1</a>; можно указывать любое значение, однако в конфигурации оно будет приведено к значению числа 2 в степени Х: максимальное значение = 32 согласно сведениям, содержащимся в <a href="#cli_args">разделе 4.2.1</a>;
- `tmpFilePref` параметр, определяет начальный префикс файла нижнего уровня разделения, т.е. с использованием английского алфавита [A-Z] - можно задействовать слияние сложностью в 32 уровня разделения/слияния (2 в 32-ой степени уровней) попарного слияния файлов-фрагментов, при этом количество пар файлов-фрагментов на одном уровне ограничено только числом помещающимся в формат положительного целого числа `java int`, - т.е. много (можно при необходимости сделать это число беззнаковым целым, что увеличит и без того большие возможности по слиянию в два раза);
- `fileExt` параметр, определяет расширение файлов, с которыми работают утилиты; значение по умолчанию `.txt`; может быть изменено на любое другое;
- `minShardFactor` параметр, определяет минимальное количество строк на один фрагмент разделения, менее которого разделение файла на фрагменты производится не будет; для примера, если при 4-х потоках разделения и 20 строках минимального размера текстового файла фактически во входном файле будет мерее 80 строк (20 х 4), то разделение на фрагменты для такого маленького файла производится не будет; значение по умолчанию равно 20-ти строкам;
- `minShardSize` параметр, определяет минимальный размер входного файла в байтах, менее которого файл не будет разделяться на фрагменты;
- `absBasePath` параметр, определяет абсолютный путь к базовому каталогу, в котором запускаются утилиты; при первом запуске утилиты определяют этот параметр самостоятельно, - в дальнейшем берут из конфигурационного файла;
- `dtcreation` параметр, содержит дату и время создания конфигурации в формате `TimeStamp`; задаётся автоматически утилитой изменившей конфигурацию.

Оставшиеся параметры конфигурационного файла, касающиеся структуры рабочих каталогов, а также значения по умолчанию для этих параметров, определены в <a href="#cat_preset">разделе 5.1</a>.
К таким параметрам относятся:
- `relRawDataPath` - путь к каталогу "сырых" входных данных;
- `relPrepDataPath` - путь к каталогу подготовленных ("нормализованных") данных;
- `relInFilePath` - путь к каталогу входных данных сортировки;
- `relTmpFilePath` - путь к каталогу промежуточных результатов сортировки;
- `relOutFilePath` - путь к каталогу выходных файлов сортировки.

Следует отметить, что приоритет параметров конфигурационного файла ниже, чем у аналогичных опций параметров передаваемых в аргументах командной строки, поскольку конфигурационный файл фактически задаёт значения по умолчанию для работы утилит, однако путём ввода нужных значений параметров в конфигурационный файл, пользователь может избавить себя от необходимости указывать параметры через аргументы командной строки при запуске утилит.

<br/>

## **6. Работа с утилитами и комплект поставки**

### **6.1. Работа с утилитами**

Как было сказано в <a href="#chapt_1">разделе 1</a>, утилиты обрабатывают входные файлы последовательно.

Вначале входные файлы подвергаются обработке утилитой `TsfDataPrepare`. Для этого нужно в каталог `data/raw_data/` относительно пути местоположения самой утилиты поместить один или несколько текстовых файлов произвольного формата данных, затем в CLI выполнить например, следующую команду:

```bash
TsfDataPrepare.exe -s -p16 data/raw_data/*
```
Эта команда означает, что утилите `TsfDataPrepare` в её исполняемом варианте (`.exe`) предлагается обработать все файлы с расширением `.txt`, находящиеся в каталоге `data/raw_data/` относительно каталога местонахождения утилиты, при этом ей нужно рассматривать содержимое всех файлов символьным (опция `-s`), выполнять обработку пулом из 16 потоков (опция `-p16`) и поместить результаты своей работы ("нормализованные" файлы) в каталог `data/prep_data/` (значение по умолчанию из конфигурационного файла) относительно местоположения утилиты. Из перечисленных опций, для этой утилиты "не обязательной" является опция размера пула потоков, поскольку эта утилита по умолчанию пытается задействовать максимально возможное число из числа необходимых потоков исполнения и в максимальном своём значении это значение может равняться количеству входных файлов переданных утилите на обработку, т.е. теоретически количество параллельных потоков задействованных этой утилитой может быть более чем 32.

Процедура обработки входных данных специально реализована в виде двух утилит, для того, чтобы пользователь при необходимости мог оценить полученный промежуточный результат, после чего возможно изменить параметры запуска утилиты, либо подкорректировать полученные утилитой значения в ручном режиме. Например, если для обработки файла представленного на <a href="#fig2_1">рис. 2.1</a> задать опцию `-s`, то на выходе мы получим пустой файл, поскольку содержимое входного файла не соответствует регулярному выражению, определяющему требования к содержимому выходного файла. Пример подобной ситуации показан на скриншоте ниже.

<a name="dif_vel"></a>

![Пример не соответствия формата](https://github.com/tsf-soft/MergeSort/assets/6228605/90971287-ba6b-499d-8a46-0f3a55d93bc7)
_<p>Рисунок 6.1. Пример не соответствия содержимого одного из файлов установленным опциям обработки</p>_

Помимо сказанного, выходные файлы утилиты `TsfDataPrepare` могут содержать много "мусорной" информации, которая не требуется для сортировки, тем не менее, утилита не может удалять такую информацию автоматически. В этом случае пользователь в "ручном режиме" может удалить не нужную информацию.

Затем, для выполнения собственно сортировки, пользователь из каталога `data/raw_data/` должен скопировать имеющиеся там файлы в каталог `data/inp_data/`. Последний каталог является местом хранения входных файлов для утилиты сортировки слиянием `TsfMergeSort`. По завершении копирования файлов, можно запустить утилиту сортировки. Это например, можно сделать следующей командой:

```bash
TsfMergeSort.exe -s -a -p8 outPutFile1.txt *
```
Последняя команда означает, что утилита `TsfMergeSort` в её исполняемом варианте (`.exe`) должна в качестве входных файлов взять из каталога `data/inp_data/` (определено конфигурационным файлом) все файлы (имя группы входных файлов `*`) с расширением `.txt` (значение параметра `fileExt` в конфигурационном файле), рассматривать все эти входные файлы как содержащие символьные данные (опция `-s`), отсортировать все входные данные в порядке возрастания (опция `-a`) используя 8 параллельных потоков (опция `-p8`) и поместить все выходные данные в файл с именем `outPutFile1.txt`, находящийся в каталоге `data/out_data/` (определено конфигурационным файлом) относительно локации запуска утилиты.

### **6.2. Комплект поставки**

"Заказчику" утилиты поставляются в следующем составе:
1. Модули исходного кода классов java, в составе архива `SourceCode-of-TsfMergeSort.zip`:
   - составляющие утилиту `TsfDataPrepare`, находятся в каталоге `SourceCode-of-TsfMergeSort.zip\shift-java\src\main\java\prepare_text_data\`;
   - составляющие утилиту `TsfMergeSort` находятся в каталоге `SourceCode-of-TsfMergeSort.zip\shift-java\src\main\java\merge_sort\`;
2. Модули скомпилированного кода классов java, в составе архива `SourceCode-of-TsfMergeSort.zip`:
   - составляющие утилиту `TsfDataPrepare`, находятся в каталоге `SourceCode-of-TsfMergeSort.zip\shift-java\target\classes\prepare_text_data\`;
   - составляющие утилиту `TsfMergeSort`, находятся в каталоге `SourceCode-of-TsfMergeSort.zip\shift-java\target\classes\merge_sort\`;
3. Модуль `pom.xml` сборщика проекта maven находиться в каталоге `SourceCode-of-TsfMergeSort.zip\shift-java\`
4. Комплект файлов тестовых данных, можно скачать здесь: [Тестовые данные для проверки утилит](https://disk.yandex.ru/d/l4aRjuNC-rBffA); <a name="fish_text"></a>
5. Сами исполняемые утилиты `TsfDataPrepare.jar`, `WinTsfDataPrepare.exe`, `TsfMergeSort.jar` и `WinTsfMergeSort.exe` можно скачать [здесь](https://disk.yandex.ru/d/YZ18-8OZkcdq_w).
6. Руководство пользователя в виде этого документа, которое также можно посмотреть на GitHub [здесь](https://github.com/tsf-soft/MergeSort).

### **6.3. Зависимости модуля сборки**

В проекте использована популярная библиотека Jackson для сериализации объектов Java в JSON-строку и десериализации JSON-строки в объект Java. Зависимости относящиеся к этой библиотеке, представлены в листинге 6.1 `pom.xml`-файла сборщика проекта ниже.


```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>Shift</groupId>
  <artifactId>java</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <name>test</name>
  <description>Проект по заданию SHIFT на знание языка Java.</description>
  
	<dependencies>
			  
	        <dependency>
	            <groupId>com.fasterxml.jackson.core</groupId>
	            <artifactId>jackson-databind</artifactId>
	            <version>2.13.3</version>
	        </dependency>
	
	        <dependency>
	            <groupId>com.fasterxml.jackson.datatype</groupId>
	            <artifactId>jackson-datatype-jsr310</artifactId>
	            <version>2.13.3</version>
	        </dependency>
	
	        <dependency>
	            <groupId>org.junit.jupiter</groupId>
	            <artifactId>junit-jupiter-api</artifactId>
	            <version>5.9.0-M1</version>
	            <scope>test</scope>
	        </dependency>
	
	        <dependency>
	            <groupId>org.projectlombok</groupId>
	            <artifactId>lombok</artifactId>
	            <version>1.18.24</version>
	            <scope>provided</scope>
	        </dependency>
	
	        <dependency>
	            <groupId>org.assertj</groupId>
	            <artifactId>assertj-core</artifactId>
	            <version>3.23.1</version>
	            <scope>test</scope>
	        </dependency>
       
  	</dependencies>
  	
</project>
```

_<p>Листинг 6.1.</p>_

<br/>

## **7. Контрибьютор проекта**

Разработчик проекта Карышев Е.Н.
e-mail: i@tsf-soft.ru
Telegram: https://t.me/tsf_soft.

Сентябрь 2023 года.

<br/>
