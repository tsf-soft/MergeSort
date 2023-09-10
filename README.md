# **Руководство пользователя**

<br/>

## **1. Назначение утилит**

Настоящее руководство содержит сведения, необходимые для использования комплекта `Command Line Interface (CLI)` утилит, выполняющих сортировку слиянием
набора текстовых файлов `произвольного` формата. Сортируемые слиянием входные файлы объединяются в один выходной текстовый файл.

<br/>

## **2. Состав комплекта утилит**

Комплект состоит из двух `CLI` утилит:
- Утилита ___подготовки___ файлов входных данных для сортировки (в двух вариантах запуска):
  
  | **Файл** | **Иконка** | **Java (ver)** | **Назначение** |
  |:-----|--------|------------|------------|
  | `TsfDataPrepare.jar` | ![Логотип Java](https://github.com/tsf-soft/MergeSort/assets/6228605/896d02ef-8d0c-4b01-b224-5246397fde94) | 1.11.0[^1]  | CLI-утилита, запускается через JVM |
  | `WinTsfDataPrepare.exe` | ![WinTsfDataPrepare - иконка](https://github.com/tsf-soft/MergeSort/assets/6228605/0013e8f1-0c0e-4f0c-937f-9edcbb599c5a) | 1.20.0[^2] | CLI-утилита, запускается исполняющей системой Windows |
  <p>Таблица 2.1.

- Утилита ___сортировки слиянием___ подготовленных файлов входных данных (в двух вариантах запуска):

  | **Файл** | **Иконка** | **Java (ver)** | **Назначение** |
  |:-----|--------|------------|------------|
  | `TsfMergeSort.jar` | ![Логотип Java](https://github.com/tsf-soft/MergeSort/assets/6228605/896d02ef-8d0c-4b01-b224-5246397fde94) | 1.11.0[^1]  | CLI-утилита, запускается через JVM |
  | `WinTsfMergeSort.exe` | ![WinTsfMergeSort - иконка](https://github.com/tsf-soft/MergeSort/assets/6228605/0f2cf81c-6ccb-4d68-8541-6658baf092d7) | 1.20.0[^2] | CLI-утилита, запускается исполняющей системой Windows |
  <p>Таблица 2.2.
  
[^1]: Утилита написана под Java 1.11.0.
[^2]: Утилита собрана с JRE 1.20.0.

<br/>

## **3. Запуск утилит**

Возможно использование обоих вариантов утилит, как запускаемый через Java-машину, так и запускаемый через исполняющую систему Windows. Результат выдаваемый соответствующими утилитами не зависит
от способа их запуска.

### **3.1. Запуск утилит из командной строки**

Для запуска утилит подготовки входных данных, в командрую строку Windows (`cmd`, `powershell`, либо иных интерпретаторов) нужно ввести такие команды:
```
<Prompt>cd "<Disk>:\<ваш каталог>"
... 
<Prompt>java -jar TsfDataPrepare.jar [<опции>] {<Имя входного файла>...}
... или
<Prompt>TsfDataPrepare.exe [<опции>] <Имя выходного файла> {<Имя входного файла>...}
```
<p>Листинг 3.1.</p>
>Примечание: Здесь в первой строке мы переходим в рабочий каталог файловой системы, где расположены запускаемые файлы. Затем, в зависимости от варианта запуска, мы используем либо `jar`-версию программы,
>либо её `exe`-версию. Об элементах командной строки, указанных в CLI-листингах, сказано далее, в разделе <a href="#cli_args">Агрументы командной строки утилит</a>.

<br/>

Для запуска утилиты сортировки поступаем аналогично, но уже применительно к файлам сортировки:
```
<Prompt>cd "<Disk>:\<ваш каталог>"
... 
<Prompt>java -jar WinTsfMergeSort.jar [<опции>] {<Имя входного файла>...}
... или
<Prompt>WinTsfMergeSort.exe [<опции>] <Имя выходного файла> {<Имя входного файла>...}
```
<p>Листинг 3.2.</p>
  
### **3.2. Агрументы командной строки утилит**

<p><a name="cli_args"></a></p>

При запуске утилит в командной строке (_Листинг 3.1._ и _Листинг 3.2._) указываются следующие аргументы:
- _[<опции>]_ - не обязательные аргументы (могут отсутствовать) в количестве до 3-х штук определяющие следующие категории:
  - тип данных содержащихся в обрабатываемых файлах;
  - направление сортировки данных;
  - степень распараллеливания процесса обработки.
- _{<Имя входного файла>...}_ - от одного до нескольких входных текстовых файлов, подлежащих обработке. Это обязательный аргумент для обоих утилит.
- _<Имя выходного файла>_ - имя (одного) выходного текстового файла, являющегося результатом работ. Это обязательный аргумент для для утилиты `WinTsfMergeSort`. Для улилиты `TsfDataPrepare` этот аргумент __не требуется__. Утилита `TsfDataPrepare` выходным файлам присваивает такие-же имена, какие имели позиционно соответствующие им входные файлы.

Специфика командной строки для утилиты `TsfDataPrepare` заключается в том, что все имена файлов, перечисленные в качестве аргументов считаются входными файлами подлежащими обработке, в то время как для утилиты `WinTsfMergeSort` позиционно первое имя файла (после опций, если они представлены), является именем выходного файла. Иные имена файлов (после первого), перечисленные далее через пробелы, - являются входными файлами для этой утилиты.
Это связано с тем, что утилита `TsfDataPrepare` обрабатывает содержимое входного файла и обработанному файлу присваивает имя входного, но находящемуся в другом репозитарии. Для утилиты `WinTsfMergeSort` ситуация иная. Вне зависимости от того, как много файлов было заявлено для неё входными, - результатом работы этой утилиты является один файл, формат и содержимое которого соттветствуют указанным при запуске программы опциям.

#### **3.2.1. Опции командной строки

В разделе <a href="#cli_args">Агрументы командной строки утилит</a> перечислены три категории опций, указываемых в командной строке. Здесь указанные опции будут описаны подробно.
Опция представляет собой сочетание двух символов: -<дефис> и <один из символов ASCII>, например `-k`.
__Опция `Тип данных`__ может принимать одно из двух значений:
- `-i` - числовые данные (целое число от 0 до 2147483647); - эта опция является значением по умолчанию;
- `-s` - символьные данные (любой отображаемый символ UTF-8, за исключением пробельных символов, к которым в т.ч. относятся и управляющие символы).

__Опция `Тип сортировки`__ может принимать одно из двух значений:
- `-a` - сортировка по возрастанию (значение по умолчанию); для чисел - по возрастанию номера, для символов - по возрастанию лексикографического порядка;
- `-d` - сортировка по убыванию; для чисел - по убыванию номер, для символов - по убыванию лексикографического порядка.



  
