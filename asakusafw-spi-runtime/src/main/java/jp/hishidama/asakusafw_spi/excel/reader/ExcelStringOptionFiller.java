package jp.hishidama.asakusafw_spi.excel.reader;

import org.apache.poi.ss.usermodel.FormulaError;

import com.asakusafw.runtime.value.StringOption;

public class ExcelStringOptionFiller extends ExcelValueOptionFiller<StringOption> {

	@SuppressWarnings("deprecation")
	@Override
	protected void fillNumeric(double d, StringOption option) {
		String value = Double.toString(d);
		if (value.endsWith(".0")) {
			value = value.substring(0, value.length() - 2);
		}
		option.modify(value);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillString(String s, StringOption option) {
		option.modify(s);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillBoolean(boolean b, StringOption option) {
		String value = Boolean.toString(b);
		option.modify(value);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillError(byte code, StringOption option) {
		FormulaError e = FormulaError.forInt(code);
		String value = e.getString();
		option.modify(value);
	}
}
