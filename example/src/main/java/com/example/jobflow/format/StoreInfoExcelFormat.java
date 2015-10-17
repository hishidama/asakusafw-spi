package com.example.jobflow.format;

import jp.hishidama.asakusafw_spi.excel.AbstractExcelFormat;
import jp.hishidama.asakusafw_spi.excel.ExcelReader;
import jp.hishidama.asakusafw_spi.excel.ExcelWriter;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.example.modelgen.dmdl.model.StoreInfo;

public class StoreInfoExcelFormat extends AbstractExcelFormat<StoreInfo> {
	@Override
	public Class<StoreInfo> getSupportedType() {
		return StoreInfo.class;
	}

	@Override
	protected ExcelReader<StoreInfo> createReader() {
		return new Reader();
	}

	@Override
	protected ExcelWriter<StoreInfo> createWriter() {
		return new Writer();
	}

	protected static class Reader extends ExcelReader<StoreInfo> {

		@Override
		protected int getSkipRows(Sheet sheet) {
			return 1;
		}

		@Override
		protected boolean fillTo(Row row, StoreInfo object) {
			fill(row, 0, object.getStoreCodeOption());
			fill(row, 1, object.getStoreNameOption());
			return true;
		}
	}

	protected static class Writer extends ExcelWriter<StoreInfo> {

		@Override
		protected SpreadsheetVersion getSpreadsheetVersion() {
			return SpreadsheetVersion.EXCEL97;
		}

		@Override
		protected boolean processHeader(Sheet sheet, int rowIndex) {
			if (rowIndex > 0) {
				return false;
			}

			Row row = sheet.createRow(rowIndex);
			header(row, 0, "店コード");
			header(row, 1, "店名称");
			return true;
		}

		private void header(Row row, int columnIndex, String header) {
			Cell cell = row.createCell(columnIndex);
			cell.setCellValue(header);

			CellStyle style;
			if (columnIndex == 0) {
				style = row.getSheet().getWorkbook().createCellStyle();
				style.setBorderTop(CellStyle.BORDER_THIN);
				style.setBorderBottom(CellStyle.BORDER_THIN);
				style.setBorderLeft(CellStyle.BORDER_THIN);
				style.setBorderRight(CellStyle.BORDER_THIN);
				style.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			} else {
				style = row.getCell(0).getCellStyle();
			}
			cell.setCellStyle(style);
		}

		@Override
		protected void emit(Row row, StoreInfo object) {
			emit(row, 0, object.getStoreCodeOption());
			emit(row, 1, object.getStoreNameOption());
		}

		private CellStyle dataStyle;

		@Override
		protected void decorate(Row row, int columnIndex, Cell cell) {
			if (cell == null) {
				return;
			}
			if (dataStyle == null) {
				CellStyle style = row.getSheet().getWorkbook().createCellStyle();
				style.setBorderTop(CellStyle.BORDER_THIN);
				style.setBorderBottom(CellStyle.BORDER_THIN);
				style.setBorderLeft(CellStyle.BORDER_THIN);
				style.setBorderRight(CellStyle.BORDER_THIN);
				dataStyle = style;
			}
			cell.setCellStyle(dataStyle);
		}
	}
}
