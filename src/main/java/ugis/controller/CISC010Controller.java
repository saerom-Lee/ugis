package ugis.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import ugis.service.CISC010Service;
import ugis.service.CmmnFileService;
import ugis.service.vo.CISC010VO.AiDataSetListRes;
import ugis.service.vo.CISC010VO.AiDataSetRes;
import ugis.service.vo.CISC010VO.AiDataSetSearch;
import ugis.service.vo.CISC010VO.AiLearningRes;
import ugis.service.vo.CISC010VO.AiLearningSearch;
import ugis.service.vo.CISC010VO.ThAiModelPrfomncEvlVidoResult;
import ugis.service.vo.CISC010VO.TnAiDataSet;
import ugis.service.vo.CISC010VO.TnAiModelPrfomncEvl;
import ugis.service.vo.CISC010VO.TnAiModelVer;

/**
 * @Class Name : CISC010Controller.java
 * @Description : AI 학습
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
public class CISC010Controller {

    private final CISC010Service cisc010Service;
    private final CmmnFileService cmmnFileService;

	@GetMapping(value = "/cisc010.do")
	public ModelAndView cisc010(ModelMap model) {
		return new ModelAndView("ci/cisc010");
	}

	@GetMapping(value = "/cisc011.do")
	public ModelAndView cisc011(ModelMap model) {
		return new ModelAndView("ci/cisc011");
	}

	@GetMapping(value = "/cisc012.do")
	public ModelAndView cisc012(ModelMap model) {
		return new ModelAndView("ci/cisc012");
	}

	@GetMapping(value = "/cisc014_pop_04.do")
	public ModelAndView cisc012_pop(ModelMap model) {
		return new ModelAndView("ci/cisc014_pop_04");
	}

	@PostMapping(value = "/cisc010/getFileNames.do")
    public ResponseEntity<List<String>> getFileNames(MultipartFile file) throws Exception {
	    return ResponseEntity.ok().body(cmmnFileService.getUncompressFile(file.getBytes()));
    }

	@PostMapping(value = "/cisc010/saveDataSet.do")
	public ResponseEntity<Long> saveDataSet(@ModelAttribute TnAiDataSet dto) throws Exception {
		return ResponseEntity.ok().body(cisc010Service.saveDataSet(dto));
	}

	@GetMapping(value = "/cisc010/deleteDataSet.do")
    public ResponseEntity<Integer> deleteDataSet(@RequestParam Long aiDataSetId) throws Exception {
        return ResponseEntity.ok().body(cisc010Service.deleteDataSet(aiDataSetId));
    }

	@PostMapping(value = "/cisc010/getAiDataSetList.do")
    public ResponseEntity<List<AiDataSetListRes>> getAiDataSetList(@RequestBody AiDataSetSearch dto) throws Exception {
        return ResponseEntity.ok().body(cisc010Service.selectAiDataSetList(dto));
    }

	@GetMapping(value = "/cisc010/getAiDataSet.do")
    public ResponseEntity<AiDataSetRes> getAiDataSet(@RequestParam Long aiDataSetId) throws Exception {
        return ResponseEntity.ok().body(cisc010Service.selectAiDataSet(aiDataSetId));
    }

	@PostMapping(value = "/cisc010/getAiModelList.do")
    public ResponseEntity<AiLearningRes> getAiModelList(@RequestBody AiLearningSearch dto) throws Exception {
        return ResponseEntity.ok().body(cisc010Service.selectAiModelList(dto));
    }

	@GetMapping(value = "/cisc010/getAiModel.do")
    public ResponseEntity<AiLearningRes> getAiModel(TnAiModelVer vo) throws Exception {
        return ResponseEntity.ok().body(cisc010Service.selectAiModel(vo));
    }

	@PostMapping(value = "/cisc010/saveAiLearning.do")
	public ResponseEntity<Integer> saveAiLearning(@ModelAttribute TnAiModelVer vo) throws Exception {
		return ResponseEntity.ok().body(cisc010Service.saveModelVer(vo));
	}

	@PostMapping(value = "/cisc010/savePrfomncEvl.do")
	public ResponseEntity<Integer> savePrfomncEvl(@ModelAttribute TnAiModelPrfomncEvl vo) throws Exception {
		return ResponseEntity.ok().body(cisc010Service.savePrfomncEvl(vo));
	}

	@PostMapping(value = "/cisc010/getPrfomncEvlResultList.do")
    public ResponseEntity<AiLearningRes> getPrfomncEvlResultList(@RequestBody AiLearningSearch dto) throws Exception {
        return ResponseEntity.ok().body(cisc010Service.selectPrfomncEvlResultList(dto));
    }

	@PostMapping(value = "/cisc010/overwriteAiLearning.do")
	public ResponseEntity<Integer> overwriteAiLearning(@ModelAttribute TnAiModelVer vo) throws Exception {
		return ResponseEntity.ok().body(cisc010Service.overwriteAiLearning(vo));
	}

	@PostMapping(value = "/cisc010/saveAsAiLearning.do")
	public ResponseEntity<Integer> saveAsAiLearning(@ModelAttribute TnAiModelVer vo) throws Exception {
		return ResponseEntity.ok().body(cisc010Service.saveAsAiLearning(vo));
	}

	@GetMapping(value = "/cisc010/deleteAiLearning.do")
	public ResponseEntity<Integer> deleteAiLearning(@ModelAttribute TnAiModelVer vo) throws Exception {
		return ResponseEntity.ok().body(cisc010Service.deleteAiLearning(vo));
	}

	@PostMapping(value = "/cisc010/updatePrfomncEvlResultComplete.do")
	public ResponseEntity<Integer> updatePrfomncEvlResultComplete(@RequestBody ThAiModelPrfomncEvlVidoResult vo) throws Exception {
		return ResponseEntity.ok().body(cisc010Service.updatePrfomncEvlResultComplete(vo));
	}



}
