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
import org.apache.poi.ss.usermodel.Workbook;

import com.example.modelgen.dmdl.model.StoreInfo;

/**
 * store_infoをExcelで読み書きするDataFormatの例（Direct I/O）.
 * <p>
 * Excel出力時に（テンプレートとなるExcelファイルを使わず）新規にExcelファイルを生成する例。
 * </p>
 */
public class StoreInfoExcelFormat extends AbstractExcelFormat<StoreInfo> {

	@Override
	public Class<StoreInfo> getSupportedType() {
		return StoreInfo.class;
	}

	// AsakusaFWでは、入力ファイルをテストの検証時に作成（出力）するので、ReaderとWriterを両方とも定義する必要がある

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
			// 先頭1行（ヘッダー行）を読み飛ばす。
			return 1;
		}

		@Override
		protected boolean fillTo(Row row, StoreInfo object) {
			// Excelの1行分の内容をAsakusaFWのデータモデルへ移送する。
			fill(row, 0, object.getStoreCodeOption());
			fill(row, 1, object.getStoreNameOption());

			// データが全て処理できたときはtrueを返す。
			// falseを返すと、データは破棄される。
			return true;
		}
	}

	protected static class Writer extends ExcelWriter<StoreInfo> {

		@Override
		protected SpreadsheetVersion getSpreadsheetVersion() {
			// 生成するExcelファイルのバージョンを返す。
			return SpreadsheetVersion.EXCEL97;
		}

		@Override
		protected void initializeWorkbook(Workbook workbook) {
			// 当メソッドはWorkbookインスタンスが生成された直後に呼ばれる。
			// 当サンプルでは空のWorkbookなので、シートを生成しておく。
			workbook.createSheet("store_info");
		}

		@Override
		protected boolean processHeader(Sheet sheet, int rowIndex) {
			// 先頭行（rowIndex==0）の場合にヘッダーを生成する例
			if (rowIndex == 0) {
				Row row = sheet.createRow(rowIndex);
				header(row, 0, "店コード");
				header(row, 1, "店名称");

				// ヘッダー行を生成した場合はtrueを返す。
				return true;
			}

			return false;
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

			// （store_infoは入力にしか使わないので、出力するのはテストデータ生成時。
			// テストではスタイルは関係ないが、当サンプルではスタイルを割り当てる例として書いてみた）
		}

		@Override
		protected void emit(Row row, StoreInfo object) {
			// AsakusaFWのデータモデルからExcelの1行分を移送する。
			emit(row, 0, object.getStoreCodeOption());
			emit(row, 1, object.getStoreNameOption());
		}

		private CellStyle dataStyle;

		@Override
		protected void decorate(Row row, int columnIndex, Cell cell) {
			// 各emitメソッドから呼ばれる。当メソッドでセルのスタイルを設定する。
			if (cell == null) {
				return;
			}

			// CellStyleインスタンスはWorkbook内に4000個程度しか作れないので、同じスタイルの場合はインスタンスを使い回す。
			if (dataStyle == null) {
				CellStyle style = row.getSheet().getWorkbook().createCellStyle();
				style.setBorderTop(CellStyle.BORDER_THIN);
				style.setBorderBottom(CellStyle.BORDER_THIN);
				style.setBorderLeft(CellStyle.BORDER_THIN);
				style.setBorderRight(CellStyle.BORDER_THIN);
				dataStyle = style;
			}
			cell.setCellStyle(dataStyle);

			// （store_infoは入力にしか使わないので、出力するのはテストデータ生成時。
			// テストではスタイルは関係ないが、当サンプルではスタイルを割り当てる例として書いてみた）
		}
	}
}
