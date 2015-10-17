package jp.hishidama.asakusafw_spi.excel.reader;

import java.text.MessageFormat;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.asakusafw.runtime.value.ValueOption;

public abstract class ExcelValueOptionFiller<T extends ValueOption<?>> {

	@SuppressWarnings("deprecation")
	public void fill(Cell cell, T option) {
		if (cell == null) {
			option.setNull();
			return;
		}

		int type = cell.getCellType();
		switch (type) {
		case Cell.CELL_TYPE_NUMERIC:
			fillNumeric(cell.getNumericCellValue(), option);
			break;
		case Cell.CELL_TYPE_STRING:
			fillString(cell.getStringCellValue(), option);
			break;
		case Cell.CELL_TYPE_FORMULA:
			fillFormula(cell, option);
			break;
		case Cell.CELL_TYPE_BLANK:
			fillBlank(cell, option);
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			fillBoolean(cell.getBooleanCellValue(), option);
			break;
		case Cell.CELL_TYPE_ERROR:
			fillError(cell.getErrorCellValue(), option);
			break;
		default:
			throw new UnsupportedOperationException(MessageFormat.format("cellType={0}", type));
		}
	}

	protected abstract void fillNumeric(double d, T option);

	protected abstract void fillString(String s, T option);

	private FormulaEvaluator formulaEvaluator;

	@SuppressWarnings("deprecation")
	protected void fillFormula(Cell cell, T option) {
		if (formulaEvaluator == null) {
			Workbook workbook = cell.getSheet().getWorkbook();
			CreationHelper helper = workbook.getCreationHelper();
			formulaEvaluator = helper.createFormulaEvaluator();
		}

		updateFormula(cell);
		CellValue value = formulaEvaluator.evaluate(cell);

		int type = value.getCellType();
		switch (type) {
		case Cell.CELL_TYPE_NUMERIC:
			fillNumeric(value.getNumberValue(), option);
			break;
		case Cell.CELL_TYPE_STRING:
			fillString(value.getStringValue(), option);
			break;
		case Cell.CELL_TYPE_BLANK:
			option.setNull();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			fillBoolean(value.getBooleanValue(), option);
			break;
		case Cell.CELL_TYPE_ERROR:
			fillError(value.getErrorValue(), option);
			break;
		default:
			throw new UnsupportedOperationException(MessageFormat.format("cellType={0}", type));
		}
	}

	/**
	 * 計算式更新.
	 * <p>
	 * 計算式の評価実行前に 計算式を変えたい場合、当メソッド内で変更する。
	 * </p>
	 * 
	 * @param cell
	 *            セル
	 */
	protected void updateFormula(Cell cell) {
		// String formula = cell.getCellFormula();
		// String update = formula.replace(foo, bar);
		// cell.setCellFormula(update);
	}

	protected void fillBlank(Cell cell, T option) {
		Cell top = searchMergedCell(cell);
		fill(top, option);
	}

	protected abstract void fillBoolean(boolean b, T option);

	protected abstract void fillError(byte code, T option);

	protected Cell searchMergedCell(Cell cell) {
		int rowIndex = cell.getRowIndex();
		int columnIndex = cell.getColumnIndex();

		Sheet sheet = cell.getSheet();
		List<CellRangeAddress> list = sheet.getMergedRegions();
		for (CellRangeAddress range : list) {
			if (range.isInRange(rowIndex, columnIndex)) {
				// 左上のセルを取得
				Row row = sheet.getRow(range.getFirstRow());
				if (row == null) {
					return null;
				}
				return row.getCell(range.getFirstColumn());
			}
		}
		return null;
	}
}
