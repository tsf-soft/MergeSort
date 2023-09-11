/*
 * Класс решает решает задачу создания прикладного (пользовательского) исключения.
 * Класс определяет пользовательское исключение, которое может возникнуть при 
 * разделении входного файла на фрагменты.
 * 
 * Автор: Карышев Е.Н., сентябрь 2023 года.
 * 
 * Подробности, в т.ч. о формате командной строки, см. README.md-файл.
 */

package merge_sort;

// мое исключение, возникающее при попытке разделения файла 
@SuppressWarnings("serial")
public class SplitFileExeption extends Exception {
	public SplitFileExeption() { super(); }
	public SplitFileExeption(String message) { super(message); }
	public SplitFileExeption(String message, Throwable cause) { super(message, cause); }
	public SplitFileExeption(Throwable cause) { super(cause); }
}
