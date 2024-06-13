import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class unZip {
    private static GameProgress openProgress (String path) throws ClassNotFoundException {
        GameProgress gameProgress = null;
        try(FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
        return gameProgress;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        openZip("Games//savegames//save.zip","Games//savegames");
        for (String s : Arrays.asList("packed_save1.dat", "packed_save2.dat", "packed_save3.dat")) {
            System.out.println(openProgress(s));
        }
    }

    private static void openZip(String pathFrom, String pathTo) {
        try(ZipInputStream zis = new ZipInputStream(new FileInputStream(pathFrom))) {
            ZipEntry zipEntry;
            String name;
            while((zipEntry = zis.getNextEntry()) != null) {
                name = zipEntry.getName();
                FileOutputStream fos = new FileOutputStream(pathTo + "\\" + name);
                for(int c = zis.read(); c != -1; c = zis.read()) {
                    fos.write(c);
                }
                fos.flush();
                zis.closeEntry();
                fos.close();
            }
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }


}
}