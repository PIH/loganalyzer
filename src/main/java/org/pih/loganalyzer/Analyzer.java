package org.pih.loganalyzer;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Main class for executing the Activity Analyzer
 */
@Parameters(separators = "=")
public class Analyzer {

    private static final Log log = LogFactory.getLog(Analyzer.class);

    @Parameter(names={"--directory"}, description="The directory that contains the activity log files to analyze")
    private File directory;

    @Parameter(names={"--type"}, description="The type of file being analyzed")
    private String type;

    @Parameter(names={"--connectionProperties"}, description="The file that contains the database connection properties")
    private File connectionProperties;

    @Parameter(names={"--batchSize"}, description="The number of log lines to commit at once")
    private Integer batchSize = 200;

    @Parameter(names={"--help", "-h"}, help = true)
    private boolean help = false;

    /**
     * Run the application
     */
	public static void main(String[] args) {
        log.info("Executing Log Analyzer");
        Analyzer analyzer = new Analyzer();
        JCommander jc = JCommander.newBuilder().addObject(analyzer).build();
        jc.parse(args);
        if (analyzer.help) {
            jc.usage();
            System.exit(0);
        }
        if (analyzer.directory == null || !analyzer.directory.exists() || !analyzer.directory.isDirectory()) {
            jc.usage();
            System.exit(1);
        }
        LogProcessor processor = null;
        if ("activityLog".equals(analyzer.type)) {
            processor = new ActivityLogProcessor();
        }
        else if ("sslAccess".equals(analyzer.type)) {
            processor = new SSLAccessLogProcessor();
        }
        else {
            jc.usage();
            System.exit(1);
        }

        try {
            analyzer.execute(processor);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void execute(LogProcessor processor) throws Exception {
        for (File file : directory.listFiles()) {
            System.out.println("Reading in " + file.getAbsolutePath());
            try (Connection connection = Mysql.createConnection(Props.readFile(connectionProperties))) {
                connection.setAutoCommit(false);
                try (PreparedStatement ps = connection.prepareStatement(processor.getBatchStatement())) {
                    int batchesProcessed = 0;
                    int rowsCurrentlyBatched = 0;
                    int totalRowsProcessed = 0;
                    log.info("Importing batches of size: " + batchSize);
                    for (String line : FileUtils.readLines(file, "UTF-8")) {
                        processor.processLine(ps, line);
                        ps.addBatch();
                        rowsCurrentlyBatched++;
                        if (rowsCurrentlyBatched % batchSize == 0) {
                            ps.executeBatch();
                            connection.commit();
                            batchesProcessed++;
                            totalRowsProcessed += rowsCurrentlyBatched;
                            rowsCurrentlyBatched = 0;
                            if (batchSize * batchesProcessed % 50000 < batchSize) {
                                log.info("Rows committed: " + batchesProcessed * batchSize);
                            }
                        }
                    }
                    if (rowsCurrentlyBatched > 0) {
                        ps.executeBatch();
                        connection.commit();
                        totalRowsProcessed += rowsCurrentlyBatched;
                        log.info("File complete.  Total rows processed: " + totalRowsProcessed);
                    }
                }
            }
        }
    }
}
