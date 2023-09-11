/*
 * Класс содержит статические методы общего назначения используемые в реализации утилиты.
 * Статический класс-библиотека методов общего назначения, используемых
 * в реализации утилиты. Все методы условно подразделены на 3 категории:
 * 	- методы работы с командной строкой (аргументами CLI);
 * 	- методы работы с файловой системой;
 * 	- информационные методы (выдают логи в порт стандартного вывода (tty0)). 
 * 
 * Автор: Карышев Е.Н., сентябрь 2023 года.
 * 
 * Подробности, в т.ч. о формате командной строки, см. README.md-файл.
 */

package merge_sort;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


// utility-класс имеющий только статические, служебные методы общего назначения/использования
public class SrvLib {
	
//	// ESC-последовательности "раскрашенного" консольного вывода
//	// для терминалов, поддерживающих ANSI-режим
//	private static final String C_START1	= "\033[0;32;1m";
//	private static final String C_START2 	= "\033[0;36;1m";
//	private static final String C_START3	= "\033[1;35;47m ";
//	private static final String C_START4 	= "\033[0;33;1m";
//	private static final String C_START5 	= "\033[2;33;1m";
//	private static final String C_START6 	= "\033[4;33;1m";
//	private static final String C_FAIL		= "\033[1;31;1m";
//	private static final String BLK_START	= "\033[15;37;46m ";
//	private static final String C_END 		= "\033[0m";
	
	// ESC-последовательности 
	// для терминалов, НЕ поддерживающих ANSI-режим
	private static final String C_START1	= "";
	private static final String C_START2 	= "";
	private static final String C_START3	= " ";
	private static final String C_START4 	= "";
	private static final String C_START5 	= "";
	private static final String C_START6 	= "";
	private static final String C_FAIL		= "";
	private static final String BLK_START	= " ";
	private static final String C_END 		= "";

	
// =================================== геттеры и конструктор ===================================
	// приватный конструктор, для сокрытия неявного общедоступного конструктора
	// которого не должно быть у класса имеющего только статические методы
	private SrvLib() {
	}
	// ESC-последовательности управления началом консольного вывода
	public static String getBlkStart() {
		return BLK_START;
	}
	public static String getGoodStart() {
		return C_START1;
	}
	public static String getNormalStart() {
		return C_START2;
	}
	public static String getFailStart() {
		return C_FAIL;
	}
	public static String getAltStart() {
		return C_START4;
	}
	public static String getCStart() {
		return C_START5;
	}
	// ESC-последовательность управления концом консольного вывода
	public static String getConEnd() {
		return C_END;
	}
	
	
// =================================== работа с командной строкой ===================================
	// обработка массива строк представленных в формате CLI,
	// данные:
	// 		<String[] args>		- текстовый массив входных аргументов
	//		<MergeConfig cfg>	- указатель глобальной конфигурации
	// 		<List<String> inF>	- список имен входных файлов
	// 		<String outF>		- имя выходного файла
	public static boolean cliArgsHanding(String[] args, MergeConfig cfg, List<String> inF, List<String> outF) {
		if (args.length < 2) return false;					// достаточно-ли входных аргументов
		for (int i = 0; i < args.length; i++) {
	    	if (i != 0) System.arraycopy(args, 1, args, 0, args.length-1);
	    	if ((!cliKeys(args[0], cfg)) && (args[0].toLowerCase().endsWith(cfg.getFileExt()))) {
		    	if (outF.isEmpty()) outF.add(fileNameExtract(args[0]));
		    		else inF.add(fileNameExtract(args[0]));
		    }
	    }
		return !(outF.isEmpty() || inF.isEmpty());
	}
	// перегруженный вариант предыдущего метода
	public static boolean cliArgsHanding(String[] args, MergeConfig cfg, List<String> inF) {
		if (args.length < 1) return false;					// достаточно-ли входных аргументов
		for (int i = 0; i < args.length; i++) {
	    	if (i != 0) System.arraycopy(args, 1, args, 0, args.length-1);
	    	if ((!cliKeys(args[0], cfg)) && (args[0].toLowerCase().endsWith(cfg.getFileExt())))
	    		inF.add(fileNameExtract(args[0]));
	    }
		return !(inF.isEmpty());
	}
	
	// поиск ключей конфигурации
	private static boolean cliKeys (String arg, MergeConfig cfg) {
    	switch (arg) {
			case "-i": 	cfg.setDataType(false);		break;	// будем сортировать целые числа
			case "-s":	cfg.setDataType(true);		break;	// будем сортировать строки
			case "-a":	cfg.setSortOrder(false);	break;	// будем сортировать по возрастанию
			case "-d":	cfg.setSortOrder(true);		break;	// будем сортировать по убыванию
			case "-t2":	cfg.setThreadPoolCnt(2);	break;	// задействуем 2-параллельных потока
			case "-t4":	cfg.setThreadPoolCnt(4);	break;	// задействуем 4-параллельных потока
			case "-t8":	cfg.setThreadPoolCnt(8);	break;	// задействуем 8-параллельных потоков
			case "-t16":cfg.setThreadPoolCnt(16);	break;	// задействуем 16-параллельных потоков
			case "-t32":cfg.setThreadPoolCnt(32);	break;	// задействуем 32-параллельных потока
			default:	return false;
	    }
    	return true;
	}

	
// =================================== методы работы с файловой системой ===================================
	// создание пустого файла-заглушки в промежуточном каталоге
	// для обеспечения корректной работы алгоритма
	public static void stubFile(int nmbF, int nmbS) {
		String addFileName = getOutFN(nmbF, nmbS);
		try (PrintWriter writer = new PrintWriter(addFileName, Charset.defaultCharset());
				) {
			writer.println("\r\n");
		} catch (IOException e) {
    		System.out.println(SrvLib.getErrPromt(getClassName()) 
    				+ "Ошибка ввода/вывода при создании дополняющего файла: "
    				+ addFileName + ".");
    		e.printStackTrace();
	    }
	}
	
	// инициализация рабочих областей (каталогов), определенных
	// текущей конфигурацией, при их отсутствии - создание этих
	// областей (каталогов) и очистка каталога промежуточных файлов
	public static void initFolders(MergeConfig cfg) {
		String dirPath1 = "любой";
		String dirPath2 = "подготовленный";
		FileList fl = new FileList(cfg);
		FileList f2 = new FileList(cfg);
		try {
			// создание каталогов рабочих файлов если их нет,
			// существующие каталоги не удаляются
			dirPath1 = cfg.getAbsBasePath() + cfg.getRelRawDataPath();
			Files.createDirectories(Paths.get(dirPath1));	// создание каталога "сырых" файлов
			dirPath2 = cfg.getAbsBasePath() + cfg.getRelPrepDataPath();
			Files.createDirectories(Paths.get(dirPath2));	// создание каталога подготовленных файлов
			dirPath1 = cfg.getAbsBasePath() + cfg.getRelInFilePath();
			Files.createDirectories(Paths.get(dirPath1));	// создание каталога входных файлов
			fl.dirFilesMove(dirPath2, dirPath1);			// перенос подготовленных файлов во входные
			dirPath1 = cfg.getAbsBasePath() + cfg.getRelTmpFilePath();
			Files.createDirectories(Paths.get(dirPath1));	// создание каталога промежуточных файлов
			f2.dirFilesDelete(dirPath1);					// полная очистка каталога промежуточных файлов 
			dirPath1 = cfg.getAbsBasePath() + cfg.getRelOutFilePath();
			Files.createDirectories(Paths.get(dirPath1));	// создание каталога выходных файлов
		} catch (IOException e) {
    		System.out.println(SrvLib.getErrPromt(getClassName()) 
    				+ "Ошибка ввода/вывода при создании каталога: " + dirPath1);
    		e.printStackTrace();
	    } 
	}
	
	// количество файлов-фрагментов в промежуточном каталоге
	public static long tmpFileCount(MergeConfig cfg) {
		Path tPh = Paths.get(cfg.getAbsBasePath() + cfg.getRelTmpFilePath());
		try (Stream<Path> fPS = Files.walk(tPh).parallel()
					.filter(p -> p.toFile().isFile() && p.toFile().getName().endsWith(cfg.getFileExt()));) {
			return fPS.count();
		} catch (IOException e) {
    		System.out.println(SrvLib.getErrPromt(getClassName()) 
    				+ "Ошибка ввода/вывода при чтении каталога: " + tPh + ".");
			e.printStackTrace();
			return 0;
		}
	}
	
	// извлечение имени файла из относительного/абсолютного пути
	public static String fileNameExtract(String fnwp) {
		String[] tokens = fnwp.split("[\\\\|/]");
		return tokens[tokens.length - 1];
	}
	

	// =================================== информационные методы ===================================
	// выдача строкового имени текущего активного класса (для статистики и логов)
	public static String getClassName() {
	    String className = Thread.currentThread().getStackTrace()[2].getClassName();
	    int lastIndex = className.lastIndexOf('.');
	    return className.substring(lastIndex + 1);
	}
	
	// формирование уникального имени единичного файла для различных уровней разделения
	// 	первым уровнем разделения считаются файлы-шарды от одного входного файла
	//	вторым уровнем разделения считаются сами входные файлы, которые разделяются
	//	и первый, и второй уровни, имеют последовательные порядковые имена
	public static String getOutFN(int nmbF, int nmbS) {
		MergeConfig cfg = TsfMergeSort.curConfig;
		String s = String.format("%1$s%2$03d-%3$04d%4$s",
						cfg.getTmpFilePref(),	// префикс имени файла
						nmbF,					// nmbF - идентификатор первого уровня разделения
						nmbS,					// nmbS - идентификатор второго уровня разделения
						cfg.getFileExt());		// расширение имени файла
		return (cfg.getAbsBasePath() + cfg.getRelTmpFilePath() + s);
	}
	
	// выдача цветного префикса ошибки
	public static String getErrPromt(String cl) {
		return (C_FAIL + cl + "-ERROR: " + C_END);
	}

    // отображение статистики предстоящей обработки	для сортировки слиянием
	public static void showHandingInfo(MergeConfig cfg, List<String> inFile, List<String> outFile) {		
		System.out.println(getClassName() + "-INFO: Сформированы следующие входные параметры:\n"
			+ "---------------------------------------------------------------------------------------------\n"
		    + " -выполняем сортировку " 		+ C_START2 + ((cfg.isDataType())? "строковых" : "числовых") + C_END + " данных;\n"
		    + " -сортировка выполняется по "	+ C_START2 + ((cfg.isSortOrder())? "убыванию" : "возрастанию") + C_END + ";\n"
		    + " -выходной файл называется: " 	+ C_START1 + outFile.get(0)	+ C_END + ";\n"
		    + " -входные файлы для сортировки: "+ C_START1 + inFile 		+ C_END + ";\n"
		    + " -сортировка распараллелена на " + C_START2 + cfg.getThreadPoolCnt() + C_END + " потока(ов) выполнения;\n"
		    + " -каталог входных файлов:  " 	+ C_START3 + cfg.getAbsBasePath() + cfg.getRelInFilePath() 	+ " \n" + C_END 
		    + " -каталог выходных файлов: " 	+ C_START3 + cfg.getAbsBasePath() + cfg.getRelOutFilePath() + " \n"  + C_END 
		);
	}
    // отображение статистики предстоящей подготовки входных данных для сортировки слиянием
	public static void showHandingInfo(MergeConfig cfg, List<String> inFile) {		
		System.out.println(	getClassName() 	+ "-INFO: Сформированы следующие входные параметры:\n"
		    + "---------------------------------------------------------------------------------------------\n"
		    + " -выполняем подготовку \"сырых\" "
		    + C_START2 + ((cfg.isDataType())? "строковых" : "числовых") + C_END + " данных произвольного формата, "
		    + "для их последующей сортировки;\n"
		    + " -при подготовке данные не упорядочиваются и не преобразуются в числа (это выполняется на следующем этапе);\n"
		    + " -входные файлы с \"сырыми\" данными: " + C_START1 + inFile 	+ C_END 	+ ";\n"
		    + " -каждый из входных файлов будет обрабатываться в одном из " + C_START2 	+ inFile.size() + C_END + " параллельных потоков;\n"
		    + " -каталог входных файлов:  " + C_START3 + cfg.getAbsBasePath() + cfg.getRelRawDataPath()	+ " \n" + C_END
		    + " -каталог выходных файлов: " + C_START3 + cfg.getAbsBasePath() + cfg.getRelInFilePath() 	+ " \n" + C_END
		);
	}
	// отображение статистики предстоящей подготовки входных данных для сортировки слиянием
	public static void showHandingInfo(FileStreamDescr fDs, String fP, String fNm, String pMsg) {
		System.out.println("\n\n---------------------------------------------------------------------------------------------\n"
    		+ pMsg + "-INFO: " + C_START2 + "РАБОТА УЛИЛИТЫ УСПЕШНО ЗАВЕРШЕНА!" + C_END
			+ "\nОтсортированный слиянием файл: " + C_START2 + fNm + C_END
			+ ", находится в \nкаталоге: " + C_START2 + fP + C_END + ";"
			+ "\nФайл является " + C_START2 + ((fDs.dataType)? "строковым" : "числовым") + C_END + ";"
			+ "\nФайл отсортирован по " + C_START2 + ((fDs.sortOrder)? "убыванию" : "возрастанию") + C_END + ";"
			+ "\nВ файле содержится: " + C_START2 + fDs.getFileTotalLineCount() + C_END + " строк;"
			+ "\nРазмер файла составляет: " + C_START2 + fDs.getFileSize() + C_END + " байт."
			+ "\n---------------------------------------------------------------------------------------------\n"
		);
	}
    
    // отображение статистики по работе пула потоков
    public static void threadStat (ArrayList<Thread> th, String cp) {
    	th.stream().forEach(t-> {
			try {
				if (t.isInterrupted()) throw new InterruptedException();
				else {
					t.join();
					System.out.println(cp + "-INFO: " + getCStart() + t.getName() + getConEnd() + " завершен успешно!" );
				}
			} catch (InterruptedException ie) {
				System.out.println(getErrPromt(cp) + getAltStart() + t.getName() + getConEnd() + " прерван!");
			    t.interrupt();
			} catch (Exception e) {
				System.out.println(getErrPromt(cp) + getAltStart() + t.getName() + getConEnd()
						+ " не завершен из-за возникшей ошибки!");
				e.printStackTrace();
			}
		});
    }
	
	// выдача результирующей статистики по результатам записи в выходной файл
	public static void outInfo(	FileStreamDescr inFile,
								FileStreamDescr outFile,
								String prompt) throws IOException, SplitFileExeption {
		if (outFile.curLinesCount > 0) {
			 System.out.println(prompt
					 + "-INFO: Из файла " 	+ C_START6 + fileNameExtract(inFile.getFileName()) + C_END
					 + " в файл -> "  		+ C_START1 + outFile.getFileName() + C_END
					 + " успешно записано " + outFile.curLinesCount + " строк.");
		} else {
			riseSplEx(outFile, prompt);
		}
	}
	// перегруженный предыдущий метод
	public static void outInfo(	FileStreamDescr inFile1,
								FileStreamDescr inFile2, 
								FileStreamDescr outFile,
								String prompt) throws IOException, SplitFileExeption {
		if (outFile.curLinesCount > 0) {
			 System.out.println(prompt
					 + "-INFO: Из файлов " + C_START6 + fileNameExtract(inFile1.getFileName()) + C_END
					 + " и " + C_START6 + fileNameExtract(inFile2.getFileName()) + C_END + " в сумме,"
					 + "\n в выходной файл -> " + C_START1 + outFile.getFileName() + C_END + ","
					 + " успешно записано " + outFile.curLinesCount + " строк.\n"
					 + "Совпадающие во входных файлах строки в выходной файл не дублировались "
					 + "(записывались однократно).");
		} else {
			riseSplEx(outFile, prompt);
		}		
	}
	// обработка ситуации, когда в выходной файл ничего не записано
	private static void riseSplEx(FileStreamDescr outFile, String prompt) throws SplitFileExeption, IOException {
		Files.deleteIfExists(Paths.get(outFile.getFileName()));
		throw new SplitFileExeption (SrvLib.getErrPromt(prompt)
				+ "В выходной файл " + C_FAIL + outFile.getFileName() + C_END
				+ " ничего не записано!");
	}
	
    // упаковка строки в ссылочный целочисленный тип
    public static Integer intNmbConvert(String line) {
		Integer num;
    	try {
			num = Integer.parseInt(line);
		} catch (NumberFormatException e) {
			num = null;
		}
    	return num;
    }
 
}
