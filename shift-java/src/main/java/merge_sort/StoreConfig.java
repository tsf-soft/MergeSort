package merge_sort;

/*
 * Класс решает две задач: сохранение рабочей конфигурации в файл и получение внешней конфигурации из файла.
 * Экземпляр класса позволяет:
 * 		- сериализовать конфигурацию по умолчанию во внешний конфигурационный файл;
 * 		- загружать рабочую конфигурацию из внешнего конфигурационного файла;
 * Наличие изменяемой (внешней) конфигурации, позволяет пользователю настраивать
 * параметры работы утилиты под свои нужды и потребности.
 * 
 * Автор: Карышев Е.Н., сентябрь 2023 года.
 * 
 * Подробности, в т.ч. о формате командной строки, см. README.md-файл.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;


public class StoreConfig {

	private MergeConfig confObj;
	private String confFileName;

	// конструктор с параметрами
	public StoreConfig(MergeConfig confObj, String confFile) throws IOException {
		if ((confObj == null) || (confFile.isEmpty()) 
				|| (confFile.length() == 0) || !(confFile.endsWith(".cfg"))) {
			throw new IOException(
					SrvLib.getClassName()
					+ "-ERROR: Не корректно заданы параметры конфигурационного файла");
		}
		this.confFileName = confFile;
		this.confObj = confObj;
	}
	// сериализация объекта-конфигурации в json-файл с расширением ".cfg"
	public void storeConfig() throws IOException {
		try (FileWriter writer = new FileWriter(this.confFileName)) {
			writer.write(this.confObj.toJSONString());
		}
	}
	// десериализация json-файла с расширением ".cfg" в объект-конфигурации 
	public MergeConfig fromFileRestore() throws IOException {
		try (BufferedReader bufRd = new BufferedReader(new FileReader(this.confFileName))) {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(bufRd, MergeConfig.class);
		}
	}
}
