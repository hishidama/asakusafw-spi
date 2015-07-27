package jp.hishidama.asakusafw_spi.dmdl.template.driver;

import java.util.Map;

import jp.hishidama.asakusafw_spi.dmdl.template.driver.TemplateFieldTrait.Configuration;

import com.asakusafw.dmdl.model.AstAttribute;
import com.asakusafw.dmdl.model.AstAttributeElement;
import com.asakusafw.dmdl.semantics.DmdlSemantics;
import com.asakusafw.dmdl.semantics.PropertyDeclaration;
import com.asakusafw.dmdl.spi.PropertyAttributeDriver;
import com.asakusafw.dmdl.util.AttributeUtil;

public class TemplateFieldDriver extends PropertyAttributeDriver {

	/**
	 * The attribute name.
	 */
	public static final String TARGET_NAME = TemplateModelDriver.TARGET_NAME + ".field"; //$NON-NLS-1$

	/**
	 * The element name of {@value} .
	 */
	public static final String ELEMENT_ID = "id"; //$NON-NLS-1$

	/**
	 * The element name of {@value} .
	 */
	public static final String ELEMENT_ROLE = "role"; //$NON-NLS-1$

	@Override
	public String getTargetName() {
		return TARGET_NAME;
	}

	@Override
	public void process(DmdlSemantics environment, PropertyDeclaration declaration, AstAttribute attribute) {
		Map<String, AstAttributeElement> elements = AttributeUtil.getElementMap(attribute);
		Configuration conf = analyzeConfig(environment, attribute, elements);
		if (conf != null) {
			TemplateFieldTraits traits = declaration.getTrait(TemplateFieldTraits.class);
			if (traits == null) {
				traits = new TemplateFieldTraits();
				declaration.putTrait(TemplateFieldTraits.class, traits);
			}
			traits.add(new TemplateFieldTrait(attribute, conf));
		}
	}

	private Configuration analyzeConfig(DmdlSemantics environment, AstAttribute attribute,
			Map<String, AstAttributeElement> elements) {
		String id = AttributeUtil.takeString(environment, attribute, elements, ELEMENT_ID, false);
		String role = AttributeUtil.takeString(environment, attribute, elements, ELEMENT_ROLE, true);
		environment.reportAll(AttributeUtil.reportInvalidElements(attribute, elements.values()));

		Configuration result = new Configuration();
		result.setId(id);
		result.setRole(role);
		return result;
	}
}
