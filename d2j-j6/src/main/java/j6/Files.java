package j6;

import pxb.java.nio.file.Path;
import pxb.java.nio.file.spi.FileSystemProvider;

import java.io.File;

public class Files {
    public static Path toPath(File file) {
        return new FileSystemProvider.DefPath(file);
    }
}
