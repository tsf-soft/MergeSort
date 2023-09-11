/*
 * Класс решает две задач: хранение конфигурации по умолчанию и использование внешней конфигурации.
 * Экземпляр класса позволяет:
 * 		- определять и хранить рабочую конфигурацию утилиты;
 * 		- сериализовать конфигурацию по умолчанию во внешний конфигурационный файл;
 * 		- загружать рабочую конфигурацию из внешнего конфигурационного файла;
 * 		- оперативно изменять параметры конфигурации и получать их значения.
 * Наличие изменяемой (внешней) конфигурации, позволяет пользователю настраивать
 * параметры работы утилиты под свои нужды и потребности.
 * Для маппинга данных в json-файлы и обратно, класс использует библиотеку Jackson.
 * 
 * Автор: Карышев Е.Н., сентябрь 2023 года.
 * 
 * Подробности, в т.ч. о формате командной строки, см. README.md-файл.
 */

package merge_sort;

import java.sql.Timestamp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter  
@AllArgsConstructor  
@NoArgsConstructor  
public class MergeConfig {

	private ObjectMapper objectMapper;

	// параметры среди обработки
	private boolean dataType  	= false; 	// тип обрабатываемых данных (по умолчанию "целое число")
	private boolean sortOrder 	= false; 	// тип сортировки выходных данных (по умолчанию "по возрастанию")
	private int threadPoolCnt 	= 4;		// количество параллельных потоков обработки (по умолчанию 4)
	private String tmpFilePref	= "A#";		// префикс имени промежуточного файла (нижний уровень разделения)
	private String fileExt		= "txt";	// расширение рабочих файлов
	private int minShardFactor	= 20;		// множитель минимального размера фрагмента разделения
	private int minShardSize	= 3000;		// минимальный размер фрагмента разделения в байтах
	private Timestamp dt;					// дата и время в современном формате	 

	// базовый каталог (абсолютный путь)
	private String absBasePath;
	// каталог хранения "сырых" (неформатированных) файлов  (относительный путь)
	private String relRawDataPath	= "data\\raw_data\\";				// "src\\main\\resources\\raw_data\\";
	// каталог хранения подготовленных данных (относительный путь)
	private String relPrepDataPath	= "data\\prep_data\\";				// "src\\main\\resources\\prep_data\\";
	// каталог хранения файлов входных данных (относительный путь)
	private String relInFilePath	= "data\\inp_data\\";				// "src\\main\\resources\\inp_data\\";		
	// каталог хранения файлов нижнего уровня разделения (относительный путь)
	private String relTmpFilePath	= "data\\temp_data\\";				// "src\\main\\resources\\temp_data\\";
	// каталог хранения выходных файлов (относительный путь)
	private String relOutFilePath	= "data\\out_data\\"; 				// "src\\main\\resources\\out_data\\";


	// конструктор (по умолчанию)
	public MergeConfig() {
		this.setDTCreation(new Timestamp(System.currentTimeMillis()));
		setAbsBasePath(System.getProperty("user.dir"));
		this.objectMapper = new ObjectMapper();
	}
	
// ============================= общие (внутренние) методы =============================
	//  изменение формата представления строки-пути
	private String reformPath(String inPh) {
		return inPh.replace("\\", "/");
	}
	//  добавление отсутствующих окончаний к строке-пути
	private String endingPath(String inPh, String holder) {
		String sEnd = "";
		inPh = ((inPh.length() == 0) || (inPh.isEmpty()))? holder : inPh;
		if (inPh.indexOf("\\") != -1) sEnd = "\\";
		else if (inPh.indexOf("/") != -1) sEnd = "/";  
		return (inPh.endsWith("\\") || inPh.endsWith("/")) ? inPh : (inPh + sEnd);
	}
	
// ============================= методы параметров среды обработки =============================
	/* работа с полем <dataType> - "тип данных"
	* возможны значения:
	* 	true - строковые данные.
	* 	false - числовые данные.
	*/ 
	public boolean isDataType() {
		return dataType;
	}
	public void setDataType(boolean dataType) {
		this.dataType = dataType;
	}
	
	/* работа с полем <sortOrder> - "направление сортировки"
	* возможны значения:
	* 	true - сортировка по убыванию.
	* 	false - сортировка по возрастанию.
	*/ 
	public boolean isSortOrder() {
		return this.sortOrder;
	}
	public void setSortOrder(boolean sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	// работа с полем <threadPoolCnt> - количество
	// параллельных потоков задействованных в процессе
	// сортировки слиянием
	public int getThreadPoolCnt() {
		return threadPoolCnt;
	}
	public void setThreadPoolCnt(int threadPoolCnt) {
		threadPoolCnt = (threadPoolCnt < 2)? 4 	: threadPoolCnt;
		threadPoolCnt = (threadPoolCnt > 64)? 64: threadPoolCnt;
		int powLvl = 0;
		while (threadPoolCnt-1 > 0) {
			threadPoolCnt = (threadPoolCnt >> 1);
			powLvl++;
		}
		this.threadPoolCnt = (int) Math.pow(2, powLvl);
	}
	
	// работа с полем <tmpFilePref> - префикс имени 
	// промежуточных файлов-фрагментов
	public String getTmpFilePref() {
		return tmpFilePref.substring(0, tmpFilePref.indexOf("#")+1);
	}
	public void setTmpFilePref(String tmpFilePref) {
		tmpFilePref = ((tmpFilePref.length() == 0) || (tmpFilePref.isEmpty()))
				? "A#" : tmpFilePref;
		this.tmpFilePref = (tmpFilePref.endsWith("#"))
				? tmpFilePref : (tmpFilePref + "#");
	}
	
	// работа с полем <fileExt> - расширение имени 
	// рабочих файлов
	public String getFileExt() {
		return "." + fileExt;
	}
	public void setFileExt(String fileExt) {
		this.fileExt = ((fileExt.length() == 0)
				|| (fileExt.isEmpty())
				|| !(fileExt.matches("^[.]?[A-Za-z]+")))
				? "txt" : minifingExt(fileExt);
	}
	private String minifingExt(String inStr) {
		return ((inStr.indexOf(".") == -1)
				? inStr.toLowerCase()
				: inStr.substring(1, inStr.length()).toLowerCase());
	}
	
	// работа с полем <minShardFactor> - множитель минимального 
	// количества рабочих строк для имеющегося пула потоков
	// которые не целесообразно разделять на фрагменты
	// (если меньше этого количества строк, то работа выполняется
	// одним рабочим процессом (без задействования пула процессов)
	public int getMinShardFactor() {
		return minShardFactor;
	}
	public void setMinShardFactor(int minShardFactor) {
		this.minShardFactor = ((minShardFactor < 10) || (minShardFactor > 300))
				? 20 : minShardFactor;
	}
	
	// работа с полем <minShardSize> - минимальный размер (байт) 
	// файла для имеющегося пула потоков
	// которые не целесообразно разделять на фрагменты
	// (если размер меньше, то работа выполняется
	// одним рабочим процессом (без задействования пула процессов)	
	public int getMinShardSize() {
		return minShardSize;
	}
	public void setMinShardSize(int minShardSize) {
		this.minShardSize = ((minShardSize < 300) || (minShardSize > 5000))
				? 3000 : minShardSize;
	}
	
	// работа с полем <dt> - дата и время создания
	// экземпляра конфигурации программы
	public Timestamp getDTCreation() {
		return dt;
	}
	public void setDTCreation(Timestamp dt) {
		this.dt = dt;
	}
	
// ============================= методы конфигурации рабочих каталогов =============================
	// работа с полем <absBasePath> - абсолютный путь
	// к основному каталогу (каталогу запуска программы)
	public String getAbsBasePath() {
		return reformPath(this.absBasePath);
	}
	public void setAbsBasePath(String absBasePath) {
		this.absBasePath = endingPath(absBasePath, System.getProperty("user.dir"));
	}
	
	// работа с полем <relRawDataPath> - относительный путь
	// к каталогу "сырых"(не обработанных) данных
	public String getRelRawDataPath() {
		return reformPath(this.relRawDataPath);
	}
	public void setRelRawDataPath(String relRawDataPath) {
		this.relRawDataPath = endingPath(relRawDataPath, "raw_data/");
	}

	// работа с полем <relPrepDataPath> - относительный путь
	// к каталогу подготовленных (отформатированных под
	// требования сортировки) данных
	public String getRelPrepDataPath() {
		return reformPath(this.relPrepDataPath);
	}
	public void setRelPrepDataPath(String relPrepDataPath) {
		this.relPrepDataPath = endingPath(relPrepDataPath, "prep_data/");
	}

	// работа с полем <relInFilePath> - относительный путь
	// к каталогу входных (отформатированных под
	// требования сортировки) данных
	public String getRelInFilePath() {
		return reformPath(this.relInFilePath);
	}
	public void setRelInFilePath(String relInFilePath) {
		this.relInFilePath = endingPath(relInFilePath, "inp_data/");
	}

	// работа с полем <relTmpFilePath> - относительный путь
	// к каталогу промежуточных файлов (файлов-фрагментов
	// нижнего уровня разделения) из которых в дальнейшем
	// будет производится сортировка слиянием
	public String getRelTmpFilePath() {
		return reformPath(this.relTmpFilePath);
	}
	public void setRelTmpFilePath(String relTmpFilePath) {
		this.relTmpFilePath = endingPath(relTmpFilePath, "temp_data/");
	}

	// работа с полем <relOutFilePath> - относительный путь
	// к каталогу файлов-результатов сортировки слиянием
	public String getRelOutFilePath() {
		return reformPath(this.relOutFilePath);
	}
	public void setRelOutFilePath(String relOutFilePath) {
		this.relOutFilePath = endingPath(relOutFilePath, "out_data/");
	}
	
	// метод сериализации объекта в JSON-строку
	public String toJSONString() throws JsonProcessingException {
		return objectMapper
	        	.writerWithDefaultPrettyPrinter()
	        	.writeValueAsString(this);
	}	
}		// класс
