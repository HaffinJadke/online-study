package com.online_study.media.model.dto;

import com.online_study.media.model.po.MediaFiles;
import lombok.Data;

/**
 * 文件上传相应结果类，继承自MediaFiles，前端如果要求新的结果参数，可以再添加
 */
@Data
public class UploadFileResultDto extends MediaFiles {
}
