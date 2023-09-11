/*
 * Класс решает задачу преобразования входного текстовый файл, произвольного формата
 * в файл удовлетворяющий требованиям ТЗ.
 * 
 * Автор: Карышев Е.Н., сентябрь 2023 года.
 * 
 * Методы класса обрабатывают входные данные в индивидуальных потоках.
 * Подробности,  см. README.md-файл.
 */

package prepare_text_data;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import merge_sort.SrvLib;
import merge_sort.FileStreamDescr;


public class FileConvert extends Thread  {
	
	FileStreamDescr inf;			// указатель на дескриптор входного файла
	FileStreamDescr outf;			// указатель на дескриптор выходного файла
	Path infP;						// путь ко входному файлу
	Path outfP;						// путь к выходному файлу
	String rul;						// RegExp-правила разделения потока из входного файла
	String rer;						// RegExp-правила соответствия срок выходного файла
	String mPrmMsg;					// префикс сообщений из этого класса
	long outLineCnt;				// счетчик строк выведенных в выходной файл

	
	// конструктор с аргументами
	public FileConvert(FileStreamDescr inf, FileStreamDescr outf) {
		this.inf		= inf;
		this.outf		= outf;
		this.infP		= Paths.get(inf.getFileName());
		this.outfP		= Paths.get(outf.getFileName());
		this.rul		= inf.getRegExpRule();				// правила разделения уст. из входного дескриптора
		this.rer		= outf.getRegExpRule();				// правила соответствия уст. из выходного дескриптора
		this.mPrmMsg 	= SrvLib.getClassName();
		this.outLineCnt = 0;
	}
	
    // основной метод запуска потока
    @Override
    public void run () {
		try {
			Stream<String> lines = Files.lines(infP, Charset.defaultCharset());
	    	BufferedWriter writer = Files.newBufferedWriter(outfP);
	    	lines.forEach(line -> outLineCnt += writeLine(line, writer));
	    	outf.setFileTotalLineCount(outLineCnt);
	    	lines.close(); writer.close();
	    	outf.setFileSize(Files.size(outfP));
	    // обработка возможных ошибок
	    } catch (IOException e) {
    		System.out.println(SrvLib.getErrPromt(mPrmMsg)
    				+ "Ошибка ввода/вывода при инициализации \n"
    				+ "входного " + inf.getFileName() + " ,\n"
    				+ "или выходного " + outf.getFileName() + "\nфайлов.");
    		e.printStackTrace();
    		interrupt();
	    }
	}
	
	/*
	* Метод записи одной "сырой" строки в буфер вывода <wr>.
	* Возвращает количество форматных-строк "ушедших" в буфер вывода <wr>
	* Аргументы:
	*		<String line> 		- входная (не форматированная строка);
	*		<BufferedWriter wr> - выходной буфер;
	*/
	private long writeLine(String line, BufferedWriter wr) {
		long count = 0;
		String trimStr = "";
		try {
			for (String dataItem : line.split(rul)) {
				trimStr = dataItem.trim();
				if ((trimStr.length() > 0) && (trimStr.matches(rer))) {
					wr.write(trimStr + "\r\n");
					count++;
				}
			}
			return count;
		} catch (IOException e) {
	    		System.out.println(SrvLib.getErrPromt(mPrmMsg)
	    				+ "Ошибка ввода/вывода при записи в файл:\n"
	    				+ outf.toString() + " .");
	    		e.printStackTrace();
	    		return outLineCnt;
		}
	}

}		// класс
