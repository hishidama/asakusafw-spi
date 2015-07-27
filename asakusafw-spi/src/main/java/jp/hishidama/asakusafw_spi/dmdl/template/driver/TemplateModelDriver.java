package jp.hishidama.asakusafw_spi.dmdl.template.driver;

import java.util.HashMap;
import java.util.Map;

import jp.hishidama.asakusafw_spi.dmdl.template.driver.TemplateModelTrait.Configuration;

import com.asakusafw.dmdl.Diagnostic;
import com.asakusafw.dmdl.Diagnostic.Level;
import com.asakusafw.dmdl.model.AstAttribute;
import com.asakusafw.dmdl.model.AstAttributeElement;
import com.asakusafw.dmdl.model.AstAttributeValue;
import com.asakusafw.dmdl.model.AstAttributeValueArray;
import com.asakusafw.dmdl.model.AstLiteral;
import com.asakusafw.dmdl.model.LiteralKind;
import com.asakusafw.dmdl.semantics.DmdlSemantics;
import com.asakusafw.dmdl.semantics.ModelDeclaration;
import com.asakusafw.dmdl.spi.ModelAttributeDriver;
import com.asakusafw.dmdl.util.AttributeUtil;

public class TemplateModelDriver extends ModelAttributeDriver {

	/**
	 * The attribute name.
	 */
	public static final String TARGET_NAME = "template"; //$NON-NLS-1$

	/**
	 * The element name of {@value} .
	 */
	public static final String ELEMENT_ID = "id"; //$NON-NLS-1$

	/**
	 * The element name of {@value} .
	 */
	public static final String ELEMENT_TEMPLATE = "template"; //$NON-NLS-1$

	/**
	 * The element name of {@value} .
	 */
	public static final String ELEMENT_CATEGORY = "category"; //$NON-NLS-1$

	/**
	 * The element name of {@value} .
	 */
	public static final String ELEMENT_TYPE_NAME_PATTERN = "type_name_pattern"; //$NON-NLS-1$

	/**
	 * The element name of {@value} .
	 */
	public static final String ELEMENT_ARGS = "args"; //$NON-NLS-1$

	@Override
	public String getTargetName() {
		return TARGET_NAME;
	}

	@Override
	public void process(DmdlSemantics environment, ModelDeclaration declaration, AstAttribute attribute) {
		Map<String, AstAttributeElement> elements = AttributeUtil.getElementMap(attribute);
		Configuration conf = analyzeConfig(environment, attribute, elements);
		if (conf != null) {
			TemplateModelTraits traits = declaration.getTrait(TemplateModelTraits.class);
			if (traits == null) {
				traits = new TemplateModelTraits();
				declaration.putTrait(TemplateModelTraits.class, traits);
			}
			traits.add(new TemplateModelTrait(attribute, conf));
		}
	}

	private Configuration analyzeConfig(DmdlSemantics environment, AstAttribute attribute,
			Map<String, AstAttributeElement> elements) {
		AstLiteral id = take(environment, elements, ELEMENT_ID, LiteralKind.STRING);
		AstLiteral template = take(environment, elements, ELEMENT_TEMPLATE, LiteralKind.STRING);
		AstLiteral category = take(environment, elements, ELEMENT_CATEGORY, LiteralKind.STRING);
		AstLiteral typeNamePattern = take(environment, elements, ELEMENT_TYPE_NAME_PATTERN, LiteralKind.STRING);
		Map<String, String> args = takeMap(environment, elements, ELEMENT_ARGS);
		environment.reportAll(AttributeUtil.reportInvalidElements(attribute, elements.values()));

		Configuration result = new Configuration();
		if (id != null) {
			result.setId(id.toStringValue());
		}
		if (checkNotEmpty(environment, ELEMENT_TEMPLATE, template)) {
			result.setTemplate(template.toStringValue());
		}
		if (checkNotEmpty(environment, ELEMENT_CATEGORY, category)) {
			result.setCategory(category.toStringValue());
		}
		if (checkNotEmpty(environment, ELEMENT_TYPE_NAME_PATTERN, typeNamePattern)) {
			result.setTypeNamePattern(typeNamePattern.toStringValue());
		}
		result.setArgs(args);
		return result;
	}

	private boolean checkNotEmpty(DmdlSemantics environment, String name, AstLiteral stringLiteral) {
		assert environment != null;
		assert name != null;
		assert stringLiteral.kind == LiteralKind.STRING;
		if (stringLiteral == null) {
			environment.report(new Diagnostic(Level.ERROR, stringLiteral, "empty element", TARGET_NAME, name));
			return false;
		}
		if (stringLiteral.toStringValue().isEmpty()) {
			environment.report(new Diagnostic(Level.ERROR, stringLiteral, "empty element", TARGET_NAME, name));
			return false;
		}
		return true;
	}

	private AstLiteral take(DmdlSemantics environment, Map<String, AstAttributeElement> elements, String elementName,
			LiteralKind kind) {
		assert environment != null;
		assert elements != null;
		assert elementName != null;
		assert kind != null;
		AstAttributeElement element = elements.remove(elementName);
		if (element == null) {
			return null;
		}
		if (!(element.value instanceof AstLiteral)) {
			environment.report(new Diagnostic(Level.ERROR, element, "invalid LiteralKind", TARGET_NAME, elementName,
					kind));
			return null;
		}
		AstLiteral literal = (AstLiteral) element.value;
		if (literal.kind != kind) {
			environment.report(new Diagnostic(Level.ERROR, element, "invalid LiteralKind", TARGET_NAME, elementName,
					kind));
			return null;
		}
		return literal;
	}

	private Map<String, String> takeMap(DmdlSemantics environment, Map<String, AstAttributeElement> elements,
			String elementName) {
		assert environment != null;
		assert elements != null;
		assert elementName != null;
		AstAttributeElement element = elements.remove(elementName);
		if (element == null) {
			return null;
		}
		if (!(element.value instanceof AstAttributeValueArray)) {
			environment.report(new Diagnostic(Level.ERROR, element, "invalid AstAttributeValueArray", TARGET_NAME,
					elementName));
			return null;
		}
		AstAttributeValueArray array = (AstAttributeValueArray) element.value;
		Map<String, String> map = new HashMap<>();
		for (AstAttributeValue attribute : array.elements) {
			if (!(attribute instanceof AstLiteral)) {
				environment.report(new Diagnostic(Level.ERROR, attribute, "invalid LiteralKind", TARGET_NAME,
						elementName));
				return null;
			}
			AstLiteral literal = (AstLiteral) attribute;
			String s = literal.toStringValue();
			int n = s.indexOf('=');
			if (n < 0) {
				environment.report(new Diagnostic(Level.ERROR, literal, "invalid =", TARGET_NAME, elementName));
				return null;
			}
			String key = s.substring(0, n).trim();
			String value = s.substring(n + 1);
			map.put(key, value);
		}
		return map;
	}
}
