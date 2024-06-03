//package ugis.controller;
//
//import java.io.File;
//import java.io.IOException;
//
//import org.apache.commons.io.FileUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Controller;
//
////@Configuration
//@EnableScheduling
//@Controller
//public class GFraFilesDeleteTask {
//
//	private static final Logger log = LoggerFactory.getLogger(GFraFilesDeleteTask.class);
//
//	@Value("#{fileProperties['ugis.gfra.download.path']}")
//	private String gFraDownPath;
//
//	@Scheduled(cron = "0 0 */3 * * *")
//	// @Scheduled(cron = "*/30 * * * * *")
//	public void userJob() throws Exception {
//		deleteFolders();
//		log.debug("GFraFilesDeleteTask - TASK SUCCESS ");
//	}
//
//	private void deleteFolders() throws IOException {
//
//		File folder = new File(gFraDownPath);
//		if (folder.exists()) {
//			FileUtils.cleanDirectory(folder);// 하위 폴더와 파일 모두 삭제
//			if (folder.isDirectory()) {
//				folder.delete(); // 대상폴더 삭제
//			}
//		}
//	}
//}
