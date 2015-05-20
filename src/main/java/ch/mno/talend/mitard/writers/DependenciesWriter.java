package ch.mno.talend.mitard.writers;

import ch.mno.talend.mitard.data.*;
import ch.mno.talend.mitard.readers.ProcessReader;
import ch.mno.talend.mitard.tools.DotHelper;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class DependenciesWriter extends AbstractWriter {
    public DependenciesWriter(Context context) {
        super(context);
    }
   /* public static void main(String[] args) throws IOException, URISyntaxException {
        final List<Process> processes = new ProcessesWrapper().findLatestsProductionProcesses();        // Write processes

        FileWriter fw = new FileWriter("target/rcent-dependencies.dot");
        fw.write("digraph G{\n");
        for (Process process : processes) {
            fw.write("   " + process.getName() + " [shape=box, style=filled, fillcolor=green, color=green, label=\"" + process.getName() + "\"];\n");
        }
        Pattern patNode = Pattern.compile("<node componentName=\"tRunJob\".*?>(.*?)<\\/node>", Pattern.DOTALL + Pattern.MULTILINE);
        Pattern patProcessType = Pattern.compile("field=\"PROCESS_TYPE\".*?value=\"(.*?)\"");
        Pattern patSoapCall = Pattern.compile("ESB_ENDPOINT.*?services.(.*?)\\&");
        for (Process process : processes) {
            String item = SVNHelper.readURL(process.getSVNPath4Item());
            Matcher matcher = patNode.matcher(item);
            while (matcher.find()) {
                String nodeBody = matcher.group(1);
                Matcher matcher2 = patProcessType.matcher(nodeBody);
                if (matcher2.find()) {
                    fw.write("    " + process.getName() + " -> " + matcher2.group(1) + ";\n");
                }
            }
            Matcher matcher3 = patSoapCall.matcher(item);
            while (matcher3.find()) {
                fw.write("    " + process.getName() + " -> " + matcher3.group(1) + ";\n");
            }
        }
        fw.write("}\n");
        fw.flush();
        fw.close();
    }*/


    /**
     * produce dependencies.dot, dependencies.png depenting on Graphviz'dot
     * @throws JAXBException
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public void write(TalendFiles talendFiles) throws JAXBException, IOException, SAXException, ParserConfigurationException {
        StringBuffer sb = new StringBuffer();
        sb.append("digraph G{\r\n");
        //sb.append("rankdir=\"LR\"\r\n");
        for (TalendFile f : talendFiles.getProcesses()) {
            if (isBlacklisted(f.getName())) continue;
            sb.append("   P_" + f.getName().replace("-", "_") + " [shape=box, style=filled, fillcolor=green, color=green, label=\"Process_" + f.getName() + "\"];\r\n");
        }
        for (TalendFile f : talendFiles.getRoutes()) {
            if (isBlacklisted(f.getName())) continue;
            sb.append("   R_" + f.getName().replace("-", "_") + " [shape=trapezium, style=filled, fillcolor=green, color=green, label=\"Route_" + f.getName() + "\"];\r\n");
        }
        for (TalendFile f : talendFiles.getServices()) {
            if (isBlacklisted(f.getName())) continue;
            sb.append("   S_" + f.getName().replace("-", "_") + " [shape=ellipse, style=filled, fillcolor=green, color=green, label=\"Service_" + f.getName() + "\"];\r\n");
        }

        for (TalendFile file : talendFiles.getProcesses()) {
            if (file.getItemFilename().contains("process")) {
                if (isBlacklisted(file.getName())) continue;
                System.out.println("Reading " + new File(file.getItemFilename()).getName());
                FileInputStream fis = new FileInputStream(file.getItemFilename());
                ProcessType process = ProcessReader.reader(fis);
                for (AbstractNodeType node : process.getNodeList()) {
                    if (node instanceof TESBConsumerType) {
                        String name = file.getName().replace("-", "_");
                        String serviceName = ((TESBConsumerType)node).getServiceName();
                        sb.append("   P_" + name + "->S_" + serviceName+"\r\n");
                    } else  if (node instanceof TESBProviderRequestType) {
                        String name = file.getName().replace("-", "_");
                        String serviceName = ((TESBProviderRequestType)node).getServiceName();
                        sb.append("   P_" + name + "->S_" + serviceName+"\r\n");
                    } else  if (node instanceof TRunJobType) {
                        String name = file.getName().replace("-", "_");
                        String processName = ((TRunJobType)node).getProcessName();
                        sb.append("   P_" + name + "->P_" + processName+"\r\n");

//                    } else  if (node instanceof TRestRequestType) {
//                        String name = file.getName();
//                        String serviceName = ((TRestRequestType)node).getServiceName();
//                        System.out.println("   P_" + name + "->S_" + serviceName);
                    }
                }
            }


            /*JAXBContext jc = JAXBContext.newInstance(ProcessType.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            ProcessType process = (ProcessType) unmarshaller.unmarshal(new File(f.getPropertiesFilename()));
            for (Node node : process.getNode()) {
                if (node.getComponentName().equals("tEsbConsumer")) {
                    for (ElementParameter parameter : node.getElementParameter()) {
                        if (parameter.getName().equals("ESB_ENDPOINT")) {
//                            <elementParameter field="TEXT" name="ESB_ENDPOINT" value="&quot;http://localhost:8200/esb/AirportService&quot;"/>
                            String endpoint = parameter.getValue();
                            int p1 = endpoint.lastIndexOf("/");
                            int p2 = endpoint.lastIndexOf("&");
                            String name = endpoint.substring(p1 + 1, p2);
                            sb.append("    P_" + f.getName() + "->S_" + name + ";\n");
                        }
                    }

                }
            }*/
        }


        sb.append("}\n");

        //File tempFile = File.createTempFile("dottemp","tmp");
        File tempFile = new File(getContext().getProductionPath()+File.separatorChar+"data"+File.separatorChar + "dependencies.dot");
        FileWriter writer = new FileWriter(tempFile);
        IOUtils.write(sb.toString(), writer);
        writer.flush();
        DotHelper.generatePNG(getContext().getDotPath(), getContext().getProductionPath()+File.separatorChar+"data"+ File.separatorChar + "dependencies.png", tempFile);
        //System.out.println(sb.toString());
    }
}