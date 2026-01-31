package com.whomade.planfAi.common.util.file.vo;

import lombok.Data;

@Data
public class CoFileVO {
    private String file_id;
    private String doc_id;
    private String file_rmk;
    private String file_nm;
    private String file_aslt_path;
    private String file_rltv_path;
    private long file_size;
    private String ss_user_id;
    private String content_type;
    private String file_ext_nm;
}
