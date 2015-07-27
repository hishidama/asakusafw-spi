package jp.hishidama.asakusafw_spi.dmdl.template.driver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.hishidama.asakusafw_spi.dmdl.template.driver.TemplateModelTrait.Configuration;
import jp.hishidama.asakusafw_spi.dmdl.template.spi.TemplateGenerator;
import sun.misc.Service;

import com.asakusafw.dmdl.Region;
import com.asakusafw.dmdl.java.emitter.EmitContext;
import com.asakusafw.dmdl.java.spi.JavaDataModelDriver;
import com.asakusafw.dmdl.model.AstNode;
import com.asakusafw.dmdl.semantics.ModelDeclaration;

public class TemplateEmitter extends JavaDataModelDriver {

	@Override
	public void generateResources(EmitContext context, ModelDeclaration model) throws IOException {
		TemplateModelTraits traits = model.getTrait(TemplateModelTraits.class);
		if (traits == null) {
			return;
		}

		String dir = getSourceDirectory(model.getOriginalAst());
		List<TemplateGenerator> generators = getGenerators();

		for (TemplateModelTrait trait : traits) {
			Configuration config = trait.getConfiguration();
			String categoryName = config.getCategory();
			String typeNamePattern = config.getTypeNamePattern();
			EmitContext next = new EmitContext(context.getSemantics(), context.getConfiguration(), model, categoryName,
					typeNamePattern);

			boolean accepted = false;
			for (TemplateGenerator generator : generators) {
				generator.initialize(next, model, trait, dir);
				if (generator.accept()) {
					generator.emit();
					accepted = true;
					break;
				}
			}
			if (!accepted) {
				// TODO エラー出力
				System.out.println("+++TemplateEmitter error+++" + trait.getOriginalAst());
			}
		}
	}

	private String getSourceDirectory(AstNode ast) {
		Region region = ast.getRegion();
		String path = new File(region.sourceFile).getAbsolutePath().replace('\\', '/');
		int n = path.lastIndexOf("/src/");
		if (n >= 0) {
			path = path.substring(0, n);
		}
		return path;
	}

	private List<TemplateGenerator> generatorList;

	@SuppressWarnings("unchecked")
	private List<TemplateGenerator> getGenerators() {
		if (generatorList == null) {
			generatorList = new ArrayList<>();
			for (Iterator<TemplateGenerator> i = Service.providers(TemplateGenerator.class); i.hasNext();) {
				generatorList.add(i.next());
			}
		}
		return generatorList;
	}
}
