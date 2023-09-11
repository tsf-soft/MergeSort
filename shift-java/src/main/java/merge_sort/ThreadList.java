/*
 * Класс решает решает задачи распараллеливания основных рабочих процессов.
 * Экземпляр класса, в зависимости от задачи может выполнять:
 * 		- распараллеливание процесса разделения входного файла на заданное количество
 * 		параллельных потоков исполнения;
 * 		- распараллеливание процесса сортировки попарным слиянием файлов.
 * Использование параллельной обработки существенно ускоряет процесс.
 * 
 * Автор: Карышев Е.Н., сентябрь 2023 года.
 * 
 * Подробности, в т.ч. о формате командной строки, см. README.md-файл.
 */

package merge_sort;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class ThreadList {
	
	ArrayList<Thread> sTL;			// список результирующих потоков разделения
	ArrayList<Thread> mTL;			// список результирующих потоков слияния
	List<String> inFile;			// список имеющихся файлов для объединения
	MergeConfig cf;					// действующая конфигурация работы
	FileStreamDescr inFlDsc;		// дескриптор входного файла
	FileStreamDescr outFlDsc;		// дескриптор выходного, результирующего файла
	int inFileNmb;					// номер выходного файла в пакете разделения
	String prmMsg;					// префикс информационного сообщения

	// конструктор класса с параметрами
	public ThreadList(MergeConfig cf, FileStreamDescr inFlDsc, int inFileNmb) {
		this.sTL 		= new ArrayList<>();	
		this.cf			= cf;
	   	this.inFlDsc	= inFlDsc;
	   	this.outFlDsc	= null;
	   	this.inFileNmb	= inFileNmb;
	    this.prmMsg 	= SrvLib.getClassName();		
	}
	public ThreadList(MergeConfig cf) {
		this.mTL 		= new ArrayList<>();	
		this.cf			= cf;
	    this.prmMsg 	= SrvLib.getClassName();		
	}

    // подготовка списка дескрипторов-фрагментов для разделения входного файла
    public ArrayList<Thread> shardingList () {
    	// если файл маленький, то его не разделяем, а просто переписываем в выходной
    	sTL.clear();											// чистый список процессов
    	int tC = cf.getThreadPoolCnt();							// количество задействованных параллельных потоков
    	int realThreadCnt	= 1;								// число потоков, задействованных для разделения входного файла
    	long filesPartLines = inFlDsc.getFileTotalLineCount();	// размер фрагмента выделенного-промежуточного файла
    	if (filesPartLines > (tC * cf.getMinShardFactor())) {
	    	filesPartLines = (long) Math.ceil((double)inFlDsc.getFileTotalLineCount()/tC);
	    	realThreadCnt  = tC;
    	}
    	// формирование дескрипторов выходных файлов-фрагментов нижнего уровня
		for (int i = 1; i <= realThreadCnt; i++) {
			long startLine = (i == 1)? i : (i - 1) * filesPartLines + 1;
		    long endLine   = i * filesPartLines;
		    // формирование потокового дескриптора выделенного файла
		    outFlDsc = new FileStreamDescr(
		    		SrvLib.getOutFN(inFileNmb, i),		// имя будущего выделенного файла
	    			filesPartLines, startLine, endLine,
	    			cf.isDataType(), cf.isSortOrder());
		    FileShard mapTS = new FileShard(inFlDsc, outFlDsc);
		    mapTS.setName("Поток выделения файла: "+ SrvLib.fileNameExtract(outFlDsc.getFileName()));
		    sTL.add(mapTS);
		}
    	return sTL;
    }		// метод shardingList
    
    // подготовка списка дескрипторов-фрагментов для сортировки слиянием промежуточных файлов
    public void mergingRun () {
    	int outPref = 0;						// промежуточные переменные для формирования префикса
    	String sOutPref = ""; 								
    	FileList mfl = new FileList(cf);		// список имеющихся файлов для объединения
    	inFile = mfl.dirFileList(cf.getAbsBasePath() + cf.getRelTmpFilePath());
    	do {
    		// меняем префикс файлам следующего уровня
			outPref = SrvLib.fileNameExtract(inFile.get(0)).split("#")[0].charAt(0);
			outPref++; sOutPref = "" + ((char) outPref);
			cf.setTmpFilePref(sOutPref);
	    	mTL.clear();						// чистый список процессов
			// попарная сортировка слиянием файлов одного уровня
    		for (int inMrgNmb = 0; inMrgNmb < inFile.size(); inMrgNmb += 2) {
    			FileStreamDescr mInFile1 = new FileStreamDescr(Paths.get(inFile.get(inMrgNmb)), cf);
    			FileStreamDescr mInFile2 = new FileStreamDescr(Paths.get(inFile.get(inMrgNmb+1)), cf);
		    	FileStreamDescr mOutFile = new FileStreamDescr(
		    			SrvLib.getOutFN(inMrgNmb, 0),
		    			0, 0, 0, cf.isDataType(), cf.isSortOrder());
		    	MergeFiles mapTM = new MergeFiles(mOutFile, mInFile1, mInFile2);
				mapTM.setName("Поток слияния файлов: "
	    				+ SrvLib.fileNameExtract(mInFile1.getFileName())
	    				+ " и " + SrvLib.fileNameExtract(mInFile2.getFileName()));
				mTL.add(mapTM);
				mapTM.start();
    		}
    		// завершение слияния одного уровня
    		SrvLib.threadStat (mTL, prmMsg);
    		inFile.clear();
    		inFile = mfl.dirFileList(cf.getAbsBasePath() + cf.getRelTmpFilePath());
    	} while (inFile.size() > 1);
    	// фиксируем выходной файл и его параметры
    	this.outFlDsc = new FileStreamDescr(Paths.get(inFile.get(0)), this.cf);
    }		// метод mergingList
    
}			// класс
