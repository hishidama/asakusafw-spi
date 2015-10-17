package jp.hishidama.asakusafw_spi.excel.reader;

import com.asakusafw.runtime.value.ShortOption;

public class ExcelShortOptionFiller extends ExcelValueOptionFiller<ShortOption> {

	@SuppressWarnings("deprecation")
	@Override
	protected void fillNumeric(double d, ShortOption option) {
		option.modify((short) d);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillString(String s, ShortOption option) {
		if (s == null) {
			option.setNull();
			return;
		}
		short value = Short.parseShort(s);
		option.modify(value);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillBoolean(boolean b, ShortOption option) {
		option.modify(b ? (short) 1 : 0);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillError(byte code, ShortOption option) {
		option.setNull();
	}
}
