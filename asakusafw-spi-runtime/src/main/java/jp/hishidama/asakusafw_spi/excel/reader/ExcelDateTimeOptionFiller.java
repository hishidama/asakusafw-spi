package jp.hishidama.asakusafw_spi.excel.reader;

import java.util.Calendar;

import com.asakusafw.runtime.value.DateTime;
import com.asakusafw.runtime.value.DateTimeOption;

public class ExcelDateTimeOptionFiller extends ExcelValueOptionFiller<DateTimeOption> {

	@SuppressWarnings("deprecation")
	@Override
	protected void fillNumeric(double d, DateTimeOption option) {
		Calendar cal = org.apache.poi.ss.usermodel.DateUtil.getJavaCalendar(d, false, null);
		long seconds = com.asakusafw.runtime.value.DateUtil.getSecondFromCalendar(cal);
		DateTime value = new DateTime(seconds);
		option.modify(value);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillString(String s, DateTimeOption option) {
		if (s == null) {
			option.setNull();
			return;
		}
		// TODO datetime format
		long seconds = com.asakusafw.runtime.value.DateUtil.parseDateTime(s, '/', ' ', ':');
		DateTime value = new DateTime(seconds);
		option.modify(value);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillBoolean(boolean b, DateTimeOption option) {
		option.setNull();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillError(byte code, DateTimeOption option) {
		option.setNull();
	}
}
