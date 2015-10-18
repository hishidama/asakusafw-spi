package jp.hishidama.asakusafw_spi.excel;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.hishidama.asakusafw_spi.excel.reader.ExcelBooleanOptionFiller;
import jp.hishidama.asakusafw_spi.excel.reader.ExcelByteOptionFiller;
import jp.hishidama.asakusafw_spi.excel.reader.ExcelDateOptionFiller;
import jp.hishidama.asakusafw_spi.excel.reader.ExcelDateTimeOptionFiller;
import jp.hishidama.asakusafw_spi.excel.reader.ExcelDoubleOptionFiller;
import jp.hishidama.asakusafw_spi.excel.reader.ExcelFloatOptionFiller;
import jp.hishidama.asakusafw_spi.excel.reader.ExcelIntOptionFiller;
import jp.hishidama.asakusafw_spi.excel.reader.ExcelLongOptionFiller;
import jp.hishidama.asakusafw_spi.excel.reader.ExcelShortOptionFiller;
import jp.hishidama.asakusafw_spi.excel.reader.ExcelStringOptionFiller;
import jp.hishidama.asakusafw_spi.excel.reader.ExcelValueOptionFillerFactory;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.asakusafw.runtime.io.ModelInput;
import com.asakusafw.runtime.value.BooleanOption;
import com.asakusafw.runtime.value.ByteOption;
import com.asakusafw.runtime.value.DateOption;
import com.asakusafw.runtime.value.DateTimeOption;
import com.asakusafw.runtime.value.DoubleOption;
import com.asakusafw.runtime.value.FloatOption;
import com.asakusafw.runtime.value.IntOption;
import com.asakusafw.runtime.value.LongOption;
import com.asakusafw.runtime.value.ShortOption;
import com.asakusafw.runtime.value.StringOption;

public abstract class ExcelReader<T> implements ModelInput<T> {
	private Workbook workbook;
	private Iterator<String> sheetNameIterator;
	private Iterator<Row> rowIterator;

	public void initialize(InputStream stream) throws IOException {
		try {
			this.workbook = WorkbookFactory.create(stream);
		} catch (InvalidFormatException e) {
			throw new IOException(e);
		}

		List<String> sheetNames = getSheetNames(workbook);
		{
			List<String> list = new ArrayList<>(sheetNames.size());
			for (String name : sheetNames) {
				Sheet sheet = workbook.getSheet(name);
				if (sheet == null) {
					list.add(name);
				}
			}
			if (!list.isEmpty()) {
				throw new IOException(MessageFormat.format("not found sheet name={0}", list));
			}
		}

		this.sheetNameIterator = sheetNames.iterator();
		this.rowIterator = null;
	}

	/**
	 * 読み込み対象シート名取得.
	 * 
	 * @param workbook
	 *            ワークブック
	 * @return シート名
	 */
	protected List<String> getSheetNames(Workbook workbook) {
		List<String> list = new ArrayList<>();
		for (Sheet sheet : workbook) {
			list.add(sheet.getSheetName());
		}
		return list;
	}

	@Override
	public boolean readTo(T object) throws IOException {
		begin: for (;;) {
			Row row;
			if (rowIterator == null) {
				if (!sheetNameIterator.hasNext()) {
					return false;
				}
				String name = sheetNameIterator.next();
				Sheet sheet = workbook.getSheet(name);
				rowIterator = sheet.rowIterator();
				if (!rowIterator.hasNext()) {
					rowIterator = null;
					continue begin;
				}

				row = rowIterator.next();
				int skipRows = getSkipRows(sheet);
				while (row.getRowNum() < skipRows) {
					if (!rowIterator.hasNext()) {
						rowIterator = null;
						continue begin;
					}
					row = rowIterator.next();
				}
			} else {
				if (!rowIterator.hasNext()) {
					rowIterator = null;
					continue begin;
				}
				row = rowIterator.next();
			}

			if (fillTo(row, object)) {
				return true;
			}
		}
	}

	/**
	 * スキップ行数取得.
	 * 
	 * @param sheet
	 *            シート
	 * @return 行数
	 */
	protected int getSkipRows(Sheet sheet) {
		return 0; // do override
	}

	/**
	 * データモデルに値を埋める.
	 * 
	 * @param row
	 *            Excelの行データ
	 * @param object
	 *            データモデル
	 * @return 正常にデータモデルに値を設定したときtrue。データを破棄（スキップ）する場合はfalse。
	 */
	protected abstract boolean fillTo(Row row, T object);

	protected final void fill(Row row, int columnIndex, BooleanOption option) {
		Cell cell = row.getCell(columnIndex);
		ExcelBooleanOptionFiller filler = excelBooleanOptionFillers.get(columnIndex);
		filler.fill(cell, option);
	}

	private final ExcelValueOptionFillerFactory<ExcelBooleanOptionFiller> excelBooleanOptionFillers = new ExcelValueOptionFillerFactory<ExcelBooleanOptionFiller>() {

		@Override
		protected ExcelBooleanOptionFiller createFiller(int index) {
			return createBooleanOptionFiller(index);
		}
	};

	protected ExcelBooleanOptionFiller createBooleanOptionFiller(int columnIndex) {
		return new ExcelBooleanOptionFiller();
	}

	protected final void fill(Row row, int columnIndex, ByteOption option) {
		Cell cell = row.getCell(columnIndex);
		ExcelByteOptionFiller filler = excelByteOptionFillers.get(columnIndex);
		filler.fill(cell, option);
	}

	private final ExcelValueOptionFillerFactory<ExcelByteOptionFiller> excelByteOptionFillers = new ExcelValueOptionFillerFactory<ExcelByteOptionFiller>() {

		@Override
		protected ExcelByteOptionFiller createFiller(int index) {
			return createByteOptionFiller(index);
		}
	};

	protected ExcelByteOptionFiller createByteOptionFiller(int columnIndex) {
		return new ExcelByteOptionFiller();
	}

	protected final void fill(Row row, int columnIndex, ShortOption option) {
		Cell cell = row.getCell(columnIndex);
		ExcelShortOptionFiller filler = excelShortOptionFillers.get(columnIndex);
		filler.fill(cell, option);
	}

	private final ExcelValueOptionFillerFactory<ExcelShortOptionFiller> excelShortOptionFillers = new ExcelValueOptionFillerFactory<ExcelShortOptionFiller>() {

		@Override
		protected ExcelShortOptionFiller createFiller(int index) {
			return createShortOptionFiller(index);
		}
	};

	protected ExcelShortOptionFiller createShortOptionFiller(int columnIndex) {
		return new ExcelShortOptionFiller();
	}

	protected final void fill(Row row, int columnIndex, IntOption option) {
		Cell cell = row.getCell(columnIndex);
		ExcelIntOptionFiller filler = excelIntOptionFillers.get(columnIndex);
		filler.fill(cell, option);
	}

	private final ExcelValueOptionFillerFactory<ExcelIntOptionFiller> excelIntOptionFillers = new ExcelValueOptionFillerFactory<ExcelIntOptionFiller>() {

		@Override
		protected ExcelIntOptionFiller createFiller(int index) {
			return createIntOptionFiller(index);
		}
	};

	protected ExcelIntOptionFiller createIntOptionFiller(int columnIndex) {
		return new ExcelIntOptionFiller();
	}

	protected final void fill(Row row, int columnIndex, LongOption option) {
		Cell cell = row.getCell(columnIndex);
		ExcelLongOptionFiller filler = excelLongOptionFillers.get(columnIndex);
		filler.fill(cell, option);
	}

	private final ExcelValueOptionFillerFactory<ExcelLongOptionFiller> excelLongOptionFillers = new ExcelValueOptionFillerFactory<ExcelLongOptionFiller>() {

		@Override
		protected ExcelLongOptionFiller createFiller(int index) {
			return createLongOptionFiller(index);
		}
	};

	protected ExcelLongOptionFiller createLongOptionFiller(int columnIndex) {
		return new ExcelLongOptionFiller();
	}

	protected final void fill(Row row, int columnIndex, FloatOption option) {
		Cell cell = row.getCell(columnIndex);
		ExcelFloatOptionFiller filler = excelFloatOptionFillers.get(columnIndex);
		filler.fill(cell, option);
	}

	private final ExcelValueOptionFillerFactory<ExcelFloatOptionFiller> excelFloatOptionFillers = new ExcelValueOptionFillerFactory<ExcelFloatOptionFiller>() {

		@Override
		protected ExcelFloatOptionFiller createFiller(int index) {
			return createFloatOptionFiller(index);
		}
	};

	protected ExcelFloatOptionFiller createFloatOptionFiller(int columnIndex) {
		return new ExcelFloatOptionFiller();
	}

	protected final void fill(Row row, int columnIndex, DoubleOption option) {
		Cell cell = row.getCell(columnIndex);
		ExcelDoubleOptionFiller filler = excelDoubleOptionFillers.get(columnIndex);
		filler.fill(cell, option);
	}

	private final ExcelValueOptionFillerFactory<ExcelDoubleOptionFiller> excelDoubleOptionFillers = new ExcelValueOptionFillerFactory<ExcelDoubleOptionFiller>() {

		@Override
		protected ExcelDoubleOptionFiller createFiller(int index) {
			return createDoubleOptionFiller(index);
		}
	};

	protected ExcelDoubleOptionFiller createDoubleOptionFiller(int columnIndex) {
		return new ExcelDoubleOptionFiller();
	}

	protected final void fill(Row row, int columnIndex, StringOption option) {
		Cell cell = row.getCell(columnIndex);
		ExcelStringOptionFiller filler = excelStringOptionFillers.get(columnIndex);
		filler.fill(cell, option);
	}

	private final ExcelValueOptionFillerFactory<ExcelStringOptionFiller> excelStringOptionFillers = new ExcelValueOptionFillerFactory<ExcelStringOptionFiller>() {

		@Override
		protected ExcelStringOptionFiller createFiller(int index) {
			return createStringOptionFiller(index);
		}
	};

	protected ExcelStringOptionFiller createStringOptionFiller(int columnIndex) {
		return new ExcelStringOptionFiller();
	}

	protected final void fill(Row row, int columnIndex, DateOption option) {
		Cell cell = row.getCell(columnIndex);
		ExcelDateOptionFiller filler = excelDateOptionFillers.get(columnIndex);
		filler.fill(cell, option);
	}

	private final ExcelValueOptionFillerFactory<ExcelDateOptionFiller> excelDateOptionFillers = new ExcelValueOptionFillerFactory<ExcelDateOptionFiller>() {

		@Override
		protected ExcelDateOptionFiller createFiller(int index) {
			return createDateOptionFiller(index);
		}
	};

	protected ExcelDateOptionFiller createDateOptionFiller(int columnIndex) {
		return new ExcelDateOptionFiller();
	}

	protected final void fill(Row row, int columnIndex, DateTimeOption option) {
		Cell cell = row.getCell(columnIndex);
		ExcelDateTimeOptionFiller filler = excelDateTimeOptionFillers.get(columnIndex);
		filler.fill(cell, option);
	}

	private final ExcelValueOptionFillerFactory<ExcelDateTimeOptionFiller> excelDateTimeOptionFillers = new ExcelValueOptionFillerFactory<ExcelDateTimeOptionFiller>() {

		@Override
		protected ExcelDateTimeOptionFiller createFiller(int index) {
			return createDateTimeOptionFiller(index);
		}
	};

	protected ExcelDateTimeOptionFiller createDateTimeOptionFiller(int columnIndex) {
		return new ExcelDateTimeOptionFiller();
	}

	@Override
	public void close() throws IOException {
		// do nothing
	}
}
