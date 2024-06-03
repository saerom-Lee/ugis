package ugis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipFile {

	public static void unzipFile(Path sourceZip, Path targetDir) {
		if(Files.notExists(targetDir)) {
			try {
				Files.createDirectories(targetDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(sourceZip.toFile()))) {

			// list files in zip
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {

				boolean isDirectory = false;
				if (zipEntry.getName().endsWith(File.separator)) {
					isDirectory = true;
				}

				Path newPath = zipSlipProtect(zipEntry, targetDir);
				if (isDirectory) {
					Files.createDirectories(newPath);
				} else {
					if (newPath.getParent() != null) {
						if (Files.notExists(newPath.getParent())) {
							Files.createDirectories(newPath.getParent());
						}
					}
					// copy files
					Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
				}

				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir) throws IOException {

		// test zip slip vulnerability
		Path targetDirResolved = targetDir.resolve(zipEntry.getName());
		// make sure normalized file still has targetDir as its prefix
		// else throws exception
		Path normalizePath = targetDirResolved.normalize();
		if (!normalizePath.startsWith(targetDir)) {
			throw new IOException("Bad zip entry: " + zipEntry.getName());
		}
		return normalizePath;
	}

	public static Path unzipByFileName(Path sourceZip, String fileName) {
		String tempdir = System.getProperty("java.io.tmpdir");

		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(sourceZip.toFile()))) {
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				Path newPath = Paths.get(tempdir, fileName);
				if(zipEntry.getName().compareToIgnoreCase(fileName) == 0) {
					Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
					return newPath;
				}
				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();

		} catch (FileNotFoundException e) {
			e.getStackTrace();
			return null;
		} catch (IOException e) {
			e.getStackTrace();
			return null;
		}
		return null;
	}

}
