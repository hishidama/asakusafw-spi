package jp.hishidama.asakusafw_spi.excel.reader;

import java.util.Calendar;

import com.asakusafw.runtime.value.Date;
import com.asakusafw.runtime.value.DateOption;

public class ExcelDateOptionFiller extends ExcelValueOptionFiller<DateOption> {

	@SuppressWarnings("deprecation")
	@Override
	protected void fillNumeric(double d, DateOption option) {
		Calendar cal = org.apache.poi.ss.usermodel.DateUtil.getJavaCalendar(d, false, null);
		int days = com.asakusafw.runtime.value.DateUtil.getDayFromCalendar(cal);
		Date value = new Date(days);
		option.modify(value);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillString(String s, DateOption option) {
		if (s == null) {
			option.setNull();
			return;
		}
		// TODO date format
		int days = com.asakusafw.runtime.value.DateUtil.parseDate(s, '/');
		Date value = new Date(days);
		option.modify(value);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillBoolean(boolean b, DateOption option) {
		option.setNull();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void fillError(byte code, DateOption option) {
		option.setNull();
	}
}
