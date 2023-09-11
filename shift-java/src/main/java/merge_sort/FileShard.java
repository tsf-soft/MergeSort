/*
 * Класс решает две задачи: ускорение обработки; приведение данных к требуемому формату.
 * Экземпляр класса выполняет выделение ("отщепление") фрагмента из входного
 * (большого) текстового файла и окончательное приведение фрагмента к требованию ТЗ (Shift).
 * Выделение фрагмента производится в индивидуальном (параллельном) потоке исполнения.
 * Количество потоков исполнения (и соответственно фрагментов) - задается при
 * запуске утилиты, либо по умолчанию берется из конфигурационного файла.
 * Маленькие файлы - не фрагментируются.
 * 
 * Автор: Карышев Е.Н., сентябрь 2023 года.
 * 
 * Подробности, в т.ч. о формате командной строки, см. README.md-файл.
 */

package merge_sort;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.stream.Stream;


// разделение текстового файла на фрагменты, согласно списка 
public class FileShard extends Thread  {
	FileStreamDescr inFlDsc;					// дескриптор входного файла
	FileStreamDescr outFlDsc;					// дескриптор входного файла
	String prmMsg;								// префикс информационного сообщения

    // конструктор класса с параметрами
    public FileShard(FileStreamDescr inFlDsc, FileStreamDescr outFlDsc) {
    	this.inFlDsc	= inFlDsc;
    	this.outFlDsc	= outFlDsc;
        this.prmMsg 	= SrvLib.getClassName();		
    }

    // основной метод запуска потока
    @Override
    public void run () {
    	// инициализация входного и выходного потока 
    	try (Stream<String> chunks = Files.lines(Paths.get(this.inFlDsc.getFileName()))
                		.skip(--this.outFlDsc.startLineNmb)
                		.limit(this.outFlDsc.endLineNmb - this.outFlDsc.startLineNmb)
                		.sorted(this.outFlDsc.dataType
		                		? (!this.outFlDsc.sortOrder
		                			? Comparator.naturalOrder()
		                			: Comparator.reverseOrder())
		                		: (!this.outFlDsc.sortOrder
		                			? Comparator.comparingInt(Integer::parseInt)
		                			: Comparator.comparingInt(a -> Integer.parseInt((String) a)).reversed())
		                		);
    		BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.outFlDsc.getFileName()));
    		) {
    		chunks.forEach(line -> {
            	try {
            		if (writeLineToFile(writer, line.trim(), this.outFlDsc)) this.outFlDsc.curLinesCount++;
            	} catch (SplitFileExeption e) {
					System.out.println(SrvLib.getErrPromt(prmMsg) 
							+ "Ошибка записи строки #"
							+ this.outFlDsc.curLinesCount + 1
							+ " в файл " + this.outFlDsc.getFileName() + ".");
					e.printStackTrace();
            	}
            });
    		SrvLib.outInfo(inFlDsc, outFlDsc, prmMsg);		// отображаем статистику переноса данных

    	// обработка возможных исключений
    	} catch (SplitFileExeption e) {
    		System.out.println(e.getMessage() + "\n"
    				+ prmMsg
    				+ "-INFO: Проверьте соответствие параметра: \"Тип данных\" фактическому содержимому входного\n"
    				+ "файла: " + SrvLib.getFailStart() + this.inFlDsc.getFileName()
    				+ SrvLib.getConEnd() + ".\n"
    				+ "Возможно, содержимое файла не соотетствует ТЗ. Например, в строках содержатся пробельные символы.\n"
    				+ "По указанной причине фрагменты составляющие входной файл " + SrvLib.getFailStart()
    				+ SrvLib.fileNameExtract(this.inFlDsc.getFileName()) + SrvLib.getConEnd()
    				+ " в дальнейшем процессе задействованы не будут.\n");
    				
    		interrupt();
    	} catch (FileNotFoundException e) {
    		System.out.println(SrvLib.getErrPromt(prmMsg) 
    				+ "Не найден входной файл " + SrvLib.getFailStart()
    				+ this.inFlDsc.getFileName() + SrvLib.getConEnd() + ".");
    		interrupt();
    	} catch (Exception e) {
    		System.out.println(SrvLib.getErrPromt(prmMsg) 
    				+ "Возникла ошибка при разделении файла " + SrvLib.getFailStart()
    				+ SrvLib.fileNameExtract(this.inFlDsc.getFileName()) + SrvLib.getConEnd() + ".");
    		interrupt();
    		e.printStackTrace();
        } 
    }		// метод run
    
    // запись строки в файл,
    // если строка не соответствует условию regExpStr, - она игнорируется и в файл не попадает
    public static boolean writeLineToFile(
    		BufferedWriter wr,
    		String line,
    		FileStreamDescr outf) throws SplitFileExeption {
    	
    	if ((line.length() > 0) && Pattern.matches(outf.getRegExpRule(), line)) {
        	try {
        		wr.write(line + "\r\n");
            	return true;
        	} catch (Exception e) {
        		throw new SplitFileExeption("Ошибка записи в файл.");
            }
    	}
    	return false;
    }
    
}		// класс
