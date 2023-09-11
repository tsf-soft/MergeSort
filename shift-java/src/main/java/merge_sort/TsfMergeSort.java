/*
 * Основной класс утилиты командной строки (CLI), выполняющей сортировку слиянием
 * входных данных (файлов).
 * 
 * Автор: Карышев Е.Н., сентябрь 2023 года.
 * 
 * Сортировка слиянием выполняется над данными (файлами), удовлетворяющими
 * требованиям ТЗ (Shift), а именно:
 * 		- входные файлы представлены в текстовом формате;
 * 		- в одной строке файла содержится одно слово (последовательность
 * 		символов не содержащая пробельных символов);
 * 		- данные во входных файлах упорядочены (отсортированы);
 * 		- файлов для слияния может быть несколько.
 * 
 * Подробности, в т.ч. о формате командной строки, см. README.md-файл.
 */

package merge_sort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;


public class TsfMergeSort {
	
	static MergeConfig curConfig;								// объект текущей конфигурации работы программы
	static StoreConfig fileConf;								// объект сохранения конфигурации
	static String cfgFileName 	= "TsfMergeSortConfig.cfg";		// имя файла, содержащего конфигурационные настройки программы
	static String pathToConfigFile;								// путь к местоположению конфигурационного файла
	static String mPrmMsg		= SrvLib.getClassName();		// префикс сообщений из этого метода
	
	// списки имен входных и выходных файлов
	static List<String> outFile = new ArrayList<>();			// список имен выходных файлов процессов
	static List<String> inFile 	= new ArrayList<>();			// список имен входных файлов процессов
	// списки дескрипторов входных и выходных файлов
	static ArrayList<FileStreamDescr> inFileDesc 	= new ArrayList<>();// список дескрипторов входных файлов
	static ArrayList<FileStreamDescr> outFileDesc 	= new ArrayList<>();// список дескрипторов выходных файлов
	// списки потоков выполнения процедуры разделения/слияния
	static ArrayList<Thread> shrdThreads;						// список потоков разделения
	static ArrayList<Thread> mergThreads	= new ArrayList<>();// список потоков слияния
	
	// основной метод
	public static void main(String[] args) throws IOException {

		// отображение заголовка программы
	    System.out.println(		
	    		"\n---------------------------------------------------------------------------------------------\n"
	    		+ "Утилита сортировки файлов методом слияния (" + mPrmMsg + ").\n"
	    		+ "Автор: Карышев Е.Н., сентябрь 2023 года.\n"
	    		+ "Изготовлено: Для участия в программе обучения \"Shift\".\n"
	    		+ "---------------------------------------------------------------------------------------------\n");

	    // определение рабочей конфигурации утилиты
	    try {					
	    	curConfig = new MergeConfig();
			pathToConfigFile = curConfig.getAbsBasePath();
			fileConf = new StoreConfig(curConfig, pathToConfigFile + cfgFileName);
			if (Files.exists(Paths.get(pathToConfigFile + cfgFileName))) {
				curConfig = fileConf.fromFileRestore();
			} else fileConf.storeConfig();
		    SrvLib.initFolders(curConfig);			// инициализация всех рабочих каталогов
		} catch (Exception e) {
			System.out.println(SrvLib.getErrPromt(mPrmMsg) 
					+ "Возникла ошибка при определении рабочей конфигурации утилиты.\n");
			e.printStackTrace();
		}
	    
	    // обработка аргументов командной строки
	    inFile.clear();
	    if (!SrvLib.cliArgsHanding(args, curConfig, inFile, outFile)) {
	    	System.out.println(SrvLib.getErrPromt(mPrmMsg)
	    			+ "Некорректно заданы входные параметры утилиты.\n");
			System.exit(0);
		}
	    // проверка фактического наличия файлов входных данных
	    FileList inFT = new FileList(curConfig);
	    List<String> bdF = inFT.isFilesReallyExist(inFile, curConfig.getRelInFilePath());
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
    	SrvLib.showHandingInfo(curConfig, inFile, outFile);
	    System.out.print("-FOR START:  Если всё верно, то "
	    				+ SrvLib.getBlkStart() + "нажмите <Enter> "
	    				+ SrvLib.getConEnd()   + " для продолжения.\n"
	    				+ "-FOR CANCEL: Если хотите прервать исполнение нажмите <Control+C>.\n");
	    System.in.read();							// ждем пользователя ... 
	    System.out.println("---------------------------------------------------------------------------------------------\n");

	    // подготовка к сортировке (разделение)
	    String fP = "";								// имя входного (разделяемого) файла
	    for (int inFileNmb  = 0; inFileNmb < inFile.size(); inFileNmb++) {
		    // проверка наличия входного файла, его свойств и параметров
	    	fP = curConfig.getAbsBasePath() + curConfig.getRelInFilePath() + inFile.get(inFileNmb);
	    	FileStreamDescr inFlDsc = new FileStreamDescr(Paths.get(fP), curConfig); 	
	    	System.out.println("\n"
	    			+ mPrmMsg + "-INFO: Во входном файле "+ SrvLib.getCStart() + inFile.get(inFileNmb)
	    			+ SrvLib.getConEnd() + " содержится " + inFlDsc.getFileTotalLineCount() + " строк.\n"
	    			+ mPrmMsg + "-INFO: Размер этого файла составляет "+ inFlDsc.getFileSize() + " байт.");
	    	
	    	// выделение фрагментов из входного файла в индивидуальных потоках
	    	ThreadList sL = new ThreadList(curConfig, inFlDsc, inFileNmb);
	    	shrdThreads = sL.shardingList();
	    	shrdThreads.forEach(Thread::start);
			// статистика по результату разделения входного файла
		    SrvLib.threadStat (shrdThreads, mPrmMsg);
	    }   
	    // статистика по результатам разделения
	    long fC = SrvLib.tmpFileCount(curConfig);
    	if ((fC % 2) != 0) {		// количество промежуточных файлов-фрагментов должно быть четным
    		SrvLib.stubFile(inFile.size(), 0);
    	}
    	if (fC < 2) {
    		System.out.println(SrvLib.getErrPromt(mPrmMsg)
    				+ "Недостаточно входных файлов для выполнения их сортировки слиянием.\n");
    		System.exit(2);
    	} else System.out.println("\n"
    			+ mPrmMsg + "-INFO: Всего, для сортировки слиянием, подготовлено " 
    			+ fC + " файлов нижнего уровня разделения.\n");

    	// собственно сортировка слиянием
    	ThreadList mg = new ThreadList(curConfig);
    	mg.mergingRun();
   	    // перемещение результирующего файла в предназначенный для него каталог
    	fP = curConfig.getAbsBasePath() + curConfig.getRelOutFilePath();
	    try {
	        Files.move(	Paths.get(mg.outFlDsc.getFileName()),
	        			Paths.get(fP + outFile.get(0)),
	        			StandardCopyOption.REPLACE_EXISTING);
	    } catch (Exception e) {
	    	System.out.println(SrvLib.getErrPromt(mPrmMsg)
	    			+ "Ошбка при создании результирующего файла:\n" + outFile.get(0) + ".\n");
	        e.printStackTrace();
	    }
	    //  отображение статистики по результатам сортировки
	    SrvLib.showHandingInfo(mg.outFlDsc, fP, outFile.get(0), mPrmMsg);
	}		// метод main

}		// класс
