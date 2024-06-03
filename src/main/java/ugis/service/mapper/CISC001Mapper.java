package ugis.service.mapper;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import ugis.service.vo.CISC001AlgorithmVO;
import ugis.service.vo.CISC001ProjectLogVO;
import ugis.service.vo.CISC001ProjectVO;
import ugis.service.vo.CISC001ScriptVO;
import ugis.service.vo.CISC001WorkResultVO;
import ugis.service.vo.CMSC003InsertVO;

@Mapper("cisc001Mapper")
public interface CISC001Mapper {

	/* 프로젝트 저장 */
	int insertProject(CISC001ProjectVO cisc001projectVO);

	/* 프로젝트 수정, 삭제 */
	int updateProject(CISC001ProjectVO cisc001projectVO);

	@SuppressWarnings({ "unchecked", "deprecation" })
	List<CISC001ProjectVO> selectProjectList(CISC001ProjectVO cisc001projectVO);

	@SuppressWarnings({ "unchecked", "deprecation" })
	List<CISC001ProjectLogVO> selectProjectLogList(CISC001ProjectLogVO cisc001projectLogVO);

	@SuppressWarnings({ "unchecked", "deprecation" })
	List<CISC001AlgorithmVO> selectAlgorithmRegList(CISC001AlgorithmVO cisc001algorithmVO);

	@SuppressWarnings({ "unchecked", "deprecation" })
	List<CISC001AlgorithmVO> selectAlgorithmList(CISC001AlgorithmVO cisc001algorithmVO);

	@SuppressWarnings({ "unchecked", "deprecation" })
	List<CISC001ScriptVO> selectScriptList(CISC001ScriptVO cisc001scriptVO);

	/* 프로젝트 로그 저장 */
	int insertProjectLog(CISC001ProjectLogVO cisc001projectLogVO);

	/* 프로젝트 로그 삭제 */
	int deleteProjectLog(CISC001ProjectLogVO cisc001projectLogVO);

	/* 알고리즘 등록 */
	int insertAlgorithm(CISC001AlgorithmVO cisc001algorithmVO);

	/* 알고리즘 수정및 삭제 */
	int updateAlgorithm(CISC001AlgorithmVO cisc001algorithmVO);

	/* 스크립트 등록 */
	int insertScript(CISC001ScriptVO cisc001scriptVO);

	/* 스크립트 수정 */
	int updateScript(CISC001ScriptVO cisc001scriptVO);

	/* 스크립트 삭제 */
	int deleteScript(CISC001ScriptVO cisc001scriptVO);

	/* 프로젝트 저장 */
	int insertWorkResult(CISC001WorkResultVO CISC001workVO);

	/* 프로젝트 저장 */
	int insertWorkResult2(CISC001WorkResultVO CISC001workVO);

	/* usgs vido id 만 저장 */
	int insertUsgsOnlyVidoId(CMSC003InsertVO insertVO);

	/* 마지막 vidoID 리턴 */
	int selectLastUsgsId();

	/* usgs meta 저장 */
	void insertUsgsMeta(CMSC003InsertVO insertVO);

}
