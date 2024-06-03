package ugis.util;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Component;

import net.coobird.thumbnailator.Thumbnails;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

@Component
public class fileUtil {
	private final int width = 280;
	public static final int MAX_SIZE = 1024;
	public void makeThumbnail(String destination) {
		
		File path = new File(destination);
		if(path.isDirectory()) {
			File[] list = path.listFiles(new FilenameFilter() {
				@Override 
				public boolean accept(File dir, String name) { 
					return name.endsWith("tif") 
							|| name.endsWith("TIF") 
							|| name.endsWith("tiff") 
							|| name.endsWith("TIFF"); 
				}
			});
			int size = list.length;
			for(int i =0; i < size; i++) {
				try {
					File f = list[i];
					String tPath = f.getPath().substring(0, f.getPath().lastIndexOf('.')) + "_280.png";
					//String tName = f.getName().substring(0, f.getNme().lastIndexOf('.')) + "_280.png";
					System.out.println(String.format("%d/%d START : %s", i +1, size, tPath));
					BufferedImage image;
					InputStream fio = new FileInputStream(f);
					image = Thumbnails.of(fio).size(width, width)
							.outputFormat("png")
							.asBufferedImage();
					fio.close();
					
					ImageIO.write(image, "png" ,new File(tPath));
					System.out.println(String.format("%d/%d END : %s", i +1, size, tPath));
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void unTarFile(String tarFile, String destination,String fefrt) {
		TarArchiveInputStream tis = null;
		try {
			FileInputStream fis = new FileInputStream(tarFile);
			// .gz
		//	GZIPInputStream gzipInputStream = new GZIPInputStream(new BufferedInputStream(fis));
			//.tar
			tis = new TarArchiveInputStream(new BufferedInputStream(fis));
			TarArchiveEntry tarEntry = null;
			while ((tarEntry = tis.getNextTarEntry()) != null) {
			System.out.println(" tar entry- " + tarEntry.getName());
			if(tarEntry.isDirectory()){
				continue;
			}else {
				// In case entry is for file ensure parent directory is in place
				// and write file content to Output Stream
				String fe = "";
				int j = tarFile.lastIndexOf('.');
				if (j > 0) {
					fe = tarFile.substring(0,j);
				}
			//	File outputFile = new File(destination + File.separator + tarEntry.getName());
				 File outputFile = new File( fe+File.separator+ tarEntry.getName());
				outputFile.getParentFile().mkdirs();
				IOUtils.copy(tis, new FileOutputStream(outputFile));
			}
			}				
		}catch(IOException ex) {
			System.out.println("Error while untarring a file- " + ex.getMessage());
		}finally {
			if(tis != null) {
				try {
					tis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
	}


	public void unTarGzFiles(String tarFile, String destination,String filename) {
		//FileInputStream fis = new FileInputStream(tarFile);
		try (
				FileInputStream in = new FileInputStream(tarFile);
				// .gz
				GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in);
				TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn)
		) {
			TarArchiveEntry tarEntry = tarIn.getNextTarEntry();
			while (tarEntry != null) {
				//String Entrynmae =  tarEntry.getName();

				//압축파일명
				int k = filename.indexOf('.');
				String fe = "";
				String fefrt="";
				if (k > 0) {
					fe = filename.substring(k + 1);
					fefrt = filename.substring(0, k);
				}


				 File path = new File(destination + File.separator +fefrt+File.separator+ tarEntry.getName());
				if (!path.getParentFile().exists()) {
					path.getParentFile().mkdirs();
				}

				if (!tarEntry.isDirectory()) {
					try (OutputStream out = new FileOutputStream(path)){
						IOUtils.copy(tarIn, out);
					}
				}
				tarEntry = tarIn.getNextTarEntry();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public  File unGzip( String gzipfile, boolean deleteGzipfileOnSuccess) throws IOException {
		File infile = new File(gzipfile);
		GZIPInputStream gin = new GZIPInputStream(new FileInputStream(infile));
		FileOutputStream fos = null;
		try {
			File outFile = new File(infile.getParent(), infile.getName().replaceAll("\\.gz$", ""));
			fos = new FileOutputStream(outFile);
			byte[] buf = new byte[100000];
			int len;
			while ((len = gin.read(buf)) > 0) {
				fos.write(buf, 0, len);
			}

			fos.close();
			if (deleteGzipfileOnSuccess) {
				infile.delete();
			}
			return outFile;
		} finally {
			if (gin != null) {
				gin.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}
	
	public void unzip(String file, String destination, String password){
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(file);
			if (zipFile.isEncrypted()) {
				zipFile.setPassword(password.toCharArray());
			}
			zipFile.extractAll(destination);
		} catch (ZipException e) {
			e.printStackTrace();
		} finally {
			if(zipFile != null) {
				try {
					zipFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void unzipcom(String zipfile,String destination){

		File file = new File(zipfile);
		String files[] = null;

		if( file.isDirectory() ){
			files = file.list();
		}else{
			files = new String[1];
			files[0] = file.getName();
			System.out.println(file.getName().getBytes());
		}
		byte[] buf = new byte[MAX_SIZE];

		ZipOutputStream outputStream = null;
		FileInputStream fileInputStream = null;
		try {
			//outputStream = new ZipOutputStream(new FileOutputStream("result.zip"));

			for (String fileName : files) {
				fileInputStream = new FileInputStream(destination+fileName);
				outputStream.putNextEntry(new ZipEntry(destination+fileName));

				int length = 0;
				while ( ( length = fileInputStream.read() ) > 0) {
					outputStream.write(buf, 0, length);
				}
				outputStream.closeEntry();
				fileInputStream.close();
			}
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.closeEntry();
				outputStream.close();
			} catch (IOException e) {
			}
		}
	}




	public String FileName(String DATA_DIRECTORY){
		File dir = new File(DATA_DIRECTORY);
		String[] filenames = dir.list();
		String filename = null;
		for (String tempfile : filenames) {
			filename  = tempfile ;
		}
		return filename;
	}
	public String[] FileNames(String DATA_DIRECTORY){
		File dir = new File(DATA_DIRECTORY);
		String[] filename = dir.list();

		return filename;
	}
	//String DATA_DIRECTORY = "c:/mine_data/";

	public  void decompress(String zipFileName, String directory, String file) throws Throwable {
		File zipFile = new File(zipFileName);
		FileInputStream fis = null;
		ZipInputStream zis = null;
		ZipEntry zipentry = null;
		try {
//파일 스트림
			fis = new FileInputStream(zipFile);
//Zip 파일 스트림
			zis = new ZipInputStream(fis);
//entry가 없을때까지 뽑기
			while ((zipentry = zis.getNextEntry()) != null) {
				String filename = zipentry.getName();
				//파일명 변경.
				int lastIdx = filename.lastIndexOf("/");
				String outputName = filename.substring(0, lastIdx); //파일명
				//압축파일명
				int k = file.lastIndexOf('.');
				String fe = "";
				String fefrt="";
				if (k > 0) {
					fe = file.substring(k + 1);
					fefrt = file.substring(0, k);
				}

				String Newfilename = filename.replace(outputName,fefrt);
				String ext  = filename.substring(lastIdx + 1);  //확장자
				String filenameNew = Newfilename ;
				File new_file = new File(directory, filenameNew);
//entiry가 폴더면 폴더 생성
				if (zipentry.isDirectory()) {
					new_file.mkdirs();
				} else {
//파일이면 파일 만들기
					createFile(new_file, zis);
				}
			}
		} catch (Throwable e) {
			throw e;
		} finally {
			if (zis != null)
				zis.close();
			if (fis != null)
				fis.close();
		}
	}
	/**
	 * 파일 만들기 메소드
	 * @param file 파일
	 * @param zis Zip스트림
	 */
	private  void createFile(File file, ZipInputStream zis) throws Throwable {
//디렉토리 확인
		File parentDir = new File(file.getParent());
//디렉토리가 없으면 생성하자
		if (!parentDir.exists()) {
			parentDir.mkdirs();
		}
//파일 스트림 선언
		try (FileOutputStream fos = new FileOutputStream(file)) {
			byte[] buffer = new byte[256];
			int size = 0;
//Zip스트림으로부터 byte뽑아내기
			while ((size = zis.read(buffer)) > 0) {
//byte로 파일 만들기
				fos.write(buffer, 0, size);
			}
		} catch (Throwable e) {
			throw e;
		}
	}

}
