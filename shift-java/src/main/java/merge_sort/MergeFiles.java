/*
 * Класс решает задачу сортировки слиянием входных файлов, содержимое которых удовлетворяет ТЗ.
 * Экземпляр класса позволяет выполнять сортировку путем попарного слияния входных файлов.
 * Поскольку процесс слияния выполняется в индивидуальном (параллельном) потоке, то
 * этот факт существенно ускоряет общую работу утилиты.
 * При слиянии, в зависимости от заданных параметров, выполняется четыре разновидности сортировки:
 * 		- сортировка по возрастанию числовых строк;
 * 		- сортировка по убыванию числовых строк;
 * 		- сортировка по лексикографическому возрастанию символьных строк;
 * 		- сортировка по лексикографическому убыванию символьных строк.
 * 
 * Автор: Карышев Е.Н., сентябрь 2023 года.
 * 
 * Подробности, в т.ч. о формате командной строки, см. README.md-файл.
 */

package merge_sort;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


// слияние отсортированных файлов с корректным содержимым
public class MergeFiles extends Thread {
	BufferedReader 	bfRd1;				// дескриптор "читателя" для первого файла
	BufferedReader 	bfRd2;				// дескриптор "читателя" для второго файла
	FileWriter 		wr;					// дескриптор "писателя"
	FileStreamDescr	outFl;				// дескриптор выходного файла
	FileStreamDescr	inFl1;				// дескриптор первого входного файла
	FileStreamDescr	inFl2;				// дескриптор второго входного файла
	String 			prmMsg;				// префикс информационного сообщения
    Integer 		num1;				// промежуточные числовое значения 1 для сортировки
    Integer 		num2;				// промежуточные числовое значения 2 для сортировки
    String cmnMsg1 = " при определении начальных условий слияния файлов.";
    String cmnMsg2 = " при слиянии файлов.";

    // конструктор класса с параметрами
	MergeFiles(FileStreamDescr outFile, FileStreamDescr inFile1, FileStreamDescr inFile2) {
      	try {
    		this.bfRd1 	= new BufferedReader(new FileReader(inFile1.getFileName()));
    		this.bfRd2 	= new BufferedReader(new FileReader(inFile2.getFileName()));
    		this.wr 	= new FileWriter(outFile.getFileName());
    		this.outFl	= outFile;
    		this.inFl1	= inFile1;
    		this.inFl2	= inFile2;
            this.prmMsg = SrvLib.getClassName();
    	} catch (FileNotFoundException e) {
    		System.out.println(SrvLib.getErrPromt(prmMsg)
    				+ "Не найден для слияния один из входных файлов: \n"
    				+ inFile1.getFileName() + " ,\n" + inFile2.getFileName() + " .");
    		closeAll();
    	} catch (IOException e) {
    		System.out.println(SrvLib.getErrPromt(prmMsg)
    				+ "Ошибка ввода/вывода" + cmnMsg1);
    		closeAll();
    		e.printStackTrace();
    	} catch (Exception e) {
    		System.out.println(SrvLib.getErrPromt(prmMsg)
    				+ "Возникла ошибка" + cmnMsg1);
    		closeAll();
    		e.printStackTrace();
    	}
	}

    @Override
    // основной метод сортировки слиянием
    public void run() {
    	try {
            String line1 = bfRd1.readLine();
            String line2 = bfRd2.readLine();
            // собственно сортировка
            while (line1 != null || line2 != null) {
        	    // когда один из аргументов пустой, - просто используем другой
        		if ((line1 == null) && (line2 != null))  {
        	        line2 = symbLine(line2, bfRd2, wr, inFl2);
        		} else if ((line1 != null) && (line2 == null)) {
        	    	line1 = symbLine(line1, bfRd1, wr, inFl1);
        	    // нужно проводить сравнение, оба аргумента не пустые
        		} else if (!outFl.dataType) {
        		// сортировка чисел
        			num1 = SrvLib.intNmbConvert(line1);
        			num2 = SrvLib.intNmbConvert(line2);
        			if (num1.compareTo(num2) == 0) {					// числа равны
        				line1 = digLine(line1, bfRd1, wr, inFl1);		// одно число записываем
        				line2 = bfRd2.readLine();						// второе число игнорируем
        			} else
        			if (((num1.compareTo(num2) > 0) && (inFl1.sortOrder))			// числа по убыванию
        				|| ((num1.compareTo(num2) < 0) && (!inFl1.sortOrder))) {	// числа по возрастанию
        	            line1 = digLine(line1, bfRd1, wr, inFl1);
        			} else
        			if (((num2.compareTo(num1) > 0) && (inFl1.sortOrder))			// числа по убыванию
                		|| ((num2.compareTo(num1) < 0) && (!inFl1.sortOrder))) {	// числа по возрастанию
        				line2 = digLine(line2, bfRd2, wr, inFl2);
        			}
        		// сортировка строк
        		} else {
        			if (line1.compareTo(line2) == 0) {					// строки равны
        				line1 = symbLine(line1, bfRd1, wr, inFl1);		// одну строку записываем
        				line2 = bfRd2.readLine();						// вторую строку игнорируем
        			} else
        			if (((line1.compareTo(line2) > 0) && (outFl.sortOrder))			// строки по убыванию
        				|| ((line1.compareTo(line2) < 0) && (!outFl.sortOrder))) {	// строки по возрастанию
        				line1 = symbLine(line1, bfRd1, wr, inFl1); 	
        			} else 
        			if (((line2.compareTo(line1) > 0) && (outFl.sortOrder))			// строки по убыванию
            			|| ((line2.compareTo(line1) < 0) && (!outFl.sortOrder))) {	// строки по возрастанию
        	            	line2 = symbLine(line2, bfRd2, wr, inFl2);	
        			}
        		}
            }		// блок while
            closeAll();
            // статистика сортировки слиянием
    		SrvLib.outInfo(inFl1, inFl2, outFl, prmMsg);
    		// удаление обработанных входных файлов
    		Files.deleteIfExists(Paths.get(inFl1.getFileName()));
    		Files.deleteIfExists(Paths.get(inFl2.getFileName()));
    	// обработка возможных исключений
    	} catch (IOException e) {
    		System.out.println(SrvLib.getErrPromt(prmMsg) + "Ошибка ввода/вывода" + cmnMsg2);
    		closeAll();
    		interrupt();
    		e.printStackTrace();
    	} catch (Exception e) {
    		System.out.println(SrvLib.getErrPromt(prmMsg) + "Возникла ошибка" + cmnMsg2);
    		closeAll();
    		interrupt();
    		e.printStackTrace();
        } 
    } 	// метод run

	
	// закрытие всех открытых ранее ресурсов
	private void closeAll() {
		try {
			bfRd1.close(); bfRd2.close(); wr.close();
		} catch (IOException e) {
			System.out.println(SrvLib.getErrPromt(prmMsg)
					+ "Ошибка ввода/вывода при закрытии потоков.");
			e.printStackTrace();
		}
	}
    
    // внутренние методы продвижения во входных строках, с записью
    private String symbLine(	String s, 
    							BufferedReader br,
    							FileWriter fw,
    							FileStreamDescr fd) throws IOException {
		s = s.trim();
		if (s.length() > 0) {
			fw.write(s + "\r\n");
			fd.curLinesCount++;
			this.outFl.curLinesCount++;
		}
    	return br.readLine();
    }
    private String digLine(		String s,
    							BufferedReader br,
    							FileWriter fw,
    							FileStreamDescr fd) throws IOException {
    	fw.write(s + "\r\n");
    	fd.curLinesCount++;
    	this.outFl.curLinesCount++;
    	return br.readLine();
	}  
}		// класс
