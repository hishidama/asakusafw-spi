package jp.hishidama.asakusafw_spi.excel.reader;

import com.asakusafw.runtime.value.LongOption;

public class ExcelLongOptionFiller extends ExcelValueOptionFiller<LongOption> {

	@SuppressWarnings("deprecation")
	@Override
	protected void fillNumeric(double d, LongOption option) {
		option.modify((long) d);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillString(String s, LongOption option) {
		if (s == null) {
			option.setNull();
			return;
		}
		long value = Long.parseLong(s);
		option.modify(value);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillBoolean(boolean b, LongOption option) {
		option.modify(b ? 1 : 0);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillError(byte code, LongOption option) {
		option.setNull();
	}
}
