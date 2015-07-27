package jp.hishidama.asakusafw_spi.dmdl.template.driver.freemarker;

import java.util.List;

import jp.hishidama.asakusafw_spi.dmdl.template.driver.beans.TemplateModelBean;
import jp.hishidama.asakusafw_spi.dmdl.template.driver.beans.TemplateRootBean;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

public class FreeMarkerRootBean extends TemplateRootBean {

	private TemplateMethodModelEx findFunction;

	public TemplateMethodModelEx getFind() {
		if (findFunction == null) {
			findFunction = new TemplateMethodModelEx() {
				@Override
				public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
					for (Object obj : arguments) {
						TemplateScalarModel arg = (TemplateScalarModel) obj;
						String modelName = arg.getAsString();
						TemplateModelBean model = find(modelName);
						if (model != null) {
							return model;
						}
					}
					return null;
				}
			};
		}
		return findFunction;
	}

	private TemplateMethodModelEx argFunction;

	public TemplateMethodModelEx getArg() {
		if (argFunction == null) {
			argFunction = new TemplateMethodModelEx() {
				@Override
				public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
					for (Object obj : arguments) {
						TemplateScalarModel arg = (TemplateScalarModel) obj;
						String key = arg.getAsString();
						return getArg(key);
					}
					return null;
				}
			};
		}
		return argFunction;
	}
}
