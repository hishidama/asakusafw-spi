package jp.hishidama.asakusafw_spi.dmdl.template.driver;

import com.asakusafw.dmdl.model.AstNode;
import com.asakusafw.dmdl.semantics.Trait;

public class TemplateFieldTrait implements Trait<TemplateFieldTrait> {

	private final AstNode originalAst;

	private final Configuration configuration;

	public TemplateFieldTrait(AstNode originalAst, Configuration configuration) {
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
		private String role;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}
	}
}
