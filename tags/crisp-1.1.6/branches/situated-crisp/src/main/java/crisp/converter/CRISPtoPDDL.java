package crisp.converter;

import crisp.planningproblem.Domain;
import crisp.planningproblem.Problem;
import crisp.planningproblem.codec.PddlOutputCodec;



import crisp.grammar.CrispGrammar;
import crisp.grammar.SituatedCrispXmlInputCodec;
import java.io.File;
import java.io.FileReader;

public class CRISPtoPDDL {

    /**
     * Print a usage message
     */
    public static void usage() {
        System.out.println("Usage: java crisp.converter.CRISPtoPPDL [CRISP grammar] [CRISP problem]");
    }

    /**
     * Main program.  When running the converter from the command line, pass
     * the name of the CRISP problem file as the first argument.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            System.err.println("No crisp problem specified");
            usage();
            System.exit(1);
        }

        // TODO some exception handling

        Problem problem = new Problem();

        long start = System.currentTimeMillis();

        SituatedCrispXmlInputCodec codec = new SituatedCrispXmlInputCodec();
        CrispGrammar grammar = new CrispGrammar();
        codec.parse(new FileReader(new File(args[0])), grammar);

        File problemfile = new File(args[1]);

        CurrentNextCrispConverter converter = new CurrentNextCrispConverter(grammar);
        Domain domain = converter.getDomain();
        converter.convert(new FileReader(problemfile), problem);
        long end = System.currentTimeMillis();

        System.out.println("Total runtime: " + (end - start) + "ms");

        System.out.println("Domain: " + domain);
        System.out.println("Problem: " + problem);

        new PddlOutputCodec().writeToDisk(domain, problem, "");
        
    }
}