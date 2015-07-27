package jp.hishidama.asakusafw_spi.dmdl.template.spi;

import java.io.IOException;
import java.io.PrintWriter;

import jp.hishidama.asakusafw_spi.dmdl.template.driver.TemplateModelTrait;
import jp.hishidama.asakusafw_spi.dmdl.template.driver.beans.TemplateBeanFactory;
import jp.hishidama.asakusafw_spi.dmdl.template.driver.beans.TemplateRootBean;

import com.asakusafw.dmdl.java.emitter.EmitContext;
import com.asakusafw.dmdl.semantics.ModelDeclaration;
import com.asakusafw.utils.java.model.syntax.ModelFactory;
import com.asakusafw.utils.java.model.syntax.PackageDeclaration;
import com.asakusafw.utils.java.model.util.Emitter;

public abstract class TemplateGenerator {

	protected EmitContext context;
	protected ModelDeclaration model;
	protected TemplateModelTrait trait;
	protected String rootDir;

	private TemplateRootBean rootBean;

	public void initialize(EmitContext context, ModelDeclaration model, TemplateModelTrait trait, String dir) {
		this.context = context;
		this.model = model;
		this.trait = trait;
		this.rootDir = dir;
		this.rootBean = null;
	}

	public abstract boolean accept();

	public void emit() throws IOException {
		ModelFactory f = context.getModelFactory();
		PackageDeclaration packageName = f.newPackageDeclaration(context.getQualifiedTypeName().getQualifier());
		String fileName = getRootBean().getClassName() + ".java";

		Emitter emitter = context.getConfiguration().getOutput();
		try (PrintWriter writer = emitter.openFor(packageName, fileName)) {
			emit(writer);
		}
	}

	protected abstract void emit(PrintWriter writer) throws IOException;

	protected final TemplateRootBean getRootBean() {
		if (rootBean == null) {
			TemplateBeanFactory factory = createFactory();
			rootBean = factory.createRootBean();
			rootBean.initialize(factory, context, model, trait, rootDir);
		}
		return rootBean;
	}

	protected abstract TemplateBeanFactory createFactory();
}
