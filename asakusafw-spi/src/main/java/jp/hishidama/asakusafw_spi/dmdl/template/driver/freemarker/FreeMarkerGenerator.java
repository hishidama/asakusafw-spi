package jp.hishidama.asakusafw_spi.dmdl.template.driver.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import jp.hishidama.asakusafw_spi.dmdl.template.driver.beans.TemplateBeanFactory;
import jp.hishidama.asakusafw_spi.dmdl.template.driver.beans.TemplateRootBean;
import jp.hishidama.asakusafw_spi.dmdl.template.spi.TemplateGenerator;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerGenerator extends TemplateGenerator {

	@Override
	public boolean accept() {
		String template = trait.getConfiguration().getTemplate();
		return template.endsWith(".ftl");
	}

	@Override
	protected void emit(PrintWriter writer) throws IOException {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
		cfg.setDirectoryForTemplateLoading(new File(rootDir));
		cfg.setDefaultEncoding("UTF-8");

		TemplateRootBean dataModel = getRootBean();

		Template template = cfg.getTemplate(trait.getConfiguration().getTemplate());
		try {
			template.process(dataModel, writer);
		} catch (TemplateException e) {
			throw new IOException(e);
		}
	}

	@Override
	protected TemplateBeanFactory createFactory() {
		return new FreeMarkerBeanFactory();
	}
}
