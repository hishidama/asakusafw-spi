package ${packageName};

/**
 * ${model.description!""}
 */
public class ${className} extends Abstract${model.camelName}CsvOutputDescription {

    @Override
    public String getBasePath() {
        return "master/${model.name}";
    }

    @Override
    public String getResourcePattern() {
        return "${model.name}.csv";
    }
<#assign keys = model.roleProperties("key")>
<#if keys?has_content>

    @Override
    public List<String> getOrder() {
        return Arrays.asList(<#list keys as key><#if key_index!=0>, </#if>"${key}"</#list>);
    }
</#if>
}
