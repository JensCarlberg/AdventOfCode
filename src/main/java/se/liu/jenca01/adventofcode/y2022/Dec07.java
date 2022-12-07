package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec07 extends Christmas {

    long sampleAnswer1 = 95437;
    long sampleAnswer2 = 24933642;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec07();
        christmas.solveSample();
        christmas.solveMy();
    }

    @Override
    public void solveSample() throws Exception {
        long start = System.currentTimeMillis();
        long solve1 = solve1(sampleData());
        long stop = System.currentTimeMillis();
        System.out.println("Example solve1 timing: " + (stop-start) + "ms");
        assertEquals(sampleAnswer1, solve1, "solve1 is expected to return " + sampleAnswer1);

        start = System.currentTimeMillis();
        long solve2 = solve2(sampleData());
        stop = System.currentTimeMillis();
        System.out.println("Example solve2 timing: " + (stop-start) + "ms");
        assertEquals(sampleAnswer2, solve2, "solve2 is expected to return " + sampleAnswer2);
    }

    @Override
    public void solveMy() throws Exception {
        System.out.println(simpleClassName() + " solve1: " + solve1(myData()));
        System.out.println(simpleClassName() + " solve2: " + solve2(myData()));
    }

    private FileSystem convertData(Stream<String> data) {
        var fileSystem = new FileSystem(new TreeSet<String>(), new TreeSet<>());
        var currentPath = "";
        var lines = data.toList();
        for (var line: lines) {
            if (line.startsWith("$ cd")) {
                var cdTo = line.substring(5);
                currentPath = normalizePath(currentPath, cdTo);
                fileSystem.dirs.add(currentPath);
                continue;
            }
            if (line.startsWith("$ ls")) {
                continue;
            }
            if (line.startsWith("dir")) {
                continue;
            }
            var parts = line.split(" ");
            var size = Integer.parseInt(parts[0]);
            var name = parts[1];
            var fullName = currentPath + (currentPath.endsWith("/") ? "" : "/") + name;

            fileSystem.files.add(new File(size, fullName));
        }
        return fileSystem;
    }

    private String normalizePath(String currentPath, String cdTo) {
        var pathParts = (currentPath + "/" + cdTo).split("/");
        var normalizedPathParts = new ArrayList();
        for (var part: pathParts)
            if (part.equals(".."))
                normalizedPathParts.remove(normalizedPathParts.size() - 1);
            else
                normalizedPathParts.add(part);
        String normPath = String.join("/", normalizedPathParts);
        if (normPath.startsWith("//"))
            return normPath.substring(1);
        if (normPath.startsWith("/"))
            return normPath;
        return "/" + normPath;
    }

    public long solve1(Stream<String> stream) {
        var fileSystem = convertData(stream);
        var dirSizes = calcDirSizes(fileSystem);

        var solve1sum = 0;
        for (var size: dirSizes.values())
            if (size <= 100000)
                solve1sum += size;
        return solve1sum;
    }

    private TreeMap<String, Integer> calcDirSizes(FileSystem fileSystem) {
        var dirSizes = new TreeMap<String, Integer>();
        for (var dir: fileSystem.dirs)
            dirSizes.put(dir, calcDirSize(dir, fileSystem));
        return dirSizes;
    }

    private int calcDirSize(String dir, FileSystem fs) {
        var matchingFiles = new TreeSet<File>();
        for (var file: fs.files)
            if (file.name.startsWith(dir))
                matchingFiles.add(file);

        int sum = 0;
        for (var file: matchingFiles)
            sum += file.size;
        return sum;
    }

    public long solve2(Stream<String> stream) {
        var fileSystem = convertData(stream);
        var totalSpace = 70000000;
        var neededSpace = 30000000;
        var usedSpace = calcDirSize("/", fileSystem);
        var freeSpace = totalSpace - usedSpace;
        var neededToFree = neededSpace - freeSpace;
        var dirSizes = calcDirSizes(fileSystem);

        var least = usedSpace;
        for (var dirSize: dirSizes.values())
            if (dirSize < least && dirSize >= neededToFree)
                least = dirSize;

        return least;
    }

    record File(int size, String name) implements Comparable<File> {
        @Override public int compareTo(File o) {
            if (!name.equals(o.name))
                return name.compareTo(o.name);
            if (size != o.size)
                return size - o.size;
            return 0;
        }
    }
//    record Dir(String name, List<File> files) {
//        static Dir dir(String name) { return new Dir(name, new ArrayList<>()); }
//    }
    record FileSystem(Set<String> dirs, Set<File> files) {}
}

/*



*/