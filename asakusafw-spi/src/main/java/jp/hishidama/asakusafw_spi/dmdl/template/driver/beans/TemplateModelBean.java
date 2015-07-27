package jp.hishidama.asakusafw_spi.dmdl.template.driver.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.asakusafw.dmdl.model.AstDescription;
import com.asakusafw.dmdl.semantics.ModelDeclaration;
import com.asakusafw.dmdl.semantics.PropertyDeclaration;

public class TemplateModelBean extends AbstractTemplateBean {

	protected final ModelDeclaration model;

	public TemplateModelBean(TemplateRootBean root, ModelDeclaration model) {
		super(root);
		this.model = model;
	}

	public String getName() {
		return model.getName().identifier;
	}

	public String getCamelName() {
		return TemplateBeanUtil.toCamelCase(getName());
	}

	public String getDescription() {
		AstDescription d = model.getDescription();
		if (d == null) {
			return null;
		}
		return d.getText();
	}

	public List<TemplatePropertyBean> getProperties() {
		List<PropertyDeclaration> list = model.getDeclaredProperties();
		List<TemplatePropertyBean> result = new ArrayList<>(list.size());
		for (PropertyDeclaration d : list) {
			result.add(factory().createPropertyBean(rootBean, d));
		}
		return result;
	}

	public List<TemplatePropertyBean> getRoleProperties(String role) {
		if (role == null) {
			return Collections.emptyList();
		}
		List<TemplatePropertyBean> list = getProperties();
		List<TemplatePropertyBean> result = new ArrayList<>(list.size());
		for (TemplatePropertyBean property : list) {
			if (role.equals(property.getRole())) {
				result.add(property);
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return getName();
	}
}
