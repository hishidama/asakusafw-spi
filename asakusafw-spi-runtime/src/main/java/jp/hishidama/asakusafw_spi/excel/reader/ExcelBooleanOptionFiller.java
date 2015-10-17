package jp.hishidama.asakusafw_spi.excel.reader;

import com.asakusafw.runtime.value.BooleanOption;

public class ExcelBooleanOptionFiller extends ExcelValueOptionFiller<BooleanOption> {

	@SuppressWarnings("deprecation")
	@Override
	protected void fillNumeric(double d, BooleanOption option) {
		option.modify(d != 0);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillString(String s, BooleanOption option) {
		if (s == null) {
			option.setNull();
			return;
		}
		boolean value = Boolean.parseBoolean(s);
		option.modify(value);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillBoolean(boolean b, BooleanOption option) {
		option.modify(b);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillError(byte code, BooleanOption option) {
		option.setNull();
	}
}
