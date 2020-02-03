package ch.mno.talend.mitard.readers.treaders;

/**
 * Created by dutoitc on 04.07.2019.
 */
public class TReaderFactory {

    // Note: keep list instead of default to enforce adding thoses types or ignoring them manually
    public static AbstractTReader build(String componentName) {
        switch (componentName) {
            case "tRESTRequest":
                return new TRestRequestReader(componentName);
            case "tESBProviderRequest":
                return new TESBProviderRequestReader(componentName);
            case "tESBConsumer":
                return new TESBConsumerReader(componentName);
            case "tRunJob":
                return new TRunJobReader(componentName);
            case "cTalendJob":
                return new CTalendJobReader(componentName);
            case "tOracleCommit":
                return new TOracleCommitReader(componentName);
            case "tMDMCommit":
                return new TMDMCommitReader(componentName);
            case "tJava":
                return new TJavaReader(componentName);
            case "tJavaRow":
                return new TJavaRowReader(componentName);
            case "tJavaFlex":
                return new TJavaFlexReader(componentName);
            case "tBonitaInstantiateProcess":
                return new TBonitaInstanciateProcessReader(componentName);
            case "tDie":
                return new TDieReader(componentName);
            case "tFixedFlowInput":
                return new TFixedFlowInputReader(componentName);
            case "tOracleInput":
                return new TOracleInputReader(componentName);
            case "tOracleOutput":
                return new TOracleOutputReader(componentName);
            case "tMDMConnection":
                return new TMDMConnectionReader(componentName);
            case "tREST":
                return new TRest(componentName);
            case "tRESTClient":
                return new TRestClient(componentName);
            case "tHttpRequest":
                return new THTTPRequest(componentName);
            case "tSOAP":
                return new TSoap(componentName);
            case "tOracleConnection":
                return new TOracleConnection(componentName);
            case "tOracleRollback":
                return new TOracleRollback(componentName);
            case "tPrejob":
                return new TPrejob(componentName);
            case "tOracleClose":
                return new TOracleClose(componentName);
            case "tMDMClose":
                return new TMDMClose(componentName);
            case "tMDMRollback":
                return new TMDMRollback(componentName);
            case "tOracleRow":
                return new TORacleRow(componentName);
            case "tMDMInput":
                return new TMDMInput(componentName);
            case "tMDMOutput":
                return new TMDMOutput(componentName);
            case "tMDMViewSearch":
                return new TMDMViewSearch(componentName);
            case "tMDMRestInput":
                return new TMDMRestInput(componentName);
            case "tFileOutputJSON":
            case "tFileInputPositional":
            case "tLogRow":
            case "tXMLMap":
            case "tFlowToIterate":
            case "tMysqlInput":
            case "tRowGenerator":
            case "tMysqlOutput":
            case "tESBProviderResponse":
            case "tRESTResponse":
            case "tPostjob":
            case "tMDMInputNullOptional":
            case "tFileInputRaw":
            case "tHashInput":
            case "tRouteOutput":
            case "tMomOutput":
            case "tFileOutputXML":
            case "tESBProviderFault":
            case "tMap":
            case "tHashOutput":
            case "tExtractXMLField":
            case "tSetGlobalVar":
            case "tConvertType":
            case "tLibraryLoad":
            case "tAssertCatcher":
            case "tLogCatcher":
            case "tFileOutputMSXML":
            case "tNormalize":
            case "RCEntLog4jLogger":
            case "tUniqRow":
            case "tUnite":
            case "tAggregateRow":
            case "tBufferInput":
            case "tFilterRow":
            case "tBufferOutput":
            case "tLogXML":
            case "tXMLInsert":
            case "tRouteInput":
            case "tWarn":
            case "tStatCatcher":
            case "tAssert":
            case "tMomRollback":
            case "tReplicate":
            case "tWriteXMLField":
            case "tCreateTable":
            case "tExtractJSONFields":
            case "tSendMail":
            case "tFilterColumns":
            case "tFlowMeterCatcher":
            case "tFileCopy":
            case "tSleep":
            case "tMomConnection":
            case "tFileDelete":
            case "tFileInputXML":
            case "tCreateTemporaryFile":
            case "tOracleTableList":
            case "tMDMTriggerInput":
            case "tParallelize":
            case "tContextLoad":
            case "tLoop":
            case "tMDMDelete":
            case "tFileOutputDelimited":
            case "tSortRow":
            case "tFileInputDelimited":
            case "tFileList":
            case "tXSDValidator":
            case "cJMS":
            case "cMessageRouter":
            case "cMessagingEndpoint":
            case "cLog":
            case "cConfig":
            case "cSetHeader":
            case "cFile":
            case "cBeanRegister":
            case "cConvertBodyTo":
            case "cOnException":
            case "cMQConnectionFactory":
            case "cJavaDSLProcessor":
            case "cProcessor":
            case "tChronometerStart":
            case "tChronometerStop":
            case "tFileOutputExcel":
            case "tFileInputExcel":
            case "tJoin":
            case "tWriteJSONField":
            case "tSynonymSearch":
            case "tMultiPatternCheck":
            case "tSampleRow":
            case "tMDMBulkLoad":
            case "tSplitRow":
            case "tAdvancedFileOutputXML":
            case "tDenormalize":
            case "tMomCommit":
            case "cCXFRS":
            case "cSetBody":
            case "cHttp":
            case "tFileOutputRaw":
            case "tFileInputFullRow":
            case "tFileExist":
            case "tSynonymOutput":
            case "tASsert":
            case "tMDMReceive":
            case "tSchemaComplianceCheck":
            case "tInfiniteLoop":
            case "tFileArchive":
            case "tFileFetch":
            case "tExtractRegexFields":
            case "tIterateToFlow":
            case "tParseRecordSet":
            case "tReplace":
            case "tJDBCConnection":
            case "tJDBCClose":
            case "tJDBCRollback":
            case "tJDBCInput":
            case "tJDBCOutput":
            case "tJDBCCommit":
            case "cREST": // Note: URL could be read and correlated with service
                return new TNodeReader(componentName);
            default:
                return null;
        }
    }

}
