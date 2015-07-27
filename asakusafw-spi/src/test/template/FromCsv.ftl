package ${packageName};

/**
 * ${model.description!""}
 */
public class ${className} extends Abstract${model.camelName}CsvInputDescription {

    @Override
    public String getBasePath() {
        return "master/${model.name}";
    }

    @Override
    public String getResourcePattern() {
        return "${model.name}.csv";
    }

    @Override
    public DataSize getDataSize() {
        return DataSize.${arg("dataSize")!"UNKOWN"};
    }
}
