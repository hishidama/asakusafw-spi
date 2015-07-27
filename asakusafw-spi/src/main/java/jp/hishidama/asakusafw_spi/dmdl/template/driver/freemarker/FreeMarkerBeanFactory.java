package jp.hishidama.asakusafw_spi.dmdl.template.driver.freemarker;

import com.asakusafw.dmdl.semantics.ModelDeclaration;

import jp.hishidama.asakusafw_spi.dmdl.template.driver.beans.TemplateBeanFactory;
import jp.hishidama.asakusafw_spi.dmdl.template.driver.beans.TemplateModelBean;
import jp.hishidama.asakusafw_spi.dmdl.template.driver.beans.TemplateRootBean;

public class FreeMarkerBeanFactory extends TemplateBeanFactory {

	@Override
	public TemplateRootBean createRootBean() {
		return new FreeMarkerRootBean();
	}

	@Override
	public TemplateModelBean createModelBean(TemplateRootBean root, ModelDeclaration model) {
		return new FreeMarkerModelBean(root, model);
	}
}
