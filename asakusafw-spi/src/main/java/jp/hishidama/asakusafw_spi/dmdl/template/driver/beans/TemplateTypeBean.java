package jp.hishidama.asakusafw_spi.dmdl.template.driver.beans;

import java.math.BigDecimal;

import org.apache.hadoop.io.Text;

import com.asakusafw.dmdl.semantics.Type;
import com.asakusafw.runtime.value.BooleanOption;
import com.asakusafw.runtime.value.ByteOption;
import com.asakusafw.runtime.value.Date;
import com.asakusafw.runtime.value.DateOption;
import com.asakusafw.runtime.value.DateTime;
import com.asakusafw.runtime.value.DateTimeOption;
import com.asakusafw.runtime.value.DecimalOption;
import com.asakusafw.runtime.value.DoubleOption;
import com.asakusafw.runtime.value.FloatOption;
import com.asakusafw.runtime.value.IntOption;
import com.asakusafw.runtime.value.LongOption;
import com.asakusafw.runtime.value.ShortOption;
import com.asakusafw.runtime.value.StringOption;

public class TemplateTypeBean extends AbstractTemplateBean {

	protected final Type type;

	public TemplateTypeBean(TemplateRootBean root, Type type) {
		super(root);
		this.type = type;
	}

	public String getName() {
		return type.toString();
	}

	public Class<?> getJavaClass() {
		switch (getName()) {
		case "BYTE":
			return byte.class;
		case "SHORT":
			return short.class;
		case "INT":
			return int.class;
		case "LONG":
			return long.class;
		case "DECIMAL":
			return BigDecimal.class;
		case "FLOAT":
			return float.class;
		case "DOUBLE":
			return double.class;
		case "TEXT":
			return Text.class;
		case "BOOLEAN":
			return boolean.class;
		case "DATE":
			return Date.class;
		case "DATETIME":
			return DateTime.class;
		default:
			return null;
		}
	}

	public Class<?> getJavaClassAs() {
		if ("TEXT".equals(getName())) {
			return String.class;
		} else {
			return getJavaClass();
		}
	}

	public Class<?> getOptionClass() {
		switch (getName()) {
		case "BYTE":
			return ByteOption.class;
		case "SHORT":
			return ShortOption.class;
		case "INT":
			return IntOption.class;
		case "LONG":
			return LongOption.class;
		case "DECIMAL":
			return DecimalOption.class;
		case "FLOAT":
			return FloatOption.class;
		case "DOUBLE":
			return DoubleOption.class;
		case "TEXT":
			return StringOption.class;
		case "BOOLEAN":
			return BooleanOption.class;
		case "DATE":
			return DateOption.class;
		case "DATETIME":
			return DateTimeOption.class;
		default:
			return null;
		}
	}

	public String getZero() {
		switch (getName()) {
		case "BYTE":
		case "SHORT":
		case "INT":
		case "LONG":
			return "0";
		case "DECIMAL":
			return "BigDecimal.ZERO";
		case "FLOAT":
		case "DOUBLE":
			return "0";
		case "TEXT":
			return "\"\"";
		case "BOOLEAN":
			return "false";
		case "DATE":
			return "new Date()";
		case "DATETIME":
			return "new DateTime()";
		default:
			return null;
		}
	}

	@Override
	public String toString() {
		return getName();
	}
}
