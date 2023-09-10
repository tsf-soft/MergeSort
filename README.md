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

## **3. Запуск утилит, аргументы командной строки**

Возможно использование обоих вариантов утилит, как запускаемый через Java-машину, так и запускаемый через исполняющую систему Windows. Результат выдаваемый соответствующими утилитами не зависит
от способа их запуска.

### **Запуск утилит из командной строки**

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
>либо её `exe`-версию. Об элементах командной строки, указанных в CLI-листингах, сказано далее, в разделе [Агрументы командной строки утилит](#агрументы-командной-строки-утилит).

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

### **Агрументы командной строки утилит**

ыфва
