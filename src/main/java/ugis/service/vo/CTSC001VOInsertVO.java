package ugis.service.vo;

import lombok.Data;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Data
public class CTSC001VOInsertVO {
    Integer VIDO_ID;
    String BGNG_DT;
    String END_DT;
    String SATLIT_NO;
    String MAP_PRJCTN_CN;
    String SPHER_CN;
    String ACQS_YR;
    String ACQS_MM;
    String LA_CN_UL;
    String LO_CN_UL;
    String LA_CN_UR;
    String LO_CN_UR;
    String LA_CN_Cen;
    String LO_CN_Cen;
    String LA_CN_LL;
    String LO_CN_LL;
    String LA_CN_LR;
    String LO_CN_LR;
    int BAND_CO;
    String PRJCTN_CN;
    String DATUM_CN;
    String VIDO_SENSOR_NM;
    String SENSOR_MODEL_NM;
    String POTOGRF_BEGIN_DT;
    String POTOGRF_END_DT;
    int TRACK_NO;
    String POTOGRF_MODE_NM;
    int ROLLANG_CO;
    int PITCHANG_CO;
    int PRDCTN_NO;
    String RAW_SATLIT_VIDO_FILE_NM;
    String SATLIT_VIDO_FILE_NM;
    String SATLIT_VIDO_HDER_FILE_NM;
    String SATLIT_VIDO_SUMRY_FILE_NM;
    String STEREO_VIDO_FILE_NM;
    String REG_DT;
    String MDFCN_DT;
    String USE_RSTRCT_CN;
    String PRODUCT_URI;
    int Resolution;
    double Cloud_Cover;
    String Extract_Path;
    String Incomming_Path;

    String IINNER_FILE_COURS_NM;
    String potogrfVidoCd;
    String vidoNm;

    List<SatelliteImageMeta> satelliteImageMetas = new ArrayList<>();

    public void setImageFileName(String targetFolder, String imageFileName) {
        IINNER_FILE_COURS_NM = Paths.get(Extract_Path, targetFolder, imageFileName).toString();
    }
}
