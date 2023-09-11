/*
 * Класс решает задачу: подготовки входных данных для утилиты сортировки.
 * Основной класс утилиты командной строки (CLI), выполняющей подготовку входных данных (файлов),
 * к требованиям ТЗ (Shift) для их последующей сортировки методом слияния.
 * 
 * Автор: Карышев Е.Н., сентябрь 2023 года.
 * 
 * Подготовка входных данных заключается в обработке текстового файла
 * произвольного формата путем приведения его к формату слов, каждое
 * из которых записано на отдельной строке и не содержит пробельных символов.
 * 
 * Подробности, в т.ч. о формате командной строки, см. README.md-файл.
 */

package prepare_text_data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import merge_sort.SrvLib;
import merge_sort.FileList;
import merge_sort.FileStreamDescr;
import merge_sort.MergeConfig;
import merge_sort.StoreConfig;


public class TsfDataPrepare {

	static MergeConfig curConfig;								// объект текущей конфигурации работы утилиты
	static StoreConfig fileConf;								// объект сохранения конфигурации
	static String cfgFileName 	= "TsfMergeSortConfig.cfg";		// имя файла, содержащего конфигурационные настройки утилиты
	static String pathToConfigFile;								// путь к местоположению конфигурационного файла
	static String mPrmMsg		= SrvLib.getClassName();		// префикс сообщений из данного класса
	// регулярное выражение для выделения в соответствии с ТЗ слов из текста произвольного формата
	static String wrdRegExp		= "\\s+|[\\x22(“]+(?=[\\S])|[;,.!?:…)“\\x22\\x27]{1,3}(?=[\\s+$])";
	
	// список коротких имен входных текстовых файлов (без путей)
	static List<String> inFile 				= new ArrayList<>();
	// список дескрипторов входных текстовых файлов, для чтения
	static List<FileStreamDescr> inFileDsc  = new ArrayList<>();
	// список дескрипторов выходных текстовых файлов, для записи
	static List<FileStreamDescr> outFileDsc = new ArrayList<>();
	// список потоков преобразования файлов
	static ArrayList<Thread> rawThreads 	= new ArrayList<>();
	

	// основной рабочий метод
	public static void main(String[] args) throws IOException {
		
		// отображение заголовка утилиты
	    System.out.println(		
	    		"\n---------------------------------------------------------------------------------------------\n"
	    		+ "Утилита подготовки входных тестовых данных для последующей сортировки методом слияния (" + mPrmMsg + ").\n"
	    		+ "Автор: Карышев Е.Н., сентябрь 2023 года.\n"
	    		+ "Изготовлено: Для участия в программе обучения \"Shift\".\n"
	    		+ "---------------------------------------------------------------------------------------------\n");

	    // определение рабочей конфигурации утилиты
	    try {					
	    	curConfig = new MergeConfig();
			pathToConfigFile = curConfig.getAbsBasePath();
			fileConf = new StoreConfig(curConfig, pathToConfigFile + cfgFileName);
			String[] more = {};
			if (Files.exists(Path.of(pathToConfigFile + cfgFileName, more))) {
				curConfig = fileConf.fromFileRestore();
			} else fileConf.storeConfig();
		    SrvLib.initFolders(curConfig);				// инициализация всех рабочих каталогов
		} catch (Exception e) {
			System.out.println(SrvLib.getErrPromt(mPrmMsg) 
					+ " Возникла ошибка при определении рабочей конфигурации утилиты.\n");
			e.printStackTrace();
		}
	    
	    // обработка аргументов командной строки и отображение статистики по входу
	    if (!SrvLib.cliArgsHanding(args, curConfig, inFile)) {
	    	System.out.println(SrvLib.getErrPromt(mPrmMsg)
	    				+ "Некорректно заданы входные параметры утилиты.\n");
			System.exit(0);
		}
	    // проверка фактического наличия файлов входных данных
	    FileList inFT = new FileList(curConfig);
	    List<String> bdF = inFT.isFilesReallyExist(inFile, curConfig.getRelRawDataPath());
	    if (!bdF.isEmpty()) {
	    	bdF.forEach(fl -> {
		    	System.out.println(SrvLib.getErrPromt(mPrmMsg)
	    				+ "Не найден входной файл: " + SrvLib.getFailStart()
	    				+ fl + SrvLib.getConEnd());
		    });
	    	System.out.println(SrvLib.getErrPromt(mPrmMsg) + "Работа утилиты будет аварийно завершена!\n"
	    				+ SrvLib.getConEnd());
			System.exit(2);
	    }
	//  отображение статистики по входным параметрам и данным
    	SrvLib.showHandingInfo(curConfig, inFile);
	    System.out.print("-FOR START:  Если всё верно, то "
	    				+ SrvLib.getBlkStart() + "нажмите <Enter> "
	    				+ SrvLib.getConEnd()   + " для продолжения.\n"
	    				+ "-FOR CANCEL: Если хотите прервать исполнение нажмите <Control+C>.\n");
	    System.in.read();							// ждем пользователя ... 
	    System.out.println("---------------------------------------------------------------------------------------------\n");

	    // обработка каждого входного файла в отдельном потоке исполнения
	    System.out.println("Обработка входных файлов: " + SrvLib.getGoodStart()
	    				+ inFile + SrvLib.getConEnd()+ " началась.");
	    inFileDsc.clear(); outFileDsc.clear(); rawThreads.clear();	// очистка всех списков
	    inFile.forEach(fl -> {
	    	Path inFNP 	= Paths.get(curConfig.getAbsBasePath() + curConfig.getRelRawDataPath() + fl);
	    	Path outFNP = Paths.get(curConfig.getAbsBasePath() + curConfig.getRelPrepDataPath() + fl);
	    	FileStreamDescr inFD = new FileStreamDescr(inFNP, curConfig);
	    	inFD.setRegExpRule(wrdRegExp);
	    	FileStreamDescr outFD = new FileStreamDescr(outFNP.toString(),
	    				0, 0, 0, curConfig.isDataType(), curConfig.isSortOrder());
	    	inFileDsc.add(inFD); outFileDsc.add(outFD);
	    	// каждый входной файл обрабатываем в индивидуальном потоке
	    	FileConvert convThread = new FileConvert(inFD, outFD);
	    	rawThreads.add(convThread);
	    	convThread.setName("Процесс конвертации файла: " + SrvLib.fileNameExtract(inFD.getFileName()));
	    	convThread.start();
	    });
	    
	    // выдача статистики по результатам обработки входных файлов
	    SrvLib.threadStat(rawThreads, mPrmMsg);
	    System.out.println("\nСТАТИСТИКА ПО РЕЗУЛЬТАТУ РАБОТЫ УТИЛИТЫ:\n"
	    			+ "---------------------------------------------------------------------------------------------\n");
	    for(int i = 0; i < inFileDsc.size(); i++) {
	    	System.out.println(mPrmMsg 
	    			+ "-INFO: Во входном файле " + SrvLib.getGoodStart() 
	    			+ SrvLib.fileNameExtract(inFileDsc.get(i).getFileName()) + SrvLib.getConEnd()
	    			+ " содержится " + SrvLib.getNormalStart() + inFileDsc.get(i).getFileTotalLineCount()
	    			+ SrvLib.getConEnd() + " строк;\n" 
	    			+ mPrmMsg + "-INFO: Размер этого входного файла составляет " + SrvLib.getNormalStart()
	    			+ inFileDsc.get(i).getFileSize() + SrvLib.getConEnd() + " байт;");
	    	System.out.println(mPrmMsg 
	    			+ "-INFO: В выходном файле " + SrvLib.getCStart() 
	    			+ SrvLib.fileNameExtract(outFileDsc.get(i).getFileName()) + SrvLib.getConEnd()
	    			+ " полученном, из \n входного файла "
	    			+ SrvLib.fileNameExtract(inFileDsc.get(i).getFileName())
	    			+ ", содержится " + SrvLib.getNormalStart() + outFileDsc.get(i).getFileTotalLineCount()
	    			+ SrvLib.getConEnd() + " строк;"
	    			+ ((outFileDsc.get(i).getFileSize() > 0) 
	    				? ("\n" + mPrmMsg + "-INFO: Размер выходного файла составляет " + SrvLib.getNormalStart()
	    					+ outFileDsc.get(i).getFileSize() + SrvLib.getConEnd() + " байт.\n")
	    				: (" Выходной файл уже существует, его размер не определялся.\n")));
	    }
    	System.out.println("---------------------------------------------------------------------------------------------\n"
        		+ mPrmMsg + "-INFO: " + SrvLib.getNormalStart() + "РАБОТА УЛИЛИТЫ УСПЕШНО ЗАВЕРШЕНА!\n"
    			+ SrvLib.getConEnd());

	}		// main-метод
	
}		// класс
