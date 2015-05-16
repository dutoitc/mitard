package ch.mno.talend.mitard.out;

/**
 * Created by dutoitc on 15.05.2015.
 */
public enum JsonViolationEnum {

    RATIO_INACTIVE_MUST_BE_MAX_30_PERCENT("More than 1/3 of inactive components.", "Too much inactive components make diagram unreadable and slower for the studio to handle."),
    SERVICE_PORT_MUST_BE_8040("Port for the service must be 8040", "By default, the studio expose services to port 8080, but on a Talend server, the services are exposed as 8040. It is best-practices to keep the same port."),
    AVOID_SYSTEM_OUT("Do not use System.out", "System.out are not easily readable. Replace them by Log4j"),
    COMPONENT_MUST_USE_EXISTING_CONNECTION("Every connected component should share an existing connection (tOracleConnection, tMDMConnection...)", "Shared connection are pooled-optimized, and easier to maintain: you only need to update the connection values instead of checking each components. "),
    COMPONENT_MUST_NOT_CLOSE_CONNECTION("A commit component must not close the connection. Use explicit connection close component instead.", "Asking to close a connection from a component could be an easy way to close a connexion, but it is not easily readable. A connection close component is more readable."),
    MISSING_DOCUMENTATION_PURPOSE("The job 'purpose' documentation must be filled", "It is a best-practise to document job. Fill the purpose field in job parameters."),
    MISSING_DOCUMENTATION_DESCRIPTION("The job 'description' documentation must be filled", "It is a best-practise to document job. Fill the description field in job parameters.");

    private String description;
    private String explanations;

    JsonViolationEnum(String description, String explanations) {
        this.description = description;
        this.explanations = explanations;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExplanations() {
        return explanations;
    }

    public void setExplanations(String explanations) {
        this.explanations = explanations;
    }


}
