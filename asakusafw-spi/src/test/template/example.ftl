package ${packageName};

public class ${className} {
<#list model.properties as p>
	// ${p.description} ${p.name} ${p.type} ${p.javaType}
</#list>
<#if arg("size") ??>
	int size = ${arg("size")};
</#if>
<#if arg("size2") ??>
	int size2 = ${arg("size2")};
</#if>
}
