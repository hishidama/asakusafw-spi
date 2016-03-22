package jp.hishidama.asakusafw_spi.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.asakusafw.runtime.io.ModelOutput;
import com.asakusafw.runtime.value.BooleanOption;
import com.asakusafw.runtime.value.ByteOption;
import com.asakusafw.runtime.value.Date;
import com.asakusafw.runtime.value.DateOption;
import com.asakusafw.runtime.value.DateTime;
import com.asakusafw.runtime.value.DateTimeOption;
import com.asakusafw.runtime.value.DateUtil;
import com.asakusafw.runtime.value.DecimalOption;
import com.asakusafw.runtime.value.DoubleOption;
import com.asakusafw.runtime.value.FloatOption;
import com.asakusafw.runtime.value.IntOption;
import com.asakusafw.runtime.value.LongOption;
import com.asakusafw.runtime.value.ShortOption;
import com.asakusafw.runtime.value.StringOption;

public abstract class ExcelWriter<T> implements ModelOutput<T> {
	private OutputStream stream;
	private Workbook workbook;
	private Map<String, IntOption> rowIndexMap = new HashMap<>();

	public void initialize(OutputStream stream) throws IOException {
		this.stream = stream;
		this.workbook = createWorkbook();
		initializeWorkbook(workbook);
	}

	protected Workbook createWorkbook() throws IOException {
		try (InputStream is = getTemplateExcelInputStream()) {
			if (is != null) {
				try {
					return WorkbookFactory.create(is);
				} catch (EncryptedDocumentException | InvalidFormatException e) {
					throw new IOException(e);
				}
			}
		}

		SpreadsheetVersion version = getSpreadsheetVersion();
		switch (version) {
		case EXCEL97:
			return new HSSFWorkbook();
		case EXCEL2007:
			return new XSSFWorkbook();
		default:
			throw new UnsupportedOperationException(MessageFormat.format("unsupported workbook. version={0}", version));
		}
	}

	protected InputStream getTemplateExcelInputStream() throws IOException {
		String templateFileName = getTemplateExcelFile();
		if (templateFileName == null) {
			return null;
		}

		InputStream is = getClass().getResourceAsStream(templateFileName);
		if (is != null) {
			return is;
		}

		String path;
		{
			Package pack = getClass().getPackage();
			if (pack != null) {
				path = pack.getName().replace('.', '/') + "/" + templateFileName;
			} else {
				path = templateFileName;
			}
		}
		throw new IOException(MessageFormat.format("templateExcelFile not found. file={0}", path));
	}

	/**
	 * テンプレートExcelファイル取得.
	 * <p>
	 * src/main/resources内のExcelファイルを初期データとして使用する。
	 * </p>
	 * 
	 * @return ファイル名
	 * @see Class#getResourceAsStream(String)
	 */
	protected String getTemplateExcelFile() {
		return null; // do override
	}

	/**
	 * Excelバージョン取得.
	 * <p>
	 * （テンプレートファイルを使わない場合、）当メソッドが返すバージョンのWorkbookを作成する。
	 * </p>
	 * 
	 * @return Excelバージョン
	 * @see #getTemplateExcelFile()
	 */
	protected SpreadsheetVersion getSpreadsheetVersion() {
		return SpreadsheetVersion.EXCEL2007; // do override
	}

	/**
	 * Workbook初期化.
	 * <p>
	 * Workbookインスタンス生成後に呼ばれる。
	 * </p>
	 * 
	 * @param workbook
	 *            ワークブック
	 */
	protected abstract void initializeWorkbook(Workbook workbook);

	@Override
	public void write(T object) throws IOException {
		Sheet sheet = selectSheet(workbook, object);
		int rowIndex = getRowIndexAndIncrement(sheet);
		for (;;) {
			if (processHeader(sheet, rowIndex)) {
				rowIndex = getRowIndexAndIncrement(sheet);
				continue;
			}
			break;
		}
		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			row = sheet.createRow(rowIndex);
		}
		emit(row, object);
	}

	/**
	 * データモデル出力先のシート選択.
	 * 
	 * @param workbook
	 *            ワークブック
	 * @param object
	 *            データモデル
	 * @return 出力先シート
	 */
	protected Sheet selectSheet(Workbook workbook, T object) {
		return workbook.getSheetAt(0); // do override
	}

	private int getRowIndexAndIncrement(Sheet sheet) {
		String name = sheet.getSheetName();
		IntOption index = rowIndexMap.get(name);
		if (index == null) {
			index = new IntOption(0);
			rowIndexMap.put(name, index);
		}
		int result = index.get();
		index.add(1);
		return result;
	}

	/**
	 * ヘッダー処理.
	 * 
	 * @param sheet
	 *            シート
	 * @param rowIndex
	 *            行番号（0 origin）
	 * @return ヘッダーを生成した場合はtrue
	 */
	protected boolean processHeader(Sheet sheet, int rowIndex) {
		return false;
	}

	protected abstract void emit(Row row, T object);

	protected void emitBlank(Row row, int columnIndex, Cell cell) {
		cell.setCellType(Cell.CELL_TYPE_BLANK);
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, BooleanOption option) {
		if (option.isNull()) {
			emitBlank(row, columnIndex, CellUtil.getCell(row, columnIndex));
			return;
		}
		emit(row, columnIndex, option.get());
	}

	protected void emit(Row row, int columnIndex, boolean value) {
		Cell cell = CellUtil.getCell(row, columnIndex);
		emit(row, columnIndex, cell, value);
	}

	protected void emit(Row row, int columnIndex, Cell cell, boolean value) {
		cell.setCellValue(value);
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, ByteOption option) {
		if (option.isNull()) {
			emitBlank(row, columnIndex, CellUtil.getCell(row, columnIndex));
			return;
		}
		emit(row, columnIndex, option.get());
	}

	protected void emit(Row row, int columnIndex, byte value) {
		Cell cell = CellUtil.getCell(row, columnIndex);
		emit(row, columnIndex, cell, value);
	}

	protected void emit(Row row, int columnIndex, Cell cell, byte value) {
		cell.setCellValue(value);
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, ShortOption option) {
		if (option.isNull()) {
			emitBlank(row, columnIndex, CellUtil.getCell(row, columnIndex));
			return;
		}
		emit(row, columnIndex, option.get());
	}

	protected void emit(Row row, int columnIndex, short value) {
		Cell cell = CellUtil.getCell(row, columnIndex);
		emit(row, columnIndex, cell, value);
	}

	protected void emit(Row row, int columnIndex, Cell cell, short value) {
		cell.setCellValue(value);
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, IntOption option) {
		if (option.isNull()) {
			emitBlank(row, columnIndex, CellUtil.getCell(row, columnIndex));
			return;
		}
		emit(row, columnIndex, option.get());
	}

	protected void emit(Row row, int columnIndex, int value) {
		Cell cell = CellUtil.getCell(row, columnIndex);
		emit(row, columnIndex, cell, value);
	}

	protected void emit(Row row, int columnIndex, Cell cell, int value) {
		cell.setCellValue(value);
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, LongOption option) {
		if (option.isNull()) {
			emitBlank(row, columnIndex, CellUtil.getCell(row, columnIndex));
			return;
		}
		emit(row, columnIndex, option.get());
	}

	protected void emit(Row row, int columnIndex, long value) {
		Cell cell = CellUtil.getCell(row, columnIndex);
		emit(row, columnIndex, cell, value);
	}

	protected void emit(Row row, int columnIndex, Cell cell, long value) {
		cell.setCellValue(value);
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, FloatOption option) {
		if (option.isNull()) {
			emitBlank(row, columnIndex, CellUtil.getCell(row, columnIndex));
			return;
		}
		emit(row, columnIndex, option.get());
	}

	protected void emit(Row row, int columnIndex, float value) {
		Cell cell = CellUtil.getCell(row, columnIndex);
		emit(row, columnIndex, cell, value);
	}

	protected void emit(Row row, int columnIndex, Cell cell, float value) {
		cell.setCellValue(value);
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, DoubleOption option) {
		if (option.isNull()) {
			emitBlank(row, columnIndex, CellUtil.getCell(row, columnIndex));
			return;
		}
		emit(row, columnIndex, option.get());
	}

	protected void emit(Row row, int columnIndex, double value) {
		Cell cell = CellUtil.getCell(row, columnIndex);
		emit(row, columnIndex, cell, value);
	}

	protected void emit(Row row, int columnIndex, Cell cell, double value) {
		cell.setCellValue(value);
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, DecimalOption option) {
		emit(row, columnIndex, option.or(null));
	}

	protected void emit(Row row, int columnIndex, BigDecimal value) {
		Cell cell = CellUtil.getCell(row, columnIndex);
		emit(row, columnIndex, cell, value);
	}

	protected void emit(Row row, int columnIndex, Cell cell, BigDecimal value) {
		if (value == null) {
			emitBlank(row, columnIndex, cell);
			return;
		}
		cell.setCellValue(value.doubleValue());
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, StringOption option) {
		emit(row, columnIndex, option.or((String) null));
	}

	protected void emit(Row row, int columnIndex, String value) {
		Cell cell = CellUtil.getCell(row, columnIndex);
		emit(row, columnIndex, cell, value);
	}

	protected void emit(Row row, int columnIndex, Cell cell, String value) {
		if (value == null) {
			emitBlank(row, columnIndex, cell);
			return;
		}
		cell.setCellValue(value);
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, DateOption option) {
		emit(row, columnIndex, option.or(null));
	}

	protected void emit(Row row, int columnIndex, Date value) {
		Cell cell = CellUtil.getCell(row, columnIndex);
		emit(row, columnIndex, cell, value);
	}

	protected void emit(Row row, int columnIndex, Cell cell, Date value) {
		if (value == null) {
			emitBlank(row, columnIndex, cell);
			return;
		}
		Calendar cal = Calendar.getInstance();
		DateUtil.setDayToCalendar(value.getElapsedDays(), cal);
		cell.setCellValue(cal);
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, DateTimeOption option) {
		emit(row, columnIndex, option.or(null));
	}

	protected void emit(Row row, int columnIndex, DateTime value) {
		Cell cell = CellUtil.getCell(row, columnIndex);
		emit(row, columnIndex, cell, value);
	}

	protected void emit(Row row, int columnIndex, Cell cell, DateTime value) {
		if (value == null) {
			emitBlank(row, columnIndex, cell);
			return;
		}
		Calendar cal = Calendar.getInstance();
		DateUtil.setSecondToCalendar(value.getElapsedSeconds(), cal);
		cell.setCellValue(cal);
		decorate(row, columnIndex, cell);
	}

	/**
	 * セル修飾.
	 * 
	 * @param row
	 *            行
	 * @param columnIndex
	 *            列インデックス
	 * @param cell
	 *            セル（nullあり）
	 */
	protected void decorate(Row row, int columnIndex, Cell cell) {
		// do override
		// CellStyleをセットする。
	}

	@Override
	public void close() throws IOException {
		prepareClose(workbook);
		try (OutputStream os = stream) {
			workbook.write(os);
		}
	}

	/**
	 * クローズ前処理.
	 * 
	 * @param workbook
	 *            ワークブック
	 */
	protected void prepareClose(Workbook workbook) {
		// do override
		// 不要なシートの削除などを行う。
	}
}
