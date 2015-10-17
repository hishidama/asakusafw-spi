package jp.hishidama.asakusafw_spi.excel.reader;

import com.asakusafw.runtime.value.IntOption;

public class ExcelIntOptionFiller extends ExcelValueOptionFiller<IntOption> {

	@SuppressWarnings("deprecation")
	@Override
	protected void fillNumeric(double d, IntOption option) {
		option.modify((int) d);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillString(String s, IntOption option) {
		if (s == null) {
			option.setNull();
			return;
		}
		int value = Integer.parseInt(s);
		option.modify(value);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillBoolean(boolean b, IntOption option) {
		option.modify(b ? 1 : 0);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillError(byte code, IntOption option) {
		option.setNull();
	}
}
