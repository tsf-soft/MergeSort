/*
 * Класс решает задачу: получение списка требуемых артефактов в заданном каталоге ОС.
 * Экземпляр класса содержит список имен всех файлов с требуемым расширением
 * в заданном каталоге.
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
import java.nio.file.StandardCopyOption;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class FileList {

	private List<String> lst;
	private List<String> badLst;
	private MergeConfig cfg;

	
	public FileList(MergeConfig curConfig) {
		lst = new ArrayList<>();
		badLst = new ArrayList<>();
		cfg = curConfig;
	}

	// поиск всех файлов в каталоге заданном <strPath> с расширением,
	// установленном в глобальной конфигурации <<TsfMergeSort.curConfig>>
	// выдает отсортированный список файлов, имеющихся в каталоге
	// и удовлетворяющих шаблону расширения
	public List<String> dirFileList(String strPath) {
		try (Stream<Path> filePathStream = Files.walk(Paths.get(strPath))
				.filter(path -> path.toString().toLowerCase().endsWith(cfg.getFileExt()))
				.sorted()) {
		    filePathStream.forEach(filePath -> this.lst.add(filePath.toString()));
			return this.lst;
		} catch (IOException e) {
			return this.lst;
		}
	}
	
	// Метод проверяет наличие всех файлов из списка <fLs>
	// в рабочем каталоге <wC>.
	// Результат: Все отсутствующие файлы включены в выходной список.
	public List<String> isFilesReallyExist (List<String> fLs, String wC) {
		badLst.clear();
		String pH = cfg.getAbsBasePath() + wC;
		fLs.forEach(fl -> {
					Path path = Paths.get(pH + SrvLib.fileNameExtract(fl));
					if (Files.notExists(path)) badLst.add(fl);
				});
		return badLst;
	}
	
	// удаление всех специфицированных файлов в каталоге
	public void dirFilesDelete(String strPath) {
		dirFileList(strPath);
		this.lst.forEach(filePath -> {
			try {
				Files.deleteIfExists(Paths.get(filePath));
			} catch (IOException e) {return;}
		});
	}
	
	// удаление всех специфицированных файлов в каталоге
	public void dirFilesMove(String inP, String outP) {
		dirFileList(inP);
		this.lst.forEach(filePath -> {
			try {
				Files.move(Paths.get(inP), Paths.get(outP), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {return;}
		});
	}
}
