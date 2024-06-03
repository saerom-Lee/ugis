package ugis.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import ugis.cmmn.imgproc.GImageProcessor;
import ugis.cmmn.imgproc.GTiffDataReader;
import ugis.service.CISC017Service;
import ugis.service.vo.CISC015VO.ObjChangeRsltListRes;
import ugis.service.vo.CISC017VO.ChangeDetctRsltListRes;
import ugis.service.vo.CISC017VO.ThChngeDetct;
import ugis.service.vo.CISC017VO.ThChngeDetctVidoResult;

/**
 * @Class Name : CISC017Controller.java
 * @Description : 변화탐지
 * @Modification Information
 * @ @ 수정일 / 수정자 / 수정내용 @ -------------------------------------------------
 * @ 2021.09.xx / ngii / 최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2021.09.xx
 * @version 1.0
 * @see
 *
 */
@RequiredArgsConstructor
@RestController
public class CISC017Controller {

	private final CISC017Service cisc017Service;

	@GetMapping(value = "/cisc017.do")
	public ModelAndView cisc017() throws Exception {
		return new ModelAndView("ci/cisc017");
	}

	@PostMapping(value = "/cisc017/saveThChngeDetct.do")
	public ResponseEntity<Long> saveThChngeDetct(@RequestBody ThChngeDetct dto) throws Exception {
		return ResponseEntity.ok().body(cisc017Service.saveThChngeDetct(dto));
	}

	@PutMapping(value = "/cisc017/updateThChngeDetctStreYn.do")
	public ResponseEntity<Integer> updateThChngeDetctStreYn(@RequestBody ThChngeDetct dto) throws Exception {
		return ResponseEntity.ok().body(cisc017Service.updateThChngeDetctStreYn(dto));
	}

	@GetMapping(value = "/cisc017/getDetctResultList.do")
	public ResponseEntity<ChangeDetctRsltListRes> getDetctResultList() throws Exception {

		// 항공영상 -> USGS_WORK tb
		ChangeDetctRsltListRes resWork = cisc017Service.selectThChngeDetctRsltWorkList();
		List<ChangeDetctRsltListRes> workList = resWork.getResultList();
		for (ChangeDetctRsltListRes restmp : workList) {
			String path = restmp.getFileCoursNm() + "/" + restmp.getAllFileNm();
			String thumb = path.replace(".tif", ".png");
			File thumbFile = new File(thumb);
			if (!thumbFile.exists()) {
				// create thumbnail
				GImageProcessor gImageProcessor = new GImageProcessor();
				gImageProcessor.createThumbnailImage(path, GTiffDataReader.BIT16ToBIT8.MAX_BIT16, thumb,
						GImageProcessor.ImageFormat.IMG_PNG, 1000, GTiffDataReader.ResamplingMethod.BILINEAR);
			}
		}

		// 드론영상 -> USGS tb
		ChangeDetctRsltListRes resUsgs = cisc017Service.selectThChngeDetctRsltList();
		List<ChangeDetctRsltListRes> usgsLisg = resUsgs.getResultList();
		for (ChangeDetctRsltListRes restmp : usgsLisg) {
			String path = restmp.getFileCoursNm() + "/" + restmp.getAllFileNm();
			String thumb = path.replace(".tif", ".png");
			File thumbFile = new File(thumb);
			if (!thumbFile.exists()) {
				// create thumbnail
				GImageProcessor gImageProcessor = new GImageProcessor();
				gImageProcessor.createThumbnailImage(path, GTiffDataReader.BIT16ToBIT8.MAX_BIT16, thumb,
						GImageProcessor.ImageFormat.IMG_PNG, 1000, GTiffDataReader.ResamplingMethod.BILINEAR);
			}
		}

		// merge WORK & USGS
		resWork.addGroupList(resUsgs.getGroupList());
		resWork.addResultList(resUsgs.getResultList());

		return ResponseEntity.ok().body(resWork);
	}

	@RequestMapping(value = "/cisc017/createThumb.do", produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] createThumb(HttpServletRequest req, HttpServletResponse resp, String filePath) {

		String ext = FilenameUtils.getExtension(new File(filePath).getName());
		if (ext.equals("tif")) {
			filePath = filePath.replace(ext, "png");
		}
		// gfa 썸네일 확장자 -> .jgp 포맷으로 변경
		if (filePath.toUpperCase().contains("GeoFraData")) {
			filePath = filePath.replace(".png", ".jpg");
		}
		File thumbFile = new File(filePath);
		if (!thumbFile.exists()) {
			// create thumbnail
			String tifPath = filePath.replace(ext, "tif");
			GImageProcessor gImageProcessor = new GImageProcessor();
			gImageProcessor.createThumbnailImage(tifPath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16, filePath,
					GImageProcessor.ImageFormat.IMG_PNG, 1000, GTiffDataReader.ResamplingMethod.BILINEAR);
		}

		Path path = Paths.get(filePath);
		byte[] fileByteArray = null;
		try {
			fileByteArray = Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileByteArray;
	}

	@PutMapping(value = "/cisc017/updateVector.do")
	public ResponseEntity<Integer> updateVector(@RequestBody ThChngeDetctVidoResult dto) throws Exception {
		return ResponseEntity.ok().body(cisc017Service.updateVector(dto));
	}

	@PutMapping(value = "/cisc017/deleteVector.do")
	public ResponseEntity<Integer> deleteVector(@RequestBody ThChngeDetctVidoResult dto) throws Exception {
		return ResponseEntity.ok().body(cisc017Service.deleteVector(dto));
	}

	@GetMapping(value = "/cisc017/getDetctVectorList.do")
	public ResponseEntity<List<ChangeDetctRsltListRes>> getDetctVectorList() throws Exception {
		return ResponseEntity.ok().body(cisc017Service.selectThChngeDetctVectorList());
	}

}
