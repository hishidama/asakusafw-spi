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

/**
 * category_summaryをExcelで読み書きするDataFormatの例（Direct I/O）.
 * <p>
 * Excel出力時に、テンプレートとなるExcelファイルを読み込んで（コピーして）使用する例。
 * </p>
 */
public class CategorySummaryExcelFormat extends AbstractExcelFormat<CategorySummary> {

	@Override
	public Class<CategorySummary> getSupportedType() {
		return CategorySummary.class;
	}

	// AsakusaFWでは、出力したファイルをテストの検証時に読み込むので、ReaderとWriterを両方とも定義する必要がある。

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
			// 読み込むシート名
			return Arrays.asList("集計結果");
		}

		@Override
		protected int getSkipRows(Sheet sheet) {
			// 先頭1行（ヘッダー行）を読み飛ばす。
			return 1;
		}

		@Override
		protected boolean fillTo(Row row, CategorySummary object) {
			// Excelの1行分の内容をAsakusaFWのデータモデルへ移送する。
			fill(row, 0, object.getCategoryCodeOption());
			fill(row, 1, object.getAmountTotalOption());
			fill(row, 2, object.getSellingPriceTotalOption());

			// データが全て処理できたときはtrueを返す。
			// falseを返すと、データは破棄される。
			return true;
		}
	}

	protected static class Writer extends ExcelWriter<CategorySummary> {

		@Override
		protected String getTemplateExcelFile() {
			// テンプレートとして使用するExcelファイル名
			// src/main/resources内の、当javaソースと同じパッケージの場所にExcelファイルを置いておく必要がある。
			return "category_summary.xls";
		}

		@Override
		protected void initializeWorkbook(Workbook workbook) {
			// 当メソッドはWorkbookインスタンスが生成された直後に呼ばれる。
			// このクラスの例では、「template」というシートを「集計結果」というシート名に変更している。
			int index = workbook.getSheetIndex("template");
			workbook.setSheetName(index, "集計結果");
		}

		@Override
		protected boolean processHeader(Sheet sheet, int rowIndex) {
			// 先頭行（rowIndex==0）をヘッダー行として無視する。
			// このクラスの例では、ヘッダー行はテンプレートのExcelファイルに書かれているものをそのまま使用するので、
			// 当メソッドとしては何もしない。
			return rowIndex == 0;
		}

		@Override
		protected void emit(Row row, CategorySummary object) {
			// AsakusaFWのデータモデルからExcelの1行分を移送する。
			emit(row, 0, object.getCategoryCodeOption());
			emit(row, 1, object.getAmountTotalOption());
			emit(row, 2, object.getSellingPriceTotalOption());
		}

		@Override
		protected void decorate(Row row, int columnIndex, Cell cell) {
			// 各emitメソッドから呼ばれる。当メソッドでセルのスタイルを設定する。
			if (cell == null) {
				return;
			}

			// 今回の例では、シートの2行目（データ行としての先頭行）のスタイルをそのまま生かす。
			if (row.getRowNum() == 1) {
				return;
			}

			// データ行としての2行目以降は、シートの2行目のスタイルをそのままコピーする。
			Sheet sheet = row.getSheet();
			Cell first = sheet.getRow(1).getCell(columnIndex);
			CellStyle style = first.getCellStyle();
			cell.setCellStyle(style);
		}
	}
}
