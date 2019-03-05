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
    MISSING_DOCUMENTATION_DESCRIPTION("The job 'description' documentation must be filled", "It is a best-practise to document job. Fill the description field in job parameters."),
    LOGCATCHER_MUST_NOT_CHAIN_TDIE("The tLogCatcher component must not be chained to a tDie", "a TDie will be catched by a tLogCatcher, which will recall the tDie, etc... in an eternal loop."),
    SERVICE_MUST_NOT_SET_DB_CONNECTION_IN_PREJOB("A job with listener must not set a DB Connection in a tPrejob", "Setting a DB connection in a tPrejob will only work for lucky times: if the database is shutdown, then again up, the connection is lost, and your job will crash on a closed connection."),
    FIRECREATEEVENT_MUST_BE_SET("A tMDMOutput must have fireEvent set (WITHREPORT)", ""),
    TOO_MUCH_COMPONENTS("Too much components (>50)", "Talend recommands 15 job maximum per process"),
    FAR_TOO_MUCH_COMPONENTS("Too much components (>100)", "Talend recommands 15 job maximum per process"),
    TRUNJOB_MUST_PROPAGATE_CHILD_RESULT("a tRunJob should propagate child result", "Child result propagation COULD happen if the schema is the same, but not always. It is a best-practise to propagate child result."),
    UNSTABLE_FILES("missing files for a component, 3 needed", "a component (process) must have 3 files. Some are missing. This could lead in unpredictable behaviour"),
    BPM_SHOULD_NOT_BE_ASYNCHRONOUS("BPM components must be synchronous", "A bug in 5.6.1 is that any async component could lead in sub-bpm run twice. Avoid BPM async as a workaround."),
    MDM_MUST_HAVE_COMMIT("Jobs with tMDMConnection using no autocommit should have explicit tMDMCommit", "A MDM connection with no autocommit and no explicit commit can cause connection problem"),
    MDM_MUST_HAVE_ROLLBACK("Jobs with tMDMConnection using no autocommit should have explicit tMDMRollback", "A MDM connection with no autocommit and no explicit rollback can cause connection leak in DB"),
    ROLLBACK_MUST_NOT_BE_DEFINED_ON_AUTOCOMMIT_CONNECTION("tOracleRollback must not be linked to a tOracleConnection with autocommit.", "Rollback could not be executed on connections with autocommit"),
    NON_AUTOCOMMIT_CONNECTION_MUST_HAVE_COMMIT_OR_ROLLBACK("tOracleConnection without autocommit must have a commit or rollback", "If a tOracleConnection has no commit/rollback, the connection will never be closed, leading to a connection leak"),
    AVOID_MDMCONNECTION_IN_SERVICE_PREJOB("Services should not open a MDM connection", "Opening a MDM connection in a prejob of a service will cause a connection sticked to the process. In Karaf, one service could be deployed multiple times, so multiple threads will be kept on the DB."),
    AVOID_DBCONNECTION_IN_SERVICE_PREJOB("Services should not open a DB connection", ""),
    MDMCONNECTION_MUST_BE_CLOSED("A connection opened by a tMDMConnection must be followed by a (commit+close) or (rollback+close)", ""),
    DBCONNECTION_MUST_BE_CLOSED("A connection opened by a tOracleConnection must be followed by a (commit+close) or (rollback+close)", ""),
    ORACLE_CONNECTION_SHOULD_USE_DATASOURCE("A tOracleConnection SHOULD use a datasource", "Using a specific connection to a database means injecting user/password and multiplying connections for each jobs, which could lead to a connection number too big. Using a datasource will delegate/centralize authentification and connections handling"),
    ORACLE_COMPONENT_SHOULD_USE_DATASOURCE_OR_TORACLECONNECTION("an oracle component SHOULD use a datasource or a tOracleConnection", "Defining user/password in jobs is potentialy dangerous, as values could be forgotten and not updated, leading to account lock. Also using datasource is a best-practice to centralize connections management."),
    COMPONENT_SHOULD_NOT_USE_ONCOMPONENTOK_WITH_FLOW_TRANSITION("a component should not define a 'onComponentOk' when having a flow transition", "The onComponentOk will execute before the flow executes, leading in miscomprehension"),
    AVOID_USING_STATCATCHER ("statCatcher should not be used.","statCatcher writes to SAM, which is unperfect, lead to big DB and little overhead. Avoid.");



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
