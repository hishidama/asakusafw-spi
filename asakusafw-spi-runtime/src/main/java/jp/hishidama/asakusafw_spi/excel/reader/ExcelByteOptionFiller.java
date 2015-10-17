package jp.hishidama.asakusafw_spi.excel.reader;

import com.asakusafw.runtime.value.ByteOption;

public class ExcelByteOptionFiller extends ExcelValueOptionFiller<ByteOption> {

	@SuppressWarnings("deprecation")
	@Override
	protected void fillNumeric(double d, ByteOption option) {
		option.modify((byte) d);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillString(String s, ByteOption option) {
		if (s == null) {
			option.setNull();
			return;
		}
		byte value = Byte.parseByte(s);
		option.modify(value);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillBoolean(boolean b, ByteOption option) {
		option.modify(b ? (byte) 1 : 0);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillError(byte code, ByteOption option) {
		option.setNull();
	}
}
