package cut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;

class TestCut {
    String read (String file) {
        StringBuilder builder = new StringBuilder();
        try{
            FileInputStream fileo = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileo));
            String strLine;
            String sep = "";
            while ((strLine = br.readLine()) != null){
                builder.append(sep);
                sep = "\n";
                builder.append(strLine);
            }
        }catch (IOException e){
            System.out.println("Ошибка с чтением файла");
        }
        return builder.toString();
    }
    @BeforeAll
    static void write() {
        String in = "Профессорская дача на берегу Финского залива.\nВ отсутствие хозяина, друга моего отца, нашей семье позволялось там жить.\n" +
                "Даже спустя десятилетия помню, как после утомительной дороги из города меня обволакивала прохлада деревянного дома.";
        try(FileWriter writer = new FileWriter("tempin.txt", false))
        {
            writer.write(in);
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @Test
    void testFileToFile() {
        Cut cut = new Cut("tempin.txt", "tempout.txt", false, "0-10");
        cut.cutPrep();
        assertEquals("Профессорс\nВ отсутств\nДаже спуст", read("tempout.txt"));
        cut = new Cut("tempin.txt", "tempout.txt", true, "0-10");
        cut.cutPrep();
        assertEquals("Профессорская дача на берегу Финского залива.\n" +
                "В отсутствие хозяина, друга моего отца, нашей семье позволялось там\n" +
                "Даже спустя десятилетия помню, как после утомительной дороги из города", read("tempout.txt"));
    }
    @Test
    void testRange() {
        Cut cut = new Cut("tempin.txt", "tempout.txt", true, "100-10");
        assertThrows(IllegalArgumentException.class, cut::cutPrep);
        cut = new Cut("tempin.txt", "tempout.txt", true, "abcd");
        assertThrows(IllegalArgumentException.class, cut::cutPrep);
        cut = new Cut("tempin.txt", "tempout.txt", false, "0-1000");
        cut.cutPrep();
        assertEquals("Профессорская дача на берегу Финского залива.\nВ отсутствие хозяина, друга моего отца, нашей семье позволялось там жить.\n" +
                "Даже спустя десятилетия помню, как после утомительной дороги из города меня обволакивала прохлада деревянного дома.", read("tempout.txt"));
        cut = new Cut("tempin.txt", "tempout.txt", false, "-1000");
        cut.cutPrep();
        assertEquals("Профессорская дача на берегу Финского залива.\nВ отсутствие хозяина, друга моего отца, нашей семье позволялось там жить.\n" +
                "Даже спустя десятилетия помню, как после утомительной дороги из города меня обволакивала прохлада деревянного дома.", read("tempout.txt"));
    }
    @Test
    void testConsoleToFile() throws FileNotFoundException {
        InputStream old = System.in;
        System.setIn(new FileInputStream("tempin.txt"));
        Cut cut = new Cut(null, "tempout.txt", true, "0-10");
        cut.cutPrep();
        assertEquals("Профессорская дача на берегу Финского залива.\n" +
                "В отсутствие хозяина, друга моего отца, нашей семье позволялось там\n" +
                "Даже спустя десятилетия помню, как после утомительной дороги из города", read("tempout.txt"));
        System.setIn(old);
    }
    @Test
    void testFileToConsole() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));
        Cut cut = new Cut("tempin.txt", null, true, "0-10");
        cut.cutPrep();
        assertEquals("Профессорская дача на берегу Финского залива.\n" +
                "В отсутствие хозяина, друга моего отца, нашей семье позволялось там\n" +
                "Даже спустя десятилетия помню, как после утомительной дороги из города", out.toString());
        System.setOut(old);
    }
    @Test
    void testConsoleToConsole() throws FileNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        InputStream oldIn = System.in;
        System.setOut(new PrintStream(out));
        System.setIn(new FileInputStream("tempin.txt"));
        Cut cut = new Cut(null, null, true, "0-10");
        cut.cutPrep();
        assertEquals("Профессорская дача на берегу Финского залива.\n" +
                "В отсутствие хозяина, друга моего отца, нашей семье позволялось там\n" +
                "Даже спустя десятилетия помню, как после утомительной дороги из города", out.toString());
        System.setIn(oldIn);
        System.setOut(oldOut);
    }
}
