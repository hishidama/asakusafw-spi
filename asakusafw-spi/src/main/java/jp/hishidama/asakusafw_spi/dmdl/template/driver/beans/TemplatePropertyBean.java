package jp.hishidama.asakusafw_spi.dmdl.template.driver.beans;

import jp.hishidama.asakusafw_spi.dmdl.template.driver.TemplateFieldTrait;
import jp.hishidama.asakusafw_spi.dmdl.template.driver.TemplateFieldTraits;

import com.asakusafw.dmdl.model.AstDescription;
import com.asakusafw.dmdl.semantics.PropertyDeclaration;

public class TemplatePropertyBean extends AbstractTemplateBean {

	protected final PropertyDeclaration declaration;

	public TemplatePropertyBean(TemplateRootBean root, PropertyDeclaration declaration) {
		super(root);
		this.declaration = declaration;
	}

	public String getName() {
		return declaration.getName().identifier;
	}

	public String getCamelName() {
		return TemplateBeanUtil.toCamelCase(getName());
	}

	public String getDescription() {
		AstDescription d = declaration.getDescription();
		if (d == null) {
			return null;
		}
		return d.getText();
	}

	private TemplateTypeBean typeBean;

	public TemplateTypeBean getType() {
		if (typeBean == null) {
			typeBean = factory().createTypeBean(rootBean, declaration.getType());
		}
		return typeBean;
	}

	public String getJavaType() {
		return getType().getJavaClass().getSimpleName();
	}

	public String getJavaTypeAs() {
		String type = getType().getName();
		if ("TEXT".equals(type)) {
			return "String";
		} else {
			return getJavaType();
		}
	}

	public String getOptionType() {
		return getType().getOptionClass().getSimpleName();
	}

	public String getGetter() {
		return "get" + getCamelName();
	}

	public String getSetter() {
		return "set" + getCamelName();
	}

	public String getGetterAs() {
		String type = getType().getName();
		if ("TEXT".equals(type)) {
			return getGetterAsString();
		} else {
			return getGetter();
		}
	}

	public String getSetterAs() {
		String type = getType().getName();
		if ("TEXT".equals(type)) {
			return getSetterAsString();
		} else {
			return getSetter();
		}
	}

	public String getGetterOption() {
		return "get" + getCamelName() + "Option";
	}

	public String getSetterOption() {
		return "set" + getCamelName() + "Option";
	}

	public String getGetterAsString() {
		return "get" + getCamelName() + "AsString";
	}

	public String getSetterAsString() {
		return "set" + getCamelName() + "AsString";
	}

	public String getRole() {
		TemplateFieldTraits traits = declaration.getTrait(TemplateFieldTraits.class);
		if (traits == null) {
			return null;
		}

		String modelId = modelTrait().getConfiguration().getId();
		for (TemplateFieldTrait trait : traits) {
			String id = trait.getConfiguration().getId();
			if (modelId == null || id == null || modelId.equals(id)) {
				return trait.getConfiguration().getRole();
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return getName();
	}
}