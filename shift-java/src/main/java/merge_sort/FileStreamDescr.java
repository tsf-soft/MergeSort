/*
 * Класс решает задачу: определения "Дескриптора файлового потока".
 * Экземпляр класса содержит необходимую информацию для обработки связанного
 * с ним файла, - как потока (последовательности) строк. Здесь хранятся
 * необходимы указатели на позиции в файле, а также флаги типов данных, 
 * типа сортировки и регулярное выражение, которому должно
 * соответствовать содержимое связанного с дескриптором файла.
 * Наличие дескриптора позволяет выполнять изолированную обработку файла
 * без перекрестных запросов.
 * 
 * Автор: Карышев Е.Н., сентябрь 2023 года.
 * 
 * Подробности, в т.ч. о формате командной строки, см. README.md-файл.
 */

package merge_sort;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

// Класс содержит все необходимы данные файла, участвующего в сортировке слиянием
public class FileStreamDescr {
	private String fileName;			// имя файла (возможно полное/неполное)
    long startLineNmb;					// номер-указатель начала области файла превращаемой в поток
    long endLineNmb;					// номер-указатель конца области файла превращаемой в поток
    long curLinesCount;					// текущей номер-указатель позиции строки в потоке
    private long totalLinesCount;		// общее количество строк в текстовом файле
    private long totalSize;				// размер файла в байтах
    boolean dataType;					// тип данных, содержащихся в файле (int=false/string=true)
    boolean sortOrder;					// тип сортировки установленный для файла (asc=false/desc=true)
    private String regExpRule;			// регулярное выражение, которому должны соответствовать данные в файле (согласно задания)

    // конструктор класса 
	public FileStreamDescr(String fileName,
								long totalLinesCount,
								long startLineNmb, long endLineNmb,
								boolean dataType, boolean sortOrder) {		
    	this.setFileName(fileName);
        this.startLineNmb 		= startLineNmb;
        this.endLineNmb 		= endLineNmb;
        this.dataType 			= dataType;
        this.sortOrder 			= sortOrder;
        this.totalLinesCount	= totalLinesCount;
        this.regExpRule 		= (dataType)? "\\S+" : "\\d+";
        setCurLinesCount(0);
        setFileTotalLineCount(0);
    }
	// перегруженный конструктор
	public FileStreamDescr(Path filePath, MergeConfig cfg) {
		if (Files.isRegularFile(filePath)) {
		// определение параметров файла
			try (Stream<String> lines = Files.lines(filePath, Charset.defaultCharset())) {
		        	this.fileName 		= filePath.toString();
		        	this.startLineNmb 	= 0;
		        	this.endLineNmb 	= 0;
		        	this.dataType 		= cfg.isDataType();
		        	this.sortOrder 		= cfg.isSortOrder();
		        	this.totalSize 		= Files.size(filePath);
		        	this.regExpRule 	= (this.dataType)? "\\S+" : "\\d+";
		            setCurLinesCount(0);
		        	setFileTotalLineCount(lines.count());
			} catch (IOException e) {
		        System.out.println(SrvLib.getErrPromt(SrvLib.getClassName())
		        		+ "Возникла ошибка при определении параметров файла "
		        		+ filePath + " .");
				e.printStackTrace();
			}
		}
	}
	
	// указатель текущей строки в потоке
	public long getCurLinesCount() {	
		return curLinesCount;
	}
	public void setCurLinesCount(long curLinesCount) {	
		this.curLinesCount = curLinesCount;
	}
	
	// имя файла
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	// размер файла в байтах
	public long getFileSize() {
		return totalSize;
	}
	public void setFileSize(long totalSize) {
		this.totalSize = totalSize;
	}

	// размер текстового файла в строках
	public long getFileTotalLineCount() {
		return totalLinesCount;
	}
	public void setFileTotalLineCount(long totalLinesCount) {
		this.totalLinesCount = totalLinesCount;
	}
	
	// регулярное выражение содержимого
	public String getRegExpRule() {
		return regExpRule;
	}
	public void setRegExpRule(String regExpRule) {
		this.regExpRule = regExpRule;
	}
}
