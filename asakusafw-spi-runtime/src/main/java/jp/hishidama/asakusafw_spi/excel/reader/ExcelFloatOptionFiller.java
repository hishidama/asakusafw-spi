package jp.hishidama.asakusafw_spi.excel.reader;

import org.apache.poi.ss.usermodel.FormulaError;

import com.asakusafw.runtime.value.FloatOption;

public class ExcelFloatOptionFiller extends ExcelValueOptionFiller<FloatOption> {

	@SuppressWarnings("deprecation")
	@Override
	protected void fillNumeric(double d, FloatOption option) {
		option.modify((float) d);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillString(String s, FloatOption option) {
		if (s == null) {
			option.setNull();
			return;
		}
		float value = Float.parseFloat(s);
		option.modify(value);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillBoolean(boolean b, FloatOption option) {
		option.modify(b ? 1 : 0);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillError(byte code, FloatOption option) {
		if (code == FormulaError.DIV0.getCode()) {
			option.modify(Float.POSITIVE_INFINITY);
		} else {
			option.setNull();
		}
	}
}
