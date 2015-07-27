package jp.hishidama.asakusafw_spi.dmdl.template.driver.beans;

import java.util.Map;

import jp.hishidama.asakusafw_spi.dmdl.template.driver.TemplateModelTrait;

import com.asakusafw.dmdl.java.emitter.EmitContext;
import com.asakusafw.dmdl.semantics.ModelDeclaration;
import com.asakusafw.utils.java.model.syntax.ModelFactory;
import com.asakusafw.utils.java.model.syntax.PackageDeclaration;

public class TemplateRootBean {
	private TemplateBeanFactory factory;
	private EmitContext context;
	private ModelDeclaration model;
	private TemplateModelTrait trait;
	private String rootDir;

	public void initialize(TemplateBeanFactory factory, EmitContext context, ModelDeclaration model,
			TemplateModelTrait trait, String rootDir) {
		this.factory = factory;
		this.context = context;
		this.model = model;
		this.trait = trait;
		this.rootDir = rootDir;
	}

	public TemplateBeanFactory factory() {
		return factory;
	}

	public EmitContext emitContext() {
		return context;
	}

	public ModelDeclaration modelDeclaration() {
		return model;
	}

	public TemplateModelTrait modelTrait() {
		return trait;
	}

	public String getRootDir() {
		return rootDir;
	}

	public String getPackageName() {
		ModelFactory f = context.getModelFactory();
		PackageDeclaration packageName = f.newPackageDeclaration(context.getQualifiedTypeName().getQualifier());
		return packageName.getName().toNameString();
	}

	public String getClassName() {
		return context.getTypeName().getToken();
	}

	private TemplateModelBean modelBean;

	public TemplateModelBean getModel() {
		if (modelBean == null) {
			modelBean = factory().createModelBean(this, model);
		}
		return modelBean;
	}

	public TemplateModelBean find(String modelName) {
		ModelDeclaration model = emitContext().getSemantics().findModelDeclaration(modelName);
		if (model != null) {
			return new TemplateModelBean(this, model);
		}
		return null;
	}

	public String getArg(String key) {
		Map<String, String> args = trait.getConfiguration().getArgs();
		if (args == null) {
			return null;
		}
		return args.get(key.trim());
	}
}
