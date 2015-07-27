package jp.hishidama.asakusafw_spi.dmdl.template.driver.beans;

import jp.hishidama.asakusafw_spi.dmdl.template.driver.TemplateModelTrait;

import com.asakusafw.dmdl.java.emitter.EmitContext;

public abstract class AbstractTemplateBean {

	protected final TemplateRootBean rootBean;

	public AbstractTemplateBean(TemplateRootBean rootBean) {
		this.rootBean = rootBean;
	}

	public final TemplateRootBean rootBean() {
		return rootBean;
	}

	protected final TemplateBeanFactory factory() {
		return rootBean.factory();
	}

	protected final EmitContext emitContext() {
		return rootBean.emitContext();
	}

	public final TemplateModelTrait modelTrait() {
		return rootBean.modelTrait();
	}
}
