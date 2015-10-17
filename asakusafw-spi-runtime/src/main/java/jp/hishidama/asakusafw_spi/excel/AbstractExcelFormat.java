package jp.hishidama.asakusafw_spi.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.asakusafw.runtime.directio.BinaryStreamFormat;
import com.asakusafw.runtime.io.ModelInput;
import com.asakusafw.runtime.io.ModelOutput;

public abstract class AbstractExcelFormat<T> extends BinaryStreamFormat<T> {

	@Override
	public long getPreferredFragmentSize() {
		return -1L;
	}

	@Override
	public long getMinimumFragmentSize() {
		return Long.MAX_VALUE;
	}

	@Override
	public ModelInput<T> createInput(Class<? extends T> dataType, String path, InputStream stream, long offset,
			long fragmentSize) throws IOException, InterruptedException {
		if (dataType == null) {
			throw new IllegalArgumentException("dataType must not be null");
		}
		if (path == null) {
			throw new IllegalArgumentException("path must not be null");
		}
		if (stream == null) {
			throw new IllegalArgumentException("stream must not be null");
		}
		ExcelReader<T> reader = createReader();
		reader.initialize(stream);
		return reader;
	}

	protected abstract ExcelReader<T> createReader();

	@Override
	public ModelOutput<T> createOutput(Class<? extends T> dataType, String path, OutputStream stream)
			throws IOException, InterruptedException {
		if (path == null) {
			throw new IllegalArgumentException("path must not be null");
		}
		if (stream == null) {
			throw new IllegalArgumentException("stream must not be null");
		}
		ExcelWriter<T> writer = createWriter();
		writer.initialize(stream);
		return writer;
	}

	protected abstract ExcelWriter<T> createWriter();
}
