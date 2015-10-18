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
		String templateFileName = getTemplateExcelFile();
		if (templateFileName != null) {
			try (InputStream is = getClass().getResourceAsStream(templateFileName)) {
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
		return null;
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
		return SpreadsheetVersion.EXCEL2007;
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

	protected void emit(Row row, int columnIndex, BooleanOption option) {
		if (option.isNull()) {
			Cell cell = row.getCell(columnIndex);
			decorate(row, columnIndex, cell);
			return;
		}
		Cell cell = CellUtil.getCell(row, columnIndex);
		cell.setCellValue(option.get());
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, ByteOption option) {
		if (option.isNull()) {
			Cell cell = row.getCell(columnIndex);
			decorate(row, columnIndex, cell);
			return;
		}
		Cell cell = CellUtil.getCell(row, columnIndex);
		cell.setCellValue(option.get());
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, ShortOption option) {
		if (option.isNull()) {
			Cell cell = row.getCell(columnIndex);
			decorate(row, columnIndex, cell);
			return;
		}
		Cell cell = CellUtil.getCell(row, columnIndex);
		cell.setCellValue(option.get());
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, IntOption option) {
		if (option.isNull()) {
			Cell cell = row.getCell(columnIndex);
			decorate(row, columnIndex, cell);
			return;
		}
		Cell cell = CellUtil.getCell(row, columnIndex);
		cell.setCellValue(option.get());
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, LongOption option) {
		if (option.isNull()) {
			Cell cell = row.getCell(columnIndex);
			decorate(row, columnIndex, cell);
			return;
		}
		Cell cell = CellUtil.getCell(row, columnIndex);
		cell.setCellValue(option.get());
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, FloatOption option) {
		if (option.isNull()) {
			Cell cell = row.getCell(columnIndex);
			decorate(row, columnIndex, cell);
			return;
		}
		Cell cell = CellUtil.getCell(row, columnIndex);
		cell.setCellValue(option.get());
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, DoubleOption option) {
		if (option.isNull()) {
			Cell cell = row.getCell(columnIndex);
			decorate(row, columnIndex, cell);
			return;
		}
		Cell cell = CellUtil.getCell(row, columnIndex);
		cell.setCellValue(option.get());
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, DecimalOption option) {
		if (option.isNull()) {
			Cell cell = row.getCell(columnIndex);
			decorate(row, columnIndex, cell);
			return;
		}
		Cell cell = CellUtil.getCell(row, columnIndex);
		BigDecimal d = option.get();
		String s = d.toPlainString();
		if (s.endsWith(".0")) {
			s = s.substring(0, s.length() - 2);
		}
		cell.setCellValue(s);
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, StringOption option) {
		if (option.isNull()) {
			Cell cell = row.getCell(columnIndex);
			decorate(row, columnIndex, cell);
			return;
		}
		Cell cell = CellUtil.getCell(row, columnIndex);
		cell.setCellValue(option.getAsString());
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, DateOption option) {
		if (option.isNull()) {
			Cell cell = row.getCell(columnIndex);
			decorate(row, columnIndex, cell);
			return;
		}
		Cell cell = CellUtil.getCell(row, columnIndex);
		Date d = option.get();
		Calendar cal = Calendar.getInstance();
		DateUtil.setDayToCalendar(d.getElapsedDays(), cal);
		cell.setCellValue(cal);
		decorate(row, columnIndex, cell);
	}

	protected void emit(Row row, int columnIndex, DateTimeOption option) {
		if (option.isNull()) {
			Cell cell = row.getCell(columnIndex);
			decorate(row, columnIndex, cell);
			return;
		}
		Cell cell = CellUtil.getCell(row, columnIndex);
		DateTime dt = option.get();
		Calendar cal = Calendar.getInstance();
		DateUtil.setSecondToCalendar(dt.getElapsedSeconds(), cal);
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
