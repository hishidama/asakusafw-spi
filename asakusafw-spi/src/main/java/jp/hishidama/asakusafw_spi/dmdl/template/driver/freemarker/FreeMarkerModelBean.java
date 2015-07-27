package jp.hishidama.asakusafw_spi.dmdl.template.driver.freemarker;

import java.util.List;

import com.asakusafw.dmdl.semantics.ModelDeclaration;

import jp.hishidama.asakusafw_spi.dmdl.template.driver.beans.TemplateModelBean;
import jp.hishidama.asakusafw_spi.dmdl.template.driver.beans.TemplateRootBean;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

public class FreeMarkerModelBean extends TemplateModelBean {

	public FreeMarkerModelBean(TemplateRootBean root, ModelDeclaration model) {
		super(root, model);
	}

	private TemplateMethodModelEx rolePropertiesFunction;

	public TemplateMethodModelEx getRoleProperties() {
		if (rolePropertiesFunction == null) {
			rolePropertiesFunction = new TemplateMethodModelEx() {
				@Override
				public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
					for (Object obj : arguments) {
						TemplateScalarModel arg = (TemplateScalarModel) obj;
						String role = arg.getAsString();
						return getRoleProperties(role);
					}
					return null;
				}
			};
		}
		return rolePropertiesFunction;
	}
}
