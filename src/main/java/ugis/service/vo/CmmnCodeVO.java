package ugis.service.vo;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CmmnCodeVO implements Serializable {

    private static final long serialVersionUID = 6873417921256860868L;

    private String groupCode;
    private String groupNm;
    private String groupCodeCn;
    private String cmmnCode;
    private String codeNm;
    private String codeCn;
    private Integer sortOrdr;

    @Builder
    public CmmnCodeVO(String groupCode, String groupNm, String groupCodeCn, String cmmnCode, String codeNm,
            String codeCn, Integer sortOrdr) {
        this.groupCode = groupCode;
        this.groupNm = groupNm;
        this.groupCodeCn = groupCodeCn;
        this.cmmnCode = cmmnCode;
        this.codeNm = codeNm;
        this.codeCn = codeCn;
        this.sortOrdr = sortOrdr;
    }

}
