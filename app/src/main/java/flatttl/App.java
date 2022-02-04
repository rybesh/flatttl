package flatttl;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.nio.charset.StandardCharsets;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

public class App {
    public static void main(String[] args) {
        if (args.length == 1) {
            Path path = Paths.get(args[0]);
            if (checkPath(path)) {
                Model model = RDFDataMgr.loadModel(path.toString());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                RDFDataMgr.write(out, model, RDFFormat.TURTLE_FLAT);
                out.toString(StandardCharsets.UTF_8)
                    .lines()
                    .sorted(cmp)
                    .forEach(System.out::println);
                System.exit(0);
            }
        }
        usage();
        System.exit(1);
    }

    private static final Comparator<String> cmp = new Comparator<String>() {
        public int compare(String s1, String s2) {
            if (isPrefix(s1) && (! isPrefix(s2))) {
                return -1;
            }
            if (isPrefix(s2) && (! isPrefix(s1))) {
                return 1;
            }
            return s1.compareTo(s2);
        }
    };

    private static boolean isPrefix(String s) {
        return s.startsWith("@prefix") || s.startsWith("PREFIX");
    }

    private static boolean checkPath(Path p) {
        if (Files.isReadable(p)) {
            return true;
        } else {
            return err("Cannot read %s", p);
        }
    }

    private static boolean err(String msgTemplate, Path p) {
        System.err.println(String.format(msgTemplate, p));
        return false;
    }

    private static void usage() {
        System.err.println();
        System.err.println("usage:");
        System.err.println("  flatttl file");
        System.err.println();
        System.err.println("  `file` should be an RDF file");
        System.err.println();
    }
}
