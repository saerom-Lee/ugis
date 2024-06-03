package ugis.service.impl;


import org.apache.commons.io.FileUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import ugis.cmmn.imgproc.GImageProcessor;
import ugis.cmmn.imgproc.GTiffDataReader;
import ugis.service.CTSC001Service;
import ugis.service.vo.CTSC001VO;
import ugis.util.XmlParser;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component("ctsc001BatchFileService")
public class CTSC001BatchFileServiceImpl {
	@Autowired
	private ugis.util.fileUtil fileUtil;
	@Resource(name = "ctsc001Service")
	private CTSC001Service<CTSC001VO> service;

	private ThreadPoolTaskScheduler scheduler;
	private String cron = "0 0 0/1 * * ?";
	String extpath = ""; //외부 파일경로
	String innerpath =""; /// 내부 파일 경로
	String fe = ""; //확장자
	String fefrt = ""; // 파일명
	CTSC001VO ctsc001VO = new CTSC001VO();
	List<CTSC001VO> select = new ArrayList<>();
	String DataType ="";
	int flag  = 0 ; // xml 수집 여부.
	int vidoId = 0 ;
	GImageProcessor gImageProcessor  = new GImageProcessor();
	GImageProcessor.ProcessCode thumprocCode = GImageProcessor.ProcessCode.SUCCESS;

	public void startScheduler() throws Exception {
		scheduler = new ThreadPoolTaskScheduler();
		scheduler.initialize();
		// scheduler setting
		scheduler.schedule(getRunnable(), getTrigger());
	}

	public void changeCronSet(String cron) {
		this.cron = cron;
	}

	public void stopScheduler() {
		scheduler.shutdown();
	}

	private Runnable getRunnable() throws Exception {
		return () -> {
			try {
				//요청된 영상정보를 검색한다.
				select = service.file_select(ctsc001VO);
				//select.add(0,ctsc001VO);
				//select = service.select(ctsc001VO);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(select.size()!=0){
				for (int i = 0; i < select.size(); i++) {
					//1. 파일경로를 가져온다.
					extpath = select.get(i).getSatlitVidoExtrlPath();
					innerpath = select.get(i).getSatlitVidoInnerPath();
					DataType = select.get(i).getPotogrfVidoCd();
					vidoId = select.get(i).getVidoId();
					flag = 0; // xml 수집 여부.
					String[] filenames = fileUtil.FileNames(extpath);
					for (int j = 0; j < filenames.length; j++) {
						if (filenames[j] != "" || filenames[j] != null) {
							String filename = filenames[j];
							String srcFile = extpath + filename;
							String dstFile = innerpath + filename;
							File src = new File(srcFile);
							File dst = new File(dstFile);
							Path oldfile = Paths.get(srcFile);
							Path newfile = Paths.get(dstFile);
							try {
								//2. 영상정보의 갯수만큼 파일을 이동한다.
								try {
									Files.move(oldfile, newfile, StandardCopyOption.ATOMIC_MOVE);
									//FileUtils.moveFile(src, dst);
								} catch (IOException e) {
									e.printStackTrace();
								}
								int k = filename.lastIndexOf('.');
								if (k > 0) {
									fe = filename.substring(k + 1);
									fefrt = filename.substring(0, k);
								}
								// 3. 영상파일의 압축을 해제 한다.
								if (fefrt.contains("L1C") || DataType == "sentinel_2a") {
									if (fe.equals("zip")) {
										fileUtil.decompress(innerpath + filename, innerpath, filename);
										if (!"".equals(fefrt)) {
											String DATA_DIRECTORY = innerpath + fefrt;
											File dir = new File(DATA_DIRECTORY);
											String[] zipfilenames = dir.list();
											String filenametemp = "";
											for (String tempfile : zipfilenames) {
												filenametemp = tempfile;
												int l = filenametemp.lastIndexOf('.');
												if (l > 0) {
													fe = filenametemp.substring(l + 1);
												}
												//센티넬일경우
												//	ctsc001VO.setVidoId(6);
												//센티넬 xml  저장을 실행한다.
												runlSentinel(filenametemp);

											}
											//센티넬 파일을 저장한다.
											for (String tempfile : zipfilenames) {
												filenametemp = tempfile;
												int l = filenametemp.lastIndexOf('.');
												if (l > 0) {
													fe = filenametemp.substring(l + 1);
												}
												//센티넬일경우
												if (DataType.equals("sentinel_2a") || "jp2".equals(fe) || "JP2".equals(fe)) {
													//sentinel
													ctsc001VO.setVidoId(6);
													//센티넬 저장을 실행한다.
													ctsc001VO.setSatlitVidoInnerPath(innerpath + filenametemp);
													ctsc001VO.setVidoNm(fefrt);
													ctsc001VO.setPotogrfVidoCd("6");
													ctsc001VO.setVidoId(vidoId);
													//ctsc001VO.setVidoId(6);
													Long insert = service.vidoinsert(ctsc001VO);
												}

											}
										}
									}
								}
								if(DataType.equals("landsat_etm_c1")||DataType.equals("landsat_etm_c2_l1")||DataType.equals("landsat_etm_c2_l2")
										||DataType.equals("landsat_8_c1")||DataType.equals("landsat_ot_c2_l1")||DataType.equals("landsat_ot_c2_l2")||"xml".equals(fe) || "XML".equals(fe)){
									String lctype = "";
									if (DataType.equals("landsat_etm_c1") || DataType.equals("landsat_etm_c2_l1") || DataType.equals("landsat_etm_c2_l2")) {
										//landsat 7호기
										lctype = "LE07";
									} else {
										//landsat 8호기
										lctype = "LC8";
									}
									if (fe.equals("tar") && fefrt.contains(lctype)) {
										//3. 이동완료후 파일을 압축을 해제한다.
										fileUtil.unTarFile(innerpath + filename, innerpath, fefrt);

										if (!"".equals(fefrt)) {
											String DATA_DIRECTORY = innerpath + fefrt;
											File dir = new File(DATA_DIRECTORY);
											String[] zipfilenames = dir.list();
											String filenametemp = "";
											for (String tempfile : zipfilenames) {
												filenametemp = tempfile;
												int l = filenametemp.indexOf('.');
												if (l > 0) {
													fe = filenametemp.substring(l + 1);
													fefrt  =  filenametemp.substring(0,l);
												}
												//landsat일경우
												//	ctsc001VO.setVidoId(1);
												//landsat 메타 저장을 실행한다.
												runlandsat(filenametemp,DATA_DIRECTORY);
											}
											//landsat 파일을 저장한다.
											for (String tempfile : zipfilenames) {
												filenametemp = tempfile;
												int l = filenametemp.lastIndexOf('.');
												if (l > 0) {
													fe = filenametemp.substring(l + 1);
												}
												if ("tif".equals(fe) || "TIF".equals(fe)) {
													ctsc001VO.setSatlitVidoInnerPath(innerpath + filenametemp);
													ctsc001VO.setVidoNm(fefrt);
													ctsc001VO.setPotogrfVidoCd("1");
													ctsc001VO.setVidoId(vidoId);
													String outputNm=innerpath + filenametemp;
													GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
													inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
													GImageProcessor.ImageFormat outImgFormat = GImageProcessor.ImageFormat.IMG_PNG;
													int thumbnailWidth = 1000;
													GTiffDataReader.ResamplingMethod resampleMethod = GTiffDataReader.ResamplingMethod.BILINEAR;
													thumprocCode = gImageProcessor.createThumbnailImage(outputNm, inMaxBit16 , DATA_DIRECTORY, outImgFormat, thumbnailWidth,resampleMethod);
													//ctsc001VO.setVidoId(select.get(i).getVidoId());
													Long insert = service.vidoinsert(ctsc001VO);
												}
											}
										}
									}
									if (fe.equals("gz")&& fefrt.contains(lctype)) {
										//	File filetemp = fileUtil.unGzip(innerpath+filename,true);
										fileUtil.unTarGzFiles(innerpath + filename, innerpath,filename);
										//2. 압축해제후 db에 저장한다.
										if (!"".equals(fefrt)) {
											//압축파일명
											int l = fefrt.indexOf('.');
											String fe = "";
											String fefrt="";
											if (l > 0) {
												fe = filename.substring(l + 1);
												fefrt = filename.substring(0, l);
											}
											String DATA_DIRECTORY = innerpath + fefrt;
											File dir = new File(DATA_DIRECTORY);
											String[] zipfilenames = dir.list();
											String filenametemp = "";
											for (String tempfile : zipfilenames) {
												filenametemp = tempfile;

												//landsat일경우
												//ctsc001VO.setVidoId(1);
												//landsat 저장을 실행한다.
												runlandsat(filenametemp,DATA_DIRECTORY);
											}
											//landsat 파일을 저장한다.
											for (String tempfile : zipfilenames) {
												filenametemp = tempfile;
												int m = filenametemp.lastIndexOf('.');
												if (m > 0) {
													fe = filenametemp.substring(m + 1);
												}
												if ("tif".equals(fe) || "TIF".equals(fe)) {
													ctsc001VO.setSatlitVidoInnerPath(innerpath + filenametemp);
													ctsc001VO.setVidoNm(fefrt);
													ctsc001VO.setPotogrfVidoCd("1");
													ctsc001VO.setVidoId(vidoId);
													//ctsc001VO.setVidoId(select.get(i).getVidoId());
													Long insert = service.vidoinsert(ctsc001VO);
												}
											}
										}
									}
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							} catch (Throwable throwable) {
								throwable.printStackTrace();
							}
						}
						try {
							//4. 압축해제후 db에 저장한다.
							if (flag == 1) {

								ctsc001VO.setFlag(4);
								ctsc001VO.setVidoId(vidoId);
								Long insert = service.vidoinsertMeta(ctsc001VO);
								service.upt(ctsc001VO);
							} else {
								//4. 실패시 실패코드로 저장한다.
								ctsc001VO.setFlag(5);
								ctsc001VO.setVidoId(vidoId);
								service.upt(ctsc001VO);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				destroy();
			}
		};

	}


	public  void runlandsat(String filenametemp,String DATA_DIRECTORY) throws Exception {

		if(filenametemp.contains("MTL.xml")){
			//압축을 해제후 xml을 파싱하여 db에 저장한다.
				//txt를 수집안했을 경우.
				if (flag==0) {
						ctsc001VO.setVidoId(1);
						saveLnadsetXml(filenametemp);
					}

			}
			if(filenametemp.contains("MTL.txt")){
				try {
					if (flag==0){
						File searchPath = new File(DATA_DIRECTORY + File.separator + filenametemp);
						BufferedReader br = new BufferedReader(new FileReader(searchPath));
						Map<String, Object> map = new HashMap<String, Object>();
						while (true) {
							String line = br.readLine();
							if (line == null) {
								break;
							}
							String[] lc_arr = line.split("=");
							map.put(lc_arr[0].trim(),lc_arr[1].trim());
						}
						//ArrayList<Map> mlt_txt = new ArrayList<Map>();
						ctsc001VO.setRawSatlitVidoFileNm((String) map.get("LANDSAT_PRODUCT_ID"));
						ctsc001VO.setPrdctnNo((String) map.get("LANDSAT_SCENE_ID")); // 생산번호
						ctsc001VO.setAcqsYr((String) map.get("DATE_ACQUIRED").toString().substring(0,4)); //년도만 추출
						ctsc001VO.setAcqsMm((String) map.get("DATE_ACQUIRED").toString().substring(5,7)); //년도만 추출
						ctsc001VO.setTrackNo((int) map.get("WRS_PATH"));
						ctsc001VO.setPotogrfModeNm((String) map.get("NADIR_OFFNADIR"));
						ctsc001VO.setRollangCo((String) map.get("ROLL_ANGLE"));
						ctsc001VO.setVidoSensorNm((String) map.get("SENSOR_ID"));
						ctsc001VO.setMapPrjctnCn((String) map.get("MAP_PROJECTION"));
						ctsc001VO.setDatumCn((String) map.get("DATUM"));
						ctsc001VO.setSpherCn((String) map.get("ELLIPSOID"));
						ctsc001VO.setLacnul((double) map.get("CORNER_UL_LAT_PRODUCT"));
						ctsc001VO.setLocnul((double) map.get("CORNER_UL_LON_PRODUCT"));
						ctsc001VO.setLacnur((double) map.get("CORNER_UR_LAT_PRODUCT"));
						ctsc001VO.setLocnur((double) map.get("CORNER_UR_LON_PRODUCT"));
						ctsc001VO.setLacnll((double) map.get("CORNER_LL_LAT_PRODUCT"));
						ctsc001VO.setLacnll((double) map.get("CORNER_LL_LAT_PRODUCT"));
						ctsc001VO.setLacnlr((double) map.get("CORNER_LR_LAT_PRODUCT"));
						ctsc001VO.setLocnlr((double) map.get("CORNER_LR_LAT_PRODUCT"));
						ctsc001VO.setCloud_cover((double) map.get("CLOUD_COVER"));
						ctsc001VO.setResolution((double) map.get("GRID_CELL_SIZE_REFLECTIVE"));
						ctsc001VO.setBandCo(11);
						ctsc001VO.setBgngDt((String) map.get("DATE_ACQUIRED"));
						ctsc001VO.setEndDt((String) map.get("DATE_ACQUIRED"));
						ctsc001VO.setEndDt((String) map.get("DATE_ACQUIRED"));
						ctsc001VO.setVidoId(vidoId);
						ctsc001VO.setVidoNm(fefrt);

					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		}
	public  void runlSentinel (String filenametemp) throws Exception {

			if("xml".equals(fe) || "XML".equals(fe)){

				//압축을 해제후 xml을 파싱하여 db에 저장한다.
					ctsc001VO.setVidoId(6);
					saveSentinelXml(filenametemp);
				}

			}

	public void saveSentinelXml(String filenametemp) throws Exception {
		XmlParser p = new XmlParser(innerpath + fefrt + File.separator + filenametemp);
	if(filenametemp.contains("MTD_MSIL1C.xml")){
		ctsc001VO.setBgngDt(p.getString("//PRODUCT_START_TIME"));
		ctsc001VO.setPotogrfBeginDt(p.getString("//PRODUCT_START_TIME"));
		ctsc001VO.setEndDt(p.getString("//PRODUCT_STOP_TIME")); //원시위성영상파일명
		ctsc001VO.setPotogrfEndDt(p.getString("//PRODUCT_STOP_TIME")); //원시위성영상파일명

		ctsc001VO.setAcqsYr(p.getString("//PRODUCT_STOP_TIME").substring(0, 4)); //년도만 추출
		ctsc001VO.setAcqsMm(p.getString("//PRODUCT_STOP_TIME").substring(5, 7)); //월만추출

		ctsc001VO.setSatlitNo(p.getString("//SPACECRAFT_NAME"));
		ctsc001VO.setTrackNo(Integer.parseInt(p.getString("//SENSING_ORBIT_NUMBER"))); //궤도번호

		ctsc001VO.setPotogrfModeNm(p.getString("//DATATAKE_TYPE")); //촬영모드명
		ctsc001VO.setSensorModelNm(p.getString("//DATATAKE_TYPE")); //영상센서명

		ctsc001VO.setRollangCo(p.getString("//ROLL_ANGLE")); //ROLL_ANGLE
		ctsc001VO.setVidoSensorNm(p.getString("//PRODUCT_TYPE")); //영상센서명

		ctsc001VO.setResolution(Double.parseDouble(p.getString("//RESOLUTION")));//resolution
		//ctsc001VO.setVidoId(select.get(i).getVidoId());

		ctsc001VO.setVidoNm(fefrt);

	}
		if(filenametemp.contains("MTD_TL.xml")){
			ctsc001VO.setCloud_cover(Double.parseDouble(p.getString("//CLOUDY_PIXEL_PERCENTAGE")));
			String cs_name = p.getString("//HORIZONTAL_CS_NAME");
			String[] cs_nm = cs_name.split("/");
			ctsc001VO.setDatumCn(cs_nm[0]);
			ctsc001VO.setSpherCn(cs_nm[0]);
			ctsc001VO.setMapPrjctnCn(cs_nm[1]);
			ctsc001VO.setMapPrjctnCn(p.getString("//HORIZONTAL_CS_CODE"));
			flag = 1;
		}



	}
	public void saveLnadsetXml(String filenametemp) throws Exception {
				XmlParser p = new XmlParser(innerpath + fefrt + File.separator + filenametemp);
				ctsc001VO.setRawSatlitVidoFileNm(p.getString("//LANDSAT_PRODUCT_ID")); //원시위성영상파일명
				ctsc001VO.setPrdctnNo(p.getString("//LANDSAT_SCENE_ID")); // 생산번호
				ctsc001VO.setAcqsYr(p.getString("//DATE_ACQUIRED").substring(0, 4)); //년도만 추출
				ctsc001VO.setAcqsMm(p.getString("//DATE_ACQUIRED").substring(5, 7)); //월만추출
				ctsc001VO.setTrackNo(Integer.parseInt(p.getString("//WRS_PATH"))); //궤도번호
				ctsc001VO.setPotogrfModeNm(p.getString("//NADIR_OFFNADIR")); //촬영모드명
				ctsc001VO.setRollangCo(p.getString("//ROLL_ANGLE")); //ROLL_ANGLE
				ctsc001VO.setVidoSensorNm(p.getString("//SENSOR_ID")); //영상센서명
				ctsc001VO.setMapPrjctnCn(p.getString("//MAP_PROJECTION")); //지도투영내용
				ctsc001VO.setDatumCn(p.getString("//DATUM")); //데이텀 내용
				ctsc001VO.setSpherCn(p.getString("//ELLIPSOID")); //데이텀 내용
				ctsc001VO.setLacnul(Double.parseDouble(p.getString("//CORNER_UL_LAT_PRODUCT")));//위도내용좌상단
				ctsc001VO.setLocnul(Double.parseDouble(p.getString("//CORNER_UL_LON_PRODUCT"))); //경도내용좌상단
				ctsc001VO.setLacnur(Double.parseDouble(p.getString("//CORNER_UR_LAT_PRODUCT"))); //위도내용우상단
				ctsc001VO.setLocnur(Double.parseDouble(p.getString("//CORNER_UR_LON_PRODUCT"))); //경도내용우상단
				ctsc001VO.setLacnll(Double.parseDouble(p.getString("//CORNER_LL_LAT_PRODUCT"))); //위도내용좌하단
				ctsc001VO.setLocnll(Double.parseDouble(p.getString("//CORNER_LL_LAT_PRODUCT"))); //경도내용좌하단
				ctsc001VO.setLacnlr(Double.parseDouble(p.getString("//CORNER_LR_LAT_PRODUCT"))); //위도내용좌하단
				ctsc001VO.setLocnlr(Double.parseDouble(p.getString("//CORNER_LR_LON_PRODUCT"))); //경도내용좌하단
				ctsc001VO.setCloud_cover(Double.parseDouble(p.getString("//CLOUD_COVER"))); //운량
				ctsc001VO.setResolution(Double.parseDouble(p.getString("//GRID_CELL_SIZE_REFLECTIVE"))); //해상도
				ctsc001VO.setBandCo(11); //
				ctsc001VO.setBgngDt(p.getString("//DATE_ACQUIRED"));
				ctsc001VO.setEndDt(p.getString("//DATE_ACQUIRED"));
				//ctsc001VO.setVidoId(select.get(i).getVidoId());
				ctsc001VO.setVidoId(vidoId);
				ctsc001VO.setVidoNm(fefrt);
				flag = 1;  //xml 수집완료 txt는 수집하지 않는다.
//				Long insert = service.vidoinsertMeta(ctsc001VO);
//				service.upt(ctsc001VO);
//				System.out.println(p);

			}


		private Trigger getTrigger () {
			// cronSetting
			return new CronTrigger(cron);
		}

		@PostConstruct
		public void init () throws Exception {
			startScheduler();
		}

		@PreDestroy
		public void destroy () {
			stopScheduler();
		}
	}

