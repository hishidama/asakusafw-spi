package jp.hishidama.asakusafw_spi.dmdl.template.driver;

import java.util.Map;

import com.asakusafw.dmdl.model.AstNode;
import com.asakusafw.dmdl.semantics.Trait;

public class TemplateModelTrait implements Trait<TemplateModelTrait> {

	private final AstNode originalAst;

	private final Configuration configuration;

	public TemplateModelTrait(AstNode originalAst, Configuration configuration) {
		if (configuration == null) {
			throw new IllegalArgumentException("configuration must not be null"); //$NON-NLS-1$
		}
		this.originalAst = originalAst;
		this.configuration = configuration;
	}

	@Override
	public AstNode getOriginalAst() {
		return originalAst;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public static class Configuration {
		private String id;
		private String template;
		private String category;
		private String typeNamePattern;
		private Map<String, String> args;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTemplate() {
			return template;
		}

		public void setTemplate(String template) {
			this.template = template;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getTypeNamePattern() {
			return typeNamePattern;
		}

		public void setTypeNamePattern(String typeNamePattern) {
			this.typeNamePattern = typeNamePattern;
		}

		public Map<String, String> getArgs() {
			return args;
		}

		public void setArgs(Map<String, String> args) {
			this.args = args;
		}
	}
}
