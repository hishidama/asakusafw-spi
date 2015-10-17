package jp.hishidama.asakusafw_spi.excel.reader;

import java.math.BigDecimal;

import com.asakusafw.runtime.value.DecimalOption;

public class ExcelDecimalOptionFiller extends ExcelValueOptionFiller<DecimalOption> {

	@SuppressWarnings("deprecation")
	@Override
	protected void fillNumeric(double d, DecimalOption option) {
		BigDecimal value = BigDecimal.valueOf(d);
		option.modify(value);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillString(String s, DecimalOption option) {
		if (s == null) {
			option.setNull();
			return;
		}
		BigDecimal value = new BigDecimal(s);
		option.modify(value);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillBoolean(boolean b, DecimalOption option) {
		option.modify(b ? BigDecimal.ONE : BigDecimal.ZERO);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillError(byte code, DecimalOption option) {
		option.setNull();
	}
}
