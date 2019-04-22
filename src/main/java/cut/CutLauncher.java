package cut;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.util.ArrayList;
import java.util.List;

public class CutLauncher {

    @Option(name = "-c", forbids = "-w")
    private boolean sym;

    @Option(name = "-w", forbids = "-c")
    private boolean word;

    @Option(name = "-o")
    private String outFile = null;

    @Argument(required = true, metaVar = "Диапазон")
    private List<String> extraArgs = new ArrayList();

    public static void main(String[] args) {
        new CutLauncher().launch(args);
    }

    private void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar cut.jar [-c|-w] [-o ofile] [file] range");
            parser.printUsage(System.err);
            return;
        }
        boolean symOrWord;
        if (word) symOrWord = true;
        if (sym) symOrWord = false;
        else symOrWord = false;
        if (extraArgs.size() > 2) throw new IllegalArgumentException();
        String range;
        String inFile = null;
        if (extraArgs.size() == 1) range = extraArgs.get(0);
        else {
            range = extraArgs.get(1);
            inFile = extraArgs.get(0);
        }
        Cut cut = new Cut(inFile, outFile, symOrWord, range);
        cut.cutPrep();

    }
}
