package zorg.gen.formula;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class GenFormula {

	private String _ftlFileName = "/templates/Formula.ftl";

	// 配置相关
	protected String pageEncoding = "UTF-8";
	protected Configuration configuration; // FreeMarker配置

	// 文件夹相关
	protected String packageName; // 输出包名
	protected String className; // 输出类名
	protected String targetDir; // 输出目标文件夹

	/**
	 * 创建和调整配置,这个在生命周期中只做一次
	 */
	public GenFormula(String packageName, String className, String targetDir) {

		this.packageName = packageName;
		this.className = className;
		this.targetDir = targetDir;
		configuration = new Configuration(Configuration.getVersion());
		configuration.setClassForTemplateLoading(this.getClass(), "");
		configuration.setEncoding(Locale.getDefault(), pageEncoding);
	}

	/**
	 * 根据srcName获取填充模板内容
	 * 
	 * @param srcName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getRootMap(String srcName) throws Exception {
		return loadFromXls(new File(srcName));

	}

	/**
	 * 加载Excel文件
	 * 
	 * @param file
	 * @return
	 */
	private Map<String, Object> loadFromXls(File file) {
		List<Map<String, Object>> formulaList = new ArrayList<>();
		try (Workbook wb = new XSSFWorkbook(file)) {
			Sheet sheet = wb.getSheetAt(0);

			Map<Integer, String> fieldMap = new HashMap<>();
			Row title = sheet.getRow(2);
			for (Cell cell : title) {
				int index = cell.getColumnIndex();
				String value = cell.getStringCellValue();
				fieldMap.put(index, value);
			}

			Set<Entry<Integer, String>> colEntrySet = fieldMap.entrySet();
			for (int i = 4; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				Map<String, Object> f = new HashMap<>();
				for (Map.Entry<Integer, String> entry : colEntrySet) {
					int colPos = entry.getKey();
					String colDesc = entry.getValue();
					Cell cell = row.getCell(colPos);
					int cellType = cell.getCellType();
					if (Cell.CELL_TYPE_NUMERIC == cellType) {
						String cellValue = String.valueOf(cell
								.getNumericCellValue());
						f.put(colDesc, cellValue);
					} else {
						String cellValue = cell.getStringCellValue();
						f.put(colDesc, cellValue);
					}
				}
				formulaList.add(f);
			}
		} catch (IOException | InvalidFormatException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		Map<String, Object> rootMap = new HashMap<>();
		rootMap.put("formulasList", formulaList);
		return rootMap;
	}

	/**
	 * 根据填充模板内容生成文件。
	 * 
	 * @param rootMaps
	 * @throws Exception
	 */
	public void genGlobalFile(Map<String, Object> rootMaps) throws Exception {
		rootMaps.put("packageName", packageName);
		rootMaps.put("className", className);
		String targetFileDir = this.targetDir
				+ packageName.replaceAll("\\.", "/") + "/";
		this.writeFile(targetFileDir, className + ".java", _ftlFileName,
				rootMaps);
	}

	/**
	 * 生成文件统一方法
	 * 
	 * @param fileName
	 * @param tempName
	 * @param rootMap
	 * @throws Exception
	 */
	private void writeFile(String targetFileDir, String fileName,
			String tempName, Object rootMap) throws Exception {
		System.out.println(targetFileDir + " " + fileName + " " + tempName
				+ "\n" + rootMap);
		/* 1，获取模板 */
		Template temp = configuration.getTemplate(tempName);
		temp.setEncoding(pageEncoding);

		// 判断目标文件夹不存在，则新建文件夹
		File dir = new File(targetFileDir);
		if (!dir.exists())
			dir.mkdirs();

		/* 2，将模板和数据合并，并生成文件 */
		String fileFullName = targetFileDir + fileName;
		System.out.println("-------开始生成" + fileFullName + "文件......------");

		File target = new File(fileFullName);
		Writer out = new OutputStreamWriter(new FileOutputStream(target),
				pageEncoding);
		temp.process(rootMap, out);
		out.flush();
		out.close();

		System.out.println("-------" + fileName + "文件生成完毕!-------\n");
	}

	/**
	 * GEN出文件。
	 * 
	 * @param args
	 *            包名、类名、输出文件夹、源文件名
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		GenFormula gen = new GenFormula(args[0], args[1], args[2]);
		gen.genGlobalFile(gen.getRootMap(args[3]));
	}
}
