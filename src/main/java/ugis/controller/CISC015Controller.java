package ugis.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
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
import ugis.service.CISC015Service;
import ugis.service.CMSC003Service;
import ugis.service.vo.CMSC003VO;
import ugis.service.vo.CISC015VO.ObjChangeListRes;
import ugis.service.vo.CISC015VO.ObjChangeRsltListRes;
import ugis.service.vo.CISC015VO.ObjChangeSearch;
import ugis.service.vo.CISC015VO.TnAiModelAnals;
import ugis.service.vo.CISC015VO.TnAiModelAnalsVidoMapngResult;

/**
 * @Class Name : CISC015Controller.java
 * @Description : 객체추출
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
public class CISC015Controller {

	private final CISC015Service cisc015Service;

	// 서비스
	@Resource(name = "cmsc003Service")
	private CMSC003Service cmsc003Service;

	@GetMapping(value = "/cisc015.do")
	public ModelAndView cisc015() throws Exception {
		return new ModelAndView("ci/cisc015");
	}

	@PostMapping(value = "/cisc015/getSateList.do")
	public ResponseEntity<List<ObjChangeListRes>> getSateList(@RequestBody ObjChangeSearch dto) throws Exception {

		// List<ObjChangeListRes> ciscList = cisc015Service.selectSateList(dto);

		List<ObjChangeListRes> resList = new ArrayList<ObjChangeListRes>();

		// usgs tb
		if (dto.getSearchPotogrfVidoCd().length > 0) {
			List<ObjChangeListRes> usgsList = cisc015Service.selectSateList(dto);
			resList.addAll(usgsList);
		}
		// usgs work tb
		List<ObjChangeListRes> workList = cisc015Service.selectUsgsWorkList(dto);
		resList.addAll(workList);

		// int i = 0;
		// for (ObjChangeListRes res : resList) {
		// if (!res.getMapPrjctnCn().equals("EPSG:4326")) {
		//
		// BigDecimal ltopx = (BigDecimal) res.getLtopCrdntX();
		// BigDecimal ltopy = (BigDecimal) res.getLtopCrdntY();
		// BigDecimal rbtmx = (BigDecimal) res.getRbtmCrdntX();
		// BigDecimal rbtmy = (BigDecimal) res.getRbtmCrdntY();
		//
		// res.setLtopCrdntX(ltopy);
		// res.setLtopCrdntY(ltopx);
		// res.setRbtmCrdntX(rbtmy);
		// res.setRbtmCrdntY(rbtmx);
		//
		// resList.set(i, res);
		// i++;
		// }
		// }
		return ResponseEntity.ok().body(resList);
	}

	@PostMapping(value = "/cisc015/saveAiModelAnals.do")
	public ResponseEntity<Long> saveAiModelAnals(@RequestBody TnAiModelAnals dto) throws Exception {
		return ResponseEntity.ok().body(cisc015Service.saveAiModelAnals(dto));
	}

	@PutMapping(value = "/cisc015/updateAiModelAnalsStreYn.do")
	public ResponseEntity<Integer> updateAiModelAnalsStreYn(@RequestBody TnAiModelAnals dto) throws Exception {
		return ResponseEntity.ok().body(cisc015Service.updateAiModelAnalsStreYn(dto));
	}

	@GetMapping(value = "/cisc015/getObjResultList.do")
	public ResponseEntity<ObjChangeRsltListRes> getObjResultList() throws Exception {

		// 항공영상 -> USGS_WORK tb
		ObjChangeRsltListRes resWork = cisc015Service.selectAiModelAnalsRsltWorkList();
		List<ObjChangeRsltListRes> workList = resWork.getResultList();
		if (workList == null) {
			workList = new ArrayList<>();
			workList.add(resWork);
		}
		for (ObjChangeRsltListRes restmp : workList) {
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
		ObjChangeRsltListRes resUsgs = cisc015Service.selectAiModelAnalsRsltList();
		List<ObjChangeRsltListRes> usgsLisg = resUsgs.getResultList();
		for (ObjChangeRsltListRes restmp : usgsLisg) {
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

		// return ResponseEntity.ok().body(cisc015Service.selectAiModelAnalsRsltList());
		return ResponseEntity.ok().body(resWork);
	}

	@RequestMapping(value = "/cisc015/createThumb.do", produces = MediaType.IMAGE_JPEG_VALUE)
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

	@PutMapping(value = "/cisc015/updateVector.do")
	public ResponseEntity<Integer> updateVector(@RequestBody TnAiModelAnalsVidoMapngResult dto) throws Exception {
		return ResponseEntity.ok().body(cisc015Service.updateVector(dto));
	}

	@PutMapping(value = "/cisc015/deleteVector.do")
	public ResponseEntity<Integer> deleteVector(@RequestBody TnAiModelAnalsVidoMapngResult dto) throws Exception {
		return ResponseEntity.ok().body(cisc015Service.deleteVector(dto));
	}

	@GetMapping(value = "/cisc015/getObjVectorList.do")
	public ResponseEntity<List<ObjChangeRsltListRes>> getObjVectorList() throws Exception {
		return ResponseEntity.ok().body(cisc015Service.selectAiModelAnalsVectorList());
	}

}
