package dfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Before;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Test {
    FileSystem fs = null;
    @Before
    public void init() throws URISyntaxException, IOException, InterruptedException {
        Configuration conf = new Configuration();

        fs = FileSystem.get(new URI("hdfs://master:9000/"), conf, "hadoop");
    }


    @org.junit.Test
    public void download() throws IOException {
        fs.copyToLocalFile(new Path("hdfs://master:9000/UpTest/ab.txt"),new Path("a.txt"));
    }

    @org.junit.Test
    public void upload() throws IOException {
        fs.copyFromLocalFile(new Path("a.txt"),new Path("hdfs://master:9000/UpTest/ab.txt"));
    }

    @org.junit.Test
    public void listFile() throws IOException {
        RemoteIterator<LocatedFileStatus> files = fs.listFiles(new Path("/"),true);
        while (files.hasNext()) {
            LocatedFileStatus next = files.next();
            Path path = next.getPath();
            System.out.println(path.getName());

        }
        System.out.println("-------------------------------------------");


    }

    @org.junit.Test
    public void listFileAndDir() throws IOException {
        FileStatus[] fs_arr = fs.listStatus(new Path("/"));
        for (FileStatus fileStatus : fs_arr) {
            System.out.println(fileStatus.getPath().getName());
        }
    }

    @org.junit.Test
    public void mkdir() throws IOException {
        fs.mkdirs(new Path("/mkdir_dothat/bbb"));
    }

    @org.junit.Test
    public void rm() throws IOException {
        fs.delete(new Path("/mkdir_dothat"),true);

    }


}
