package jp.hishidama.asakusafw_spi.dmdl.template.driver;

import java.util.ArrayList;

import com.asakusafw.dmdl.model.AstNode;
import com.asakusafw.dmdl.semantics.Trait;

public class TemplateModelTraits extends ArrayList<TemplateModelTrait> implements Trait<TemplateModelTraits> {

	private static final long serialVersionUID = -7778886884453096977L;

	@Override
	public AstNode getOriginalAst() {
		return null;
	}
}
