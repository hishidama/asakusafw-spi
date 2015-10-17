package com.example.jobflow.format;

import java.util.Arrays;
import java.util.List;

import jp.hishidama.asakusafw_spi.excel.AbstractExcelFormat;
import jp.hishidama.asakusafw_spi.excel.ExcelReader;
import jp.hishidama.asakusafw_spi.excel.ExcelWriter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.example.modelgen.dmdl.model.CategorySummary;

public class CategorySummaryExcelFormat extends AbstractExcelFormat<CategorySummary> {
	@Override
	public Class<CategorySummary> getSupportedType() {
		return CategorySummary.class;
	}

	@Override
	protected ExcelReader<CategorySummary> createReader() {
		return new Reader();
	}

	@Override
	protected ExcelWriter<CategorySummary> createWriter() {
		return new Writer();
	}

	protected static class Reader extends ExcelReader<CategorySummary> {

		@Override
		protected List<String> getSheetNames(Workbook workbook) {
			return Arrays.asList("集計結果");
		}

		@Override
		protected int getSkipRows(Sheet sheet) {
			return 1;
		}

		@Override
		protected boolean fillTo(Row row, CategorySummary object) {
			fill(row, 0, object.getCategoryCodeOption());
			fill(row, 1, object.getAmountTotalOption());
			fill(row, 2, object.getSellingPriceTotalOption());
			return true;
		}
	}

	protected static class Writer extends ExcelWriter<CategorySummary> {

		@Override
		protected String getTemplateExcelFile() {
			return "category_summary.xls";
		}

		@Override
		protected void initializeWorkbook(Workbook workbook) {
			int index = workbook.getSheetIndex("template");
			workbook.setSheetName(index, "集計結果");
		}

		@Override
		protected boolean processHeader(Sheet sheet, int rowIndex) {
			return rowIndex == 0;
		}

		@Override
		protected void emit(Row row, CategorySummary object) {
			emit(row, 0, object.getCategoryCodeOption());
			emit(row, 1, object.getAmountTotalOption());
			emit(row, 2, object.getSellingPriceTotalOption());
		}

		@Override
		protected void decorate(Row row, int columnIndex, Cell cell) {
			if (cell == null) {
				return;
			}
			if (row.getRowNum() == 1) {
				return;
			}

			Sheet sheet = row.getSheet();
			Cell first = sheet.getRow(1).getCell(columnIndex);
			CellStyle style = first.getCellStyle();
			cell.setCellStyle(style);
		}
	}
}
