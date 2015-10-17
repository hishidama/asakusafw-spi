package jp.hishidama.asakusafw_spi.excel.reader;

import java.util.ArrayList;
import java.util.List;

public abstract class ExcelValueOptionFillerFactory<F> {

	private final List<F> list = new ArrayList<>();

	public F get(int index) {
		while (index >= list.size()) {
			list.add(null);
		}
		F filler = list.get(index);
		if (filler == null) {
			filler = createFiller(index);
			list.set(index, filler);
		}
		return filler;
	}

	protected abstract F createFiller(int index);
}
