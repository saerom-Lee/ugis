package ugis.service;

import java.util.List;

import ugis.service.vo.CISC001AlgorithmVO;
import ugis.service.vo.CISC001ProjectLogVO;
import ugis.service.vo.CISC001ProjectVO;
import ugis.service.vo.CISC001ScriptVO;
import ugis.service.vo.CISC001WorkResultVO;
import ugis.service.vo.CMSC003InsertVO;

public interface CISC001Service {

	/**
	 * 프로젝트 생성
	 *
	 * @param CISC001ProjectVO
	 * @return
	 * @throws Exception
	 */
	public int insertProject(CISC001ProjectVO cisc001projectVO) throws Exception;

	/**
	 * 프로젝트 수정
	 *
	 * @param CISC001ProjectVO
	 * @return
	 * @throws Exception
	 */
	public int updateProject(CISC001ProjectVO cisc001projectVO) throws Exception;

	/**
	 * 프로젝트 검색
	 *
	 * @param CISC001ProjectVO
	 * @return
	 * @throws Exception
	 */
	public List<CISC001ProjectVO> selectProjectList(CISC001ProjectVO cisc001projectVO) throws Exception;

	/**
	 * 프로젝트 로그 검색
	 *
	 * @param CISC001ProjectLogVO
	 * @return
	 * @throws Exception
	 */
	public List<CISC001ProjectLogVO> selectProjectLogList(CISC001ProjectLogVO cisc001projectLogVO) throws Exception;

	/**
	 * 프로젝트 로그 입력
	 *
	 * @param CISC001ProjectLogVO
	 * @return
	 * @throws Exception
	 */
	public int insertProjectLog(CISC001ProjectLogVO cisc001projectLogVO) throws Exception;

	/**
	 * 프로젝트 로그 삭제
	 *
	 * @param CISC001ProjectVO
	 * @return
	 * @throws Exception
	 */
	public int deleteProjectLog(CISC001ProjectLogVO cisc001projectLogVO) throws Exception;

	/**
	 * 등록 알고리즘 리스트 검색
	 *
	 * @param CISC001AlgorithmVO
	 * @return
	 * @throws Exception
	 */
	public List<CISC001AlgorithmVO> selectAlgorithmRegList(CISC001AlgorithmVO cisc001algorithmVO) throws Exception;

	/**
	 * 알고리즘 등록
	 *
	 * @param CISC001AlgorithmVO
	 * @return
	 * @throws Exception
	 */
	public int insertAlgorithm(CISC001AlgorithmVO cisc001algorithmVO) throws Exception;

	/**
	 * 알고리즘 수정및 삭제
	 *
	 * @param CISC001AlgorithmVO
	 * @return
	 * @throws Exception
	 */
	public int updateAlgorithm(CISC001AlgorithmVO cisc001algorithmVO) throws Exception;

	/**
	 * 알고리즘 검색
	 *
	 * @param CISC001AlgorithmVO
	 * @return
	 * @throws Exception
	 */
	public List<CISC001AlgorithmVO> selectAlgorithmList(CISC001AlgorithmVO cisc001algorithmVO) throws Exception;

	/**
	 * 스크립트 등록
	 *
	 * @param CISC001ScriptVO
	 * @return
	 * @throws Exception
	 */
	public int insertScript(CISC001ScriptVO cisc001scriptVO) throws Exception;

	/**
	 * 스크립트 검색
	 *
	 * @param CISC001ScriptVO
	 * @return
	 * @throws Exception
	 */
	public List<CISC001ScriptVO> selectScriptList(CISC001ScriptVO cisc001scriptVO) throws Exception;

	/**
	 * 스크립트 수정
	 *
	 * @param CISC001ScriptVO
	 * @return
	 * @throws Exception
	 */
	public int updateScript(CISC001ScriptVO cisc001scriptVO) throws Exception;

	/**
	 * 스크립트 삭제
	 *
	 * @param CISC001ScriptVO
	 * @return
	 * @throws Exception
	 */
	public int deleteScript(CISC001ScriptVO cisc001scriptVO) throws Exception;

	/**
	 * 작업내역 생성
	 *
	 * @param CISC001WorkResultVO
	 * @return
	 * @throws Exception
	 */
	public int insertWorkResult(CISC001WorkResultVO CISC001workVO) throws Exception;

	public void insertWorkResult2(CISC001WorkResultVO workVO);

	public int insertUsgsOnlyVidoId(CMSC003InsertVO insertVO) throws Exception;

	public int selectLastUsgsId() throws Exception;

	public void insertUsgsMeta(CMSC003InsertVO insertVO) throws Exception;
}
