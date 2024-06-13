import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    private static void saveGame(String path, GameProgress game) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(game);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void zipFiles(String filePath, List<String> listObjectPath) {
        try (ZipOutputStream zipS = new ZipOutputStream(new FileOutputStream(filePath))) {
            int count = 1;
            for (String path : listObjectPath) {
                FileInputStream fis = new FileInputStream(path);
                ZipEntry entry = new ZipEntry("packed_save" + count++ + ".dat");
                zipS.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zipS.write(buffer);
                zipS.closeEntry();
                fis.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteUnpackingFiles() {
        File files = new File("Games\\savegames");
        if(files.isDirectory()) {
            for(File file : Objects.requireNonNull(files.listFiles())) {
                if(!file.getName().contains(".zip")) {
                    file.delete();
                }
            }
        }
    }

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();

        List<File> folderList = Arrays.asList(
                new File("Games"),
                new File("Games//temp"),
                new File("Games//src"),
                new File("Games//res"),
                new File("Games//savegames"),
                new File("Games//src//main"),
                new File("Games//src//test"),
                new File("Games//res//drawables"),
                new File("Games//res//vectors"),
                new File("Games//res//icons")
        );

        List<File> fileList = Arrays.asList(
                new File("Games//src//main//Main.java"),
                new File("Games//src//main//Utils.java"),
                new File("Games//temp//temp.txt")
        );

        folderList.stream().forEach(folder -> {
            if (folder.mkdir()) sb.append("Директория " + folder.getName() + " создана\n");
            else sb.append("Директория " + folder.getName() + " не создана\n");
        });

        fileList.stream().forEach(file -> {
            try {
                if (file.createNewFile()) sb.append("Файл " + file.getName() + " создан\n");
                else sb.append("Файл " + file.getName() + " не создан\n");
            } catch (IOException ex) {
                sb.append(ex.getMessage() + '\n');
            }
        });

        try (FileWriter log = new FileWriter("Games//temp//temp.txt", false)) {
            log.write(sb.toString());
            log.flush();
        } catch (IOException ex) {
            sb.append(ex.getMessage() + '\n');
        }
        try (BufferedReader br = new BufferedReader(
                new FileReader("Games//temp//temp.txt"))) {
            String s;
            while ((s = br.readLine()) != null) System.out.println(s);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        GameProgress gp1 = new GameProgress(100, 15, 1, 0.5);
        GameProgress gp2 = new GameProgress(75, 12, 2, 4);
        GameProgress gp3 = new GameProgress(63, 7, 4, 17);

        saveGame("Games\\savegames\\save1.dat", gp1);
        saveGame("Games\\savegames\\save2.dat", gp2);
        saveGame("Games\\savegames\\save3.dat", gp3);

        List<String> paths = new ArrayList<>();
        paths.add("Games\\savegames\\save1.dat");
        paths.add("Games\\savegames\\save2.dat");
        paths.add("Games\\savegames\\save3.dat");

        zipFiles("Games//savegames//save.zip", paths);
        deleteUnpackingFiles();
    }
}