package jp.hishidama.asakusafw_spi.dmdl.template.driver.beans;

import com.asakusafw.dmdl.semantics.ModelDeclaration;
import com.asakusafw.dmdl.semantics.PropertyDeclaration;
import com.asakusafw.dmdl.semantics.Type;

public abstract class TemplateBeanFactory {

	public TemplateRootBean createRootBean() {
		return new TemplateRootBean();
	}

	public TemplateModelBean createModelBean(TemplateRootBean root, ModelDeclaration model) {
		return new TemplateModelBean(root, model);
	}

	public TemplatePropertyBean createPropertyBean(TemplateRootBean root, PropertyDeclaration declaration) {
		return new TemplatePropertyBean(root, declaration);
	}

	public TemplateTypeBean createTypeBean(TemplateRootBean root, Type type) {
		return new TemplateTypeBean(root, type);
	}
}
