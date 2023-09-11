/*
 * Класс решает задачу: создания и хранения списка "Дескрипторов файловых потоков".
 * Экземпляр класса содержит список "Дескрипторов файловых потоков".
 * Определение того, что такое "Дескриптор файлового потока"
 * (применительно) к данной реализации, - содержится в классе FileStreamDescr.
 * 
 * Автор: Карышев Е.Н., сентябрь 2023 года.
 * 
 * Подробности, в т.ч. о формате командной строки, см. README.md-файл.
 */

package merge_sort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;


public class FileDescriptorList {

	// формируемый список дескрипторов текстовых файлов
	private List<FileStreamDescr> fdl	= null;

	// конструктор класса (из файлового каталога)
	public FileDescriptorList(String strPath, MergeConfig cfg) {
		String fExt = cfg.getFileExt();
		try (Stream<Path> filePathStream = Files.walk(Paths.get(strPath))
				.filter(path -> path.toString().toLowerCase().endsWith(fExt))
				.sorted()) {
			// обход всех файлов с <расширением> fExt в каталоге <strPath>
		    filePathStream.forEach(filePath -> {
		    	FileStreamDescr fsd = new FileStreamDescr(filePath, cfg);
		    	this.fdl.add(fsd);
			});
		} catch (IOException e) {}
	}
	// перегруженный конструктор (из списка файлов)
	public FileDescriptorList(List<String> fln, MergeConfig cfg) {
		fln.sort((a, b) -> (a.compareTo(b)));
		// обход всех файлов в списке <fP>
		fln.forEach(filePath -> {
	    	FileStreamDescr fsd = new FileStreamDescr(Paths.get(filePath), cfg);
	    	this.fdl.add(fsd);
		});
	}
	
	// метод выдает указатель на список дескрипторов
	// всех текстовых файлов, находящихся в каталоге
	public List<FileStreamDescr> getFDList () {
		return fdl;
	}		// метод getFDList 

}			// класс
