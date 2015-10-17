package jp.hishidama.asakusafw_spi.excel.reader;

import org.apache.poi.ss.usermodel.FormulaError;

import com.asakusafw.runtime.value.DoubleOption;

public class ExcelDoubleOptionFiller extends ExcelValueOptionFiller<DoubleOption> {

	@SuppressWarnings("deprecation")
	@Override
	protected void fillNumeric(double d, DoubleOption option) {
		option.modify(d);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillString(String s, DoubleOption option) {
		if (s == null) {
			option.setNull();
			return;
		}
		double value = Double.parseDouble(s);
		option.modify(value);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillBoolean(boolean b, DoubleOption option) {
		option.modify(b ? 1 : 0);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillError(byte code, DoubleOption option) {
		if (code == FormulaError.DIV0.getCode()) {
			option.modify(Double.POSITIVE_INFINITY);
		} else {
			option.setNull();
		}
	}
}
