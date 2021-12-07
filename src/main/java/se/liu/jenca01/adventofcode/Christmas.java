package se.liu.jenca01.adventofcode;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Christmas {

    public abstract void solveSample() throws Exception;

    public abstract void solveMy() throws Exception;

    public Stream<String> sampleData() throws Exception {
        return readData(simpleClassName(), ".sample");
    }

    public List<String> toList(Stream<String> stream) {
        return stream.collect(Collectors.toList());
    }

    public List<String> copy(List<String> data) {
        return toList(data.stream());
    }

    public Stream<String> sampleData(int no) throws Exception {
        return readData(simpleClassName(), ".sample." + no);
    }

    public Stream<String> myData() throws Exception {
        return readData(simpleClassName(), "");
    }

    public String simpleClassName() {
        return this.getClass().getSimpleName();
    }

    private Stream<String> readData(String fileBase, String extras) throws Exception {
        var path = Paths.get(new File(".").getCanonicalPath(), "src", "main", "resources", year(), fileBase + extras + ".txt");
        ensureExists(path);
        var data = Files.lines(path);
        return data;
    }

    public String year() {
        return this.getClass().getPackageName().replace("se.liu.jenca01.adventofcode.y", "");
    }

    private void ensureExists(Path path) throws Exception {
        var file = path.toFile();
        if (file.exists()) return;
        file.createNewFile();
    }

}
