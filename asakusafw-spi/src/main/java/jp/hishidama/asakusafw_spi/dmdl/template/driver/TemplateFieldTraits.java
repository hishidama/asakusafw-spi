package jp.hishidama.asakusafw_spi.dmdl.template.driver;

import java.util.ArrayList;

import com.asakusafw.dmdl.model.AstNode;
import com.asakusafw.dmdl.semantics.Trait;

public class TemplateFieldTraits extends ArrayList<TemplateFieldTrait> implements Trait<TemplateFieldTraits> {

	private static final long serialVersionUID = 5779642711214778855L;

	@Override
	public AstNode getOriginalAst() {
		return null;
	}
}
